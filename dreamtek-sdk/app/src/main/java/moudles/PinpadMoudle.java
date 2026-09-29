package moudles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Button;

import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.KeyCoorInfo;
import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;
import com.dreamtek.smartpos.deviceservice.aidl.RFSearchListener;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IKLD;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IMKSK;
import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import Utils.LogUtil;
import Utils.LogUtils;
import Utils.TDESUtils;
import base.MyApplication;
import view.custompinpad.InputPinActivity;
import view.custompinpad.InputPinParameterCache;
import view.custompinpad.PinPadInitPinInputCustomViewParamIn;

/**
 * Created by WenpengL1 on 2016/12/28.
 */
public class PinpadMoudle {
    Context context;
    IMKSK imksk;
    IDukpt iDukpt;
    IRFCardReader irfCardReader;
    ISerialPort iSerialport;

    IPinpad iPinpad;
    IEMV iemv;
    IKLD ikld;
    IUsbSerialPort iUsbSerialPort;
    LogUtils logUtils;
    public Button btn[] = new Button[10];
    public Button btn_conf, btn_cancel, btn_del;
    private Map<String, Button> keysMap = new HashMap<>();

    static CountDownLatch countDownLatch;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> startPinInput = new ArrayList<String>();
    ArrayList<String> stopPinInput = new ArrayList<String>();
    ArrayList<String> initPinInputCustomView = new ArrayList<String>();
    ArrayList<String> startPinInputCustomView = new ArrayList<String>();
    ArrayList<String> endPinInputCustomView = new ArrayList<String>();


