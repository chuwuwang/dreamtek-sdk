package Utils.canvasReceipt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// import static com.verifone.demo.emv.VFIApplication.getInstance;

/**
 * Created by Simon on 2019/2/1.
 */

/**
 * Class {@code PrinterCanvas} is for calling PrinterEx to draw PrinterItem on canvas
 * and the canvas can be set to printer or activity
 *
 *  2020/7/31, Simon
 *  #1, Renname the setPrinterItems() to append
 */
public class CanvasReceiptContext extends CanvasReceipt {

    private final String TAG = this.getClass().getSimpleName();

    protected static Context context;

    public CanvasReceiptContext(Context context) {
        this.context = context;
    }

    public void append(List<ReceiptRowsDef> printerItems) {
        if( null != printerItems ){
            if( printerItems.size() > 0 ){
                append(printerItems, printerItems.get(0));
            }
        }
    }


    @Override
    protected Bitmap getAssetBitmap(String path ){
        if( null == context ){
            return null;
        }
        try {
            InputStream is = context.getAssets().open( path );
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Bitmap getResBitmap(int id ) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getInstance().getResources(), id );
//        return bitmap;
        return null;
    }

}
