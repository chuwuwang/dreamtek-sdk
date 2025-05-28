package testtools;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import Utils.LogUtil;

public class SafeHandler extends Handler {
    private final static String TAG = "SafeHandler";
    //用来保存Tn原有handler
    private Handler mNestedHandler;

    public SafeHandler(Handler nestedHandler) {
        //构造方法里将Tn原有Handler传入
        mNestedHandler = nestedHandler;
    }

    /**
     * handleMessage是在dispatchMessage里被调用的，所以在这里捕获异常就可以
     *
     * @param msg
     */
    @Override
    public void dispatchMessage(Message msg) {
        try {
            super.dispatchMessage(msg);
        } catch (WindowManager.BadTokenException e) {
            LogUtil.d(TAG, "BadTokenException");
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        //交由原有Handler处理
        mNestedHandler.handleMessage(msg);
    }
}