    public static final String BUNDLE_PINPARAM_ISONLINE = "isOnline";
    public static final String BUNDLE_PINPARAM_PAN = "pan";
    public static final String BUNDLE_PINPARAM_PINLIMIT = "pinLimit";
    public static final String BUNDLE_PINPARAM_TIMEOUT = "timeout";
    public static final String BUNDLE_PINPARAM_DESTYPE = "desType";
    public static final String BUNDLE_PINPARAM_PROMPTSTR = "promptString";
    public static final String BUNDLE_GLOBALPARAM_DISPONE = "Display_One";
    public static final String BUNDLE_GLOBALPARAM_DISPTWO = "Display_Two";
    public static final String BUNDLE_GLOBALPARAM_DISPTHR = "Display_Three";
    public static final String BUNDLE_GLOBALPARAM_DISPFOU = "Display_Four";
    public static final String BUNDLE_GLOBALPARAM_DISPFIV = "Display_Five";
    public static final String BUNDLE_GLOBALPARAM_DISPSIX = "Display_Six";
    public static final String BUNDLE_GLOBALPARAM_DISPSEV = "Display_Seven";
    public static final String BUNDLE_GLOBALPARAM_DISPEIG = "Display_Eight";
    public static final String BUNDLE_GLOBALPARAM_DISPNIN = "Display_Nine";
    public static final String BUNDLE_GLOBALPARAM_DISPZER = "Display_Zero";
    public static final String BUNDLE_GLOBALPARAM_DISPCON = "Display_Confirm";
    public static final String BUNDLE_GLOBALPARAM_DISPBAC = "Display_BackSpace";
    private final String TAG = "PinPadManager";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //            Log.i(TAG, "msg:" + msg.getData().getString("msg"));
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
            logUtils.addCaseLog(msg.getData().getString("message"));
            logUtils.showCaseLog();
        }
    };

    public PinpadMoudle(Context context, IPinpad iPinpad, IMKSK imksk, IDukpt iDukpt,
                        IRFCardReader irfCardReader, ISerialPort iSerialport, IEMV iemv,
                        IUsbSerialPort iUsbSerialPort, IKLD ikld) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.imksk = imksk;
        this.iDukpt = iDukpt;
        this.irfCardReader = irfCardReader;
        this.iSerialport = iSerialport;
        this.iemv = iemv;
        this.iUsbSerialPort = iUsbSerialPort;
        this.ikld = ikld;
        this.iPinpad =iPinpad;
        addAllapi();
        countDownLatch = new CountDownLatch(1);
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.PinpadMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    if (i.getName().startsWith("L01")) {
                        startPinInput.add(i.getName());
                    } else if (i.getName().startsWith("L02")) {
                        stopPinInput.add(i.getName());
                    } else if (i.getName().startsWith("L03")) {
                        initPinInputCustomView.add(i.getName());
                    } else if (i.getName().startsWith("L04")) {
                        startPinInputCustomView.add(i.getName());
                    } else if(i.getName().startsWith("L05")){
                        endPinInputCustomView.add(i.getName());
                    }
                }
            }

            caseNames.add(startPinInput);
            caseNames.add(stopPinInput);
            caseNames.add(initPinInputCustomView);
            caseNames.add(startPinInputCustomView);
            caseNames.add(endPinInputCustomView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.PinpadMoudle");
            Log.i("aClass.getMethod", name);
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + " The test case is completed. \n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }
    public ArrayList<String> getApiList() {
        return apiList;
    }
    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }
    /**
     * @param keyId
     * @param key
     * @param algorithmType
     *   	            0x02-3des ecb algorithm<BR> 0x04-SM4 ecb algorithm<BR> 0x06-AES ecb algorithm<BR>
     *   	            0x82-3des cbc algorithm<BR> 0x84-SM4 cbc algorithm<BR> 0x86-AES cbc algorithm<BR>
     * @param checkValue
     */
    private void loadPlainMasterKey(int keyId, byte[] key, int algorithmType, byte[] checkValue) {
        try {
            boolean ret = imksk.loadPlainMasterKey(keyId, key, algorithmType, checkValue);
            logUtils.addCaseLog("loadPlainMasterKey ret=" + ret);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * load the work key given decrypt type
     *
     * @param keyType select the workkey type<BR>
     *     |---1-MAC key, 2-PIN key, 3-TD key<BR>
     *     |---5-(SM4)MAC key, 6-(SM4)PIN key, 7-(SM4)TD key<BR>
     *     |---9-(AES)MAC key, 10-(AES)PIN key, 11-(AES)TD key<BR>
     * @param mkId the id of master key for decrypt work key
     * @param wkId set the workkey id (index 0~254)
     * @param decKeyType select decrypt key type<BR>
     *     |---0x00-3DES master key<BR>
     *     |---0x01-transport key<BR>
     *     |---0x02-SM4 master key<BR>
     *     |---0x03-AES master key<BR>
     *     |---0x04-SM4 transport key<BR>
     *     |---0x05-AES transport key<BR>
     *     |---0x80-CBC 3DES master key<BR>
     *     |---0x81-CBC transport key<BR>
     *     |---0x82-CBC SM4 master key<BR>
     *     |---0x83-CBC AES master key<BR>
     *     |---0x84-CBC SM4 transport key<BR>
     *     |---0x85-CBC AES transport key<BR>
     * @param key
     * @param checkValue check value (null for none)
     * @param extend - extend param
     * <ul>
     *     <li>isCBCType(boolean) judge the mk encrypt mode whether is CBC mode(default false)</li>
     *     <li>initVec(byte[]) cbc initVec(default 16byte 0)</li>
     * </ul>
     * @return true on success, false on failure
     */
    private void loadEncryptedPinKey(int keyType, int mkId, int wkId, int decKeyType, byte[] key, byte[] checkValue, android.os.Bundle extend) {
        try {
            boolean ret = imksk.loadSessionKey(keyType, mkId, wkId, decKeyType, key, checkValue, extend);
            logUtils.addCaseLog("loadSessionKey ret=" + ret);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFixedPinKeyInKeyIndex99() {
        String clearPinKeyHex = "30405C4387CF9AB69E25B2DBA6481261";
        String clearMasterKeyHex = "31313131313131313131313131313131";
        byte[] clearPinKey = StringUtil.hexStr2Bytes(clearPinKeyHex);
        byte[] clearMasterKey = StringUtil.hexStr2Bytes(clearMasterKeyHex);
        byte[] encryptedPinKey = TDESUtils.encrypt3DESByECBMode(clearMasterKey, clearPinKey);
        String encryptedPinKeyHex = StringUtil.byte2HexStr(encryptedPinKey);

        int masterKeyIdx = 1;
        int pinKeyIdx = 99;
        int TDES_ECB = 0x02;
        loadPlainMasterKey(masterKeyIdx, clearMasterKey, TDES_ECB, null);
        int PIN_KEY = 0x02;
        int TDES_MASTER_KEY = 0x00;
        loadEncryptedPinKey(PIN_KEY, masterKeyIdx, pinKeyIdx, TDES_MASTER_KEY, encryptedPinKey, null, new Bundle());
    }

//    private void loadAESPinKeyInKeyIndex99() {
//        String clearPinKeyHex = "30405C4387CF9AB69E25B2DBA6481261";
//        String clearMasterKeyHex = "31313131313131313131313131313131";
//        byte[] clearPinKey = StringUtil.hexStr2Bytes(clearPinKeyHex);
//        byte[] clearMasterKey = StringUtil.hexStr2Bytes(clearMasterKeyHex);
//        byte[] encryptedPinKey = TDESUtils.encrypt3DESByECBMode(clearMasterKey, clearPinKey);
//        String encryptedPinKeyHex = StringUtil.byte2HexStr(encryptedPinKey);
//
//        int masterKeyIdx = 1;
//        int pinKeyIdx = 99;
//        int TDES_ECB = 0x06;
//        loadPlainMasterKey(masterKeyIdx, clearMasterKey, TDES_ECB, null);
//        int PIN_KEY = 0x10;
//        int TDES_MASTER_KEY = 0x03;
//        loadEncryptedPinKey(PIN_KEY, masterKeyIdx, pinKeyIdx, TDES_MASTER_KEY, encryptedPinKey, null, new Bundle());
//    }

    /**
     * clear M/S key
     *
     * @param keyId clear key id
     * @param keyType
     * <ul>
     * <li> 0x00-DES MK</li>
     * <li> 0x01-SM4 MK</li>
     * <li> 0x02-AES MK</li>
     * <li> 0x10-DES PIN</li>
     * <li> 0x11-SM4 PIN</li>
     * <li> 0x12-AES PIN</li>
     * <li> 0x20-DES MAC</li>
     * <li> 0x21-SM4 MAC</li>
     * <li> 0x22-AES MAC</li>
     * <li> 0x30-DES DATA</li>
     * <li> 0x31-SM4 DATA</li>
     * <li> 0x32-AES DATA</li>
     * <li> 0x40-DES TEK</li>
     * <li> 0x41-AES TEK</li>
     * </ul>
     * @param extend - extend param for the future
     */
    private void clearKey(int keyId, int keyType, android.os.Bundle extend) {
        try {
            boolean ret = imksk.clearKey(keyId, keyType,  extend);
            logUtils.addCaseLog("clearKey ret=" + ret);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearDesPinKey(int keyId) {
        clearKey(keyId, 0x10, new Bundle());
    }

    public void L01001() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0,4,6,8};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01002() {
        int keyId = 99;
        clearDesPinKey(keyId);

        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01003() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 255;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01004() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = -1;

        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01005() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01006() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01007() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;

        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", " ");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01008() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", null);
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01009() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01010() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
//        byte[] pinLimit = {0, 4};
        param.putByteArray("pinLimit", null);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01011() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01012() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01013() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01014() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }


    public void L01015() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01016() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 5);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }


    public void L01017() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
//        param.putInt("timeout", 5);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01018() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01019() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", -1);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01020() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
//        My01startPinInput(keyId, param, globalparam);
        try {
            iPinpad.startPinInput(keyId, param, globalparam, new PinInputListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {

                }

                @Override
                public void onConfirm(android.os.Bundle pinInfos) throws RemoteException {
                    Log.i(TAG, "isByPass:" + pinInfos.getBoolean("isByPass"));
                    byte[] encryptedPinBlock = pinInfos.getByteArray("pinblock");
                    boolean isByPass = pinInfos.getBoolean("isByPass");
                    Message msg = new Message();
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(encryptedPinBlock));
                    msg.getData().putString("msg", "isByPass:" + isByPass + "\nPIN: " + StringUtil.byte2HexStr
                            (encryptedPinBlock));
                    handler.sendMessage(msg);

                    // My19getDukptKsn();
                }

                @Override
                public void onCancel() throws RemoteException {

                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    logUtils.addCaseLog("onError errorCode=" + errorCode);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void L01021() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
//        My01startPinInput(keyId, param, globalparam);
        try {
            iPinpad.startPinInput(keyId, param, globalparam, new PinInputListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {

                }

                @Override
                public void onConfirm(android.os.Bundle pinInfos) throws RemoteException {

                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG, "onCancel Cancel PIN Input");
                    Message msg = new Message();
                    msg.getData().putString("msg", "onCancel Cancel PIN Input");
                    handler.sendMessage(msg);


                    if (countDownLatch!=null){
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    logUtils.addCaseLog("onError errorCode=" + errorCode);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void L01022() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
//        My01startPinInput(keyId, param, globalparam);
        try {
            iPinpad.startPinInput(keyId, param, globalparam, null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

//    public void L01023() {

//        loadAESPinKeyInKeyIndex99();
//        int keyId = 99;
//        Bundle param = new Bundle();
//        Bundle globalparam = new Bundle();
//        byte[] pinLimit = {0, 6};
//        param.putByteArray("pinLimit", pinLimit);
//        param.putInt("timeout", 0);
//        param.putBoolean("isOnline", true);
//        param.putString("promptString", "please input your cardpin:");
//        param.putString("pan", "6226901508781352");
//        param.putInt("desType", 2);//AES
//        param.putString("numbersFont", "");
//        param.putString("promptsFont", "");
//        param.putString("otherFont", "");
//        param.putByteArray("displayKeyValue", null);
//
//        globalparam.putString("Display_One", null);
//        globalparam.putString("Display_Two", null);
//        globalparam.putString("Display_Three", null);
//        globalparam.putString("Display_Four", null);
//        globalparam.putString("Display_Five", null);
//        globalparam.putString("Display_Six", null);
//        globalparam.putString("Display_Seven", null);
//        globalparam.putString("Display_Eight", null);
//        globalparam.putString("Display_Nine", null);
//        globalparam.putString("Display_Zero", null);
//        globalparam.putString("Display_Confirm", null);
//        globalparam.putString("Display_BackSpace", null);
//        My01startPinInput(keyId, param, globalparam);
//    }

    public void L01025() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {12};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01027() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {7};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01028() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {8};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01029() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
//        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {7};
//        param.putByteArray("pinLimit", pinLimit);
//        param.putInt("timeout", 0);
//        param.putBoolean("isOnline", true);
//        param.putString("promptString", "please input your cardpin:");
//        param.putString("pan", "6226901508781352");
//        param.putInt("desType", 1);//3DES
//        param.putString("numbersFont", "");
//        param.putString("promptsFont", "");
//        param.putString("otherFont", "");
//        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
//        My01startPinInput(keyId, null, globalparam);
        try {
            iPinpad.startPinInput(keyId, null, globalparam, new PinInputListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {

                }

                @Override
                public void onConfirm(Bundle pinInfos) throws RemoteException {

                }

                @Override
                public void onCancel() throws RemoteException {

                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    logUtils.addCaseLog("onError errorCode=" + errorCode);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void L01030() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {9};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01031() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {10};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01032() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {11};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01033() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {-1};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01034() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {13};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01035() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 9;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01037() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
//        My01startPinInput(keyId, param, globalparam);
        try {
            iPinpad.startPinInput(keyId, param, globalparam, new PinInputListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {

                }

                @Override
                public void onConfirm(android.os.Bundle pinInfos) throws RemoteException {
                    Log.i(TAG, "isByPass:" + pinInfos.getBoolean("isByPass"));
                    byte[] encryptedPinBlock = pinInfos.getByteArray("pinblock");
                    boolean isByPass = pinInfos.getBoolean("isByPass");
                    Message msg = new Message();
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(encryptedPinBlock));
                    msg.getData().putString("msg", "isByPass:" + isByPass + "\nPIN: " + StringUtil.byte2HexStr
                            (encryptedPinBlock));
                    handler.sendMessage(msg);

                    // My19getDukptKsn();
                }

                @Override
                public void onCancel() throws RemoteException {

                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    logUtils.addCaseLog("onError errorCode=" + errorCode);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void L01038() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01039() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01040() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01041() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01042() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }



    public void L01043() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {-1,0,1,2,3,4,5,6,7,8,9,10,11,12,13};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01044() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01045() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {1,2,3,5};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01046() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

//        globalparam.putString("Display_One", null);
//        globalparam.putString("Display_Two", null);
//        globalparam.putString("Display_Three", null);
//        globalparam.putString("Display_Four", null);
//        globalparam.putString("Display_Five", null);
//        globalparam.putString("Display_Six", null);
//        globalparam.putString("Display_Seven", null);
//        globalparam.putString("Display_Eight", null);
//        globalparam.putString("Display_Nine", null);
//        globalparam.putString("Display_Zero", null);
//        globalparam.putString("Display_Confirm", null);
//        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, null);
    }
    public void L01047() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

//        globalparam.putString("Display_One", null);
//        globalparam.putString("Display_Two", null);
//        globalparam.putString("Display_Three", null);
//        globalparam.putString("Display_Four", null);
//        globalparam.putString("Display_Five", null);
//        globalparam.putString("Display_Six", null);
//        globalparam.putString("Display_Seven", null);
//        globalparam.putString("Display_Eight", null);
//        globalparam.putString("Display_Nine", null);
//        globalparam.putString("Display_Zero", null);
//        globalparam.putString("Display_Confirm", null);
//        globalparam.putString("Display_BackSpace", null);
        globalparam.putString(" ", " ");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01048() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "The PIN pad is ready. Enter the online PIN first, then enter the offline PIN.");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01049() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6228480039042651172");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01050() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "62284800390426511720");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01051() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "622848003904");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01052() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "62284800390");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01053() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "%@@@@!!!!!!~~~~~~~1234");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }


    public void L01054() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 0);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01055() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 5);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01056() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "62284800390");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01058() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, null);
    }

    public void L01059() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", "A");
        globalparam.putString("Display_Two", "B");
        globalparam.putString("Display_Three", "C");
        globalparam.putString("Display_Four", "D");
        globalparam.putString("Display_Five", "E");
        globalparam.putString("Display_Six", "F");
        globalparam.putString("Display_Seven", "G");
        globalparam.putString("Display_Eight", "H");
        globalparam.putString("Display_Nine", "I");
        globalparam.putString("Display_Zero", "J");
        globalparam.putString("Display_Confirm", "X");
        globalparam.putString("Display_BackSpace", "Y");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01060() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", "one");
        globalparam.putString("Display_Two", "two");
        globalparam.putString("Display_Three", "three");
        globalparam.putString("Display_Four", "four");
        globalparam.putString("Display_Five", "five");
        globalparam.putString("Display_Six", "six");
        globalparam.putString("Display_Seven", "seven");
        globalparam.putString("Display_Eight", "eight");
        globalparam.putString("Display_Nine", "nine");
        globalparam.putString("Display_Zero", "zero");
        globalparam.putString("Display_Confirm", "confirm");
        globalparam.putString("Display_BackSpace", "backspace");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01061() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", "1");
        globalparam.putString("Display_Two", "2");
        globalparam.putString("Display_Three", "3");
        globalparam.putString("Display_Four", "4");
        globalparam.putString("Display_Five", "5");
        globalparam.putString("Display_Six", "6");
        globalparam.putString("Display_Seven", "7");
        globalparam.putString("Display_Eight", "8");
        globalparam.putString("Display_Nine", "9");
        globalparam.putString("Display_Zero", "10");
        globalparam.putString("Display_Confirm", "Confirm");
        globalparam.putString("Display_BackSpace", "BackSpace");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01062() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01063() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", "");
        globalparam.putString("Display_Two", "");
        globalparam.putString("Display_Three", "");
        globalparam.putString("Display_Four", "");
        globalparam.putString("Display_Five", "");
        globalparam.putString("Display_Six", "");
        globalparam.putString("Display_Seven", "");
        globalparam.putString("Display_Eight", "");
        globalparam.putString("Display_Nine", "");
        globalparam.putString("Display_Zero", "");
        globalparam.putString("Display_Confirm", "");
        globalparam.putString("Display_BackSpace", "");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01064() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", null);
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01065() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "/system/fonts/Roboto-Italic.ttf");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01066() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "system/fonts/abcd.ttf");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01067() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", null);
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01068() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "system/fonts/Roboto-Italic.ttf");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01069() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "system/fonts/abcd.ttf");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01070() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", null);
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01071() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "system/fonts/Roboto-Italic.ttf");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01072() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "system/fonts/abcd.ttf");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01073() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "f");
        byte[] displayKeyValue = {};
        param.putByteArray("displayKeyValue", displayKeyValue);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01074() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "f");
        byte[] displayKeyValue = {0,1,2,3,4,5,6,7,8,9};
        param.putByteArray("displayKeyValue", displayKeyValue);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01075() {

        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "f");
        byte[] displayKeyValue = {9,8,7,6,0,5,4,3,2,1};
        param.putByteArray("displayKeyValue", displayKeyValue);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L02001() {
        My02stopPinInput();
    }

    public void L02002() {
        loadFixedPinKeyInKeyIndex99();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);

        globalparam.putString("Display_One", null);
        globalparam.putString("Display_Two", null);
        globalparam.putString("Display_Three", null);
        globalparam.putString("Display_Four", null);
        globalparam.putString("Display_Five", null);
        globalparam.putString("Display_Six", null);
        globalparam.putString("Display_Seven", null);
        globalparam.putString("Display_Eight", null);
        globalparam.putString("Display_Nine", null);
        globalparam.putString("Display_Zero", null);
        globalparam.putString("Display_Confirm", null);
        globalparam.putString("Display_BackSpace", null);
        try {
            iPinpad.startPinInput(keyId, param, globalparam, new PinInputListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {
                    My02stopPinInput();
                }

                @Override
                public void onConfirm(Bundle pinInfos) throws RemoteException {

                }

                @Override
                public void onCancel() throws RemoteException {

                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    logUtils.addCaseLog("onError errorCode=" + errorCode);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }



    public void L03001() {
        int keyId = 99;
        loadFixedPinKeyInKeyIndex99();
        PinPadInitPinInputCustomViewParamIn paramIn = new PinPadInitPinInputCustomViewParamIn();
        paramIn.setKeyId(keyId);

        paramIn.setPinLimit(new byte[] {0, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        paramIn.setTimeout(60);
        paramIn.setOnlineState(true);
        paramIn.setPan("6226901508781352");
        paramIn.setKeyType(PinPadInitPinInputCustomViewParamIn.TDES_TYPE);
        paramIn.setDesType(0x01); // 3DES MK/SK
        paramIn.setKeyboardNumberPosition(null);

        InputPinParameterCache.setPinPadInitPinInputCustomViewParamIn(paramIn);
        Intent intent = new Intent(context, InputPinActivity.class);
        context.startActivity(intent);
    }

    private void My01startPinInput(int keyId, Bundle param, Bundle globalparam) {
        try {
            iPinpad.startPinInput(keyId, param, globalparam, new PinInputListener.Stub() {
                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.i(TAG, "len=" + len + " key=" + key);
                    Message msg = new Message();
                    msg.getData().putString("msg", "Password length entered len=" + len + " The current key value=" + key);
                    handler.sendMessage(msg);
                }

                /**
                 * on confirm the PIN
                 *
                 * @param pinInfos the PIN number, null if no pin inputed
                 * <ul>
                 *    <li>pinblock(byte[]) encrypted pin data or plain key if it's offline pin</li>
                 *    <li>ksn(byte[]) ksn</li>
                 *    <li>isByPass(boolean) pin status</li>
                 *    <li>isEncrypt(boolean) encrypt(online)/plain(offline) key</li>
                 * </ul>
                 */
                @Override
                public void onConfirm(android.os.Bundle pinInfos) throws RemoteException {
                    Log.i(TAG, "luoyi");
                    byte[] encryptedPinBlock = pinInfos.getByteArray("pinblock");
                    boolean isByPass = pinInfos.getBoolean("isByPass");
                    Message msg = new Message();
                    msg.getData().putString("msg", "onConfirm\nisByPass=" + isByPass + "\nencryptedPinBlock=" + StringUtil.byte2HexStr(encryptedPinBlock) + "\n");
                    handler.sendMessage(msg);
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(encryptedPinBlock));


                    if (countDownLatch!=null){
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG, "onCancel Cancel PIN Input");
                    Message msg = new Message();
                    msg.getData().putString("msg", "onCancel Cancel PIN Input");
                    handler.sendMessage(msg);


                    if (countDownLatch!=null){
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    Log.i(TAG, "onError:" + errorCode);
                    Message msg = new Message();
                    msg.getData().putString("msg", "onError:" + errorCode);
                    handler.sendMessage(msg);


                    if (countDownLatch!=null){
                        countDownLatch.countDown();
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void My02stopPinInput(){

        Log.i(TAG, "My02stopPinInput() executed");
        try {
            iPinpad.stopPinInput();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("stopPinInput test passed");
    }


    public class Mypinpadlistener extends PinInputListener.Stub {
        @Override
        public void onInput(int len, int key) throws RemoteException {
            Log.i(TAG, "len=" + len + " key=" + key);
            Message msg = new Message();
            msg.getData().putString("msg", "Entered PIN length=" + len + "; current key=" + key);
            handler.sendMessage(msg);
        }

        @Override
        public void onConfirm(android.os.Bundle pinInfos) throws RemoteException {
            Log.i(TAG, "isByPass:" + pinInfos.getBoolean("isByPass"));
            byte[] encryptedPinBlock = pinInfos.getByteArray("pinblock");
            boolean isByPass = pinInfos.getBoolean("isByPass");
            Message msg = new Message();
            Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(encryptedPinBlock));
            msg.getData().putString("msg", "isByPass:" + isByPass + "\nPIN: " + StringUtil.byte2HexStr
                    (encryptedPinBlock));
            handler.sendMessage(msg);

            // My19getDukptKsn();
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            Log.i(TAG, "onError:" + errorCode);
            Message msg = new Message();
            msg.getData().putString("msg", "onError:" + errorCode);
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel() throws RemoteException {
            Log.i(TAG, "onCancel: PIN entry canceled");
            Message msg = new Message();
            msg.getData().putString("msg", "onCancel: PIN entry canceled");
            handler.sendMessage(msg);
        }
    }

    private Map My03initPinInputCustomView(int keyId, Bundle param, List<PinKeyCoorInfo> pinKeyInfos, Mypinpadlistener listener) {
        Map initView = null;
        try {
            initView = iPinpad.initPinInputCustomView(keyId, param, pinKeyInfos, listener);
            if (initView == null) {
                logUtils.addCaseLog("Failed to initialize the custom keyboard interface");
                String errString = imksk.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("The custom keyboard interface was initialized successfully");
                Set<Map.Entry<String, String>> entrys = initView.entrySet();
                for (Map.Entry<String, String> entry : entrys) {
                    logUtils.addCaseLog(entry.getKey() + " display--> " + entry.getValue());
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return initView;
    }

    private void My04startPinInputCustomView() {
        try {
            long startTime = System.currentTimeMillis();
            iPinpad.startPinInputCustomView();
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("startPinInputCustomView executeTime : " + (endTime - startTime) + " ms");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void My05endPinInputCustomView(){
        try {
            iPinpad.endPinInputCustomView();
        } catch (Exception e) {
            logUtils.addCaseLog(e.getMessage());
            e.printStackTrace();
        }
    }
}