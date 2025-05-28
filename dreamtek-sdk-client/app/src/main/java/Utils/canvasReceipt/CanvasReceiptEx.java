package Utils.canvasReceipt;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simon on 2019/2/20.
 */

public class CanvasReceiptEx {

    private static final String TAG = "PrinterEx";
    public static final int MAX_WIDTH = 384;
    public int max_receipt_count = 3+1;

    CanvasReceiptBase printerReceipt[];
//    CanvasReceiptBase printerCanvas;

    /* receipt count include the receipt for showing
    by default,
    index 0 for showing;
    index 1 for the 1st receipt
          2 for the 2nd receipt
          3 for the 3rd receipt
    */
    public CanvasReceiptEx( int receiptCount) {
//        printerCanvas = new CanvasReceiptBase(false);
        if( receiptCount <= 0 ){
            max_receipt_count = 4;
        } else {
            max_receipt_count = receiptCount;
        }
        printerReceipt = new CanvasReceiptBase[max_receipt_count];
        printerReceipt[0] = new CanvasReceiptBase(false );
        for (int i = 1; i < max_receipt_count; i++) {
            printerReceipt[i] = new CanvasReceiptBase(true);
        }
    }

    public int addText(int format, String text, int printerMode, String fontFilename, boolean feedLine, int color) throws RemoteException {
        int ret = 0;
        if( 0 == (printerMode & ReceiptItemDefine.PStyle_receipt_all)  ) {
            printerMode = ReceiptItemDefine.PStyle_receipt_all;
        }
        if( (printerMode & ReceiptItemDefine.PStyle_receipt_1 ) != 0 && max_receipt_count > 1 ){
            ret = printerReceipt[1].addText(format,text, fontFilename, feedLine, Color.BLACK);
        }
        if( (printerMode & ReceiptItemDefine.PStyle_receipt_2 ) != 0 && max_receipt_count > 2 ){
            ret = printerReceipt[2].addText(format,text, fontFilename, feedLine, Color.BLACK);
        }
        if( (printerMode & ReceiptItemDefine.PStyle_receipt_3 ) != 0 && max_receipt_count > 3){
            ret = printerReceipt[3].addText(format,text, fontFilename, feedLine, Color.BLACK);
        }
        if( (printerMode & ReceiptItemDefine.PStyle_receipt_show ) != 0 ){
            ret = printerReceipt[0].addText(format,text, fontFilename, feedLine, color);
        }
        return ret;
    }

    public void addImage(int format, byte[] imageData) throws RemoteException {
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].addImage(format, imageData);
        }
    }
    public void addImage(int format, Bitmap bitmap) throws RemoteException {
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].addImage(format, bitmap);
        }
    }
    public void addLine(int format ) throws RemoteException {
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].addLine(format);
        }
    }
    public void addQrCode(int format, String qrCode){
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].addQrCode(format, qrCode);
        }
    }
    public void addBarCode(int format, String barcode){
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].addBarCode(format, barcode);
        }
    }
    public void feedPixel(Bundle format, int pixel ) throws RemoteException {
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].feedPixel(format,pixel);
        }
    }
    public void writeRuler( int mode ){
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].writeRuler(mode);
        }
    }
    public void scrollBack( ){
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].scrollBack();
        }
    }

    // 0 for showing, 1,2,3 for print
    public Bitmap getBitmap(int index){
        return printerReceipt[index].getBitmap();
    }
    public int getHeight( int index) {
        return printerReceipt[index].getHeight();
    }

    public byte[] getData( int index ) {
        return printerReceipt[index].getData();
    }

    public byte[] getBytesByBitmap(Bitmap bitmap, int index) {
        return printerReceipt[index].getBytesByBitmap(bitmap);
    }

    String receiptFn = "";
    public String save(String path, String comment ){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        String path_date = "/" + sdfDate.format( date ) + "/";
        String fn = sdfDateTime.format(date);
        if(fn.compareTo( receiptFn) == 0 ){
            sdfDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS");
            fn = sdfDateTime.format(date);
        } else {
            receiptFn = fn;
        }
        for (int i = 0; i < max_receipt_count; i++) {
            printerReceipt[i].save(  path  + path_date + fn + "_" + comment + "_"+ i +".png");;
        }
        return fn + "_" + comment;
    }

    public void set_line_space( int line_space[] ){
        for (int i = 0; i < max_receipt_count && i < line_space.length ; i++) {
            printerReceipt[i].setLine_space( line_space[i]);
        }

    }
}
