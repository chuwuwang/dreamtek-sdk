package moudles.newModules;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.PrinterListener;
import com.dreamtek.smartpos.deviceservice.aidl.QrCodeContent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.canvasReceipt.*;
import base.MyApplication;
//import moudles.PintBtMoudle;

/**
 * Created by Simon on 2021/10/21
 */
public class PrinterModule extends TestModule {
    private static final String TAG = "PrinterModule";

    public int T_getStatus() throws RemoteException {
        int code = iPrinter.getStatus();
        return code;
    }

    public void T_setGray(String gray)  throws RemoteException {
        iPrinter.setGray( Integer.valueOf(gray) );
    }

    public void T_addText(String sFormat, String text)  throws RemoteException {
        Bundle param = convert( sFormat, null );
        iPrinter.addText(param, text);
    }

    public void T_addBarCode(String sFormat, String barcode)  throws RemoteException  {
        Bundle param = convert( sFormat, null );
        iPrinter.addBarCode( param, barcode);
    }

    public void T_addQrCode(String sFormat, String qrCode)  throws RemoteException {
        Bundle param = convert( sFormat, null );
        iPrinter.addQrCode( param, qrCode);
    }

    public void T_addImage(String sFormat, String sImageData) throws RemoteException {
//        Bundle param = convert( sFormat, null );
//        byte[] imageData = StringUtil.hexStr2Bytes(sImageData);

//        iPrinter.addImage(param, imageData);
    }
    public void T_addImage(String sFormat, String path,  String filename) throws RemoteException {
//        Bundle param = convert( sFormat, null );
//        iPrinter.addImage(param, getImgByte(path + "/" + filename) );
    }

    public int T_startPrint( String waitingFinish )  throws RemoteException {
        Log.d(TAG, "T_startPrint");
        boolean isBlockPrinting = Boolean.valueOf( waitingFinish );
        int ret = 0;
        waitingPrintResult(20);    // 等待上次打印完成

        // 开始打印
        synchronized ( isPrintingLock ) {
            isPrinting = true;
        }
//            silencePrinting = silence;
        iPrinter.startPrint( printerModuleListener );
        if( !silencePrinting ){
            logUtils.addCaseLog("startPrint调用完成");
        }

        if( isBlockPrinting ){
            // 等待打印结果
            ret = waitingPrintResult(300);
        }

        return ret;
    }

    public void T_feedLine(String lines)  throws RemoteException {
        iPrinter.feedLine(Integer.valueOf(lines) );
    }

    public void T_addQrCodesInLine(String sHeight, String sLeftOffset, String barcode, String sHeight2, String sLeftOffset2, String barcode2 ) throws RemoteException {

        List<QrCodeContent> qrCodes = new ArrayList<>();

        QrCodeContent qrCodeContent = new QrCodeContent( Integer.valueOf( sHeight), Integer.valueOf( sLeftOffset), barcode );
        qrCodes.add( qrCodeContent );
        if( sHeight2.length() > 0 && sLeftOffset2.length()> 0 & barcode2.length() > 0 ) {
            QrCodeContent qrCodeContent2 = new QrCodeContent( Integer.valueOf( sHeight2 ), Integer.valueOf( sLeftOffset2 ), barcode2 );
            qrCodes.add( qrCodeContent2 );
        }
        iPrinter.addQrCodesInLine(qrCodes);
    }

    public void T_setLineSpace(String space)  throws RemoteException {
        iPrinter.setLineSpace(Integer.valueOf(space) );
    }

    public void T_addTextInLine(String sFormat, String lString, String mString, String rString, String mode)  throws RemoteException {
        Bundle param = convert( sFormat, null );
        iPrinter.addTextInLine( param,  lString,  mString,  rString,  Integer.valueOf(mode) );
    }

    public void T_startSaveCachePrint()  throws RemoteException {
        iPrinter.startSaveCachePrint(printerModuleListener);
    }

    public int T_cleanCache()  throws RemoteException {
            int result = iPrinter.cleanCache();
            return result;
    }

