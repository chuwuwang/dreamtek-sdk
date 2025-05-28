package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.laikey.jatools.Utils;
import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import Utils.LogUtils;
import base.MyApplication;


public class DukptMoudle {
    Context context;
    IDukpt iDukpt;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> loadDukptKey = new ArrayList<String>();
    ArrayList<String> calculateMac = new ArrayList<String>();
    ArrayList<String> calculateData = new ArrayList<String>();
    ArrayList<String> getDukptKSN = new ArrayList<String>();
    ArrayList<String> increaseKSN = new ArrayList<String>();
    ArrayList<String> isKeyExist = new ArrayList<String>();
    ArrayList<String> clearKey = new ArrayList<String>();

    private String dukptKey = getRandomString(32); // IPEK
    //    private String dukptKey = "6AC292FAA1315B4D858AB3A3D7D5933B";
    private String ksn = "FFFF9876543210E00000";
    private String mac = "4012345678909D987";
    private String data = "4012345678909D987";
    private String ksn_error = "FFFF9876543210E123";

    public static String getRandomString(int length) {
        String str = "ABCDEF0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(16);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public DukptMoudle(Context context, IDukpt iDukpt) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iDukpt = iDukpt;
        addAllapi();
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.DukptMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "N01":
                            loadDukptKey.add(i.getName());
                            break;
                        case "N02":
                            calculateMac.add(i.getName());
                            break;
                        case "N03":
                            calculateData.add(i.getName());
                            break;
                        case "N04":
                            getDukptKSN.add(i.getName());
                            break;
                        case "N05":
                            increaseKSN.add(i.getName());
                            break;
                        case "N06":
                            isKeyExist.add(i.getName());
                            break;
                        case "N07":
                            clearKey.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(loadDukptKey);
            caseNames.add(calculateMac);
            caseNames.add(calculateData);
            caseNames.add(getDukptKSN);
            caseNames.add(increaseKSN);
            caseNames.add(isKeyExist);
            caseNames.add(clearKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*******************************load dukpt******************************************/
    public boolean My01loadDukptKey(int keyId, byte[] ksn, byte[] key, byte[] checkValue, Bundle extend) {
        boolean ret = false;
        try {
            long startTime = System.currentTimeMillis();
            ret = iDukpt.loadDukptKey(keyId, ksn, key, checkValue, extend);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadDukptKey executeTime : " + (endTime - startTime) + " ms");

            if (ret) {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]success");
                this.printMsgTool("Result：download dukpt key =" + ret);
            } else {
                logUtils.addCaseLog("download dukpt key[" + keyId + "]failed");
                String errString = iDukpt.getLastError();
                this.printMsgTool("Result：download dukpt key =" + ret);
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void N01001() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPlainKey", false);
        bundle.putInt("TEKIndex", 0);
        bundle.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(0, KSN, key, null, bundle);
        if (retValue) {
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
    }

    public void N01002() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 0);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, null, extend);
        if (retValue) {
            logUtils.addCaseLog("N01002 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01002 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01002 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01002 case result：" + retValue + "，execute case Failed！");
    }

    public void N01003() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01003 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01003 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01003 case result：" + retValue + "，execute case Failed！");
    }

    public void N01004() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
//        AF8C074A692A3666
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(1, KSN, key, null, extend);
        if (retValue) {
            logUtils.addCaseLog("N01004 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01004 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01004 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01004 case result：" + retValue + "，execute case Failed！");
    }

    public void N01005() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
//        BF8C074A692A3666
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(2, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01005 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01005 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01005 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01005 case result：" + retValue + "，execute case success！");
    }

    public void N01006() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 0);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(3, KSN, key, null, extend);
        if (retValue) {
            logUtils.addCaseLog("N01006 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01006 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01006 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01006 case result：" + retValue + "，execute case Failed！");
    }

    public void N01007() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 0);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(4, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01007 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01007 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01007 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01007 case result：" + retValue + "，execute case Failed！");
    }

    public void N01008() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 0);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(2, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01008 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01008 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01008 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01008 case result：" + retValue + "，execute case Failed！");
    }

    public void N01009() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9724");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 0);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01009 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01009 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01009 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01009 case result：" + retValue + "，execute case success！");
    }

    public void N01010() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01010 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01010 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01010 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01010 case result：" + retValue + "，execute case success！");
    }

    public void N01011() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", -1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01011 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01011 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01011 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01011 case result：" + retValue + "，execute case success！");
    }

    public void N01012() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 100);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01012 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01012 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01012 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01012 case result：" + retValue + "，execute case success！");
    }

    public void N01013() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(-1, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01013 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01013 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01013 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01013 case result：" + retValue + "，execute case success！");
    }

    public void N01014() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(5, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01014 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01014 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01014 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01014 case result：" + retValue + "，execute case success！");
    }

    //    public void N01015() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
