package moudles;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;

public class UsbSerialPortMoudle {
    Context context;
    IUsbSerialPort usbSerialPort;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> isUsbSerialConnect = new ArrayList<String>();
    ArrayList<String> read = new ArrayList<String>();
    ArrayList<String> write = new ArrayList<String>();
    LogUtils logUtils;

    public UsbSerialPortMoudle(Context context, IUsbSerialPort iUsbSerialPort) {
        this.context = context;
        this.usbSerialPort = iUsbSerialPort;
        logUtils = MyApplication.serviceMoudle.logUtils;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.UsbSerialPortMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "W01":
                            isUsbSerialConnect.add(i.getName());
                            break;
                        case "W02":
                            read.add(i.getName());
                            break;
                        case "W03":
                            write.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(isUsbSerialConnect);
            caseNames.add(read);
            caseNames.add(write);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        try {
            Class aClass = Class.forName("moudles.UsbSerialPortMoudle");
            Method method = aClass.getDeclaredMethod(name);
            method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void W01001() {
        My01isUsbSerialConnect();
    }

    void W02001() {
        My02read();
    }

    void W03001() {
        My03write();
    }

    void W03002(){
        Runnable writeTask = new Runnable() {
            private volatile boolean running = My01isUsbSerialConnect();

            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(50);
                        My03write();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    running = My01isUsbSerialConnect();
                }
            }
        };
        Thread thread = new Thread(writeTask);
        thread.start();
    }


    public boolean My01isUsbSerialConnect() {
        Log.d("TAG", "isUsbSerialConnect execute");
        boolean result = false;
        try {
            result = usbSerialPort.isUsbSerialConnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

       logUtils.addCaseLog("****连接Usb设备:" + result);
        return result;
    }

    public int My02read() {
        try {
            byte[] readData = new byte[10];
            int num = usbSerialPort.read(readData, 5000);
            logUtils.addCaseLog("readData = "+StringUtil.byte2HexStr(readData));
            logUtils.addCaseLog( "读取数据:" + num);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int My03write() {

        byte[] writeData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        try {
            usbSerialPort.write(writeData);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
