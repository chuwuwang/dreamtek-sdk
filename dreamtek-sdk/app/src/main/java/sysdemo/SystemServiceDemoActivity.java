package sysdemo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.system_service.aidl.IAppInstallObserver;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Utils.ServiceManager;

/**
 * Customer reference: bind once, get a typed module, call its AIDL methods on a worker.
 * The full test catalog is a separate screen; these examples do not use its dispatcher or JSON.
 */
public class SystemServiceDemoActivity extends AppCompatActivity {
    private final ServiceManager services = ServiceManager.getInstance();
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private final List<Button> actions = new ArrayList<>();
    private TextView status;
    private TextView result;
    private EditText apkPath;
    private volatile boolean destroyed;
    private boolean busy;
    private volatile int requestId;

    // Only this listener belongs to this Activity. Other screens share the same service binding.
    private final ServiceManager.Listener listener = new ServiceManager.Listener() {
        @Override public void onConnected(IDeviceService service) { updateState(); }
        @Override public void onDisconnected() { connectionChanged(); }
        @Override public void onConnectionFailed(String message) { updateState(); }
        @Override public void onSystemStateChanged(String state) {
            if (!"READY".equals(state)) connectionChanged();
            else updateState();
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("System Service Demo");
        ScrollView scroll = new ScrollView(this);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(16, 16, 16, 16);
        page.setFocusableInTouchMode(true);
        page.requestFocus();
        scroll.addView(page);
        setContentView(scroll);
        status = text(page, "Connecting…");
        result = text(page, "Select an example. Results appear here.");
        result.setTextIsSelectable(true);

        text(page, "System / ISystemManager");
        action(page, "Read screen brightness", this::readBrightness);
        apkPath = new EditText(this);
        apkPath.setSingleLine(true);
        apkPath.setHint("APK path readable by the service process");
        page.addView(apkPath);
        action(page, "Verify APK signature", this::verifyApk);
        action(page, "Install test APK (callback example)", this::confirmInstall);

        text(page, "Device Info / ISysDeviceInfo");
        action(page, "Read model and serial number", this::readDeviceInfo);
        action(page, "Read device info with Bundle keys", this::readExtendedDeviceInfo);

        text(page, "Network / INetworkManager");
        action(page, "Read network type", this::readNetworkType);
        action(page, "Read current network details", this::readNetworkDetails);

        text(page, "Settings / ISettingsManager");
        action(page, "Read sleep duration", this::readSleepDuration);
        action(page, "Read screen lock state", this::readScreenLock);

        Button fullTests = new Button(this);
        fullTests.setText("Open full system test catalog");
        page.addView(fullTests);
        fullTests.setOnClickListener(view -> startActivity(new Intent(this, SystemServiceTestActivity.class)));

        services.connect(this, listener);
    }

    // Each example obtains its module explicitly. Parameters and AIDL calls stay together.
    private void readBrightness() {
        ISystemManager system = services.getSystemManager();
        call("getScreenBrightness", () -> "Brightness = " + system.getScreenBrightness());
    }

    private void verifyApk() {
        ISystemManager system = services.getSystemManager();
        String path = apkPath.getText().toString().trim();
        if (path.isEmpty()) { result.setText("Enter an APK path first."); return; }
        call("verifySignSync", () -> "Signature verified = " + system.verifySignSync(path));
    }

    private void readDeviceInfo() {
        ISysDeviceInfo deviceInfo = services.getSysDeviceInfo();
        call("Device info", () -> "Model = " + deviceInfo.getModel()
                + "\nSerial number = " + deviceInfo.getSerialNo());
    }

    private void readExtendedDeviceInfo() {
        ISysDeviceInfo deviceInfo = services.getSysDeviceInfo();
        call("getDeviceInfoEx", () -> {
            Bundle request = new Bundle();
            request.putString("SN", "");
            request.putString("romVer", "");
            Bundle response = deviceInfo.getDeviceInfoEx(request);
            return describe(response);
        });
    }

    private void readNetworkType() {
        INetworkManager network = services.getNetworkManager();
        call("getNetworkType", () -> "Network type = " + network.getNetworkType());
    }

    private void readNetworkDetails() {
        INetworkManager network = services.getNetworkManager();
        call("getCurrentNetworkDetails", () -> describe(network.getCurrentNetworkDetails()));
    }

    private void readSleepDuration() {
        ISettingsManager settings = services.getSettingsManager();
        call("getSleepDuration", () -> "Sleep duration = " + settings.getSleepDuration() + " ms");
    }

    private void readScreenLock() {
        ISettingsManager settings = services.getSettingsManager();
        call("isScreenLock", () -> "Screen locked = " + settings.isScreenLock());
    }

    private void confirmInstall() {
        String path = apkPath.getText().toString().trim();
        if (path.isEmpty()) { result.setText("Enter a prepared test APK path first."); return; }
        new AlertDialog.Builder(this).setTitle("Install test APK?")
                .setMessage(path + "\nUse a known test package and signature. Installation may replace an existing app.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Install once", (dialog, which) -> installApk(path)).show();
    }

    // Callback example: the Binder callback only posts a result; no UI work runs on its thread.
    private void installApk(String path) {
        ISystemManager system = services.getSystemManager();
        if (system == null || busy || destroyed) return;
        long connection = services.getGeneration();
        int request = begin("installApp: " + path, connection);
        String installerPackageName = getPackageName();
        worker.execute(() -> {
            if (!isCurrent(request, connection)) return;
            try {
                system.installApp(path, new InstallObserver(this, request, connection), installerPackageName);
                // A void return only means the request returned; wait for the callback.
            } catch (Exception error) {
                failed(request, connection, error);
            }
        });
    }

    private static final class InstallObserver extends IAppInstallObserver.Stub {
        private final WeakReference<SystemServiceDemoActivity> activity;
        private final int request;
        private final long connection;

        InstallObserver(SystemServiceDemoActivity activity, int request, long connection) {
            this.activity = new WeakReference<>(activity);
            this.request = request;
            this.connection = connection;
        }

        @Override public void onInstallFinished(String packageName, int returnCode) {
            SystemServiceDemoActivity page = activity.get();
            if (page != null) page.finish(request, connection, "onInstallFinished\nPackage = "
                    + packageName + "\nReturn code = " + returnCode);
        }
    }

    // Shared thread/UI handling. The examples above contain the customer-facing API calls.
    private void call(String name, Callable<String> operation) {
        if (services.getSystemManager() == null || busy || destroyed) return;
        long connection = services.getGeneration();
        int request = begin(name, connection);
        worker.execute(() -> {
            if (!isCurrent(request, connection)) return;
            try { finish(request, connection, operation.call()); }
            catch (Exception error) { failed(request, connection, error); }
        });
    }

    private int begin(String name, long connection) {
        int request = ++requestId;
        busy = true;
        result.setText(name + "…");
        updateState();
        main.postDelayed(() -> finish(request, connection,
                "TIMEOUT after 60 seconds. The device may still be working; do not automatically retry."), 60000L);
        return request;
    }

    private boolean isCurrent(int request, long connection) {
        return !destroyed && request == requestId && connection == services.getGeneration();
    }

    private void finish(int request, long connection, String value) {
        main.post(() -> {
            if (!isCurrent(request, connection) || !busy) return;
            busy = false;
            result.setText(value);
            updateState();
        });
    }

    private void failed(int request, long connection, Exception error) {
        finish(request, connection, "ERROR: " + error);
        if (error instanceof RemoteException) services.onRemoteException(connection, (RemoteException) error);
        else if (error.toString().contains("SYSTEM_MODULE_")) services.refreshSystemModules();
    }

    private void connectionChanged() {
        if (destroyed) return;
        if (busy) {
            ++requestId;
            busy = false;
            result.setText("Connection changed; the previous operation was not replayed.");
        }
        updateState();
    }

    private void updateState() {
        if (destroyed) return;
        status.setText(services.getSystemState());
        for (Button button : actions) button.setEnabled(services.getSystemManager() != null && !busy);
    }

    private String describe(Bundle bundle) {
        if (bundle == null) return "null";
        StringBuilder text = new StringBuilder();
        for (String key : bundle.keySet()) text.append(key).append(" = ").append(bundle.get(key)).append('\n');
        return text.length() == 0 ? "Empty Bundle" : text.toString();
    }

    private TextView text(LinearLayout page, String value) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(16);
        page.addView(text);
        return text;
    }

    private void action(LinearLayout page, String label, Runnable action) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setEnabled(false);
        button.setOnClickListener(view -> action.run());
        page.addView(button);
        actions.add(button);
    }

    @Override protected void onDestroy() {
        destroyed = true;
        ++requestId;
        services.disconnect(listener);
        main.removeCallbacksAndMessages(null);
        worker.shutdownNow();
        super.onDestroy();
    }
}
