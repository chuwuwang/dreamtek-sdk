package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.function.api.FunctionApiHolder;
import com.verifone.function.api.emv.IEmvL2;
import com.verifone.smartpos.api.SdkApiHolder;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.CandidateAppInfo;
import com.dreamtek.smartpos.deviceservice.aidl.CheckCardListener;
import com.dreamtek.smartpos.deviceservice.aidl.OnlineResultHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import Utils.LogUtils;
import base.MyApplication;

import static android.content.ContentValues.TAG;
import static base.TransType.CHECK_BALANCE;
import static base.TransType.EC_BALANCE;
import static base.TransType.PRE_AUTH;
import static base.TransType.PURCHASE;
import static base.TransType.Q_PURCHASE;
import static base.TransType.Q_QUERY;
import static base.TransType.SALE_VOID;
import static base.TransType.SIMPLE_PROCESS;
import static base.TransType.TRANSFER;
import static cn.verifone.atoolsjar.utils.Utils.*;

/**
 * Created by WenpengL1 on 2016/12/29.
 */

public class PbocBtMoudle {
//    Context context;
//    IPBOC ipboc;
//    LogUtils logUtils;
//    private static IEmvL2 emvl2;
//    ArrayList<String> apiList = new ArrayList<String>();
//    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
//    ArrayList<String> checkCard = new ArrayList<String>();
//    ArrayList<String> stopCheckCard = new ArrayList<String>();
//    ArrayList<String> readUPCard = new ArrayList<String>();
//    ArrayList<String> startPBOC = new ArrayList<String>();
//    ArrayList<String> abortPBOC = new ArrayList<String>();
//    ArrayList<String> updateAID = new ArrayList<String>();
//    ArrayList<String> updateRID = new ArrayList<String>();
//    ArrayList<String> importAmount = new ArrayList<String>();
//    ArrayList<String> importAppSelect = new ArrayList<String>();
//    ArrayList<String> importPin = new ArrayList<String>();
//    ArrayList<String> importCertConfirmResult = new ArrayList<String>();
//    ArrayList<String> importCardConfirmResult = new ArrayList<String>();
//    ArrayList<String> inputOnlineResult = new ArrayList<String>();
//    ArrayList<String> setEMVData = new ArrayList<String>();
//    ArrayList<String> getAppTLVList = new ArrayList<String>();
//    ArrayList<String> getCardData = new ArrayList<String>();
//    ArrayList<String> getPBOCData = new ArrayList<String>();
//    ArrayList<String> CandidateAppInfo = new ArrayList<String>();
//    ArrayList<String> getAID = new ArrayList<String>();
//    ArrayList<String> getRID = new ArrayList<String>();
//    ArrayList<String> getProcessCardType = new ArrayList<String>();
//    ArrayList<String> startEMV = new ArrayList<String>();
//    //08索引的公钥
//    String cakeystr08 =
//            "9F0605A0000003339F220108DF060101DF070101DF028190B61645EDFD5498FB246444037A0FA18C0F101EBD8EFA54573CE6E6A7FBF63ED21D66340852B0211CF5EEF6A1CD989F66AF21A8EB19DBD8DBC3706D135363A0D683D046304F5A836BC1BC632821AFE7A2F75DA3C50AC74C545A754562204137169663CFCC0B06E67E2109EBA41BC67FF20CC8AC80D7B6EE1A95465B3B2657533EA56D92D539E5064360EA4850FED2D1BFDF040103DF0314EE23B616C95C02652AD18860E48787C079E8E85ADF050400000000";
//    //09索引的公钥
//    String cakeystr09 =
//            "9F0605A0000003339F220109DF060101DF070101DF0281B0EB374DFC5A96B71D2863875EDA2EAFB96B1B439D3ECE0B1826A2672EEEFA7990286776F8BD989A15141A75C384DFC14FEF9243AAB32707659BE9E4797A247C2F0B6D99372F384AF62FE23BC54BCDC57A9ACD1D5585C303F201EF4E8B806AFB809DB1A3DB1CD112AC884F164A67B99C7D6E5A8A6DF1D3CAE6D7ED3D5BE725B2DE4ADE23FA679BF4EB15A93D8A6E29C7FFA1A70DE2E54F593D908A3BF9EBBD760BBFDC8DB8B54497E6C5BE0E4A4DAC29E5DF040103DF0314A075306EAB0045BAF72CDD33B3B678779DE1F527DF05083230313831323331";
//
//    //18索引的公钥,国密公钥
//    String cakeystr18 =
//            "9F0605A0000003339F220118DF060101DF070101DF024037710FEB7CC3617767874E85509C268E8F931D68773E93A89F39A4247DFE2D280FC5BC838353885B6DAD447C8F90116BD9D314047591989F67F319544D42A48BDF040103DF0314A075306EAB0045BAF72CDD33B3B678779DE1F527DF050400000000";
//
//
//    //aid:A00000000333010106
//    String aidctlsA00000000333010106 =
//            "9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000001DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000999999999DF2106000000100000";
//    String aidA00000000333010106 =
//            "9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000001DF150400000000DF160199DF170199DF14039F37049F7B06000000100000";
//
//    //aid:A00000000333010101
//    String aidctlsA00000000333010101 =
//            "9F0607A00000033301019F09020020DF010100DF1105D8689CF800DF1205D8689CF800DF130500100000009F1B0400000500DF150400000010DF160190DF170110DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000000100";
//    String aidA00000000333010101 =
//            "9F0607A00000033301019F09020020DF010100DF1105D8689CF800DF1205D8689CF800DF130500100000009F1B0400000500DF150400000010DF160190DF170110DF14039F3704DF1801019F7B06000000100000";
//    //floor limit changed from 00000001 to 00000100 9F1B
//    String aidA00000000333010101_change =
//            "9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000100DF150400000000DF160199DF170199DF14039F37049F7B06000000100000";
//
//
//    //aid:A00000000333010102
//    String aidctlsA00000000333010102 =
//            "9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000001DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000999999999DF2106000000000100";
//    String aidA00000000333010102 =
//            "9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000001DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000";
//
//    /*bundle key for startPBOC function*/
//    public static final String BUNDLE_STARTPBOCPARAM_CARDTYPE = "cardType";
//    public static final String BUNDLE_STARTPBOCPARAM_AUTHAMOUNT = "authAmount";
//    public static final String BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC = "isSupportQ";
//    public static final String BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM = "isSupportSM";
//    public static final String BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE = "isQPBOCForceOnline";
//    public static final String BUNDLE_STARTPBOCPARAM_MERCHANT_NAME = "merchantName";
//    public static final String BUNDLE_STARTPBOCPARAM_MERCHANT_ID = "merchantId";
//    public static final String BUNDLE_STARTPBOCPARAM_TERMINAL_ID = "terminalId";
//
//    public static final String BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS = "isPanConfirmOnSimpeProcess";
//    public static final String BUNDLE_STARTEMVPARAM_CARDTYPE = "cardType";
//    public static final String BUNDLE_STARTEMVPARAM_TRANSCODE = "transProcessCode";
//    public static final String BUNDLE_STARTEMVPARAM_AUTHAMOUNT = "authAmount";
//    public static final String BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC = "isSupportQ";
//    public static final String BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM = "isSupportSM";
//    public static final String BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE = "isQPBOCForceOnline";
//    public static final String BUNDLE_STARTEMVPARAM_MERCHANT_NAME = "merchantName";
//    public static final String BUNDLE_STARTEMVPARAM_MERCHANT_ID = "merchantId";
//    public static final String BUNDLE_STARTEMVPARAM_TERMINAL_ID = "terminalId";
//
//    public static final String BUNDLE_NOT_CHANGE_CARDTYPE = "doNotChangeCardType";
//
//    /*bundle key for startPBOC handler function for import result for service*/
//    public static final String BUNDLE_IMPORT_APPSELECTION = "importAppSelectResult";
//    public static final String BUNDLE_IMPORT_IS_PIN_INPUT = "IsPinInput";
//    public static final String BUNDLE_IMPORT_IMPORT_PIN = "importPIN";
//    public static final String BUNDLE_IMPORT_IMPORT_AMOUNT = "importAmount";
//    public static final String BUNDLE_IMPORT_CANCEL_CARD_CONFIRM = "cancelCardConfirmResult";
//    public static final String BUNDLE_IMPORT_IS_GET_PBOC_DATA = "getPBOCData";
//
//    /*test option*/
//    public static final String BUNDLE_IMPORT_IS_GET_CARD_DATA = "isGetCardData";
//    public static final String BUNDLE_IMPORT_GET_CARD_DATA = "getCardData";
//    public static final String BUNDLE_IMPORT_GET_APPTLV_LIST = "getAppTLVListOption";
//
//    /*bundle key for online process handler for import result of online*/
//    public static final String BUNDLE_IMPORT_IS_ONLINE = "isOnline";
//    public static final String BUNDLE_IMPORT_RESCODE = "respCode";
//    public static final String BUNDLE_IMPORT_AUTHCODE = "authCode";
//    public static final String BUNDLE_IMPORT_FIELD55 = "field55";
//
//
//    /*Bundle key for response of transace*/
//    public static final String BUNDLE_TRANS_RESULT = "RESULT";
//    public static final String BUNDLE_ARQC_DATA = "ARQC_DATA";
//    public static final String BUNDLE_TC_DATA = "TC_DATA";
//    public static final String BUNDLE_SCRIPT_DATA = "SCRIPT_DATA";
//    public static final String BUNDLE_REVERSAL_DATA = "REVERSAL_DATA";
//    public static final String BUNDLE_TRANS_ERROR = "ERROR";
//
//
//    /*PBOC TRANS FLOW DATA*/
//    public static final String BUNDLE_PBOC_PAN = "PAN";
//    public static final String BUNDLE_PBOC_TRACK1 = "TRACK1";
//    public static final String BUNDLE_PBOC_TRACK2 = "TRACK2";
//    public static final String BUNDLE_PBOC_TRACK3 = "TRACK3";
//    public static final String BUNDLE_PBOC_CARD_SN = "CARD_SN";
//    public static final String BUNDLE_PBOC_SERVICE_CODE = "SERVICE_CODE";
//    public static final String BUNDLE_PBOC_EXPIRED_DATE = "EXPIRED_DATE";
//    public static final String BUNDLE_EMV_CARDORG = "CARD_ORG";
//    public static final String BUNDLE_EMV_DATE = "DATE";
//    public static final String BUNDLE_EMV_TIME = "TIME";
//    public static final String BUNDLE_EMV_HOLDERNAME = "CARD_HOLDER_NAME";
//
//
//
//
//    private static Bundle transOption = new Bundle();
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            logUtils.addCaseLog(msg.getData().getString("message"));
//            logUtils.showCaseLog();
//        }
//    };
//
//    public PbocBtMoudle(Context context, IPBOC ipboc) {
//        this.context = context;
//        logUtils = MyApplication.serviceMoudle.logUtils;
//        this.ipboc = ipboc;
//        addAllapi();
//    }
//
//    public void addAllapi() {
//        try {
//            Class aClass = Class.forName("moudles.PbocBtMoudle");
//            Method[] methods = aClass.getDeclaredMethods();
//            Method[] methods1 = aClass.getMethods();
//            for (Method i : methods) {
//                if (i.getName().startsWith("My")) {
//                    apiList.add(i.getName().replace("My", ""));
//                } else {
//                    switch (i.getName().substring(0, 3)) {
//                        case "K01":
//                            checkCard.add(i.getName());
//                            break;
//                        case "K02":
//                            stopCheckCard.add(i.getName());
//                            break;
//                        case "K03":
//                            readUPCard.add(i.getName());
//                            break;
//                        case "K04":
//                            updateAID.add(i.getName());
//                            break;
//                        case "K05":
//                            updateRID.add(i.getName());
//                            break;
//                        case "K06":
//                            importAmount.add(i.getName());
//                            break;
//                        case "K07":
//                            importAppSelect.add(i.getName());
//                            break;
//                        case "K08":
//                            importPin.add(i.getName());
//                            break;
//                        case "K09":
//                            importCertConfirmResult.add(i.getName());
//                            break;
//                        case "K10":
//                            importCardConfirmResult.add(i.getName());
//                            break;
//                        case "K11":
//                            inputOnlineResult.add(i.getName());
//                            break;
//                        case "K12":
//                            setEMVData.add(i.getName());
//                            break;
//                        case "K13":
//                            getAppTLVList.add(i.getName());
//                            break;
//                        case "K14":
//                            getCardData.add(i.getName());
//                            break;
//                        case "K15":
//                            getPBOCData.add(i.getName());
//                            break;
//                        case "K16":
//                            CandidateAppInfo.add(i.getName());
//                            break;
//                        case "K17":
//                            startPBOC.add(i.getName());
//                            break;
//                        case "K18":
//                            abortPBOC.add(i.getName());
//                            break;
//                        case "K21":
//                            getAID.add(i.getName());
//                            break;
//                        case "K22":
//                            getRID.add(i.getName());
//                            break;
//                        case "K23":
//                            getProcessCardType.add(i.getName());
//                            break;
//                        case "K24":
//                            startEMV.add(i.getName());
//                            break;
//                    }
//                }
//            }
//            caseNames.add(checkCard);
//            caseNames.add(stopCheckCard);
//            caseNames.add(readUPCard);
//            caseNames.add(updateAID);
//            caseNames.add(updateRID);
//
//            caseNames.add(importAmount);
//            caseNames.add(importAppSelect);
//            caseNames.add(importPin);
//            caseNames.add(importCertConfirmResult);
//            caseNames.add(importCardConfirmResult);
//
//            caseNames.add(inputOnlineResult);
//            caseNames.add(setEMVData);
//            caseNames.add(getAppTLVList);
//            caseNames.add(getCardData);
//            caseNames.add(getPBOCData);
//            caseNames.add(CandidateAppInfo);
//            caseNames.add(startPBOC);
//            caseNames.add(abortPBOC);
//            caseNames.add(getAID);
//            caseNames.add(getRID);
//            caseNames.add(getProcessCardType);
//            caseNames.add(startEMV);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void K01001() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01002() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01003() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01004() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01005() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01006() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01007() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01008() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01009() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01010() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01011() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01012() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01013() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01014() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01015() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01016() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01017() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01018() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", false);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01019() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01020() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01021() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", false);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01022() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01023() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01024() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", false);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01025() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 60, new MyListener());
//    }
//
//    public void K01026() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 0, new MyListener());
//    }
//
//    public void K01027() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, -1, new MyListener());
//    }
//
//
//    public void K01028() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 2147483647, new MyListener());
//
//    }
//
//    public void K01029() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 2147483647 + 1, new MyListener());
//
//    }
//
//    public void K01030() {
//        My01checkCard(null, 30, new MyListener());
//
//    }
//
////    public void K01031()
////    {
////        logUtils.addCaseLog("本案例废弃");
////
////    }
//
//    public void K01032() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, null);
//
//    }
//
//    public void K01033() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01034() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01035() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01036() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01037() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01038() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01039() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01040() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01041() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01042() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01043() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
////    public void K01044()
////    {
////        Bundle cardOption = new Bundle();
////        cardOption.putBoolean("supportMagCard", true);
////        cardOption.putBoolean("supportICCard", true);
////        cardOption.putBoolean("supportRFCard", true);
////        My01checkCard(cardOption, 30, new MyListener());
////    }
////
////    public void K01045()
////    {
////        Bundle cardOption = new Bundle();
////        cardOption.putBoolean("supportMagCard", true);
////        cardOption.putBoolean("supportICCard", true);
////        cardOption.putBoolean("supportRFCard", true);
////        My01checkCard(cardOption, 30, new MyListener());
////    }
////
////    public void K01046()
////    {
////        Bundle cardOption = new Bundle();
////        cardOption.putBoolean("supportMagCard", true);
////        cardOption.putBoolean("supportICCard", true);
////        cardOption.putBoolean("supportRFCard", true);
////        My01checkCard(cardOption, 30, new MyListener());
////    }
//
//    public void K01047() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01048() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01049() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
////    public void K01050()
////    {
////        logUtils.addCaseLog("本案例废弃");
////    }
////
////    public void K01051()
////    {
////        logUtils.addCaseLog("本案例废弃");
////    }
//
//    public void K01052() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01053() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01054() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01055() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01056() {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        My01checkCard(cardOption, 30, new MyListener());
//    }
//
//    public void K01057() {
//        ((MyApplication) context).serviceMoudle.getIrfCardReaderMoudle().My05isExist();
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                logUtils.addCaseLog("Multiple threads perform ipboc. CheckCard lookup");
//                Bundle cardOption = new Bundle();
//                cardOption.putBoolean("supportMagCard", true);
//                cardOption.putBoolean("supportICCard", true);
//                cardOption.putBoolean("supportRFCard", true);
//                My01checkCard(cardOption, 30, new moudles.PbocBtMoudle.MyListener());
//            }
//        }.start();
//
//    }
//
//    public void K01058() {
//        ((MyApplication) context).serviceMoudle.getIrfCardReaderMoudle().My05isExist();
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                logUtils.addCaseLog("Multiple threads perform ipboc. CheckCard lookup");
//                Bundle cardOption = new Bundle();
//                cardOption.putBoolean("supportMagCard", true);
//                cardOption.putBoolean("supportICCard", true);
//                cardOption.putBoolean("supportRFCard", true);
//                My01checkCard(cardOption, 30, new moudles.PbocBtMoudle.MyListener());
//            }
//        }.start();
//
//    }
//
//    public void K01059() {
//        ((MyApplication) context).serviceMoudle.getInsertCardReaderMoudle().My03isCardIn(0);
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                logUtils.addCaseLog("Multiple threads perform ipboc. CheckCard lookup");
//                Bundle cardOption = new Bundle();
//                cardOption.putBoolean("supportMagCard", true);
//                cardOption.putBoolean("supportICCard", true);
//                cardOption.putBoolean("supportRFCard", true);
//                My01checkCard(cardOption, 30, new moudles.PbocBtMoudle.MyListener());
//            }
//        }.start();
//
//    }
//
//    public void K01060() {
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                logUtils.addCaseLog("Multiple threads perform ipboc. CheckCard lookup");
//                Bundle cardOption = new Bundle();
//                cardOption.putBoolean("supportMagCard", true);
//                cardOption.putBoolean("supportICCard", true);
//                cardOption.putBoolean("supportRFCard", true);
//                My01checkCard(cardOption, 30, new moudles.PbocBtMoudle.MyListener());
//            }
//        }.start();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                logUtils.addCaseLog("Multiple threads execute isExist");
//                ((MyApplication) context).serviceMoudle.getIrfCardReaderMoudle().My05isExist();
//            }
//        }.start();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                logUtils.addCaseLog("Multiple threads execute isCardIn");
//                ((MyApplication) context).serviceMoudle.getInsertCardReaderMoudle().My03isCardIn(0);
//            }
//        }.start();
//
//    }
//
//    public void K02001() {
//        My02stopCheckCard();
//    }
//
//    public void K02002() {
//        K01025();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02003() {
//        for (int i = 0; i < 50; i++) {
//            K02002();
//        }
//    }
//
//    public void K02004() {
//        K02002();
//    }
//
//    public void K02005() {
//        K02002();
//    }
//
//    public void K02006() {
//        K02002();
//    }
//
//    public void K02007() {
//        K01004();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02008() {
//        K01007();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02009() {
//        K01010();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02010() {
//        K01013();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02011() {
//        K01016();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02012() {
//        K01019();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K02013() {
//        K01022();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    this.sleep(10000);
//                    My02stopCheckCard();
//                    Message message = new Message();
//                    message.getData().putString("message", "stopCheckCard end");
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void K03001() {
//        My03readUPCard(new MyAPDUListener());
//    }
//
//    public void K04001() {
//        My04updateAID(1, 1, "9F0606A000000025019F09020001DF0101009F1B0400000000DF150400000000DF170110DF160125DF14039F37049F1A0203565F2A0203569F3501229F3303E0F0C89F4005F000F0A0019F7B06000000001388DF13050000000000DF1205CC00000000DF1105CC00000000DF180101");
//    }
//
//    public void K04003() {
//        My04updateAID(2, 1, "9F0606A000000025019F09020001DF010100DF150400000000DF170100DF160100DF14039F37049F1A0203565F2A0203569F3501229F3303E0E0889F400572000020019F7B06000000001388DF13050010000000DF1205DE00FC9800DF1105DC50FC9800DF2006000100000000DF1906000000000000DF21060000005000009F6604B6404280DF2A020356DF180101");
//    }
//
//    public void K04004() {
//        K04001();
//        My04updateAID(2, 2, aidctlsA00000000333010102);
//    }
//
//    public void K04005() {
//        My04updateAID(3, 1, aidA00000000333010101);
//        My21getAID(1);
//        My21getAID(2);
//    }
//
//    public void K04009() {
//        My04updateAID(0, 1, aidA00000000333010101);
//    }
//
//    public void K04011() {
//        My04updateAID(4, 2, aidctlsA00000000333010101);
//    }
//
//    public void K04013() {
//        My04updateAID(1, 2, "abcdedfsfsdfsdfs2134234234sdfsdfsdfws");
//    }
//
//    public void K04015() {
//        //aid:2048字节有效接触参数
//        int length = aidctlsA00000000333010101.length();
//        String aid = aidctlsA00000000333010101;
//        while (aid.length() < 2048) {
//            aid = aid + "0";
//        }
//        String str = aid.substring(0, 2048);
//        if (str.length() == 2048) {
//            My04updateAID(1, 2, str);
//        }
//    }
//
//    public void K04016() {
//        My04updateAID(1, 2, null);
//    }
//
//
//    public void K04017() {
//        int length = aidctlsA00000000333010101.length();
//        String aid = aidctlsA00000000333010101;
//        while (aid.length() < 2049) {
//            aid = aid + "0";
//        }
//        String str = aid.substring(0, 2049);
//        if (str.length() == 2049) {
//            My04updateAID(1, 2, str);
//        }
//    }
//
//    public void K04018() {
//        My04updateAID(1, 1, aidA00000000333010101);
//        My04updateAID(1, 1, aidA00000000333010102);
//        My04updateAID(1, 1, aidA00000000333010106);
//    }
//
//    public void K04022() {
//        My04updateAID(3, 2, aidctlsA00000000333010101);
//        My21getAID(1);
//        My21getAID(2);
//    }
//
//    public void K04028() {
//        My04updateAID(1, 1, aidA00000000333010101_change);
//    }
//
//    public void K04032() {
//        My04updateAID(2, 2, null);
//    }
//
//    public void K04033() {
//        My04updateAID(1, 2, "");
//    }
//
//    public void K04034() {
//        My04updateAID(2, 2, "");
//    }
//
//    public void K04035() {
//        My04updateAID(3, 1, "");
//        My21getAID(1);
//        My21getAID(2);
//    }
//
//    public void K04036() {
//        My04updateAID(3, 2, null);
//        My21getAID(1);
//        My21getAID(2);
//    }
//
//    public void K04037() {
//        My04updateAID(3, 1, "9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000001DF150400000000DF160199DF170199DF14039F37049F7B06000000100000");
//    }
//
//    public void K04038() {
//        My04updateAID(1, 2, "9F0608A000000333010103DF0101009F08020020");
//    }
//
//    public void K04039() {
//        My04updateAID(1, 2, aidctlsA00000000333010102);
//    }
//
//    public void K04040() {
//        My04updateAID(1, 2, aidctlsA00000000333010101);
//        My04updateAID(1, 2, aidctlsA00000000333010102);
//        My04updateAID(1, 2, aidctlsA00000000333010106);
//    }
//
//    public void K04041() {
//        My04updateAID(1, 0, aidA00000000333010101);
//    }
//
//    public void K04042() {
//        My04updateAID(1, 3, aidA00000000333010101);
//    }
//
//    public void K04043() {
//        int cnt = 100;
//        int i ;
//        boolean ret = false;
//        String aidstr;
//
//        for (i = 0; i < cnt; i++) {
//            String str = "9F0608A0000003330101" + String.format("%02d", i);//两位数，不足位补0
//            logUtils.addCaseLog("应用ID " + str);
//            aidstr =
//                    "9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000001DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000000010DF2006000999999999DF2106000000000100";
//            String aidstr1 = aidstr.replaceAll("9F0608A000000333010101", str);
//            ret = My04updateAID(1, 2, aidstr1);
//            if (ret) {
//                logUtils.addCaseLog("应用ID" + i + "新增成功");
//                logUtils.showCaseLog();
//            } else {
//                logUtils.addCaseLog("应用ID" + i + "新增失败");
//                logUtils.showCaseLog();
//            }
//        }
//    }
//
//
//    public void K05001() {
//        My05updateRID(1, cakeystr18);
//        My05updateRID(1, cakeystr08);
//        My05updateRID(1, cakeystr09);
//    }
//
//    public void K05002() {
//        My05updateRID(2, cakeystr09);
//    }
//
//    public void K05003() {
//        My05updateRID(2, cakeystr09);
//    }
//
//    public void K05004() {
//        My05updateRID(3, cakeystr09);
//    }
//
//    public void K05008() {
//        My05updateRID(0, cakeystr08);
//    }
//
//    public void K05010() {
//        My05updateRID(4, cakeystr08);
//    }
//
//    public void K05012() {
//        My05updateRID(1, "abcdedfsfsdfsdfs2134234234sdfsdfsdfws");
//    }
//
//    public void K05016() {
//        My05updateRID(1, null);
//    }
//
//    public void K05017() {
//        String capkstr =
//                "9F0605A0000000049F220106DF05083230313631323331DF060101DF070101DF0281F8CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747FDF040103DF0314F910A1504D5FFB793D94F3B500765E1ABCAD72D9";
//        My05updateRID(1, capkstr);
//        My05updateRID(2, capkstr);
//        My05updateRID(3, null);
//    }
//
//    public void K05018() {
//        My05updateRID(2, "");
//    }
//
//    public void K05019() {
//        My05updateRID(2, null);
//    }
//
//    public void K05020() {
//        My05updateRID(2, "1234");
//    }
//
//    public void K05021() {
//        My05updateRID(1, "");
//    }
//
//    public void K05022() {
//        My05updateRID(3, null);
//    }
//
//    public void K05023() {
//        My05updateRID(3, "");
//    }
//
//    public void K05024() {
//        My05updateRID(3, cakeystr08);
//    }
//
//    public void K05025() {
//        My05updateRID(3, "9F0605A0000000049F220106DF05083230313631323331DF060101DF070101DF0281F8CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747FDF040103DF0314F910A1504D5FFB793D94F3B500765E1ABCAD72D9");
//    }
//
//    public void K05026() {
//        My05updateRID(1, "9F0605A0000003339F220109DF060101DF070101DF0281B0EB374DFC5A96B71D2863875EDA2EAFB96B1B439D3ECE0B1826A2672EEEFA7990286776F8BD989A15141A75C384DFC14FEF9243AAB32707659BE9E4797A247C2F0B6D99372F384AF62FE23BC54BCDC57A9ACD1D5585C303F201EF4E8B806AFB809DB1A3DB1CD112AC884F164A67B99C7D6E5A8A6DF1D3CAE6D7ED3D5BE725B2DE4ADE23FA679BF4EB15A93D8A6E29C7FFA1A70DE2E54F593D908A3BF9EBBD760BBFDC8DB8B54497E6C5BE0E4A4DAC29E5DF040103DF0314A075306EAB0045BAF72CDD33B3B678779DE1F527DF05083230323031323331");
//    }
//
//    public void K05027() {
//        My05updateRID(1, "9F0605A0000003339F220109DF060101");
//    }
//
//   /* public void K05028() {
//        int cnt = 100;
//        int iRet = 0, i = 1;
//        boolean ret = false;
//        String cakeystr;
//
//        for (i = 0; i < cnt; i++) {
//            String str = "9F2201" + String.format("%02d", i);//两位数，不足位补0
//            logUtils.addCaseLog("Public key index" + str);
//            cakeystr =
//                    "9F0605A0000000039F220100DF060101DF070101DF028190BE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627BDF040103DF0314EE1511CEC71020A9B90443B37B1D5F6E703030F6DF050400000000";
//                    "9F0605A0000003339F220118DF060101DF070101DF024037710FEB7CC3617767874E85509C268E8F931D68773E93A89F39A4247DFE2D280FC5BC838353885B6DAD447C8F90116BD9D314047591989F67F319544D42A48BDF040103DF0314A075306EAB0045BAF72CDD33B3B678779DE1F527DF050400000000";
//            String cakeystr1 = cakeystr.replaceAll("9F220100", str);
////            logUtils.addCaseLog("公钥参数是" + cakeystr1 );
//            ret = My05updateRID(1, cakeystr1);
//            if (ret) {
//                logUtils.addCaseLog("Public key index" + i + "add success");
//                logUtils.showCaseLog();
//            } else {
//                logUtils.addCaseLog("Public key index" + i + "add fail");
//                logUtils.showCaseLog();
//            }
//        }
//    }*/
//
//    public void K05028() {
//        int cnt = 100;
//        int iRet = 0;
//        boolean ret = false;
//        String cakeystr;
//
//        for (int i = 0; i < cnt; i++) {
//            String str = "9F2201" + String.format("%02d", i);//两位数，不足位补0
//            logUtils.addCaseLog("公钥索引" + str);
//            cakeystr =
//                    "9F0605A0000003339F220107DF060101DF070101DF028190B61645EDFD5498FB246444037A0FA18C0F101EBD8EFA54573CE6E6A7FBF63ED21D66340852B0211CF5EEF6A1CD989F66AF21A8EB19DBD8DBC3706D135363A0D683D046304F5A836BC1BC632821AFE7A2F75DA3C50AC74C545A754562204137169663CFCC0B06E67E2109EBA41BC67FF20CC8AC80D7B6EE1A95465B3B2657533EA56D92D539E5064360EA4850FED2D1BFDF040103DF0314EE23B616C95C02652AD18860E48787C079E8E85ADF050400000000";
//            String cakeystr1 = cakeystr.replaceAll("9F220100", str);
////            logUtils.addCaseLog("公钥参数是" + cakeystr1 );
//            ret = My05updateRID(1, cakeystr1);
//            if (ret) {
//                logUtils.addCaseLog("公钥索引" + i + "新增成功");
//                logUtils.showCaseLog();
//            } else {
//                logUtils.addCaseLog("公钥索引" + i + "新增失败");
//                logUtils.showCaseLog();
//            }
//        }
//    }
//
//    public void K06003() {
//        My06importAmount(1234L);
//    }
//
//    public void K06006() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 0);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K06007() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 1234);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K06008() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 999999999);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K06009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, -1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K06010() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 3);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K07002() {
//        My07importAppSelect(1);
//    }
//
//    public void K07005() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 1000);
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, -1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K07008() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 1000);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K07009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K07010() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 2);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 1000);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K07013() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, -1);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K07014() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 3);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K07015() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 0);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08003() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
////        import接口的第一个入参
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K08004() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456789012");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K08005() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 2);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08006() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, -1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08007() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, null);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
////        此处金额1.01元
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08008() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "!acc& *");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08010() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08011() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08012() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08013() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08014() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K08015() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, null);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//
//    public void K08016() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, null);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean("isPinpadError", true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08017() {
//        byte[] str = "123456".getBytes();
//        My08importPin(1, str);
//    }
//
//    public void K08018() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08019() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, null);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08020() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, null);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08021() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08022() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08023() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08024() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, null);
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K08025() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K09001() {
//        My09importCertConfirmResult(0);
//    }
//
//    public void K09002() {
//        My09importCertConfirmResult(1);
//    }
//
//    public void K09003() {
//        My09importCertConfirmResult(2);
//    }
//
//    public void K10001() {
//        My10importCardConfirmResult(true);
//    }
//
//    public void K10002() {
//        My10importCardConfirmResult(false);
//    }
//
//    public void K10003() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
////        此参数决定此接口的入参
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K10004() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K10005() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K10006() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11001() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, "00");
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, "2012343");
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My11inputOnlineResult(bundle, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11003() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, null);
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, "2012343");
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My11inputOnlineResult(bundle, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11004() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, "");
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, "2012343");
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My11inputOnlineResult(bundle, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11005() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, "00");
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, "");
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My11inputOnlineResult(bundle, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11006() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, "00");
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, null);
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My11inputOnlineResult(bundle, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11007() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11008() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "09");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K11010() {
//        logUtils.addCaseLog("This case is abandoned");
//    }
//
//    public void K11011() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11012() {
//        logUtils.addCaseLog("This case is abandoned");
//    }
//
//    public void K11013() {
//        logUtils.addCaseLog("This case is abandoned");
//    }
//
//    public void K11014() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "910A1A1B1C1D1E1F2A2B3030\n");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//        My11inputOnlineResult(null, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11015() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, "00");
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//
//        My11inputOnlineResult(bundle, null);
//    }
//
//    public void K11016() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11017() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, null);
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, null);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11018() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11019() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "01");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11020() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11021() {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        bundle.putString(BUNDLE_IMPORT_RESCODE, "00");
//        bundle.putString(BUNDLE_IMPORT_AUTHCODE, "2012343");
//        bundle.putString(BUNDLE_IMPORT_FIELD55,
//                "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//
//        My11inputOnlineResult(bundle, new OnlineResultHandler.Stub() {
//            @Override
//            public void onProccessResult(int result, Bundle data) throws RemoteException {
//                logUtils.addCaseLog("onProccessResult callback");
//                logUtils.addCaseLog("On-line processing result" + result);
//                String str = "TC_DATA:" + data.getString(BUNDLE_TC_DATA) + "\n" +
//                        "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                        "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//                Message message = new Message();
//                message.getData().putString("message", str);
//                handler.sendMessage(message);
//            }
//        });
//    }
//
//    public void K11022() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K11023() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//
//    public void K12001() {
//        byte[] acquirerID = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
//        byte[] termCap = {(byte) 0xe0, (byte) 0xf1, (byte) 0xc8};
//        byte[] addTermCap = {(byte) 0xe0, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01};
//        byte[] countryCode = {(byte) 0x01, (byte) 0x56};
//        byte[] currencyCode = {(byte) 0x01, (byte) 0x56};
//        byte[] termType = {(byte) 0x22};
//
//        Collection<String> tlvList = new ArrayList<String>();
//
//        tlvList.add(byte2HexStr(asc2Bcd("9F15020000")));
//        tlvList.add(byte2HexStr(asc2Bcd("9F160F")) + byte2HexStr("123456789012345".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F4E0D")) + byte2HexStr("Verifone Test".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1C08")) + byte2HexStr("12345678".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F0106")) + byte2HexStr(acquirerID));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1E08")) + byte2HexStr("50342027".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3501")) + byte2HexStr(termType));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3303")) + byte2HexStr(termCap));
//        tlvList.add(byte2HexStr(asc2Bcd("9F4005")) + byte2HexStr(addTermCap));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1A02")) + byte2HexStr(countryCode));
//        tlvList.add(byte2HexStr(asc2Bcd("5F2A02")) + byte2HexStr(currencyCode));
//        tlvList.add(byte2HexStr(asc2Bcd("5F3601" + "02")));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3C02")) + byte2HexStr(currencyCode));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3D0102")));
//        My12setEMVData((List<String>) tlvList);
//    }
//
//
////    String getStrings(byte[] bytes)
////    {
////        String str = "";
////        for (int i = 0; i < bytes.length; i++)
////        {
////            str = str + bytes[i];
////        }
////        return str;
////    }
//
//    public void K12002() {
//        byte[] acquirerID = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
//        byte[] termCap = {(byte) 0xe0, (byte) 0xf1, (byte) 0xc8};
//        byte[] addTermCap = {(byte) 0xe0, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01};
//        byte[] countryCode = {(byte) 0x01, (byte) 0x56};
//        byte[] currencyCode = {(byte) 0x01, (byte) 0x56};
//        byte[] termType = {(byte) 0x22};
//
//        Collection<String> tlvList = new ArrayList<String>();
//
//        tlvList.add(byte2HexStr(asc2Bcd("9F150200")));
//        tlvList.add(byte2HexStr(asc2Bcd("9F160F")) + byte2HexStr("999999999999999".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F4E0D")) + byte2HexStr("VERIFONE TEST".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1C08")) + byte2HexStr("88888888".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F0106")) + byte2HexStr(acquirerID));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1E08")) + byte2HexStr("50342028".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3501")) + byte2HexStr(termType));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3303")) + byte2HexStr(termCap));
//        tlvList.add(byte2HexStr(asc2Bcd("9F4005")) + byte2HexStr(addTermCap));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1A02")) + byte2HexStr(countryCode));
//        tlvList.add(byte2HexStr(asc2Bcd("5F2A02")) + byte2HexStr(currencyCode));
//        tlvList.add(byte2HexStr(asc2Bcd("5F3601" + "02")));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3C02")) + byte2HexStr(currencyCode));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3D0102")));
//        My12setEMVData((List<String>) tlvList);
//    }
//
//    public void K12003() {
//        byte[] acquirerID = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
//        byte[] termCap = {(byte) 0xe0, (byte) 0xf1, (byte) 0xc8};
//        byte[] addTermCap = {(byte) 0xe0, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01};
//        byte[] countryCode = {(byte) 0x01, (byte) 0x56};
//        byte[] currencyCode = {(byte) 0x01, (byte) 0x56};
//        byte[] termType = {(byte) 0x22};
//
//        Collection<String> tlvList = new ArrayList<String>();
//
//        tlvList.add(byte2HexStr(asc2Bcd("9F150200")));
//        tlvList.add(byte2HexStr(asc2Bcd("9F160F")) + byte2HexStr("123456789012345".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F4E0D")) + byte2HexStr("Verifone Test".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1C08")) + byte2HexStr("12345678".getBytes()));
//        // tlvList.add(byte2HexSls.asc2Bcd("9F0106")) + byte2HexStr(acquirerID));
//        tlvList.add(byte2HexStr(asc2Bcd("9F1E08")) + byte2HexStr("50342027".getBytes()));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3501")) + byte2HexStr(termType));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3303")) + byte2HexStr(termCap));
//        tlvList.add(byte2HexStr(asc2Bcd("9F4005")) + byte2HexStr(addTermCap));
//        // tlvList.add(byte2HexStr(asc2Bcd("9F1A02")) + byte2HexStr(countryCode));
//        tlvList.add(byte2HexStr(asc2Bcd("5F2A02")) + byte2HexStr(currencyCode));
//        tlvList.add(byte2HexStr(asc2Bcd("5F3601" + "02")));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3C02")) + byte2HexStr(currencyCode));
//        tlvList.add(byte2HexStr(asc2Bcd("9F3D0102")));
//        My12setEMVData((List<String>) tlvList);
//    }
//
//    public void K12004() {
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("abcdedfsfsdfsdfs21 34234234sdfsdfsdfws@#￥ 捏\"囧槑（mei）");
//        My12setEMVData(strings);
//    }
//
//    public void K12005() {
//        ArrayList<String> strings = new ArrayList<>();
//        My12setEMVData(strings);
//    }
//
//    public void K12006() {
//        My12setEMVData(null);
//    }
//
//    public void K13001() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13002() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }//K13002
//
//    public void K13003() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }//K13003
//
//    public void K13004() {
//        //abcdedfsfsdfsd  fs213423423  4sdfsdfs  dfws！@#￥   捏"囧槑（mei）
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13005() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13006() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13007() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13008() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K13010() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//
//    public void K14001() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, "StandardData");//standarddatd就是四个tag：9F79、9F51,DF79,DF71
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K14002() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, "StandardData");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//
//
//    }
//
//
//    public void K14003() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, "abcdedfsfsdfsdfs2134234234sdfsdfsdfws！@#￥ 捏\"囧槑（mei）");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K14004() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, null);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K14005() {
//
//        My14getCardData("9F79");
//    }
//
//    public void K14006() {
//        My14getCardData("9F51");
//    }
//
//    public void K14007() {
//        My14getCardData("DF79");
//    }
//
//    public void K14008() {
//        My14getCardData("DF71");
//    }
//
//    public void K14009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, "");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K14010() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, "9F1B");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K14011() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA, true);
//        importData.putString(BUNDLE_IMPORT_GET_CARD_DATA, "11");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K15001() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(EC_BALANCE, startPbocData, new MyPbocHandler(importData));
//        My15getPBOCData("BALANCE");
//    }
//
//    public void K15002() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(EC_BALANCE, startPbocData, new MyPbocHandler(importData));
//        My15getPBOCData("BALANCE");
//    }
//
//    public void K15003() {
//        My15getPBOCData(null);
//    }
//
//    public void K15004() {
//        My15getPBOCData("abc34234sfws！@#￥\"囧槑（m）");
//    }
//
//    public void K15005() {
//        String str = "";
//        for (int i = 0; i < 2048; i++) {
//            str = str + i;
//        }
//        My15getPBOCData(str);
//    }
//
////    public void K15006()
////    {
////        My15getPBOCData("pan");
////    }
//    public void K15008() {
//        My15getPBOCData("");
//    }
//
//    public void K16001() {
//        CandidateAppInfo candidateAppInfo = My16getCandidateAppInfo();
//    }
//
//    public void K16002() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test of Agricultural Bank");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "99998888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//        CandidateAppInfo candidateAppInfo = My16getCandidateAppInfo();
//    }
//
//    public void K16003() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test of Agricultural Bank");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "99998888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//        CandidateAppInfo candidateAppInfo = My16getCandidateAppInfo();
//    }
//
//    public void K16004() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test of Agricultural Bank");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "99998888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//        CandidateAppInfo candidateAppInfo = My16getCandidateAppInfo();
//    }
//
//    public void K17001() {
//        Bundle intent = new Bundle();
//
//        intent.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        intent.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        intent.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        intent.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        intent.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//
//        intent.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        My17startPBOC(1, intent, null);
//    }
//
//    public void K17002() {
//        Bundle importData = new Bundle();
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(1, null, new MyPbocHandler(importData));
//    }
//
//    public void K17003() {
//        Bundle intent = new Bundle();
//        Bundle importData = new Bundle();
//
//        intent.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        intent.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        intent.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        intent.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        intent.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        intent.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//        //交易类型非法
//        My17startPBOC(0, intent, new MyPbocHandler(importData));
//    }
//
//    public void K17004() {
//        Bundle intent = new Bundle();
//        Bundle importData = new Bundle();
//
//        intent.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        intent.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        intent.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        intent.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        intent.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        intent.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        intent.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//        //交易类型非法
//        My17startPBOC(13, intent, new MyPbocHandler(importData));
//    }
//
//    public void K17005() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, -1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData, 7));
//    }
//
//    public void K17006() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "1234567");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
////        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//        My17startPBOC(Q_QUERY, startPbocData, new MyPbocHandler(importData, Q_QUERY));
//    }
//
//    public void K17007() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_QUERY, startPbocData, new MyPbocHandler(importData, Q_QUERY));
//    }
//
//    public void K17008() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(13, startPbocData, new MyPbocHandler(importData, Q_QUERY));
//    }
//
//    public void K17009() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(13, startPbocData, new MyPbocHandler(importData, Q_QUERY));
//    }
//
//    public void K17010() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(1, startPbocData, new MyPbocHandler(importData, EC_BALANCE));
//    }
//
//    public void K17011() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "9F1A0201569F02060000000001005F2A0201569A031801029C0100950500000000009F3704A82256BE82027C009F26085626A1B84A2EEB519F2701409F100A07010103900000010A019F360234939F03060000000000009F3303E0F8C89F34031F00009F74064543433030318408A000000333010101");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(EC_BALANCE, startPbocData, new MyPbocHandler(importData, EC_BALANCE));
//    }
//
//    public void K17012() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "101938");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17013() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17014() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 100001);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17015() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10001);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17016() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 9);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17017() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17018() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17019() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData, Q_PURCHASE));
//    }
//
//    public void K17020() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(8, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17021() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非接脱机限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17022() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(Q_PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    //// TODO: 2017/1/12   一下参数*100
//    public void K17023() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "88888888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);//大于DF21的值，才会输密
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "101938");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17024() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17025() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17026() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 100001);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 999999999);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17027() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 9999999);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17028() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17029() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17030() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17031() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17032() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17033() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17034() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17035() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
////    public void K17036()
////    {
////        Bundle startPbocData = new Bundle();
////        Bundle importData = new Bundle();
////
////        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
////        //非解脱及限额  1000
////        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
////        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
////        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
////        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
////        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
////        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "020001020270");
////        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "001");
////        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
////
////        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
////        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
////        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
////        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 10);
////        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
////
////        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
////        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
////        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
////        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
////        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
////
////        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
////
////    }
//
//    public void K17037() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17038() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17039() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17040() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17041() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 999999999);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17042() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17043() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17044() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(SALE_VOID, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17045() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17046() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17047() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17048() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17049() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17050() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17051() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17052() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17053() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17054() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17055() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17056() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17057() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17058() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17059() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(8, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17060() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 2);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17061() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(13, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17062() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, -1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test of Agricultural Bank");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "99998888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(CHECK_BALANCE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17063() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, -1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(1, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17064() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, -1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(13, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17065() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, -1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(12, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17066() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(12, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17067() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17068() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(8, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17069() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17070() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17071() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17072() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17073() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17074() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17075() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17076() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17077() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, false);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(9, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17078() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 12345);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test of Agricultural Bank");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "99998888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 123);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(SIMPLE_PROCESS, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17079() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test of Agricultural Bank");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "123456789012345");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "99998888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(PRE_AUTH, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17080() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 101);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(10, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17081() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(12, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17082() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(1, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17083() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00100000");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 1);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17084() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(7, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17085() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        //非解脱及限额  1000
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "88888888");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);//大于DF21的值，才会输密
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "101938");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//        importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K17087() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My17startPBOC(TRANSFER, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17088() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My17startPBOC(TRANSFER, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K17089() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 1);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "00000001");
//        startPbocData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My17startPBOC(SIMPLE_PROCESS, startPbocData, new MyPbocHandler(importData));
//
//    }
//
//    public void K18001() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 0);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean("cancelAmountConfirmResult", true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K18002() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 100);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K18003() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 100);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "ABC test");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean("cancelSelectApplication", true);
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K18004() {
//        Bundle startPbocData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//        startPbocData.putLong(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 100);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//        startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "010001020270123");
//        startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "12345678");
//
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 0);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        My17startPBOC(PURCHASE, startPbocData, new MyPbocHandler(importData));
//    }
//
//    public void K18005() {
//        K01001();
//        My18abortPBOC();
//    }
//
//    public void K18006() {
//        My18abortPBOC();
//    }
//
//    public void K21001() {
//        My21getAID(1);
//    }
//
//    public void K21002() {
//        My21getAID(2);
//    }
//
//    public void K21003() {
//        My21getAID(1);
//    }
//
//    public void K21004() {
//        My21getAID(2);
//    }
//
//    public void K21005() {
//        My21getAID(0);
//    }
//
//    public void K21006() {
//        My21getAID(3);
//    }
//
//    public void K22001() {
//        My22getRID();
//    }
//
//    public void K22002() {
//        My22getRID();
//    }
//
//    public void K23001() {My23getProcessCardType();}
//
//    public void K23002() {My23getProcessCardType();}
//
//    public void K23003() {My23getProcessCardType();}
//
//    public void K23004() {My23getProcessCardType();}
//
//    public void K23005() {My23getProcessCardType();}
//
//    public void K23006() {My23getProcessCardType();}
//
//    public void K23007() {My23getProcessCardType();}
//
//    public void K23008() {My23getProcessCardType();}
//
//    public void K23009() {My23getProcessCardType();}
//
//    public void K23010() {My23getProcessCardType();}
//
//    public void K23011() {My23getProcessCardType();}
//
//    public void K24001() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24002() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24003() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x20);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(2, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24004() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,false);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x20);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(2, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24005() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x20);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(2, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24006() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(0, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24007() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(3, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24008() {
//        Bundle importData = new Bundle();
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, null, new MyPbocHandler(importData));
//
//    }
//
//    public void K24009() {
//        Bundle startemvData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        My24startEMV(1, startemvData, null);
//
//    }
//
//    public void K24010() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,false);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x20);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(2, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24011() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, -1);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24012() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 1);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24013() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, null);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24014() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24015() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, null);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24016() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24017() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "12345678901234");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24018() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "1234567890123456");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24019() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, null);
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24020() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24021() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "1234567");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24022() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "123456789");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24023() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, -1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24024() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 2);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24025() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0xFF);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24026() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, false);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24027() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x31);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24028() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x31);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24029() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x40);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 1);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24030() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x40);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 1);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24031() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x03);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24032() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x03);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24033() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x20);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(2, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24034() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x20);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(2, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24035() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24036() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24037() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 100);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24038() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 101);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24039() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 0);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24040() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 101);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24041() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 100000);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24042() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 100001);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24043() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 101);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24044() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 101);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24045() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 101);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, false);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//    public void K24046() {
//        Bundle startemvData = new Bundle();
//        Bundle importData = new Bundle();
//
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_PANCONFONSIMPROCESS,true);
//        startemvData.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//        startemvData.putByte(BUNDLE_STARTEMVPARAM_TRANSCODE,(byte)0x00);
//        startemvData.putLong(BUNDLE_STARTEMVPARAM_AUTHAMOUNT, 101);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_QPBOC, false);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_SUPPORT_SM, true);
//        startemvData.putBoolean(BUNDLE_STARTEMVPARAM_IS_QPBOC_FORCE_ONLINE, false);
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_NAME, "Service test for overseas version");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_MERCHANT_ID, "123456789012345");
//        startemvData.putString(BUNDLE_STARTEMVPARAM_TERMINAL_ID, "12345678");
//        startemvData.putBoolean(BUNDLE_NOT_CHANGE_CARDTYPE, true);
//
//        importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//        importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//        importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//        importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//
//        importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//        importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//        importData.putString(BUNDLE_IMPORT_AUTHCODE, "010203");
//        importData.putString(BUNDLE_IMPORT_FIELD55, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
//
//        My24startEMV(1, startemvData, new MyPbocHandler(importData));
//
//    }
//
//
//
//    /**
//     * pboc检卡
//     *
//     * @param cardOption 设置参数
//     * @param timeout    超时时间,以秒为单位
//     * @param listener   监听回调
//     */
//    public void My01checkCard(Bundle cardOption, int timeout, CheckCardListener listener) {
//        try {
//            logUtils.addCaseLog("checkCard execute");
//            ipboc.checkCard(cardOption, timeout, listener);
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("checkCard Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My02stopCheckCard() {
//        try {
//            ipboc.stopCheckCard();
//            logUtils.addCaseLog("stopCheckCard execute");
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("stopCheckCard Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My03readUPCard(UPCardListener listener) {
//        try {
//            ipboc.readUPCard(listener);
//            logUtils.addCaseLog("readUPCard execute");
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("readUPCard Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public boolean My04updateAID(int operation, int aidType, String aid) {
//        try {
//            logUtils.addCaseLog("updateAID execute");
//            logUtils.addCaseLog("Update AID:\n" + aid);
//            Log.d("PBOC", "Update AID:" + aid);
//            boolean isSuccess = ipboc.updateAID(operation, aidType, aid);
//            Log.d("TAG", "" + isSuccess);
//
//            if (isSuccess) {
//                logUtils.addCaseLog("updateAID return true");
//                logUtils.addCaseLog("Get AID:\n" + Arrays.toString(ipboc.getAID(aidType)));
//                Log.d("TAG", "Access to the kernel AID:" + Arrays.toString(ipboc.getAID(aidType)));
//            } else {
//                logUtils.addCaseLog("updateAID return false");
//            }
//            return isSuccess;
//        } catch (Exception e) {
//            logUtils.addCaseLog("updateAID Perform abnormal");
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean My05updateRID(int operation, String rid) {
//        try {
//            Log.d("PBOC", "update RID:" + rid);
//            boolean isSuccess = ipboc.updateRID(operation, rid);
//            if (isSuccess) {
//                logUtils.addCaseLog("updateRID return true");
//                logUtils.addCaseLog("Get RID:\n" + Arrays.toString(ipboc.getRID()));
//                Log.d("PBOC", "Access to the kernel RID:" + Arrays.toString(ipboc.getRID()));
//            } else {
//                logUtils.addCaseLog("updateRID return false");
//            }
//            return isSuccess;
//        } catch (Exception e) {
//            logUtils.addCaseLog("updateAID Perform abnormal");
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public void My06importAmount(long amount) {
//        try {
//            logUtils.addCaseLog("importAmount execute");
//            ipboc.importAmount((int) amount);
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("importAmount Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My07importAppSelect(int index) {
//        try {
//            logUtils.addCaseLog("importAppSelect execute:" + index + "APP");
//            ipboc.importAppSelect(index);
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("importAppSelect Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My08importPin(int option, byte[] pin) {
//        try {
//            logUtils.addCaseLog("importPin execute");
//            ipboc.importPin(option, pin);
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("importPin Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My09importCertConfirmResult(int option) {
//        try {
//            ipboc.importCertConfirmResult(option);
//            logUtils.addCaseLog("importCertConfirmResult execute end");
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("importCertConfirmResult Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My10importCardConfirmResult(boolean pass) {
//        try {
//            ipboc.importCardConfirmResult(pass);
//            logUtils.addCaseLog("\nimportCardConfirmResult execute end");
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("\nimportCardConfirmResult Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My11inputOnlineResult(Bundle onlineResult, OnlineResultHandler handler) {
//        try {
//            ipboc.inputOnlineResult(onlineResult, handler);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void My12setEMVData(List<String> tlvList) {
//        try {
//            ipboc.setEMVData(tlvList);
//            emvl2 = FunctionApiHolder.getInstance().getEmvL2();
//            emvl2.init(MyApplication.getContext() ,"com.verifone.service_demo");
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F15"),2)));//type传2，Get terminal parameters
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F16"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F4E"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F1C"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F01"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F1E"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F35"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F33"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F40"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F1A"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("5F2A"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("5F36"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F3C"),2)));
//            logUtils.addCaseLog("Get terminal parameters:\n" + byte2HexStr(emvl2.getTag(StringUtil.hexStr2Bytes("9F3D"),2)));
//        } catch (Exception e) {
//            logUtils.addCaseLog("setEMVData Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public void My13getAppTLVList(String[] tagList) {
//        try {
//            String strs = ipboc.getAppTLVList(tagList);
//            logUtils.addCaseLog("\ngetAppTLVList callback completed");
//            if (null != strs) {
//                logUtils.addCaseLog("AppTLVList: " + strs);
//            } else {
//                logUtils.addCaseLog("The return length is empty");
//            }
//        } catch (Exception e) {
//            logUtils.addCaseLog("getAppTLVList callback exception");
//            e.printStackTrace();
//        }
//    }
//
//    public byte[] My14getCardData(String tagName) {
//        try {
//            byte[] strs = ipboc.getCardData(tagName);
//
//            logUtils.addCaseLog("\ngetCardData execute end TAG:" + tagName);
//            if (null != strs && strs.length > 0) {
//                logUtils.addCaseLog("" + byte2HexStr(strs));
//            } else {
//                logUtils.addCaseLog("The return length is empty");
//            }
//            Log.i(TAG, "tagName:" + tagName + strs);
//            return strs;
//        } catch (Exception e) {
//            logUtils.addCaseLog("getCardData Perform abnormal");
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String My15getPBOCData(String tagName) {
//        try {
//            logUtils.addCaseLog("\ngetPBOCData execute" + tagName);
//            String str = ipboc.getPBOCData(tagName);
//            if (null != str && str.length() > 0) {
//                logUtils.addCaseLog("getPBOCData return" + str);
//            } else {
//                logUtils.addCaseLog("getPBOCData return null");
//            }
//            return str;
//        } catch (Exception e) {
//            logUtils.addCaseLog("getPBOCData Perform abnormal");
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public CandidateAppInfo My16getCandidateAppInfo() {
//        try {
//            CandidateAppInfo candidateAppInfo = ipboc.getCandidateAppInfo();
//            logUtils.addCaseLog("getCandidateAppInfo execute");
//            if (null != candidateAppInfo) {
//                logUtils.addCaseLog("getCandidateAppInfo success");
//            } else {
//                logUtils.addCaseLog("getCandidateAppInfo return null");
//            }
//            return candidateAppInfo;
//        } catch (Exception e) {
//            logUtils.addCaseLog("getCandidateAppInfo Perform abnormal");
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public void My17startPBOC(final int transType, final Bundle intent, final PBOCHandler pbochandler) {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        String msg;
//
//        if (intent == null) {
//            msg = "Input parameter error";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//            return;
//        }
//        if (intent.getBoolean(BUNDLE_NOT_CHANGE_CARDTYPE)) {
//            int cardType = intent.getInt(BUNDLE_STARTPBOCPARAM_CARDTYPE);
//            if (cardType == 0)
//                logUtils.addCaseLog("Please insert the card");
//            else if (cardType == 1)
//                logUtils.addCaseLog("Please swipe the card");
//        } else
//            logUtils.addCaseLog("Please insert/wave/swipe your card");
//
//        My01checkCard(cardOption, 30, new CheckCardListener.Stub() {
//            String msg;
//
//            @Override
//            public void onCardSwiped(Bundle track) throws RemoteException {
//                msg = "Card detection: credit card swiped successfully";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//
//                My02stopCheckCard();
//            }
//
//            @Override
//            public void onCardPowerUp() throws RemoteException {
//                msg = "Card test: IC card inserted successfully";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//
//                My02stopCheckCard();
//
//                if (pbochandler == null) {
//                    message = new Message();
//                    msg = "Handler is null";
//                    message.getData().putString("message", msg);
//                    handler.sendMessage(message);
//                }
//                try {
//                    Bundle intent1 = intent;
//
//                    /*don't change transaction type for test case K17, card type will be changed according the check card result for other test case */
//                    if (intent.getBoolean(BUNDLE_NOT_CHANGE_CARDTYPE) == false)
//                        intent1.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//                    ipboc.startPBOC(transType, intent1, pbochandler);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCardActivate() throws RemoteException {
//                msg = "Card test: sipe contact-less card successfully";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//
//                My02stopCheckCard();
//
//                if (pbochandler == null) {
//                    message = new Message();
//                    Log.d(TAG, "Handler is null");
//                    msg = "Handler is null";
//                    message.getData().putString("message", msg);
//                    handler.sendMessage(message);
//                }
//
//                try {
//                    Bundle intent1 = intent;
//
//                    /*don't change transaction type for test case K17, card type will be changed according the check card result for other test case */
//                    if (intent.getBoolean(BUNDLE_NOT_CHANGE_CARDTYPE) == false)
//                        intent1.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 1);
//                    ipboc.startPBOC(transType, intent1, pbochandler);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onTimeout() throws RemoteException {
//                logUtils.addCaseLog("Card detection: timeout");
//                My02stopCheckCard();
//                Message message1 = new Message();
//                message1.getData().putString("message", msg);
//                handler.sendMessage(message1);
//            }
//
//            @Override
//            public void onError(int error, String message) throws RemoteException {
//                msg = "card detection: Error" + error + message;
//
//                My02stopCheckCard();
//                Message message1 = new Message();
//                if (error == 3)
//                    message1.getData().putString("message", msg + "Fallback");
//                else message1.getData().putString("message", msg);
//                handler.sendMessage(message1);
//
//                if (error == 3) {
//                    Bundle startPbocData = new Bundle();
//                    Bundle importData = new Bundle();
//
//                    startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
//                    //非解脱及限额  1000
//                    startPbocData.putInt(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 100);
//                    startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
//                    startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
//                    startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
//                    startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "VFI test");
//                    startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "020001020270");
//                    startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "001");
//
//                    /*data for test process*/
//                    importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
//                    importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
//                    importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
//                    importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
//                    importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
//                    importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
//
//                    /*data for online process*/
//                    importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
//                    importData.putString(BUNDLE_IMPORT_RESCODE, "00");
//                    importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
//                    importData.putString(BUNDLE_IMPORT_FIELD55, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
//
//                    My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
//                }
//            }
//        });
//    }
//
//    public void My18abortPBOC() {
//        try {
//            ipboc.abortPBOC();
//            logUtils.addCaseLog("abortPBOC execute");
//        } catch (Exception e) {
//            logUtils.addCaseLog("abortPBOC Perform abnormal");
//            e.printStackTrace();
//        }
//    }
//
//    public String[] My21getAID(int type) {
//        try {
//            logUtils.addCaseLog("getAID:\n" + Arrays.toString(ipboc.getAID(type)));
//        } catch (Exception e) {
//            logUtils.addCaseLog("getAID Perform abnormal");
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String[] My22getRID() {
//        try {
//            logUtils.addCaseLog("getRID:\n" + Arrays.toString(ipboc.getRID()));
//        } catch (Exception e) {
//            logUtils.addCaseLog("getRID Perform abnormal");
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public int My23getProcessCardType() {
//        try {
//            int ret = ipboc.getProcessCardType();
//            logUtils.addCaseLog("No card type:" + ret);
//            return ret;
//        } catch (Exception e) {
//            logUtils.addCaseLog("get contact-less card type Perform abnormal");
//            e.printStackTrace();
//            return -1;
//        }
//    }
//
//    public void My24startEMV(final int processType, final Bundle intent, final PBOCHandler pbochandler) {
//        Bundle cardOption = new Bundle();
//        cardOption.putBoolean("supportMagCard", true);
//        cardOption.putBoolean("supportICCard", true);
//        cardOption.putBoolean("supportRFCard", true);
//        String msg;
//
//        if (intent == null) {
//                msg = "input parameter error";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//                return;
//        }
//        if (intent.getBoolean(BUNDLE_NOT_CHANGE_CARDTYPE)) {
//            int cardType = intent.getInt(BUNDLE_STARTEMVPARAM_CARDTYPE);
//            if (cardType == 0)
//                logUtils.addCaseLog("Please insert the card");
//            else if (cardType == 1)
//                logUtils.addCaseLog("Please swipe the card");
//        } else
//            logUtils.addCaseLog("Please insert/wave/swipe your card");
//
//        My01checkCard(cardOption, 30, new CheckCardListener.Stub() {
//            String msg;
//
//            @Override
//            public void onCardSwiped(Bundle track) throws RemoteException {
//                msg = "Card detection: credit card swiped successfully";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//
//                My02stopCheckCard();
//            }
//
//            @Override
//            public void onCardPowerUp() throws RemoteException {
//                msg = "Card test: IC card inserted successfully";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//
//                My02stopCheckCard();
//
//                if (pbochandler == null) {
//                    message = new Message();
//                    msg = "Handler is null";
//                    message.getData().putString("message", msg);
//                    handler.sendMessage(message);
//                }
//                try {
//                    Bundle intent1 = intent;
//
//                    /*don't change transaction type for test case K17, card type will be changed according the check card result for other test case */
//                    if (intent.getBoolean(BUNDLE_NOT_CHANGE_CARDTYPE) == false)
//                        intent1.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 0);
//                    ipboc.startEMV(processType, intent1, pbochandler);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCardActivate() throws RemoteException {
//                msg = "Card test: swipe contact-less card successfully";
//                Message message = new Message();
//                message.getData().putString("message", msg);
//                handler.sendMessage(message);
//
//                My02stopCheckCard();
//
//                if (pbochandler == null) {
//                    message = new Message();
//                    Log.d(TAG, "Handler is null");
//                    msg = "Handler is null";
//                    message.getData().putString("message", msg);
//                    handler.sendMessage(message);
//                }
//
//                try {
//                    Bundle intent1 = intent;
//
//                    /*don't change transaction type for test case K17, card type will be changed according the check card result for other test case */
//                    if (intent.getBoolean(BUNDLE_NOT_CHANGE_CARDTYPE) == false)
//                        intent1.putInt(BUNDLE_STARTEMVPARAM_CARDTYPE, 1);
//                    ipboc.startEMV(processType, intent1, pbochandler);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onTimeout() throws RemoteException {
//                logUtils.addCaseLog("Card detection: timeout");
//                My02stopCheckCard();
//                Message message1 = new Message();
//                message1.getData().putString("message", msg);
//                handler.sendMessage(message1);
//            }
//
//            @Override
//            public void onError(int error, String message) throws RemoteException {
//                msg = "Card detection: error" + error + message;
//
//                My02stopCheckCard();
//                Message message1 = new Message();
//                if (error == 3)
//                    message1.getData().putString("message", msg + "Fallback");
//                else message1.getData().putString("message", msg);
//                handler.sendMessage(message1);
//
////                if (error == 3) {
////                    Bundle startPbocData = new Bundle();
////                    Bundle importData = new Bundle();
////
////                    startPbocData.putInt(BUNDLE_STARTPBOCPARAM_CARDTYPE, 0);
////                    //非解脱及限额  1000
////                    startPbocData.putInt(BUNDLE_STARTPBOCPARAM_AUTHAMOUNT, 100);
////                    startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_QPBOC, true);
////                    startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_SUPPORT_SM, true);
////                    startPbocData.putBoolean(BUNDLE_STARTPBOCPARAM_IS_QPBOC_FORCE_ONLINE, true);
////                    startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_NAME, "VFI test");
////                    startPbocData.putString(BUNDLE_STARTPBOCPARAM_MERCHANT_ID, "020001020270");
////                    startPbocData.putString(BUNDLE_STARTPBOCPARAM_TERMINAL_ID, "001");
////
////                    /*data for test process*/
////                    importData.putInt(BUNDLE_IMPORT_APPSELECTION, 1);
////                    importData.putInt(BUNDLE_IMPORT_IS_PIN_INPUT, 1);
////                    importData.putString(BUNDLE_IMPORT_IMPORT_PIN, "123456");
////                    importData.putInt(BUNDLE_IMPORT_IMPORT_AMOUNT, 101);
////                    importData.putBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM, false);
////                    importData.putBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA, true);
////
////                    /*data for online process*/
////                    importData.putBoolean(BUNDLE_IMPORT_IS_ONLINE, true);
////                    importData.putString(BUNDLE_IMPORT_RESCODE, "00");
////                    importData.putString(BUNDLE_IMPORT_AUTHCODE, "123456");
////                    importData.putString(BUNDLE_IMPORT_FIELD55, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
////
////                    My17startPBOC(11, startPbocData, new MyPbocHandler(importData));
////                }
//            }
//        });
//    }
//
//    public ArrayList<String> getApiList() {
//        return apiList;
//    }
//
//    public ArrayList<ArrayList<String>> getCaseNames() {
//        return caseNames;
//    }
//
//    public void runTheMethod(int groupPosition, int childPosition) {
//        String name = caseNames.get(groupPosition).get(childPosition);
//        logUtils.clearLog();
//        try {
//            Class aClass = Class.forName("moudles.PbocBtMoudle");
//            Method method = aClass.getMethod(name);
//            method.invoke(this);
//            logUtils.addCaseLog(name + "execute case end");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void showTheCaseInfo(int groupPosition, int childPosition) {
//        String name = caseNames.get(groupPosition).get(childPosition);
//        logUtils.printCaseInfo(name);
//    }
//
//    class MyListener extends CheckCardListener.Stub {
//        String msg;
//
//        @Override
//        public void onCardActivate() throws RemoteException {
//            msg = "Card detection: success";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//        }
//
//        @Override
//        public void onCardSwiped(Bundle track) throws RemoteException {
//            String msg = "PAN:" + track.getString(BUNDLE_PBOC_PAN) +
//                    "\nTRACK1:" + track.getString(BUNDLE_PBOC_TRACK1) +
//                    "\nTRACK2:" + track.getString(BUNDLE_PBOC_TRACK2) +
//                    "\nTRACK3:" + track.getString(BUNDLE_PBOC_TRACK3) +
//                    "\nSERVICE_CODE:" + track.getString(BUNDLE_PBOC_SERVICE_CODE) +
//                    "\nEXPIRED_DATE:" + track.getString("EXPIRED_DATE");
//            Log.e("tag", msg);
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//        }
//
//        @Override
//        public void onCardPowerUp() throws RemoteException {
//            msg = "Card test: IC card inserted successfully";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//        }
//
//        @Override
//        public void onTimeout() throws RemoteException {
//            msg = "Card detection: timeout";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//        }
//
//        @Override
//        public void onError(int error, String message) throws RemoteException {
//            msg = message;
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//        }
//    }
//
//    class MyAPDUListener extends UPCardListener.Stub {
//        @Override
//        public void onError(int error, String message) throws RemoteException {
//            Message message1 = new Message();
//            message1.getData().putString("message", "error card:" + error + "error information" + message);
//            handler.sendMessage(message1);
//        }
//
//        @Override
//        public void onRead(Bundle data) throws RemoteException {
//            Message message1 = new Message();
//            String str = "PAN:" + data.getString(BUNDLE_PBOC_PAN, "inexistence ") + "\n" +
//                    "TRACK2:" + data.getString(BUNDLE_PBOC_TRACK2, "inexistence ") + "\n" +
//                    "TRACK3:" + data.getString(BUNDLE_PBOC_TRACK3, "inexistence") + "\n" +
//                    "CARD_SN:" + data.getString(BUNDLE_PBOC_CARD_SN, "inexistence") + "\n" +
//                    "EXPIRED_DATE:" + data.getString("EXPIRED_DATE", "inexistence") + "\n" +
//                    "TLV_DATA:" + data.getString("TLV_DATA", "inexistence ");
//            message1.getData().putString("message", str);
//            handler.sendMessage(message1);
//        }
//    }
//
//    class MyPbocHandler extends PBOCHandler.Stub {
//        String msg;
//        int transType = 7;
//        String str;
//        int type;
//        Bundle importData;
//
//        public MyPbocHandler() {
//            type = 0;
//        }
//
//        public MyPbocHandler(Bundle importData) {
//            this.importData = importData;
//            type = 1;
//        }
//
//        public MyPbocHandler(int transType) {
//            this.transType = transType;
//            type = 2;
//        }
//
//        public MyPbocHandler(Bundle importData, int transType) {
//            this.importData = importData;
//            this.transType = transType;
//            type = 3;
//        }
//
//        @Override
//        public void onTransactionResult(int result, Bundle data) throws RemoteException {
//            Log.i(TAG, "onTransactionResult callback, result:" + result +
//                    "\nTC_DATA:" + data.getString(BUNDLE_TC_DATA) +
//                    "\nREVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA) +
//                    "\nERROR:" + data.getString(BUNDLE_TRANS_ERROR));
//
//            if ((type == 3) && (transType == EC_BALANCE || transType == Q_QUERY)) {
//                String ecBalance = My15getPBOCData("BALANCE");
//                Log.i(TAG, "BALANCE:" + ecBalance);
//
//                if (ecBalance != null)
//                    msg = "Electronic cash balance:" + ecBalance;
//                logUtils.addCaseLog(msg);
//
//            }
//
//            msg = "results:" + result + "\n" + data.getString(BUNDLE_TRANS_ERROR);
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//
//        }
//
//        @Override
//        public void onSelectApplication(List<String> appList) throws RemoteException {
//            int i = 1;
//            Log.d(TAG, "onSelectApplication callback");
//
//            Boolean cancelSelectApplication = importData.getBoolean("cancelSelectApplication");
//            if (cancelSelectApplication) {
//                My18abortPBOC();
//                logUtils.addCaseLog("Deselect the application");
//                return;
//            }
//
//            for (String str : appList) {
//                msg = i++ + ".AppName=" + str + "\n";
//
//            }
//            logUtils.addCaseLog(msg);
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//
//            My07importAppSelect(importData.getInt(BUNDLE_IMPORT_APPSELECTION));
//            //showMsgBox("应用选择",msg, "onSelectApplication", false);
//        }
//
//        @Override
//        public void onRequestOnlineProcess(Bundle aaResult) throws RemoteException {
//            // 结果类型：QPBOC_ARQC(201) - qPBOC联机请求;	AARESULT_ARQC(2) - 行为分析结果ARQC
////            Log.i(TAG, "onRequestOnlineProcess callback, RESULT:" + aaResult.getInt(BUNDLE_TRANS_RESULT) +
////                    "\nARQC_DATA:" + aaResult.getString(BUNDLE_ARQC_DATA) +
////                    "\nREVERSAL_DATA:" + aaResult.getString(BUNDLE_REVERSAL_DATA));
//            String result = "onRequestOnlineProcess callback RESULT:" + aaResult.getInt(BUNDLE_TRANS_RESULT) +
//                    "\nARQC_DATA:" + aaResult.getString(BUNDLE_ARQC_DATA) +
//                    "\nREVERSAL_DATA:" + aaResult.getString(BUNDLE_REVERSAL_DATA) +
//                    "\nCARD_ORG:" + aaResult.getString(BUNDLE_EMV_CARDORG) +
//                    "\nPAN:" + aaResult.getString(BUNDLE_PBOC_PAN) +
//                    "\nTRACK2:" + aaResult.getString(BUNDLE_PBOC_TRACK2) +
//                    "\nEXPIRD_DATE:" + aaResult.getString(BUNDLE_PBOC_EXPIRED_DATE) +
//                    "\nCARD_SN:" + aaResult.getString(BUNDLE_PBOC_CARD_SN) +
//                    "\nDATE:" + aaResult.getString(BUNDLE_EMV_DATE) +
//                    "\nTIME:" + aaResult.getString(BUNDLE_EMV_TIME) +
//                    "\nCARD_HOLDER_NAME:" + aaResult.getString(BUNDLE_EMV_HOLDERNAME);
//
//            Boolean getAppTLVListOption = importData.getBoolean(BUNDLE_IMPORT_GET_APPTLV_LIST);
//            if (getAppTLVListOption) {
//                String[] strlist = {"9F33", "9F40", "9F10", "9F26", "95", "9F37", "9F1E", "9F36",
//                        "82", "9F1A", "9A", "9B", "50", "84", "5F2A", "8F"};
//
//                My13getAppTLVList(strlist);
//            }
//
//            Boolean getData = importData.getBoolean(BUNDLE_IMPORT_IS_GET_PBOC_DATA);
//            if (getData) {
//                String strs = "PAN:" + My15getPBOCData(BUNDLE_PBOC_PAN) + "\n" +
//                        "TRACK2:" + My15getPBOCData(BUNDLE_PBOC_TRACK2) + "\n" +
//                        "CARD_SN:" + My15getPBOCData(BUNDLE_PBOC_CARD_SN) + "\n" +
//                        "EXPIRED_DATE:" + My15getPBOCData("EXPIRED_DATE") + "\n" +
//                        "DATE:" + My15getPBOCData("DATE") + "\n" +
//                        "TIME:" + My15getPBOCData("TIME") + "\n" +
//                        "BALANCE:" + My15getPBOCData("BALANCE") + "\n" +
//                        "CURRENCY:" + My15getPBOCData("CURRENCY");
//
//                Log.d(TAG, "getPBOCData : " + strs);
//                msg = strs;
//                Message message1 = new Message();
//                message1.getData().putString("message", "");
//                handler.sendMessage(message1);
//            }
//
//            String[] tvr = {"95"};
//            My13getAppTLVList(tvr);
//
//            msg = "\nOnline request: " + "\n" + result;
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//
//            /*Go online*/
//
//            /*do online finish process, 2nd GAC*/
//            My11inputOnlineResult(importData, new MyOnlineResultHandler());
//
//            //showMsgBox("联机请求","是否返回联机成功?", "onRequestOnlineProcess", false);
//        }
//
//        @Override
//        public void onRequestInputPIN(boolean isOnlinePin, int retryTimes) throws RemoteException {
//
//            String result = "onRequestInputPIN callback, isOnlinePin:" + isOnlinePin + "\n" +
//                    "retryTimes:" + retryTimes;
//
//            int IsPinInput = importData.getInt(BUNDLE_IMPORT_IS_PIN_INPUT);
//
//            if (importData.getBoolean("isPinpadError")) {
//                if (isOnlinePin)
//                    msg = "Request online PIN: password keyboard error";
//                else
//                    msg = "Request offline PIN: password keyboard error";
//            } else {
//                if (IsPinInput == 0) {
//                    if (isOnlinePin)
//                        msg = "Request online PIN: cancel";
//                    else
//                        msg = "Request offline PIN: cancel";
//                    My18abortPBOC();
//                } else {
//                    if (isOnlinePin)
//                        msg = "Request online PIN: " + IsPinInput;
//                    else
//                        msg = "Request online PIN: " + IsPinInput + "Retry count：" + retryTimes;
//                }
//            }
//
//            msg = result;
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//
//            String importpin = importData.getString(BUNDLE_IMPORT_IMPORT_PIN);
//            if (importpin == null)
//                My08importPin(IsPinInput, null);
//            else
//                My08importPin(IsPinInput, importpin.getBytes());
//        }
//
//        @Override
//        public void onRequestAmount() throws RemoteException {
//            Log.d(TAG, "onRequestAmount callback");
//
//            Boolean cancelAmountConfirmResult = importData.getBoolean("cancelAmountConfirmResult");
//            if (cancelAmountConfirmResult) {
//                My18abortPBOC();
//                msg = "Cancel import amount";
//            } else {
//                msg = "Please import the amount:" + importData.getInt(BUNDLE_IMPORT_IMPORT_AMOUNT);
//                //import amount to kernel
//                My06importAmount(importData.getInt(BUNDLE_IMPORT_IMPORT_AMOUNT));
//            }
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//        }
//
//        @Override
//        public void onConfirmCertInfo(String certType, String certInfo) throws RemoteException {
//            Log.d(TAG, "onConfirmCertInfo callback, certType:" + certType +
//                    "certInfo:" + certInfo);
//
//            msg = "证Certification: Confirmation";
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            handler.sendMessage(message1);
//
//            ipboc.importCertConfirmResult(1);
//        }
//
//        @Override
//        public void onConfirmCardInfo(Bundle info) throws RemoteException {
//            String result = "onConfirmCardInfo callback, \nPAN:" + info.getString(BUNDLE_PBOC_PAN) +
//                    "\nTRACK2:" + info.getString(BUNDLE_PBOC_TRACK2) +
//                    "\nCARD_SN:" + info.getString(BUNDLE_PBOC_CARD_SN)+
//                    "\nSERVICE_CODE:" + info.getString(BUNDLE_PBOC_SERVICE_CODE) +
//                    "\nEXPIRED_DATE:" + info.getString(BUNDLE_PBOC_EXPIRED_DATE);
//
//            Boolean isGetCardData = importData.getBoolean(BUNDLE_IMPORT_IS_GET_CARD_DATA);
//            if (isGetCardData == true) {
//                if ("StandardData".equals(importData.getString(BUNDLE_IMPORT_GET_CARD_DATA))) {
//                    My14getCardData("9F79");
//                    My14getCardData("9F51");
//                    My14getCardData("DF79");
//                    My14getCardData("DF71");
//                } else
//                    My14getCardData(importData.getString(BUNDLE_IMPORT_GET_CARD_DATA));
//            }
//
//            Boolean CancelCardConfirmResult = importData.getBoolean(BUNDLE_IMPORT_CANCEL_CARD_CONFIRM);
//
//            if (CancelCardConfirmResult) {
//                My18abortPBOC();
//                msg = "number:" + info.getString(BUNDLE_PBOC_PAN) + "cancel";
//                My10importCardConfirmResult(false);
//            } else {
//                msg = "number:" + info.getString(BUNDLE_PBOC_PAN) + "enter";
//                My10importCardConfirmResult(true);
//            }
//
//            msg = result;
//            Message message1 = new Message();
//            message1.getData().putString("message", msg);
//            Log.d(TAG, "onConfirmCardInfo: " + msg);
//            handler.sendMessage(message1);
//        }
//    }
//
//    class MyOnlineResultHandler extends OnlineResultHandler.Stub {
//
//        @Override
//        public void onProccessResult(int result, Bundle data) throws RemoteException {
//            logUtils.addCaseLog("\nOnProcessResult online post processing function");
//
//            // 结果类型:
////            Log.i(TAG, "onRequestOnlineProcess callback, RESULT:" + result +
////                    "\nTC_DATA:" + data.getString(BUNDLE_TC_DATA) +
////                    "\nSCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) +
////                    "\nREVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA));
//
//            String str = "online result callback, RESULT:" + result + "\nTC_DATA:" + data.getString(BUNDLE_TC_DATA, "n.s.(not specified)") + "\n" +
//                    "SCRIPT_DATA:" + data.getString(BUNDLE_SCRIPT_DATA) + "\n" +
//                    "REVERSAL_DATA:" + data.getString(BUNDLE_REVERSAL_DATA);
//            Message message = new Message();
//            message.getData().putString("message", str);
//            handler.sendMessage(message);
//        }
//    }
}
