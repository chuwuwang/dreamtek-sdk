package moudles;


import static com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter.addText.format.VALUE_FontSize_HUGE_48;
import static com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter.addText.format.VALUE_FontSize_LARGE_32_32;
import static com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter.addText.format.VALUE_FontSize_LARGE_DH_32_64_IN_BOLD;
import static com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24;
import static com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD;
import static com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16;

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
import com.dreamtek.smartpos.deviceservice.constdefine.ConstIPrinter;
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
    // ConstIPrinter does not define addText font values 6/7 or boundary values such as -1/8; those literals are retained for test coverage.
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
            logUtils.addCaseLog("addText execution exception");
            e.printStackTrace();
        }
    }

    public void My04addBarCode(Bundle format, String barcode) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addBarCode(format, barcode);
            logUtils.addCaseLog("addBarCode execution completed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("addBarCode execution exception");
            e.printStackTrace();
        }
    }

    public void My05addQrCode(Bundle format, String qrCode) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addQrCode(format, qrCode);
            logUtils.addCaseLog("addQrCode execution completed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("addQrCode execution exception");

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
                logUtils.addCaseLog("startPrint call completed");
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("My07startPrint exception");
            e.printStackTrace();
        }
    }

    public void My08feedLine(int lines) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.feedLine(lines);
            logUtils.addCaseLog("feedLine paper feed completed");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void My09addQrCodesInLine(List<QrCodeContent> qrCodes) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addQrCodesInLine(qrCodes);
            logUtils.addCaseLog("Add multiple QR codes success");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("Add multiple QR codes failed");
        }
    }

    public void My10setLineSpace(int space) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.setLineSpace(space);
            logUtils.addCaseLog("Set line spacing completed");
        } catch (RemoteException e) {
            e.printStackTrace();
            logUtils.addCaseLog("Set line spacing failed");
        }
    }

    public void My11addTextInLine(Bundle format, String lString, String mString, String rString, int mode) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addTextInLine(format, lString, mString, rString, mode);
            logUtils.addCaseLog("addTextInLine execution completed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("addTextInLine execution exception");
            e.printStackTrace();
        }
    }

    public void My12startSaveCachePrint(PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.startSaveCachePrint(listener);
            logUtils.addCaseLog("startSaveCachePrint call completed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("startSaveCachePrint exception");
            e.printStackTrace();
        }
    }

    public int My13cleanCache() {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return 0;
        try {
            int result = iPrinter.cleanCache();
            logUtils.addCaseLog("Clear cache result = " + result);
            return result;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Clear cache exception");
            e.printStackTrace();
            return -1;
        }
    }

    public void My14startPrintInEmv(PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.startPrintInEmv(listener);
            logUtils.addCaseLog("My14startPrintInEmv executed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("My14startPrintInEmv execution exception");
            e.printStackTrace();
        }
    }

    public void My15autoPrint(PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.startPrintInEmv(listener);
            logUtils.addCaseLog("My14startPrintInEmv executed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("My14startPrintInEmv execution exception");
            e.printStackTrace();
        }
    }

    public void My16addScreenCapture(Bundle bundle, PrinterListener listener) {
        if ("X990 Mini".equals(Build.MODEL) || "X990 PINPad".equals(Build.MODEL))
            return;
        try {
            iPrinter.addScreenCapture(bundle);
            iPrinter.startPrintInEmv(listener);
            logUtils.addCaseLog("My14startPrintInEmv executed");
        } catch (RemoteException e) {
            logUtils.addCaseLog("My14startPrintInEmv execution exception");
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
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
        logUtils.addCaseLog("Text added");
        //Barcode  D04002
        Bundle format1 = new Bundle();
        //align
        format1.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format1.putInt("width", 192);
        format1.putInt("height", 128);
        My04addBarCode(format1, "13524044282");
        logUtils.addCaseLog("Barcode added");

        //QR code  5002();
        Bundle format2 = new Bundle();
        format2.putInt("offset", 50);//expectedHeight
        format2.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format2, "www.13524044282.qq.com");
        logUtils.addCaseLog("QR code added");

        //Image D06003();
//        Bundle format3 = new Bundle();
//        format3.putInt("offset", 50);
//        format3.putInt("width", 384);
//        format3.putInt("height", 128);
        D17001();
        logUtils.addCaseLog("Image added");
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

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_SMALL_16_16 );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_NORMAL_24_24  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_LARGE_32_32  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_LARGE_DH_32_64_IN_BOLD  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_HUGE_48   );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");

        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_SMALL_16_16 );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_NORMAL_24_24  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_LARGE_32_32  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_LARGE_DH_32_64_IN_BOLD  );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");
        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, VALUE_FontSize_HUGE_48   );
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Dreamtek");

        My08feedLine(2);

        format.clear();
        format.putInt("height", 88);
        format.putInt("width", 200);
        format.putInt("offset", 0);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.barcode);

        try {
            iPrinter.addBmpImage(format, bitmap);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        My08feedLine(2);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "esta es una prueba de texto");

        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Esta es una prueba de texto");


        My08feedLine(5);

        format.clear();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_32_32);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "más grande");

        My08feedLine(1);

        format.clear();
        My03addText(format, "@feenicia");
        My08feedLine(5);
        My07startPrint(new MyListener());
    }

    void D03002() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - small font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03003() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        My03addText(format, "Aa1 test - small font center aligned bold");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03004() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        My03addText(format, "Aa1 test - small font right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03005() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03006() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        My03addText(format, "Aa1 test - medium font center aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03007() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        My03addText(format, "Aa1 test - medium font right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03008() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Aa1 test - medium font 2x height bold left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03009() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        My03addText(format, "Aa1 test - medium font 2x height bold center aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03010() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        My03addText(format, "Aa1 test - medium font 2x height bold right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03011() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_32_32);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03012() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_32_32);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        My03addText(format, "Aa1 test - large font center aligned bold");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03013() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_32_32);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large font right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03014() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_DH_32_64_IN_BOLD);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large font 2x height bold left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03015() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_DH_32_64_IN_BOLD);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large font 2x height bold center aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03016() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_LARGE_DH_32_64_IN_BOLD);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large font 2x height bold right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03017() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_HUGE_48);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - huge font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");

        My07startPrint(new MyListener());
    }

    void D03018() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_HUGE_48);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        My03addText(format, "Aa1 test - huge font center aligned bold");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03019() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_HUGE_48);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - huge font right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03020() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 6);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Aa1 test - medium font 2x width bold left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03021() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 6);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        My03addText(format, "Aa1 test - medium font 2x width bold center aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03022() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 6);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        My03addText(format, "Aa1 test - medium font 2x width bold right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03023() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 7);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large 2x width bold left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03024() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 7);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large 2x width bold center aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03025() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 7);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format, "Aa1 test - large 2x width bold right aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03026() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, -1);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03027() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, 8);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }


    void D03028() {
        Bundle format = new Bundle();
//        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }


    void D03029() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, -1);
                My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03030() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, 3);
                My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03031() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
