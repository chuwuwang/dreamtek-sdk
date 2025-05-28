package Utils.canvasReceipt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Simon on 2019/4/10.
 *
 * Class {@code PrinterExBase} is for draw content on canvas
 * and the canvas can be set to printer or activity
 *
 * 2020/7/31, Simon,
 * #1, Using the MAX_WIDTH to create the 1D barcode
 * #2, Fixed the issue the Barcode is too small
 */

public class CanvasReceiptBase {

    private static int INDEX=-1;
    private final String TAG = this.getClass().getSimpleName() + INDEX;
    public static final int MAX_WIDTH = 384;

    protected int line_space = 3;
    boolean isPaperReceipt = true;

    int offsetY;
    int offsetX;
    Canvas canvas;
    Bitmap bitmap;
    Paint paint;

    String lastFontFilename = "";

    public static Map<String, Typeface> typefaceMap = null;

    public CanvasReceiptBase(boolean isPaperReceipt ) {
        --INDEX;
        this.isPaperReceipt = isPaperReceipt;

        offsetY = 0;
        offsetX = 0;

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setTextSize(16);
//        paint.setColor(Color.BLACK);
        paint.setColor(Color.BLACK);
        bitmap = Bitmap.createBitmap(MAX_WIDTH, MAX_WIDTH * 4, Bitmap.Config.ARGB_8888);
        if( isPaperReceipt ){
            bitmap.eraseColor(Color.WHITE);
        }

        canvas = new Canvas(bitmap);
        // writeRuler(0);
        if( typefaceMap == null ){
            typefaceMap = new HashMap<>();
        }
    }

