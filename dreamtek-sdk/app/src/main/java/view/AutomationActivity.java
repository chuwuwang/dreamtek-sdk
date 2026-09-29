package view;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.verifone.activity.BuildConfig;
import com.verifone.activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import Utils.Constants;
import Utils.ServiceManager;
import automation.AutomationCase;
import automation.AutomaticCaseResult;
import automation.IcCardAutomationSession;
import automation.MagCardAutomationSession;
import automation.TesterCaseRegistry;
import automation.TtsSerialQueue;
import base.MyApplication;

/** ADB-only automation bridge for the legacy Tester Test catalog. */
public class AutomationActivity extends AppCompatActivity {
    private static final String TAG = "AutomationActivity";
    private static final String SOCKET_NAME = "tester_test_automation";
    private static final int PROTOCOL_VERSION = 1;
    private static final long TTS_TIMEOUT_MS = 30_000L;

    private final Object outputLock = new Object();
    private final ArrayDeque<String> outputBacklog = new ArrayDeque<>();
    private final Map<String, ArrayBlockingQueue<JSONObject>> promptResponses =
            new ConcurrentHashMap<>();

    private volatile BufferedWriter activeWriter;
    private volatile boolean destroyed;
    private volatile boolean deviceServiceReady;
    private volatile boolean registryBuilding;
    private volatile boolean ready;
    private volatile boolean running;
    private volatile boolean abortRequested;
    private volatile boolean exitRequested;
    private volatile JSONObject currentPrompt;

    private TextView statusView;
    private TextView promptView;
    private TextView logView;
    private final ServiceManager services = ServiceManager.getInstance();
    private ServiceManager.Listener serviceListener;