//        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, 3);
                My03addText(format, "Aa1 test - medium font left aligned");
        My03addText(format, "Esta es una prueba de texto");
        My03addText(format, "SERTI");
        My03addText(format, "esta es una prueba de texto");
        My03addText(format, "más grande");
        My03addText(format, "@feenicia");
        My07startPrint(new MyListener());
    }

    void D03032() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Previous line not full oneline");
        My03addText(format, "Line wrap parameter test");
        //A line is very long and must wrap
        My03addText(format, "Previous line very long, still not full after wrap onelineoneday");
        My03addText(format, "Line wrap parameter test");
        My07startPrint(new MyListener());
    }

    void D03033() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Line wrap parameter test: previous line already full");
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "Line wrap parameter test");
        My07startPrint(new MyListener());
    }

    void D03034() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_NoCRLF);
        My03addText(format, "Previous line not full oneline");
        My03addText(format, "Line wrap parameter test");
        My03addText(format, "Previous line very long, still not full after wrap onelineoneday");
        My03addText(format, "Line wrap parameter test");
        My07startPrint(new MyListener());
    }

    void D03035() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_NoCRLF);
        My03addText(format, "Line wrap parameter test: previous line already full");
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_NoCRLF);
        My03addText(format, "Line wrap parameter test");
        My07startPrint(new MyListener());
    }

    void D03036() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Line wrap parameter test: previous line already full");
        My03addText(format, "Line wrap parameter test");
        My07startPrint(new MyListener());
    }

    void D03037() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        My03addText(format, "Aa1 test - medium font bold left aligned");
        My07startPrint(new MyListener());
    }

    void D03038() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
