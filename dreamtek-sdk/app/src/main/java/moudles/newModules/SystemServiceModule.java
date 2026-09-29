package moudles.newModules;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.dreamtek.smartpos.system_service.aidl.IAppDeleteObserver;
import com.dreamtek.smartpos.system_service.aidl.IAppInstallObserver;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.IVerifysignCallback;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;

public class SystemServiceModule extends TestModule {
    private static final String TAG = "SystemServiceModule";
    public boolean isConnect = false;

    public SystemServiceModule() {
        super();
    }

    public SystemServiceModule(Context context) {
        this();
        this.context = context;
    }

    /**
     * Install an APK silently.
     *
     * @param apkPath            absolute path to the APK file
     * @param installerPackageName package name of the installer (can be empty)
     * @return "0" for success, "Error:<code>" for failure
     */
    public String T_installApp(String apkPath, String installerPackageName) {
        final Object lock = new Object();
        final int[] result = new int[]{-12345};

        IAppInstallObserver observer = new IAppInstallObserver.Stub() {
            @Override
            public void onInstallFinished(String packageName, int returnCode) {
                Log.d(TAG, "installApp callback: " + packageName + " code=" + returnCode);
                logUtils.addCaseLog("installApp callback: " + packageName + " code=" + returnCode);
                synchronized (lock) {
                    result[0] = returnCode;
                    lock.notify();
                }
            }
        };

        try {
            iSystemManager.installApp(apkPath, observer, installerPackageName);
            synchronized (lock) {
                lock.wait(120000);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "installApp RemoteException: " + e.getMessage());
            e.printStackTrace();
            logUtils.addCaseLog("installApp exception: " + e.getMessage());
            return "Error:RemoteException";
        } catch (InterruptedException e) {
            Log.e(TAG, "installApp interrupted: " + e.getMessage());
            e.printStackTrace();
            return "Error:Timeout";
        }

        if (result[0] == -12345) {
            return "Error:Timeout";
        }
        return result[0] == 0 ? "0" : "Error:" + result[0];
    }

    /**
     * Uninstall an app silently.
     *
     * @param packageName package name to uninstall
     * @return "0" for success, "Error:<code>" for failure
     */
    public String T_uninstallApp(String packageName) {
        final Object lock = new Object();
        final int[] result = new int[]{-12345};

        IAppDeleteObserver observer = new IAppDeleteObserver.Stub() {
            @Override
            public void onDeleteFinished(String pkgName, int returnCode) {
                Log.d(TAG, "uninstallApp callback: " + pkgName + " code=" + returnCode);
                logUtils.addCaseLog("uninstallApp callback: " + pkgName + " code=" + returnCode);
                synchronized (lock) {
                    result[0] = returnCode;
                    lock.notify();
                }
            }
        };

        try {
            iSystemManager.uninstallApp(packageName, observer);
            synchronized (lock) {
                lock.wait(60000);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "uninstallApp RemoteException: " + e.getMessage());
            e.printStackTrace();
            logUtils.addCaseLog("uninstallApp exception: " + e.getMessage());
            return "Error:RemoteException";
        } catch (InterruptedException e) {
            Log.e(TAG, "uninstallApp interrupted: " + e.getMessage());
            e.printStackTrace();
            return "Error:Timeout";
        }

        if (result[0] == -12345) {
            return "Error:Timeout";
        }
        return result[0] == 0 ? "0" : "Error:" + result[0];
    }

    /**
     * Reboot the device.
     */
    public void T_reboot() throws RemoteException {
        logUtils.addCaseLog("reboot");
        iSystemManager.reboot();
    }

    /**
     * Mask or unmask the Home key.
     *
     * @param state true=disable home key, false=enable
     */
    public void T_isMaskHomeKey(String state) throws RemoteException {
        boolean bState = Boolean.parseBoolean(state);
        logUtils.addCaseLog("isMaskHomeKey: " + bState);
        iSystemManager.isMaskHomeKey(bState);
    }

    /**
     * Mask or unmask the Recent/Overview key.
     *
     * @param state true=disable, false=enable
     */
    public void T_isMaskRecentStatusKey(String state) throws RemoteException {
        boolean bState = Boolean.parseBoolean(state);
        logUtils.addCaseLog("isMaskRecentStatusKey: " + bState);
        iSystemManager.isMaskRecentStatusKey(bState);
    }

    /**
     * Mask or unmask the status bar.
     *
     * @param state true=disable status bar, false=enable
     */
    public void T_isMaskStatusBard(String state) throws RemoteException {
        boolean bState = Boolean.parseBoolean(state);
        logUtils.addCaseLog("isMaskStatusBard: " + bState);
        iSystemManager.isMaskStatusBard(bState);
    }

