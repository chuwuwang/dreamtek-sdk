package moudles.newModules;

import android.os.IBinder;
import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.FelicaInfomation;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.FelicaListener;

import java.util.List;

public class FelicaModule extends TestModule {
    private static final String TAG = "FelicaModule";
    Object callbackLock = new Object();
    int search_result;

    protected boolean T_powerOn() {
        try {
            logUtils.addCaseLog("Felica power on");
            iFelica.powerOn();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Felica power on exception");
            e.printStackTrace();
            return false;
        }
    }

    protected boolean T_powerOff() {
        try {
            logUtils.addCaseLog("Felica power off");
            iFelica.powerOff();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Felica power off exception");
            e.printStackTrace();
            return false;
        }
    }

    protected boolean T_searchCard(String conflictType, String systemNumOne, String systemNumTwo,
                                   String requestType, String timeout) throws RemoteException {
        synchronized (callbackLock) {
            try {
                callbackLock.wait(10);   // 等待 10毫秒，清除未完成交易的消息
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        iFelica.searchCard(Integer.valueOf(conflictType), Byte.valueOf(systemNumOne), Byte.valueOf(systemNumTwo),
                Integer.valueOf(requestType), new FelicaListener.Stub() {
                    @Override
                    public void onSearchResult(int ret, List<FelicaInfomation> felicaInfos) throws RemoteException {
                        synchronized (callbackLock) {
                            search_result = ret;
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
        if (search_result == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected byte[] T_communicate(String sendData) {
        try {
            logUtils.addCaseLog("communicate execute");
            return iFelica.communicate(StringUtil.hexStr2Bytes(sendData));
        } catch (RemoteException e) {
            logUtils.addCaseLog("communicate exception");
            e.printStackTrace();
            return null;
        }
    }

    protected boolean T_stopSearch() {
        try {
            logUtils.addCaseLog("stop search execute");
            iFelica.stopSearch();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("stop search exception");
            e.printStackTrace();
            return false;
        }
    }
}
