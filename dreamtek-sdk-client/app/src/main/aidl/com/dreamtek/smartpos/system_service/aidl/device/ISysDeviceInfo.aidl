package com.dreamtek.smartpos.system_service.aidl.device;
import com.dreamtek.smartpos.system_service.aidl.device.TusnData;

/**
 * <p> get device information, including software and hardware information.
 */
interface ISysDeviceInfo {

    /**
     * <p> get the serial number(SN) of the terminal.
     * @return {@code String}
     * @
     */
    String getSerialNo();

    /**
     * <p> get the IMSI of the terminal.
     * @return {@code String}
     * @
     */
    String getIMSI();

    /**
     * <p> get the IMEI of the terminal.
     * @return {@code String}
     * @
     */
    String getIMEI();

    /**
     * <p> get the ICCID of the SIM card which present.
     * @return {@code String}
     * @
     */
    String getICCID();

    /**
     * <p> get name of manufacture
     * @return {@code String}
     * @
     */
    String getManufacture();

    /**
     * <p> get model of the terminal
     * @return {@code String}
     * @
     */
    String getModel();

    /**
     * <p> get the version of the Android OS.
     * @return {@code String}
     * @
     */
    String getAndroidOSVersion();

    /**
     * <p> get the version of Android Kernel
     * @return {@code String}
     * @
     */
    String getAndroidKernelVersion();

    /**
     * <p> get the ROM version of Android.
     * @return {@code String}
     * @
     */
    String getROMVersion();

    /**
     * <p> get the firmare version of the terminal.
     * @return {@code String}
     * @
     */
    String getFirmwareVersion();

    /**
     * <p> get the hardware version
     * @return {@code String}
     * @
     */
    String getHardwareVersion();

    /**
     * <p> get the terminal UnionPay serial number.
     *
     * @param mode, passing 0
     * @param input, passing random number for calculating the Mac value of terminal UnionPay serial number. byte array length is between 4 and 10 byte.
     * @return null if fail.
     * @Deprecated
     * @
     */
    TusnData getTUSN(int mode, in byte[] input);

    /**
     * <p> get the PN of the terminal.
     *
     * @return {@code String}
     * @
     */
    String getPN();

    /**
     * <p> set power key disable or enable
     *
     * @param status true - disable the power key, false - enable the power key
     * @
     */
    void setPowerStatus(boolean status);

    /**
     * <p> get the Total RAM. Unit is byte
     *
     * @return {@code String}
     * @
     */
    String getRamTotal();

    /**
     * <p> get the available RAM capacity. Unit is byte.
     *
     * @return {@code String}
     * @
     */
    String getRamAvailable();

    /**
     * <p> get the flash RAM capacity. Unit is byte.
     *
     * @return {@code String}
     * @
     */
    String getRomTotal();

    /**
     * <p> get the available flash RAM capacity. Unit is byte.
     *
     * @return {@code String}
     * @
     */
    String getRomAvailable();

    /**
     * <p> get the mobile data usage total amount. Unit is byte.
     *
     * @return {@code String}
     * @
     */
    String getMobileDataUsageTotal();

    /**
     * <p> get the boot count.
     *
     * @return {@code String}
     * @
     */
    String getBootCounts();

    /**
     * <p> get the print paper length. Unit is millimeter.
     *
     * @return {@code String}
     * @
     */
    String getPrintPaperLen();

    /**
     * <p> get the times of magnetic card used.
     *
     * @return {@code String}
     * @
     */
    String getMagCardUsedTimes();

    /**
     * <p> get the times of smart card used.
     *
     * @return {@code String}
     * @
     */
    String getSmartCardUsedTimes();

    /**
     * <p> get the times of CTLS card used.
     *
     * @return {@code String}
     * @
     */
    String getCTLSCardUsedTimes();

    /**
     * <p> get the Battery Temperaturd.
     *
     * @return {@code String}
     * @
     */
    String getBatteryTemperature();

    /**
     * <p> get the Battery level.
     *
     * @return {@code String}
     * @
     */
    String getBatteryLevel();

    /**
     * <p> get the MEID info.
     *
     * @return {@code String}
     * @
     */
    String getMEID();

    /**
     * <p> get the tampler code.
     *
     * @return {@code String}
     * @
     */
    String getTamperCode();

    /**
     * <p> get the DTKSystemService Version.
     *
     * @return {@code String}
     * @
     */
    String getServiceVersion();

    /**
     * <p> Get certificate.
     * @param mode 0 : sponsor digest; others : not support.
     * @return {@code String} if param is 0, return sponsor digest certificate. Otherwise, retuen "";
     * @
     */
    String getCertificate(int mode);

