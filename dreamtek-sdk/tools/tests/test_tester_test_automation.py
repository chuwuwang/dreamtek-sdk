import importlib.util
import json
import re
import socket
import sys
import tempfile
import unittest
import xml.etree.ElementTree as ET
import zipfile
from contextlib import redirect_stdout
from io import StringIO
from pathlib import Path
from unittest.mock import patch


SCRIPT = Path(__file__).resolve().parents[1] / "tester_test_automation.py"
SPEC = importlib.util.spec_from_file_location("tester_test_automation", SCRIPT)
automation = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
sys.modules[SPEC.name] = automation
SPEC.loader.exec_module(automation)


class RedactionTest(unittest.TestCase):
    def test_redacts_payment_secrets(self):
        source = (
            "PAN=6222021234567890 TRACK2:6222021234567890D2712 "
            "PIN=1234 KEY=A1B2 APDU=00A4040008A000000333010101"
        )
        redacted = automation.redact_text(source)
        self.assertNotIn("6222021234567890", redacted)
        self.assertNotIn("1234", redacted)
        self.assertNotIn("00A4040008", redacted)
        self.assertGreaterEqual(redacted.count("[REDACTED]"), 5)


class ReportConsistencyTest(unittest.TestCase):
    def sample_payload(self):
        return {
            "schemaVersion": 1,
            "runId": "run-12345678",
            "selection": "beer",
            "status": "COMPLETED",
            "startedAt": "2026-08-27T00:00:00+00:00",
            "finishedAt": "2026-08-27T00:00:03+00:00",
            "durationMs": 3000,
            "environment": {"adbSerial": "DEVICE01", "appVersion": "3.0.0"},
            "summary": {"PASS": 1, "FAIL": 1, "ERROR": 0, "NOT_RUN": 1, "TOTAL": 3},
            "cases": [
                {"id": "beer:B01001", "module": "beer", "name": "B01001", "api": "beep",
                 "precondition": "ready", "risk": "LOW", "status": "PASS", "durationMs": 1000,
                 "expectedResult": "heard one beep",
                 "actualResult": "heard beep"},
                {"id": "beer:B01002", "module": "beer", "name": "B01002", "api": "beep",
                 "precondition": "ready", "risk": "LOW", "status": "FAIL", "durationMs": 2000,
                 "actualResult": "PAN=6222021234567890 was mistakenly entered"},
                {"id": "pboc:UNAVAILABLE", "module": "pboc", "name": "UNAVAILABLE", "api": "UNAVAILABLE",
                 "precondition": "none", "risk": "HIGH", "status": "NOT_RUN", "durationMs": 0,
                 "actualResult": "disabled"},
            ],
            "errors": [],
        }

    def test_json_html_xlsx_have_same_counts_statuses_and_durations(self):
        with tempfile.TemporaryDirectory() as directory:
            paths = automation.write_reports(self.sample_payload(), Path(directory))
            json_payload = json.loads(paths["json"].read_text(encoding="utf-8"))
            html_text = paths["html"].read_text(encoding="utf-8")

            self.assertEqual(3, json_payload["summary"]["TOTAL"])
            self.assertIn('meta name="case-count" content="3"', html_text)
            self.assertIn('data-total="3"', html_text)
            self.assertNotIn("6222021234567890", json.dumps(json_payload))
            self.assertNotIn("6222021234567890", html_text)

            with zipfile.ZipFile(paths["xlsx"]) as workbook:
                worksheet = ET.fromstring(workbook.read("xl/worksheets/sheet1.xml"))
            namespace = {"x": "http://schemas.openxmlformats.org/spreadsheetml/2006/main"}
            rows = worksheet.findall("x:sheetData/x:row", namespace)
            self.assertEqual(8, len(rows))
            all_text = " ".join("".join(row.itertext()) for row in rows)
            self.assertIn("TOTAL3", all_text.replace(" ", ""))
            self.assertIn("PASS", all_text)
            self.assertIn("FAIL", all_text)
            self.assertIn("NOT_RUN", all_text)
            self.assertIn("heard one beep", all_text)
            self.assertIn("1000", all_text)
            self.assertIn("2000", all_text)
            self.assertNotIn("6222021234567890", all_text)