//        byte[] KSN = Utils.hexStr2Bytes(ksn);
//        byte[] key = Utils.hexStr2Bytes(dukptKey);
//        byte[] kcv = Utils.hexStr2Bytes("");
//        Bundle extend = new Bundle();
//        extend.putBoolean("isPlainKey",true);
//        extend.putInt("TEKIndex",1);
//        extend.putBoolean("KSNAutoIncrease",true);
//        boolean retValue = My01loadDukptKey(99,KSN,key,kcv,extend);
//        if(retValue){
//            logUtils.addCaseLog("N01015 case result：" + retValue + "，execute case Failed！");
//            this.printMsgTool("N01015 case result：" + retValue + "，execute case Failed！");
//            return;
//        }
//        logUtils.addCaseLog("N01015 case result：" + retValue + "，execute case success！");
//        this.printMsgTool("N01015 case result：" + retValue + "，execute case success！");
//    }
    public void N01016() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, null, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01016 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01016 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01016 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01016 case result：" + retValue + "，execute case success！");
    }

    public void N01017() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes("");
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01017 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01017 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01017 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01017 case result：" + retValue + "，execute case success！");
    }

    public void N01018() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes("FFFF9876543210E000");
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01018 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01018 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01018 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01018 case result：" + retValue + "，execute case success！");
    }

    public void N01019() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, null, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01019 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01019 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01019 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01019 case result：" + retValue + "，execute case success！");
    }

    public void N01020() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes("");
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01020 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01020 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01020 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01020 case result：" + retValue + "，execute case success！");
    }

    public void N01021() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes("010203040506070809000102030405");
        byte[] kcv = Utils.hexStr2Bytes("");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", true);
        boolean retValue = My01loadDukptKey(0, KSN, key, kcv, extend);
        if (retValue) {
            logUtils.addCaseLog("N01021 case result：" + retValue + "，execute case Failed！");
            this.printMsgTool("N01021 case result：" + retValue + "，execute case Failed！");
            return;
        }
        logUtils.addCaseLog("N01021 case result：" + retValue + "，execute case success！");
        this.printMsgTool("N01021 case result：" + retValue + "，execute case success！");
    }

    /*public void N01022() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(3,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
    }

    public void N01023() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(5,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
    }

    public void N01024() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(6,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
    }

    public void N01025() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(7,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
    }

    public void N01026() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(8,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
    }

    public void N01027() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(9,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01001 case result：" + retValue + "，execute case Failed！");
    }

    public void N01028() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        byte[] kcv = Utils.hexStr2Bytes("5221A1A0301B9725");
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey",false);
        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease",true);
        boolean retValue = My01loadDukptKey(10,KSN,key,kcv,extend);
        if(retValue){
            logUtils.addCaseLog("N01028 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01028 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01028 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01028 case result：" + retValue + "，execute case Failed！");
    }
*/
    public void N01022() {
//        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] kcv = null;
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
//        extend.putInt("TEKIndex",0);
        extend.putBoolean("KSNAutoIncrease", false);
        int num = 3;
        String temp = dukptKey.substring(0, dukptKey.length() - 2);
        for (int keyId = 0; keyId < 100; keyId++) {
            String k = String.format("%02d", keyId);
            byte[] key = Utils.hexStr2Bytes(temp + k);
            Log.d("fpc", "dukpt : " + dukptKey
                    + "\n qwer : " + temp + k);
            boolean retValue = My01loadDukptKey(keyId, KSN, key, kcv, extend);
            if (retValue) {
                logUtils.addCaseLog("N01022 case result：" + "keyId=" + keyId + ",retValue=" + retValue + "，execute case success！");
                this.printMsgTool("N01022 case result：" + "keyId=" + keyId + "retValue=" + retValue + "，execute case success！");
            } else {
                logUtils.addCaseLog("N01022 case result：" + "keyId=" + keyId + ",retValue=" + retValue + "，execute case Failed！");
                this.printMsgTool("N01022 case result：" + "keyId=" + keyId + ",retValue=" + retValue + "，execute case Failed！");
            }
        }
    }


    public void N01023() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(254, KSN, key, null, extend);
        if (retValue) {
            logUtils.addCaseLog("N01023 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01023 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01023 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01023 case result：" + retValue + "，execute case Failed！");
    }

    public void N01024() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(255, KSN, key, null, extend);
        if (retValue) {
            logUtils.addCaseLog("N01024 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01024 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01024 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01024 case result：" + retValue + "，execute case Failed！");
    }

    public void N01025() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes(ksn);
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putInt("TEKIndex", 1);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(256, KSN, key, null, extend);
        if (retValue) {
            logUtils.addCaseLog("N01025 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N01025 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N01025 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N01025 case result：" + retValue + "，execute case Failed！");
    }

    public void N01026() {
        ((MyApplication) context).serviceMoudle.getMKSK().MKSK03002();
        byte[] KSN = Utils.hexStr2Bytes("FFFF9876543210E00000");
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", false);
        extend.putInt("TEKIndex", 0);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(0, KSN, key, null, extend);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My04getDukptKsn(0, 0);
//        if(retValue){
//            logUtils.addCaseLog("N01026 case result：" + retValue + "，execute case success！");
//            this.printMsgTool("N01026 case result：" + retValue + "，execute case success！");
//            return;
//        }
//        logUtils.addCaseLog("N01026 case result：" + retValue + "，execute case Failed！");
//        this.printMsgTool("N01026 case result：" + retValue + "，execute case Failed！");
    }

    public void N01027() {
        byte[] KSN = Utils.hexStr2Bytes("FFFF987654321220E00000");
        byte[] key = Utils.hexStr2Bytes(dukptKey);
        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putBoolean("KSNAutoIncrease", false);
        boolean retValue = My01loadDukptKey(0, KSN, key, null, extend);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My04getDukptKsn(0, 0);
//        if(retValue){
//            logUtils.addCaseLog("N01026 case result：" + retValue + "，execute case success！");
//            this.printMsgTool("N01026 case result：" + retValue + "，execute case success！");
//            return;
//        }
//        logUtils.addCaseLog("N01026 case result：" + retValue + "，execute case Failed！");
//        this.printMsgTool("N01026 case result：" + retValue + "，execute case Failed！");
    }


    /**********************************end***************************************/


    /*******************************calculate mac******************************************/
    public byte[] My02calculateMAC(int keyId, int type, byte[] CBCInitVec, byte[] data, int desType, Bundle extend) {
        byte[] result = null;
        byte[] ksn = null;
        try {

            long startTime = System.currentTimeMillis();
            Bundle bundleRet = iDukpt.calculateMAC(keyId, type, CBCInitVec, data, desType, extend);
            if (bundleRet != null) {
                result = bundleRet.getByteArray("encryptedData");
                ksn = bundleRet.getByteArray("ksn");
            }
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("calculateMAC executeTime : " + (endTime - startTime) + " ms");

            if (result == null) {
                logUtils.addCaseLog("calculateMAC failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("Result：calculateMAC failed，error=" + errString);
                }
            } else {
                logUtils.addCaseLog("calculateMAC  success");
                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result：calculateMAC success，result=" + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result：KSN=" + StringUtil.byte2HexStr(ksn));
                logUtils.addCaseLog("");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void N02001() throws RemoteException {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 0, extend));
        if (result.equals("EADCB69EEFA8BCFA")) {
            logUtils.addCaseLog("N02001 case result：" + result + "，execute case success！");
            this.printMsgTool("N02001 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02001 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02001 case result：" + result + "，execute case Failed！");
        My04getDukptKsn(0, 0);
    }

    public void N02002() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 0, extend));
        if (result.equals("BCC244E9FD76E9AC")) {
            logUtils.addCaseLog("N02002 case result：" + result + "，execute case success！");
            this.printMsgTool("N02002 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02002 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02002 case result：" + result + "，execute case Failed！");
    }

    public void N02003() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 1, null, data, 1, extend));
        if (result.equals("2EFBFA62C50566EC")) {
            logUtils.addCaseLog("N02003 case result：" + result + "，execute case success！");
            this.printMsgTool("N02003 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02003 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02003 case result：" + result + "，execute case Failed！");
    }

    public void N02004() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 1, null, data, 1, extend));
        if (result.equals("639C4EA3E894CE77")) {
            logUtils.addCaseLog("N02004 case result：" + result + "，execute case success！");
            this.printMsgTool("N02004 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02004 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02004 case result：" + result + "，execute case Failed！");
    }

    public void N02005() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, data, 1, extend));
        if (result.equals("3033463732373641")) {
            logUtils.addCaseLog("N02005 case result：" + result + "，execute case success！");
            this.printMsgTool("N02005 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02005 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02005 case result：" + result + "，execute case Failed！");
    }

    public void N02006() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, data, 1, extend));
        if (result.equals("3337324241334433")) {
            logUtils.addCaseLog("N02006 case result：" + result + "，execute case success！");
            this.printMsgTool("N02006 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02006 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02006 case result：" + result + "，execute case Failed！");
    }

    public void N02007() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 3, null, data, 1, extend));
        if (result.equals("4B8C7ECF771B6F4D")) {
            logUtils.addCaseLog("N02007 case result：" + result + "，execute case success！");
            this.printMsgTool("N02007 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02007 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02007 case result：" + result + "，execute case Failed！");
    }

    public void N02008() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 3, null, data, 1, extend));
        if (result.equals("2F3A9E035DDE3050")) {
            logUtils.addCaseLog("N02008 case result：" + result + "，execute case success！");
            this.printMsgTool("N02008 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02008 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02008 case result：" + result + "，execute case Failed！");
    }

    public void N02009() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, data, 1, extend));
        if (result.equals("C45462265C970B54")) {
            logUtils.addCaseLog("N02009 case result：" + result + "，execute case success！");
            this.printMsgTool("N02009 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02009 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02009 case result：" + result + "，execute case Failed！");
    }

    public void N02010() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, data, 1, extend));
        if (result.equals("D41722C7D0FA497C")) {
            logUtils.addCaseLog("N02010 case result：" + result + "，execute case success！");
            this.printMsgTool("N02010 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02010 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02010 case result：" + result + "，execute case Failed！");
    }

    public void N02011() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 0, extend));
        if (result.equals("EADCB69EEFA8BCFA")) {
            logUtils.addCaseLog("N02011 case result：" + result + "，execute case success！");
            this.printMsgTool("N02011 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02011 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02011 case result：" + result + "，execute case Failed！");
    }

    public void N02012() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 0, extend));
        if (result.equals("BCC244E9FD76E9AC")) {
            logUtils.addCaseLog("N02012 case result：" + result + "，execute case success！");
            this.printMsgTool("N02012 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02012 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02012 case result：" + result + "，execute case Failed！");
    }

    public void N02013() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 1, null, data, 0, extend));
        if (result.equals("B1279A152ECFBEC0")) {
            logUtils.addCaseLog("N02013 case result：" + result + "，execute case success！");
            this.printMsgTool("N02013 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02013 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02013 case result：" + result + "，execute case Failed！");
    }

    public void N02014() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 1, null, data, 0, extend));
        if (result.equals("17D464567F36DD49")) {
            logUtils.addCaseLog("N02014 case result：" + result + "，execute case success！");
            this.printMsgTool("N02014 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02014 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02014 case result：" + result + "，execute case Failed！");
    }

    public void N02015() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, data, 0, extend));
        if (result.equals("4530393532313038")) {
            logUtils.addCaseLog("N02015 case result：" + result + "，execute case success！");
            this.printMsgTool("N02015 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02015 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02015 case result：" + result + "，execute case Failed！");
    }

    public void N02016() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, data, 0, extend));
        if (result.equals("3738453441334444")) {
            logUtils.addCaseLog("N02016 case result：" + result + "，execute case success！");
            this.printMsgTool("N02016 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02016 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02016 case result：" + result + "，execute case Failed！");
    }

    public void N02017() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 3, null, data, 0, extend));
        if (result.equals("3C3B5A103D88DD82")) {
            logUtils.addCaseLog("N02017 case result：" + result + "，execute case success！");
            this.printMsgTool("N02017 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02017 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02017 case result：" + result + "，execute case Failed！");
    }

    public void N02018() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 3, null, data, 0, extend));
        if (result.equals("4875D07A7EAF0BE8")) {
            logUtils.addCaseLog("N02018 case result：" + result + "，execute case success！");
            this.printMsgTool("N02018 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02018 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02018 case result：" + result + "，execute case Failed！");
    }

    public void N02019() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, data, 0, extend));
        if (result.equals("EADCB69EEFA8BCFA")) {
            logUtils.addCaseLog("N02019 case result：" + result + "，execute case success！");
            this.printMsgTool("N02019 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02019 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02019 case result：" + result + "，execute case Failed！");
    }

    public void N02020() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, data, 0, extend));
        if (result.equals("BCC244E9FD76E9AC")) {
            logUtils.addCaseLog("N02020 case result：" + result + "，execute case success！");
            this.printMsgTool("N02020 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02020 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02020 case result：" + result + "，execute case Failed！");
    }

    public void N02021() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 3, extend));
        if (result.equals("BAE1B0C0A075EEB0")) {
            logUtils.addCaseLog("N02021 case result：" + result + "，execute case success！");
            this.printMsgTool("N02021 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02021 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02021 case result：" + result + "，execute case Failed！");
    }

    public void N02022() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 3, extend));
        if (result.equals("81DE5AB788424B09")) {
            logUtils.addCaseLog("N02022 case result：" + result + "，execute case success！");
            this.printMsgTool("N02022 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02022 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02022 case result：" + result + "，execute case Failed！");
    }

    public void N02023() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 1, null, data, 3, extend));
        if (result.equals("BAE1B0C0A075EEB0")) {
            logUtils.addCaseLog("N02023 case result：" + result + "，execute case success！");
            this.printMsgTool("N02023 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02023 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02023 case result：" + result + "，execute case Failed！");
    }

    public void N02024() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 1, null, data, 3, extend));
        if (result.equals("81DE5AB788424B09")) {
            logUtils.addCaseLog("N02024 case result：" + result + "，execute case success！");
            this.printMsgTool("N02024 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02024 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02024 case result：" + result + "，execute case Failed！");
    }

    public void N02025() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, data, 3, extend));
        if (result.equals("4341333833354630")) {
            logUtils.addCaseLog("N02025 case result：" + result + "，execute case success！");
            this.printMsgTool("N02025 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02025 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02025 case result：" + result + "，execute case Failed！");
    }

    public void N02026() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, data, 3, extend));
        if (result.equals("3134424142353343")) {
            logUtils.addCaseLog("N02026 case result：" + result + "，execute case success！");
            this.printMsgTool("N02026 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02026 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02026 case result：" + result + "，execute case Failed！");
    }

    public void N02027() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 3, null, data, 3, extend));
        if (result.equals("BAE1B0C0A075EEB0")) {
            logUtils.addCaseLog("N02027 case result：" + result + "，execute case success！");
            this.printMsgTool("N02027 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02027 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02027 case result：" + result + "，execute case Failed！");
    }

    public void N02028() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 3, null, data, 3, extend));
        if (result.equals("81DE5AB788424B09")) {
            logUtils.addCaseLog("N02028 case result：" + result + "，execute case success！");
            this.printMsgTool("N02028 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02028 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02028 case result：" + result + "，execute case Failed！");
    }

    public void N02029() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, data, 3, extend));
        if (result.equals("BAE1B0C0A075EEB0")) {
            logUtils.addCaseLog("N02029 case result：" + result + "，execute case success！");
            this.printMsgTool("N02029 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02029 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02029 case result：" + result + "，execute case Failed！");
    }

    public void N02030() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, data, 3, extend));
        if (result.equals("81DE5AB788424B09")) {
            logUtils.addCaseLog("N02030 case result：" + result + "，execute case success！");
            this.printMsgTool("N02030 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02030 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02030 case result：" + result + "，execute case Failed！");
    }

    // calculateMAC失败，错误=Key ID Invalid
    public void N02031() {
        N01002();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(-1, 0, null, data, 1, extend);
    }

    // calculateMAC失败，错误=Key ID Invalid
    public void N02032() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(5, 0, null, data, 1, extend);
    }

    // 结果一直在改变，验证肯定失败。
    public void N02033() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(1, 0, null, data, 1, extend));
        if (result.equals("555F0A2A49039812")) {
            logUtils.addCaseLog("N02033 case result：" + result + "，execute case success！");
            this.printMsgTool("N02033 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02033 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02033 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateMAC失败，错误=Calcute mac failed
    public void N02034() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, -1, null, data, 1, extend);
    }

    // 执行结果：calculateMAC失败，错误=Calcute mac failed
    public void N02035() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 5, null, data, 1, extend);
    }

    // 执行结果：calculateMAC失败，错误=Parameter Invalid
    public void N02036() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 4, initVec, data, 1, extend);
    }

    // 执行结果：calculateMAC失败，错误=Parameter Invalid
    public void N02037() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("01010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 4, initVec, data, 1, extend);
    }

    public void N02038() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0000000000000000");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, initVec, data, 1, extend));
        if (result.equals("C45462265C970B54")) {
            logUtils.addCaseLog("N02038 case result：" + result + "，execute case success！");
            this.printMsgTool("N02038 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02038 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02038 case result：" + result + "，execute case Failed！");
    }

    public void N02039() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0101010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, initVec, data, 1, extend));
        if (result.equals("F32310F6D2CB9A15")) {
            logUtils.addCaseLog("N02039 case result：" + result + "，execute case success！");
            this.printMsgTool("N02039 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02039 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02039 case result：" + result + "，execute case Failed！");
    }

    public void N02040() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0000000000000000");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, initVec, data, 0, extend));
        if (result.equals("EADCB69EEFA8BCFA")) {
            logUtils.addCaseLog("N02040 case result：" + result + "，execute case success！");
            this.printMsgTool("N02040 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02040 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02040 case result：" + result + "，execute case Failed！");
    }

    public void N02041() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0101010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, initVec, data, 0, extend));
        if (result.equals("BF42CCFE499B7107")) {
            logUtils.addCaseLog("N02041 case result：" + result + "，execute case success！");
            this.printMsgTool("N02041 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02041 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02041 case result：" + result + "，execute case Failed！");
    }

    public void N02042() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0000000000000000");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, initVec, data, 3, extend));
        if (result.equals("BAE1B0C0A075EEB0")) {
            logUtils.addCaseLog("N02042 case result：" + result + "，execute case success！");
            this.printMsgTool("N02042 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02042 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02042 case result：" + result + "，execute case Failed！");
    }

    public void N02043() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0101010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, initVec, data, 3, extend));
        if (result.equals("CD7E87E874FBB96D")) {
            logUtils.addCaseLog("N02043 case result：" + result + "，execute case success！");
            this.printMsgTool("N02043 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02043 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02043 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateMAC失败，错误=Parameter Invalid
    public void N02044() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 0, null, null, 1, extend);
    }

    // 执行结果：calculateMAC失败，错误=Parameter Invalid
    public void N02045() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 0, null, data, 1, extend);
    }

    public void N02046() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, mac.getBytes(), 1, extend));
        if (result.equals("C5BC8EC1621EF5EE")) {
            logUtils.addCaseLog("N02046 case result：" + result + "，execute case success！");
            this.printMsgTool("N02046 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02046 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02046 case result：" + result + "，execute case Failed！");
    }

    public void N02047() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 2, null, mac.getBytes(), 0, extend));
        if (result.equals("4336334546374446")) {
            logUtils.addCaseLog("N02047 case result：" + result + "，execute case success！");
            this.printMsgTool("N02047 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02047 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02047 case result：" + result + "，execute case Failed！");
    }

    public void N02048() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 4, null, mac.getBytes(), 3, extend));
        if (result.equals("A5B2D246C6F94989")) {
            logUtils.addCaseLog("N02048 case result：" + result + "，execute case success！");
            this.printMsgTool("N02048 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02048 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02048 case result：" + result + "，execute case Failed！");
    }

    public void N02049() {
        N01003();
        byte[] data = new byte[2048];
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My02calculateMAC(0, 0, null, data, 1, extend));
        if (result.equals("AE1728663FFD94D6")) {
            logUtils.addCaseLog("N02049 case result：" + result + "，execute case success！");
            this.printMsgTool("N02049 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N02049 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N02049 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateMAC失败，错误=Data is too long
    public void N02050() {
        N01003();
        byte[] data = new byte[2049];
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 0, null, data, 1, extend);
    }

    // 执行结果：calculateMAC失败，错误=Parameter Invalid
    public void N02051() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 0, null, data, -1, extend);
    }

    // 执行结果：calculateMAC失败，错误=Parameter Invalid
    public void N02052() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 0, null, data, 4, extend);
    }

