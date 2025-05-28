package Utils.canvasReceipt;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2020/4/2
 *
 * 2020/7/31, Simon
 * #1, Renname the setPrinterItems() to append
 */
public class CanvasReceipt {
    private final String TAG = this.getClass().getSimpleName();

    protected CanvasReceiptEx printerEx;

    protected List<ReceiptRow> printerItems = null;

    int line_space[];


    public CanvasReceipt(){
    }

    public void append(List<ReceiptRowsDef> printerItems) {
        if( printerItems.size() > 0 ){
            append(printerItems, printerItems.get(0));
        }
    }

    public void append(List<ReceiptRowsDef> printerItems, ReceiptRowsDef r) {
        if (this.printerItems == null) {
            this.printerItems = new ArrayList<>();
        }
        for (ReceiptRowsDef printerItem : printerItems
        ) {

            this.printerItems.add(printerItem.toReceiptRows());
        }
    }
    public void append(ReceiptRowsDef printerItem) {
        if (this.printerItems == null) {
            this.printerItems = new ArrayList<>();
        }
        this.printerItems.add(printerItem.toReceiptRows());
    }
    public void append(ReceiptRow printerItem) {
        if (this.printerItems == null) {
            this.printerItems = new ArrayList<>();
        }
        this.printerItems.add(printerItem);
    }
    public void append(List<ReceiptRow> printerItems, ReceiptRow r ) {
        if( null == printerItems ){
            return;
        }
        if (this.printerItems == null) {
            this.printerItems = new ArrayList<>();
        }
        this.printerItems.addAll( printerItems );
    }
    public void clear(){
        if( null != printerItems ){
            printerItems.clear();
        }
    }

    public void initializeData(Bundle extraItems) {
        Log.d(TAG, "initializeData");

        for (ReceiptRowsDef p : ReceiptRowsDef.values()
        ) {
            if( p == ReceiptRowsDef.LOGO_CACHE ){
                continue;
            }
            p.restore();
        }
    }

    private void initializeDefaltRecp() {
        printerItems = new ArrayList<>();

        for (ReceiptRowsDef p : ReceiptRowsDef.values()
        ) {
            printerItems.add(p.toReceiptRows());
        }
    }

    public static void initialize() {
//        PrinterCanvas.iPrinter = DeviceHelper.getInstance().getPrinter();
    }

    protected Bitmap getAssetBitmap(String path ){
        return null;
    }
    protected Bitmap getResBitmap(int id ) {
        Log.e(TAG, "cannot call getResBitmap");
        return null;
    }


