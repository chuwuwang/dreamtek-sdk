package moudles;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.PrinterListener;
import com.dreamtek.smartpos.deviceservice.aidl.QrCodeContent;
import com.verifone.activity.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import Utils.LogUtils;
import base.MyApplication;

/**
 * Created by WenpengL1 on 2016/12/29.
 */

public class PrintBtMoudle {
    Context context;
    IPrinter iPrinter;
    static LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> getStatus = new ArrayList<String>();
    ArrayList<String> setGray = new ArrayList<String>();
    ArrayList<String> addText = new ArrayList<String>();
    ArrayList<String> addBarCode = new ArrayList<String>();
    ArrayList<String> addQrCode = new ArrayList<String>();
    ArrayList<String> feedLine = new ArrayList<String>();
    ArrayList<String> startPrint = new ArrayList<String>();
    ArrayList<String> addQrCodesInLine = new ArrayList<String>();
    ArrayList<String> setLineSpace = new ArrayList<String>();
    ArrayList<String> addTextInLine = new ArrayList<String>();
    ArrayList<String> startSaveCachePrint = new ArrayList<String>();
    ArrayList<String> cleanCache = new ArrayList<String>();
    ArrayList<String> startPrintInEmv = new ArrayList<String>();
    ArrayList<String> autoPrint = new ArrayList<String>();
    ArrayList<String> addScreenCapture = new ArrayList<String>();
    ArrayList<String> addBmpImage = new ArrayList<>();
    static final String BASEPATH = "/sdcard/Pictures/";
    //align
    static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;
    //font
    static final int SMALL = 0;
    static final int NORMAL = 0;
    static final int LARGE = 0;
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
            logUtils.showCaseLog();
        }
    };

    public PrintBtMoudle(Context context, IPrinter iPrinter) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iPrinter = iPrinter;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.PrintBtMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "D01":
                            getStatus.add(i.getName());
                            break;
                        case "D02":
                            setGray.add(i.getName());
                            break;
                        case "D03":
                            addText.add(i.getName());
                            break;
                        case "D04":
                            addBarCode.add(i.getName());
                            break;
                        case "D05":
                            addQrCode.add(i.getName());
                            break;
                        case "D07":
                            startPrint.add(i.getName());
                            break;
                        case "D08":
                            feedLine.add(i.getName());
                            break;
                        case "D09":
                            addQrCodesInLine.add(i.getName());
                            break;
                        case "D10":
                            setLineSpace.add(i.getName());
                            break;
                        case "D11":
                            addTextInLine.add(i.getName());
                            break;
                        case "D12":
                            startSaveCachePrint.add(i.getName());
                            break;
                        case "D13":
                            cleanCache.add(i.getName());
                            break;
                        case "D14":
                            startPrintInEmv.add(i.getName());
                            break;
                        case "D15":
                            autoPrint.add(i.getName());
                            break;
                        case "D16":
                            addScreenCapture.add(i.getName());
                            break;
                        case "D17":
                            addBmpImage.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(getStatus);
            caseNames.add(setGray);
            caseNames.add(addText);
            caseNames.add(addBarCode);
            caseNames.add(addQrCode);

            caseNames.add(startPrint);
            caseNames.add(feedLine);
            caseNames.add(addQrCodesInLine);
            caseNames.add(setLineSpace);
            caseNames.add(addTextInLine);
            caseNames.add(startSaveCachePrint);
            caseNames.add(cleanCache);
            caseNames.add(startPrintInEmv);
            caseNames.add(autoPrint);
            caseNames.add(addScreenCapture);
            caseNames.add(addBmpImage);
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
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.PrintBtMoudle");
            Method method = aClass.getDeclaredMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "Case execution completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    public int My01getStatus() {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return 0;
        try {
            int code = iPrinter.getStatus();
            logUtils.addCaseLog("getStatus return code：" + code);
            return code;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute My01getStatus exception");
            e.printStackTrace();
            return 0;
        }
    }

    public void My02setGray(int gray) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.setGray(gray);
            logUtils.addCaseLog("Execute setGray finish");
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute setGray exception");
            e.printStackTrace();
        }
    }

    public void My03addText(Bundle format, String text) {
        addText(format, text, false);
    }

    public void addText(Bundle format, String text, boolean silence) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addText(format, text);
            if (!silence) {
                logUtils.addCaseLog("Execute addText finish");
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("addText执行异常");
            e.printStackTrace();
        }
    }

    public void My04addBarCode(Bundle format, String barcode) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addBarCode(format, barcode);
            logUtils.addCaseLog("addBarCode执行完成");
        } catch (RemoteException e) {
            logUtils.addCaseLog("addBarCode执行异常");
            e.printStackTrace();
        }
    }

    public void My05addQrCode(Bundle format, String qrCode) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addQrCode(format, qrCode);
            logUtils.addCaseLog("执行addQrCode完成");
        } catch (RemoteException e) {
            logUtils.addCaseLog("执行addQrCode异常");

            e.printStackTrace();
        }
    }

    public void My07startPrint(MyListener listener) {
        startPrint(listener, false);
    }

    boolean silencePrinting = false;

    public void startPrint(MyListener listener, boolean silence) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL) || "X990 UX".equals(Build.MODEL))
            return;
        try {
            boolean printing = false;
            do {
                synchronized (isPrintingLock) {
                    printing = isPrinting;
                }
                if (printing) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (printing);

            synchronized (isPrintingLock) {
                isPrinting = true;
            }
            silencePrinting = silence;
            iPrinter.startPrint(listener);
            if (!silence) {
                logUtils.addCaseLog("startPrint调用完成");
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("My07startPrint异常");
            e.printStackTrace();
        }
    }

    public void My08feedLine(int lines) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.feedLine(lines);
            logUtils.addCaseLog("feedLine走纸执行完成");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void My09addQrCodesInLine(List<QrCodeContent> qrCodes) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addQrCodesInLine(qrCodes);
            logUtils.addCaseLog("添加多个二维码成功");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("添加多个二维码失败");
        }
    }

    public void My10setLineSpace(int space) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.setLineSpace(space);
            logUtils.addCaseLog("设置行间距执行完成");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("设置行间距执行失败");
        }
    }

    public void My11addTextInLine(Bundle format, String lString, String mString, String rString, int mode) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addTextInLine(format, lString, mString, rString, mode);
            logUtils.addCaseLog("addTextInLine执行完成");
        } catch (RemoteException e) {
            logUtils.addCaseLog("addTextInLine执行异常");
            e.printStackTrace();
        }
    }

    public void My12startSaveCachePrint(PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.startSaveCachePrint(listener);
            logUtils.addCaseLog("startSaveCachePrint调用完成");
        } catch (RemoteException e) {
            logUtils.addCaseLog("startSaveCachePrint异常");
            e.printStackTrace();
        }
    }

    public int My13cleanCache() {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return 0;
        try {
            int result = iPrinter.cleanCache();
            logUtils.addCaseLog("清除缓存结果 = " + result);
            return result;
        } catch (RemoteException e) {
            logUtils.addCaseLog("清除缓存异常");
            e.printStackTrace();
            return -1;
        }
    }

    public void My14startPrintInEmv(PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.startPrintInEmv(listener);
            logUtils.addCaseLog("My14startPrintInEmv执行");
        } catch (RemoteException e) {
            logUtils.addCaseLog("My14startPrintInEmv执行异常");
            e.printStackTrace();
        }
    }

    public void My15autoPrint(PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.startPrintInEmv(listener);
            logUtils.addCaseLog("My14startPrintInEmv执行");
        } catch (RemoteException e) {
            logUtils.addCaseLog("My14startPrintInEmv执行异常");
            e.printStackTrace();
        }
    }

    public void My16addScreenCapture(Bundle bundle, PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addScreenCapture(bundle);
            iPrinter.startPrintInEmv(listener);
            logUtils.addCaseLog("My14startPrintInEmv执行");
        } catch (RemoteException e) {
            logUtils.addCaseLog("My14startPrintInEmv执行异常");
            e.printStackTrace();
        }
    }

    public void My17addBmpImage(Bundle format, Bitmap image) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addBmpImage(format, image);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void startPrint() {
        My07startPrint(null);
    }

    //    void D01001() {
