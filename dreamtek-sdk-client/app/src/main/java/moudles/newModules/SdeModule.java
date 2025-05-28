package moudles.newModules;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.verifone.activity.R;
import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.EMVHandler;
import com.dreamtek.smartpos.deviceservice.aidl.EMVTransParams;
import com.dreamtek.smartpos.deviceservice.aidl.OnlineResultHandler;
import com.dreamtek.smartpos.deviceservice.aidl.RequestACTypeHandler;
import com.dreamtek.smartpos.deviceservice.aidl.sde.CardDetectionListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Utils.LogUtil;
import base.MyApplication;
import entity.cases.KeyBoardDatas;
import moudles.SdeMoudle;
import moudles.newModules.data.PanBundleStore;
import view.PinInputActivity;
import view.SecurityKeyboardActivity;

public class SdeModule extends TestModule{

    private static final String TAG = "SdeModule";
    Object sdeLock = new Object();
    public static final Object sdeLock1 = new Object();

    String getProcessType;
    String getIntent;
    String cardType;
    String importData;

    String appTLVDataMsg;
    String cardDataMsg;

    private String sdeResult = "";
    private int cardDetectionResult = -1;

    public static final int BundleConfig_String = 0;
    public static final int BundleConfig_int = 1;
    public static final int BundleConfig_boolean = 2;
    public static final int BundleConfig_byte = 3;
    public static final int BundleConfig_byte_A = 4;
    public static final int BundleConfig_long = 5;
    public static final int BundleConfig_float = 6;

    String sdataKey;
    String smode;
    String scardOption;
    String stimeout;

    String msg;


    protected boolean T_setEncryptDataKey(String dataKey1,String mode1) throws RemoteException {

        BundleConfig[] bundleConfigs = new BundleConfig[]{
                new BundleConfig("keyMngType",BundleConfig_int),
                new BundleConfig("index",BundleConfig_int)
        };
        Bundle dataKey = convert(dataKey1,bundleConfigs);
        Bundle mode = convert(mode1,null);
        sdataKey = dataKey1;
        smode = mode1;
        logUtils.addCaseLog("datakey = "+convert(dataKey));
        logUtils.addCaseLog("mode = "+convert(mode));
        boolean result = iSde.setEncryptDataKey(dataKey,mode);
        logUtils.addCaseLog("result = "+result);
        return result;
    }

    protected String T_getEncryptedMsgData(String data1,String extend1) throws RemoteException {
        byte[] data = StringUtil.hexStr2Bytes(data1);
//        byte[] data2 = BCDDecode.str2Bcd(data1);
//        byte[] data = data1.getBytes(StandardCharsets.UTF_8);
        Bundle extend = convert(extend1,null);
        Bundle bundle = iSde.getEncryptedMsgData(data,extend);

        String encryptedData = StringUtil.byte2HexStr(bundle.getByteArray("encryptedData"));
        String ksn = StringUtil.byte2HexStr(bundle.getByteArray("ksn"));
        String initVec = StringUtil.byte2HexStr(bundle.getByteArray("initVec"));

        Bundle result = new Bundle();
        result.putString("encryptedData",encryptedData);
        result.putString("ksn",ksn);
        result.putString("initVec",initVec);

//        logUtils.addCaseLog(encryptedData+"_"+ksn+"_"+initVec);
//        logUtils.addCaseLog("bundle = "+bundle);
        logUtils.addCaseLog("return convert(result)"+convert(result));

        return encryptedData;

    }

