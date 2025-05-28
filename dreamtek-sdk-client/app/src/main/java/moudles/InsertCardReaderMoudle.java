package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.HexUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.PrinterListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import Utils.LogUtils;
import base.MyApplication;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 接触式IC卡
 */
public class InsertCardReaderMoudle {
    Context context;
    ISmartCardReader icCardReader;
    ISmartCardReader psamCardReader;
    IPrinter iPrinter;
    LogUtils logUtils;

    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> powerUp = new ArrayList<String>();
    ArrayList<String> powerDown = new ArrayList<String>();
    ArrayList<String> isCardIn = new ArrayList<String>();
    ArrayList<String> exchangeApdu = new ArrayList<String>();
    //    ArrayList<String> cardReset = new ArrayList<String>();
    ArrayList<String> isPSAMCardExists = new ArrayList<String>();
    ArrayList<String> checkCardStatus = new ArrayList<String>();
    ArrayList<String> getPowerUpATR = new ArrayList<String>();
    ArrayList<String> powerUpWithConfig = new ArrayList<String>();
    ArrayList<String> checkCardFlow = new ArrayList<>();

    public InsertCardReaderMoudle(Context context, ISmartCardReader icCardReader, ISmartCardReader psamCardReader, IPrinter iPrinter) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.icCardReader = icCardReader;
        this.psamCardReader = psamCardReader;
        this.iPrinter = iPrinter;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.InsertCardReaderMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "I01":
                            powerUp.add(i.getName());
                            break;
                        case "I02":
                            powerDown.add(i.getName());
                            break;
                        case "I03":
                            isCardIn.add(i.getName());
                            break;
                        case "I05":
                            exchangeApdu.add(i.getName());
                            break;
//                        case "I05":
//                            cardReset.add(i.getName());
//                            break;
                        case "I06":
                            isPSAMCardExists.add(i.getName());
                            break;
                        case "I07":
                            checkCardStatus.add(i.getName());
                            break;
                        case "I08":
                            getPowerUpATR.add(i.getName());
                            break;
                        case "I09":
                            powerUpWithConfig.add(i.getName());
                            break;
                        case "I10":
                            checkCardFlow.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(powerUp);
            caseNames.add(powerDown);
            caseNames.add(isCardIn);
            caseNames.add(exchangeApdu);
//            caseNames.add(cardReset);
            caseNames.add(isPSAMCardExists);
            caseNames.add(checkCardStatus);
            caseNames.add(getPowerUpATR);
            caseNames.add(powerUpWithConfig);
            caseNames.add(checkCardFlow);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean My01powerUp(int slotNo) {
        try {
            boolean result;
            if (slotNo == 0) {
                result = icCardReader.powerUp();
            } else {
                result = psamCardReader.powerUp();
            }
            logUtils.addCaseLog("Power-up  " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("Power-up -> false");
            return false;
        }
    }

    public boolean My02powerDown(int slotNo) {
        try {
            boolean result;
            if (slotNo == 0) {
                result = icCardReader.powerDown();
            } else {
                result = psamCardReader.powerDown();
            }
            logUtils.addCaseLog("Power-down " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("Power-down -> false");
            return false;
        }
    }

    public boolean My03isCardIn(int slotNo) {
        try {
            boolean result;
            if (slotNo == 0) {
                result = icCardReader.isCardIn();
            } else {
                result = psamCardReader.isCardIn();
            }
            logUtils.addCaseLog("isCardIn() return code：" + result);
            return result;

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

//    private byte[] My04cardReset(int resetType) {
//        try {
//            return iInsertCardReader.cardReset(resetType);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }




    private void printTest(final int times) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Bundle format = new Bundle();
                //align
                format.putInt("font", 0);
                format.putInt("align", 0);
                String str = "";
                str = "";
//            str = String.valueOf(i) + "测试数据\n";
                for (int i = 0; i < times; i++) {
                    str = String.valueOf(i) + "测试数据\n";
                    addText(format, str, false);
                    Log.d(TAG, "\nDD_NUMBER: " + i + " printTest");

                }
                startPrint(new MyListener(), false);

            }
        }).start();
    }

