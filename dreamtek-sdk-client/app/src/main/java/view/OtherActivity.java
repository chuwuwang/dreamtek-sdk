package view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.verifone.activity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherActivity extends AppCompatActivity {

    @BindView(R.id.signalDbm)
    Button signalDbmBt;
    @BindView(R.id.sqlite)
    Button sqliteBt;
    @BindView(R.id.esignature)
    Button esign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        signalDbmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSignalDbm();
            }
        });
        sqliteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sqIntent = new Intent();
                sqIntent.setClass(OtherActivity.this, SqliteActivity.class);
                startActivity(sqIntent);
            }
        });
        esign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sqIntent = new Intent();
                sqIntent.setClass(OtherActivity.this, SignatureActivity.class);
                startActivity(sqIntent);
            }
        });
    }

    private int signalDbm = 0;
    private int signalAsuLevel = 0;

    private void checkSignalDbm() {
        final TelephonyManager telmanager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener listen = new PhoneStateListener() {
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                signalDbm = getSignalStrengthByName(signalStrength, "getDbm");

                signalAsuLevel = getSignalStrengthByName(signalStrength, "getAsuLevel");
                Log.d("TAG", "signalDbm= " + signalDbm + " signalAsuLevel= " + signalAsuLevel + " getCdmaLevel= " + getSignalStrengthByName(signalStrength, "getCdmaLevel")
                        + " getEvdoLevel= " + getSignalStrengthByName(signalStrength, "getEvdoLevel") + " getGsmLevel= " + getSignalStrengthByName(signalStrength, "getGsmLevel")
                        + " getLteLevel= " + getSignalStrengthByName(signalStrength, "getLteLevel") + " getLevel= " + getSignalStrengthByName(signalStrength, "getLevel"));

            }
        };

        telmanager.listen(listen, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telmanager.listen(listen, PhoneStateListener.LISTEN_NONE);
    }

    private int getSignalStrengthByName(SignalStrength signalStrength, String methodName) {
        try {
            Class classFromName = Class.forName(SignalStrength.class.getName());
            java.lang.reflect.Method method = classFromName.getDeclaredMethod(methodName);
            Object object = method.invoke(signalStrength);
            return (int) object;
        } catch (Exception ex) {
            return 0;
        }
    }
}