    public int addText(int format, String text, String fontFilename, boolean feedLine, int color) throws RemoteException {
//        String fontFilename = fontFormat.getString("fontStyle", "");

        //
        Paint paintText = new Paint(paint);
        boolean isBoldFont = (format & (ReceiptItemDefine.PStyle_blod)) > 0;
        boolean isNewLine = feedLine;

        int typeFaceStyle = Typeface.NORMAL;

        int fontSize = (format & 0x00FF); // fontFormat.getInt(PrinterConfig.BUNDLE_PRINT_FONT);

        int justify = (format & 0x0F00)>>8;
        String justifyLeft = "",justifyRight = "";
        int justifyWidth = 0;
        if( justify > 0 ){
            justifyWidth = justify*384/16;
            String temp[] = text.split("\\|");
            if( temp.length == 2 ) {
                justifyLeft = temp[0];
                justifyRight = temp[1];
                Log.d(TAG, "Set Justify width " + justifyWidth + ", :" + justify + ", " + justifyLeft + ", " + justifyRight);
            } else if( text.startsWith("\\|")) {
                Log.d(TAG, "Set Justify width " + justifyWidth + ", :" + justify + ", Only right" );
                justifyLeft = "";
                justifyRight = text;
            } else {
                Log.d(TAG, "Set Justify width " + justifyWidth + ", :" + justify + ", Only left" );
                justifyLeft = text;
                justifyRight = "";
            }
        }

        if (fontSize == ReceiptItemDefine.PStyle_font_size_middle) {
            fontSize = 24;
        }
        else if (fontSize == ReceiptItemDefine.PStyle_font_size_small ) {
            fontSize = 16;
        } else if (fontSize == ReceiptItemDefine.PStyle_font_size_big ) {
            fontSize = 24;
//            entity.setMultipleHeight(2);
//            entity.setMultipleWidth(1);
            fontSize = 28;
            paintText.setTextScaleX(0.5f);
            isBoldFont = true;
        } else if (fontSize == ReceiptItemDefine.PStyle_font_size_large ) {
            fontSize = 32;
        } else if (fontSize == ReceiptItemDefine.PStyle_font_size_large_bold ) {
            fontSize = 32;
//            entity.setMultipleHeight(2);
//            entity.setMultipleWidth(1);
            isBoldFont = true;
        } else if (fontSize == ReceiptItemDefine.PStyle_font_size_hurge ) {
            fontSize = 48;
        }

        if( isBoldFont ){
            typeFaceStyle = Typeface.BOLD;
        }

        if( null != fontFilename && fontFilename.length() > 0 && lastFontFilename.compareTo(fontFilename) != 0 ){
            Log.d(TAG, "font size:"+ fontSize+", Style:" + fontFilename );
            lastFontFilename = fontFilename;
            Typeface typeface;
            if( typefaceMap.containsKey( fontFilename ) ) {
                typeface = typefaceMap.get( fontFilename );
            } else {
                typeface = Typeface.createFromFile(fontFilename);
                typefaceMap.put( fontFilename, typeface );
            }
            paintText.setTypeface( typeface );
            paint.setTypeface( typeface );
        }

//        int alignType = fontFormat.getInt(PrinterConfig.BUNDLE_PRINT_ALIGN);
        Paint.Align align = Paint.Align.LEFT;
        int x = 0;
        int left, right;
        if (  0 != (format & ReceiptItemDefine.PStyle_align_left) ) {
            // do NOT REMOVE here

        }
        else if (  0 != (format & ReceiptItemDefine.PStyle_align_center) ) {
            align = Paint.Align.CENTER;
            x = MAX_WIDTH / 2;
        } else if (  0 != (format & ReceiptItemDefine.PStyle_align_right) ) {
            align = Paint.Align.RIGHT;
            x = MAX_WIDTH;
        }

        paintText.setTextSize(fontSize);
        paintText.setTextAlign(align);
        paintText.setColor(color);

        offsetY += fontSize;
        int width = (int) paintText.measureText( text,0, text.length() );
        Log.d(TAG, "pixel "+width+" of " + text + ", try print at [" + offsetX + ", " + offsetY + "]");
        if( justifyWidth > 0 ) {
            width = justifyWidth;
        }

        {
            if( 0 != (format & ReceiptItemDefine.PStyle_align_left) ) {
                if (width + offsetX > MAX_WIDTH) {
                    Log.d(TAG, "Over size, write in new line, left");
                    if( offsetX > 0 ) {
                        offsetY += fontSize;
                    }
                    offsetX = 0;
                    right = MAX_WIDTH;
                } else {
                    offsetX += width;
                    right = width;
                }
                left = 0;
            }
            else if( 0 !=  (format & ReceiptItemDefine.PStyle_align_center)) {
                if ( offsetX > 0 && (width + offsetX * 2 > MAX_WIDTH) ){
                    Log.d(TAG, "Over size, write in new line, center");
                    offsetY += fontSize;
                    offsetX = 0;
                    left = 0;
                } else {
                    offsetX = (MAX_WIDTH / 2 + width / 2);
                    left = (MAX_WIDTH-width)/2;
                }
                right = left + width;
            }
            else {
                if ( offsetX> 0 && (width + offsetX > MAX_WIDTH) ) {
                    Log.d(TAG, "Over size, write in new line, right");
                    offsetY += fontSize;
                    offsetX = 0;
                    left = 0;
                } else {
                    offsetX = MAX_WIDTH;
                    left = MAX_WIDTH-width;
                }
                right = MAX_WIDTH;
            }
        }

        if( justify == 0 ){
            if( width < 384 ) {
                if( color == Color.BLACK ) {
                    // Color.BLACK means only on Paper
                    if( 0 != (format & ReceiptItemDefine.PStyle_revert_row) ){
                        // public void drawRect(float left, float top, float right, float bottom, @RecentlyNonNull Paint paint) {
                        canvas.drawRect( 0, offsetY-fontSize+2, 384, offsetY+4, paintText );
                        paintText.setColor(Color.WHITE);
                    } else if( 0 != (format & ReceiptItemDefine.PStyle_revert) ){
                        // public void drawRect(float left, float top, float right, float bottom, @RecentlyNonNull Paint paint) {
                        canvas.drawRect( left, offsetY-fontSize+2, right, offsetY+4, paintText );
                        paintText.setColor(Color.WHITE);
                    } else {
                    }
                }
                // already set align to paintText
                canvas.drawText(text, x, offsetY, paintText);
            } else {
                // 自动换行
                if( color == Color.BLACK ) {
                    // Color.BLACK means only on Paper
                    if( 0 != (format & ReceiptItemDefine.PStyle_revert_row) ){
                        paintText.setColor(Color.WHITE);
                    } else if( 0 != (format & ReceiptItemDefine.PStyle_revert) ){
                        paintText.setColor(Color.WHITE);
                    } else {
                    }
                }

                TextPaint tp = new TextPaint();
                tp.setColor( paintText.getColor() );
                tp.setStyle(Paint.Style.FILL);
                tp.setTextSize( paintText.getTextSize() );
                if( null != fontFilename && fontFilename.length() > 0  ){
                    tp.setTypeface( paintText.getTypeface() );
                }
                Layout.Alignment layoutAlign = Layout.Alignment.ALIGN_NORMAL;
                if( align == Paint.Align.RIGHT ) {
                    layoutAlign = Layout.Alignment.ALIGN_OPPOSITE;
                } else if( align == Paint.Align.CENTER ) {
                    layoutAlign = Layout.Alignment.ALIGN_CENTER;
                }
                StaticLayout myStaticLayout = new StaticLayout(
                        text,
                        tp,
                        384,
                        layoutAlign,1.0f,0.0f,false);

                // 画在画布上
                if( color == Color.BLACK ) {
                    if (0 != (format & ReceiptItemDefine.PStyle_revert_row)) {
                        // 先处理反显（上边的反显只能处理1行）
                        paintText.setColor( color );
                        canvas.drawRect(left, offsetY - fontSize + 2, right, offsetY + (myStaticLayout.getHeight()) - fontSize, paintText);
                        paintText.setColor(Color.WHITE);
                    } else if (0 != (format & ReceiptItemDefine.PStyle_revert)) {
                        // 先处理反显（上边的反显只能处理1行）
                        paintText.setColor( color );
                        for (int i = 0; i < myStaticLayout.getLineCount(); i++) {
                            int l = (int)myStaticLayout.getLineLeft(i);
                            int r = (int)myStaticLayout.getLineRight(i);

                            Log.d(TAG, "pos: (" + l + ", " + r + " )");
                            int y = offsetY - fontSize + 2 + (fontSize+2)*i;
                            canvas.drawRect(l, y , r, y + fontSize + 2, paintText);
                        }
                        paintText.setColor(Color.WHITE);
                    }
                }

                canvas.save();
                offsetY -= fontSize;    // For StaticLayout, (0,0) at top,left. Other cases, (0,0) as bottom,left
                canvas.translate(0, offsetY);
                myStaticLayout.draw(canvas );
                canvas.restore();

                offsetY += (myStaticLayout.getHeight());
                offsetX  = myStaticLayout.getWidth();

                Log.d(TAG, "Wrapped text, height:" + (myStaticLayout.getHeight()) +", Y=" + offsetY);
            }
        } else {
            if( 0 != (format & ReceiptItemDefine.PStyle_align_left) ) {
                // left
                // justifyWidth
                canvas.drawText(justifyLeft, x, offsetY, paintText);
                paintText.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(justifyRight, x + justifyWidth, offsetY, paintText);
            } else if( 0 != (format & ReceiptItemDefine.PStyle_align_center) ) {
                // left
                // justifyWidth
                paintText.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(justifyLeft, justifyWidth/2, offsetY, paintText);
                paintText.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(justifyRight, 384 - justifyWidth/2, offsetY, paintText);
            } else {
                paintText.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(justifyLeft, 384 - justifyWidth, offsetY, paintText);
                paintText.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(justifyRight, 384 , offsetY, paintText);
            }
        }


        if( isNewLine ) {
            offsetX = 0;
            offsetY += line_space;

        } else {
            offsetY -= fontSize;
        }



        return offsetX;
    }

