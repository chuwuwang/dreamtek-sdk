package moudles.newModules;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.BLKData;
import com.dreamtek.smartpos.deviceservice.aidl.CheckCardListener;
import com.dreamtek.smartpos.deviceservice.aidl.DRLData;
import com.dreamtek.smartpos.deviceservice.aidl.EMVHandler;
import com.dreamtek.smartpos.deviceservice.aidl.EMVTransParams;
import com.dreamtek.smartpos.deviceservice.aidl.IssuerUpdateHandler;
import com.dreamtek.smartpos.deviceservice.aidl.OnlineResultHandler;
import com.dreamtek.smartpos.deviceservice.aidl.RequestACTypeHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.LogUtil;
import base.MyApplication;

import static cn.verifone.atoolsjar.utils.Utils.asc2Bcd;

/**
 * Created by Simon on 2021/11/1
 */
public class EmvModule extends TestModule {

//    private static final String TAG = "EmvModule";
//
//    boolean isAutoStartEMV =false;
//
//    String startEMVParam;
//
//    String setCtlsPreProcessParam; //检卡前调用，只对非接有效
//    String setBypassRemainingPINParam; //startEMV前，设置参数，只对接触EMV有效
//    String setEmvProcessingRequestOnlineParam; //确认卡号时，设置强制联机，只对接触EMV有效
//    String setRequestACTypeParam; //Reset 1st GAC type AAC, TC, or ARQC,客户使用场景为当TC时，强制设置为ARQC,只适用于接触EMV
//    String registerKernelAIDParam; //非接注册内核
//
//
//    String importCardConfirmResultParam;
//    String importAppSelectionParam;
//    String importPinParam;
//    String onConfirmCertInfoParam;
//    String importOnlineResultParam;
//    String reqTLVDataParam;
//
//    String startEmvWithTransParamsParam;
//    String EMVTransParamsParam;
//
//
//    String onTransactionResultParam;
//    String onProccessResultParam;
//
//    String setEMVDataParam;
//    String getCardDataParam;
//
//    String appTLVDataMsg;
//    String emvDataMsg;
//    String cardDataMsg;
//
//
//    String expectedCARDInfo;
//    String expectedOnRequestOnlineProcessResultParam;
//    String expectedOnTransactionResultParam;
//    String expectedOnProccessResultParam;
//    String expectedGetAppTLVListParam;
//
//    String expectedCallbackPerformedParam;
//
//    Object emvLock = new Object();
//
//    String onRequestOnlineProcessResult = "false";
//
//    public String T_checkCardBlocking(String sCardOption, String sTimeout) throws RemoteException {
//
//       return T_checkCard_startEMV( sCardOption, sTimeout ,"", "","", "", "" , "", "", "", "", "" ,"","","","","","","","");
//    }
//    public String T_checkCardUnblocking(String sCardOption, String sTimeout) throws RemoteException {
//        // 没有 timeout 的为异步处理，发出检卡后，就返回了，可以去执行其他案例
//        // 超时时间使用 60秒！不要重复测试
//        // 检卡
//        Bundle cardOption = convert( sCardOption, null );
//        iemv.checkCard(cardOption, Integer.valueOf(sTimeout), checkCardListener );
//        LogUtil.d(TAG, "Waiting check card finished ...");
//        return "waiting for card, 60秒";
//    }
//    public String T_PartialEMV(String sCardOption, String sTimeout,
//                              String startEMV,
//                              String onConfirmCardInfo ,String getAppTLVList, String getCardData) throws RemoteException {
//        // 简易流程，有其他参数为全流程
//        if( startEMV.trim().length() > 0 ){
//            startEMV += "_processType=2=1" ;
//        }
//        return T_checkCard_startEMV( sCardOption, sTimeout, startEMV,  "","", "", "", onConfirmCardInfo, "", "", "",  getAppTLVList,"",getCardData,"","","","","","");
//    }
//
//   /*测试接口：
//   * checkCard
//   * startEMV
//   * importAmount
//   * importAppSelection
//   * importCertConfirmResult(无需测试)
//   * importCardConfirmResult
//   * importPin
//   * importOnlineResult/inputOnlineResult
//   * setEMVData
//   * getAppTLVList
//   * getCardData
//   * getEMVData
//   * registerKernelAID
//   * emvProcessingRequestOnline
//   * setCtlsPreProcess
//   * setIssuerUpdateHandler
//   * setIssuerUpdateScript
//   * setRequestACTypeCallBack
//   * setRequestACType
//   * setByPassAllPin
//   * */
//
//    public String T_checkCard_startEMV(String checkCard, String sTimeout,
//
//                            String startEMV, //startEMV参数
//
//                            String setCtlsPreProcess, //检卡前调用
//                            String setBypassRemainingPIN,//startEMV前，设置参数
//                            String setEmvProcessingRequestOnline, //确认卡号时，设置
//                            String setRequestACType, //Reset 1st GAC type AAC, TC, or ARQC,客户使用场景为当TC时，强制设置为ARQC
//                            String registerKernelAID, //非接注册内核,只适用于非接
//
//                            //startEMV callback调用时，需要import的参数
//                            String importCardConfirmResult,
//                            String importAppSelection,
//                            String importPin,
//                            /*String onConfirmCertInfo,*/
//                            String importOnlineResult,
//
//                            //--------------------//
////                            String getAppTLVList,   //格式为，9F37.95.9F26, 所有的tag会在请求联机时获取一次，在交易结果时获取一次。
//                            String setEMVData,   //设置EMV data的tag,格式为，5f2a
//                            String getCardData,   //获取card data数据，格式为，9F37.95.9F26, 所有的tag会在请求联机时获取一次，在交易结果时获取一次。
//
//                            //expected data and check if that is expected.
//                            String expectedCardInfo,
//                            String expectedOnRequestOnlineProcessResult,
//                            String expectedOnTransactionResult,
//                            String expectedOnProccessResult,
//                            String expectedGetAppTLVData,
//                            String expectedCallbackPerformed
//
//                              /*String onTransactionResult,
//                              String onProccessResult*/ ) throws RemoteException {
//
//        synchronized ( emvLock ){
//            try {
//                emvLock.wait(10);   // 等待 10毫秒，清除未完成交易的消息
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            checkCardResult = -1;
//            emvResult = "";
//        }
//
//
//        // 等待打印完成
//        if( 0 != printMode){
//            Message message1 = new Message();
//            message1.getData().putString("message", "Waiting print finished to start check card!");
//            handler.sendMessage(message1);
//
//            LogUtil.d(TAG, "waiting print finished ...");
//            ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(5);
//            LogUtil.d(TAG, "waiting print finished ...");
//            ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(5);
//            LogUtil.d(TAG, "waiting print finished ... ok");
//
//            try {
//                Thread.sleep( 200 );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        startEMVParam = startEMV;
//
//        setCtlsPreProcessParam = setCtlsPreProcess;  //检卡前调用
//        setBypassRemainingPINParam = setBypassRemainingPIN;//startEMV前，设置参数
//        setEmvProcessingRequestOnlineParam = setEmvProcessingRequestOnline; //确认卡号时，设置
//        setRequestACTypeParam = setRequestACType;
//        registerKernelAIDParam = registerKernelAID;
//
//        importCardConfirmResultParam = importCardConfirmResult;
//        importAppSelectionParam = importAppSelection;
//        importPinParam = importPin;
//        //onConfirmCertInfoParam = onConfirmCertInfo;
//        importOnlineResultParam = importOnlineResult;
//
//
//        //reqTLVDataParam = getAppTLVList;
//        setEMVDataParam = setEMVData;
//        getCardDataParam = getCardData;
//
//        expectedCARDInfo = expectedCardInfo;
//        expectedOnRequestOnlineProcessResultParam = expectedOnRequestOnlineProcessResult;
//        expectedOnTransactionResultParam = expectedOnTransactionResult;
//        expectedOnProccessResultParam = expectedOnProccessResult;
//        expectedGetAppTLVListParam = expectedGetAppTLVData;
//        expectedCallbackPerformedParam = expectedCallbackPerformed;
//        //onTransactionResultParam = onTransactionResult;
//        //onProccessResultParam = onProccessResult;
//
//
////        isAutoStartEMV = (startEMV.trim().length() > 0);
////        if( isAutoStartEMV ){
////            emvOption = convert( sEmvOption, null);
////            emvProcessType = Integer.valueOf( sEmvProcessType);
////            emvCallbackOption = convert( sEmvCallBack, null);
////        }
//
//        //交易预处理
//        if (setCtlsPreProcessParam != null && !setCtlsPreProcessParam.equals(""))
//            T_setCtlsPreProcess(setCtlsPreProcessParam);
//
//        // 检卡
//        Bundle cardOption = convert( checkCard, null );
//        iemv.checkCard(cardOption, Integer.valueOf(sTimeout), checkCardListener );
//        int checkCardRet = 0;
//        LogUtil.d(TAG, "Waiting check card finished ...");
//        synchronized ( emvLock ){
//            try {
//                emvLock.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            checkCardRet = checkCardResult;
//        }
//        LogUtil.d(TAG, "Check card: " + checkCardRet );
//        if( checkCardRet != 2 && checkCardRet != 3 ) {
//            // 非插卡、贴卡
//            return "Check card:" + checkCardRet;
//        }
//
//        String emvRet;
//        if( startEMV.trim().length() > 0 ){
//            LogUtil.d(TAG, "Waiting EMV finished ...");
//            synchronized (emvLock) {
//                try {
//                    emvLock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                emvRet = emvResult;
//            }
//            LogUtil.d(TAG, "EMV return: "+ emvRet );
//            return emvRet;
//        } else {
//            LogUtil.d(TAG, "Check card:" + checkCardRet );
//            return "Check card:" + checkCardRet;
//        }
//    }
//
//
//    /*测试接口：
//     * checkCard
//     * startEmvWithTransParams
//     * importAmount
//     * importAppSelection
//     * importPin
//     * importCertConfirmResult(无需测试)
//     * importCardConfirmResult
//     * importOnlineResult/inputOnlineResult
//     * setEMVData
//     * getAppTLVList
//     * getCardData
//     * getEMVData
//     * registerKernelAID
//     * emvProcessingRequestOnline
//     * setCtlsPreProcess
//     * setIssuerUpdateHandler
//     * setIssuerUpdateScript
//     * setRequestACTypeCallBack
//     * setRequestACType
//     * setByPassAllPin
//     * */
//
//    public String T_checkCard_startEmvWithTransParams(String checkCard, String sTimeout,
//
//                            String startEmvWithTransParams, //startEMV参数
//
//                            String setCtlsPreProcess, //检卡前调用
//                            String setBypassRemainingPIN,//startEMV前，设置参数
//                            String setEmvProcessingRequestOnline, //确认卡号时，设置
//                            String setRequestACType, //Reset 1st GAC type AAC, TC, or ARQC,客户使用场景为当TC时，强制设置为ARQC
//                            String registerKernelAID, //非接注册内核,只适用于非接
//
//                            //startEMV callback调用时，需要import的参数
//                            String importCardConfirmResult,
//                            String importAppSelection,
//                            String importPin,
//                            /*String onConfirmCertInfo,*/
//                            String importOnlineResult,
//
//                            //--------------------//
//                            String getAppTLVList,   //格式为，9F37.95.9F26, 所有的tag会在请求联机时获取一次，在交易结果时获取一次。
//
//                            String setEMVData,   //设置EMV data的tag,格式为，5f2a
//                            String getCardData,   //获取card data数据，格式为，9F37.95.9F26, 所有的tag会在请求联机时获取一次，在交易结果时获取一次。
//                            String EMVTransParams // 用于startEmvWithTransParams 里面的第二个参数
//                              /*String onTransactionResult,
//                              String onProccessResult*/ ) throws RemoteException {
//
//        synchronized ( emvLock ){
//            try {
//                emvLock.wait(10);   // 等待 10毫秒，清除未完成交易的消息
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            checkCardResult = -1;
//            emvResult = "";
//        }
//
//
//        // 等待打印完成
//        if( 0 != printMode){
//            Message message1 = new Message();
//            message1.getData().putString("message", "Waiting print finished to start check card!");
//            handler.sendMessage(message1);
//
//            LogUtil.d(TAG, "waiting print finished ...");
//            ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(5);
//            LogUtil.d(TAG, "waiting print finished ...");
//            ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(5);
//            LogUtil.d(TAG, "waiting print finished ... ok");
//
//            try {
//                Thread.sleep( 200 );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        setCtlsPreProcessParam = setCtlsPreProcess;  //检卡前调用
//        setBypassRemainingPINParam = setBypassRemainingPIN;//startEMV前，设置参数
//        setEmvProcessingRequestOnlineParam = setEmvProcessingRequestOnline; //确认卡号时，设置
//        setRequestACTypeParam = setRequestACType;
//        registerKernelAIDParam = registerKernelAID;
//
//        importCardConfirmResultParam = importCardConfirmResult;
//        importAppSelectionParam = importAppSelection;
//        importPinParam = importPin;
//        //onConfirmCertInfoParam = onConfirmCertInfo;
//        importOnlineResultParam = importOnlineResult;
//
//        startEmvWithTransParamsParam = startEmvWithTransParams;
//        EMVTransParamsParam = EMVTransParams;
//
//        reqTLVDataParam = getAppTLVList;
//        setEMVDataParam = setEMVData;
//        getCardDataParam = getCardData;
//
//        //onTransactionResultParam = onTransactionResult;
//        //onProccessResultParam = onProccessResult;
//
//
////        isAutoStartEMV = (startEMV.trim().length() > 0);
////        if( isAutoStartEMV ){
////            emvOption = convert( sEmvOption, null);
////            emvProcessType = Integer.valueOf( sEmvProcessType);
////            emvCallbackOption = convert( sEmvCallBack, null);
////        }
//
//        //交易预处理
//        if (setCtlsPreProcessParam != null && !setCtlsPreProcessParam.equals(""))
//            T_setCtlsPreProcess(setCtlsPreProcessParam);
//
//        // 检卡
//        Bundle cardOption = convert( checkCard, null );
//        iemv.checkCard(cardOption, Integer.valueOf(sTimeout), checkCardListener_ForStartEMVWithTransParams );
//        int checkCardRet = 0;
//        LogUtil.d(TAG, "Waiting check card finished ...");
//        synchronized ( emvLock ){
//            try {
//                emvLock.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            checkCardRet = checkCardResult;
//        }
//        LogUtil.d(TAG, "Check card: " + checkCardRet );
//        if( checkCardRet != 2 && checkCardRet != 3 ) {
//            // 非插卡、贴卡
//            return "Check card:" + checkCardRet;
//        }
//
//        String emvRet;
//        if( startEmvWithTransParams.trim().length() > 0 ){
//            LogUtil.d(TAG, "Waiting EMV finished ...");
//            synchronized (emvLock) {
//                try {
//                    emvLock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                emvRet = emvResult;
//            }
//            LogUtil.d(TAG, "EMV return: "+ emvRet );
//            return emvRet;
//        } else {
//            LogUtil.d(TAG, "Check card:" + checkCardRet );
//            return "Check card:" + checkCardRet;
//        }
//    }
//
//
//    public String T_getAppTLVList( String tlvList) throws RemoteException {
//
//        LogUtil.d(TAG, "T_getAppTLVList" + tlvList);
//
//        String[] tlvList1 = convert2StringArray( tlvList, '.');
//
//        String strs = iemv.getAppTLVList(tlvList1);
//
//        LogUtil.d(TAG, "TLV Data: " + strs);
//
//        return strs;
//
//    }
//
//    String[] convert2StringArray(String list, char regex) {
//        String regexItem = String.valueOf(regex);
//        LogUtil.d(TAG, "Convert: " + list + " , " + regexItem);
//        List<String> keyValueLs = new ArrayList<String>();
//        if (regexItem.equals(".")) {
//            regexItem = "\\.";
//        }
//        String[] keyValues = list.split(regexItem);
//        for (String keyValue : keyValues
//        ) {
//            keyValue = keyValue.trim();
//            if (keyValue.length() > 0) {
//                keyValueLs.add(keyValue);
//            }
//        }
//        LogUtil.d(TAG, "add items: " + keyValueLs.size());
//        return keyValues;
//    }
//
//
//    public Map<String, java.lang.Integer> convertStr2Map(String str, String itemSeparator, String keyValueSeparator) {
//        Map<String, java.lang.Integer> map = new HashMap<>();
//        if (TextUtils.isEmpty(str) || !str.contains(itemSeparator) || !str.contains(keyValueSeparator))
//            return map;
//        String[] items = str.split(itemSeparator);
//        for (String item : items) {
//            String[] keyValue = item.split(keyValueSeparator);
//            if (keyValue.length < 2) {
//                continue;
//            } else {
//                map.put(keyValue[0], Integer.valueOf(keyValue[1]));
//            }
//        }
//        return map;
//    }
//
//
////    public String T_checkCard(String sCardOption, String sTimeout, String sEmvOption, String sEmvProcessType, String sEmvCallBack) throws RemoteException {
////
////        synchronized ( emvLock ){
////            try {
////                emvLock.wait(10);   // 等待 10毫秒，清除未完成交易的消息
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            checkCardResult = -1;
////            emvResult = "";
////        }
////
//////        isAutoStartEMV = (sEmvOption.length() > 0);
//////        if( isAutoStartEMV ){
//////            emvOption = convert( sEmvOption, null);
//////            emvProcessType = Integer.valueOf( sEmvProcessType);
//////            emvCallbackOption = convert( sEmvCallBack, null);
//////        }
////
////        Bundle cardOption = convert( sCardOption, null );
////        iemv.checkCard(cardOption, Integer.valueOf(sTimeout), checkCardListener );
////        int checkCardRet = 0;
////        LogUtil.d(TAG, "Waiting check card finished ...");
////        synchronized ( emvLock ){
////            try {
////                emvLock.wait();
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            checkCardRet = checkCardResult;
////        }
////        LogUtil.d(TAG, "Check card: " + checkCardRet );
////        if( checkCardRet != 2 && checkCardRet != 3 ) {
////            return "Check card:" + checkCardRet;
////        }
////        String emvRet;
////        if( isAutoStartEMV ){
////            LogUtil.d(TAG, "Waiting EMV finished ...");
////            synchronized (emvLock) {
////                try {
////                    emvLock.wait();
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                emvRet = emvResult;
////            }
////            LogUtil.d(TAG, "EMV return: "+ emvRet );
////            return emvRet;
////        } else {
////            return "Check card:" + checkCardRet;
////        }
////    }
//    public void T_stopCheckCard() throws RemoteException {
//        iemv.stopCheckCard();
//    }
//
//    public boolean T_updateAID(String operation, String aidType, String aid) throws RemoteException {
//        return iemv.updateAID( Integer.valueOf(operation), Integer.valueOf(aidType), aid);
//    }
//
//    public boolean T_updateRID(String operation, String rid) throws RemoteException {
//        return iemv.updateRID(Integer.valueOf(operation), rid);
//    }
//
//    public String T_getAndCheckAID(String aidType, String aid) throws RemoteException {
//        int type = Integer.valueOf(aidType);
//        int foundRecord = 0, recordNum=0;
//        String[] tagStrlist;
//        int Flag=0;
//
//        //Get AID tag 9F26
//        String tag9F26 = findTag(String.valueOf(aid), "9F06");
//
//        String[] Str = iemv.getAID(type);
//
//        if((type == 1 || type == 2) && Str != null) {
//
//            int iMax = Str.length - 1;
//            int i;
//            for (i = 0; i< Str.length ; i++) {
//
//                String aidValueInKernel = findTag(String.valueOf(Str[i]), "9F06");
//
//                if (aidValueInKernel.compareTo(tag9F26) == 0) {
//
//                    foundRecord = 1;
//
//                    logUtils.addCaseLog("\nAID PARAMS FOUND :");
//                    logUtils.addCaseLog("\n" + convertTLVinLines(String.valueOf(Str[i])));
//
//                    recordNum = i;
//                    break;
//
//                }
//            }
//
//            if (foundRecord == 1) {
//                String valueInput=null;
//                String valueInKernel=null;
//                String[] TagStrlist;
//
//                if (type==1){
//                    TagStrlist = new String[]{/*"9F15",*/"9F06", "9F09", "DF01", "9F1B", "DF15", "DF17", "DF16", "97",
//                            "DF14", "9F1A", "5F2A", "9F35", "9F33", "9F40", "DF12", "DF11", "DF13", "9F7B", "9F66", "DF18", "9F1D", "97", "9F01", "DF8117", "DF8118", "DF8119", "DF811F"};
//                }else{
//                    TagStrlist = new String[]{"DF14", "5F2A", "DF17", "9F09", "DF16", "9F1D", "9F7B", "DF8119",
//                            "DF811E", "9F33", "9F40", "DF8118", "DF18", "9F35", "97", "DF12","9F1A","9F15","DF15","DF811B","DF11","9F06","DF01","DF23","9F1B","DF20","DF19","DF21","9F66"};
//
//                }
//
//                //find tags in the records
//                int j = 0;
//                StringBuilder stringBuilder = new StringBuilder();
//                for (j = 0; j < TagStrlist.length; j++) {
//
//                    valueInput = findTag1(String.valueOf(aid), TagStrlist[j]);
//                    valueInKernel = findTag1(String.valueOf(Str[recordNum]), TagStrlist[j]);
//
//                    //当输入预期不存在时，继续检查下一个tag
//                    if (valueInput == null)
//                        continue;
//
//                    //当输入预期值为""时，表示只检查改tag是否存在，不检查该tag的值
//                    if (valueInput == "" && valueInKernel != null && valueInKernel != "")
//                        continue;
//
//                    if (valueInKernel != null && valueInKernel.compareTo(valueInput) != 0) {
//                        logUtils.addCaseLog("\nTag: " + TagStrlist[j] + " Value Error");
//                        Flag++;
//                        stringBuilder.append(TagStrlist[j]).append(" Value Error ");
//                    }
//                }
//
//                if (Flag > 0) {
//                    return stringBuilder.toString().trim();
//                } else
//                    return "true";
//            } else
//                return "EMV AID NOT FOUND";
//        }
//
//        return "null";
//    }
//
//    public String T_getAID(String sType, String AIDValue) throws RemoteException {
//        int type = Integer.valueOf(sType);
//        int foundRecord = 0, recordNum = 0;
//
//        String[] tagStrlist;
//
//        //Get AID tag 9F26
//        //String tag9F26 = findTag(String.valueOf(AID), "9F06");
//
//        String[] Str = iemv.getAID(type);
//
//        if(type == 1 || type == 2) {
//
//            int iMax = Str.length - 1;
//            int i;
//            for (i = 0; i< Str.length ; i++) {
//
//                String aidValueInKernel = findTag(String.valueOf(Str[i]), "9F06");
//
//                if (aidValueInKernel.compareTo(AIDValue) == 0) {
//
//                    foundRecord = 1;
//
//                    logUtils.addCaseLog("\nAID PARAMS FOUND :");
//                    logUtils.addCaseLog("\n" + convertTLVinLines(String.valueOf(Str[i])));
//
//                    recordNum = i;
//                    break;
//
//                }
//            }
//            if (foundRecord == 1) {
//                return "AID PARAMS FOUNDED: " + convertTLVinLines(String.valueOf(Str[recordNum]));
//            }
//
//        }
//        return  "AID PARAMS NOT FOUND";
//    }
//
//    public String T_getRID() throws RemoteException {
//        return Arrays.toString(iemv.getRID());
//    }
//
//    public String T_getAndCheckRID(String rid) throws RemoteException {
//
//        int foundRecord = 0, recordNum=0;
//        String tag9F26 = findTag(String.valueOf(rid), "9F06");
//        int flag=0;
//
//        String[] Str = iemv.getRID();
//
// //       int iMax = Str.length - 1;
//        if (Str!=null){
//            for (int i = 0; i<Str.length ; i++) {
//
//                String aidValueInKernel = findTag(String.valueOf(Str[i]), "9F06");
//
//                if (aidValueInKernel.compareTo(tag9F26) == 0) {
//
//                    foundRecord = 1;
//
//                    logUtils.addCaseLog("\nRID PARAMS FOUND :");
//                    logUtils.addCaseLog("\n" + convertTLVinLines(String.valueOf(Str[i])));
//
//                    recordNum = i;
//                    break;
//
//                }
//            }
//        }
////            if (i == iMax)
////                return "AID PARAMS NOT FOUND";
//
//        if (foundRecord == 1) {
//            //find tags in the records
//            String[] rid_TagStrlist = {"9F06", "9F22", "DF05", "DF03", "DF07", "DF06", "DF02", "DF04", "DF31"};
//
//            int j;
//            StringBuilder stringBuilder = new StringBuilder();
//            for (j = 0;j< rid_TagStrlist.length; j++) {
//                String valueInput = findTag1(String.valueOf(rid), rid_TagStrlist[j]);
//                String valueInKernel = findTag1(String.valueOf(Str[recordNum]), rid_TagStrlist[j]);
//
////                if (j>rid_TagStrlist.length)
////                    break;
//
//                //当输入预期不存在时，继续检查下一个tag
//                if (valueInput == null){
//                    Log.d(TAG,"value input == null");
//                }
//                else if(valueInKernel != null && valueInKernel.compareTo(valueInput) != 0){
//                    logUtils.addCaseLog("\nCAPK Tag: " + rid_TagStrlist[j] + " Value Error");
//                    flag++;
//                    stringBuilder.append(rid_TagStrlist[j]).append(" Value Error ");
//
//                }//当输入预期值为""时，表示只检查改tag是否存在，不检查该tag的值
//                else if (valueInput.equals("") && valueInKernel != null && !valueInKernel.equals("")){
//                    Log.d(TAG,"输入预期值为空");
//                } else if (j == (rid_TagStrlist.length - 1)){
//                    break;//return "true" /*+ convertTLVinLines(String.valueOf(Str[recordNum]))*/;
//                }
//            }
//            Log.d(TAG,"flag = "+flag);
//
//            if (flag == 0){
//                return "true";
//            }else
//                return stringBuilder.toString().trim();
//        }
//        else{
//            if (Str == null)
//                return "null";
//            else
//                return "RID PARAMS NOT FOUND";
//        }
//
//
//    }
//
//    public void T_importAmount(String amount) throws RemoteException {
//        iemv.importAmount( Long.valueOf(amount) );
//    }
//
//    public void T_importAppSelect(String index) throws RemoteException {
//        iemv.importAppSelection(Integer.valueOf(index));
//    }
//
//    public void T_importPin(String option, String pin) throws RemoteException {
//        iemv.importPin(Integer.valueOf(option), StringUtil.hexStr2Bytes(pin) );
//    }
//
//    public void T_importCertConfirmResult(String option) throws RemoteException {
//        iemv.importCertConfirmResult(Integer.valueOf(option) );
//    }
//
//    public void T_importCardConfirmResult(String pass) throws RemoteException {
//        iemv.importCardConfirmResult( Boolean.valueOf(pass) );
//    }
//
//    public void T_importOnlineResult(String sOnlineResult) throws RemoteException {
//        Bundle onlineResult = convert( sOnlineResult, null );
//        iemv.importOnlineResult(onlineResult, onlineResultHandler );
//    }
////
////    public void T_startEMV(String processType, String sBundle, ) throws RemoteException {
////
////        EMVHandler emvhandler = emvHandler;
////        Bundle intent = convert( sBundle, null );
////
////        Bundle cardOption = new Bundle();
////        cardOption.putBoolean("supportMagCard", true);
////        cardOption.putBoolean("supportSmartCard", true);
////        cardOption.putBoolean("supportCTLSCard", true);
////        String msg;
////
////        if (intent.getBoolean("doNotChangeCardType")) {
////            int cardType = intent.getInt("cardType");
////            if (cardType == 0)
////                logUtils.addCaseLog("Please insert the card");
////            else if (cardType == 1)
////                logUtils.addCaseLog("Please swipe the card");
////        } else
////            logUtils.addCaseLog("Please insert/wave/swipe your card");
////
////        T_checkCard(cardOption, 30, );
////    }
//
//    public void T_abortEMV() throws RemoteException {
//        iemv.abortEMV();
//    }
//
//    public String T_getCardData(String tagName) throws RemoteException {
//
//        String tmp = StringUtil.byte2HexStr(iemv.getCardData(tagName));
//
//        LogUtil.d(TAG, "YAPING : getCardData tagName"+tagName+"TagVale"+ tmp);
//        return tmp;
//    }
//
//    // 使用 ; 分隔 多个值
//    public void T_setEMVData( String tlvList) throws RemoteException {
//        T_setEMVData( tlvList, ";", "=");
//    }
//    public void T_setEMVData( String tlvList, String regexItem, String regexKeyValue) throws RemoteException {
//        if( regexItem.length() == 0 ) {
//            regexItem = ".";
//        }
//        if( regexKeyValue.length() == 0 ) {
//            regexKeyValue = "-";
//        }
//        List<String[]> list = convert( tlvList, regexItem, regexKeyValue);
//        List<String> list2 = new ArrayList<>();
//        for (String[] tv : list  ) {
//            if( tv.length == 1 ) {
//                list2.add( tv[0] );
//            } else if( tv.length == 2 ) {
//                list2.add( tv[0]+ String.format("%02d", tv[1].length()/2 ) + tv[1] );
//            }
//        }
//        iemv.setEMVData(list2);
//    }
//
//
//
//    public String T_getEMVData(String tagName) throws RemoteException {
//
//
//        String str = iemv.getEMVData(tagName);
//
//        LogUtil.d(TAG, "YAPING : getEMVData tagName"+tagName+"TagVale"+ str);
//
//        logUtils.addCaseLog("getEMVData execute" + tagName);
//        if (null != str && str.length() > 0) {
//            logUtils.addCaseLog("getEMVData return " + str);
//        } else {
//            logUtils.addCaseLog("getEMVData return null");
//        }
//        return str;
//    }
//
//
//
//    protected String getEMVDataAndCheck( String list, String type ) {
//        // getEMVData=PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY=0
//        List<String[]> keyValues = convert( list, ".", "-" );
//
//        String failure = "";
//        String msg;
//        LogUtil.d(TAG, "Type: " + type );
//        for ( String[] kv: keyValues  ) {
//            try {
//                if( null == kv ){
//                    Log.w(TAG, "no data was read from list");
//                    printMsgToolAppend("no data was read from list", Log.INFO );
//                    continue;
//                }
//                if( kv.length == 0 ){
//                    Log.w(TAG, "empty data was read from list");
//                    printMsgToolAppend("empty data was read from list", Log.INFO );
//                    continue;
//                }
//                if( null == kv[0]  ){
//                    Log.w(TAG, "no key was read from list");
//                    printMsgToolAppend("no key was read from list", Log.INFO );
//                }
//                if( kv[0].length() == 0  ){
//                    Log.w(TAG, "empty key was read from list");
//                    printMsgToolAppend("empty key was read from list", Log.INFO );
//                }
//                String value;
//                if( 0 == type.compareTo( "getEMVData") ){
//                    value = iemv.getEMVData( kv[0] );
//                } else if( 0 == type.compareTo( "getCardData") ){
//                    value = StringUtil.byte2HexStr(iemv.getCardData( kv[0] ) );
//                } else {
//                    value = "";
//                }
//                if ( null == value ) {
//                    msg = String.format("Got[%s][null]", kv[0]  );
//                    Log.w(TAG, msg );
//                    printMsgToolAppend(msg, Log.INFO );
//
//                    if( kv.length > 1 ){
//
//                    }
//                } else if( kv.length > 1 ){
//                    if( 0 == kv[1].compareToIgnoreCase( value)) {
//                        msg = String.format("Success check[%s][%s]", kv[0], kv[1]  );
//                        Log.i(TAG, msg );
//                        printMsgToolAppend(msg, Log.DEBUG );
//                    } else {
//                        msg = String.format("Failure to check[%s][%s], got[%s] ", kv[0], kv[1] ,value );
//                        failure += msg;
//                        Log.e(TAG, msg );
//                        printMsgToolAppend(msg, Log.ERROR );
//                    }
//                }else if ( value.length() > 0) {
//                    msg = String.format("Got[%s][%s], skip to check", kv[0], value  );
//                    LogUtil.d(TAG, msg );
//                    printMsgToolAppend(msg, Log.DEBUG );
//                } else {
//                    msg = String.format("Cannot Get[%s][%s]", kv[0], value  );
//                    Log.w(TAG, msg );
//                    printMsgToolAppend(msg, Log.INFO );
//                }
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//        return failure;
//        // getCardData=9f51.9F79.DF71.DF79.8E=0
//
//        // setEMVData=9f51-xxx.9F79-xxxx.DF71.DF79.8E=0
//
//    }
//
//    //已废弃，不需要测试
////    public int T_getProcessCardType() throws RemoteException {
////        int ret = iemv.getProcessCardType();
////        logUtils.addCaseLog("No card type:" + ret);
////        return ret;
////    }
//
//    public void T_registerKernelAID( String registerAidList) throws RemoteException {
//
//        Map<String, java.lang.Integer> map = convertStr2Map(registerAidList,"","=");
//        iemv.registerKernelAID(map);
//    }
//
//    public boolean T_updateVisaAPID(int operation, DRLData drlData) throws RemoteException {
//        return iemv.updateVisaAPID(operation,drlData);
//
//    }
//
//    public boolean T_updateCardBlk(int operation, BLKData blkData, int type) throws RemoteException {
//        return iemv.updateCardBlk(operation, blkData, type);
//    }
//
//    public int T_emvProcessingRequestOnline() throws RemoteException {
//        int ret = iemv.emvProcessingRequestOnline();
//        return ret;
//    }
//    public String T_getCAPK(String sType, String RID, String capkIndex) throws RemoteException {
//
//
//        int type = Integer.valueOf(sType);
//
//
//        if(type == 1 || type == 2) {
//
//            String[] Str = iemv.getCAPK(type);
//
//            int iMax = Str.length - 1;
//            for (int i = 0; ; i++) {
//
//                String ridValueInKernel = findTag(String.valueOf(Str[i]), "9F06");
//                String capkIndexInKernel = findTag(String.valueOf(Str[i]), "9F22");
//
//                if (ridValueInKernel.compareTo(RID) == 0 && capkIndexInKernel.compareTo(capkIndex) == 0) {
//                    logUtils.addCaseLog("\nCAPK PARAMS FOUNDED :");
//                    logUtils.addCaseLog("\n" + convertTLVinLines(String.valueOf(Str[i])));
//                    return "CAPK PARAMS FOUNDED: " + convertTLVinLines(String.valueOf(Str[i]));
//                }
//                if (i == iMax)
//                    return "CAPK PARAMS NOT FOUND";
//            }
//        } else {
//            return  "TYPE ERROR";
//        }
//    }
//
//    public void T_enableTrack(int trkNum) throws RemoteException {
//        iemv.enableTrack(trkNum);
//    }
//
//
//    public void T_checkCardMs(Bundle cardOption, long timeout, CheckCardListener listener) throws RemoteException {
//        iemv.checkCardMs(cardOption, timeout, listener);
//    }
//
//    //kernel 版本获取
//    public void T_getCtlsSpecVer()throws RemoteException {
//
//        Map<String, String> map= new HashMap<>();
//        map = iemv.getCtlsSpecVer();
//
//        String Ver = "JCB:" + map.get("JCB") + ";\n" + "AMEX:" + map.get("AMEX") + ";\n" +"VISA:" + map.get("VISA") + ";\n"+ "DISCOVER:" + map.get("DISCOVER") + ";\n"+ "RUPAY:" + map.get("RUPAY") + ";\n"+ "PURE:" + map.get("PURE")+ ";\n" + "MIR:" + map.get("MIR");
//
//        logUtils.addCaseLog("CTLSSpecVersion: " + Ver);
//    }
//
//    public boolean T_setCtlsPreProcess(String inParams) throws RemoteException {
//
//        Bundle bundle = convert( inParams, null);
//        return iemv.setCtlsPreProcess(bundle);
//    }
//
//    public boolean T_setByPassAllPin(String inParams) throws RemoteException {
//
//        LogUtil.d(TAG, "setByPassAllPin : " + inParams);
//        iemv.setByPassAllPin(Boolean.valueOf(inParams));
//        if (iemv.isByPassAllPin())
//            return true;
//        else
//            return false;
//    }
//
//    public boolean T_emvProcessingRequestOnline(String inParams) throws RemoteException {
//        int ret = -1;
//
//        if(Boolean.valueOf(inParams)) {
//            ret = iemv.emvProcessingRequestOnline();
//        }
//
//        if (ret == 0)
//            return true;
//        else
//            return false;
//    }
//
//    public void T_setRequestACTypeCallBack(RequestACTypeHandler requestACTypehandler) throws RemoteException {
//        iemv.setRequestACTypeCallBack(requestACTypehandler);
//    }
//
//    public void T_setRequestACType(String requestACType) throws RemoteException {
//
//        LogUtil.d(TAG, "setRequestACType = "+requestACType);
//        iemv.setRequestACType(Integer.valueOf(requestACType));
//    }
//
//    public void T_setIssuerUpdateHandler(IssuerUpdateHandler issuerUpdateHandler) throws RemoteException {
//
//        iemv.setIssuerUpdateHandler(issuerUpdateHandler);
//    }
//
//    public void T_setIssuerUpdateScript() throws RemoteException {
//        iemv.setIssuerUpdateScript();
//    }
//
//    public boolean T_updateGroupParam(String operation, String paramType, String params,String groupName, String drlID, String clssFloorLimit, String clssTransLimit, String cvmRequiredLimit) throws RemoteException{
//
//        Bundle bundle = new Bundle();
//
//        byte[] drlID1 = new byte[16];
//        byte[] clssFloorLimit1 = new byte[6];
//        byte[] clssTransLimit1 = new byte[6];
//        byte[] cvmRequiredLimit1 = new byte[6];
//
//        if (drlID != null && !drlID.equals("")) {
//            drlID1 = StringUtil.hexStr2Bytes(drlID);
//            clssFloorLimit1 = StringUtil.hexStr2Bytes(clssFloorLimit);
//            clssTransLimit1 = StringUtil.hexStr2Bytes(clssTransLimit);
//            cvmRequiredLimit1 = StringUtil.hexStr2Bytes(cvmRequiredLimit);
//            DRLData drlData = new DRLData(drlID1,clssFloorLimit1,clssTransLimit1,cvmRequiredLimit1);
//
//            bundle.putParcelable("DRLData",drlData);
//        }
//
//        bundle.putString("groupName",groupName);
//
//        return iemv.updateGroupParam(Integer.valueOf(operation),Integer.valueOf(paramType), params,bundle );
//    }
//
//
//
//    String msg;
//    String str;
//    Bundle importData;
//
//    boolean onSelectApplicationCallback=false, onConfirmCardInfoCallback=false, onRequestInputPINCallback=false,onRequestOnlineProcessCallback=false,onTransactionResultCallback=false,onProccessResultCallback=false;
//    private String emvResult = "";
//
//    protected EMVHandler emvHandler = new EMVHandler.Stub() {
//
//        @Override
//        public void onRequestAmount() throws RemoteException {
//            Log.e(TAG, "onRequestAmount should never be called");
//        }
//
//        @Override
//        public void onSelectApplication(List<Bundle> appList) throws RemoteException {
//            onSelectApplicationCallback=true;
//            LogUtil.d(TAG, "callback: onSelectApplication");
//
//            String options = importAppSelectionParam; //emvCallbackOption.getString("onSelectApplication");
//            // 第一个参数为应用选择的索引，-1 表示直接返回；
//            // 之后的参数（可选），为应用信息，用于比较，使用.分割——不一致时，打印error
//            String[] option = options.split(";");
//            int index = -1;
//            if( option.length > 0 ) {
//                index = Integer.valueOf( option[0] );
//            }
//
//            if( index < 0 ){
//                T_abortEMV();
//                synchronized (emvLock){
//                    emvResult = "-1, onSelectApplication";
//                    emvLock.notify();
//                }
//                return;
//            }
//
//            int j = 1;
//            String appinfo="";
//            for (Bundle app : appList) {
//                String str1 = app.getString("aidName");
//                String str2 = app.getString("aidLabel");
//                String str3 = app.getString("aid");
//
//                appinfo = appinfo + (j++ + ".aidName=" + str1 + " aidLabel=" + str2 + " aid=" + str3+"\n");
//
////                if( option.length > j ) {
////                    if (0 != option[j].compareToIgnoreCase(str1 + "," + str2 + "," + str3)) {
////                        Log.e(TAG, "");
////                    }
////                }
//            }
//
//            logUtils.addCaseLog("onSelectApplication callback is performed");
//            logUtils.addCaseLog(appinfo);
//
////            if( option.length > 1 ){
////                int i = 1;
////                for (Bundle app : appList) {
////                    String str1 = app.getString("aidName");
////                    String str2 = app.getString("aidLabel");
////                    String str3 = app.getString("aid");
////
////                    msg = msg + (i++ + ".aidName=" + str1 + " aidLabel=" + str2 + " aid=" + str3+"\n");
////                    if( option.length > i ){
////                        if( 0 != option[i].compareToIgnoreCase(str1 + "," + str2  + "," + str3) ){
////                            Log.e(TAG, "");
////                        }
////                        ++i;
////                    }
////                }
////            }
////            Message message1 = new Message();
////            message1.getData().putString("message", msg);
////            handler.sendMessage(message1);
//
//            iemv.importAppSelection( index );
//        }
//
//        @Override
//        public void onConfirmCardInfo(Bundle info) throws RemoteException {
//
//            onConfirmCardInfoCallback = true;
//            LogUtil.d(TAG, "callback: onConfirmCardInfo" );
//
//            //设置交易强制联机
//            if (setEmvProcessingRequestOnlineParam != null && !setEmvProcessingRequestOnlineParam.equals(""))
//              T_emvProcessingRequestOnline(setEmvProcessingRequestOnlineParam);
//
////            Looper.prepare();
//            // importCardConfirmResult=true=2
//            //;pan.track2.card sn.service code. expired date. card type
//            //;PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY
//            //;9f51.9F79.DF71.DF79.8E
//            // getEMVData=PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY=0
//            // getCardData=9f51.9F79.DF71.DF79.8E=0
//
//            // setEMVData=9f51-xxx.9F79-xxxx.DF71.DF79.8E=0
//
//            // getEMVData2=PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY=0
//            // getCardData2=9f51.9F79.DF71.DF79.8E=0
//
//            Bundle bundle = convert( importCardConfirmResultParam, null);
//
////
////
////            String options = onConfirmCardInfoParam; // emvCallbackOption.getString("onConfirmCardInfo", "");
////            // 第一个参数为确认卡信息结果：true, false, 其他为不执行确认，带执行后面getdata后，再返回
////            // 第二参数为提供的卡信息， 使用 . 分割多个，设置该参数时会进行比较，不设置时，使用 空格; 忽略该参数
////            // pan.track2.card sn. service code. expired date. card type
////            // 第3参数为获取的EMV的TAG (getEMVData)，使用 . 分割多个标签
////            // - 分割预期结果用于对比（可选）
////            // 第4个参数为 getCardData，使用 . 分割多个标签， - 分割预期结果用于对比（可选）
////            String[] option = options.split(";");
//
//            String result = "onConfirmCardInfo callback, \nPAN:" + info.getString("PAN") +
//                    "\nTRACK2:" + info.getString("TRACK2") +
//                    "\nCARD_SN:" + info.getString("CARD_SN")+
//                    "\nSERVICE_CODE:" + info.getString("SERVICE_CODE") +
//                    "\nEXPIRED_DATE:" + info.getString("EXPIRED_DATE") +
//                    "\nCARD_TYPE:" + info.getInt("CARD_TYPE");
//            printMsgTool(result, Log.DEBUG );
//
//
//            logUtils.addCaseLog("");
//            logUtils.addCaseLog("onConfirmCardInfo callback is performed \nPAN:" + info.getString("PAN"));
//            logUtils.addCaseLog("TRACK1:" + info.getString("TRACK1"));
//            logUtils.addCaseLog("TRACK2:" + info.getString("TRACK2"));
//            logUtils.addCaseLog("CARD_SN:" + info.getString("CARD_SN"));
//            logUtils.addCaseLog("SERVICE_CODE:" + info.getString("SERVICE_CODE"));
//            logUtils.addCaseLog("EXPIRED_DATE:" + info.getString("EXPIRED_DATE"));
//            logUtils.addCaseLog("CARD_TYPE:" + info.getInt("CARD_TYPE"));
//
//            if (expectedCARDInfo != null) { //比较卡片数据是否与预期相同
//
//                //解析预期数据
////                BundleConfig[] bundleConfigs = new BundleConfig[]{
////                        new BundleConfig("PAN", BundleConfig_String),
////                        new BundleConfig("TRACK1", BundleConfig_String),
////                        new BundleConfig("TRACK2", BundleConfig_String),
////                        new BundleConfig("CARDSN", BundleConfig_String),
////                        new BundleConfig("SERVICECODE", BundleConfig_String),
////                        new BundleConfig("EXPIREDDATE", BundleConfig_String),
////                        new BundleConfig("CARDTYPE", BundleConfig_int),
////                };
//                Bundle param = convert(expectedCARDInfo, null);
//
//                String pan = param.getString("PAN");
//                String track1 = param.getString("TRACK1");
//                String track2 = param.getString("TRACK2");
//                String cardsn = param.getString("CARDSN");
//                String servicecode = param.getString("SERVICECODE");
//                String expireddate = param.getString("EXPIREDDATE");
//                int cardtype = param.getInt("CARDTYPE");
//
//                if ((pan !=null && pan.compareTo(info.getString("PAN")) != 0)
////                        || (track1 !=null && track1.compareTo(info.getString("TRACK1")) != 0)
//                        || (track2 !=null && track2.compareTo(info.getString("TRACK2")) != 0)
//                        || (cardsn !=null && cardsn.compareTo(info.getString("CARD_SN")) != 0)
//                        || (servicecode !=null && servicecode.compareTo(info.getString("SERVICE_CODE")) != 0)
//                        || (expireddate !=null && expireddate.compareTo(info.getString("EXPIRED_DATE")) != 0)
//                        || (cardtype != info.getInt("CARD_TYPE"))) {
//
//                    logUtils.addCaseLog("Failed to check card info");
//                }
//
//            }
//
//
//            if( bundle.containsKey( "check" ) ){
//                // 有 check 关键词时，进行检查
//                List<String[]> keyValues = convert( bundle.getString("check"), ".", "-");
//
//                // _check=PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY
//                for (int i = 0; i < keyValues.size(); i++) {
//                    String[] keyValue = keyValues.get(i);
//                    if( keyValue[0].compareTo("CARD_TYPE") == 0) {
//                        int cardType = info.getInt( "CARD_TYPE" );
//                        if( keyValue.length > 1 ) {
//                            if( cardType == Integer.valueOf( keyValue[1] )) {
//                                Log.i( TAG,  String.format("Card type got [%d] is expected ", cardType ) );
//                            } else {
//                                Log.e( TAG,  String.format("Card type got [%d] is not expected as [%d]", cardType, Integer.valueOf( keyValue[1] ) ) );
//                            }
//                        } else {
//                            Log.i( TAG,  String.format("Card type [%d]", cardType ) );
//                        }
//                    } else {
//                        String value = info.getString( keyValue[0] );
//                        if( keyValue.length > 1 ) {
//                            keyValue[1] = keyValue[1];
//                            String msg;
//                            if( value.compareTo( keyValue[1]) == 0 ) {
//                                msg = String.format("[%s]=[%s], PASS", keyValue[0], keyValue[1] );
//                                printMsgTool( msg, Log.DEBUG );
//                                Log.i( TAG,  msg );
//                                logUtils.addCaseLog(msg);
//                            } else {
//                                msg = String.format("[%s]=[%s] Want [%s]", keyValue[0],value, keyValue[1] );
//                                Log.e( TAG,  msg );
//                                printMsgTool( msg, Log.ERROR );
//                                logUtils.addCaseLog(msg);
//                            }
//                        } else {
//                            String msg = String.format("[%s]=[%s], skip to check", keyValue[0], value );
//                            Log.i( TAG, msg );
//                            printMsgTool( msg, Log.DEBUG );
//                            logUtils.addCaseLog(msg);
//                        }
//                    }
////
////                    if( keyValue.length == 1 ) {
////                        Log.i( TAG, "Get Key: " + keyValue[0] + ", value: " + info.getString( keyValue[0] ) );
////                    } else {
////                    }
////                    if( i == (tags.length -1) ) {
////                        ret = Integer.valueOf(cardInfo[i]) - info.getInt( tags[i]);
////                        if( 0 != ret ){
////                            Log.e(TAG, "failure to check " + tags[i] + ", want: " + cardInfo[i] + ", got: " + info.getInt( tags[i] ));
////                        }
////                    } else {
////                        ret = cardInfo[i].compareToIgnoreCase( info.getString( tags[i]) );
////                        if( 0 != ret ){
////                            Log.e(TAG, "failure to check " + tags[i] + ", want: " + cardInfo[i] + ", got: " + info.getString( tags[i] ));
////                        }
////                    }
//                }
//            }
//
//            if( bundle.containsKey("getEMVData")){
//                printMsgTool( "checking with getEMVData:" , Log.DEBUG );
//                String failure = getEMVDataAndCheck( bundle.getString("getEMVData"), "getEMVData" );
//                if( failure.length() > 0 ) {
//                    printMsgTool( "fail to getEMVData ↑", Log.ERROR );
//                    Log.e(TAG, failure);
//                }
//            }
////            if( bundle.containsKey("getCardData")){
////                printMsgTool( "checking with getCardData:" , Log.DEBUG );
////                String failure = getEMVDataAndCheck( bundle.getString("getCardData"), "getCardData" );
////                if( failure.length() > 0 ) {
////                    printMsgTool( "fail to getCardData ↑", Log.ERROR );
////                    Log.e(TAG, failure);
////                }
////            }
//
//            //set EMV data
//            if (setEMVDataParam != null && !setEMVDataParam.equals(""))
//                T_setEMVData(setEMVDataParam);
//
//            if (getCardDataParam != null && !getCardDataParam.equals(""))
//                cardDataMsg = T_getCardData(getCardDataParam);
//
//            // set
////            if( bundle.containsKey( "setEMVData" )) {
////                printMsgTool( "calling with setEMVData:" , Log.DEBUG );
////                T_setEMVData( bundle.getString("setEMVData"), ".", "-");
////            }
//
//            // 再次获取
////            if( bundle.containsKey("getEMVData2")){
////                printMsgTool( "checking with getEMVData2:" , Log.DEBUG );
////                String failure = getEMVDataAndCheck( bundle.getString("getEMVData2"), "getEMVData" );
////                if( failure.length() > 0 ) {
////                    printMsgTool( "fail to getEMVData2 ↑", Log.ERROR );
////                    Log.e(TAG, failure);
////                }
////            }
////            if( bundle.containsKey("getCardData2")){
////                printMsgTool( "checking with getCardData2:" , Log.DEBUG );
////                String failure = getEMVDataAndCheck( bundle.getString("getCardData2"), "getCardData" );
////                if( failure.length() > 0 ) {
////                    printMsgTool( "fail to getCardData2 ↑", Log.ERROR );
////                    Log.e(TAG, failure);
////                }
////            }
//
//            if( bundle.containsKey("importCardConfirmResult")  ) {
//
//                if( bundle.getBoolean( "importCardConfirmResult" ) ){
//                    LogUtil.d(TAG, "iemv.importCardConfirmResult(true)");
//                    printMsgTool( "calling importCardConfirmResult with true:" , Log.DEBUG );
//                    iemv.importCardConfirmResult(true);
//                } else {
//                    LogUtil.d(TAG, "iemv.importCardConfirmResult(false)");
//                    printMsgTool( "calling iemv.importCardConfirmResult(false):" , Log.DEBUG );
//                    iemv.importCardConfirmResult(false);
//                }
//            }else {
//                Log.w(TAG, "skip to import result");
//                printMsgTool( "skip to import result" , Log.INFO );
//                synchronized (emvLock){
//                    emvResult = "-1, onConfirmCardInfo";
//                    emvLock.notify();
//                }
//            }
//
//        }
//
//        @Override
//        public void onRequestInputPIN(boolean isOnlinePin, int retryTimes) throws RemoteException {
//
//            onRequestInputPINCallback = true;
//
//            LogUtil.d(TAG, "callback: onRequestInputPIN, isOnlinePin:" + isOnlinePin);
//
//            String result = "onRequestInputPIN callback:\nisOnlinePin:" + isOnlinePin + "\n" +
//                    "retryTimes:" + retryTimes;
//            printMsgTool(result, Log.DEBUG );
//
//            logUtils.addCaseLog("onRequestInputPIN callback is performed \nisOnlinePin:" + isOnlinePin);
//
//            if (isOnlinePin == false)
//                logUtils.addCaseLog("offline PIN retryTimes:" + retryTimes);
//
//            Bundle bundle = convert( importPinParam, null);
//
//            if( bundle.containsKey("importPin") ) {
//                if( bundle.getInt("importPin") == 0 ){
//                    // 取消 pin
//                    printMsgTool( "取消输入 PIN", Log.INFO );
//                    iemv.importPin(0, null);
//                    synchronized (emvLock){
//                        emvResult = "-2, onRequestInputPIN,取消PIN";
//                        emvLock.notify();
//                    }
//                } else if( bundle.getInt("importPin") < 0 ) {
//                    // 不输入 pin
//                    printMsgTool( "不响应 PIN，直接返回", Log.INFO );
//                    synchronized (emvLock){
//                        emvResult = "-3, onRequestInputPIN，不输入PIN";
//                        emvLock.notify();
//                    }
//                }
//                return;
//            }
//
//            if( bundle.containsKey("PinBlock") && bundle.containsKey("option")) {
//
//                int option = bundle.getInt( "option" );
//                String pinBlock = bundle.getString("PinBlock").trim();
//
//                if (pinBlock.compareToIgnoreCase("null") == 0) {
//                    printMsgTool("importPin with null", Log.INFO);
//                    if (option == 0) {
//                        printMsgTool( "取消输入 PIN", Log.INFO );
//                        iemv.importPin(option, null);
//                        synchronized (emvLock){
//                            emvResult = "-2, onRequestInputPIN,取消PIN";
//                            emvLock.notify();
//                        }
//                    }
//                    else
//                        iemv.importPin(option, null);
//                } else {
//                    printMsgTool("importPin with " + pinBlock, Log.INFO);
//                    iemv.importPin(option, StringUtil.hexStr2Bytes(pinBlock));
//                }
////                return;
//            }
//            else {
//                // show pin pad
//                String slot = bundle.getString("slot", "2");
//                String sBundle = importPinParam.trim();
//                String sBundle2 = "";
//                String pinBlock = ((PinpadModule) ((MyApplication) context).newServiceModule.getModule("Pinpad")).T_startPinInput(slot, sBundle, sBundle2);
//                LogUtil.d(TAG, "Got Pin: " + pinBlock);
//                printMsgTool("Got Pin: " + pinBlock, Log.INFO);
//                if (pinBlock.compareToIgnoreCase("isNonePin") == 0) {
//                    iemv.importPin(1, null);
//                } else if (pinBlock.compareToIgnoreCase("Cancel") == 0) {
//                    // Cancel
//                    iemv.importPin(0, null);
//                } else if (pinBlock.startsWith("Error")) {
//                    // error
//                    iemv.importPin(0, null);
//                } else {
//                    iemv.importPin(1, StringUtil.hexStr2Bytes(pinBlock));
//                }
//            }
//        }
//
//        @Override
//        public void onConfirmCertInfo(String certType, String certInfo) throws RemoteException {
//            LogUtil.d(TAG, "onConfirmCertInfo callback, certType:" + certType +
//                    "certInfo:" + certInfo);
//
//            msg = "Certification: Confirmation";
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//
//            iemv.importCertConfirmResult(1);
//        }
//
//        @Override
//        public void onRequestOnlineProcess(Bundle aaResult) throws RemoteException {
//            onRequestOnlineProcessCallback=true;
//
//            printMsgToolAppend( "onRequestOnlineProcess callback is performed", Log.INFO );
//
//            //通过getAppTLVList获取交易数据
////            if (reqTLVDataParam != null && !reqTLVDataParam.equals(""))
////                appTLVDataMsg = T_getAppTLVList(reqTLVDataParam);
//
//            String result = "SIGNATURE:" + aaResult.getBoolean("SIGNATURE") + "\nCTLS_CVMR:" + aaResult.getInt("CTLS_CVMR")+"\nRESULT:" + aaResult.getInt("RESULT") +
//                    "\nARQC_DATA:\r" + convertTLVinLines(aaResult.getString("ARQC_DATA") ) +
//                    "\nREVERSAL_DATA:\r" + convertTLVinLines( aaResult.getString("REVERSAL_DATA") ) /*+ "\nappTLVDataMsg:\r"  + convertTLVinLines(appTLVDataMsg)*/;
//            printMsgTool(result, Log.DEBUG );
//
//            LogUtil.d(TAG, "callback: onRequestOnlineProcess" );
//
//            logUtils.addCaseLog("\nonRequestOnlineProcess callback is performed");
//            logUtils.addCaseLog("RESULT:" + aaResult.getInt("RESULT"));
//            logUtils.addCaseLog("SIGNATURE:" + aaResult.getBoolean("SIGNATURE"));
//            logUtils.addCaseLog("CTLS_CVMR:" + aaResult.getInt("CTLS_CVMR"));
//            logUtils.addCaseLog("ARQC_DATA:" + convertTLVinLines(aaResult.getString("ARQC_DATA") ));
//            logUtils.addCaseLog("REVERSAL_DATA:" + convertTLVinLines( aaResult.getString("REVERSAL_DATA") ));
//
// //           logUtils.addCaseLog("appTLVDataMsg:"  + convertTLVinLines(appTLVDataMsg));
//
//            //logUtils.addCaseLog("\n"+result);
//            result = "onRequestOnlineProcess callback:\n" + result;
//
//
//            Bundle expectedData = convert( expectedOnRequestOnlineProcessResultParam, null );
//            if (checkEMVTransResut(aaResult.getInt("RESULT"),aaResult, expectedData,1) && getAndCheckAppTLVList(expectedGetAppTLVListParam, 1))
//                onRequestOnlineProcessResult = "true";
//
//            //联机交易处理,设置importOnlineResult的结果
//            String options = importOnlineResultParam;
//            String[] option = options.split(";");
//
////            Boolean getAppTLVListOption = importData.getBoolean("getAppTLVListOption");
////            Boolean setEmvData = importData.getBoolean("issetEMVdata");
////            if (getAppTLVListOption) {
////                String[] strlist = {"9F26","9F27","9F10","9F37","9F1A","9F36","95","9A","9C","9F02","5F2A","82","9F03","9F33","9F34","9F35","84","9F1E","9F09","9F41","8E","9F63"};
////                T_getAppTLVList(strlist);
////            }
////
////            if(setEmvData) {
////                byte[] acquirerID = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
////                byte[] termCap = {(byte) 0xe0, (byte) 0xf1, (byte) 0xc8};
////                byte[] addTermCap = {(byte) 0xe0, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01};
////                byte[] countryCode = {(byte) 0x01, (byte) 0x56};
////                byte[] currencyCode = {(byte) 0x01, (byte) 0x56};
////                byte[] termType = {(byte) 0x22};
////
////                ArrayList<String> tlvList = new ArrayList<String>();
////
////                tlvList.add(("9F0106") + StringUtil.byte2HexStr(acquirerID));
////                tlvList.add(("9F3501") + StringUtil.byte2HexStr(termType));
////                tlvList.add(("9F3303") + StringUtil.byte2HexStr(termCap));
////                tlvList.add(("9F4005") + StringUtil.byte2HexStr(addTermCap));
////                tlvList.add(("9F1A02") + StringUtil.byte2HexStr(countryCode));
////                tlvList.add(("5F2A02") + StringUtil.byte2HexStr(currencyCode));
////                T_setEMVData(tlvList);
////            }
//
//            String[] tvr = {"95"};
////            My13getAppTLVList(tvr);
////            Boolean getProcessCardType = importData.getBoolean("isgetProcessCardType");
////            if(getProcessCardType){
////                T_getProcessCardType();
////            }
//
//            msg = "\nOnline request: " + "\n" + result;
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//            /*Go online*/
//            /*do online finish process, 2nd GAC*/
//            if( option[0].length() > 0 ){
//                LogUtil.d(TAG, "Try to convert: " + option[0].trim() );
//                Bundle bundle = convert( option[0].trim(), null);
//                iemv.importOnlineResult( bundle , onlineResultHandler );
//            } else {
//                synchronized (emvLock){
//                    emvResult = "onRequestOnlineProcess:skip";
//                    emvLock.notify();
//                }
//            }
//        }
//
//        @Override
//        public void onTransactionResult(int result, Bundle data) throws RemoteException {
//
//            onTransactionResultCallback =true;
//
//            LogUtil.d(TAG, "callback: onTransactionResult = " + result);
//
//            logUtils.addCaseLog("\nonTransactionResult callback is performed");
//            logUtils.addCaseLog("Results:" + result);
//            String checkEMVTransResutMsg = "false";
//
//            if( 0 == result || 9 == result ) {
//
//                //通过getAppTLVList获取交易数据
////                if (reqTLVDataParam != null && !reqTLVDataParam.equals(""))
////                    appTLVDataMsg = T_getAppTLVList(reqTLVDataParam);
//
//                Log.i(TAG, "onTransactionResult callback, result:" + result +
//                        "\nTC_DATA:" + data.getString("TC_DATA") +
//                        "\nREVERSAL_DATA:" + data.getString("REVERSAL_DATA") +
//                        "\nERROR:" + data.getString("ERROR") + "\nSIGNATURE:" + data.getBoolean("SIGNATURE") + "\nCTLS_CVMR:" + data.getInt("CTLS_CVMR") +"\ncardDataMsg:" +cardDataMsg /*+ "\nAppTLVData:" + appTLVDataMsg*/);
//
//                printMsgToolAppend( "onTransactionResult:", Log.INFO );
//                msg = "Results:" + result + "\nTC_DATA:" + convertTLVinLines(data.getString("TC_DATA") )+
//                        "\nREVERSAL_DATA:" + convertTLVinLines(data.getString("REVERSAL_DATA")) +
//                        "\nERROR:" + data.getString("ERROR") + "\nSIGNATURE:" + data.getBoolean("SIGNATURE") + "\nCTLS_CVMR:" + data.getInt("CTLS_CVMR");
//                printMsgTool( msg, Log.DEBUG );
//
//
//                logUtils.addCaseLog("ERROR:" + data.getString("ERROR"));
//                logUtils.addCaseLog("SIGNATURE:" + data.getBoolean("SIGNATURE"));
//                logUtils.addCaseLog("CTLS_CVMR:" + data.getInt("CTLS_CVMR"));
//                logUtils.addCaseLog("TC_DATA:" + convertTLVinLines(data.getString("TC_DATA") ));
//                logUtils.addCaseLog("REVERSAL_DATA:" + convertTLVinLines( data.getString("REVERSAL_DATA") ));
//
//                //logUtils.addCaseLog("appTLVDataMsg" + convertTLVinLines(appTLVDataMsg));
//                //logUtils.addCaseLog("\n" + msg + "\nappTLVDataMsg" + convertTLVinLines(appTLVDataMsg));
//
//
//
//                Bundle expectedData = convert( expectedOnTransactionResultParam, null );
//                if (checkEMVTransResut(result,data, expectedData,2) && getAndCheckAppTLVList(expectedGetAppTLVListParam, 2))
//                    checkEMVTransResutMsg = "true";
//
//
//            } else {
//                msg = "onTransactionResult callback, result:" + result + ", " + getEMVMsgByCode( result);
//                Log.e(TAG, msg );
//                printMsgTool( msg, Log.ERROR );
//
//            }
//            synchronized (emvLock){
//                if ( (result ==0) || (result ==9) ){
//                    Bundle callback = convert(expectedCallbackPerformedParam,null);
//                    if (callback.getBoolean("onSelectApplication") == onSelectApplicationCallback && callback.getBoolean("onConfirmCardInfo") == onConfirmCardInfoCallback
//                            && callback.getBoolean("onRequestInputPIN") == onRequestInputPINCallback
////                        && callback.getBoolean("onRequestOnlineProcess") == onRequestOnlineProcessCallback
//                            && callback.getBoolean("onTransactionResult") == onTransactionResultCallback
//                            && callback.getBoolean("onProccessResult") == onProccessResultCallback)
//                        emvResult = result + "," + checkEMVTransResutMsg+","+"true";
//                    else
//                        emvResult = result + "," + checkEMVTransResutMsg+","+"false";
//                }
//                else{
//                    emvResult = "" + result + ", "+ getEMVMsgByCode( result);
//                }
//
//                emvLock.notify();
//            }
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//        }
//    };
//
//
//    /*
//  type = 1 -> onRequestOnlineProcess 回调里面获取 -> 第一次GAC之后获取
//  type = 2 -> onTransactionResult 回调里面获取 -> TC 脱机批准 第一次GAC之后获取
//  type = 3 -> onProccessResult 回调里面获取 ->第二次GAC之后获取
//  * */
//    boolean getAndCheckAppTLVList(String expectedDataStr, int type) throws RemoteException {
//        String[] TagStrlist;
//        int tagErrFlag = 0,tagNotFoundFlag = 0;
//        String msg;
//
//        if (type == 1){
//            TagStrlist = new String[]{"9B", "84", "9F34", "5F20", "50", "9F40",
//                    "9F12", "9F11", "5F2A", "9F26", "95", "5F34", "82", "9A", "9C", "9F02", "9F03", "9F09", "9F10", "9F1A", "9F1E", "9F27", "9F33", "9F35"
//                    , "9F36", "9F37", "9F41", "5A", "57", "5A", "8F", "9F0D", "9F0E", "9F0F", "9F15", "9F63", "9F6E", "9F5B", "9F20", "56"/*, "91", "8A", "9F71", "9F7E"*/};
//            msg ="onRequestOnlineProcess";
//        }else if (type == 2){
//            TagStrlist = new String[]{"9B", "84", "9F34", "5F20", "50", "9F40",
//                    "9F12", "9F11", "5F2A", "9F26", "95", "5F34", "82", "9A", "9C", "9F02", "9F03", "9F09", "9F10", "9F1A", "9F1E", "9F27", "9F33", "9F35"
//                    , "9F36", "9F37", "9F41", "5A", "57", "5A", "8F", "9F0D", "9F0E", "9F0F", "9F15", "9F63", "9F6E", "9F5B", "9F20", "56"/*, "91", "8A", "9F71", "9F7E"*/};
//            msg ="onTransactionResult";
//        }else if (type == 3){
//            TagStrlist = new String[]{"9B", "84", "9F34", "5F20", "50", "9F40",
//                    "9F12", "9F11", "5F2A", "9F26", "95", "5F34", "82", "9A", "9C", "9F02", "9F03", "9F09", "9F10", "9F1A", "9F1E", "9F27", "9F33", "9F35"
//                    , "9F36", "9F37", "9F41", "5A", "57", "5A", "8F", "9F0D", "9F0E", "9F0F", "9F15", "9F63", "9F6E", "91", "8A", "9F5B", "9F20", "56"/*, "9F71", "9F7E"*/};
//            msg ="onProccessResult";
//        }
//        else
//            return false;
//
//        logUtils.addCaseLog(msg + " getAPPTLv Data :");
//
//        int i;
//        String strs = iemv.getAppTLVList(TagStrlist);
//
//        logUtils.addCaseLog(convertTLVinLines( strs ));
//
//        String valueInService;
//        String valueExpected;
//
//        for (i = 0; i< TagStrlist.length; i++) {
//            valueExpected = findTag1(String.valueOf(expectedDataStr), TagStrlist[i]);
//            valueInService = findTag1(String.valueOf(strs), TagStrlist[i]);
//
//            //只检查Service 是否有该Tag返回，不检查tag的值是否正确，比如：当expected设置为9B00时
//            if (valueExpected == "" && valueInService != null && valueInService != "") {
//                continue;
//            }
//
//            //当预期的tag不存在时，无需检查Service是否能获得该tag
//            if (valueExpected == null ){
//                continue;
//            }
//
//            if (valueExpected == "" && valueInService == null) {
//                logUtils.addCaseLog("Tag: " + TagStrlist[i] + " NOT FOUND");
//                tagNotFoundFlag++;
//                continue;
//            }
//
//            //当预期与service返回的tag值不相同时
//            if (valueInService != null && valueInService.compareTo(valueExpected) != 0)  {
//                logUtils.addCaseLog("Tag: " + TagStrlist[i] + " Value Error");
//                tagErrFlag++;
//                continue;
//            }
//
//            if (i == (TagStrlist.length - 1)  ) {
//                break;
//            }
//        }
//
//        if (tagErrFlag==0 && tagNotFoundFlag==0){
//            logUtils.addCaseLog(msg + " getAPPTLv Data Correct");
//            return true;
//        }
//        else{
//            logUtils.addCaseLog(msg + " getAPPTLv Data Wrong");
//            return false;
//        }
//    }
//
//    /*
//    type = 1 -> ARQC_DATA checking
//    type = 2 -> TC_DATA checking
//    type = 3 -> SCRIPT_DATA checking
//    type = 4 -> REVERSAL_DATA checking
//    * */
//    boolean checkEMVTagValue(String serviceDataStr, String expectedDataStr, int type)
//    {
//        //      String arqcData = aaResult.getString("ARQC_DATA");
//        int tagErrFlag = 0,tagNotFoundFlag = 0;
//        String[] TagStrlist;
//        String errMsg;
//
//        if (type == 1) {
//            //field55_ARQC 接触和非接相同
//            TagStrlist = new String[]{"9F26", "9C", "9F27", "9F10", "9F37", "9F36", "95", "9A",
//                    "9F02", "5F2A", "82", "9F1A", "9F03", "9F33", "9F34", "9F35", "9F1E", "84", "9F09", "9F41"};
//        }else if (type ==2){
//            //field55_TC 接触和非接相同
//            TagStrlist = new String[]{"9F26", "9C", "9F27", "9F10", "9F37", "9F36", "95", "9A",
//                    "9F02", "5F2A", "82", "9F1A", "9F03", "9F33", "9F34", "9F35", "9F1E", "84", "9F09", "9F41", "9F63", "91"};
//
//        }else if (type ==3){
//            //field55_SCRIPT
//            TagStrlist = new String[]{"9F10", "9F26", "9F33", "95", "9F37", "9F1E", "9F36", "82",
//                    "9F1A", "9A"};
//
//        }else if (type ==4){
//            //REVERSAL_DATA
//            TagStrlist = new String[]{"9F26", "9C", "9F27", "9F10", "9F37", "9F36", "95", "9A",
//                    "9F02", "5F2A", "82", "9F1A", "9F03", "9F33", "9F34", "9F35", "9F1E", "84", "9F09", "9F41"};
//
//        }
//        else
//            return false;
//
//        int i;
//        String valueInService;
//        String valueExpected;
//
//        for (i = 0; i< TagStrlist.length; i++) {
//            valueExpected = findTag1(String.valueOf(expectedDataStr), TagStrlist[i]);
//            valueInService = findTag1(String.valueOf(serviceDataStr), TagStrlist[i]);
//
//            //只检查Service 是否有该Tag返回，不检查tag的值是否正确，当expected设置为9F1600时
//            if (valueExpected == "" && valueInService != null && valueInService != "") {
//                continue;
//            }
//
//            //当预期的tag不存在时，无需检查Service是否能获得该tag
//            if (valueExpected == null ){
//                continue;
//            }
//
//            if (valueExpected == "" && valueInService == null) {
//                logUtils.addCaseLog("Tag: " + TagStrlist[i] + " NOT FOUND");
//                tagNotFoundFlag++;
//                continue;
//            }
//
//            //当预期与service返回的tag值不相同时
//            if (valueInService != null && valueInService.compareTo(valueExpected) != 0)  {
//                logUtils.addCaseLog("Tag: " + TagStrlist[i] + " Value Error");
//                tagErrFlag++;
//                continue;
//            }
//
//            if (i == (TagStrlist.length - 1)) {
//                break;
//            }
//        }
//
//        if (tagErrFlag==0 && tagNotFoundFlag==0){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
///*
//* type  -> 1 : onRequestOnlineProcess
//*          2 : onTransactionResult
//*          3 : onProccessResult
//* */
//    boolean checkEMVTransResut(int result, Bundle serviceData, Bundle expectedData, int type)
//    {
//        int flag=0;
//        String msg = null;
//
//        logUtils.addCaseLog("Checking Result Data.....\n");
//        if (type == 1) {
//
//            msg = "onRequestOnlineProcess : ";
//            if (serviceData.getInt("RESULT") != expectedData.getInt("RESULT")){
//                logUtils.addCaseLog(msg + " RESULT Data Wrong");
//                flag++;
//            }
//
//            if (serviceData.getBoolean("SIGNATURE") != expectedData.getBoolean("SIGNATURE")){
//                logUtils.addCaseLog(msg + " SIGNATURE Data Wrong");
//                flag++;
//            }
//
//            //检查ARQC_DATA是否符合预期
//            if (checkEMVTagValue(serviceData.getString("ARQC_DATA"), expectedData.getString("ARQCDATA"), 1)==false){
//                logUtils.addCaseLog(msg + " ARQC_DATA Data Wrong");
//                flag++;
//            }
//
//            //检查REVERSAL_DATA是否符合预期 REVERSAL_DATA和ARQC_DATA一摸一样
//            if (checkEMVTagValue(serviceData.getString("REVERSAL_DATA"), expectedData.getString("REVERSALDATA"), 1)==false){
//                logUtils.addCaseLog(msg + " REVERSAL_DATA Data Wrong");
//                flag++;
//            }
//        }else if (type == 2){
//
//            msg = "onTransactionResult : ";
//
//            if (result != expectedData.getInt("RESULT")){
//                logUtils.addCaseLog(msg + " RESULT Data Wrong");
//                flag++;
//            }
//
//            if (serviceData.getInt("CTLS_CVMR") != expectedData.getInt("CTLSCVMR")){
//                logUtils.addCaseLog(msg + " CTLS_CVMR Data Wrong");
//                flag++;
//            }
//
//            if (serviceData.getInt("CARD_TYPE") != expectedData.getInt("CARDTYPE")){
//                logUtils.addCaseLog(msg + " CARD_TYPE Data Wrong");
//                flag++;
//            }
//
//            //检查TC_DATA是否符合预期
//            if (checkEMVTagValue(serviceData.getString("TC_DATA"), expectedData.getString("TCDATA"), 2)==false){
//                logUtils.addCaseLog(msg + " TC_DATA Data Wrong");
//                flag++;
//            }
//
//            //检查REVERSAL_DATA是否符合预期 REVERSAL_DATA和ARQC_DATA一摸一样
//            if (checkEMVTagValue(serviceData.getString("REVERSAL_DATA"), expectedData.getString("REVERSALDATA"), 2)==false){
//                logUtils.addCaseLog(msg + " REVERSAL_DATA Data Wrong");
//                flag++;
//            }
//
//        }else if (type == 3){
//
//            msg = "onProccessResult : ";
//
//            if (result != expectedData.getInt("RESULT")){
//                logUtils.addCaseLog(msg + " RESULT Data Wrong");
//                flag++;
//            }
//
//            //检查TC_DATA是否符合预期
//            if (checkEMVTagValue(serviceData.getString("TC_DATA"), expectedData.getString("TCDATA"), 2)==false){
//                logUtils.addCaseLog(msg + " TC_DATA Data Wrong");
//                flag++;
//            }
//
//            //检查REVERSAL_DATA是否符合预期 REVERSAL_DATA和ARQC_DATA一摸一样
//            if (checkEMVTagValue(serviceData.getString("REVERSAL_DATA"), expectedData.getString("REVERSALDATA"), 2)==false){
//                logUtils.addCaseLog(msg + " REVERSAL_DATA Data Wrong");
//                flag++;
//            }
//
//            if (checkEMVTagValue(serviceData.getString("SCRIPT_DATA"), expectedData.getString("SCRIPTDATA"), 3)==false){
//                logUtils.addCaseLog(msg + " SCRIPT_DATA Data Wrong");
//                flag++;
//            }
//        }
//
//        if (flag == 0 ){
//            logUtils.addCaseLog(msg + " Data is Correct");
//            return true;
//        }
//        else{
//            logUtils.addCaseLog(msg + " Data is Wrong");
//            return false;
//        }
//    }
//
//    protected OnlineResultHandler onlineResultHandler = new OnlineResultHandler.Stub() {
//
//        @Override
//        public void onProccessResult(int result, Bundle data) throws RemoteException {
//
//            onProccessResultCallback = true;
//
//            logUtils.addCaseLog("\nOnProcessResult callback is performed");
//
//            //通过getAppTLVList获取交易数据
////            if (reqTLVDataParam != null && !reqTLVDataParam.equals(""))
////                appTLVDataMsg = T_getAppTLVList(reqTLVDataParam);
//
//
//            printMsgToolAppend( "Online results callback:", Log.INFO );
//            String str = "RESULT:" + result
//                    + "\nTC_DATA:\n" + convertTLVinLines(data.getString("TC_DATA"))
//                    + "\nSCRIPT_DATA:\n" + convertTLVinLines(data.getString("SCRIPT_DATA"))
//                    + "\nREVERSAL_DATA:\n" + convertTLVinLines(data.getString("REVERSAL_DATA"))
//                    +"\ncardDataMsg:" +cardDataMsg /*+ "\nAppTLVData:" + appTLVDataMsg*/;
//            printMsgTool( str, Log.DEBUG );
////            Message message = new Message();
////            message.getData().putString("message", str);
////            handler.sendMessage(message);
//
//            logUtils.addCaseLog("RESULT:" + result + ": " + getOnlineResultMsg( result));
//            logUtils.addCaseLog("\nTC_DATA:\n" + convertTLVinLines(data.getString("TC_DATA")));
//            logUtils.addCaseLog("\nREVERSAL_DATA:\n" + convertTLVinLines(data.getString("REVERSAL_DATA")));
//            logUtils.addCaseLog("\nSCRIPT_DATA:\n" + convertTLVinLines(data.getString("SCRIPT_DATA")));
////            logUtils.addCaseLog("\nAppTLVData:" + convertTLVinLines(appTLVDataMsg));
//            logUtils.addCaseLog("\ngetCardData:" + cardDataMsg);
//            logUtils.addCaseLog("\n");
//
//
//            String checkEMVTransResult="false";
//            Bundle expectedData = convert( expectedOnProccessResultParam, null );
//            if (checkEMVTransResut(result,data, expectedData,3) && getAndCheckAppTLVList(expectedGetAppTLVListParam, 3))
//                checkEMVTransResult = "true";
//
//            synchronized (emvLock){
////                emvResult = "" + result + ", "+ getOnlineResultMsg( result);
//
//                if (onRequestOnlineProcessCallback == true){
//                    emvResult = result + "," + onRequestOnlineProcessResult + ",";
//                }else{
//                    emvResult = result + ",";
//                }
//                Bundle callback = convert(expectedCallbackPerformedParam,null);
//                if (callback.getBoolean("onSelectApplication") == onSelectApplicationCallback && callback.getBoolean("onConfirmCardInfo") == onConfirmCardInfoCallback
//                        && callback.getBoolean("onRequestInputPIN") == onRequestInputPINCallback
//                        && callback.getBoolean("onRequestOnlineProcess") == onRequestOnlineProcessCallback
//                        && callback.getBoolean("onTransactionResult") == onTransactionResultCallback
//                        && callback.getBoolean("onProccessResult") == onProccessResultCallback)
//                    emvResult += checkEMVTransResult+","+"true";
//                else
//                    emvResult += checkEMVTransResult+","+"false";
//
//                emvLock.notify();
//            }
//
//        }
//    };
//
//    private int checkCardResult = -1;
//
//    protected CheckCardListener checkCardListener = new CheckCardListener.Stub() {
//        String msg;
//
//        @Override
//        public void onCardSwiped(Bundle track) throws RemoteException {
//            msg = "Card detection: credit card swiped successfully";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            T_stopCheckCard();
//            synchronized (emvLock ){
//                checkCardResult = 1;
//                emvLock.notify();
//            }
//        }
//
//        @Override
//        public void onCardPowerUp() throws RemoteException {
//            Log.i(TAG, "onCardPowerUp ... ");
//            msg = "Card test: IC card inserted successfully";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            T_stopCheckCard();
//            synchronized (emvLock ){
//                checkCardResult = 2;
//                emvLock.notify();
//            }
//
//            //检卡前设置是否bypass剩余PIN
//            if (setBypassRemainingPINParam != null && !setBypassRemainingPINParam.equals(""))
//                T_setByPassAllPin(setBypassRemainingPINParam);
//
//
//            //设置setRequestACType的callback
//            if (setRequestACTypeParam != null &&  !setRequestACTypeParam.equals("") ) {
//                T_setRequestACTypeCallBack(requestACTypehandler);
//            }
//
//
//            if( startEMVParam.length() > 0 ){
//                LogUtil.d(TAG, "iemv.startEMV" );
//                int emvProcessType = 1;
//                if( startEMVParam.contains( "processType=2" ) ){
//                    emvProcessType = 2;
//                }
//                Bundle emvOption = convert( startEMVParam, null);
//                emvOption.putInt("cardType",0);
//                iemv.startEMV(emvProcessType, emvOption, emvHandler);
//            }
//            Log.i(TAG, "onCardPowerUp ... return");
//        }
//
//        @Override
//        public void onCardActivate() throws RemoteException {
//            msg = "Card test: the card is successfully connected";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            T_stopCheckCard();
//            synchronized (emvLock ){
//                checkCardResult = 3;
//                emvLock.notify();
//            }
//
//            //非接设置发卡行脚本回调
//            T_setIssuerUpdateHandler(issuerUpdateHandler);
//
//            //设置选择指定非接内核
//            if (registerKernelAIDParam != "null") {
//                T_registerKernelAID(registerKernelAIDParam);
//            }
//            if( startEMVParam.length() > 0 ){
//                LogUtil.d(TAG, "iemv.startEMV" );
//                int emvProcessType = 1;
//                if( startEMVParam.contains( "_processType=2" ) ){
//                    emvProcessType = 2;
//                }
//                Bundle emvOption = convert( startEMVParam, null);
//                emvOption.putInt("cardType",1);
//                iemv.startEMV(emvProcessType, emvOption, emvHandler);
//            }
////                /*don't change transaction type for test case K17, card type will be changed according the check card result for other test case */
////                if (intent.getBoolean("doNotChangeCardType") == false)
////                    intent1.putInt("cardType", 1);
////                iemv.startEMV(processType, intent1, emvhandler);
//        }
//
//        @Override
//        public void onTimeout() throws RemoteException {
//            logUtils.addCaseLog("Card detection: timeout");
//            T_stopCheckCard();
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//            synchronized (emvLock ){
//                checkCardResult = 4;
//                emvLock.notify();
//            }
//
//        }
//
//        @Override
//        public void onError(int error, String message) throws RemoteException {
//            msg = "Card detection: Error:" + error + ", " + message;
//            Log.e(TAG, msg );
//
//            T_stopCheckCard();
//            Message message1 = new Message();
//            if (error == 3)
//                message1.getData().putString("message", msg + "Fallback deal");
//            else message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//            synchronized (emvLock ){
//                checkCardResult = 0-error;
//                emvLock.notify();
//            }
//
//        }
//    };
//
//    protected CheckCardListener checkCardListener_ForStartEMVWithTransParams = new CheckCardListener.Stub() {
//        String msg;
//
//        @Override
//        public void onCardSwiped(Bundle track) throws RemoteException {
//            msg = "Card detection: credit card swiped successfully";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            T_stopCheckCard();
//            synchronized (emvLock ){
//                checkCardResult = 1;
//                emvLock.notify();
//            }
//        }
//
//        @Override
//        public void onCardPowerUp() throws RemoteException {
//            Log.i(TAG, "onCardPowerUp ... ");
//            msg = "Card test: IC card inserted successfully";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            T_stopCheckCard();
//            synchronized (emvLock ){
//                checkCardResult = 2;
//                emvLock.notify();
//            }
//
//            //检卡前设置是否bypass剩余PIN
//            if (setBypassRemainingPINParam != null && !setBypassRemainingPINParam.equals(""))
//                T_setByPassAllPin(setBypassRemainingPINParam);
//
//
//            //设置setRequestACType的callback
//            if (setRequestACTypeParam != null &&  !setRequestACTypeParam.equals("") ) {
//                T_setRequestACTypeCallBack(requestACTypehandler);
//            }
//
//
//            if( startEmvWithTransParamsParam.length() > 0 ){
//                LogUtil.d(TAG, "iemv.startEmvWithTransParamsParam" );
//
//                Bundle emvOption2 = convert( EMVTransParamsParam, null);
//
//                byte[] termCap =  StringUtil.hexStr2Bytes(emvOption2.getString("termCap"));
//                byte[] termAddCap =  StringUtil.hexStr2Bytes(emvOption2.getString("termAddCap"));
//                byte[] termType =  StringUtil.hexStr2Bytes(emvOption2.getString("termType"));
//                byte[] countryCode =  StringUtil.hexStr2Bytes(emvOption2.getString("countryCode"));
//
//                byte[] currencyCode =  StringUtil.hexStr2Bytes(emvOption2.getString("currencyCode"));
//                byte[] referCurrencyCode =  StringUtil.hexStr2Bytes(emvOption2.getString("referCurrencyCode"));
//
//                byte[] transType = StringUtil.hexStr2Bytes(emvOption2.getString("transType"));
//
//                boolean isForceOnline = emvOption2.getBoolean("isForceOnline");
//
//                String merchantId = emvOption2.getString("merchantId");
//                String terminalId = emvOption2.getString("terminalId");
//                String merchantName = emvOption2.getString("merchantName");
//                long amount = emvOption2.getLong("amount");
//
//                long otherAmount = emvOption2.getLong("otherAmount");
//
//                int transFlowType = emvOption2.getInt("transFlowType");
//
//                int transCardType = emvOption2.getInt("transCardType");
//
//                transCardType = 0;
//
//                String otherParam = emvOption2.getString("otherParam");
//
//                EMVTransParams emvTransParams = new EMVTransParams(termCap,termAddCap,termType,countryCode,
//                        currencyCode, referCurrencyCode, transType[0],isForceOnline, merchantId,terminalId,merchantName,
//                        amount, otherAmount, transFlowType, transCardType);
//
//                Bundle emvOption = convert( startEmvWithTransParamsParam, null);
//                iemv.startEmvWithTransParams(emvTransParams, emvOption, emvHandler);
//            }
//            Log.i(TAG, "onCardPowerUp ... return");
//        }
//
//        @Override
//        public void onCardActivate() throws RemoteException {
//            msg = "Card test: the card is successfully connected";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            T_stopCheckCard();
//            synchronized (emvLock ){
//                checkCardResult = 3;
//                emvLock.notify();
//            }
//
//            //非接设置发卡行脚本回调
//            T_setIssuerUpdateHandler(issuerUpdateHandler);
//
//            //设置选择指定非接内核
//            if (registerKernelAIDParam != "null") {
//                T_registerKernelAID(registerKernelAIDParam);
//            }
//            if( startEmvWithTransParamsParam.length() > 0 ){
//                LogUtil.d(TAG, "iemv.startEmvWithTransParamsParam" );
//
//                Bundle emvOption2 = convert( EMVTransParamsParam, null);
//
//                byte[] termCap =  StringUtil.hexStr2Bytes(emvOption2.getString("termCap"));
//                byte[] termAddCap =  StringUtil.hexStr2Bytes(emvOption2.getString("termAddCap"));
//                byte[] termType =  StringUtil.hexStr2Bytes(emvOption2.getString("termType"));
//                byte[] countryCode =  StringUtil.hexStr2Bytes(emvOption2.getString("countryCode"));
//
//                byte[] currencyCode =  StringUtil.hexStr2Bytes(emvOption2.getString("currencyCode"));
//                byte[] referCurrencyCode =  StringUtil.hexStr2Bytes(emvOption2.getString("referCurrencyCode"));
//
//                byte[] transType = StringUtil.hexStr2Bytes(emvOption2.getString("transType"));
//
//                boolean isForceOnline = emvOption2.getBoolean("isForceOnline");
//
//                String merchantId = emvOption2.getString("merchantId");
//                String terminalId = emvOption2.getString("terminalId");
//                String merchantName = emvOption2.getString("merchantName");
//                long amount = emvOption2.getLong("amount");
//
//                long otherAmount = emvOption2.getLong("otherAmount");
//
//                int transFlowType = emvOption2.getInt("transFlowType");
//
//                int transCardType = emvOption2.getInt("transCardType");
//
//                transCardType = 1;
//
//                String otherParam = emvOption2.getString("otherParam");
//
//                EMVTransParams emvTransParams = new EMVTransParams(termCap,termAddCap,termType,countryCode,
//                        currencyCode, referCurrencyCode, transType[0],isForceOnline, merchantId,terminalId,merchantName,
//                        amount, otherAmount, transFlowType, transCardType);
//
//                Bundle emvOption = convert( startEmvWithTransParamsParam, null);
//                iemv.startEmvWithTransParams(emvTransParams, emvOption, emvHandler);
//            }
////                /*don't change transaction type for test case K17, card type will be changed according the check card result for other test case */
////                if (intent.getBoolean("doNotChangeCardType") == false)
////                    intent1.putInt("cardType", 1);
////                iemv.startEMV(processType, intent1, emvhandler);
//        }
//
//        @Override
//        public void onTimeout() throws RemoteException {
//            logUtils.addCaseLog("Card detection: timeout");
//            T_stopCheckCard();
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//            synchronized (emvLock ){
//                checkCardResult = 4;
//                emvLock.notify();
//            }
//
//        }
//
//        @Override
//        public void onError(int error, String message) throws RemoteException {
//            msg = "Card detection: Error:" + error + ", " + message;
//            Log.e(TAG, msg );
//
//            T_stopCheckCard();
//            Message message1 = new Message();
//            if (error == 3)
//                message1.getData().putString("message", msg + "Fallback deal");
//            else message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//            synchronized (emvLock ){
//                checkCardResult = 0-error;
//                emvLock.notify();
//            }
//
//        }
//    };
//
//
//    protected RequestACTypeHandler requestACTypehandler = new RequestACTypeHandler.Stub() {
//
//        @Override
//        public void requestConfirmACType(String aid, int defaultACType) throws RemoteException {
//            LogUtil.d(TAG, "callback: requestConfirmACType");
//            T_setRequestACType(setRequestACTypeParam);
//        }
//    };
//
//    protected IssuerUpdateHandler issuerUpdateHandler = new IssuerUpdateHandler.Stub() {
//
//        @Override
//        public void onRequestIssuerUpdate() throws RemoteException {
//            LogUtil.d(TAG, "callback: onRequestIssuerUpdate");
//            T_setIssuerUpdateScript();
//        }
//    };
//
//
//
//
//    String[][] emvCodeMessage = new String[][]{
//        {"0" , "AARESULT_TC(0) - TC on action analysis"},
//        {"8" , "EMV_NO_APP(8) - emv no application(aid param)"},
//            {"9" , "EMV_COMPLETE(9) - emv complete"},
//            {"11" , "EMV_OTHER_ERROR(11) - emv other error,transaction abort"},
//            {"12" , "EMV_FALLBACK(12) - FALLBACK"},
//            {"13" , "EMV_DATA_AUTH_FAIL(13) - data auth fail"},
//            {"14" , "EMV_APP_BLOCKED(14) - app has been blocked"},
//            {"15" , "EMV_NOT_ECCARD(15) - not EC"},
//            {"16" , "EMV_UNSUPPORT_ECCARD(16) - unsupport EC"},
//            {"17" , "EMV_AMOUNT_EXCEED_ON_PURELYEC(17) - amount exceed EC"},
//
//            { "18", "EMV_SET_PARAM_ERROR(18) - set parameter fail on 9F7A" },
//            { "19", "EMV_PAN_NOT_MATCH_TRACK2(19) - pan not match track2" },
//            { "20", "EMV_CARD_HOLDER_VALIDATE_ERROR(20) - card holder validate error" },
//            { "21", "EMV_PURELYEC_REJECT(21) - purely EC transaction reject" },
//            { "22", "EMV_BALANCE_INSUFFICIENT(22) - balance insufficient" },
//            { "23", "EMV_AMOUNT_EXCEED_ON_RFLIMIT_CHECK(23) - amount exceed the CTLS limit" },
//            { "24", "EMV_CARD_BIN_CHECK_FAIL(24) - check card failed" },
//            { "25", "EMV_CARD_BLOCKED(25) - card has been block" },
//            { "26", "EMV_MULTI_CARD_ERROR(26) - multiple card conflict" },
//            { "27", "EMV_INITERR_GPOCMD(27) - GPO Processing Options response error" },
//            { "28", "EMV_GACERR_GACCMD(28) - GAC response error" },
//            { "29", "EMV_TRY_AGAIN(29) - Try again" },
//            { "30", "EMV_ODA_FAILED(30) - ODA failed" },
//            { "31", "EMV_CVM_FAILED(31) - CVM response error" },
//
//            { "60", "EMV_RFCARD_PASS_FAIL(60) - tap card failure" },
////            { "", "AARESULT_TC(0) - TC on action analysis" },
//            { "1", "AARESULT_AAC(1) - refuse on action analysis" },
//
//            { "202", "CTLS_AAC(202) - refuse on CTLS" },
//            { "203", "CTLS_ERROR(203) - error on CTLS" },
//            { "204", "CTLS_TC(204) - approval on CTLS" },
//            { "205", "CTLS_CONT(205) - need contact" },
//            { "206", "CTLS_NO_APP(206) - result of CTLS, no application (UP Card maybe available)" },
//            { "207", "CTLS_NOT_CPU_CARD(207) - not a cpu card" },
//            { "208", "CTLS_ABORT(208) - Transation abort" },
//            { "209", "CTLS_ISSUERUPDATE_APPROVE(209) - Second tap, issuer update approve" },
//            { "210", "CTLS_CARD_BLOCK(210) -  6A81 error card block" },
//            { "211", "CTLS_SEL_FILE_INVALID(211) -  6283 error Selected file invalidated" },
//
//            { "150", "EMV_SEE_PHONE(150) - paypass result, please check the result on phone" },
//            { "301", "QPBOC_KERNAL_INIT_FAILED(301) - CTLS kernel init failed" }
//    };
//    Map<java.lang.Integer, String> mapCodeMessage = null;
//
//    String getEMVMsgByCode( int result ){
//        if( null ==  mapCodeMessage) {
//            mapCodeMessage= new HashMap<>();
//            for (int i = 0; i < emvCodeMessage.length ; i++) {
//                mapCodeMessage.put(  Integer.valueOf(emvCodeMessage[i][0]) , emvCodeMessage[i][1]);
//            }
//        }
//        return mapCodeMessage.get( result );
//    }
//
//    String[][] emvOnlineMessage = new String[][]{
//            {"0", "ONLINE_RESULT_TC(0) - 联机成功 | online result TC (success) "},
//            {"1", "ONLINE_RESULT_AAC(1) - 联机拒绝 | online result AAC (refuse) "},
//            {"101", "ONLINE_RESULT_OFFLINE_TC(101) - 联机失败，脱机成功 | online false, offline success"},
//            {"102", "ONLINE_RESULT_SCRIPT_NOT_EXECUTE(102) - 脚本未执行 | the script not execute"},
//            {"103", "ONLINE_RESULT_SCRIPT_EXECUTE_FAIL(103) - 脚本执行失败 | failure while execute script"},
//            {"104", "ONLINE_RESULT_NO_SCRIPT(104) - 联机失败，未下送脚本 | online failure, not send the script"},
//            {"105", "ONLINE_RESULT_TOO_MANY_SCRIPT(105) - 联机失败，脚本超过1个 | online failure, more than one script"},
//            {"106", "ONLINE_RESULT_TERMINATE(106) - 联机失败，交易终止(GAC返回非9000，要提示交易终止,0x8F) | online failure, transaction terminate. return transaction terminate while GAC is not 9000, 0x8F "},
//            {"107", "ONLINE_RESULT_ERROR(107) - 联机失败，EMV内核错误 | online failure, error in EMV"},
//            {"110", "ONLINE_RESULT_OTHER_ERROR(110) - 其他错误 | other error"}
//    };
//    Map<java.lang.Integer, String> mapOnlineMessage = null;
//
//    String getOnlineResultMsg( int result ){
//        if( null ==  mapOnlineMessage) {
//            mapOnlineMessage = new HashMap<>();
//            for (int i = 0; i < emvOnlineMessage.length ; i++) {
//                mapOnlineMessage.put( Integer.valueOf(emvOnlineMessage[i][0]), emvOnlineMessage[i][1]);
//            }
//        }
//        return mapOnlineMessage.get( result );
//    }

}
