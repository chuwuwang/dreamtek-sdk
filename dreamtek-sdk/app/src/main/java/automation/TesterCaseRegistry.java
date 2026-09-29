package automation;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Utils.CaseNameUtils;
import Utils.Constants;
import view.TestActivity;

/**
 * Fixed registry for every module visible on the legacy "Tester Test" screen.
 *
 * The module mapping is deliberately explicit. Case method names are read from the existing
 * legacy catalogs so that the automation entry point runs the same cases as the UI. Stable IDs
 * combine the fixed module ID and the existing case method name.
 */
public final class TesterCaseRegistry {
    private static final long DEFAULT_TIMEOUT_MS = 120_000L;
    private static final List<String> AUTOMATIC_MAGCARD_CASES =
            Collections.unmodifiableList(Arrays.asList(
                    "H01003",
                    "H01005",
                    "H02002",
                    "H04001",
                    "H04002",
                    "H04003"));
    private static final List<String> AUTOMATIC_ICCARD_CASES =
            Collections.unmodifiableList(Arrays.asList(
                    "I01001",
                    "I02002",
                    "I03001",
                    "I03002",
                    "I05001",
                    "I06001",
                    "I07001",
                    "I07002",
                    "I07003",
                    "I08001",
                    "I09001",
                    "I10001"));

    private final Context context;
    private final Map<String, ModuleEntry> modules = new LinkedHashMap<>();

    public TesterCaseRegistry(Context context) {
        this.context = context.getApplicationContext();
        CaseNameUtils names = CaseNameUtils.getInstance(context.getApplicationContext());
        add(names, automaticDescriptor(Constants.serviceBt, "Get Service", "Device service connected",
                "LOW"));
        add(names, descriptor(Constants.magcardBt, "Magcard", "Prepare a test magnetic card", "MEDIUM",
                "Swipe the card when prompted on screen and by voice", "Remove the magnetic card"));
        add(names, descriptor(Constants.touchicBt, "ICCard", "Prepare a contact IC card", "MEDIUM",
                "Insert the card when prompted", "Remove the inserted card and verify that the slot is empty"));
        add(names, descriptor(Constants.notouchIcBt, "CTLS Card", "Prepare a contactless card", "MEDIUM",
                "Tap the card when prompted", "Remove the contactless card from the sensing area"));
        add(names, descriptor(Constants.beerBt, "Beeper", "The environment permits beeping", "LOW",
                "Listen for the beeper", ""));
        add(names, descriptor(Constants.ledBt, "LED", "The device LEDs are visible", "LOW",
                "Observe the LED color and on/off state", ""));
        add(names, descriptor(Constants.scanBt, "Scanner", "Prepare a barcode or QR code", "MEDIUM",
                "Place the code in the scanning area", "Remove the code"));
        add(names, descriptor(Constants.pintBt, "Printer", "Load sufficient printer paper", "MEDIUM",
                "Observe the printed output", "Remove the printout and verify that the paper compartment is ready"));
        add(names, descriptor(Constants.serialPortBt, "Serial Port", "Connect a compatible serial peripheral", "HIGH",
                "Verify the serial cable and peripheral model", "Disconnect the peripheral used only for this case"));
        add(names, descriptor(Constants.emvBt, "EMV", "Prepare an EMV test card", "HIGH",
                "Insert or tap the card and complete the prompted transaction steps", "Remove all payment cards"));
        add(names, descriptor(Constants.pinpadBt, "Pinpad", "Verify the KAP and test-key environment", "DESTRUCTIVE",
                "Use test keys only and follow the PIN pad prompts", "Verify that the PIN pad has exited its input screen"));
        addUnsupported(descriptor(Constants.pbocBt, "PBOC", "Legacy PBOC AIDL is unavailable in the current service JAR",
                "HIGH", "No operator action required", ""), "PBOC implementation is disabled in this source tree");
        add(names, descriptor(Constants.serviceInfoBt, "ServiceInfo", "Device service connected", "LOW",
                "Verify the service information", ""));
        addUnsupported(descriptor(Constants.eppBt, "EPP", "Legacy EPP AIDL is unavailable in the current service JAR",
                "DESTRUCTIVE", "No operator action required", ""), "EPP implementation is disabled in this source tree");
        add(names, descriptor(Constants.usbPortBt, "USB Port", "Connect a compatible USB serial peripheral", "HIGH",
                "Verify that the USB peripheral is connected", "Safely remove the test USB peripheral"));
        add(names, descriptor(Constants.externalSerialPortBt, "X990 Base", "Connect the X990 base", "HIGH",
                "Verify that the base and cable are connected", "Return the base to its idle state"));
        addUnsupported(descriptor(Constants.x990PinpadBt, "X990 PINPAD",
                "Legacy X990 PINPAD AIDL is unavailable in the current service JAR", "DESTRUCTIVE",
                "No operator action required", ""), "X990 PINPAD implementation is disabled in this source tree");
        add(names, descriptor(Constants.sdeBt, "SDE", "Use test data and test keys only", "DESTRUCTIVE",
                "Verify authorization for secure-domain operations", "Verify that sensitive input has been cleared"));
        add(names, descriptor(Constants.dukptBt, "DUKPT", "Use test keys only", "DESTRUCTIVE",
                "Verify the test KSN and key conditions", "Verify that the key-test environment has been restored"));
        add(names, descriptor(Constants.MKSK, "MKSK", "Use test keys only", "DESTRUCTIVE",
                "Verify authorization for master/session key tests", "Verify that the key-test environment has been restored"));
        add(names, descriptor(Constants.ultralightCardBt, "UltralightCard", "Prepare an Ultralight card", "MEDIUM",
                "Place the card in the sensing area", "Remove the card from the sensing area"));
        add(names, descriptor(Constants.ultralightCardCBt, "UltralightCard C", "Prepare an Ultralight C card", "MEDIUM",
                "Place the Ultralight C card in the sensing area", "Remove the card from the sensing area"));
        add(names, descriptor(Constants.ultralightCardEV1Bt, "UltralightCard EV1", "Prepare an Ultralight EV1 card", "MEDIUM",
                "Place the Ultralight EV1 card in the sensing area", "Remove the card from the sensing area"));
        add(names, descriptor(Constants.rsa, "RSA", "Use test keys and test data only", "DESTRUCTIVE",
                "Verify that the RSA test will not overwrite production material", "Verify that test key material has been cleared"));
    }