class EnglishUiTest(unittest.TestCase):
    def test_source_comments_and_ui_text_have_no_han_characters(self):
        root = SCRIPT.parents[1]
        candidates = []
        for source_root, suffixes in (
            (root / "app", {".java", ".xml", ".gradle"}),
            (root / "tools", {".py"}),
            (root / "pycharm", {".py"}),
        ):
            candidates.extend(
                path for path in source_root.rglob("*")
                if path.is_file()
                and path.suffix in suffixes
                and "build" not in path.parts
                and "__pycache__" not in path.parts
            )
        candidates.append(root / "json_pinpad_single.txt")
        han = re.compile(r"[\u3400-\u4dbf\u4e00-\u9fff]")
        violations = []
        for path in candidates:
            text = path.read_text(encoding="utf-8-sig")
            if han.search(text):
                violations.append(str(path.relative_to(root)))
        self.assertEqual([], violations)

    def test_android_and_tts_are_forced_to_english(self):
        root = SCRIPT.parents[1]
        locale_helper = (root / "app/src/main/java/Utils/LocaleHelper.java").read_text(
            encoding="utf-8"
        )
        tts_queue = (root / "app/src/main/java/automation/TtsSerialQueue.java").read_text(
            encoding="utf-8"
        )
        self.assertIn("return Locale.ENGLISH", locale_helper)
        self.assertNotIn("Locale.SIMPLIFIED_CHINESE", locale_helper)
        self.assertIn("engine.setLanguage(Locale.US)", tts_queue)
        self.assertFalse((root / "app/src/main/res/values-zh/strings.xml").exists())


class CollectorTest(unittest.TestCase):
    def test_collector_uses_strict_statuses_and_marks_missing_cases(self):
        cases = [
            {"id": "led:L1", "module": "led", "name": "L1"},
            {"id": "led:L2", "module": "led", "name": "L2"},
        ]
        collector = automation.RunCollector("run", "led", {}, cases)
        collector.case_started({"case": cases[0]})
        collector.case_finished(
            {"caseId": "led:L1", "module": "led", "name": "L1", "status": "PASS", "durationMs": 17}
        )
        collector.finish(
            {"status": "COMPLETED", "durationMs": 20,
             "summary": {"PASS": 1, "FAIL": 0, "ERROR": 0, "NOT_RUN": 1, "TOTAL": 2}}
        )
        payload = collector.payload()
        self.assertEqual(["PASS", "NOT_RUN"], [case["status"] for case in payload["cases"]])
        self.assertEqual(17, payload["cases"][0]["durationMs"])
        self.assertEqual(2, payload["summary"]["TOTAL"])


class ProtocolClientTest(unittest.TestCase):
    def test_receive_continues_after_idle_timeout(self):
        client_socket, server_socket = socket.socketpair()
        client = automation.ProtocolClient(0)
        client.socket = client_socket
        client_socket.settimeout(0.01)
        try:
            with self.assertRaises(automation.ProtocolTimeout):
                client.receive()
            server_socket.sendall(b'{"type":"READY"}\n')
            self.assertEqual("READY", client.receive()["type"])
        finally:
            client.close()
            server_socket.close()


class AdbForwardCleanupTest(unittest.TestCase):
    def test_forward_cleanup_is_verified_and_retried_once(self):
        client = object.__new__(automation.AdbClient)
        client.adb = "adb"
        client.serial = "DEVICE01"
        calls = []
        listings = iter([
            "DEVICE01 tcp:53535 localabstract:tester_test_automation",
            "",
        ])

        def fake_run(*args, **kwargs):
            calls.append(args)
            if args == ("forward", "--list"):
                return next(listings)
            return ""

        client._run = fake_run
        with patch("time.sleep"):
            client.remove_forward(53535)

        removes = [call for call in calls if call[:2] == ("forward", "--remove")]
        self.assertEqual(2, len(removes))

    def test_stop_activity_force_stops_the_test_app(self):
        client = object.__new__(automation.AdbClient)
        calls = []
        client._run = lambda *args, **kwargs: calls.append(args) or ""

        client.stop_activity()

        self.assertEqual(
            [("shell", "am", "force-stop", automation.APP_PACKAGE)], calls
        )


