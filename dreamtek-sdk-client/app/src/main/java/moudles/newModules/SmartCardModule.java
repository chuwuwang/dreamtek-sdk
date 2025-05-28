package moudles.newModules;

import static com.verifone.b.b.aa;

import android.os.Bundle;
import android.os.RemoteException;

import com.verifone.smartpos.api.entities.card.contracless.felica.FelicaSearchParam;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.RFSearchListener;
import com.dreamtek.smartpos.deviceservice.aidl.SmartCardStatusChangedEvent;

/**
 * Created by Simon on 2021/8/16
 */
public class SmartCardModule extends SmartCardMod {
    private static final String TAG = "SmartCardModule";

    Object callbackLock = new Object();

    public SmartCardModule(){
        super();
        moduleType = 1;
    }

/*
    public boolean T_powerUp(String slot) throws RemoteException {

        return iSmartCardReaders[Integer.valueOf(slot)].powerUp();

    }

    public boolean T_powerDown(String slot) throws RemoteException {

        return iSmartCardReaders[Integer.valueOf(slot)].powerDown();

    }

    public boolean T_isCardIn(String slot) throws RemoteException {

        return iSmartCardReaders[Integer.valueOf(slot)].isCardIn();

    }

    public byte[] T_exchangeApdu(String sApdu, String slot) throws RemoteException {
        byte[] apdu = StringUtil.hexStr2Bytes(sApdu);

        byte[] result = iSmartCardReaders[Integer.valueOf(slot)].exchangeApdu(apdu);
        return result;
    }

    public boolean T_isPSAMCardExists(String slot) throws RemoteException {
        boolean blRet = false;

        blRet = iSmartCardReaders[Integer.valueOf(slot)].isPSAMCardExists();

        return blRet;
    }

    public byte T_checkCardStatus(String slot) throws RemoteException {

        return iSmartCardReaders[Integer.valueOf(slot)].checkCardStatus();

    }

    public byte[] T_getPowerUpATR(String slot) throws RemoteException {

        return iSmartCardReaders[Integer.valueOf(slot)].getPowerUpATR();

    }

    public byte[] T_powerUpWithConfig( String sBundle, String slot) throws RemoteException {
        BundleConfig[] bundleConfigs = new BundleConfig[ ]{};
        Bundle bundle = convert( sBundle, bundleConfigs );
        return iSmartCardReaders[Integer.valueOf(slot)].powerUpWithConfig(bundle);

    }

    */
/*等待用户IC卡 插卡，插卡后上电成功后，做exchangAPDU交互*//*

    public byte[] T_checkAndReadICCard(String sApdu,String sslot, String timeoutSeconds) throws RemoteException {

        int slot = Integer.valueOf( sslot );
        int timeout = Integer.valueOf( timeoutSeconds)*1000;

        long endTime = System.currentTimeMillis() + timeout ;

        boolean bRet = false;
        do{

           bRet = iSmartCardReaders[Integer.valueOf(slot)].isCardIn();;

        } while ( System.currentTimeMillis() < endTime && (!bRet) );

        if( !bRet ){
            Log.e(TAG, "Timeout while check card");
            logUtils.addCaseLog("Timeout while check card");
            return null;
        }

        bRet = iSmartCardReaders[Integer.valueOf(slot)].powerUp();
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

            bRet = iSmartCardReaders[Integer.valueOf(slot)].isPSAMCardExists();

        } while ( System.currentTimeMillis() < endTime && (!bRet) );

        if( !bRet ){
            Log.e(TAG, "Timeout while check card");
            logUtils.addCaseLog("Timeout while check card");
            return null;
        }

        bRet = iSmartCardReaders[Integer.valueOf(slot)].powerUp();
        if( !bRet ){
            Log.e(TAG, "power up failed");
            logUtils.addCaseLog("power up failed");
            return null;
        }

        byte[] ret = T_exchangeApdu(sApdu);
        return ret;

    }


    public void T_detectCardStatusChanged(String timeout, String slot) throws RemoteException {

        synchronized (callbackLock) {
            try {
                callbackLock.wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        iSmartCardReaders[Integer.valueOf(slot)].detectCardStatusChanged(Integer.valueOf(timeout), new SmartCardStatusChangedEvent.Stub() {
            @Override
            public void onChanged(int statusNow) throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("onChanged statusNow : " + statusNow);
                    callbackLock.notify();
                }
            }

            @Override
            public void onTimeout() throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("onTimeout : " );
                    callbackLock.notify();
                }
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("onError errorCode : " + errorCode);
                    callbackLock.notify();
                }
            }
        });
        synchronized (callbackLock) {
            try {
                callbackLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }
*/

}
