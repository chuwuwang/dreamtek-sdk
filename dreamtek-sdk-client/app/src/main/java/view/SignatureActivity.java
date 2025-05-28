package view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.verifone.activity.R;

public class SignatureActivity extends Activity {

    private SurfaceDrawCanvas canvas;
    private String num;
    private Runnable task;
    private Handler handler;
    private int finishTime = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signature);

        Intent intent = this.getIntent();

        num = intent.getStringExtra("num");
        if (num == null || num.trim().equals(""))
            num = "12345678";

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                finishActivity();
                super.handleMessage(msg);
            }
        };

        task = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        };
        handler.postDelayed(task, finishTime);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        Display display = this.getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        Log.d("", "sw" + screenWidth + " " + "sh" + display.getHeight());

        int height = display.getHeight() - dip2px(SignatureActivity.this, 70);

        Log.d("", "w " + height * 3 + " " + "h " + height);

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
        canvas = new SurfaceDrawCanvas(SignatureActivity.this, num, 384, 768, 1000);

        LinearLayout.LayoutParams canvasLayout = new LinearLayout.LayoutParams(768, 1000);
        canvas.setLayoutParams(canvasLayout);
        layout.addView(canvas);
        Log.d("", "lw" + layout.getWidth() + " " + "lh" + layout.getHeight());
        canvas.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handler.removeCallbacks(task);
                handler.postDelayed(task, finishTime);
                return false;
            }
        });

        // 确认
        Button confirm = (Button) this.findViewById(R.id.confirm);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Bitmap result = canvas.saveCanvas();


                // result为空即未签名不符合规范
                if (result == null) {
                    Toast.makeText(SignatureActivity.this, "签名不符合规范", Toast.LENGTH_SHORT);
                    return;
                }
                Log.d("", "w:" + result.getWidth());
                Log.d("", "h:" + result.getHeight());
                finishActivity();
            }
        });

        // 取消
        Button reset = (Button) this.findViewById(R.id.reset);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.resetCanvas();
            }
        });
    }

    private void finishActivity() {
        setResult(RESULT_CANCELED, null);
        SignatureActivity.this.finish();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.e("dip2px", (int) (dipValue * scale + 0.5f) + "");
        return (int) (dipValue * scale + 0.5f);
    }
}
