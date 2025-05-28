package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.MagCardListener;

public class MagCardReader extends TestModule {
    private static final String TAG = "MagCardReader";
    Object callbackLock = new Object();

    public void T_searchCard(String timeout) throws RemoteException {
        synchronized (callbackLock) {
            try {
                callbackLock.wait(10);   // 等待 10毫秒，清除未完成交易的消息
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        iMagCardReader.searchCard(Integer.valueOf(timeout), new MagCardListener.Stub() {
            @Override
            public void onSuccess(Bundle track) throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("Search Success,the PAN : " + track.getString("PAN"));
                }
            }

            @Override
            public void onError(int error, String message) throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("Search Error : " + message);
                }
            }

            @Override
            public void onTimeout() throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("Search timeout");
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
    }

    public void T_stopSearch() {
        try {
            logUtils.addCaseLog("stop search execute");
            iMagCardReader.stopSearch();
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("stop search exception");
        }
    }

    public void T_enableTrack(String trkNum) {
        try {
            logUtils.addCaseLog("enable Track execute");
            iMagCardReader.enableTrack(Integer.parseInt(trkNum));
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("enable track exception");
        }
    }

}
