// SystemService.aidl
package com.dreamtek.smartpos.system_service.aidl;

import android.os.Bundle;
import com.dreamtek.smartpos.system_service.aidl.IAppInstallObserver;
import com.dreamtek.smartpos.system_service.aidl.IAppDeleteObserver;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import com.dreamtek.smartpos.system_service.aidl.IVerifysignCallback;

interface ISystemManager {
	/**
	 * Install an apk
	 * @param apkPath - apk's absolute url.
	 * @param observer - callback handler.
	 * @param installerPackageName - packagename of installer apk.
	 * @see IAppInstallObserver
	 */
    void installApp(String apkPath, IAppInstallObserver observer, String installerPackageName);

	/**
	 * remove an apk
	 * @param packageName - package name which need to remove.
	 * @param observer - callback handler.
	 * @see IAppDeleteObserver
	 */
    void uninstallApp(String packageName, IAppDeleteObserver observer);

	/**
	 * reboot terminal
	 */
    void reboot();

    /**
     * enable/disable home key
     * @param state true-disable home key  false-enable home key
     */
    void isMaskHomeKey(boolean state);

    /**
     * enable/disable system status bar
     * @param state true-disable system status bar  false-enable system status bar
     */
    void isMaskStatusBard(boolean state);

    /**
     * upgrade K21 driver
     * @param sysBin - path of sys bin file
     * @param appBin - path of app bin file
     * @return result
     */
    boolean chekcK21Update(String sysBin, String appBin);

    /**
     * upgrade ROM driver
     * @param zipPath - path of rom package
     */
    void updateROM(String zipPath);

    /**
     * get NetworkManager object
     * @return NetworkManager object
     * @see INetworkManager
     */
    INetworkManager getNetworkManager();

    /*
     * set location mode(only support android 5/7)
     * <p>If turn on status 2 or 3 from 0 or 1, system will pop up a window to let user allow IZat for hardware acceleration</p>
     * @param status 0: off 1: sensor only 2: battery saving 3: high accuracy
     */
    void setLocationMode( int status );

    /*
     * get ADB status
     * @return true-Adb enable false-Adb disable
     */
    boolean isAdbMode();

    /**
    * kill application
    * @param packageName application packageName
    * @return true-success false-fail
    */
    boolean killApplication(String packageName);

    /**
    * restart application
    * @param packageName application packageName
    * @return true-success false-fail
    */
    boolean restartApplication(String packageName);

    /**
    * init logcat configuration.
    *
    * @param logcatBufferSize: set logcat buffer size.
    * <blockquote>
    * if logcatBufferSize == 0, set default logcat buffer size
    * logcatBufferSizeSuffix: set logcat buffer size suffix. 0: "M", 1: "K".
    * </bolckquote>
    */
    void initLogcat(int logcatBufferSize, int logcatBufferSizeSuffix, in Bundle bundle);

    /**
    * get log buffer file
    *
    * @param logcatFileName: specify the log file name, if logcatFileName == null, set default logcat file name
    * @param compressType: set logcat compress type. 0: "none", 1: "gz".
    * <BR><b>user needs to delete log file. if the log file locates in default log path, it will be deleted 7 days after creation.</b>
    */
    String getLogcat(String logcatFileName, int compressType);

    /**
    * Get the usage count of all apps to be used at the specific time range
    *
    * @param beginTime get apps info begin time
    * <br>For example, beginTime = Calendar.getInstance().setDate(date).getTimeInMillis();
    * @param endTime get apps info end time
    * <br>For example, endTime = Calendar.getInstance().getTimeInMillis();
    * @return Bundle "UsageStatsList" : json string of List<UsageStats> object.
    */
    Bundle getLaunchAppsInfo(long beginTime, long endTime);

    /**
    * get ISettingsManager objet
    * <p>get SettingsManager to execute settings actions</p>
    * @return ISettingsManager
    * @see ISettingsManager
    **/
    ISettingsManager getSettingsManager();

    /**
    * take capture of the screen
    * @return Bitmap data of screen capture
    */
    Bitmap takeCapture();

    /**
    * device shutdown
    */
    void shutdownDevice();

    boolean UpdateSecurityDriver(String updatePackagePath);

    /**
     * Whether the application is currently foreground or background
     * @reture true-foreground false-background
     */
    boolean isAppForeground(String packageName);

    /**
     * update base band, will reboot device automatically after call this API, pls note: only support MG baseband.
     * noted:  mini/miniPro not support this function
     * @param filePath
     * @return false-when copy base band file failed, when success will reboot device.
     * @throws RemoteException
     */
    boolean updateBaseBand(String filePath);

    /**
     * Get screen brightness
     * @return brightness
     * @throws RemoteException
     */
    int getScreenBrightness();

    /**
     * Input data to adjust screen brightness:
     * the data is greater than or equal to 10 and less than or equal to 255(10<=data<=255)
     * @param brightnessData
     * @throws RemoteException
     */
    void changeScreenBrightness(int brightnessData);

    /**
     * Verify UKey or OS signature
     * <b>This interface is time-consuming and should be avoided in the main thread as much as possible</ b>
     * @param path: the path of the file to be verified and signed
     * @param enableDualVerify: has no effect on mini & pro machines.
     * @return true:verify sign success; false:verify sign fail
     * @throws RemoteException
     */
    boolean verifySignSync(String path, boolean enableDualVerify);

    /**
     * Verify UKey or OS signature
     * <b>This interface is time-consuming and should be avoided in the main thread as much as possible</ b>
     * @param path: the path of the file to be verified and signed
     * @param enableDualVerify: has no effect on mini & pro machines.
     * @param callback: signature verification result callback
     * @throws RemoteException
     */
    void verifySign(String path, boolean enableDualVerify, IVerifysignCallback callback);

    /**
     * NFC switch
     * noted: mini/miniPro not support this function
     * @param enable: NFC ON or OFF
     * @return 1:open success, 0 close success, -1 operation false
     */
    int setNfcEnable(boolean enable);

    /**
     * Set application uninstall permission
     * noted: mini/miniPro not support this function
     * If the app is not installed, disabling this app returns false.
     * @param packageName: the package name of the application to be prohibit uninstall
     * @param prohibitState: true-prohibit uninstall the package, false-allow uninstall the package
     * @return true-set success, false-set failure
     */
    boolean prohibitUninstall(String packageName, boolean prohibitState);

    /**
     * Enable and disable the installation of a user certificate on the device.
     * noted: mini/miniPro not support this function
     * When the Apl is called to disable the certificate installation, it must block alL the installation download methods available in the device/Android settings
     * @param enable - true prohibit CA install; false enable CA install
     * @return true/false
     */
    boolean prohibitCAInstall(boolean enable);

    /**
     * get the state of whether it's prohibit CA install or not
     * noted:  mini/miniPro not support this function
     * @return true-prohibit install/false-enable install
     */
    boolean isProhibitCAInstall();
}