    public Bitmap convertBitmap(ReceiptRowType printerItemType, ReceiptItem printerElement) {
        Log.d(TAG, "Try convert bitmap:" + printerElement.sValue);
        Bitmap bitmap;
        if (printerItemType == ReceiptRowType.IMAGE_ASSETS) {
            bitmap = getAssetBitmap( printerElement.sValue );
            if( null == bitmap ){
                return null;
            }
        } else {
            bitmap = Bitmap.createBitmap(384, 4, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
        }

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();


//        int colorThreshold=0xFFa0a0a0;    //  0xFF------ for revert
//        int colorThreshold=0xFF606060;    //  0xFF------ for revert, 60 much white
//                        int colorThreshold=0xFF404040;    //  0xFF------ for revert, 40 more white
        int colorThreshold = 0;
        if ((printerElement.style & ReceiptItemDefine.PStyle_revert) == ReceiptItemDefine.PStyle_revert) {
            colorThreshold |= 0xFF000000;
        }
        if ((printerElement.style & ReceiptItemDefine.PStyle_image_contrast_light) == ReceiptItemDefine.PStyle_image_contrast_light) {
            colorThreshold |= 0x00a0a0a0;
        } else if ((printerElement.style & ReceiptItemDefine.PStyle_image_contrast_normal) == ReceiptItemDefine.PStyle_image_contrast_normal) {
            colorThreshold |= 0x00808080;
        } else if ((printerElement.style & ReceiptItemDefine.PStyle_image_contrast_heavy) == ReceiptItemDefine.PStyle_image_contrast_heavy) {
            colorThreshold |= 0x00404040;
        }

//                            printerEx.addImage(fmtImage, bitmap);
//                            printerEx.feedPixel( null, 8);

        // convert bitmap -- start
        int r, g, b;
        int r_t, g_t, b_t;
        r_t = ((colorThreshold & 0x00FF0000) >> 16);
        g_t = ((colorThreshold & 0x0000FF00) >> 8);
        b_t = ((colorThreshold & 0x000000FF));
        Log.d(TAG, "Color Threshold:" + r_t + ", " + g_t + ", " + b_t);

        int pixels[] = new int[width * height];
        int pixels2[] = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int n = 0;
        for (int i = 0; i < height; i++) {
//                                String line ="Pixel:";
            for (int j = 0; j < width; j++) {
                int pixel = pixels[n];
//                                    if( j < 64 ){
//                                        line += Integer.toHexString(pixel);
////                                        line += ",";
//                                    }
                r = ((pixel & 0x00FF0000) >> 16);
                g = ((pixel & 0x0000FF00) >> 8);
                b = ((pixel & 0x000000FF));
                if ((colorThreshold & 0x00FFFFFF) > 0) {
                    // convert color
                    if (r > r_t) {
                        r = 0xFF;
                    }
                    if (g > g_t) {
                        g = 0xFF;
                    }
                    if (b > b_t) {
                        b = 0xFF;
                    }

                    int c = (r < g) ? r : g;
                    c = (c < b) ? c : b;

                    r = c;
                    g = c;
                    b = c;

//                                    pixels[n] = (0xFF000000 + (r << 16) + (g << 8) + b );
                    pixels[n] = (0xFF000000 + (c << 16) + (c << 8) + c);
                    pixels2[n] = pixels[n];
                }

                if ((colorThreshold & 0xFF000000) == 0xFF000000) {
                    // revert
                    if ((r + g + b) < 600) {
                        r = 0xFF;
                        g = 0xFF;
                        b = 0xFF;
                    } else {
                        r = 0;
                        g = 0;
                        b = 0;
//                                            r = 0xFF - r;
//                                            g = 0xFF - g;
//                                            b = 0xFF - b;
                    }
                    pixels[n] = (0xFF000000 + (r << 16) + (g << 8) + b);
                }

//                                    if( j < 64 ){
//                                        line += "-";
//                                        line += Integer.toHexString(pixels[n]);
//                                        line += ",";
//                                    }

                n++;
            }
//                                Log.d("PIXEL", line);
        }
//                            bitmap = Bitmap.createBitmap(pixels2, 0, width, width, height, Bitmap.Config.ARGB_8888);
//                            // convert bitmap -- end
//                            printerEx.addImage(fmtImage, bitmap);
//                            printerEx.feedPixel( null, 8);

        bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        // convert bitmap -- end
        return bitmap;
            }

            // receiptCount
    public byte[] draw( int receiptCount) {
        Log.d(TAG, "print()");
        if (null == printerItems) {
//            initializeDefaltRecp();
        }
        printerEx = new CanvasReceiptEx(receiptCount);
        if( null != line_space ){
            printerEx.set_line_space( line_space );
        }
        try {
            for (ReceiptRow printerItem : printerItems
            ) {
                switch (printerItem.type) {
                    case IMAGE_STORAGE:
                    case IMAGE_ASSETS:

                        Bitmap bitmap = convertBitmap(printerItem.type, printerItem.title);
                        if (null != bitmap) {
                            printerEx.addImage(printerItem.title.style, bitmap);
                        }

                        if (printerItem.value.sValue != null) {
                            if (printerItem.value.sValue.length() > 0) {
                                int w = 0;
                                int h = 0;
                                if (null != bitmap) {
                                    w = bitmap.getWidth();
                                    h = bitmap.getHeight();
                                }
                                bitmap = convertBitmap(printerItem.type, printerItem.value);
                                if (null != bitmap) {

                                    if ((printerItem.title.style & ReceiptItemDefine.PStyle_align_left) != 0
                                            && (printerItem.value.style & ReceiptItemDefine.PStyle_align_right) != 0) {
                                        // try print 2 logo in one line
                                        if ((w + bitmap.getWidth()) < CanvasReceiptEx.MAX_WIDTH) {
                                            // print 2 logo in one line
                                            printerEx.feedPixel(null, 0 - h - 2);
                                        } else {

                                        }
                                    }
                                    printerEx.addImage(printerItem.value.style, bitmap);
                                }
                            }
                        }
                        break;
                    case STRING:
                        boolean feedLine = true;
                        if (printerItem.title.sValue.length() > 0) {
                            if (printerItem.isForceMultiLines) {
                                feedLine = true;
                            }
                            else if( 0 != (printerItem.title.style & ReceiptItemDefine.PStyle_align_append) ) {
                                feedLine = false;
                            } else if (null != printerItem.value.sValue) {
                                if (printerItem.value.sValue.length() > 0) {
                                    feedLine = false;
                                }
                            }

                            if( 0 == ( printerItem.title.style & ReceiptItemDefine.PStyle_align_set ) )
                            {
                                // no alignment set, set to left
                                printerItem.title.style |= ReceiptItemDefine.PStyle_align_left;
                                Log.d(TAG, "set alignment to left");
                            }
                            printerEx.addText(printerItem.title.style, printerItem.title.sValue, printerItem.printerMode, printerItem.title.fontFile, feedLine, printerItem.title.color);
                        }
                        if (null != printerItem.value.sValue) {
                            if (printerItem.value.sValue.length() > 0) {
                                printerItem.value.style |= ReceiptItemDefine.PStyle_align_right;    // set default to right, the workflow will check left, center first. So, no infect if be set to left or center
                                if( 0 != (printerItem.value.style & ReceiptItemDefine.PStyle_align_append) ) {
                                    feedLine = false;
                                } else {
                                    feedLine = true;
                                }
                                    printerEx.addText(printerItem.value.style, printerItem.value.sValue, printerItem.printerMode, printerItem.value.fontFile, feedLine, printerItem.value.color);
                            }
                        }
                        break;
                    case LINE:
                        printerEx.addLine((printerItem.title.style & 0xFFF));
                        break;
                    case FEED:
                        printerEx.feedPixel(null, (printerItem.title.style & 0xFF));
                        break;
                    case QRCODE:
                        boolean needScrollBack = false;
                        if (printerItem.title.sValue.length() > 0 && printerItem.value.sValue.length() > 0) {
                            // there're 2 QR code to be print, using left & right alignment
                            printerItem.title.style = ((printerItem.title.style & ReceiptItemDefine.PStyle_align_remove) | ReceiptItemDefine.PStyle_align_left);
                            if ((printerItem.title.style & ReceiptItemDefine.PStyle_size) > 180) {
                                printerItem.title.style &= ReceiptItemDefine.PStyle_size_set_0;
                                printerItem.title.style |= 180;
                            }

                            printerItem.value.style = ((printerItem.value.style & ReceiptItemDefine.PStyle_align_remove) | ReceiptItemDefine.PStyle_align_right);
                            if ((printerItem.value.style & ReceiptItemDefine.PStyle_size) > 180) {
                                printerItem.value.style &= ReceiptItemDefine.PStyle_size_set_0;
                                printerItem.value.style |= 180;
                            }

                            needScrollBack = true;
                        }
                        ReceiptItem qrcode = printerItem.title;
                        if (qrcode.sValue.length() > 0) {
                            printerEx.addQrCode(qrcode.style, qrcode.sValue);
                        }

                        qrcode = printerItem.value;
                        if (qrcode.sValue.length() > 0) {
                            if (needScrollBack) {
                                printerEx.scrollBack();
                            }
                            printerEx.addQrCode(qrcode.style, qrcode.sValue);
                        }

                        break;
                    case BARCODE:
                        printerEx.addBarCode(printerItem.value.style, printerItem.value.sValue);
                        if (printerItem.title.sValue.length() > 0) {
                            addString(printerItem.title);
                        }
                        break;
                    case IMAGE_BCD:
                        // title
                        addString(printerItem.title);
                        // image
                        if (null != printerItem.value.sValue) {
                            Log.d(TAG, "add IMAGE_BCD, size" + printerItem.value.sValue.length());
                            if (printerItem.value.sValue.length() > 0) {
                                printerEx.addImage(printerItem.value.style, (Utility.hexStr2Byte(printerItem.value.sValue)));
                            }
                        }
                        break;
                    case IMAGE_RES:
                        // title
                        addString(printerItem.title);

                        // image
                        if (0 != printerItem.value.iValue) {
                            if( null == printerItem.value.bitmap ){
                                printerItem.value.bitmap = getResBitmap( printerItem.value.iValue );
                            }
                            printerEx.addImage(printerItem.value.style, printerItem.value.bitmap );

                            if( ReceiptRowsDef.LOGO_CACHE.value.sValue.length() == 0  ){
//                                int bytesCnt = bitmap1.getByteCount();
//                                ByteBuffer byteBuffer = ByteBuffer.allocate( bytesCnt );
//                                bitmap1.copyPixelsFromBuffer( byteBuffer );
//                                ReceiptRowsDef.LOGO_CACHE.value.sValue = StringUtil.bcd2Asc( byteBuffer.array() );

//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                                ReceiptRowsDef.LOGO_CACHE.value.sValue = StringUtil.bcd2Asc( baos.toByteArray() );
//                                Log.d(TAG, "Cache logo, size:" + baos.toByteArray().length );
                            }
                        }
                        break;
                }

            }

            if( savePath.length() > 0 ){
                saveReceipts( savePath, "" );
            }

            return printerEx.getData(0);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception :" + e.getMessage());
            for (StackTraceElement m : e.getStackTrace()
            ) {
                Log.e(TAG, "Exception :" + m);

            }

        }
        return new byte[0];
    }

    public void addString(ReceiptItem printerElement) {
        if (printerElement.sValue.length() > 0) {
            try {
                printerEx.addText(printerElement.style, printerElement.sValue, 3, "", true, Color.BLACK);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmap(int index) {
        return printerEx.getBitmap(index);
    }
    public Bitmap[] getBitmap() {
        Bitmap receipts[] = new Bitmap[printerEx.max_receipt_count -1];
        for (int i = 0; i < printerEx.max_receipt_count -1; i++) {
            receipts[i] = printerEx.getBitmap(i+1);
        }
        return receipts;
    }

    public String saveReceipts(String path, String comment) {
        return printerEx.save(path, comment);
    }
    String savePath = "";
    public void setAutoSave(String path) {
        savePath = path;
    }

    public void setLine_space(int line_space[]) {
        this.line_space = line_space;
    }

}