//        My01getStatus();
//    }
    void D01001() {
//        Log.d("TAG", "testPrinter");
//        // bundle format for addText
//        Bundle format = new Bundle();
//
//        try {
//
//            Bundle fmtAddTextInLineِTest = new Bundle();
//            //
//            fmtAddTextInLineِTest.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.NORMAL_24_24 );
//            fmtAddTextInLineِTest.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_ALGER );
//
//            iPrinter.addTextInLine(fmtAddTextInLineِTest,
//                    "", "",
//                    "فرعي  شقة  عمارة  بلوك  قطاع  يومية",
//                    0);
//
//            iPrinter.addTextInLine(fmtAddTextInLineِTest,
//                    "", "",
//                    "64    64    12    10    01    00",
//                    0);
//
//
//
//            iPrinter.addTextInLine(fmtAddTextInLineِTest,
//                    "", "",
//                    "مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية ",
//                    0);
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT);
//            iPrinter.addText(format, "مصر للنظم الهندسية");
//
//
//
//
//            iPrinter.addText(format, "Systems Engineering of Egypt");
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.LARGE_DH_32_64_IN_BOLD);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            iPrinter.addText(format, "Systems Engineering of Egypt");
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.HUGE_48);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            iPrinter.addText(format, "Systems Engineering of Egypt");
//
//            iPrinter.feedLine(3);
//
//            // image
//            byte[] buffer = null;
//            InputStream is = null;
//            try {
//                is = this.getAssets().open("verifone_logo.jpg");
//                // get the size
//                int size = is.available();
//                // crete the array of byte
//                buffer = new byte[size];
//                is.read(buffer);
//                // close the stream
//                is.close();
//                Log.d("TAG", "image");
//            } catch (IOException e) {
//                Log.d("TAG", "image fail");
//                e.printStackTrace();
//            }
//
//            if( null != buffer) {
//                Bundle fmtImage = new Bundle();
//                fmtImage.putInt("offset", (384-200)/2);
//                fmtImage.putInt("width", 250);  // bigger then actual, will print the actual
//                fmtImage.putInt("height", 128); // bigger then actual, will print the actual
//                iPrinter.addImage( fmtImage, buffer );
//
//                fmtImage.putInt("offset", 50 );
//                fmtImage.putInt("width", 100 ); // smaller then actual, will print the setting
//                fmtImage.putInt("height", 24); // smaller then actual, will print the setting
//                iPrinter.addImage( fmtImage, buffer );
//            }
//
//            Bundle fmtAddTextInLine = new Bundle();
//            //
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_FORTE );
//            iPrinter.addTextInLine(fmtAddTextInLine, "Verifone X9-Series", "", "", 0);
//            //
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.NORMAL_24_24 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_segoesc );
//            iPrinter.addTextInLine(fmtAddTextInLine, "", "", "This is the Print Demo", 0);
//
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
//            iPrinter.addText(format, "Hello Verifone in font NORMAL_24_24!");
//            // left
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT );
//            iPrinter.addText(format, "Left Alignment long string here: PrinterConfig.addText.Alignment.LEFT ");
//
//            // right
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.RIGHT );
//            iPrinter.addText(format, "Right Alignment  long  string with wrapper here");
//
//            iPrinter.addText(format, "--------------------------------");
//            Bundle fmtAddBarCode = new Bundle();
//            fmtAddBarCode.putInt( PrinterConfig.addBarCode.Alignment.BundleName, PrinterConfig.addBarCode.Alignment.RIGHT );
//            fmtAddBarCode.putInt( PrinterConfig.addBarCode.Height.BundleName, 64 );
//            iPrinter.addBarCode( fmtAddBarCode, "123456 Verifone" );
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.FONT_AGENCYB);
//            iPrinter.addTextInLine(fmtAddTextInLine, "", "123456 Verifone", "", 0);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English );    // set to the default
//
//            iPrinter.addText(format, "--------------------------------");
//
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_ALGER );
//            iPrinter.addTextInLine( fmtAddTextInLine, "Left", "Center", "right", 0);
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_BROADW );
//            iPrinter.addTextInLine( fmtAddTextInLine, "L & R", "", "Divide Equally", 0);
//            iPrinter.addTextInLine( fmtAddTextInLine, "L & R", "", "Divide flexible", PrinterConfig.addTextInLine.mode.Devide_flexible);
//            // left
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT );
//            iPrinter.addText(format, "--------------------------------");
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_segoesc );
//            iPrinter.addTextInLine( fmtAddTextInLine,
//                    "", "",
//                    "Right long string here call addTextInLine ONLY give the right string",
//                    0);
//
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT );
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24 );
//            iPrinter.addText(format, "--------------------------------");
//
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);  // this the default
//            iPrinter.addTextInLine( fmtAddTextInLine, "", "#",
//                    "Right long string with the center string",
//                    0);
//            iPrinter.addText(format, "--------------------------------");
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.SMALL_16_16);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.FONT_AGENCYB);
//            iPrinter.addTextInLine( fmtAddTextInLine, "Print the QR code far from the barcode to avoid scanner found both of them", "",
//                    "",
//                    PrinterConfig.addTextInLine.mode.Devide_flexible);
//
//            Bundle fmtAddQRCode = new Bundle();
//            fmtAddQRCode.putInt( PrinterConfig.addQrCode.Offset.BundleName, 128);
//            fmtAddQRCode.putInt( PrinterConfig.addQrCode.Height.BundleName, 128);
//            iPrinter.addQrCode( fmtAddQRCode, "www.seegypt.com");
//
//            iPrinter.addTextInLine( fmtAddTextInLine, "", "try to scan it",
//                    "",
//                    0);
//
//
//            iPrinter.addText(format, "---------X-----------X----------");
//            iPrinter.feedLine(5);
//            // start print here
//            Log.d("TAG", "end printer");
//            iPrinter.startPrint(new MyListener());
//        } catch (RemoteException e) {
//            Log.d("TAG", "testPrinter fail");
//            e.printStackTrace();
//        }

    }

    void D01002() {
        My01getStatus();
    }

    void D01003() {
        My07startPrint(new MyListener());
        My01getStatus();
    }

    void D01004() {
        D03045();
        try {
            Thread.sleep(2000);
            My01getStatus();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void D01005() {
        My01getStatus();
    }

    void D01006() {
        My01getStatus();
    }

    private void grayTestDemo() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
//        }
//        for (int i = 0; i < 10; i++) {
//            My03addText(format, "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//        }
        logUtils.addCaseLog("文字添加完毕");
        //条形码  D04002
        Bundle format1 = new Bundle();
        //align
        format1.putInt("align", 0);
        format1.putInt("width", 192);
        format1.putInt("height", 128);
        My04addBarCode(format1, "13524044282");
        logUtils.addCaseLog("条形码添加完毕");

        //二维码  5002();
        Bundle format2 = new Bundle();
        format2.putInt("offset", 50);//expectedHeight
        format2.putInt("expectedHeight", 128);
        My05addQrCode(format2, "www.13524044282.qq.com");
        logUtils.addCaseLog("二维码添加完毕");

        //图片D06003();
//        Bundle format3 = new Bundle();
//        format3.putInt("offset", 50);
//        format3.putInt("width", 384);
//        format3.putInt("height", 128);
        D17001();
        logUtils.addCaseLog("图片添加成功");
        My07startPrint(new MyListener());
        My08feedLine(2);
    }

    void D02001() {
        My02setGray(0);
        grayTestDemo();
    }

    void D02002() {
        My02setGray(1);
        grayTestDemo();
    }

    void D02003() {
        My02setGray(2);
        grayTestDemo();
    }

    void D02004() {
        My02setGray(3);
        grayTestDemo();
    }

    void D02005() {
        My02setGray(4);
        grayTestDemo();
    }

    void D02006() {
        My02setGray(5);
        grayTestDemo();
    }

    void D02007() {
        My02setGray(6);
        grayTestDemo();
    }

    void D02008() {
        My02setGray(7);
        grayTestDemo();
    }

    void D02009() {
        My02setGray(-1);
        grayTestDemo();
    }

    void D02010() {
        My02setGray(8);
        grayTestDemo();
    }

    void D03001() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putBoolean("newline", true);
//        format.putFloat("scale_w",(float)1.5);
//        format.putFloat("scale_h",(float)1.5);
        My03addText(format, "ABCDabcd1234测试打印");
//        My07startPrint(new MyListener());
    }

    void D03002() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        format.putBoolean("newline", true);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-小字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03003() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 1);
        format.putBoolean("bold", true);
        My03addText(format, "Aa1测-小字体中对齐加粗");
        My07startPrint(new MyListener());
    }

    void D03004() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 2);
        My03addText(format, "Aa1测-小字体右对齐");
        My07startPrint(new MyListener());
    }

    void D03005() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-中字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03006() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 1);
        My03addText(format, "Aa1测-中字体中对齐");
        My07startPrint(new MyListener());
    }

    void D03007() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 2);
        My03addText(format, "Aa1测-中字体右对齐");
        My07startPrint(new MyListener());
    }

    void D03008() {
        Bundle format = new Bundle();
        format.putInt("font", 2);
        format.putInt("align", 0);
        My03addText(format, "Aa1测-中字体2倍高粗左对齐");
        My07startPrint(new MyListener());
    }

    void D03009() {
        Bundle format = new Bundle();
        format.putInt("font", 2);
        format.putInt("align", 1);
        My03addText(format, "Aa1测-中字体2倍高粗中对齐");
        My07startPrint(new MyListener());
    }

    void D03010() {
        Bundle format = new Bundle();
        format.putInt("font", 2);
        format.putInt("align", 2);
        My03addText(format, "Aa1测-中字体2倍高粗右对齐");
        My07startPrint(new MyListener());
    }

    void D03011() {
        Bundle format = new Bundle();
        format.putInt("font", 3);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03012() {
        Bundle format = new Bundle();
        format.putInt("font", 3);
        format.putInt("align", 1);
        format.putBoolean("bold", true);
        My03addText(format, "Aa1测-大字体中对齐加粗");
        My07startPrint(new MyListener());
    }

    void D03013() {
        Bundle format = new Bundle();
        format.putInt("font", 3);
        format.putInt("align", 2);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大字体右对齐");
        My07startPrint(new MyListener());
    }

    void D03014() {
        Bundle format = new Bundle();
        format.putInt("font", 4);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大字体2倍高粗左对齐");
        My07startPrint(new MyListener());
    }

    void D03015() {
        Bundle format = new Bundle();
        format.putInt("font", 4);
        format.putInt("align", 1);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大字体2倍高粗中对齐");
        My07startPrint(new MyListener());
    }

    void D03016() {
        Bundle format = new Bundle();
        format.putInt("font", 4);
        format.putInt("align", 2);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大字体2倍高粗右对齐");
        My07startPrint(new MyListener());
    }

    void D03017() {
        Bundle format = new Bundle();
        format.putInt("font", 5);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-巨大字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03018() {
        Bundle format = new Bundle();
        format.putInt("font", 5);
        format.putInt("align", 1);
        format.putBoolean("bold", true);
        My03addText(format, "Aa1测-巨大字体中对齐加粗");
        My07startPrint(new MyListener());
    }

    void D03019() {
        Bundle format = new Bundle();
        format.putInt("font", 5);
        format.putInt("align", 2);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-巨大字体右对齐");
        My07startPrint(new MyListener());
    }

    void D03020() {
        Bundle format = new Bundle();
        format.putInt("font", 6);
        format.putInt("align", 0);
        My03addText(format, "Aa1测-中字体2倍宽粗左对齐");
        My07startPrint(new MyListener());
    }

    void D03021() {
        Bundle format = new Bundle();
        format.putInt("font", 6);
        format.putInt("align", 1);
        My03addText(format, "Aa1测-中字体2倍宽粗中对齐");
        My07startPrint(new MyListener());
    }

    void D03022() {
        Bundle format = new Bundle();
        format.putInt("font", 6);
        format.putInt("align", 2);
        My03addText(format, "Aa1测-中字体2倍宽粗右对齐");
        My07startPrint(new MyListener());
    }

    void D03023() {
        Bundle format = new Bundle();
        format.putInt("font", 7);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大2倍宽粗左对齐");
        My07startPrint(new MyListener());
    }

    void D03024() {
        Bundle format = new Bundle();
        format.putInt("font", 7);
        format.putInt("align", 1);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大2倍宽粗中对齐");
        My07startPrint(new MyListener());
    }

    void D03025() {
        Bundle format = new Bundle();
        format.putInt("font", 7);
        format.putInt("align", 2);
        format.putBoolean("bold", false);
        My03addText(format, "Aa1测-大2倍宽粗右对齐");
        My07startPrint(new MyListener());
    }

    void D03026() {
        Bundle format = new Bundle();
        format.putInt("font", -1);
        format.putInt("align", 0);
        My03addText(format, "Aa1测-中字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03027() {
        Bundle format = new Bundle();
        format.putInt("font", 8);
        format.putInt("align", 0);
        My03addText(format, "Aa1测-中字体左对齐");
        My07startPrint(new MyListener());
    }


    void D03028() {
        Bundle format = new Bundle();
//        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "Aa1测-中字体左对齐");
        My07startPrint(new MyListener());
    }


    void D03029() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", -1);
        My03addText(format, "Aa1测-小字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03030() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 3);
        My03addText(format, "Aa1测-小字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03031() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
//        format.putInt("align", 3);
        My03addText(format, "Aa1测-小字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03032() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("newline", true);
        My03addText(format, "前一行未占满oneline");
        My03addText(format, "换行参数测试");
        //某一行很长必须换行
        My03addText(format, "前一行很长，换行后仍未占满onelineoneday");
        My03addText(format, "换行参数测试");
        My07startPrint(new MyListener());
    }

    void D03033() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("newline", true);
        My03addText(format, "换行参数测试：前一行占满已经占满");
        format.putBoolean("newline", true);
        My03addText(format, "换行参数测试");
        My07startPrint(new MyListener());
    }

    void D03034() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("newline", false);
        My03addText(format, "前一行未占满oneline");
        My03addText(format, "换行参数测试");
        My03addText(format, "前一行很长，换行后仍未占满onelineoneday");
        My03addText(format, "换行参数测试");
        My07startPrint(new MyListener());
    }

    void D03035() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("newline", false);
        My03addText(format, "换行参数测试：前一行占满已经占满");
        format.putBoolean("newline", false);
        My03addText(format, "换行参数测试");
        My07startPrint(new MyListener());
    }

    void D03036() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "换行参数测试：前一行占满已经占满");
        My03addText(format, "换行参数测试");
        My07startPrint(new MyListener());
    }

    void D03037() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", true);
        My03addText(format, "Aa1测-中字体加粗左对齐");
        My07startPrint(new MyListener());
    }

    void D03038() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
