package com.dreamtek.systemservice.test.demo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        Log.d(TAG,"AppReceiver onReceive action = "+action);
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
//        Log.d(TAG, "onReceive: packageName = "+packageName);
        if (action != null) {
            switch (action) {
                case Intent.ACTION_PACKAGE_ADDED:
                    // 应用被安装
                    if (packageName.equals("com.dreamtek.smartpos.system_service"))
                        Log.d(TAG, "SystemService installed: " + packageName);
                    break;
                case Intent.ACTION_PACKAGE_REMOVED:
                    // 应用被卸载
                    String packageNameRemoved = intent.getData().getEncodedSchemeSpecificPart();
                    if (packageNameRemoved.equals("com.dreamtek.smartpos.system_service"))
                        Log.d(TAG, "SystemService uninstalled: " + packageNameRemoved);
                    break;
                case Intent.ACTION_PACKAGE_REPLACED:
                    // 应用被替换
                    String packageReplace = intent.getData().getEncodedSchemeSpecificPart();
                    if (packageReplace.equals("com.dreamtek.smartpos.system_service"))
                        Log.d(TAG, "SystemService replaced: " + packageReplace);
                    break;
            }
        }
    }
}
