package testtools;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import Utils.LogUtil;

public class SafeHandler extends Handler {
    private final static String TAG = "SafeHandler";

    private Handler mNestedHandler;

    public SafeHandler(Handler nestedHandler) {

        mNestedHandler = nestedHandler;
    }

    /**

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

        mNestedHandler.handleMessage(msg);
    }
}