    /**
     * Check and update K21 security driver.
     *
     * @param sysBin system bin path
     * @param appBin app bin path
     * @return "true" or "false"
     */
    public boolean T_chekcK21Update(String sysBin, String appBin) throws RemoteException {
        logUtils.addCaseLog("checK21Update: sysBin=" + sysBin + " appBin=" + appBin);
        return iSystemManager.chekcK21Update(sysBin, appBin);
    }

    /**
     * Update ROM firmware.
     *
     * @param zipPath absolute path to the ROM zip file
     */
    public void T_updateROM(String zipPath) throws RemoteException {
        logUtils.addCaseLog("updateROM: " + zipPath);
        iSystemManager.updateROM(zipPath);
    }

    /**
     * Get the INetworkManager sub-interface.
     *
     * @return description of the network manager binder
     */
    public String T_getNetworkManager() throws RemoteException {
        INetworkManager nm = iSystemManager.getNetworkManager();
        String result = nm != null ? "INetworkManager: " + nm.asBinder().isBinderAlive() : "null";
        logUtils.addCaseLog("getNetworkManager: " + result);
        return result;
    }

    /**
     * Set location mode.
     *
     * @param status 0=off, 1=sensors, 2=battery saving, 3=high accuracy
     */
    public void T_setLocationMode(String status) throws RemoteException {
        int mode = Integer.parseInt(status);
        logUtils.addCaseLog("setLocationMode: " + mode);
        iSystemManager.setLocationMode(mode);
    }

    /**
     * Query whether ADB is enabled.
     *
     * @return true if ADB is enabled
     */
    public boolean T_isAdbMode() throws RemoteException {
        boolean result = iSystemManager.isAdbMode();
        logUtils.addCaseLog("isAdbMode: " + result);
        return result;
    }

    /**
     * Force-stop an application.
     *
     * @param packageName package name to kill
     * @return true for success
     */
    public boolean T_killApplication(String packageName) throws RemoteException {
        logUtils.addCaseLog("killApplication: " + packageName);
        return iSystemManager.killApplication(packageName);
    }

    /**
     * Restart an application.
     *
     * @param packageName package name to restart
     * @return true for success
     */
    public boolean T_restartApplication(String packageName) throws RemoteException {
        logUtils.addCaseLog("restartApplication: " + packageName);
        return iSystemManager.restartApplication(packageName);
    }

    /**
     * Initialize logcat buffer configuration.
     *
     * @param bufferSize    buffer size number
     * @param suffix        0=M(megabytes), 1=K(kilobytes)
     * @param bundleParams  additional config as key=value pairs separated by _
     */
    public void T_initLogcat(String bufferSize, String suffix, String bundleParams) throws RemoteException {
        int size = Integer.parseInt(bufferSize);
        int sfx = Integer.parseInt(suffix);
        Bundle bundle = convert(bundleParams, null);
        logUtils.addCaseLog("initLogcat: size=" + size + " suffix=" + sfx);
        iSystemManager.initLogcat(size, sfx, bundle);
    }

    /**
     * Capture logcat to a file.
     *
     * @param logcatFileName output file name
     * @param compressType   0=none, 1=gz
     * @return file path of the captured log
     */
    public String T_getLogcat(String logcatFileName, String compressType) throws RemoteException {
        int type = Integer.parseInt(compressType);
        logUtils.addCaseLog("getLogcat: file=" + logcatFileName + " compress=" + type);
        String result = iSystemManager.getLogcat(logcatFileName, type);
        logUtils.addCaseLog("getLogcat result: " + result);
        return result;
    }

    /**
     * Get app usage statistics.
     *
     * @param beginTime start time in millis
     * @param endTime   end time in millis
     * @return bundle as string containing usage stats JSON
     */
    public String T_getLaunchAppsInfo(String beginTime, String endTime) throws RemoteException {
        long begin = Long.parseLong(beginTime);
        long end = Long.parseLong(endTime);
        logUtils.addCaseLog("getLaunchAppsInfo: begin=" + begin + " end=" + end);
        Bundle result = iSystemManager.getLaunchAppsInfo(begin, end);
        String sResult = result != null ? result.toString() : "null";
        logUtils.addCaseLog("getLaunchAppsInfo result: " + sResult);
        return sResult;
    }

    /**
     * Get the ISettingsManager sub-interface.
     *
     * @return description of the settings manager binder
     */
    public String T_getSettingsManager() throws RemoteException {
        ISettingsManager sm = iSystemManager.getSettingsManager();
        String result = sm != null ? "ISettingsManager: " + sm.asBinder().isBinderAlive() : "null";
        logUtils.addCaseLog("getSettingsManager: " + result);
        return result;
    }

