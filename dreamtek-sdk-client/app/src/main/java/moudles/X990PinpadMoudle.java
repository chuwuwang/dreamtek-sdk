package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.CheckCardListener;
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;
import com.dreamtek.smartpos.deviceservice.aidl.RFSearchListener;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;

public class X990PinpadMoudle {
/*
    Context context;
    static LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    IX990Pinpad ix990Pinpad;
    IPinpad iPinpad;
    IRFCardReader irfCardReader;
    ISerialPort iSerialport;
    IRSA irsa;
    IEMV iemv;
    private final String TAG = "X990PinPadManager";
    ArrayList<String> startX990PinInput = new ArrayList<String>();
    ArrayList<String> endInputPin = new ArrayList<String>();
    ArrayList<String> rfSearchCard = new ArrayList<String>();
    ArrayList<String> openSerial = new ArrayList<>();
    ArrayList<String> initSerial = new ArrayList<>();
    ArrayList<String> writeSerial = new ArrayList<>();
    ArrayList<String> readSerial = new ArrayList<>();
    ArrayList<String> emvCheckCard = new ArrayList<>();
    ArrayList<String> isUsbSerialConnect = new ArrayList<>();
    ArrayList<String> usbSerialRead = new ArrayList<>();
    ArrayList<String> usbSerialWrite = new ArrayList<>();


    public X990PinpadMoudle(Context context, IX990Pinpad ix990Pinpad, IPinpad iPinpad, IRFCardReader irfCardReader,ISerialPort iSerialport, IEMV iemv, IRSA irsa) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.ix990Pinpad = ix990Pinpad;
        this.iPinpad = iPinpad;
        this.irfCardReader = irfCardReader;
        this.iSerialport = iSerialport;
        this.iemv = iemv;
        this.irsa = irsa;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.X990PinpadMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "P01":
                            startX990PinInput.add(i.getName());
                            break;
                        case "P02":
                            endInputPin.add(i.getName());
                            break;
                        case "P03":
                            rfSearchCard.add(i.getName());
                            break;
                        case "P04":
                            openSerial.add(i.getName());
                            break;
                        case "P05":
                            initSerial.add(i.getName());
                            break;
                        case "P06":
                            writeSerial.add(i.getName());
                            break;
                        case "P07":
                            readSerial.add(i.getName());
                            break;
                        case "P08":
                            emvCheckCard.add(i.getName());
                            break;
                        case "P09":
                            isUsbSerialConnect.add(i.getName());
                            break;
                        case "P10":
                            usbSerialWrite.add(i.getName());
                            break;
                        case "P11":
                            usbSerialRead.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(startX990PinInput);
            caseNames.add(endInputPin);
            caseNames.add(rfSearchCard);
            caseNames.add(openSerial);
            caseNames.add(initSerial);
            caseNames.add(writeSerial);
            caseNames.add(readSerial);
            caseNames.add(emvCheckCard);
            caseNames.add(isUsbSerialConnect);
            caseNames.add(usbSerialWrite);
            caseNames.add(usbSerialRead);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void My01startX990PinInput(Bundle param){
        try {
            ix990Pinpad.startKeyboardInput(param, new PinInputListener.Stub() {
                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.i(TAG, "len = " + len + ";key = " + key);
                    Message msg = new Message();
                    msg.getData().putString("msg", "input length entered len = " + len + " The current key value = " + key);
                    handler.sendMessage(msg);
                }

                @Override
                public void onConfirm(byte[] data, boolean isNonePin) throws RemoteException {
                    Log.i(TAG, "确认");
                    Log.i(TAG, "input Data:" + StringUtil.byte2HexStr(data));

                    Message msg = new Message();
                    msg.getData().putString("msg", "input data:" + StringUtil.byte2HexStr(data));
                    handler.sendMessage(msg);
                    */
/*
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
                    boolean isNonePin = pinInfos.getBoolean("isEncrypt");
                    Log.i(TAG, "isNonePin:" + isNonePin);
                    Message msg = new Message();
                    Log.i(TAG, "PIN: " + StringUtil.byte2HexStr(data));
//                        logUtils.addCaseLog("PIN: " + StringUtil.byte2HexStr(data));
                    msg.getData().putString("msg", "isNonePin:" + isNonePin + "\nPIN: " + StringUtil.byte2HexStr
                            (data));
                    handler.sendMessage(msg);
                    *//*

                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.i(TAG, "onCancel Cancel PIN Input");
                    Message msg = new Message();
                    msg.getData().putString("msg", "onCancel Cancel PIN Input");
                    handler.sendMessage(msg);
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    Log.i(TAG, "onError:" + errorCode);
                    logUtils.addCaseLog("onError:"+errorCode);
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
        }
    }

    public void My02endInputPin(){

    }

    public void My03rfSearchCard() {

    }

    public boolean My04openSerial(){
        try {
            logUtils.addCaseLog("open()执行");
            return iSerialport.open();
        } catch (RemoteException e) {
            logUtils.addCaseLog("open()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    public boolean My05initSerial(int bps, int par, int dbs){
        try {
            logUtils.addCaseLog("init()执行");
            logUtils.addCaseLog("bps=" + bps + ", par=" + par + ", dbs=" + dbs);
            return iSerialport.init(bps, par, dbs);
        } catch (RemoteException e) {
            logUtils.addCaseLog("init()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    public int My06writeSerial(byte[] data, int timeout){
        try {
            logUtils.addCaseLog("write() execute");
            return iSerialport.write(data, timeout);
        } catch (RemoteException e) {
            logUtils.addCaseLog("write() exception");
            e.printStackTrace();
            return 0;
        }
    }

    public int My07readSerial(byte[] buffer, int expectLen, int timeout){
        try {
            logUtils.addCaseLog("read()执行");
            return iSerialport.read(buffer, expectLen, timeout);
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read()执行异常-");
            npe.printStackTrace();
            return -1;
        } catch (RemoteException e) {
            logUtils.addCaseLog("read()执行异常");
            e.printStackTrace();
            return -1;
        }
    }

    public void My08emvCheckCard(Bundle cardOption, int timeout, CheckCardListener listener){
        try {
            iemv.checkCard(cardOption, timeout, listener);
            logUtils.addCaseLog("checkCard execute");
        } catch (RemoteException e) {
            logUtils.addCaseLog("checkCard execute exception");
            e.printStackTrace();
        }
    }


    public void P01001() {
        //测试X990Pinput键盘, 输入pan
        logUtils.addCaseLog("startX990PinInput executed()");
        Bundle param = new Bundle();
        param.putInt("timeout", 30);
        My01startX990PinInput(param);
    }

    public void P01002() {
        //测试X990Pinput键盘, 限制输入pin的长度
        logUtils.addCaseLog("startX990PinInput executed(),pinLimit = {0,4,5,6,25,26}");
        Bundle param = new Bundle();
        param.putInt("timeout", 30);
        param.putByteArray("pinLimit",new byte[]{0,4,5,6,25,26});
        My01startX990PinInput(param);
    }

    public void P01003() {
        //测试X990Pinput键盘, 限制输入pin的长度
        logUtils.addCaseLog("startX990PinInput executed(),pinLimit = {}");
        Bundle param = new Bundle();
        param.putInt("timeout", 30);
        param.putByteArray("pinLimit",new byte[]{});
        My01startX990PinInput(param);
    }

    public void P01004() {
        //测试X990Pinput键盘, 限制输入pin的长度
        logUtils.addCaseLog("startX990PinInput executed(),pinLimit = null");
        Bundle param = new Bundle();
        param.putInt("timeout", 30);
        param.putByteArray("pinLimit", null);
        My01startX990PinInput(param);
    }

    public void P01005() {
        //测试X990Pinput键盘, 限制输入pin的长度
        logUtils.addCaseLog("startX990PinInput executed(),pinLimit = {-1,0,26}");
        Bundle param = new Bundle();
        param.putInt("timeout", 30);
        param.putByteArray("pinLimit",new byte[]{-1,0,26});
        My01startX990PinInput(param);
    }

    public void P01006() {
        //测试X990Pinput键盘, 限制输入pin的长度
        logUtils.addCaseLog("startX990PinInput executed(),pinLimit = {-1,0,4,25}");
        Bundle param = new Bundle();
        param.putInt("timeout", 30);
        param.putByteArray("pinLimit",new byte[]{-1,0,4,25});
        My01startX990PinInput(param);
    }

    public void P02001() {
        //测试关闭键盘
        try {
            logUtils.addCaseLog( "stopPinInput() executed");
            boolean ret = ix990Pinpad.endInputPin();
            logUtils.addCaseLog("ret = "+ret);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void P03001() {
        //直接RF search card
        logUtils.addCaseLog("rfsearch card executed");
//        My03rfSearchCard(new rfSearchListener(), 60);
        new IrfCardReaderMoudle(context,irfCardReader,irsa).J01004();
    }

    public void P03002(){
        //开启x990 pinpad键盘的同时，进行 rf search card
        P01001();
        P03001();
    }

    public void P04001(){
        boolean blRet = My04openSerial();
        logUtils.addCaseLog("open执行返回值:[" + blRet + "]");
    }

    public void P05001(){
        boolean blRet;
        My04openSerial();

        blRet = My05initSerial(115200, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void P06001(){
        //write data1
        byte[] writeData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        logUtils.addCaseLog("执行L41001");
        int i = My06writeSerial(writeData, 5000);
        Log.d(TAG, "L41001 i = " + i);
    }

    public void P06002(){
        byte[] writeData = new byte[]{0x0A, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C};
        logUtils.addCaseLog("执行L41002");
        int i = My06writeSerial(writeData, 5000);
        Log.d(TAG, "L41002 i = " + i);
    }

    public void P07001(){
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[27];

        readBytes = My07readSerial(readData, 27, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog( "执行read()返回值:[" + readBytes + "]");
        int i = My06writeSerial(readData, 5000);
        logUtils.addCaseLog("发送i = " + i);
    }

    public void P08001(){
        //测试emv寻卡
        Bundle cardOption = new Bundle();
        cardOption.putBoolean("supportMagCard", true);
        cardOption.putBoolean("supportSmartCard", true);
        cardOption.putBoolean("supportCTLSCard", true);
        My08emvCheckCard(cardOption, 30, new MyListener());
    }

    public void P08002(){
        //测试emv寻卡
        Bundle cardOption = new Bundle();
        cardOption.putBoolean("supportMagCard", true);
        cardOption.putBoolean("supportSmartCard", true);
        cardOption.putBoolean("supportCTLSCard", true);
        try {
            iemv.checkCard(cardOption, 30, new MyListener());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class rfSearchListener extends RFSearchListener.Stub {
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
                message.getData().putString("message", "Result: CPU_CARD_A");
            if (cardType == 0x06)
                message.getData().putString("message","Result:CPU_CARD_B");
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

    private class MyListener extends CheckCardListener.Stub {
        String msg;
        long startTime = System.currentTimeMillis();
        boolean needSecondTapScript = false;

        public MyListener() {
        }

        public MyListener(boolean needSecondTapScript) {
            this.needSecondTapScript = needSecondTapScript;
        }

        @Override
        public void onCardActivate() throws RemoteException {
            msg = "Card test: successfully waved the card";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("onCardActivate execute Time" + (endTime - startTime));
//            My28turnOff(1);
            if (needSecondTapScript) {
                iemv.setIssuerUpdateScript();
            }
        }

        @Override
        public void onCardSwiped(Bundle track) throws RemoteException {
            msg = "PAN:" + track.getString("PAN") + "\nTRACK1:" + track.getString("TRACK1") + "\nTRACK2:" + track.getString("TRACK2") + "\nTRACK3:" + track.getString("TRACK3") + "\nSERVICE_CODE:" + track.getString("SERVICE_CODE") + "\nEXPIRED_DATE:" + track.getString("EXPIRED_DATE");
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("onCardSwiped execute Time" + (endTime - startTime));
//            My28turnOff(1);
        }

        @Override
        public void onCardPowerUp() throws RemoteException {
//            ((MyApplication) context).serviceMoudle.getBeerMoudle().B01006();
            msg = "Card test: IC card inserted successfully";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("onCardPowerUp execute Time" + (endTime - startTime));
        }

        @Override
        public void onTimeout() throws RemoteException {
            msg = "Card detection: timeout";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("onTimeout execute Time" + (endTime - startTime));
        }

        @Override
        public void onError(int error, String message) throws RemoteException {
            msg = "Check card error code = " + error + " " + message;
            Message message1 = new Message();
            message1.getData().putString("message", msg);
            handler.sendMessage(message1);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("onError execute Time" + (endTime - startTime));
        }
    }



    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.X990PinpadMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "execute 完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
            logUtils.showCaseLog();
        }
    };



    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    private void printMsgTool(String msg) {
        if (null == msg || "null".equals(msg) || msg.contains("null")) {
            msg = "The execution result：";
        }
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}
