package com.dreamtek.systemservice.test.demo.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dreamtek.smartpos.system_service.aidl.IAppDeleteObserver;
import com.dreamtek.smartpos.system_service.aidl.IAppInstallObserver;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.IVerifysignCallback;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import com.dreamtek.systemservice.test.demo.R;
import com.dreamtek.systemservice.test.demo.utils.FileHelper;
import com.dreamtek.systemservice.test.demo.utils.SystemServiceAccess;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class SystemFragment extends Fragment {

    private static ISystemManager systemManager = null;
    private static ISettingsManager settingsManager = null;
    private static INetworkManager iNetworkManager = null;
    private static final String TAG = SystemFragment.class.getSimpleName();
    private final SystemServiceAccess.Listener mSystemServiceListener = new SystemServiceAccess.Listener() {
        @Override
        public void onSystemReady(ISystemManager system, ISysDeviceInfo deviceInfo,
                                  INetworkManager network, ISettingsManager settings) {
            Log.d(TAG, "system service ready");
            systemManager = system;
            settingsManager = settings;
            iNetworkManager = network;
        }

        @Override
        public void onSystemLost() {
            Log.d(TAG, "system service disconnected.");
            systemManager = null;
            // 通知用户服务已断开
            Toast.makeText(getContext(), "System service disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    Button updateK21, updateSecurityDriver, installSelf, installOtherAPK,
            getBrightness, changeBrightness, setLocationMode, killApplication, getLogcat, initLogcat,
            restartApplication, reboot, getLaunchAppsInfo, isAdbMode, installApp,
            updateROM,uninstallApp, isCaInstallEnable, isMaskHomeKey, isMaskStatusBard,isMaskRecentKey;

    EditText etChangeBrightness1, et_setLocationMode, et_updateROM, et_packageName,
            et_signPath, et_installApp, et_updateK21,et_installerPkgName;
    Switch sw_Enable;
    SeekBar swipeSeekBar;
    TextView valueTextView;
    Switch sw_installerpkgName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_system, container, false);


        initParam(view);


        initView();

        view.findViewById(R.id.takeCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap;
                if (systemManager != null) {
                    Log.d(TAG, "takeCapture executed()");
                    try {
                        bitmap = systemManager.takeCapture();
                        Log.d(TAG, "takeCapture succeed. The byte count of bitmap is:" + bitmap.getByteCount());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.d(TAG, "error:takeCapture failed");
                    }
                }
            }
        });
        view.findViewById(R.id.shutdownDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (systemManager != null) {
                    Log.d(TAG, "shutdownDevice executed()");
                    try {
                        systemManager.shutdownDevice();
                        Log.d(TAG, "shutdownDevice succeed...");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        view.findViewById(R.id.isAppForeground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (systemManager != null) {
                    Log.d(TAG, "isAppForeground executed()");
                    try {
                        String packageName = !TextUtils.isEmpty(et_packageName.getText().toString()) ? et_packageName.getText().toString() : null;
                        Log.d(TAG, "the package is[:" + packageName + "]");
                        boolean appForeground = systemManager.isAppForeground(packageName);
                        Toast.makeText(getContext(), "isAppForeground? :" + appForeground, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "isAppForeground? :" + appForeground);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        view.findViewById(R.id.verifySignSync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String filePath = !TextUtils.isEmpty(et_signPath.getText().toString()) ? et_signPath.getText().toString() : "";
                    boolean result = systemManager.verifySignSync(filePath);
                    Toast.makeText(getContext(), "result = " + result, Toast.LENGTH_SHORT).show();

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        view.findViewById(R.id.verifySign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String filePath = !TextUtils.isEmpty(et_signPath.getText().toString()) ? et_signPath.getText().toString() : "";
                    systemManager.verifySign(filePath, new IVerifysignCallback.Stub() {
                        @Override
                        public void onVerifySignResult(final boolean result) throws RemoteException {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "result = " + result, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });


                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return view;
    }

    private void initParam(View view) {
        //绑定id组件
        swipeSeekBar = view.findViewById(R.id.swipeSeekBar);
        installSelf = view.findViewById(R.id.installSelf);
        et_signPath = view.findViewById(R.id.et_signPath);
        installOtherAPK = view.findViewById(R.id.installOtherAPK1);
        updateK21 = view.findViewById(R.id.updateK21);
        updateSecurityDriver = view.findViewById(R.id.updateSecurityDriver);
        getBrightness = view.findViewById(R.id.getBrightness);
        changeBrightness = view.findViewById(R.id.changeBrightness);
        etChangeBrightness1 = (EditText) view.findViewById(R.id.etChangeBrightness1);
        reboot = view.findViewById(R.id.reboot);
        isMaskHomeKey = view.findViewById(R.id.isMaskHomeKey);
        isMaskRecentKey = view.findViewById(R.id.isMaskRecentKey);
        isMaskStatusBard = view.findViewById(R.id.isMaskStatusBard);
        setLocationMode = view.findViewById(R.id.setLocationMode);
        killApplication = view.findViewById(R.id.killApplication);
        restartApplication = view.findViewById(R.id.restartApplication);
        getLaunchAppsInfo = view.findViewById(R.id.getLaunchAppsInfo);
        isAdbMode = view.findViewById(R.id.isAdbMode);
        installApp = view.findViewById(R.id.installApp);
        updateROM = view.findViewById(R.id.updateROM);
        uninstallApp = view.findViewById(R.id.uninstallApp);
        et_setLocationMode = view.findViewById(R.id.et_setLocationMode);
        et_updateROM = view.findViewById(R.id.et_updateROM);
        et_packageName = view.findViewById(R.id.et_packageName);
        et_installApp = view.findViewById(R.id.et_installApp1);
        et_updateK21 = view.findViewById(R.id.et_updateK21);
        sw_Enable = view.findViewById(R.id.sw_Enable);
        initLogcat = view.findViewById(R.id.initLogcat);
        getLogcat = view.findViewById(R.id.getLogcat);
        valueTextView = view.findViewById(R.id.valueTextView);
        et_installerPkgName = view.findViewById(R.id.et_installerPkgName);
        sw_installerpkgName = view.findViewById(R.id.sw_installerpkgName);
    }

    public void initView() {
        installSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installSelf(view);
                Log.d(TAG, "您点击了install_self");
            }
        });

        installOtherAPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(systemManager==null){
                    bindSystemService();
                }
                installOtherAPK(view);
                Log.d(TAG, "您点击了installOtherApk");
            }
        });

        updateK21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateK21(view);
            }
        });

        updateSecurityDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSecurityDriver(view);
            }
        });

        getBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getBrightness(view);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        swipeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int scaledProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Update the displayed value as the seek bar is changed
                valueTextView.setText(String.valueOf(progress) + " %");
                try {
                    scaledProgress = (int) Math.round(progress*2.55);
                    if(scaledProgress < 10){
                        scaledProgress = 10;
                    }
                    Log.d(TAG, "onProgressChanged: progress: " + progress + " - scaledProgress: " + scaledProgress);
                    systemManager.changeScreenBrightness(scaledProgress);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        changeBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    changeBrightness(view);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reboot(view);
            }
        });

        setLocationMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocationMode(view);
            }
        });

        killApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                killApplication(view);
            }
        });

        restartApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartApplication(view);
            }
        });

        getLaunchAppsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLaunchAppsInfo(view);
            }
        });
        isAdbMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdbMode(view);
            }
        });

        installApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installApp(view);
            }
        });
        updateROM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateROM(view);
            }
        });

        uninstallApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = !TextUtils.isEmpty(et_packageName.getText().toString()) ? et_packageName.getText().toString() : null;
                Log.d(TAG, "packageName = " + packageName);
                if (TextUtils.isEmpty(packageName)) {
                    Toast.makeText(getContext(), "请输入包名", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    systemManager.uninstallApp(packageName, new IAppDeleteObserver() {
                        @Override
                        public void onDeleteFinished(String packageName, int returnCode) throws RemoteException {
                            Log.d(TAG, "onDeleteFinished packageName = " + packageName + ". returnCode =" + returnCode);
                            Toast.makeText(SystemFragment.this.getContext(), "packageName = " + packageName + ";returnCode = " + returnCode, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    });
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        isMaskHomeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_Enable.isChecked();
                try {
                    systemManager.isMaskHomeKey(isChecked);
                    Toast.makeText(getContext(), "isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        isMaskRecentKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_Enable.isChecked();
                try {
                    systemManager.isMaskRecentStatusKey(isChecked);
                    Toast.makeText(getContext(), "isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        isMaskStatusBard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_Enable.isChecked();
                try {
                    systemManager.isMaskStatusBard(isChecked);
                    Toast.makeText(getContext(), "isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        initLogcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int logcatBufferSize = 10;  // 设置缓冲区大小为 10 MB 或 10 KB
                int logcatBufferSizeSuffix = 0;  // 0 表示单位为 MB
                Bundle logcatParam = new Bundle();  // 创建空的 Bundle 对象
                try {
                    systemManager.initLogcat(logcatBufferSize, logcatBufferSizeSuffix, logcatParam);
                    Toast.makeText(getContext(), "initLocat成功", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Toast.makeText(getContext(), "initLocat异常" + e, Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        });


        getLogcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String logcat = systemManager.getLogcat(null, 0);
                    Log.d(TAG, "getLogcat executed,the file path is:[" + logcat + "]");
                } catch (RemoteException e) {
                    Log.d(TAG, "getLogcat failed: " + e);
                    throw new RuntimeException(e);
                }
            }
        });
    }
    String installerPkgName;

    public void installOtherAPK(View view) {
        String installerPkgName = "";
        if (sw_installerpkgName.isChecked()){
            Log.d(TAG,"installerPackageName不设置默认包名");
            installerPkgName = !TextUtils.isEmpty(et_installerPkgName.getText().toString()) ? et_installerPkgName.getText().toString() : "";
        }else {
            installerPkgName = getContext().getPackageName();
            Log.d(TAG,"installerPackageName 设为默认"+installerPkgName);
        }
        String path = !TextUtils.isEmpty(et_installApp.getText().toString()) ? et_installApp.getText().toString() : null;
        Log.d(TAG, "installOtherAPK path = " + path);
        Log.d(TAG, "installerPkgName = " + installerPkgName);
        this.installerPkgName = installerPkgName;
//        Toast.makeText(MainActivity.this, "path = " + path, Toast.LENGTH_SHORT).show();
        install(path, installerPkgName);
    }


    private void install(final String path, final String installerPkgName) {
        Log.d(TAG,"install installerPkgName = "+installerPkgName);
        if (systemManager != null) {
            try {
                systemManager.installApp(path, new IAppInstallObserver.Stub() {
                    @Override
                    public void onInstallFinished(String packageName, int returnCode) {
                        Log.d(TAG, "path = " + path + "; packageName = " + packageName);
                        Log.d(TAG, "onInstallFinished returnCode:" + returnCode);
                    }
                }, installerPkgName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        bindSystemService();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        bindSystemService();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        SystemServiceAccess.unbind(mSystemServiceListener);
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        bindSystemService();
    }

    private void bindSystemService() {
        // 通过宿主注入的 Provider 复用统一设备服务连接（新库 IDeviceService.getSystemManager()），
        // 不再直接绑定 com.dreamtek.smartpos.system_service。
        SystemServiceAccess.bind(getContext(), mSystemServiceListener);
    }


    public void installApp(View view) {
        //调用系统文件夹
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.android.package-archive"); // 过滤 APK 文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 100);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 100) {
            return;
        }
        Log.e(TAG, "data = " + data);
        Uri uri = data.getData();
        Log.e(TAG, "uri = " + uri + "；uri.getAuthority():" + uri.getAuthority() + ";getPath = " + uri.getPath());
        String apkPath = null;
        if (Build.MODEL.equals("X990")) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                if (data.toString().toLowerCase().contains("primary")) {
                    // 在 Android 9 以下
                    apkPath = FileHelper.getFileAbsolutePath(this.getContext(), uri);
                    Log.d(TAG, "android7 apkPath = " + apkPath);
                } else {
                    Toast.makeText(this.getContext(), "来自usb", Toast.LENGTH_SHORT).show();
                    apkPath = FileHelper.getFileFromOtgUri(this.getContext(), uri);
                    Log.d(TAG, "android 7 apkpath = " + apkPath);
                }
            } else {
                // 在 Android 9 及以上的设备
                if (uri.getAuthority().equals("com.android.providers.downloads.documents")) {
                    //从SDcard里安装的
                    Toast.makeText(this.getContext(), "来自sdcard/download", Toast.LENGTH_SHORT).show();
                    apkPath = FileHelper.getFileFromContentUri(this.getContext(), uri);
                    Log.d(TAG, "android10 apkPath = " + apkPath);
                } else if (uri.getAuthority().equals("com.android.externalstorage.documents")) {
                    Toast.makeText(this.getContext(), "来自usb", Toast.LENGTH_SHORT).show();
                    apkPath = FileHelper.getFileFromOtgUri(this.getContext(), uri);
                    Log.d(TAG, "android 10 apkpath = " + apkPath);
                } else {
                    Toast.makeText(this.getContext(), "不支持使用此路径安装", Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (systemManager != null) {
            try {
                final String finalApkPath = apkPath;
                systemManager.installApp(apkPath, new IAppInstallObserver.Stub() {
                    @Override
                    public void onInstallFinished(String packageName, int returnCode) {
                        Log.d(TAG, "finalApkPath = " + finalApkPath + "; packageName = " + packageName);
                        Log.d(TAG, "onInstallFinished returnCode:" + returnCode);
                    }

                }, getContext().getPackageName());
                Toast.makeText(SystemFragment.this.getContext(), "installApp执行完毕", Toast.LENGTH_SHORT).show();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    // 根据 URI 获取文件的真实路径
    private String getRealPathFromUri(Uri uri) {
        String filePath = null;
        try (@SuppressLint({"NewApi", "LocalSuppress"})
             Cursor cursor = this.getContext().getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String fileName = cursor.getString(index);
                filePath = this.getContext().getExternalFilesDir(null) + "/" + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public void installSelf(View view) {
        String path = "SystemServiceTestDemo.apk";
        copyFileFromAssets(path);
    }


    private void copyFileFromAssets(String path) {
        Log.d(TAG, "copyFileFromAssets path = " + path);
        String localPath = "/sdcard/" + path;
        Log.d(TAG, "localPath = " + localPath);
        File file = new File(localPath);
        try {
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                InputStream inputStream = getActivity().getAssets().open(path);
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                byte[] data = new byte[1024];
                for (int len; (len = inputStream.read(data)) != -1; ) {
                    os.write(data, 0, len);
                }
                inputStream.close();
                os.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        install(localPath);
    }

//    private void install(final String path) {
//        if (systemManager != null) {
//            try {
//                systemManager.installApp(path, new IAppInstallObserver.Stub() {
//                    @Override
//                    public void onInstallFinished(String packageName, int returnCode) {
//                        Log.d(TAG, "path = " + path + "; packageName = " + packageName);
//                        Log.d(TAG, "onInstallFinished returnCode:" + returnCode);
//                    }
//                }, BuildConfig.APPLICATION_ID);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable checkSystemManagerRunnable;

    private void install(final String path) {
        if (systemManager != null) {
            checkSystemManagerRunnable = new Runnable() {
                @Override
                public void run() {
                    if (systemManager != null) {
                        try {
                            systemManager.installApp(path, new IAppInstallObserver.Stub() {
                                @Override
                                public void onInstallFinished(String packageName, int returnCode) {
                                    Log.d(TAG, "path = " + path + "; packageName = " + packageName);
                                    Log.d(TAG, "onInstallFinished returnCode:" + returnCode);
                                }
                            }, getContext().getPackageName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } finally {
                            // 移除Runnable以避免重复调用
                            handler.removeCallbacks(checkSystemManagerRunnable);
                        }
                    } else {
                        // 继续等待systemManager不为null
                        handler.postDelayed(this, 0);
                    }
                }
            };
        }

        // 开始检查
        handler.postDelayed(checkSystemManagerRunnable, 0);

    }

    public void updateK21(View view) {
        if (systemManager == null) return;
        try {
            String value = !TextUtils.isEmpty(et_updateK21.getText().toString()) ? et_updateK21.getText().toString() : null;
            Log.d(TAG, "updateK21 your input " + value);
            String sysBin = "";
            String appBin = "";
            if (value != null && !value.isEmpty()) {
                String[] configs = value.split(",");
                sysBin = configs[0];
                appBin = configs[1];
            }
            Log.d(TAG, "sysBin = " + sysBin + "; appBin = " + appBin);
            boolean result = systemManager.chekcK21Update(sysBin, appBin);
//                boolean result = systemManager.chekcK21Update("/sdcard/Download/X990_K21_VF_DRIVER_V0.46_sgn.bin", "/sdcard/Download/X990_K21_VF_CORE_V1.93_sgn.bin");
            Toast.makeText(getContext(), "updateK21 result:" + result, Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateSecurityDriver(View view) {
        if (systemManager == null) return;
        try {
            String path = !TextUtils.isEmpty(et_updateROM.getText().toString()) ? et_updateROM.getText().toString() : null;
            Log.d(TAG, "updateSecurityDriver path = " + path);
            boolean result = systemManager.UpdateSecurityDriver(path);

            Toast.makeText(getContext(), "updateSecurityDriver:" + result, Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

//            boolean result = systemManager.UpdateSecurityDriver("/sdcard/Download/X990-V1.0.0(202105271546).vfuup");

    }

    public int getBrightness(View view) throws RemoteException {
        int brightnessData = -1;
        if (systemManager != null) {
            Log.d(TAG, "getBrightness executed()");
            brightnessData = systemManager.getScreenBrightness();
            Log.d(TAG, "The system brightness is:" + brightnessData);
            Toast.makeText(SystemFragment.this.getContext(), "bright = " + brightnessData, Toast.LENGTH_SHORT).show();
            return brightnessData;
        }
        Log.d(TAG, "FALSE:The system brightness cannot be queried");
        return -1;
    }

    public void changeBrightness(View view) throws RemoteException {
        Log.d(TAG, "changeBrightness executed()");
        if (etChangeBrightness1 != null) {
            if (TextUtils.isEmpty(etChangeBrightness1.getText().toString())) {
                Toast.makeText(getContext(), "please enter bright", Toast.LENGTH_SHORT).show();
                return;
            }
            int brightness = Integer.parseInt(etChangeBrightness1.getText().toString());
            Log.d(TAG, "brightness(etChangeBrightness) is:" + brightness);
            if (brightness >= 10 && brightness <= 255) {
                systemManager.changeScreenBrightness(brightness);
            } else {
                Log.d(TAG, "ERROR,OUT OF RANGE");
            }
        }
    }

    public void reboot(View view) {
        if (systemManager != null) {
            try {
                systemManager.reboot();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setLocationMode(View view) {
        if (systemManager != null) {
            try {
                String mode = !TextUtils.isEmpty(et_setLocationMode.getText().toString()) ? et_setLocationMode.getText().toString() : null;
                if (mode != null) {
                    systemManager.setLocationMode(Integer.parseInt(mode));
                    switch (mode) {
                        case "0":
                            Toast.makeText(getContext(), "mode = 0, off", Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            Toast.makeText(getContext(), "mode = 1, sensor only", Toast.LENGTH_SHORT).show();
                            break;
                        case "2":
                            Toast.makeText(getContext(), "mode = 2, battery saving", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(getContext(), "mode = 3, high accuracy", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getContext(), "Invalid error mode: " + mode, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getContext(), "mode is null", Toast.LENGTH_SHORT).show();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void killApplication(View view) {
        if (systemManager != null) {
            try {
                String packageName = !TextUtils.isEmpty(et_packageName.getText().toString()) ? et_packageName.getText().toString() : null;
                boolean killApplication = systemManager.killApplication(packageName);
                Log.d(TAG, "kill package: " + packageName + " ,result:" + killApplication);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void restartApplication(View view) {
        if (systemManager != null) {
            try {
                String packageName = !TextUtils.isEmpty(et_packageName.getText().toString()) ? et_packageName.getText().toString() : null;
                boolean restartApplication = systemManager.restartApplication(packageName);
                Log.d(TAG, "restart package: " + packageName + " ,result:" + restartApplication);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void getLaunchAppsInfo(View view) {
        if (systemManager != null) {
            try {
                Bundle launchAppsInfo = systemManager.getLaunchAppsInfo(0, System.currentTimeMillis());
                Log.d(TAG, "launchAppsInfo=" + launchAppsInfo.getString("UsageStatsList"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void isAdbMode(View view) {
        if (systemManager != null) {
            try {
                Log.d(TAG, "return bool" + systemManager.isAdbMode());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateROM(View view) {
        if (systemManager != null) {
            try {
                String path = !TextUtils.isEmpty(et_updateROM.getText().toString()) ? et_updateROM.getText().toString() : null;
                systemManager.updateROM(path);
                Toast.makeText(getContext(), "path = " + path, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "path = " + path);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Bundle parseInput(int num) {
        if (num == 2) {
            //传两个参数
            String input = !TextUtils.isEmpty(et_signPath.getText().toString()) ? et_signPath.getText().toString() : "";
            String[] parts = input.split(":");
            Bundle bundle = new Bundle();
            if (parts.length == 2) {
                String filePath = parts[0];
                String value = parts[1];

                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    boolean runValue = Boolean.parseBoolean(value);
                    Log.d(TAG, "file path: " + filePath);
                    Log.d(TAG, "runValue Value: " + runValue);
                    bundle.putString("filePath", filePath);
                    bundle.putBoolean("enableDualVerify", runValue);
                    return bundle;
                } else {
                    Log.e(TAG, "Invalid input format.");
                }
            } else {
                Log.e(TAG, "Invalid input format.");
            }
        }
        return null;
    }


//从assets路径复制
private String copyAssetGetFilePath(String fileName){
        //在update ROM调用以下两行
//        String path = copyAssetGetFilePath("X990_INTLv7_ota_Android7_20231230_to_20240222_signed(CRC32_AF3D14E0).zip");
//        systemManager.updateROM(path);

        try{
        File cacheDir=getContext().getCacheDir();
        if(!cacheDir.exists()){
        cacheDir.mkdirs();
        }
        File outFile=new File(cacheDir,fileName);
        if(!outFile.exists()){
        boolean res=outFile.createNewFile();
        if(!res){
        return null;
        }
        }else{
        if(outFile.length()>10){//表示已经写入一次
        return outFile.getPath();
        }
        }
        InputStream is=getContext().getAssets().open(fileName);
        FileOutputStream fos=new FileOutputStream(outFile);
        byte[]buffer=new byte[1024];
        int byteCount;
        while((byteCount=is.read(buffer))!=-1){
        fos.write(buffer,0,byteCount);
        }
        fos.flush();
        is.close();
        fos.close();
        return outFile.getPath();
        }catch(IOException e){
        e.printStackTrace();
        }
        return null;
        }

        }