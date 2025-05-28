package moudles.newModules;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import moudles.PinpadMoudle;

/**
 * Created by RuihaoS on 2021/5/8.
 */
public class PinpadModule extends TestModule {
    public Button btn[] = new Button[10];
    public Button btn_conf, btn_cancel, btn_del;
    private Map<String, Button> keysMap = new HashMap<>();

    //TEK
    byte[] tekKeyCheckValue = {(byte) 0x73, (byte) 0xB8, (byte) 0x17, (byte) 0xC3, (byte) 0x02, (byte) 0xAB, (byte) 0xE8, (byte) 0x5B};
    //    String tekKeyString = "2494E889B78C6F895593728E4990EE21";
    String tekKeyString = "3CAEFDD46D32C796059FBA457C7D5D1E3CAEFDD46D32C796";
    byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);

    //明文主密钥
    byte[] mKeyCheckValue = {(byte) 0xD2, (byte) 0xB9, (byte) 0x1C, (byte) 0xC5, (byte) 0xA7, (byte) 0x58, (byte) 0xBB, (byte) 0x08};
    String mKeyString = "111111111111111122222222222222221111111111111111";
    byte[] mKey = BCDDecode.str2Bcd(mKeyString);

    //密文主密钥
    String mEKeyString = "FCAD332CE832FC41C22B74F0FC49271B3131313131313131";
    //用TEK解出的明文主密钥 79926203906BA05E942F15B3870A9A8637D4201B41001791
    String mEKeyCheckValueString = "964A50B457B998E7";
    byte[] mEKey = BCDDecode.str2Bcd(mEKeyString);
    byte[] mEKeyCheckValue = BCDDecode.str2Bcd(mEKeyCheckValueString);

    //MAC Key
    String macKeyString = "b740ce7c2fb7fcd8808b5f288eecc6b4b740ce7c2fb7fcd8";
    //    String macKeyString = "E2E2F4834C77E7F2E2E2F4834C77E7F2";
    String macKeyCheckValueString = "F0467E2BC67CDC70";
    //用主密钥111111111111111122222222222222221111111111111111对MacKey进行解密的值对8个字节的0进行加密得到checkValue
    byte[] macKey = BCDDecode.str2Bcd(macKeyString);
    byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueString);

    //PIN Key
    String pinKeyString = "B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E";
    //    String pinKeyString = "89B07B35A1B3F47E89B07B35A1B3F488";
    String pinKeyCheckValueString = "A677495148115613";
    byte[] pinKey = BCDDecode.str2Bcd(pinKeyString);
    byte[] pinKeyCheckValue = BCDDecode.str2Bcd(pinKeyCheckValueString);

    //TD Key
    String tdKeyString = "42527dea2a1553e1211b0ea0d459076642527dea2a1553e1";
    //    String tdKeyString = "088CAED653BCAAA368FCC0118AD7D337";
    String tdKeyCheckValueString = "A9AA621560D74109";
    byte[] tdKey = BCDDecode.str2Bcd(tdKeyString);
    byte[] tdKeyCheckValue = BCDDecode.str2Bcd(tdKeyCheckValueString);

    //Dukpt Key
    String dukptksnString = "01020304050607080901";
    byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
    String dukptkeyString = "343434343434343435353535353535353434343434343434";
    byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
    String dukptkeycheckvalueString = "F39941C42F44E374";
    byte[] dukptkeycheckvalue = BCDDecode.str2Bcd(dukptkeycheckvalueString);

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
    private byte[] data;


//    void FormatKeyArea() {
//        //格式化密钥区
//        try {
////            boolean ret = SdkApiHolder.getInstance().getKeyManager().formatKeyArea();
//            boolean ret = Ped.getInstance().formatKeyArea();
//            Log.i(TAG, "formatKeyArea=" + ret);
//            if (ret) {
//                logUtils.addCaseLog("Format key area successfully");
//            } else {
//                logUtils.addCaseLog("Format key area failed");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//        /* catch (PinPadException e) {
//            e.printStackTrace();
//            return;
//        } catch (SDKException e) {
//            e.printStackTrace();
//            return;
//        }*/
//    }

