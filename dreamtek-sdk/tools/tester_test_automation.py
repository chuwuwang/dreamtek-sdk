#!/usr/bin/env python3
"""Windows controller for the ADB-only Tester Test automation activity."""

from __future__ import annotations

import argparse
import datetime as dt
import html
import json
import os
import re
import socket
import subprocess
import sys
import time
import uuid
import zipfile
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Iterable
from xml.sax.saxutils import escape as xml_escape


APP_PACKAGE = "com.verifone.service_demo"
AUTOMATION_COMPONENT = f"{APP_PACKAGE}/view.AutomationActivity"
DEVICE_SERVICE_PACKAGE = "com.dreamtek.smartpos.deviceservice"
TTS_PACKAGE = "com.iflytek.speechcloud"
TTS_VERSION_NAME = "1.0.10024"
TTS_VERSION_CODE = "10024"
SOCKET_NAME = "tester_test_automation"
VALID_STATUSES = ("PASS", "FAIL", "ERROR", "NOT_RUN")


class AutomationError(RuntimeError):
    pass


class ProtocolDisconnected(AutomationError):
    pass


class ProtocolTimeout(AutomationError):
    pass


def redact_text(value: Any) -> str:
    """Remove common payment secrets before display or report persistence."""
    text = "" if value is None else str(value)
    keyed = re.compile(
        r"(?i)\b(PAN|TRACK\s*[123]?|PIN(?:BLOCK)?|KEY|KSN|APDU|FIELD\s*55)\b"
        r"\s*[:=]\s*([^\s,;|]+)"
    )
    text = keyed.sub(lambda match: f"{match.group(1)}=[REDACTED]", text)
    text = re.sub(r"(?<!\d)\d{12,19}(?!\d)", "[REDACTED_DIGITS]", text)
    return text


def sanitize(value: Any) -> Any:
    if isinstance(value, dict):
        return {str(key): sanitize(item) for key, item in value.items()}
    if isinstance(value, list):
        return [sanitize(item) for item in value]
    if isinstance(value, str):
        return redact_text(value)
    return value


