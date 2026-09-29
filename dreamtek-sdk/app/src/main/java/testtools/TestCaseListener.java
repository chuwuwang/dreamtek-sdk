package testtools;

import java.util.List;

public interface TestCaseListener {
    public void onReceived(List<Testcase> testcases);
}