    public int T_startPrintInEmv( String waitingFinish )  throws RemoteException  {
        Log.d(TAG, "T_startPrintInEmv");
        boolean isBlockPrinting = Boolean.valueOf( waitingFinish );
        int ret = 0;
        waitingPrintResult(20);    // 等待上次打印完成

        // 开始打印
        synchronized ( isPrintingLock ) {
            isPrinting = true;
        }
//            silencePrinting = silence;
        iPrinter.startPrintInEmv(printerModuleListener);
        if( !silencePrinting ){
            logUtils.addCaseLog("startPrint调用完成");
        }

        if( isBlockPrinting ){
            // 等待打印结果
            ret = waitingPrintResult(20);
        }

        return ret;
    }

    public void T_addScreenCapture( String sFormat )  throws RemoteException  {

        Bundle param = convert( sFormat, null );
       iPrinter.addScreenCapture( param );
//            iPrinter.startPrintInEmv(listener);
    }

    private boolean isPrinting = false;
    private static final Object isPrintingLock = new Object();
    boolean silencePrinting = false;
    int printResult = 0;

    PrinterModuleListener printerModuleListener = new PrinterModuleListener();

    class PrinterModuleListener extends PrinterListener.Stub {
        @Override
        public void onError(int error) throws RemoteException {
            printResult = error;
            synchronized ( isPrintingLock ) {
                isPrinting = false;
                isPrintingLock.notifyAll();
            }
            if( !silencePrinting ) {
                Message msg = new Message();
                msg.getData().putString("msg", "打印错误,错误码:" + error);
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onFinish() throws RemoteException {
            printResult = 0;
            synchronized ( isPrintingLock ) {
                isPrinting = false;
                isPrintingLock.notifyAll();
            }
            if( !silencePrinting ) {
                Message msg = new Message();
                msg.getData().putString("msg", "打印完成");
                handler.sendMessage(msg);
            }
        }
    }

    public int waitingPrintResult( int timeoutSeconds){
        boolean printing = true;
        long endTime = System.currentTimeMillis() + timeoutSeconds*1000 ;

        synchronized ( isPrintingLock ) {
            try {
                isPrintingLock.wait(10);    // 等待10 毫秒，清除之前未接收的消息
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printing = isPrinting;
        }
        while( printing ) {
            synchronized ( isPrintingLock ) {
                try {
                    isPrintingLock.wait(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printing = isPrinting;
            }
            if( System.currentTimeMillis() > endTime ){
                break;
            }
        };

        return printResult;
    }

    private byte[] getImgByte( String path ) {
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

    boolean hungupPrinting = false;
    public void setHungupPrinting( boolean hungupPrinting ){
        this.hungupPrinting = hungupPrinting;
    }
    public void printMsg_bk(String msg, int size, boolean bold, boolean silence, boolean emv, boolean doPrinting ) throws RemoteException {
        silencePrinting = silence;
        if( !silence ) {
            logUtils.addCaseLog("Printing: " + msg );
        }

        Bundle format = new Bundle();
        format.putInt("font", size);
        if( bold ){
            format.putBoolean( "bold", true );
        }
        format.putInt("align", 0);
        if( msg.startsWith("\n") ){
            iPrinter.addText(format, " ");
        }
        String[] lines = msg.split("\n");
        for ( String line: lines  ) {
            iPrinter.addText(format, line);
        }
        if( msg.endsWith("\n") ){
            iPrinter.addText(format, " ");
        }
        if( doPrinting && (!hungupPrinting) ) {
            if( emv ) {
                T_startPrintInEmv( "false");    // 每次打印前会等待上次打印完成，这里就不等了
            } else {
                T_startPrint( "false" );
            }
            if( !silence ) {
                logUtils.addCaseLog("startPrint调用完成");
            }
        }
    }
    CanvasReceiptContext printerCanvas = null;
    public void printMsg(String msg, int size, boolean bold, boolean silence, boolean emv, boolean doPrinting ) throws RemoteException {
        if (Build.MODEL.equals("X990 Mini") || Build.MODEL.equals("X990 PINPad")){
            return;
        }
        silencePrinting = silence;
        if( !silence ) {
            logUtils.addCaseLog("Printing: " + msg );
        }

        if( null == printerCanvas ){
            printerCanvas = new CanvasReceiptContext((MyApplication) context);
        }

        if( msg.startsWith("\n") ){
//            printerCanvas.append(ReceiptRowsDef.FEED);
            printerCanvas.append(ReceiptRowsDef.LINE_DOT_8X);
        }
        String[] lines = msg.split("\n");
        for ( String line: lines  ) {

            String[] text2Print = line.split("\\|");    // 竖线分割左右
            if ((text2Print.length == 1 && text2Print[0].trim().length() > 0)  // 一个元素且有长度
                    || text2Print.length >= 1) {
                ReceiptRowsDef rowsDef;
                if (size == 3) {
                    // error
                    rowsDef = ReceiptRowsDef.FONT_ERROR;
                } else if (size == 0) {
                    // debug
                    rowsDef = ReceiptRowsDef.FONT_DEBUG;
                } else {
                    // info
                    rowsDef = ReceiptRowsDef.FONT_INFO;
                }
                rowsDef.set("", "");
                rowsDef.setTitle(text2Print[0]);
                if (text2Print.length > 1) {
                    rowsDef.set(text2Print[1]);
                }
                printerCanvas.append(rowsDef);

                // 有些打印内容包含 | 所以会被分割为多个字符串，这里打印其他的字符串。
                for (int i = 2; i < text2Print.length ; i++) {
                    rowsDef.setTitle( "" );
                    rowsDef.set(text2Print[i]);
                    printerCanvas.append(rowsDef);
                }

                printerCanvas.append(ReceiptRowsDef.FEED);
                if (false) {
                    printerCanvas.append(ReceiptRowsDef.LINE_DOT_8X);
                }
            }
        }
        if( false ) {
            printerCanvas.append( ReceiptRowsDef.LINE_DOT_8X ); // 打印分割线
        }

        if( false ) {
            printerCanvas.append( ReceiptRowsDef.FEED_24p );
            printerCanvas.append( ReceiptRowsDef.FEED_24p );
            printerCanvas.append( ReceiptRowsDef.FEED_24p );
        }
        if( msg.endsWith("\n") ){
            printerCanvas.append(ReceiptRowsDef.FEED);
        }



        printerCanvas.draw(2);  // 画2张图，第0张用于显示，第1张用于打印

        Bitmap printerBitmap = printerCanvas.getBitmap(1);
        printerCanvas.clear();

        Bundle bundleImage = new Bundle();
        bundleImage.putInt("offset", 0);
        bundleImage.putInt("width", printerBitmap.getWidth());
        bundleImage.putInt("height", printerBitmap.getHeight());
        iPrinter.addBmpImage( bundleImage, printerBitmap);
        if( doPrinting && (!hungupPrinting) ) {
            if( emv ) {
                T_startPrintInEmv( "false");    // 每次打印前会等待上次打印完成，这里就不等了
            } else {
                T_startPrint( "false" );
            }
            if( !silence ) {
                logUtils.addCaseLog("startPrint调用完成");
            }
        }
    }

    public void appendMsg(String msg, int type, boolean emv){
        try {
            int size = 1;
            boolean bold = false;
            switch ( type & 0x07 ) {
                case Log.DEBUG:
                    size = 0;
                    break;
                case Log.ERROR:
                    size = 3;
                    bold = true;
                    break;
                default:
                    break;
            }

            printMsg(msg, size, bold, true, emv, false );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


public void printMsg(String msg, boolean silence, boolean emv){
    try {
        printMsg(msg, 1, false, silence, emv, true );
    } catch (RemoteException e) {
        e.printStackTrace();
    }
}
    public void printDbgMsg(String msg, boolean silence, boolean emv ){
        try {
            printMsg(msg, 0, true, silence, emv, true );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void printErrMsg(String msg, boolean silence, boolean emv ){
        try {
            printMsg(msg, 3, true, silence, emv, true );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