    private static ModuleDescriptor descriptor(String id, String name, String precondition,
                                               String risk, String manualStep, String cleanupStep) {
        return new ModuleDescriptor(id, name, precondition, risk, DEFAULT_TIMEOUT_MS,
                manualStep, cleanupStep, true);
    }

    private static ModuleDescriptor automaticDescriptor(String id, String name,
                                                        String precondition, String risk) {
        return new ModuleDescriptor(id, name, precondition, risk, DEFAULT_TIMEOUT_MS,
                "", "", false);
    }

    private void add(CaseNameUtils names, ModuleDescriptor descriptor) {
        List<AutomationCase> cases = new ArrayList<>();
        String unavailable = "";
        try {
            ArrayList<String> apis = names.getMoudleAPIs(descriptor.id);
            ArrayList<ArrayList<String>> groups = names.getCaseNames(descriptor.id);
            if (apis == null || groups == null) {
                unavailable = "Legacy module returned no case catalog";
            } else {
                int groupCount = Math.min(apis.size(), groups.size());
                for (int group = 0; group < groupCount; group++) {
                    ArrayList<String> caseNames = groups.get(group);
                    if (caseNames == null) {
                        continue;
                    }
                    for (int child = 0; child < caseNames.size(); child++) {
                        String caseName = caseNames.get(child);
                        if (Constants.magcardBt.equals(descriptor.id)
                                && !AUTOMATIC_MAGCARD_CASES.contains(caseName)) {
                            continue;
                        }
                        if (Constants.touchicBt.equals(descriptor.id)
                                && !AUTOMATIC_ICCARD_CASES.contains(caseName)) {
                            continue;
                        }
                        String api;
                        if (Constants.serviceBt.equals(descriptor.id)) {
                            api = serviceApi(caseName);
                        } else if (Constants.magcardBt.equals(descriptor.id)) {
                            api = magcardApi(caseName);
                        } else if (Constants.touchicBt.equals(descriptor.id)) {
                            api = icCardApi(caseName);
                        } else {
                            api = apis.get(group);
                        }
                        CaseProfile profile = caseProfile(descriptor, caseName);
                        cases.add(new AutomationCase(
                                stableId(descriptor.id, caseName), descriptor.id, descriptor.name,
                                caseName, api, descriptor.precondition,
                                expectedResult(caseName), descriptor.risk,
                                profile.timeoutMs, profile.manualStep, profile.cleanupStep,
                                descriptor.operatorRequired
                                        && !Constants.magcardBt.equals(descriptor.id)
                                        && !Constants.touchicBt.equals(descriptor.id),
                                profile.userActionRequired,
                                profile.supported,
                                profile.unavailableReason, group, child));
                    }
                }
                if (cases.isEmpty()) {
                    unavailable = "Legacy module has no executable cases";
                }
            }
        } catch (Throwable error) {
            unavailable = error.getClass().getSimpleName() + ": " + safeMessage(error);
        }
        if (!unavailable.isEmpty()) {
            cases.add(unavailableCase(descriptor, unavailable));
        }
        modules.put(descriptor.id, new ModuleEntry(descriptor, cases));
    }

