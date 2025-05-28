package moudles.newModules;

import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Simon on 2021/9/8
 */
public class SysSettingsModule extends TestModule {


    /**
     * settings set actions
     *
     * @param settingsType: please refer to SettingType class; current include:
     *                      <li>SettingType.LAUNCHER</li>
     *                      <li>SettingType.DATE_TIME</li>
     * @param bundle:       please refer to SettingActions;
     *                      <ul> if settingsType is SettingType.LAUNCHER
     *                        <li>SettingsActions.LauncherActions.SET_LAUNCHER</li>
     *                      </ul
     *                      <ul> if settingsType is SettingType.DATE_TIME
     *                        <li>SettingsActions.DATE_TIME.SET_AUTO_SYSTEM_TIME_STATE</li>
     *                        <li>SettingsActions.DATE_TIME.SET_AUTO_SYSTEM_TIME_ZONE_STATE</li>
     *                        <li>SettingsActions.DATE_TIME.SET_SYSTEM_TIME</li>
     *                      </ul
     * @return 0: set success;   -1: set fail
     * @since 1.6.0
     * <pre>
     * For example:
     *       since 1.6.0.0
     *       SettingsType.DATE_TIME:
     *       //set system time whether to sync auto network time
     *       Bundle bundle = new Bundle();
     *       bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.SET_AUTO_SYSTEM_TIME_STATE);
     *       More informations of "SYSTEM_TIME_ACTIONS" pleae refer to SettingsActions.SystemTimeActions class;
     *       bundle.putInt("AUTO_SYSTEM_TIME", 0);
     *       0: disable sync
     *       1: sync
     *
     *       //set system time zone whether to sync auto network time zone
     *       Bundle bundle = new Bundle();
     *       bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.SET_AUTO_SYSTEM_TIME_ZONE_STATE);
     *       More informations of "SYSTEM_TIME_ACTIONS" pleae refer to SettingsActions.SystemTimeActions class;
     *       bundle.putInt("AUTO_SYSTEM_TIME_ZONE", 0);
     *       0: disable sync
     *       1: sync
     *
     *       since 1.6.0.1
     *       //set system time
     *       Bundle bundle = new Bundle();
     *       bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.SET_AUTO_SYSTEM_TIME_ZONE_STATE);
     *       More informations of "SYSTEM_TIME_ACTIONS" pleae refer to SettingsActions.SystemTimeActions class;
     *       bundle.putString("SYSTEM_DATE", "20200707"); date format is yyyyMMdd;
     *       bundle.putString("SYSTEM_TIME", "150629"); time format is HHmmss;
     *
     *       since 1.6.0.1
     *       //set launcher
     *       Bundle bundle = new Bundle();
     *       bundle.putString("LAUNCHER_ACTIONS", SettingsActions.LauncherActions.SET_LAUNCHER);
     *       More informations of "LAUNCHER_ACTIONS" pleae refer to SettingsActions.LauncherActions class;
     *       bundle.putString("LAUNCHER_PACKAGE_NAME", "com.vfi.android.payment"); // passing launcher application package name.
     *       bundle.putBoolean("RUN_PACKAGE", true); //true: run application, false: not run application.
     * </pre>
     **/
    int T_settingsSetActions(String settingsType, String bundle) throws RemoteException {
 /*       BundleConfig[] bundleConfigs = new BundleConfig[]{
//                new BundleConfig("AUTO_SYSTEM_TIME", BundleConfig_boolean),
                new BundleConfig("SYSTEM_TIME_ACTIONS", BundleConfig_String),
                new BundleConfig("SYSTEM_DATE", BundleConfig_String),
                new BundleConfig("SYSTEM_TIME", BundleConfig_String),
        };*/

        BundleConfig[] bundleConfigs = new BundleConfig[]{
                new BundleConfig("LAUNCHER_ACTIONS", BundleConfig_String),
                new BundleConfig("LAUNCHER_PACKAGE_NAME", BundleConfig_String),
                new BundleConfig("RUN_PACKAGE", BundleConfig_boolean),
        };
        Bundle bBundle = convert(bundle, bundleConfigs);


        return iSettingsManager.settingsSetActions(Integer.valueOf(settingsType), bBundle);
    }

