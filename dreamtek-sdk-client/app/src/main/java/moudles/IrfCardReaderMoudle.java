package moudles;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.laikey.jatools.Utils;
import com.verifone.smartpos.utils.BCDDecode;
import com.verifone.smartpos.utils.BytesUtil;
import com.verifone.smartpos.utils.HexUtil;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.RFSearchListener;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import Utils.LogUtils;
import base.MyApplication;

//import com.verifone.smartpos.utils.BCDDecode;
//import com.socsi.utils.HexUtil;
//import com.socsi.utils.StringUtil;

//import static cn.verifone.atoolsjar.utils.Utils.bcd2Asc;
//import static cn.verifone.atoolsjar.utils.Utils.byte2HexStr;

public class IrfCardReaderMoudle {
    Context context;
    IRFCardReader irfCardReader;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> searchCard = new ArrayList<String>();
    ArrayList<String> stopSearch = new ArrayList<String>();
    ArrayList<String> halt = new ArrayList<String>();
    ArrayList<String> activate = new ArrayList<String>();
    ArrayList<String> isExist = new ArrayList<String>();
    ArrayList<String> cardReset = new ArrayList<String>();
    ArrayList<String> authBlock = new ArrayList<String>();
    ArrayList<String> authSector = new ArrayList<String>();
    ArrayList<String> readBlock = new ArrayList<String>();
    ArrayList<String> writeBlock = new ArrayList<String>();
    ArrayList<String> exchangeApdu = new ArrayList<String>();
    ArrayList<String> increaseValue = new ArrayList<String>();
    ArrayList<String> decreaseValue = new ArrayList<String>();
    ArrayList<String> getCardInfo = new ArrayList<String>();
    ArrayList<String> restore = new ArrayList<String>();
    ArrayList<String> transfer = new ArrayList<String>();
    ArrayList<String> CloseRfField = new ArrayList<String>();

