package Utils.canvasReceipt;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Simon on 2019/2/27.
 */

public class ReceiptItem {
    private static final int DEFAULT_FONT_SIZE = 16;
    private static final int DEFAULT_FONT_COLOR = Color.BLUE;  // only for show the receipt on screen

    public String sValue;
    public int style;   // refer the PrinterDefine definition.
//    public int size;    // font size for print string, barcode height for barcode, QR code board size for QR code
    public int iValue;
    public int color;   // the color is ONLY for showing on screen
    public String fontFile;

    public Bitmap bitmap;   // for IMAGE_RES

    public ReceiptItem(){
        set("", DEFAULT_FONT_SIZE, 0, 0);
    }
    public ReceiptItem(ReceiptItem pe){
        set(pe.sValue, 0, pe.style, pe.iValue );
        this.fontFile = pe.fontFile;
        this.bitmap = pe.bitmap;
    }
    public ReceiptItem( int size ){
        set("", size, 0, 0);
    }
    public ReceiptItem(String sValue ){
        set(sValue, DEFAULT_FONT_SIZE, 0, 0);
    }
    public ReceiptItem(String sValue, String fontFile ){
        set(sValue, DEFAULT_FONT_SIZE, 0, 0);
        this.fontFile = fontFile;
    }
    public ReceiptItem(String sValue, int size){
        set(sValue, size, 0, 0);
    }
    public ReceiptItem(String sValue, int size, String fontFile ){
        set(sValue, size, 0, 0);
        this.fontFile = fontFile;
    }
    public ReceiptItem(String sValue, int size, int style  ){
        set(sValue, size, style, 0);
    }
    public ReceiptItem(String sValue, int size, int style, int iValue ){
        set(sValue, size, style, iValue);
    }
    public ReceiptItem(String sValue, int size, int style, String fontFile ){
        set(sValue, size, style, 0);
        this.fontFile = fontFile;
    }
    public ReceiptItem(String sValue, int size, int style, int iValue, String fontFile ){
        set(sValue, size, style, iValue);
        this.fontFile = fontFile;
    }
    public ReceiptItem(String sValue, int size, int style, int iValue, String fontFile, int color ){
        set(sValue, size, style, iValue, color);
        this.fontFile = fontFile;
    }

    public ReceiptItem(String sValue, String font_bold, int color) {
        set(sValue, DEFAULT_FONT_SIZE, 0, 0);
        this.fontFile = fontFile;
        this.color = color;
    }

    public void set(String sValue, int size, int style, int iValue ){
        this.sValue = sValue;
        this.style = (style|size);
        this.iValue = iValue;
        this.fontFile = ReceiptItemDefine.Font_default;

        this.color = DEFAULT_FONT_COLOR;
    }
    public void set(String sValue, int size, int style, int iValue, int color ){
        this.sValue = sValue;
        this.style = (style|size);
        this.iValue = iValue;
        this.fontFile = ReceiptItemDefine.Font_default;

        this.color = color;
    }
    public void set(int size ){
        this.style = ((this.style&ReceiptItemDefine.PStyle_size_set_0) |size);
    }
    public void setAlignment( int alignment ){
        this.style &= ReceiptItemDefine.PStyle_align_remove;
        this.style |= alignment;
    }

}