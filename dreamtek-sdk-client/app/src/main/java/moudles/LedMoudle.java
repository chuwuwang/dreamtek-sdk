package moudles;

/**
 * Created by WenpengL1 on 2016/12/27.
 */

import android.content.Context;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.ILed;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Utils.LogUtil;
import Utils.LogUtils;
import base.MyApplication;

//import com.socsi.utils.FileUtils;

/**
 * led灯控制模块
 */
public class LedMoudle {
    Context context;
    ILed iledDriver;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> turnOn = new ArrayList<String>();
    ArrayList<String> turnOff = new ArrayList<String>();
    ArrayList<String> ledControl = new ArrayList<String>();
    ArrayList<String> autoLed = new ArrayList<String>();

    public LedMoudle(Context context, ILed iledDriver) {
        this.context = context;
        logUtils = MyApplication.logUtils;
        this.iledDriver = iledDriver;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.LedMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "C01":
                            turnOn.add(i.getName());
                            break;
                        case "C02":
                            turnOff.add(i.getName());
                            break;
                        case "C03":
                            ledControl.add(i.getName());
                            break;
                        case "C04":
                            autoLed.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(turnOn);
            caseNames.add(turnOff);
            caseNames.add(ledControl);
            caseNames.add(autoLed);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void C01001() {
        My01turnOn(1);
    }

    public void C01002() {
        My01turnOn(2);
    }

    public void C01003() {
        My01turnOn(3);
    }

    public void C01004() {
        My01turnOn(4);
    }

    public void C01005() {
        My01turnOn(0);
    }

    public void C01006() {
        My01turnOn(5);
    }

    public void C01007() {
        for (int i = 0;i<10;i++){
            C01001();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C01002();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C01003();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C01004();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void C01008() {
        My01turnOn(1);
    }

    public void C01009() {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().D03028();
    }

//    public void C01010() {
//        ((MyApplication) context).serviceMoudle.getScanBtMoudle().E01004();
//    }

    public void C01011() {
        ((MyApplication) context).serviceMoudle.getBeerMoudle().B01006();
    }

    public void C01013() throws InterruptedException {

        C01001();
//        this.printLedInfo(2, 1);
//        this.printLedInfo(3, 1);
//        this.printLedInfo(4, 1);
        new Thread() {
        public void run() {
            try {

//                Thread.sleep(5000);
                My01turnOn(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            My01turnOn(1);
        }
    }.start();

        new Thread() {
            public void run() {
                try {

//                Thread.sleep(5000);
                    My01turnOn(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            My01turnOn(1);
            }
        }.start();

        new Thread() {
            public void run() {
                try {

//                Thread.sleep(5000);
                    My01turnOn(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            My01turnOn(1);
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
}

    public void C01014() throws InterruptedException {

        C01001();
        this.printLedInfo(3, 1);
        this.printLedInfo(4, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {

                    My01turnOn(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }

    public void C01015() throws InterruptedException {

        C01001();
        this.printLedInfo(2, 1);
        this.printLedInfo(3, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {

                    My01turnOn(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }

    public void C01016() throws InterruptedException {

        C01002();
        this.printLedInfo(3, 1);
        this.printLedInfo(4, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {

                    My01turnOn(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();

    }


    public void C01017() throws InterruptedException {

        C01001();
        this.printLedInfo(2, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }

    public void C01018() throws InterruptedException {

        C01001();
        this.printLedInfo(3, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }

    public void C01019() throws InterruptedException {

        C01001();
        this.printLedInfo(4, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }

    public void C01020() throws InterruptedException {

        C01002();
        this.printLedInfo(3, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }

    public void C01021() throws InterruptedException {

        C01002();
        this.printLedInfo(4, 1);
        new Thread() {
            public void run() {
                try {

                    My01turnOn(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(2000);
        C02001();
        C02002();
        C02003();
        C02004();
    }


    public void C02001() {
        My02turnOff(1);
    }

    public void C02002() {
        My02turnOff(2);
    }

    public void C02003() {
        My02turnOff(3);
    }

    public void C02004() {
        My02turnOff(4);
    }

    public void C02005() {
        My02turnOff(1);
    }

    public void C02006() {
        My02turnOff(0);
    }

    public void C02007() {
        My02turnOff(5);
    }

    public void C02008() {
        for (int i = 0;i < 10;i++){
            C01001();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C01002();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C01003();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C01004();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C02001();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C02002();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C02003();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C02004();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void C02009() throws InterruptedException {

        C01013();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void C02010() throws InterruptedException {

        C01014();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void C02011() throws InterruptedException {

        C01015();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void C02012() throws InterruptedException {

        C01016();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void C02013() throws InterruptedException {

        C01017();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void C02014() throws InterruptedException {

        C01018();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void C02015() throws InterruptedException {

        C01019();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void C02016() throws InterruptedException {

        C01020();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void C02017() throws InterruptedException {

        C01021();

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    My02turnOff(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void C03001() {
        byte status = 1;
        byte led = 0x01;
        My03ledControl(led,status);
    }

    public void C03002() {

        byte status = 1;
        byte led = 0x02;
        My03ledControl(led,status);
    }

    public void C03003() {

        byte status = 1;
        byte led = 0x04;
        My03ledControl(led,status);
    }

    public void C03004() {

        byte status = 1;
        byte led = 0x08;
        My03ledControl(led,status);
    }

    public void C03005() {

        byte status = 0;
        byte led = 0x01;
        My03ledControl(led,status);
    }

    public void C03006() {

        byte status = 0;
        byte led = 0x02;
        My03ledControl(led,status);
    }
    public void C03007() {

        byte status = 0;
        byte led = 0x04;
        My03ledControl(led,status);
    }
    public void C03008() {

        byte status = 0;
        byte led = 0x08;
        My03ledControl(led,status);
    }
    public void C03009() {


            byte status = 1;
            byte led = 0x00;
            My03ledControl(led,status);
    }
    public void C03010() {


        byte status = 1;
        byte led = 0x05;
        My03ledControl(led,status);
    }
    public void C03011() {


        byte status = -1;
        byte led = 0x01;
        My03ledControl(led,status);
    }
    public void C03012() {


        byte status = 2;
        byte led = 0x01;
        My03ledControl(led,status);
    }
    public void C03013() {
        byte status = 1;
        byte led = 0x01;
        My03ledControl(led,status);
    }
    public void C03014() {
        for (int i = 0;i < 10;i++){
            C03001();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03002();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03003();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03004();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03005();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03006();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03007();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            C03008();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public void C03015() {
        C03001();
        byte status = 1;
        byte led = 0x01;
        My03ledControl(led,status);
    }
       public void C03016() {
        C03002();
        byte status = 1;
        byte led = 0x02;
        My03ledControl(led,status);
    }
    public void C03017() {
        C03003();
        byte status = 1;
        byte led = 0x04;
        My03ledControl(led,status);
    }

    public void C03018() {
        C03004();
        byte status = 1;
        byte led = 0x08;
        My03ledControl(led,status);
    }
    public void C03019() {
        C03005();
        byte status = 0;
        byte led = 0x01;
        My03ledControl(led,status);
    }
    public void C03020() {
        C03002();
        byte status = 0;
        byte led = 0x02;
        My03ledControl(led,status);
    }
    public void C03021() {
        C03003();
        byte status = 0;
        byte led = 0x04;
        My03ledControl(led,status);
    }

    public void C03022() {
        C03004();
        byte status = 0;
        byte led = 0x08;
        My03ledControl(led,status);
    }

    public void C03023() {
        C03001();
        logUtils.addCaseLog("ThreadName：" + Thread.currentThread().getName());
        new Thread() {
            public void run() {
                try {
                    logUtils.addCaseLog("ThreadName：" + Thread.currentThread().getName());
                    String[] strs = new String[1];
                    strs[2]= "1";
                    byte status = 1;
                    byte led = 0x02;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    logUtils.addCaseLog("ThreadName：" + Thread.currentThread().getName() + "，error：" + e);
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    byte status = 1;
                    byte led = 0x04;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    byte status = 1;
                    byte led = 0x08;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    public void C03024() {
        C03023();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    byte status = 0;
                    byte led = 0x01;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    byte status = 0;
                    byte led = 0x02;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    byte status = 0;
                    byte led = 0x04;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    byte status = 0;
                    byte led = 0x08;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void C03025() {
        C03023();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    byte status = 0;
                    byte led = 0x02;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    byte status = 0;
                    byte led = 0x04;
                    My03ledControl(led,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }



    public void My01turnOn(int led) {
        try {
            logUtils.addCaseLog("turnOn execute");
            iledDriver.turnOn(led);
//            this.printLedInfo(led, 1);
        } catch (RemoteException e) {
            logUtils.addCaseLog("turnOn Perform abnormal");
            e.printStackTrace();
        }
    }

    private void printLedInfo(int led, int status){
        String msg = "";
        if(led == 1){
            msg = "Execution result: blue light";
        } else if(led == 2){
            msg = "Execution result: yellow light";
        } else if(led == 3){
            msg = "Execution result: green light";
        } else if(led == 4){
            msg = "Execution result: red light";
        }

        if(status == 1){
            this.printMsgTool(msg + "Light up");
        } else if(status == 0){
            this.printMsgTool(msg + "Put out");
        } else if(status == 2){
            this.printMsgTool(msg + "flashing");
        }
    }

    public void My02turnOff(int led) {
        try {
            logUtils.addCaseLog("turnOff execute");
            iledDriver.turnOff(led);
            this.printLedInfo(led, 0);
        } catch (RemoteException e) {
            logUtils.addCaseLog("turnOff Perform abnormal");
            e.printStackTrace();
        }
    }

    public void My03ledControl(byte led, byte status) {
        try {
            logUtils.addCaseLog("ledControl execute");
            iledDriver.ledControl(led,  status);
            this.printLedInfo(led, status);
        } catch (RemoteException e) {
            logUtils.addCaseLog("ledControl execute abnormal");
            e.printStackTrace();
        }
    }
    public void My04autoLed(byte led, byte status) {
        try {
            logUtils.addCaseLog("ledControl execute");
            iledDriver.ledControl(led,  status);
        } catch (RemoteException e) {
            logUtils.addCaseLog("ledControl execute abnormal");
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
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.LedMoudle");
            Method method = aClass.getMethod(name);
            LogUtil.d("TAG", "---> name:" + method.getName());
            method.invoke(this);
            logUtils.addCaseLog(name + "Execute the case");
            logUtils.addCaseLog(name + "Execute the case");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showTheCaseInfo(int groupPosition, int childPosition)
    {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    public void C04_AUTO() {

        try {
            //通过类文件获取类对象
            Class aClass = Class.forName("moudles.LedMoudle");
            this.printMsgTool("-------led moudle------");
            this.printMsgTool("Automated test cases begin execute");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("C") && !method.getName().equals("C04_AUTO")) {
                    this.printMsgTool(method.getName() + "Methods to execute......");
                    method.invoke(this);
                    this.printMsgTool(method.getName() + "Methods the end execute！！！");
                    Thread.sleep(1000);
                }
            }
            this.printMsgTool("The automated test case EXECUTE completes");
            logUtils.addCaseLog("The automated test case EXECUTE completes");
            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D08018();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printMsgTool(String msg){
        //((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File txtFile = new File("E:\\test_log\\LedMoudle_" + sdf.format(new Date()).substring(0, 9) + ".txt");
        if(!txtFile.exists()){
            txtFile.mkdirs();
        }
        try {
            PrintWriter pw = new PrintWriter(txtFile);
            pw.println(msg);
            pw.close();

            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}