    /**
     * settings read actions
     *
     * @param settingsType: please refer to SettingType; current only 'SettingsType.DATE_TIME'
     * @param bundle:       please refer to SettingActions; current include:
     *                      <li>SettingActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_STATE</li>
     *                      <li>SettingActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_ZONE_STATE</li>
     * @return <ul> if SettingActions is SystemTimeActions.GET_AUTO_SYSTEM_TIME_STATE
     *   <li>SYSTEM_TIME_ACTIONS<int> 1: sync auto system time;    0: disable sync auto system time</li>
     * </ul>
     * <ul> if SettingActions is SystemTimeActions.GET_AUTO_SYSTEM_TIME_ZONE_STATE
     *   <li>AUTO_SYSTEM_TIME_ZONE<int> 1: sync auto system time zone;    0: disable sync auto system time zone</li>
     * </ul>
     * @since 1.6.0
     * <pre>
     *   For example:
     *   SettingsType.DATE_TIME:
     *       //get system time whether to sync auto network time
     *       Bundle bundle = new Bundle();
     *       bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_STATE);
     *       More informations of "SYSTEM_TIME_ACTIONS" pleae refer to SettingsActions.SystemTimeActions class;
     *
     *   @return bundle
     *       int state = bundle.getInt("AUTO_SYSTEM_TIME");
     *       state: 1: sync auto system time;    0: disable sync auto system time
     *
     *       //get system time zone whether to sync auto network time zone
     *       Bundle bundle = new Bundle();
     *       bundle.putString("SYSTEM_TIME_ACTIONS", SettingsActions.SystemTimeActions.GET_AUTO_SYSTEM_TIME_ZONE_STATE);
     *       More informations of "SYSTEM_TIME_ACTIONS" pleae refer to SettingsActions.SystemTimeActions class;
     *
     *   @return bundle
     *       int state = bundle.getInt("AUTO_SYSTEM_TIME_ZONE");
     *       state: 1: sync auto system time zone;    0: disable sync auto system time zone
     * </pre>
     **/
    String T_settingsReadActions(String settingsType, String bundle) throws RemoteException {
        BundleConfig[] bundleConfigs = new BundleConfig[]{
//                new BundleConfig("AUTO_SYSTEM_TIME", BundleConfig_boolean),
                new BundleConfig("SYSTEM_TIME_ACTIONS", BundleConfig_String),
                new BundleConfig("SYSTEM_DATE", BundleConfig_String),
                new BundleConfig("SYSTEM_TIME", BundleConfig_String),
        };

       /* BundleConfig[] bundleConfigs = new BundleConfig[]{
                new BundleConfig("LAUNCHER_ACTIONS", BundleConfig_String),
                new BundleConfig("LAUNCHER_PACKAGE_NAME", BundleConfig_String),
                new BundleConfig("RUN_PACKAGE", BundleConfig_boolean),
        };*/
        Bundle bBundle = convert(bundle, bundleConfigs);
        Bundle ret = iSettingsManager.settingsReadActions(Integer.valueOf(settingsType), bBundle);
        return ret.toString();
    }


    /**
     * set PCI reboot time.
     *
     * @param hour(24-hour format range:0~23)
     * @param min(range:0~59)
     * @param sec(range:0~59)
     * @return result
     * @since 1.7.0
     **/
    boolean T_settingPCIRebootTime(String hour, String min, String sec) throws RemoteException {
        return iSettingsManager.settingPCIRebootTime(Integer.valueOf(hour), Integer.valueOf(min), Integer.valueOf(sec));
    }

    /**
     * get PCI reboot time.
     *
     * @return seconds for unit
     * @since 1.7.0
     **/
    long T_getPCIRebootTime() throws RemoteException {
        return iSettingsManager.getPCIRebootTime();
    }

    /**
     * set Screen Lock
     *
     * @param isLock true lock screen, false not lock
     * @since 1.8.2
     **/
    void T_setScreenLock(String isLock) throws RemoteException {
        iSettingsManager.setScreenLock(Boolean.valueOf(isLock));
    }

    /**
     * set device brightness level
     *
     * @param level 0~255
     * @return true for success, false for failure
     * @since 1.8.2
     **/
    boolean T_setDeviceBrightnessLevel(String level) throws RemoteException {
        return iSettingsManager.setDeviceBrightnessLevel(Integer.valueOf(level));
    }