/*
    public byte[] T_getDukptKsn(int keyIdx) {
        byte[] result = null;
        try {
            result = iDukpt.getDukptKsn(keyIdx);
            if (result == null) {
                logUtils.addCaseLog("getKSN failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("Result：getKSN failed，error=" + errString, Log.ERROR);
                }
            } else {
                logUtils.addCaseLog("getKSN success");
                logUtils.addCaseLog("KSN: " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result：getKSN success，result=" + StringUtil.byte2HexStr(result), Log.INFO);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean T_isKeyExist(String skeyId, String skeyType) throws RemoteException {
        boolean ret = false;
        String keyTypeString = null;
        int keyId = 0, keyType = 0;
//        try {
        keyId = Integer.valueOf(skeyId);
        keyType = Integer.valueOf(skeyType);
//        } catch (NumberFormatException e) {
//            logUtils.addCaseLog("param parse exception");
//        }
//        if (keyType == PinpadKeyType.MASTERKEY)
//            keyTypeString = "Master key ";
//        else if (keyType == PinpadKeyType.DUKPTKEY)
//            keyTypeString = "dukpt key";
//        else
//            keyTypeString = "work key";
//        try {
        ret = iPinpad.isKeyExist(keyType, keyId);
        if (ret) {
//                logUtils.addCaseLog(keyTypeString + "[" + keyId + "] exist");
//                this.printMsgTool("Result：" + keyTypeString + "[" + keyId + "]exist");
        } else {
//                logUtils.addCaseLog(keyTypeString + "[" + keyId + "]no-exist");
//                String errString = iPinpad.getLastError();
//                this.printMsgTool("Result：" + keyTypeString + "[" + keyId + "]no-exist");
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
        }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        return ret;
    }

    public boolean T_loadTEK(String skeyId, String skey, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            ret = iPinpad.loadTEK(keyId, key, checkValue);
            if (ret) {
                logUtils.addCaseLog("The plaintext transfer key was downloaded successfully");
                this.printMsgTool("Execution result: download plaintext transfer key successfully");
            } else {
                logUtils.addCaseLog("Failed to download the plaintext transfer key");
                String errString = iPinpad.getLastError();
                this.printMsgTool("Execution result：Failed to download the plaintext transfer key");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    public boolean T_loadEncryptMainKey(String skeyId, String skey, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            ret = iPinpad.loadEncryptMainKey(keyId, key, checkValue);
            if (ret) {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
                this.printMsgTool("Execution result：Download the ciphertext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                this.printMsgTool("Execution result：Download the ciphertext master key[" + keyId + "]failed");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private boolean T_loadMainKey(String skeyId, String skey, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            ;
            ret = iPinpad.loadMainKey(keyId, key, checkValue);
            if (ret) {
                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]successfully");
                this.printMsgTool("Execution result：Download the plaintext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                this.printMsgTool("Execution result：Download the plaintext master key[" + keyId + "]failed");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private boolean T_loadWorkKey(String skeyType, String smkId, String swkId, String skey, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(smkId) || TextUtils.isEmpty(swkId) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int mkId = Integer.valueOf(smkId);
            int keyType = Integer.valueOf(skeyType);
            int wkId = Integer.valueOf(swkId);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);

            ret = iPinpad.loadWorkKey(keyType, mkId, wkId, key, checkValue);
            if (ret) {
                logUtils.addCaseLog("Download the work key[" + wkId + "]successfully");
                this.printMsgTool("Execution result：Download the work key[" + wkId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the work key[" + wkId + "]failed");
                String errString = iPinpad.getLastError();
                this.printMsgTool("Execution result：Download the work key[" + wkId + "]failed");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private byte[] T_calcMAC(String skeyId, String sdata) {
        byte[] MyCalcMAC = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sdata)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            MyCalcMAC = iPinpad.calcMAC(keyId, data);
            if (MyCalcMAC == null) {
                logUtils.addCaseLog("Failed to compute MAC");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
                this.printMsgTool("Execution result: The calculation of MAC failed" + "getLastError=" + errString);

            } else {
                logUtils.addCaseLog("Calculating Mac Success");
                logUtils.addCaseLog("MAC: " + StringUtil.byte2HexStr(MyCalcMAC));
                this.printMsgTool("Execution result: Calculate MAC successfully" + StringUtil.byte2HexStr(MyCalcMAC));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return MyCalcMAC;
    }

    private byte[] T_encryptTrackData(String smode, String skeyId, String strack) {
        byte[] MyEncryptTrackData = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(smode) || TextUtils.isEmpty(strack)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int mode = Integer.valueOf(smode);
            byte[] track = StringUtil.hexStr2Bytes(strack);
            MyEncryptTrackData = iPinpad.encryptTrackData(mode, keyId, track);
            if (MyEncryptTrackData == null) {
                logUtils.addCaseLog("Failed to encrypt track data");
                String errString = iPinpad.getLastError();
                this.printMsgTool("Execution result: Encryption track data failed");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("Encrypted track data successfully");
                this.printMsgTool("Execution result: track data encrypted successfully");
                logUtils.addCaseLog("encryptTrackData:");
//                logUtils.addCaseLog(StringUtil.byte2HexStr(BCDDecode.str2Bcd(StringUtil.byteToStr(MyEncryptTrackData))));
                logUtils.addCaseLog(StringUtil.byte2HexStr(MyEncryptTrackData));

            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        }
        return MyEncryptTrackData;
    }

    String pinInputResult = "";

    String T_startPinInput(String skeyId, String sBundle, String sBundle2) {
        pinInputResult = "";
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sBundle)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            pinInputResult = "Invalid parameters";
//            return pinInputResult;
//        }
        try {
            final int keyId = Integer.valueOf(skeyId);
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("pinLimit", BundleConfig_byte_A),
                    new BundleConfig("timeout", BundleConfig_int),
                    new BundleConfig("isOnline", BundleConfig_boolean),
                    new BundleConfig("promptString", BundleConfig_String),
                    new BundleConfig("pan", BundleConfig_String),
                    new BundleConfig("desType", BundleConfig_int),
                    new BundleConfig("pinFormatType", BundleConfig_int),
                    new BundleConfig("dispersionType", BundleConfig_int),
                    new BundleConfig("globleParam", BundleConfig_String),
                    new BundleConfig("numbersFont", BundleConfig_String),
                    new BundleConfig("promptsFont", BundleConfig_String),
                    new BundleConfig("otherFont", BundleConfig_String),
                    new BundleConfig("displayKeyValue", BundleConfig_byte_A),
                    new BundleConfig("random", BundleConfig_byte_A),
                    new BundleConfig("notificatePinLenError", BundleConfig_boolean)
            };
            Bundle param = convert(sBundle, bundleConfigs);

            BundleConfig[] bundleConfigs2 = new BundleConfig[]{
                    new BundleConfig("Display_One", BundleConfig_String),
                    new BundleConfig("Display_Two", BundleConfig_String),
                    new BundleConfig("Display_Three", BundleConfig_String),
                    new BundleConfig("Display_Four", BundleConfig_String),
                    new BundleConfig("Display_Five", BundleConfig_String),
                    new BundleConfig("Display_Six", BundleConfig_String),
                    new BundleConfig("Display_Seven", BundleConfig_String),
                    new BundleConfig("Display_Eight", BundleConfig_String),
                    new BundleConfig("Display_Nine", BundleConfig_String),
                    new BundleConfig("Display_Zero", BundleConfig_String),
                    new BundleConfig("Display_Confirm", BundleConfig_String),
                    new BundleConfig("Display_BackSpace", BundleConfig_String)
            };
            Bundle globalparam;
            if (sBundle2.equals("-")) {
                globalparam = new Bundle();
            } else {
                globalparam = convert(sBundle2, bundleConfigs2);
            }

            iBeeper.startBeep(1000);

            final int desType = param.getInt("desType");


            iPinpad.startPinInput(keyId, param, globalparam, new PinInputListener.Stub() {
                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.i(TAG, "len=" + len + " key=" + key);
                    Message msg = new Message();
                    msg.getData().putString("msg", "Password length entered len=" + len + " The current key value=" + key);
                    handler.sendMessage(msg);
                }

                @Override

                public void onConfirm(@NonNull byte[] data, boolean isNonePin) throws RemoteException {
                    if (desType == 4) {
                        Log.i(TAG, "Get DUKPT KSN");
                        byte[] ksn = T_getDukptKsn(keyId);
                        if (ksn == null) {
                            logUtils.addCaseLog("getKSN failed");
                            String errString = iDukpt.getLastError();
                            if (errString != null) {
                                logUtils.addCaseLog(errString);
                                printMsgTool("Result：getKSN failed，error=" + errString);
                            }
                        } else {
                            Log.i(TAG, "ksn=" + ksn);
                            logUtils.addCaseLog("getKSN success");
                            logUtils.addCaseLog("KSN: " + StringUtil.byte2HexStr(ksn));
                            printMsgTool("Result：getKSN success，result=" + StringUtil.byte2HexStr(ksn));
                        }
                    }


                    Log.i(TAG, "isNonePin:" + isNonePin);
                    Message msg = new Message();
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
//                        logUtils.addCaseLog("PIN: " + StringUtil.byte2HexStr(data));
                    msg.getData().putString("msg", "isNonePin:" + isNonePin + "\nPIN: " + StringUtil.byte2HexStr
                            (data));
                    handler.sendMessage(msg);

                    if (isNonePin) {
                        synchronized (PinpadModule.class) {
                            pinInputResult = "isNonePin";
                        }

                    } else {
                        synchronized (PinpadModule.class) {
                            pinInputResult = StringUtil.byte2HexStr(data);
                        }
                    }


                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG, "onCancel Cancel PIN Input");
                    Message msg = new Message();
                    msg.getData().putString("msg", "onCancel Cancel PIN Input");
                    handler.sendMessage(msg);
                    synchronized (PinpadModule.class) {
                        pinInputResult = "Cancel";
                    }
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    Log.i(TAG, "onError:" + errorCode);
                    synchronized (PinpadModule.class) {
                        pinInputResult = "Error:" + errorCode;
                    }
                    Message msg = new Message();
                    String errString = iPinpad.getLastError();
                    if (errString != null) {
                        Log.i(TAG, "getLastError=" + errString);
                        msg.getData().putString("msg", errString);
                        handler.sendMessage(msg);
                        return;
                    }
                    msg.getData().putString("msg", "onError:" + errorCode);
                    handler.sendMessage(msg);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("startPinInput exception got:" + e.getMessage());
            return pinInputResult;
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, Encoding error");
            e.printStackTrace();
            logUtils.addCaseLog("startPinInput exception got:" + e.getMessage());
            return pinInputResult;
        }
        int len = 0;

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (PinpadModule.class) {
                len = pinInputResult.length();
            }
        } while (len == 0);
        Log.d(TAG, "startPinInput returns:" + pinInputResult);
        logUtils.addCaseLog("startPinInput returns:" + pinInputResult);
        return pinInputResult;
    }

    private void T_stopPinInput() {
        try {
            iPinpad.stopPinInput();
            logUtils.addCaseLog("Stop success");
            this.printMsgTool("Result：Stop success");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("RemoteException：Stop failed");
        }
    }

    private void T_submitPinInput() {
        try {
            iPinpad.submitPinInput();
            logUtils.addCaseLog("Submitted successfully");
            this.printMsgTool("Result：Submitted successfully");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("submit failure");
        }
    }

    private void T_getLastError() {
        try {
            iPinpad.getLastError();
            this.printMsgTool("Result" + iPinpad.getLastError());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean T_loadWorkKeyWithDecryptType(String skeyType, String smkId, String swkId, String sdecKeyType, String skey, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(smkId) || TextUtils.isEmpty(swkId)
//                || TextUtils.isEmpty(sdecKeyType) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyType = Integer.valueOf(skeyType);
            int mkId = Integer.valueOf(smkId);
            int wkId = Integer.valueOf(swkId);
            int decKeyType = Integer.valueOf(sdecKeyType);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            ret = iPinpad.loadWorkKeyWithDecryptType(keyType, mkId, wkId, decKeyType, key, checkValue);
            if (ret) {
                logUtils.addCaseLog("Download the work key[" + wkId + "]successfully");
                this.printMsgTool("Result：Download the work key[" + wkId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the work key[" + wkId + "]failed");
                this.printMsgTool("Result：Download the work key[" + wkId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private byte[] T_calcMACWithCalType(String skeyId, String stype, String sCBCInitVec, String sdata, String sdesType, String sdukptRequest) {
        byte[] MyCalcMAC = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(stype) || TextUtils.isEmpty(sCBCInitVec)
//                || TextUtils.isEmpty(sdata) || TextUtils.isEmpty(sdesType) || TextUtils.isEmpty(sdukptRequest)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int type = Integer.valueOf(stype);
            int desType = Integer.valueOf(sdesType);
            boolean dukptRequest = Boolean.valueOf(sdukptRequest);
            byte[] CBCInitVec = StringUtil.hexStr2Bytes(sCBCInitVec);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            MyCalcMAC = iPinpad.calcMACWithCalType(keyId, type, CBCInitVec, data, desType, dukptRequest);
            if (MyCalcMAC == null) {
                logUtils.addCaseLog("Failed to calc MAC");
                String errString = iPinpad.getLastError();
                this.printMsgTool("Result：Failed to calc MAC");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("Calculating Mac Success");
                this.printMsgTool("Result：Calculating Mac Success");
                logUtils.addCaseLog("MAC: " + StringUtil.byte2HexStr(MyCalcMAC));
                this.printMsgTool("Result：MAC：" + StringUtil.byte2HexStr(MyCalcMAC));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return MyCalcMAC;
    }

    private byte[] T_calculateData(String smode, String sdesType, String skey, String sdata) {
        byte[] result = null;
//        if (TextUtils.isEmpty(smode) || TextUtils.isEmpty(sdesType) || TextUtils.isEmpty(skey)
//                || TextUtils.isEmpty(sdata)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int mode = Integer.valueOf(smode);
            int desType = Integer.valueOf(sdesType);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            result = iPinpad.calculateData(mode, desType, key, data);
            if (result == null) {
                logUtils.addCaseLog("Encryption failure");
                this.printMsgTool("Result：Encryption failure");

                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                if ((mode == 0)) {
                    logUtils.addCaseLog("Encryption success");
                    this.printMsgTool("Result：Encryption success");
                    logUtils.addCaseLog("Encryption result: " + StringUtil.byte2HexStr(result));
                    this.printMsgTool("Result: " + StringUtil.byte2HexStr(result));
                } else if ((mode == 1)) {
                    logUtils.addCaseLog("Decryption success");
                    this.printMsgTool("Result：Decryption success");
                    logUtils.addCaseLog("Decryption result: " + StringUtil.byte2HexStr(result));
                    this.printMsgTool("Decryption result: " + StringUtil.byte2HexStr(result));
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return result;
    }

    private boolean T_savePlainKey(String skeyType, String skeyId, String skey) {
        boolean result = false;
        //        if (TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return;
//        }
        try {
            int keyType = Integer.valueOf(skeyType);
            int keyId = Integer.valueOf(skeyId);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            result = iPinpad.savePlainKey(keyType, keyId, key);
            if (result == true) {
                logUtils.addCaseLog("Save key plaintext successfully");
                this.printMsgTool("Result：Save key plaintext successfully");
            } else {
                logUtils.addCaseLog("Failed to save key plaintext");
                this.printMsgTool("Result：Failed to save key plaintext");
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return result;
    }

    private boolean T_loadDukptKey(String skeyId, String sksn, String skey) {
        return loadDukptKey(skeyId, sksn, skey, "");
    }

    private boolean T_loadDukptKey(String skeyId, String sksn, String skey, String checkValue) {
        return loadDukptKey(skeyId, sksn, skey, checkValue);
    }

    private boolean loadDukptKey(String skeyId, String sksn, String skey, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sksn) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            byte[] ksn = StringUtil.hexStr2Bytes(sksn);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            ret = iPinpad.loadDukptKey(keyId, ksn, key, checkValue);
            if (ret) {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]success");
                this.printMsgTool("Result：download dukpt key[" + keyId + "]success");
            } else {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]failed");
                this.printMsgTool("Result：download dukpt key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private byte[] T_encryptTrackDataWithAlgorithmType(String smode, String skeyId, String salgorithmType, String strkData, String sdukptRequest) {
        byte[] MyEncryptTrackData = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(smode) || TextUtils.isEmpty(salgorithmType)
//                || TextUtils.isEmpty(sdukptRequest) || TextUtils.isEmpty(strkData)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int mode = Integer.valueOf(smode);
            int algorithmType = Integer.valueOf(salgorithmType);
            byte[] trkData = StringUtil.hexStr2Bytes(strkData);
            boolean dukptRequest = Boolean.valueOf(sdukptRequest);
            Log.i(TAG, "parameters: " + mode + ", " + keyId + ", " + algorithmType + ", " + strkData + ", " + dukptRequest);
            MyEncryptTrackData = iPinpad.encryptTrackDataWithAlgorithmType(mode, keyId, algorithmType, trkData, dukptRequest);
            if (MyEncryptTrackData == null) {
                logUtils.addCaseLog("Failed to encrypt track data");
                this.printMsgTool("Result：Failed to encrypt track data");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("Encrypted track data successfully");
                this.printMsgTool("Result：Encrypted track data successfully");
                logUtils.addCaseLog("encryptTrackData:");
                logUtils.addCaseLog(StringUtil.byte2HexStr(MyEncryptTrackData));
                this.printMsgTool(StringUtil.byte2HexStr(MyEncryptTrackData));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return MyEncryptTrackData;
    }

    private byte[] T_dukptEncryptData(String sdestype, String salgorithm, String skeyid, String sdata) {
        return T_dukptEncryptData(sdestype, salgorithm, skeyid, sdata, "");
    }

    private byte[] T_dukptEncryptData(String sdestype, String salgorithm, String skeyid, String sdata, String sCBCInitVec) {
        byte[] result = null;
//        if (TextUtils.isEmpty(sdestype) || TextUtils.isEmpty(salgorithm) || TextUtils.isEmpty(skeyid)
//                || TextUtils.isEmpty(sdata) ) { // || TextUtils.isEmpty(sCBCInitVec)
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int destype = Integer.valueOf(sdestype);
            int algorithm = Integer.valueOf(salgorithm);
            int keyid = Integer.valueOf(skeyid);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            byte[] CBCInitVec = StringUtil.hexStr2Bytes(sCBCInitVec);

            result = iPinpad.dukptEncryptData(destype, algorithm, keyid, data, CBCInitVec);
            if (result == null) {
                logUtils.addCaseLog("DUKPT encryption failed");
                this.printMsgTool("Result：DUKPT encryption failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("DUKPT encryption successfully");
                this.printMsgTool("Result：DUKPT encryption successfully");
                logUtils.addCaseLog("result:");
                logUtils.addCaseLog(StringUtil.byte2HexStr(result));
                this.printMsgTool(StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return result;
    }

    private byte[] T_getDukptKsn() {
        byte[] KSN = null;
        try {
            KSN = iPinpad.getDukptKsn();
            if (KSN == null) {
                logUtils.addCaseLog("Get KSN failed");
                this.printMsgTool("Result：Get KSN failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("Get KSN successfully");
                logUtils.addCaseLog("KSN:");
                logUtils.addCaseLog(StringUtil.byte2HexStr(KSN));
                this.printMsgTool("Result：get KSN successfully");
                this.printMsgTool(StringUtil.byte2HexStr(KSN));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return KSN;
    }

    private boolean T_loadTEKWithAlgorithmType(String skeyId, String skey, String salgorithmType, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey) || TextUtils.isEmpty(salgorithmType)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            byte algorithmType = Byte.valueOf(salgorithmType);
            ret = iPinpad.loadTEKWithAlgorithmType(keyId, key, algorithmType, checkValue);
            if (ret) {
                logUtils.addCaseLog("Download the transfer key successfully");
                this.printMsgTool("Result：Download the transfer key successfully");
            } else {
                logUtils.addCaseLog("Failed to download the transport key");
                this.printMsgTool("Result：Failed to download the transport key");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private boolean T_loadEncryptMainKeyWithAlgorithmType(String skeyId, String skey, String salgorithmType, String scheckValue) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey) || TextUtils.isEmpty(salgorithmType)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);

            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            int algorithmType = Integer.valueOf(salgorithmType);
            ret = iPinpad.loadEncryptMainKeyWithAlgorithmType(keyId, key, algorithmType, checkValue);
            if (ret) {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
                this.printMsgTool("Result：Download the ciphertext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
                this.printMsgTool("Result：Download the ciphertext master key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private boolean T_loadMainKeyWithAlgorithmType(String skeyId, String skey, String salgorithmType, String scheckValue) throws RemoteException {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey) || TextUtils.isEmpty(salgorithmType)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
//        try {
        int keyId = Integer.valueOf(skeyId);

        byte[] key = StringUtil.hexStr2Bytes(skey);
        byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);

        int algorithmType = Integer.valueOf(salgorithmType);
        ret = iPinpad.loadMainKeyWithAlgorithmType(keyId, key, algorithmType, checkValue);
        if (ret) {
//                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]successfully");
//                this.printMsgTool("Result：Download the plaintext master key[" + keyId + "]successfully");
        } else {
            logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]failed");
            this.printMsgTool("Result：Download the plaintext master key[" + keyId + "]failed");
            String errString = iPinpad.getLastError();
            if (errString != null) {
                Log.i(TAG, "getLastError=" + errString);
                logUtils.addCaseLog(errString);
            }
        }
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("Case failed, RemoteException");
//            e.printStackTrace();
//        } catch (NumberFormatException e) {
//            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
//        }
        return ret;
    }

    private byte[] T_getKeyKCV(String skeyIndex, String skeyType) {
        byte[] KCV = null;
//        if (TextUtils.isEmpty(skeyIndex) || TextUtils.isEmpty(skeyType)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int keyIndex = Integer.valueOf(skeyIndex);
            int keyType = Integer.valueOf(skeyType);
            KCV = iPinpad.getKeyKCV(keyIndex, keyType);
            if (KCV == null) {
                logUtils.addCaseLog("Failed to get KCV");
                this.printMsgTool("Result：Failed to get KCV");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("Acquire KCV successfully");
                this.printMsgTool("Result：Acquire KCV successfully");
                logUtils.addCaseLog("KCV:");
                logUtils.addCaseLog(StringUtil.byte2HexStr(KCV));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return KCV;
    }

    */