class AdbClient:
    def __init__(self, adb: str, serial: str | None = None):
        self.adb = adb
        self.serial = serial or self._select_device()
        self._original_stay_on_while_plugged_in: str | None = None
        self._screen_awake_configured = False

    def _base(self) -> list[str]:
        return [self.adb, "-s", self.serial]

    def _run(self, *args: str, timeout: float = 30.0, check: bool = True) -> str:
        command = self._base() + list(args)
        completed = subprocess.run(
            command,
            capture_output=True,
            text=True,
            encoding="utf-8",
            errors="replace",
            timeout=timeout,
        )
        output = (completed.stdout + completed.stderr).strip()
        if check and completed.returncode != 0:
            raise AutomationError(
                f"ADB command failed ({completed.returncode}): {' '.join(command)}\n"
                f"{redact_text(output)}"
            )
        return output

    def _select_device(self) -> str:
        completed = subprocess.run(
            [self.adb, "devices", "-l"],
            capture_output=True,
            text=True,
            encoding="utf-8",
            errors="replace",
            timeout=15,
        )
        if completed.returncode != 0:
            raise AutomationError(f"Unable to list ADB devices: {completed.stderr.strip()}")
        devices: list[str] = []
        unauthorized: list[str] = []
        for line in completed.stdout.splitlines()[1:]:
            parts = line.split()
            if len(parts) < 2:
                continue
            if parts[1] == "device":
                devices.append(parts[0])
            elif parts[1] in {"unauthorized", "offline"}:
                unauthorized.append(f"{parts[0]} ({parts[1]})")
        if len(devices) != 1:
            detail = f"; unavailable: {', '.join(unauthorized)}" if unauthorized else ""
            raise AutomationError(
                f"Exactly one authorized ADB device is required; found {len(devices)}{detail}. "
                "Use --serial when multiple devices are attached."
            )
        return devices[0]

    def verify_state(self) -> None:
        state = self._run("get-state")
        if state.strip() != "device":
            raise AutomationError(f"ADB device {self.serial} is not ready: {state}")

    def package_info(self, package: str) -> dict[str, Any]:
        path_output = self._run("shell", "pm", "path", package, check=False)
        if not any(line.startswith("package:") for line in path_output.splitlines()):
            raise AutomationError(
                f"Required package is not installed: {package}. "
                "This controller never installs APKs or services."
            )
        dump = self._run("shell", "dumpsys", "package", package, timeout=45)
        version_name = _first_match(dump, r"\bversionName=([^\s]+)") or "UNKNOWN"
        version_code = _first_match(dump, r"\bversionCode=(\d+)") or "UNKNOWN"
        update_time = _first_match(dump, r"\blastUpdateTime=(.+)") or "UNKNOWN"
        return {
            "package": package,
            "versionName": version_name,
            "versionCode": version_code,
            "lastUpdateTime": update_time.strip(),
        }

    def start_activity(self) -> None:
        output = self._run(
            "shell", "am", "start", "--user", "0", "-W", "-n", AUTOMATION_COMPONENT,
            timeout=45,
        )
        if "Error:" in output or "SecurityException" in output:
            raise AutomationError(f"Unable to start protected automation activity: {output}")

    def stop_activity(self) -> None:
        self._run("shell", "am", "force-stop", APP_PACKAGE, check=False)

    def keep_screen_awake(self) -> int:
        original = self._run(
            "shell", "settings", "get", "global", "stay_on_while_plugged_in",
        ).strip()
        self._original_stay_on_while_plugged_in = (
            original if re.fullmatch(r"\d+", original) else None
        )
        self._screen_awake_configured = True
        self._run("shell", "input", "keyevent", "KEYCODE_WAKEUP", check=False)
        self._run("shell", "wm", "dismiss-keyguard", check=False)
        self._run("shell", "svc", "power", "stayon", "true")
        observed = self._run(
            "shell", "settings", "get", "global", "stay_on_while_plugged_in"
        ).strip()
        try:
            awake_mask = int(observed)
        except ValueError as error:
            raise AutomationError(
                "Unable to keep the POS screen awake while powered; "
                f"stay_on_while_plugged_in={observed or 'UNKNOWN'}"
            ) from error
        if awake_mask <= 0:
            raise AutomationError(
                "Unable to keep the POS screen awake while powered; "
                f"stay_on_while_plugged_in={awake_mask}"
            )
        return awake_mask

    def restore_screen_awake(self) -> None:
        if not getattr(self, "_screen_awake_configured", False):
            return
        original = self._original_stay_on_while_plugged_in
        if original is None:
            self._run(
                "shell", "settings", "delete", "global", "stay_on_while_plugged_in",
                check=False,
            )
        else:
            self._run(
                "shell", "settings", "put", "global", "stay_on_while_plugged_in",
                original, check=False,
            )
        self._screen_awake_configured = False

    def forward(self) -> int:
        output = self._run("forward", "tcp:0", f"localabstract:{SOCKET_NAME}")
        try:
            return int(output.splitlines()[-1].strip())
        except (ValueError, IndexError) as error:
            raise AutomationError(f"ADB did not return a forwarded TCP port: {output}") from error

    def remove_forward(self, port: int) -> None:
        local = f"tcp:{port}"
        for attempt in range(2):
            self._run("forward", "--remove", local, check=False)
            forwards = self._run("forward", "--list", check=False)
            still_present = any(
                len(parts := line.split()) >= 3
                and parts[0] == self.serial
                and parts[1] == local
                and parts[2] == f"localabstract:{SOCKET_NAME}"
                for line in forwards.splitlines()
            )
            if not still_present:
                return
            if attempt == 0:
                time.sleep(0.1)
        print(f"Warning: ADB forward cleanup did not remove {local}", file=sys.stderr)


def _first_match(text: str, pattern: str) -> str | None:
    match = re.search(pattern, text, re.MULTILINE)
    return match.group(1) if match else None