//        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean,false);
        My03addText(format, "Aa1 test - medium font left aligned");
        My07startPrint(new MyListener());
    }

    void D03039() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        My03addText(format, "ABCabc long string right aligned with line wrap terminal type date time voucher signature card number Sichuan Deyang Jingyang 1234567890RMB:/*()SI,-auto line wrap test");
        My07startPrint(new MyListener());
    }

    void D03040() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, null);
        My07startPrint(new MyListener());
    }

    void D03041() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "");
        My07startPrint(new MyListener());
    }

    void D03042() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "          10 spaces");
        My07startPrint(new MyListener());
    }

    void D03043() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "！@#￥%……&*（）——+-={}【】|、《》，。、？‘“；：:\"><?/\\|][}{:;’‘");
        My07startPrint(new MyListener());
    }

    void D03044() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Special CJK chars (mei) (yin) (ni) (jiao) (jiao) (biao) (ku) (lue) (su) (ma) (jing) (huan)\"(ning)(ning)");
        My07startPrint(new MyListener());
    }

    void D03045() {
        Bundle format = new Bundle();
        //align
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_NoCRLF);
        //"!@#$ *().::{}^&<>?""(ning)
        String text = "";
        for (int i = 0; i < 1000; i++) {
//        for (int i = 0; i < 10; i++) {
            text = text + "CJK";
        }
        My03addText(format, text);
        logUtils.addCaseLog("Large data print, current 1000 CJK chars placed");
        My07startPrint(new MyListener());
    }

    void D03046() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "ABCDefg\nAgricultural Bank of China");
        My07startPrint(new MyListener());
    }

    void D03047() {
        for (int i = 0; i < 10; i++) {
            Bundle format = new Bundle();
            format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
            format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
            My03addText(format, "Iteration " + i + " adding text");
//            My07startPrint(new MyListener());
        }


        My07startPrint(new MyListener());
    }

    void D03048() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China Dongdian Middle School Jingyang District Deyang City Sichuan Province China ");
        My07startPrint(new MyListener());
        try {

            Thread.sleep(1000);
            My03addText(format, "Adding text during print process");
            Thread.sleep(10000);
            My07startPrint(new MyListener());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void D03049() {
        My03addText(null, "ABCDefg\\nAgricultural Bank of China");
        My07startPrint(new MyListener());
    }

    void D03050() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, null);
        My03addText(format, "Default Chinese print 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        My07startPrint(new MyListener());
    }

    void D03051() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "abcdefg");
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03052() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03053() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        My03addText(format, "Default English print 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        My07startPrint(new MyListener());
    }

    void D03054() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03055() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/system/fonts/fzzdx.ttf");
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03056() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/system/fonts/fzzdx.ttf");
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03057() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/sdcard/download/ARIALNB.TTF");
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03058() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "");
        My03addText(format, "The bright sun sets behind the mountains the Yellow River flows to the sea");
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/system/fonts/SIMKAI.TTF");
        My03addText(format, "The bright sun sets behind the mountains the Yellow River flows to the sea");
        My07startPrint(new MyListener());
    }

    void D03059() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/system/fonts/simsun.ttc");
        My03addText(format, "The bright sun sets behind the mountains the Yellow River flows to the sea");
        My07startPrint(new MyListener());
    }

    void D03060() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/system/fonts/abcd.ttf");
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03061() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putFloat("scale_w", (float) 1.5);
        format.putFloat("scale_h", (float) 1.5);
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03062() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putFloat("scale_w", (float) 2);
        format.putFloat("scale_h", (float) 2);
        My03addText(format, "ABCDabcd1234 test print");
        My07startPrint(new MyListener());
    }

    void D03063() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        My03addText(format, "S_________________");
        My03addText(format, "S_______________________");
        My03addText(format, "S____----------——————--______________");
        My07startPrint(new MyListener());
    }

    void D03064() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        format.putBoolean("isColorInvert", true);
        My03addText(format, "S_________________");
        My03addText(format, "S_______________________");
        My03addText(format, "S____----------——————--______________");
        My07startPrint(new MyListener());
    }


    void D04001() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
    }

    void D04002() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04003() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04004() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 0);
        My04addBarCode(format, "1234567890ABC");
        My07startPrint(new MyListener());
    }

    void D04005() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 1);
        My04addBarCode(format, "A1234567890$a");
        My07startPrint(new MyListener());
    }

    void D04006() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 1);
        My04addBarCode(format, "A1234567890$a");
        My07startPrint(new MyListener());
    }

    void D04007() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 1);
        My04addBarCode(format, "A1234567890$a");
        My07startPrint(new MyListener());
    }

    void D04008() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 2);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04009() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 2);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04010() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 2);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04011() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 3);
        My04addBarCode(format, "1234567890W+");
        My07startPrint(new MyListener());
    }

    void D04012() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04013() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 5);
        My04addBarCode(format, "1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04014() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 6);
        My04addBarCode(format, "47112346");
        My07startPrint(new MyListener());
    }

    void D04015() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 7);
        My04addBarCode(format, "6901234567892");
        My07startPrint(new MyListener());
    }

    void D04016() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 8);
        My04addBarCode(format, "12345678901234");
        My07startPrint(new MyListener());
    }

    /**
     * Service version 3.10 does not support MAXICODE QR_COD RSS_14 UPC_EAN_EXTENSION barcode types, corresponding to D04017/D04020/D04021/D04014 cases
     */


   /* void D04017() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType",9);
        My04addBarCode(format, "1234%5678aW");
        My07startPrint(new MyListener());
    }*/

    void D04018() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 10);
        My04addBarCode(format, "1234%567890aW");
        My07startPrint(new MyListener());
    }

    void D04019() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 11);
        My04addBarCode(format, "1234%567890aW");
        My07startPrint(new MyListener());
    }

  /*  void D04020() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType",12);
        My04addBarCode(format, "0120012345678909");
        My07startPrint(new MyListener());
    }*/

