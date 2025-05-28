package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.verifone.activity.R;

import Utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 第二级界面
 */
public class TestActivity extends AppCompatActivity {
    @BindView(R.id.service_bt)
    Button serviceBt;
    @BindView(R.id.magcard_bt)
    Button magcardBt;
    @BindView(R.id.touchic_bt)
    Button touchicBt;
    @BindView(R.id.notouchIc_bt)
    Button notouchIcBt;
    @BindView(R.id.beer_bt)
    Button beerBt;
    @BindView(R.id.led_bt)
    Button ledBt;
    @BindView(R.id.scan_bt)
    Button scanBt;
    @BindView(R.id.pint_bt)
    Button pintBt;
    @BindView(R.id.port_bt)
    Button serialPortBt;
    @BindView(R.id.pinpad_bt)
    Button pinpadBt;
    @BindView(R.id.pboc_bt)
    Button pbocBt;
    @BindView(R.id.serviceinfo_bt)
    Button serviceInfoBt;
    @BindView(R.id.usbport_bt)
    Button usbPortBt;
    @BindView(R.id.ex_port_bt)
    Button externalSrialPost;
    @BindView(R.id.x990Pinpad_bt)
    Button x990PinpadBt;
    @BindView(R.id.emv_bt)
    Button emvBt;
    @BindView(R.id.dukpt_bt)
    Button dukptBt;
    @BindView(R.id.mksk_bt)
    Button mkskBt;
    @BindView(R.id.ultralight_bt)
    Button ultralightbt;
    @BindView(R.id.ultralightC_bt)
    Button ultralightCbt;
    @BindView(R.id.ultralightEV1_bt)
    Button ultralightEV1bt;
    @BindView(R.id.sde_bt)
    Button sdebt;
    @BindView(R.id.epp_bt)
    Button eppBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();
    }

    //设置进入第三级界面的监听器
    private void initListener() {
        usbPortBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.usbPortBt);
            }
        });

        serviceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.serviceBt);
            }
        });
        magcardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.magcardBt);
            }
        });
        touchicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.touchicBt);
            }
        });
        notouchIcBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.notouchIcBt);
            }
        });
        beerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.beerBt);
            }
        });
        ledBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.ledBt);
            }
        });
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.scanBt);
            }
        });
        pintBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.pintBt);
            }
        });
        serialPortBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.serialPortBt);
            }
        });
        pinpadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.pinpadBt);
            }
        });
        pbocBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.pbocBt);
            }
        });
        serviceInfoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.serviceInfoBt);
            }
        });
        externalSrialPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.externalSerialPortBt);
            }
        });
        x990PinpadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterThirdActivity(Constants.x990PinpadBt);
            }
        });
        emvBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.emvBt);
            }
        });

        sdebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterThirdActivity(Constants.sdeBt);
            }
        });
        dukptBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.dukptBt);
            }
        });

        mkskBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.MKSK);
            }
        });

        ultralightbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.ultralightCardBt);
            }

        });
        ultralightCbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.ultralightCardCBt);
            }

        });
        ultralightEV1bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.ultralightCardEV1Bt);
            }

        });
        eppBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterThirdActivity(Constants.eppBt);
            }
        });
    }

    public void enterThirdActivity(String moudleName) {
        Log.d("TAG", "moudleName:" + moudleName);
        Intent intent = new Intent(TestActivity.this, ThirdActivity.class);
        intent.putExtra("name", moudleName);
        startActivity(intent);
    }
}