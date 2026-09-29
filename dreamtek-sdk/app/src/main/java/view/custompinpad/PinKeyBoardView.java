package view.custompinpad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.verifone.activity.R;

import java.util.ArrayList;

import Utils.LogUtil;
import base.MyApplication;

public class PinKeyBoardView extends View {
    private final String         TAG  = "ScbPinKeyBoard";
    private       int            contentsize;
    private       int[]          coordinateInt;
    private       DisplayMetrics dm;
    private       float          height;
    private       float          keyBoardheight;
    private       int[]          nums;
    private final Point          p    = new Point(540, 880);
    private       Paint          paint;
    private       Path           path;
    private       float          width;

    public PinKeyBoardView(Context paramContext) {
        super(paramContext);
        getScreenResolution(paramContext);
        init();
    }

    public PinKeyBoardView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        getScreenResolution(paramContext);
        init();
    }

    private void drawDeleteIconCenter(Canvas canvas, float centerX, float centerY) {
        Context context = getContext();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_delete);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float scale = ((float) this.contentsize) / bitmapWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        LogUtil.d(TAG, "contentsize=" + contentsize + " Scale=" + scale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        if (bitmap != null) {
            float leftTopX = centerX - bitmapWidth / 2;
            float leftTopY = centerY - bitmapHeight / 2;

            canvas.drawBitmap(bitmap, leftTopX, leftTopY, paint);
        }
    }

    private void drawStringCenter(Canvas canvas, float centerX, float centerY, String paramString) {
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeWidth(4.0F);
        Paint.FontMetrics localFontMetrics = this.paint.getFontMetrics();
        int i = (int) this.paint.measureText(paramString);
        int j = (int) Math.ceil(localFontMetrics.descent - localFontMetrics.ascent);
        int k = (int) localFontMetrics.descent;
        canvas.drawText(paramString, centerX - i / 2, centerY - k + j / 2, this.paint);
    }

    private void drawDelete(Canvas canvas, float deleteCenterX, float deleteCenterY, float radius) {
        float[] start = {deleteCenterX - radius - 5, deleteCenterY};
        float leftPointOffset = 23.0F;
        paint.setColor(getResources().getColor(R.color.color_black));
        paint.setStrokeWidth(5.0F);
        canvas.drawLine(start[0], start[1],
                start[0] + leftPointOffset,start[1] - leftPointOffset,
                paint);
        canvas.drawLine(start[0], start[1],
                start[0]  + leftPointOffset, start[1]  + leftPointOffset,
                paint);

        canvas.drawLine(start[0], start[1],
                deleteCenterX + 25.0F, deleteCenterY,
                paint);

//        float leftPointOffset = radius / 3.0F * 2.0F;
//        paint.setColor(ContextCompat.getColor(getContext(),R.color.color_brown));
//        this.path.reset();
//        this.path.moveTo(deleteCenterX - radius - leftPointOffset, deleteCenterY);
//        this.path.lineTo(deleteCenterX - radius, deleteCenterY - radius);
//        this.path.lineTo(deleteCenterX + radius, deleteCenterY - radius);
//        this.path.lineTo(deleteCenterX + radius, deleteCenterY + radius);
//        this.path.lineTo(deleteCenterX - radius, deleteCenterY + radius);
//        this.path.close();
//        canvas.drawPath(this.path, this.paint);
//
//        float cancelRadius = radius / 2.0F;
//        this.paint.setColor(Color.WHITE);
//        this.paint.setStrokeWidth(4.0F);
//        canvas.drawLine(deleteCenterX - cancelRadius, deleteCenterY - cancelRadius, deleteCenterX + cancelRadius, deleteCenterY + cancelRadius, this.paint);
//        canvas.drawLine(deleteCenterX - cancelRadius, deleteCenterY + cancelRadius, deleteCenterX + cancelRadius, deleteCenterY - cancelRadius, this.paint);
    }

    private void getScreenResolution(Context paramContext) {
        WindowManager windowManager = (WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE);
        this.dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(this.dm);
        Log.i("N900PinKeyBoard", "hright=" + this.dm.heightPixels + ";width" + this.dm.widthPixels);
    }

    private void init() {
        this.path = new Path();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.nums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    }

    public ArrayList<PinKeyCoordinate> getCoordinateList() {
        ArrayList<PinKeyCoordinate> pinKeyCoordinates = new ArrayList<>();

        int[] localObject = new int[2];
        getLocationOnScreen(localObject);
        int keyboardLeftTopX = localObject[0];
        int keyboardLeftTopY = localObject[1];
        LogUtil.d("TAG", "keyboardLeftTopX=" + keyboardLeftTopX + " keyboardLeftTopY=" + keyboardLeftTopY);

        int x0 = keyboardLeftTopX;
        int x1 = (int) (keyboardLeftTopX + this.width / 4.0F);
        int x2 = (int) (keyboardLeftTopX + this.width / 4.0F * 2.0F);
        int x3 = (int) (keyboardLeftTopX + this.width / 4.0F * 3.0F);
        int x4 = (int) (keyboardLeftTopX + this.width);

        int y0 = keyboardLeftTopY;
        int y1 = (int) (keyboardLeftTopY + keyBoardheight / 4.0F);
        int y2 = (int) (keyboardLeftTopY + keyBoardheight / 4.0F * 2.0F);
        int y3 = (int) (keyboardLeftTopY + keyBoardheight / 4.0F * 3.0F);
        int y4 = (int) (keyboardLeftTopY + keyBoardheight);

        LogUtil.d("TAG, x0=" + x0 + " x1=" + x1 + " x2=" + x2 + " x3=" + x3);
        LogUtil.d("TAG, y0=" + y0 + " y1=" + y1 + " y2=" + y2 + " y3=" + x3 + " y4=" + y4);

        int TYPE_NUM = PinKeyCoordinate.KEY_TYPE_NUM;
        int TYPE_CONFIRM = PinKeyCoordinate.KEY_TYPE_CONFIRM;
        int TYPE_CANCEL = PinKeyCoordinate.KEY_TYPE_CANCEL;
        int TYPE_DELETE = PinKeyCoordinate.KEY_TYPE_DELETE;

        int confirmRightBottmY = (int) (this.keyBoardheight) + keyboardLeftTopY;



        pinKeyCoordinates.add(new PinKeyCoordinate("btn_0", x0, y0, x1, y1, TYPE_NUM));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_1", x1, y0, x2, y1, TYPE_NUM));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_2", x2, y0, x3, y1, TYPE_NUM));

        pinKeyCoordinates.add(new PinKeyCoordinate("btn_3", x0, y1, x1, y2, TYPE_NUM));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_4", x1, y1, x2, y2, TYPE_NUM));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_5", x2, y1, x3, y2, TYPE_NUM));

        pinKeyCoordinates.add(new PinKeyCoordinate("btn_6", x0, y2, x1, y3, TYPE_NUM));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_7", x1, y2, x2, y3, TYPE_NUM));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_8", x2, y2, x3, y3, TYPE_NUM));

        pinKeyCoordinates.add(new PinKeyCoordinate("btn_9", x1, y3, x2, y4, TYPE_NUM));

        pinKeyCoordinates.add(new PinKeyCoordinate("btn_10", x3, y0, x4, y1, TYPE_DELETE));
        pinKeyCoordinates.add(new PinKeyCoordinate("btn_11", x3, y1, x4, y4, TYPE_CONFIRM));

        pinKeyCoordinates.add(new PinKeyCoordinate("btn_12", x0, y3, x1, y4, TYPE_CANCEL));

        return pinKeyCoordinates;
    }

    private void drawTable(Canvas canvas){
        Paint paint1 = new Paint();
        Paint paint2 = new Paint();
        paint1.setStrokeWidth(2.0f);
        paint1.setColor(Color.parseColor("#E1E1E1"));
        paint2.setStrokeWidth(2.0f);
        paint2.setColor(Color.parseColor("#5C4D4D"));

        canvas.drawLine(0, 0, width / 4.0f * 3.0f, 0, paint1);
        canvas.drawLine(0, keyBoardheight / 4.0f , width / 4.0f * 3.0f, keyBoardheight / 4.0f, paint1);
        canvas.drawLine(0, keyBoardheight / 4.0f * 2.0f, width / 4.0f * 3.0f, keyBoardheight / 4.0f * 2.0f, paint1);
        canvas.drawLine(0, keyBoardheight / 4.0f * 3.0f, width / 4.0f * 3.0f, keyBoardheight / 4.0f * 3.0f, paint1);
        canvas.drawLine(width / 4.0f * 3.0f, 0, width, 0, paint2);

        canvas.drawLine(width / 4.0f, 0, width / 4.0f, keyBoardheight, paint1);
        canvas.drawLine(width / 2.0f, 0, width / 2.0f, keyBoardheight, paint1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xffffffff - 657926);
        drawTable(canvas);
        this.paint.setAlpha(255);
        this.paint.setColor(ContextCompat.getColor(getContext(),R.color.color_brown));


        Rect rect = new Rect((int)width / 4 * 3, (int)0, (int)width, (int)keyBoardheight/4);
        paint .setColor(Color.parseColor("#6F5F5E"));
        canvas.drawRect(rect, paint);

        paint.setColor(Color.parseColor("#FFD400"));
        rect = new Rect((int)(int)width / 4 * 3, (int)keyBoardheight/4, (int)width, (int)keyBoardheight);
        canvas.drawRect(rect, paint);

        float centerX = width / 8 * 7;
        float centerY = keyBoardheight / 8 * 5;
        this.paint.setColor(Color.parseColor("#3A3433"));
        this.paint.setTextSize(32);
        drawStringCenter(canvas, centerX, centerY, MyApplication.getContext().getString(R.string.btn_next));

        this.paint.setTextSize(40);
        drawStringCenter(canvas, this.width / 8.0F, keyBoardheight / 8.0F *7.0F, MyApplication.getContext().getString(R.string.btn_cancel));     //cancel

        this.paint.setTextSize(64);
        drawStringCenter(canvas, this.width / 8.0F, keyBoardheight / 8.0F, this.nums[0] + "");                   //1
        drawStringCenter(canvas, this.width / 8.0F * 3.0F, keyBoardheight / 8.0F, this.nums[1] + "");            //2
        drawStringCenter(canvas, this.width / 8.0F * 5.0F, keyBoardheight / 8.0F, this.nums[2] + "");            //3
        drawStringCenter(canvas, this.width / 8.0F, keyBoardheight / 8.0F * 3.0F, this.nums[3] + "");            //4
        drawStringCenter(canvas, this.width / 8.0F * 3.0F, keyBoardheight / 8.0F * 3.0F, this.nums[4] + "");     //5
        drawStringCenter(canvas, this.width / 8.0F * 5.0F, keyBoardheight / 8.0F * 3.0F, this.nums[5] + "");     //6
        drawStringCenter(canvas, this.width / 8.0F, keyBoardheight / 8.0F * 5.0F, this.nums[6] + "");            //7
        drawStringCenter(canvas, this.width / 8.0F * 3.0F, keyBoardheight / 8.0F * 5.0F, this.nums[7] + "");     //8
        drawStringCenter(canvas, this.width / 8.0F * 5.0F, keyBoardheight / 8.0F * 5.0F, this.nums[8] + "");     //9
        drawStringCenter(canvas, this.width / 8.0F * 3.0F, keyBoardheight / 8.0F * 7.0F, this.nums[9] + "");     //0

        drawDeleteIconCenter(canvas, this.width / 8.0F * 7.0F, keyBoardheight / 8.0F);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = MeasureSpec.getSize(widthMeasureSpec);
//        this.height = (this.width / 14.3F * 13.0F);
        this.height = 1280.0F;
        keyBoardheight = height - 760.0F;


        widthMeasureSpec = 15;
        for (; ; ) {
            if (widthMeasureSpec < 100) {
                this.paint.setTextSize(widthMeasureSpec);
                Paint.FontMetrics localFontMetrics = this.paint.getFontMetrics();
                float f1 = localFontMetrics.descent;
                float f2 = localFontMetrics.ascent;
                float f3 = this.paint.measureText(getContext().getString(R.string.pinpad_confirm));

                if ((f1 - f2 > this.keyBoardheight / 8.0F) || (f3 > this.width / 8.0F)) {
                    this.contentsize = widthMeasureSpec;
                    setMeasuredDimension((int) this.width, (int) this.keyBoardheight);
                    break;
                }
            } else {
                this.contentsize = 46;
                setMeasuredDimension((int) this.width, (int) this.keyBoardheight);
                break;
            }
            widthMeasureSpec += 1;
        }
    }

    public void setRandomNumber(int[] paramArrayOfInt) {
        this.nums = paramArrayOfInt;
        invalidate();
    }
}

