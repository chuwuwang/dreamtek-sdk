package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.MagCardListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtil;
import Utils.LogUtils;
import base.MyApplication;

/**
 * 磁条卡阅读器
 */
public class MagCardReaderMoudle {
    Context context;
    IMagCardReader iMagCardReader;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> searchCard = new ArrayList<String>();
    ArrayList<String> stopSearch = new ArrayList<String>();
    ArrayList<String> autoTest = new ArrayList<String>();
    ArrayList<String> searchCardEx = new ArrayList<>();
    private static final int START_PRINT = 1;
    private static final int START_SCAN = 2;

    boolean flag;//取消寻卡执行完毕标志

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
            logUtils.showCaseLog();
            switch (msg.what) {
                case START_PRINT:
                    MyApplication.serviceMoudle.getPintBtMoudle().D03028();
                    break;
                default:
                    break;
            }
        }
    };

    public MagCardReaderMoudle(Context context, IMagCardReader iMagCardReader) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iMagCardReader = iMagCardReader;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.MagCardReaderMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "H01":
                            searchCard.add(i.getName());
                            break;
                        case "H02":
                            stopSearch.add(i.getName());
                            break;
                        case "H03":
                            autoTest.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(searchCard);
            caseNames.add(stopSearch);
            caseNames.add(autoTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void My01searchCard(int timeout, MagCardListener listener) {
        try {
            Message msg = new Message();
            msg.getData().putString("msg", "Please swipe the magnetic stripe card");
            handler.sendMessage(msg);
            iMagCardReader.searchCard(timeout, listener);
        } catch (RemoteException e) {
            Message msg = new Message();
            msg.getData().putString("msg", "Wait for an exception to swipe");
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    public void My02stopSearch() {
        try {
            Message msg = new Message();
            msg.getData().putString("msg", "Execute the cancellation of the swipe");
            handler.sendMessage(msg);
            iMagCardReader.stopSearch();
        } catch (RemoteException e) {
            Message msg = new Message();
            msg.getData().putString("msg", "Cancel the swipe exception");
            handler.sendMessage(msg);
            e.printStackTrace();
            return;
        }
        Message msg = new Message();
        msg.getData().putString("msg", "Cancel credit card successfully");
        handler.sendMessage(msg);
    }

    public void My03autoTest() {
        try {
            Message msg = new Message();
            msg.getData().putString("msg", "Execute the cancellation of the swipe");
            handler.sendMessage(msg);
            iMagCardReader.stopSearch();
        } catch (RemoteException e) {
            Message msg = new Message();
            msg.getData().putString("msg", "Cancel the swipe exception");
            handler.sendMessage(msg);
            e.printStackTrace();
            return;
        }
        Message msg = new Message();
        msg.getData().putString("msg", "Cancel credit card successfully");
        handler.sendMessage(msg);
    }

    public void stopSearchThread() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                My02stopSearch();
                flag = false;
            }
        }.start();
    }

    public void H01001() {

        int timeout = 0;
        My01searchCard(timeout, new MyListener());
    }

    public void H01002() {
        int timeout = 10;
        My01searchCard(timeout, new MyListener());
    }

    public void H01003() {
        int timeout = 60;
        My01searchCard(timeout, new MyListener());
    }

    public void H01004() {
        int timeout = 10;
        My01searchCard(timeout, null);
    }

    public void H01005() {
        H01003();
    }

    public void H01013() {
        H01003();
    }

    public void H01014() {
        int timeout = 300;
        My01searchCard(timeout, new MyListener());
    }

    public void H01015() {
        int timeout = -1;
        My01searchCard(timeout, new MyListener());
    }

    public void H01016() {
        int timeout = 301;
        My01searchCard(timeout, new MyListener());
    }

    public void H01018() {

        new Thread() {
            public void run() {
                try {
                    Message msg = new Message();
                    msg.getData().putString("msg", "The first execution waits for the card swipe");
                    handler.sendMessage(msg);
                    H01003();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    Message msg = new Message();
                    msg.getData().putString("msg", "The second execution waits for the card to be swiped");
                    handler.sendMessage(msg);
                    My01searchCard(30, new MyListener());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void H01019() {
        new Thread() {
            public void run() {
                try {
                    Message msg = new Message();
                    msg.getData().putString("msg", "Execute wait for card swipe");
                    handler.sendMessage(msg);
                    H01003();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //调用startprint
                Message msg = new Message();
                msg.getData().putString("msg", "Perform the boot print process");
                handler.sendMessage(msg);
                msg = new Message();
                msg.what = START_PRINT;
                handler.sendMessage(msg);
            }

        }.start();
    }

    public void H01020() {
        new Thread() {
            public void run() {
                try {
                    Message msg = new Message();
                    msg.getData().putString("msg", "Execute wait for card swipe");
                    handler.sendMessage(msg);
                    H01003();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //调用startprint
                Message msg = new Message();
                msg.getData().putString("msg", "Perform the start scan process");
                handler.sendMessage(msg);
                msg = new Message();
                msg.what = START_SCAN;
                handler.sendMessage(msg);
            }

        }.start();
    }

    public void H01023() {
        H01003();
    }

//    public void H01024() {
//        int timeout = -1;
//        My01searchCard(timeout, new MyListener());
//    }


    public void H01025() {
        int timeout = 1;
        My01searchCard(timeout, new MyListener());
    }

    public void H01026() {
        int timeout = 300;
        My01searchCard(timeout, new MyListener());
    }

    public void H01027() {
        int timeout = 60;
        My01searchCard(timeout, new MyListener());
    }

    // stopSearch
    public void H02001() {
        My02stopSearch();
    }

    public void H02002() {
        int timeout = 60;
        My01searchCard(timeout, new MyListener());
        Message msg = new Message();
        msg.getData().putString("msg", "Pls wait ten seconds！");
        handler.sendMessage(msg);
        stopSearchThread();
    }

    public void H02003() {
        for (int i = 0; i < 10 ; i++) {
            H02002();
            try {
                Thread.sleep(15000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            if (i == 9){
                Message msg = new Message();
                msg.getData().putString("msg", "H02002 alredy excuted 10 times！");
                handler.sendMessage(msg);
            }
        }
    }

    public void H02005() {
        int timeout = 60;
        My01searchCard(timeout, new MyListener());
        My02stopSearch();
        My02stopSearch();
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        logUtils.addCaseLog("Excute " + name + " cases");
        try {
            Class aClass = Class.forName("moudles.MagCardReaderMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    class MyListener extends MagCardListener.Stub {
        @Override
        public void onError(int error, String message) throws RemoteException {
            Message msg = new Message();
            msg.getData().putString("msg", "Card swipe failure, error code:" + error + '(' + message + ')');
            handler.sendMessage(msg);
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

            msg.getData().putString("msg", "Swipe card successfully" + "\n" +
                    "PAN:" + pan + "\n" +
                    "TRACK1:" + track1 + "\n" +
                    "TRACK2:" + track2 + "\n" +
                    "TRACK3:" + track3 + "\n" +
                    "SERVICE_CODE:" + serviceCode + "\n" +
                    "EXPIRED_DATE:" + expiredDate + "\n");

            handler.sendMessage(msg);
        }

        @Override
        public void onTimeout() throws RemoteException {
            Message msg = new Message();
            msg.getData().putString("msg", "Swipe the timeout");
            handler.sendMessage(msg);
        }
    }

    public void showCaseInfo(String name) {
        logUtils.printCaseInfo(name);
    }

    public void H03_AUTO() {

        try {
            //通过类文件获取类对象
            Class aClass = Class.forName("moudles.DeviceBtMoudle");
            this.printMsgTool("------Magcard----");
            this.printMsgTool("The automated test case starts execution");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                String name = method.getName();
                if(!name.startsWith("H")){
                    continue;
                }
                int index = Integer.valueOf(name.substring(1, 3));
                if(index >= 0 && !method.getName().equals("H03_AUTO")){
                    //if (method.getName().startsWith("F") && !method.getName().equals("F36_AUTO")) {
                    this.printMsgTool(method.getName() + "Method starts executing......");
                    method.invoke(this);
                    this.printMsgTool(method.getName() + "Method end execution！！！");
//                    SystemClock.sleep(5000);
                    Thread.sleep(1000);
                }
            }
            this.printMsgTool("The automated test case has been executed");
            logUtils.addCaseLog("The automated test case has been executed");
//            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D08018();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printMsgTool(String msg){
        if(null == msg || "null".equals(msg) || msg.contains("null")){
            msg = "Result：";
        }
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