class ProtocolClient:
    def __init__(self, port: int):
        self.port = port
        self.socket: socket.socket | None = None
        self.receive_buffer = bytearray()

    def connect(self, timeout: float = 30.0) -> None:
        self.close()
        deadline = time.monotonic() + timeout
        last_error: OSError | None = None
        while time.monotonic() < deadline:
            try:
                sock = socket.create_connection(("127.0.0.1", self.port), timeout=3)
                sock.settimeout(5)
                self.socket = sock
                self.receive_buffer.clear()
                return
            except OSError as error:
                last_error = error
                time.sleep(0.5)
        raise AutomationError(f"Unable to connect to forwarded automation socket: {last_error}")

    def send(self, message: dict[str, Any]) -> None:
        if self.socket is None:
            raise ProtocolDisconnected("Automation socket is not connected")
        try:
            payload = (json.dumps(message, ensure_ascii=False, separators=(",", ":")) + "\n")
            self.socket.sendall(payload.encode("utf-8"))
        except socket.timeout as error:
            raise ProtocolTimeout(str(error)) from error
        except OSError as error:
            raise ProtocolDisconnected(str(error)) from error

    def receive(self) -> dict[str, Any]:
        if self.socket is None:
            raise ProtocolDisconnected("Automation socket is not connected")
        while b"\n" not in self.receive_buffer:
            try:
                chunk = self.socket.recv(65536)
            except socket.timeout as error:
                raise ProtocolTimeout(str(error)) from error
            except OSError as error:
                raise ProtocolDisconnected(str(error)) from error
            if not chunk:
                raise ProtocolDisconnected("Automation socket closed")
            self.receive_buffer.extend(chunk)
        try:
            raw_line, _, remainder = self.receive_buffer.partition(b"\n")
            self.receive_buffer = bytearray(remainder)
            line = raw_line.decode("utf-8", errors="strict")
            message = json.loads(line)
        except (UnicodeDecodeError, json.JSONDecodeError) as error:
            invalid = raw_line.decode("utf-8", errors="replace")
            raise AutomationError(f"Invalid protocol JSON: {redact_text(invalid)}") from error
        if not isinstance(message, dict) or "type" not in message:
            raise AutomationError(f"Invalid protocol message: {sanitize(message)}")
        return message

    def close(self) -> None:
        if self.socket is not None:
            try:
                self.socket.close()
            except OSError:
                pass
        self.socket = None
        self.receive_buffer.clear()