    public void addImage(int format, byte[] imageData) throws RemoteException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length );
        addImage( format, bitmap );
    }
    public Bitmap convertImage(Bitmap bitmap){
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int color = bitmap.getPixel(j, i);
                    int g = Color.green(color);
                    int r = Color.red(color);
                    int b = Color.blue(color);
                    int a = Color.alpha(color);
                    if(g>=196&&r>=196&&b>=196){
                        a = 0;
                    }
                    color = Color.argb(a, r, g, b);
                    createBitmap.setPixel(j, i, color);
                }
            }
            return createBitmap;
        }
        return null;
    }
    public void addImage(int format, Bitmap bitmapSrc) {
        int offset = 0;
        if( null == bitmapSrc ){
            Log.e(TAG, "addImage:null" );
            return;
        }
        Log.d(TAG, "addImage : " + bitmapSrc.getHeight() + "," + bitmapSrc.getWidth() );
        int expectWidth = (format & ReceiptItemDefine.PStyle_size);

        Bitmap bitmap;
        if( expectWidth == 0 ){
            bitmap = bitmapSrc;
        } else {
            // zoom
            float scale = ((float)expectWidth) / bitmapSrc.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmapSrc,0,0, bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix,true);
        }

        {
            offset = 0;
            if( 0 != (format & ReceiptItemDefine.PStyle_align_left) ){
                // left
            } else if( 0 != (format & ReceiptItemDefine.PStyle_align_center) ){
                // center
                offset = (MAX_WIDTH-bitmap.getWidth())/2;
            } else {
                offset = (MAX_WIDTH-bitmap.getWidth());
                // right
            }
        }