    /**
     * <p> Get Battery charging times.
     * @return {@code String}
     * @
     */
    String getBatteryChargingTimes();

     /**
     * <p> get device status
     * @param bundle {@link android.os.Bundle}
     * <p><pre>{@code
     *      Bundle bundle = new Bundle();
     *      bundle.putString("DeviceType", "PRINTER");
     * }
     * </pre>
     * <p>the values of DeviceType:
     * <ul>
     *   <li>@{code String}"PRINTER"</li>
     *   <li>@{code String}"ICCARDREADER_SLOT1"</li>
     *   <li>@{code String}"ICCARDREADER_SLOT2"</li>
     *   <li>@{code String}"RFCARDREADER"</li>
     *   <li>@{code String}"SAMCARDREADER_SLOT1"</li>
     *   <li>@{code String}"SAMCARDREADER_SLOT2"</li>
     *   <li>@{code String}"PINPAD"</li>
     *   <li>@{code String}"CAMERA_FRONT"</li>
     *   <li>@{code String}"CAMERA_REAR"</li>
     *   <li>@{code String}"SDCARD"</li>
     * </ul>
     * @return 0:normal; -1:abnormal.
     * @
     */
     int getDeviceStatus(in Bundle bundle);

    /**
     * <p> get button battery voltage
     * @return value of voltage
     * @
     */
     String getButtonBatteryVol();

    /**
     * get information of device
     * @return - the key of bundle :
     * <ul>
     *   <li>{code String}SN (description:Serial No)</li>
     *   <li>{code String}PN (description:Product No)</li>
     *   <li>{code String}IMSI</li>
     *   <li>{code String}IMEI (description:International Mobile Equipment Identity)</li>
     *   <li>{code String}MEID (description:Mobile Equipment Identifier)</li>
     *   <li>{code String}manufacture (description:get manufacture message)</li>
     *   <li>{code String}deviceModel (description:get device model)</li>
     *   <li>{code String}androidOsVer (description:get android OS version)</li>
     *   <li>{code String}androidKernalVer (description:get android kernel version)</li>
     *   <li>{code String}romVer (description:get rom version)</li>
     *   <li>{code String}firmwareVer (description:get firmware version)</li>
     *   <li>{code String}hardwareVer (description:get hardware version)</li>
     *   <li>{code String}DTKSysSerivceVer (description:get DTKSysService version)</li>
     *   <li>{code String}VRKSn (description:get VRK sn)</li>
     *   <li>{code String}SponsorID (description:get sponsor id)</li>
     * </ul>
     * @
     */
    Bundle getDeviceInfo();


    /**
     * Get information of device, getDevInfos - key is device info that you want to get, value is extension conditions(normally is "")
     * @param extrend
     * key include:
     * <ul>
     *      <li>{code String}SN (description:Serial No)</li>
     *      <li>{code String}PN (description:Product No)</li>
     *      <li>{code String}IMSI</li>
     *      <li>{code String}IMEI (description:International Mobile Equipment Identity)</li>
     *      <li>{code String}MEID (description:Mobile Equipment Identifier)</li>
     *      <li>{code String}manufacture (description:get manufacture message)</li>
     *      <li>{code String}deviceModel (description:get device model)</li>
     *      <li>{code String}androidOsVer (description:get android OS version)</li>
     *      <li>{code String}androidKernalVer (description:get android kernel version)</li>
     *      <li>{code String}romVer (description:get rom version)</li>
     *      <li>{code String}firmwareVer (description:get firmware version)</li>
     *      <li>{code String}hardwareVer (description:get hardware version)</li>
     *      <li>{code String}SPVer (description:get SP version)</li>
     *      <li>{code String}DTKSysSerivceVer (description:get DTKSysService version)</li>
     *      <li>{code String}VRKSn (description:get VRK sn)</li>
     *      <li>{code String}SponsorID (description:get sponsor HashValue)</li>
     *      <li>{code String}SponsorName (description:get sponsor Name)</li>
     *      <li>{code String}bootVer(description:get Boot version)</li>
     *      <li>{code String}IMEIS (iemi list for dual sim,return type is StringArray)</li>
     *      <li>{code String}IMSIS (imsi list for dual sim,return type is StringArray)</li>
     *      <li>{code String}ICCIDS (iccid list for dual sim,return type is StringArray)</li>
     * </ul>
     * @return bundle - return info which you search by key input getDevInfos
     * @throws RemoteException
     * @
     */
    Bundle getDeviceInfoEx(in Bundle extrend);
}
