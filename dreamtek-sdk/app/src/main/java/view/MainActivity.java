package view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.verifone.activity.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Utils.InstallReceiverManager;
import Utils.ServiceManager;
import testtools.MessageService;

/**
 * Primary (first-level) UI screen.
 */
public class MainActivity extends AppCompatActivity {
    private static String TAG = "TestClient";

    private static int pid = 0;

    Button testBt, devpBt, newTestBt, simpleEmvDemoBt;
    TextView deviceServiceInfoTv, systemServiceInfoTv;

    private final ServiceManager services = ServiceManager.getInstance();
    private boolean destroyed;
    private InstallReceiverManager installReceiver;
    private boolean deviceServiceConnected;

    public static String byte2HexStr(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            String stmp = "";
            StringBuilder sb = new StringBuilder("");
            for (int n = 0; n < data.length; n++) {
                stmp = Integer.toHexString(data[n] & 0xFF);
                sb.append(stmp.length() == 1 ? "0" + stmp : stmp);
            }
            return sb.toString().toUpperCase().trim();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testBt = (Button) findViewById(R.id.test_bt);
        devpBt = (Button) findViewById(R.id.devp_bt);
        newTestBt = (Button) findViewById(R.id.new_test_bt);
        simpleEmvDemoBt = (Button) findViewById(R.id.simple_emv_demo_bt);
        deviceServiceInfoTv = (TextView) findViewById(R.id.device_service_info);
        systemServiceInfoTv = (TextView) findViewById(R.id.system_service_info);

        startLogcat();

        installReceiver = new InstallReceiverManager(this, new InstallReceiverManager.InstallCallback() {
            @Override
            public void installSuccessCallback(String packageName) {

                if (ServiceManager.PACKAGE_NAME.equals(packageName)) {
                    bindDeviceService();
                    Log.i(TAG, "Install Success, PackageName=" + packageName);
                }

                updateServiceInfo();
            }
        });

        installReceiver.registerInstallReceiver();

        services.connect(this, serviceListener);
        updateServiceInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            startService(new Intent(this, MessageService.class));
        } catch (IllegalStateException error) {
            // A screen-off launch can still be background-restricted. Retry on the next resume.
            Log.w(TAG, "Legacy message listener deferred until the app is foreground", error);
        }
        updateServiceInfo();
    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        installReceiver.unregisterInstallReceiver();
        services.disconnect(serviceListener);
        super.onDestroy();
    }

    private void bindDeviceService() {
        if (!destroyed) services.reconnect();
    }

    public void getConnected(View view) {
        bindDeviceService();
    }

    public void testIn(View v) {
        startActivity(new Intent(this, TestActivity.class));
    }

    public void devpIn(View v) {
        startActivity(new Intent(this, DevelopActivity.class));
    }

    public void otherIn(View v) {
        startActivity(new Intent(this, OtherActivity.class));
    }

    public void newTestIn(View v) {
        startActivity(new Intent(this, NewTestActivity.class));
    }

    public void simpleEmvDemoIn(View v) {
        startActivity(new Intent(this, com.dreamtek.demo_emv.MainActivity.class));
    }

    private final ServiceManager.Listener serviceListener = new ServiceManager.Listener() {
        @Override public void onConnectionFailed(String message) {
            onDisconnected();
        }

        @Override public void onSystemStateChanged(String state) {
            updateServiceInfo();
        }

        @Override
        public void onConnected(IDeviceService deviceService) {
            if (destroyed) return;
            newTestBt.setEnabled(true);
            testBt.setEnabled(true);
            devpBt.setEnabled(true);
            simpleEmvDemoBt.setEnabled(true);
            Toast.makeText(MainActivity.this, getString(R.string.toast_connected), Toast.LENGTH_SHORT).show();

            deviceServiceConnected = true;
            updateServiceInfo();
        }

        @Override
        public void onDisconnected() {
            Toast.makeText(MainActivity.this, getString(R.string.toast_disconnected), Toast.LENGTH_SHORT).show();
            devpBt.setEnabled(false);
            testBt.setEnabled(false);
            newTestBt.setEnabled(false);
            simpleEmvDemoBt.setEnabled(false);
            deviceServiceConnected = false;
            updateServiceInfo();

        }
    };

    public void systemServiceDemoIn(View view) {
        startActivity(new Intent(this, sysdemo.SystemServiceDemoActivity.class));
    }

    public void systemServiceTestIn(View view) {
        startActivity(new Intent(this, sysdemo.SystemServiceTestActivity.class));
    }

    public void systemServiceClientIn(View view) {
        // Ported DTKSystemServiceTestClient UI; its pages obtain the system interfaces
        // through the provider registered in MyApplication (unified device-service AAR).
        startActivity(new Intent(this, com.dreamtek.systemservice.test.demo.BottomNavActivity.class));
    }

    private void startLogcat() {

        if (pid == Binder.getCallingPid()) {
            return;
        }
        pid = Binder.getCallingPid();

        String logPath = "/sdcard/";

        String logFileName = logPath + "testclient.log";

        String command = "logcat -v threadtime -f " + logFileName + " -r 1000";
        Log.i(TAG, "logging: " + command);
        execCommand(command, false);
    }

    private void execCommand(String cmd, boolean isNeedReadProcess) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            if (isNeedReadProcess) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                StringBuilder log = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    log.append(line);
                }
            }

        } catch (IOException e) {
        }
    }

    private void updateServiceInfo() {
        if (destroyed) return;
        deviceServiceInfoTv.setText(buildServiceInfo(
                R.string.device_service_name,
                ServiceManager.PACKAGE_NAME,
                deviceServiceConnected));
        systemServiceInfoTv.setText("Embedded System: " + services.getSystemState());

    }

    private String buildServiceInfo(int serviceNameRes, String packageName, boolean connected) {
        String status = getString(connected
                ? R.string.service_status_connected
                : R.string.service_status_disconnected);
        String version = getString(R.string.service_value_unknown);
        String lastUpdate = getString(R.string.service_value_unknown);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo.versionName != null && !packageInfo.versionName.isEmpty()) {
                version = packageInfo.versionName;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm", Locale.getDefault());
            lastUpdate = dateFormat.format(new Date(packageInfo.lastUpdateTime));
        } catch (PackageManager.NameNotFoundException e) {
            status = getString(R.string.service_status_not_installed);
            Log.w(TAG, "Service package not installed: " + packageName);
        }

        return getString(
                R.string.service_info_format,
                getString(serviceNameRes),
                status,
                version,
                lastUpdate);
    }

}
