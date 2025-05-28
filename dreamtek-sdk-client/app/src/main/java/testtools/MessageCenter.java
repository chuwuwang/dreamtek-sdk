package testtools;

public class MessageCenter {
    private static MessageCenter instance;
    private TestCaseListener testCaseListener;

    public static MessageCenter getInstance() {
        if (instance == null) {
            instance = new MessageCenter();
        }
        return instance;
    }

    public void setTestCaseListener(TestCaseListener testCaseListener) {
        this.testCaseListener = testCaseListener;
    }

    public TestCaseListener getTestCaseListener() {
        return testCaseListener;
    }
}
