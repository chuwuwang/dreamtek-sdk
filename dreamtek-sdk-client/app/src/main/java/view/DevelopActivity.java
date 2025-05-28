package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.socsi.k21gpio.K21GpioController;
import com.verifone.activity.R;
import com.verifone.exception.KeyException;
import com.verifone.exception.SDKException;
import com.verifone.smartpos.api.SdkApiHolder;

public class DevelopActivity extends AppCompatActivity implements View.OnClickListener {
    private Button pinBtn;
    private Button pinKBtn;
    private Button pinForkatKey;
    private Button cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        pinBtn = (Button) findViewById(R.id.custom_pin);
        pinBtn.setOnClickListener(this);
        pinForkatKey = (Button) findViewById(R.id.format_key);
        pinForkatKey.setOnClickListener(this);
        cameraBtn = (Button) findViewById(R.id.custom_camera);
        cameraBtn.setOnClickListener(this);
        pinKBtn = (Button) findViewById(R.id.custom_pin_forKorea);
        pinKBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.custom_pin:
                intent = new Intent(this, CustomPinActivity.class);
                startActivity(intent);
                break;
            case R.id.custom_pin_forKorea:
                intent = new Intent(this, CustomKoreaPinActivity.class);
                startActivity(intent);
                break;
            case R.id.format_key:
                try {
                    // 程序启动首先调用指令服务初始化
                    boolean commandInit = SdkApiHolder.getInstance().getDeviceMaster().init(this.getApplicationContext());
                    Log.e("TAG", "command init=" + commandInit);
                    new K21GpioController().K21PowerOn();
                    boolean res = SdkApiHolder.getInstance().getKeyManager().keyRecoveryFactory();
                    Toast.makeText(this, "format key result:" + res, Toast.LENGTH_SHORT).show();
                } catch (KeyException | SDKException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.custom_camera:
                Intent intentCamera = new Intent(this, CustomCameraActivity.class);
                startActivity(intentCamera);
                break;
        }
    }

}
