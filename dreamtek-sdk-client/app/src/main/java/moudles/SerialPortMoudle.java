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


    //构造函数，做初始化操作
    public SerialPortMoudle(Context context, ISerialPort iSerialport, ISerialPort iSerialPortNew) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iSerialport = iSerialport;
        this.iSerialPortNew = iSerialPortNew;
        addAllapi();
    }


    //将所有案例添加到相应的方法分类下面，案例名称=相应的测试方法名。
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
        boolean blRet = My01open();
//
        logUtils.addCaseLog("open执行返回值:[" + blRet + "]");
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

        logUtils.addCaseLog("open执行返回值:[" + blRet + "]");
    }

    public void G01006() {
        MyApplication.serviceMoudle.setDeviceType4();//wireless-rs232
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        if (iSerialport!=null){
            try {
                boolean blRet = this.iSerialport.open();
                logUtils.addCaseLog("open执行返回值:[" + blRet + "]");
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
            logUtils.addCaseLog("usb-rs232，open执行返回值:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01009() {
        MyApplication.serviceMoudle.setDeviceType7();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        logUtils.addCaseLog("pedestal-rs232，open执行返回值:[" + blRet + "]");
    }

    public void G01010() {
        MyApplication.serviceMoudle.setDeviceType8();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        logUtils.addCaseLog("pedestal-pinpad，open执行返回值:[" + blRet + "]");
    }

    public void G01011() {
        //DX16 typeA
        MyApplication.serviceMoudle.setDeviceType9();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        logUtils.addCaseLog("used for DX16 typeA，counterTop-usb2dx16. open执行返回值:[" + blRet + "]");
    }


    public void G01012() {
        //open DX16的第二个串口
        MyApplication.serviceMoudle.setNewDeviceType1();
        this.iSerialPortNew = MyApplication.serviceMoudle.iSerialPortNew;
        try {
            boolean blRet = iSerialPortNew.open();
            logUtils.addCaseLog("pedestal-pinpad, open执行返回值:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01013() {
        //open DX16的第二个串口
        MyApplication.serviceMoudle.setNewDeviceType2();
        this.iSerialPortNew = MyApplication.serviceMoudle.iSerialPortNew;
        try {
            boolean blRet = iSerialPortNew.open();
            logUtils.addCaseLog("pedestal-rs232, open执行返回值:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01014() {
        //open DX16的第二个串口
        MyApplication.serviceMoudle.setNewDeviceType3();
        this.iSerialPortNew = MyApplication.serviceMoudle.iSerialPortNew;
        try {
            boolean blRet = iSerialPortNew.open();
            logUtils.addCaseLog("counterTop-usb2dx16, open执行返回值:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01015() {

        MyApplication.serviceMoudle.setDeviceType10();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        try {
            boolean blRet = iSerialport.open();
            logUtils.addCaseLog("usb-rs232-pinpad, open执行返回值:[" + blRet + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G01016() {
        //ux
        MyApplication.serviceMoudle.setDeviceType11();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        Log.d("TAG", "used for ux com1，ux. open执行返回值:[" + blRet + "]");
        logUtils.addCaseLog("used for ux com1，ux. open执行返回值:[" + blRet + "]");
    }

    public void G01017() {
        //ux
        MyApplication.serviceMoudle.setDeviceType12();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        Log.d("TAG", "used for ux com2，ux. open执行返回值:[" + blRet + "]");
        logUtils.addCaseLog("used for ux com2，ux. open执行返回值:[" + blRet + "]");
    }
    public void G01018() {
        //usb2rs232-VID-PID
        MyApplication.serviceMoudle.setDeviceType13();
        this.iSerialport = MyApplication.serviceMoudle.iSerialport;
        boolean blRet = My01open();
        Log.d("TAG", "used for usb2rs232-VID-PID. open执行返回值:[" + blRet + "]");
        logUtils.addCaseLog("used for usb2rs232-VID-PID. open执行返回值:[" + blRet + "]");
    }

    //以下是close()方法的测试案例
    public void G02001() {
        boolean blRet;
        blRet = My02close();
        logUtils.addCaseLog("close执行返回值:[" + blRet + "]");
    }

    public void G02002() {
        G01001();
        boolean blRet;
        blRet = My02close();
        logUtils.addCaseLog("close执行返回值:[" + blRet + "]");
    }

    public void G02003() {
        boolean blRet;
        try {
            blRet = iSerialPortNew.close();
            logUtils.addCaseLog("close()执行 blRet=" + blRet);
        } catch (RemoteException e) {
            logUtils.addCaseLog("close()执行异常");
            throw new RuntimeException(e);
        }
    }

    public void G02004() {
        G01010();
        boolean blRet;
        try {
            blRet = iSerialPortNew.close();
            logUtils.addCaseLog("close()执行 blRet=" + blRet);
        } catch (RemoteException e) {
            logUtils.addCaseLog("close()执行异常");
            throw new RuntimeException(e);
        }
    }

    //以下是init()方法的测试案例
    public void G03001() {
        boolean blRet;
        My01open();

        blRet = My03init(115200, 0, 8);

        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03002() {
        boolean blRet;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03003() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 2, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03004() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 1, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03005() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 2, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03006() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 1, 6);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03007() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03008() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 3, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03009() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 1, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03010() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03011() {
        boolean blRet;
//        My01open();

        blRet = My03init(19200, 2, 6);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03012() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03013() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 2, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03014() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 1, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03015() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 2, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03016() {
        boolean blRet;
//        My01open();

        blRet = My03init(9600, 0, 6);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03017() {
        boolean blRet;
//        My01open();

        blRet = My03init(4800, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03018() {
        boolean blRet;
//        My01open();

        blRet = My03init(50, 1, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03019() {
        boolean blRet;
//        My01open();

        blRet = My03init(14400, 2, 6);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03020() {
        boolean blRet;
//        My01open();

        blRet = My03init(1200, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03021() {
        boolean blRet;
//        My01open();

        blRet = My03init(1800, 0, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03022() {
        boolean blRet;
//        My01open();

        blRet = My03init(2400, 0, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03023() {
        boolean blRet;
//        My01open();

        blRet = My03init(4800, 2, 6);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03024() {
        boolean blRet;
//        My01open();

        blRet = My03init(4000000, 0, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03025() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, -1, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03026() {
        boolean blRet;
//        My01open();

        blRet = My03init(38400, 1, 5);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03027() {
        boolean blRet;
//        My01open();

        blRet = My03init(115200, 0, 10);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03028() {
        boolean blRet;
//        My01open();
        blRet = My03init(9600, 0, -1);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03029() {
        boolean blRet;
//        My01open();
        blRet = My03init(28800, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03030() {
        boolean blRet;
//        My01open();
        blRet = My03init(115200, 0, 9);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03031() {
        boolean blRet;
//        My01open();
        blRet = My03init(57600, 2, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03032() {
        boolean blRet;
//        My01open();
        blRet = My03init(115200, 0, 4);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }


    public void G03033() {
        boolean blRet;
//        My01open();
        blRet = My03init(57600, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03034() {
        boolean blRet;
//        My01open();
        blRet = My03init(38400, 0, 8);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03035() {
        boolean blRet;
//        My01open();
        //波特率9600，数据位7，校验位2-Even
        blRet = My03init(9600, 2, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    public void G03036() {
        try {
            logUtils.addCaseLog("bps=" + 115200 + ", par=" + 0 + ", dbs=" + 8);
            boolean blRet = iSerialPortNew.init(115200, 0, 8);
            logUtils.addCaseLog("init执行返回值:[" + blRet + "]");

        } catch (RemoteException e) {
            logUtils.addCaseLog("init()执行异常");
            e.printStackTrace();
        }
    }

    public void G03037() {
        boolean blRet;
//        My01open();
        //波特率9600，数据位7，校验位1-Odd
        blRet = My03init(9600, 1, 7);
        logUtils.addCaseLog("init执行返回值:[" + blRet + "]");
    }

    //以下是write()方法的测试案例
    public void G04001() {
        boolean blRet;
        int writtenBytes;

//        blRet = My03init(115200, 0, 8);
//        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04002() {
        boolean blRet;
        int writtenBytes;

        My02close();

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04003() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04006() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 0);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04007() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, -1);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04008() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, Integer.MAX_VALUE);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04009() {
        boolean blRet;
        int writtenBytes;

//        blRet = My03init(115200, 0, 8);
//        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(null, 500);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04010() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[0], 500);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04011() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[1024], 5);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04012() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
        byte[] data = new byte[4096];
        for (int i = 0; i < 4096; i++) {
            data[i] = 0x31;
        }

        writtenBytes = My04write(data, 5000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04013() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
        byte[] data = new byte[4097];
        for (int i = 0; i < 4096; i++) {
            data[i] = 0x31;
        }
        data[4096] = 0x11;

        writtenBytes = My04write(data, 5000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04014() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[40967], 60000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04015() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[40967], 3600000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04016() {
        boolean blRet;
        int writtenBytes;

        writtenBytes = My04write(new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0}, 1000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
        SystemClock.sleep(100);

        writtenBytes = My04write(new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0}, 1000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
        SystemClock.sleep(100);

        writtenBytes = My04write(new byte[]{(byte) 0x98, 0x76, 0x54, 0x32, (byte) 0x10, (byte) 0x2F, (byte) 0x2E, (byte) 0x2D}, 1000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04017() {
        boolean blRet;
        int writtenBytes;

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        writtenBytes = My04write(new byte[40967], 3000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
    }

    public void G04018() {
        //DX16的双串口测试，直接write，看关闭某一个串口另外一个是否能正常write
        try {
            int writtenBytes = iSerialport.write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 3000,null);
            logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void G04019() {
        //DX16的双串口测试，直接write，看关闭某一个串口另外一个是否能正常write
        try {
            int writtenBytes = iSerialPortNew.write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 3000,null);
            logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //以下是read()方法的测试案例
    public void G05001() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

//        blRet = My03init(115200, 0, 8);
//        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
//        this.printMsgTool("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回数据:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05002() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[1024];


        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 1024, 60000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05003() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[2048];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 2048, 60000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05004() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, 60000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
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
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05008() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, 0);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
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
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, -1);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05012() {
        My02close();

        int readBytes;
        byte[] readData = new byte[100];

        readBytes = My05read(readData, 100, -1);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05016() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(null, 100, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05017() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[0];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05018() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 99, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05019() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 101, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05020() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 0, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05021() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, -1, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05022() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[40967];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 40967, 60000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
    }

    public void G05023() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
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
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 100, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
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
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
        this.printMsgTool("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + readData + "]");
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回数据:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05026() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

        blRet = My03init(38400, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
        this.printMsgTool("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回数据:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05027() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

        blRet = My03init(57600, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
        this.printMsgTool("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回数据:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05028() {
        boolean blRet;
        int readBytes;
        byte[] readData = new byte[32];

        blRet = My03init(9600, 1, 7);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");
        this.printMsgTool("执行init()返回值:[" + blRet + "]");

        readBytes = My05read(readData, 32, 10000);
        logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
        logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        this.printMsgTool("执行read()返回数据:[" + StringUtil.byte2HexStr(readData) + "]");
    }

    public void G05029() {
        //测试DX30 X990长时间休眠后，read数据是否成功
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int readBytes;
                    byte[] readData = new byte[32];
                    readBytes = My05read(readData, 32, 1000);
                    Log.d("TAG", "执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
                    Log.d("TAG", "执行read()返回值:[" + readBytes + "]");

                }
            }
        }).start();

    }

    public void G05030() {
        //DX16 第二个串口
        int readBytes;
        byte[] readData = new byte[32];
        try {
            readBytes = iSerialport.read(readData, 32, 10000);
            logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
            logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
            this.printMsgTool("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
            this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void G05031() {
        //DX16 第二个串口
        int readBytes;
        byte[] readData = new byte[32];
        try {
            readBytes = iSerialPortNew.read(readData, 32, 10000);
            logUtils.addCaseLog("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
            logUtils.addCaseLog("执行read()返回值:[" + readBytes + "]");
            this.printMsgTool("执行read()返回值:[" + StringUtil.byte2HexStr(readData) + "]");
            this.printMsgTool("执行read()返回值:[" + readBytes + "]");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    //以下是clearInputBuffer()方法的测试案例
    public void G06001() {
        boolean blRet;

        blRet = My02close();
        logUtils.addCaseLog("执行close()返回值:[" + blRet + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("执行My06clearInputBuffer()返回值:[" + blRet + "]");
    }

    public void G06002() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        //接收
        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("执行read()返回值:[" + recvBytes + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("执行My06clearInputBuffer()返回值:[" + blRet + "]");
    }

    public void G06003() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

//        blRet = My01open();
//        logUtils.addCaseLog("执行open()返回值:[" + blRet + "]");
//
//        blRet = My03init(115200, 0, 8);
//        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("执行My06clearInputBuffer()返回值:[" + blRet + "]");

        //接收
        recvBytes = My05read(recvData, 4, 2000);
        logUtils.addCaseLog("执行read()返回值:[" + recvBytes + "]");
    }

    public void G06004() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        //先接收2字节
        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("执行read()返回值:[" + recvBytes + "]");

        blRet = My06clearInputBuffer();
        logUtils.addCaseLog("执行My06clearInputBuffer()返回值:[" + blRet + "]");

        //再接收剩下的(最大100字节)
        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("执行read()返回值:[" + recvBytes + "]");
    }

    public void G06005() {
        boolean blRet = My06clearInputBuffer();
        logUtils.addCaseLog("清空iSerialPort缓存, blRet = " + blRet);
    }

    //以下是isBufferEmpty()方法的测试案例
    public void G07001() {
        boolean blRet;

        blRet = My02close();
        logUtils.addCaseLog("执行close()返回值:[" + blRet + "]");

        blRet = My07isBufferEmpty(true);
        logUtils.addCaseLog("执行isBufferEmpty(true)返回值:[" + blRet + "]");
    }

    public void G07002() {
        boolean blRet;

        blRet = My02close();
        logUtils.addCaseLog("执行close()返回值:[" + blRet + "]");

        blRet = My07isBufferEmpty(false);
        logUtils.addCaseLog("执行isBufferEmpty(false)返回值:[" + blRet + "]");
    }

    public void G07003() {
        boolean blRet;

//        blRet = My01open();
//        logUtils.addCaseLog("执行open()返回值:[" + blRet + "]");
//

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");


        blRet = My07isBufferEmpty(true);
        logUtils.addCaseLog("执行isBufferEmpty(true)返回值:[" + blRet + "]");
    }

    public void G07006() {
        boolean blRet;

//        blRet = My01open();
//        logUtils.addCaseLog("执行open()返回值:[" + blRet + "]");
//

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");


        blRet = My07isBufferEmpty(false);
        logUtils.addCaseLog("执行isBufferEmpty(false)返回值:[" + blRet + "]");
    }


    public void G07004() {
        boolean blRet;
        int recvBytes;
        byte[] recvData = new byte[100];

//        blRet = My01open();
//        logUtils.addCaseLog("执行open()返回值:[" + blRet + "]");
//

        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");


        //接收
        recvBytes = My05read(recvData, 4, 5000);
        logUtils.addCaseLog("执行read()返回值:[" + recvBytes + "]");

        blRet = My07isBufferEmpty(true);
        logUtils.addCaseLog("执行isBufferEmpty(true)返回值:[" + blRet + "]");
    }

    public void G07005() {
        boolean blRet;
        blRet = My03init(115200, 0, 8);
        logUtils.addCaseLog("执行init()返回值:[" + blRet + "]");

        int writtenBytes;

        writtenBytes = My04write(new byte[]{0x02, 0x00, 0x01, 0x1a, 0x03}, 5000);
        logUtils.addCaseLog("执行write()返回值:[" + writtenBytes + "]");

        blRet = My07isBufferEmpty(false);
        logUtils.addCaseLog("执行isBufferEmpty(false)返回值:[" + blRet + "]");
    }


    private boolean My01open() {
        try {
            logUtils.addCaseLog("open()执行");
            return iSerialport.open();
        } catch (RemoteException e) {
            logUtils.addCaseLog("open()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    private boolean My02close() {
        try {
            logUtils.addCaseLog("close()执行");
            return iSerialport.close();
        } catch (RemoteException e) {
            logUtils.addCaseLog("close()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    private boolean My03init(int bps, int par, int dbs) {
        try {
            logUtils.addCaseLog("init()执行");
            logUtils.addCaseLog("bps=" + bps + ", par=" + par + ", dbs=" + dbs);
            return iSerialport.init(bps, par, dbs);
        } catch (RemoteException e) {
            logUtils.addCaseLog("init()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    private int My04write(byte[] data, int timeout) {
        try {
            logUtils.addCaseLog("write()执行");
            //logUtils.addCaseLog("data len=" + data.length + ", timeout=" + timeout);
            return iSerialport.write(data, timeout,null);
        } catch (RemoteException e) {
            logUtils.addCaseLog("write()执行异常");
            e.printStackTrace();
            return 0;
        }
    }

    private int My05read(byte[] buffer, int expectLen, int timeout) {
        try {
            logUtils.addCaseLog("read()执行");
            return iSerialport.read(buffer, expectLen, timeout);
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read()执行异常-");
            npe.printStackTrace();
            return -1;
        } catch (RemoteException e) {
            logUtils.addCaseLog("read()执行异常");
            e.printStackTrace();
            return -1;
        }
    }

    public int read(byte[] buffer, int expectLen, int timeout) {
        try {
            logUtils.addCaseLog("read()执行");
            return iSerialport.read(buffer, expectLen, timeout);
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read()执行异常-");
            npe.printStackTrace();
            return -1;
        } catch (RemoteException e) {
            logUtils.addCaseLog("read()执行异常");
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
            logUtils.addCaseLog(name + "案例执行完毕");
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