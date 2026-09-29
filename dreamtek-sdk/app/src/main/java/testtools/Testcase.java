package testtools;

public class Testcase {
    public String ModuleId;
    public String API;
    public String CaseID;
    public String Describe;
    public String Result;
    public String Parameter;
    public String ExpectResult;
    public String ActualResult;
    public String Status;
    public String Log;
    public String ResultType;
    public String TestClientAPIIntroduction;
    public String AIDLIntroduction;

    public String getModuleId() {
        return ModuleId;
    }

    public void setModuleId(String moduleId) {
        ModuleId = moduleId;
    }

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public String getCaseID() {
        return CaseID;
    }

    public void setCaseID(String caseID) {
        CaseID = caseID;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public String getExpectResult() {
        return ExpectResult;
    }

    public void setExpectResult(String expectResult) {
        ExpectResult = expectResult;
    }

    public String getActualResult() {
        return ActualResult;
    }

    public void setActualResult(String actualResult) {
        ActualResult = actualResult;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLog() {
        return Log;
    }

    public void setLog(String log) {
        Log = log;
    }

    public String getResultType() {
        return ResultType;
    }

    public void setResultType(String resultType) {
        ResultType = resultType;
    }

    public String getTestClientAPIIntroduction() {
        return TestClientAPIIntroduction;
    }

    public void setTestClientAPIIntroduction(String testClientAPIIntroduction) {
        TestClientAPIIntroduction = testClientAPIIntroduction;
    }

    public String getAIDLIntroduction() {
        return AIDLIntroduction;
    }

    public void setAIDLIntroduction(String AIDLIntroduction) {
        this.AIDLIntroduction = AIDLIntroduction;
    }

    @Override
    public String toString() {
        return "Testcase{" +
                "ModuleId='" + ModuleId + '\'' +
                ", API='" + API + '\'' +
                ", CaseID='" + CaseID + '\'' +
                ", Describe='" + Describe + '\'' +
                ", Result='" + Result + '\'' +
                ", Parameter='" + Parameter + '\'' +
                ", ExpectResult='" + ExpectResult + '\'' +
                ", ActualResult='" + ActualResult + '\'' +
                ", Status='" + Status + '\'' +
                ", Log='" + Log + '\'' +
                ", ResultType='" + ResultType + '\'' +
                ", TestClientAPIIntroduction='" + TestClientAPIIntroduction + '\'' +
                ", AIDLIntroduction='" + AIDLIntroduction + '\'' +
                '}';
    }
}
