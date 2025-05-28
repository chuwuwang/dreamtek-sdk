package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IMKSK;
import com.dreamtek.smartpos.deviceservice.constdefine.ConstIMKSK;
import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtil;
import Utils.LogUtils;
import base.MyApplication;

public class MKSKModule {
    static LogUtils logUtils;
    Context context;
    IMKSK imksk;
    private final String TAG = "MKSKManager";

    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> isKeyExist = new ArrayList<String>();
    ArrayList<String> clearKey = new ArrayList<String>();
    ArrayList<String> loadTEK = new ArrayList<String>();
    ArrayList<String> loadMasterKey = new ArrayList<String>();
    ArrayList<String> loadPlainMasterKey = new ArrayList<String>();
    ArrayList<String> loadSessionKey = new ArrayList<String>();
    ArrayList<String> calculateMAC = new ArrayList<String>();
    ArrayList<String> calculateData = new ArrayList<String>();
    ArrayList<String> calculateBySessionKey = new ArrayList<String>();
    ArrayList<String> calculateByMasterKey = new ArrayList<String>();
    ArrayList<String> getKeyKCV = new ArrayList<String>();
    ArrayList<String> encryptPinFormat0 = new ArrayList<String>();
    ArrayList<String> getRandom = new ArrayList<String>();

    //明文主密钥
    byte[] mKeyCheckValue = {(byte) 0xD2, (byte) 0xB9, (byte) 0x1C, (byte) 0xC5, (byte) 0xA7, (byte) 0x58, (byte) 0xBB, (byte) 0x08};
    String mKeyString = "111111111111111122222222222222221111111111111111";
    byte[] mKey = BCDDecode.str2Bcd(mKeyString);

    //MAC Key
    String macKeyString = "b740ce7c2fb7fcd8808b5f288eecc6b4b740ce7c2fb7fcd8";
    //    String macKeyString = "E2E2F4834C77E7F2E2E2F4834C77E7F2";
    String macKeyCheckValueString = "";
    //用主密钥111111111111111122222222222222221111111111111111对MacKey进行解密的值对8个字节的0进行加密得到checkValue
    byte[] macKey = BCDDecode.str2Bcd(macKeyString);
    byte[] macKeyCheckValue = BCDDecode.str2Bcd(macKeyCheckValueString);

    //TD Key
    String tdKeyString = "42527dea2a1553e1211b0ea0d459076642527dea2a1553e1";
    //    String tdKeyString = "088CAED653BCAAA368FCC0118AD7D337";
    String tdKeyCheckValueString = "A9AA621560D74109";
    // A9AA621560D74109
    byte[] tdKey = BCDDecode.str2Bcd(tdKeyString);
    byte[] tdKeyCheckValue = BCDDecode.str2Bcd(tdKeyCheckValueString);

    //PIN Key
    String pinKeyString = "B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E";
    //,,,B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E
    //    String pinKeyString = "89B07B35A1B3F47E89B07B35A1B3F488";
    String pinKeyCheckValueString = "A677495148115613";
    //    String pinKeyCheckValueString = "C1205D74FCD48B50";
    byte[] pinKey = BCDDecode.str2Bcd(pinKeyString);
    byte[] pinKeyCheckValue = BCDDecode.str2Bcd(pinKeyCheckValueString);