//        Log.d(TAG, "addImage, offset:" + offset );
        if( !isPaperReceipt ){
            bitmap = convertImage(bitmap);    // the convert is using for change color, remove the background
        }
        canvas.drawBitmap( bitmap, offset, offsetY, null );
        scrollDown( bitmap.getHeight() + 2);
    }
    // draw horizontal line
    public void addLine(int format) throws RemoteException {
        int width = format & 0x0F;
        Paint paintLIne = new Paint( paint );
        if( (format & 0xFF0) == 0 ){
            paintLIne.setPathEffect(null);
        } else {
            int length = (format & 0xFF0) >> 4;
            paintLIne.setPathEffect(new DashPathEffect(new float[]{length , length  }, 0));
        }
        paintLIne.setStrokeWidth(width);

        offsetY += 2;
        offsetY += 2;
        canvas.drawLine(1,offsetY,MAX_WIDTH-1, offsetY, paintLIne );
        offsetY += width;
        offsetY += 2;
    }
    public void addQrCode(int format, String qrCode){
//        int size = (format & ReceiptItemDefine.PStyle_size);
//        Bitmap bitmap = create1D2DcodeImage(qrCode,size, BarcodeFormat.QR_CODE );
//        addImage( format, bitmap );

    }
    public void addBarCode(int format, String barcode){
        // BarcodeFormat.CODE_128
//        int size = (format& ReceiptItemDefine.PStyle_size);
//        Bitmap bitmap = create1D2DcodeImage(barcode,size, BarcodeFormat.CODE_128 );
//        addImage( (format & ReceiptItemDefine.PStyle_size_set_0)+384, bitmap );
    }

    public void feedPixel(Bundle format, int pixel ) throws RemoteException {
        offsetY += pixel;
    }
    public void writeRuler( int mode ){
        int x = 0;
        int y = 0;
        paint.setStrokeWidth(1);
        int c = paint.getColor();
        paint.setColor(Color.GRAY);
        paint.setPathEffect(new DashPathEffect(new float[]{16f,48f}, 0));
        for( x = -8; x <= MAX_WIDTH*10;  ){
            canvas.drawLine(x,0,x, MAX_WIDTH, paint );
            canvas.drawLine(0,x,MAX_WIDTH, x, paint );
            x+= 32;
        }
        paint.setColor(c);
    }

    protected int lastScrollDownPixel = 0;
    public int scrollDown( int pixel ){
        offsetY += pixel;
        lastScrollDownPixel = pixel;
        return offsetY;
    }

    public int scrollBack(){
        int a = lastScrollDownPixel;
        offsetY -= lastScrollDownPixel;
        lastScrollDownPixel = 0;
        return a;
    }

    public Bitmap getBitmap(){
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,0,MAX_WIDTH, offsetY);
        return newBitmap;
    }
    public int getHeight() {
        return offsetY;
    }

    public byte[] getData() {
        return getBytesByBitmap(bitmap);
    }

    public byte[] getBytesByBitmap(Bitmap bitmap) {

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,0,MAX_WIDTH, offsetY);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();

    }
//
//    private Bitmap create1D2DcodeImage(String content, int size, BarcodeFormat barcodeFormat) {
//        Log.d(TAG, "create1D2DcodeImage, size:" + size + ", content:" + content);
//        if( content.length() == 0 ){
//            return null;
//        }
//        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8" );
//        hints.put(EncodeHintType.MARGIN, 1);
//        if( size == 0 ){
//            size = 360;
//        }
//        int QRCODE_SIZE = size;
//        int QRCODE_SIZEf = size;
//        int width = size;
//        int height = size;
//        if( barcodeFormat != BarcodeFormat.QR_CODE ){
//            // bar code
//            width = MAX_WIDTH ;
//        }
//        BitMatrix bitMatrix = null;
//        try {
////                bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
//            bitMatrix = new MultiFormatWriter().encode(content, barcodeFormat, width, height, hints);
//
//            width = bitMatrix.getWidth();
//            height = bitMatrix.getHeight();
//
//            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            b.eraseColor(Color.WHITE);
//            for( int y=0; y< height ; y++){
//                for( int x=0; x<width; x++){
//                    if( bitMatrix.get(x,y)){
//                        b.setPixel(x,y, Color.BLACK);
//                    }
//                }
//            }
//            return b;
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        } catch ( Exception e ){
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void save(String path){
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,0,MAX_WIDTH, offsetY);
        saveBitmap( newBitmap, path);
    }

    /**
     *
     * @param bitmap Bitmap
     */
    public static void saveBitmap(Bitmap bitmap, String path) {
        String savePath = path;
        File filePic;
        try {
            filePic = new File(savePath);
            if (!filePic.getParentFile().exists()) {
                if (!filePic.getParentFile().mkdirs()) {
                    Log.e("tag", "mkdirs fails:" + filePic.getParentFile());
                }
            }
            if (!filePic.exists()) {
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e("tag", "saveBitmap to "+  savePath  +": " + e.getMessage());
            return;
        }
        Log.i("tag", "saveBitmap success: " + filePic.getAbsolutePath());
    }

    public void setLine_space(int line_space) {
        this.line_space = line_space;
    }

    private static class Log{
        static void i( String tag, String log){

        }
        static void e( String tag, String log){

        }
        static void w( String tag, String log){

        }
        static void d( String tag, String log){

        }
        static void v( String tag, String log){
            // LogUtil.v( tag, log);
        }
    }

}

