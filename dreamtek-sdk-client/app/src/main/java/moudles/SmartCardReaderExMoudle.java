package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;

public class SmartCardReaderExMoudle {
/*
    Context context;
    ISmartCardReaderEx iSmartCardReaderEx;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> search = new ArrayList<String>();
    ArrayList<String> stopSearch = new ArrayList<String>();
    ArrayList<String> powerUp = new ArrayList<String>();
    ArrayList<String> powerDown = new ArrayList<String>();
    ArrayList<String> certificationLoad = new ArrayList<String>();
    ArrayList<String> certificationRemove = new ArrayList<String>();
    ArrayList<String> read = new ArrayList<String>();
    ArrayList<String> write = new ArrayList<String>();
    ArrayList<String> communicate = new ArrayList<String>();
    ArrayList<String> checkLogicCardStatus = new ArrayList<>();
    ArrayList<String> setLogicalCardType = new ArrayList<>();

    public static final byte CARD_AT24C01 = 0x01;
    public static final byte CARD_AT24C02 = 0x02;
    public static final byte CARD_AT24C04 = 0x03;
    public static final byte CARD_AT24C08 = 0x04;
    public static final byte CARD_AT24C16 = 0x05;
    public static final byte CARD_AT24C32 = 0x06;
    public static final byte CARD_AT24C64 = 0x07;
    public static final byte CARD_AT24C128 = 0x08;
    public static final byte CARD_AT24C256 = 0x09;
    public static final byte CARD_AT24C512 = 0x0A;
    public static final byte CARD_AT88SC102 = 0x0B;
    public static final byte CARD_AT88SC1604 = 0x0C;
    public static final byte CARD_AT88SC1608 = 0x0D;
    public static final byte CARD_SLE44X2 = 0x0E;
    public static final byte CARD_SLE44X8 = 0x0F;
    public static final byte AUTO_SET = (byte) 0xFF;


    public SmartCardReaderExMoudle(Context context, ISmartCardReaderEx iSmartCardReaderEx) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iSmartCardReaderEx = iSmartCardReaderEx;
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
            Class aClass = Class.forName("moudles.SmartCardReaderExMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 4)) {
                        case "Ex01":
                            search.add(i.getName());
                            break;
                        case "Ex02":
                            stopSearch.add(i.getName());
                            break;
                        case "Ex03":
                            powerUp.add(i.getName());
                            break;
                        case "Ex04":
                            powerDown.add(i.getName());
                            break;
                        case "Ex05":
                            certificationLoad.add(i.getName());
                            break;
                        case "Ex06":
                            certificationRemove.add(i.getName());
                            break;
                        case "Ex07":
                            read.add(i.getName());
                            break;
                        case "Ex08":
                            write.add(i.getName());
                            break;
                        case "Ex09":
                            communicate.add(i.getName());
                            break;
                        case "Ex10":
                            checkLogicCardStatus.add(i.getName());
                            break;
                        case "Ex11":
                            setLogicalCardType.add(i.getName());
                            break;


                    }
                }
            }
            caseNames.add(search);
            caseNames.add(stopSearch);
            caseNames.add(powerUp);
            caseNames.add(powerDown);
            caseNames.add(certificationLoad);
            caseNames.add(certificationRemove);
            caseNames.add(read);
            caseNames.add(write);
            caseNames.add(communicate);
            caseNames.add(checkLogicCardStatus);
            caseNames.add(setLogicalCardType);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void My01search(SmartCardExSearchListener listener, int timeout) {
        try {
            iSmartCardReaderEx.search(listener, timeout);

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }


    public void My02stopSearch() {
        try {
            iSmartCardReaderEx.stopSearch();

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    public boolean My03powerUp() {
        boolean blRet = false;
        try {
            blRet = iSmartCardReaderEx.powerUp();
            logUtils.addCaseLog("powerUp() return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            logUtils.addCaseLog("powerUp is not executing properly");
            e.printStackTrace();
            return false;

        }
    }

    public boolean My04powerDown() {
        boolean blRet = false;
        try {
            blRet = iSmartCardReaderEx.powerDown();
            logUtils.addCaseLog("powerDown() return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            logUtils.addCaseLog("powerDown is not executing properly");
            e.printStackTrace();
            return false;

        }
    }

    public boolean My05certificationLoad(byte areaType, byte[] credentials) {
        boolean blRet = false;
        try {
            blRet = iSmartCardReaderEx.certificationLoad(areaType, credentials);
            logUtils.addCaseLog("certificationLoad return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            logUtils.addCaseLog("certificationLoad is not executing properly");
            e.printStackTrace();
            return false;

        }
    }

    public boolean My06certificationRemove(byte areaType, byte[] credentials) {
        boolean blRet = false;
        try {
            blRet = iSmartCardReaderEx.certificationRemove(areaType, credentials);
            logUtils.addCaseLog("certificationRemove return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            logUtils.addCaseLog("certificationRemove is not executing properly");
            e.printStackTrace();
            return false;

        }
    }

    public byte[] My07read(byte areaType, int offset, int length) {
        try {
            return iSmartCardReaderEx.read(areaType, offset, length);

        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean My08write(byte areaType, int offset, byte[] data) {
        boolean blRet = false;
        try {
            blRet = iSmartCardReaderEx.write(areaType, offset, data);
            logUtils.addCaseLog("write return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            logUtils.addCaseLog("write is not executing properly");
            e.printStackTrace();
            return false;

        }
    }

    public Bundle My09communicate(byte commType, byte areaType, int offset, int length, byte[] data) {
        try {
            Bundle result = iSmartCardReaderEx.communicate(commType, areaType, offset, length, data);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void My10checkLogicCardStatus(){
        try {
            byte result = iSmartCardReaderEx.checkLogicCardStatus();
            logUtils.addCaseLog("check result = "+result);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My11setLogicalCardType(byte cardType){
        logUtils.addCaseLog("cardType = "+cardType);
        try {
            boolean result = iSmartCardReaderEx.setLogicalCardType(cardType);
            logUtils.addCaseLog("set result = "+result);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void Ex01001() {
        My01search(null, 60);
    }

    public void Ex01002() {
        My01search(new mylistener(), 60);
    }
    public void Ex01003() {
        My01search(new mylistener(), 60);
    }
    public void Ex01004() {
        My01search(new mylistener(), 60);
    }
    public void Ex01005() {
        My01search(new mylistener(), 60);
    }
    public void Ex01006() {
        My01search(new mylistener(), 60);
    }
    public void Ex01007() {
        My01search(new mylistener(), 60);
    }
    public void Ex01008() {
        My01search(new mylistener(), 60);
    }
    public void Ex01009() {
        My01search(new mylistener(), 60);
    }

    public void Ex01010() {

        My01search(new mylistener(), 0);
    }
    public void Ex01011() {

        My01search(new mylistener(), -1);
    }
    public void Ex01012() {

        My01search(new mylistener(), 10);
    }
    public void Ex01013() {

        My01search(new mylistener(), 60);
    }



    public void Ex02001() {
        My02stopSearch();
    }
    public void Ex02002() {
        Ex01002();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                My02stopSearch();
            }
        }.start();

    }
    public void Ex02003() {
        Ex01002();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                My02stopSearch();
            }
        }.start();

    }

    public void Ex03001() {

        My03powerUp();
    }

    public void Ex03002() {

        My03powerUp();
    }

    public void Ex03003() {

        My03powerUp();
    }

    public void Ex04001() {

        My04powerDown();
    }

    public void Ex04002() {

        My04powerDown();
    }

    public void Ex04003() {

        My04powerDown();
    }



    public void Ex07001() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07002() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07003() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07004() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07005() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07006() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07007() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07008() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,0,256));
    }

    public void Ex07009() {

        String retValue = StringUtil.byte2HexStr(My07read((byte)1,256,256));
    }





    public void Ex08001() {
     
       byte [] data =BCDDecode.str2Bcd("8765432187654321");
        My08write((byte)1,0,data);
    }

    public void Ex10001(){
        My10checkLogicCardStatus();
    }

    public void Ex11001(){
        My11setLogicalCardType(CARD_AT24C08);//0x04
    }

    public void Ex11002(){
        My11setLogicalCardType(CARD_AT24C16);//0x05
    }

    public void Ex11003(){
        My11setLogicalCardType(CARD_AT24C64);//0x07
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("message"));
            logUtils.showCaseLog();
        }
    };

    private class mylistener extends SmartCardExSearchListener.Stub {
        @Override
        public void onSuccess(int cardType) throws RemoteException {
            Message message = new Message();
            if (cardType == 1)
                message.getData().putString("message", "Result: AT24");
            if (cardType == 2)
                message.getData().putString("message", "Result: SLE44X2");
            if (cardType == 3)
                message.getData().putString("message", "Result: SLE44X8");
            if (cardType == 4)
                message.getData().putString("message", "Result: AT88SC102");
            if (cardType == 5)
                message.getData().putString("message", "Result: AT88SC1604");
            if (cardType == 6)
                message.getData().putString("message", "Result: AT88SC1608");

            handler.sendMessage(message);
        }

        @Override
        public void onFail(int S) throws RemoteException {
            Message msg = new Message();
            if (S == -1)
                msg.getData().putString("message", "error code = " + S + "\nCommunication error");

            handler.sendMessage(msg);
        }

        public void onTimeout() throws RemoteException {
            Message msg = new Message();
            logUtils.addCaseLog("timeout execute 完毕");
            Log.d("TAG", "result: timeout");
            msg.getData().putString("msg", "readcard the timeout");
            handler.sendMessage(msg);
        }
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.SmartCardReaderExMoudle");
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
*/
}
  /*  public UltraLightCardCMoudle(Context context, IUltraLightCardC iUltraLightCardC) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iUltraLightCardC = iUltraLightCardC;
        addAllapi();
    }*/

