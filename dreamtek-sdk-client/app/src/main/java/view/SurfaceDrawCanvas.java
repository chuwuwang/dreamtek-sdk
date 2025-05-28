package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SurfaceDrawCanvas extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint, paintText;
    private Canvas canvas;
    private Bitmap bitmap;
    // private Bitmap originBitmap;
    private int mov_x;
    private int mov_y;
    private String signaValue;
    final static int BUFFER_SIZE = 10000;
    public byte[] top;
    private int width, height;
    SurfaceHolder holder;
    // private Rect textRect;
    private int textW = 0;
    private int textH = 0;

    public SurfaceDrawCanvas(Context context, String signValue,
                             int screenWidth, int width, int height) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);
        String numEnd = "";

        for (int i = 0; i < signValue.length(); i = i + 2) {
            String numStart = null;
            numStart = signValue.substring(i, i + 2);
            numEnd = numEnd + " " + numStart;
        }

        this.signaValue = numEnd;
        this.width = width;
        this.height = height;
        Log.d("", "screenWidth:" + screenWidth);
        Log.d("", "width:" + width);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        // paint.setStyle(Style.FILL);
        paint.setStrokeWidth(4);
        // paint.setFakeBoldText(true);
        paint.setLinearText(true);
        // paint.setSubpixelText(true);
        // paint.setFilterBitmap(true);
        paint.setStrokeCap(Cap.ROUND);
        paint.setDither(true);
        paint.setAntiAlias(true);

        paintText = new Paint();
        String familyName = "Times New Roman";
        Typeface font = Typeface.create(familyName, Typeface.NORMAL);
        paintText.setTypeface(font);
        paintText.setTextSize(3);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.BLACK);
        // getResources().getColor(R.color.gray)
        paintText.setStyle(Style.FILL);
        // paintText.setStrokeWidth(4* zoomMultiples);

        bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        Log.d("Surface", "w:" + bitmap.getWidth());
        Log.d("Surface", "h:" + bitmap.getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new MyThread().start();
        threadFlag = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadFlag = false;
    }

    boolean threadFlag = false;

    class MyThread extends Thread {
        @Override
        public void run() {
            while (threadFlag) {
                long startTime = System.currentTimeMillis();

                myDraw(canvas);
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 30) {
                    try {
                        Thread.sleep(30 - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void resetCanvas() {
        isOnTouch = false;
        isCoverTrait = false;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        canvas.setBitmap(bitmap);
    }

    public Bitmap saveCanvas() {
        if (isOnTouch) {
            saveCroppedImage(bitmap);
            Bitmap newbmp = compress(bitmap);
//            bitmap  = BitmapFactory.decodeFile("/sdcard/Pictures/temp_cropped.jpg");
            return newbmp;
        } else {
            return null;
        }

    }

    private void saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/Pictures");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/Pictures/temp.jpg".trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
        String newFilePath = "/sdcard/Pictures" + "/" + mName + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void saveBmpToFile(byte[] result) {
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/UPSignature.bmp";
        Log.d("", path);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(result);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * �ȱ�ѹ��ͼƬ
     */
    private Bitmap compress(Bitmap bitmap) {
        Matrix matrix = new Matrix();

        matrix.postScale((float) 384 / bitmap.getWidth(), (float) 500 / bitmap.getHeight());
        // matrix.postRotate(180);
        Bitmap compressBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return compressBitmap;
    }

    protected void myDraw(Canvas canvas) {
        canvas = holder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(bitmap, 0, 0, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    private boolean isOnTouch = false;
    private boolean isCoverTrait = false;
    private int down_x = 0;
    private int down_y = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // canvas.drawPoint(mov_x, mov_y, paint);
            if (!isOnTouch) {
                down_x = (int) event.getX();
                down_y = (int) event.getY();
            }
            // Log.d("", "dx:" + down_x + " dy:" + down_y);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            canvas.drawLine(mov_x, mov_y, event.getX(), event.getY(), paint);
            if (!isOnTouch && (Math.abs(down_x - mov_x) > 100 || Math.abs(down_y - mov_y) > 50)) {
                isOnTouch = true;
            }
            // Log.d("", "ml:" + textRect.left + " mr:" + textRect.right +
            // " mt:"
            // + textRect.top + " mb:" + textRect.bottom + "isCoverTrait:"
            // + isCoverTrait);
            // if (!isCoverTrait && textRect.contains(mov_x, mov_y)) {
            //    isCoverTrait = true;
            // }
            // Log.d("", "mx:" + mov_x + " my:" + mov_y + "isOnTouch:" +
            // isOnTouch);

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mov_x = (int) event.getX();
            mov_y = (int) event.getY();
        }
        mov_x = (int) event.getX();
        mov_y = (int) event.getY();
        return true;
    }
}