    /**
     * set is show battery percent in status bar
     *
     * @param isShow, true for show, false for hide
     * @return true for success, false for failure
     * @since 1.8.2
     **/
    boolean T_isShowBatteryPercent(String isShow) throws RemoteException {
        return iSettingsManager.isShowBatteryPercent(Boolean.valueOf(isShow));
    }

    /**
     * enable the permission by packageName
     * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
     *
     * @param packageName, the packageName of application
     * @since 1.8.3
     **/
    void T_enableAlertWindow(String packageName) throws RemoteException {
        iSettingsManager.enableAlertWindow(packageName);
    }

    /**
     * clear application caches by packageName
     *
     * @param packageName, the packageName of application
     * @since 1.8.8
     **/
    void T_clearCachesByPackageName(String packageName) throws RemoteException {
        iSettingsManager.clearCachesByPackageName(packageName);
    }

    /**
     * set default system language
     *
     * @param language ,  language name
     * @param country  ,  country name
     * @since 1.10.1
     * <p>
     * Below is language and country define
     * zh_CN en_AU en_IN fr_FR it_IT es_ES et_EE de_DE nl_NL cs_CZ pl_PL ja_JP
     * zh_TW en_US zh_HK ru_RU ko_KR nb_NO es_US da_DK el_GR tr_TR pt_PT pt_BR rm_CH sv_SE bg_BG
     * ca_ES en_GB fi_FI hi_IN hr_HR hu_HU in_ID iw_IL lt_LT lv_LV ro_RO sk_SK sl_SI sr_RS uk_UA
     * vi_VN tl_PH ar_EG fa_IR th_TH sw_TZ ms_MY af_ZA zu_ZA am_ET hi_IN en_XA ar_XB fr_CA km_KH
     * lo_LA ne_NP si_LK mn_MN hy_AM az_AZ ka_GE my_MM mr_IN ml_IN is_IS mk_MK ky_KG eu_ES gl_ES
     * bn_BD ta_IN kn_IN te_IN uz_UZ ur_PK kk_KZ
     * <p>
     * For example: if you want to set Chinese language, you can call like this: setDefaultSystemLanguage("zh", "CN");
     */
    void T_setDefaultSystemLanguage(String language, String country) throws RemoteException {
        Log.d(language, "language : " + language);
        iSettingsManager.setDefaultSystemLanguage(language, country);
    }

    void T_setTimeZone(String timeZoneCode) throws RemoteException {
        iSettingsManager.setTimeZone(timeZoneCode);

    }

    boolean T_setBootingAnimation(String resources) throws RemoteException {
        Bundle bundle = convert(resources, null);
        return iSettingsManager.setBootingAnimation(bundle);
    }

    // 缺省使用 ; 分割多个值
    String T_getBootingAnimation(String resources) throws RemoteException {
        List<String> keys = convert(resources, ';');
        Bundle bundle = iSettingsManager.getBootingAnimation(keys);
        return convert(bundle);
    }

    /**
     * Installs the given certificate as a user CA.
     *
     * @param the path of cetificate
     * @since 1.11.7
     */
    boolean T_loadCertificate(String certPath) throws RemoteException {
        return iSettingsManager.loadCertificate(certPath);
    }

    /**
     * Set the password set by the system, and remove the password when passing a null value.
     *
     * @param password to set (limit 2~10 number)
     * @since 1.11.9
     * note:ROM version needs to be upgraded to 20220804 or above
     */
    boolean T_setSettingPwd(String pwd) throws RemoteException {
        return iSettingsManager.setSettingPwd(pwd);
    }

    /**
     * enable device power supply to other device(just support V2 device), need to reboot device to let this setting take effect.
     *
     * @param powerSupply
     * @throws RemoteException
     */
    void T_setAllowChargingUsbDevices(String powerSupply) throws RemoteException {
        Log.d("TAG", "powerSupply: " + powerSupply);
        boolean value = powerSupply.toUpperCase().equals ("TRUE") ? true : false;
        iSettingsManager.setAllowChargingUsbDevices(value);
    }

    /**
     * get Power Supply State
     *
     * @return true-enable power supply; false-disable power supply
     * @throws RemoteException
     */
    boolean T_getAllowChargingUsbDevicesStatus() throws RemoteException {
        return iSettingsManager.getAllowChargingUsbDevicesStatus();
    }
}