@dataclass
class RunCollector:
    run_id: str
    selection: str
    environment: dict[str, Any]
    selected_cases: list[dict[str, Any]]

    def __post_init__(self) -> None:
        self.started_at = dt.datetime.now(dt.timezone.utc)
        self.records: dict[str, dict[str, Any]] = {}
        self.order: list[str] = []
        self.errors: list[dict[str, Any]] = []
        self.protocol_summary: dict[str, Any] | None = None
        self.run_status = "IN_PROGRESS"
        self.duration_ms = 0

    def add_error(self, message: dict[str, Any]) -> None:
        self.errors.append(sanitize(message))

    def case_started(self, message: dict[str, Any]) -> None:
        metadata = sanitize(message.get("case") or {})
        case_id = str(metadata.get("id") or message.get("caseId") or "UNKNOWN")
        if case_id not in self.records:
            self.order.append(case_id)
            self.records[case_id] = {
                **metadata,
                "id": case_id,
                "status": "NOT_RUN",
                "actualResult": "Case did not finish",
                "durationMs": 0,
            }

    def case_finished(self, message: dict[str, Any]) -> None:
        clean = sanitize(message)
        case_id = str(clean.get("caseId") or "UNKNOWN")
        if case_id not in self.records:
            metadata = next(
                (case for case in self.selected_cases if case.get("id") == case_id),
                {"id": case_id, "module": clean.get("module"), "name": clean.get("name")},
            )
            self.order.append(case_id)
            self.records[case_id] = dict(metadata)
        status = str(clean.get("status", "ERROR"))
        if status not in VALID_STATUSES:
            status = "ERROR"
        self.records[case_id].update(
            status=status,
            actualResult=clean.get("actualResult", ""),
            durationMs=max(0, int(clean.get("durationMs") or 0)),
        )

    def finish(self, message: dict[str, Any]) -> None:
        clean = sanitize(message)
        self.run_status = str(clean.get("status", "COMPLETED"))
        self.duration_ms = max(0, int(clean.get("durationMs") or 0))
        summary = clean.get("summary")
        self.protocol_summary = summary if isinstance(summary, dict) else None
        self._fill_missing("NOT_RUN", "No terminal case result received")

    def disconnect_failure(self, reason: str) -> None:
        self.run_status = "ERROR"
        self.errors.append({"type": "ERROR", "code": "USB_DISCONNECTED", "detail": redact_text(reason)})
        unfinished = [case_id for case_id in self.order if self.records[case_id]["status"] == "NOT_RUN"]
        if unfinished:
            current = unfinished[-1]
            self.records[current].update(status="ERROR", actualResult="USB/ADB communication was lost")
        self._fill_missing("NOT_RUN", "Run stopped after USB/ADB disconnect")
        self.duration_ms = int((dt.datetime.now(dt.timezone.utc) - self.started_at).total_seconds() * 1000)

    def _fill_missing(self, status: str, reason: str) -> None:
        for metadata in self.selected_cases:
            case_id = str(metadata.get("id"))
            if case_id not in self.records:
                self.order.append(case_id)
                self.records[case_id] = {
                    **sanitize(metadata),
                    "status": status,
                    "actualResult": reason,
                    "durationMs": 0,
                }

    def payload(self) -> dict[str, Any]:
        cases = [self.records[case_id] for case_id in self.order]
        counts = {status: 0 for status in VALID_STATUSES}
        for case in cases:
            counts[case["status"]] += 1
        counts["TOTAL"] = len(cases)
        if self.protocol_summary is not None:
            normalized = {key: int(self.protocol_summary.get(key, 0)) for key in (*VALID_STATUSES, "TOTAL")}
            if normalized != counts:
                self.errors.append(
                    {
                        "type": "ERROR",
                        "code": "SUMMARY_MISMATCH",
                        "detail": f"Device summary {normalized} differs from received cases {counts}",
                    }
                )
        return sanitize(
            {
                "schemaVersion": 1,
                "runId": self.run_id,
                "selection": self.selection,
                "status": self.run_status,
                "startedAt": self.started_at.isoformat(),
                "finishedAt": dt.datetime.now(dt.timezone.utc).isoformat(),
                "durationMs": self.duration_ms,
                "environment": self.environment,
                "summary": counts,
                "cases": cases,
                "errors": self.errors,
            }
        )


def write_reports(payload: dict[str, Any], output_dir: Path) -> dict[str, Path]:
    output_dir.mkdir(parents=True, exist_ok=True)
    stamp = dt.datetime.now().strftime("%Y%m%d-%H%M%S")
    stem = f"tester-test-{stamp}-{payload['runId'][:8]}"
    paths = {
        "json": output_dir / f"{stem}.json",
        "html": output_dir / f"{stem}.html",
        "xlsx": output_dir / f"{stem}.xlsx",
    }
    paths["json"].write_text(
        json.dumps(sanitize(payload), ensure_ascii=False, indent=2), encoding="utf-8"
    )
    paths["html"].write_text(_html_report(payload), encoding="utf-8")
    _write_xlsx(payload, paths["xlsx"])
    return paths