//        format.putBoolean("bold",false);
        My03addText(format, "Aa1测-中字体左对齐");
        My07startPrint(new MyListener());
    }

    void D03039() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 2);
        My03addText(format, "ABCabc长字符串右对齐可换行户终端号类型日期时间凭证签名卡号四川省德阳市旌阳区1234567890RMB:/*（）SI,-自动换行测试");
        My07startPrint(new MyListener());
    }

    void D03040() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        My03addText(format, null);
        My07startPrint(new MyListener());
    }

    void D03041() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        My03addText(format, "");
        My07startPrint(new MyListener());
    }

    void D03042() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "          10个空格");
        My07startPrint(new MyListener());
    }

    void D03043() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "！@#￥%……&*（）——+-={}【】|、《》，。、？‘“；：:\"><?/\\|][}{:;’‘");
        My07startPrint(new MyListener());
    }

    void D03044() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "囧槑（mei）烎(yin)氼（ni）嘂（jiao）嘦（jiao）嫑（biao）圐（ku）圙（lue）玊（su）孖（ma）丼（jing）嬛(huan)\"甯(ning)寗(ning)");
        My07startPrint(new MyListener());
    }

    void D03045() {
        Bundle format = new Bundle();
        //align
        format.putInt("font", 0);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putBoolean("newline", false);
        //"!@#$ *().::{}^&<>?""甯(ning)
        String text = "";
        for (int i = 0; i < 1000; i++) {
//        for (int i = 0; i < 10; i++) {
            text = text + "国";
        }
        My03addText(format, text);
        logUtils.addCaseLog("大数据打印,当前放置的1000个国");
        My07startPrint(new MyListener());
    }

    void D03046() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "ABCDefg\n中国农业银行");
        My07startPrint(new MyListener());
    }

    void D03047() {
        for (int i = 0; i < 10; i++) {
            Bundle format = new Bundle();
            format.putInt("font", 1);
            format.putInt("align", 0);
            My03addText(format, "第 " + i + " 次添加文本");
//            My07startPrint(new MyListener());
        }


        My07startPrint(new MyListener());
    }

    void D03048() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学四川省德阳市旌阳区东电中学");
        My07startPrint(new MyListener());
        try {

            Thread.sleep(1000);
            My03addText(format, "打印过程中添加文本");
            Thread.sleep(10000);
            My07startPrint(new MyListener());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void D03049() {
        My03addText(null, "ABCDefg\\n中国农业银行");
        My07startPrint(new MyListener());
    }

    void D03050() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", null);
        My03addText(format, "默认中文打印0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        My07startPrint(new MyListener());
    }

    void D03051() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "abcdefg");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03052() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03053() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        My03addText(format, "默认英文打印0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        My07startPrint(new MyListener());
    }

    void D03054() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03055() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "/system/fonts/fzzdx.ttf");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03056() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "/system/fonts/fzzdx.ttf");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03057() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "/sdcard/download/ARIALNB.TTF");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03058() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "");
        My03addText(format, "白日依山尽黄河入海流");
        format.putString("fontStyle", "/system/fonts/SIMKAI.TTF");
        My03addText(format, "白日依山尽黄河入海流");
        My07startPrint(new MyListener());
    }

    void D03059() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "/system/fonts/simsun.ttc");
        My03addText(format, "白日依山尽黄河入海流");
        My07startPrint(new MyListener());
    }

    void D03060() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "/system/fonts/abcd.ttf");
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03061() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putFloat("scale_w", (float) 1.5);
        format.putFloat("scale_h", (float) 1.5);
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03062() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        format.putBoolean("bold", true);
        format.putFloat("scale_w", (float) 2);
        format.putFloat("scale_h", (float) 2);
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D03063() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("newline", true);
        My03addText(format, "S_________________");
        My03addText(format, "S_______________________");
        My03addText(format, "S____----------——————--______________");
        My07startPrint(new MyListener());
    }

    void D03064() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("newline", true);
        format.putBoolean("isColorInvert", true);
        My03addText(format, "S_________________");
        My03addText(format, "S_______________________");
        My03addText(format, "S____----------——————--______________");
        My07startPrint(new MyListener());
    }


    void D04001() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
    }

    void D04002() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04003() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04004() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04005() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 1);
        My04addBarCode(format, "A1234567890$a");
        My07startPrint(new MyListener());
    }

    void D04006() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 1);
        My04addBarCode(format, "A1234567890$a");
        My07startPrint(new MyListener());
    }

    void D04007() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 1);
        My04addBarCode(format, "A1234567890$a");
        My07startPrint(new MyListener());
    }

    void D04008() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 2);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04009() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 2);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04010() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 2);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04011() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 3);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04012() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04013() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 5);
        My04addBarCode(format, "1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04014() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 6);
        My04addBarCode(format, "47112346");
        My07startPrint(new MyListener());
    }

    void D04015() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 7);
        My04addBarCode(format, "6901234567892");
        My07startPrint(new MyListener());
    }

    void D04016() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 8);
        My04addBarCode(format, "12345678901234");
        My07startPrint(new MyListener());
    }

    /**
     * 3.10版本的service不支持 MAXICODE QR_COD  RSS_14 UPC_EAN_EXTENSION码质，对应D04017/D04020/D04021/D04014 case
     */


   /* void D04017() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType",9);
        My04addBarCode(format, "1234%5678aW");
        My07startPrint(new MyListener());
    }*/

    void D04018() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 10);
        My04addBarCode(format, "1234%567890aW");
        My07startPrint(new MyListener());
    }

    void D04019() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 11);
        My04addBarCode(format, "1234%567890aW");
        My07startPrint(new MyListener());
    }

  /*  void D04020() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType",12);
        My04addBarCode(format, "0120012345678909");
        My07startPrint(new MyListener());
    }*/

