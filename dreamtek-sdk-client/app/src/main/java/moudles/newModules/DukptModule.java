package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.Button;

import com.verifone.smartpos.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import entity.cases.BaseCase;

/**
 * Created by RuihaoS on 2021/5/8.
 */
public class DukptModule extends TestModule {

/*
    public Button btn[] = new Button[10];
    public Button btn_conf, btn_cancel, btn_del;
    private Map<String, Button> keysMap = new HashMap<>();

    private final String TAG = "DukptManager";

    private byte[] data;

    */
/*******************************load dukpt******************************************//*

    public boolean T_loadDukptKey(String skeyId, String sksn, String skey, String scheckValue, String sIsPlainKey, String sTEKIndex, String sKSNAutoIncrease) {
        return T_loadDukptKey(skeyId, sksn, skey, scheckValue, sIsPlainKey + " _ " + sTEKIndex + " _ " + sKSNAutoIncrease);
    }

    public boolean T_loadDukptKey(String skeyId, String sksn, String skey, String scheckValue, String sBundle) {
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sksn) || TextUtils.isEmpty(skey) ) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return false;
//        }

        boolean ret = false;
        try {

            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("isPlainKey", BundleConfig_boolean),
                    new BundleConfig("TEKIndex", BundleConfig_int),
                    new BundleConfig("KSNAutoIncrease", BundleConfig_boolean)
            };
            Bundle extend = convert(sBundle, bundleConfigs);
            byte[] key = StringUtil.hexStr2Bytes(skey);
            byte[] ksn = StringUtil.hexStr2Bytes(sksn);
            byte[] checkValue = StringUtil.hexStr2Bytes(scheckValue);


            long startTime = System.currentTimeMillis();
            ret = iDukpt.loadDukptKey(Integer.parseInt(skeyId), ksn, key, checkValue, extend);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("loadDukptKey executeTime : " + (endTime - startTime) + " ms");

            if (ret) {
                logUtils.addCaseLog("download dukpt key[" + skeyId + "]success");
                this.printMsgTool("Result：download dukpt key =" + ret);
            } else {
                logUtils.addCaseLog("download dukpt key[" + skeyId + "]failed");
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

    */
/*********************************increase KSN****************************************//*

    public byte[] T_increaseKSN(String sIndex) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = iDukpt.increaseKSN(Integer.parseInt(sIndex));
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

    public byte[] T_increaseKsnEX(String sIndex, String keyType, String sBundle) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = iDukpt.increaseKsnEX(Integer.parseInt(sIndex), Integer.parseInt(keyType), null);
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

    */
/*******************************calculate mac******************************************//*

    public byte[] T_calculateMAC(String skeyId, String stype, String sCBCInitVec, String sdata, String sdesType, String sBundle) {
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(stype) || TextUtils.isEmpty(sCBCInitVec)
//                || TextUtils.isEmpty(sdata) || TextUtils.isEmpty(sdesType) || TextUtils.isEmpty(sBundle)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }

        byte[] result = null;
        try {

            byte[] CBCInitVec = StringUtil.hexStr2Bytes(sCBCInitVec);
            byte[] data = StringUtil.hexStr2Bytes(sdata);
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("variantRequestKey", BundleConfig_boolean)
            };
            Bundle extend = convert(sBundle, bundleConfigs);

            long startTime = System.currentTimeMillis();
            result = iDukpt.calculateMAC(Integer.parseInt(skeyId), Integer.parseInt(stype), CBCInitVec, data, Integer.parseInt(sdesType), extend);
            if (result.length > 8)
                result = Arrays.copyOfRange(result, 0, 8);
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
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }


    */
/*********************************calculate data****************************************//*

    public byte[] T_calculateData(String skeyId, String sencryptType, String salgorithmModel, String sdata, String sinitVec, String svariantRequestKey, String sdukptDispersionType) {
        return T_calculateData(skeyId, sencryptType, salgorithmModel, sdata, sinitVec, svariantRequestKey + " _ " + sdukptDispersionType);
    }

    public byte[] T_calculateData(String skeyId, String sencryptType, String salgorithmModel, String sdata, String sinitVec, String sBundle) {
//        if (TextUtils.isEmpty(skeyId) || TextUtils.isEmpty(sencryptType) || TextUtils.isEmpty(salgorithmModel)
//                || TextUtils.isEmpty(sdata) || TextUtils.isEmpty(sinitVec) || TextUtils.isEmpty(sBundle) ) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, Invalid params");
//            return null;
//        }

        byte[] result = null;
        try {
            byte[] initVec = StringUtil.hexStr2Bytes(sinitVec);
            byte[] data = StringUtil.hexStr2Bytes(sdata);

            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("variantRequestKey", BundleConfig_boolean),
                    new BundleConfig("dukptDispersionType", BundleConfig_byte)
            };

            Bundle extend = convert(sBundle, bundleConfigs);

            long startTime = System.currentTimeMillis();
            result = iDukpt.calculateData(Integer.parseInt(skeyId), Integer.parseInt(sencryptType), Integer.parseInt(salgorithmModel), data, initVec, extend);
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
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }


    */
/*********************************get KSN****************************************//*

    public byte[] T_getDukptKsn(String skeyIdx) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = iDukpt.getDukptKsn(Integer.parseInt(skeyIdx));
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

    public byte[] T_getDukptKsnEX(String skeyIdx, String keyType, String sBundle) {
        byte[] result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = iDukpt.getDukptKsnEX(Integer.parseInt(skeyIdx), Integer.parseInt(keyType), null);
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

    public boolean T_setDukptCFG(String skeyId, String sAutoIncrease) {
        return T_setDukptCFG(skeyId + " _ " + sAutoIncrease);
    }

    public boolean T_setDukptCFG(String sBundle) {

        boolean ret = false;
        try {
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyId", BundleConfig_int),
                    new BundleConfig("autoIncrease", BundleConfig_boolean),
                    new BundleConfig("keyType",BundleConfig_int)
            };
            Bundle extend = convert(sBundle, bundleConfigs);
            long startTime = System.currentTimeMillis();
            ret = iDukpt.setDukptCFG(extend);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("setDukptCFG executeTime : " + (endTime - startTime) + " ms");

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


    */
/***
     * @deprecated please see {@link #runTheMethod(BaseCase caseInfo)}
     * @param groupPosition
     * @param childPosition
     *//*

    public void runTheMethod(int groupPosition, int childPosition) {
        BaseCase caseInfo = caseNames.get(groupPosition).get(childPosition);
        String apiName = caseInfo.getApi();
        String caseId = caseInfo.getCaseId();
        String methodParam = caseInfo.getMethodParams();
        Object[] methods = new Object[]{};
        if (!TextUtils.isEmpty(methodParam)) {
            methods = methodParam.split(",");
        }
        logUtils.addCaseLog(caseId + " params:" + methodParam);
        logUtils.clearLog();
        try {
            Class<?> aClass = Class.forName("moudles.newModules.DukptModule");
            Method method = aClass.getDeclaredMethod(apiName);
            */
/** 获取本方法所有参数类型 **//*

            method.setAccessible(true);
            method.invoke(this, methods);
            logUtils.addCaseLog(caseId + "execute 完毕");
        } catch (Exception e) {
            e.printStackTrace();
            logUtils.addCaseLog(caseId + "exception found during execution");
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