def _html_report(payload: dict[str, Any]) -> str:
    summary = payload["summary"]
    rows = []
    for case in payload["cases"]:
        cells = [
            case.get("id", ""),
            case.get("moduleName") or case.get("module", ""),
            case.get("name", ""),
            case.get("precondition", ""),
            case.get("expectedResult", ""),
            case.get("risk", ""),
            case.get("status", ""),
            case.get("durationMs", 0),
            case.get("actualResult", ""),
        ]
        rows.append(
            "<tr>" + "".join(f"<td>{html.escape(str(redact_text(cell)))}</td>" for cell in cells) + "</tr>"
        )
    environment = "<br>".join(
        f"{html.escape(str(key))}: {html.escape(str(redact_text(value)))}"
        for key, value in payload["environment"].items()
    )
    return f"""<!doctype html>
<html lang="en"><head><meta charset="utf-8">
<meta name="case-count" content="{summary['TOTAL']}">
<title>Tester Test Automation Report</title>
<style>
body{{font-family:Arial,sans-serif;margin:24px;color:#222}}
table{{border-collapse:collapse;width:100%;font-size:13px}}th,td{{border:1px solid #bbb;padding:7px;text-align:left;vertical-align:top}}
th{{background:#eee}}.summary span{{display:inline-block;margin:4px 16px 12px 0}}
</style></head><body>
<h1>Tester Test Automation Report</h1>
<p>Run ID: {html.escape(payload['runId'])}<br>Status: {html.escape(payload['status'])}<br>{environment}</p>
<div class="summary" data-total="{summary['TOTAL']}">
<span>PASS: {summary['PASS']}</span><span>FAIL: {summary['FAIL']}</span>
<span>ERROR: {summary['ERROR']}</span><span>NOT_RUN: {summary['NOT_RUN']}</span>
</div>
<table><thead><tr><th>Case ID</th><th>Module</th><th>Name</th><th>Precondition</th><th>Expected Result</th><th>Risk</th><th>Status</th><th>Duration (ms)</th><th>Actual Result</th></tr></thead>
<tbody>{''.join(rows)}</tbody></table></body></html>"""


