package moudles;

import static com.verifone.smartpos.api.printer.PrinterConst.BarcodeFormat.CODE_39;
import static com.verifone.smartpos.api.printer.PrinterConst.BarcodeFormat.QR_CODE;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.IScanner;
import com.dreamtek.smartpos.deviceservice.aidl.PrinterListener;
import com.dreamtek.smartpos.deviceservice.aidl.ScannerListener;
import com.verifone.activity.R;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import Utils.canvasReceipt.CanvasReceiptContext;
import Utils.canvasReceipt.ReceiptRowsDef;
import base.MyApplication;
import java.text.SimpleDateFormat;
import java.util.Date;

import static base.MyApplication.serviceMoudle;

/**
 * Created by WenpengL1 on 2016/12/29.
 */

public class ScanBtMoudle {
    Context context;
    IScanner iScanner;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> startScan = new ArrayList<String>();
    ArrayList<String> stopScan = new ArrayList<String>();
    ArrayList<String> scannerInit = new ArrayList<String>();
    ArrayList<String> openFlashLight = new ArrayList<String>();
    ArrayList<String> switchScanner = new ArrayList<String>();
    ArrayList<String> create1D2DcodeImage = new ArrayList<>();
    ArrayList<String> injectHoneywellLicence = new ArrayList<>();
    ArrayList<String> checkHoneyKey = new ArrayList<>();
    static final int BACK_CAMERA = 0;
    static final int FRONT_CAMERA = 1;
    IPrinter iPrinter;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
            logUtils.showCaseLog();
        }
    };

    public ScanBtMoudle(Context context, IScanner iScanner, IPrinter iPrinter) {
        this.context = context;
        logUtils = serviceMoudle.logUtils;
        this.iScanner = iScanner;
        this.iPrinter = iPrinter;
        addAllapi();
    }


    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.ScanBtMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "E01":
                            startScan.add(i.getName());
                            break;
                        case "E02":
                            stopScan.add(i.getName());
                            break;
                        case "E03":
                            scannerInit.add(i.getName());
                            break;
                        case "E04":
                            openFlashLight.add(i.getName());
                            break;
                        case "E05":
                            switchScanner.add(i.getName());
                            break;
                        case "E06":
                            create1D2DcodeImage.add(i.getName());
                            break;
                        case "E07":
                            injectHoneywellLicence.add(i.getName());
                            break;
                        case "E08":
                            checkHoneyKey.add(i.getName());
                    }
                }
            }
            caseNames.add(startScan);
            caseNames.add(stopScan);
            caseNames.add(scannerInit);
            caseNames.add(openFlashLight);
            caseNames.add(switchScanner);
            caseNames.add(create1D2DcodeImage);
            caseNames.add(injectHoneywellLicence);
            caseNames.add(checkHoneyKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void E01001() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(0, new ScanListener());
    }

    public void E01002() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(-1, new ScanListener());
    }

    public void E01003() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(60, new ScanListener());
    }

    public void E01004() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(10, new ScanListener());
    }

    public void E01005() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(2147483647, new ScanListener());
    }

    public void E01006() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(60, null);
    }

    public void E01007() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(60, new ScanListener());
    }

    public void E01008() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(60, new ScanListener());
    }

    public void E01009() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);

        Bundle bundle = new Bundle();
        bundle.putString("topTitleString", "请对准二维码进行扫描");
        bundle.putString("upPromptString", "扫描框上标题");
        bundle.putString("downPromptString", "扫描框下标题");
        bundle.putBoolean("showScannerBorder", true);
        bundle.putBoolean("disableBackKeyEvent", true);
        startScanEX(60, bundle, new ScanListener());
    }

    public void E01010() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);

        Bundle bundle = new Bundle();
        bundle.putString("topTitleString", "请对准二维码进行扫描");
        bundle.putString("upPromptString", "扫描框上标题");
        bundle.putString("downPromptString", "扫描框下标题");
        bundle.putBoolean("showScannerBorder", true);
        bundle.putBoolean("disableBackKeyEvent", true);
        startScanEX(60, bundle, new ScanListener());
    }

    public void E01011() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);

        Bundle bundle = new Bundle();
        bundle.putString("topTitleString", "请对准二维码进行扫描");
        bundle.putString("upPromptString", "扫描框上标题");
        bundle.putString("downPromptString", "扫描框下标题");
        bundle.putBoolean("showScannerBorder", true);
        bundle.putBoolean("disableBackKeyEvent", false);
        startScanEX(60, bundle, new ScanListener());
    }

    public void E01012() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);

        Bundle bundle = new Bundle();
        bundle.putString("topTitleString", "请对准二维码进行扫描");
        bundle.putString("upPromptString", "扫描框上标题");
        bundle.putString("downPromptString", "扫描框下标题");
        bundle.putBoolean("showScannerBorder", true);
        bundle.putBoolean("disableBackKeyEvent", false);
        startScanEX(60, bundle, new ScanListener());
    }

    public void E01013() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);

        Bundle bundle = new Bundle();
        bundle.putBoolean("showScannerBorder", true);
        bundle.putBoolean("disableBackKeyEvent", true);
        startScanEX(60, bundle, new ScanListener());
        try {
            Thread.sleep(5000);
            My02stopScan();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01014() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);

        Bundle bundle = new Bundle();
        bundle.putBoolean("showScannerBorder", true);
        bundle.putBoolean("disableBackKeyEvent", true);
        startScanEX(60, bundle, new ScanListener());
        try {
            Thread.sleep(5000);
            My02stopScan();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01018() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(60, new ScanListener());
    }

    public void E01019() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My01startScan(60, new ScanListener());
        try {
            Thread.sleep(5000);
            My01startScan(30, new ScanListener());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01023() {
        E01008();
    }

    public void E01026() {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().D03028();
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        try {
            Thread.sleep(1000);
            My01startScan(60, new ScanListener());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01027() {
        E01008();
        try {
            Thread.sleep(5000);
            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D03028();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01048() {
        E01008();
    }

    public void E01049() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", false);
        msgPrompt.putInt("scannerSelect", 1);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01050() {
        try {
            iScanner.startScan(null, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01051() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", null);
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01052() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "");
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01053() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", null);
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01054() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", "");
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01055() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", null);
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01056() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", "");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01057() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "四川省德阳市旌阳区黄河路");
        msgPrompt.putString("upPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆猪血丸子");
        msgPrompt.putString("downPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆糖油糍粑");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01058() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "四川省德阳市旌阳区黄河西路");
        msgPrompt.putString("upPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆炒猪血小丸子");
        msgPrompt.putString("downPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆炸糖油小糍粑");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01059() {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "one day in your life You'll remember a place ");
        msgPrompt.putString("upPromptString", "Someone touching your face You'll come back and you'll look around you");
        msgPrompt.putString("downPromptString", "Then you'll remember me somehow Though you don't need me now I will stay in your heart");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01060() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(0, new ScanListener());
    }

    public void E01061() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(-1, new ScanListener());
    }

    public void E01062() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(60, new ScanListener());
    }

    public void E01063() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(10, new ScanListener());
    }

    public void E01064() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(2147483647, new ScanListener());
    }

    public void E01065() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(60, null);
    }

    public void E01066() {
        E01062();
    }

    public void E01067() {
        E01062();
    }

    public void E01077() {
        E01062();
    }

    public void E01078() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My01startScan(60, new ScanListener());
        try {
            Thread.sleep(5000);
            My01startScan(30, new ScanListener());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01079() {
        E01062();
    }

    public void E01081() {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().D03028();
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        try {
            Thread.sleep(3000);
            My01startScan(60, new ScanListener());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01082() {
        E01062();
        try {
            Thread.sleep(5000);
            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D03028();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void E01092() {
        E01062();
    }

    public void E01093() {
        E01062();
    }

    public void E01094() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", false);
//        msgPrompt.putInt("scannerSelect",1);
//        msgPrompt.putInt("scannerSelect",0);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01095() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        try {
            iScanner.startScan(null, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01096() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", null);
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01097() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "");
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01098() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", null);
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01099() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", "");
        msgPrompt.putString("downPromptString", "下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01100() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", null);
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01101() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "主标题");
        msgPrompt.putString("upPromptString", "上标题");
        msgPrompt.putString("downPromptString", "");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01102() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "四川省德阳市旌阳区黄河路");
        msgPrompt.putString("upPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆猪血丸子");
        msgPrompt.putString("downPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆糖油糍粑");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01103() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "四川省德阳市旌阳区黄河西路");
        msgPrompt.putString("upPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆炒猪血丸子");
        msgPrompt.putString("downPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆炸糖油糍粑");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01104() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "one day in your life You'll remember a place ");
        msgPrompt.putString("upPromptString", "Someone touching your face You'll come back and you'll look around you");
        msgPrompt.putString("downPromptString", "Then you'll remember me somehow Though you don't need me now I will stay in your heart");
        msgPrompt.putBoolean("showScannerBorder", true);
        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01105() {
        E01062();
    }

    public void E01106() {
        E01062();
    }

    public void E01107() {
        E01062();
    }

    public void E01108() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "四川省德阳市旌阳区黄河路");
        msgPrompt.putString("upPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆猪血丸子");
        msgPrompt.putString("downPromptString", "湖南省长沙市岳麓区麓山路集贤宾馆糖油糍粑");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putInt("scannerSelect", 1);

        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01109() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "陕西省西安市");
        msgPrompt.putString("upPromptString", "西安理工大学");
        msgPrompt.putString("downPromptString", "西安交通大学");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putInt("scannerSelect", 0);

        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }

    }

    public void E01110() {
        E01108();
        new Thread() {
            public void run() {
                try {
                    E01109();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public void E01111() {
        E01003();

    }

    public void E01112() {
        E01003();

    }

    public void E01113() {
        E01003();

    }

    public void E01114() {
        E01003();

    }

    public void E01115() {
        E01062();

    }

    public void E01116() {
        E01062();

    }

    public void E01117() {
        E01062();

    }

    public void E01118() {
        E01062();
    }

    public void E01200() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        startHoneywellScan(60, new ScanListener());
    }

    public void E01201() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        startHoneywellScan(60, new ScanListener());
    }

    public void E01202(){
        //打开enableScannerWindowProtect,前置摄像头
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);

        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putBoolean("enableScannerWindowProtect",true);
        //      msgPrompt.putString("decodeLibName", "honeywell;436297729");
//        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649");

        try {
            iScanner.startScan(msgPrompt, 0, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01203(){
        //打开enableScannerWindowProtect，后置摄像头
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);

        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putBoolean("enableScannerWindowProtect",true);
        //      msgPrompt.putString("decodeLibName", "honeywell;436297729");
//        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649");

        try {
            iScanner.startScan(msgPrompt, 0, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01204(){
        //disable enableScannerWindowProtect，前置
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);

        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putBoolean("enableScannerWindowProtect",false);
        //      msgPrompt.putString("decodeLibName", "honeywell;436297729");
//        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649");

        try {
            iScanner.startScan(msgPrompt, 0, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01205(){
        //disable enableScannerWindowProtect，前置
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);

        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putBoolean("enableScannerWindowProtect",false);
        //      msgPrompt.putString("decodeLibName", "honeywell;436297729");
//        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649");

        try {
            iScanner.startScan(msgPrompt, 0, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01206(){
        //霍尼  enableScannerWindowProtect=true
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649;436391937");
        msgPrompt.putBoolean("enableScannerWindowProtect",true);

        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01207(){
        //霍尼  enableScannerWindowProtect=true
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649;436391937");
        msgPrompt.putBoolean("enableScannerWindowProtect",true);

        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01208(){
        //霍尼  enableScannerWindowProtect=false
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649;436391937");
        msgPrompt.putBoolean("enableScannerWindowProtect",false);

        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E01209(){
        //霍尼  enableScannerWindowProtect=false
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649;436391937");
        msgPrompt.putBoolean("enableScannerWindowProtect",false);

        try {
            iScanner.startScan(msgPrompt, 60, new ScanListener());
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void E02001() {
        ((MyApplication) context).serviceMoudle.getiScanner(FRONT_CAMERA);
        My02stopScan();
    }

    public void E02002() {
        E01008();
        new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    this.sleep(5000);
                    My02stopScan();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void E02003() {
        for (int i = 0; i < 10; i++) {
            E01008();
            new Thread() {
                @Override
                public void run() {
//                super.run();
                    try {
                        this.sleep(500);
                        My02stopScan();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public void E02005() {
        My02stopScan();
        My02stopScan();
    }

    public void E02006() {
        ((MyApplication) context).serviceMoudle.getiScanner(BACK_CAMERA);
        My02stopScan();
    }

    public void E02007() {
        E01062();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    this.sleep(5000);
                    My02stopScan();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void E02008() {
        for (int i = 0; i < 10; i++) {
            E02007();
        }
    }

    public void E02010() {
        My02stopScan();
        My02stopScan();
    }

    public void E03001() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 0);
        param.putInt("y1", 0);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01001();

    }

    public void E03002() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 0);
        param.putInt("y1", 128);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01001();

    }

    public void E03003() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 0);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01001();

    }

    public void E03004() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 128);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01001();

    }


    public void E03005() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", false);
        param.putInt("x1", 0);
        param.putInt("y1", 0);
        param.putInt("width", 0);
        param.putInt("height", 0);


        My03scannerInit(param);
        E01001();

    }

    public void E03006() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 0);
        param.putInt("y1", 0);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03007() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 0);
        param.putInt("y1", 128);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03008() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 0);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03009() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 128);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }


    public void E03010() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", false);
        param.putInt("x1", 0);
        param.putInt("y1", 0);
        param.putInt("width", 0);
        param.putInt("height", 0);


        My03scannerInit(param);
        E01060();

    }

    public void E03011() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", -1);
        param.putInt("y1", 128);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03012() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 2048);
        param.putInt("y1", 300);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03013() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", -1);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03014() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 2048);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03015() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 2048);
        param.putInt("y1", 2048);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03016() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 128);
        param.putInt("width", 0);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03017() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 128);
        param.putInt("width", 512);
        param.putInt("height", 0);


        My03scannerInit(param);
        E01060();

    }

    public void E03018() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 128);
        param.putInt("width", 0);
        param.putInt("height", 0);


        My03scannerInit(param);
        E01060();

    }

    public void E03019() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 128);
        param.putInt("y1", 128);
        param.putInt("width", 1000);
        param.putInt("height", 512);


        My03scannerInit(param);
        E01060();

    }

    public void E03020() {
        Bundle param = new Bundle();
        param.putBoolean("customUI", true);
        param.putInt("x1", 0);
        param.putInt("y1", 0);
        param.putInt("width", 512);
        param.putInt("height", 512);


        My03scannerInit(param);
        E05001();
    }


    public void E04001() {
        E01062();
        new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    this.sleep(5000);
                    My04openFlashLight(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public void E04002() {
        E01062();
        new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    this.sleep(5000);
                    My04openFlashLight(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public void E04003() {


        E01062();
        new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    this.sleep(5000);
                    My04openFlashLight(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void E04004() {


        E01062();
        new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    this.sleep(5000);
                    My04openFlashLight(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void E05001() {

        E01001();
        new Thread() {
            public void run() {
                try {
                    E01001();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }.start();

        new Thread() {
            @Override
            public void run() {
//                super.run();
                try {
                    this.sleep(50000);
                    My05switchScanner();
                    My01startScan(60, new ScanListener());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void E05002() {


        new Thread() {
            public void run() {
                try {
                    this.sleep(50000);
                    My05switchScanner();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                E01060();
            }

        }.start();

        My01startScan(60, new ScanListener());

    }

    public void E06001() {
        My06create1D2DcodeImage();

    }

    public void E06002() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 0);
            bundleImage.putInt("width", 40);
            bundleImage.putInt("height", 40);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("xuna.l@verifone.cn", 200, QR_CODE);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("打印邮箱");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }

    public void E06003() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 0);
            bundleImage.putInt("width", 50);
            bundleImage.putInt("height", 50);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("123456789abcdefg", 220, QR_CODE);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("打印数字+字母");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }


    public void E06004() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 0);
            bundleImage.putInt("width", 60);
            bundleImage.putInt("height", 60);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("!@#$%^&*()", 230, QR_CODE);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("打印特殊字符");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }

    public void E06005() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 10);
            bundleImage.putInt("width", 40);
            bundleImage.putInt("height", 40);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("  ", 200, QR_CODE);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("打印空");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }

    public void E06006() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 40);
            bundleImage.putInt("width", 40);
            bundleImage.putInt("height", 40);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("123456789abcdefgABCF!@#$%/46542ZgbHKKBF6220203041065429943544MKJJpin1322556", 200, QR_CODE);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("打印长数据");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }

    public void E06007() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 40);
            bundleImage.putInt("width", 40);
            bundleImage.putInt("height", 40);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("五九九九九九离开了数量大幅减少了阿斯蒂芬手动阀微软微软啊的法国确认飞机叫阿里的杀伤敌方", 200, QR_CODE);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("打印汉字");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }

    public void E07001() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V1*";
        String data = "2024-07-20";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07002() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V9*";
        String data = "2024-07-20";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07003() {
        String licenseSn = "trial-siot-verif-jmurp-05112022";
        String deviceSn = "V9*";
        String data = "2024-07-20";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07004() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V4*";
        String data = "2024-07-20";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07005() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V9*";
        String data = "2023-07-20";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07006() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V1*";
        String data = null;
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07007() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V9*";
        String data = null;
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07008() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V1*";
        String data = " ";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07009() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V9*";
        String data = " ";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07010() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V1*";
        String data = "";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07011() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V9*";
        String data = "";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07012() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V1*";
        String data = "2024-07";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07013() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V9*";
        String data = "adcd123";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07014() {
        String licenseSn = "siot-2018-9197-verif-jmurp-01122023";
        String deviceSn = "V*";
        String data = "2025-04-16";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07015() {
        String licenseSn = "trialff-siot-verif-jmurp-05112022";
        String deviceSn = "V*";
        String data = "2025-04-16";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07016() {
        String licenseSn = "siot-2018-9197-verif-jmurp-01122023";
        String deviceSn = "V8*";
        String data = "2025-04-16";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07017() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V*";
        String data = null;
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07018() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V*";
        String data = " ";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07019() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V*";
        String data = "";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07020() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V*";
        String data = "2025-04";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E07021() {
        String licenseSn = "dev-pssvs-verif-jmurp-11082023";
        String deviceSn = "V*";
        String data = "adcd123";
        My07injectHoneywellLicence(licenseSn, deviceSn, data);
    }

    public void E08001() {
        logUtils.addCaseLog("E08001 execute");
        My08checkHoneyKey();
    }

    public void My01startScan(long timeout, ScannerListener listener) {

        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
 //      msgPrompt.putString("decodeLibName", "honeywell;436297729");
//        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649");

        try {
            iScanner.startScan(msgPrompt, timeout, listener);
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    private void startScanEX(long timeout, Bundle setting, ScannerListener listener) {
        try {
            iScanner.startScan(setting, timeout, listener);
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void startHoneywellScan(long timeout, ScannerListener listener) {
        Bundle msgPrompt = new Bundle();
        msgPrompt.putString("topTitleString", "请对准二维码进行扫描");
        msgPrompt.putString("upPromptString", "扫描框上标题");
        msgPrompt.putString("downPromptString", "扫描框下标题");
        msgPrompt.putBoolean("showScannerBorder", true);
        msgPrompt.putString("decodeLibName", "honeywell;436289537;436297729;436379649;436391937");

        try {
            iScanner.startScan(msgPrompt, timeout, listener);
        } catch (RemoteException e) {
            logUtils.addCaseLog("startScan执行失败");
            e.printStackTrace();
        }
    }

    public void My02stopScan() {
        try {
            iScanner.stopScan();
            logUtils.addCaseLog("stopScan执行完毕");
        } catch (RemoteException e) {
            logUtils.addCaseLog("stopScan执行异常");
            e.printStackTrace();
        }
    }

    public void My03scannerInit(Bundle param) {
        try {
            iScanner.scannerInit(param);
            logUtils.addCaseLog("scannerInit执行完毕");
        } catch (RemoteException e) {
            logUtils.addCaseLog("scannerInit执行异常");
            e.printStackTrace();
        }
    }


    public void My04openFlashLight(boolean enable) {
        try {
            iScanner.openFlashLight(enable);
            logUtils.addCaseLog("openFlashLight执行完毕");
        } catch (RemoteException e) {
            logUtils.addCaseLog("openFlashLight执行异常");
            e.printStackTrace();
        }
    }

    public void My05switchScanner() {
        try {
            iScanner.switchScanner();
            logUtils.addCaseLog("switchScanner执行完毕");
        } catch (RemoteException e) {
            logUtils.addCaseLog("switchScanner执行异常");
            e.printStackTrace();
        }
    }


    public boolean isPrinting = false;
    private static final Object isPrintingLock = new Object();
    boolean silencePrinting = false;
//    int printResult = 0;
//    CanvasRecesiptContext printerCanvas = null;
//    boolean hungupPrinting = false;
//    PrinterModuleListener printerModuleListener = new PrinterModuleListener();

    class MyListener extends PrinterListener.Stub {
        @Override
        public void onError(int error) throws RemoteException {
            synchronized (isPrintingLock) {
                isPrinting = false;
            }
            if (!silencePrinting) {
                Message msg = new Message();
                msg.getData().putString("msg", "打印错误,错误码:" + error);
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onFinish() throws RemoteException {
            synchronized (isPrintingLock) {
                isPrinting = false;
            }
            if (!silencePrinting) {
                Message msg = new Message();
                msg.getData().putString("msg", "打印完成");
                handler.sendMessage(msg);
            }
        }
    }

    public void My06create1D2DcodeImage() {

        try {
            Toast.makeText(this.context, "开始执行......", Toast.LENGTH_SHORT).show();
            Bundle bundleImage = new Bundle();
            bundleImage.putInt("offset", 0);
            bundleImage.putInt("width", 40);
            bundleImage.putInt("height", 40);
            bundleImage.putInt("gray", 128);
            Bitmap bitmap = iScanner.create1D2DcodeImage("www.baidu.com", 150, QR_CODE);
//            Bitmap image = BitmapFactory.decodeResource(this.context.getResources(), R.mipmap.ic_launcher);
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
            logUtils.addCaseLog(bitmap.toString());
            iPrinter.addBmpImage(bundleImage, bitmap);
            iPrinter.startPrint(new MyListener());
//            logUtils.addCaseLog(image.toString());
            logUtils.addCaseLog("create1D2DcodeImage执行完毕");

        } catch (RemoteException e) {
            logUtils.addCaseLog("create1D2DcodeImage执行异常");
            e.printStackTrace();
        }
    }

    public void My07injectHoneywellLicence(String licenseSn, String deviceSn, String data) {
        try {
            boolean ret = false;
            logUtils.addCaseLog("My07injectHoneywellLicence execute;");
            logUtils.addCaseLog("licenseSn = " + licenseSn + ";   ");
            logUtils.addCaseLog("deviceSn = " + deviceSn + ";   ");
            logUtils.addCaseLog("data = " + data + ";   ");

            ret = iScanner.injectHoneywellLicence(licenseSn, deviceSn, data);
            logUtils.addCaseLog("return ret = " + ret);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My08checkHoneyKey() {
        boolean res = false;
        try {
            res = iScanner.checkHoneywellLicense();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("register key: " + res);
    }



    public class ScanListener extends ScannerListener.Stub {
        long st = System.currentTimeMillis();
       {
            Message msg = new Message();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            msg.getData().putString("msg", "\n"+dfs.format(st));
            handler.sendMessage(msg);
        }

        int num = 0;

        public ScanListener(int num) {
            this.num = num;
        }

        public ScanListener() {

        }

        @Override
        public void onSuccess(String barcode) throws RemoteException {

            Message msg = new Message();
            SimpleDateFormat dfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            long sdt = System.currentTimeMillis() - st;
            msg.getData().putString("msg", num + "扫描成功,扫码:" + barcode+"\n"+dfe.format(System.currentTimeMillis())+"\n"+sdt);

            handler.sendMessage(msg);
        }

        @Override
        public void onError(int error, String message) throws RemoteException {
            Message msg = new Message();
            msg.getData().putString("msg", num + "扫描失败,错误码:" + error + ",错误信息:" + message);
            handler.sendMessage(msg);
        }

        @Override
        public void onTimeout() throws RemoteException {
            Message msg = new Message();
            msg.getData().putString("msg", num + "扫描超时");
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel() throws RemoteException {
            Message msg = new Message();
            msg.getData().putString("msg", num + "用户取消");
            handler.sendMessage(msg);
        }
    }

    class MyThread extends Thread {
        long time;

        public MyThread(long time) {
            this.time = time;
        }

        @Override
        public void run() {
            super.run();
            My01startScan(30, new ScanListener());
            try {
                this.sleep(time);
                My02stopScan();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        logUtils.printCaseInfo(name);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.ScanBtMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "案例执行完毕");
            logUtils.showCaseLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }
}