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
        // 文档中说 错误提示要显示10秒， 一个3.5秒，所以直接写死，调三次
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
                    // 现在因为 https://redmine.verifone.cn/redmine/issues/3852
                    // 无法控制 toast的重复显示问题， 先注掉
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

        // 解决Android 7.1 之后 BadTokenException 的问题 , Android 8.0 之后, 系统源码已修复
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