def _write_xlsx(payload: dict[str, Any], path: Path) -> None:
    summary = payload["summary"]
    rows: list[list[Any]] = [
        ["Run ID", payload["runId"]],
        ["Status", payload["status"]],
        ["TOTAL", summary["TOTAL"], "PASS", summary["PASS"], "FAIL", summary["FAIL"],
         "ERROR", summary["ERROR"], "NOT_RUN", summary["NOT_RUN"]],
        [],
        ["Case ID", "Module", "Name", "API", "Precondition", "Expected Result", "Risk", "Status", "Duration (ms)", "Actual Result"],
    ]
    for case in payload["cases"]:
        rows.append(
            [
                case.get("id", ""),
                case.get("moduleName") or case.get("module", ""),
                case.get("name", ""),
                case.get("api", ""),
                case.get("precondition", ""),
                case.get("expectedResult", ""),
                case.get("risk", ""),
                case.get("status", ""),
                int(case.get("durationMs") or 0),
                case.get("actualResult", ""),
            ]
        )
    worksheet = _worksheet_xml(rows)
    entries = {
        "[Content_Types].xml": """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
<Default Extension="xml" ContentType="application/xml"/>
<Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
<Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
<Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
</Types>""",
        "_rels/.rels": """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>""",
        "xl/workbook.xml": """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
<sheets><sheet name="Results" sheetId="1" r:id="rId1"/></sheets></workbook>""",
        "xl/_rels/workbook.xml.rels": """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
</Relationships>""",
        "xl/styles.xml": """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
<fonts count="1"><font><sz val="11"/><name val="Calibri"/></font></fonts>
<fills count="2"><fill><patternFill patternType="none"/></fill><fill><patternFill patternType="gray125"/></fill></fills>
<borders count="1"><border><left/><right/><top/><bottom/><diagonal/></border></borders>
<cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>
<cellXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/></cellXfs>
<cellStyles count="1"><cellStyle name="Normal" xfId="0" builtinId="0"/></cellStyles>
<dxfs count="0"/><tableStyles count="0" defaultTableStyle="TableStyleMedium2" defaultPivotStyle="PivotStyleLight16"/>
</styleSheet>""",
        "xl/worksheets/sheet1.xml": worksheet,
    }
    with zipfile.ZipFile(path, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for name, content in entries.items():
            archive.writestr(name, content.encode("utf-8"))


def _worksheet_xml(rows: Iterable[Iterable[Any]]) -> str:
    xml_rows = []
    for row_index, row in enumerate(rows, start=1):
        cells = []
        for column_index, value in enumerate(row, start=1):
            reference = f"{_column_name(column_index)}{row_index}"
            if isinstance(value, (int, float)) and not isinstance(value, bool):
                cells.append(f'<c r="{reference}"><v>{value}</v></c>')
            else:
                cleaned = xml_escape(redact_text(value))
                cells.append(f'<c r="{reference}" t="inlineStr"><is><t>{cleaned}</t></is></c>')
        xml_rows.append(f'<row r="{row_index}">{"".join(cells)}</row>')
    return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
<sheetData>""" + "".join(xml_rows) + "</sheetData></worksheet>"


def _column_name(index: int) -> str:
    name = ""
    while index:
        index, remainder = divmod(index - 1, 26)
        name = chr(65 + remainder) + name
    return name


class Controller:
    def __init__(self, adb: AdbClient, protocol: ProtocolClient, output_dir: Path):
        self.adb = adb
        self.protocol = protocol
        self.output_dir = output_dir
        self.hello: dict[str, Any] = {}
        self.ready: dict[str, Any] = {}
        self.startup_errors: list[dict[str, Any]] = []
        self.cached_prompt_responses: dict[str, dict[str, Any]] = {}

    def wait_until_ready(self) -> None:
        deadline = time.monotonic() + 90
        recovered = False
        while time.monotonic() < deadline:
            try:
                message = self.protocol.receive()
            except ProtocolTimeout:
                continue
            except ProtocolDisconnected:
                if recovered:
                    raise
                recovered = True
                self.protocol.connect(timeout=20)
                continue
            kind = message["type"]
            if kind == "HELLO":
                self.hello = sanitize(message)
                if int(message.get("protocolVersion", -1)) != 1:
                    raise AutomationError(f"Unsupported protocol version: {message.get('protocolVersion')}")
            elif kind == "READY":
                self.ready = sanitize(message)
                return
            elif kind == "ERROR":
                self.startup_errors.append(sanitize(message))
                print(f"[Device error] {message.get('code')}: {redact_text(message.get('detail'))}")
                if not message.get("recoverable", False):
                    raise AutomationError(f"Device preflight failed: {message.get('code')}")
        raise AutomationError("Timed out waiting for HELLO/READY from the POS")

    @property
    def modules(self) -> list[dict[str, Any]]:
        modules = self.ready.get("modules")
        return modules if isinstance(modules, list) else []

    def run(self, selection: str) -> dict[str, Path]:
        selected = self._selected_cases(selection)
        run_id = str(uuid.uuid4())
        environment = {
            "adbSerial": self.adb.serial,
            "appId": self.hello.get("appId", APP_PACKAGE),
            "appVersion": self.hello.get("appVersion", "UNKNOWN"),
            "deviceServiceVersion": self.hello.get("deviceService", "UNKNOWN"),
            "systemServiceState": self.hello.get("systemService", "UNKNOWN"),
            "systemServiceSource": self.hello.get("systemServiceSource", "UNKNOWN"),
            "systemServiceHostVersion": self.hello.get("systemServiceHostVersion", "UNKNOWN"),
            "ttsEngine": self.hello.get("ttsEngine", TTS_PACKAGE),
            "ttsEngineVersion": self.hello.get("ttsEngineVersion", "UNKNOWN"),
        }
        collector = RunCollector(run_id, selection, environment, selected)
        module_selection = next(
            (module for module in self.modules if module.get("id") == selection), None
        )
        if selection == "ALL":
            command = {"type": "RUN_ALL", "runId": run_id}
        elif module_selection is not None:
            command = {"type": "RUN_MODULE", "runId": run_id}
            command["module"] = selection
        else:
            command = {"type": "RUN_CASE", "runId": run_id, "caseId": selection}
        self.protocol.send(command)
        recovered = False
        while True:
            try:
                message = self.protocol.receive()
            except ProtocolTimeout:
                continue
            except ProtocolDisconnected as error:
                if recovered:
                    collector.disconnect_failure(str(error))
                    break
                print("Communication interrupted; performing the single reconnect attempt…")
                recovered = True
                try:
                    self.protocol.connect(timeout=20)
                    continue
                except AutomationError as reconnect_error:
                    collector.disconnect_failure(str(reconnect_error))
                    break

            kind = message["type"]
            if kind in {"HELLO", "READY"}:
                continue
            if kind == "CASE_STARTED":
                collector.case_started(message)
                case = message.get("case") or {}
                print(f"\nStarted {case.get('id')}: {case.get('name')}")
            elif kind == "OPERATOR_PROMPT":
                self._handle_prompt(message)
            elif kind == "CASE_FINISHED":
                collector.case_finished(message)
                print(
                    f"Result {message.get('caseId')}: {message.get('status')} "
                    f"({message.get('durationMs', 0)} ms)"
                )
            elif kind == "ERROR":
                collector.add_error(message)
                print(f"[Device error] {message.get('code')}: {redact_text(message.get('detail'))}")
            elif kind == "RUN_FINISHED":
                collector.finish(message)
                break
        payload = collector.payload()
        paths = write_reports(payload, self.output_dir)
        _print_summary(payload, paths)
        return paths

    def _selected_cases(self, selection: str) -> list[dict[str, Any]]:
        if selection == "ALL":
            return [case for module in self.modules for case in module.get("cases", [])]
        module = next((item for item in self.modules if item.get("id") == selection), None)
        if module is not None:
            return list(module.get("cases", []))
        selected = [
            case
            for item in self.modules
            for case in item.get("cases", [])
            if case.get("id") == selection
        ]
        if not selected:
            raise AutomationError(f"Unknown module or case selection: {selection}")
        return selected

    def _handle_prompt(self, message: dict[str, Any]) -> None:
        prompt_id = str(message.get("promptId"))
        cached = self.cached_prompt_responses.get(prompt_id)
        if cached is not None:
            self.protocol.send(cached)
            return
        kind = str(message.get("kind", ""))
        text = redact_text(message.get("text", ""))
        print(f"\n[Operator confirmation/{kind}] {text}")
        if kind == "RESULT":
            answer = input("Enter p=PASS, f=FAIL, a=ABORT: ").strip().lower()
            verdict = {"p": "PASS", "f": "FAIL", "a": "ABORT"}.get(answer, "ERROR")
        else:
            answer = input("Enter y=COMPLETED, f=FAIL, a=ABORT: ").strip().lower()
            verdict = {"y": "CONFIRMED", "f": "FAIL", "a": "ABORT"}.get(answer, "ERROR")
        note = ""
        if verdict in {"FAIL", "ERROR"}:
            note = redact_text(input("Note (do not enter PAN, track, PIN, key, or APDU data): ").strip())
        response = {
            "type": "OPERATOR_PROMPT",
            "action": "response",
            "promptId": prompt_id,
            "verdict": verdict,
            "note": note,
        }
        self.cached_prompt_responses[prompt_id] = response
        self.protocol.send(response)

    def confirm_selection(self, selection: str) -> bool:
        if selection == "ALL":
            if not self.ready.get("allMapped", False):
                print("Run All is unavailable because at least one visible Tester Test module is not mapped.")
                return False
            destructive = [module for module in self.modules if module.get("destructive")]
            print("\nRun All includes these destructive modules:")
            for module in destructive:
                print(f"- {module.get('name')} ({len(module.get('cases', []))} cases)")
            phrase = f"RUN ALL {self.adb.serial}"
            return input(f"Enter the confirmation phrase [{phrase}]: ").strip() == phrase
        module = next((item for item in self.modules if item.get("id") == selection), None)
        if module is None:
            module = next(
                (item for item in self.modules
                 if any(case.get("id") == selection for case in item.get("cases", []))),
                None,
            )
        if module and module.get("destructive"):
            phrase = f"RUN {selection} {self.adb.serial}"
            print(f"Module {module.get('name')} is marked destructive.")
            return input(f"Enter the confirmation phrase [{phrase}]: ").strip() == phrase
        return True

    def menu(self) -> None:
        while True:
            print("\nTester Test Automation Menu")
            for index, module in enumerate(self.modules, start=1):
                print(f"{index:2d}. {module.get('name')} [{module.get('id')}] "
                      f"({len(module.get('cases', []))} cases)")
            print(" A. Run All")
            print(" Q. Exit")
            choice = input("Select an option: ").strip()
            if choice.lower() == "q":
                self.exit()
                return
            if choice.lower() == "a":
                selection = "ALL"
            else:
                try:
                    selection = str(self.modules[int(choice) - 1]["id"])
                except (ValueError, IndexError, KeyError):
                    print("Invalid selection.")
                    continue
            if self.confirm_selection(selection):
                self.run(selection)
            else:
                print("The confirmation phrase did not match; the test was not started.")

    def exit(self) -> None:
        self.protocol.send({"type": "EXIT"})
        deadline = time.monotonic() + 10
        while time.monotonic() < deadline:
            try:
                message = self.protocol.receive()
            except ProtocolTimeout:
                continue
            if message.get("type") == "EXIT" and message.get("accepted") is True:
                return
        raise AutomationError("Timed out waiting for the device EXIT acknowledgement")


def _print_summary(payload: dict[str, Any], paths: dict[str, Path]) -> None:
    summary = payload["summary"]
    print(
        "\nRun finished: "
        f"PASS={summary['PASS']} FAIL={summary['FAIL']} ERROR={summary['ERROR']} "
        f"NOT_RUN={summary['NOT_RUN']} TOTAL={summary['TOTAL']}"
    )
    print("Reports:")
    for kind, path in paths.items():
        print(f"- {kind.upper()}: {path.resolve()}")


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--adb", default=os.environ.get("ADB", "adb"), help="adb executable")
    parser.add_argument("--serial", help="ADB device serial")
    parser.add_argument("--output-dir", type=Path, default=Path("reports") / "tester-test")
    group = parser.add_mutually_exclusive_group()
    group.add_argument("--module", help="run one registry module and exit")
    group.add_argument("--case", dest="case_id", help="run one registered case and exit")
    group.add_argument("--all", action="store_true", help="run all mapped modules and exit")
    return parser.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(argv or sys.argv[1:])
    adb = AdbClient(args.adb, args.serial)
    port: int | None = None
    protocol: ProtocolClient | None = None
    try:
        adb.verify_state()
        tts_details = adb.package_info(TTS_PACKAGE)
        if (
            tts_details["versionName"] != TTS_VERSION_NAME
            or tts_details["versionCode"] != TTS_VERSION_CODE
        ):
            raise AutomationError(
                f"Required iFlytek TTS {TTS_PACKAGE} {TTS_VERSION_NAME} "
                f"({TTS_VERSION_CODE}); installed {tts_details['versionName']} "
                f"({tts_details['versionCode']})."
            )
        package_details = [
            adb.package_info(APP_PACKAGE),
            adb.package_info(DEVICE_SERVICE_PACKAGE),
            tts_details,
        ]
        print("Preflight passed:")
        for package in package_details:
            print(f"- {package['package']}: {package['versionName']} ({package['versionCode']})")
        port = adb.forward()
        adb.start_activity()
        awake_mask = adb.keep_screen_awake()
        print("The POS screen will stay awake during the test session "
              f"(stay_on_while_plugged_in={awake_mask}); the original setting is restored on exit.")
        protocol = ProtocolClient(port)
        protocol.connect()
        controller = Controller(adb, protocol, args.output_dir)
        controller.wait_until_ready()
        if args.all:
            if controller.confirm_selection("ALL"):
                controller.run("ALL")
                controller.exit()
                return 0
            return 2
        if args.module:
            if controller.confirm_selection(args.module):
                controller.run(args.module)
                controller.exit()
                return 0
            return 2
        if args.case_id:
            if controller.confirm_selection(args.case_id):
                controller.run(args.case_id)
                controller.exit()
                return 0
            return 2
        controller.menu()
        return 0
    except KeyboardInterrupt:
        if protocol is not None:
            try:
                protocol.send({"type": "ABORT", "reason": "PC operator interrupted"})
            except AutomationError:
                pass
        print("\nAbort requested.", file=sys.stderr)
        return 130
    except (AutomationError, subprocess.TimeoutExpired) as error:
        print(f"Error: {redact_text(error)}", file=sys.stderr)
        return 1
    finally:
        if protocol is not None:
            protocol.close()
        if port is not None:
            adb.remove_forward(port)
        adb.stop_activity()
        adb.restore_screen_awake()


if __name__ == "__main__":
    raise SystemExit(main())