class ScreenAwakeTest(unittest.TestCase):
    def test_screen_is_woken_kept_awake_and_original_setting_is_restored(self):
        client = object.__new__(automation.AdbClient)
        client.adb = "adb"
        client.serial = "DEVICE01"
        client._original_stay_on_while_plugged_in = None
        client._screen_awake_configured = False
        calls = []
        settings_values = iter(["3", "15"])

        def fake_run(*args, **kwargs):
            calls.append(args)
            if args == ("shell", "settings", "get", "global", "stay_on_while_plugged_in"):
                return next(settings_values)
            return ""

        client._run = fake_run
        self.assertEqual(15, client.keep_screen_awake())
        self.assertIn(("shell", "input", "keyevent", "KEYCODE_WAKEUP"), calls)
        self.assertIn(("shell", "wm", "dismiss-keyguard"), calls)
        self.assertIn(("shell", "svc", "power", "stayon", "true"), calls)

        client.restore_screen_awake()
        self.assertIn(
            ("shell", "settings", "put", "global", "stay_on_while_plugged_in", "3"),
            calls,
        )
        self.assertFalse(client._screen_awake_configured)


class TtsEngineSelectionTest(unittest.TestCase):
    def test_iflytek_engine_and_version_are_pinned_without_pc_install(self):
        root = SCRIPT.parents[1]
        tts_queue = (root / "app/src/main/java/automation/TtsSerialQueue.java").read_text(
            encoding="utf-8"
        )
        manifest = (root / "app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")
        controller = SCRIPT.read_text(encoding="utf-8")

        self.assertIn('ENGINE_PACKAGE = "com.iflytek.speechcloud"', tts_queue)
        self.assertIn('EXPECTED_ENGINE_VERSION = "1.0.10024"', tts_queue)
        self.assertIn("SPEECH_RATE = 0.9f", tts_queue)
        self.assertIn("}, ENGINE_PACKAGE);", tts_queue)
        self.assertIn('<package android:name="com.iflytek.speechcloud"', manifest)
        self.assertIn('TTS_VERSION_CODE = "10024"', controller)
        self.assertNotRegex(controller, r"adb[^\n]*install")

    def test_direct_runs_send_exit_after_the_report_is_written(self):
        controller = SCRIPT.read_text(encoding="utf-8")
        direct_run = re.search(
            r"if args\.module:(.*?)controller\.menu\(\)", controller, re.DOTALL
        )
        self.assertIsNotNone(direct_run)
        assert direct_run is not None
        self.assertRegex(
            direct_run.group(1),
            r"controller\.run\(args\.module\)\s+controller\.exit\(\)",
        )

    def test_pc_sets_screen_awake_after_starting_the_activity(self):
        controller = SCRIPT.read_text(encoding="utf-8")
        self.assertRegex(
            controller,
            r"adb\.start_activity\(\)\s+awake_mask = adb\.keep_screen_awake\(\)",
        )
        self.assertIn("adb.stop_activity()", controller)
        self.assertIn("adb.restore_screen_awake()", controller)


