package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.RFSearchListener;

import java.util.Arrays;

public class RFCardReaderModule extends TestModule {

    Object callbackLock = new Object();
    int search_result;

/*
    public int T_searchCard(String timeout) throws RemoteException {

        synchronized (callbackLock) {
            try {
                callbackLock.wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        irfCardReader.searchCard(new RFSearchListener.Stub() {
            @Override
            public void onCardPass(int cardType) throws RemoteException {
                synchronized (callbackLock) {
                    search_result = cardType;
                    callbackLock.notify();
                    logUtils.addCaseLog("Search Success,search_result : " + search_result);
                }
            }

            @Override
            public void onFail(int error, String message) throws RemoteException {
                synchronized (callbackLock) {
                    search_result = error;
                    callbackLock.notify();
                    logUtils.addCaseLog("Search Failed,search_result : " + search_result);
                }
            }
        }, Integer.valueOf(timeout));
        synchronized (callbackLock) {
            try {
                callbackLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return search_result;
    }

    public boolean T_stopSearch() {
        try {
            irfCardReader.stopSearch();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("stop search exception");
            e.printStackTrace();
            return false;
        }
    }

    public int T_activate(String driver) throws RemoteException {
        byte[] responseData = new byte[64];
        int result = irfCardReader.activate(driver, responseData);
        byte[] response = Arrays.copyOfRange(responseData,1,responseData[0]);
        logUtils.addCaseLog("activate responseData: " + StringUtil.byte2HexStr(response));
        return result;
    }

    public boolean T_halt() {
        try {
            irfCardReader.halt();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("half exception");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_isExist() {
        try {
            return irfCardReader.isExist();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] T_exchangeApdu(String apdu) throws RemoteException {
        byte[] result = irfCardReader.exchangeApdu(StringUtil.hexStr2Bytes(apdu));
        logUtils.addCaseLog("exchange apdu result: " + StringUtil.byte2HexStr(result));
        return result;
    }

    public byte[] T_cardReset() throws RemoteException {
        byte[] result = irfCardReader.cardReset();
        byte[] response = Arrays.copyOfRange(result,1,result[0]);
        logUtils.addCaseLog("card result: " + StringUtil.byte2HexStr(response));
        return result;
    }

    public int T_authBlock(String blockNo, String keyType, String key) throws RemoteException {
        int result = irfCardReader.authBlock(Integer.valueOf(blockNo), Integer.valueOf(keyType), StringUtil.hexStr2Bytes(key));
        logUtils.addCaseLog("auth block result: " + result);
        return result;
    }

    public int T_authSector(String sectorNo, String keyType, String key) throws RemoteException {
        int result = irfCardReader.authSector(Integer.valueOf(sectorNo), Integer.valueOf(keyType), StringUtil.hexStr2Bytes(key));
        logUtils.addCaseLog("auth sector result: " + result);
        return result;
    }

    public int T_readBlock(String blockNo) throws RemoteException {
        byte[] data = new byte[64];
        int result = irfCardReader.readBlock(Integer.valueOf(blockNo), data);
        logUtils.addCaseLog("readBlock: " + StringUtil.byte2HexStr(data));
        return result;
    }

    public int T_writeBlock(String blockNo, String data) throws RemoteException {
        int result = irfCardReader.writeBlock(Integer.valueOf(blockNo), StringUtil.hexStr2Bytes(data));
        logUtils.addCaseLog("write block: " + data);
        return result;
    }

    public int T_increaseValue(String blockNo, String value) throws RemoteException {
        int result = irfCardReader.increaseValue(Integer.valueOf(blockNo), Integer.valueOf(value));
        logUtils.addCaseLog("increase value:" + value);
        return result;
    }

    public int T_decreaseValue(String blockNo, String value) throws RemoteException {
        int result = irfCardReader.decreaseValue(Integer.valueOf(blockNo), Integer.valueOf(value));
        logUtils.addCaseLog("decrease value" + value);
        return result;
    }

    public String T_getCardInfo() throws RemoteException {
        Bundle result = irfCardReader.getCardInfo();
        String sResult = "";
        for (String key : result.keySet()) {
            sResult += result.get(key);
            sResult += "|";
        }
        return sResult;
    }

    public byte T_restore(String blockNo) throws RemoteException {
        byte result = irfCardReader.restore(Byte.valueOf(blockNo));
        return result;
    }

    public byte T_transfer(String blockNo) throws RemoteException {
        return irfCardReader.transfer(Byte.valueOf(blockNo));
    }

    public boolean T_CloseRfField() {
        try {
            irfCardReader.CloseRfField();
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
*/
}
