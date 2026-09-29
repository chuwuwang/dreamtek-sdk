package automation;

/** Result produced by a case whose expected outcome can be checked without an operator. */
public final class AutomaticCaseResult {
    public final String status;
    public final String actualResult;

    private AutomaticCaseResult(String status, String actualResult) {
        this.status = status;
        this.actualResult = actualResult;
    }

    public static AutomaticCaseResult pass(String actualResult) {
        return new AutomaticCaseResult("PASS", actualResult);
    }

    public static AutomaticCaseResult fail(String actualResult) {
        return new AutomaticCaseResult("FAIL", actualResult);
    }

    public static AutomaticCaseResult error(String actualResult) {
        return new AutomaticCaseResult("ERROR", actualResult);
    }

    public static AutomaticCaseResult notRun(String actualResult) {
        return new AutomaticCaseResult("NOT_RUN", actualResult);
    }
}
