package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
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

import Utils.LogUtils;
import base.MyApplication;

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
    ArrayList<String> isKeyExist = new ArrayList<String>();
    ArrayList<String> loadTEK = new ArrayList<String>();
    ArrayList<String> loadEncryptMainKey = new ArrayList<String>();
    ArrayList<String> loadPlainMasterKey = new ArrayList<String>();
    ArrayList<String> loadWorkKey = new ArrayList<String>();
    ArrayList<String> encryptTrackData = new ArrayList<String>();
    ArrayList<String> calcMAC = new ArrayList<String>();
    ArrayList<String> startPinInput = new ArrayList<String>();
    ArrayList<String> submitPinInput = new ArrayList<String>();
    ArrayList<String> stopPinInput = new ArrayList<String>();
    ArrayList<String> getLastError = new ArrayList<String>();
    ArrayList<String> loadWorkKeyWithDecryptType = new ArrayList<String>();
    ArrayList<String> calcMACWithCalType = new ArrayList<String>();
    ArrayList<String> calculateData = new ArrayList<String>();
    ArrayList<String> savePlainKey = new ArrayList<String>();
    ArrayList<String> loadDukptKey = new ArrayList<String>();
    ArrayList<String> encryptTrackDataWithAlgorithmType = new ArrayList<String>();
    ArrayList<String> dukptEncryptData = new ArrayList<String>();
    ArrayList<String> getDukptKsn = new ArrayList<String>();
    ArrayList<String> loadTEKWithAlgorithmType = new ArrayList<String>();
    ArrayList<String> loadEncryptMainKeyWithAlgorithmType = new ArrayList<String>();
    ArrayList<String> loadPlainMasterKeyWithAlgorithmType = new ArrayList<String>();
    ArrayList<String> getKeyKCV = new ArrayList<String>();
    ArrayList<String> initPinInputCustomView = new ArrayList<String>();
    ArrayList<String> startPinInputCustomView = new ArrayList<String>();
    ArrayList<String> endPinInputCustomView = new ArrayList<String>();
    ArrayList<String> calculateDataEx = new ArrayList<String>();
    ArrayList<String> encryptPinFormat0 = new ArrayList<String>();
    ArrayList<String> calculateByDataKey = new ArrayList<String>();
    ArrayList<String> loadEncryptMainKeyEX = new ArrayList<String>();
    ArrayList<String> loadWorkKeyEX = new ArrayList<String>();
    ArrayList<String> clearKey = new ArrayList<String>();
    ArrayList<String> loadDukptKeyEX = new ArrayList<String>();
    ArrayList<String> loadTEKEX = new ArrayList<String>();
    ArrayList<String> getRandom = new ArrayList<String>();
    ArrayList<String> autoPinpad = new ArrayList<String>();
    ArrayList<String> startX990PinInput = new ArrayList<String>();
    ArrayList<String> searchCard = new ArrayList<String>();
    ArrayList<String> openSerial = new ArrayList<>();
    ArrayList<String> initSerial = new ArrayList<>();
    ArrayList<String> writeSerial = new ArrayList<>();
    ArrayList<String> emvCheckCard = new ArrayList<>();
    ArrayList<String> readSerial = new ArrayList<>();
    ArrayList<String> isUsbSerialConnect = new ArrayList<>();
    ArrayList<String> read = new ArrayList<>();
    ArrayList<String> write = new ArrayList<>();
    ArrayList<String> keyStoreTR34Payload = new ArrayList<>();

    //TEK
    byte[] tekKeyCheckValue = {(byte) 0x73, (byte) 0xB8, (byte) 0x17, (byte) 0xC3, (byte) 0x02, (byte) 0xAB, (byte) 0xE8, (byte) 0x5B};
    //    String tekKeyString = "2494E889B78C6F895593728E4990EE21";
    String tekKeyString = "3CAEFDD46D32C796059FBA457C7D5D1E3CAEFDD46D32C796";
    //      String tekKeyString = "3CAEFDD46D32C796059FBA457C7D5D1E3CAEFDD46D32C7RL";
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
    String macKeyCheckValueString = "";
    //用主密钥111111111111111122222222222222221111111111111111对MacKey进行解密的值对8个字节的0进行加密得到checkValue
    byte[] macKey = BCDDecode.str2Bcd(macKeyString);
    byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueString);

    //PIN Key
    String pinKeyString = "B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E";
    //,,,B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E
    //    String pinKeyString = "89B07B35A1B3F47E89B07B35A1B3F488";
    String pinKeyCheckValueString = "A677495148115613";
    //    String pinKeyCheckValueString = "C1205D74FCD48B50";
    byte[] pinKey = BCDDecode.str2Bcd(pinKeyString);
    byte[] pinKeyCheckValue = BCDDecode.str2Bcd(pinKeyCheckValueString);

    //TD Key
    String tdKeyString = "42527dea2a1553e1211b0ea0d459076642527dea2a1553e1";
    //    String tdKeyString = "088CAED653BCAAA368FCC0118AD7D337";
    String tdKeyCheckValueString = "A9AA621560D74109";
    // A9AA621560D74109
    byte[] tdKey = BCDDecode.str2Bcd(tdKeyString);
    byte[] tdKeyCheckValue = BCDDecode.str2Bcd(tdKeyCheckValueString);

    //Dukpt Key
    String dukptksnString = "01020304050607080901";
    byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
    String dukptkeyString = "34343434343434343535353535353535";
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
//
////    void FormatKeyArea() {
////        //格式化密钥区
////        try {
//////            boolean ret = SdkApiHolder.getInstance().getKeyManager().formatKeyArea();
////            boolean ret = Ped.getInstance().formatKeyArea();
////            Log.i(TAG, "formatKeyArea=" + ret);
////            if (ret) {
////                logUtils.addCaseLog("Format key area successfully");
////            } else {
////                logUtils.addCaseLog("Format key area failed");
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////            return;
////        }
////        /* catch (PinPadException e) {
////            e.printStackTrace();
////            return;
////        } catch (SDKException e) {
////            e.printStackTrace();
////            return;
////        }*/
////    }
//
    public byte[] getDukptKsn(int keyIdx) {
        byte[] result = null;
        try {
            result = iDukpt.getDukptKSN(keyIdx,0,new Bundle());
            if (result == null) {
                logUtils.addCaseLog("getKSN failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("Result：getKSN failed，error=" + errString);
                }
            } else {
                logUtils.addCaseLog("getKSN success");
                logUtils.addCaseLog("KSN: " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result：getKSN success，result=" + StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }
//
//    private boolean My01isKeyExist(int keyType, int keyId) {
//        boolean ret = false;
//        String keyTypeString = null;
//        if (keyType == PinpadKeyType.MASTERKEY)
//            keyTypeString = "Master key ";
//        else if (keyType == PinpadKeyType.DUKPTKEY)
//            keyTypeString = "dukpt key";
//        else
//            keyTypeString = "work key";
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.isKeyExist(keyType, keyId);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("isKeyExist executeTime : " + (endTime - startTime) + " ms");
//            if (ret) {
//                logUtils.addCaseLog(keyTypeString + "[" + keyId + "] exist");
////                this.printMsgTool("Result：" + keyTypeString + "[" + keyId + "]exist");
//            } else {
//                logUtils.addCaseLog(keyTypeString + "[" + keyId + "]no-exist");
//                String errString = iPinpad.getLastError();
////                this.printMsgTool("Result：" + keyTypeString + "[" + keyId + "]no-exist");
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
    private boolean loadTEK(int keyId, byte[] key, byte[] checkValue) {
        boolean ret = false;
        try {
            long startTime = System.currentTimeMillis();
            ret = imksk.loadTEK(keyId, key, (byte)0x02, checkValue,new Bundle());
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadTEK executeTime : " + (endTime - startTime) + " ms");
            if (ret) {
                logUtils.addCaseLog("The plaintext transfer key was downloaded successfully");
//                this.printMsgTool("Execution result: download plaintext transfer key successfully");
            } else {
                logUtils.addCaseLog("Failed to download the plaintext transfer key");
                String errString = imksk.getLastError();
//                this.printMsgTool("Execution result：Failed to download the plaintext transfer key");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }
//
//    private boolean My03loadEncryptMainKey(int keyId, byte[] key, byte[] checkValue) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadEncryptMainKey(keyId, key, checkValue);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadEncryptMainKey executeTime : " + (endTime - startTime) + " ms");
//            if (ret) {
//                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
//                this.printMsgTool("Execution result：Download the ciphertext master key[" + keyId + "]successfully");
//            } else {
//                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
//                String errString = iPinpad.getLastError();
//                this.printMsgTool("Execution result：Download the ciphertext master key[" + keyId + "]failed");
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace
//        }
//        return ret;
//    }
//
    private boolean loadPlainMasterKey(int keyId, byte[] key, int algorithmType , byte[] checkValue) {
        boolean ret = false;
        try {
            long startTime = System.currentTimeMillis();
            ret = imksk.loadPlainMasterKey(keyId, key, algorithmType, checkValue);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadPlainMasterKey executeTime : " + (endTime - startTime) + " ms");
            if (ret) {
                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]successfully");
//                this.printMsgTool("Execution result：Download the plaintext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]failed");
                String errString = imksk.getLastError();
//                this.printMsgTool("Execution result：Download the plaintext master key[" + keyId + "]failed");
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }
//
//    private boolean My05loadWorkKey(int keyType, int mkId, int wkId, byte[] key, byte[] checkValue) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadWorkKey(keyType, mkId, wkId, key, checkValue);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadWorkKey executeTime : " + (endTime - startTime) + " ms");
//            if (ret) {
//                logUtils.addCaseLog("Download the work key[" + wkId + "]successfully");
////                this.printMsgTool("Execution result：Download the work key[" + wkId + "]successfully");
//            } else {
//                logUtils.addCaseLog("Download the work key[" + wkId + "]failed");
//                String errString = iPinpad.getLastError();
////                this.printMsgTool("Execution result：Download the work key[" + wkId + "]failed");
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private byte[] My06calcMAC(int keyId, byte[] data) {
//        byte[] MyCalcMAC = null;
//        try {
//
//            long startTime = System.currentTimeMillis();
//            MyCalcMAC = iPinpad.calcMAC(keyId, data);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("calcMAC executeTime : " + (endTime - startTime) + " ms");
//
//            if (MyCalcMAC == null) {
//                logUtils.addCaseLog("Failed to compute MAC");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//                this.printMsgTool("Execution result: The calculation of MAC failed" + "getLastError=" + errString);
//
//            } else {
//                logUtils.addCaseLog("Calculating Mac Success");
//                logUtils.addCaseLog("MAC: " + StringUtil.byte2HexStr(MyCalcMAC));
//                this.printMsgTool("Execution result: Calculate MAC successfully" + StringUtil.byte2HexStr(MyCalcMAC));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return MyCalcMAC;
//    }
//
//    private byte[] My07encryptTrackData(int mode, int keyId, byte[] track) {
//        byte[] MyEncryptTrackData = null;
//        try {
//
//            long startTime = System.currentTimeMillis();
//            MyEncryptTrackData = iPinpad.encryptTrackData(mode, keyId, track);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("encryptTrackData executeTime : " + (endTime - startTime) + " ms");
//
//            if (MyEncryptTrackData == null) {
//                logUtils.addCaseLog("Failed to encrypt track data");
//                String errString = iPinpad.getLastError();
//                this.printMsgTool("Execution result: Encryption track data failed");
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                logUtils.addCaseLog("Encrypted track data successfully");
//                this.printMsgTool("Execution result: track data encrypted successfully");
//                logUtils.addCaseLog("encryptTrackData:");
////                logUtils.addCaseLog(StringUtil.byte2HexStr(BCDDecode.str2Bcd(StringUtil.byteToStr(MyEncryptTrackData))));
//                logUtils.addCaseLog(StringUtil.byte2HexStr(MyEncryptTrackData));
//
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return MyEncryptTrackData;
//    }
//

//
//    private void My09stopPinInput() {
//        try {
//            long startTime = System.currentTimeMillis();
//            iPinpad.stopPinInput();
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("stopPinInput executeTime : " + (endTime - startTime) + " ms");
//            logUtils.addCaseLog("Stop success");
//            this.printMsgTool("Result：Stop success");
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("RemoteException：Stop failed");
//        }
//    }
//
//    private void My10submitPinInput() {
//        try {
//            long startTime = System.currentTimeMillis();
//            iPinpad.submitPinInput();
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("submitPinInput executeTime : " + (endTime - startTime) + " ms");
//
//            logUtils.addCaseLog("Submitted successfully");
//            this.printMsgTool("Result：Submitted successfully");
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("submit failure");
//        }
//    }
//
//    private void My11getLastError() {
//        try {
//            long startTime = System.currentTimeMillis();
//            iPinpad.getLastError();
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("getLastError executeTime : " + (endTime - startTime) + " ms");
//
//            this.printMsgTool("Result" + iPinpad.getLastError());
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
    private boolean loadWorkKey(int keyType, int mkId, int wkId, int decKeyType, byte[] key, byte[] checkValue) {
        boolean ret = false;
        try {
            long startTime = System.currentTimeMillis();
            ret = imksk.loadSessionKey(keyType, mkId, wkId, decKeyType, key, checkValue,new Bundle());
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadWorkKeyWithDecryptType executeTime : " + (endTime - startTime) + " ms");

            if (ret) {
                logUtils.addCaseLog("Download the work key[" + wkId + "]successfully");
                this.printMsgTool("Result：Download the work key[" + wkId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the work key[" + wkId + "]failed");
                this.printMsgTool("Result：Download the work key[" + wkId + "]failed");
                String errString = imksk.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }
//
//    private void My13calcMACWithCalType(int keyId, int type, byte[] CBCInitVec, byte[] data, int desType, boolean dukptRequest) {
//        byte[] MyCalcMAC = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            MyCalcMAC = iPinpad.calcMACWithCalType(keyId, type, CBCInitVec, data, desType, dukptRequest);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("calcMACWithCalType executeTime : " + (endTime - startTime) + " ms");
//
//
//            if (MyCalcMAC == null) {
//                logUtils.addCaseLog("Failed to calc MAC");
//                String errString = iPinpad.getLastError();
//                this.printMsgTool("Result：Failed to calc MAC");
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                logUtils.addCaseLog("Calculating Mac Success");
//                this.printMsgTool("Result：Calculating Mac Success");
//                logUtils.addCaseLog("MAC: " + StringUtil.byte2HexStr(MyCalcMAC));
//                this.printMsgTool("Result：MAC：" + StringUtil.byte2HexStr(MyCalcMAC));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("submit failure");
//        }
//    }
//
//    private byte[] My14calculateData(int mode, int desType, byte[] key, byte[] data) {
//        byte[] result = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            result = iPinpad.calculateData(mode, desType, key, data);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("calculateData executeTime : " + (endTime - startTime) + " ms");
//
//
//            if (result == null) {
//                logUtils.addCaseLog("Encryption failure");
//                this.printMsgTool("Result：Encryption failure");
//
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                if ((mode == 0)) {
//                    logUtils.addCaseLog("Encryption success");
//                    this.printMsgTool("Result：Encryption success");
//                    logUtils.addCaseLog("Encryption result: " + StringUtil.byte2HexStr(result));
//                    this.printMsgTool("Result: " + StringUtil.byte2HexStr(result));
//                } else if ((mode == 1)) {
//                    logUtils.addCaseLog("Decryption success");
//                    this.printMsgTool("Result：Decryption success");
//                    logUtils.addCaseLog("Decryption result: " + StringUtil.byte2HexStr(result));
//                    this.printMsgTool("Decryption result: " + StringUtil.byte2HexStr(result));
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("Encryption/decryption failed");
//        }
//        return result;
//    }
//
//    private void My15savePlainKey(int keyType, int keyId, byte[] key) {
//        try {
//            long startTime = System.currentTimeMillis();
//            boolean result = iPinpad.savePlainKey(keyType, keyId, key);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("savePlainKey executeTime : " + (endTime - startTime) + " ms");
//
//            if (result == true) {
//                logUtils.addCaseLog("Save key plaintext successfully");
//                this.printMsgTool("Result：Save key plaintext successfully");
//            } else {
//                logUtils.addCaseLog("Failed to save key plaintext");
//                this.printMsgTool("Result：Failed to save key plaintext");
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("Failed to save key plaintext");
//        }
//    }
//
    private boolean loadDukptKey(int keyId, byte[] ksn, byte[] key, byte[] checkValue) {
        boolean ret = false;
        try {
            long startTime = System.currentTimeMillis();
            ret = iDukpt.loadDukptKey(keyId, ksn, key, checkValue,new Bundle());
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadDukptKey executeTime : " + (endTime - startTime) + " ms");

            if (ret) {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]success");
                this.printMsgTool("Result：download dukpt key[" + keyId + "]success");
            } else {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]failed");
                this.printMsgTool("Result：download dukpt key[" + keyId + "]failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }
//
//    private byte[] My17encryptTrackDataWithAlgorithmType(int mode, int keyId, int algorithmType, byte[] trkData, boolean dukptRequest) {
//        byte[] MyEncryptTrackData = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            MyEncryptTrackData = iPinpad.encryptTrackDataWithAlgorithmType(mode, keyId, algorithmType, trkData, dukptRequest);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("encryptTrackDataWithAlgorithmType executeTime : " + (endTime - startTime) + " ms");
//
//
//            if (MyEncryptTrackData == null) {
//                logUtils.addCaseLog("Failed to encrypt track data");
//                this.printMsgTool("Result：Failed to encrypt track data");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                logUtils.addCaseLog("Encrypted track data successfully");
//                this.printMsgTool("Result：Encrypted track data successfully");
//                logUtils.addCaseLog("encryptTrackData:");
//                logUtils.addCaseLog(StringUtil.byte2HexStr(MyEncryptTrackData));
//                this.printMsgTool(StringUtil.byte2HexStr(MyEncryptTrackData));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return MyEncryptTrackData;
//    }
//
//    private byte[] My18dukptEncryptData(int destype, int algorithm, int keyid, byte[] data, byte[] CBCInitVec) {
//        byte[] result = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            result = iPinpad.dukptEncryptData(destype, algorithm, keyid, data, CBCInitVec);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("dukptEncryptData executeTime : " + (endTime - startTime) + " ms");
//
//            if (result == null) {
//                logUtils.addCaseLog("DUKPT encryption failed");
//                this.printMsgTool("Result：DUKPT encryption failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                logUtils.addCaseLog("DUKPT encryption successfully");
//                this.printMsgTool("Result：DUKPT encryption successfully");
//                logUtils.addCaseLog("result:");
//                logUtils.addCaseLog(StringUtil.byte2HexStr(result));
//                this.printMsgTool(StringUtil.byte2HexStr(result));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//    private boolean My20loadTEKWithAlgorithmType(int keyId, byte[] key, byte algorithmType, byte[] checkValue) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadTEKWithAlgorithmType(keyId, key, algorithmType, checkValue);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadTEKWithAlgorithmType executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("Download the transfer key successfully");
//                this.printMsgTool("Result：Download the transfer key successfully");
//            } else {
//                logUtils.addCaseLog("Failed to download the transport key");
//                this.printMsgTool("Result：Failed to download the transport key");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private boolean My21loadEncryptMainKeyWithAlgorithmType(int keyId, byte[] key, int algorithmType, byte[] checkValue) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadEncryptMainKeyWithAlgorithmType(keyId, key, algorithmType, checkValue);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadEncryptMainKeyWithAlgorithmType executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
//                this.printMsgTool("Result：Download the ciphertext master key[" + keyId + "]successfully");
//            } else {
//                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
//                this.printMsgTool("Result：Download the ciphertext master key[" + keyId + "]failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
    private boolean loadPlainMainKey(int keyId, byte[] key, int algorithmType, byte[] checkValue) {
        boolean ret = false;
        try {
            long startTime = System.currentTimeMillis();
            ret = imksk.loadPlainMasterKey(keyId, key, algorithmType, checkValue);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadPlainMasterKeyWithAlgorithmType executeTime : " + (endTime - startTime) + " ms");

            if (ret) {
                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]successfully");
                this.printMsgTool("Result：Download the plaintext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the plaintext master key[" + keyId + "]failed");
                this.printMsgTool("Result：Download the plaintext master key[" + keyId + "]failed");
                String errString = imksk.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }
//
//    private byte[] My23getKeyKCV(int keyIndex, int keyType) {
//        byte[] KCV = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            KCV = iPinpad.getKeyKCV(keyIndex, keyType);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("getKeyKCV executeTime : " + (endTime - startTime) + " ms");
//
//            if (KCV == null) {
//                logUtils.addCaseLog("Failed to get KCV");
//                this.printMsgTool("Result：Failed to get KCV");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                logUtils.addCaseLog("Acquire KCV successfully");
//                this.printMsgTool("Result：Acquire KCV successfully");
//                logUtils.addCaseLog("KCV:");
//                logUtils.addCaseLog(StringUtil.byte2HexStr(KCV));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return KCV;
//    }
//

//
//    private byte[] My27calculateDataEx(int mode, int desType, byte[] key, byte[] data, byte[] initVec) {
//        byte[] result = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            result = iPinpad.calculateDataEx(mode, desType, key, data, initVec);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("calculateDataEx executeTime : " + (endTime - startTime) + " ms");
//
//
//            if (result == null) {
//                logUtils.addCaseLog("Encryption/decryption failed");
//                this.printMsgTool("Result：Encryption/decryption failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                if ((mode == 0)) {
//                    logUtils.addCaseLog("Encryption success");
//                    this.printMsgTool("Execution result: Encryption/decryption successful");
//                    logUtils.addCaseLog("Encryption result: " + StringUtil.byte2HexStr(result));
//                    this.printMsgTool("Execution result: Encryption result: " + StringUtil.byte2HexStr(result));
//                } else if ((mode == 1)) {
//                    logUtils.addCaseLog("Decryption success");
//                    this.printMsgTool("Execution result: decryption successful");
//                    logUtils.addCaseLog("Decryption result: " + StringUtil.byte2HexStr(result));
//                    this.printMsgTool("Execution result: Decryption result: " + StringUtil.byte2HexStr(result));
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("Encryption/decryption failed");
//        }
//        return result;
//    }
//
//    private byte[] My28encryptPinFormat0(int pinKeyId, int desType, byte[] cardNumber, String passwd) {
//        byte[] result = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            result = iPinpad.encryptPinFormat0(pinKeyId, desType, cardNumber, passwd);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("encryptPinFormat0 executeTime : " + (endTime - startTime) + " ms");
//
//            if (result == null) {
//                logUtils.addCaseLog("PIN encryption failed");
//                this.printMsgTool("Execution result: PIN encryption failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//
//                logUtils.addCaseLog("IN encryption successful");
//                this.printMsgTool("Execution result: PIN encryption was successful");
//                logUtils.addCaseLog("Encryption result: " + StringUtil.byte2HexStr(result));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("PIN encryption failed");
//        }
//        return result;
//    }
//
//    private byte[] My29calculateByDataKey(int keyId, int encAlg, int encMode, int encFlag, byte[] data, byte[] initVec) {
//        byte[] result = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            result = iPinpad.calculateByDataKey(keyId, encAlg, encMode, encFlag, data, initVec);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("calculateByDataKey executeTime : " + (endTime - startTime) + " ms");
//
//            if (result == null) {
//                logUtils.addCaseLog("TDKEY computation failed");
//                this.printMsgTool("Result: TDKEY computation failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//
//                logUtils.addCaseLog("The TDKEY was calculated successfully");
//                this.printMsgTool("Result: TDKEY was successfully calculated");
//                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(result));
//                this.printMsgTool("Result: " + StringUtil.byte2HexStr(result));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            logUtils.addCaseLog("TDKEY computation failed");
//        }
//        return result;
//    }
//
//    private boolean My30loadEncryptMainKeyEX(int keyId, byte[] key, int algorithmType, byte[] checkValue, Bundle extend) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadEncryptMainKeyEX(keyId, key, algorithmType, checkValue, extend);
//            long endTime = System.currentTimeMillis();
////            logUtils.addCaseLog("loadEncryptMainKeyEX executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
////                this.printMsgTool("Execution result: Download the ciphertext master key[" + keyId + "]successfully");
//            } else {
//                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
////                this.printMsgTool("Execution result: Download the ciphertext master key[" + keyId + "]failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
////                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private boolean My31loadWorkKeyEX(int keyType, int mkId, int wkId, int decKeyType, byte[] key, byte[] checkValue, Bundle extend) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadWorkKeyEX(keyType, mkId, wkId, decKeyType, key, checkValue, extend);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadWorkKeyEX executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("Download the working key[" + wkId + "]successfully");
//                this.printMsgTool("Result:Download the working key[" + wkId + "]successfully");
//            } else {
//                logUtils.addCaseLog("Download the working key[" + wkId + "]failed");
//                this.printMsgTool("Result:Download the working key[" + wkId + "]failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private boolean My32clearKey(int keyId, int keyType) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.clearKey(keyId, keyType);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("clearKey executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("Clear key successfully");
//                this.printMsgTool("Result:Clear key successfully");
//            } else {
//                logUtils.addCaseLog("Clear key failed");
//                this.printMsgTool("Result:Clear key failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private boolean My33loadDukptKeyEX(int keyId, byte[] ksn, byte[] key, byte[] checkValue, Bundle extend) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadDukptKeyEX(keyId, ksn, key, checkValue, extend);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadDukptKeyEX executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("download dukpt key[" + keyId + "]successfully");
//                this.printMsgTool("Result:download dukpt key[" + keyId + "]successfully");
//            } else {
//                logUtils.addCaseLog("download dukpt key[" + keyId + "]failed");
//                this.printMsgTool("Result:download dukpt key[" + keyId + "]failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private boolean My34loadTEKEX(int keyId, byte[] key, byte algorithmType, byte[] checkValue, Bundle extend) {
//        boolean ret = false;
//        try {
//            long startTime = System.currentTimeMillis();
//            ret = iPinpad.loadTEKEX(keyId, key, algorithmType, checkValue, extend);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("loadTEKEX executeTime : " + (endTime - startTime) + " ms");
//
//            if (ret) {
//                logUtils.addCaseLog("Download the transmission key[" + keyId + "]successfully");
//                this.printMsgTool("Result:Download the transmission key[" + keyId + "]successfully");
//            } else {
//                logUtils.addCaseLog("Download the transmission key[" + keyId + "]failed");
//                this.printMsgTool("Result:Download the transmission key[" + keyId + "]failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private byte[] My35getRandom(byte length) {
//        byte[] Random = null;
//        try {
//            long startTime = System.currentTimeMillis();
//            Random = iPinpad.getRandom(length);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("getRandom executeTime : " + (endTime - startTime) + " ms");
//
//
//            if (length == (byte) 0 || length > (byte) 127) {
//                logUtils.addCaseLog("Failed to get random number");
//                this.printMsgTool("Result：Failed to get random number");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            } else {
//                logUtils.addCaseLog("Retrieve random number successfully");
//                logUtils.addCaseLog("Random:");
//                logUtils.addCaseLog(StringUtil.byte2HexStr(Random));
//                this.printMsgTool("Result：Retrieve random number successfully");
//                this.printMsgTool("Random:");
//                this.printMsgTool(StringUtil.byte2HexStr(Random));
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return Random;
//    }
//
//    //36autoPinpad仅用于执行自动化测试
//    private boolean My36autoPinpad(int keyId, byte[] key, byte algorithmType, byte[] checkValue, Bundle extend) {
//        boolean ret = false;
//        try {
//            ret = iPinpad.loadTEKEX(keyId, key, algorithmType, checkValue, extend);
//            if (ret) {
//                logUtils.addCaseLog("Download the transmission key[" + keyId + "]successfully");
//
//            } else {
//                logUtils.addCaseLog("Download the transmission key[" + keyId + "]failed");
//                String errString = iPinpad.getLastError();
//                if (errString != null) {
//                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    private void My37startX990PinInput(Bundle param) {
//        try {
//            ix990Pinpad.startKeyboardInput(param, new PinInputListener.Stub() {
//                @Override
//                public void onInput(int len, int key) throws RemoteException {
//                    Log.i(TAG, "len = " + len + ";key = " + key);
//                    Message msg = new Message();
//                    msg.getData().putString("msg", "input length entered len = " + len + " The current key value = " + key);
//                    handler.sendMessage(msg);
//                }
//
//                @Override
//                public void onConfirm(byte[] data, boolean isNonePin) throws RemoteException {
//                    Log.i(TAG, "确认");
//                    Log.i(TAG, "input Data:" + StringUtil.byte2HexStr(data));
//
//                    Message msg = new Message();
//                    msg.getData().putString("msg", "input data:" + StringUtil.byte2HexStr(data));
//                    handler.sendMessage(msg);
//                    /*
//                    if (ksn == null) {
//                        logUtils.addCaseLog("getKSN failed");
//                        String errString = iDukpt.getLastError();
//                        if (errString != null) {
//                            logUtils.addCaseLog(errString);
//                            printMsgTool("Result：getKSN failed，error=" + errString);
//                        }
//                    } else {
//                        Log.i(TAG, "ksn=" + ksn);
//                        logUtils.addCaseLog("getKSN success");
//                        logUtils.addCaseLog("KSN: " + StringUtil.byte2HexStr(ksn));
//                        printMsgTool("Result：getKSN success，result=" + StringUtil.byte2HexStr(ksn));
//                    }
//                    boolean isNonePin = pinInfos.getBoolean("isEncrypt");
//                    Log.i(TAG, "isNonePin:" + isNonePin);
//                    Message msg = new Message();
//                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
////                        logUtils.addCaseLog("PIN: " + StringUtil.byte2HexStr(data));
//                    msg.getData().putString("msg", "isNonePin:" + isNonePin + "\nPIN: " + StringUtil.byte2HexStr
//                            (data));
//                    handler.sendMessage(msg);
//                    */
//                }
//
//                @Override
//                public void onCancel() throws RemoteException {
//                    Log.i(TAG, "onCancel Cancel PIN Input");
//                    Message msg = new Message();
//                    msg.getData().putString("msg", "onCancel Cancel PIN Input");
//                    handler.sendMessage(msg);
//                }
//
//                @Override
//                public void onError(int errorCode) throws RemoteException {
//                    Log.i(TAG, "onError:" + errorCode);
//                    Message msg = new Message();
//                    String errString = iPinpad.getLastError();
//                    if (errString != null) {
//                        Log.i(TAG, "getLastError=" + errString);
//                        msg.getData().putString("msg", errString);
//                        handler.sendMessage(msg);
//                        return;
//                    }
//                    msg.getData().putString("msg", "onError:" + errorCode);
//                    handler.sendMessage(msg);
//                }
//            });
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
////        try {
////            ix990Pinpad.startKeyboardInput(param, new PinInputListener.Stub() {
////                @Override
////                public void onInput(int len, int key) throws RemoteException {
////                    Log.i(TAG,"len = "+len+";key = "+key);
////                    Message msg = new Message();
////                    msg.getData().putString("msg","input length entered len = "+len+" The current key value = "+ key);
////                    handler.sendMessage(msg);
////                }
////
////                @Override
////                public void onConfirm(byte[] data, boolean isNonePin) throws RemoteException {
////                    Log.i(TAG,"确认");
////                    Log.i(TAG,"PIN:"+StringUtil.byte2HexStr(data));
////                    byte[] ksn = getDukptKsn(0);
////                    if (ksn == null){
////                        logUtils.addCaseLog("getKSN failed");
////                        String errString = iDukpt.getLastError();
////                        if (errString != null) {
////                            logUtils.addCaseLog(errString);
////                            printMsgTool("Result：getKSN failed，error=" + errString);
////                        }
////                    }else {
////                        Log.i(TAG, "ksn=" + ksn);
////                        logUtils.addCaseLog("getKSN success");
////                        logUtils.addCaseLog("KSN: " + StringUtil.byte2HexStr(ksn));
////                        printMsgTool("Result：getKSN success，result=" + StringUtil.byte2HexStr(ksn));
////                    }
////                    Log.i(TAG, "isNonePin:" + isNonePin);
////                    Message msg = new Message();
////                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
//////                        logUtils.addCaseLog("PIN: " + StringUtil.byte2HexStr(data));
////                    msg.getData().putString("msg", "isNonePin:" + isNonePin + "\nPIN: " + StringUtil.byte2HexStr
////                            (data));
////                    handler.sendMessage(msg);
////                }
////
////                @Override
////                public void onCancel() throws RemoteException {
////                    Log.i(TAG, "onCancel Cancel PIN Input");
////                    Message msg = new Message();
////                    msg.getData().putString("msg", "onCancel Cancel PIN Input");
////                    handler.sendMessage(msg);
////                }
////
////                @Override
////                public void onError(int errorCode) throws RemoteException {
////                    Log.i(TAG, "onError:" + errorCode);
////                    Message msg = new Message();
////                    String errString = iPinpad.getLastError();
////                    if (errString != null) {
////                        Log.i(TAG, "getLastError=" + errString);
////                        msg.getData().putString("msg", errString);
////                        handler.sendMessage(msg);
////                        return;
////                    }
////                    msg.getData().putString("msg", "onError:" + errorCode);
////                    handler.sendMessage(msg);
////                }
////            });
////        } catch (RemoteException e) {
////            e.printStackTrace();
////        }
//    }
//
//    public void My38searchCard(RFSearchListener listener, int timeout) {
//        try {
//            irfCardReader.searchCard(listener, timeout);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean My39openSerial() {
//        try {
//            logUtils.addCaseLog("open()执行");
//            return iSerialport.open();
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("open()执行异常");
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean My40initSerial(int bps, int par, int dbs) {
//        try {
//            logUtils.addCaseLog("init()执行");
//            logUtils.addCaseLog("bps=" + bps + ", par=" + par + ", dbs=" + dbs);
//            return iSerialport.init(bps, par, dbs);
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("init()执行异常");
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public int My41writeSerial(byte[] data, int timeout) {
//        try {
//            logUtils.addCaseLog("write() execute");
//            return iSerialport.write(data, timeout);
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("write() exception");
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    public void My42emvCheckCard(Bundle cardOption, int timeout, CheckCardListener listener) {
//        try {
//            iemv.checkCard(cardOption, timeout, listener);
//            logUtils.addCaseLog("checkCard execute");
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("checkCard execute exception");
//            e.printStackTrace();
//        }
//    }
//
//
//    public int My43readSerial(byte[] buffer, int expectLen, int timeout) {
//        try {
//            logUtils.addCaseLog("read()执行");
//            return iSerialport.read(buffer, expectLen, timeout);
//        } catch (NullPointerException npe) {
//            logUtils.addCaseLog("read()执行异常-");
//            npe.printStackTrace();
//            return -1;
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("read()执行异常");
//            e.printStackTrace();
//            return -1;
//        }
//
////        try {
////            logUtils.addCaseLog("read()执行");
////            byte[] readData = new byte[32];
////            int i = iSerialport.read(readData,5,5000);
////            Log.d(TAG,"读取数据:"+i);
////            return i;
////        } catch (NullPointerException npe) {
////            logUtils.addCaseLog("read()执行异常-");
////            npe.printStackTrace();
////            return -1;
////        } catch (RemoteException e) {
////            logUtils.addCaseLog("read()执行异常");
////            e.printStackTrace();
////            return -1;
////        }
//    }
//
//    public int My44isUsbSerialConnect() {
//        Log.d("TAG", "isUsbSerialConnect execute");
//        boolean result = false;
//        try {
//            result = iUsbSerialPort.isUsbSerialConnect();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("TAG", "****连接Usb设备:" + result);
//        return 0;
//    }
//
//    public int My45read() {
//        try {
//            byte[] readData = new byte[3];
//            int num = iUsbSerialPort.read(readData, 5000);
//            Log.d("TAG", "读取数据:" + num);
//
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        return 0;
//    }
//
//    public int My46write() {
//        byte[] writeData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
//        try {
//            iUsbSerialPort.write(writeData);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public int My47keyStoreTR34Payload(String path) throws IOException {
//        logUtils.addCaseLog("My47keyStoreTR34Payload executed");
//        byte[] bytes = readJsonFile(path);
//        Log.d(TAG,"bytes = "+ Arrays.toString(bytes));
//        try {
//            return ikld.keyStoreTR34Payload(bytes);
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public byte[] readJsonFile(String path) throws IOException {
//        logUtils.addCaseLog("readJsonFile start.....path="+path+".");
//        File file = new File(path);
//        BufferedReader bufferedReader = null;
//        bufferedReader = new BufferedReader(new FileReader(file));
//        StringBuilder stringBuilder = new StringBuilder();
//        String s;
//        while ((s = bufferedReader.readLine()) != null) {
//            stringBuilder.append(s);
//        }
//        Log.i(TAG, "json: " + stringBuilder);
//        bufferedReader.close();
//        return stringBuilder.toString().getBytes();
//    }
//
//
//
//    public void L01001() {
//        logUtils.printCaseInfo("L01001");
//        logUtils.addCaseLog("execute L01001");
//        Log.i(TAG, "L01001() executed");
//        boolean ret;
//        int keyId = 0;
//        int keyType = PinpadKeyType.MASTERKEY;
//        ret = My04loadPlainMasterKey(keyId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        boolean retValue = My01isKeyExist(keyType, keyId);
//        if (retValue) {
//            logUtils.addCaseLog("L01001 case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01001 case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01001 case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01001 case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01002() {
//        logUtils.printCaseInfo("L01002");
//        logUtils.addCaseLog("Execute the L01002 case");
//        Log.i(TAG, "L01002() executed");
//        boolean ret;
//        int keyId = 0;
//        int keyType = PinpadKeyType.MASTERKEY;
//        ret = My04loadPlainMasterKey(keyId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        keyId = 38;
//        boolean retValue = My01isKeyExist(keyType, keyId);
//        if (retValue) {
//            logUtils.addCaseLog("L01002 case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01002 case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01002 case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01002 case results：" + retValue + "，execute success！");
//    }
//
//
//    public void L01003() {
//        logUtils.printCaseInfo("L01003");
//        logUtils.addCaseLog("Execute the case L01003");
//        Log.i(TAG, "L01003() executed");
//
//        boolean ret;
//        int mkId = 0;
//        //先下装明文主密钥
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //下装MAC工作密钥
//        int keyType = PinpadKeyType.MACKEY;
//        int wkId = 99;
//        ret = My05loadWorkKey(keyType, mkId, wkId, macKey, null);
//        if (!ret) {
//            return;
//        }
//
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01003 Case results：" + retValue + "，execute Failed！");
//
//    }
//
//    public void L01004() {
//        logUtils.printCaseInfo("L01004");
//        logUtils.addCaseLog("Execute the L01004 case");
//        Log.i(TAG, "L01004() executed");
//        boolean ret;
//        int mkId = 0;
//        //先下装明文主密钥
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //下装MAC工作密钥
//        int keyType = PinpadKeyType.MACKEY;
//        int wkId = 99;
//        ret = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        wkId = 38;
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01004 Case results：" + retValue + "，execute success！");
//
//    }
//
//    public void L01005() {
//        logUtils.printCaseInfo("L01005");
//        logUtils.addCaseLog("Execute the L01005 case");
//        Log.i(TAG, "L01005() executed");
//        boolean ret;
//        int mkId = 0;
//        //先下装明文主密钥
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //下装PIN工作密钥
//        int keyType = PinpadKeyType.PINKEY;
//        int wkId = 99;
//        ret = My05loadWorkKey(keyType, mkId, wkId, pinKey, pinKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01005 Case results：" + retValue + "，execute Failed！");
//
//    }
//
//    public void L01006() {
//
//
//        logUtils.printCaseInfo("L01006");
//        logUtils.addCaseLog("Execute the L01006 case");
//        Log.i(TAG, "L01006() executed");
//        boolean ret;
//        int mkId = 1;
//        //先下装明文主密钥
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //下装PIN工作密钥
//        int keyType = PinpadKeyType.PINKEY;
//        int wkId = 1;
//        ret = My05loadWorkKey(keyType, mkId, wkId, pinKey, pinKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        wkId = 1;
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01006 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01006 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01006 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01006 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01007() {
//        logUtils.printCaseInfo("L01007");
//        logUtils.addCaseLog("Execute the L01007 case");
//        Log.i(TAG, "L01007() executed");
//        boolean ret;
//        int mkId = 0;
//        //先下装明文主密钥
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //下装TD工作密钥
//        int keyType = PinpadKeyType.TDKEY;
//        int wkId = 99;
//        ret = My05loadWorkKey(keyType, mkId, wkId, tdKey, tdKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01008() {
//        logUtils.printCaseInfo("L01008");
//        logUtils.addCaseLog("Execute the L01008 case");
//        Log.i(TAG, "L01008() executed");
//        boolean ret;
//        int mkId = 0;
//        //先下装明文主密钥
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //下装TD工作密钥
//        int keyType = PinpadKeyType.TDKEY;
//        int wkId = 99;
//        ret = My05loadWorkKey(keyType, mkId, wkId, tdKey, tdKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        wkId = 49;
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01009() {
//        logUtils.printCaseInfo("L01009");
//        logUtils.addCaseLog("Execute the L01009 case");
//        Log.i(TAG, "L01009() executed");
//        //下装明文主密钥
//        boolean ret;
//        int mkId = 0;
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //测试无效的keyType
//        int keyType = -1;
//        boolean retValue = My01isKeyExist(keyType, mkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01010() {
//        logUtils.printCaseInfo("L01010");
//        logUtils.addCaseLog("Execute the L01010 case");
//        Log.i(TAG, "L01010() executed");
//        //下装明文主密钥
//        boolean ret;
//        int mkId = 0;
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //测试无效的keyType
//        int keyType = 16;
//        Log.i(TAG, "keyType=" + keyType);
//        boolean retValue = My01isKeyExist(keyType, mkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01010 Case results：" + retValue + "，execute success！");
//
//    }
//
//    public void L01011() {
//        logUtils.printCaseInfo("L01011");
//        logUtils.addCaseLog("Execute the L01011 case");
//        Log.i(TAG, "L01011() executed");
//        //下装明文主密钥
//        boolean ret;
//        int mkId = 0;
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //测试无效的keyId
//        mkId = -1;
//        boolean retValue = My01isKeyExist(PinpadKeyType.MASTERKEY, mkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01012() {
//        logUtils.printCaseInfo("L01012");
//        logUtils.addCaseLog("Execute the L01012 case");
//        Log.i(TAG, "L01012() executed");
//        //下装明文主密钥
//        boolean ret;
//        int mkId = 0;
//        ret = My04loadPlainMasterKey(mkId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }
//        //测试无效的keyId
//        mkId = 100;
//        boolean retValue = My01isKeyExist(PinpadKeyType.MASTERKEY, mkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01012 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01012 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01012 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01012 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01013() {
//        L16003();
//        boolean retValue = My01isKeyExist(12, 0);
//        My01isKeyExist(12, 1);
//        My01isKeyExist(12, 2);
//        My01isKeyExist(12, 3);
//        My01isKeyExist(12, 4);
//        if (retValue) {
//            logUtils.addCaseLog("L01013 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01013 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01013 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01013 Case results：" + retValue + "，execute Failed！");
//    }
//
//
//    public void L01014() {
//        L16003();
//        int kId = 14;
//        boolean retValue = My01isKeyExist(12, kId);
//        if (retValue) {
//            logUtils.addCaseLog("L01014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01015() {
//        L22002();
//        boolean retValue = My01isKeyExist(8, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01016() {
//        L22002();
//        boolean retValue = My01isKeyExist(8, 91);
//        if (retValue) {
//            logUtils.addCaseLog("L01016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01016 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01017() {
//        L12022();
//        boolean retValue = My01isKeyExist(9, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01017 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01017 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01017 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01017 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01018() {
//        L12022();
//        boolean retValue = My01isKeyExist(9, 9);
//
//        if (retValue) {
//            logUtils.addCaseLog("L01018 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01018 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01018 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01018 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01019() {
//        L12023();
//        boolean retValue = My01isKeyExist(10, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01019 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01019 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01019 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01019 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01020() {
//        L12023();
//        boolean retValue = My01isKeyExist(10, 59);
//
//        if (retValue) {
//            logUtils.addCaseLog("L01020 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01020 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01020 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01020 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01021() {
//        L12024();
//        boolean retValue = My01isKeyExist(11, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01021 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01021 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01021 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01021 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01022() {
//        L12024();
//        boolean retValue = My01isKeyExist(11, 69);
//
//        if (retValue) {
//            logUtils.addCaseLog("L01022 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01022 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01022 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01022 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01023() {
//        loadTEK();
//        boolean retValue = My01isKeyExist(13, 0);
//        if (retValue) {
//            logUtils.addCaseLog("L01023 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01023 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01023 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01023 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01024() {
//        loadTEK();
//        My01isKeyExist(13, 1);
//        boolean retValue = My01isKeyExist(8, 91);
//        if (retValue) {
//            logUtils.addCaseLog("L01024 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01024 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01024 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01024 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L01025() {
//        L20006();
//        boolean retValue = My01isKeyExist(15, 10);
//        if (retValue) {
//            logUtils.addCaseLog("L01025 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01025 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01025 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01025 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01026() {
//        L20006();
//        boolean retValue = My01isKeyExist(15, 2);
//
//        if (retValue) {
//            logUtils.addCaseLog("L01026 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L01026 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L01026 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L01026 Case results：" + retValue + "，execute success！");
//    }
//
//    //L01027-L01037仅用于测试Service升级后，密钥是否会丢失测试
//    public void L01027() {
//        logUtils.printCaseInfo("L01027");
//        logUtils.addCaseLog("Execute the L01027 case");
//        Log.i(TAG, "L01027() executed");
//        boolean ret;
//        int keyId = 0;
//        int keyType = PinpadKeyType.MASTERKEY;
//       /* ret = My04loadPlainMasterKey(keyId, mKey, mKeyCheckValue);
//        if (!ret) {
//            return;
//        }*/
//        boolean retValue = My01isKeyExist(keyType, keyId);
//        if (retValue) {
//            logUtils.addCaseLog("L01027 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01027 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01027 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01027 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01028() {
//        logUtils.printCaseInfo("L01028");
//        logUtils.addCaseLog("Execute the L01028 case");
//        Log.i(TAG, "L01028() executed");
//
//        //下装MAC工作密钥
//        int keyType = PinpadKeyType.MACKEY;
//        int wkId = 99;
//
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01028 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01028 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01028 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01028 Case results：" + retValue + "，execute Failed！");
//
//    }
//
//    public void L01029() {
//        logUtils.printCaseInfo("L01029");
//        logUtils.addCaseLog("Execute the L01029 case");
//        Log.i(TAG, "L01029() executed");
//
//        int keyType = PinpadKeyType.PINKEY;
//        int wkId = 99;
//
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01029 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01029 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01029 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01029 Case results：" + retValue + "，execute Failed！");
//
//    }
//
//    public void L01030() {
//        logUtils.printCaseInfo("L01030");
//        logUtils.addCaseLog("Execute the L01030 case");
//        Log.i(TAG, "L01030() executed");
//
//        int keyType = PinpadKeyType.TDKEY;
//        int wkId = 99;
//
//        boolean retValue = My01isKeyExist(keyType, wkId);
//        if (retValue) {
//            logUtils.addCaseLog("L01030 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01030 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01030 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01030 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01031() {
//
//        boolean retValue = My01isKeyExist(12, 0);
//        if (retValue) {
//            logUtils.addCaseLog("L01031 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01031 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01031 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01031 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01032() {
//
//        boolean retValue = My01isKeyExist(8, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01032 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01032 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01032 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01032 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01033() {
//        boolean retValue = My01isKeyExist(9, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01033 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01033 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01033 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01033 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01034() {
//        boolean retValue = My01isKeyExist(10, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01034 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01034 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01034 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01034 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01035() {
//        boolean retValue = My01isKeyExist(11, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L01035 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01035 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01035 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01035 Case results：" + retValue + "，execute Failed！");
//    }
//
//
//    public void L01036() {
//        boolean retValue = My01isKeyExist(13, 0);
//        if (retValue) {
//            logUtils.addCaseLog("L01036 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01036 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01036 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01036 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01037() {
//        boolean retValue = My01isKeyExist(15, 10);
//        if (retValue) {
//            logUtils.addCaseLog("L01037 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L01037 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L01037 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L01037 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L01038() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("824CF43A1F9447C8");
//        Log.d(TAG, "L01038: start loadEncryptWorkKey: " + System.currentTimeMillis());
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 1, checkvalue, extend);
//        Log.d(TAG, "L01038: finish loadEncryptWorkKey: " + System.currentTimeMillis());
//
//        //下装PIN工作密钥
//        int keyType = PinpadKeyType.MACKEY;
//        int wkId = 99;
//        Log.d(TAG, "L01038: start loadWorkKey: " + System.currentTimeMillis());
//        boolean ret = My05loadWorkKey(keyType, 99, wkId, macKey, macKeyCheckValue);
//        Log.d(TAG, "L01038: finish loadWorkKey: " + ret + " : " + System.currentTimeMillis());
//    }
//
////    public void loadTEK() {
////        logUtils.printCaseInfo("loadTEK");
////        logUtils.addCaseLog("执行loadTEK案例");
////        Log.i(TAG, "loadTEK() executed");
////
////        boolean ret = false;
////        String tekString = "31313131313131313131313131313131";
////        byte[] tek = BCDDecode.str2Bcd(tekString);
////
////        // plain mk = "61616161616161616161616161616161"
//////        String mkString = "C6B9D102DDEAB169F324CBDBE733F00A";
////        String mkString = "EB625B1AE49B9465EB625B1AE49B9465";
////        byte[] mk = BCDDecode.str2Bcd(mkString);
////        // mk check value
//////        String mkKCVString = "DC3A2C49AA385B0C"; //DC3A2C49AA385B0C6742D9C32B181215 take first 8 bytes
//////        byte[] mkKCV = BCDDecode.str2Bcd(mkKCVString);
////
////       // plain pinkey = "62626262626262626262626262626262"
//////        String pinKeyString = "1949B5A470574237D187843495A3E617";
////        String pinKeyString = "E2258670B1BFE9D7E2258670B1BFE9D7";
////        byte[] pinKey = BCDDecode.str2Bcd(pinKeyString);
////
////        try {
////            ret = iPinpad.loadTEKWithAlgorithmType(0, tek, (byte)0x02, null);
////            Log.d(TAG, "" +ret);
////            ret = iPinpad.loadEncryptMainKeyWithAlgorithmType(0, mk, 0x01, null);
////            Log.d(TAG, "" +ret);
////            ret = iPinpad.loadWorkKeyWithDecryptType(2, 0, 0, 0x00, pinKey, null);
////            Log.d(TAG, "" +ret);
////            ret = iPinpad.isKeyExist(10, 0);
////            Log.d(TAG, "" +ret);
////
////            Bundle param = new Bundle();
////            String panBlock = "6214920206694802";
////            byte[] pinLimit = {4, 12};
////            param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
////            param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
////            param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
////            param.putString(BUNDLE_PINPARAM_PAN, panBlock);
////            param.putString("promptString", "hello world");
////            param.putInt("keysType", 0x00);
////            param.putInt("desType", 0x01);
//////            param.putInt("pinAlgMode", 3);
////            My01startPinInput(0, param);
////
////        } catch (RemoteException e) {
////            e.printStackTrace();
////        }
////
////    }
//
    public void loadTEK() {
        logUtils.printCaseInfo("loadTEK");
        logUtils.addCaseLog("Execute the loadTEK case");
        Log.i(TAG, "loadTEK() executed");
        int keyId = 0;
        String keyString = "31313131313131313131313131313131";
        byte[] key = BCDDecode.str2Bcd(keyString);
        byte[] checkValue = {(byte) 0x40, (byte) 0x82, (byte) 0x6A, (byte) 0x58, (byte) 0x00, (byte) 0x60, (byte) 0x8C, (byte) 0x87};
        boolean retValue = loadTEK(keyId, key, checkValue);
        if (retValue) {
            logUtils.addCaseLog("loadTEK Case results：" + retValue + "，execute success！");
            this.printMsgTool("loadTEK Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("loadTEK Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("loadTEK Case results：" + retValue + "，execute Failed！");
    }
//
//    public void L02002() {
//        logUtils.printCaseInfo("L02002");
//        logUtils.addCaseLog("Execute the L02002 case");
//        Log.i(TAG, "L02002() executed");
//        int keyId = 99;
//        //给key和check value赋随机值
//        String keyString = "313131313131313132323232323232323333333333333333";
//        byte[] key = BCDDecode.str2Bcd(keyString);
//        byte[] checkValue = {(byte) 0x40, (byte) 0x82, (byte) 0x6A, (byte) 0x58, (byte) 0x00, (byte) 0x60, (byte) 0x8C, (byte) 0x87};
//        String checkValueStr;
//
//        String dataStr = "0000000000000000";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//
//        String initVecStr = "0000000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//
//        key = My35getRandom((byte) 24);
//        checkValueStr = StringUtil.byte2HexStr(My27calculateDataEx(0, 1, key, data, initVec));
//        checkValue = BCDDecode.str2Bcd(checkValueStr);
//        //下载TEK
//        boolean retValue = My02loadTEK(keyId, key, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L02002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L02002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L02002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L02003() {
//        logUtils.printCaseInfo("L02003");
//        logUtils.addCaseLog("Execute the L02003 case");
//        Log.i(TAG, "L02003() executed");
//        byte[] checkValue = null;
//        int keyId = 90;
//
//        boolean retValue = My02loadTEK(keyId, tekKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L02003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L02003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L02003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L02004() {
//        logUtils.printCaseInfo("L02004");
//        logUtils.addCaseLog("Execute the L02004 case");
//        Log.i(TAG, "L02004() executed");
//        int keyId = 99;
//        String tekKeyStr = "3CAEFDD46D32C796059FBA457C7D5D1E3CAEFDD46D32C7";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyStr);
//        boolean retValue = My02loadTEK(keyId, tekKey, tekKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02004 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02009() {
//        logUtils.printCaseInfo("L02009");
//        logUtils.addCaseLog("Execute the L02009 case");
//        Log.i(TAG, "L02009() executed");
//        int keyId = 99;
//        String tekKeyStr = "3CAEFDD46D32C796059FBA457C7D5D1E3CAEFDD46D32C79611";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyStr);
//        boolean retValue = My02loadTEK(keyId, tekKey, tekKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("LL02009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02010() {
//        logUtils.printCaseInfo("L02010");
//        logUtils.addCaseLog("Execute the L02010 case");
//        Log.i(TAG, "L02010() executed");
//        //测试无效key
//        int keyId = 0;
//        byte[] key = null;
//        boolean retValue = My02loadTEK(keyId, key, tekKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02011() {
//        logUtils.printCaseInfo("L02011");
//        logUtils.addCaseLog("Execute the L02011 case");
//        Log.i(TAG, "L02011() executed");
//        //测试无效key
//        int keyId = 0;
//        byte[] key = {};
//        boolean retValue = My02loadTEK(keyId, key, tekKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02014() {
//        logUtils.printCaseInfo("L02014");
//        logUtils.addCaseLog("Execute the L02014 case");
//        Log.i(TAG, "L02014() executed");
//        int keyId = 0;
//        byte[] checkValue = {(byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22};
//        boolean retValue = My02loadTEK(keyId, tekKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02015() {
//        logUtils.printCaseInfo("L02015");
//        logUtils.addCaseLog("Execute the L02015 case");
//        Log.i(TAG, "L02015() executed");
//        int keyId = 0;
//        //给key赋随机值
//        String keyString = "31313131313131313131313131313131";
//        byte[] key = BCDDecode.str2Bcd(keyString);
//        key = My35getRandom((byte) 16);
//        byte[] checkValue = {};
//        boolean retValue = My02loadTEK(keyId, key, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L02015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L02015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L02015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L02016() {
////        iPinpad.clearKey(0,13);
//        logUtils.printCaseInfo("L02016");
//        logUtils.addCaseLog("Execute the L02016 case");
//        Log.i(TAG, "L02016() executed");
//        int keyId = 16;
//
//        String keyString = "313131313131313131313131313131313131313131313131";
//        byte[] key = BCDDecode.str2Bcd(keyString);
//
//        String initVecStr = "0000000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//
//        String dataStr = "0000000000000000";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//
//        byte[] checkValue = {(byte) 0x40, (byte) 0x82, (byte) 0x6A, (byte) 0x58, (byte) 0x00, (byte) 0x60, (byte) 0x8C, (byte) 0x87};
//        String checkValueStr;
//        checkValueStr = StringUtil.byte2HexStr(My27calculateDataEx(0, 1, key, data, initVec));
//        checkValue = BCDDecode.str2Bcd(checkValueStr);
//
//
//        key = My35getRandom((byte) 24);
//
//        boolean retValue = My02loadTEK(keyId, key, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02016 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("LL02016 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L02016 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L02016 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L02017() {
//        logUtils.printCaseInfo("L02017");
//        logUtils.addCaseLog("Execute the L02017 case");
//        Log.i(TAG, "L02017() executed");
//        int keyId = 0;
//        String checkvalueString = "73B817C302ABE85B73";
//        byte[] checkValue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My02loadTEK(keyId, tekKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02017 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02017 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02017 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02017 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02018() {
//        logUtils.printCaseInfo("L02018");
//        logUtils.addCaseLog("Execute the L02018 case");
//        Log.i(TAG, "L02002() executed");
//        int keyId = -1;
//        boolean retValue = My02loadTEK(keyId, tekKey, tekKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02018 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02018 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02018 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02018 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L02019() {
//        logUtils.printCaseInfo("L02019");
//        logUtils.addCaseLog("Execute the L02019 case");
//        Log.i(TAG, "L02019() executed");
//        int keyId = 100;
//        boolean retValue = My02loadTEK(keyId, tekKey, tekKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02019 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02019 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02019 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02019 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03001() {
//        loadTEK();
//        logUtils.printCaseInfo("L03001");
//        logUtils.addCaseLog("Execute the L03001 case");
//        Log.i(TAG, "L03001() executed");
//        int keyId = 0;
//        byte[] checkValue = null;
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L03001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L03001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L03001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    /* public void L03002() {
//         FormatKeyArea();
//         loadTEK();
//         logUtils.printCaseInfo("L03002");
//         logUtils.addCaseLog("执行L03002案例");
//         Log.i(TAG, "L03002() executed");
//         int keyId = 99;
//
//         String mEKeyStr ="EB625B1AE49B9465EB625B1AE49B9465";
//         String mEKeyCheckValueStr = "5FF660CB09F83313";
//         byte[] mEKey = BCDDecode.str2Bcd(mEKeyStr);
//         byte[] mEKeyCheckValue = BCDDecode.str2Bcd(mEKeyCheckValueStr);
//         boolean retValue =  My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//         if(retValue){
//             logUtils.addCaseLog("L03002 Case results：" + retValue + "，execute success！");
//             this.printMsgTool("L03002 Case results：" + retValue + "，execute success！");
//             return;
//         }
//         logUtils.addCaseLog("L03002 Case results：" + retValue + "，execute Failed！");
//         this.printMsgTool("L03002 Case results：" + retValue + "，execute Failed！");
//         //        byte[] key2 = new byte[17];
//         //        System.arraycopy(mEKey, 0, key2, 0, 16);
//         //        key2[16] = PinpadKeyType.MASTERKEY;
//         //        My03loadEncryptMainKey(keyId, key2, mEKeyCheckValue);
//         //
//         //        byte[] key3 = new byte[25];
//         //        System.arraycopy(mEKey, 0, key3, 0, 16);
//         //        key3[16] = PinpadKeyType.MASTERKEY;
//         //        System.arraycopy(mEKeyCheckValue, 0, key3, 17, 8);
//         //        My03loadEncryptMainKey(keyId, key3, null);
//         //
//         //        byte[] key4 = new byte[25];
//         //        System.arraycopy(mEKey, 0, key4, 0, 16);
//         //        key4[16] = PinpadKeyType.MASTERKEY;
//         //        System.arraycopy(macKeyCheckValue, 0, key4, 17, 8);
//         //        My03loadEncryptMainKey(keyId, key4, mEKeyCheckValue);
//         //
//         //        byte[] checkValue1 = new byte[8];
//         //        My03loadEncryptMainKey(keyId, key4, checkValue1);
//         //
//         //        byte[] checkValue2 = new byte[4];
//         //        My03loadEncryptMainKey(keyId, key4, checkValue2);
//     }
// */
//    public void L03003() {
//        loadTEK();
//        logUtils.printCaseInfo("L03003");
//        logUtils.addCaseLog("Execute the L03003 case");
//        Log.i(TAG, "L03003() executed");
//        String keyStr = "FCAD332CE832FC41C22B74F0FC49271B31313131313131";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        int keyId = 0;
//        boolean retValue = My03loadEncryptMainKey(keyId, key, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03003 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03003 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03003 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03003 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03004() {
//        loadTEK();
//        logUtils.printCaseInfo("L03004");
//        logUtils.addCaseLog("Execute the L03004 case");
//        Log.i(TAG, "L03003() executed");
//        String keyStr = "FCAD332CE832FC41C22B74F0FC49271B313131313131313131";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        int keyId = 0;
//        boolean retValue = My03loadEncryptMainKey(keyId, key, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03004 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03005() {
//        loadTEK();
//        logUtils.printCaseInfo("L03005");
//        logUtils.addCaseLog("Execute the L03005 case");
//        Log.i(TAG, "L03005() executed");
//        //测试无效keyId
//        int keyId = 100;
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03005 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03005 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03005 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03005 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03006() {
//        loadTEK();
//        logUtils.printCaseInfo("L03006");
//        logUtils.addCaseLog("Execute the L03006 case");
//        Log.i(TAG, "L03006() executed");
//        //测试无效keyId
//        int keyId = -1;
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03006 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03006 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03006 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03006 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03008() {
//        logUtils.printCaseInfo("L03008");
//        logUtils.addCaseLog("Execute the L03008 case");
//        Log.i(TAG, "L03008() executed");
//        //格式化密钥区
//        //FormatKeyArea();
//        int keyId = 0;
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03010() {
//        loadTEK();
//        logUtils.printCaseInfo("L03010");
//        logUtils.addCaseLog("Execute the L03010 case");
//        Log.i(TAG, "L03010() executed");
//        int keyId = 0;
//        byte[] key = null;
//        boolean retValue = My03loadEncryptMainKey(keyId, key, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03011() {
//        loadTEK();
//        logUtils.printCaseInfo("L03011");
//        logUtils.addCaseLog("Execute the L03011 case");
//        Log.i(TAG, "L03011() executed");
//        int keyId = 0;
//        byte[] key = {};
//        boolean retValue = My03loadEncryptMainKey(keyId, key, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03013() {
//        //FormatKeyArea();
//        loadTEK();
//        logUtils.printCaseInfo("L03013");
//        logUtils.addCaseLog("Execute the L03013 case");
//        Log.i(TAG, "L03013() executed");
//        int keyId = 10;
//        String mEKeyString = "ACAD332CE832FC41C22B74F0FC49271B3131313131313131";
//        byte[] mEKey = BCDDecode.str2Bcd(mEKeyString);
//        byte[] checkValue = {};
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03013 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L03013 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L03013 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L03013 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L03015() {
//        loadTEK();
//        logUtils.printCaseInfo("L03015");
//        logUtils.addCaseLog("Execute the L03015 case");
//        Log.i(TAG, "L03015() executed");
//        int keyId = 0;
//        byte[] checkValue = new byte[8];
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03015 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03015 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03015 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03015 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L03024() {
//        loadTEK();
//        logUtils.printCaseInfo("L03015");
//        logUtils.addCaseLog("Execute the L03024 case");
//        Log.i(TAG, "L03024() executed");
//        int keyId = 0;
//        String mEKeyString = "F10D332CE832FC41C22B74F0FC49271B3131313131313131";
//        String mEKeyCheckValueString = "6CB472EE95BD1E58";
//        byte[] mEKey = BCDDecode.str2Bcd(mEKeyString);
//        byte[] mEKeyCheckValue = BCDDecode.str2Bcd(mEKeyCheckValueString);
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03024 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L03024 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L03024 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L03024 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L03025() {
//        loadTEK();
//        logUtils.printCaseInfo("L03015");
//        logUtils.addCaseLog("Execute the L03025 case");
//        Log.i(TAG, "L03015() executed");
//        int keyId = 0;
//        String mEKeyCheckValueStr = "964A50B457B998";
//        byte[] mEKeyCheckValue = BCDDecode.str2Bcd(mEKeyCheckValueStr);
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03025 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L03025 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L03025 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L03025 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L03026() {
//        loadTEK();
//        logUtils.printCaseInfo("L03026");
//        logUtils.addCaseLog("Execute the L03026 case");
//        Log.i(TAG, "L03015() executed");
//        int keyId = 99;
//        String mEKeyCheckValueStr = "964A50B457B998E796";
//        byte[] mEKeyCheckValue = BCDDecode.str2Bcd(mEKeyCheckValueStr);
//        boolean retValue = My03loadEncryptMainKey(keyId, mEKey, mEKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L03026 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L03026 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L03026 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L03026 Case results：" + retValue + "，execute success！");
//    }
//
//    //    public void loadPlainMainKey() {
////        logUtils.printCaseInfo("loadPlainMainKey");
////        logUtils.addCaseLog("Execute the loadPlainMainKey case");
////        Log.i(TAG, "loadPlainMainKey() executed");
////        int keyId = 0;
////        String keyString = "111111111111111122222222222222223333333333333333";
////        byte[] key = BCDDecode.str2Bcd(keyString);
////        byte[] checkValue = {(byte) 0x40, (byte) 0x82, (byte) 0x6A, (byte) 0x58, (byte) 0x00, (byte) 0x60, (byte) 0x8C, (byte) 0x87};
////        String checkValueStr;
////
////        String dataStr = "0000000000000000";
////        byte[] data = BCDDecode.str2Bcd(dataStr);
////
////        String initVecStr = "0000000000000000";
////        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
////
////        key = My35getRandom((byte) 16);
////        checkValueStr = StringUtil.byte2HexStr(My27calculateDataEx(0, 1, key, data, initVec));
////        checkValue = BCDDecode.str2Bcd(checkValueStr);
////
////        boolean retValue = My04loadPlainMasterKey(keyId, key, checkValue);
////        if (retValue) {
////            logUtils.addCaseLog("loadPlainMainKey Case results：" + retValue + "，execute success！");
////            this.printMsgTool("loadPlainMainKey Case results：" + retValue + "，execute success！");
////            return;
////        }
////        logUtils.addCaseLog("loadPlainMainKey Case results：" + retValue + "，execute Failed！");
////        this.printMsgTool("loadPlainMainKey Case results：" + retValue + "，execute Failed！");
////    }
    public void loadPlainMainKey() {
        logUtils.printCaseInfo("loadPlainMainKey");
        logUtils.addCaseLog("Execute the loadPlainMainKey case");
        Log.i(TAG, "loadPlainMainKey() executed");
        int keyId = 0;
        boolean retValue = loadPlainMainKey(keyId, mKey,0x02, mKeyCheckValue);
        if (retValue) {
            logUtils.addCaseLog("loadPlainMainKey Case results：" + retValue + "，execute success！");
            this.printMsgTool("loadPlainMainKey Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("loadPlainMainKey Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("loadPlainMainKey Case results：" + retValue + "，execute Failed！");
    }
//
//    public void L04002() {
//        logUtils.printCaseInfo("L04002");
//        logUtils.addCaseLog("Execute the L04002 case");
//        Log.i(TAG, "L04002() executed");
//        int keyId = 99;
//        String KeyString = "343434343434343435353535353535353434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        byte[] checkValue = null;
//        boolean retValue = My04loadPlainMasterKey(keyId, Key, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L04002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L04002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L04002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L04005() {
//        logUtils.printCaseInfo("L04005");
//        logUtils.addCaseLog("Execute the L04005 case");
//        Log.i(TAG, "L04005() executed");
//        //测试无效keyId
//        int keyId = 100;
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, mKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L02019 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L02019 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L02019 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L02019 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L04006() {
//        logUtils.printCaseInfo("L04006");
//        logUtils.addCaseLog("Execute the L04006 case");
//        Log.i(TAG, "L04006() executed");
//        //测试无效keyId
//        int keyId = -1;
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, mKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04006 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04006 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04006 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L04006 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L04008() {
//        logUtils.printCaseInfo("L04008");
//        logUtils.addCaseLog("Execute the L04008 case");
//        Log.i(TAG, "L04008() executed");
//        int keyId = 0;
//        byte[] key = {};
//        boolean retValue = My04loadPlainMasterKey(keyId, key, mKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L04008 Case results：" + retValue + "，execute success！");
//    }
//
///*
//    public void L04009() {
//        logUtils.printCaseInfo("L04009");
//        logUtils.addCaseLog("执行L04009案例");
//        Log.i(TAG, "L04009() executed");
//        int keyId = 0;
//        byte[] key = null;
//        My04loadPlainMasterKey(keyId, key, mKeyCheckValue);
//    }
//*/
//
//    public void L04011() {
//        logUtils.printCaseInfo("L04011");
//        logUtils.addCaseLog("Execute the L04011 case");
//        Log.i(TAG, "L04011() executed");
//        int keyId = 0;
////        byte[] checkValue = {(byte) 0xD2, (byte) 0xB9, (byte) 0x1C};
//        byte[] checkValue = {(byte) 0xD2};
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04011 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L04011 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L04011 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L04011 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L04012() {
//        logUtils.printCaseInfo("L04012");
//        logUtils.addCaseLog("Execute the L04012 case");
//        Log.i(TAG, "L04012() executed");
//        int keyId = 0;
//        byte[] checkValue = {};
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04012 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L04012 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L04012 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L04012 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L04013() {
//        logUtils.printCaseInfo("L04013");
//        logUtils.addCaseLog("Execute the L04013 case");
//        Log.i(TAG, "L04013() executed");
//        //23字节主密钥
//        int keyId = 0;
//        byte[] key = {(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11};
//        boolean retValue = My04loadPlainMasterKey(keyId, key, mKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L04013 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L04014() {
//        logUtils.printCaseInfo("L04014");
//        logUtils.addCaseLog("Execute the L04014 case");
//        Log.i(TAG, "L04014() executed");
//        int keyId = 0;
//        byte[] checkValue = {(byte) 0xD2, (byte) 0xB9, (byte) 0x1C, (byte) 0xC5, (byte) 0xA7, (byte) 0x58, (byte) 0xBB, (byte) 0x08, (byte) 0xD2};
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L04014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L04015() {
//        logUtils.printCaseInfo("L04015");
//        logUtils.addCaseLog("Execute the L04015 case");
//        Log.i(TAG, "L04020() executed");
//        int keyId = 99;
//        String KeyString = "343434343434343435353535353535353434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String KCVString = "D2DB51F1D2013A63";
//        byte[] checkvalue = BCDDecode.str2Bcd(KCVString);
//        boolean retValue = My04loadPlainMasterKey(keyId, Key, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L04015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L04015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L04015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L04015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L04016() {
//        logUtils.printCaseInfo("L04016");
//        logUtils.addCaseLog("Execute the L04013 case");
//        Log.i(TAG, "L04013() executed");
//        //25字节主密钥
//        int keyId = 0;
//        byte[] key = {(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11};
//        boolean retValue = My04loadPlainMasterKey(keyId, key, mKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04016 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("LL04016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L04017() {
//        logUtils.printCaseInfo("L04017");
//        logUtils.addCaseLog("Execute the L04017 case");
//        Log.i(TAG, "L04020() executed");
//        int keyId = 99;
//        byte[] checkValue = {(byte) 0x11, (byte) 0xB9, (byte) 0x1C, (byte) 0xC5, (byte) 0xA7, (byte) 0x58, (byte) 0xBB, (byte) 0x08};
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04017 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04017 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04017 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L04017 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L04018() {
//        for (int i = 0; i < 10; i++) {
//            logUtils.addCaseLog("Execute the L04018 case");
//            Log.i(TAG, "L04018() executed");
//
//            byte[] mKeyCheckValue = BCDDecode.str2Bcd("F90BD412462B766E");
//            String mKeyString = "77777777777777778888888888888888";
//            byte[] mKey = BCDDecode.str2Bcd(mKeyString);
//
//            boolean retValue = My04loadPlainMasterKey(10, mKey, mKeyCheckValue);
//            if (retValue) {
//                logUtils.addCaseLog("L04018 Case results：第" + i + "次," + retValue + "，execute success！");
//                this.printMsgTool("L04018 Case results：第" + i + "次," + retValue + "，execute success！");
//
//            } else {
//                logUtils.addCaseLog("L04018 Case results：第" + i + "次," + retValue + "，execute Failed！");
//                this.printMsgTool("L04018 Case results：第" + i + "次," + retValue + "，execute Failed！");
//            }
//        }
//    }
//
    public void loadPlainMasterKey() {
        logUtils.printCaseInfo("loadPlainMainKey");
        logUtils.addCaseLog("Execute the loadPlainMainKey case");
        Log.i(TAG, "L04020() executed");
        int keyId = 99;
        String mKeystr = "11111111111111113333333333333333";
        byte[] mKey = BCDDecode.str2Bcd(mKeystr);
        String KCVstr = "92A3295FF6050FC8";
        byte[] CheckValue = BCDDecode.str2Bcd(KCVstr);
        boolean retValue = loadPlainMasterKey(keyId, mKey,0x02, CheckValue);
        if (retValue) {
            logUtils.addCaseLog("loadPlainMainKey Case results：" + retValue + "，execute success！");
            this.printMsgTool("loadPlainMainKey Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("loadPlainMainKey Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("loadPlainMasterKey Case results：" + retValue + "，execute Failed！");
    }
//
//    public void L04020() {
//        logUtils.printCaseInfo("L04020");
//        logUtils.addCaseLog("Execute the L04020 case");
//        Log.i(TAG, "L04020() executed");
//        int keyId = 99;
//        String mKeystr = "111111111111111122222222222222";
//        byte[] mKey = BCDDecode.str2Bcd(mKeystr);
//        boolean retValue = My04loadPlainMasterKey(keyId, mKey, mKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04020 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L04020 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L04020 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L04020 Case results：" + retValue + "，execute success！");
//    }
//
//    // 下载keyId=1的主密钥
//    public void L04021() {
//        logUtils.printCaseInfo("L04021");
//        logUtils.addCaseLog("Execute the L04021 case");
//        Log.i(TAG, "L04021() executed");
//        int keyId = 1;
//        String Keystr = "31313131313131313131313131313131";
//        byte[] Key = BCDDecode.str2Bcd(Keystr);
//        byte[] checkValue = {(byte) 0x40, (byte) 0x82, (byte) 0x6A, (byte) 0x58, (byte) 0x00, (byte) 0x60, (byte) 0x8C, (byte) 0x87};
//        String checkValueStr;
//        String dataStr = "0000000000000000";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//
//        String initVecStr = "0000000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        Key = My35getRandom((byte) 16);
//        checkValueStr = StringUtil.byte2HexStr(My27calculateDataEx(0, 1, Key, data, initVec));
//        checkValue = BCDDecode.str2Bcd(checkValueStr);
//        boolean retValue = My04loadPlainMasterKey(keyId, Key, checkValue);
//        if (retValue) {
//            logUtils.addCaseLog("L04021 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L04021 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L04021 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L04021 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L05001() {
//        logUtils.printCaseInfo("L05001");
//        logUtils.addCaseLog("Execute the L05001 case");
//        Log.i(TAG, "L05001() executed");
//        //无效的密钥类型
//        int keyType = 4;
//        int mkId = 0;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05001 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05001 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05001 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05001 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05002() {
//        logUtils.printCaseInfo("L05002");
//        logUtils.addCaseLog("Execute the L05002 case");
//        Log.i(TAG, "L05002() executed");
//        //测试密钥数据为空
//        int keyType = PinpadKeyType.MACKEY;
//        int mkId = 0;
//        int wkId = 99;
//        byte[] key = null;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, key, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05002 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05002 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05002 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05002 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05003() {
//        logUtils.printCaseInfo("L05003");
//        logUtils.addCaseLog("Execute the L05003 case");
//        Log.i(TAG, "L05003() executed");
//        //测试密钥数据为空
//        int keyType = PinpadKeyType.MACKEY;
//        int mkId = 0;
//        int wkId = 99;
//        byte[] key = {};
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, key, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05003 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05003 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05003 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05003 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05004() {
//        logUtils.printCaseInfo("L05004");
//        logUtils.addCaseLog("Execute the L05004 case");
//        Log.i(TAG, "L05004() executed");
//        //无效的mkId
//        int keyType = PinpadKeyType.MACKEY;
//        int mkId = 100;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05004 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05005() {
//        logUtils.printCaseInfo("L05005");
//        logUtils.addCaseLog("Execute the L05005 case");
//        Log.i(TAG, "L05005() executed");
//        //无效的wkId
//        int keyType = PinpadKeyType.MACKEY;
//        int mkId = 0;
//        int wkId = 100;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05005 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05005 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05005 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05005 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05053() {
//        logUtils.addCaseLog("Execute the L05053 case");
//        Log.i(TAG, "L05053() executed");
//
//        loadPlainMainKey();
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        Log.i(TAG, "macKey=" + macKeyString);
//        Log.i(TAG, "macKeyCheckValue=" + macKeyCheckValueString);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05053 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05053 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05053 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05053 Case results：" + retValue + "，execute Failed！");
//    }
//
    public void loadWorkKey() {
        logUtils.addCaseLog("Execute the loadWorkKey case");
        Log.i(TAG, "loadWorkKey() executed");
        loadPlainMainKey();
        int keyType = 2;
        int mkId = 0;
        int wkId = 99;
        Log.i(TAG, "pinKey=" + pinKeyString);
        Log.i(TAG, "pinKeyCheckValue=" + pinKeyCheckValueString);
        boolean retValue = loadWorkKey(keyType, mkId, wkId,0x00, pinKey, null);
        if (retValue) {
            logUtils.addCaseLog("loadWorkKey Case results：" + retValue + "，execute success！");
            this.printMsgTool("loadWorkKey Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("loadWorkKey Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("loadWorkKey Case results：" + retValue + "，execute Failed！");
    }
//
//    public void L05055() {
//        logUtils.addCaseLog("Execute the L05055 case");
//        Log.i(TAG, "L05055() executed");
//        loadPlainMainKey();
//
//        int keyType = 3;
//        int mkId = 0;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, tdKey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L05055 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05055 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05055 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05055 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L05056() {
//        logUtils.addCaseLog("Execute the L05056 case");
//        Log.i(TAG, "L05056() executed");
//
//        int keyType = 1;
//        int mkId = 6;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05056 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05056 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05056 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05056 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05057() {
//        logUtils.addCaseLog("Execute the L05057 case");
//        Log.i(TAG, "L05057() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        //密钥25字节
//        String macKeyStr = "b740ce7c2fb7fcd8808b5f288eecc6b4b740ce7c2fb7fcd811";
//        byte[] key = BCDDecode.str2Bcd(macKeyStr);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, key, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05057 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05057 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05057 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05057 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05058() {
//        logUtils.addCaseLog("Execute the L05058 case");
//        Log.i(TAG, "L05058() executed");
//
//        int keyType = 0;
//        int mkId = 0;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05058 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05058 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05058 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05058 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05059() {
//        logUtils.addCaseLog("Execute the L05059 case");
//        Log.i(TAG, "L05059() executed");
//
//        int keyType = 1;
//        int mkId = -1;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05059 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05059 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05059 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05059 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05060() {
//        loadPlainMainKey();
//        logUtils.addCaseLog("Execute the L05060 case");
//        Log.i(TAG, "L05060() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = -1;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05060 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05060 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05060 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05060 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05061() {
//        loadPlainMainKey();
//        logUtils.addCaseLog("Execute the L05061 case");
//        Log.i(TAG, "L05061() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        byte[] checkvalue = null;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L05061 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05061 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05061 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05061 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L05062() {
//        loadPlainMainKey();
//        logUtils.addCaseLog("Execute the L05062 case");
//        Log.i(TAG, "L05062() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        byte[] checkvalue = {};
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L05062 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05062 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05062 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05062 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L05063() {
//        logUtils.addCaseLog("Execute the L05063 case");
//        Log.i(TAG, "L05063() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        String macKeyCheckValueStr = "F0467E2BC67CDC";
//        byte[] checkvalue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L05063 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05063 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05063 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05063 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05064() {
//        logUtils.addCaseLog("Execute the L05064 case");
//        Log.i(TAG, "L05064() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        String macKeyCheckValueStr = "F0467E2BC67CDC70F0";
//        byte[] checkvalue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L05064 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05064 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05064 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05064 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05065() {
//        logUtils.addCaseLog("Execute the L05065 case");
//        Log.i(TAG, "L05065() executed");
//
//        // FormatKeyArea();
//
////        SdkApiHolder.getInstance().getKeyManager().formatKeyArea();
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05065 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05065 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05065 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05065 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05066() {
//        logUtils.addCaseLog("Execute the L05066 case");
//        Log.i(TAG, "L05066() executed");
//
//        int keyType = 1;
//        int mkId = 0;
//        int wkId = 99;
//        String macKeyCheckValueStr = "1111111111111111";
//        byte[] checkvalue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L05066 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L05066 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L05066 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L05066 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L05067() {
//        logUtils.addCaseLog("Execute the L05067 case");
//        Log.i(TAG, "L05067() executed");
//        loadPlainMasterKey();
//
//        int keyType = 1;
//        int mkId = 99;
//        int wkId = 10;
//        String macKeyStr = "b740ce7c2fb7fcd8808b5f288eecc6b4";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        String KCVStr = "28D578C39FD5F802";
//        byte[] CheckValue = BCDDecode.str2Bcd(KCVStr);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, macKey, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05067 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05067 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05067 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05067 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void loadWorkKey() {
//        logUtils.addCaseLog("Execute the loadWorkKey case");
//        Log.i(TAG, "loadWorkKey() executed");
//
//        loadPlainMasterKey();
//
//        int keyType = 2;
//        int mkId = 99;
//        int wkId = 10;
//        String pinKeyStr = "B0BCE9315C0AA31E5E6667A037DE0AC4";
//        byte[] pinKey = BCDDecode.str2Bcd(pinKeyStr);
//        String KCVStr = "6EE8CDC05B3E4523";
//        byte[] CheckValue = BCDDecode.str2Bcd(KCVStr);
//        boolean retValue = loadWorkKey(keyType, mkId, wkId, 0x00,pinKey, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("loadWorkKey Case results：" + retValue + "，execute success！");
//            this.printMsgTool("loadWorkKey Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("loadWorkKey Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("loadWorkKey Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L05069() {
//        logUtils.addCaseLog("Execute the L05069 case");
//        Log.i(TAG, "L05069() executed");
//
//        loadPlainMasterKey();
//
//        int keyType = 3;
//        int mkId = 99;
//        int wkId = 0;
//        String tdKeyStr = "42527dea2a1553e1211b0ea0d4590766";
//        byte[] tdKey = BCDDecode.str2Bcd(tdKeyStr);
//        String KCVStr = "BEA12E005922E905";
//        byte[] CheckValue = BCDDecode.str2Bcd(KCVStr);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, tdKey, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L05069 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05069 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05069 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05069 Case results：" + retValue + "，execute Failed！");
//    }
//
//    // 下pinkey = 1
//    public void L05070() {
//        logUtils.addCaseLog("Execute the loadWorkKey case");
//        Log.i(TAG, "loadWorkKey() executed");
//
//        L04021();
//        int keyType = 2;
//        int mkId = 1;
//        int wkId = 1;
//        Log.i(TAG, "pinKey=" + pinKeyString);
//        Log.i(TAG, "pinKeyCheckValue=" + pinKeyCheckValueString);
//        boolean retValue = My05loadWorkKey(keyType, mkId, wkId, pinKey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L05070 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L05070 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L05070 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L05070 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void calcmac_demo() {
//
//        String mKeystr = "11111111111111112222222222222222";
//        byte[] mKey = BCDDecode.str2Bcd(mKeystr);
//        String KCVstr = "D2B91CC5A758BB08";
//        byte[] CheckValue = BCDDecode.str2Bcd(KCVstr);
//        My04loadPlainMasterKey(0, mKey, CheckValue);
//
//        String macKeyStr = "b740ce7c2fb7fcd8808b5f288eecc6b4";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        String KCVStr = "F0467E2BC67CDC70";
//        byte[] CheckValue2 = BCDDecode.str2Bcd(KCVStr);
//        My05loadWorkKey(1, 0, 99, macKey, CheckValue2);
//    }
//
//    public void L06001() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06001");
//        logUtils.addCaseLog("Execute the L06001 case");
//        Log.i(TAG, "L06001() executed");
//        String dataString = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = 99;
//        String retValue = StringUtil.byte2HexStr(My06calcMAC(wkId, data));
//        if (retValue.equals("DC7D44FACCE4A53D")) {
//            logUtils.addCaseLog("L06001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L06001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L06001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L06001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L06003() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06003");
//        logUtils.addCaseLog("Execute the L06003 case");
//        Log.i(TAG, "L06002() executed");
//        String dataString = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = 99;
//        String retValue = StringUtil.byte2HexStr(My06calcMAC(wkId, data));
//        if (retValue.equals("5B22ABBB31F57F1D")) {
//            logUtils.addCaseLog("L06003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L06003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L06003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L06003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L06004() {
//        //FormatKeyArea();
//        Boolean ret = My32clearKey(99, 0x20);
//        logUtils.printCaseInfo("L06004");
//        logUtils.addCaseLog("Execute the L06004 case");
//        Log.i(TAG, "L06003() executed");
//        String dataString = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = 99;
//        String retValue = StringUtil.byte2HexStr(My06calcMAC(wkId, data));
//        if (retValue.equals(null)) {
//            logUtils.addCaseLog("L06004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L06004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L06004 Case results：" + retValue + "，execute Success！");
//        this.printMsgTool("L06004 Case results：" + retValue + "，execute Success！");
//    }
//
//    public void L06005() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06005");
//        logUtils.addCaseLog("Execute the L06005 case");
//        Log.i(TAG, "L06005() executed");
//        //测试无效的wkId
//        String dataString = "123456789ABCDEF00123456789ABCDEF";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = 100;
//        String retValue = StringUtil.byte2HexStr(My06calcMAC(wkId, data));
//        if (retValue.equals(null)) {
//            logUtils.addCaseLog("L06005 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L06005 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L06005 Case results：" + retValue + "，execute Success！");
//        this.printMsgTool("L06005 Case results：" + retValue + "，execute Success！");
//    }
//
//    public void L06006() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06006");
//        logUtils.addCaseLog("Execute the L06006 case");
//        Log.i(TAG, "L06006() executed");
//        //测试无效的wkId
//        String dataString = "123456789ABCDEF00123456789ABCDEF";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = -1;
//        My06calcMAC(wkId, data);
//    }
//
//    public void L06008() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06008");
//        logUtils.addCaseLog("Execute the L06008 case");
//        Log.i(TAG, "L06008() executed");
//        int wkId = 99;
//        byte[] data = new byte[1024];
//        Arrays.fill(data, (byte) 0x31);
//        My06calcMAC(wkId, data);
//    }
//
//    public void L06009() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06009");
//        logUtils.addCaseLog("Execute the L06009 case");
//        Log.i(TAG, "L06009() executed");
//        int wkId = 99;
//        byte[] data = new byte[2048];
//        Arrays.fill(data, (byte) 0x31);
//        My06calcMAC(wkId, data);
//    }
//
//    public void L06010() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06010");
//        logUtils.addCaseLog("Execute the L06010 case");
//        Log.i(TAG, "L06010() executed");
//        int wkId = 99;
//        byte[] data = {};
//        My06calcMAC(wkId, data);
//    }
//
//    public void L06011() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06011");
//        logUtils.addCaseLog("Execute the L06011 case");
//        Log.i(TAG, "L06011() executed");
//        int wkId = 99;
//        byte[] data = null;
//        My06calcMAC(wkId, data);
//    }
//
//    public void L06013() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06013");
//        logUtils.addCaseLog("Execute the L06013 case");
//        Log.i(TAG, "L06013() executed");
//        int wkId = 99;
//        String dataString = "111111111111111122222222";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        String retValue = StringUtil.byte2HexStr(My06calcMAC(wkId, data));
//        if (retValue.equals("6317BDF640D987B0")) {
//            logUtils.addCaseLog("L06013 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L06013 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L06013 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L06013 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L06014() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06014");
//        logUtils.addCaseLog("Execute the L06014 case");
//        Log.i(TAG, "L06014() executed");
//        int wkId = 99;
//        byte[] data = new byte[192000];
//        Arrays.fill(data, (byte) 0x31);
//        My06calcMAC(wkId, data);
//
//    }
//
//    public void L06015() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06001");
//        logUtils.addCaseLog("Execute the L06015 case");
//        Log.i(TAG, "L06001() executed");
//        String dataString = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = 9;
//        My06calcMAC(wkId, data);
//    }
//
//    public void L06016() {
//        calcmac_demo();
//        logUtils.printCaseInfo("L06001");
//        logUtils.addCaseLog("Execute the L06015 case");
//        Log.i(TAG, "L06001() executed");
//        String dataString = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(dataString);
//        int wkId = 9;
//        My06calcMAC(wkId, data);
//    }
//
//    public void L07001() {
//        logUtils.printCaseInfo("L07001");
//        logUtils.addCaseLog("Execute the L07001 case");
//        Log.i(TAG, "L07001() executed");
//        L05055();
//        int mode = 0;   //ECB
//        int wkId = 99;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07002() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("1557379CDB3C59F816ACE83FD0AB0DB02E65CA016EB01BC55438430E267328140C3F2F15A8A1308B2E9A5EEAB1AF05DF")) {
//            logUtils.addCaseLog("L07002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L07002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L07002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L07002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L07003() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        String trackString = "3131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("33313331333133313331333133313331333133313331333133313331333133313331333133313331333133313331")) {
//            logUtils.addCaseLog("L07003 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L07003 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L07003 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L07003 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L07004() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        String trackString = "31313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("3331333133313331333133313331333133313331333133313331333133313331333133313331333133313331333133313331")) {
//            logUtils.addCaseLog("L07004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L07004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L07004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L07004 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L07005() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        My07encryptTrackData(mode, wkId, null);
//    }
//
//    public void L07006() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        String trackString = "";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07007() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        String trackString = "%%%%%%%^^^^^^^!!!!!!!!!!!!!!!~~~";
//        byte[] track = trackString.getBytes();
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("3AEB4559582634E3CC2FE678F1825E55D2A0F6A7F22C36BE3B46E5BBC583EA8B")) {
//            logUtils.addCaseLog("L07007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L07007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L07007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L07007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L07008() {
//        L05055();
//        int mode = 0;
//        int wkId = 98;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07009() {
//        L05069();
//        int mode = 0;
//        int wkId = 0;
//        String trackString = "31313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("C39E1CECD12E7A0CC39E1CECD12E7A0CC39E1CECD12E7A0CC39E1CECD12E7A0C")) {
//            logUtils.addCaseLog("L07009 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L07009 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L07009 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L07009 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L07010() {
//        L05069();
//        int mode = 1;
//        int wkId = 0;
//        String trackString = "31313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("C39E1CECD12E7A0CF0E79A05A9CE152928B8FC1A060A07703B0228C0138C428B")) {
//            logUtils.addCaseLog("L07010 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L07010 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L07010 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L07010 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L07011() {
//        L05055();
//        int mode = -1;
//        int wkId = 99;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07012() {
//        L05055();
//        int mode = 2;
//        int wkId = 99;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07013() {
//        L05055();
//        int mode = 1;
//        int wkId = -1;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07014() {
//        L05055();
//        int mode = 1;
//        int wkId = 100;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = trackString.getBytes();
//        My07encryptTrackData(mode, wkId, track);
//    }
//
//    public void L07015() {
//        L05055();
//        int mode = 0;
//        int wkId = 99;
//        byte[] track = new byte[2048];
//        Arrays.fill(track, (byte) 0x31);
//        My07encryptTrackData(mode, wkId, track);
//        String retValue = StringUtil.byte2HexStr(My07encryptTrackData(mode, wkId, track));
//
//        if (retValue.equals("9D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEEACD99D9104459EEACD9")) {
//            logUtils.addCaseLog("L07015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L07015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L07015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L07015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L07016() {
//        L05055();
//        int mode = 1;
//        int wkId = 99;
//        byte[] track = new byte[2049];
//        Arrays.fill(track, (byte) 0x31);
//        My07encryptTrackData(mode, wkId, track);
//    }
//
    public void L01001() {
        //下载PIN KEY
        loadWorkKey();
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

    private class mylistener extends RFSearchListener.Stub {
        @Override
        public void onCardPass(int cardType) throws RemoteException {
            Message message = new Message();
            if (cardType == 0x00)
                message.getData().putString("message", "Result: M1卡(S50)");
            if (cardType == 0x01)
                message.getData().putString("message", "Result: M1卡(S70)");
            if (cardType == 0x02)
                message.getData().putString("message", "Result: PRO卡");
            if (cardType == 0x03)
                message.getData().putString("message", "Result: S50_PRO卡");
            if (cardType == 0x04)
                message.getData().putString("message", "Result: S70_PRO卡");
            if (cardType == 0x05)
                message.getData().putString("message", "Result: CPU卡");
            if (cardType == 0x07)
                message.getData().putString("message", "Result: Mifare UltraLight card");
            if (cardType == 0x08)
                message.getData().putString("message", "Result: Mifare Desfire card");
            if (cardType == 0x0B)
                message.getData().putString("message", "Result: UltraLight card");
            if (cardType == 0x0C)
                message.getData().putString("message", "Result: Mifare plus card");
            if (cardType == 0x0D)
                message.getData().putString("message", "Result: Mifare UltraLight card EV1");
            if (cardType == 0x0E)
                message.getData().putString("message", "Result: Mifare UltraLight card Nano");
            handler.sendMessage(message);
        }

        @Override
        public void onFail(int error, String message) throws RemoteException {
            Message msg = new Message();
            if (error == 0xA2)
                msg.getData().putString("message", "error code = " + error + "\nCommunication error");
            if (error == 0xA3)
                msg.getData().putString("message", "error code = " + error + "\nThe data returned by the card does not conform to the specification");
            if (error == 0x84)
                msg.getData().putString("message", "error code = " + error + "\nMultiple cards exist in the induction zone");
            if (error == 0xA7)
                msg.getData().putString("message", "error code = " + error + "\nTimeout no response");
            if (error == 0xB3)
                msg.getData().putString("message", "error code = " + error + "\nPro card or TypeB card is not active");
            if (error == 0xff01)
                msg.getData().putString("message", "error code = " + error + "\nThe master service is abnormal");
            if (error == 0xff02)
                msg.getData().putString("message", "error code = " + error + "\nAbnormal request");
            handler.sendMessage(msg);
        }
    }


    public void L01002() {
        //格式化密钥区
        //FormatKeyArea();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 20);
        param.putBoolean("isOnline", false);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        param.putBoolean("notificatePinLenError", true);

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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 100;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 10);
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
        //下载PIN KEY
        loadWorkKey();
        int keyId = -1;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
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

    public void L01005() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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
        param.putBoolean("notificatePinLenError", true);


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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 4};
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

    public void L01007() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "");
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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {};
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

    public void L01010() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
        param.putByteArray("pinLimit", null);
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

  /*  public void L01011() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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
    }*/

    public void L01012() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
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

    public void L01013() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
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

    public void L01014() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
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

    public void L01015() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6, 7, 9};
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

    public void L01016() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 30);
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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 50);
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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

    public void L01019() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 90);
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
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 6};
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

    public void L01022() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 6};
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

    public void L01024() {
        //下载PIN KEY
//        L05010();
        logUtils.printCaseInfo("L01024");
        logUtils.addCaseLog("执行L01024案例");
        Log.i(TAG, "L01024() executed");
        int keyId = 0;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        String panBlock = "622700120375078";
        byte[] pinLimit = {0, 6};
        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, false);
        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPONE, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTWO, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTHR, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFOU, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFIV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSIX, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSEV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPEIG, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPNIN, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPZER, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPCON, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPBAC, null);
        try {
            iPinpad.startPinInput(keyId, param, globalparam, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void L01025() {
        L12023();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 2);//AES
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

    public void L01026() {
        L12023();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352123");
        param.putInt("desType", 2);//AES
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
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {12};
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

    public void L01035() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", false);
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

    public void L01036() {
        loadWorkKey();
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

    public void L01037() {
        loadWorkKey();
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

    public void L01038() {
        loadWorkKey();
        int keyId = 99;
        Bundle globalparam = new Bundle();
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPONE, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTWO, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTHR, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFOU, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFIV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSIX, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSEV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPEIG, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPNIN, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPZER, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPCON, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPBAC, null);
        My01startPinInput(keyId, null, globalparam);
    }

    public void L01039() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {9};
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

    public void L01040() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {10};
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

    public void L01041() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {11};
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

    public void L01042() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {-1};
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

    public void L01043() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {13};
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
        loadWorkKey();
        int keyId = 9;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

    public void L01045() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", false);
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
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 4};
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

    public void L01047() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
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

    public void L01048() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {4};
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

    public void L01049() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
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

    public void L01050() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
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

    public void L01051() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
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

    public void L01052() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
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

    public void L01053() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0};
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

    /* public void L01054() {
         loadWorkKey();
         int keyId = 99;
         Bundle param = new Bundle();
         Bundle globalparam = new Bundle();
         byte[] pinLimit = {1, 2, 3, 5};
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
 */
    public void L01055() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", null);
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

    public void L01056() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "");
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

    public void L01057() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "键盘已启动，请先输入联机PIN，请继续输入脱机PIN！！！");
//        param.putString("promptString", "%%%@@@！！！%%%@@@！！！%%%@@@！！！%%%@@@！！！%%%@@@！！！");
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

    public void L01058() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01059() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01060() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01061() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        String panBlock = "62284800390";
        byte[] pinLimit = {6};
        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
        param.putString(BUNDLE_PINPARAM_PROMPTSTR, "please input pin:");
        param.putInt(BUNDLE_PINPARAM_DESTYPE, 1);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPONE, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTWO, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTHR, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFOU, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFIV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSIX, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSEV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPEIG, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPNIN, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPZER, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPCON, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPBAC, null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01062() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        String panBlock = "%@@@@!!!!!!~~~~~~~1234";
        byte[] pinLimit = {6};
        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
        param.putString(BUNDLE_PINPARAM_PROMPTSTR, "please input pin:");
        param.putInt(BUNDLE_PINPARAM_DESTYPE, 1);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPONE, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTWO, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTHR, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFOU, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFIV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSIX, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSEV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPEIG, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPNIN, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPZER, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPCON, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPBAC, null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01063() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 0);
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

    public void L01064() {
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 5);
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

    public void L01065() {
        loadWorkKey();
        int keyId = 10;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        String panBlock = "6226901508781352";
        byte[] pinLimit = {6};
        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
        param.putString(BUNDLE_PINPARAM_PROMPTSTR, "please input pin:");
        param.putInt(BUNDLE_PINPARAM_DESTYPE, 1);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPONE, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTWO, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTHR, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFOU, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFIV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSIX, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSEV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPEIG, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPNIN, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPZER, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPCON, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPBAC, null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01066() {
        loadWorkKey();
        int keyId = 10;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        String panBlock = "6228480039042651172";
        byte[] pinLimit = {6};
        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
        param.putString(BUNDLE_PINPARAM_PROMPTSTR, "please input pin:");
        param.putInt(BUNDLE_PINPARAM_DESTYPE, 1);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPONE, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTWO, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPTHR, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFOU, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPFIV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSIX, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPSEV, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPEIG, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPNIN, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPZER, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPCON, null);
        globalparam.putString(BUNDLE_GLOBALPARAM_DISPBAC, null);
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01067() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        String panBlock = "6226901508781352";
        byte[] pinLimit = {6};
        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 0);
        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
        param.putString(BUNDLE_PINPARAM_PROMPTSTR, "please input pin:");
        param.putInt(BUNDLE_PINPARAM_DESTYPE, 1);
        My01startPinInput(keyId, param, null);
    }

    public void L01068() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

        globalparam.putString("Display_One", "O");
        globalparam.putString("Display_Two", "T");
        globalparam.putString("Display_Three", "R");
        globalparam.putString("Display_Four", "F");
        globalparam.putString("Display_Five", "V");
        globalparam.putString("Display_Six", "X");
        globalparam.putString("Display_Seven", "S");
        globalparam.putString("Display_Eight", "E");
        globalparam.putString("Display_Nine", "N");
        globalparam.putString("Display_Zero", "Z");
        globalparam.putString("Display_Confirm", "C");
        globalparam.putString("Display_BackSpace", "B");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01069() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

        globalparam.putString("Display_One", "One");
        globalparam.putString("Display_Two", "Two");
        globalparam.putString("Display_Three", "Three");
        globalparam.putString("Display_Four", "Four");
        globalparam.putString("Display_Five", "Five");
        globalparam.putString("Display_Six", "Six");
        globalparam.putString("Display_Seven", "Seven");
        globalparam.putString("Display_Eight", "Eight");
        globalparam.putString("Display_Nine", "Nine");
        globalparam.putString("Display_Zero", "Zero");
        globalparam.putString("Display_Confirm", "confirm");
        globalparam.putString("Display_BackSpace", "backspace");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01070() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

        globalparam.putString("Display_One", "1");
        globalparam.putString("Display_Two", "2");
        globalparam.putString("Display_Three", "3");
        globalparam.putString("Display_Four", "4");
        globalparam.putString("Display_Five", "5");
        globalparam.putString("Display_Six", "6");
        globalparam.putString("Display_Seven", "7");
        globalparam.putString("Display_Eight", "8");
        globalparam.putString("Display_Nine", "9");
        globalparam.putString("Display_Zero", "0");
        globalparam.putString("Display_Confirm", "confirm");
        globalparam.putString("Display_BackSpace", "backspace");
        My01startPinInput(keyId, param, globalparam);
    }

    public void L01071() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

    public void L01072() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
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

    public void L01073() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01074() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "system/fonts/Roboto-Italic.ttf");
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

    public void L01075() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01076() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01077() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01078() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01079() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01080() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01081() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
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

    public void L01082() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
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

    public void L01083() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        byte[] displayKeyValue = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
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

    public void L01084() {
        //下载PIN KEY
        loadWorkKey();
        int keyId = 99;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        byte[] displayKeyValue = {9, 8, 7, 6, 0, 5, 4, 3, 2, 1};
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

    public void L01090() {
        //下载DUKPT KEY
        loadDukptKey();
        int keyId = 1;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 4);//DUKPT 3DES
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

    public void L01091() {
        //下载DUKPT KEY
        loadDukptKey();
        int keyId = 0;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 4);//DUKPT 3DES
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

    public void L01092() {
        //下载DUKPT KEY
        loadDukptKey();
        int keyId = 0;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352123");
        param.putInt("desType", 4);//DUKPT 3DES
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

    //pinkey = 1
   /* public void L01093() {
        //下载PIN KEY
        L05070();
        int keyId = 1;
        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 4);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putByteArray("displayKeyValue", null);
        param.putBoolean("notificatePinLenError", true);


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
    }*/


    public void L01094() {
        //下载PIN KEY
//        loadWorkKey();
        int keyId = 99;

        Bundle param = new Bundle();
        Bundle globalparam = new Bundle();
        byte[] pinLimit = {0,4 };
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 0);
        param.putBoolean("isOnline", true);
        param.putString("promptString", "please input your cardpin:");
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putString("numbersFont", "");
        param.putString("promptsFont", "");
        param.putString("otherFont", "");
        param.putBoolean("isEpp",true);
        param.putByteArray("displayKeyValue", null);
//        param.putInt("pinFormatType",1);


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
//
    public void L02001() {
        logUtils.printCaseInfo("L02001");
        logUtils.addCaseLog("执行L02001案例");
        My02stopPinInput();
    }


    public void L02002() {
        L01001();
        logUtils.printCaseInfo("L02002");
        logUtils.addCaseLog("执行L02002案例");
        Log.i(TAG, "L02002() executed");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        My02stopPinInput();
        logUtils.addCaseLog("stopPinInput测试正常");
    }
//
//    public void L10001() {
//        My10submitPinInput();
//    }
//
//    public void L10002() {
//        My10submitPinInput();
//    }
//
//    public void L11001() {
//        String lastErrorString = null;
//        try {
//            lastErrorString = iPinpad.getLastError();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        if (lastErrorString == null) {
//            logUtils.addCaseLog("LastError=null");
//        } else {
//            logUtils.addCaseLog("LastError=" + lastErrorString);
//        }
//    }
//
//    public void L12001() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12001 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12001 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12002() {
//        L02002();
//        int wkId = 30;
//        String pinKeyStr = "333333333333333322222222222222221111111111111111";
//        String pinKeyCheckValueStr = "77C56456F87B6F2E";
//        byte[] pinKey = BCDDecode.str2Bcd(pinKeyStr);
//        byte[] pinKeyCheckValue = BCDDecode.str2Bcd(pinKeyCheckValueStr);
//        boolean retValue = loadWorkKey(2, 0, wkId, 1, pinKey, pinKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12002 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12002 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12003() {
//        L02002();
//        int wkId = 66;
//        String tdKeyStr = "222222222222222244444444444444446666666666666666";
//        String tdKeyCheckValueStr = "548D76E158FF09EC";
//        byte[] tdKey = BCDDecode.str2Bcd(tdKeyStr);
//        byte[] tdKeyCheckValue = BCDDecode.str2Bcd(tdKeyCheckValueStr);
//        boolean retValue = loadWorkKey(3, 0, wkId, 1, tdKey, tdKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12003 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12003 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12004() {
//        loadTEK();
//        int wkId = 30;
//        String pinKeyStr = "22222222222222221111111111111111";
//        String pinKeyCheckValueStr = "02CD0E946DF44509";
//        byte[] pinKey = BCDDecode.str2Bcd(pinKeyStr);
//        byte[] pinKeyCheckValue = BCDDecode.str2Bcd(pinKeyCheckValueStr);
//        boolean retValue = loadWorkKey(2, 0, wkId, 1, pinKey, pinKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12004 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12004 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12005() {
//        L02002();
//        int wkId = 66;
//        String tdKeyStr = "222222222222222244444444444444446666666666666666";
//        String tdKeyCheckValueStr = "548D76E158FF09EC";
//        byte[] tdKey = BCDDecode.str2Bcd(tdKeyStr);
//        byte[] tdKeyCheckValue = BCDDecode.str2Bcd(tdKeyCheckValueStr);
//        boolean retValue = loadWorkKey(3, 98, wkId, 1, tdKey, tdKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12005 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12005 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12005 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12005 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12006() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "1111111111111111";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12006 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12006 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12006 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12006 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12007() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(0, 0, wkId, 1, macKey, macKeyCheckValue);
//
//        if (retValue) {
//            logUtils.addCaseLog("L12007 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12007 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12007 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12007 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12008() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(12, 0, wkId, 1, macKey, macKeyCheckValue);
//
//
//        if (retValue) {
//            logUtils.addCaseLog("L12008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12009() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, -1, wkId, 1, macKey, macKeyCheckValue);
//
//        if (retValue) {
//            logUtils.addCaseLog("L12009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12010() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 100, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12011() {
//        L02002();
//        int wkId = -1;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12012() {
//        L02002();
//        int wkId = 100;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12012 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12012 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12012 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12012 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12013() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, -1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12013 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12014() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "111111111111111122222222222222223333333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 99, wkId, 4, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12015() {
//        L02002();
//        int wkId = 3;
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, null, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12015 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12015 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12015 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12015 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12016() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12016 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12017() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "1111111111111111222222222222222233333333333333";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12017 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12017 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12017 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12017 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12018() {
//        L02002();
//        int wkId = 3;
//        String macKeyStr = "11111111111111112222222222222222333333333333333344";
//        String macKeyCheckValueStr = "9578168B512A27E6";
//        byte[] macKey = BCDDecode.str2Bcd(macKeyStr);
//        byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueStr);
//        boolean retValue = loadWorkKey(1, 0, wkId, 1, macKey, macKeyCheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12018 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12018 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12018 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12018 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12019() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333333";
//        String CheckValueStr = "FB4C8F44A6761464";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(1, 0, 0, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12019 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12019 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12019 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12019 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12020() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333334";
//        String CheckValueStr = "ECA6CF01CF986521";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(2, 0, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12020 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12020 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12020 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12020 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12021() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333335";
//        String CheckValueStr = "851E1127ED0B6850";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(3, 0, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12021 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12021 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12021 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12021 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12022() {
//        String mainKeyString = "31333333333333333434343434343434";
//        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
//        loadPlainMasterKey(99, mainKey, 6, null);//下载AES明文主密钥
//
//        String KeyStr = "35353535353535353636363636363636";
//        String CheckValueStr = "4CABA909924B00B9";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//AES主密钥解密类型
//        boolean retValue = loadWorkKey(9, 99, 99, 3, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12022 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12022 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12022 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12022 Case results：" + retValue + "，execute Failed！");
//    }
//
    public void L12023() {
        String mainKeyString = "31333333333333333434343434343434";
        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
        loadPlainMainKey(99, mainKey, 6, null);//下载AES明文主密钥

        String KeyStr = "35353535353535353636363636363637";
        String CheckValueStr = "6B0A0CC12028EC01";
        byte[] Key = BCDDecode.str2Bcd(KeyStr);
        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//AES主密钥解密类型
        boolean retValue = loadWorkKey(10, 99, 99, 3, Key, CheckValue);
        if (retValue) {
            logUtils.addCaseLog("L12023 Case results：" + retValue + "，execute success");
            this.printMsgTool("L12023 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("L12023 Case results：" + retValue + "，execute Failed！！");
        this.printMsgTool("L12023 Case results：" + retValue + "，execute Failed！");
    }
//
//    public void L12024() {
//        String mainKeyString = "31333333333333333434343434343434";
//        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
//        loadPlainMasterKey(99, mainKey, 6, null);//下载AES明文主密钥
//
//        String KeyStr = "35353535353535353636363636363638";
//        String CheckValueStr = "3671C330A464D81E";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//AES主密钥解密类型
//        boolean retValue = loadWorkKey(11, 99, 99, 3, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12024 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12024 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12024 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12024 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12025() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333333";
//        String CheckValueStr = "1122334455667788";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(2, 0, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12025 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12025 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12025 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12025 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12026() {
//        String mainKeyString = "33333333333333333434343434343434";
//        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
//        loadPlainMasterKey(99, mainKey, 6, null);//下载AES明文主密钥
//
//        String KeyStr = "35353535353535353636363636363636";
//        String CheckValueStr = "1122334455667788";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//AES主密钥解密类型
//        boolean retValue = loadWorkKey(11, 99, 99, 3, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12026 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12026 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12026 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12026 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12027() {
//        L02002();
//        int wkId = 66;
//        String tdKeyStr = "222222222222222244444444444444446666666666666666";
//        byte[] tdKey = BCDDecode.str2Bcd(tdKeyStr);
//        boolean retValue = loadWorkKey(3, 0, wkId, 1, tdKey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L12027 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12027 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12027 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12027 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12028() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333334";
//        String CheckValueStr = "";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(2, 0, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12028 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12028 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12028 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12028 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12029() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333334";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        boolean retValue = loadWorkKey(2, 0, 99, 0, Key, null);
//        if (retValue) {
//            logUtils.addCaseLog("L12029 Case results：" + retValue + "，execute success");
//            this.printMsgTool("L12029 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12029 Case results：" + retValue + "，execute Failed！！");
//        this.printMsgTool("L12029 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12030() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333333";
//        String CheckValueStr = "FB4C8F44A67614";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(2, 0, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12030 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L12030 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L12030 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L12030 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L12031() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333333";
//        String CheckValueStr = "FB4C8F44A6761464FB";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(2, 0, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12031 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12031 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12031 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12031 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12032() {
//        loadPlainMainKey();
//        String KeyStr = "313131313131313132323232323232323333333333333333";
//        String CheckValueStr = "FB4C8F44A6761464";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//3DES主密钥解密类型
//        boolean retValue = loadWorkKey(2, 1, 99, 0, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12032 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12032 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12032 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12032 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12033() {
//        String mainKeyString = "33333333333333333434343434343434";
//        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
//        loadPlainMasterKey(99, mainKey, 6, null);//下载AES明文主密钥
//
//        String KeyStr = "35353535353535353636363636363636";
//        String CheckValueStr = "2C81C8732F8DF220";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//AES主密钥解密类型
//        boolean retValue = loadWorkKey(11, 98, 99, 3, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12033 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12033 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12033 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12033 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L12034() {
////        String mainKeyString = "33333333333333333434343434343434";
////        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
////        loadPlainMasterKey(99, mainKey, 6, null);//下载AES明文主密钥
//
//        String KeyStr = "35353535353535353636363636363636";
//        String CheckValueStr = "2C81C8732F8DF220";
//        byte[] Key = BCDDecode.str2Bcd(KeyStr);
//        byte[] CheckValue = BCDDecode.str2Bcd(CheckValueStr);//AES主密钥解密类型
//        boolean retValue = loadWorkKey(11, 99, 99, 3, Key, CheckValue);
//        if (retValue) {
//            logUtils.addCaseLog("L12034 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L12034 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L12034 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L12034 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L13001() {
//        L05053();
//        int keyId = 99;
//        int type = 0;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
////        String retValue = StringUtil.byte2HexStr (My13calcMACWithCalType(keyId, type, null, data, destype,false));
//
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//
//
//    }
//
//    public void L13002() {
//        L05053();
//        int keyId = 99;
//        int type = 0;
//        int destype = 0;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13003() {
//        L05053();
//        int keyId = 99;
//        int type = 0;
//        int destype = 1;
//        String datastr = "1111111111111111222222222222222233333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13004() {
//        L05053();
//        int keyId = 99;
//        int type = 2;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13005() {
//        L05053();
//        int keyId = 99;
//        int type = 2;
//        int destype = 0;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13006() {
//        L05053();
//        int keyId = 99;
//        int type = 2;
//        int destype = 1;
//        String datastr = "1111111111111111222222222222222233333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13007() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13008() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = 0;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13009() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = 1;
//        String datastr = "1111111111111111222222222222222233333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13010() {
//        L05053();
//        int keyId = -1;
//        int type = 0;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13011() {
//        L05053();
//        int keyId = 100;
//        int type = 0;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13012() {
//        L05053();
//        int keyId = 9;
//        int type = 0;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13013() {
//        L05053();
//        int keyId = 99;
//        int type = -1;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13014() {
//        L05053();
//        int keyId = 99;
//        int type = 5;
//        int destype = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13015() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = -1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13016() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = 4;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13017() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, null, destype, false);
//    }
//
//    public void L13018() {
//        L05053();
//        int keyId = 99;
//        int type = 3;
//        int destype = 1;
//        String datastr = "";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13019() {
//        L16003();
//        int keyId = 0;
//        int type = 0;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13020() {
//        L16003();
//        int keyId = 1;
//        int type = 0;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13021() {
//        L16003();
//        int keyId = 0;
//        int type = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13022() {
//        L16003();
//        int keyId = 0;
//        int type = 2;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13023() {
//        L16003();
//        int keyId = 0;
//        int type = 3;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13024() {
//        L16003();
//        int keyId = 0;
//        int type = 1;
//        String datastr = "111111111111111122222222222222223333333333333333";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13025() {
//        L05067();
//        int keyId = 10;
//        int type = 1;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13026() {
//        L05053();
//        int keyId = 99;
//        int type = 1;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13027() {
//        L05053();
//        int keyId = 99;
//        int type = 1;
//        String datastr = "1111111111111111222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13028() {
//        L16003();
//        int keyId = 0;
//        int type = 0;
//        String datastr = "1111111111111111222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13029() {
//        L16003();
//        int keyId = 0;
//        int type = 0;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13030() {
//        L16003();
//        int keyId = 0;
//        int type = 2;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13031() {
//        L16003();
//        int keyId = 0;
//        int type = 3;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13032() {
//        L16003();
//        int keyId = 0;
//        int type = 2;
//        String datastr = "111111111111111122222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13033() {
//        L16003();
//        int keyId = -1;
//        int type = 2;
//        String datastr = "111111111111111122222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13034() {
//        L16003();
//        int keyId = 5;
//        int type = 2;
//        String datastr = "111111111111111122222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 0;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13035() {
//        L12022();
//        int keyId = 99;
//        int type = 0;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13036() {
//        L12022();
//        int keyId = 99;
//        int type = 1;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13037() {
//        L12022();
//        int keyId = 99;
//        int type = 2;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13038() {
//        L12022();
//        int keyId = 99;
//        int type = 3;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13039() {
//        L12022();
//        int keyId = 99;
//        int type = 3;
//        String datastr = "1111111111111111222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13040() {
//        L16003();
//        int keyId = 0;
//        int type = 0;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13041() {
//        L16003();
//        int keyId = 0;
//        int type = 1;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13042() {
//        L16003();
//        int keyId = 0;
//        int type = 2;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13043() {
//        L16003();
//        int keyId = 0;
//        int type = 3;
//        String datastr = "11111111111111112222222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13044() {
//        L16003();
//        int keyId = 0;
//        int type = 0;
//        String datastr = "1111111111111111222222222222";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        int destype = 3;
//        My13calcMACWithCalType(keyId, type, null, data, destype, true);
//    }
//
//    public void L13045() {
//        L05053();
//        int keyId = 99;
//        int type = 0;
//        byte[] data = new byte[2048];
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L13046() {
//        L05053();
//        int keyId = 99;
//        int type = 0;
//        byte[] data = new byte[2049];
//        int destype = 1;
//        My13calcMACWithCalType(keyId, type, null, data, destype, false);
//    }
//
//    public void L14001() {
//        int mode = 0;
//        int desType = 0;
//        byte[] key = BCDDecode.str2Bcd("3232323232363738");
//        byte[] data = BCDDecode.str2Bcd("1111111111111111");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("10515626048C648D")) {
//            logUtils.addCaseLog("L14001 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14001 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14001 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14001 Case results：" + retValue + "execute Failed！");
//
//    }
//
//    public void L14002() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("32323232323232323232323232323236");
//        byte[] data = BCDDecode.str2Bcd("11111111111111111111111111111111");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("42ABD54033D3921C42ABD54033D3921C")) {
//            logUtils.addCaseLog("L14002 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14002 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14002 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14002 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14003() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323235");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("9DF2DFDDD4E9F2509DF2DFDDD4E9F2509DF2DFDDD4E9F250")) {
//            logUtils.addCaseLog("L14003 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14003 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14003 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14003 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14004() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("3232323232323232323232323232323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//
//
//    }
//
//    public void L14005() {
//        int mode = 1;
//        int desType = 0;
//        byte[] key = BCDDecode.str2Bcd("3232323232323231");
//        byte[] data = BCDDecode.str2Bcd("1111111111111111");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("9977C7ED73C1E20C")) {
//            logUtils.addCaseLog("L14005 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14005 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14005 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14005 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14006() {
//        int mode = 1;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("32323232323232323232323232323234");
//        byte[] data = BCDDecode.str2Bcd("11111111111111111111111111111111");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("18FBD01911C2674D18FBD01911C2674D")) {
//            logUtils.addCaseLog("L14006 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14006 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14006 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14006 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14007() {
//        int mode = 1;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323239");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("227002ED0E0699E1227002ED0E0699E1227002ED0E0699E1")) {
//            logUtils.addCaseLog("L14007 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14007 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14007 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14007 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14008() {
//        int mode = 1;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("32323232324032323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("1111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14009() {
//        int mode = -1;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14010() {
//        int mode = 2;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14011() {
//        int mode = 1;
//        int desType = -1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14012() {
//        int mode = 1;
//        int desType = 7;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14013() {
//        int mode = 1;
//        int desType = 1;
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, null, data);
//    }
//
//    public void L14014() {
//        int mode = 1;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("");
//        byte[] data = BCDDecode.str2Bcd("111111111111111111111111111111111111111111111111");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14016() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323232");
//        My14calculateData(mode, desType, key, null);
//    }
//
//    public void L14017() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323232323232323232323232323232");
//        byte[] data = BCDDecode.str2Bcd("");
//        My14calculateData(mode, desType, key, data);
//    }
//
//    public void L14019() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232323232323432323232323232323232323232");
//        byte[] data = new byte[2048];
//        Arrays.fill(data, (byte) 0x11);
//
//
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C651616172DBAAE5C6516161")) {
//            logUtils.addCaseLog("L14019 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14019 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14019 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14019 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14020() {
//        int mode = 0;
//        int desType = 1;
//        byte[] key = BCDDecode.str2Bcd("323232323232413232323232323232323232323232323232");
//        byte[] data = new byte[2049];
//        Arrays.fill(data, (byte) 0x11);
//        My14calculateData(mode, desType, key, data);
//
//    }
//
//    public void L14021() {
//        int mode = 0;
//        int desType = 3;
//        byte[] key = BCDDecode.str2Bcd("32323232323232323737373737373737");
//        byte[] data = BCDDecode.str2Bcd("34343434343434343535353535353535");
//
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//
//        if (retValue.equals("306B59D80774A2FF55969BF0E1249361")) {
//            logUtils.addCaseLog("L14021 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14021 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14021 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14021 Case results：" + retValue + "execute Failed！");
//    }
//
//    /*public void L14022() {
//        int mode = 0;
//        int desType = 6;
//        byte[] key = BCDDecode.str2Bcd("31333537393232323838383838363838");
//        byte[] data = BCDDecode.str2Bcd("34343434343434343535353535353535");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//        if (retValue.equals("24680F893B456EBA60964FDD5D73CD05")) {
//            logUtils.addCaseLog("L14022 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14022 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14022 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14022 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14023() {
//        int mode = 1;
//        int desType = 3;
//        byte[] key = BCDDecode.str2Bcd("32323232323232323737373737373737");
//        byte[] data = BCDDecode.str2Bcd("34343434343434343535353535353535");
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//        if (retValue.equals("F05B287B6CAC9426A760970D726E0540")) {
//            logUtils.addCaseLog("L14023 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14023 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14023 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14023 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L14024() {
//        int mode = 1;
//        int desType = 6;
//        byte[] key = BCDDecode.str2Bcd("31333537393232323838383838363838");
//        byte[] data = BCDDecode.str2Bcd("34343434343434343535353535353535");
//
//        String retValue = StringUtil.byte2HexStr(My14calculateData(mode, desType, key, data));
//        if (retValue.equals("827B7B6ACB5E1CC5734AAA2B6D2C7587")) {
//            logUtils.addCaseLog("L14024 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L14024 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L14024 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L14024 Case results：" + retValue + "execute Failed！");
//    }*/
//
//    public void L15001() {
//        int keyType = 1;
//        int keyId = 0;
//        byte[] key = BCDDecode.str2Bcd("31313131313141513131813131313192");
//        My15savePlainKey(keyType, keyId, key);
//
//
//    }
//
//    public void L15002() {
//        int keyType = 1;
//        int keyId = 99;
//        byte[] key = BCDDecode.str2Bcd("313131313131313131313131313141313131313131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15003() {
//        int keyType = 2;
//        int keyId = 1;
//        byte[] key = BCDDecode.str2Bcd("31313131313131313131316131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15004() {
//        int keyType = 2;
//        int keyId = 98;
//        byte[] key = BCDDecode.str2Bcd("313131313131313138313131313131313131313131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15005() {
//        int keyType = 3;
//        int keyId = 10;
//        byte[] key = BCDDecode.str2Bcd("31313131313131313191313131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15006() {
//        int keyType = 3;
//        int keyId = 50;
//        byte[] key = BCDDecode.str2Bcd("313131313131313131313131213131313131313131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15007() {
//        int keyType = 0;
//        int keyId = 0;
//        byte[] key = BCDDecode.str2Bcd("31313131313131313131316161313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15008() {
//        int keyType = 4;
//        int keyId = 0;
//        byte[] key = BCDDecode.str2Bcd("31313131313131312131413131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15009() {
//        int keyType = 1;
//        int keyId = -1;
//        byte[] key = BCDDecode.str2Bcd("31313131313131313121313171313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15010() {
//        int keyType = 1;
//        int keyId = 100;
//        byte[] key = BCDDecode.str2Bcd("31313131313131513131313131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15011() {
//        int keyType = 1;
//        int keyId = 0;
//        My15savePlainKey(keyType, keyId, null);
//    }
//
//    public void L15012() {
//        int keyType = 1;
//        int keyId = 0;
//        byte[] key = BCDDecode.str2Bcd("");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L15014() {
//        int keyType = 1;
//        int keyId = 0;
//        byte[] key = BCDDecode.str2Bcd("313131313131316171313131313131");
//        My15savePlainKey(keyType, keyId, key);
//    }
//
//    public void L16001() {
////        FormatKeyArea();
//        int keyId = 3;
//        String dukptksnString = "FFFFFFFF120000000000";
//        byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        String dukptkeyString = "3AAE214EDF5AB6D4FFE8E6CD884936BE";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        Boolean retValue1 = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        keyId = 4 ;
//        dukptksnString = "FFFFFFFF130000000000";
//        dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        dukptkeyString = "30F03FF55C991FA6359F3153E2783B4D";
//        dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        Boolean retValue2 = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        logUtils.addCaseLog("L1600101 Case results：" + retValue1 + " ！");
//        logUtils.addCaseLog("L1600101 Case results：" + retValue2 + " ！");
//    }
//
//    public void L1600101() {
////        FormatKeyArea();
//        int keyId = 1;
//        String dukptksnString = "01020304050607080901";
//        byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        String dukptkeyString = "363435353534343435333535363835353434343434323235";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        String dukptkeycheckvalueString = "F39941C42F44E374";
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
////        Boolean retValue = loadDukptKey(keyId,dukptksn,dukptkey,null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L1600101 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L1600101 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L1600101 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L1600101 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L1600102() {
////        FormatKeyArea();
//        int keyId = 2;
//        String dukptksnString = "01020304050607080901";
//        byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        String dukptkeyString = "363434353534343435353535363835353434343434323235";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        String dukptkeycheckvalueString = "F39941C42F44E374";
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
////        Boolean retValue = loadDukptKey(keyId,dukptksn,dukptkey,null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L1600102 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L1600102 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L1600102 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L1600102 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L1600103() {
////        FormatKeyArea();
//        int keyId = 3;
//        String dukptksnString = "01020304050607080901";
//        byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        String dukptkeyString = "363434353534343435353535363535353434343434323235";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        String dukptkeycheckvalueString = "F39941C42F44E374";
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L1600103 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L1600103 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16001 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16001 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L1600104() {
////        FormatKeyArea();
//        int keyId = 4;
//        String dukptksnString = "01020304050607080901";
//        byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        String dukptkeyString = "363434353534343435353535353535353434343434323235";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        String dukptkeycheckvalueString = "F39941C42F44E374";
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L1600104 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L1600104 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L1600104 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L1600104 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L1600105() {
////        FormatKeyArea();
//        int keyId = 0;
//        String dukptksnString = "01020304050607080901";
//        byte[] dukptksn = BCDDecode.str2Bcd(dukptksnString);
//        String dukptkeyString = "363437343434343735353535353535353434343434323235";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyString);
//        String dukptkeycheckvalueString = "F39941C42F44E374";
//        byte[] dukptkeycheckvalue = BCDDecode.str2Bcd(dukptkeycheckvalueString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L1600105 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L1600105 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L1600105 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L1600105 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L16002() {
////        FormatKeyArea();
//        L02002();
//        int keyId = 0;
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L16002 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16002 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16002 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16002 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L16003() {
////        FormatKeyArea();
//        loadTEK();
//        int keyId = 0;
//
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L16003 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16003 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16003 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16003 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L16004() {
//        loadTEK();
//        int keyId = 4;
//
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L16004 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16004 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16004 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16004 Case results：" + retValue + "execute Failed！");
//
//    }
//
//    public void L16005() {
//        loadTEK();
//        int keyId = -1;
//
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//
//        if (retValue) {
//            logUtils.addCaseLog("L16005 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16005 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16005 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16005 Case results：" + retValue + "execute success！");
//
//    }
//
//    public void L16006() {
//        loadTEK();
//        int keyId = 5;
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16006 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16006 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16006 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16006 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16007() {
//        loadTEK();
//        int keyId = 0;
//        Boolean retValue = loadDukptKey(keyId, null, dukptkey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16007 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16007 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16007 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16007 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16008() {
//        loadTEK();
//        int keyId = 0;
//        String ksnString = "";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        Boolean retValue = loadDukptKey(keyId, ksn, dukptkey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16008 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16008 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16008 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16008 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16009() {
//        loadTEK();
//        int keyId = 0;
//        Boolean retValue = loadDukptKey(keyId, dukptksn, null, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16009 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16009 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16009 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16009 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16010() {
//        loadTEK();
//        int keyId = 0;
//        String keyString = "";
//        byte[] key = BCDDecode.str2Bcd(keyString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, key, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16010 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16010 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16010 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16010 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16011() {
//        loadTEK();
//        int keyId = 0;
//        String kcvString = "";
//        byte[] kcv = BCDDecode.str2Bcd(kcvString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, kcv);
//        if (retValue) {
//            logUtils.addCaseLog("L16011 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16011 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16011 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16011 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L16012() {
//        loadTEK();
//        int keyId = 1;
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, dukptkeycheckvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L16012 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16012 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16012 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16012 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L16013() {
//        loadTEK();
//        int keyId = 0;
//        String kcvString = "1111111122222222";
//        byte[] kcv = BCDDecode.str2Bcd(kcvString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, kcv);
//        if (retValue) {
//            logUtils.addCaseLog("L16013 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16013 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16013 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16013 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16014() {
//        loadTEK();
//        int keyId = 0;
//        String ksnString = "010203040506070809";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        Boolean retValue = loadDukptKey(keyId, ksn, dukptkey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16014 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16014 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16014 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16014 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16015() {
//        loadTEK();
//        int keyId = 0;
//        String ksnString = "0102030405060708090001";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        Boolean retValue = loadDukptKey(keyId, ksn, dukptkey, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16015 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16015 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16015 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16015 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16016() {
//        loadTEK();
//        int keyId = 0;
//        String keyString = "3434343434343434343434343434343434343434343434";//23个
//        byte[] key = BCDDecode.str2Bcd(keyString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, key, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16016 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16016 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16016 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16016 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16017() {
//        loadTEK();
//        int keyId = 0;
//        String keyString = "34343434343434343434343434343434343434343434343434";//25个
//        byte[] key = BCDDecode.str2Bcd(keyString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, key, null);
//        if (retValue) {
//            logUtils.addCaseLog("L16017 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16017 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16017 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16017 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16018() {
//        loadTEK();
//        int keyId = 0;
//        String kcvString = "F39941C42F44E3";
//        byte[] kcv = BCDDecode.str2Bcd(kcvString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, kcv);
//        if (retValue) {
//            logUtils.addCaseLog("L16018 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16018 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16018 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16018 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16019() {
//        loadTEK();
//        int keyId = 0;
//        String kcvString = "F39941C42F44E374F3";
//        byte[] kcv = BCDDecode.str2Bcd(kcvString);
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, kcv);
//        if (retValue) {
//            logUtils.addCaseLog("L16019 Case results：" + retValue + "execute Failed！");
//            this.printMsgTool("L16019 Case results：" + retValue + "execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L16019 Case results：" + retValue + "execute success！");
//        this.printMsgTool("L16019 Case results：" + retValue + "execute success！");
//    }
//
//    public void L16020() {
//        loadTEK();
//        int keyId = 2;
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, dukptkeycheckvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L16020 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16020 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16020 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16020 Case results：" + retValue + "execute Failed！");
//    }
//
//    public void L16021() {
//        loadTEK();
//        int keyId = 3;
//        Boolean retValue = loadDukptKey(keyId, dukptksn, dukptkey, dukptkeycheckvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L16021 Case results：" + retValue + "execute success！");
//            this.printMsgTool("L16021 Case results：" + retValue + "execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L16021 Case results：" + retValue + "execute Failed！");
//        this.printMsgTool("L16021 Case results：" + retValue + "execute Failed！");
//    }
//
    public void loadDukptKey() {
        loadTEK();
        int keyId = 0;
        String ksnString = "FFFF9876543210E00001";
        byte[] ksn = BCDDecode.str2Bcd(ksnString);
        String dukptkeyStr = "34343434343434343535353535353535";
        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyStr);
        Boolean retValue = loadDukptKey(keyId, ksn, dukptkey, null);
        if (retValue) {
            logUtils.addCaseLog("loadDukptKey Case results：" + retValue + "execute success！");
            this.printMsgTool("loadDukptKey Case results：" + retValue + "execute success！");
            return;
        }
        logUtils.addCaseLog("loadDukptKey Case results：" + retValue + "execute Failed！");
        this.printMsgTool("loadDukptKey Case results：" + retValue + "execute Failed！");
    }
//
//    public void L17001() {
//        L05055();
//        int mode = 0;   //ECB
//        int kId = 99;
//        int algorithmType = 1;//3DES
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, false));
//
//
//        if (retValue.equals("9D9104459EEEACD99D9104459EEEACD99D9104459EEEACD9")) {
//            logUtils.addCaseLog("L17001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17002() {
//        L05055();
//        int mode = 1;
//        int kId = 99;
//        int algorithmType = 1;//3DES
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, false));
//        if (retValue.equals("9D9104459EEEACD99A38CD87895440D764AF76DA0D497E31")) {
//            logUtils.addCaseLog("L17002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17003() {
//        L12024();
//        int mode = 0;
//        int kId = 99;
//        int algorithmType = 3;
//        String trackString = "31313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, false));
//        if (retValue.equals("2CBDB5260BE7429BCC882142EF7DF7D8")) {
//            logUtils.addCaseLog("L17003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17004() {
//        L12024();
//        int mode = 1;
//        int kId = 99;
//        int algorithmType = 3;
//        String trackString = "31313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, false));
//        if (retValue.equals("2CBDB5260BE7429BCC882142EF7DF7D8")) {
//            logUtils.addCaseLog("L17004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17005() {
//        loadDukptKey();
//        int mode = 0;   //ECB
//        int kId = 0;
//        int algorithmType = 1;//3DES
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true));
//        if (retValue.equals("DE8D6DE63405D9ABDF04A7DC69F41DCDAA4900387078CA0516E7B8575E10253A")) {
//            logUtils.addCaseLog("L17005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17005 Case results：" + retValue + "，execute Failed！");
//
//    }
//
//    public void L17006() {
//        loadDukptKey();
//        int mode = 1;   //CBC
//        int kId = 0;
//        int algorithmType = 1;//3DES
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true));
//        if (retValue.equals("72B00025988C5EEA37964270C75807C07D89CF2CAA7666C4")) {
//            logUtils.addCaseLog("L17006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17007() {
//        loadDukptKey();
//        int mode = 0;
//        int kId = 0;
//        int algorithmType = 3;
//        String trackString = "31313131313131313232323232323232";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true));
//        if (retValue.equals("B5D6E2128E8E94CCBF8B494F1D9BA90C")) {
//            logUtils.addCaseLog("L17007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17008() {
//        loadDukptKey();
//        int mode = 1;
//        int kId = 0;
//        int algorithmType = 3;
//        String trackString = "31313131313131313232323232323232";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true));
//        if (retValue.equals("0B137D187EF5E5E2A33495BB8047823A")) {
//            logUtils.addCaseLog("L17008 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17008 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17008 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17008 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17009() {
//        loadDukptKey();
//        int mode = 1;
//        int kId = -1;
//        int algorithmType = 3;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true);
//    }
//
//    public void L17010() {
//        loadDukptKey();
//        int mode = 1;
//        int kId = 5;
//        int algorithmType = 3;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true);
//    }
//
//    public void L17011() {
//        loadDukptKey();
//        int mode = 1;
//        int kId = 3;
//        int algorithmType = 3;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true);
//    }
//
//    public void L17012() {
//        loadDukptKey();
//        int mode = 1;
//        int kId = 3;
//        int algorithmType = 3;
//        String trackString = "3131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        String retValue = StringUtil.byte2HexStr(My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, true));
//        if (retValue.equals("3131313131313131313131313131313131313131313131")) {
//            logUtils.addCaseLog("L17012 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L17012 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L17012 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L17012 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L17013() {
//        L05055();
//        int mode = 1;
//        int kId = 99;
//        int algorithmType = 0;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, false);
//
//    }
//
//    public void L17014() {
//        L05055();
//        int mode = 1;
//        int kId = 99;
//        int algorithmType = 4;
//        String trackString = "313131313131313131313131313131313131313131313131";
//        byte[] track = BCDDecode.str2Bcd(trackString);
//        My17encryptTrackDataWithAlgorithmType(mode, kId, algorithmType, track, false);
//    }
//
//    public void L18001() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(2, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060709");
//        loadDukptKey(2, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 2;
//        int keyid = 2;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18002() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
////        loadDukptKey(0,dukptksn,dukptkey,null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18003() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18004() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18005() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = -1;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18006() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 4;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18007() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 0;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18008() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 3;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18009() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = -1;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18010() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 5;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18011() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 3;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18012() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, null, CBCInitVec);
//    }
//
//    public void L18013() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        String datastr = "";
//        byte[] data = datastr.getBytes();
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18014() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        String datastr = "3030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18015() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        String datastr = "30303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18016() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        byte[] data = new byte[2048];
//        Arrays.fill(data, (byte) 0x30);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18017() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        byte[] data = new byte[2049];
//        Arrays.fill(data, (byte) 0x30);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18019() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, null);
//    }
//
//    public void L18020() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//
//    public void L18021() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0101010101010101";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18022() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "01010101010101";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18023() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 0;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "010101010101010101";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18024() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 3;
//        int algorithmType = 2;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L18025() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//        byte[] dukptksn = BCDDecode.str2Bcd("FFFF9876543210E00002");
//        byte[] dukptkey = BCDDecode.str2Bcd("01020304050607080102030405060708");
//        loadDukptKey(0, dukptksn, dukptkey, null);
//
//        int destype = 3;
//        int algorithmType = 1;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//    }
//
//    public void L19001() {
//        getDukptKsn();
//    }
//
//    public void L19002() {
//        L13019();
//        getDukptKsn();
//    }
//
//    public void L19003() {
//        L17005();
//        getDukptKsn();
//    }
//
//    public void L19004() {
//        L18002();
//        getDukptKsn();
//    }
//
//    public void L19005() {
//        L16003();
//        int destype = 1;
//        int algorithmType = 2;
//        int keyid = 0;
//        String datastr = "303030303030303030303030303030303030303030303030";
//        byte[] data = BCDDecode.str2Bcd(datastr);
//        String CBCinitvecStr = "0000000000000000";
//        byte[] CBCInitVec = BCDDecode.str2Bcd(CBCinitvecStr);
//        for (int i = 0; i < 22; i++) {
//            My18dukptEncryptData(destype, algorithmType, keyid, data, CBCInitVec);
//            getDukptKsn();
//        }
//    }
//
//    public void L19006() {
//        L16003();//L01075()
//        int keyId = 0;
//        Bundle param = new Bundle();
//        String panBlock = "6226901508781352";
//        byte[] pinLimit = {0, 6};
//        param.putByteArray(BUNDLE_PINPARAM_PINLIMIT, pinLimit);
//        param.putInt(BUNDLE_PINPARAM_TIMEOUT, 60);
//        param.putBoolean(BUNDLE_PINPARAM_ISONLINE, true);
//        param.putString(BUNDLE_PINPARAM_PAN, panBlock);
//        param.putString(BUNDLE_PINPARAM_PROMPTSTR, "please input pin:");
//        param.putInt(BUNDLE_PINPARAM_DESTYPE, 4);
//        for (int i = 0; i < 3; i++) {
//            logUtils.addCaseLog("第" + i + "次弹出密码键盘");
//            My01startPinInput(keyId, param, null);
//            try {
//                Thread.sleep(15000);//延时加长，是因为这个方法实现是异步的，不会等到我输入完再做后面的事
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void L20001() {
//        loadTEK();
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20002() {
//        L02002();
//        int keyId = 99;
//        String KeyString = "313131313131313132323232323232323333333333333333";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "27F7C8B4BA6E92A5";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20003() {
//        int keyId = 10;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "0D28E17571051319";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 2;//3DES明文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20004() {
//        int keyId = 0;
//        String KeyString = "313131313131313132323232323232323333333333333333";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "40826A5800608C87";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 2;//3DES明文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20005() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "5642B6EFC62204A9";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 5;//AES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20006() {
//        int keyId = 10;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "CBBA94FEE8C1A171";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 6;//AES明文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20006 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20006 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20007() {
//        int keyId = -1;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20007 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20007 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20007 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20007 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20008() {
//        int keyId = 100;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20009() {
//        int keyId = 0;
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, null, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20010() {
//        int keyId = 0;
//        String KeyString = "";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20011() {
//        int keyId = 0;
//        String KeyString = "313131313131313132323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20012() {
//        int keyId = 0;
//        String KeyString = "3131313131313131323232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20012 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20012 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20012 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20012 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20013() {
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 0;
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20013 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20014() {
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 7;
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20015() {
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, null);
//        if (retValue) {
//            logUtils.addCaseLog("L20015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20016() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 5;//AES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20016 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20016 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20016 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20016 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20017() {
//        loadTEK();
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20017 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20017 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20017 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20017 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20018() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "1122334455667788 ";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 5;//AES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20018 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20018 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20018 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20018 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20019() {
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 6;//AES明文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20019 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20019 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20019 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20019 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L20020() {
//        loadTEK();
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F16633416262";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20020 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L20020 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L20020 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L20020 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L20021() {
//        loadTEK();
//        int keyId = 0;
//        String KeyString = "31313131313131313232323232323232";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        String checkvalueString = "58F166334162627F58";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        byte algorithmType = (byte) 1;//3DES密文
//        boolean retValue = My20loadTEKWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L20021 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20021 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20021 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20021 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21001() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21002() {
//        L20004();
//        int keyId = 0;
//        String KeyString = "343434343434343435353535353535353636363636363636";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
////        String checkvalueString = "D82307FB9DE6C0E7";
//        String checkvalueString = "0742F7CBD722FE46";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21003() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "34343434343434343535353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "EFD5BB7A78AE1A57";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21004() {
//        L20016();
//        int keyId = 10;
//        String KeyString = "36363636363636363737373737373737";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "C22885CA28E3119C";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21005() {
//        L20001();
//        int keyId = -1;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21005 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21005 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21005 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21005 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21006() {
//        L20001();
//        int keyId = 100;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21006 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21006 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21006 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21006 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21007() {
//        L20001();
//        int keyId = 0;
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, null, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21007 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21007 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21007 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21007 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21008() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21009() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "343434343434343435353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "EFD5BB7A78AE1A57";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21010() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "3434343434343434353535353535353536";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "EFD5BB7A78AE1A57";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21011() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 0;
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21012() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;
//        String checkvalueString = "977D2726C4F20FB4";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21012 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21012 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21012 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21012 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21013() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21013 Case results果：" + retValue + "，execute success！");
//    }
//
//    public void L21014() {
//        L20004();
//        int keyId = 99;
//        String KeyString = "343434343434343435353535353535353636363636363636";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L20021 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L20014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L20014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21015() {
//        L20006();
//        int keyId = 10;
//        String KeyString = "34343434343434343535353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21015 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21015 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21015 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21015 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21016() {
//        L20016();
//        int keyId = 10;
//        String KeyString = "36363636363636363737373737373737";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21016 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L21017() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, null);
//        if (retValue) {
//            logUtils.addCaseLog("L21017 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21017 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21017 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21017 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21018() {
//        L20016();
//        int keyId = 10;
//        String KeyString = "36363636363636363737373737373737";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 5;//AES密文
//        String checkvalueString = "";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21018 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21018 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21018 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21018 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21019() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20F";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21019 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L21019 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L21019 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L21019 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L21020() {
//        L20001();
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;//3DES密文
//        String checkvalueString = "977D2726C4F20FB497";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = My21loadEncryptMainKeyWithAlgorithmType(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L21020 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L21020 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L21020 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L21020 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22001() {
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L22001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L22001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L22001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L22002() {
//        int keyId = 99;
//        String KeyString = "333333333333333334343434343434343535353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;//AES明文
//        String checkvalueString = "78E3CB7F2968C767";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L22002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L22002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L22002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L22003() {
//        int keyId = -1;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22003 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22003 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22003 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22003 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22004() {
//        int keyId = 100;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22004 Case results：" + retValue + "，execute success！");
//
//    }
//
//    public void L22005() {
//        int keyId = 0;
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, null, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22005 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22005 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22005 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22005 Case results：" + retValue + "，execute success！");
//
//    }
//
//    public void L22006() {
//        int keyId = 0;
//        String KeyString = "";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22006 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22006 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22006 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22006 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22007() {
//        int keyId = 99;
//        String KeyString = "3333333333333333343434343434343435353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;//AES明文
//        String checkvalueString = "78E3CB7F2968C767";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22007 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22007 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22007 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22007 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22008() {
//        int keyId = 99;
//        String KeyString = "33333333333333333434343434343434353535353535353536";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;//AES明文
//        String checkvalueString = "78E3CB7F2968C767";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22008 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22009() {
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 1;
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22010() {
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 7;
//        String checkvalueString = "03E0686FCAB069B1";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22011() {
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, null);
//        if (retValue) {
//            logUtils.addCaseLog("L22011 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L22011 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L22011 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L22011 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L22012() {
//        int keyId = 99;
//        String KeyString = "333333333333333334343434343434343535353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;//AES明文
//        String checkvalueString = "";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22012 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L22012 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L22012 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L22012 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L22013() {
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22013 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22014() {
//        int keyId = 99;
//        String KeyString = "333333333333333334343434343434343535353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;//AES明文
//        String checkvalueString = "1122334455667788";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L22015() {
//        int keyId = 0;
//        String KeyString = "33333333333333333434343434343434";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 2;//3DES明文
//        String checkvalueString = "03E0686FCAB069";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L22015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L22015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L22015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L22016() {
//        int keyId = 99;
//        String KeyString = "333333333333333334343434343434343535353535353535";
//        byte[] Key = BCDDecode.str2Bcd(KeyString);
//        int algorithmType = 6;//AES明文
//        String checkvalueString = "78E3CB7F2968C7678C0316FBF78466CF12";
//        byte[] checkvalue = BCDDecode.str2Bcd(checkvalueString);
//        boolean retValue = loadPlainMasterKey(keyId, Key, algorithmType, checkvalue);
//        if (retValue) {
//            logUtils.addCaseLog("L22016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L22016 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L22016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L22016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void getkcv_demo_DES() {
//        //索引1下载主密钥（DES）
//        String mkeyStr = "40404040404040404141414141414141";
//        byte[] mkey = BCDDecode.str2Bcd(mkeyStr);
//        My04loadPlainMasterKey(1, mkey, null);
//        //索引2下载TD密钥（DES）
//        String tdkeyStr = "39393939393939393838383838383838";
//        byte[] tdkey = BCDDecode.str2Bcd(tdkeyStr);
//        My05loadWorkKey(3, 1, 2, tdkey, null);
//        //索引3下载PIN密钥（DES）
//        String pinkeyStr = "50505050505050504949494949494949";
//        byte[] pinkey = BCDDecode.str2Bcd(pinkeyStr);
//        My05loadWorkKey(2, 1, 3, pinkey, null);
//        //索引4下载MAC密钥（DES）
//        String mackeyStr = "20202020202020203030303030303030";
//        byte[] mackey = BCDDecode.str2Bcd(mackeyStr);
//        My05loadWorkKey(1, 1, 4, mackey, null);
//        //索引4下载传输密钥（DES）,实际下到了索引0
//        String tekeyStr = "10101010101010102525252525252525";
//        byte[] tekey = BCDDecode.str2Bcd(tekeyStr);
//        My02loadTEK(0, tekey, null);
////        My02loadTEK(4, tekey, null);
//    }
//
//
//    public void getkcv_demo_AES() {
//        //索引0下载传输密钥（AES）
//        String tekeyStr = "10101010101010102525252525252528";
//        byte[] tekey = BCDDecode.str2Bcd(tekeyStr);
//        My20loadTEKWithAlgorithmType(0, tekey, (byte) 6, null);
//        //索引99下载主密钥（AES）
//        String mkeyStr = "47474040404040404141414141414141";
//        byte[] mkey = BCDDecode.str2Bcd(mkeyStr);
//        loadPlainMasterKey(99, mkey, 6, null);
//        //索引98下载TD密钥（AES）
//        String tdkeyStr = "39393939394647393838383838383838";
//        byte[] tdkey = BCDDecode.str2Bcd(tdkeyStr);
//        loadWorkKey(11, 99, 98, 3, tdkey, null);
//        //索引97下载PIN密钥（AES）
//        String pinkeyStr = "50505050565050504949494949494949";
//        byte[] pinkey = BCDDecode.str2Bcd(pinkeyStr);
//        loadWorkKey(10, 99, 97, 3, pinkey, null);
//        //索引96下载MAC密钥（AES）
//        String mackeyStr = "20202020202020203033343030303030";
//        byte[] mackey = BCDDecode.str2Bcd(mackeyStr);
//        loadWorkKey(9, 99, 96, 3, mackey, null);
//    }
//
//    public void L23001() {
//        getkcv_demo_DES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(0, 4));
//
//        if (retValue.equals("A228A02D7CC3C8F4")) {
//            logUtils.addCaseLog("L23001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23002() {
//        getkcv_demo_DES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(1, 5));
//
//        if (retValue.equals("C4B738CDD18D6E48")) {
//            logUtils.addCaseLog("L23002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23003() {
//        getkcv_demo_DES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(2, 1));
//
//        if (retValue.equals("CB5012BD3A04D5E8")) {
//            logUtils.addCaseLog("L23003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23004() {
//        getkcv_demo_DES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(3, 2));
//
//        if (retValue.equals("B3DD928BB85B8C28")) {
//            logUtils.addCaseLog("L23004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23005() {
//        getkcv_demo_DES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(4, 3));
//
//        if (retValue.equals("6F1B1DC8D914B83B")) {
//            logUtils.addCaseLog("L23005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23006() {
//        getkcv_demo_DES();
//        My23getKeyKCV(37, 2);
//    }
//
//    public void L23007() {
//        getkcv_demo_DES();
//        My23getKeyKCV(-1, 2);
//    }
//
//    public void L23008() {
//        getkcv_demo_DES();
//        My23getKeyKCV(100, 2);
//    }
//
//    public void L23009() {
//        getkcv_demo_AES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(0, 0x24));
//
//        if (retValue.equals("DEFCADB70B8C1BE38778402611CCA032")) {
//            logUtils.addCaseLog("L23009 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23009 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23009 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23009 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23010() {
////        getkcv_demo_AES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(99, 0x02));
//        this.printMsgTool("value = "+retValue);
////
////        if (retValue.equals("AAC047D4D3747E46756EC5C478AF8E19")) {
////            logUtils.addCaseLog("L23010 Case results：" + retValue + "，execute success！");
////            this.printMsgTool("L23010 Case results：" + retValue + "，execute success！");
////            return;
////        }
////        logUtils.addCaseLog("L23010 Case results：" + retValue + "，execute Failed！");
////        this.printMsgTool("L23010 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23011() {
//        getkcv_demo_AES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(98, 0x21));
//
//        if (retValue.equals("861A2CFB1DA52554771F600A35963474")) {
//            logUtils.addCaseLog("L23011 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23011 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23011 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23011 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23012() {
//        getkcv_demo_AES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(97, 0x22));
//
//        if (retValue.equals("4F5C03A88A6D21D58EE158ECE424F76F")) {
//            logUtils.addCaseLog("L23012 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23012 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23012 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23012 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23013() {
//        getkcv_demo_AES();
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(96, 0x23));
//
//        if (retValue.equals("D727E37FB92E6D53CE4CD052D88A963B")) {
//            logUtils.addCaseLog("L23013 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L23013 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L23013 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L23013 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L23014() {
//        getkcv_demo_AES();
//        My23getKeyKCV(96, 0);
//    }
//
//    public void L23015() {
//        getkcv_demo_AES();
//        My23getKeyKCV(96, 0x26);
//    }
//
//    public void L23016() {
//        String retValue = StringUtil.byte2HexStr(My23getKeyKCV(99, 0x02));
//        this.printMsgTool("value = "+retValue);
//    }
//
//
    public void L03001() {
        loadWorkKey();
        int keyid = 99;

        Bundle param = new Bundle();
        byte[] pinLimit = {0, 4, 6};
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putByteArray("displayKeyValue", null);

        List<PinKeyCoorInfo> pinKeyInfos = new ArrayList<>();
        PinKeyCoorInfo pinKeyInfos_0 = new PinKeyCoorInfo("btn_0", 0, 500, 240, 400, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_1 = new PinKeyCoorInfo("btn_1", 240, 500, 480, 400, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_2 = new PinKeyCoorInfo("btn_2", 480, 500, 720, 400, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_3 = new PinKeyCoorInfo("btn_3", 0, 400, 240, 300, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_4 = new PinKeyCoorInfo("btn_4", 240, 400, 480, 300, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_5 = new PinKeyCoorInfo("btn_5", 480, 400, 720, 300, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_6 = new PinKeyCoorInfo("btn_6", 0, 300, 240, 200, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_7 = new PinKeyCoorInfo("btn_7", 240, 300, 480, 200, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_8 = new PinKeyCoorInfo("btn_8", 480, 300, 720, 200, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_9 = new PinKeyCoorInfo("btn_9", 240, 200, 480, 100, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_10 = new PinKeyCoorInfo("btn_10", 0, 200, 240, 100, KeyCoorInfo.TYPE_CONF);
        PinKeyCoorInfo pinKeyInfos_11 = new PinKeyCoorInfo("btn_11", 480, 600, 720, 500, KeyCoorInfo.TYPE_CANCEL);
        PinKeyCoorInfo pinKeyInfos_12 = new PinKeyCoorInfo("btn_12", 480, 200, 720, 100, KeyCoorInfo.TYPE_DEL);
        pinKeyInfos.add(pinKeyInfos_0);
        pinKeyInfos.add(pinKeyInfos_1);
        pinKeyInfos.add(pinKeyInfos_2);
        pinKeyInfos.add(pinKeyInfos_3);
        pinKeyInfos.add(pinKeyInfos_4);
        pinKeyInfos.add(pinKeyInfos_5);
        pinKeyInfos.add(pinKeyInfos_6);
        pinKeyInfos.add(pinKeyInfos_7);
        pinKeyInfos.add(pinKeyInfos_8);
        pinKeyInfos.add(pinKeyInfos_9);
        pinKeyInfos.add(pinKeyInfos_10);
        pinKeyInfos.add(pinKeyInfos_11);
        pinKeyInfos.add(pinKeyInfos_12);

        My03initPinInputCustomView(keyid, param, pinKeyInfos, new Mypinpadlistener());
        My04startPinInputCustomView();
    }

    public void L03002() {
//        loadWorkKey();
        int keyid = 99;

        Bundle param = new Bundle();
        byte[] pinLimit = {0, 4, 6};
        param.putByteArray("pinLimit", new byte[]{0,4,6});
        param.putInt("timeout", 60);
        param.putBoolean("isOnline", true);
        param.putString("pan", "6226901508781352");
        param.putInt("desType", 1);//3DES
        param.putByteArray("displayKeyValue", null);
        param.putBoolean("isEpp",true);

        List<PinKeyCoorInfo> pinKeyInfos = new ArrayList<>();
        PinKeyCoorInfo pinKeyInfos_0 = new PinKeyCoorInfo("btn_0", 0, 500, 240, 400, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_1 = new PinKeyCoorInfo("btn_1", 240, 500, 480, 400, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_2 = new PinKeyCoorInfo("btn_2", 480, 500, 720, 400, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_3 = new PinKeyCoorInfo("btn_3", 0, 400, 240, 300, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_4 = new PinKeyCoorInfo("btn_4", 240, 400, 480, 300, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_5 = new PinKeyCoorInfo("btn_5", 480, 400, 720, 300, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_6 = new PinKeyCoorInfo("btn_6", 0, 300, 240, 200, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_7 = new PinKeyCoorInfo("btn_7", 240, 300, 480, 200, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_8 = new PinKeyCoorInfo("btn_8", 480, 300, 720, 200, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_9 = new PinKeyCoorInfo("btn_9", 240, 200, 480, 100, KeyCoorInfo.TYPE_NUM);
        PinKeyCoorInfo pinKeyInfos_10 = new PinKeyCoorInfo("btn_10", 0, 200, 240, 100, KeyCoorInfo.TYPE_CONF);
        PinKeyCoorInfo pinKeyInfos_11 = new PinKeyCoorInfo("btn_11", 480, 600, 720, 500, KeyCoorInfo.TYPE_CANCEL);
        PinKeyCoorInfo pinKeyInfos_12 = new PinKeyCoorInfo("btn_12", 480, 200, 720, 100, KeyCoorInfo.TYPE_DEL);
        pinKeyInfos.add(pinKeyInfos_0);
        pinKeyInfos.add(pinKeyInfos_1);
        pinKeyInfos.add(pinKeyInfos_2);
        pinKeyInfos.add(pinKeyInfos_3);
        pinKeyInfos.add(pinKeyInfos_4);
        pinKeyInfos.add(pinKeyInfos_5);
        pinKeyInfos.add(pinKeyInfos_6);
        pinKeyInfos.add(pinKeyInfos_7);
        pinKeyInfos.add(pinKeyInfos_8);
        pinKeyInfos.add(pinKeyInfos_9);
        pinKeyInfos.add(pinKeyInfos_10);
        pinKeyInfos.add(pinKeyInfos_11);
        pinKeyInfos.add(pinKeyInfos_12);

        My03initPinInputCustomView(keyid, param, pinKeyInfos, new Mypinpadlistener());
        My04startPinInputCustomView();
    }

    public void L05001() {
        logUtils.addCaseLog("L05001");
        My05endPinInputCustomView();
    }


//    public void L27001() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "0000000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My27calculateDataEx(2, 6, key, data, initVec));
//
//        if (retValue.equals("368E34E391A1A199C2CED460E720BF81")) {
//            logUtils.addCaseLog("L27001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L27001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L27001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L27001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L27002() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "0101010101010101";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My27calculateDataEx(2, 6, key, data, initVec));
//
//        if (retValue.equals("3729911561E9C00578F55CD0D894C769")) {
//            logUtils.addCaseLog("L27002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L27002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L27002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L27002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L27003() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "0000000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My27calculateDataEx(3, 6, key, data, initVec));
//        if (retValue.equals("4BDE44896C79D2BA030E237CC8C2B9D4")) {
//            logUtils.addCaseLog("L27003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L27003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L27003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L27003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L27004() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "0101010101010101";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My27calculateDataEx(3, 6, key, data, initVec));
//        if (retValue.equals("4ADF45886D78D3BB030E237CC8C2B9D4")) {
//            logUtils.addCaseLog("L27004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L27004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L27004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L27004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L27005() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My27calculateDataEx(2, 6, key, data, null));
//        if (retValue.equals("368E34E391A1A199C2CED460E720BF81")) {
//            logUtils.addCaseLog("L27005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L27005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L27005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L27005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L27006() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My27calculateDataEx(3, 6, key, data, initVec));
//        if (retValue.equals("4BDE44896C79D2BA030E237CC8C2B9D4")) {
//            logUtils.addCaseLog("L27006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L27006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L27006 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L27006 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L27007() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "00000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        My27calculateDataEx(2, 6, key, data, initVec);
//    }
//
//    public void L27008() {
//        String keyStr = "10101080101010102525252525252528";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "000000000000000011";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        My27calculateDataEx(3, 6, key, data, initVec);
//    }
//
//    public void L28001() {//卡号6226901508781352
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        String retValue = StringUtil.byte2HexStr(My28encryptPinFormat0(10, 1, cardNumber, passwd));
//
//        if (retValue.equals("148573F0630D22AC")) {
//            logUtils.addCaseLog("L28001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L28001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L28001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L28001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L28002() {//卡号6226901508781352123
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32, 0x31, 0x32, 0x33};
//        String passwd = "1234";
//        String retValue = StringUtil.byte2HexStr(My28encryptPinFormat0(10, 1, cardNumber, passwd));
//
//        if (retValue.equals("8267FE2BC6BE1361")) {
//            logUtils.addCaseLog("L28002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L28002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L28002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L28002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L28003() {//卡号6226901508781352
//        L12023();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        My28encryptPinFormat0(99, 2, cardNumber, passwd);
//    }
//
//    public void L28004() {//卡号6226901508781352123
//        L12023();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32, 0x31, 0x32, 0x33};
//        String passwd = "1234";
//        My28encryptPinFormat0(99, 2, cardNumber, passwd);
//    }
//
//    public void L28005() {//卡号6226901508781352
//        loadDukptKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        My28encryptPinFormat0(0, 4, cardNumber, passwd);
//        My28encryptPinFormat0(0, 4, cardNumber, passwd);
//    }
//
//    public void L28006() {//卡号6226901508781352123
//        loadDukptKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32, 0x31, 0x32, 0x33};
//        String passwd = "1234";
//        My28encryptPinFormat0(0, 4, cardNumber, passwd);
//    }
//
//    public void L28007() {//卡号6226901508781352
//        L12023();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        My28encryptPinFormat0(88, 2, cardNumber, passwd);
//    }
//
//    public void L28008() {//卡号6226901508781352
//        L12023();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        My28encryptPinFormat0(-1, 2, cardNumber, passwd);
//    }
//
//    public void L28009() {//卡号6226901508781352
//        L12023();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        My28encryptPinFormat0(100, 2, cardNumber, passwd);
//    }
//
//    public void L28010() {//卡号6226901508781352
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        String retValue = StringUtil.byte2HexStr(My28encryptPinFormat0(10, 0, cardNumber, passwd));
//        if (retValue.equals("148573F0630D22AC")) {
//            logUtils.addCaseLog("L28010 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L28010 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L28010 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L28010 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L28011() {//卡号6226901508781352
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "123456";
//        String retValue = StringUtil.byte2HexStr(My28encryptPinFormat0(10, 5, cardNumber, passwd));
//        if (retValue.equals("148573F0630D22AC")) {
//            logUtils.addCaseLog("L28011 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L28011 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L28011 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L28011 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L28012() {//卡号6226901508781352
//        loadWorkKey();
//        String passwd = "123456";
//        My28encryptPinFormat0(10, 1, null, passwd);
//    }
//
//    public void L28013() {//卡号6226901508781352
//        loadWorkKey();
//        byte[] cardNumber = {};
//        String passwd = "123456";
//        My28encryptPinFormat0(10, 1, cardNumber, passwd);
//    }
//
//    public void L28014() {//卡号622690150878
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38};
//        String passwd = "123456";
//        My28encryptPinFormat0(10, 1, cardNumber, passwd);
//    }
//
//    public void L28015() {//卡号622690150878
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36};
//        String passwd = "123456";
//        My28encryptPinFormat0(10, 1, cardNumber, passwd);
//    }
//
//    public void L28016() {//卡号6226901508781352
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        My28encryptPinFormat0(10, 1, cardNumber, null);
//    }
//
//    public void L28017() {//卡号6226901508781352
//        loadWorkKey();
//        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
//        String passwd = "";
//        My28encryptPinFormat0(10, 1, cardNumber, passwd);
//    }
//
//    public void L29001() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 1, 0, data, null));
//        if (retValue.equals("981F2E430C10AD3703DB57D1C3659AB5")) {
//            logUtils.addCaseLog("L06001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L06001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29002() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 2, 0, data, null));
//        if (retValue.equals("981F2E430C10AD375391191870EB7E5A")) {
//            logUtils.addCaseLog("L29002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29003() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 1, 1, data, null));
//        if (retValue.equals("4B60022F12CBBDC6549094627129B092")) {
//            logUtils.addCaseLog("L29003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29004() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 2, 1, data, null));
//        if (retValue.equals("4B60022F12CBBDC6D61216E0F3AB3210")) {
//            logUtils.addCaseLog("L29004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29005() {
//        L12024();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 3, 1, 0, data, null));
//        if (retValue.equals("45F04FABEF0E369FC4E523EF0754D493")) {
//            logUtils.addCaseLog("L29005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29006() {
//        L12024();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 3, 2, 0, data, null));
//        if (retValue.equals("45F04FABEF0E369FC4E523EF0754D493")) {
//            logUtils.addCaseLog("L29006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29006 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29006 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29007() {
//        L12024();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 3, 1, 1, data, null));
//        if (retValue.equals("6A6C46799DBD5E5D1587219E5900080C")) {
//            logUtils.addCaseLog("L29007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29008() {
//        L12024();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 3, 2, 1, data, null));
//        if (retValue.equals("6A6C46799DBD5E5D1587219E5900080C")) {
//            logUtils.addCaseLog("L29008 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29008 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29008 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29008 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29009() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(-1, 1, 1, 0, data, null);
//    }
//
//    public void L29010() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(100, 1, 1, 0, data, null);
//    }
//
//    public void L29011() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(97, 1, 1, 0, data, null);
//    }
//
//    public void L29012() {
//        L12024();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(97, 3, 1, 0, data, null);
//    }
//
//    public void L29013() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 0, 1, 0, data, null);
//    }
//
//    public void L29014() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 4, 1, 0, data, null);
//    }
//
//    public void L29015() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 1, 0, 0, data, null);
//    }
//
//    public void L29016() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 1, 3, 0, data, null);
//    }
//
//    public void L29017() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 1, 1, -1, data, null);
//    }
//
//    public void L29018() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 1, 1, 2, data, null);
//    }
//
//    public void L29019() {
//        L05055();
//        My29calculateByDataKey(99, 1, 1, 0, null, null);
//    }
//
//    public void L29020() {
//        L05055();
//        String dataStr = "";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        My29calculateByDataKey(99, 1, 1, 0, data, null);
//    }
//
//    public void L29021() {
//        L05055();
//        String dataStr = "828282828282828238383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 1, 0, data, null));
//        if (retValue.equals("981F2E430C10AD3780DB0B6DFF29CD8A")) {
//            logUtils.addCaseLog("L29021 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29021 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29021 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29021 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29022() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "0000000000000000";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 2, 0, data, initVec));
//        if (retValue.equals("981F2E430C10AD375391191870EB7E5A")) {
//            logUtils.addCaseLog("L29022 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29022 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29022 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29022 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29023() {
//        L12024();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "01010101010101010101010101010101";//不足16字节，补0
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 3, 2, 0, data, initVec));
//        if (retValue.equals("B71EF5FB42614EA967B4FD25BF0CF3C6")) {
//            logUtils.addCaseLog("L29023 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29023 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29023 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29023 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29024() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 2, 0, data, initVec));
//        if (retValue.equals("981F2E430C10AD375391191870EB7E5A")) {
//            logUtils.addCaseLog("L29024 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29024 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29024 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29024 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L29025() {
//        L05055();
//        String dataStr = "82828282828282823838383838383838";
//        byte[] data = BCDDecode.str2Bcd(dataStr);
//        String initVecStr = "01010101010101";
//        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
//        String retValue = StringUtil.byte2HexStr(My29calculateByDataKey(99, 1, 2, 0, data, initVec));
//        if (retValue.equals("C135B33CD6BCFA33C0D8AB1C8ED2ED5D")) {
//            logUtils.addCaseLog("L29025 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L29025 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L29025 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L29025 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30001() {
//        L20001();
//        String keyStr = "82828282828382823838383838383838";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 1, null, null);
//        if (retValue) {
//            logUtils.addCaseLog("L30001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30002() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("824CF43A1F9447C8");
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 1, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30003() {
//        String tekKeyString = "31313131313131313232323232323279";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);
//        //AES明文
//        My20loadTEKWithAlgorithmType(0, tekKey, (byte) 6, null);
//
//        String keyStr = "82828122828382823838340838383838";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        String kcvStr = "ECE6411AAA9B81B8";
//        byte[] checkvalue = BCDDecode.str2Bcd(kcvStr);
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 5, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30004() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828283408382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("9156F80A4DE57C7F");
//        Boolean retValue = My30loadEncryptMainKeyEX(19, key, 0x81, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30005() {
//        String tekKeyString = "31313131313131313232323232323279";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);
//        //AES明文
//        My20loadTEKWithAlgorithmType(0, tekKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382824938340838383838");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("35B602B01EA81C0C");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x85, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30006() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("81828283408382823837363838383838");
//        byte[] initVec = BCDDecode.str2Bcd("01010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("667DA436D4C1B25A");
//        Boolean retValue = My30loadEncryptMainKeyEX(19, key, 0x81, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30006 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30006 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30007() {
//        String tekKeyString = "31313131313131313232323232323279";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);
//        //AES明文
//        My20loadTEKWithAlgorithmType(0, tekKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382888938340838383838");
//        byte[] initVec = BCDDecode.str2Bcd("01010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("E51BA9AAD4A7EF23");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x85, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30008() {
//        String tekKeyString = "31313131313131313232323232323279";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);
//        //AES明文
//        My20loadTEKWithAlgorithmType(0, tekKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382888938340838383838");
//        byte[] initVec = BCDDecode.str2Bcd("010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("E51BA9AAD4A7EF23");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x85, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30008 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30008 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30008 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30008 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30010() {
//        byte[] mkey = BCDDecode.str2Bcd("03030303030303030202020202020202");
//        My04loadPlainMasterKey(10, mkey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382888938340830083808");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 10);
//        byte[] checkvalue = BCDDecode.str2Bcd("C0850BF9380F6B26");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 1, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30010 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30010 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30010 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30010 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30011() {//AES明文主密钥
//        byte[] mKey = BCDDecode.str2Bcd("13131313131313133131313131313131");
//        loadPlainMasterKey(99, mKey, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82000122828382888938340830083808");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 99);
//        byte[] checkvalue = BCDDecode.str2Bcd("F25673BF2B347AD7");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 5, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30011 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30011 Case results：" + retValue + "，execute uccss！");
//        this.printMsgTool("L30011 Case results：" + retValue + "，execute uccss！");
//    }
//
//    public void L30012() {
//        byte[] mkey = BCDDecode.str2Bcd("03030303030303030202020202020202");
//        My04loadPlainMasterKey(10, mkey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382888938340830083808");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 10);
//        byte[] checkvalue = BCDDecode.str2Bcd("1561017F6AEFAF49");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x81, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30012 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30012 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30012 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30012 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30013() {//AES明文主密钥
//        byte[] mKey = BCDDecode.str2Bcd("13131313131313133131313131313131");
//        loadPlainMasterKey(99, mKey, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82000122828382888938340830083708");
//        byte[] initVec = BCDDecode.str2Bcd("01010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 99);
//        byte[] checkvalue = BCDDecode.str2Bcd("55210AF2ABCA641C");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x85, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30013 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30013 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30013 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30013 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30014() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("124CF43A1F9447C8");
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 1, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30014 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30015() {
//        String tekKeyString = "31313131313131313232323232323279";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);
//        //AES明文
//        My20loadTEKWithAlgorithmType(0, tekKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382824938340838383838");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("15B602B01EA81C0C");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x85, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30015 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30015 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30015 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30015 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30016() {//AES明文主密钥
//        byte[] mKey = BCDDecode.str2Bcd("13131313131313133131313131313131");
//        loadPlainMasterKey(99, mKey, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82000122828382888938340830083808");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 99);
//        byte[] checkvalue = BCDDecode.str2Bcd("125673BF2B347AD7");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 5, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30016 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30017() {
//        byte[] mkey = BCDDecode.str2Bcd("03030303030303030202020202020202");
//        My04loadPlainMasterKey(10, mkey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382888938340830083808");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 10);
//        byte[] checkvalue = BCDDecode.str2Bcd("1561017F6AEFAF48");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x81, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30017 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30017 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30017 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30017 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30018() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 1);
//        byte[] checkvalue = BCDDecode.str2Bcd("824CF43A1F9447C8");
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 1, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30018 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30018 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30018 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30018 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30019() {
//        byte[] mkey = BCDDecode.str2Bcd("03030303030303030202020202020202");
//        My04loadPlainMasterKey(10, mkey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382888938340830083808");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 11);
//        byte[] checkvalue = BCDDecode.str2Bcd("1561017F6AEFAF49");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x81, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30019 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30019 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30019 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30019 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30020() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("824CF43A1F9447C8");
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 0, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30020 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30020 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30020 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30020 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30021() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);//3DES传输密钥
//
//        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        byte[] checkvalue = BCDDecode.str2Bcd("824CF43A1F9447C8");
//        Boolean retValue = My30loadEncryptMainKeyEX(99, key, 0x86, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30021 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L30021 Case results：" + retValue + "，execute FailedS！");
//            return;
//        }
//        logUtils.addCaseLog("L30021 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L30021 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L30022() {
//        String tekKeyString = "31313131313131313232323232323279";
//        byte[] tekKey = BCDDecode.str2Bcd(tekKeyString);
//        //AES明文
//        My20loadTEKWithAlgorithmType(0, tekKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828122828382824938340838383838");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putBoolean("isMasterEncMasterMode", false);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 0x85, null, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30022 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30022 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30022 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30022 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L30023() {//AES明文主密钥
//        byte[] mKey = BCDDecode.str2Bcd("13131313131313133131313131313131");
//        loadPlainMasterKey(99, mKey, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82000122828382888938340830083808");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", null);
//        extend.putBoolean("isMasterEncMasterMode", true);
//        extend.putInt("decryptKeyIndex", 99);
//        byte[] checkvalue = BCDDecode.str2Bcd("");
//        Boolean retValue = My30loadEncryptMainKeyEX(0, key, 5, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L30023 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L30023 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L30023 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L30023 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31001() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        String keyStr = "82828285078382823838340838383838";
//        byte[] key = BCDDecode.str2Bcd(keyStr);
//        String kcvStr = "97911CE4DF53AEC6";
//        byte[] checkvalue = BCDDecode.str2Bcd(kcvStr);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, checkvalue, null);
//        if (retValue) {
//            logUtils.addCaseLog("L31001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31001 Case results：" + retValue + "，execute Failed！");
//
//    }
//
//    public void L31002() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31003() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285073882823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("B236A076A10B0322");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(2, 99, 0, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31004() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285073882823838340838377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("211FE3A974B435BA");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(3, 99, 10, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31005() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823838340838377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("75C1071C9C9B266C");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 0, 99, 1, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31006() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882023838340838377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("807643451223D4CF");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(2, 0, 99, 1, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31006 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31006 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31007() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828280073882823838340838377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("B851F0315FDF68B2");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(3, 0, 99, 1, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31008() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("89815AE7D566F7E9");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(9, 99, 99, 3, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31008 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31008 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31008 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31008 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31009() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823738340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("9FB76EA0A8BB83BF");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(10, 99, 99, 3, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31009 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31009 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31009 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31009 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31010() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073881233838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("A92C57573EEF4CB2");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(11, 99, 99, 3, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31010 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31010 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31010 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31010 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31011() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("12828285073882823838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("0BD7ED71BCEF02F7");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(9, 10, 99, 5, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31011 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31011 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31011 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31011 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31012() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("14828285073882823838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("91A6A9E1BE955DB8");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(10, 10, 99, 5, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31012 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31012 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31012 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31012 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31013() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("19828285073882823838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("32BF4EADBC406128");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(11, 10, 99, 5, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31013 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31013 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31013 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31013 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31014() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("B4D9741853E23F78");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", null);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0x80, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31014 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31014 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31014 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31014 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31015() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078888823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("CC0A87D474B21926");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(2, 99, 99, 0x80, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31015 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31015 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31015 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31015 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31016() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823830340808383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("18086A4F01B11399");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(3, 99, 99, 0x80, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31016 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31016 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31016 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31016 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31017() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078370823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("0C263CEE0D025E0D");
//        byte[] initVec = BCDDecode.str2Bcd("01010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(1, 0, 99, 0x81, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31017 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31017 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31017 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31017 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31018() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078370823765340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("EE1DCE87432DDE54");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(2, 0, 99, 0x81, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31018 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31018 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31018 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31018 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31019() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285012370823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("EE62218D37704D1F");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(3, 0, 99, 0x81, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31019 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31019 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31019 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31019 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31020() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("95327D3041D23C98");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(9, 99, 98, 0x83, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31020 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31020 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31020 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31020 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31021() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("32828285071234823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("39762DF52940ADB0");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", null);
//        Boolean retValue = My31loadWorkKeyEX(10, 99, 98, 0x83, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31021 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31021 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31021 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31021 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31022() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("42828285071234823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("C6AB611FB50BFE22");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(11, 99, 98, 0x83, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31022 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31022 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31022 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31022 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31023() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234823838340838383333");
//        byte[] checkvalue = BCDDecode.str2Bcd("1BB0CF62B5B61E4C");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(9, 10, 97, 0x85, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31023 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31023 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31023 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31023 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31024() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234878938340838383333");
//        byte[] checkvalue = BCDDecode.str2Bcd("A730D69A7B707539");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", null);
//        Boolean retValue = My31loadWorkKeyEX(10, 10, 97, 0x85, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31024 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31024 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31024 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31024 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31025() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234823838340838383444");
//        byte[] checkvalue = BCDDecode.str2Bcd("5B208F2638B6E71B");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(11, 10, 97, 0x85, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31025 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31025 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31025 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31025 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31026() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(0, 99, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31026 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31026 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31026 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31026 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31027() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(12, 99, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31027 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31027 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31027 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31027 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31028() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, -1, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31028 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31028 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31028 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31028 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31029() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 100, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31029 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31029 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31029 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31029 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31030() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285073882823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("B236A076A10B0322");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(2, 98, 0, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31030 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31030 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31030 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31030 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31031() {
//        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
//        My02loadTEK(0, tekey, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823838340838377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("75C1071C9C9B266C");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(2, 1, 99, 1, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31031 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31031 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31031 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31031 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31032() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("95327D3041D23C98");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(9, 90, 98, 0x83, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31032 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31032 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31032 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31032 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31033() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234823838340838383333");
//        byte[] checkvalue = BCDDecode.str2Bcd("1BB0CF62B5B61E4C");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(11, 33, 97, 0x85, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31033 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31033 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31033 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31033 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31034() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, -1, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31034 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31034 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31034 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31034 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31035() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 100, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31035 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31035 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31035 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31035 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31036() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, -1, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31036 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31036 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31036 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31036 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31037() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0x86, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31037 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31037 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31037 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31037 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31038() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, null, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31038 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31038 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31038 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31038 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31039() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31039 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31039 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31039 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31039 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31040() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("828282850783828238383408383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31040 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31040 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31040 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31040 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31041() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = new byte[2048];
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, null, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31041 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31041 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31041 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31041 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31042() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = new byte[2049];
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, null, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31042 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31042 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31042 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31042 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31043() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823838340818377838");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(11, 99, 99, 3, key, null, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31043 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31043 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31043 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31043 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31044() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(11, 99, 99, 3, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31044 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31044 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31044 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31044 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L31045() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("92828285073882823838340818377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("89815AE7D566F7E1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(11, 99, 99, 3, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31045 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31045 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31045 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31045 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31046() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285073882823838340838377838");
//        byte[] checkvalue = BCDDecode.str2Bcd("211FE3A974B435B1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My31loadWorkKeyEX(3, 99, 10, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31046 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31046 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31046 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31046 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31047() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("B4D9741853E23F71");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(3, 99, 99, 0x80, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31047 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31047 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31047 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31047 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31048() {//AES明文传输密钥
//        byte[] teKey = BCDDecode.str2Bcd("31313131313131313232323232323235");
//        My20loadTEKWithAlgorithmType(10, teKey, (byte) 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285071234823838340838383333");
//        byte[] checkvalue = BCDDecode.str2Bcd("1BB0CF62B5B61E41");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(9, 10, 97, 0x85, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31048 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31048 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31048 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31048 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31049() {
//        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
//        My04loadPlainMasterKey(99, mKey, null);
//
//        byte[] key = BCDDecode.str2Bcd("82828285078382823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("97911CE4DF53AEC6");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        Boolean retValue = My31loadWorkKeyEX(1, 99, 99, 0, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31049 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31049 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31049 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31049 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L31050() {//AES明文主密钥
//        byte[] Key = BCDDecode.str2Bcd("92929292929292923030303030303030");
//        loadPlainMasterKey(99, Key, 6, null);
//
//        byte[] key = BCDDecode.str2Bcd("42828285071234823838340838383838");
//        byte[] checkvalue = BCDDecode.str2Bcd("C6AB611FB50BFE22");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My31loadWorkKeyEX(11, 99, 98, 0x83, key, checkvalue, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L31050 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L31050 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L31050 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L31050 Case results：" + retValue + "，execute success！");
//    }
//
//    public void clearkey_demo() {
//        //下载所有类型密钥
//        L22001();
//        L05067();
//        loadWorkKey();
//        L05069();
//        L12022();
//        L12023();
//        L12024();
//        L20002();
//        L20005();
//    }
//
//    public void L32001() {
//        clearkey_demo();
//        My01isKeyExist(0, 0);
//        Boolean retValue = My32clearKey(0, 0x00);
//        My01isKeyExist(0, 0);
//        if (retValue) {
//            logUtils.addCaseLog("L32001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32002() {
//        clearkey_demo();
//        My01isKeyExist(8, 99);
//        Boolean retValue = My32clearKey(99, 0x02);
//        My01isKeyExist(8, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L32002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32003() {
//        clearkey_demo();
//        My01isKeyExist(2, 10);
//        Boolean retValue = My32clearKey(10, 0x10);
//        My01isKeyExist(2, 10);
//        if (retValue) {
//            logUtils.addCaseLog("L32003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32004() {
//        clearkey_demo();
//        My01isKeyExist(10, 99);
//        Boolean retValue = My32clearKey(99, 0x12);
//        My01isKeyExist(10, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L32004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32005() {
//        clearkey_demo();
//        My01isKeyExist(1, 10);
//        Boolean retValue = My32clearKey(10, 0x20);
//        My01isKeyExist(1, 10);
//        if (retValue) {
//            logUtils.addCaseLog("L32005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32006() {
//        clearkey_demo();
//        My01isKeyExist(9, 99);
//        Boolean retValue = My32clearKey(99, 0x22);
//        My01isKeyExist(9, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L32006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32006 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32006 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32006 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32007() {
//        clearkey_demo();
//        My01isKeyExist(3, 0);
//        Boolean retValue = My32clearKey(0, 0x30);
//        My01isKeyExist(3, 0);
//        if (retValue) {
//            logUtils.addCaseLog("L32007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32008() {
//        clearkey_demo();
//        My01isKeyExist(11, 99);
//        Boolean retValue = My32clearKey(99, 0x32);
//        My01isKeyExist(11, 99);
//        if (retValue) {
//            logUtils.addCaseLog("L32008 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32008 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32008 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32008 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32009() {
//        clearkey_demo();
//        Boolean retValue = My32clearKey(-1, 0x00);
//        if (retValue) {
//            logUtils.addCaseLog("L32009 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L32009 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L32009 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L32009 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L32010() {
//        clearkey_demo();
//        Boolean retValue = My32clearKey(100, 0x00);
//        if (retValue) {
//            logUtils.addCaseLog("L32010 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L32010 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L32010 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L32010 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L32011() {
//        clearkey_demo();
//        Boolean retValue = My32clearKey(0, -1);
//        if (retValue) {
//            logUtils.addCaseLog("L32011 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L32011 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L32011 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L32011 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L32012() {
//        clearkey_demo();
//        Boolean retValue = My32clearKey(0, 51);
//        if (retValue) {
//            logUtils.addCaseLog("L32012 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L32012 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L32012 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L32012 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L32013() {
//        clearkey_demo();
//        Boolean retValue = My32clearKey(71, 0x00);
//        if (retValue) {
//            logUtils.addCaseLog("L32013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L32013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L32013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L32013 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L32014() {
//        Boolean retValue = My32clearKey(0, 0x00);
//        if (retValue) {
//            logUtils.addCaseLog("L32014 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32014 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32014 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32014 Case results：" + retValue + "，execute Failed！");
//    }
//
//    /*public void L32015() {
//
////        FormatKeyArea();
//    }
//
//    public void L32016() {
//        clearkey_demo();
//        My01isKeyExist(13, 2);
//        Boolean retValue = My32clearKey(2, 0x50);
//        My01isKeyExist(13, 2);
//        if (retValue) {
//            logUtils.addCaseLog("L32016 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32016 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32016 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32016 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L32017() {
//        clearkey_demo();
//        My01isKeyExist(15, 10);
//        Boolean retValue = My32clearKey(10, 0x51);
//        My01isKeyExist(15, 10);
//        if (retValue) {
//            logUtils.addCaseLog("L32016 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L32016 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L32016 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L32016 Case results：" + retValue + "，execute Failed！");
//    }*/
//    public void L33001() {
//        loadTEK();
//        String ksnString = "FFFF9876543210E00001";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        String dukptkeyStr = "34343434343434343535353535353535";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyStr);
//        Boolean retValue = My33loadDukptKeyEX(4, ksn, dukptkey, null, null);
//        if (retValue) {
//            logUtils.addCaseLog("L33001 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L33001 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L33001 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L33001 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L33002() {
//        loadTEK();
//        String ksnString = "FFFF9876543210E00001";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        String dukptkeyStr = "34343434343434343535353535353535";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyStr);
//        String kcvStr = "F39941C42F44E374";
//        byte[] kcv = BCDDecode.str2Bcd(kcvStr);
//        Bundle extend = new Bundle();
//        extend.putBoolean("loadPlainKey", false);
//        Boolean retValue = My33loadDukptKeyEX(4, ksn, dukptkey, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L33002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L33002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L33002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L33002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L33003() {
//        loadTEK();
//        String ksnString = "FFFF9876543210E00001";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        String dukptkeyStr = "34343434343434343535353535353535";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyStr);
//        String kcvStr = "D2DB51F1D2013A63";
//        byte[] kcv = BCDDecode.str2Bcd(kcvStr);
//        Bundle extend = new Bundle();
//        extend.putBoolean("loadPlainKey", true);
//        Boolean retValue = My33loadDukptKeyEX(0, ksn, dukptkey, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L33003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L33003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L33003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L33003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L33004() {
//        loadTEK();
//        String ksnString = "FFFF9876543210E00001";
//        byte[] ksn = BCDDecode.str2Bcd(ksnString);
//        String dukptkeyStr = "34343434343434343535353535353535";
//        byte[] dukptkey = BCDDecode.str2Bcd(dukptkeyStr);
//        String kcvStr = "00DB51F1D2013A63";
//        byte[] kcv = BCDDecode.str2Bcd(kcvStr);
//        Bundle extend = new Bundle();
//        extend.putBoolean("loadPlainKey", true);
//        Boolean retValue = My33loadDukptKeyEX(0, ksn, dukptkey, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L33004 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L33004 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L33004 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L33004 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34001() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Boolean retValue = My34loadTEKEX(50, key, (byte) 2, kcv, null);
//        if (retValue) {
//            logUtils.addCaseLog("L34001 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34001 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34001 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34001 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34002() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34002 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34002 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34002 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34002 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34003() {
//        L34002();
//        byte[] key = BCDDecode.str2Bcd("41414141414140415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("BBAF9CC6774F9904");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(99, key, (byte) 1, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34003 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34003 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34003 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34003 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34004() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415522525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("8A5033F739981739");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(10, key, (byte) 6, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34004 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34004 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34004 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34004 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34005() {
//        L34004();
//        byte[] key = BCDDecode.str2Bcd("01414141414141415522525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("917D7755CCADE124");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putInt("decryptKeyIndex", 10);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 5, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34005 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34005 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34005 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34005 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34006() {
//        byte[] key = BCDDecode.str2Bcd("71414141414141415522525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("BAC25DFE140EEC08");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", null);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x82, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34006 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L31050 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L31050 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L31050 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34007() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("3B6A40F0E5C8AB53");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34007 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34007 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34007 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34007 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34008() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525362535252");
//        byte[] kcv = BCDDecode.str2Bcd("1F2A6BE41F658940");//KCV不区分CBC和ECB
//        byte[] initVec = BCDDecode.str2Bcd("01010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34008 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34008 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34008 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34008 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34009() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141416622525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("D4343F0E9A2F874D");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x86, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34009 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34009 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34009 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34009 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34010() {
//        L34009();
//        byte[] key = BCDDecode.str2Bcd("41414141414141416622525252525000");
//        byte[] kcv = BCDDecode.str2Bcd("B5CA2854DD5F193D");
//        byte[] initVec = BCDDecode.str2Bcd("01010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x85, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34010 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34010 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34010 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34010 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34011() {
//        L34009();
//        byte[] key = BCDDecode.str2Bcd("41414141414141416622525252525000");
//        byte[] kcv = BCDDecode.str2Bcd("78E5636409837757");
//        byte[] initVec = BCDDecode.str2Bcd("010101010101010101010101010101");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x85, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34011 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34011 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34011 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34011 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34012() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(-1, key, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34012 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34012 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34012 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34012 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34013() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(100, key, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34013 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34013 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34013 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34013 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34014() {
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, null, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34014 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34014 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34014 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34014 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34015() {
//        byte[] key = BCDDecode.str2Bcd("");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34015 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34015 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34015 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34015 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34016() {
//        byte[] key = BCDDecode.str2Bcd("414141414141414152525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34016 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34016 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34016 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34016 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34017() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34017 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34017 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34017 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34017 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34018() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x87, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34018 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34018 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34018 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34018 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34019() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C2");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 2, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34019 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34019 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34019 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34019 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34020() {
//        L34004();
//        byte[] key = BCDDecode.str2Bcd("01414141414141415522525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("917D7755CCADE125");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(10, key, (byte) 5, kcv, extend);
//
//        //插入日志
//        VTestLog vTestLog = new VTestLog();
//        vTestLog.setMoudleName("PinpadMoudle");
//        vTestLog.setMethodName("L34020");
//        vTestLog.setBackValue(retValue ? "true" : "false");
//
//        if (retValue) {
//            logUtils.addCaseLog("L34020 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34020 Case results：" + retValue + "，execute Failed！");
//            //vTestLog.setLogContent("L34020 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34020 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34020 Case results：" + retValue + "，execute success！");
//        vTestLog.setLogContent("L34020 Case results：" + retValue + "，execute success！");
//        vTestLog.setResult(retValue ? "success" : "failed");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
//        vTestLog.setCreateTime(format.format(new Date()));
//        VerifoneTestLogDao verifoneTestLogDao = new VerifoneTestLogDao();
//        verifoneTestLogDao.insertLog(vTestLog);
//    }
//
//    public void L34021() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("3B6A40F0E5C8AB50");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34021 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34021 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34021 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34021 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34022() {
//        byte[] key = BCDDecode.str2Bcd("41414141414141416622525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("D4343F0E9A2F8741");
//        byte[] initVec = BCDDecode.str2Bcd("00000000000000000000000000000000");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x86, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34022 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34022 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34022 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34022 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34023() {
//        L34004();
//        byte[] key = BCDDecode.str2Bcd("01414141414141415522525252525252");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", false);
//        extend.putInt("decryptKeyIndex", 10);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 5, null, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34023 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34023 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34023 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34023 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34024() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 0);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34024 Case results：" + retValue + "，execute success！");
//            this.printMsgTool("L34024 Case results：" + retValue + "，execute success！");
//            return;
//        }
//        logUtils.addCaseLog("L34024 Case results：" + retValue + "，execute Failed！");
//        this.printMsgTool("L34024 Case results：" + retValue + "，execute Failed！");
//    }
//
//    public void L34025() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("3B6A40F0E5C8AB53");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", -1);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34025 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34025 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34025 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34025 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34026() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("3B6A40F0E5C8AB53");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 100);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34026 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34026 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34026 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34026 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L34027() {
//        L34006();
//        byte[] key = BCDDecode.str2Bcd("71414141414141415500525252525252");
//        byte[] kcv = BCDDecode.str2Bcd("3B6A40F0E5C8AB53");
//        byte[] initVec = BCDDecode.str2Bcd("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isCBCType", true);
//        extend.putByteArray("initVec", initVec);
//        extend.putInt("decryptKeyIndex", 16);
//        Boolean retValue = My34loadTEKEX(0, key, (byte) 0x81, kcv, extend);
//        if (retValue) {
//            logUtils.addCaseLog("L34027 Case results：" + retValue + "，execute Failed！");
//            this.printMsgTool("L34027 Case results：" + retValue + "，execute Failed！");
//            return;
//        }
//        logUtils.addCaseLog("L34027 Case results：" + retValue + "，execute success！");
//        this.printMsgTool("L34027 Case results：" + retValue + "，execute success！");
//    }
//
//    public void L35001() {
//        My35getRandom((byte) 0);
//    }
//
//    /**
//     *
//     */
//    public void L35002() {
//
//        for (int i = 1; i <= 127; i++) {
////       My35getRandom((byte)i);
//
//            String retValue = StringUtil.byte2HexStr(My35getRandom((byte) i));
//            logUtils.addCaseLog("L35002 Case results：" + "i=" + i + "时,随机数=" + retValue);
//            this.printMsgTool("L35002 Case results：" + "i=" + i + "时,随机数=" + retValue);
//
//
//        }
//    }
//
//    public void L35003() {
//        My35getRandom((byte) 256);
//    }
//
//
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

            getDukptKsn(0);
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            Log.i(TAG, "onError:" + errorCode);
            Message msg = new Message();
            String errString = imksk.getLastError();
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

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.PinpadMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "execute 完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }


//    public void L36_AUTO() {
//
//        try {
//            //通过类文件获取类对象
//            Class aClass = Class.forName("moudles.PinpadMoudle");
//            this.printMsgTool("-------Pinpad模块------");
//            this.printMsgTool("自动化测试案例开始执行");
//            Method[] methods = aClass.getDeclaredMethods();
//            for (Method method : methods) {
//                if (method.getName().startsWith("L") && !method.getName().equals("L01013") && !method.getName().equals("L01014") && !method.getName().equals("L35_AUTO")) {
//                    this.printMsgTool(method.getName() + "方法开始执行......");
//                    method.invoke(this);
//                    this.printMsgTool(method.getName() + "方法结束执行！！！");
//                    Thread.sleep(1000);
//                }
//            }
//            this.printMsgTool("自动化测试execute 完毕");
//            logUtils.addCaseLog("自动化测试execute 完毕");
//            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D08018();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void L37001() {
//        //测试X990Pinput键盘, 输入pan
//        Bundle param = new Bundle();
//        param.putInt("timeout", 30);
//        My37startX990PinInput(param);
//    }
//
//    public void L37006() {
//        //测试关闭键盘
//        try {
//            Log.d(TAG, "stopPinInput executed");
//            ix990Pinpad.endInputPin();
//
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void L38001() {
//        //测试寻卡
////        L37001();
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                //同时SearchCard
////                try {
////                    Thread.sleep(6000);
////                    My38searchCard(new mylistener(),100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////
////            }
////        }).start();
//        My38searchCard(new mylistener(), 100);
//    }
//
//    public void L39001() {
//        boolean blRet = My39openSerial();
//
//        logUtils.addCaseLog("open执行返回值:[" + blRet + "]");
//    }
//
//    public void L40001() {
//        boolean blRet;
//        My39openSerial();
//
//        blRet = My40initSerial(115200, 0, 8);
//        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
//    }
//
//    public void L41001() {
//        //write data1
//        byte[] writeData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
//        logUtils.addCaseLog("执行L41001");
//        int i = My41writeSerial(writeData, 5000);
//        Log.d(TAG, "L41001 i = " + i);
//    }
//
//    public void L41002() {
//        byte[] writeData = new byte[]{0x0A, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C};
//        logUtils.addCaseLog("执行L41002");
//        int i = My41writeSerial(writeData, 5000);
//        Log.d(TAG, "L41002 i = " + i);
//    }
//
//    public void L42001() {
//        //测试emv寻卡
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportSmartCard", true);
//        cardOption.putBoolean("supportCTLSCard", true);
//        My42emvCheckCard(cardOption, 30, new MyListener());
//    }
//
//    public void L42002() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportSmartCard", true);
//        cardOption.putBoolean("supportCTLSCard", true);
//        try {
//            iemv.checkCard(cardOption, 30, new MyListener());
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void L43001() {
//        //读
////        My43readSerial();
//        boolean blRet;
//        int readBytes;
//        byte[] readData = new byte[27];
//
//        readBytes = My43readSerial(readData, 27, 10000);
//        Log.d(TAG, "执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
//        Log.d(TAG, "执行read()返回值:[" + readBytes + "]");
//        int i = My41writeSerial(readData, 5000);
//        Log.d(TAG, "发送i = " + i);
//    }
//
//    public void L44001() {
//        My44isUsbSerialConnect();
//    }
//
//    public void L45001() {
//        My45read();
//    }
//
//    public void L46001() {
//        My46write();
//    }
//
//    public void L47001() throws IOException {
//        int ret = My47keyStoreTR34Payload("/sdcard/Download/KRD_030-525-140.json");
//        logUtils.addCaseLog("ret = "+ret);
//    }
//
//
    private void printMsgTool(String msg) {

        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    private class MyListener extends CheckCardListener.Stub {
//        String msg;
//        long startTime = System.currentTimeMillis();
//        boolean needSecondTapScript = false;
//
//        public MyListener() {
//        }
//
//        public MyListener(boolean needSecondTapScript) {
//            this.needSecondTapScript = needSecondTapScript;
//        }
//
//        @Override
//        public void onCardActivate() throws RemoteException {
//            msg = "Card test: successfully waved the card";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("onCardActivate execute Time" + (endTime - startTime));
////            My28turnOff(1);
//            if (needSecondTapScript) {
//                iemv.setIssuerUpdateScript();
//            }
//        }
//
//        @Override
//        public void onCardSwiped(Bundle track) throws RemoteException {
//            msg = "PAN:" + track.getString("PAN") + "\nTRACK1:" + track.getString("TRACK1") + "\nTRACK2:" + track.getString("TRACK2") + "\nTRACK3:" + track.getString("TRACK3") + "\nSERVICE_CODE:" + track.getString("SERVICE_CODE") + "\nEXPIRED_DATE:" + track.getString("EXPIRED_DATE");
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("onCardSwiped execute Time" + (endTime - startTime));
////            My28turnOff(1);
//        }
//
//        @Override
//        public void onCardPowerUp() throws RemoteException {
////            ((MyApplication) context).serviceMoudle.getBeerMoudle().B01006();
//            msg = "Card test: IC card inserted successfully";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("onCardPowerUp execute Time" + (endTime - startTime));
//        }
//
//        @Override
//        public void onTimeout() throws RemoteException {
//            msg = "Card detection: timeout";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("onTimeout execute Time" + (endTime - startTime));
//        }
//
//        @Override
//        public void onError(int error, String message) throws RemoteException {
//            msg = "Check card error code = " + error + " " + message;
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//            long endTime = System.currentTimeMillis();
//            logUtils.addCaseLog("onError execute Time" + (endTime - startTime));
//        }
//    }


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

                @Override

                public void onConfirm(@NonNull byte[] data, boolean isNonePin) throws RemoteException {
                    Log.i(TAG, "luoyi");
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
                    byte[] ksn = getDukptKsn(0);
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


                    Log.i(TAG, "isNonePin:" + isNonePin);
                    Message msg = new Message();
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
//                        logUtils.addCaseLog("PIN: " + StringUtil.byte2HexStr(data));
                    msg.getData().putString("msg", "isNonePin:" + isNonePin + "\nPIN: " + StringUtil.byte2HexStr
                            (data));
                    handler.sendMessage(msg);

                    //Epp锁
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

                    //Epp锁
                    if (countDownLatch!=null){
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    Log.i(TAG, "onError:" + errorCode);
                    Message msg = new Message();
                    String errString = imksk.getLastError();
                    if (errString != null) {
                        Log.i(TAG, "getLastError=" + errString);
                        msg.getData().putString("msg", errString);
                        handler.sendMessage(msg);
                        return;
                    }
                    msg.getData().putString("msg", "onError:" + errorCode);
                    handler.sendMessage(msg);

                    //Epp锁
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
        logUtils.addCaseLog("stopPinInput测试正常");
    }


    private Map My03initPinInputCustomView(int keyId, Bundle param, List<PinKeyCoorInfo> pinKeyInfos, Mypinpadlistener listener) {
        Map initView = null;
        try {
            initView = iPinpad.initPinInputCustomView(keyId, param, pinKeyInfos, listener);
            if (initView == null) {
                logUtils.addCaseLog("Failed to initialize the custom keyboard interface");
                this.printMsgTool("Result：Failed to initialize the custom keyboard interface");
                String errString = imksk.getLastError();
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