    static IRSA irsa;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("message"));
            logUtils.showCaseLog();
        }
    };

    public IrfCardReaderMoudle(Context context, IRFCardReader irfCardReader, IRSA irsa) {
        this.context = context;
        logUtils = MyApplication.logUtils;
        this.irfCardReader = irfCardReader;
        this.irsa = irsa;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.IrfCardReaderMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "J01":
                            searchCard.add(i.getName());
                            break;
                        case "J02":
                            stopSearch.add(i.getName());
                            break;
                        case "J03":
                            halt.add(i.getName());
                            break;
                        case "J04":
                            activate.add(i.getName());
                            break;
                        case "J05":
                            isExist.add(i.getName());
                            break;
                        case "J06":
                            cardReset.add(i.getName());
                            break;
                        case "J07":
                            authBlock.add(i.getName());
                            break;
                        case "J08":
                            authSector.add(i.getName());
                            break;
                        case "J09":
                            readBlock.add(i.getName());
                            break;
                        case "J10":
                            writeBlock.add(i.getName());
                            break;
                        case "J11":
                            exchangeApdu.add(i.getName());
                            break;
                        case "J12":
                            increaseValue.add(i.getName());
                            break;
                        case "J13":
                            decreaseValue.add(i.getName());
                            break;
                        case "J14":
                            getCardInfo.add(i.getName());
                            break;
                        case "J15":
                            restore.add(i.getName());
                            break;
                        case "J16":
                            transfer.add(i.getName());
                            break;
                        case "J17":
                            CloseRfField.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(searchCard);
            caseNames.add(stopSearch);
            caseNames.add(halt);
            caseNames.add(activate);
            caseNames.add(isExist);
            caseNames.add(cardReset);
            caseNames.add(authBlock);
            caseNames.add(authSector);
            caseNames.add(readBlock);
            caseNames.add(writeBlock);
            caseNames.add(exchangeApdu);
            caseNames.add(increaseValue);
            caseNames.add(decreaseValue);
            caseNames.add(getCardInfo);
            caseNames.add(restore);
            caseNames.add(transfer);
            caseNames.add(CloseRfField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void My01searchCard(RFSearchListener listener, int timeout) {
        try {
            irfCardReader.searchCard(listener, timeout);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void My02stopSearch() {
        try {
            irfCardReader.stopSearch();
            My17CloseRfField();
            logUtils.addCaseLog("Successfully stopSearch");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void My03halt() {
        try {
            irfCardReader.halt();
            logUtils.addCaseLog("Successfully shut down the device");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("Failed to shut down device");
        }
    }

    public int My04activate(String driver, byte[] responseData) {
        try {
            int result = irfCardReader.activate(driver, responseData);
            logUtils.addCaseLog("activate ret = " + result);
//          /**/  String res = bcd2Asc(responseData);
            String res = Utils.byte2HexStr(responseData);
            logUtils.addCaseLog("The response data: " + res);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("ret = -1");
            return -1;

        }
    }

    public boolean My05isExist() {
        boolean blRet = false;
        try {
            blRet = irfCardReader.isExist();
            logUtils.addCaseLog("isExist() return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            logUtils.addCaseLog("My05iseXist is not executing properly");
            e.printStackTrace();
            return false;
        }
    }

    public byte[] My06cardReset() {
        try {
            return irfCardReader.cardReset();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int My07authBlock(int blockNo, int keyType, byte[] key) {
        try {
            int result = irfCardReader.authBlock(blockNo, keyType, key);
            logUtils.addCaseLog("authBlock() return code：" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int My08authSector(int sectorNo, int keyType, byte[] key) {
        try {
            int result = irfCardReader.authSector(sectorNo, keyType, key);
            logUtils.addCaseLog("authSector ret = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int My09readBlock(int blockNo, byte[] data) {
        try {
            int result = irfCardReader.readBlock(blockNo, data);
            logUtils.addCaseLog("readBlock ret = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int My10writeBlock(int blockNo, byte[] data) {
        try {
            int result = irfCardReader.writeBlock(blockNo, data);
            logUtils.addCaseLog("writeBlock ret = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] My11exchangeApdu(byte[] apdu) {
        try {
            byte[] ret = irfCardReader.exchangeApdu(apdu);
            logUtils.addCaseLog("APDU communication returns data=" + HexUtil.toString(ret));
            return ret;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int My12increaseValue(int blockNo, int value) {
        try {
            int result = irfCardReader.increaseValue(blockNo, value);
            logUtils.addCaseLog("increaseValue ret = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int My13decreaseValue(int blockNo, int value) {
        try {
            int result = irfCardReader.decreaseValue(blockNo, value);
            logUtils.addCaseLog("decreaseValue ret = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*public Bundle My14getCardInfo(RFSearchListener listener, int timeout) {
        try
        {
            listener.onCardPass(0x05);
            Bundle result = irfCardReader.getCardInfo();
            Object obj = result.get("cardInfo");
            byte[] bytes = result.getByteArray("cardInfo");
            logUtils.addCaseLog("getCardInfo ret = "+result);
            logUtils.addCaseLog("getCardInfo bytes = "+bytes);
            return result;
        } catch(RemoteException e)
        {
            e.printStackTrace();
            return null;
        }
    }*/
    public Bundle My14getCardInfo() {
        try {

            Bundle result = irfCardReader.getCardInfo();
//            byte[] bytes = result.getByteArray("cardInfo");
            logUtils.addCaseLog("getCardInfo = " + StringUtil.byte2HexStr(result.getByteArray("sn")));
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte My15restore(byte blockNo) {
        byte result = 0x00;
        try {

            result = irfCardReader.restore(blockNo);
//            byte[] bytes = result.getByteArray("cardInfo");
            logUtils.addCaseLog("restore = " + StringUtil.byte2HexStr(result));

        } catch (RemoteException e) {
            e.printStackTrace();

        }
        return result;

    }

    public byte My16transfer(byte blockNo) {
        byte result = 0x00;
        try {

            result = irfCardReader.transfer(blockNo);
//            byte[] bytes = result.getByteArray("cardInfo");
            logUtils.addCaseLog("transfer = " + result);

        } catch (RemoteException e) {
            e.printStackTrace();

        }
        return result;

    }

    public void My17CloseRfField() {
        try {
            irfCardReader.closeRfField();
            logUtils.addCaseLog("Contact-less closed successfully");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("Contact-less closed failed");
        }
    }


    public void stopSearchThread() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    My02stopSearch();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void J01002() {
        My01searchCard(null, 60);
    }

    public void J01004() {
        My01searchCard(new mylistener(), 60);
    }


    public void J01005() {

        J01004();
    }

    public void J01006() {

        J01004();
    }

    public void J01007() {

        J01004();
    }

    public void J01008() {

        J01004();
    }

    public void J01009() {

        J01004();
    }

    public void J01010() {

        J01004();
    }

    public void J01011() {

        J01004();
    }

    public void J01012() {

        J01004();
    }

    public void J01013() {

        J01004();
    }

    public void J01014() {
        My01searchCard(new mylistener(), 0);
    }

    public void J01015() {
        My01searchCard(new mylistener(), -1);
    }

    public void J01016() {
        My01searchCard(new mylistener(), 10);
    }

    public void J01017() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01018() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01019() {
        My01searchCard(new mylistener(), 60);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    My01searchCard(new mylistener(), 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

//    public void J01020() {
//        My01searchCard(new mylistener(), 60);
//    }
//
//    public void J01021() {
//        My01searchCard(new mylistener(), 60);
//    }

    public void J01022() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01023() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01024() {
        My01searchCard(new mylistener(), 60);
        My17CloseRfField();
    }

    public void J01025() {
        My01searchCard(new mylistener(), 60);
    }

   /* public void J01026() {
        My01searchCard(new mylistener(),60);
    }*/

    public void J01027() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01028() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01029() {
        My01searchCard(new mylistener(), 60);
    }

    public void J01030() {
        My01searchCard(new mylistener(), 60);
    }


    public void J01031() {

        new Thread() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    J01004();
//                    if (i == 99) {
//                        Message msg = new Message();
//                        msg.getData().putString("msg", "J01004 alredy excuted 100 times！");
//                        handler.sendMessage(msg);
//                    }
                }
            }
        }.start();
    }


    public void J02001() {
        My02stopSearch();
    }

    public void J02002() {
        J01004();
        stopSearchThread();
    }

    public void J02003() {
        for (int i = 0; i < 10; i++) {
            J02002();
            if (i == 9) {
                Message msg = new Message();
                msg.getData().putString("msg", "J02003 alredy excuted 10 times！");
                handler.sendMessage(msg);
            }
        }
    }

    public void J03001() {
        My03halt();
    }

    public void J03002() {
        J01004();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                My03halt();
            }
        }.start();
    }

    public void J04001() {
        String driver = "S50";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
      /*  String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);*/
    }

    public void J04002() {
        String driver = "S70";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
    }

    public void J04003() {
        String driver = "CPU";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04004() {
        String driver = "PRO";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04007() {
        String driver = "S80";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04008() {
        byte[] responseData = new byte[24];
        My04activate(null, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04009() {
        String driver = "！@#";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04010() {
        String driver = "";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    /*    public void J04012() {
            String driver = "S50";
            byte[] responseData = new byte[24];
            My04activate(driver,responseData);
            String res = bcd2Asc(responseData);
            logUtils.addCaseLog("The response data: " + res);
        }
        public void J04013() {
            String driver = "S50";
            byte[] responseData = new byte[24];
            My04activate(driver,responseData);
            String res = bcd2Asc(responseData);
            logUtils.addCaseLog("The response data: " + res);
        }*/
    public void J04015() {
        String driver = "CPU";
        byte[] responseData = new byte[24];

        String xxx = "";
        My04activate(driver, responseData);

        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04016() {
        String driver = "CPU";
        My04activate(driver, null);
    }

    public void J04017() {
        String driver = "CPU";
        byte[] responseData = new byte[]{};
        My04activate(driver, responseData);
    }

    public void J04018() {
        String driver = "CPU";
        byte[] responseData = new byte[4];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04019() {
        String driver = "CPU";
        byte[] responseData = new byte[5];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04020() {
        String driver = "Mifare UltraLight card";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04021() {
        String driver = "Mifare Desfire card";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04022() {
        String driver = "NTAG card";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04023() {
        String driver = "ICode card";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04024() {
        String driver = "UltraLight card";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04025() {
        String driver = "Mifare plus card";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04026() {
        String driver = "Mifare UltraLight card EV1";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04027() {
        String driver = "Mifare UltraLight card Nano";
        byte[] responseData = new byte[24];
        My04activate(driver, responseData);
        String res = bcd2Asc(responseData);
        logUtils.addCaseLog("The response data: " + res);
    }

    public void J04028() {
//        try {
//            byte[] sAtr = irfCardReader.getPowerUpATR();
//
//            logUtils.addCaseLog("ATR data = "+ StringUtil.byte2HexStr(sAtr));
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
    }


    public void J05001() {
        My05isExist();
    }

    public void J05002() {
        My05isExist();
    }

    public void J05003() {
        My05isExist();
    }

    public void J05004() {
        My05isExist();
    }

    public void J05005() {
        My05isExist();
    }

    public void J05007() {
        My05isExist();
    }

    public void J05008() {
        My05isExist();
    }

    public void J05009() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                logUtils.addCaseLog("Multi-thread implementation of contactless IC card search card");
                J05007();
            }
        }.start();

        ((MyApplication) context).serviceMoudle.getInsertCardReaderMoudle().My03isCardIn(1);

    }

    public void J05010() {
        My05isExist();
    }

    public void J05011() {
        My05isExist();
    }

    public void J05012() {
        My05isExist();
    }

    public void J05013() {
        My05isExist();
    }

    public void J05014() {
        My05isExist();
    }

    public void J05015() {
        My05isExist();
    }

    public void J05016() {
        My05isExist();
    }

    public void J05017() {
        My05isExist();
    }

    public void J05080(){
        try {
            Bundle keyBundle = new Bundle();
            keyBundle.putInt("keyIndex", 0);
            Bundle resKeybundle = irsa.generateRSAKeyPair(keyBundle); // if you had key, ignore this
            if (!resKeybundle.getBoolean("isSuccess")) {
                logUtils.addCaseLog("keyGen failed");

                return;
            }

            Bundle testBundle = new Bundle();
            testBundle.putInt("keyIndex", 0);
            testBundle.putByteArray("data", "Hello, world".getBytes());
            testBundle.putString("paddingType", "0");
            Bundle bundle = irsa.RSAEncryption(testBundle);
            if (bundle.getBoolean("isSuccess")) {
                bundle.getByteArray("encryptedData");
                logUtils.addCaseLog("encryption successful");
                logUtils.addCaseLog("res: " + bundle.getByteArray("encryptedData"));
                decrypted(bundle);
            } else {
                logUtils.addCaseLog("encryption failed");
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("RSA encryption abnormal");
            e.printStackTrace();
        }
    }

    public void decrypted(Bundle bundle){
        try {
            Bundle res_bundle = irsa.RSADecryption(bundle);
            if (res_bundle.getBoolean("isSuccess")) {
                byte[] ddd = res_bundle.getByteArray("data");
                String res = new String(ddd);
                logUtils.addCaseLog("decryption data: " + res + "\n");
                logUtils.addCaseLog("decryption successful" + "\n");
            } else {
                logUtils.addCaseLog("encryption failed");
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("RSA encryption abnormal");
            e.printStackTrace();
        }
    }


    public void J06001() {
        String res = HexUtil.toString(My06cardReset());
//        String res = bcd2Asc(My06cardReset());
        logUtils.addCaseLog("result: " + res);
        logUtils.showCaseLog();
//       String res = bcd2Asc(My06cardReset());
//        logUtils.addCaseLog("Result: " + My06cardReset());
//        logUtils.showCaseLog();
    }

    public void J06002() {
        String res = HexUtil.toString(My06cardReset());
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    private String bcd2Asc(byte[] My06cardReset) {
        return null;
    }

    /* public void J06003() {
         String res = bcd2Asc(My06cardReset());
         logUtils.addCaseLog("Result: " + res);
         logUtils.showCaseLog();
     }
 */
    public void J07001() {
        int blockNo = 3;
        int keyType = 0;//KEY_A
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07002()//先寻卡
    {
        int blockNo = 62;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07003() {
        int blockNo = 63;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
    }

    public void J07004() {
        int blockNo = -1;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07005() {
        int blockNo = 64;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07006() {
        int blockNo = 0;
        int keyType = -1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07007() {
        int blockNo = 0;
        int keyType = 2;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07008() {
        int blockNo = 0;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xF2;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
    }

    public void J07009() {
        int blockNo = 0;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0x12;
        key[1] = (byte) 0x35;
        key[2] = (byte) 0x70;
        key[3] = (byte) 0x00;
        key[4] = (byte) 0x34;
        key[5] = (byte) 0x66;
        My07authBlock(blockNo, keyType, key);
    }

    public void J07010() {
        int blockNo = 0;
        int keyType = 0;
        My07authBlock(blockNo, keyType, null);

    }

    public void J07011() {
        int blockNo = 0;
        int keyType = 1;
        byte[] key = new byte[]{};
        My07authBlock(blockNo, keyType, key);

    }

    public void J07012() {
        int blockNo = 0;
        int keyType = 0;
        byte[] key = new byte[5];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07013() {
        int blockNo = 0;
        int keyType = 1;
        byte[] key = new byte[7];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        key[6] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
    }


    public void J07014() {
        int blockNo = 1;
        int keyType = 0;//KEY_A
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07015() {
        int blockNo = 1;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

    public void J07016() {
        J03001();//halt
        int blockNo = 0;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

    }

//    public void J07018() {
//        int blockNo = 0;
//        int keyType = 0;//key-a
//        String keystr = "FFFFFFFFFFFF";
//        byte[] key = BCDDecode.str2Bcd(keystr);
////        byte[] key1 = Utils.hexStr2Bytes(keystr);
//        My07authBlock(blockNo, keyType, key);
//    }

    public void J07017() {
        int blockNo = 63;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(blockNo, keyType, key);
    }

    public void J07018() {
        int blockNo = 64;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(blockNo, keyType, key);
    }

    public void J07019() {
        int blockNo = 128;
        int keyType = 0;//key-a
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(blockNo, keyType, key);
    }

    public void J07020() {
        int blockNo = 200;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(blockNo, keyType, key);
    }

    public void J07021() {
        int blockNo = 255;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(blockNo, keyType, key);
    }

    public void J07022() {
        int blockNo = 256;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(blockNo, keyType, key);
    }

    public void J08001() {
        int sectorNo = 0;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08002() {
        int sectorNo = 15;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08003() {
        int sectorNo = 10;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }


    public void J08004() {
        int sectorNo = -1;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08005() {
        int sectorNo = 16;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08006() {
        int sectorNo = 0;
        int keyType = -1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08007() {
        int sectorNo = 0;
        int keyType = 2;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08008() {
        int sectorNo = 0;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0x1F;
        key[1] = (byte) 0xF2;
        key[2] = (byte) 0x3F;
        key[3] = (byte) 0xF4;
        key[4] = (byte) 0xF5;
        key[5] = (byte) 0xF4;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08009() {
        int sectorNo = 0;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0x1F;
        key[1] = (byte) 0xF2;
        key[2] = (byte) 0x3F;
        key[3] = (byte) 0xF4;
        key[4] = (byte) 0xF5;
        key[5] = (byte) 0xF4;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08010() {
        int sectorNo = 0;
        int keyType = 0;
        My08authSector(sectorNo, keyType, null);
    }

    public void J08011() {
        int sectorNo = 0;
        int keyType = 0;
        byte[] key = new byte[]{};
        My08authSector(sectorNo, keyType, key);
    }

    public void J08012() {
        int sectorNo = 0;
        int keyType = 0;
        byte[] key = new byte[5];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08013() {
        int sectorNo = 0;
        int keyType = 1;
        byte[] key = new byte[7];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        key[6] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08014() {
        int sectorNo = 0;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08015() {
        int sectorNo = 1;
        int keyType = 1;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }

    public void J08016() {
        J03001();//halt
        int sectorNo = 0;
        int keyType = 0;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My08authSector(sectorNo, keyType, key);
    }
//    public void J08019() {
//        int sectorNo = 0;
//        int keyType = 0;//key-a
//        String keystr = "FFFFFFFFFFFF";
//        byte[] key = BCDDecode.str2Bcd(keystr);
//        My08authSector(sectorNo, keyType, key);
//    }
//
//    public void J08020() {
//        int sectorNo = 10;
//        int keyType = 1;//key-b
//        String keystr = "FFFFFFFFFFFF";
//        byte[] key = BCDDecode.str2Bcd(keystr);
//        My08authSector(sectorNo, keyType, key);
//    }

    public void J08017() {
        int sectorNo = 31;
        int keyType = 0;//key-a
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(sectorNo, keyType, key);
    }

    public void J08018() {
        int sectorNo = 32;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(sectorNo, keyType, key);
    }

    public void J08019() {
        int sectorNo = 35;
        int keyType = 0;//key-a
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(sectorNo, keyType, key);
    }

    public void J08020() {
        int sectorNo = 39;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(sectorNo, keyType, key);
    }

    public void J08021() {
        int sectorNo = 40;
        int keyType = 1;//key-b
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(sectorNo, keyType, key);
    }

    public void J09001() {
        J07001();
        int blockNo = 0;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09002() {
        J07002();
        int blockNo = 63;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09003() {
        J01004();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        J07002();
        int blockNo = 62;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = StringUtil.bytesToHexString(data);
//       String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09004() {
        J07001();
        int blockNo = 1;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09005() {
        J07001();
        int blockNo = 2;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09006() {
        J07001();
        int blockNo = 3;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
//        String res = byte2HexStr(data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }


    public void J09007() {
        J07002();
        int blockNo = -1;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
//        String res = byte2HexStr(data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    private String byte2HexStr(byte[] data) {
        return null;
    }

    public void J09008() {
        int blockNo = 64;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

//        int blockNo = 64;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09009() {
        J03001();//halt
        int blockNo = 62;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09010() {
        J07002();
        int blockNo = 1;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09011() {
        J07001();
        int blockNo = 4;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09012() {
//        J07002();
        int blockNo = 62;
        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
//        String res = byte2HexStr(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09013() {
        J07002();
        int blockNo = 62;
        My09readBlock(blockNo, null);
    }

    public void J09014() {
        J07002();
        int blockNo = 62;
        byte[] data = new byte[2049];
        My09readBlock(blockNo, data);
        String res = byte2HexStr(data);
//        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09015() {
        J07002();
        int blockNo = 62;
        byte[] data = new byte[]{};
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09016() {
        J07002();
        int blockNo = 62;
        byte[] data = new byte[1];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09017() {
        J07002();
        int blockNo = 62;
        byte[] data = new byte[15];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09018() {
//        J07002();
        int blockNo = 256;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);

        byte[] data = new byte[16];
        My09readBlock(blockNo, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J09019() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(0, 1, key);

        for (int i = 0; i < 4; i++) {
            byte[] data = new byte[16];
            My09readBlock(i, data);
            String res = Utils.bcd2Asc(data);
            logUtils.addCaseLog("Read block data: " + res);
        }
    }

    public void J09020() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(126, 1, key);

        for (int i = 124; i < 128; i++) {
            byte[] data = new byte[16];
            My09readBlock(i, data);
            String res = Utils.bcd2Asc(data);
            logUtils.addCaseLog("Read block data: " + res);
        }
    }

    public void J09021() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(130, 1, key);

        for (int i = 128; i < 144; i++) {
            byte[] data = new byte[16];
            My09readBlock(i, data);
            String res = Utils.bcd2Asc(data);
            logUtils.addCaseLog("Read block data: " + res);
        }
    }

    public void J09022() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(250, 1, key);

        for (int i = 240; i < 256; i++) {//最后一个扇区
            byte[] data = new byte[16];
            My09readBlock(i, data);
            String res = Utils.bcd2Asc(data);
            logUtils.addCaseLog("Read block data: " + res);
        }
    }


    public void J10001() {

        J07002();
        int blockNo = 62;//不要改写分区的第4块数据
        String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10002() {
        J07002();
        int blockNo = 60;
        String datastr = "FEFFFFFFFFFFFFFFFFFFFEFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J10003() {
        J07002();
        int blockNo = 61;
        String datastr = "000000000000FF078067FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J10004() {
        J07002();
        int blockNo = 63;
        String datastr = "010203040506FF0780690708090A0B0C";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        String key_a_str = "010203040506";
        byte[] key_a = BCDDecode.str2Bcd(key_a_str);
        My07authBlock(63, 0, key_a);

        String key_b_str = "0708090A0B0C";
        byte[] key_b = BCDDecode.str2Bcd(key_b_str);
        My07authBlock(63, 1, key_b);

        String data3str = "FFFFFFFFFFFFFF078069FFFFFFFFFFFF";//恢复默认密钥
        byte[] data3 = BCDDecode.str2Bcd(data3str);
        My10writeBlock(63, data3);
    }

    public void J10005() {
        J07002();
        int blockNo = -1;
        String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10006() {
        J07002();
        int blockNo = 64;//不要改写分区的第4块数据
        String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10007() {
        J07002();
        int blockNo = 62;//不要改写分区的第4块数据
        String datastr = "000000000000FF078069FFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10008() {
        J07002();
        int blockNo = 62;//不要改写分区的第4块数据
        String datastr = "000000000000FF078069FFFFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10009() {
        J07002();
        int blockNo = 59;
        String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10010() {
        J03001();
        int blockNo = 62;//不要改写分区的第4块数据
        String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10011() {
        J07002();
        int blockNo = 62;
        String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10012() {
        J07002();
        int blockNo = 62;//不要改写分区的第4块数据
        String datastr = "";//69-68
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);
    }

    public void J10013() {
        J07002();
        int blockNo = 62;//不要改写分区的第4块数据
        My10writeBlock(blockNo, null);
    }

    public void J10014() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(0, 1, key);

        String datastr = "01020304050607080910111213141516";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(1, data);

        byte[] data2 = new byte[16];
        My09readBlock(1, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J10015() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(125, 1, key);

        String datastr = "00010203040506070809101112131415";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(126, data);

        byte[] data2 = new byte[16];
        My09readBlock(126, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J10016() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(143, 1, key);

        String datastr = "15000102030405060708091011121314";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(142, data);

        byte[] data2 = new byte[16];
        My09readBlock(142, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J10017() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(254, 1, key);

        String datastr = "15000102030405060708091011121315";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(250, data);

        byte[] data2 = new byte[16];
        My09readBlock(250, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
    }

    public void J11001() {
        J04003();//激活卡片
        byte[] apdu = new byte[19];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;
        apdu[4] = (byte) 0x0E;
        apdu[5] = (byte) 0x31;
        apdu[6] = (byte) 0x50;
        apdu[7] = (byte) 0x41;
        apdu[8] = (byte) 0x59;
        apdu[9] = (byte) 0x2E;
        apdu[10] = (byte) 0x53;
        apdu[11] = (byte) 0x59;
        apdu[12] = (byte) 0x53;
        apdu[13] = (byte) 0x2E;
        apdu[14] = (byte) 0x44;
        apdu[15] = (byte) 0x44;
        apdu[16] = (byte) 0x46;
        apdu[17] = (byte) 0x30;
        apdu[18] = (byte) 0x31;

        My11exchangeApdu(apdu);
    }

    public void J11002() {
        J04003();
        My11exchangeApdu(null);

    }

    public void J11003() {
        J04003();
        byte[] apdu = new byte[]{};
        My11exchangeApdu(apdu);
    }

    public void J11004() {
        J04003();
        byte[] apdu = new byte[19];
        apdu[0] = (byte) 0x02;
        apdu[1] = (byte) 0x02;
        apdu[2] = (byte) 0x02;
        apdu[3] = (byte) 0x02;
        apdu[4] = (byte) 0x02;
        apdu[5] = (byte) 0x02;
        apdu[6] = (byte) 0x02;
        apdu[7] = (byte) 0x02;
        apdu[8] = (byte) 0x02;
        apdu[9] = (byte) 0x00;
        apdu[10] = (byte) 0x00;
        apdu[11] = (byte) 0x00;
        apdu[12] = (byte) 0x00;
        apdu[13] = (byte) 0x00;
        apdu[14] = (byte) 0x00;
        apdu[15] = (byte) 0x00;
        apdu[16] = (byte) 0x00;
        apdu[17] = (byte) 0x00;
        apdu[18] = (byte) 0x00;
        My11exchangeApdu(apdu);
    }

    public void J11005() {
        J04003();
        byte[] apdu = new byte[10];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;
        apdu[4] = (byte) 0x0E;
        apdu[5] = (byte) 0x31;
        apdu[6] = (byte) 0x50;
        apdu[7] = (byte) 0x41;
        apdu[8] = (byte) 0x59;
        apdu[9] = (byte) 0x2E;

        My11exchangeApdu(apdu);
    }

    public void J11006() {
        J04003();
        byte[] apdu = new byte[20];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;
        apdu[4] = (byte) 0x0E;
        apdu[5] = (byte) 0x31;
        apdu[6] = (byte) 0x50;
        apdu[7] = (byte) 0x41;
        apdu[8] = (byte) 0x59;
        apdu[9] = (byte) 0x2E;
        apdu[10] = (byte) 0x53;
        apdu[11] = (byte) 0x59;
        apdu[12] = (byte) 0x53;
        apdu[13] = (byte) 0x2E;
        apdu[14] = (byte) 0x44;
        apdu[15] = (byte) 0x44;
        apdu[16] = (byte) 0x46;
        apdu[17] = (byte) 0x30;
        apdu[18] = (byte) 0x31;
        apdu[19] = (byte) 0x47;

        My11exchangeApdu(apdu);
    }

    public void J11007() {
        J04003();
        byte[] apdu = new byte[1024];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;
        apdu[4] = (byte) 0x0E;
        apdu[5] = (byte) 0x31;
        apdu[6] = (byte) 0x50;
        apdu[7] = (byte) 0x41;
        apdu[8] = (byte) 0x59;
        apdu[9] = (byte) 0x2E;
        apdu[10] = (byte) 0x53;
        apdu[11] = (byte) 0x59;
        apdu[12] = (byte) 0x53;
        apdu[13] = (byte) 0x2E;
        apdu[14] = (byte) 0x44;
        apdu[15] = (byte) 0x44;
        apdu[16] = (byte) 0x46;
        apdu[17] = (byte) 0x30;
        apdu[18] = (byte) 0x31;

        My11exchangeApdu(apdu);
    }

    public void J11008() {
        J04003();
        byte[] apdu = new byte[19];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;
        apdu[4] = (byte) 0x0E;
        apdu[5] = (byte) 0x31;
        apdu[6] = (byte) 0x50;
        apdu[7] = (byte) 0x41;
        apdu[8] = (byte) 0x59;
        apdu[9] = (byte) 0x2E;
        apdu[10] = (byte) 0x53;
        apdu[11] = (byte) 0x59;
        apdu[12] = (byte) 0x53;
        apdu[13] = (byte) 0x2E;
        apdu[14] = (byte) 0x44;
        apdu[15] = (byte) 0x44;
        apdu[16] = (byte) 0x46;
        apdu[17] = (byte) 0x30;
        apdu[18] = (byte) 0x31;

        My11exchangeApdu(apdu);
    }

    public void J11009() {
        J03001();
        byte[] apdu = new byte[19];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;
        apdu[4] = (byte) 0x0E;
        apdu[5] = (byte) 0x31;
        apdu[6] = (byte) 0x50;
        apdu[7] = (byte) 0x41;
        apdu[8] = (byte) 0x59;
        apdu[9] = (byte) 0x2E;
        apdu[10] = (byte) 0x53;
        apdu[11] = (byte) 0x59;
        apdu[12] = (byte) 0x53;
        apdu[13] = (byte) 0x2E;
        apdu[14] = (byte) 0x44;
        apdu[15] = (byte) 0x44;
        apdu[16] = (byte) 0x46;
        apdu[17] = (byte) 0x30;
        apdu[18] = (byte) 0x31;

        My11exchangeApdu(apdu);
    }

    public void J11010() {
        J04003();
        byte[] apdu = new byte[4];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;

        My11exchangeApdu(apdu);
    }

    public void J11011() {
        J04003();
        byte[] apdu = new byte[3];
        //选择命令报文
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;

        My11exchangeApdu(apdu);
    }

    public void J11012() {

        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x08,
                (byte) 0x0A0, (byte) 0X00, (byte) 0X00, (byte) 0X03, (byte) 0x33, (byte) 0x01,(byte) 0x01, (byte) 0x02};
        String res = HexUtil.toString(My11exchangeApdu(bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void J11013() {

        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x07,
                (byte) 0x0A0, (byte) 0X00, (byte) 0X00, (byte) 0X00, (byte) 0x04, (byte) 0x10, (byte) 0x10};
        String res = HexUtil.toString(My11exchangeApdu(bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void J11014() {
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x07,
                (byte) 0x0A0, (byte) 0X00, (byte) 0X00, (byte) 0X00, (byte) 0x03, (byte) 0x10, (byte) 0x10};
        String res = HexUtil.toString(My11exchangeApdu(bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void J11015() {

        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x07,
                (byte) 0x0A1, (byte) 0X23, (byte) 0X45, (byte) 0X67, (byte) 0x83, (byte) 0x10, (byte) 0x10};
        String res = HexUtil.toString(My11exchangeApdu(bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void J12020() {
        int blockNo = 5;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, 1, key);

        String datastr = "FEFFFFFFFFFFFFFFFFFFFEFFFFFFFFFF";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(blockNo, 3);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data after increment: " + res2);
    }

    public void J12001() {
        int blockNo = 5;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(blockNo, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data after increment: " + res2);
    }

    public void J12002() {
        int blockNo = 5;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(blockNo, 100);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data after increment: " + res2);
    }

    public void J12004() {
        J04001();
        int blockNo = 5;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式
        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
        My12increaseValue(64, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data after increment: " + res2);
    }

    public void J12006() {
        J04001();
        int blockNo = 5;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式
        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
        My12increaseValue(-1, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After value-added Read block data: " + res2);
    }

    public void J12008() {
        J04001();
        int blockNo = 5;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式
        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
        My12increaseValue(blockNo, -1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After value-added Read block data: " + res2);
    }

    public void J12009() {
        J04001();
        int blockNo = 5;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式
        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
        My12increaseValue(blockNo, 0);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After value-added Read block data: " + res2);
        if (res2.equals("5D0D0000A2F2FFFF5D0D000011EE11EE J12009  case results EE11EE")) {
            logUtils.addCaseLog("：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J12009  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J12009  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J12009  case results：" + res2 + "，Execute case Failed！");
    }

    public void J12010() {
        int blockNo = 5;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        J03001();
        My12increaseValue(blockNo, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After value-added Read block data: " + res2);
    }

    public void J12012() {
        J04001();
        int blockNo = 5;
        int keyType = 1;//KEY_B
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, keyType, key);
        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式
        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);
        My12increaseValue(39, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After value-added Read block data: " + res2);
    }

    public void J12013() {
        int blockNo = 5;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(blockNo, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After value-added Read block data: " + res2);
    }

    public void J12014() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(0, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(1, data);

        byte[] data2 = new byte[16];
        My09readBlock(1, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(1, 1);

        My09readBlock(1, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res2);
        if (res2.equals("5E0D0000A1F2FFFF5E0D000011EE11EE")) {
            logUtils.addCaseLog("J12014  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J12014  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J12014  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J12014  case results：" + res2 + "，Execute case Failed！");
    }

    public void J12015() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(126, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(126, data);

        byte[] data2 = new byte[16];
        My09readBlock(126, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(126, 1);

        My09readBlock(126, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res2);
        if (res2.equals("5E0D0000A1F2FFFF5E0D000011EE11EE")) {
            logUtils.addCaseLog("J12015  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J12015  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J12015  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J12015  case results：" + res2 + "，Execute case Failed！");
    }


    public void J12016() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(134, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(134, data);

        byte[] data2 = new byte[16];
        My09readBlock(134, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(134, 1);

        My09readBlock(134, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res2);
        if (res2.equals("5E0D0000A1F2FFFF5E0D000011EE11EE")) {
            logUtils.addCaseLog("J12016  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J12016  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J12016  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J12016  case results：" + res2 + "，Execute case Failed！");

    }

    public void J12017() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(248, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(248, data);

        byte[] data2 = new byte[16];
        My09readBlock(248, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(248, 1);

        My09readBlock(248, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res2);
        logUtils.addCaseLog("Read block data: " + res2);
        if (res2.equals("5E0D0000A1F2FFFF5E0D000011EE11EE")) {
            logUtils.addCaseLog("J12017  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J12017  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J12017  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J12017  case results：" + res2 + "，Execute case Failed！");


    }

    public void J13001() {
        J12001();
        My13decreaseValue(5, 1);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13002() {
        J12001();
        My13decreaseValue(5, 0);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13003() {
        J12001();
        My13decreaseValue(5, -1);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13004() {
        J12002();
        My13decreaseValue(5, 100);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13005() {
        J12001();
        My13decreaseValue(-1, 1);
        byte[] data = new byte[16];
        My09readBlock(-1, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13006() {
        J12001();
        My13decreaseValue(64, 1);
        byte[] data = new byte[16];
        My09readBlock(64, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13007() {
        J12001();
        My13decreaseValue(49, 1);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13008() {
        J03001();
        My13decreaseValue(5, 1);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13009() {
        J12001();
        My13decreaseValue(5, 1);
        byte[] data = new byte[16];
        My09readBlock(5, data);
        String res = Utils.bcd2Asc(data);
        logUtils.addCaseLog("After impairment，Read block data: " + res);
    }

    public void J13010() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My08authSector(0, 1, key);

        String datastr = "5E0D0000A1F2FFFF5E0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(2, data);

        byte[] data2 = new byte[16];
        My09readBlock(2, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My13decreaseValue(2, 1);

        My09readBlock(2, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After impairment，Sector values: " + res2);
        if (res2.equals("5D0D0000A2F2FFFF5D0D000011EE11EE")) {
            logUtils.addCaseLog("J13010  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J13010  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J13010  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J13010  case results：" + res2 + "，Execute case Failed！");

    }

    public void J13011() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(126, 1, key);

        String datastr = "5E0D0000A1F2FFFF5E0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(126, data);

        byte[] data2 = new byte[16];
        My09readBlock(126, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My13decreaseValue(126, 1);

        My09readBlock(126, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After impairment，Sector values: " + res2);
        if (res2.equals("5D0D0000A2F2FFFF5D0D000011EE11EE")) {
            logUtils.addCaseLog("J13011  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J13011  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J13011  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J13011  case results：" + res2 + "，Execute case Failed！");
    }

    public void J13012() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(142, 1, key);

        String datastr = "5E0D0000A1F2FFFF5E0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(142, data);

        byte[] data2 = new byte[16];
        My09readBlock(142, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);


        My13decreaseValue(142, 1);

        My09readBlock(142, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After impairment，Sector values: " + res2);
        if (res2.equals("5D0D0000A2F2FFFF5D0D000011EE11EE")) {
            logUtils.addCaseLog("J13012  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J13012  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J13012  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J13012  case results：" + res2 + "，Execute case Failed！");
    }

    public void J13013() {
        String keystr = "FFFFFFFFFFFF";
        byte[] key = BCDDecode.str2Bcd(keystr);
        My07authBlock(250, 0, key);

        String datastr = "5E0D0000A1F2FFFF5E0D000011EE11EE";
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(250, data);

        byte[] data2 = new byte[16];
        My09readBlock(250, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My13decreaseValue(250, 1);

        My09readBlock(250, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("After impairment，Sector values: " + res2);
        if (res2.equals("5D0D0000A2F2FFFF5D0D000011EE11EE")) {
            logUtils.addCaseLog("J13013  case results：" + res2 + "，Execute case Succss！");
            this.printMsgTool("J13013  case results：" + res2 + "，Execute case Succss！");
            return;
        }
        logUtils.addCaseLog("J13013  case results：" + res2 + "，Execute case Failed！");
        this.printMsgTool("J13013  case results：" + res2 + "，Execute case Failed！");
    }

    public void J14001() {
//        J01004();
        My14getCardInfo();
    }

    public void J14002() {

        My14getCardInfo();
        stopSearchThread();
    }

    public void J14003() {

        My14getCardInfo();
        stopSearchThread();
    }

    public void J14004() {

        My14getCardInfo();
        stopSearchThread();
    }

    public void J14005() {

        My14getCardInfo();
        stopSearchThread();
    }

    public void J14006() {

        My14getCardInfo();
        stopSearchThread();
    }

    public void J15001() {
        {
            int blockNo = 1;
            int keyType = 0;//KEY_A
            byte[] key = new byte[6];
            key[0] = (byte) 0xFF;
            key[1] = (byte) 0xFF;
            key[2] = (byte) 0xFF;
            key[3] = (byte) 0xFF;
            key[4] = (byte) 0xFF;
            key[5] = (byte) 0xFF;
            My07authBlock(blockNo, keyType, key);

        }


    /*    {
//        J07002();
            int blockNo = 1;//不要改写分区的第4块数据
            String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
            byte[] data = BCDDecode.str2Bcd(datastr);
            My10writeBlock(blockNo,data);
        }*/

        My15restore((byte) 2);

    }

    public void J15002() {
        {
            int blockNo = 6;
            int keyType = 0;//KEY_A
            byte[] key = new byte[6];
            key[0] = (byte) 0xFF;
            key[1] = (byte) 0xFF;
            key[2] = (byte) 0xFF;
            key[3] = (byte) 0xFF;
            key[4] = (byte) 0xFF;
            key[5] = (byte) 0xFF;
            My07authBlock(blockNo, keyType, key);

        }
   /*     {
//        J07002();
            int blockNo = 2;//不要改写分区的第4块数据
            String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
            byte[] data = BCDDecode.str2Bcd(datastr);
            My10writeBlock(blockNo,data);
        }*/

        My15restore((byte) 6);

    }

//    public void J15003() {
//        {
//            int blockNo = 16;
//            int keyType = 0;//KEY_A
//            byte[] key = new byte[6];
//            key[0] = (byte) 0xFF;
//            key[1] = (byte) 0xFF;
//            key[2] = (byte) 0xFF;
//            key[3] = (byte) 0xFF;
//            key[4] = (byte) 0xFF;
//            key[5] = (byte) 0xFF;
//            My07authBlock(blockNo, keyType, key);
//
//        }
//   /*     {
////        J07002();
//            int blockNo = 4;//不要改写分区的第4块数据
//            String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
//            byte[] data = BCDDecode.str2Bcd(datastr);
//            My10writeBlock(blockNo,data);
//        }
//*/
//        My15restore((byte) 16);
//
//    }
//
//    public void J15004() {
//        {
//            int blockNo = 32;
//            int keyType = 0;//KEY_A
//            byte[] key = new byte[6];
//            key[0] = (byte) 0xFF;
//            key[1] = (byte) 0xFF;
//            key[2] = (byte) 0xFF;
//            key[3] = (byte) 0xFF;
//            key[4] = (byte) 0xFF;
//            key[5] = (byte) 0xFF;
//            My07authBlock(blockNo, keyType, key);
//
//        }
//        /*{
////        J07002();
//            int blockNo = 5;//不要改写分区的第4块数据
//            String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
//            byte[] data = BCDDecode.str2Bcd(datastr);
//            My10writeBlock(blockNo,data);
//        }*/
//
//        My15restore((byte) 32);
//
//    }
//
//    public void J15005() {
//        {
//            int blockNo = 64;
//            int keyType = 0;//KEY_A
//            byte[] key = new byte[6];
//            key[0] = (byte) 0xFF;
//            key[1] = (byte) 0xFF;
//            key[2] = (byte) 0xFF;
//            key[3] = (byte) 0xFF;
//            key[4] = (byte) 0xFF;
//            key[5] = (byte) 0xFF;
//            My07authBlock(blockNo, keyType, key);
//
//        }
//      /*  {
////        J07002();
//            int blockNo = 6;//不要改写分区的第4块数据
//            String datastr = "000000000000FF078069FFFFFFFFFFFF";//68-69
//            byte[] data = BCDDecode.str2Bcd(datastr);
//            My10writeBlock(blockNo,data);
//        }
//*/
//        My15restore((byte) 64);
//
//    }

    public void J16001() {
        int blockNo = 5;
        byte[] key = new byte[6];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        My07authBlock(blockNo, 1, key);

        String datastr = "5D0D0000A2F2FFFF5D0D000011EE11EE";//特定的格式
        byte[] data = BCDDecode.str2Bcd(datastr);
        My10writeBlock(blockNo, data);  //进行增减值，只能对一个扇区的第1、2块（从0开始）数据进行，而且还必须是那个格式

        byte[] data2 = new byte[16];
        My09readBlock(blockNo, data2);
        String res = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data: " + res);

        My12increaseValue(blockNo, 1);
        My09readBlock(blockNo, data2);
        String res2 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data after increment: " + res2);
        My16transfer((byte) 5);
        My09readBlock(blockNo, data2);
        String res3 = Utils.bcd2Asc(data2);
        logUtils.addCaseLog("Read block data after increment: " + res3);

    }

    public void J17001() {
        My17CloseRfField();
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

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.IrfCardReaderMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "Perform case");
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