//     void D04021() {
//         Bundle format = new Bundle();
//         format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
//         format.putInt("height", 128);
//         format.putInt("barCodeType",13);
//         My04addBarCode(format, "0120044501754127");
//         My07startPrint(new MyListener());
//     }

    void D04022() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_RIGHT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 14);
        My04addBarCode(format, "45631431967");
        My07startPrint(new MyListener());
    }

    void D04023() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 15);
        My04addBarCode(format, "04029311");
        My07startPrint(new MyListener());
    }

   /* void D04024() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType",16);
        My04addBarCode(format, "45631431967");
        My07startPrint(new MyListener());
    }*/

    void D04025() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, -1);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04026() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, 3);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04027() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", -1);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04028() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 128);
        format.putInt("barCodeType", 17);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04029() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 0);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04030() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", -1);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04031() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 200);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04032() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putInt("height", 384);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "1234567890(W)");
        My07startPrint(new MyListener());
    }

    void D04033() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, null);
        My07startPrint(new MyListener());
    }

    void D04036() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_CENTER);
        format.putInt("height", 128);
        format.putInt("barCodeType", 4);
        My04addBarCode(format, "");
        My07startPrint(new MyListener());
    }

    void D05001() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format, "18620518880");
    }

    void D05002() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format, "18620518880");
        My07startPrint(new MyListener());
    }

    void D05003() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format, "18620518880@qq.com");
        My07startPrint(new MyListener());
    }

    void D05004() {
        Bundle format = new Bundle();
        format.putInt("offset", -1);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format, "18620518880@qq.com");
        My07startPrint(new MyListener());
    }

    void D05005() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 334);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05006() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 335);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05008() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, -1);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05009() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 0);
        My05addQrCode(format, "https://www.baidu.com/");
        My07startPrint(new MyListener());
    }

    void D05010() {
        Bundle format = new Bundle();
        format.putInt("offset", 50);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format, null);
        My07startPrint(new MyListener());
    }

    void D05011() {
        Bundle format = new Bundle();
        format.putInt("offset", 10);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 192);
        My05addQrCode(format, "");
        My07startPrint(new MyListener());
    }

    void D05012() {
        Bundle format = new Bundle();
        format.putInt("offset", 10);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 192);
        My05addQrCode(format, "     ");
        My07startPrint(new MyListener());
    }

    void D05013() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 192);
        My05addQrCode(format, "(ning)");
        My07startPrint(new MyListener());
    }

    void D05014() {
        Bundle format = new Bundle();
        format.putInt("offset", 1);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 192);
        My05addQrCode(format, "！@#￥%……");
        My07startPrint(new MyListener());
    }

    void D05015() {
        Bundle format = new Bundle();
        format.putInt("offset", 0);//expectedHeight
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 384);
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
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 384);
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
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 1);
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
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 385);
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
            My03addText(format, "Sunflower!");
            My17addBmpImage(format, imgs);
        }
        imgs = getBitmapByte(R.drawable.bee);
        if (imgs != null) {
            My03addText(format, "Little bee");
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
        //Put the receipt in sdcard/download with the following name, can adapt to 384 width receipt printing.
        // The file can be copied in res/drawable. If you want to use another receipt, please change it to this name.
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "ABCDabcd1234 test print");
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School Sichuan Province Deyang City Jingyang District Dongdian Middle School");
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
                logUtils.addCaseLog("Multi-thread execution");
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        String str = "";
        for (int i = 0; i < 500; i++) {
            str = str + "A";
        }
        My03addText(format, str);
        My07startPrint(new MyListener());
    }

    void D07020() {

        Bundle format = new Bundle();
        //align
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        String str = "";

        for (int i = 0; i < 40; i++) {
            str = "";
//            str = String.valueOf(i) + "test data\n";
            str = String.valueOf(i) + "test data\n";
            My03addText(format, str);
        }

        My07startPrint(new MyListener());
        logUtils.addCaseLog("Please power off and restart...");
        logUtils.addCaseLog("Then execute D07021");

    }

    void D07021() {

        logUtils.addCaseLog("After power-off restart, call startPrint directly");
        My07startPrint(new MyListener());

    }

    void D07022() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        String str = "Print #1 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #2 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #3 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #4 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #5 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #6 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #7 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #8 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #9 test data";
        My03addText(format, str);
        My07startPrint(new MyListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        str = "Print #10 test data";
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
//        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
//        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
//        My03addText(format, "TVR VALUE");
////        My07startPrint(new MyListener());
//        My08feedLine(2);
////        My07startPrint(new MyListener());
//        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
//        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
//        My03addText(format, "AMOUNT");


        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
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
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
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
                logUtils.addCaseLog("Multi-thread execution");
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
        QrCodeContent qrCodeContent1 = new QrCodeContent(385, 0, "www.baidu.com");//1st param: height, 2nd param: leftOffset
        qrCodes.add(qrCodeContent1);

        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09004() {//Print multiple QR codes on the same line
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 0, "www.baidu.com");//1st param: height, 2nd param: leftOffset
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
        QrCodeContent qrCodeContent1 = new QrCodeContent(50, 5, "www.baidu.com");//1st param: height, 2nd param: leftOffset
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
        QrCodeContent qrCodeContent1 = new QrCodeContent(150, 20, "www.baidu.com");//1st param: height, 2nd param: leftOffset
        qrCodes.add(qrCodeContent1);
        QrCodeContent qrCodeContent2 = new QrCodeContent(150, 20, "www.baidu.com");
        qrCodes.add(qrCodeContent2);

        My09addQrCodesInLine(qrCodes);
        My07startPrint(new MyListener());
        My08feedLine(1);
    }

    void D09015() {
        List<QrCodeContent> qrCodes = new ArrayList<>();
        QrCodeContent qrCodeContent1 = new QrCodeContent(100, 20, "www.baidu.com");//1st param: height, 2nd param: leftOffset
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
        My03addText(format, "Line spacing test - The bright sun sets behind the mountains, the Yellow River flows into the sea. To see a thousand miles further, climb one more floor.");
        My07startPrint(new MyListener());
    }

    void D10002() {
        My10setLineSpace(0);

        Bundle format = new Bundle();
        My03addText(format, "Line spacing test - The bright sun sets behind the mountains, the Yellow River flows into the sea. To see a thousand miles further, climb one more floor.");
        My07startPrint(new MyListener());
    }

    void D10003() {
        My10setLineSpace(5);

        Bundle format = new Bundle();
        My03addText(format, "Line spacing test - The bright sun sets behind the mountains, the Yellow River flows into the sea. To see a thousand miles further, climb one more floor.");
        My07startPrint(new MyListener());
    }

    void D10004() {
        My10setLineSpace(50);

        Bundle format = new Bundle();
        My03addText(format, "Line spacing test - The bright sun sets behind the mountains, the Yellow River flows into the sea. To see a thousand miles further, climb one more floor.");
        My07startPrint(new MyListener());
    }

    void D11004() {

        My10setLineSpace(50);
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D10005() {
        My10setLineSpace(51);

        Bundle format = new Bundle();
        My03addText(format, "Line spacing test - The bright sun sets behind the mountains, the Yellow River flows into the sea. To see a thousand miles further, climb one more floor.");
        My07startPrint(new MyListener());
    }

    void D10006() {
        My10setLineSpace(1);

        Bundle format = new Bundle();
        My03addText(format, "Line spacing test - The bright sun sets behind the mountains, the Yellow River flows into the sea. To see a thousand miles further, climb one more floor.");
        My07startPrint(new MyListener());
    }

    void D11001() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
//        My07startPrint(new MyListener());
    }

    void D11002() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        format.putFloat("scale_w", (float) 1.5);
        format.putFloat("scale_h", (float) 1.5);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11003() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11005() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the river flows to sea";
        String mstr = "Moonlight before bed frost on the ground";
        String rstr = "Farmers weeding at noon sweat into soil";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11006() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = " The sun sets behind mountains the river flows to sea ";
        String mstr = " Moonlight before bed frost on the ground ";
        String rstr = " I look up at the moon I bow and think of home ";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11007() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Moonlight before bed";
        String rstr = "Frost on ground";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11008() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Frost on ground";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11009() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Frost on ground";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11010() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11011() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11012() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11013() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String mstr = "";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11014() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11015() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "";
        String rstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11016() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11017() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11018() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11019() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11020() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the river flows to sea";
        String mstr = "Moonlight before bed frost on the ground";
        String rstr = "Farmers weeding at noon sweat into soil";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11021() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = " The sun sets behind mountains the river flows to sea ";
        String mstr = " Moonlight before bed frost on the ground ";
        String rstr = " I look up at the moon I bow and think of home ";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11022() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Moonlight before bed";
        String rstr = "Frost on ground";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11023() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Frost on ground";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11024() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Frost on ground";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11025() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11026() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11027() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11028() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String mstr = "";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11029() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11030() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "";
        String rstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11031() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11032() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11033() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11034() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11035() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11036() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11037() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11038() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the river flows to sea";
        String mstr = "Moonlight before bed frost on the ground";
        String rstr = "Farmers weeding at noon sweat into soil";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11039() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = " The sun sets behind mountains the river flows to sea ";
        String mstr = " Moonlight before bed frost on the ground ";
        String rstr = " I look up at the moon I bow and think of home ";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11040() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Moonlight before bed";
        String rstr = "Frost on ground";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11041() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Frost on ground";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11042() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Frost on ground";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11043() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11044() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11045() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11046() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String mstr = "";
        String rstr = "";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11047() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11048() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "";
        String rstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11049() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11050() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11051() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11052() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11053() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11054() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11055() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11056() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11057() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11058() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the river flows to sea";
        String mstr = "Moonlight before bed frost on the ground";
        String rstr = "Farmers weeding at noon sweat into soil";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11059() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = " The sun sets behind mountains the river flows to sea ";
        String mstr = " Moonlight before bed frost on the ground ";
        String rstr = " I look up at the moon I bow and think of home ";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11060() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Moonlight before bed";
        String rstr = "Frost on ground";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11061() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Frost on ground";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11062() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before bed";
        String mstr = "Frost on ground";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11063() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11064() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String mstr = "";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11065() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        String rstr = "Clouds think of her dress flowers think of her face spring breeze brushes the railing with dew";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11066() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String mstr = "";
        String rstr = "";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11067() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11068() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "";
        String mstr = "";
        String rstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11069() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11070() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11071() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11072() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11073() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11074() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets behind mountains the Yellow River flows to sea to see further climb one more floor";
        String mstr = "Moonlight before my bed I wonder if it is frost on the ground I look up at the moon I bow my head and think of home";
        String rstr = "Farmers weeding at noon sweat drops into the soil who knows the food on the plate every grain is hard work";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11075() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = null;
        String mstr = null;
        String rstr = null;
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11076() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11077() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible P";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11078() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible P";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11079() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11080() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11081() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11082() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11083() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11084() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "system/fonts/SIMKAI.TTF");
        String lstr = "Moonlight before bed";
        String mstr = "Frost on ground";
        String rstr = "Look up at the moon";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11085() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "system/SIMKAI.TTF");
        String lstr = "Moonlight before bed";
        String mstr = "Frost on ground";
        String rstr = "Look up at the moon";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11086() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "abcdefg");
        String lstr = "time";
        String mstr = "month";
        String rstr = "year";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11087() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, null);
        String lstr = "time";
        String mstr = "month";
        String rstr = "year";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11088() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "");
        String lstr = "time";
        String mstr = "month";
        String rstr = "year";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11089() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, -1);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    /**
     * Redmine issue #298
     * Likai: Recommend using canvas to draw images, then use our image print function. Suggest closing this bug.
     * <p>
     * tester: Also comment out this case in the code
     */
    void D11090() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, 4);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11091() {
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 2;
        My11addTextInLine(null, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11092() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "The sun sets The sun sets";
        String mstr = "River flows to sea The sun sets";
        String rstr = "Climb higher The sun sets";
        int mode = -1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11093() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11094() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11095() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11096() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11097() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11098() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11099() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11100() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11101() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/system/fonts/DroidSans.ttf");
        String lstr = "sun";
        String mstr = "moon";
        String rstr = "start";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11102() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "/sdcard/Download/VerifoneArabic.ttf");
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11103() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "system/fonts/abcd.ttf");
        String lstr = "The sun sets";
        String mstr = "River flows to sea";
        String rstr = "Climb higher";
        int mode = 1;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11104() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11105() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11106() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11107() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Thriller artist";
        String mstr = "Michael Jackson";
        String rstr = "Invicible POP";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11108() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11109() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11110() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11111() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "في ماء قمر";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11112() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Moonlight before bed frost on ground frost";
        String mstr = "Moonlight before bed frost on the ground frost";
        String rstr = "Moonlight before bed frost on ground frost";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11113() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_CHINESE);
        String lstr = "AMOUNT:";
        String mstr = "";
        String rstr = "RRN:000008000000 EXPIRY:XXXXXX";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11114() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "sdcard/download/ARIALNB.TTF");
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
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "sdcard/download/ARIALNB.TTF");
        String lstr = "SEQUENCE : 0008 MASTER(T)";
        String mstr = "";
        String rstr = "RRN : 000008000000 EXPIRYP : XXXX";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }


    void D11116() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "sdcard/download/ARIALNB.TTF");
        String lstr = "SEQUENCE : 0008 MASTER(T)";
        String mstr = "";
        String rstr = "RRN : 000008000000 EXPIRY : XXXX";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);

