package moudles;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.verifone.smartpos.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;

public class SerialPortMoudle {
    Context context;
    ISerialPort iSerialport;
    ISerialPort iSerialPortNew;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> open = new ArrayList<String>();
    ArrayList<String> close = new ArrayList<String>();
    ArrayList<String> init = new ArrayList<String>();
    ArrayList<String> read = new ArrayList<String>();
    ArrayList<String> write = new ArrayList<String>();
    ArrayList<String> clearInputBuffer = new ArrayList<String>();
    ArrayList<String> isBufferEmpty = new ArrayList<String>();


    // Constructor: perform initialization.
    public SerialPortMoudle(Context context, ISerialPort iSerialport, ISerialPort iSerialPortNew) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iSerialport = iSerialport;
        this.iSerialPortNew = iSerialPortNew;
        addAllapi();
    }


    // Add all cases to the corresponding method category; case name = method name under test.
    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.SerialPortMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "G01":
                            open.add(i.getName());
                            break;
                        case "G02":
                            close.add(i.getName());
                            break;
                        case "G03":
                            init.add(i.getName());
                            break;
                        case "G04":
                            write.add(i.getName());
                            break;
                        case "G05":
                            read.add(i.getName());
                            break;
                        case "G06":
                            clearInputBuffer.add(i.getName());
                            break;
                        case "G07":
                            isBufferEmpty.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(open);
            caseNames.add(close);
            caseNames.add(init);
            caseNames.add(write);
            caseNames.add(read);
            caseNames.add(clearInputBuffer);
            caseNames.add(isBufferEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void G01001() {
        //test DB01
        MyApplication.serviceMoudle.setDeviceType1();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;

        boolean blRet = My01open();
        logUtils.addCaseLog("open returned:[" + blRet + "]");

        blRet = My03init(9600, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");

        int readBytes;
        byte[] readData = new byte[50];

        readBytes = My05read(readData, 50, 10000);
        logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned data:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G01002() {
        MyApplication.serviceMoudle.setDeviceType2();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        try {
            this.iSerialport.open();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void G01003() {
        MyApplication.serviceMoudle.setDeviceType3();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        try {
            this.iSerialport.open();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void G01005() {
        My01open();
        boolean blRet = My01open();

        logUtils.addCaseLog("open returned:[" + blRet + "]");
    }

    public void G01006() {
        MyApplication.serviceMoudle.setDeviceType4();//wireless-rs232
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        if (iSerialport!=null){
            try {
                boolean blRet = this.iSerialport.open();
                logUtils.addCaseLog("open returned:[" + blRet + "]");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            logUtils.addCaseLog("iSerialPort = "+null);
        }

    }

    public void G01007() {
        MyApplication.serviceMoudle.setDeviceType5();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        try {
            this.iSerialport.open();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01008() {
        MyApplication.serviceMoudle.setDeviceType6();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        try {
            boolean blRet = this.iSerialport.open();
            logUtils.addCaseLog("usb-rs232, open returned:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01009() {
        MyApplication.serviceMoudle.setDeviceType7();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        logUtils.addCaseLog("pedestal-rs232, open returned:[" + blRet + "]");
    }

    public void G01010() {
        MyApplication.serviceMoudle.setDeviceType8();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        logUtils.addCaseLog("pedestal-pinpad, open returned:[" + blRet + "]");
    }

    public void G01011() {
        //DX16 typeA
        MyApplication.serviceMoudle.setDeviceType9();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        logUtils.addCaseLog("used for DX16 typeA, counterTop-usb2dx16. open returned:[" + blRet + "]");
    }


    public void G01012() {

        MyApplication.serviceMoudle.setNewDeviceType1();
        this.iSerialPortNew = MyApplication.serviceMoudle.iSerialPortNew;
        try {
            boolean blRet = iSerialPortNew.open();
            logUtils.addCaseLog("pedestal-pinpad, open returned:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01013() {

        MyApplication.serviceMoudle.setNewDeviceType2();
        this.iSerialPortNew = MyApplication.serviceMoudle.iSerialPortNew;
        try {
            boolean blRet = iSerialPortNew.open();
            logUtils.addCaseLog("pedestal-rs232, open returned:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01014() {

        MyApplication.serviceMoudle.setNewDeviceType3();
        this.iSerialPortNew = MyApplication.serviceMoudle.iSerialPortNew;
        try {
            boolean blRet = iSerialPortNew.open();
            logUtils.addCaseLog("counterTop-usb2dx16, open returned:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01015() {

        MyApplication.serviceMoudle.setDeviceType10();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        try {
            boolean blRet = iSerialport.open();
            logUtils.addCaseLog("usb-rs232-pinpad, open returned:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01016() {
        //ux
        MyApplication.serviceMoudle.setDeviceType11();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        Log.d("TAG", "used for ux com1, ux. open returned:[" + blRet + "]");
        logUtils.addCaseLog("used for ux com1, ux. open returned:[" + blRet + "]");
    }

    public void G01017() {
        //ux
        MyApplication.serviceMoudle.setDeviceType12();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        Log.d("TAG", "used for ux com2, ux. open returned:[" + blRet + "]");
        logUtils.addCaseLog("used for ux com2, ux. open returned:[" + blRet + "]");
    }
    public void G01018() {
        //usb2rs232-VID-PID
        MyApplication.serviceMoudle.setDeviceType13();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        Log.d("TAG", "used for usb2rs232-VID-PID. open returned:[" + blRet + "]");
        logUtils.addCaseLog("used for usb2rs232-VID-PID. open returned:[" + blRet + "]");
    }


    public void G02001() {
        boolean blRet;
        blRet = My02close();
        logUtils.addCaseLog("close returned:[" + blRet + "]");
    }

    public void G02002() {
        G01001();
        boolean blRet;
        blRet = My02close();
        logUtils.addCaseLog("close returned:[" + blRet + "]");
    }

    public void G02003() {
        boolean blRet;
        try {
            blRet = iSerialPortNew.close();
            logUtils.addCaseLog("close() executed blRet=" + blRet);
        } catch (RemoteException e) {
            logUtils.addCaseLog("close() failed with an exception");
            throw new RuntimeException(e);
        }
    }

    public void G02004() {
        G01010();
        boolean blRet;
        try {
            blRet = iSerialPortNew.close();
            logUtils.addCaseLog("close() executed blRet=" + blRet);
        } catch (RemoteException e) {
            logUtils.addCaseLog("close() failed with an exception");
            throw new RuntimeException(e);
        }
    }


    public void G03001() {
        boolean blRet;
        My01open();

        blRet = My03init(115200, 0, 8);

        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03002() {
        boolean blRet;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03003() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 2, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03004() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 1, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03005() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 2, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03006() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 1, 6);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03007() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03008() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 3, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03009() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 1, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03010() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03011() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 2, 6);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03012() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03013() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 2, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03014() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 1, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03015() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 2, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03016() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 0, 6);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03017() {
        boolean blRet;
//        My01open();

        blRet = My03init(4800, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03018() {
        boolean blRet;
//        My01open();

        blRet = My03init(50, 1, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03019() {
        boolean blRet;
//        My01open();

        blRet = My03init(14400, 2, 6);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03020() {
        boolean blRet;
//        My01open();

        blRet = My03init(1200, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03021() {
        boolean blRet;
//        My01open();

        blRet = My03init(1800, 0, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03022() {
        boolean blRet;
//        My01open();

        blRet = My03init(2400, 0, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03023() {
        boolean blRet;
//        My01open();

        blRet = My03init(4800, 2, 6);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03024() {
        boolean blRet;
//        My01open();

        blRet = My03init(4000000, 0, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03025() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, -1, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03026() {
        boolean blRet;
//        My01open();

        blRet = My03init(38400, 1, 5);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03027() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 0, 10);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03028() {
        boolean blRet;
//        My01open();
        blRet = My03init(9600, 0, -1);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03029() {
        boolean blRet;
//        My01open();
        blRet = My03init(28800, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03030() {
        boolean blRet;
//        My01open();
        blRet = My03init(115200, 0, 9);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03031() {
        boolean blRet;
//        My01open();
        blRet = My03init(57600, 2, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03032() {
        boolean blRet;
//        My01open();
        blRet = My03init(115200, 0, 4);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }


    public void G03033() {
        boolean blRet;
//        My01open();
        blRet = My03init(57600, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03034() {
        boolean blRet;
//        My01open();
        blRet = My03init(38400, 0, 8);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03035() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 2, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }

    public void G03036() {
        try {
            logUtils.addCaseLog("bps=" + 115200 + ", par=" + 0 + ", dbs=" + 8);
            boolean blRet = iSerialPortNew.init(115200, 0, 8);
            logUtils.addCaseLog("init returned:[" + blRet + "]");

        } catch (RemoteException e) {
            logUtils.addCaseLog("init() failed with an exception");
            e.printStackTrace();
        }
    }

    public void G03037() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 1, 7);
        logUtils.addCaseLog("init returned:[" + blRet + "]");
    }


    public void G04001() {
        boolean blRet;
        int writtenBytes;

//        blRet = My03init(115200, 0, 8);


        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04002() {
        boolean blRet;
        int writtenBytes;

        My02close();

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04003() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04006() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 0);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04007() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, -1);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04008() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, Integer.MAX_VALUE);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04009() {
        boolean blRet;
        int writtenBytes;

//        blRet = My03init(115200, 0, 8);


        writtenBytes = My04write(null, 500);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04010() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[0], 500);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04011() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[1024], 5);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04012() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");
        byte[] data = new byte[4096];
        for (int i = 0; i < 4096; i++) {
            data[i] = 0x31;
        }

        writtenBytes = My04write(data, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04013() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");
        byte[] data = new byte[4097];
        for (int i = 0; i < 4096; i++) {
            data[i] = 0x31;
        }
        data[4096] = 0x11;

        writtenBytes = My04write(data, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04014() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[40967], 60000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04015() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[40967], 3600000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04016() {
        boolean blRet;
        int writtenBytes;

        writtenBytes = My04write(new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0}, 1000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
        SystemClock.sleep(100);

        writtenBytes = My04write(new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0}, 1000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
        SystemClock.sleep(100);

        writtenBytes = My04write(new byte[]{(byte) 0x98, 0x76, 0x54, 0x32, (byte) 0x10, (byte) 0x2F, (byte) 0x2E, (byte) 0x2D}, 1000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04017() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        writtenBytes = My04write(new byte[40967], 3000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
    }

    public void G04018() {

        try {
            int writtenBytes = iSerialport.write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 3000,null);
            logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G04019() {

        try {
            int writtenBytes = iSerialPortNew.write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 3000,null);
            logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public void G05001() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

//        blRet = My03init(115200, 0, 8);



        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned data:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05002() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[1024];


        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 1024, 60000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05003() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[2048];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 2048, 60000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05004() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, 60000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05005() {
        G05004();
    }

    public void G05006() {
        G05004();
    }

    public void G05007() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05008() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, 0);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05009() {
        G05008();
    }

    public void G05010() {
        G05008();
    }

    public void G05011() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, -1);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05012() {
        My02close();

        int readBytes;
        byte[] readData = new byte[100];

        readBytes = My05read(readData, 100, -1);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05016() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(null, 100, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05017() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[0];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05018() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 99, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05019() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 101, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05020() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 0, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05021() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, -1, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05022() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[40967];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 40967, 60000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
    }

    public void G05023() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    this.sleep(3000);
                    My03init(115200, 0, 8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void G05024() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    this.sleep(3000);
                    My02close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void G05025() {
        boolean blRet;
        int readBytes;

        byte[] readData = new byte[32];


        int writtenBytes;

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");
        this.printMsgTool("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("read() returned:[" + readData + "]");
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned data:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05026() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

        blRet = My03init(38400, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");
        this.printMsgTool("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned data:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05027() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

        blRet = My03init(57600, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");
        this.printMsgTool("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned data:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05028() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

        blRet = My03init(9600, 1, 7);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");
        this.printMsgTool("init() returned:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned:[" + readBytes + "]");
        this.printMsgTool("read() returned data:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05029() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int readBytes;
                    byte[] readData = new byte[32];
                    readBytes = My05read(readData, 32, 1000);
                    Log.d("TAG", "read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
                    Log.d("TAG", "read() returned:[" + readBytes + "]");

                }
            }
        }).start();

    }

    public void G05030() {

        int readBytes;
        byte[] readData = new byte[32];
        try {
            readBytes = iSerialport.read(readData, 32, 10000);
            logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
            logUtils.addCaseLog("read() returned:[" + readBytes + "]");
            this.printMsgTool("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
            this.printMsgTool("read() returned:[" + readBytes + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void G05031() {

        int readBytes;
        byte[] readData = new byte[32];
        try {
            readBytes = iSerialPortNew.read(readData, 32, 10000);
            logUtils.addCaseLog("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
            logUtils.addCaseLog("read() returned:[" + readBytes + "]");
            this.printMsgTool("read() returned:[" + StringUtil.byte2HexStr(readData) + "]");
            this.printMsgTool("read() returned:[" + readBytes + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }


    public void G06001() {
        boolean blRet;

        blRet = My02close();
        logUtils.addCaseLog("close() returned:[" + blRet + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("My06clearInputBuffer() returned:[" + blRet + "]");
    }

    public void G06002() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");


        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("read() returned:[" + recvBytes + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("My06clearInputBuffer() returned:[" + blRet + "]");
    }

    public void G06003() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

//        blRet = My01open();

//
//        blRet = My03init(115200, 0, 8);


        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("My06clearInputBuffer() returned:[" + blRet + "]");


        recvBytes = My05read(recvData, 4, 2000);
        logUtils.addCaseLog("read() returned:[" + recvBytes + "]");
    }

    public void G06004() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");


        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("read() returned:[" + recvBytes + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("My06clearInputBuffer() returned:[" + blRet + "]");


        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("read() returned:[" + recvBytes + "]");
    }

    public void G06005() {
        boolean blRet = My06clearInputBuffer();
        logUtils.addCaseLog("Cleared the iSerialPort buffer, blRet = " + blRet);
    }


    public void G07001() {
        boolean blRet;

        blRet = My02close();
        logUtils.addCaseLog("close() returned:[" + blRet + "]");

        blRet = My07isBufferEmpty(true);
        logUtils.addCaseLog("isBufferEmpty(true) returned:[" + blRet + "]");
    }

    public void G07002() {
        boolean blRet;

        blRet = My02close();
        logUtils.addCaseLog("close() returned:[" + blRet + "]");

        blRet = My07isBufferEmpty(false);
        logUtils.addCaseLog("isBufferEmpty(false) returned:[" + blRet + "]");
    }

    public void G07003() {
        boolean blRet;

//        blRet = My01open();

//

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");


        blRet = My07isBufferEmpty(true);
        logUtils.addCaseLog("isBufferEmpty(true) returned:[" + blRet + "]");
    }

    public void G07006() {
        boolean blRet;

//        blRet = My01open();

//

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");


        blRet = My07isBufferEmpty(false);
        logUtils.addCaseLog("isBufferEmpty(false) returned:[" + blRet + "]");
    }


    public void G07004() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

//        blRet = My01open();

//

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");



        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("read() returned:[" + recvBytes + "]");

        blRet = My07isBufferEmpty(true);
        logUtils.addCaseLog("isBufferEmpty(true) returned:[" + blRet + "]");
    }

    public void G07005() {
        boolean blRet;
        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init() returned:[" + blRet + "]");

        int writtenBytes;

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("write() returned:[" + writtenBytes + "]");

        blRet = My07isBufferEmpty(false);
        logUtils.addCaseLog("isBufferEmpty(false) returned:[" + blRet + "]");
    }


    private boolean My01open() {
        try {
            logUtils.addCaseLog("open() executed");
            return iSerialport.open();
        } catch (RemoteException e) {
            logUtils.addCaseLog("open() failed with an exception");
            e.printStackTrace();
            return false;
        }
    }

    private boolean My02close() {
        try {
            logUtils.addCaseLog("close() executed");
            return iSerialport.close();
        } catch (RemoteException e) {
            logUtils.addCaseLog("close() failed with an exception");
            e.printStackTrace();
            return false;
        }
    }

    private boolean My03init(int bps, int par, int dbs) {
        try {
            logUtils.addCaseLog("init() executed");
            logUtils.addCaseLog("bps=" + bps + ", par=" + par + ", dbs=" + dbs);
            return iSerialport.init(bps, par, dbs);
        } catch (RemoteException e) {
            logUtils.addCaseLog("init() failed with an exception");
            e.printStackTrace();
            return false;
        }
    }

    private int My04write(byte[] data, int timeout) {
        try {
            logUtils.addCaseLog("write() executed");
            //logUtils.addCaseLog("data len=" + data.length + ", timeout=" + timeout);
            return iSerialport.write(data, timeout,null);
        } catch (RemoteException e) {
            logUtils.addCaseLog("write() failed with an exception");
            e.printStackTrace();
            return 0;
        }
    }

    private int My05read(byte[] buffer, int expectLen, int timeout) {
        try {
            logUtils.addCaseLog("read() executed");
            return iSerialport.read(buffer, expectLen, timeout);
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read() failed with a null buffer: ");
            npe.printStackTrace();
            return -1;
        } catch (RemoteException e) {
            logUtils.addCaseLog("read() failed with an exception");
            e.printStackTrace();
            return -1;
        }
    }

    public int read(byte[] buffer, int expectLen, int timeout) {
        try {
            logUtils.addCaseLog("read() executed");
            return iSerialport.read(buffer, expectLen, timeout);
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read() failed with a null buffer: ");
            npe.printStackTrace();
            return -1;
        } catch (RemoteException e) {
            logUtils.addCaseLog("read() failed with an exception");
            e.printStackTrace();
            return -1;
        }
    }


    private boolean My06clearInputBuffer() {
        try {
            return iSerialport.clearInputBuffer();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean My07isBufferEmpty(boolean input) {
        try {
            return iSerialport.isBufferEmpty(input);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
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
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.SerialPortMoudle");
            Log.i("aClass.getMethod", name);
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "The test case is completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    private void printMsgTool(String msg) {
        if (null == msg || "null".equals(msg) || msg.contains("null")) {
            msg = "The execution result：";
        }
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}