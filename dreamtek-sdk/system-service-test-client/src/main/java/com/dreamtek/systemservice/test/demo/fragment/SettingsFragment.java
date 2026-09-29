package com.dreamtek.systemservice.test.demo.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ICustomerConfigurationUpdateListener;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;

import com.dreamtek.smartpos.system_service.aidl.settings.SettingsActions;
import com.dreamtek.smartpos.system_service.aidl.settings.SettingsType;
import com.dreamtek.systemservice.test.demo.R;
import com.dreamtek.systemservice.test.demo.utils.ImageLoader;
import com.dreamtek.systemservice.test.demo.utils.SystemServiceAccess;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class SettingsFragment extends Fragment {


    private static ISettingsManager settingsManager = null;
    private static ISystemManager systemManager = null;
    private static INetworkManager iNetworkManager = null;
    private int autoSystemTimeState = 1;
    private int autoSystemTimeZoneState = 1;
    private static final String TAG = SettingsFragment.class.getSimpleName();
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
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 101;


    Button getAutoSystemTime, getAutoSystemTimeZone, setAutoSystemTime, setAutoSystemTimeZone, setTimeZone, setSleepDuration,
            settingsSetActions, setSystemTime, setLauncher, upgradeCustomPackages,
            load_CaCertificate, setWallpaper, setSettingPwd, setScreenSleep, enableAlertWindow, jumpToDialog,
            settingPCIRebootTime, getPCIRebootTime, setDefaultSystemLanguage, clearCachesByPackageName,
            getSleepDuration, settingsReadActions, set_battery_percent, setScreenLock, isScreenLock,setDeviceBrightnessLevel;
    EditText etSettingPwd1, etSetLauncher, et_loadCertificate, et_upgradeCustomPackages,
            et_setDefaultSystemLanguage, et_clearCachesByPackageName, et_setPCIRebootTime,
            et_enableAlertWindow, et_settingsSetActions, et_setTimeZone, et_setWallpaper, et_tf_enable,
            et_power_optimize, et_launcher_action, et_launcher_pkg, et_launcher_run, et_power_op_enable, et_system_time, et_system_time_zone, et_system_time_date,et_brightnessLevel,edt_duration;
    RadioGroup rg_settingsSetActions, rg_time_zone;
    RadioButton rg_setlauncher, rg_datatime, rg_power_optimize, rg_tfenable, time_zone_state, time_state;
    LinearLayout option1Layout, option2Layout, option3Layout, option4Layout;
    Switch sw_settingEnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        bindSystemService();
        initParam(view);
        initView();
        return view;
    }


    public void initView() {
        getAutoSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAutoSystemTime(view);
            }
        });
        getAutoSystemTimeZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAutoSystemTimeZone(view);
            }
        });
        setAutoSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAutoSystemTime(view);
            }
        });
        setAutoSystemTimeZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAutoSystemTimeZone(view);
            }
        });
        setTimeZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mySetTimeZone(view);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        setSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSystemTime(view);
            }
        });
        setLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLauncher();
            }
        });

        load_CaCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_CaCertificate(view);
            }
        });
        setSettingPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSettingPwd(view);
            }
        });
        settingPCIRebootTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingPCIRebootTime(view);
            }
        });
        getPCIRebootTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPCIRebootTime(view);
            }
        });

        setScreenSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    settingsManager.setScreenSleep();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        upgradeCustomPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packagePath = !TextUtils.isEmpty(et_upgradeCustomPackages.getText().toString()) ? et_upgradeCustomPackages.getText().toString() : null;
                try {
                    settingsManager.upgradeCustomPackages(packagePath, new ICustomerConfigurationUpdateListener.Stub() {
                        @Override
                        public void onError(int errCode, String msg) throws RemoteException {
                            Toast.makeText(getContext(), "errorCode = " + errCode + "; msg = " + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        enableAlertWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsManager != null) {
                    String packageName = !TextUtils.isEmpty(et_enableAlertWindow.getText().toString()) ? et_enableAlertWindow.getText().toString() : null;
                    try {
                        settingsManager.enableAlertWindow(packageName);
                        Toast.makeText(getContext(), packageName, Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        jumpToDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOverlayPermissionAndShowDialog();

            }
        });

        setDefaultSystemLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultSystemLanguage();
            }
        });
        clearCachesByPackageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = !TextUtils.isEmpty(et_clearCachesByPackageName.getText().toString()) ? et_clearCachesByPackageName.getText().toString() : null;
                try {
                    Log.d(TAG, "清除本包名的应用缓存数据 packageName = " + packageName);
                    settingsManager.clearCachesByPackageName(packageName);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        settingsSetActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsSetActions(view);
            }
        });

        settingsReadActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsReadActions(view);
            }
        });
        rg_settingsSetActions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                rg_settingsSetActions(radioGroup, id);
            }
        });
        rg_time_zone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton radioButton = (RadioButton) getView().findViewById(id);
                String value = radioButton.getText().toString();
                et_system_time.setVisibility(View.GONE);
                et_system_time_zone.setVisibility(View.GONE);
                et_system_time_date.setVisibility(View.GONE);
                // library 模块的 R.id 非 final，不能用于 switch，改为 if-else（逻辑不变）
                if (id == R.id.time_state) {
                    et_system_time.setVisibility(View.VISIBLE);
                } else if (id == R.id.time_zone_state) {
                    et_system_time_zone.setVisibility(View.VISIBLE);
                    et_system_time_date.setVisibility(View.VISIBLE);
                }
            }
        });
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(view);
            }
        });
        setSleepDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSleepDuration(view);
            }
        });
        getSleepDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int result = settingsManager.getSleepDuration();
                    Toast.makeText(getContext(), "getSleepDuration = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        set_battery_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_battery_percent();
            }
        });
        setScreenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenLock();
            }
        });
        isScreenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean result = settingsManager.isScreenLock();
                    Toast.makeText(getContext(), "isScreenLock = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        setDeviceBrightnessLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String string = et_brightnessLevel.getText().toString();
                    if (TextUtils.isEmpty(string)) {
                        Toast.makeText(getContext(), "请输入亮度值，0-255", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int level = Integer.parseInt(string);
                    if (level < 0 || level > 255) {
                        Toast.makeText(getContext(), "请输入正确的亮度值", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean result = settingsManager.setDeviceBrightnessLevel(level);
                    Toast.makeText(getContext(), "setDeviceBrightnessLevel = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Log.d(TAG, "setDeviceBrightnessLevel 异常: " + e);
                    Toast.makeText(getContext(), "请输入正确的亮度值", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void settingsSetActions(View view) {

        int id = rg_settingsSetActions.getCheckedRadioButtonId();
        int settingType = -1;
        Bundle bundle = new Bundle();
        // library 模块的 R.id 非 final，不能用于 switch，改为 if-else（逻辑不变）
        if (id == R.id.rg_datatime) {
                settingType = SettingsType.DATE_TIME;
                int time_id = rg_time_zone.getCheckedRadioButtonId();
                String SYSTEM_TIME_ACTIONS = "";
                if (rg_time_zone.getCheckedRadioButtonId() == R.id.time_state) {
                    SYSTEM_TIME_ACTIONS = "SET_AUTO_SYSTEM_TIME_STATE";
                } else if (rg_time_zone.getCheckedRadioButtonId() == R.id.time_zone_state) {
                    SYSTEM_TIME_ACTIONS = "SET_AUTO_SYSTEM_TIME_ZONE_STATE";
                }

                String AUTO_SYSTEM_TIME1 = !TextUtils.isEmpty(et_system_time.getText().toString()) ? et_system_time.getText().toString() : "";
                String AUTO_SYSTEM_TIME_ZONE1 = !TextUtils.isEmpty(et_system_time_zone.getText().toString()) ? et_system_time_zone.getText().toString() : "";
                String systemTimeData = !TextUtils.isEmpty(et_system_time_date.getText().toString()) ? et_system_time_date.getText().toString() : "";
                String[] parts = systemTimeData.split("_");
                String SYSTEM_TIME = "", SYSTEM_DATE = "";
                if (parts.length == 2) {
                    SYSTEM_TIME = parts[0];
                    SYSTEM_DATE = parts[1];
                    Log.d(TAG, "SYSTEM_TIME = " + SYSTEM_TIME + "; SYSTEM_DATE = " + SYSTEM_DATE);
                }


                if (!SYSTEM_TIME_ACTIONS.isEmpty()) {
                    bundle.putString("SYSTEM_TIME_ACTIONS", SYSTEM_TIME_ACTIONS);
                }
                if (!AUTO_SYSTEM_TIME1.isEmpty()) {
                    int AUTO_SYSTEM_TIME = Integer.parseInt(AUTO_SYSTEM_TIME1);
                    bundle.putInt("AUTO_SYSTEM_TIME", AUTO_SYSTEM_TIME);
                }
                if (!AUTO_SYSTEM_TIME_ZONE1.isEmpty()) {
                    int AUTO_SYSTEM_TIME_ZONE = Integer.parseInt(AUTO_SYSTEM_TIME_ZONE1);
                    bundle.putInt("AUTO_SYSTEM_TIME_ZONE", AUTO_SYSTEM_TIME_ZONE);
                }
                if (!SYSTEM_TIME.isEmpty()) {
                    bundle.putString("SYSTEM_TIME", SYSTEM_TIME);
                }
                if (!SYSTEM_DATE.isEmpty()) {
                    bundle.putString("SYSTEM_DATE", SYSTEM_DATE);
                }

                Log.d(TAG, "your input bundle = " + bundle);

                try {
                    int result = settingsManager.settingsSetActions(settingType, bundle);
                    Log.d(TAG, "result = " + result);
                    Toast.makeText(getContext(), "result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

        } else if (id == R.id.rg_setlauncher) {
                settingType = SettingsType.LAUNCHER;
                String launcheractions = !TextUtils.isEmpty(et_launcher_action.getText().toString()) ? et_launcher_action.getText().toString() : "SET_LAUNCHER";
                String launcherPackage = !TextUtils.isEmpty(et_launcher_pkg.getText().toString()) ? et_launcher_pkg.getText().toString() : null;
                String launcherRun = !TextUtils.isEmpty(et_launcher_run.getText().toString()) ? et_launcher_run.getText().toString() : "";
                Log.d(TAG, "launcheractions = " + launcheractions + "; launcherPackage = " + launcherPackage + "; launcherRun = " + launcherRun);
                bundle.putString("LAUNCHER_ACTIONS", launcheractions);
                bundle.putString("LAUNCHER_PACKAGE_NAME", launcherPackage);
                if ("true".equals(launcherRun)) {
                    bundle.putBoolean("RUN_PACKAGE", true);
                } else if ("false".equals(launcherRun)) {
                    bundle.putBoolean("RUN_PACKAGE", false);
                } else if (launcherRun.isEmpty()) {
                    Toast.makeText(getContext(), "RUN_PACKAGE为空，不传入本参数", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "RUN_PACKAGE格式不正确，不传入本参数", Toast.LENGTH_SHORT).show();
                }

                try {
                    int result = settingsManager.settingsSetActions(settingType, bundle);
                    Toast.makeText(getContext(), "set launcher result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
        } else if (id == R.id.rg_power_optimize) {
                settingType = SettingsType.POWER_OPTIMIZE;
                String packageName = !TextUtils.isEmpty(et_power_optimize.getText().toString()) ? et_power_optimize.getText().toString() : "";
                String enable = !TextUtils.isEmpty(et_power_op_enable.getText().toString()) ? et_power_op_enable.getText().toString() : null;
                Log.d(TAG, "packageName = " + packageName + "; enable = " + enable);
                if ("true".equals(enable)) {
                    bundle.putBoolean("POWER_OPTIMIZE_ENABLE", true);
                } else if ("false".equals(enable)) {
                    bundle.putBoolean("POWER_OPTIMIZE_ENABLE", false);
                } else {
                    Toast.makeText(getContext(), "enable格式不正确，请输入true/false，不设置该value", Toast.LENGTH_SHORT).show();
                }
                bundle.putString("POWER_OPTIMIZE_PACKAGE", packageName);
                try {
                    int result = settingsManager.settingsSetActions(settingType, bundle);
                    Toast.makeText(getContext(), "result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
        } else if (id == R.id.rg_tfenable) {
                settingType = SettingsType.TF_ENABLE;
                String value = !TextUtils.isEmpty(et_tf_enable.getText().toString()) ? et_tf_enable.getText().toString() : null;
                if ("true".equals(value)) {
                    bundle.putBoolean("TF_ENABLE", true);
                } else if ("false".equals(value)) {
                    bundle.putBoolean("TF_ENABLE", false);
                } else {
                    Toast.makeText(getContext(), "TF_ENABLE格式不正确，请输入true/false，不设置该value", Toast.LENGTH_SHORT).show();
                }
                try {
                    int result = settingsManager.settingsSetActions(settingType, bundle);
                    Toast.makeText(getContext(), "enable = " + value + " , result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    private void settingsReadActions(View view) {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            if (rg_time_zone.getCheckedRadioButtonId() == R.id.time_state) {
                String SYSTEM_TIME_ACTIONS = SettingsActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_STATE;
                bundle.putString("SYSTEM_TIME_ACTIONS", SYSTEM_TIME_ACTIONS);
                try {
                    Bundle resultBundle = settingsManager.settingsReadActions(SettingsType.DATE_TIME, bundle);
                    autoSystemTimeZoneState = resultBundle.getInt("AUTO_SYSTEM_TIME");
                    Toast.makeText(getContext(), "autoSystemTimeZoneState = " + autoSystemTimeZoneState + "; state is " + (autoSystemTimeZoneState == 1 ? "sync" : "disable sync"), Toast.LENGTH_LONG).show();

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else if (rg_time_zone.getCheckedRadioButtonId() == R.id.time_zone_state) {
                String SYSTEM_TIME_ACTIONS = SettingsActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_ZONE_STATE;
                bundle.putString("SYSTEM_TIME_ACTIONS", SYSTEM_TIME_ACTIONS);
                try {
                    Bundle resultBundle = settingsManager.settingsReadActions(SettingsType.DATE_TIME, bundle);
                    autoSystemTimeState = resultBundle.getInt("AUTO_SYSTEM_TIME_ZONE");
                    Toast.makeText(getContext(), "autoSystemTimeState = " + autoSystemTimeState + "state is " + (autoSystemTimeState == 1 ? "sync" : "disable sync"), Toast.LENGTH_LONG).show();

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void setSleepDuration(View view) {
        try {
            String text = edt_duration.getText().toString();
            int duration = 0;
            if (!TextUtils.isEmpty(text)) {
                duration = Integer.parseInt(text);
            }
            if (duration < 10000 || duration > 2147483646) {
                Toast.makeText(getContext(), "请输入正确的休眠时间", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "setSleepDuration duration is: " + duration);
            settingsManager.setSleepDuration(duration);
        } catch (RemoteException e) {
            Log.d(TAG, "setSleepDuration 异常: " + e);
            throw new RuntimeException(e);
        }
    }


    private void setScreenLock() {
        boolean isChecked = sw_settingEnable.isChecked();
        try {
            settingsManager.setScreenLock(isChecked);
            Toast.makeText(getContext(), "isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    private void set_battery_percent() {
        boolean isChecked = sw_settingEnable.isChecked();
        try {
            boolean result = settingsManager.isShowBatteryPercent(isChecked);
            Toast.makeText(getContext(), "isChecked = " + isChecked + "; result = " + result, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    //Try this dialog
    static class GlobalDialog {
        private static ProgressDialog dialog = null;
        private static Handler handler = new Handler();

        public static void show(Context context, String message, DialogInterface.OnShowListener listener) {
            dialog = new ProgressDialog(context);
            dialog.setMessage(message);
            dialog.setCancelable(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            }

            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnShowListener(listener);
            dialog.show();
            Log.d("GlobalDialog", "dialog is showing");

            // Set a timer to close the dialog after 5 seconds
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            }, 5000);
        }

        public static void hide() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        public static boolean isShowing() {
            return dialog != null && dialog.isShowing();
        }
    }

    private void displayGlobalDialog() {
        GlobalDialog.show(getContext(), "This is a global dialog", new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.d(TAG, "onShow executed");
            }
        });
    }

    private void checkOverlayPermissionAndShowDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
            } else {
                displayGlobalDialog();
            }
        } else {
            displayGlobalDialog();
        }
    }

    private void rg_settingsSetActions(RadioGroup radioGroup, int id) {
        RadioButton radioButton = (RadioButton) getView().findViewById(id);
        String value = radioButton.getText().toString();
        option1Layout.setVisibility(View.GONE);
        option2Layout.setVisibility(View.GONE);
        option3Layout.setVisibility(View.GONE);
        option4Layout.setVisibility(View.GONE);
        settingsReadActions.setVisibility(View.GONE);
        // library 模块的 R.id 非 final，不能用于 switch，改为 if-else（逻辑不变）
        if (id == R.id.rg_datatime) {
            option1Layout.setVisibility(View.VISIBLE);
            settingsReadActions.setVisibility(View.VISIBLE);
        } else if (id == R.id.rg_setlauncher) {
            option2Layout.setVisibility(View.VISIBLE);
        } else if (id == R.id.rg_power_optimize) {
            option3Layout.setVisibility(View.VISIBLE);
        } else if (id == R.id.rg_tfenable) {
            option4Layout.setVisibility(View.VISIBLE);
        }
    }

    private void setDefaultSystemLanguage() {
        if (null != settingsManager) {
            String input = !TextUtils.isEmpty(et_setDefaultSystemLanguage.getText().toString()) ? et_setDefaultSystemLanguage.getText().toString() : null;
            String language = "";
            String country = "";
            if (null != input) {
                String[] parts = input.split("_");
                if (parts.length == 2) {
                    language = parts[0];
                    country = parts[1];
                } else if (parts.length == 1) {
                    language = parts[0];
                }
            }
            try {
                Log.d(TAG, "language = " + language + ";country = " + country);
                settingsManager.setDefaultSystemLanguage(language, country);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setWallpaper(View view) {
        ImageLoader imageLoader = new ImageLoader();
        Bitmap bitmap = null;
        String input = !TextUtils.isEmpty(et_setWallpaper.getText().toString()) ? et_setWallpaper.getText().toString() : "";
        if (!input.isEmpty()) {
            String[] parts = input.split("/");
            int len = parts.length;
            String pictureName = parts[len - 1];
            Log.d(TAG, "path = " + input + ";pictureName = " + pictureName);

//            if (input.startsWith("/sdcard/Download/")) {
//                // 从/sdcard/Download/加载
//                bitmap = imageLoader.getBitmapFromSdCard(pictureName);
//            } else {
//                bitmap = imageLoader.getBitmapFromUsb(pictureName);
//            }
            bitmap = BitmapFactory.decodeFile(input);


        } else {
            Toast.makeText(getContext(), "input null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bitmap != null) {
            Log.d(TAG, bitmap.getHeight() + " * " + bitmap.getWidth());
        } else {
            Log.d(TAG, "bitmap is null");
        }

//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("wallpaper1", "drawable", getContext().getPackageName()));
//                Log.e("let", bitmap.getHeight() + " * " + bitmap.getWidth());
        try {
            boolean flag = settingsManager.setWallpaper(bitmap);
            Log.d(TAG, "set wallpaper result = " + flag);
            Toast.makeText(SettingsFragment.this.getContext(), "flag = " + flag, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getContext())) {
                    displayGlobalDialog();
                } else {
                    Toast.makeText(getContext(), "Permission denied to draw overlays", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void bindSystemService() {
        // 通过宿主注入的 Provider 复用统一设备服务连接（新库 IDeviceService.getSystemManager()），
        // 不再直接绑定 com.dreamtek.smartpos.system_service。
        SystemServiceAccess.bind(getContext(), mSystemServiceListener);
    }

    @Override
    public void onDestroy() {
        SystemServiceAccess.unbind(mSystemServiceListener);
        super.onDestroy();
    }

    public void getAutoSystemTime(View view) {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_STATE);
            try {
                Bundle resultBundle = settingsManager.settingsReadActions(SettingsType.DATE_TIME, bundle);
                autoSystemTimeState = resultBundle.getInt("AUTO_SYSTEM_TIME", 0);

                Toast.makeText(getContext(), "get system time auto state is " + (autoSystemTimeState == 1 ? "sync" : "disable sync"), Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAutoSystemTimeZone(View view) {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_ZONE_STATE);
            try {
                Bundle resultBundle = settingsManager.settingsReadActions(SettingsType.DATE_TIME, bundle);
                autoSystemTimeZoneState = resultBundle.getInt("AUTO_SYSTEM_TIME_ZONE", 0);

                Toast.makeText(getContext(), "get system time zone auto state is " + (autoSystemTimeZoneState == 1 ? "sync" : "disable sync"), Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    //After selecting the time zone, click the button to set the time zone
    public void mySetTimeZone(View view) throws RemoteException {
        if (settingsManager != null) {
            String timeZone = !TextUtils.isEmpty(et_setTimeZone.getText().toString()) ? et_setTimeZone.getText().toString() : null;
            Log.d(TAG, "timeZone = " + timeZone);
            settingsManager.setTimeZone(timeZone);
        }
    }

    public void setAutoSystemTime(View view) {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.SET_AUTO_SYSTEM_TIME_STATE);
            autoSystemTimeState = autoSystemTimeState == 1 ? 0 : 1;
            bundle.putInt("AUTO_SYSTEM_TIME", autoSystemTimeState);
            try {
                int result = settingsManager.settingsSetActions(SettingsType.DATE_TIME, bundle);
                Toast.makeText(getContext(), "set settings system time auto " + (autoSystemTimeState == 1 ? "sync " : "disable sync ") + (result == 0 ? "success" : "fail"), Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAutoSystemTimeZone(View view) {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.SET_AUTO_SYSTEM_TIME_ZONE_STATE);
            autoSystemTimeZoneState = autoSystemTimeZoneState == 1 ? 0 : 1;
            bundle.putInt("AUTO_SYSTEM_TIME_ZONE", autoSystemTimeZoneState);
            try {
                int result = settingsManager.settingsSetActions(SettingsType.DATE_TIME, bundle);
                Toast.makeText(getContext(), "set settings system time zone auto " + (autoSystemTimeZoneState == 1 ? "sync " : "disable sync ") + (result == 0 ? "success" : "fail"), Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSystemTime(View view) {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.SET_SYSTEM_TIME);
            String date = "20200602";
            String time = "150629";
            bundle.putString("SYSTEM_DATE", date);
            bundle.putString("SYSTEM_TIME", time);
            try {
                int result = settingsManager.settingsSetActions(SettingsType.DATE_TIME, bundle);
                Toast.makeText(getContext(), "set settings system time to " + date + " " + time + " " + (result == 0 ? "success" : "fail"), Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void settingPCIRebootTime(View view) {
        String input = !TextUtils.isEmpty(et_setPCIRebootTime.getText().toString()) ? et_setPCIRebootTime.getText().toString() : null;
        if (input != null) {
            String[] timeParts = input.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            int sec = Integer.parseInt(timeParts[2]);
            try {
                settingsManager.settingPCIRebootTime(hour, min, sec);
                Log.d(TAG, "测试重启时间：" + hour + ":" + min + ":" + sec);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "input = null", Toast.LENGTH_SHORT).show();
        }

    }

    public void getPCIRebootTime(View view) {
        if (settingsManager != null) {
            try {
                long rebootTime = settingsManager.getPCIRebootTime();
                Log.d(TAG, "重启时间获取" + rebootTime);
                String formatTime = formatSecondsToTime(rebootTime);
                Log.d(TAG, "settingManager.getPCIRebootTime = " + settingsManager.getPCIRebootTime() + ";格式化时间string=" + formatTime);
                Toast.makeText(getContext(), "return:" + rebootTime + ";格式化后：" + formatTime, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    String formatSecondsToTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    public void load_CaCertificate(View view) {
        //   boolean ret = settingsManager.loadCertificate("/sdcard/testCA.cer");
        try {
            String certPath = !TextUtils.isEmpty(et_loadCertificate.getText().toString()) ? et_loadCertificate.getText().toString() : null;
            boolean ret = settingsManager.loadCertificate(certPath);
            Log.d(TAG, "load CA:" + ret);
            Toast.makeText(getContext(), "load CA result = " + ret, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setSettingPwd(View view) {
        try {
            boolean ret = settingsManager.setSettingPwd(!TextUtils.isEmpty(etSettingPwd1.getText().toString()) ? etSettingPwd1.getText().toString() : "");
            Toast.makeText(SettingsFragment.this.getContext(), "result:" + ret, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "result:" + ret + ";password:" + etSettingPwd1.getText().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setLauncher() {
        if (settingsManager != null) {
            Bundle bundle = new Bundle();
            String packageName;
            boolean runValue = true;
            int result;
            bundle.putString("LAUNCHER_ACTIONS", SettingsActions.LauncherActions.SET_LAUNCHER);
            String launcherstr = !TextUtils.isEmpty(etSetLauncher.getText().toString()) ? etSetLauncher.getText().toString() : "";
            Log.d(TAG, "launcher input string = " + launcherstr);

            bundle = parseLauncherInput(launcherstr);
            Log.d(TAG, "bundle = " + bundle);
            if (null != bundle) {
                Log.d(TAG, "bundle != null");
                packageName = bundle.getString("packageName");
                runValue = bundle.getBoolean("runValue");
                bundle.putString("LAUNCHER_PACKAGE_NAME", packageName);
                bundle.putBoolean("RUN_PACKAGE", runValue);
                Log.d(TAG, "packageName = " + packageName);
                try {
                    result = settingsManager.settingsSetActions(SettingsType.LAUNCHER, bundle);
                    Log.d(TAG, "set launcher result is " + ((result == 0) ? "Success" : "fail" + "; result = " + result));
                    Toast.makeText(getContext(), " set launcher result is " + (result == 0 ? "success" : "fail"), Toast.LENGTH_LONG).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "bundle = null", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private Bundle parseLauncherInput(String input) {
        String[] parts = input.split(":");
        Bundle bundle = new Bundle();
        if (parts.length == 2) {
            String packageName = parts[0];
            String value = parts[1];

            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                boolean runValue = Boolean.parseBoolean(value);
                Log.d(TAG, "Package Name: " + packageName);
                Log.d(TAG, "runValue Value: " + runValue);
                bundle.putString("packageName", packageName);
                bundle.putBoolean("runValue", runValue);
                return bundle;
            } else if (value.isEmpty()) {

                Log.d(TAG, "Package Name: " + packageName);
                Log.d(TAG, "runValue Value=null, set default = true ");
                bundle.putString("packageName", packageName);
                bundle.putBoolean("runValue", true);
                return bundle;
            } else {
                Log.e(TAG, "Invalid input format.");
            }
        } else {
            Log.e(TAG, "Invalid input format.");
        }
        return null;
    }


    /**
     * settingsSetActions 入参格式转化
     * 入参格式为 "LAUNCHER_ACTIONS=SET_LAUNCHER=string,LAUNCHER_PACKAGE_NAME=com.verifone.adc.launcher=string,RUN_PACKAGE=true=boolean" 的字符串
     */
    public Bundle parseSettingsString(String input) {
        Bundle bundle = new Bundle();
        Log.d(TAG, "parseSettingsString input = " + input);

        String[] parts = input.split(",");

        for (String part : parts) {
            String[] keyValueAndType = part.split("=");

            if (keyValueAndType.length == 3) {
                String key = keyValueAndType[0]; // key
                String valueString = keyValueAndType[1]; // value
                String typeString = keyValueAndType[2]; // type

                switch (typeString) {
                    case "int":
                        try {
                            int intValue = Integer.parseInt(valueString);
                            bundle.putInt(key, intValue);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "NumberFormatException e = " + e);
                        }
                        break;
                    case "boolean":
                        boolean booleanValue = Boolean.parseBoolean(valueString);
                        bundle.putBoolean(key, booleanValue);
                        break;
                    case "string":
                        bundle.putString(key, valueString);
                        break;
                    default:
                        break;
                }

            } else {
                Toast.makeText(getContext(), "格式不正确", Toast.LENGTH_SHORT).show();
            }
        }
        Log.d(TAG, "parseSettingsString bundle = " + bundle);
        return bundle;
    }

    private void initParam(View view) {
        getAutoSystemTime = view.findViewById(R.id.getAutoSystemTime);
        getAutoSystemTimeZone = view.findViewById(R.id.getAutoSystemTimeZone);
        setAutoSystemTime = view.findViewById(R.id.setAutoSystemTime);
        setAutoSystemTimeZone = view.findViewById(R.id.setAutoSystemTimeZone);
        setSystemTime = view.findViewById(R.id.setSystemTime);
        setLauncher = view.findViewById(R.id.setLauncher);
        load_CaCertificate = view.findViewById(R.id.load_CaCertificate);
        etSettingPwd1 = view.findViewById(R.id.etSettingPwd1);
        setSettingPwd = view.findViewById(R.id.setSettingPwd);
        setTimeZone = view.findViewById(R.id.setTimeZone);
        et_setTimeZone = view.findViewById(R.id.et_setTimeZone);
        settingPCIRebootTime = view.findViewById(R.id.settingPCIRebootTime);
        getPCIRebootTime = view.findViewById(R.id.getPCIRebootTime);
        et_tf_enable = view.findViewById(R.id.et_tf_enable);
        setScreenSleep = view.findViewById(R.id.setScreenSleep);
        etSetLauncher = view.findViewById(R.id.etSetLauncher);
        et_loadCertificate = view.findViewById(R.id.et_loadCertificate);
        et_upgradeCustomPackages = view.findViewById(R.id.et_upgradeCustomPackages);
        upgradeCustomPackages = view.findViewById(R.id.upgradeCustomPackages);
        et_setPCIRebootTime = view.findViewById(R.id.et_setPCIRebootTime);
        et_enableAlertWindow = view.findViewById(R.id.et_enableAlertWindow);
        enableAlertWindow = view.findViewById(R.id.enableAlertWindow);
        jumpToDialog = view.findViewById(R.id.jumpToDialog);
        settingsSetActions = view.findViewById(R.id.settingsSetActions);
        et_settingsSetActions = view.findViewById(R.id.et_settingsSetActions);
        et_setDefaultSystemLanguage = view.findViewById(R.id.et_setDefaultSystemLanguage);
        setDefaultSystemLanguage = view.findViewById(R.id.setDefaultSystemLanguage);
        clearCachesByPackageName = view.findViewById(R.id.clearCachesByPackageName);
        et_clearCachesByPackageName = view.findViewById(R.id.et_clearCachesByPackageName);
        setWallpaper = view.findViewById(R.id.setWallpaper);
        et_setWallpaper = view.findViewById(R.id.et_setWallpaper);
        rg_settingsSetActions = view.findViewById(R.id.rg_settingsSetActions);
        rg_setlauncher = view.findViewById(R.id.rg_setlauncher);
        rg_power_optimize = view.findViewById(R.id.rg_power_optimize);
        rg_datatime = view.findViewById(R.id.rg_datatime);
        rg_tfenable = view.findViewById(R.id.rg_tfenable);
        option1Layout = view.findViewById(R.id.option1Layout);
        option2Layout = view.findViewById(R.id.option2Layout);
        et_power_optimize = view.findViewById(R.id.et_power_optimize);
        settingsReadActions = view.findViewById(R.id.settingsReadActions);
        et_launcher_action = view.findViewById(R.id.et_launcher_action);
        et_launcher_pkg = view.findViewById(R.id.et_launcher_pkg);
        et_launcher_run = view.findViewById(R.id.et_launcher_run);
        et_power_op_enable = view.findViewById(R.id.et_power_op_enable);
        option3Layout = view.findViewById(R.id.option3Layout);
        option4Layout = view.findViewById(R.id.option4Layout);
        et_system_time = view.findViewById(R.id.et_system_time);
        et_system_time_zone = view.findViewById(R.id.et_system_time_zone);
        et_system_time_date = view.findViewById(R.id.et_system_time_date);
        et_brightnessLevel = view.findViewById(R.id.et_brightnessLevel);
        edt_duration = view.findViewById(R.id.edt_duration);
        set_battery_percent = view.findViewById(R.id.set_battery_percent);
        setScreenLock = view.findViewById(R.id.setScreenLock);
        isScreenLock = view.findViewById(R.id.isScreenLock);
        setDeviceBrightnessLevel = view.findViewById(R.id.setDeviceBrightnessLevel);
        setWallpaper = view.findViewById(R.id.setWallpaper);
        getSleepDuration = view.findViewById(R.id.getSleepDuration);
        setSleepDuration = view.findViewById(R.id.setSleepDuration);
        sw_settingEnable = view.findViewById(R.id.sw_settingEnable);
        rg_time_zone = view.findViewById(R.id.rg_time_zone);
        time_state = view.findViewById(R.id.time_state);
        time_zone_state = view.findViewById(R.id.time_zone_state);

    }


}