/**
     * -- 有demo测试
     *
     * @param keyId
     * @param param
     * @param pinKeyInfos
     * @param listener
     * @return
     *//*

    private Map T_initPinInputCustomView(int keyId, String param, List<PinKeyCoorInfo> pinKeyInfos, PinpadMoudle.Mypinpadlistener listener) {
        Map initView = null;
        try {

            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("pinLimit", BundleConfig_byte_A),
                    new BundleConfig("timeout", BundleConfig_int),
                    new BundleConfig("isOnline", BundleConfig_boolean),
                    new BundleConfig("pan", BundleConfig_String),
                    new BundleConfig("pinFormatType", BundleConfig_int),
                    new BundleConfig("dispersionType", BundleConfig_int),
                    new BundleConfig("otherFont", BundleConfig_String),
                    new BundleConfig("displayKeyValue", BundleConfig_byte_A),
                    new BundleConfig("random", BundleConfig_byte_A),
            };

            Bundle bundle = convert(param, bundleConfigs);
            initView = iPinpad.initPinInputCustomView(keyId, bundle, pinKeyInfos, listener);

            if (initView == null) {
                logUtils.addCaseLog("Failed to initialize the custom keyboard interface");
                this.printMsgTool("Result：Failed to initialize the custom keyboard interface");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("The custom keyboard interface was initialized successfully");
                this.printMsgTool("Result：The custom keyboard interface was initialized successfully");
                Set<Map.Entry<String, String>> entrys = initView.entrySet();
                for (Map.Entry<String, String> entry : entrys) {
                    logUtils.addCaseLog(entry.getKey() + " display--> " + entry.getValue());
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return initView;
    }

    */