//     void D04021() {
//         Bundle format = new Bundle();
//         format.putInt("align", 1);
//         format.putInt("height", 128);
//         format.putInt("barCodeType",13);
//         My04addBarCode(format, "0120044501754127");
//         My07startPrint(new MyListener());
//     }

    void D04022() {
        Bundle format = new Bundle();
        format.putInt("align", 2);
        format.putInt("height", 128);
        format.putInt("barCodeType", 14);
        My04addBarCode(format, "45631431967");
        My07startPrint(new MyListener());
    }

    void D04023() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 15);
        My04addBarCode(format, "04029311");
        My07startPrint(new MyListener());
    }

   /* void D04024() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType",16);
        My04addBarCode(format, "45631431967");
        My07startPrint(new MyListener());
    }*/

    void D04025() {
        Bundle format = new Bundle();
        format.putInt("align", -1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04026() {
        Bundle format = new Bundle();
        format.putInt("align", 3);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04027() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", -1);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04028() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 128);
        format.putInt("barCodeType", 17);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04029() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 0);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04030() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", -1);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04031() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 200);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04032() {
        Bundle format = new Bundle();
        format.putInt("align", 0);
        format.putInt("height", 384);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04033() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 385);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04034() {
        My04addBarCode(null, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04035() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, null);
        My07startPrint(new MyListener());
    }

    void D04036() {
        Bundle format = new Bundle();
        format.putInt("align", 1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "");
        My07startPrint(new MyListener());
    }

    void D05001() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt("expectedHeight", 128);
        My05addQrCode(format, "18620518880");
    }

    void D05002() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt("expectedHeight", 128);
        My05addQrCode(format, "18620518880");
        My07startPrint(new MyListener());
    }

    void D05003() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt("expectedHeight", 128);
        My05addQrCode(format, "18620518880@qq.com");
        My07startPrint(new MyListener());
    }

    void D05004() {
        Bundle format = new Bundle();
        format.putInt("offset", -1);//expectedHeight
        format.putInt("expectedHeight", 128);
        My05addQrCode(format, "18620518880@qq.com");
        My07startPrint(new MyListener());
    }

    void D05005() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt("expectedHeight", 334);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05006() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt("expectedHeight", 335);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05008() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt("expectedHeight", -1);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05009() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt("expectedHeight", 0);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05010() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt("expectedHeight", 128);
        My05addQrCode(format, null);
        My07startPrint(new MyListener());
    }

    void D05011() {
        Bundle format = new Bundle();
        format.putInt("offset", 10);//expectedHeight
        format.putInt("expectedHeight", 192);
        My05addQrCode(format, "");
        My07startPrint(new MyListener());
    }

    void D05012() {
        Bundle format = new Bundle();
        format.putInt("offset", 10);//expectedHeight
        format.putInt("expectedHeight", 192);
        My05addQrCode(format, "     ");
        My07startPrint(new MyListener());
    }

    void D05013() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt("expectedHeight", 192);
        My05addQrCode(format, "甯(ning)寗");
        My07startPrint(new MyListener());
    }

    void D05014() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt("expectedHeight", 192);
        My05addQrCode(format, "！@#￥%……");
        My07startPrint(new MyListener());
    }

    void D05015() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt("expectedHeight", 384);
        String str = "";
        for (int i = 0; i < 1108; i++)//559
        {
            str = str + i;
        }
        My05addQrCode(format, str);
        My07startPrint(new MyListener());
    }

    void D05016() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt("expectedHeight", 384);
        String str = "";
        for (int i = 0; i < 1500; i++)//559
        {
            str = str + i;
        }
        My05addQrCode(format, str);
        My07startPrint(new MyListener());
    }

    void D05017() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt("expectedHeight", 1);
        My05addQrCode(format, "597488968");
        My07startPrint(new MyListener());
    }

    void D05018() {
        My05addQrCode(null, "597488968");
        My07startPrint(new MyListener());
    }

    void D05019() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt("expectedHeight", 385);
        My05addQrCode(format, "597488968@qq.com");
        My07startPrint(new MyListener());
    }

    void D17002() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.simple);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
        }
    }

    void D17003() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.head);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17004() {
        Bundle format = new Bundle();
        format.putInt("offset", 10);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.head);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17005() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.head);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17006() {
        Bundle format = new Bundle();
        format.putInt("offset", 200);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.head);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17007() {
        Bundle format = new Bundle();
        format.putInt("offset", -1);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.simple);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17008() {
        Bundle format = new Bundle();
        format.putInt("offset", 384);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.simple);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17009() {
        Bundle format = new Bundle();
        format.putInt("offset", 385);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.simple);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17010() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.girl);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17011() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.cat);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17012() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.woman);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17013() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.panda);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17014() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.barcode);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17015() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.qrcode);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17016() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.caise);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17017() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.empty);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17018() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.big);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
    }

    void D17019() {

        D03028();

        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap bitmap = getBitmapByte(R.drawable.simple);
        if (bitmap != null) {
            My17addBmpImage(format, bitmap);
            My07startPrint(new MyListener());
        }
        D03028();

//        try {
//            Thread.sleep(10000);
//            My07startPrint(new MyListener());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    void D17020() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap imgs = getBitmapByte(R.drawable.cat);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.woman);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.panda);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.bee);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.girl);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.barcode);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.qrcode);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.caise);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.empty);
        My17addBmpImage(format, imgs);
        imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17021() {

        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap imgs = getBitmapByte(R.drawable.caise);
        if (imgs != null) {
            My03addText(format, "向日葵！");
            My17addBmpImage(format, imgs);
        }
        imgs = getBitmapByte(R.drawable.bee);
        if (imgs != null) {
            My03addText(format, "小蜜蜂");
            My17addBmpImage(format, imgs);
        }
        My07startPrint(new MyListener());

    }

//    void D06021() {
//        Bundle format = new Bundle();
//        format.putInt("offset", 50);
//        format.putInt("gray", 100);
//        My06addImage(format, "".getBytes());
//        My07startPrint(new MyListener());
//    }

//    void D06022() {
//        Bundle format = new Bundle();
//        format.putInt("offset", 50);
//        format.putInt("gray", 100);
//        My06addImage(format, "     ".getBytes());
//        My07startPrint(new MyListener());
//    }

    void D17022() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);
        format.putInt("gray", 100);
        My17addBmpImage(format, null);
        My07startPrint(new MyListener());
    }

    void D17023() {
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(null, imgs);
        My07startPrint(new MyListener());
    }

    void D17024() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 0);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17025() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 100);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17026() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 200);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17027() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 255);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17028() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", -1);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17029() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("gray", 256);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
        My17addBmpImage(format, imgs);
        My07startPrint(new MyListener());
    }

    void D17030() {
        //把小票放在sdcard/download下，名称如下，可以适配384宽度打印小票。
        // 文件可以在res/drawable里复制，如果要用其他的小票，也请改成这个名称。
        Bundle format = new Bundle();

        Bitmap bitmap = getBitmapFromPath("/sdcard/Download/receipt.png");
        logUtils.addCaseLog("bitmap.getWidth() "+bitmap.getWidth()+";bitmap.getHeight()="+bitmap.getHeight());
        format.putInt("offset", 0);
        format.putInt("width", bitmap.getWidth());
        format.putInt("height", bitmap.getHeight());
        My17addBmpImage(format, bitmap);
        My07startPrint(new MyListener());
    }
    private Bitmap getBitmapFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    void D07001() {
        My07startPrint(new MyListener());
    }

    void D07002() {
        Bundle format = new Bundle();
        //align
        format.putInt("font", 0);
        format.putInt("align", 0);
        My03addText(format, "ABCDabcd1234测试打印");
        My07startPrint(new MyListener());
    }

    void D07003() {
        D04001();
        My07startPrint(new MyListener());
    }

    void D07004() {
        D05001();
        My07startPrint(new MyListener());
    }

    void D07006() {
        My07startPrint(null);
    }

    void D07007() {
        D07002();
    }

    void D07011() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        My03addText(format, "中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学中国四川省德阳市旌阳区东电中学");
        My07startPrint(new MyListener());
      /*  try{
        Thread.sleep(10000);}
        catch (Exception e){
            e.printStackTrace();
        }*/
        My07startPrint(new MyListener());
    }

    void D07014() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                logUtils.addCaseLog("多线程执行");
                D03028();
            }
        }.start();

        ((MyApplication) context).serviceMoudle.getBeerMoudle().My1startBeep(10000);
    }

    void D07015() {
        D03028();
        ((MyApplication) context).serviceMoudle.getLedMoudle().My01turnOn(1);
    }

    void D07017() {
        D03028();
        ((MyApplication) context).serviceMoudle.getInsertCardReaderMoudle().My03isCardIn(0);

    }

    void D07018() {
        D03028();
        ((MyApplication) context).serviceMoudle.getIrfCardReaderMoudle().J05001();

    }

    void D07019() {
        Bundle format = new Bundle();
        //align
        format.putInt("font", 0);
        format.putInt("align", 0);
        String str = "";
        for (int i = 0; i < 500; i++) {
            str = str + "国";
        }
        My03addText(format, str);
        My07startPrint(new MyListener());
    }

    void D07020() {

        Bundle format = new Bundle();
        //align
        format.putInt("font", 0);
        format.putInt("align", 0);
        String str = "";

        for (int i = 0; i < 40; i++) {
            str = "";
//            str = String.valueOf(i) + "测试数据\n";
            str = String.valueOf(i) + "测试数据\n";
            My03addText(format, str);
        }

        My07startPrint(new MyListener());
        logUtils.addCaseLog("请断电重启...");
        logUtils.addCaseLog("之后执行D07021");

    }

    void D07021() {

        logUtils.addCaseLog("断电重启后，直接调用startPrint");
        My07startPrint(new MyListener());

    }

    void D07022() {
        Bundle format = new Bundle();
        format.putInt("font", 0);
        format.putInt("align", 0);
        String str = "第1次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第2次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第3次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第4次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第5次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第6次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第7次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第8次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第9次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "第10次打印四川省东电中学";
        My03addText(format, str);
        My07startPrint(new MyListener());
    }

