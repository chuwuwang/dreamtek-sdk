package testtools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;

import java.util.List;

import Utils.LogUtil;


public class MessageService extends Service {
    private final String TAG = "MessageService";
    private WifiCommImpl comm;
//    private Handler handler = new Handler();
    private Thread thread;
    private StringBuffer stringBuffer = new StringBuffer();



    public static void start(Context context) {
        context.startService(new Intent(context, MessageService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "MessageService onCreate()");
        initComm();
    }

    public synchronized void initComm() {
        startListenPaymentMessage();
        LogUtil.d(TAG, "call initComm finished");
    }

    public void startListenPaymentMessage() {
        comm = new WifiCommImpl(17538, 9999);
        if (comm != null) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    comm.init();
                    comm.listenAndRead(new WifiCommImpl.MsgCallback() {
                        @Override
                        public void onReceive(byte[] msg) {
                            if (msg != null) {
                                stringBuffer.append(new String(msg));
                            } else {
                                System.out.println("服务器收到：" + stringBuffer.toString());

                                try {
                                    List<Testcase> testcases = JSON.parseArray(stringBuffer.toString(), Testcase.class);
                                    for (Testcase testcase: testcases) {
                                        LogUtil.d(TAG, testcase.toString());
                                    }
                                    if (MessageCenter.getInstance().getTestCaseListener() != null) {
                                        MessageCenter.getInstance().getTestCaseListener().onReceived(testcases);
                                    } else {
                                        ToastUtil.showToastLong("收到新测试案例！请先到NEW TEST界面并在PC重新发送案例！");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                stringBuffer.setLength(0);
                            }
                        }
                    });
                }
            });
            thread.start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
//        return handler;
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (comm != null) {
            comm.stopListen();
        }
        LogUtil.d(TAG, "MessageService onDestroy()");
    }

//    public class Handler extends IMsgService.Stub {
//        @Override
//        public void reinitializeComm() {
//            LogUtil.d(TAG, "call reinitializeComm");
//            if (comm != null) {
//                comm.stopListen();
//            }
//
//            initComm();
//        }
//
//        @Override
//        public void changeMaxSingleFrameLen(int length) throws RemoteException {
//            LogUtil.d(TAG, "changeMaxSingleFrameLen=" + length);
//            if (comm != null) {
//                comm.setMaxSingleFrameLength(length);
//            }
//        }
//    }

}
