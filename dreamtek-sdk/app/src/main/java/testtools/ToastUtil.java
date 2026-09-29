package testtools;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.reflect.Field;

import Utils.LogUtil;
import base.MyApplication;

/**
 * Created by CuncheW1 on 2017/3/16.
 */

public class ToastUtil {
    private final static String TAG = "ToastUtil";

    // Toast.LENGTH_LONG = 3.5s
    // Toast.LENGTH_SHORT = 2s
    // fix Redmine issue #4532 - notifications should be displayed for 6 seconds only
    // https://redmine.verifone.cn/redmine/issues/4532
    public static void showToastLong(final String msg) {
        show(msg, Toast.LENGTH_LONG);
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showToastShort(final String msg) {
        // fix issue - https://redmine.verifone.cn/redmine/issues/3852

        show(msg, Toast.LENGTH_LONG);
        show(msg, Toast.LENGTH_SHORT);
    }

    private static Toast toast = null;

    public static void show(final String message, final int duration) {
//      handler.removeCallbacks(runnable);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {


//                    if (toast != null) {
//                        toast.setText(message);
//                        toast.setDuration(Toast.LENGTH_LONG);
//                    } else {
//                        initToast(message, Toast.LENGTH_LONG);
//                    }
                    if (TextUtils.isEmpty(message) || TextUtils.isEmpty(message.trim())){
                        return;
                    }
                    initToast(message, duration);
                    toast.show();
                } catch (Exception e) {
                    LogUtil.d(TAG, "UnExcept exception when show toast : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    private static void initToast(String message, final int duration) throws Exception {
        toast = Toast.makeText(MyApplication.getContext(), message, duration);


        // @see https://blog.csdn.net/jungle_pig/article/details/83550300
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1 && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Field tnField = Toast.class.getDeclaredField("mTN");
            tnField.setAccessible(true);
            Object mTn = tnField.get(toast);
            Field handlerField = mTn.getClass().getDeclaredField("mHandler");
            handlerField.setAccessible(true);
            Handler handlerOfTn = (Handler) handlerField.get(mTn);
            handlerField.set(mTn, new SafeHandler(handlerOfTn));
        }
    }
}