    private void addUnsupported(ModuleDescriptor descriptor, String reason) {
        modules.put(descriptor.id, new ModuleEntry(descriptor,
                Collections.singletonList(unavailableCase(descriptor, reason))));
    }

    private static AutomationCase unavailableCase(ModuleDescriptor descriptor, String reason) {
        return new AutomationCase(stableId(descriptor.id, "UNAVAILABLE"), descriptor.id,
                descriptor.name, "UNAVAILABLE", "UNAVAILABLE", descriptor.precondition, "",
                descriptor.risk, descriptor.timeoutMs, descriptor.manualStep,
                descriptor.cleanupStep, descriptor.operatorRequired, false, false, reason, -1, -1);
    }

    private static String stableId(String module, String caseName) {
        return module + ":" + caseName;
    }

    private static String serviceApi(String caseName) {
        if (caseName.startsWith("A01")) {
            return "getBeeper()";
        }
        if (caseName.startsWith("A02")) {
            return "getLed()";
        }
        if (caseName.startsWith("A03")) {
            return "getPrinter()";
        }
        if (caseName.startsWith("A04")) {
            return "getScanner(int)";
        }
        if (caseName.startsWith("A05")) {
            return "getSerialPort(String)";
        }
        if (caseName.startsWith("A06")) {
            return "getServiceInfo()";
        }
        if (caseName.startsWith("A08")) {
            return "getMagCardReader()";
        }
        if (caseName.startsWith("A09")) {
            return "getSmartCardReader(int)";
        }
        if (caseName.startsWith("A10")) {
            return "getRFCardReader()";
        }
        if (caseName.startsWith("A11")) {
            return "getPinpad(int)";
        }
        if (caseName.startsWith("A12")) {
            return "getUsbSerialPort()";
        }
        if (caseName.startsWith("A13")) {
            return "getEMV()";
        }
        if (caseName.startsWith("A14")) {
            return "getDUKPT()";
        }
        if ("A15_AUTO".equals(caseName)) {
            return "aggregate Get Service contracts";
        }
        return "UNKNOWN";
    }

    private static String magcardApi(String caseName) {
        if (caseName.startsWith("H01")) {
            return "searchCard(int, MagCardListener)";
        }
        if (caseName.startsWith("H02")) {
            return "stopSearch()";
        }
        if ("H03_AUTO".equals(caseName)) {
            return "autoTest()";
        }
        if (caseName.startsWith("H04")) {
            return "enableTrack(int) + searchCard(int, MagCardListener)";
        }
        return "UNKNOWN";
    }