    private void ddTest(final int times) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                I01006();
                for (int i = 0; i < times; i++) {
                    //PSAM卡测试用的
                    byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                            (byte) 0x3f, (byte) 0X00};
                    String res = HexUtil.toString(My05exchangeApdu(1, bytes));
                    Log.d(TAG, "\nNUMBER: " + i + "ddTest result:" + res);
                    if (res == null) res = "None" ;

                    logUtils.addCaseLog("Result: " + res);
                    logUtils.showCaseLog();
                }
//            }
//        }).start();

    }

    private static final String TAG = "InsertCardReaderMoudle";

    class MyListener extends PrinterListener.Stub {
        @Override
        public void onError(int error) throws RemoteException {
            Log.d(TAG, "onError: reason:" + error);

        }

        @Override
        public void onFinish() throws RemoteException {
            Log.d(TAG, "onFinish");
        }
    }

    public void startPrint(MyListener listener, boolean silence) {

        try {
            boolean printing = false;
            do {

                if (printing) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (printing);


            iPrinter.startPrint(listener);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addText(Bundle format, String text, boolean silence) {
        try {
            iPrinter.addText(format, text);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public byte[] My05exchangeApdu(int slotNo, byte[] apdu) {
        try {
            byte[] result;
            if (slotNo == 0) {
                result = icCardReader.exchangeApdu(apdu);
            } else {
                result = psamCardReader.exchangeApdu(apdu);
            }
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "My05exchangeApdu: " + e.getMessage());
            return null;
        }
    }

    public boolean My06isPSAMCardExists(int slotNo) {
        boolean blRet = false;
        try {
            if (slotNo == 0) {
                blRet = icCardReader.isPSAMCardExists();
            } else {
                blRet = psamCardReader.isPSAMCardExists();
            }
            logUtils.addCaseLog("isPSAMCardExists() return code：" + blRet);
            return blRet;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte My07checkCardStatus(int slotNo) {
        try {
            byte result;
            if (slotNo == 0) {
                result = icCardReader.checkCardStatus();
            } else {
                result = psamCardReader.checkCardStatus();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return (byte) 0x03;
        }
    }

    public byte[] My08getPowerUpATR(int slotNo) {
        try {
            byte[] result;
            if (slotNo == 0) {
                result = icCardReader.getPowerUpATR();
            } else {
                result = psamCardReader.getPowerUpATR();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] My09powerUpWithConfig(int slotNo, Bundle param) {
        try {
            byte[] result;
            if (slotNo == 0) {
                result = icCardReader.powerUpWithConfig(param);
            } else {
                result = psamCardReader.powerUpWithConfig(param);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void My10CheckCardFlow() {

    }

    public void I08001() {
        My08getPowerUpATR(0);
        String res = HexUtil.toString(My08getPowerUpATR(0));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I08002() {
        My08getPowerUpATR(1);
        String res = HexUtil.toString(My08getPowerUpATR(1));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I01001() {
        My01powerUp(0);
        /*try{
            byte [] b;
            b = icCardReader.getPowerUpATR();
            logUtils.addCaseLog(Arrays.toString(b));
        }catch (Exception e) {
        }*/
    }

    public void I01002() {
        My01powerUp(0);
        /*try{
            byte [] b;
            b = icCardReader.getPowerUpATR();
            logUtils.addCaseLog(Arrays.toString(b));
        }catch (Exception e) {
        }*/
    }

    public void I01003() {
        My01powerUp(0);
      /*  try{
            byte [] b;
            b = icCardReader.getPowerUpATR();
            logUtils.addCaseLog(Arrays.toString(b));
        }catch (Exception e) {
        }*/
    }

//    public void I01004() {
//        My01powerUp(0);
//        try {
//            byte[] b;
//            b = icCardReader.getPowerUpATR();
//            logUtils.addCaseLog(Arrays.toString(b));
//        } catch (Exception e) {
//        }
//    }

//    public void I01005() {
//        My01powerUp(0);
//        try {
//            byte[] b;
//            b = icCardReader.getPowerUpATR();
//            logUtils.addCaseLog(Arrays.toString(b));
//        } catch (Exception e) {
//        }
//    }

    public void I01006() {
        My03isCardIn(0);
        My01powerUp(0);
        /*try{
            byte [] b;
            b = icCardReader.getPowerUpATR();
            logUtils.addCaseLog(Arrays.toString(b));
        }catch (Exception e) {
        }*/
    }

    public void I01007() {
        My03isCardIn(1);
        My01powerUp(1);
        /*try{
            byte [] b;
            b = icCardReader.getPowerUpATR();
            logUtils.addCaseLog(Arrays.toString(b));
        }catch (Exception e) {
        }*/
    }

    public void I01008() {
        My03isCardIn(2);
        My01powerUp(2);
        /*try{
            byte [] b;
            b = icCardReader.getPowerUpATR();
            logUtils.addCaseLog(Arrays.toString(b));
        }catch (Exception e) {
        }*/
    }


//    public void I01006() throws RemoteException {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("ATRcheck",false);
//        byte [] ATR;
//        ATR = icCardReader.powerUpWithConfig(bundle);
//        My01powerUp();
//    }

    public void I02001() {
        My02powerDown(0);
    }

    public void I02002() {
        My02powerDown(0);
    }

    public void I02003() {
        My02powerDown(0);
    }

    public void I02004() {
        My02powerDown(1);
    }

    public void I02005() {
        My02powerDown(1);
    }

    public void I03001() {
        My03isCardIn(0);
    }

    public void I03002() {
        My03isCardIn(0);
    }

    public void I03003() {
        My03isCardIn(0);
    }

    public void I03004() {
        My03isCardIn(0);
    }

    public void I03005() {
        My03isCardIn(0);
    }

    public void I03006() {
        My03isCardIn(0);
    }

    public void I03007() {
        My03isCardIn(1);
    }

    public void I03008() {
        My03isCardIn(1);
    }

//    public void I04001() {
//        String res = bcd2Asc(My04cardReset(0));
//        logUtils.addCaseLog("返回结果: " + res);
//        Log.d("TAG", "result: " + res);
//        logUtils.showCaseLog();
//    }
//
//    public void I04002() {
//        String res = bcd2Asc(My04cardReset(0));
//        logUtils.addCaseLog("返回结果: " + res);
//        Log.d("TAG", "result: " + res);
//        logUtils.showCaseLog();
//    }
//
//    public void I04003() {
//        String res = bcd2Asc(My04cardReset(0));
//        logUtils.addCaseLog("返回结果: " + res);
//        Log.d("TAG", "result: " + res);
//        logUtils.showCaseLog();
//    }
//
//    public void I04005() {
//        String res = bcd2Asc(My04cardReset(1));
//        logUtils.addCaseLog("返回结果: " + res);
//        Log.d("TAG", "result: " + res);
//        logUtils.showCaseLog();
//    }
//
//    public void I04007() {
//        String res = bcd2Asc(My04cardReset(-1));
//        logUtils.addCaseLog("返回结果: " + res);
//        Log.d("TAG", "result: " + res);
//        logUtils.showCaseLog();
//    }
//
//    public void I04008() {
//        String res = bcd2Asc(My04cardReset(2));
//        logUtils.addCaseLog("返回结果: " + res);
//        Log.d("TAG", "result: " + res);
//        logUtils.showCaseLog();
//    }

    public void I05001() {
        I01001();
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0E,
                (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
                (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F', (byte) '0', (byte) '1', (byte) '0'};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05002() {
        I01001();
        String res = HexUtil.toString(My05exchangeApdu(0, null));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05003() {
        I01001();
        byte[] apdu = new byte[]{};
        String res = HexUtil.toString(My05exchangeApdu(0, apdu));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05004() {
        I01001();
        byte[] apdu = new byte[19];
        apdu[0] = (byte) 0x01;
        apdu[1] = (byte) 0x01;
        apdu[2] = (byte) 0x01;
        apdu[3] = (byte) 0x01;
        apdu[4] = (byte) 0x01;
        apdu[5] = (byte) 0x01;
        apdu[6] = (byte) 0x01;
        apdu[7] = (byte) 0x01;
        apdu[8] = (byte) 0x01;
        apdu[9] = (byte) 0x01;
        apdu[10] = (byte) 0x01;
        apdu[11] = (byte) 0x01;
        apdu[12] = (byte) 0x01;
        apdu[13] = (byte) 0x00;
        apdu[14] = (byte) 0x00;
        apdu[15] = (byte) 0x00;
        apdu[16] = (byte) 0x00;
        apdu[17] = (byte) 0x00;
        apdu[18] = (byte) 0x00;
        String res = HexUtil.toString(My05exchangeApdu(0, apdu));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05005() {
        I01001();
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0E,
                (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
                (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F', (byte) '0'};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05006() {
        I01001();
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0E,
                (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
                (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F', (byte) '0', (byte) '1', (byte) '4'};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05007() {
        I01001();
        byte[] apdu = new byte[1024];
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
        String res = HexUtil.toString(My05exchangeApdu(0, apdu));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05008() {

        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0E,
                (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
                (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F', (byte) '0', (byte) '1'};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }


    public void I05009() {
        I01001();
        byte[] apdu = new byte[4];
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;
        apdu[3] = (byte) 0x00;

        String res = HexUtil.toString(My05exchangeApdu(0, apdu));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05010() {
        I01001();
        byte[] apdu = new byte[3];
        apdu[0] = (byte) 0x00;
        apdu[1] = (byte) 0xA4;
        apdu[2] = (byte) 0x04;

        String res = HexUtil.toString(My05exchangeApdu(0, apdu));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05011() {
        I05001();
    }

    public void I05012() {
        I01001();
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x08,
                (byte) 0x0A0, (byte) 0X00, (byte) 0X00, (byte) 0X03, (byte) 0x33, (byte) 0x01, (byte) 0x01, (byte) 0x01};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05013() {
        I01001();
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x07,
                (byte) 0x0A0, (byte) 0X00, (byte) 0X00, (byte) 0X00, (byte) 0x04, (byte) 0x10, (byte) 0x10};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }



    public void I05014() {
        I01001();
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x07,
                (byte) 0x0A0, (byte) 0X00, (byte) 0X00, (byte) 0X00, (byte) 0x03, (byte) 0x10, (byte) 0x10};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05015() {
        I01001();
        //SELECT 1PAY.SYS.DDF01
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x07,
                (byte) 0x0A1, (byte) 0X23, (byte) 0X45, (byte) 0X67, (byte) 0x83, (byte) 0x10, (byte) 0x10};
        String res = HexUtil.toString(My05exchangeApdu(0, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05016() {
        I01006();
        //PSAM卡测试用的
        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                (byte) 0x3f, (byte) 0X00};
        String res = HexUtil.toString(My05exchangeApdu(1, bytes));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I05017() {

        Log.d(TAG, "\nTest I05023 start");

        ddTest(40);

        printTest(5);

    }

    public void I05018() {
        Log.d(TAG, "\nTest I05024 start");

        ddTest(40);

        printTest(40);
    }

    public void I06001() {
        My06isPSAMCardExists(1);
    }

    public void I06002() {
        My06isPSAMCardExists(1);
    }

    public void I06003() {
        My06isPSAMCardExists(1);
    }

    public void I06004() {
        My06isPSAMCardExists(1);
    }

    public void I07001() {
        byte b = My07checkCardStatus(0);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

    public void I07002() {
        I01001();
        byte b = My07checkCardStatus(0);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

    public void I07003() {
        I01001();
        byte b = My07checkCardStatus(0);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

    public void I07004() {
        byte b = My07checkCardStatus(0);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

    public void I07005() {
        I01001();
        I02001();
        byte b = My07checkCardStatus(0);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

    public void I07006() {
        byte b = My07checkCardStatus(0);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }


    public void I07007() {
        byte b = My07checkCardStatus(1);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

//    public void I07008() {
//        I01006();
//        I02004();
//        byte b = My07checkCardStatus(1);
//        logUtils.addCaseLog("Result: " + String.valueOf(b));
//    }

    public void I07009() {
        I01006();
        byte b = My07checkCardStatus(1);
        logUtils.addCaseLog("Result: " + String.valueOf(b));
    }

    public void I09001() {
        Bundle param = new Bundle();
        param.putBoolean("ATRCheck", true);
        My09powerUpWithConfig(0, param);

        String res = HexUtil.toString(My09powerUpWithConfig(0, param));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I09002() {
        Bundle param = new Bundle();
        param.putBoolean("ATRCheck", false);
        My09powerUpWithConfig(0, param);

        String res = HexUtil.toString(My09powerUpWithConfig(0, param));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I09003() {
        Bundle param = new Bundle();
        param.putBoolean("ATRCheck", true);
        My09powerUpWithConfig(1, param);

        String res = HexUtil.toString(My09powerUpWithConfig(1, param));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }

    public void I09004() {
        Bundle param = new Bundle();
        param.putBoolean("ATRCheck", false);
        My09powerUpWithConfig(1, param);

        String res = HexUtil.toString(My09powerUpWithConfig(1, param));
        logUtils.addCaseLog("Result: " + res);
        logUtils.showCaseLog();
    }


    static class Data {
        private int step;
        private boolean succeed;
        private byte[] response;

        public Data(int step) {
            this.step = step;
        }

        public Data(int step, boolean succeed) {
            this.step = step;
            this.succeed = succeed;
        }

        public Data(int step, byte[] response) {
            this.step = step;
            this.response = response;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public boolean isSucceed() {
            return succeed;
        }

        public void setSucceed(boolean succeed) {
            this.succeed = succeed;
        }

        public byte[] getResponse() {
            return response;
        }

        public void setResponse(byte[] response) {
            this.response = response;
        }
    }

    public void I10001() {
        Observable.create(new ObservableOnSubscribe<Data>() {
                    @Override
                    public void subscribe(ObservableEmitter<Data> emitter) throws Exception {
                        while (!icCardReader.isCardIn()) {
                            emitter.onNext(new Data(0, false));
                            Thread.sleep(500);
                        }
                        emitter.onNext(new Data(0, true));
                        boolean result = icCardReader.powerUp();
                        emitter.onNext(new Data(1, result));
                        if (!result)
                            return;
                        byte[] bytes = {(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0E,
                                (byte) '1', (byte) 'P', (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
                                (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F', (byte) '0', (byte) '1', (byte) '0'};
                        byte[] res = icCardReader.exchangeApdu(bytes);
                        emitter.onNext(new Data(2, res));
                        result = icCardReader.powerDown();
                        emitter.onNext(new Data(3, result));
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Data data) {
                        switch (data.step) {
                            case 0:
                                logUtils.addCaseLog("<br/>card inserted:" + data.succeed);
                                break;
                            case 1:
                                logUtils.addCaseLog("<br/>power up result:" + data.succeed);
                                break;
                            case 2:
                                logUtils.addCaseLog("<br/>exchange result: " + HexUtil.toString(data.response));
                                break;
                            case 3:
                                logUtils.addCaseLog("<br/>power down result:" + data.succeed);
                                break;
                        }
                        logUtils.showCaseLog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
            Class aClass = Class.forName("moudles.InsertCardReaderMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }
}