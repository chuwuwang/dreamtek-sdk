package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

/**
 * Created by Simon on 2021/8/18
 */
public class InsertCardModule extends SmartCardMod {
    private static final String TAG = "InsertCardModule";

    public InsertCardModule(){
        super();
        moduleType = 2;
    }

/*
    public boolean T_powerUp(String slot) throws RemoteException {

        return iInsertCardReaders[Integer.valueOf(slot)].powerUp();

    }

    public boolean T_powerDown(String slot) throws RemoteException {

        return iInsertCardReaders[Integer.valueOf(slot)].powerDown();

    }

    public boolean T_isCardIn(String slot) throws RemoteException {

        return iInsertCardReaders[Integer.valueOf(slot)].isCardIn();

    }

    public byte[] T_exchangeApdu(String sApdu, String slot) throws RemoteException {
        byte[] apdu = StringUtil.hexStr2Bytes(sApdu);

        byte[] result = iInsertCardReaders[Integer.valueOf(slot)].exchangeApdu(apdu);
        return result;
    }

    public boolean T_isPSAMCardExists(String slot) throws RemoteException {
        boolean blRet = false;

        blRet = iInsertCardReaders[Integer.valueOf(slot)].isPSAMCardExists();

        return blRet;
    }

    */
/*等待用户IC卡 插卡，插卡后上电成功后，做exchangAPDU交互*//*

    public byte[] T_checkAndReadICCard(String sApdu,String sslot, String timeoutSeconds) throws RemoteException {

        int slot = Integer.valueOf( sslot );
        int timeout = Integer.valueOf( timeoutSeconds)*1000;

        long endTime = System.currentTimeMillis() + timeout ;

        boolean bRet = false;
        do{

            bRet = iInsertCardReaders[Integer.valueOf(slot)].isCardIn();;

        } while ( System.currentTimeMillis() < endTime && (!bRet) );

        if( !bRet ){
            Log.e(TAG, "Timeout while check card");
            logUtils.addCaseLog("Timeout while check card");
            return null;
        }

        bRet = iInsertCardReaders[Integer.valueOf(slot)].powerUp();
        if( !bRet ){
            Log.e(TAG, "power up failed");
            logUtils.addCaseLog("power up failed");
            return null;
        }

        byte[] ret = T_exchangeApdu(sApdu);
        return ret;

    }

    */
/*isPSAMCardExists等待用户PSAM卡 插卡，插卡后上电成功后，做exchangAPDU交互*//*

    public byte[] T_checkAndReadPSAMCard(String sApdu,String sslot, String timeoutSeconds) throws RemoteException {

        int slot = Integer.valueOf( sslot );
        int timeout = Integer.valueOf( timeoutSeconds)*1000;

        long endTime = System.currentTimeMillis() + timeout ;

        boolean bRet = false;
        do{

            bRet = iInsertCardReaders[Integer.valueOf(slot)].isPSAMCardExists();

        } while ( System.currentTimeMillis() < endTime && (!bRet) );

        if( !bRet ){
            Log.e(TAG, "Timeout while check card");
            logUtils.addCaseLog("Timeout while check card");
            return null;
        }

        bRet = iInsertCardReaders[Integer.valueOf(slot)].powerUp();
        if( !bRet ){
            Log.e(TAG, "power up failed");
            logUtils.addCaseLog("power up failed");
            return null;
        }

        byte[] ret = T_exchangeApdu(sApdu);
        return ret;

    }
*/

}