    private static String icCardApi(String caseName) {
        if (caseName.startsWith("I01")) {
            return "powerUp()";
        }
        if (caseName.startsWith("I02")) {
            return "powerDown()";
        }
        if (caseName.startsWith("I03")) {
            return "isCardIn()";
        }
        if (caseName.startsWith("I05")) {
            return "powerUp() + exchangeApdu(byte[])";
        }
        if (caseName.startsWith("I06")) {
            return "isPSAMCardExists()";
        }
        if (caseName.startsWith("I07")) {
            return "powerUp()/powerDown() + checkCardStatus()";
        }
        if (caseName.startsWith("I08")) {
            return "powerUp() + getPowerUpATR()";
        }
        if (caseName.startsWith("I09")) {
            return "powerUpWithConfig(Bundle)";
        }
        if (caseName.startsWith("I10")) {
            return "detectCardStatusChanged(int, SmartCardStatusChangedEvent)";
        }
        return "UNKNOWN";
    }

    private CaseProfile caseProfile(ModuleDescriptor descriptor, String caseName) {
        if (Constants.touchicBt.equals(descriptor.id)) {
            return icCardCaseProfile(descriptor, caseName);
        }
        if (!Constants.magcardBt.equals(descriptor.id)) {
            return CaseProfile.supported(descriptor.timeoutMs, descriptor.manualStep,
                    descriptor.cleanupStep);
        }
        switch (caseName) {
            case "H01001":
                return CaseProfile.supported(90_000L, "Do not swipe a card; wait for the default 60-second timeout callback", "");
            case "H01002":
                return CaseProfile.supported(40_000L, "Do not swipe a card; wait for the 10-second timeout callback", "");
            case "H01003":
                return CaseProfile.supported(90_000L, "Do not swipe a card; wait for the 60-second timeout callback", "");
            case "H01004":
                return CaseProfile.unsupported(40_000L,
                        "listener=null exposes no callback or observable state that can prove "
                                + "a physical swipe was ignored");
            case "H01005":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a test card normally with data on tracks 1, 2, and 3",
                        "Remove the magnetic card");
            case "H01013":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Quickly swipe a test card with data on tracks 1, 2, and 3", "Remove the magnetic card");
            case "H01014":
                return CaseProfile.supported(330_000L, "Do not swipe a card; wait for the 300-second timeout callback", "");
            case "H01015":
                return CaseProfile.supported(90_000L, "Do not swipe a card; wait for the invalid timeout to fall back to 60 seconds",
                        "");
            case "H01016":
                return CaseProfile.supported(90_000L, "Do not swipe a card; wait for the out-of-range timeout to fall back to 60 seconds",
                        "");
            case "H01018":
                return CaseProfile.supportedWithUserAction(120_000L,
                        "Wait for the second search call, then swipe a test card and observe concurrent-search behavior", "Remove the magnetic card");
            case "H01019":
                return CaseProfile.unsupported(120_000L,
                        "The legacy D03028 printer case does not expose its callback to the "
                                + "Magcard automatic evaluator");
            case "H01020":
                return CaseProfile.unsupported(descriptor.timeoutMs,
                        "Code/document mismatch: START_SCAN is posted but the Handler has no "
                                + "START_SCAN branch, so scanning is not started");
            case "H01023":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Insert and tap cards during the search; verify that the magnetic-card listener does not respond", "Remove all test cards");
            case "H01025":
                return CaseProfile.supported(30_000L, "Do not swipe a card; wait for the 1-second timeout callback", "");
            case "H01026":
                return CaseProfile.supportedWithUserAction(330_000L,
                        "Swipe a test magnetic card about 100 seconds after the search starts",
                        "Remove the magnetic card");
            case "H01027":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a test card with damaged magnetic-stripe data", "Remove the magnetic card");
            case "H02001":
                return CaseProfile.supported(30_000L, "Do not swipe a card; verify that stopping is allowed when no search is active", "");
            case "H02002":
                return CaseProfile.supportedWithUserAction(120_000L,
                        "Wait about 10 seconds for the search to stop, then swipe and verify no reader response", "Remove the magnetic card");
            case "H02003":
                return CaseProfile.supported(180_000L,
                        "Wait for 10 search-and-stop cycles and verify that every cycle finishes", "");
            case "H02005":
                return CaseProfile.supportedWithUserAction(60_000L,
                        "Stop twice, then swipe a test card and verify that the reader no longer responds", "Remove the magnetic card");
            case "H03_AUTO":
                return CaseProfile.unsupported(descriptor.timeoutMs,
                        "No workbook case exists and the legacy code reflects DeviceBtMoudle "
                                + "methods onto MagCardReaderMoudle");
            case "H04001":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a card with data on tracks 1, 2, and 3; verify that only track 1 is returned",
                        "Remove the magnetic card");
            case "H04002":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a card with data on tracks 1, 2, and 3; verify that only track 2 is returned",
                        "Remove the magnetic card");
            case "H04003":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a card with data on tracks 1, 2, and 3; verify that only track 3 is returned",
                        "Remove the magnetic card");
            case "H04004":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a card with data on tracks 1, 2, and 3; verify that only tracks 1 and 2 are returned",
                        "Remove the magnetic card");
            case "H04005":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a card with data on tracks 1, 2, and 3; verify that only tracks 1 and 3 are returned",
                        "Remove the magnetic card");
            case "H04006":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Swipe a card with data on tracks 1, 2, and 3; verify that tracks 1, 2, and 3 are returned",
                        "Remove the magnetic card");
            case "H04007":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Set the track mask to 0, swipe a card with data on tracks 1, 2, and 3, "
                                + "and verify that all tracks are returned", "Remove the magnetic card");
            case "H04008":
                return CaseProfile.supportedWithUserAction(90_000L,
                        "Set the track mask to 0b00001000, swipe a card with data on tracks 1, 2, and 3, "
                                + "and verify that all tracks are returned", "Remove the magnetic card");
            default:
                return CaseProfile.unsupported(descriptor.timeoutMs,
                        "No reviewed module-2 contract for " + caseName);
        }
    }

    private static CaseProfile icCardCaseProfile(ModuleDescriptor descriptor, String caseName) {
        switch (caseName) {
            case "I01001":
            case "I02002":
            case "I03002":
            case "I05001":
            case "I07001":
            case "I07002":
            case "I08001":
            case "I09001":
                return CaseProfile.supportedWithUserAction(60_000L,
                        "Insert the contact IC card if it is not already inserted",
                        "");
            case "I03001":
            case "I07003":
                return CaseProfile.supportedWithUserAction(60_000L,
                        "Remove the contact IC card from the slot", "");
            case "I06001":
                return CaseProfile.supported(30_000L,
                        "Leave PSAM slot 1 empty", "");
            case "I10001":
                return CaseProfile.supportedWithUserAction(60_000L,
                        "Remove any inserted contact IC card, then insert and remove it",
                        "");
            default:
                return CaseProfile.unsupported(descriptor.timeoutMs,
                        "No reviewed module-3 contract for " + caseName);
        }
    }

    private String expectedResult(String caseName) {
        String icCardExpected = icCardExpectedResult(caseName);
        if (!icCardExpected.isEmpty()) {
            return icCardExpected;
        }
        int resourceId = context.getResources().getIdentifier(caseName, "string",
                context.getPackageName());
        if (resourceId == 0) {
            return "";
        }
        String description = context.getString(resourceId);
        String marker = "Expected Result:";
        int start = description.indexOf(marker);
        if (start < 0) {
            return "";
        }
        String expected = description.substring(start + marker.length()).trim();
        expected = before(expected, "\nRemarks:");
        expected = before(expected, " Remarks:");
        return expected.trim();
    }

    private static String icCardExpectedResult(String caseName) {
        switch (caseName) {
            case "I01001":
                return "powerUp returns true";
            case "I02002":
                return "powerDown returns true";
            case "I03001":
                return "isCardIn returns false";
            case "I03002":
                return "isCardIn returns true";
            case "I05001":
                return "APDU response status word is 9000";
            case "I06001":
                return "isPSAMCardExists for slot 1 returns false";
            case "I07001":
                return "powered-down inserted-card status is 1";
            case "I07002":
                return "powered-up inserted-card status is 2";
            case "I07003":
                return "removed-card status is 0";
            case "I08001":
                return "powerUp returns true and ATR is non-empty";
            case "I09001":
                return "ATRCheck=true returns a non-empty ATR";
            case "I10001":
                return "status callbacks report 1 after insertion and 0 after removal";
            default:
                return "";
        }
    }

    private static String before(String value, String marker) {
        int index = value.indexOf(marker);
        return index < 0 ? value : value.substring(0, index);
    }

    private static String safeMessage(Throwable error) {
        return error.getMessage() == null ? "no detail" : error.getMessage();
    }

    public ModuleEntry getModule(String id) {
        return modules.get(id);
    }

    public AutomationCase getCase(String id) {
        for (ModuleEntry module : modules.values()) {
            for (AutomationCase automationCase : module.cases) {
                if (automationCase.id.equals(id)) {
                    return automationCase;
                }
            }
        }
        return null;
    }

    public List<ModuleEntry> getModules() {
        return new ArrayList<>(modules.values());
    }

    public List<AutomationCase> getAllCases() {
        List<AutomationCase> result = new ArrayList<>();
        for (ModuleEntry module : modules.values()) {
            result.addAll(module.cases);
        }
        return result;
    }

    public boolean isFullyMapped() {
        List<String> visibleModules = TestActivity.getAutomationModuleIds();
        if (modules.size() != visibleModules.size()) {
            return false;
        }
        for (String moduleId : visibleModules) {
            ModuleEntry module = modules.get(moduleId);
            if (module == null) {
                return false;
            }
            if (module.cases.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public JSONArray toJson() throws JSONException {
        JSONArray array = new JSONArray();
        for (ModuleEntry module : modules.values()) {
            array.put(module.toJson());
        }
        return array;
    }

    public static final class ModuleEntry {
        public final ModuleDescriptor descriptor;
        public final List<AutomationCase> cases;

        ModuleEntry(ModuleDescriptor descriptor, List<AutomationCase> cases) {
            this.descriptor = descriptor;
            this.cases = Collections.unmodifiableList(new ArrayList<>(cases));
        }

        JSONObject toJson() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("id", descriptor.id);
            json.put("name", descriptor.name);
            json.put("risk", descriptor.risk);
            json.put("destructive", "DESTRUCTIVE".equals(descriptor.risk));
            JSONArray caseArray = new JSONArray();
            for (AutomationCase automationCase : cases) {
                caseArray.put(automationCase.toJson());
            }
            json.put("cases", caseArray);
            return json;
        }
    }

    private static final class ModuleDescriptor {
        final String id;
        final String name;
        final String precondition;
        final String risk;
        final long timeoutMs;
        final String manualStep;
        final String cleanupStep;
        final boolean operatorRequired;

        ModuleDescriptor(String id, String name, String precondition, String risk, long timeoutMs,
                         String manualStep, String cleanupStep, boolean operatorRequired) {
            this.id = id;
            this.name = name;
            this.precondition = precondition;
            this.risk = risk;
            this.timeoutMs = timeoutMs;
            this.manualStep = manualStep;
            this.cleanupStep = cleanupStep;
            this.operatorRequired = operatorRequired;
        }
    }

    private static final class CaseProfile {
        final long timeoutMs;
        final String manualStep;
        final String cleanupStep;
        final boolean userActionRequired;
        final boolean supported;
        final String unavailableReason;

        private CaseProfile(long timeoutMs, String manualStep, String cleanupStep,
                            boolean userActionRequired, boolean supported,
                            String unavailableReason) {
            this.timeoutMs = timeoutMs;
            this.manualStep = manualStep;
            this.cleanupStep = cleanupStep;
            this.userActionRequired = userActionRequired;
            this.supported = supported;
            this.unavailableReason = unavailableReason;
        }

        static CaseProfile supported(long timeoutMs, String manualStep, String cleanupStep) {
            return new CaseProfile(timeoutMs, manualStep, cleanupStep, false, true, "");
        }

        static CaseProfile supportedWithUserAction(long timeoutMs, String manualStep,
                                                   String cleanupStep) {
            return new CaseProfile(timeoutMs, manualStep, cleanupStep, true, true, "");
        }

        static CaseProfile unsupported(long timeoutMs, String reason) {
            return new CaseProfile(timeoutMs, "No operator action required", "", false, false, reason);
        }
    }
}