//        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
//        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
//        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, "sdcard/download/ARIALNB.TTF");
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
////        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
////        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean,false);
////        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
////        String lstr = "Thriller artist";
////        String mstr = "Michael Jackson";
////        String rstr = "Invicible POP";
////        int mode = 1;
////        My11addTextInLine(format,lstr,mstr,rstr,mode);
////        My07startPrint(new MyListener());
//    }

    void D11117() {
//        Test mixed print can successfully print all content
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format2.putInt("height", 128);
        format2.putInt("barCodeType", 4);
        My04addBarCode(format2, "1234567890ABC");

        Bundle format3 = new Bundle();
        format3.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format3.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format3.putBoolean(ConstIPrinter.addText.format.KEY_newline_boolean, ConstIPrinter.addText.format.VALUE_newline_AppendCRLF);
        format3.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        My03addText(format3, "ABCDabcd1234 test print");

        Bundle format4 = new Bundle();
        format4.putInt("offset", 50);//expectedHeight
        format4.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
        My05addQrCode(format4, "18620518880");

        Bundle format5 = new Bundle();
        format5.putInt("offset", 0);
        format5.putInt("width", 384);
        format5.putInt("height", 384);
        format5.putInt("gray", 100);
        Bitmap imgs = getBitmapByte(R.drawable.simple);
//            My06addImage(format5, imgs);
//            logUtils.addCaseLog("Image added");
        My07startPrint(new MyListener());
        My08feedLine(3);

        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_English);
        String lstr = "Moonlight before bed frost on ground frost";
        String mstr = "Moonlight before bed frost on the ground frost";
        String rstr = "Moonlight before bed frost on ground frost";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);

        My07startPrint(new MyListener());
    }

    void D11118() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11119() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11120() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11121() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 0;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11122() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11123() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11124() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_DH_24_48_IN_BOLD);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11125() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putString(ConstIPrinter.addTextInLine.format.KEY_GlobalFont_String, ConstIPrinter.addTextInLine.format.VALUE_GlobalFont_Arabic);
        String lstr = "فراغ شمس";
        String mstr = "";
        String rstr = "في صحراء نجم";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11126() {
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr1 = "Font0Left";
        String mstr1 = "Font0Middle";
        String rstr1 = "Font0Right";
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format2.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format2.putFloat("scale_w", (float) 2.0);
        format2.putFloat("scale_h", (float) 2.0);
        String lstr2 = "Font0 Float2 and Font3 print effect is the same";
        String mstr2 = "Font0 Float2 and Font3 print effect is the same";
        String rstr2 = "Font0 Float2 and Font3 print effect is the same";
        Bundle format3 = new Bundle();
        format3.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_LARGE_32_32);
        format3.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr3 = "Font0 Float2 and Font3 print effect is the same";
        String mstr3 = "Font0 Float2 and Font3 print effect is the same";
        String rstr3 = "Font0 Float2 and Font3 print effect is the same";
        int mode = 2;
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format3, lstr3, mstr3, rstr3, mode);
        My07startPrint(new MyListener());
    }
    void D11127() {
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr1 = "Font1Left";
        String mstr1 = "Font1Middle";
        String rstr1 = "Font1Right";
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_SMALL_16_16);
        format2.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format2.putFloat("scale_w", (float) 2.0);
        format2.putFloat("scale_h", (float) 2.0);
        String lstr2 = "Font0 Float2 and Font3 print effect is the same";
        String mstr2 = "Font0 Float2 and Font3 print effect is the same";
        String rstr2 = "Font0 Float2 and Font3 print effect is the same";
        Bundle format3 = new Bundle();
        format3.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format3.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format3.putFloat("scale_w", (float) 2.0);
        format3.putFloat("scale_h", (float) 2.0);
        String lstr3 = "Font1 Float2 is larger than the previous font";
        String mstr3 = "Font1 Float2 is larger than the previous font";
        String rstr3 = "Font1 Float2 is larger than the previous font";
        int mode = 2;
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format3, lstr3, mstr3, rstr3, mode);
        My07startPrint(new MyListener());
    }

    void D11128() {
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr1 = "Font1 normal font";
        String mstr1 = "Font1 normal font";
        String rstr1 = "Font1 normal font";
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format2.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format2.putFloat("scale_w", (float) 0.1);
        format2.putFloat("scale_h", (float) 0.1);
        String lstr2 = "0.1x height width scale";
        String mstr2 = "0.1x height width scale";
        String rstr2 = "0.1x height width scale";
        Bundle format3 = new Bundle();
        format3.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format3.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format3.putFloat("scale_w", (float) 0.5);
        format3.putFloat("scale_h", (float) 0.5);
        String lstr3 = "0.5x height width scale";
        String mstr3 = "0.5x height width scale";
        String rstr3 = "0.5x height width scale";
        Bundle format4 = new Bundle();
        format4.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format4.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format4.putFloat("scale_w", (float) 1.4);
        format4.putFloat("scale_h", (float) 1.4);
        String lstr4 = "1.4x height width scale";
        String mstr4 = "1.4x height width scale";
        String rstr4 = "1.4x height width scale";
        Bundle format5 = new Bundle();
        format5.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format5.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format5.putFloat("scale_w", (float) 2.0);
        format5.putFloat("scale_h", (float) 2.0);
        String lstr5 = "2.0x height width scale";
        String mstr5 = "2.0x height width scale";
        String rstr5 = "2.0x height width scale";
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
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, 5);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putFloat("scale_w", (float) 0.1f);
        format.putFloat("scale_h", (float) 0.1f);
        String lstr = "Font5 boundary 0.1f print test";
        String mstr = "Font5 boundary 0.1f print test";
        String rstr = "Font5 boundary 0.1f print test";
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, 5);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format1.putFloat("scale_w", (float) 4.0f);
        format1.putFloat("scale_h", (float) 4.0f);
        String lstr1 = "Font5 boundary 4.0f print test";
        String mstr1 = "Font5 boundary 4.0f print test";
        String rstr1 = "Font5 boundary 4.0f print test";
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, 5);
        format2.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr2 = "Font5 print test";
        String mstr2 = "Font5 print test";
        String rstr2 = "Font5 print test";
        int mode = 2;
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My07startPrint(new MyListener());
    }

    void D11130() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putFloat("scale_w", (float) 2.4f);
        format.putFloat("scale_h", (float) 0.8f);
        String lstr = "Test different height width scale values";
        String mstr = "Test different height width scale values";
        String rstr = "Test different height width scale values";
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format1.putFloat("scale_w", (float) 0.8f);
        format1.putFloat("scale_h", (float) 2.4f);
        String lstr1 = "Test different height width scale values";
        String mstr1 = "Test different height width scale values";
        String rstr1 = "Test different height width scale values";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My07startPrint(new MyListener());
    }

    void D11131() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr = "Font1 original font";
        String mstr = "Font1 original font";
        String rstr = "Font1 original font";
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format1.putFloat("scale_w", (float) 2.0f);
        String lstr1 = "Font1 width only";
        String mstr1 = "Font1 width only";
        String rstr1 = "Font1 width only";
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format2.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format2.putFloat("scale_h", (float) 2.0f);
        String lstr2 = "Font1 height only";
        String mstr2 = "Font1 height only";
        String rstr2 = "Font1 height only";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My11addTextInLine(format2, lstr2, mstr2, rstr2, mode);
        My07startPrint(new MyListener());
    }

    void D11132() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        String lstr = "Font1 original font";
        String mstr = "Font1 original font";
        String rstr = "Font1 original font";
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format1.putFloat("scale_w", (float) 1.5f);
        format1.putFloat("scale_h", (float) 1.5f);
        String lstr1 = "Add left column only";
        String mstr1 = null;
        String rstr1 = null;
        Bundle format2 = new Bundle();
        format2.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format2.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format2.putFloat("scale_w", (float) 1.5f);
        format2.putFloat("scale_h", (float) 1.5f);
        String lstr2 = null;
        String mstr2 = "Add middle column only";
        String rstr2 = null;
        Bundle format3 = new Bundle();
        format3.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format3.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format3.putFloat("scale_w", (float) 1.5f);
        format3.putFloat("scale_h", (float) 1.5f);
        String lstr3 = null;
        String mstr3 = "Add right column only";
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_NORMAL_24_24);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putFloat("scale_w", (float) 2.0f);
        format.putFloat("scale_h", (float) 2.0f);
        Bundle format1 = new Bundle();
        format1.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, ConstIPrinter.addTextInLine.format.VALUE_FontSize_NORMAL_24_24);
        format1.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format1.putFloat("scale_w", (float) 2.0f);
        format1.putFloat("scale_h", (float) 2.0f);
        String lstr1 = "addtextinline print same font";
        String mstr1 = "addtextinline print same font";
        String rstr1 = "addtextinline print same font";
        int mode = 2;
        My03addText(format, "addtext double width print test");
        My11addTextInLine(format1, lstr1, mstr1, rstr1, mode);
        My07startPrint(new MyListener());
    }

    void D11134() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, 5);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putFloat("scale_w", (float) 0.0f);
        format.putFloat("scale_h", (float) 0.0f);
        String lstr = "Font5 error when height width scale is 0";
        String mstr = "Font5 error when height width scale is 0";
        String rstr = "Font5 error when height width scale is 0";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D11135() {
        Bundle format = new Bundle();
        format.putInt(ConstIPrinter.addTextInLine.format.KEY_FontSize_int, 5);
        format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_NO);
        format.putFloat("scale_w", (float) 4.1f);
        format.putFloat("scale_h", (float) 4.1f);
        String lstr = "Font5 error when height width scale is 4.1";
        String mstr = "Font5 error when height width scale is 4.1";
        String rstr = "Font5 error when height width scale is 4.1";
        int mode = 2;
        My11addTextInLine(format, lstr, mstr, rstr, mode);
        My07startPrint(new MyListener());
    }

    void D12001() {
        Bundle format = new Bundle();

        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, ConstIPrinter.addText.format.VALUE_FontSize_SMALL_16_16);
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
        My03addText(format, "ABCDabcd1234 test print");

        format.putInt("width", 192);
        format.putInt("height", 128);
        My04addBarCode(format, "13524044282");

        format.putInt("offset", 50);
        format.putInt(ConstIPrinter.addQrCode.format.KEY_Height_String, 128);
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
        My03addText(format, "EMV card number confirmation process print info:\nCard No:        Exchange Rate:");
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


    //byte array to image
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

    //byte array to hex string
    public String byte2string(byte[] data) {
        if (data == null || data.length <= 1) return "0x";
        if (data.length > 200000) return "0x";
        StringBuffer sb = new StringBuffer();
        int buf[] = new int[data.length];
        //byte array to decimal
        for (int k = 0; k < data.length; k++) {
            buf[k] = data[k] < 0 ? (data[k] + 256) : (data[k]);
        }
        //decimal to hexadecimal
        for (int k = 0; k < buf.length; k++) {
            if (buf[k] < 16) sb.append("0" + Integer.toHexString(buf[k]));
            else sb.append(Integer.toHexString(buf[k]));
        }
        return "0x" + sb.toString().toUpperCase();
    }

    private Bitmap getBitmapByte(int id) {
        Resources res = context.getResources(); // Get resource object
        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
        return bitmap;
    }

    private byte[] getImgByte() {
//        String path = Environment.getExternalStorageDirectory().getPath() + "/confused.png";
        String path = BASEPATH + "/pic/simple.bmp";
        Log.d("TAG", path);
        File mFile = new File(path);
        //If the file exists
        if (mFile.exists()) {
            // Bitmap bitmap = BitmapFactory.decodeFile(path);
            byte[] img = image2byte(path);
            return img;
        } else {
            logUtils.addCaseLog("File not found");
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
                msg.getData().putString("msg", "Print error, error code:" + error);
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
                msg.getData().putString("msg", "Print complete");
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
        format.putInt(ConstIPrinter.BUNDLE_PRINT_FONT, size);
        if (bold) {
            format.putBoolean(ConstIPrinter.addText.format.KEY_StyleBold_boolean, ConstIPrinter.addText.format.VALUE_StyleBold_YES);
        }
        format.putInt(ConstIPrinter.BUNDLE_PRINT_ALIGN, ConstIPrinter.addText.format.VALUE_Alignment_LEFT);
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
            //Get class object from class file
            Class aClass = Class.forName("moudles.PrintBtMoudle");
            this.printMsgTool("-------Printer Module------");
            this.printMsgTool("Automated test cases start executing");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("D") && !method.getName().equals("D15_AUTO")) {
                    this.printMsgTool(method.getName() + " method starts executing......");
                    method.invoke(this);
                    this.printMsgTool(method.getName() + " method finished!!!");
                    Thread.sleep(1000);
                }
            }
            this.printMsgTool("Automated test cases execution completed");
            logUtils.addCaseLog("Automated test cases execution completed");
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