    protected String T_cardDetection(String cardOption,String timeout) throws RemoteException {

        scardOption = cardOption;
        stimeout = timeout;

        synchronized ( sdeLock ){
            try {
                sdeLock.wait(10);   // 等待 10毫秒，清除未完成交易的消息
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cardDetectionResult = -1;
            sdeResult = "";
        }

        // 等待打印完成
        if( 0 != printMode){
            Message message1 = new Message();
            message1.getData().putString("message", "Waiting print finished to start check card!");
            handler.sendMessage(message1);

            LogUtil.d(TAG, "waiting print finished ...");
            ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(5);
            LogUtil.d(TAG, "waiting print finished ...");
            ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(5);
            LogUtil.d(TAG, "waiting print finished ... ok");

            try {
                Thread.sleep( 200 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        BundleConfig[] bundleConfigs = new BundleConfig[ ]{
//                new BundleConfig("supportMagCard", BundleConfig_boolean),
//                new BundleConfig("supportSmardCard", BundleConfig_boolean),
//                new BundleConfig("supportCTLSCard", BundleConfig_boolean)
//        };
        Bundle cardOption1 = convert( scardOption, null );
        int timeout1 = Integer.valueOf(stimeout);


        logUtils.addCaseLog("cardOption1 = "+convert(cardOption1));
        logUtils.addCaseLog("timeout1="+timeout1);

        iSde.cardDetection(cardOption1,timeout1,cardDetectionListener);

        int cardDetectionRet = 0;
        logUtils.addCaseLog("Waiting check card finished ...");
        synchronized ( sdeLock ){
            try {
                sdeLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cardDetectionRet = cardDetectionResult;
        }
        logUtils.addCaseLog( "Check card: " + cardDetectionRet );
        if( cardDetectionRet != 2 && cardDetectionRet != 3 ) {
            // 非插卡、贴卡
            return "cardDetectionRet=" + cardDetectionRet;
        }
        return "cardDetectionRet=" + cardDetectionRet;

    }

    protected boolean T_setObfuscatedPanNum(String first,String last) throws RemoteException {
        return iSde.setObfuscatedPanNum(Integer.valueOf(first),Integer.valueOf(last));
    }

    protected void T_cleanSensitiveData() throws RemoteException {
        iSde.cleanSensitiveData();
    }

    protected String T_getSDEVer() throws RemoteException {
        return iSde.getSDEVer();
    }

    protected boolean T_configCardBinRange(String path) throws RemoteException {
        boolean ret = iSde.configCardBinRange(path);
        logUtils.addCaseLog("ret = "+ret);
        return ret;
    }


//    String initSecurityKeyBoardViewParam;
//    String initPinInputViewParam;

    public static int activityCount = 0;

    public static final Object keyLock = new Object();

    protected String T_keyboardFromParam(String Bundle1,String keyId,String Bundle2,String pinBlockType,String keySystem,String mode,String extraParams){

        Bundle panBundle = PanBundleStore.getBundle();
        if (panBundle!=null){
            String PAN = panBundle.getString("PAN");
            String PIN = panBundle.getString("PIN");
            Log.d(TAG,"PAN = "+PAN+"; PIN="+PIN);
        }else {
            android.util.Log.d(TAG, "T_keyboardFromParam: panBundle is null");
        }

        KeyBoardDatas.getInstance().setPinBlock(null);
        synchronized (keyLock){
            getInitPinInputViewParam( keyId, Bundle2, pinBlockType, keySystem, mode, extraParams);
            getInitSecurityKeyBoardViewParam(Bundle1);
            while (activityCount>0){
                try {
                    keyLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        byte[] pinblock = KeyBoardDatas.getInstance().getPinBlock();
        logUtils.addCaseLog("测试pinblock="+ StringUtil.byte2HexStr(pinblock));


        return StringUtil.byte2HexStr(pinblock);
    }

    protected synchronized void getInitSecurityKeyBoardViewParam2(String initSecurityKeyBoardView){
        BundleConfig[] bundleConfig = new BundleConfig[]{
                new BundleConfig("displayKeyValue",BundleConfig_byte_A),
                new BundleConfig("pinLimit",BundleConfig_byte),
                new BundleConfig("timeout",BundleConfig_int)
        };
        Bundle bundle = convert(initSecurityKeyBoardView,bundleConfig);
        Bundle initBundle = new Bundle();
        initBundle.putByteArray("displayKeyValue",bundle.getByteArray("displayKeyValue"));
        initBundle.putInt("timeout",bundle.getInt("timeout"));
        initBundle.putByteArray("pinLimit", new byte[]{bundle.getByte("pinLimit")});

        Intent intent = new Intent(context,SecurityKeyboardActivity.class);
        intent.putExtra("initBundle",initBundle);

//        启动新的activity时，增加计数器
        synchronized (keyLock){
            activityCount++;
        }
        context.startActivity(intent);
    }

    protected synchronized void getInitPinInputViewParam2(String keyId,String Bundle2,String pinBlockType,String keySystem,String mode,String extraParams){

        Bundle initBundle = new Bundle();
        int skeyId = Integer.valueOf(keyId);
        initBundle.putInt("keyId",skeyId);
        initBundle.putInt("keyId2",skeyId);

        int spinBlockType = Integer.valueOf(pinBlockType);
        initBundle.putInt("pinBlockType",spinBlockType);

        int skeySystem = Integer.valueOf(keySystem);
        initBundle.putInt("keySystem",skeySystem);

        int smode = Integer.valueOf(mode);
        initBundle.putInt("mode",smode);

        BundleConfig[] bundleConfig = new BundleConfig[]{
                new BundleConfig("isOnline",BundleConfig_boolean),
                new BundleConfig("displayKeyValue",BundleConfig_byte_A),
                new BundleConfig("pinLimit",BundleConfig_byte),
                new BundleConfig("timeout",BundleConfig_int)
        };
        Bundle bundle = convert(Bundle2,bundleConfig);

        initBundle.putBoolean("isOnline",bundle.getBoolean("isOnline"));
        initBundle.putByteArray("displayKeyValue",bundle.getByteArray("displayKeyValue"));
        initBundle.putByteArray("pinLimit",new byte[]{bundle.getByte("pinLimit")});
        initBundle.putInt("timeout",bundle.getInt("timeout"));

        BundleConfig[] bundleConfig2 = new BundleConfig[]{
                new BundleConfig("dispersionType",BundleConfig_byte),
                new BundleConfig("notifyPinLenError",BundleConfig_boolean),
                new BundleConfig("random",BundleConfig_byte_A),
        };
        Bundle bundle2 = convert(extraParams,bundleConfig2);

        initBundle.putByte("dispersionType",bundle2.getByte("dispersionType"));
        initBundle.putBoolean("notifyPinLenError",bundle2.getBoolean("notifyPinLenError"));
        initBundle.putByteArray("random",bundle2.getByteArray("random"));


        Intent intent = new Intent(context, PinInputActivity.class);
        //启动新的activity时，增加计数器
        synchronized (keyLock){
            activityCount++;
        }
        intent.putExtra("initBundle",initBundle);
        context.startActivity(intent);
    }


    protected synchronized void getInitSecurityKeyBoardViewParam(String initSecurityKeyBoardView){
        Bundle initBundle = new Bundle();
        String[] parts = initSecurityKeyBoardView.split(",");
        String[] subparts = null;
        String[] values = null;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
            subparts = parts[i].split("_");
            for (int j = 0; j < subparts.length; j++) {
                subparts[j] = subparts[j].trim();
                values = subparts[j].split("=");
                if (values.length == 2){
                    String name = values[0].trim();
                    String value = values[1].trim();
                    switch (name){
                        case "displayKeyValue":
                            initBundle.putByteArray("displayKeyValue",StringtoCharArray(value));
                            break;
                        case "pinLimit":
                            String[] pinLimitArr = value.split("\\+");
                            byte[] byteArr = new byte[pinLimitArr.length];
                            for (int k = 0; k < pinLimitArr.length; k++) {
                                byteArr[k] = Byte.parseByte(pinLimitArr[k]);
                            }
                            initBundle.putByteArray("pinLimit",byteArr);
                            break;
                        case "timeout":
                            initBundle.putInt("timeout",Integer.valueOf(value));
                            break;

                    }
                }
            }
        }

        Intent intent = new Intent(context,SecurityKeyboardActivity.class);
        intent.putExtra("initBundle",initBundle);

        //启动新的activity时，增加计数器
        synchronized (keyLock){
            activityCount++;
        }
        context.startActivity(intent);

    }

    protected synchronized void getInitPinInputViewParam(String keyId,String Bundle2,String pinBlockType,String keySystem,String mode,String extraParams){

        Bundle initBundle = new Bundle();

        //转化bundle2
        String[] parts = Bundle2.split(",");
        String[] subparts = null;
        String[] values = null;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
            subparts = parts[i].split("_");
            for (int j = 0; j < subparts.length; j++) {
                subparts[j] = subparts[j].trim();
                values = subparts[j].split("=");
                if (values.length == 2){
                    String name = values[0].trim();
                    String value = values[1].trim();
                    switch (name){
                        case "isOnline":
                            initBundle.putBoolean("isOnline",Boolean.parseBoolean(value));
                            break;
                        case "displayKeyValue":
                            initBundle.putByteArray("displayKeyValue",StringtoCharArray(value));
                            break;
                        case "pinLimit":
                            initBundle.putByteArray("pinLimit",StringtoCharArray(value));
                            break;
                        case "timeout":
                            initBundle.putInt("timeout",Integer.valueOf(value));
                            break;
                    }
                }
            }
        }

        int skeyId = Integer.valueOf(keyId);
        initBundle.putInt("keyId",skeyId);
        initBundle.putInt("keyId2",skeyId);

        int spinBlockType = Integer.valueOf(pinBlockType);
        initBundle.putInt("pinBlockType",spinBlockType);

        int skeySystem = Integer.valueOf(keySystem);
        initBundle.putInt("keySystem",skeySystem);

        int smode = Integer.valueOf(mode);
        initBundle.putInt("mode",smode);

        if (extraParams != null && !extraParams.equals("")){
            Log.d(TAG,"extraParams != null");
            String[] parts2 = extraParams.split(",");
            String[] subparts2 = null;
            String[] values2 = null;
            for (int i = 0; i < parts2.length; i++) {
                parts2[i] = parts2[i].trim();
                subparts2 = parts2[i].split("_");
                for (int j = 0; j < subparts2.length; j++) {
                    subparts2[j] = subparts2[j].trim();
                    values2 = subparts2[j].split("=");
                    if (values2.length == 2){
                        String name = values2[0].trim();
                        String value = values2[1].trim();
                        switch (name){
                            case "dispersionType":
                                initBundle.putByte("dispersionType",Byte.parseByte(value));
                                break;
                            case "notifyPinLenError":
                                initBundle.putBoolean("notifyPinLenError",Boolean.parseBoolean(value));
                                break;
                            case "random":
                                initBundle.putByteArray("random",StringtoCharArray(value));
                                break;
                        }
                    }
                }
            }
        } else {
            Log.d(TAG,"extraParams == null");
        }

        Intent intent = new Intent(context, PinInputActivity.class);
        //启动新的activity时，增加计数器
        synchronized (keyLock){
            activityCount++;
        }
        intent.putExtra("initBundle",initBundle);
        context.startActivity(intent);
    }


    public byte[] StringtoCharArray(String myString){
        char[] chars = myString.toCharArray();  //将字符串转换为字符数组
        byte[] bytes = new byte[chars.length];  //创建一个字节数组来存储结果
        for (int i = 0; i < chars.length; i++) {
            if (chars[i]>='A' && chars[i] <='E'){
                bytes[i] = (byte) (chars[i]-'A'+10);
            }else {
                bytes[i] = (byte) (chars[i] - '0');  //将字符转换为相应的数值类型值
            }
        }
        Log.d(TAG,"bytes="+ Arrays.toString(bytes));
        return bytes;
    }
    public boolean T_loadDukptKey(String skeyId, String sksn, String skey, String scheckValue, String sBundle) {

        boolean ret = false;
        try {

            BundleConfig[] bundleConfigs = new BundleConfig[ ]{
                    new BundleConfig("isPlainKey", BundleConfig_boolean),
                    new BundleConfig("TEKIndex", BundleConfig_int),
                    new BundleConfig("KSNAutoIncrease", BundleConfig_boolean),
                    new BundleConfig("keyType",BundleConfig_int),
                    new BundleConfig("AESinitialKeyType",BundleConfig_byte)
            };
            Bundle extend = convert( sBundle, bundleConfigs );
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

    protected CardDetectionListener cardDetectionListener = new CardDetectionListener.Stub() {
        String msg;
        @Override
        public void onCardSwiped(Bundle track) throws RemoteException {
            msg = "Card detection: credit card swiped successfully";
            Log.d(TAG,"测试onCardSwiped");
//            msg = "PAN:" + track.getString("PAN") + "\nTRACK1:" + track.getString("TRACK1") + "\nTRACK2:" + track.getString("TRACK2") + "\nTRACK3:" + track.getString("TRACK3") + "\nSERVICE_CODE:" + track.getString("SERVICE_CODE") + "\nEXPIRED_DATE:" + track.getString("EXPIRED_DATE");
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            iemv.stopCheckCard();
            synchronized (sdeLock ){
                cardDetectionResult = 1;
                sdeLock.notify();
            }

        }

        @Override
        public void onCardPowerUp() throws RemoteException {
            Log.i(TAG, "onCardPowerUp ... ");
            Log.d(TAG,"测试onCardPowerUp");

            msg = "Card test: IC card inserted successfully";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            iemv.stopCheckCard();
            synchronized (sdeLock ){
                cardDetectionResult = 2;
                sdeLock.notify();
            }

        }

        @Override
        public void onCardActivate() throws RemoteException {
            msg = "CCard test: successfully waved the card";
            Log.d(TAG,"测试onCardActivate");
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            iemv.stopCheckCard();
            synchronized (sdeLock ){
                cardDetectionResult = 3;
                sdeLock.notify();
            }

        }

        @Override
        public void onTimeout() throws RemoteException {
            logUtils.addCaseLog("Card detection: timeout");
            Log.d(TAG,"测试onTimeout");

            Message message1 = new Message();
            message1.getData().putString("message", msg);
            handler.sendMessage(message1);

            iemv.stopCheckCard();
            synchronized (sdeLock ){
                cardDetectionResult = 4;
                sdeLock.notify();
            }
        }

        @Override
        public void onError(int error, String message) throws RemoteException {
            msg = "Card detection: Error:" + error + ", " + message;
            Log.d(TAG,"测试onError");

            Log.e(TAG, msg );

            iemv.stopCheckCard();

            Message message1 = new Message();
            if (error == 3)
                message1.getData().putString("message", msg + "Fallback deal");
            else message1.getData().putString("message", msg);
            handler.sendMessage(message1);

            synchronized (sdeLock ){
                cardDetectionResult = 0-error;
                sdeLock.notify();
            }

        }
    };


    protected String getEMVDataAndCheck( String list, String type ) {
        // getEMVData=PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY=0
        List<String[]> keyValues = convert( list, ".", "-" );

        String failure = "";
        String msg;
        LogUtil.d(TAG, "Type: " + type );
        for ( String[] kv: keyValues  ) {
            try {
                if( null == kv ){
                    Log.w(TAG, "no data was read from list");
                    printMsgToolAppend("no data was read from list", Log.INFO );
                    continue;
                }
                if( kv.length == 0 ){
                    Log.w(TAG, "empty data was read from list");
                    printMsgToolAppend("empty data was read from list", Log.INFO );
                    continue;
                }
                if( null == kv[0]  ){
                    Log.w(TAG, "no key was read from list");
                    printMsgToolAppend("no key was read from list", Log.INFO );
                }
                if( kv[0].length() == 0  ){
                    Log.w(TAG, "empty key was read from list");
                    printMsgToolAppend("empty key was read from list", Log.INFO );
                }
                String value;
                if( 0 == type.compareTo( "getEMVData") ){
                    value = iemv.getEMVData( kv[0] );
                } else if( 0 == type.compareTo( "getCardData") ){
                    value = StringUtil.byte2HexStr(iemv.getCardData( kv[0] ) );
                } else {
                    value = "";
                }
                if ( null == value ) {
                    msg = String.format("Got[%s][null]", kv[0]  );
                    Log.w(TAG, msg );
                    printMsgToolAppend(msg, Log.INFO );

                    if( kv.length > 1 ){

                    }
                } else if( kv.length > 1 ){
                    if( 0 == kv[1].compareToIgnoreCase( value)) {
                        msg = String.format("Success check[%s][%s]", kv[0], kv[1]  );
                        Log.i(TAG, msg );
                        printMsgToolAppend(msg, Log.DEBUG );
                    } else {
                        msg = String.format("Failure to check[%s][%s], got[%s] ", kv[0], kv[1] ,value );
                        failure += msg;
                        Log.e(TAG, msg );
                        printMsgToolAppend(msg, Log.ERROR );
                    }
                }else if ( value.length() > 0) {
                    msg = String.format("Got[%s][%s], skip to check", kv[0], value  );
                    LogUtil.d(TAG, msg );
                    printMsgToolAppend(msg, Log.DEBUG );
                } else {
                    msg = String.format("Cannot Get[%s][%s]", kv[0], value  );
                    Log.w(TAG, msg );
                    printMsgToolAppend(msg, Log.INFO );
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return failure;
        // getCardData=9f51.9F79.DF71.DF79.8E=0

        // setEMVData=9f51-xxx.9F79-xxxx.DF71.DF79.8E=0

    }

    String[] convert2StringArray(String list, char regex) {
        String regexItem = String.valueOf(regex);
        LogUtil.d(TAG, "Convert: " + list + " , " + regexItem);
        List<String> keyValueLs = new ArrayList<String>();
        if (regexItem.equals(".")) {
            regexItem = "\\.";
        }
        String[] keyValues = list.split(regexItem);
        for (String keyValue : keyValues
        ) {
            keyValue = keyValue.trim();
            if (keyValue.length() > 0) {
                keyValueLs.add(keyValue);
            }
        }
        LogUtil.d(TAG, "add items: " + keyValueLs.size());
        return keyValues;
    }



    String[][] emvCodeMessage = new String[][]{
            {"0" , "AARESULT_TC(0) - TC on action analysis"},
            {"8" , "EMV_NO_APP(8) - emv no application(aid param)"},
            {"9" , "EMV_COMPLETE(9) - emv complete"},
            {"11" , "EMV_OTHER_ERROR(11) - emv other error,transaction abort"},
            {"12" , "EMV_FALLBACK(12) - FALLBACK"},
            {"13" , "EMV_DATA_AUTH_FAIL(13) - data auth fail"},
            {"14" , "EMV_APP_BLOCKED(14) - app has been blocked"},
            {"15" , "EMV_NOT_ECCARD(15) - not EC"},
            {"16" , "EMV_UNSUPPORT_ECCARD(16) - unsupport EC"},
            {"17" , "EMV_AMOUNT_EXCEED_ON_PURELYEC(17) - amount exceed EC"},

            { "18", "EMV_SET_PARAM_ERROR(18) - set parameter fail on 9F7A" },
            { "19", "EMV_PAN_NOT_MATCH_TRACK2(19) - pan not match track2" },
            { "20", "EMV_CARD_HOLDER_VALIDATE_ERROR(20) - card holder validate error" },
            { "21", "EMV_PURELYEC_REJECT(21) - purely EC transaction reject" },
            { "22", "EMV_BALANCE_INSUFFICIENT(22) - balance insufficient" },
            { "23", "EMV_AMOUNT_EXCEED_ON_RFLIMIT_CHECK(23) - amount exceed the CTLS limit" },
            { "24", "EMV_CARD_BIN_CHECK_FAIL(24) - check card failed" },
            { "25", "EMV_CARD_BLOCKED(25) - card has been block" },
            { "26", "EMV_MULTI_CARD_ERROR(26) - multiple card conflict" },
            { "27", "EMV_INITERR_GPOCMD(27) - GPO Processing Options response error" },
            { "28", "EMV_GACERR_GACCMD(28) - GAC response error" },
            { "29", "EMV_TRY_AGAIN(29) - Try again" },
            { "30", "EMV_ODA_FAILED(30) - ODA failed" },
            { "31", "EMV_CVM_FAILED(31) - CVM response error" },

            { "60", "EMV_RFCARD_PASS_FAIL(60) - tap card failure" },
//            { "", "AARESULT_TC(0) - TC on action analysis" },
            { "1", "AARESULT_AAC(1) - refuse on action analysis" },

            { "202", "CTLS_AAC(202) - refuse on CTLS" },
            { "203", "CTLS_ERROR(203) - error on CTLS" },
            { "204", "CTLS_TC(204) - approval on CTLS" },
            { "205", "CTLS_CONT(205) - need contact" },
            { "206", "CTLS_NO_APP(206) - result of CTLS, no application (UP Card maybe available)" },
            { "207", "CTLS_NOT_CPU_CARD(207) - not a cpu card" },
            { "208", "CTLS_ABORT(208) - Transation abort" },
            { "209", "CTLS_ISSUERUPDATE_APPROVE(209) - Second tap, issuer update approve" },
            { "210", "CTLS_CARD_BLOCK(210) -  6A81 error card block" },
            { "211", "CTLS_SEL_FILE_INVALID(211) -  6283 error Selected file invalidated" },

            { "150", "EMV_SEE_PHONE(150) - paypass result, please check the result on phone" },
            { "301", "QPBOC_KERNAL_INIT_FAILED(301) - CTLS kernel init failed" }
    };
    Map<java.lang.Integer, String> mapCodeMessage = null;


    String getEMVMsgByCode( int result ){
        if( null ==  mapCodeMessage) {
            mapCodeMessage= new HashMap<>();
            for (int i = 0; i < emvCodeMessage.length ; i++) {
                mapCodeMessage.put(  Integer.valueOf(emvCodeMessage[i][0]) , emvCodeMessage[i][1]);
            }
        }
        return mapCodeMessage.get( result );
    }


    String[][] emvOnlineMessage = new String[][]{
            {"0", "ONLINE_RESULT_TC(0) - 联机成功 | online result TC (success) "},
            {"1", "ONLINE_RESULT_AAC(1) - 联机拒绝 | online result AAC (refuse) "},
            {"101", "ONLINE_RESULT_OFFLINE_TC(101) - 联机失败，脱机成功 | online false, offline success"},
            {"102", "ONLINE_RESULT_SCRIPT_NOT_EXECUTE(102) - 脚本未执行 | the script not execute"},
            {"103", "ONLINE_RESULT_SCRIPT_EXECUTE_FAIL(103) - 脚本执行失败 | failure while execute script"},
            {"104", "ONLINE_RESULT_NO_SCRIPT(104) - 联机失败，未下送脚本 | online failure, not send the script"},
            {"105", "ONLINE_RESULT_TOO_MANY_SCRIPT(105) - 联机失败，脚本超过1个 | online failure, more than one script"},
            {"106", "ONLINE_RESULT_TERMINATE(106) - 联机失败，交易终止(GAC返回非9000，要提示交易终止,0x8F) | online failure, transaction terminate. return transaction terminate while GAC is not 9000, 0x8F "},
            {"107", "ONLINE_RESULT_ERROR(107) - 联机失败，EMV内核错误 | online failure, error in EMV"},
            {"110", "ONLINE_RESULT_OTHER_ERROR(110) - 其他错误 | other error"}
    };
    Map<java.lang.Integer, String> mapOnlineMessage = null;

    String getOnlineResult( int result ){
        if( null ==  mapOnlineMessage) {
            mapOnlineMessage = new HashMap<>();
            for (int i = 0; i < emvOnlineMessage.length ; i++) {
                mapOnlineMessage.put( Integer.valueOf(emvOnlineMessage[i][0]), emvOnlineMessage[i][1]);
            }
        }
        return mapOnlineMessage.get( result );
    }

    protected boolean T_setPaddingAlignType(String alignType) throws RemoteException{
        /**
             A=1__B=2__C=3，itemSeparator 为 "__"，keyValueSeparator 为 "="
             或A=1__
             Key:   Value:
             "A"    1
             "B"    2
             "C"    3
         */
        Map<String, Byte> alignTypeMap = convertStr2Map(alignType,"__","=");
        boolean ret = iSde.setPaddingAlignType(alignTypeMap);
        Log.d(TAG,"ret = "+ret);
        return ret;
    }

    public Map<String, Byte> convertStr2Map(String str, String itemSeparator, String keyValueSeparator) {
        Map<String, Byte> map = new HashMap<>();
        if (TextUtils.isEmpty(str) || !str.contains(itemSeparator) || !str.contains(keyValueSeparator))
            return map;
        String[] items = str.split(itemSeparator);
        for (String item : items) {
            String[] keyValue = item.split(keyValueSeparator);
            if (keyValue.length < 2) {
                continue;
            } else {
                map.put(keyValue[0], Byte.valueOf(keyValue[1]));
            }
        }
        return map;
    }


}
