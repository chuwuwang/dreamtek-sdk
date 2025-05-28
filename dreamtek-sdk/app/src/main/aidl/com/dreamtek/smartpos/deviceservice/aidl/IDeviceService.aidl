package com.dreamtek.smartpos.deviceservice.aidl;

import com.dreamtek.smartpos.deviceservice.aidl.IBeeper;
import com.dreamtek.smartpos.deviceservice.aidl.ILed;
import com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IScanner;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.sde.ISde;

import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IFelica;
import com.dreamtek.smartpos.deviceservice.aidl.utils.IUtils;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.INtagCard;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IKLD;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IICodeCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardEV1;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardC;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardNano;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;
import com.dreamtek.smartpos.deviceservice.aidl.IWirelessBaseHelper;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IMKSK;
import com.dreamtek.smartpos.deviceservice.aidl.IServiceInfo;

/**
 * <p> Device service, get each service interface (object) in this interface
 */
interface IDeviceService {
    /**
     * <p> get the IBeeper interface object for Beeper.
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IBeeper}
     * @
     */
    IBeeper getBeeper();
    
    /**
     * <p> get the ILed interface object for Led.
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.ILed}
     * @
     */
    ILed getLed();
    
    /**
     * <p> get the ISerialPort interface object for Serial Port.
     *
     * @param {@code String} deviceType
     * <p> the key of deviceType param is as follow:
     * <ul>
     *   <li>"rs232"(description:the port via build in serial chip micro USB cable (one side is micro USB connect to terminal, another side is 9 pin interface connect to COM port in PC.))</li>
     *   <li>"usb-rs232"(description:the port via micro USB cable）</li>
     * </ul>
     *
     * <p>special device type</p>
     * <ul>
     *   <li><B>Type1:</B>set VID (and PID) directly or only VID, such as "usb2rs232-11CA-0204", "usb2rs232-11CA".
     *   <li>"usb2rs232-Z-TEK"(same as "usb2rs232-0403-6001"), (description:definied in @{link   com.dreamtek.smartpos.devicemanager.util.SerialPortChart.Z_TEK})</li>
     * </ul>
     *
     * <p>For given driver ftdi, cdc, ch34, cp21, proli to load a device</P>
     * <ul>
     *   <li><B>type2:</B> usb2rs232-VIP-PID-Driver</li>
     *   <li>usb2rs232-0403-6001-ftdi</li>
     * </ul>
     *
     * <p>For given slot to load a device in case of there're same Devices attached</p>
     * <ul>
     *   <li><B>Type3:</B> Slot started from 0</li>
     *   <li>usb2rs232-VIP-PID-Slot</li>
     *   <li>usb2rs232-VIP-PID-Driver-Slot</li>
     *   <li>usb2rs232-11CA-0204-1</li>
     *   <li>usb2rs232-0403-6001-ftdi-1</li>
     * </ul>
     *
     * <p>For given Name of port on terminal's pedestal</p>
     * <ul>
     *   <li><B>Type5:</B> There are 2 serial ports on terminal with pedestal</li>
     *   <li>pedestal-rs232</li>
     *   <li>pedestal-pinpad</li>
     * </ul>
     *
     * <p>For given Name of port on wireless base</p>
     * <ul>
     *   <li><B>Type6:</B> There are 2 serial ports on wireless base</li>
     *   <li>wireless-rs232</li>
     *   <li>wireless-pinpad</li>
     * </ul>
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.ISerialPort}
     */

    ISerialPort getSerialPort(String deviceType);
    
    /**
     * <p> get the IScanner interface object for scanner
     *
     * @param cameraId 1:set front scanner, 0:set rear scanner
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IScanner}
     * @
     */
    IScanner getScanner(int cameraId);

    /**
     * <p> get the IMagCardReader interface object for magnetic card reader
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader}
     * @
     */
    IMagCardReader getMagCardReader();

    
    /**
     * <p> get the IRFCardReader interface object for CTLS card
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader}
     * @
     */
    IRFCardReader getRFCardReader();
    
    /**
     * <p>kapId get IPinpad interface object for Pinpad
     *
     * @param kapId : the index refer the keys set
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IPinpad}
     * @
     */
    IPinpad getPinpad(int kapId);
    
    /**
     * <p> get IPrinter interface object for printer
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IPrinter}
     * @
     */
    IPrinter getPrinter();

     /**
     * <p>  get IExternalSerialPort interface object for external serial port
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IExternalSerialPort}
     * @
     */
    IExternalSerialPort getExternalSerialPort();

    /**
     * <p> get the usb-serial device(such as X9, C520H) connect with OTG cable
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort}
     * @
     */
    IUsbSerialPort getUsbSerialPort();

    /**
     * <p>slotNo slotNo value as follow:
     * <ul>
     *     <li>{@code 0}:IC card slot</li>
     *     <li>{@code 1}:SAM1 card slot</li>
     *     <li>{@code 2}:SAM2 card slot</li>
     * </ul>
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader}
     * @
     */
    ISmartCardReader getSmartCardReader(int slotNo);

    /**
     * <p> get IEMV interface object.
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IEMV}
     *
     */
    IEMV getEMV();

    /**
     * <p> get IDukpt interface object.
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IDukpt}
     *
     */
    IDukpt getDUKPT();

    /**
     * <p> get IFelica interface object
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IFelica}
     *
     */
    IFelica getFelica();

    /**
     * <p> get IUtils interface object
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IUtils}
     *
     */
    IUtils getUtils();

    /**
     * <p> get IKLD interface object
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IKLD}
     *
     */
    IKLD getIKLD();

    /**
     * <p> get INtagCard interface object
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.INtagCard}
     *
     */
    INtagCard getNtag();

    /**
     * <p> get ICode interface object
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IICodeCard}
     *
     */
    IICodeCard getICode();

    /**
     * <p> get UltraLightCard interface object
     * Note:
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IUltraLightCard}
     *
     */
    IUltraLightCard getUtrlLightManager();

    /**
     * <p> get IRSA interface object
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IRSA}
     *
     */
    IRSA getIRSA();

    /**
     * <p> get UltraLightCardEV1 interface object
     * Note:
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IUltraLightEV1Card}
     *
     */
    IUltraLightCardEV1 getUtrlLightEV1Manager();

    /**
     * <p> get UltraLightCCard interface object
     * Note:
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IUltraLightCCard}
     *
     */
    IUltraLightCardC getUtrlLightCManager();

    /**
     * <p> get UltraLightCard interface object
     * Note:
     *
     * @return {@link com.dreamtek.smartpos.deviceservice.aidl.IUltraLightNANOCard}
     *
     */
    IUltraLightCardNano getUtrlLightNanoManager();

    IWirelessBaseHelper getWirelessBaseHelper();

    ISde getSde();

    IMKSK getMKSK();

    IServiceInfo getServiceInfo();
}
