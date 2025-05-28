package moudles.newModules;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;

/**
 * Created by Simon on 2021/8/18
 */
abstract class SmartCardMod extends TestModule {
    private static final String TAG = "SmartCardMod";
    protected int moduleType;

//    protected boolean T_selectSlot( String sSlot ) throws RemoteException {
//        smartCardSlot = Integer.valueOf( sSlot );
//        Log.d(TAG, "Set slot: " + smartCardSlot);
//        return true;
//    }
//
//    protected boolean T_powerUp() throws RemoteException {
//        boolean result = false;
//
//        if( moduleType == 1 ) {
//            result = iSmartCardReaders[smartCardSlot].powerUp();
//        } else if( moduleType == 2){
//            result = iInsertCardReaders[smartCardSlot].powerUp();
//        }
//        logUtils.addCaseLog("moduleType " + moduleType);
//        logUtils.addCaseLog("Power-up " + result);
//        return result;
//    }
//
//    protected boolean T_powerDown() throws RemoteException {
//        {
//            boolean result = false;
//            if( moduleType == 1 ) {
//                result = iSmartCardReaders[smartCardSlot].powerDown();
//            } else if( moduleType == 2){
//                result = iInsertCardReaders[smartCardSlot].powerDown();
//            }
//
//            logUtils.addCaseLog("Power-down " + result);
//            return result;
//        }
//    }
//
//    protected boolean T_isCardIn() throws RemoteException {
//        boolean result = false;
//        if( moduleType == 1 ) {
//            result = iSmartCardReaders[smartCardSlot].isCardIn();
//        } else if( moduleType == 2){
//            result = iInsertCardReaders[smartCardSlot].isCardIn();
//        }
//
//        return result;
//
//    }
//
////    private byte[] My04cardReset(int resetType) {
////        try {
////            return iInsertCardReader.cardReset(resetType);
////        } catch (RemoteException e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
//
//    public byte[] T_exchangeApdu(String sApdu) throws RemoteException {
//        byte[] apdu = StringUtil.hexStr2Bytes(sApdu);
//        if( moduleType == 1){
//            return iSmartCardReaders[smartCardSlot].exchangeApdu(apdu);
//
//        } else if( moduleType == 2 ){
//            return iInsertCardReaders[smartCardSlot].exchangeApdu(apdu);
//        }
//        return null;
//    }
//
//
//    public boolean T_isPSAMCardExists() throws RemoteException {
//        boolean blRet = false;
//        if( moduleType == 1) {
//            blRet = iSmartCardReaders[smartCardSlot].isPSAMCardExists();
//        } else if( moduleType == 2){
//            blRet = iInsertCardReaders[smartCardSlot].isPSAMCardExists();
//        }
//
//            return blRet;
//    }
//
//    public byte[] T_chkPowerApdu( String timeoutSeconds, String sApdu ) throws RemoteException {
//        return T_chkPowerApdu( timeoutSeconds, sApdu, String.valueOf(smartCardSlot) );
//    }
//    public byte[] T_chkPowerApdu( String timeoutSeconds, String sApdu, String sSlot ) throws RemoteException {
//        int slot = Integer.valueOf( sSlot );
//        int lastSlot = smartCardSlot;
//        smartCardSlot = slot;
//        int timeout = Integer.valueOf( timeoutSeconds)*1000;
//
//        long endTime = System.currentTimeMillis() + timeout ;
//
//        boolean bRet = false;
//        do{
//            if( slot == 0 ) {
//                bRet = T_isCardIn();
//            } else {
//                bRet = T_isPSAMCardExists();
//            }
//        } while ( System.currentTimeMillis() < endTime && (!bRet) );
//
//        if( !bRet ){
//            Log.e(TAG, "Timeout while check card");
//            smartCardSlot = lastSlot;
//            return null;
//        }
//
//        bRet = T_powerUp();
//        if( !bRet ){
//            Log.e(TAG, "power up failed");
//            smartCardSlot = lastSlot;
//            return null;
//        }
//
//        byte[] ret = T_exchangeApdu(sApdu);
//
//        smartCardSlot = lastSlot;
//
//        return ret;
//
//    }

}