//    void D07023() {
//
//        Log.d(TAG, "testPrinter");
//        // bundle format for addText
//        Bundle format = new Bundle();
//
//        try {
//
//            Bundle fmtAddTextInLineِTest = new Bundle();
//            //
//            fmtAddTextInLineِTest.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.NORMAL_24_24 );
//            fmtAddTextInLineِTest.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_ALGER );
//
//            iPrinter.addTextInLine(fmtAddTextInLineِTest,
//                    "", "",
//                    "فرعي  شقة  عمارة  بلوك  قطاع  يومية",
//                    0);
//
//            iPrinter.addTextInLine(fmtAddTextInLineِTest,
//                    "", "",
//                    "64    64    12    10    01    00",
//                    0);
//
//
//
//            iPrinter.addTextInLine(fmtAddTextInLineِTest,
//                    "", "",
//                    "مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية مصر للنظم الهندسية ",
//                    0);
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT);
//            iPrinter.addText(format, "مصر للنظم الهندسية");
//
//
//
//
//            iPrinter.addText(format, "Systems Engineering of Egypt");
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.LARGE_DH_32_64_IN_BOLD);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            iPrinter.addText(format, "Systems Engineering of Egypt");
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.HUGE_48);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            iPrinter.addText(format, "Systems Engineering of Egypt");
//
//            iPrinter.feedLine(3);
//
//            // image
//            byte[] buffer = null;
//            InputStream is = null;
//            try {
//                is = this.getAssets().open("verifone_logo.jpg");
//                // get the size
//                int size = is.available();
//                // crete the array of byte
//                buffer = new byte[size];
//                is.read(buffer);
//                // close the stream
//                is.close();
//                Log.d(TAG, "image");
//            } catch (IOException e) {
//                Log.d(TAG, "image fail");
//                e.printStackTrace();
//            }
//
//            if( null != buffer) {
//                Bundle fmtImage = new Bundle();
//                fmtImage.putInt("offset", (384-200)/2);
//                fmtImage.putInt("width", 250);  // bigger then actual, will print the actual
//                fmtImage.putInt("height", 128); // bigger then actual, will print the actual
//                iPrinter.addImage( fmtImage, buffer );
//
//                fmtImage.putInt("offset", 50 );
//                fmtImage.putInt("width", 100 ); // smaller then actual, will print the setting
//                fmtImage.putInt("height", 24); // smaller then actual, will print the setting
//                iPrinter.addImage( fmtImage, buffer );
//            }
//
//            Bundle fmtAddTextInLine = new Bundle();
//            //
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_FORTE );
//            iPrinter.addTextInLine(fmtAddTextInLine, "Verifone X9-Series", "", "", 0);
//            //
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.NORMAL_24_24 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_segoesc );
//            iPrinter.addTextInLine(fmtAddTextInLine, "", "", "This is the Print Demo", 0);
//
//
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
//            iPrinter.addText(format, "Hello Verifone in font NORMAL_24_24!");
//            // left
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT );
//            iPrinter.addText(format, "Left Alignment long string here: PrinterConfig.addText.Alignment.LEFT ");
//
//            // right
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.RIGHT );
//            iPrinter.addText(format, "Right Alignment  long  string with wrapper here");
//
//            iPrinter.addText(format, "--------------------------------");
//            Bundle fmtAddBarCode = new Bundle();
//            fmtAddBarCode.putInt( PrinterConfig.addBarCode.Alignment.BundleName, PrinterConfig.addBarCode.Alignment.RIGHT );
//            fmtAddBarCode.putInt( PrinterConfig.addBarCode.Height.BundleName, 64 );
//            iPrinter.addBarCode( fmtAddBarCode, "123456 Verifone" );
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.FONT_AGENCYB);
//            iPrinter.addTextInLine(fmtAddTextInLine, "", "123456 Verifone", "", 0);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English );    // set to the default
//
//            iPrinter.addText(format, "--------------------------------");
//
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_ALGER );
//            iPrinter.addTextInLine( fmtAddTextInLine, "Left", "Center", "right", 0);
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_BROADW );
//            iPrinter.addTextInLine( fmtAddTextInLine, "L & R", "", "Divide Equally", 0);
//            iPrinter.addTextInLine( fmtAddTextInLine, "L & R", "", "Divide flexible", PrinterConfig.addTextInLine.mode.Devide_flexible);
//            // left
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT );
//            iPrinter.addText(format, "--------------------------------");
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32 );
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_segoesc );
//            iPrinter.addTextInLine( fmtAddTextInLine,
//                    "", "",
//                    "Right long string here call addTextInLine ONLY give the right string",
//                    0);
//
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT );
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24 );
//            iPrinter.addText(format, "--------------------------------");
//
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);  // this the default
//            iPrinter.addTextInLine( fmtAddTextInLine, "", "#",
//                    "Right long string with the center string",
//                    0);
//            iPrinter.addText(format, "--------------------------------");
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.SMALL_16_16);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.FONT_AGENCYB);
//            iPrinter.addTextInLine( fmtAddTextInLine, "Print the QR code far from the barcode to avoid scanner found both of them", "",
//                    "",
//                    PrinterConfig.addTextInLine.mode.Devide_flexible);
//
//            Bundle fmtAddQRCode = new Bundle();
//            fmtAddQRCode.putInt( PrinterConfig.addQrCode.Offset.BundleName, 128);
//            fmtAddQRCode.putInt( PrinterConfig.addQrCode.Height.BundleName, 128);
//            iPrinter.addQrCode( fmtAddQRCode, "www.seegypt.com");
//
//            iPrinter.addTextInLine( fmtAddTextInLine, "", "try to scan it",
//                    "",
//                    0);
//
//
//            iPrinter.addText(format, "---------X-----------X----------");
//            iPrinter.feedLine(5);
//            // start print here
//            Log.d(TAG, "end printer");
//            iPrinter.startPrint(new MyListener());
//        } catch (RemoteException e) {
//            Log.d(TAG, "testPrinter fail");
//            e.printStackTrace();
//        }
//
//
//    }

    void D08001() {
        My08feedLine(1);
        My07startPrint(new MyListener());
    }

    void D08002() {
        My08feedLine(100);
        My07startPrint(new MyListener());
    }

    void D08003() {
        My08feedLine(0);
        My07startPrint(new MyListener());
    }

    void D08004() {
        My08feedLine(-1);
        My07startPrint(new MyListener());
    }

    void D08006() {
//        Bundle format = new Bundle();
//        format.putInt("font", 0);
//        format.putInt("align", 0);
//        My03addText(format, "TVR VALUE");
////        My07startPrint(new MyListener());
//        My08feedLine(2);
////        My07startPrint(new MyListener());
//        format.putInt("font", 2);
//        format.putInt("align", 0);
//        My03addText(format, "AMOUNT");


        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "TVR VALUE";
        String mstr = "";
        String rstr = "0000000";
        int mode = 2;
        My03addText(format, "****************************");
        My11addTextInLine(format, lstr, mstr, rstr, mode);
///////////////////////////
        My08feedLine(2);
///////////////////////////
        Bundle format1 = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr1 = "AMOUNT";
        String mstr1 = "";
        String rstr1 = "0.80";
        int mode1 = 0;
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode1);

        My03addText(format, "****************************");

        My07startPrint(new MyListener());
    }

    void D08007() {
        My08feedLine(100);
        My07startPrint(new MyListener());
    }

    void D08008() {
        My08feedLine(100);
        My07startPrint(new MyListener());
    }

    void D08011() {
        D03028();
        try {
            Thread.sleep(1000);
            My08feedLine(10);
            Thread.sleep(5000);
            My07startPrint(new MyListener());
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    void D08015() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                logUtils.addCaseLog("多线程执行");
                My08feedLine(10);
                My07startPrint(new MyListener());
            }
        }.start();

        My08feedLine(12);
        My07startPrint(new MyListener());
    }

    void D08018() {
        My08feedLine(5);
        My07startPrint(new MyListener());
    }

    void D08019() {
        My08feedLine(50);
        My07startPrint(new MyListener());
    }

    void D08020() {
        My08feedLine(70);
        My07startPrint(new MyListener());
    }

    void D08021() {
        My08feedLine(5);
    }


    void D09001() {
        My09addQrCodesInLine(null);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09002() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09003() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(385, 0, "www.baidu.com");//第一个入参height，第二个入参leftOffset
        qrCodes.add(qrCodeContent1);

        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09004() {//同一行打印多个二维码
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 0, "www.baidu.com");//第一个入参height，第二个入参leftOffset
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09005() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, 1, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
    }

    void D09006() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, -1, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, -1, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09007() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, 0, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, 0, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09008() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(-1, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(-1, 0, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09009() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(0, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(0, 0, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09010() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(385, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(385, 0, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09011() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, 5, null);
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, 0, null);
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09012() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, 5, "");
        qrCodes.add(qrCodeContent3);
        QrCodeContent qrCodeContent4 = new QrCodeContent(80, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent4);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, 0, "");
        qrCodes.add(qrCodeContent5);
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09013() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");//第一个入参height，第二个入参leftOffset
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(60, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(70, 5, "www.baidu.com");
        qrCodes.add(qrCodeContent3);
        qrCodes.add(null);
        QrCodeContent qrCodeContent5 = new QrCodeContent(104, 0, "www.baidu.com");
        qrCodes.add(qrCodeContent5);
        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09014() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(150, 20, "www.baidu.com");//第一个入参height，第二个入参leftOffset
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(150, 20, "www.baidu.com");
        qrCodes.add(qrCodeContent2);

        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09015() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(100, 20, "www.baidu.com");//第一个入参height，第二个入参leftOffset
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(100, 20, "www.qq.com");
        qrCodes.add(qrCodeContent2);
        QrCodeContent qrCodeContent3 = new QrCodeContent(100, 20, "www.jingdong.com");
        qrCodes.add(qrCodeContent3);

        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D10001() {
        My10setLineSpace(-1);

        Bundle format = new Bundle();
        My03addText(format, "行间距测试--两个黄鹂鸣翠柳，一行白鹭上青天，窗含西岭千秋雪，门泊东吴万里船");
        My07startPrint(new MyListener());
    }

    void D10002() {
        My10setLineSpace(0);

        Bundle format = new Bundle();
        My03addText(format, "行间距测试--两个黄鹂鸣翠柳，一行白鹭上青天，窗含西岭千秋雪，门泊东吴万里船");
        My07startPrint(new MyListener());
    }

    void D10003() {
        My10setLineSpace(5);

        Bundle format = new Bundle();
        My03addText(format, "行间距测试--两个黄鹂鸣翠柳，一行白鹭上青天，窗含西岭千秋雪，门泊东吴万里船");
        My07startPrint(new MyListener());
    }

    void D10004() {
        My10setLineSpace(50);

        Bundle format = new Bundle();
        My03addText(format, "行间距测试--两个黄鹂鸣翠柳，一行白鹭上青天，窗含西岭千秋雪，门泊东吴万里船");
        My07startPrint(new MyListener());
    }

    void D11004() {

        My10setLineSpace(50);
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D10005() {
        My10setLineSpace(51);

        Bundle format = new Bundle();
        My03addText(format, "行间距测试--两个黄鹂鸣翠柳，一行白鹭上青天，窗含西岭千秋雪，门泊东吴万里船");
        My07startPrint(new MyListener());
    }

    void D10006() {
        My10setLineSpace(1);

        Bundle format = new Bundle();
        My03addText(format, "行间距测试--两个黄鹂鸣翠柳，一行白鹭上青天，窗含西岭千秋雪，门泊东吴万里船");
        My07startPrint(new MyListener());
    }

    void D11001() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
//        My07startPrint(new MyListener());
    }

    void D11002() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        format.putFloat("scale_w", (float) 1.5);
        format.putFloat("scale_h", (float) 1.5);
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11003() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11005() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽 黄河入海流";
        String mstr = "床前明月光 疑是地上霜";
        String rstr = "锄禾日当午 汗滴禾下土";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11006() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = " 白日依山尽 黄河入海流 ";
        String mstr = " 床前明月光 疑是地上霜 ";
        String rstr = " 举头望明月 低头思故乡 ";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11007() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "床前明月光";
        String rstr = "疑是地上霜";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11008() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "疑是地上霜";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11009() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "疑是地上霜";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11010() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11011() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11012() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11013() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String mstr = "";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11014() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11015() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "";
        String rstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11016() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11017() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11018() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11019() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11020() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽 黄河入海流";
        String mstr = "床前明月光 疑是地上霜";
        String rstr = "锄禾日当午 汗滴禾下土";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11021() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = " 白日依山尽 黄河入海流 ";
        String mstr = " 床前明月光 疑是地上霜 ";
        String rstr = " 举头望明月 低头思故乡 ";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11022() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "床前明月光";
        String rstr = "疑是地上霜";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11023() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "疑是地上霜";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11024() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "疑是地上霜";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11025() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11026() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11027() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11028() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String mstr = "";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11029() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11030() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "";
        String rstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11031() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11032() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11033() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11034() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11035() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11036() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11037() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11038() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽 黄河入海流";
        String mstr = "床前明月光 疑是地上霜";
        String rstr = "锄禾日当午 汗滴禾下土";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11039() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = " 白日依山尽 黄河入海流 ";
        String mstr = " 床前明月光 疑是地上霜 ";
        String rstr = " 举头望明月 低头思故乡 ";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11040() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "床前明月光";
        String rstr = "疑是地上霜";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11041() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "疑是地上霜";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11042() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "疑是地上霜";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11043() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11044() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11045() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11046() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String mstr = "";
        String rstr = "";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11047() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11048() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "";
        String rstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11049() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11050() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11051() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11052() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11053() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11054() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11055() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11056() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11057() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11058() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽 黄河入海流";
        String mstr = "床前明月光 疑是地上霜";
        String rstr = "锄禾日当午 汗滴禾下土";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11059() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = " 白日依山尽 黄河入海流 ";
        String mstr = " 床前明月光 疑是地上霜 ";
        String rstr = " 举头望明月 低头思故乡 ";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11060() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "床前明月光";
        String rstr = "疑是地上霜";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11061() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "疑是地上霜";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11062() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光";
        String mstr = "疑是地上霜";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11063() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11064() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String mstr = "";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11065() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        String rstr = "云想衣裳花想容，春风拂槛露华浓，若非群玉山头见，会向瑶台月下逢";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11066() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String mstr = "";
        String rstr = "";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11067() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11068() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "";
        String mstr = "";
        String rstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11069() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11070() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11071() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11072() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11073() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11074() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼";
        String mstr = "床前明月光，疑是地上霜，举头望明月，低头思故乡";
        String rstr = "锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11075() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Chinese");
        String lstr = null;
        String mstr = null;
        String rstr = null;
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11076() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11077() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible P";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11078() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible P";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11079() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11080() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11081() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11082() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11083() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11084() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "system/fonts/SIMKAI.TTF");
        String lstr = "床前明月光";
        String mstr = "疑是地上霜";
        String rstr = "举头望明月";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11085() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "system/SIMKAI.TTF");
        String lstr = "床前明月光";
        String mstr = "疑是地上霜";
        String rstr = "举头望明月";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11086() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "abcdefg");
        String lstr = "time";
        String mstr = "month";
        String rstr = "year";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11087() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", null);
        String lstr = "time";
        String mstr = "month";
        String rstr = "year";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11088() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", true);
        format.putString("fontStyle", "");
        String lstr = "time";
        String mstr = "month";
        String rstr = "year";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11089() {
        Bundle format = new Bundle();
        format.putInt("fontSize", -1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    /**
     * Redmine issue #298
     * Likai：建议使用canvas自己绘制成图片，然后调用我们的图片打印功能完成此需求，建议关闭此bug
     * <p>
     * tester：同步在代码中注销该案例
     */
    void D11090() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 4);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11091() {
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 2;
        My11addTextInLine(null, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11092() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "白日依山尽 白日依山尽";
        String mstr = "黄河入海流 白日依山尽";
        String rstr = "欲穷千里目 白日依山尽";
        int mode = -1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11093() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11094() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11095() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11096() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11097() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11098() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11099() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11100() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11101() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "system/fonts/fzzdx.ttf");
        String lstr = "sun";
        String mstr = "moon";
        String rstr = "start";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11102() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "/sdcard/Download/VerifoneArabic.ttf");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11103() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "system/fonts/abcd.ttf");
        String lstr = "白日依山尽";
        String mstr = "黄河入海流";
        String rstr = "欲穷千里目";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11104() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11105() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11106() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11107() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11108() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11109() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11110() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11111() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11112() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "床前明月光 疑是地上霜霜";
        String mstr = "床前明月光光光 疑是地上霜霜霜";
        String rstr = "床前明月光 疑是地上霜霜";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11113() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Chinese");
        String lstr = "AMOUNT:";
        String mstr = "";
        String rstr = "RRN:000008000000 EXPIRY:XXXXXX";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11114() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "sdcard/download/ARIALNB.TTF");
        String lstr = "SEQUENCE:0008";
        String mstr = "";
        String rstr = "RRN:000008000000";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11115() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "sdcard/download/ARIALNB.TTF");
        String lstr = "SEQUENCE : 0008 MASTER(T)";
        String mstr = "";
        String rstr = "RRN : 000008000000 EXPIRYP : XXXX";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11116() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "sdcard/download/ARIALNB.TTF");
        String lstr = "SEQUENCE : 0008 MASTER(T)";
        String mstr = "";
        String rstr = "RRN : 000008000000 EXPIRY : XXXX";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);