class RegistryCoverageTest(unittest.TestCase):
    def test_fixed_registry_maps_all_visible_tester_test_buttons(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(encoding="utf-8")
        activity = (root / "app/src/main/java/view/TestActivity.java").read_text(encoding="utf-8")
        visible = set(re.findall(r"enterThirdActivity\(Constants\.(\w+)\)", activity))
        mapped = set(re.findall(r"(?:descriptor|automaticDescriptor)\(Constants\.(\w+)", registry))
        canonical = set(
            re.findall(
                r"getAutomationModuleIds\(\).*?return Arrays\.asList\((.*?)\);",
                activity,
                re.DOTALL,
            )[0].replace("Constants.", "").replace("\n", "").replace(" ", "").split(",")
        )
        self.assertEqual(24, len(visible))
        self.assertEqual(visible, mapped)
        self.assertEqual(visible, canonical)
        self.assertIn("TestActivity.getAutomationModuleIds()", registry)

    def test_get_service_is_automatic_and_uses_return_contracts(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        service = (root / "app/src/main/java/moudles/ServiceMoudle.java").read_text(
            encoding="utf-8"
        )
        self.assertIn("automaticDescriptor(Constants.serviceBt", registry)
        self.assertIn("if (!automationCase.operatorRequired)", activity)
        self.assertIn("runAutomaticServiceCase", activity)
        self.assertIn("returned a live Binder", service)
        self.assertIn("the API contract defines only camera IDs 0 and 1", service)
        self.assertIn("rejected the unsupported device type with null", service)
        self.assertIn("if (Constants.serviceBt.equals(descriptor.id))", registry)
        self.assertIn("api = serviceApi(caseName)", registry)

    def test_device_socket_bind_has_one_recovery_attempt(self):
        root = SCRIPT.parents[1]
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        recovery = re.search(
            r"openServerSocketWithRecovery\(\).*?for \(int attempt = 0; attempt < 2",
            activity,
            re.DOTALL,
        )
        self.assertIsNotNone(recovery)
        self.assertIn("SystemClock.sleep(500L)", activity)

    def test_registry_is_built_on_the_ui_looper_for_legacy_handlers(self):
        root = SCRIPT.parents[1]
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        registry_block = re.search(
            r"private void maybeBuildRegistry\(\).*?private void startSocketServer\(\)",
            activity,
            re.DOTALL,
        )
        self.assertIsNotNone(registry_block)
        assert registry_block is not None
        self.assertIn("runOnUiThread", registry_block.group(0))
        self.assertNotIn('new Thread(() ->', registry_block.group(0))

    def test_module_two_cases_have_code_reviewed_expectations(self):
        root = SCRIPT.parents[1]
        module = (root / "app/src/main/java/moudles/MagCardReaderMoudle.java").read_text(
            encoding="utf-8"
        )
        code_cases = set(re.findall(r"^    public void (H\w+)\(", module, re.MULTILINE))
        self.assertEqual(29, len(code_cases))

        def resource_cases(path):
            resources = ET.parse(path).getroot()
            return {
                item.attrib["name"]
                for item in resources.findall("string")
                if item.attrib.get("name", "").startswith("H")
            }

        default_cases = resource_cases(root / "app/src/main/res/values/strings.xml")
        self.assertEqual(code_cases, default_cases)
        self.assertFalse((root / "app/src/main/res/values-zh/strings.xml").exists())

        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        automation_case = (root / "app/src/main/java/automation/AutomationCase.java").read_text(
            encoding="utf-8"
        )
        host = SCRIPT.read_text(encoding="utf-8")
        self.assertIn('case "H01020":', registry)
        self.assertIn('case "H03_AUTO":', registry)
        self.assertIn("Code/document mismatch", registry)
        self.assertIn("expectedResult(caseName)", registry)
        self.assertIn('json.put("expectedResult", expectedResult)', automation_case)
        self.assertIn('case.get("expectedResult", "")', host)

    def test_module_two_automation_is_limited_to_the_selected_cases(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        allowlist = re.search(
            r"AUTOMATIC_MAGCARD_CASES\s*=\s*"
            r"Collections\.unmodifiableList\(Arrays\.asList\((.*?)\)\);",
            registry,
            re.DOTALL,
        )
        self.assertIsNotNone(allowlist)
        assert allowlist is not None
        self.assertEqual(
            {
                "H01003", "H01005", "H02002",
                "H04001", "H04002", "H04003",
            },
            set(re.findall(r'"(H\w+)"', allowlist.group(1))),
        )
        self.assertRegex(
            registry,
            r"Constants\.magcardBt\.equals\(descriptor\.id\)\s*"
            r"&& !AUTOMATIC_MAGCARD_CASES\.contains\(caseName\)\) \{\s*"
            r"continue;",
        )

    def test_module_two_only_speaks_short_action_prompts_when_action_is_ready(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        automation_case = (root / "app/src/main/java/automation/AutomationCase.java").read_text(
            encoding="utf-8"
        )
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        expected_user_action_cases = {
            "H01005", "H01013", "H01018", "H01023",
            "H01026", "H01027", "H02002", "H02005", "H04001", "H04002",
            "H04003", "H04004", "H04005", "H04006", "H04007", "H04008",
        }
        actual_user_action_cases = set(re.findall(
            r'case "(H\w+)":\s+return CaseProfile\.supportedWithUserAction',
            registry,
        ))
        self.assertEqual(expected_user_action_cases, actual_user_action_cases)
        self.assertIn('json.put("userActionRequired", userActionRequired)', automation_case)
        self.assertIn("if (automationCase.userActionRequired)", activity)
        automatic_magcard = re.search(
            r"private ResultStatus executeAutomaticMagCardCase\(.*?"
            r"private ResultStatus executeAutomaticIcCardCase\(",
            activity,
            re.DOTALL,
        )
        self.assertIsNotNone(automatic_magcard)
        assert automatic_magcard is not None
        self.assertLess(
            automatic_magcard.group(0).index("beginAutomaticMagCardCase"),
            automatic_magcard.group(0).index("awaitUserActionReady"),
        )
        self.assertEqual(1, automatic_magcard.group(0).count("speakAndWait"))
        self.assertNotIn("automationCase.moduleName", automatic_magcard.group(0))
        self.assertNotIn("automationCase.cleanupStep", automatic_magcard.group(0))
        for prompt in (
            "Please swipe the card.",
            "Please swipe the card quickly.",
            "Please insert and tap the test cards.",
            "Please swipe the damaged card.",
            "Please swipe the three-track card.",
        ):
            self.assertIn(prompt, activity)

        evaluator = (root / "app/src/main/java/automation/MagCardAutomationSession.java").read_text(
            encoding="utf-8"
        )
        self.assertIn("LONG_SEARCH_ACTION_DELAY_MS = 100_000L", evaluator)
        self.assertIn("markUserActionReady()", evaluator)

    def test_module_two_uses_callback_verdicts_without_pc_result_prompts(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        evaluator = (root / "app/src/main/java/automation/MagCardAutomationSession.java").read_text(
            encoding="utf-8"
        )
        automatic_magcard = re.search(
            r"private ResultStatus executeAutomaticMagCardCase\(.*?"
            r"private ResultStatus executeAutomaticCase\(",
            activity,
            re.DOTALL,
        )
        self.assertIsNotNone(automatic_magcard)
        assert automatic_magcard is not None
        self.assertNotIn("askOperator", automatic_magcard.group(0))
        self.assertIn("session.await(automationCase.timeoutMs)", automatic_magcard.group(0))
        self.assertIn("&& !Constants.magcardBt.equals(descriptor.id)", registry)
        self.assertIn("onSuccess(Bundle track)", evaluator)
        self.assertIn("onError(int error, String message)", evaluator)
        self.assertIn("onTimeout()", evaluator)
        self.assertIn("card data omitted", evaluator)

    def test_module_two_unobservable_cases_are_explicitly_not_run(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        for case_name in ("H01004", "H01019", "H01020", "H03_AUTO"):
            case_block = re.search(
                rf'case "{case_name}":(.*?)(?=\n\s*case "|\n\s*default:)',
                registry,
                re.DOTALL,
            )
            self.assertIsNotNone(case_block, case_name)
            assert case_block is not None
            self.assertIn("CaseProfile.unsupported", case_block.group(1), case_name)

    def test_module_three_automation_is_limited_to_workbook_auto_cases(self):
        root = SCRIPT.parents[1]
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        allowlist = re.search(
            r"AUTOMATIC_ICCARD_CASES\s*=\s*"
            r"Collections\.unmodifiableList\(Arrays\.asList\((.*?)\)\);",
            registry,
            re.DOTALL,
        )
        self.assertIsNotNone(allowlist)
        assert allowlist is not None
        self.assertEqual(
            {
                "I01001", "I02002", "I03001", "I03002", "I05001", "I06001",
                "I07001", "I07002", "I07003", "I08001", "I09001", "I10001",
            },
            set(re.findall(r'"(I\w+)"', allowlist.group(1))),
        )
        self.assertRegex(
            registry,
            r"Constants\.touchicBt\.equals\(descriptor\.id\)\s*"
            r"&& !AUTOMATIC_ICCARD_CASES\.contains\(caseName\)\) \{\s*"
            r"continue;",
        )

    def test_module_three_uses_returns_and_status_callbacks_without_pc_verdicts(self):
        root = SCRIPT.parents[1]
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        registry = (root / "app/src/main/java/automation/TesterCaseRegistry.java").read_text(
            encoding="utf-8"
        )
        evaluator = (root / "app/src/main/java/automation/IcCardAutomationSession.java").read_text(
            encoding="utf-8"
        )
        automatic_iccard = re.search(
            r"private ResultStatus executeAutomaticIcCardCase\(.*?"
            r"private ResultStatus finishTtsError\(",
            activity,
            re.DOTALL,
        )
        self.assertIsNotNone(automatic_iccard)
        assert automatic_iccard is not None
        self.assertNotIn("askOperator", automatic_iccard.group(0))
        self.assertIn("session.executeAfterUserAction()", automatic_iccard.group(0))
        self.assertIn("session.await(automationCase.timeoutMs)", automatic_iccard.group(0))
        self.assertIn("&& !Constants.touchicBt.equals(descriptor.id)", registry)
        for api in (
            "powerUp()", "powerDown()", "isCardIn()", "exchangeApdu(SELECT_PSE_APDU)",
            "isPSAMCardExists()", "checkCardStatus()", "getPowerUpATR()",
            "powerUpWithConfig(config)", "detectCardStatusChanged(30",
        ):
            self.assertIn(api, evaluator)
        self.assertIn("statusWord=", evaluator)
        self.assertIn("response data omitted", evaluator)
        self.assertIn("ATR data omitted", evaluator)

    def test_completed_module_is_announced_once_at_module_boundaries(self):
        root = SCRIPT.parents[1]
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        self.assertIn("boolean announceModuleCompletion = caseId.isEmpty()", activity)
        self.assertIn("boolean moduleBoundary = caseIndex == cases.size() - 1", activity)
        self.assertIn("speakModuleCompletion(automationCase.moduleName)", activity)
        self.assertIn('moduleName + " module completed testing."', activity)
        self.assertIn('sendError("TTS_FAILED", ttsQueue.getFailureReason(), false)', activity)

    def test_manual_cases_do_not_wait_for_pc_ready_or_cleanup_confirmation(self):
        root = SCRIPT.parents[1]
        activity = (root / "app/src/main/java/view/AutomationActivity.java").read_text(
            encoding="utf-8"
        )
        self.assertNotRegex(activity, r'askOperator\([^;]+"READY"')
        self.assertNotRegex(activity, r'askOperator\([^;]+"CLEANUP"')
        self.assertNotIn("isConfirmed(JSONObject response)", activity)
        self.assertRegex(activity, r'askOperator\([^;]+"RESULT"')


class ControllerFlowTest(unittest.TestCase):
    class FakeAdb:
        serial = "DEVICE01"

    class FakeProtocol:
        def __init__(self, messages=None):
            self.messages = list(messages or [])
            self.sent = []

        def send(self, message):
            self.sent.append(message)

        def receive(self):
            if not self.messages:
                raise AssertionError("No fake protocol message remains")
            return self.messages.pop(0)

        def connect(self, timeout=20):
            return None

    @staticmethod
    def module():
        return {
            "id": "beer",
            "name": "Beeper",
            "risk": "LOW",
            "destructive": False,
            "cases": [{
                "id": "beer:B01001",
                "module": "beer",
                "moduleName": "Beeper",
                "name": "B01001",
                "api": "beep",
                "precondition": "ready",
                "risk": "LOW",
                "timeoutMs": 1000,
                "manualStep": "listen",
                "cleanupStep": "",
                "userActionRequired": False,
                "supported": True,
            }],
        }

    def controller(self, protocol, output_dir):
        controller = automation.Controller(self.FakeAdb(), protocol, output_dir)
        controller.hello = {
            "appId": automation.APP_PACKAGE,
            "appVersion": "3.0.0",
            "deviceService": "1.0.2",
            "systemService": "DISABLED",
            "systemServiceSource": "embedded-device-service",
            "systemServiceHostVersion": "1.0.2",
        }
        controller.ready = {"allMapped": True, "modules": [self.module()]}
        return controller

    def test_single_module_result_prompt_and_report_flow(self):
        case = self.module()["cases"][0]
        protocol = self.FakeProtocol([
            {"type": "CASE_STARTED", "case": case},
            {"type": "OPERATOR_PROMPT", "promptId": "prompt-1", "kind": "RESULT", "text": "listen"},
            {"type": "CASE_FINISHED", "caseId": case["id"], "module": "beer", "name": "B01001",
             "status": "PASS", "actualResult": "heard", "durationMs": 25},
            {"type": "RUN_FINISHED", "status": "COMPLETED", "durationMs": 30,
             "summary": {"PASS": 1, "FAIL": 0, "ERROR": 0, "NOT_RUN": 0, "TOTAL": 1}},
        ])
        with tempfile.TemporaryDirectory() as directory, patch("builtins.input", return_value="p"):
            paths = self.controller(protocol, Path(directory)).run("beer")
            self.assertTrue(all(path.exists() for path in paths.values()))
        self.assertEqual("RUN_MODULE", protocol.sent[0]["type"])
        self.assertEqual("beer", protocol.sent[0]["module"])
        self.assertEqual("OPERATOR_PROMPT", protocol.sent[1]["type"])
        self.assertEqual("PASS", protocol.sent[1]["verdict"])

    def test_single_registered_case_uses_run_case_and_writes_one_row(self):
        case = self.module()["cases"][0]
        protocol = self.FakeProtocol([
            {"type": "CASE_STARTED", "case": case},
            {"type": "CASE_FINISHED", "caseId": case["id"], "module": "beer",
             "name": "B01001", "status": "PASS", "actualResult": "heard",
             "durationMs": 25},
            {"type": "RUN_FINISHED", "status": "COMPLETED", "durationMs": 30,
             "summary": {"PASS": 1, "FAIL": 0, "ERROR": 0, "NOT_RUN": 0,
                         "TOTAL": 1}},
        ])
        with tempfile.TemporaryDirectory() as directory:
            paths = self.controller(protocol, Path(directory)).run("beer:B01001")
            payload = json.loads(paths["json"].read_text(encoding="utf-8"))
        self.assertEqual("RUN_CASE", protocol.sent[0]["type"])
        self.assertEqual("beer:B01001", protocol.sent[0]["caseId"])
        self.assertEqual(1, payload["summary"]["TOTAL"])
        self.assertEqual(["beer:B01001"], [item["id"] for item in payload["cases"]])
        self.assertEqual("DISABLED", payload["environment"]["systemServiceState"])
        self.assertEqual("embedded-device-service", payload["environment"]["systemServiceSource"])
        self.assertEqual("1.0.2", payload["environment"]["systemServiceHostVersion"])
        self.assertNotIn("systemServiceVersion", payload["environment"])

    def test_all_command_and_device_bound_confirmation(self):
        protocol = self.FakeProtocol([
            {"type": "CASE_STARTED", "case": self.module()["cases"][0]},
            {"type": "CASE_FINISHED", "caseId": "beer:B01001", "module": "beer", "name": "B01001",
             "status": "NOT_RUN", "actualResult": "aborted", "durationMs": 0},
            {"type": "RUN_FINISHED", "status": "ABORTED", "durationMs": 1,
             "summary": {"PASS": 0, "FAIL": 0, "ERROR": 0, "NOT_RUN": 1, "TOTAL": 1}},
        ])
        with tempfile.TemporaryDirectory() as directory:
            controller = self.controller(protocol, Path(directory))
            with patch("builtins.input", return_value="RUN ALL DEVICE01"):
                self.assertTrue(controller.confirm_selection("ALL"))
            controller.run("ALL")
        self.assertEqual("RUN_ALL", protocol.sent[0]["type"])

    def test_menu_exit_sends_exit(self):
        protocol = self.FakeProtocol([{"type": "EXIT", "accepted": True}])
        with tempfile.TemporaryDirectory() as directory, patch("builtins.input", return_value="q"):
            self.controller(protocol, Path(directory)).menu()
        self.assertEqual([{"type": "EXIT"}], protocol.sent)

    def test_menu_omits_unavailable_case_explanations(self):
        protocol = self.FakeProtocol([{"type": "EXIT", "accepted": True}])
        module = self.module()
        module["id"] = "magcard"
        module["name"] = "Magcard"
        module["cases"][0].update(
            supported=False,
            unavailableReason="A deliberately long module-2 explanation",
        )
        with tempfile.TemporaryDirectory() as directory:
            controller = self.controller(protocol, Path(directory))
            controller.ready["modules"] = [module]
            output = StringIO()
            with patch("builtins.input", return_value="q"), redirect_stdout(output):
                controller.menu()
        self.assertIn("1. Magcard [magcard] (1 cases)", output.getvalue())
        self.assertNotIn("A deliberately long module-2 explanation", output.getvalue())

    def test_exit_waits_for_device_acknowledgement(self):
        protocol = self.FakeProtocol([
            {"type": "HELLO"},
            {"type": "EXIT", "accepted": True},
        ])
        with tempfile.TemporaryDirectory() as directory:
            self.controller(protocol, Path(directory)).exit()
        self.assertEqual([{"type": "EXIT"}], protocol.sent)


if __name__ == "__main__":
    unittest.main()
