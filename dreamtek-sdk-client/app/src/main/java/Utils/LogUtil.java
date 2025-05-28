
package Utils;

import android.util.Log;

public class LogUtil {
    private static ILogEntry logEntry = new JavaLogEntry();
    public static final String TAG = LogUtil.class.getSimpleName();
    /**
     * 控制变量，是否显示log日志
     */
    public static boolean isShowLog = true;
    public static String defaultMsg = "";
    public static final int V = Log.VERBOSE;  // 调整常量值，和安卓的一致
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;

    public static int mLogLevel = V;

    //规定每段显示的长度
    private static int MAX_LENGTH_SINGLE_LINE = 2000;

    /**
     * 初始化控制变量
     *
     * @param isShowLog
     */
    public static void init(boolean isShowLog) {
        LogUtil.isShowLog = isShowLog;
    }
    public static void init(int logLevel) {
        Log.i(TAG, "set logging level to " + logLevel );
        LogUtil.mLogLevel = logLevel;
    }

    /**
     * 初始化控制变量和默认日志
     *
     * @param isShowLog
     * @param defaultMsg
     */
    public static void init(boolean isShowLog, String defaultMsg) {
        LogUtil.isShowLog = isShowLog;
        LogUtil.defaultMsg = defaultMsg;
    }

    public static void setLogEntry(ILogEntry logEntry) {
        if (logEntry != null) {
            LogUtil.logEntry = logEntry;
        }
    }

    public static void v() {
        llog(V, null, defaultMsg);
    }

    public static void v(Object obj) {
        llog(V, null, obj);
    }

    public static void v(String tag, Object obj) {
        llog(V, tag, obj);
    }

    public static void d() {
        llog(D, null, defaultMsg);
    }

    public static void d(Object obj) {
        llog(D, null, obj);
    }

    public static void d(String tag, Object obj) {
        llog(D, tag, obj);
    }

    public static void i() {
        llog(I, null, defaultMsg);
    }

    public static void i(Object obj) {
        llog(I, null, obj);
    }

    public static void i(String tag, String obj) {
        llog(I, tag, obj);
    }

    public static void w() {
        llog(W, null, defaultMsg);
    }

    public static void w(Object obj) {
        llog(W, null, obj);
    }

    public static void w(String tag, Object obj) {
        llog(W, tag, obj);
    }

    public static void e() {
        llog(E, null, defaultMsg);
    }

    public static void e(Object obj) {
        llog(E, null, obj);
    }

    public static void e(String tag, Object obj) {
        llog(E, tag, obj);
    }


    /**
     * 执行打印方法
     *
     * @param type
     * @param tagStr
     * @param obj
     */
    public static void llog(int type, String tagStr, Object obj) {
        String msg;
        if (!isShowLog) {
            return;
        }

        if( type < mLogLevel ){
            // 级别不到、不打印
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        if( stackTrace[index].getClassName().endsWith("$Log") ) {
            // 多找一层以适应各个class增加的Log类来打印日志的情况
            ++index;
        }
        // 找到文件名、行号、方法名
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();


        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[(").append(className).append(":").append(lineNumber).append(")(").append(methodName).append(")] ");


        if (obj == null) {
            msg = "";
        } else {
            msg = obj.toString();
        }
        if (msg != null) {
            stringBuilder.append(msg);
        }
        ++index;
        if( index < stackTrace.length ){
            // dump the caller information
            String callerClass = stackTrace[index].getFileName();
            String callerMethod = stackTrace[index].getMethodName();
            int callerLineNumber = stackTrace[index].getLineNumber();
            stringBuilder.append(" | Caller: [(").append(callerClass).append(":").append(callerLineNumber).append(")(").append(callerMethod).append(")] ");
        }


        if (LogUtil.logEntry == null) {
            System.out.println("Info: logEntry is null");
            return;
        }

        String logStr = stringBuilder.toString();
        switch (type) {
            case V:
                Log.v(tag, logStr);
//                LogUtil.logEntry.v(tag, logStr);
                break;
            case D:
                Log.d(tag, logStr);
//                LogUtil.logEntry.d(tag, logStr);
                break;
            case I:
                Log.i(tag, logStr);
//                LogUtil.logEntry.i(tag, logStr);
                break;
            case W:
                Log.w(tag, logStr);
//                LogUtil.logEntry.w(tag, logStr);
                break;
            case E:
                Log.e(tag, logStr);
//                LogUtil.logEntry.e(tag, logStr);
                break;
        }
    }

    public interface ILogEntry {
        void v(String TAG, String logInfo);

        void d(String TAG, String logInfo);

        void i(String TAG, String logInfo);

        void w(String TAG, String logInfo);

        void e(String TAG, String logInfo);
    }

    public static class JavaLogEntry implements ILogEntry {

        @Override
        public void v(String TAG, String logInfo) {
            printIn(TAG, logInfo);
        }

        @Override
        public void d(String TAG, String logInfo) {
            printIn(TAG, logInfo);
        }

        @Override
        public void i(String TAG, String logInfo) {
            printIn(TAG, logInfo);
        }

        @Override
        public void w(String TAG, String logInfo) {
            printIn(TAG, logInfo);
        }

        @Override
        public void e(String TAG, String logInfo) {
            printIn(TAG, logInfo);
        }
    }

    public static void printIn(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            System.out.println(tag + " : " + msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                System.out.println(tag + " : " + logContent);
            }
            System.out.println(tag + " : " + msg);// 打印剩余日志
        }
        // TODO: 2019-03-18 保存到文件
//        if (LogOutPutUtil.isIsOutPutLog()) {
//            LogOutPutUtil.outPutLog(tag + " : " + msg);
//        }
    }
}