//       public void N02053() {
//        N01003();
//        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
//        My02calculateMAC(0,0,null,data,1,null);
//    }

    // KSN自动加一，无法验证
    public void N02054() {
        N01004();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My02calculateMAC(0, 0, null, data, 1, extend);
    }

    /**********************************end***************************************/


    /*********************************calculate data****************************************/
    public byte[] My03calculateData(int keyId, int encryptType, int algorithmModel, byte[] data, byte[] initVec, Bundle extend) {
        byte[] result = null;
        byte[] ksn = null;
        try {

            long startTime = System.currentTimeMillis();
            Bundle bundleRet = iDukpt.calculateData(keyId, encryptType, algorithmModel, data, initVec, extend);
            if (bundleRet != null) {
                result = bundleRet.getByteArray("encryptedData");
                ksn = bundleRet.getByteArray("ksn");
            }
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("calculateData executeTime : " + (endTime - startTime) + " ms");


            if (result == null) {
                logUtils.addCaseLog("calculateData failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("Result：calculateData failed，error=" + errString);
                }
            } else {
                logUtils.addCaseLog("calculateData success");
                logUtils.addCaseLog("Result: " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result：calculateData success，error=" + StringUtil.byte2HexStr(result));
                this.printMsgTool("Result：KSN=" + StringUtil.byte2HexStr(ksn));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void N03001() {
//        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 2, data, null, extend));
        if (result.equals("D2A09D55E28956BCD8E17A8A31D35993 ")) {
            logUtils.addCaseLog("N03001 case result：" + result + "，execute case success！");
            this.printMsgTool("N03001 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03001 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03001 case result：" + result + "，execute case Failed！");
    }

    public void N03002() {
//        N01002();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 2, data, null, extend));
        if (result.equals("8B7D94C281AE5B7BFC615D20CB86C40A")) {
            logUtils.addCaseLog("N03002 case result：" + result + "，execute case success！");
            this.printMsgTool("N03002 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03002 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03002 case result：" + result + "，execute case Failed！");
    }

    public void N03003() {
//        N01002();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 2, data, null, extend));
        if (result.equals("D2A09D55E28956BC317FC35152592985")) {
            logUtils.addCaseLog("N03003 case result：" + result + "，execute case success！");
            this.printMsgTool("N03003 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03003 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03003 case result：" + result + "，execute case Failed！");
    }

    public void N03004() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 1, data, null, extend));
        if (result.equals("8B7D94C281AE5B7BFEC64F257ACB52D8")) {
            logUtils.addCaseLog("N03004 case result：" + result + "，execute case success！");
            this.printMsgTool("N03004 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03004 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03004 case result：" + result + "，execute case Failed！");
    }

    public void N03005() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 0, 2, data, null, extend));
        if (result.equals("C3ADC1C2D4BEF472711C25774196DD23")) {
            logUtils.addCaseLog("N03005 case result：" + result + "，execute case success！");
            this.printMsgTool("N03005 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03005 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03005 case result：" + result + "，execute case Failed！");
    }

    public void N03006() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 0, 2, data, null, extend));
        if (result.equals("79FA5BB4CA6B424C2B44DE615C45642C")) {
            logUtils.addCaseLog("N03006 case result：" + result + "，execute case success！");
            this.printMsgTool("N03006 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03006 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03006 case result：" + result + "，execute case Failed！");
    }

    public void N03007() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 0, 1, data, null, extend));
        if (result.equals("C3ADC1C2D4BEF4726FDAFC670EA1779D")) {
            logUtils.addCaseLog("N03007 case result：" + result + "，execute case success！");
            this.printMsgTool("N03007 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03007 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03007 case result：" + result + "，execute case Failed！");
    }

    public void N03008() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 0, 1, data, null, extend));
        if (result.equals("79FA5BB4CA6B424C9495EB0AAF442DE1")) {
            logUtils.addCaseLog("N03008 case result：" + result + "，execute case success！");
            this.printMsgTool("N03008 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03008 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03008 case result：" + result + "，execute case Failed！");
    }

    public void N03009() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 3, 2, data, null, extend));
        if (result.equals("5A315CF6F68CE3E5A07473B185BE5271")) {
            logUtils.addCaseLog("N03009 case result：" + result + "，execute case success！");
            this.printMsgTool("N03009 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03009 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03009 case result：" + result + "，execute case Failed！");
    }

    public void N03010() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 3, 2, data, null, extend));
        if (result.equals("DF42943322644F091A852DB79E94FE70")) {
            logUtils.addCaseLog("N03010 case result：" + result + "，execute case success！");
            this.printMsgTool("N03010 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03010 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03010 case result：" + result + "，execute case Failed！");
    }

    public void N03011() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 3, 1, data, null, extend));
        if (result.equals("DF42943322644F091A852DB79E94FE70")) {
            logUtils.addCaseLog("N03011 case result：" + result + "，execute case success！");
            this.printMsgTool("N03011 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03011 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03011 case result：" + result + "，execute case Failed！");
    }

    public void N03012() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", false);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 3, 1, data, null, extend));
        if (result.equals("DF42943322644F091A852DB79E94FE70")) {
            logUtils.addCaseLog("N03012 case result：" + result + "，execute case success！");
            this.printMsgTool("N03012 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03012 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03012 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03013() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(-1, 1, 2, data, null, extend);
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03014() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(5, 1, 2, data, null, extend);
    }

    // 加密值每回都在变，验证肯定失败
    public void N03015() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(1, 1, 2, data, null, extend));
        if (result.equals("AA1670BBAFFDD6648ED0E7D27D149233")) {
            logUtils.addCaseLog("N03015 case result：" + result + "，execute case success！");
            this.printMsgTool("N03015 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03015 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03015 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03016() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, -1, 2, data, null, extend);
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03017() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 4, 2, data, null, extend);
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03018() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 0, data, null, extend);
    }

    public void N03019() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 3, data, null, extend));
        if (result.equals("C3FFB330DA4C0FF64425CFAC1D9A8A24")) {
            logUtils.addCaseLog("N03019 case result：" + result + "，execute case success！");
            this.printMsgTool("N03019 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03019 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03019 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03020() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 2, null, null, extend);
    }

    // 执行结果：calculateData失败，错误=Key ID Invalid
    public void N03021() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 2, data, null, extend);
    }

    public void N03022() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 2, data.getBytes(), null, extend));
        if (result.equals("FC0D53B7EA1FDA9ED06B9FE305E350CA55F0524793A302F9")) {
            logUtils.addCaseLog("N03022 case result：" + result + "，execute case success！");
            this.printMsgTool("N03022 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03022 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03022 case result：" + result + "，execute case Failed！");
    }

    public void N03023() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 0, 2, data.getBytes(), null, extend));
        if (result.equals("9C0F35CB4004BAD2D1B6532DF16195245F6C5D825CC9B445")) {
            logUtils.addCaseLog("N03023 case result：" + result + "，execute case success！");
            this.printMsgTool("N03023 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03023 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03023 case result：" + result + "，execute case Failed！");
    }

    public void N03024() {
        N01003();
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 3, 2, data.getBytes(), null, extend));
        if (result.equals("49F3B426A1BB29BC60294A69FBA6C6AF93BB4FCAA9D4725FB214DDEF0CE9885B")) {
            logUtils.addCaseLog("N03024 case result：" + result + "，execute case success！");
            this.printMsgTool("N03024 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03024 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03024 case result：" + result + "，execute case Failed！");
    }

    // 字节太长，不添加自动化验证，可以看打印结果。
    public void N03025() {
        N01003();
        byte[] data = new byte[2048];
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 2, data, null, extend);
    }

    // 字节数2049，执行结果：calculateData失败，错误=Data is too long
    public void N03026() {
        N01003();
        byte[] data = new byte[2049];
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 2, data, null, extend);
    }

    // 执行结果：calculateData失败，错误=Parameter Invalid
    public void N03027() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 1, data, initVec, extend);
    }

    public void N03030() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0101010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 1, 1, data, initVec, extend));
        if (result.equals("E41D286E0DE4DF0AB89EBD4B2C9A2B61")) {
            logUtils.addCaseLog("N03030 case result：" + result + "，execute case success！");
            this.printMsgTool("N03030 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03030 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03030 case result：" + result + "，execute case Failed！");
    }

    public void N03031() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0101010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 0, 1, data, initVec, extend));
        if (result.equals("DF127C6A10E12E5A75411D3E3ACB61B9")) {
            logUtils.addCaseLog("N03031 case result：" + result + "，execute case success！");
            this.printMsgTool("N03031 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03031 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03031 case result：" + result + "，execute case Failed！");
    }

    public void N03032() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("0101010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        String result = StringUtil.byte2HexStr(My03calculateData(0, 3, 1, data, initVec, extend));
        if (result.equals("61DC0D721A443488580E65B465D503BF")) {
            logUtils.addCaseLog("N03032 case result：" + result + "，execute case success！");
            this.printMsgTool("N03032 case result：" + result + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N03032 case result：" + result + "，execute case Failed！");
        this.printMsgTool("N03032 case result：" + result + "，execute case Failed！");
    }

    // 执行结果：calculateData失败，错误=Parameter Invalid
    public void N03033() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        byte[] initVec = BCDDecode.str2Bcd("01010101010101");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 1, data, initVec, extend);
    }

    // execute case success，无返回值
    public void N03034() {
        N01003();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        My03calculateData(0, 1, 2, data, null, null);
    }

    // 返回值一直在改变
    public void N03035() {
        N01004();
        byte[] data = BCDDecode.str2Bcd("34343434343434343939393939393939");
        Bundle extend = new Bundle();
        extend.putBoolean("variantRequestKey", true);
        My03calculateData(0, 1, 2, data, null, extend);
    }
    /************************************end*************************************/


    /*********************************get KSN****************************************/
    public byte[] My04getDukptKsn(int keyIdx, int keyType) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = iDukpt.getDukptKSN(keyIdx, keyType, null);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("getDukptKsn executeTime : " + (endTime - startTime) + " ms");

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

    public void N04001() {
        My04getDukptKsn(0, 0);
    }

    public void N04002() {
        My04getDukptKsn(1, 0);
    }

    public void N04003() {
        My04getDukptKsn(2, 0);
    }

    public void N04004() {
        My04getDukptKsn(3, 0);
    }

    public void N04005() {
        My04getDukptKsn(4, 0);
    }

    public void N04006() {
        My04getDukptKsn(-1, 0);
    }

    public void N04007() {
        My04getDukptKsn(5, 0);
    }
    /************************************end*************************************/

    /*********************************increase KSN****************************************/
    public byte[] My05increaseKSN(int index, int keyType) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = iDukpt.increaseKSN(index, keyType, null);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("increaseKSN executeTime : " + (endTime - startTime) + " ms");


            if (result == null) {
                logUtils.addCaseLog("increaseKSN failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("Result：increaseKSN，error=" + errString);
                }
            } else {
                logUtils.addCaseLog("increaseKSN success");
                logUtils.addCaseLog("increment after KSN : " + StringUtil.byte2HexStr(result));
                this.printMsgTool("Execution result: increment after KSN, result=" + StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void N05001() {
//        N01002();
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);
        My05increaseKSN(0, 0);

        My04getDukptKsn(0, 0);
        My04getDukptKsn(1, 0);
    }

    public void N05002() {
        N01002();
        My05increaseKSN(1, 0);
        My05increaseKSN(1, 0);
        My05increaseKSN(1, 0);
        My05increaseKSN(1, 0);
        My05increaseKSN(1, 0);
        My05increaseKSN(1, 0);
    }

    public boolean My06isKeyExist(int keyType, int keyId) {
        boolean ret = false;
        try {
            ret = iDukpt.isKeyExist(keyType, keyId, null);

            if (ret) {
                logUtils.addCaseLog("setDukptCFG success,ret=" + ret);
                this.printMsgTool("setDukptCFG success, result=" + ret);
            } else {
                logUtils.addCaseLog("download dukpt key failed,ret=" + ret);
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("setDukptCFG，error=" + errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void N06001() {
        boolean retValue = My06isKeyExist(0, 0x01);
        if (retValue) {
            logUtils.addCaseLog("N06001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N06001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N06001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N06001 case result：" + retValue + "，execute case Failed！");
    }

    public boolean My07clearKey(int keyId, int keyType) {
        boolean ret = false;
        try {
            ret = iDukpt.clearKey(keyId, keyType, null);

            if (ret) {
                logUtils.addCaseLog("setDukptCFG success,ret=" + ret);
                this.printMsgTool("setDukptCFG success, result=" + ret);
            } else {
                logUtils.addCaseLog("download dukpt key failed,ret=" + ret);
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    logUtils.addCaseLog(errString);
                    this.printMsgTool("setDukptCFG，error=" + errString);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void N07001() {
        boolean retValue = My07clearKey(0, 0x01);
        if (retValue) {
            logUtils.addCaseLog("N06001 case result：" + retValue + "，execute case success！");
            this.printMsgTool("N06001 case result：" + retValue + "，execute case success！");
            return;
        }
        logUtils.addCaseLog("N06001 case result：" + retValue + "，execute case Failed！");
        this.printMsgTool("N06001 case result：" + retValue + "，execute case Failed！");
    }

    /************************************end*************************************/


    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.DukptMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "execute case completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCaseInfo(int groupPosition, int childPosition) {
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