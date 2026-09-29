package moudles.newModules;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.MagCardListener;

import Utils.LogUtils;
import moudles.MagCardReaderMoudle;

public class MagCardReaderModule extends TestModule {
    private static final String TAG = "MagCardReader";
    Object callbackLock = new Object();
    String searchCard_result;


    public String T_searchCard(String timeout) throws RemoteException {

        synchronized (callbackLock) {
            try {
                callbackLock.wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        iMagCardReader.searchCard(Integer.valueOf(timeout), new MagCardListener.Stub() {
            @Override
            public void onSuccess(Bundle track) throws RemoteException {
                synchronized (callbackLock) {
                    String pan = track.getString("PAN");
                    String track1 = track.getString("TRACK1");
                    String track2 = track.getString("TRACK2");
                    String track3 = track.getString("TRACK3");
                    String serviceCode = track.getString("SERVICE_CODE");
                    String expiredDate = track.getString("EXPIRED_DATE");
                    logUtils.addCaseLog("Search successful" + "\n" +
                            "PAN:" + pan + "\n" +
                            "TRACK1:" + track1 + "\n" +
                            "TRACK2:" + track2 + "\n" +
                            "TRACK3:" + track3 + "\n" +
                            "SERVICE_CODE:" + serviceCode + "\n" +
                            "EXPIRED_DATE:" + expiredDate + "\n");

                    searchCard_result = "Search successful" + "\n" +
                            "PAN:" + pan + "\n" +
                            "TRACK1:" + track1 + "\n" +
                            "TRACK2:" + track2 + "\n" +
                            "TRACK3:" + track3 + "\n" +
                            "SERVICE_CODE:" + serviceCode + "\n" +
                            "EXPIRED_DATE:" + expiredDate + "\n";

                    callbackLock.notify();
                }
            }

            @Override
            public void onError(int error, String message) throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("Search Error : " + message);
                    searchCard_result = "Search Error";
                    callbackLock.notify();
                }
            }

            @Override
            public void onTimeout() throws RemoteException {
                synchronized (callbackLock) {
                    logUtils.addCaseLog("Search timeout");
                    searchCard_result = "Search Timeout";
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
        if(searchCard_result == null){
            return null;
        }else{
            return  searchCard_result;
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

class MyListener extends MagCardListener.Stub {
    @Override
    public void onError(int error, String message) throws RemoteException {
//        Message msg = new Message();
//        msg.getData().putString("msg", "Card swipe failure, error code:" + error + '(' + message + ')');
        TestModule.Log.i("YAPING", "onError" + error );
        TestModule.logUtils.addCaseLog("Card swipe failure, error code:" + error + '(' + message + ')');

    }

    @Override
    public void onSuccess(Bundle track) throws RemoteException {
        Message msg = new Message();
        String pan = track.getString("PAN");
        String track1 = track.getString("TRACK1");
        String track2 = track.getString("TRACK2");
        String track3 = track.getString("TRACK3");
        String serviceCode = track.getString("SERVICE_CODE");
        String expiredDate = track.getString("EXPIRED_DATE");

        TestModule.Log.i("YAPING", "onSuccess");
//        msg.getData().putString("msg", "Swipe card successfully" + "\n" +
//                "PAN:" + pan + "\n" +
//                "TRACK1:" + track1 + "\n" +
//                "TRACK2:" + track2 + "\n" +
//                "TRACK3:" + track3 + "\n" +
//                "SERVICE_CODE:" + serviceCode + "\n" +
//                "EXPIRED_DATE:" + expiredDate + "\n");
//
//        TestModule.logUtils.addCaseLog(msg);
        TestModule.logUtils.addCaseLog("Swipe card successfully" + "\n" +
                "PAN:" + pan + "\n" +
                "TRACK1:" + track1 + "\n" +
                "TRACK2:" + track2 + "\n" +
                "TRACK3:" + track3 + "\n" +
                "SERVICE_CODE:" + serviceCode + "\n" +
                "EXPIRED_DATE:" + expiredDate + "\n");

    }

    @Override
    public void onTimeout() throws RemoteException {
        //Message msg = new Message();
//        msg.getData().putString("msg", "Swipe the timeout");
//        TestModule.logUtils.addCaseLog(msg);
        TestModule.Log.i("YAPING", "onTimeout");
        TestModule.logUtils.addCaseLog("Swipe the timeout");
    }
}