    /**
     * Take a screenshot.
     *
     * @return bitmap dimensions or "null"
     */
    public String T_takeCapture() throws RemoteException {
        logUtils.addCaseLog("takeCapture");
        Bitmap bitmap = iSystemManager.takeCapture();
        if (bitmap != null) {
            String result = bitmap.getWidth() + "x" + bitmap.getHeight();
            logUtils.addCaseLog("takeCapture: " + result);
            bitmap.recycle();
            return result;
        }
        logUtils.addCaseLog("takeCapture: null");
        return "null";
    }

    /**
     * Shut down the device.
     */
    public void T_shutdownDevice() throws RemoteException {
        logUtils.addCaseLog("shutdownDevice");
        iSystemManager.shutdownDevice();
    }

    /**
     * Update security driver (SP firmware). Synchronous, may take up to 5 minutes.
     *
     * @param updatePackagePath path to the update package
     * @return true for success
     */
    public boolean T_UpdateSecurityDriver(String updatePackagePath) throws RemoteException {
        logUtils.addCaseLog("UpdateSecurityDriver: " + updatePackagePath);
        return iSystemManager.UpdateSecurityDriver(updatePackagePath);
    }

    /**
     * Check if an app is in the foreground.
     *
     * @param packageName package name to check
     * @return true if the app is foreground
     */
    public boolean T_isAppForeground(String packageName) throws RemoteException {
        boolean result = iSystemManager.isAppForeground(packageName);
        logUtils.addCaseLog("isAppForeground: " + packageName + " = " + result);
        return result;
    }

    /**
     * Get current screen brightness.
     *
     * @return brightness value (10-255)
     */
    public int T_getScreenBrightness() throws RemoteException {
        int result = iSystemManager.getScreenBrightness();
        logUtils.addCaseLog("getScreenBrightness: " + result);
        return result;
    }

    /**
     * Set screen brightness.
     *
     * @param brightnessData brightness value (10-255)
     */
    public void T_changeScreenBrightness(String brightnessData) throws RemoteException {
        int brightness = Integer.parseInt(brightnessData);
        logUtils.addCaseLog("changeScreenBrightness: " + brightness);
        iSystemManager.changeScreenBrightness(brightness);
    }

    /**
     * Verify APK/ZIP signature synchronously.
     *
     * @param path file path to verify
     * @return true if signature is valid
     */
    public boolean T_verifySignSync(String path) throws RemoteException {
        logUtils.addCaseLog("verifySignSync: " + path);
        boolean result = iSystemManager.verifySignSync(path);
        logUtils.addCaseLog("verifySignSync result: " + result);
        return result;
    }

    /**
     * Verify APK/ZIP signature asynchronously.
     *
     * @param path file path to verify
     * @return "true" or "false" from callback
     */
    public String T_verifySign(String path) {
        final Object lock = new Object();
        final boolean[] result = new boolean[]{false};
        final boolean[] done = new boolean[]{false};

        IVerifysignCallback callback = new IVerifysignCallback.Stub() {
            @Override
            public void onVerifySignResult(boolean verifyResult) {
                Log.d(TAG, "verifySign callback: " + verifyResult);
                logUtils.addCaseLog("verifySign callback: " + verifyResult);
                synchronized (lock) {
                    result[0] = verifyResult;
                    done[0] = true;
                    lock.notify();
                }
            }
        };

        try {
            iSystemManager.verifySign(path, callback);
            synchronized (lock) {
                lock.wait(60000);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "verifySign RemoteException: " + e.getMessage());
            e.printStackTrace();
            logUtils.addCaseLog("verifySign exception: " + e.getMessage());
            return "Error:RemoteException";
        } catch (InterruptedException e) {
            Log.e(TAG, "verifySign interrupted: " + e.getMessage());
            e.printStackTrace();
            return "Error:Timeout";
        }

        if (!done[0]) {
            return "Error:Timeout";
        }
        return String.valueOf(result[0]);
    }

    /**
     * Get the ISysDeviceInfo sub-interface.
     *
     * @return description of the device info binder
     */
    public String T_getSysDeviceInfo() throws RemoteException {
        ISysDeviceInfo di = iSystemManager.getSysDeviceInfo();
        String result = di != null ? "ISysDeviceInfo: " + di.asBinder().isBinderAlive() : "null";
        logUtils.addCaseLog("getSysDeviceInfo: " + result);
        return result;
    }
}