    public MKSKModule(Context context, IMKSK imksk) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.imksk = imksk;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.MKSKModule");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    if (i.getName().startsWith("MKSK01")) {
                        isKeyExist.add(i.getName());
                    } else if (i.getName().startsWith("MKSK02")) {
                        clearKey.add(i.getName());
                    } else if (i.getName().startsWith("MKSK03")) {
                        loadTEK.add(i.getName());
                    } else if (i.getName().startsWith("MKSK04")) {
                        loadMasterKey.add(i.getName());
                    } else if (i.getName().startsWith("MKSK05")) {
                        loadPlainMasterKey.add(i.getName());
                    } else if (i.getName().startsWith("MKSK06")) {
                        loadSessionKey.add(i.getName());
                    } else if (i.getName().startsWith("MKSK07")) {
                        calculateMAC.add(i.getName());
                    } else if (i.getName().startsWith("MKSK08")) {
                        calculateData.add(i.getName());
                    } else if (i.getName().startsWith("MKSK09")) {
                        calculateBySessionKey.add(i.getName());
                    } else if (i.getName().startsWith("MKSK10")) {
                        calculateByMasterKey.add(i.getName());
                    } else if (i.getName().startsWith("MKSK11")) {
                        getKeyKCV.add(i.getName());
                    } else if (i.getName().startsWith("MKSK12")) {
                        encryptPinFormat0.add(i.getName());
                    } else if (i.getName().startsWith("MKSK13")) {
                        getRandom.add(i.getName());
                    }
                }
                caseNames.add(isKeyExist);
                caseNames.add(clearKey);
                caseNames.add(loadTEK);
                caseNames.add(loadMasterKey);
                caseNames.add(loadPlainMasterKey);
                caseNames.add(loadSessionKey);
                caseNames.add(calculateMAC);
                caseNames.add(calculateData);
                caseNames.add(calculateBySessionKey);
                caseNames.add(calculateByMasterKey);
                caseNames.add(getKeyKCV);
                caseNames.add(encryptPinFormat0);
                caseNames.add(getRandom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void MKSK01001() {
        int keyId = 0;
        int keyType = ConstIMKSK.isKeyExist.keyType.Master_Key_3DES;
        boolean retValue = My01isKeyExist(keyType, keyId);
        if (retValue) {
            logUtils.addCaseLog("MKSK01001 case results：" + retValue + "，execute success！");
            this.printMsgTool("MKSK01001 case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("MKSK01001 case results：" + retValue + "，execute Failed！");
        this.printMsgTool("MKSK01001 case results：" + retValue + "，execute Failed！");
    }

    public void MKSK02001() {
        int keyId = 0;
        int keyType = ConstIMKSK.isKeyExist.keyType.Master_Key_3DES;
        boolean retValue = My02clearKey(keyType, keyId);
        if (retValue) {
            logUtils.addCaseLog("MKSK02001 case results：" + retValue + "，execute success！");
            this.printMsgTool("MKSK02001 case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("MKSK02001 case results：" + retValue + "，execute Failed！");
        this.printMsgTool("MKSK02001 case results：" + retValue + "，execute Failed！");
    }

    public void MKSK03001() {
        byte[] key = BCDDecode.str2Bcd("41414141414141415252525252525252");
        byte[] kcv = BCDDecode.str2Bcd("A7391D741C8F16C1");
        Boolean retValue = My03loadTEK(0, key, (byte) 2, kcv, null);
        if (retValue) {
            logUtils.addCaseLog("MKSK03001 Case results：" + retValue + "，execute success！");
            this.printMsgTool("MKSK03001 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("MKSK03001 Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("MKSK03001 Case results：" + retValue + "，execute Failed！");
    }

    public void MKSK03002() {
        logUtils.printCaseInfo("L02001");
        logUtils.addCaseLog("Execute the L02001 case");
        Log.i(TAG, "L02001() executed");
        int keyId = 0;
        String keyString = "31313131313131313131313131313131";
        byte[] key = BCDDecode.str2Bcd(keyString);
        byte[] checkValue = {(byte) 0x40, (byte) 0x82, (byte) 0x6A, (byte) 0x58, (byte) 0x00, (byte) 0x60, (byte) 0x8C, (byte) 0x87};
        boolean retValue = My03loadTEK(keyId, key, (byte)2, checkValue, null);
        if (retValue) {
            logUtils.addCaseLog("L02001 Case results：" + retValue + "，execute success！");
            this.printMsgTool("L02001 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("L02001 Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("L02001 Case results：" + retValue + "，execute Failed！");
    }

    public void MKSK04001() {
        byte[] tekey = BCDDecode.str2Bcd("31313131313131313131313131313131");
        My03loadTEK(0, tekey, (byte)0x02, null, null);//3DES传输密钥

        byte[] key = BCDDecode.str2Bcd("82828282828382823837363838383838");
        Bundle extend = new Bundle();
        extend.putBoolean("isCBCType", false);
        extend.putByteArray("initVec", null);
        extend.putBoolean("isMasterEncMasterMode", false);
        extend.putInt("decryptKeyIndex", 0);
        byte[] checkvalue = BCDDecode.str2Bcd("824CF43A1F9447C8");
        Boolean retValue = My04loadMasterKey(99, key, 1, checkvalue, extend);
        if (retValue) {
            logUtils.addCaseLog("L30002 Case results：" + retValue + "，execute success！");
            this.printMsgTool("L30002 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("L30002 Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("L30002 Case results：" + retValue + "，execute Failed！");
    }

    public void MKSK05001() {
        String mainKeyString = "31333333333333333434343434343434";
        byte[] mainKey = BCDDecode.str2Bcd(mainKeyString);
        Boolean retValue = My05loadPlainMasterKey(0, mainKey, 2, null);//下载3DES明文主密钥
        if (retValue) {
            logUtils.addCaseLog("MKSK05001 Case results：" + retValue + "，execute success");
            this.printMsgTool("MKSK05001 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("MKSK05001 Case results：" + retValue + "，execute Failed！！");
        this.printMsgTool("MKSK05001 Case results：" + retValue + "，execute Failed！");
    }

    public void MKSK06001() {
        byte[] mKey = BCDDecode.str2Bcd("11111111111111113333333333333333");
        My05loadPlainMasterKey(99, mKey, 0x02, null);

        String keyStr = "82828285078382823838340838383838";
        byte[] key = BCDDecode.str2Bcd(keyStr);
        String kcvStr = "97911CE4DF53AEC6";
        byte[] checkvalue = BCDDecode.str2Bcd(kcvStr);
        Boolean retValue = My06loadSessionKey(1, 99, 0, 0, key, checkvalue, null);
        if (retValue) {
            logUtils.addCaseLog("L31001 Case results：" + retValue + "，execute success！");
            this.printMsgTool("L31001 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("L31001 Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("L31001 Case results：" + retValue + "，execute Failed！");
    }

    public void MKSK07001() {
        My05loadPlainMasterKey(99, mKey, 0x02, null);

        int keyType = 1;
        int mkId = 99;
        int wkId = 0;
        int decKeyType = 0x00;
        Log.i(TAG, "macKey=" + macKeyString);
        Log.i(TAG, "macKeyCheckValue=" + macKeyCheckValueString);
        My06loadSessionKey(keyType, mkId, wkId, decKeyType, macKey, macKeyCheckValue, null);

        int keyId = 0;
        int type = 0;
        int destype = 1;
        String datastr = "111111111111111122222222222222223333333333333333";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My07calculateMAC(keyId, type, null, data, destype, null);
    }

    public void MKSK08001() {
        String keyStr = "10101080101010102525252525252528";
        byte[] key = BCDDecode.str2Bcd(keyStr);
        String dataStr = "82828282828282823838383838383838";
        byte[] data = BCDDecode.str2Bcd(dataStr);
        String initVecStr = "0000000000000000";
        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
        String retValue = StringUtil.byte2HexStr(My08calculateData(0, 1, key, data, initVec));

        if (retValue.equals("368E34E391A1A199C2CED460E720BF81")) {
            logUtils.addCaseLog("MKSK08001 Case results：" + retValue + "，execute success！");
            this.printMsgTool("MKSK08001 Case results：" + retValue + "，execute success！");
            return;
        }
        logUtils.addCaseLog("MKSK08001 Case results：" + retValue + "，execute Failed！");
        this.printMsgTool("MKSK08001 Case results：" + retValue + "，execute Failed！");
    }

    public void MKSK09001() {

        My05loadPlainMasterKey(99, mKey, 0x02, null);

        int keyType = 3;
        int mkId = 99;
        int wkId = 0;
        int decKeyType = 0x00;
        Log.i(TAG, "dataKey=" + tdKeyString);
        Log.i(TAG, "dataKeyCheckValue=" + tdKeyCheckValueString);
        My06loadSessionKey(keyType, mkId, wkId, decKeyType, tdKey, tdKeyCheckValue, null);

        String dataStr = "82828282828282823838383838383838";
        byte[] data = BCDDecode.str2Bcd(dataStr);
        String initVecStr = "0000000000000000";
        byte[] initVec = BCDDecode.str2Bcd(initVecStr);
        String retValue = StringUtil.byte2HexStr(My09calculateBySessionKey(0, 1, 1, 1, 0, data, initVec));

        logUtils.addCaseLog("MKSK08001 Case results：" + retValue + "，execute success！");
        this.printMsgTool("MKSK08001 Case results：" + retValue + "，execute success！");
    }

    public void MKSK10001() {
        My05loadPlainMasterKey(99, mKey, 0x02, null);

        String dataStr = "82828282828282823838383838383838";
        byte[] data = BCDDecode.str2Bcd(dataStr);
        String retValue = StringUtil.byte2HexStr(My10calculateByMasterKey(99, 1, 0, data, null));

        logUtils.addCaseLog("MKSK08001 Case results：" + retValue + "，execute success！");
        this.printMsgTool("MKSK08001 Case results：" + retValue + "，execute success！");
    }

    public void MKSK11001() {
        String retValue = StringUtil.byte2HexStr(My11getKeyKCV(0, 0x05));
        logUtils.addCaseLog("MK11001 Case results：" + retValue + "，execute success！");
        this.printMsgTool("MK11001 Case results：" + retValue + "，execute success！");
    }

    public void MKSK12001() {//卡号6226901508781352
        My05loadPlainMasterKey(99, mKey, 0x02, null);

        int keyType = 2;
        int mkId = 99;
        int wkId = 0;
        int decKeyType = 0x00;
        Log.i(TAG, "dataKey=" + pinKeyString);
        Log.i(TAG, "dataKeyCheckValue=" + pinKeyCheckValueString);
        My06loadSessionKey(keyType, mkId, wkId, decKeyType, pinKey, pinKeyCheckValue, null);
        //卡号6226901508781352
        byte[] cardNumber = {0x36, 0x32, 0x32, 0x36, 0x39, 0x30, 0x31, 0x35, 0x30, 0x38, 0x37, 0x38, 0x31, 0x33, 0x35, 0x32};
        String passwd = "123456";
        String retValue = StringUtil.byte2HexStr(My12encryptPinFormat0(0, 1, cardNumber, passwd));

        logUtils.addCaseLog("Case results：" + retValue + "，execute success！");
        this.printMsgTool("Case results：" + retValue + "，execute success！");
    }

    public void MKSK13001() {
        My13getRandom((byte)4);
    }
    /**********************************************************************************************/

    private boolean My01isKeyExist(int keyType, int keyId) {
        boolean ret = false;
        String keyTypeString = null;
        if (keyType == ConstIMKSK.isKeyExist.keyType.Master_Key_3DES)
            keyTypeString = "Master key 3des";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.MAC_Key_3DES)
            keyTypeString = "Mac key 3des";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.PIN_Key_3DES)
            keyTypeString = "PIN key 3des";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.TEK_Key_3DES)
            keyTypeString = "TEK key 3des";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.Master_Key_AES)
            keyTypeString = "Master key AES";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.MAC_Key_AES)
            keyTypeString = "Mac key AES";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.PIN_Key_AES)
            keyTypeString = "PIN key AES";
        else if (keyType == ConstIMKSK.isKeyExist.keyType.TEK_Key_AES)
            keyTypeString = "TEK key AES";

        try {
            ret = imksk.isKeyExist(keyId, keyType, null);
            if (ret) {
                logUtils.addCaseLog(keyTypeString + "[" + keyId + "] exist");
            } else {
                logUtils.addCaseLog(keyTypeString + "[" + keyId + "]no-exist");
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

    private boolean My02clearKey(int keyId, int keyType) {
        boolean ret = false;
        try {
            ret = imksk.clearKey(keyId, keyType, null);
            if (ret) {
                logUtils.addCaseLog("Clear key successfully");
                this.printMsgTool("Result:Clear key successfully");
            } else {
                logUtils.addCaseLog("Clear key failed");
                this.printMsgTool("Result:Clear key failed");
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

    private boolean My03loadTEK(int keyId, byte[] key, byte algorithmType, byte[] checkValue, Bundle extend) {
        boolean ret = false;
        try {
            ret = imksk.loadTEK(keyId, key, algorithmType, checkValue, extend);
            if (ret) {
                logUtils.addCaseLog("Download the transmission key[" + keyId + "]successfully");
                this.printMsgTool("Result:Download the transmission key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the transmission key[" + keyId + "]failed");
                this.printMsgTool("Result:Download the transmission key[" + keyId + "]failed");
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

    private boolean My04loadMasterKey(int keyId, byte[] key, int algorithmType, byte[] checkValue, Bundle extend) {
        boolean ret = false;
        try {
            ret = imksk.loadMasterKey(keyId, key, algorithmType, checkValue, extend);

            if (ret) {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]successfully");
//                this.printMsgTool("Execution result: Download the ciphertext master key[" + keyId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the ciphertext master key[" + keyId + "]failed");
//                this.printMsgTool("Execution result: Download the ciphertext master key[" + keyId + "]failed");
                String errString = imksk.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
//                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private boolean My05loadPlainMasterKey(int keyId, byte[] key, int algorithmType, byte[] checkValue) {
        boolean ret = false;
        try {
            ret = imksk.loadPlainMasterKey(keyId, key, algorithmType, checkValue);
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

    private boolean My06loadSessionKey(int keyType, int mkId, int wkId, int decKeyType, byte[] key, byte[] checkValue, Bundle extend) {
        boolean ret = false;
        try {
            ret = imksk.loadSessionKey(keyType, mkId, wkId, decKeyType, key, checkValue, extend);

            if (ret) {
                logUtils.addCaseLog("Download the working key[" + wkId + "]successfully");
                this.printMsgTool("Result:Download the working key[" + wkId + "]successfully");
            } else {
                logUtils.addCaseLog("Download the working key[" + wkId + "]failed");
                this.printMsgTool("Result:Download the working key[" + wkId + "]failed");
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

    private void My07calculateMAC(int keyId, int type, byte[] CBCInitVec, byte[] data, int desType, Bundle bundle) {
        byte[] MyCalcMAC = null;
        try {
            MyCalcMAC = imksk.calculateMAC(keyId, type, CBCInitVec, data, desType, bundle);

            if (MyCalcMAC == null) {
                logUtils.addCaseLog("Failed to calc MAC");
                String errString = imksk.getLastError();
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
            e.printStackTrace();
            logUtils.addCaseLog("submit failure");
        }
    }

    private byte[] My08calculateData(int mode, int desType, byte[] key, byte[] data, byte[] initVec) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = imksk.calculateData(mode, desType, key, data, initVec, null);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("calculateDataEx executeTime : " + (endTime - startTime) + " ms");


            if (result == null) {
                logUtils.addCaseLog("Encryption/decryption failed");
                this.printMsgTool("Result：Encryption/decryption failed");
                String errString = imksk.getLastError();
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
            e.printStackTrace();
            logUtils.addCaseLog("Encryption/decryption failed");
        }
        return result;
    }

    private byte[] My09calculateBySessionKey(int keyId, int keyType, int encAlg, int encMode, int encFlag, byte[] data, byte[] initVec) {
        byte[] result = null;
        try {
            result = imksk.calculateBySessionKey(keyId, keyType, encAlg, encMode, encFlag, data, null);

            if (result == null) {
                logUtils.addCaseLog("TDKEY computation failed");
                this.printMsgTool("Result: TDKEY computation failed");
                String errString = imksk.getLastError();
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
            e.printStackTrace();
            logUtils.addCaseLog("TDKEY computation failed");
        }
        return result;
    }

    private byte[] My10calculateByMasterKey(int keyId, int keyType, int algorithmMode, byte[] data, Bundle extend) {
        byte[] result = null;
        try {
            result = imksk.calculateByMasterKey(keyId, keyType, algorithmMode, data, null);

            if (result == null) {
                logUtils.addCaseLog("computation failed");
                this.printMsgTool("Result: computation failed");
                String errString = imksk.getLastError();
                if (errString != null) {
                    Log.i(TAG, "getLastError=" + errString);
                    logUtils.addCaseLog(errString);
                }
            } else {
                logUtils.addCaseLog("The calculate successfully");
                this.printMsgTool("Result: calculate was successfully");
                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result: " + StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("computation failed");
        }
        return result;
    }

    private byte[] My11getKeyKCV(int keyIndex, int keyType) {
        byte[] KCV = null;
        try {
            KCV = imksk.getKeyKCV(keyIndex, keyType);

            if (KCV == null) {
                logUtils.addCaseLog("Failed to get KCV");
                this.printMsgTool("Result：Failed to get KCV");
                String errString = imksk.getLastError();
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
            e.printStackTrace();
        }
        return KCV;
    }

    private byte[] My12encryptPinFormat0(int pinKeyId, int desType, byte[] cardNumber, String passwd) {
        byte[] result = null;
        try {
            result = imksk.encryptPinFormat0(pinKeyId, desType, cardNumber, passwd);

            if (result == null) {
                logUtils.addCaseLog("PIN encryption failed");
                this.printMsgTool("Execution result: PIN encryption failed");
                String errString = imksk.getLastError();
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
            e.printStackTrace();
            logUtils.addCaseLog("PIN encryption failed");
        }
        return result;
    }

    private byte[] My13getRandom(byte length) {
        byte[] Random = null;
        try {
            long startTime = System.currentTimeMillis();
            Random = imksk.getRandom(length);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("getRandom executeTime : " + (endTime - startTime) + " ms");


            if (length == (byte) 0 || length > (byte) 127) {
                logUtils.addCaseLog("Failed to get random number");
                this.printMsgTool("Result：Failed to get random number");
                String errString = imksk.getLastError();
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
            e.printStackTrace();
        }
        return Random;
    }
    /**********************************************************************************************/

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
            logUtils.showCaseLog();
        }
    };

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        LogUtil.d(TAG, "---> name:" + name);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.MKSKModule");
            Method method = aClass.getDeclaredMethod(name);
            LogUtil.d(TAG, "---> name:" + method.getName());
            method.invoke(this);
            logUtils.addCaseLog(name + "Case execution completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    private void printMsgTool(String msg) {

        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