//        format.putInt("fontSize", 1);
//        format.putBoolean("bold", false);
//        format.putString("fontStyle", "sdcard/download/ARIALNB.TTF");
//        lstr = "VISA(T)";
//        mstr = "";
//        rstr = "EXPIRY:XXXXXX";
//        My11addTextInLine(format, lstr, mstr, rstr, mode);

        My07startPrint(new MyListener());
    }

//    void D11116() {
//        D11047();
//        new Thread() {
//            public void run() {
//                try {
//                    D11085();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
////        Bundle format = new Bundle();
////        format.putInt("fontSize", 0);
////        format.putBoolean("bold",false);
////        format.putString("fontStyle", "English");
////        String lstr = "Thriller artist";
////        String mstr = "Michael Jackson";
////        String rstr = "Invicible POP";
////        int mode = 1;
////        My11addTextInLine(format,lstr,mstr,rstr,mode);
////        My07startPrint(new MyListener());
//    }

    void D11117() {
//        测试混合打印可成功打印所有内容
        Bundle format2 = new Bundle();
        format2.putInt("align", 0);
        format2.putInt("height", 128);
        format2.putInt("barCodeType", 4);
        My04addBarCode(format2, "1234567890ABC");

        Bundle format3 = new Bundle();
        format3.putInt("font", 0);
        format3.putInt("align", 0);
        format3.putBoolean("newline", true);
        format3.putBoolean("bold", false);
        My03addText(format3, "ABCDabcd1234测试打印");

        Bundle format4 = new Bundle();
        format4.putInt("offset", 50);//expectedHeight
        format4.putInt("expectedHeight", 128);
        My05addQrCode(format4, "18620518880");

        Bundle format5 = new Bundle();
        format5.putInt("offset", 0);
        format5.putInt("width", 384);
        format5.putInt("height", 384);
        format5.putInt("gray", 100);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
//            My06addImage(format5, imgs);
//            logUtils.addCaseLog("图片添加成功");
        My07startPrint(new MyListener());
        My08feedLine(3);

        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "English");
        String lstr = "床前明月光 疑是地上霜霜";
        String mstr = "床前明月光光光 疑是地上霜霜霜";
        String rstr = "床前明月光 疑是地上霜霜";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);

        My07startPrint(new MyListener());
    }

    void D11118() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11119() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11120() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11121() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11122() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 0);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11123() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11124() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 2);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11125() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 3);
        format.putBoolean("bold", false);
        format.putString("fontStyle", "Arabic");
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11126() {
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 0);
        format1.putBoolean("bold", false);
        String lstr1 = "Font0Left";
        String mstr1 = "Font0Middle";
        String rstr1 = "Font0Right";
        Bundle format2 = new Bundle();
        format2.putInt("fontSize", 0);
        format2.putBoolean("bold", false);
        format2.putFloat("scale_w", (float) 2.0);
        format2.putFloat("scale_h", (float) 2.0);
        String lstr2 = "Font0的Float2和Font3打印效果相同";
        String mstr2 = "Font0的Float2和Font3打印效果相同";
        String rstr2 = "Font0的Float2和Font3打印效果相同";
        Bundle format3 = new Bundle();
        format3.putInt("fontSize", 3);
        format3.putBoolean("bold", false);
        String lstr3 = "Font0的Float2和Font3打印效果相同";
        String mstr3 = "Font0的Float2和Font3打印效果相同";
        String rstr3 = "Font0的Float2和Font3打印效果相同";
        int mode = 2;
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format3, lstr3, mstr3, rstr3, mode);
        My07startPrint(new MyListener());
    }
    void D11127() {
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 1);
        format1.putBoolean("bold", false);
        String lstr1 = "Font1Left";
        String mstr1 = "Font1Middle";
        String rstr1 = "Font1Right";
        Bundle format2 = new Bundle();
        format2.putInt("fontSize", 0);
        format2.putBoolean("bold", false);
        format2.putFloat("scale_w", (float) 2.0);
        format2.putFloat("scale_h", (float) 2.0);
        String lstr2 = "Font0的Float2和Font3打印效果相同";
        String mstr2 = "Font0的Float2和Font3打印效果相同";
        String rstr2 = "Font0的Float2和Font3打印效果相同";
        Bundle format3 = new Bundle();
        format3.putInt("fontSize", 1);
        format3.putBoolean("bold", false);
        format3.putFloat("scale_w", (float) 2.0);
        format3.putFloat("scale_h", (float) 2.0);
        String lstr3 = "Font1的Float2比上一部分字体大";
        String mstr3 = "Font1的Float2比上一部分字体大";
        String rstr3 = "Font1的Float2比上一部分字体大";
        int mode = 2;
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format3, lstr3, mstr3, rstr3, mode);
        My07startPrint(new MyListener());
    }

    void D11128() {
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 1);
        format1.putBoolean("bold", false);
        String lstr1 = "Font1正常字体";
        String mstr1 = "Font1正常字体";
        String rstr1 = "Font1正常字体";
        Bundle format2 = new Bundle();
        format2.putInt("fontSize", 1);
        format2.putBoolean("bold", false);
        format2.putFloat("scale_w", (float) 0.1);
        format2.putFloat("scale_h", (float) 0.1);
        String lstr2 = "0.1倍高倍宽";
        String mstr2 = "0.1倍高倍宽";
        String rstr2 = "0.1倍高倍宽";
        Bundle format3 = new Bundle();
        format3.putInt("fontSize", 1);
        format3.putBoolean("bold", false);
        format3.putFloat("scale_w", (float) 0.5);
        format3.putFloat("scale_h", (float) 0.5);
        String lstr3 = "0.5倍高倍宽";
        String mstr3 = "0.5倍高倍宽";
        String rstr3 = "0.5倍高倍宽";
        Bundle format4 = new Bundle();
        format4.putInt("fontSize", 1);
        format4.putBoolean("bold", false);
        format4.putFloat("scale_w", (float) 1.4);
        format4.putFloat("scale_h", (float) 1.4);
        String lstr4 = "1.4倍高倍宽";
        String mstr4 = "1.4倍高倍宽";
        String rstr4 = "1.4倍高倍宽";
        Bundle format5 = new Bundle();
        format5.putInt("fontSize", 1);
        format5.putBoolean("bold", false);
        format5.putFloat("scale_w", (float) 2.0);
        format5.putFloat("scale_h", (float) 2.0);
        String lstr5 = "2.0倍高倍宽";
        String mstr5 = "2.0倍高倍宽";
        String rstr5 = "2.0倍高倍宽";
        int mode = 2;
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format3, lstr3, mstr3, rstr3, mode);
        My11addTextInLine(format4, lstr4, mstr4, rstr4, mode);
        My11addTextInLine(format5, lstr5, mstr5, rstr5, mode);
        My07startPrint(new MyListener());
    }


    void D11129() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 5);
        format.putBoolean("bold", false);
        format.putFloat("scale_w", (float) 0.1f);
        format.putFloat("scale_h", (float) 0.1f);
        String lstr = "字号5边界值0.1f打印测试";
        String mstr = "字号5边界值0.1f打印测试";
        String rstr = "字号5边界值0.1f打印测试";
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 5);
        format1.putBoolean("bold", false);
        format1.putFloat("scale_w", (float) 4.0f);
        format1.putFloat("scale_h", (float) 4.0f);
        String lstr1 = "字号5边界值4.0f打印测试";
        String mstr1 = "字号5边界值4.0f打印测试";
        String rstr1 = "字号5边界值4.0f打印测试";
        Bundle format2 = new Bundle();
        format2.putInt("fontSize", 5);
        format2.putBoolean("bold", false);
        String lstr2 = "字号5打印测试";
        String mstr2 = "字号5打印测试";
        String rstr2 = "字号5打印测试";
        int mode = 2;
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My07startPrint(new MyListener());
    }

    void D11130() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        format.putFloat("scale_w", (float) 2.4f);
        format.putFloat("scale_h", (float) 0.8f);
        String lstr = "测试倍高倍宽数值不一致";
        String mstr = "测试倍高倍宽数值不一致";
        String rstr = "测试倍高倍宽数值不一致";
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 1);
        format1.putBoolean("bold", false);
        format1.putFloat("scale_w", (float) 0.8f);
        format1.putFloat("scale_h", (float) 2.4f);
        String lstr1 = "测试倍高倍宽数值不一致";
        String mstr1 = "测试倍高倍宽数值不一致";
        String rstr1 = "测试倍高倍宽数值不一致";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My07startPrint(new MyListener());
    }

    void D11131() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        String lstr = "Font1原字体";
        String mstr = "Font1原字体";
        String rstr = "Font1原字体";
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 1);
        format1.putBoolean("bold", false);
        format1.putFloat("scale_w", (float) 2.0f);
        String lstr1 = "Font1只倍宽";
        String mstr1 = "Font1只倍宽";
        String rstr1 = "Font1只倍宽";
        Bundle format2 = new Bundle();
        format2.putInt("fontSize", 1);
        format2.putBoolean("bold", false);
        format2.putFloat("scale_h", (float) 2.0f);
        String lstr2 = "Font1只倍高";
        String mstr2 = "Font1只倍高";
        String rstr2 = "Font1只倍高";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My07startPrint(new MyListener());
    }

    void D11132() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 1);
        format.putBoolean("bold", false);
        String lstr = "Font1原字体";
        String mstr = "Font1原字体";
        String rstr = "Font1原字体";
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 1);
        format1.putBoolean("bold", false);
        format1.putFloat("scale_w", (float) 1.5f);
        format1.putFloat("scale_h", (float) 1.5f);
        String lstr1 = "只添加左列";
        String mstr1 = null;
        String rstr1 = null;
        Bundle format2 = new Bundle();
        format2.putInt("fontSize", 1);
        format2.putBoolean("bold", false);
        format2.putFloat("scale_w", (float) 1.5f);
        format2.putFloat("scale_h", (float) 1.5f);
        String lstr2 = null;
        String mstr2 = "只添加中间列";
        String rstr2 = null;
        Bundle format3 = new Bundle();
        format3.putInt("fontSize", 1);
        format3.putBoolean("bold", false);
        format3.putFloat("scale_w", (float) 1.5f);
        format3.putFloat("scale_h", (float) 1.5f);
        String lstr3 = null;
        String mstr3 = "只添加右列";
        String rstr3 = null;
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format3, lstr3, mstr3, rstr3, mode);
        My07startPrint(new MyListener());
    }

    void D11133() {
        Bundle format = new Bundle();
        format.putInt("font", 1);
        format.putInt("align", 0);
        format.putBoolean("bold", false);
        format.putFloat("scale_w", (float) 2.0f);
        format.putFloat("scale_h", (float) 2.0f);
        Bundle format1 = new Bundle();
        format1.putInt("fontSize", 1);
        format1.putBoolean("bold", false);
        format1.putFloat("scale_w", (float) 2.0f);
        format1.putFloat("scale_h", (float) 2.0f);
        String lstr1 = "addtextinline打印字体相同";
        String mstr1 = "addtextinline打印字体相同";
        String rstr1 = "addtextinline打印字体相同";
        int mode = 2;
        My03addText(format, "addtext倍宽打印测试");
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My07startPrint(new MyListener());
    }

    void D11134() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 5);
        format.putBoolean("bold", false);
        format.putFloat("scale_w", (float) 0.0f);
        format.putFloat("scale_h", (float) 0.0f);
        String lstr = "Font5倍高倍宽为0时报错";
        String mstr = "Font5倍高倍宽为0时报错";
        String rstr = "Font5倍高倍宽为0时报错";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11135() {
        Bundle format = new Bundle();
        format.putInt("fontSize", 5);
        format.putBoolean("bold", false);
        format.putFloat("scale_w", (float) 4.1f);
        format.putFloat("scale_h", (float) 4.1f);
        String lstr = "Font5倍高倍宽为4.1时报错";
        String mstr = "Font5倍高倍宽为4.1时报错";
        String rstr = "Font5倍高倍宽为4.1时报错";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D12001() {
        Bundle format = new Bundle();

        format.putInt("font", 0);
        format.putInt("align", 0);
        My03addText(format, "ABCDabcd1234测试打印");

        format.putInt("width", 192);
        format.putInt("height", 128);
        My04addBarCode(format, "13524044282");

        format.putInt("offset", 50);
        format.putInt("expectedHeight", 128);
        My05addQrCode(format, "www.13524044282.qq.com");

        format.putInt("gray", 100);
        format.putInt("offset", 0);
//        format.putInt("width", 128);
//        format.putInt("height", 128);
        My07startPrint(new MyListener());
    }

    void D12002() {
        My12startSaveCachePrint(new MyListener());
    }

    void D13001() {
        D12001();
        My13cleanCache();
    }

    void D14001() {
        Bundle format = new Bundle();
        My03addText(format, "EMV卡号确认流程中打印信息：\n卡号：        汇率：");
        My14startPrintInEmv(new MyListener());
    }

    void D16001() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("width", 200);
        format.putInt("height", 200);
        format.putInt("gray", 100);
        My16addScreenCapture(format, new MyListener());
    }

    void D16002() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        format.putInt("width", 384);
        format.putInt("height", 384);
        format.putInt("gray", 100);
        My16addScreenCapture(format, new MyListener());
    }

    void D16003() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);
        format.putInt("width", 100);
        format.putInt("height", 100);
        format.putInt("gray", 255);
        My16addScreenCapture(format, new MyListener());
    }

    void D17001() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.confused);

        try {
            iPrinter.addBmpImage(format, bitmap);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        My07startPrint(new MyListener());
    }

    public byte[] image2byte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }


    //byte数组到图片
    public void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        try {
            FileOutputStream imageOutput = new FileOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    //byte数组到16进制字符串
    public String byte2string(byte[] data) {
        if (data == null || data.length <= 1) return "0x";
        if (data.length > 200000) return "0x";
        StringBuffer sb = new StringBuffer();
        int buf[] = new int[data.length];
        //byte数组转化成十进制
        for (int k = 0; k < data.length; k++) {
            buf[k] = data[k] < 0 ? (data[k] + 256) : (data[k]);
        }
        //十进制转化成十六进制
        for (int k = 0; k < buf.length; k++) {
            if (buf[k] < 16) sb.append("0" + Integer.toHexString(buf[k]));
            else sb.append(Integer.toHexString(buf[k]));
        }
        return "0x" + sb.toString().toUpperCase();
    }

    private Bitmap getBitmapByte(int id) {
        Resources res = context.getResources(); // 获取资源对象
        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
        return bitmap;
    }

    private byte[] getImgByte() {
//        String path = Environment.getExternalStorageDirectory().getPath() + "/confused.png";
        String path = BASEPATH + "/pic/simple.bmp";
        Log.d("TAG", path);
        File mFile = new File(path);
        //若该文件存在
        if (mFile.exists()) {
            // Bitmap bitmap = BitmapFactory.decodeFile(path);
            byte[] img = image2byte(path);
            return img;
        } else {
            logUtils.addCaseLog("文件不存在");
            return null;
        }
    }

    private boolean isPrinting = false;
    private static final Object isPrintingLock = new Object();

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

    public void printMsg(String msg) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        printMsg(msg, false);
    }

    public void printMsg(String msg, int size, boolean bold, boolean silence) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        Bundle format = new Bundle();
        format.putInt("font", size);
        if (bold) {
            format.putBoolean("bold", true);
        }
        format.putInt("align", 0);
        if (msg.startsWith("\n")) {
            addText(format, " ", silence);
        }
        String[] lines = msg.split("\n");
        for (String line : lines) {
            addText(format, line, silence);
        }
        if (msg.endsWith("\n")) {
            addText(format, " ", silence);
        }
        startPrint(new MyListener(), silence);

    }

    public void printMsg(String msg, boolean silence) {
        printMsg(msg, 1, false, silence);
    }

    public void printDbgMsg(String msg, boolean silence) {
        printMsg(msg, 0, true, silence);
    }

    public void printErrMsg(String msg, boolean silence) {
        printMsg(msg, 3, true, silence);
    }

    public void D15_AUTO() {

        try {
            //通过类文件获取类对象
            Class aClass = Class.forName("moudles.PrintBtMoudle");
            this.printMsgTool("-------Printer模块------");
            this.printMsgTool("自动化测试案例开始执行");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("D") && !method.getName().equals("D15_AUTO")) {
                    this.printMsgTool(method.getName() + "方法开始执行......");
                    method.invoke(this);
                    this.printMsgTool(method.getName() + "方法结束执行！！！");
                    Thread.sleep(1000);
                }
            }
            this.printMsgTool("自动化测试案例执行完毕");
            logUtils.addCaseLog("自动化测试案例执行完毕");
            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D08018();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printMsgTool(String msg) {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private void initScreenOffTime(int millisecond){
//        int offTime;
//        offTime = Settings.System.getInt(Context.getContext().getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE);
//
//    }
}
