package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.IBeeper;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Utils.LogUtils;
import base.MyApplication;

/**
 * Created by WenpengL1 on 2016/12/29.
 */

public class BeerMoudle {
    Context context;
    IBeeper iBeeper;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> startBeep = new ArrayList<String>();
    ArrayList<String> stopBeep = new ArrayList<String>();
    ArrayList<String> autoBeep = new ArrayList<String>();
    ArrayList<String> startBeepWithConfig = new ArrayList<String>();

    public BeerMoudle(Context context, IBeeper iBeeper) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iBeeper = iBeeper;
        addAllapi();
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.BeerMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i :methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "B01":
                            startBeep.add(i.getName());
                            break;
                        case "B02":
                            stopBeep.add(i.getName());
                            break;
                        case "B03":
                            autoBeep.add(i.getName());
                            break;
                        case "B04":
                            startBeepWithConfig.add(i.getName());
                    }
                }
            }
            caseNames.add(startBeep);
            caseNames.add(stopBeep);
            caseNames.add(autoBeep);
            caseNames.add(startBeepWithConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void My1startBeep(int ms) {
        try {
            logUtils.addCaseLog("Execute startBeep success");
            iBeeper.startBeep(ms);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute startBeep Failed");
            e.printStackTrace();
        }
    }

    public void My2stopBeep() {
        try {
            logUtils.addCaseLog("stopBeep执行成功");
            iBeeper.stopBeep();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute stopBeep failed");
            e.printStackTrace();
        }
    }
    public void My3autoTest() {
        try {
            logUtils.addCaseLog("stopBeep执行成功");
            iBeeper.stopBeep();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute stopBeep failed");
            e.printStackTrace();
        }
    }
    public void My4startBeepWithConfig(int msec, Bundle bundle) {
        try {
            logUtils.addCaseLog("Execute start success");
            iBeeper.startBeepWithConfig(msec, bundle);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute start failed");
            e.printStackTrace();
        }
    }


    public void B01001() {
        this.printMsgTool("buzzer 0s" );
        My1startBeep(0);
    }
    public void B01002() {
        this.printMsgTool("buzzer 500ms" );
        My1startBeep(500);
    }
    public void B01003() {
        this.printMsgTool("buzzer 5000ms" );
        My1startBeep(5000);
    }
    public void B01004()
    {
        this.printMsgTool("buzzer 0s" );
        My1startBeep(-1);
    }
    public void B01005() {
        this.printMsgTool("buzzer 10s" );
        for (int i = 0; i < 5 ; i++) {
            My1startBeep(1000);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();

            }
        }
    }
    public void B01006() {
//        this.printMsgTool("buzzer 10s" );
        My1startBeep(10000);
    }
    public void B01007() {
        B01006();

        new Thread()
        {
            public void run () {
                try {
                    My1startBeep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    public void B01008() {
        this.printMsgTool("Beeping and searching for the card all proceeded normally" );
        My1startBeep(5000);
        ((MyApplication) context).serviceMoudle.magCardReaderMoudle.H01001();
    }

    public void B02001() {
        this.printMsgTool("buzzer No response" );
        My2stopBeep();
    }

    public void B02002() {
        B01006();
        this.printMsgTool("The buzzing will stop in three seconds" );

        MyThread myThread = new MyThread(3000);
        myThread.start();
    }

    public void B02003() {
        B01006();
        this.printMsgTool("The buzzing will stop in three seconds" );
        MyThread myThread = new MyThread(3000);
        myThread.start();
    }

    public void B02004(){
        this.printMsgTool("Beeping and printing were carried out normally" );
                My1startBeep(10000);

                new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((MyApplication) context).serviceMoudle.getPintBtMoudle().D03028();
            }
        }.start();
    }

    public void B02006() {
        this.printMsgTool("Beeping and searching for the card all proceeded normally" );
        My1startBeep(5000);
        try {
            Thread.sleep(5000);
            ((MyApplication) context).serviceMoudle.magCardReaderMoudle.H01001();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //((MyApplication) context).serviceMoudle.magCardReaderMoudle.H01001();
    }

/*    public void B03001() {

        //B01001方法
        logUtils.addCaseLog("B01001方法调用开始");
        try {
            this.B01001();
//            String[] strs = {"2"};
//            String str = strs[2];
        } catch (Exception e) {
            e.printStackTrace();
            logUtils.addCaseLog("B01001方法异常，error：" + e);
        } finally {
            logUtils.addCaseLog("B01001方法调用结束");
        }

        //B01002方法
        logUtils.addCaseLog("B01002方法调用开始");
        try {
            this.B01002();
        } catch (Exception e) {
            e.printStackTrace();
            logUtils.addCaseLog("B01002方法异常，error：" + e);
        } finally {
            logUtils.addCaseLog("B01002方法调用结束");
        }

        //B01003方法
        logUtils.addCaseLog("B01003方法调用开始");
        try {
            this.B01003();
        } catch (Exception e) {
            e.printStackTrace();
            logUtils.addCaseLog("B01003方法异常，error：" + e);
        } finally {
            logUtils.addCaseLog("B01003方法调用结束");
        }

    }*/

    public void B03_AUTO() {

        try {
            //通过类文件获取类对象
            Class aClass = Class.forName("moudles.BeerMoudle");
            this.printMsgTool("-------Beeper module------");
            this.printMsgTool("The automated test case starts execution");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("B") && !method.getName().equals("B03_AUTO")) {
                    this.printMsgTool(method.getName() + "Method starts executing......");
                    method.invoke(this);
                    this.printMsgTool(method.getName() + "Method end executing！！！");
                    Thread.sleep(1000);
                }
            }
            this.printMsgTool("The automated test case has been executed");
            logUtils.addCaseLog("The automated test case has been executed");
            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D08018();

            File file = new File("E:/data/data/user.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void B04001(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 5000);
        My4startBeepWithConfig(1000, bundle);
    }
    public void B04002(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 2000);
        My4startBeepWithConfig(1000, bundle);
    }
    public void B04003(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 1200);
        My4startBeepWithConfig(1000, bundle);
    }
    public void B04004(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 800);
        My4startBeepWithConfig(1000, bundle);
    }
    public void B04005(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 600);
        My4startBeepWithConfig(1000, bundle);
    }
    public void B04006(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 400);
        My4startBeepWithConfig(1000, bundle);
    }
    public void B04007(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 800);
        My4startBeepWithConfig(0, bundle);
    }
    public void B04008(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 0);
        My4startBeepWithConfig(5000, bundle);
    }

    public void B04009(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 20000);
        My4startBeepWithConfig(5000, bundle);
    }
    public void B04010(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 20);
        My4startBeepWithConfig(5000, bundle);
    }
    public void B04011(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 30000);
        My4startBeepWithConfig(5000, bundle);
    }
    public void B04012(){
        Bundle bundle = new Bundle();
        bundle.putInt("HZ", 10);
        My4startBeepWithConfig(5000, bundle);
    }

   /* public void B03_PRINT(){
        for(int i=0;i<10;i++){
            this.printMsgTool("当前值：" + i);
            logUtils.addCaseLog("当前值：" + i);
        }
    }*/

    private void printMsgTool(String msg){
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

     /*   new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((MyApplication) context).serviceMoudle.getPbocBtMoudle().K01001();
            }
        }.start();
    }*/



    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.BeerMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "case execute finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }
    class MyThread extends Thread {
        int time;

        public MyThread(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            super.run();
            try {
                this.sleep(time);
                My2stopBeep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}