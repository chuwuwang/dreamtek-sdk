package automation;

import org.json.JSONException;
import org.json.JSONObject;

/** Immutable metadata and dispatch coordinates for one legacy Tester Test case. */
public final class AutomationCase {
    public final String id;
    public final String moduleId;
    public final String moduleName;
    public final String name;
    public final String api;
    public final String precondition;
    public final String expectedResult;
    public final String risk;
    public final long timeoutMs;
    public final String manualStep;
    public final String cleanupStep;
    public final boolean operatorRequired;
    public final boolean userActionRequired;
    public final boolean supported;
    public final String unavailableReason;
    public final int groupPosition;
    public final int childPosition;

    AutomationCase(String id, String moduleId, String moduleName, String name, String api,
                   String precondition, String expectedResult, String risk, long timeoutMs,
                   String manualStep, String cleanupStep, boolean operatorRequired,
                   boolean userActionRequired, boolean supported,
                   String unavailableReason,
                   int groupPosition, int childPosition) {
        this.id = id;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.name = name;
        this.api = api;
        this.precondition = precondition;
        this.expectedResult = expectedResult;
        this.risk = risk;
        this.timeoutMs = timeoutMs;
        this.manualStep = manualStep;
        this.cleanupStep = cleanupStep;
        this.operatorRequired = operatorRequired;
        this.userActionRequired = userActionRequired;
        this.supported = supported;
        this.unavailableReason = unavailableReason;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("module", moduleId);
        json.put("moduleName", moduleName);
        json.put("name", name);
        json.put("api", api);
        json.put("precondition", precondition);
        json.put("expectedResult", expectedResult);
        json.put("risk", risk);
        json.put("timeoutMs", timeoutMs);
        json.put("manualStep", manualStep);
        json.put("cleanupStep", cleanupStep);
        json.put("operatorRequired", operatorRequired);
        json.put("userActionRequired", userActionRequired);
        json.put("supported", supported);
        if (!supported) {
            json.put("unavailableReason", unavailableReason);
        }
        return json;
    }
}