/**
     * -- 有demo测试
     *//*

    private void T_startPinInputCustomView() {
        try {
            iPinpad.startPinInputCustomView();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private byte[] T_calculateDataEx(String smode, String sdesType, String skey, String sdata, String sinitVec) {
        byte[] result = null;
//        if (TextUtils.isEmpty(smode) || TextUtils.isEmpty(sdesType) || TextUtils.isEmpty(skey) || TextUtils.isEmpty(sdata) || TextUtils.isEmpty(sinitVec)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int mode = Integer.valueOf(smode);
            int desType = Integer.valueOf(sdesType);


            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            byte[] initVec = StringUtil.hexStr2Bytes(sinitVec);

            result = iPinpad.calculateDataEx(mode, desType, key, data, initVec);
            if (result == null) {
                logUtils.addCaseLog("Encryption/decryption failed");
                this.printMsgTool("Result：Encryption/decryption failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                if ((mode == 0)) {
                    logUtils.addCaseLog("Encryption success");
                    this.printMsgTool("Execution result: Encryption/decryption successful");
                    logUtils.addCaseLog("Encryption result: " + StringUtil.byte2HexStr(result));
                    this.printMsgTool("Execution result: Encryption result: " + StringUtil.byte2HexStr(result));
                } else if ((mode == 1)) {
                    logUtils.addCaseLog("Decryption success");
                    this.printMsgTool("Execution result: decryption successful");
                    logUtils.addCaseLog("Decryption result: " + StringUtil.byte2HexStr(result));
                    this.printMsgTool("Execution result: Decryption result: " + StringUtil.byte2HexStr(result));
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return result;
    }

    private byte[] T_encryptPinFormat0(String spinKeyId, String sdesType, String scardNumber, String passwd) {
        byte[] result = null;
//        if (TextUtils.isEmpty(spinKeyId) || TextUtils.isEmpty(sdesType) || TextUtils.isEmpty(scardNumber) || TextUtils.isEmpty(passwd)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int pinKeyId = Integer.valueOf(spinKeyId);
            int desType = Integer.valueOf(sdesType);
            byte[] cardNumber = StringUtil.hexStr2Bytes(scardNumber);

            result = iPinpad.encryptPinFormat0(pinKeyId, desType, cardNumber, passwd);
            if (result == null) {
                logUtils.addCaseLog("PIN encryption failed");
                this.printMsgTool("Execution result: PIN encryption failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {

                logUtils.addCaseLog("IN encryption successful");
                this.printMsgTool("Execution result: PIN encryption was successful");
                logUtils.addCaseLog("Encryption result: " + StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return result;
    }

    private byte[] T_calculateByDataKey(String skeyId, String sencAlg, String sencMode, String sencFlag, String sdata, String sinitVec) {
        byte[] result = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sencAlg) || TextUtils.isEmpty(sencMode) || TextUtils.isEmpty(sencFlag)
//                || TextUtils.isEmpty(sdata) || TextUtils.isEmpty(sinitVec)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int encAlg = Integer.valueOf(sencAlg);
            int encMode = Integer.valueOf(sencMode);
            int encFlag = Integer.valueOf(sencFlag);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            byte[] initVec = StringUtil.hexStr2Bytes(sinitVec);

            result = iPinpad.calculateByDataKey(keyId, encAlg, encMode, encFlag, data, initVec);
            if (result == null) {
                logUtils.addCaseLog("TDKEY computation failed");
                this.printMsgTool("Result: TDKEY computation failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {

                logUtils.addCaseLog("The TDKEY was calculated successfully");
                this.printMsgTool("Result: TDKEY was successfully calculated");
                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result: " + StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return result;
    }

    private boolean T_loadEncryptMainKeyEX(String skeyId, String skey, String salgorithmType, String scheckValue, String sextend) {
        boolean ret = false;
        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey) || TextUtils.isEmpty(salgorithmType)) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return ret;
        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int algorithmType = Integer.valueOf(salgorithmType);

            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);

            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("isCBCType", BundleConfig_boolean),
                    new BundleConfig("initVec", BundleConfig_byte_A),
                    new BundleConfig("isMasterEncMasterMode", BundleConfig_boolean),
                    new BundleConfig("decryptKeyIndex", BundleConfig_int)
            };
            Bundle extend = convert(sextend, bundleConfigs);

            ret = iPinpad.loadEncryptMainKeyEX(keyId, key, algorithmType, checkValue, extend);
            if (ret) {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
                this.printMsgTool("Execution result: Download the ciphertext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
                this.printMsgTool("Execution result: Download the ciphertext master key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private boolean T_loadWorkKeyEX(String skeyType, String smkId, String swkId, String sdecKeyType, String skey, String scheckValue, String sextend) {
        boolean ret = false;
        if (TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(smkId) || TextUtils.isEmpty(swkId) || TextUtils.isEmpty(sdecKeyType) || TextUtils.isEmpty(skey)) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return ret;
        }

        try {

            int keyType = Integer.valueOf(skeyType);
            int mkId = Integer.valueOf(smkId);
            int wkId = Integer.valueOf(swkId);
            int decKeyType = Integer.valueOf(sdecKeyType);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);

            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("isCBCType", BundleConfig_boolean),
                    new BundleConfig("initVec", BundleConfig_byte_A)
            };
            Bundle extend = convert(sextend, bundleConfigs);

            ret = iPinpad.loadWorkKeyEX(keyType, mkId, wkId, decKeyType, key, checkValue, extend);
            if (ret) {
                logUtils.addCaseLog("Download the working key[" + wkId + "]successfully");
                this.printMsgTool("Result:Download the working key[" + wkId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the working key[" + wkId + "]failed");
                this.printMsgTool("Result:Download the working key[" + wkId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private boolean T_clearKey(String skeyIdStart, String skeyType, String sKeyIdEnd) {
        if (TextUtils.isEmpty(skeyIdStart) || TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(sKeyIdEnd)) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return false;
        }
        int start = Integer.valueOf(skeyIdStart);
        int end = Integer.valueOf(sKeyIdEnd);
        if (end < start) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return false;
        }
        for (int i = start; i <= end; i++) {
            T_clearKey(String.valueOf(i), skeyType, false);
        }
        logUtils.addCaseLog("Clear key Finished");
        this.printMsgTool("Result:Clear key Finished");

        return true;
    }

    private boolean T_clearKey(String skeyId, String skeyType) {
        return T_clearKey(skeyId, skeyType, true);
    }

    private boolean T_clearKey(String skeyId, String skeyType, boolean updateResult) {
        boolean ret = false;
        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skeyType)) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return false;
        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int keyType = Integer.valueOf(skeyType);
            ret = iPinpad.clearKey(keyId, keyType);
            if (ret) {
                if (updateResult) {
                    logUtils.addCaseLog("Clear key successfully");
                    this.printMsgTool("Result:Clear key successfully");
                }
            } else {
                if (updateResult) {
                    logUtils.addCaseLog("Clear key failed");
                    this.printMsgTool("Result:Clear key failed");
                    String errString = iPinpad.getLastError();
                    if (errString != null) {
                        Log.i(TAG, "getLastError=" + errString);
                        logUtils.addCaseLog(errString);
                    }
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    */
/**
     * 废弃 可以不用实现
     *//*

    @Deprecated
    private boolean T_loadDukptKeyEX(String skeyId, String sksn, String skey, String scheckValue, String sextend) {
        boolean ret = false;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sksn) || TextUtils.isEmpty(skey)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }
        try {

            int keyId = Integer.valueOf(skeyId);
            byte[] ksn = StringUtil.hexStr2Bytes(sksn);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("loadPlainKey", BundleConfig_boolean),
                    new BundleConfig("TEKIndex", BundleConfig_int)
            };
            Bundle extend = convert(sextend, bundleConfigs);
            ret = iPinpad.loadDukptKeyEX(keyId, ksn, key, checkValue, extend);
            if (ret) {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]successfully");
                this.printMsgTool("Result:download dukpt key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]failed");
                this.printMsgTool("Result:download dukpt key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private byte[] T_calculateByWorkKey(String skeyId, String skeyType, String sencAlg, String sencMode, String sencFlag, String sdata, String sextend) throws RemoteException {
        byte[] ret = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(sencAlg) || TextUtils.isEmpty(sencMode) || TextUtils.isEmpty(sencFlag) || TextUtils.isEmpty(sdata)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return ret;
//        }
        Log.d(TAG, String.format("Params:%s,%s,%s,%s,%s,%s,%s", skeyId, skeyType, sencAlg, sencMode, sencFlag, sdata, sextend));
//        try {
        int keyId = Integer.valueOf(skeyId);
        int keyType = Integer.valueOf(skeyType);
        int encAlg = Integer.valueOf(sencAlg);
        int encMode = Integer.valueOf(sencMode);
        int encFlag = Integer.valueOf(sencFlag);
        byte[] data = StringUtil.hexStr2Bytes(sdata);
        BundleConfig[] bundleConfigs = new BundleConfig[]{
                new BundleConfig("initVec", BundleConfig_byte_A)
        };
        Bundle extend = convert(sextend, bundleConfigs);
        ret = iPinpad.calculateByWorkKey(keyId, keyType, encAlg, encMode, encFlag, data, extend);
        if (ret == null) {
            logUtils.addCaseLog("calculateByWorkKey failed");
            this.printMsgTool("Result: calculateByWorkKey computation failed");
            String errString = iPinpad.getLastError();
            if (errString != null) {
                Log.i(TAG, "getLastError=" + errString);
                logUtils.addCaseLog(errString);
            }
        } else {
//                logUtils.addCaseLog("calculateByWorkKey successfully");
//                this.printMsgTool("Result: calculateByWorkKey was successfully calculated");
//                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(ret));
//                this.printMsgTool("Result: " + StringUtil.byte2HexStr(ret));
        }
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("Case failed, RemoteException");
//            e.printStackTrace();
//        } catch (NumberFormatException e) {
//            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
//        }

        return ret;
    }

    private byte[] T_calculateByMSKey(String skeyId, String skeyType, String salgorithmMode, String sdata, String sextend) {
        byte[] ret = null;
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skeyType) || TextUtils.isEmpty(salgorithmMode) || TextUtils.isEmpty(sdata)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return ret;
//        }
        try {
            int keyId = Integer.valueOf(skeyId);
            int keyType = Integer.valueOf(skeyType);
            int encAlg = Integer.valueOf(salgorithmMode);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("initVec", BundleConfig_byte_A)
            };
            Bundle extend = convert(sextend, bundleConfigs);

            ret = iPinpad.calculateByMSKey(keyId, keyType, encAlg, data, extend);
            if (ret == null) {
                logUtils.addCaseLog("calculateByMSKey failed");
                this.printMsgTool("Result: calculateByMSKey computation failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("calculateByMSKey successfully");
                this.printMsgTool("Result: calculateByMSKey was successfully calculated");
                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(ret));
                this.printMsgTool("Result: " + StringUtil.byte2HexStr(ret));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }


        return ret;
    }

    private boolean T_loadTEKEX(String skeyId, String skey, String salgorithmType, String scheckValue, String sextend) {
        boolean ret = false;
        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(skey) || TextUtils.isEmpty(salgorithmType)) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return ret;
        }
        try {

            int keyId = Integer.valueOf(skeyId);
            byte algorithmType = (byte) Integer.valueOf(salgorithmType);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("isCBCType", BundleConfig_boolean),
                    new BundleConfig("initVec", BundleConfig_byte_A)
            };
            Bundle extend = convert(sextend, bundleConfigs);
            ret = iPinpad.loadTEKEX(keyId, key, algorithmType, checkValue, extend);
            if (ret) {
                logUtils.addCaseLog("Download the transmission key[" + keyId + "]successfully");
                this.printMsgTool("Result:Download the transmission key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the transmission key[" + keyId + "]failed");
                this.printMsgTool("Result:Download the transmission key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    private byte[] T_getRandom(String slength) {
        byte[] Random = null;
        if (TextUtils.isEmpty(slength)) {
            logUtils.clearLog();
            logUtils.addCaseLog("Case failed, Invalid params");
            return null;
        }
        try {
            byte length = Byte.valueOf(slength);
            Random = iPinpad.getRandom(length);
            if (length == (byte) 0 || length > (byte) 127) {
                logUtils.addCaseLog("Failed to get random number");
                this.printMsgTool("Result：Failed to get random number");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("Retrieve random number successfully");
                logUtils.addCaseLog("Random:");
                logUtils.addCaseLog(StringUtil.byte2HexStr(Random));
                this.printMsgTool("Result：Retrieve random number successfully");
                this.printMsgTool("Random:");
                this.printMsgTool(StringUtil.byte2HexStr(Random));
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return Random;
    }

    //36autoPinpad仅用于执行自动化测试
    private boolean T_autoPinpad(int keyId, byte[] key, byte algorithmType, byte[] checkValue, Bundle extend) {
        boolean ret = false;
        try {
            ret = iPinpad.loadTEKEX(keyId, key, algorithmType, checkValue, extend);
            if (ret) {
                logUtils.addCaseLog("Download the transmission key[" + keyId + "]successfully");

            } else {
                logUtils.addCaseLog("Download the transmission key[" + keyId + "]failed");
                String errString = iPinpad.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("Case failed, RemoteException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            logUtils.addCaseLog("Case failed, NumberFormatException, Invalid params");
        }
        return ret;
    }

    public class Mypinpadlistener extends PinInputListener.Stub {
        @Override
        public void onInput(int len, int key) throws RemoteException {
            Log.i(TAG, "len=" + len + " key=" + key);
            Message msg = new Message();
            msg.getData().putString("msg", "已输入密码长度len=" + len + " 当前key值=" + key);
            handler.sendMessage(msg);
        }

        @Override
        public void onConfirm(byte[] data, boolean isNonePin) throws RemoteException {
            Log.i(TAG, "isNonePin:" + isNonePin);
            Message msg = new Message();
            Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
            msg.getData().putString("msg", "isNonePin:" + isNonePin + "\nPIN: " + StringUtil.byte2HexStr
                    (data));
            handler.sendMessage(msg);

            // My19getDukptKsn();
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            Log.i(TAG, "onError:" + errorCode);
            Message msg = new Message();
            String errString = iPinpad.getLastError();
            if (errString != null) {
                Log.i(TAG, "getLastError=" + errString);
                msg.getData().putString("msg", errString);
                handler.sendMessage(msg);
                return;
            }
            msg.getData().putString("msg", "onError:" + errorCode);
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel() throws RemoteException {
            Log.i(TAG, "onCancel 取消Pin输入");
            Message msg = new Message();
            msg.getData().putString("msg", "onCancel 取消Pin输入");
            handler.sendMessage(msg);
        }
    }


    private boolean isStrEqualtoNull(String str) {
        boolean ret = false;
        if (TextUtils.isEmpty(str) || str.equalsIgnoreCase("null")) {
            ret = true;
        }
        return ret;
    }
*/

}