    private TtsSerialQueue ttsQueue;
    private TesterCaseRegistry registry;
    private LocalServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        statusView = findViewById(R.id.automation_status);
        promptView = findViewById(R.id.automation_prompt);
        logView = findViewById(R.id.automation_log);
        setStatus("Initializing automation entry point…");
        startSocketServer();
        initializeTts();
        bindRequiredServices();
    }

    private void initializeTts() {
        String version = packageVersion(TtsSerialQueue.ENGINE_PACKAGE);
        if (!TtsSerialQueue.EXPECTED_ENGINE_VERSION.equals(version)) {
            setStatus("iFlytek TTS version mismatch; execution blocked");
            sendError("TTS_VERSION_MISMATCH", "Required " + TtsSerialQueue.ENGINE_PACKAGE
                    + " version " + TtsSerialQueue.EXPECTED_ENGINE_VERSION
                    + ", installed " + version, false);
            return;
        }
        ttsQueue = new TtsSerialQueue(this, new TtsSerialQueue.InitListener() {
            @Override
            public void onReady() {
                appendLog("English TTS is ready");
                maybeBuildRegistry();
            }

            @Override
            public void onFailure(String reason) {
                setStatus("TTS unavailable; execution blocked");
                sendError("TTS_UNAVAILABLE", reason, false);
            }
        });
    }

    private void bindRequiredServices() {
        if (!isPackageInstalled(ServiceManager.PACKAGE_NAME)) {
            sendError("PACKAGE_MISSING", "Device service is not installed: "
                    + ServiceManager.PACKAGE_NAME, false);
        }

        serviceListener = new ServiceManager.Listener() {
            @Override public void onConnectionFailed(String message) {
                deviceServiceReady = false;
                sendError("SERVICE_BIND_FAILED", message, true);
            }

            @Override
            public void onConnected(IDeviceService deviceService) {
                if (destroyed) return;
                deviceServiceReady = true;
                appendLog("Device service connected");
                maybeBuildRegistry();
            }

            @Override
            public void onDisconnected() {
                deviceServiceReady = false;
                sendError("SERVICE_DISCONNECTED", "Device service disconnected; reconnecting", true);
            }
        };
        services.connect(this, serviceListener);
    }

    private boolean isPackageInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException error) {
            return false;
        }
    }

    private void maybeBuildRegistry() {
        if (!deviceServiceReady || ttsQueue == null || !ttsQueue.isReady()
                || registryBuilding || ready || destroyed) {
            return;
        }
        registryBuilding = true;
        runOnUiThread(() -> {
            try {
                registry = new TesterCaseRegistry(getApplicationContext());
                ready = true;
                setStatus("Automation ready; waiting for a PC command");
                sendReady();
            } catch (Throwable error) {
                sendError("REGISTRY_FAILED", safeError(error), false);
                setStatus("Case registration failed");
            } finally {
                registryBuilding = false;
            }
        });
    }

    private void startSocketServer() {
        new Thread(() -> {
            try {
                serverSocket = openServerSocketWithRecovery();
                appendLog("Local socket started: " + SOCKET_NAME);
                while (!destroyed) {
                    LocalSocket socket = serverSocket.accept();
                    handleClient(socket);
                }
            } catch (IOException error) {
                if (!destroyed) {
                    Log.e(TAG, "Automation socket failed", error);
                    setStatus("Automation socket failure");
                }
            }
        }, "automation-socket").start();
    }

    private LocalServerSocket openServerSocketWithRecovery() throws IOException {
        IOException lastError = null;
        for (int attempt = 0; attempt < 2 && !destroyed; attempt++) {
            try {
                return new LocalServerSocket(SOCKET_NAME);
            } catch (IOException error) {
                lastError = error;
                if (attempt == 0) {
                    appendLog("Local socket binding failed; performing the single recovery attempt after 500 ms");
                    SystemClock.sleep(500L);
                }
            }
        }
        if (lastError != null) {
            throw lastError;
        }
        throw new IOException("Automation activity was destroyed before socket startup");
    }

    private void handleClient(LocalSocket socket) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                    StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    StandardCharsets.UTF_8));
            synchronized (outputLock) {
                activeWriter = writer;
                writeLineLocked(helloMessage().toString());
                while (!outputBacklog.isEmpty()) {
                    writeLineLocked(outputBacklog.removeFirst());
                }
                if (ready) {
                    writeLineLocked(readyMessage().toString());
                }
                if (currentPrompt != null) {
                    writeLineLocked(currentPrompt.toString());
                }
            }
            String line;
            while ((line = reader.readLine()) != null && !destroyed) {
                handleCommand(new JSONObject(line));
            }
        } catch (Exception error) {
            Log.w(TAG, "PC automation connection closed: " + safeError(error));
        } finally {
            synchronized (outputLock) {
                if (activeWriter == writer) {
                    activeWriter = null;
                }
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private JSONObject helloMessage() throws JSONException {
        JSONObject json = message("HELLO");
        json.put("protocolVersion", PROTOCOL_VERSION);
        json.put("appId", BuildConfig.APPLICATION_ID);
        json.put("appVersion", BuildConfig.VERSION_NAME);
        json.put("deviceService", packageVersion(ServiceManager.PACKAGE_NAME));
        json.put("systemService", services.getSystemState());
        json.put("systemServiceSource", "embedded-device-service");
        json.put("systemServiceHostVersion", packageVersion(ServiceManager.PACKAGE_NAME));
        json.put("ttsEngine", TtsSerialQueue.ENGINE_PACKAGE);
        json.put("ttsEngineVersion", packageVersion(TtsSerialQueue.ENGINE_PACKAGE));
        return json;
    }

    private String packageVersion(String packageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(packageName, 0);
            return info.versionName == null ? "UNKNOWN" : info.versionName;
        } catch (PackageManager.NameNotFoundException error) {
            return "NOT_INSTALLED";
        }
    }

    private void sendReady() {
        try {
            send(readyMessage());
        } catch (JSONException error) {
            sendError("PROTOCOL_ERROR", safeError(error), false);
        }
    }

    private JSONObject readyMessage() throws JSONException {
        JSONObject json = message("READY");
        json.put("allMapped", registry != null && registry.isFullyMapped());
        json.put("modules", registry == null ? new JSONArray() : registry.toJson());
        return json;
    }

    private void handleCommand(JSONObject command) throws JSONException {
        String type = command.optString("type", "");
        switch (type) {
            case "RUN_MODULE":
                startRun(command.optString("runId", UUID.randomUUID().toString()),
                        command.optString("module", ""), "", false);
                break;
            case "RUN_CASE":
                startRun(command.optString("runId", UUID.randomUUID().toString()), "",
                        command.optString("caseId", ""), false);
                break;
            case "RUN_ALL":
                startRun(command.optString("runId", UUID.randomUUID().toString()),
                        "ALL", "", true);
                break;
            case "OPERATOR_PROMPT":
                acceptPromptResponse(command);
                break;
            case "ABORT":
                abortRequested = true;
                releasePrompts("ABORT");
                break;
            case "EXIT":
                abortRequested = true;
                exitRequested = true;
                releasePrompts("ABORT");
                runOnUiThread(this::finish);
                break;
            default:
                sendError("UNKNOWN_COMMAND", "Unknown command type: " + type, false);
                break;
        }
    }

    private synchronized void startRun(String runId, String moduleId, String caseId, boolean all) {
        if (!ready || registry == null) {
            sendError("NOT_READY", "Services, TTS, or registry are not ready", true);
            return;
        }
        if (running) {
            sendError("RUN_IN_PROGRESS", "Another run is already active", false);
            return;
        }
        List<AutomationCase> cases;
        if (all) {
            if (!registry.isFullyMapped()) {
                sendError("REGISTRY_INCOMPLETE", "RUN_ALL is disabled until every visible module is mapped", false);
                return;
            }
            cases = registry.getAllCases();
        } else if (!caseId.isEmpty()) {
            AutomationCase automationCase = registry.getCase(caseId);
            if (automationCase == null) {
                sendError("UNKNOWN_CASE", "Unknown case: " + caseId, false);
                return;
            }
            cases = new ArrayList<>();
            cases.add(automationCase);
        } else {
            TesterCaseRegistry.ModuleEntry module = registry.getModule(moduleId);
            if (module == null) {
                sendError("UNKNOWN_MODULE", "Unknown module: " + moduleId, false);
                return;
            }
            cases = new ArrayList<>(module.cases);
        }
        if (!deviceServiceReady) {
            sendError("NOT_READY", "A required service is disconnected", true);
            sendRejectedFinished(runId, cases, "Required service is disconnected");
            return;
        }
        if (isDeviceLocked()) {
            sendError("DEVICE_LOCKED", "Unlock the POS before starting automation", false);
            sendRejectedFinished(runId, cases, "Device is locked");
            return;
        }
        running = true;
        abortRequested = false;
        boolean announceModuleCompletion = caseId.isEmpty();
        new Thread(() -> executeRun(runId, cases, announceModuleCompletion),
                "automation-run").start();
    }

    private boolean isDeviceLocked() {
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return manager != null && manager.isKeyguardLocked();
    }

    private void executeRun(String runId, List<AutomationCase> cases,
                            boolean announceModuleCompletion) {
        long runStart = SystemClock.elapsedRealtime();
        Map<ResultStatus, Integer> counts = new EnumMap<>(ResultStatus.class);
        for (ResultStatus status : ResultStatus.values()) {
            counts.put(status, 0);
        }
        boolean stop = false;
        for (int caseIndex = 0; caseIndex < cases.size(); caseIndex++) {
            AutomationCase automationCase = cases.get(caseIndex);
            if (stop || abortRequested) {
                emitNotRun(runId, automationCase, "Run aborted", counts);
                continue;
            }
            ResultStatus status = executeCase(runId, automationCase);
            counts.put(status, counts.get(status) + 1);
            if (status == ResultStatus.ERROR && abortRequested) {
                stop = true;
            }
            boolean moduleBoundary = caseIndex == cases.size() - 1
                    || !automationCase.moduleId.equals(cases.get(caseIndex + 1).moduleId);
            if (announceModuleCompletion && moduleBoundary && !stop && !abortRequested
                    && !speakModuleCompletion(automationCase.moduleName)) {
                stop = true;
            }
        }
        JSONObject finished = messageQuiet("RUN_FINISHED");
        try {
            finished.put("runId", runId);
            finished.put("status", abortRequested ? "ABORTED" : "COMPLETED");
            finished.put("durationMs", SystemClock.elapsedRealtime() - runStart);
            JSONObject summary = new JSONObject();
            for (ResultStatus status : ResultStatus.values()) {
                summary.put(status.name(), counts.get(status));
            }
            summary.put("TOTAL", cases.size());
            finished.put("summary", summary);
            send(finished);
        } catch (JSONException error) {
            sendError("PROTOCOL_ERROR", safeError(error), false);
        } finally {
            running = false;
            currentPrompt = null;
            setStatus("Run finished; waiting for a PC command");
        }
    }

    private ResultStatus executeCase(String runId, AutomationCase automationCase) {
        long start = SystemClock.elapsedRealtime();
        sendCaseStarted(runId, automationCase);
        if (!automationCase.supported) {
            sendCaseFinished(runId, automationCase, ResultStatus.NOT_RUN,
                    automationCase.unavailableReason, SystemClock.elapsedRealtime() - start);
            return ResultStatus.NOT_RUN;
        }

        if (!awaitServices(10_000L)) {
            abortRequested = true;
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    "Required service is disconnected", SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }

        if (Constants.magcardBt.equals(automationCase.moduleId)) {
            return executeAutomaticMagCardCase(runId, automationCase, start);
        }

        if (Constants.touchicBt.equals(automationCase.moduleId)) {
            return executeAutomaticIcCardCase(runId, automationCase, start);
        }

        if (!automationCase.operatorRequired) {
            return executeAutomaticCase(runId, automationCase, start);
        }

        setStatus("Running: " + automationCase.id);
        String readyInstruction;
        if (automationCase.userActionRequired) {
            readyInstruction = "Prepare the required test card. The interface will start shortly; act only after the voice prompt says to begin.";
        } else {
            readyInstruction = "About to run: " + automationCase.manualStep;
        }
        String spoken = automationCase.moduleName + "。" + automationCase.name + "。"
                + readyInstruction;
        if (!ttsQueue.speakAndWait(spoken, TTS_TIMEOUT_MS)) {
            abortRequested = true;
            String reason = ttsQueue.getFailureReason();
            sendError("TTS_FAILED", reason, false);
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR, reason,
                    SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }

        AtomicReference<Throwable> executionError = new AtomicReference<>();
        CountDownLatch executionFinished = new CountDownLatch(1);
        runOnUiThread(() -> {
            try {
                MyApplication.serviceMoudle.runTheMethod(automationCase.moduleId,
                        automationCase.groupPosition, automationCase.childPosition);
            } catch (Throwable error) {
                executionError.set(error);
            } finally {
                executionFinished.countDown();
            }
        });
        try {
            if (!executionFinished.await(automationCase.timeoutMs, TimeUnit.MILLISECONDS)) {
                abortRequested = true;
                sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                        "Case execution timed out", SystemClock.elapsedRealtime() - start);
                return ResultStatus.ERROR;
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            abortRequested = true;
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    "Case execution interrupted", SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }
        if (executionError.get() != null) {
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    safeError(executionError.get()), SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }

        if (automationCase.userActionRequired) {
            setStatus("Waiting for operator action: " + automationCase.id);
            String actionInstruction = "Begin the operation. " + automationCase.manualStep;
            if (!ttsQueue.speakAndWait(actionInstruction, TTS_TIMEOUT_MS)) {
                abortRequested = true;
                String reason = ttsQueue.getFailureReason();
                sendError("TTS_FAILED", reason, false);
                sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                        reason, SystemClock.elapsedRealtime() - start);
                return ResultStatus.ERROR;
            }
        }

        String resultInstruction = "Action: " + automationCase.manualStep;
        if (!automationCase.expectedResult.isEmpty()) {
            resultInstruction += "\nExpected: " + automationCase.expectedResult;
        }
        resultInstruction += "\nVerify the device behavior, then select PASS or FAIL on the PC.";
        JSONObject resultResponse = askOperator(runId, automationCase, "RESULT",
                resultInstruction, automationCase.timeoutMs);
        ResultStatus status = responseStatus(resultResponse, ResultStatus.ERROR);
        String note = responseNote(resultResponse, "Operator result");
        if (status == ResultStatus.ERROR) {
            abortRequested = true;
        }

        if (!automationCase.cleanupStep.isEmpty() && !abortRequested) {
            if (!ttsQueue.speakAndWait(automationCase.cleanupStep, TTS_TIMEOUT_MS)) {
                status = ResultStatus.ERROR;
                note = ttsQueue.getFailureReason();
                abortRequested = true;
            }
        }

        sendCaseFinished(runId, automationCase, status, note,
                SystemClock.elapsedRealtime() - start);
        return status;
    }

    private ResultStatus executeAutomaticMagCardCase(String runId,
                                                     AutomationCase automationCase,
                                                     long start) {
        setStatus("Running and evaluating automatically: " + automationCase.id);
        MagCardAutomationSession session;
        try {
            session = MyApplication.serviceMoudle.beginAutomaticMagCardCase(automationCase.name);
        } catch (Throwable error) {
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    safeError(error), SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }

        if (automationCase.userActionRequired) {
            setStatus("Waiting for operator action: " + automationCase.id);
            if (session.awaitUserActionReady(automationCase.timeoutMs)
                    && !ttsQueue.speakAndWait(magCardVoicePrompt(automationCase.name),
                    TTS_TIMEOUT_MS)) {
                session.cancel();
                return finishTtsError(runId, automationCase, start);
            }
        }

        AutomaticCaseResult result = session.await(automationCase.timeoutMs);
        ResultStatus status;
        try {
            status = ResultStatus.valueOf(result.status);
        } catch (IllegalArgumentException error) {
            status = ResultStatus.ERROR;
        }
        String actualResult = result.actualResult;

        sendCaseFinished(runId, automationCase, status, actualResult,
                SystemClock.elapsedRealtime() - start);
        return status;
    }

    private static String magCardVoicePrompt(String caseName) {
        if ("H01013".equals(caseName)) {
            return "Please swipe the card quickly.";
        }
        if ("H01023".equals(caseName)) {
            return "Please insert and tap the test cards.";
        }
        if ("H01027".equals(caseName)) {
            return "Please swipe the damaged card.";
        }
        if (caseName.startsWith("H04")) {
            return "Please swipe the three-track card.";
        }
        return "Please swipe the card.";
    }

    private ResultStatus executeAutomaticIcCardCase(String runId,
                                                    AutomationCase automationCase,
                                                    long start) {
        setStatus("Running and evaluating automatically: " + automationCase.id);
        IcCardAutomationSession session;
        try {
            session = MyApplication.serviceMoudle.beginAutomaticIcCardCase(automationCase.name);
        } catch (Throwable error) {
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    safeError(error), SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }

        if (!session.awaitUserActionReady(automationCase.timeoutMs)) {
            AutomaticCaseResult result = session.await(1L);
            return finishAutomaticResult(runId, automationCase, start, result);
        }
        if (automationCase.userActionRequired
                && !ttsQueue.speakAndWait(icCardVoicePrompt(automationCase.name),
                TTS_TIMEOUT_MS)) {
            session.cancel();
            return finishTtsError(runId, automationCase, start);
        }

        session.executeAfterUserAction();
        return finishAutomaticResult(runId, automationCase, start,
                session.await(automationCase.timeoutMs));
    }

    private static String icCardVoicePrompt(String caseName) {
        if ("I03001".equals(caseName) || "I07003".equals(caseName)) {
            return "Please remove the contact I C card.";
        }
        if ("I10001".equals(caseName)) {
            return "Please remove any inserted contact I C card, then insert and remove it.";
        }
        return "Please insert the contact I C card if it is not already inserted.";
    }

    private ResultStatus finishAutomaticResult(String runId, AutomationCase automationCase,
                                               long start, AutomaticCaseResult result) {
        ResultStatus status;
        try {
            status = ResultStatus.valueOf(result.status);
        } catch (IllegalArgumentException error) {
            status = ResultStatus.ERROR;
        }
        sendCaseFinished(runId, automationCase, status, result.actualResult,
                SystemClock.elapsedRealtime() - start);
        return status;
    }

    private boolean speakModuleCompletion(String moduleName) {
        setStatus(moduleName + " module completed testing");
        if (ttsQueue.speakAndWait(moduleName + " module completed testing.",
                TTS_TIMEOUT_MS)) {
            return true;
        }
        abortRequested = true;
        sendError("TTS_FAILED", ttsQueue.getFailureReason(), false);
        return false;
    }

    private ResultStatus finishTtsError(String runId, AutomationCase automationCase,
                                        long start) {
        abortRequested = true;
        String reason = ttsQueue.getFailureReason();
        sendError("TTS_FAILED", reason, false);
        sendCaseFinished(runId, automationCase, ResultStatus.ERROR, reason,
                SystemClock.elapsedRealtime() - start);
        return ResultStatus.ERROR;
    }

    private ResultStatus executeAutomaticCase(String runId, AutomationCase automationCase,
                                              long start) {
        setStatus("Running and evaluating automatically: " + automationCase.id);
        AtomicReference<AutomaticCaseResult> automaticResult = new AtomicReference<>();
        AtomicReference<Throwable> executionError = new AtomicReference<>();
        CountDownLatch executionFinished = new CountDownLatch(1);
        runOnUiThread(() -> {
            try {
                if (!Constants.serviceBt.equals(automationCase.moduleId)) {
                    throw new IllegalStateException(
                            "No automatic executor for module " + automationCase.moduleId);
                }
                automaticResult.set(MyApplication.serviceMoudle.runAutomaticServiceCase(
                        automationCase.groupPosition, automationCase.childPosition));
            } catch (Throwable error) {
                executionError.set(error);
            } finally {
                executionFinished.countDown();
            }
        });
        try {
            if (!executionFinished.await(automationCase.timeoutMs, TimeUnit.MILLISECONDS)) {
                sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                        "Automatic case execution timed out",
                        SystemClock.elapsedRealtime() - start);
                return ResultStatus.ERROR;
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    "Automatic case execution interrupted",
                    SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }
        if (executionError.get() != null) {
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    safeError(executionError.get()), SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }
        AutomaticCaseResult result = automaticResult.get();
        if (result == null) {
            sendCaseFinished(runId, automationCase, ResultStatus.ERROR,
                    "Automatic evaluator returned no result",
                    SystemClock.elapsedRealtime() - start);
            return ResultStatus.ERROR;
        }
        ResultStatus status;
        try {
            status = ResultStatus.valueOf(result.status);
        } catch (IllegalArgumentException error) {
            status = ResultStatus.ERROR;
        }
        sendCaseFinished(runId, automationCase, status, result.actualResult,
                SystemClock.elapsedRealtime() - start);
        return status;
    }

    private boolean awaitServices(long timeoutMs) {
        long deadline = SystemClock.elapsedRealtime() + timeoutMs;
        while ((!deviceServiceReady) && !abortRequested
                && SystemClock.elapsedRealtime() < deadline) {
            SystemClock.sleep(100L);
        }
        return deviceServiceReady && !abortRequested;
    }

    private JSONObject askOperator(String runId, AutomationCase automationCase, String kind,
                                   String text, long timeoutMs) {
        String promptId = UUID.randomUUID().toString();
        ArrayBlockingQueue<JSONObject> responseQueue = new ArrayBlockingQueue<>(1);
        promptResponses.put(promptId, responseQueue);
        JSONObject prompt = messageQuiet("OPERATOR_PROMPT");
        try {
            prompt.put("runId", runId);
            prompt.put("promptId", promptId);
            prompt.put("kind", kind);
            prompt.put("caseId", automationCase.id);
            prompt.put("text", text);
            prompt.put("timeoutMs", timeoutMs);
            JSONArray choices = new JSONArray();
            if ("RESULT".equals(kind)) {
                choices.put("PASS");
                choices.put("FAIL");
            } else {
                choices.put("CONFIRMED");
                choices.put("FAIL");
            }
            choices.put("ABORT");
            prompt.put("choices", choices);
            currentPrompt = prompt;
            setPrompt(text);
            send(prompt);
            JSONObject response = responseQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
            if (response == null) {
                JSONObject timeout = new JSONObject();
                timeout.put("verdict", "ERROR");
                timeout.put("note", "Operator prompt timed out");
                return timeout;
            }
            return response;
        } catch (Exception error) {
            JSONObject failed = new JSONObject();
            try {
                failed.put("verdict", "ERROR");
                failed.put("note", safeError(error));
            } catch (JSONException ignored) {
            }
            return failed;
        } finally {
            promptResponses.remove(promptId);
            currentPrompt = null;
            setPrompt("Waiting for the next action");
        }
    }

    private void acceptPromptResponse(JSONObject response) {
        String promptId = response.optString("promptId", "");
        ArrayBlockingQueue<JSONObject> queue = promptResponses.get(promptId);
        if (queue == null) {
            sendError("UNKNOWN_PROMPT", "No pending prompt: " + promptId, false);
            return;
        }
        queue.offer(response);
    }

    private void releasePrompts(String verdict) {
        for (ArrayBlockingQueue<JSONObject> queue : promptResponses.values()) {
            JSONObject response = new JSONObject();
            try {
                response.put("verdict", verdict);
                response.put("note", "Run aborted");
            } catch (JSONException ignored) {
            }
            queue.offer(response);
        }
    }

    private static ResultStatus responseStatus(JSONObject response, ResultStatus fallback) {
        if (response == null) {
            return fallback;
        }
        try {
            String verdict = response.optString("verdict", fallback.name()).toUpperCase(Locale.ROOT);
            if ("ABORT".equals(verdict)) {
                return ResultStatus.ERROR;
            }
            return ResultStatus.valueOf(verdict);
        } catch (IllegalArgumentException error) {
            return fallback;
        }
    }

    private static String responseNote(JSONObject response, String fallback) {
        return response == null ? fallback : response.optString("note", fallback);
    }

    private void sendCaseStarted(String runId, AutomationCase automationCase) {
        JSONObject json = messageQuiet("CASE_STARTED");
        try {
            json.put("runId", runId);
            json.put("case", automationCase.toJson());
            send(json);
        } catch (JSONException error) {
            sendError("PROTOCOL_ERROR", safeError(error), false);
        }
    }

    private void sendCaseFinished(String runId, AutomationCase automationCase, ResultStatus status,
                                  String actualResult, long durationMs) {
        JSONObject json = messageQuiet("CASE_FINISHED");
        try {
            json.put("runId", runId);
            json.put("caseId", automationCase.id);
            json.put("module", automationCase.moduleId);
            json.put("name", automationCase.name);
            json.put("status", status.name());
            json.put("actualResult", actualResult);
            json.put("durationMs", durationMs);
            send(json);
        } catch (JSONException error) {
            sendError("PROTOCOL_ERROR", safeError(error), false);
        }
    }

    private void emitNotRun(String runId, AutomationCase automationCase, String reason,
                            Map<ResultStatus, Integer> counts) {
        sendCaseStarted(runId, automationCase);
        sendCaseFinished(runId, automationCase, ResultStatus.NOT_RUN, reason, 0L);
        counts.put(ResultStatus.NOT_RUN, counts.get(ResultStatus.NOT_RUN) + 1);
    }

    private void sendRejectedFinished(String runId, List<AutomationCase> cases, String reason) {
        for (AutomationCase automationCase : cases) {
            sendCaseStarted(runId, automationCase);
            sendCaseFinished(runId, automationCase, ResultStatus.NOT_RUN, reason, 0L);
        }
        JSONObject json = messageQuiet("RUN_FINISHED");
        try {
            json.put("runId", runId);
            json.put("status", "ERROR");
            json.put("durationMs", 0L);
            json.put("reason", reason);
            JSONObject summary = new JSONObject();
            for (ResultStatus result : ResultStatus.values()) {
                summary.put(result.name(), result == ResultStatus.NOT_RUN ? cases.size() : 0);
            }
            summary.put("TOTAL", cases.size());
            json.put("summary", summary);
            send(json);
        } catch (JSONException error) {
            Log.e(TAG, "Cannot send empty run result", error);
        }
    }

    private void sendError(String code, String detail, boolean recoverable) {
        JSONObject json = messageQuiet("ERROR");
        try {
            json.put("code", code);
            json.put("detail", detail);
            json.put("recoverable", recoverable);
            send(json);
        } catch (JSONException error) {
            Log.e(TAG, "Cannot serialize protocol error", error);
        }
    }

    private static JSONObject message(String type) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("timestampMs", System.currentTimeMillis());
        return json;
    }

    private static JSONObject messageQuiet(String type) {
        try {
            return message(type);
        } catch (JSONException impossible) {
            return new JSONObject();
        }
    }

    private void send(JSONObject json) {
        String line = json.toString();
        synchronized (outputLock) {
            if (activeWriter == null) {
                if (!isCurrentPrompt(json)) {
                    outputBacklog.addLast(line);
                }
                return;
            }
            try {
                writeLineLocked(line);
            } catch (IOException error) {
                if (!isCurrentPrompt(json)) {
                    outputBacklog.addLast(line);
                }
                activeWriter = null;
            }
        }
    }

    private boolean isCurrentPrompt(JSONObject json) {
        JSONObject prompt = currentPrompt;
        return prompt != null
                && "OPERATOR_PROMPT".equals(json.optString("type"))
                && prompt.optString("promptId").equals(json.optString("promptId"));
    }

    private void writeLineLocked(String line) throws IOException {
        activeWriter.write(line);
        activeWriter.newLine();
        activeWriter.flush();
    }

    private void setStatus(String text) {
        if (statusView != null) {
            statusView.post(() -> statusView.setText(text));
        }
    }

    private void setPrompt(String text) {
        if (promptView != null) {
            promptView.post(() -> promptView.setText(text));
        }
    }

    private void appendLog(String text) {
        Log.i(TAG, text);
        if (logView != null) {
            logView.post(() -> logView.append("\n" + text));
        }
    }

    private static String safeError(Throwable error) {
        String message = error.getMessage();
        return error.getClass().getSimpleName() + (message == null ? "" : ": " + message);
    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        abortRequested = true;
        releasePrompts("ABORT");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
        if (ttsQueue != null) {
            ttsQueue.shutdown();
        }
        if (serviceListener != null) services.disconnect(serviceListener);

        if (exitRequested) {
            try {
                JSONObject exit = message("EXIT");
                exit.put("accepted", true);
                send(exit);
            } catch (JSONException error) {
                Log.e(TAG, "Cannot send EXIT acknowledgement", error);
            }
        }
        super.onDestroy();
    }

    private enum ResultStatus {
        PASS,
        FAIL,
        ERROR,
        NOT_RUN
    }
}
