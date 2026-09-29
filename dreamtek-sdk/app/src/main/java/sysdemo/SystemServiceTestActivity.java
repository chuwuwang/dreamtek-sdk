package sysdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.dreamtek.smartpos.system_service.aidl.*;
import com.dreamtek.smartpos.system_service.aidl.settings.ICustomerConfigurationUpdateListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Utils.ServiceManager;

/** Interactive system cases sharing the application's current device-service connection. */
public class SystemServiceTestActivity extends AppCompatActivity {
    private static final int PICK_FILE = 401;
    private final Handler main = new Handler(Looper.getMainLooper());
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final List<JSONObject> cases = new ArrayList<>();
    private final List<EditText> fields = new ArrayList<>();
    private final ServiceManager services = ServiceManager.getInstance();
    private final ServiceManager.Listener listener = new ServiceManager.Listener() {
        @Override public void onConnected(com.dreamtek.smartpos.deviceservice.aidl.IDeviceService device) { refreshState(); }
        @Override public void onDisconnected() { refreshState(); }
        @Override public void onSystemStateChanged(String state) { refreshState(); }
    };
    private JSONObject selected;
    private LinearLayout content;
    private LinearLayout form;
    private TextView status;
    private TextView output;
    private Button run;
    private Button restore;
    private Operation operation;
    private boolean destroyed;
    private EditText fileTarget;
    private String fileCase;
    private String restoreKey;
    private Object[] restoreArgs;
    private long restoreGeneration;
    private File evidence;

    private static final class Operation {
        final OperationGate gate = new OperationGate();
        final long start = SystemClock.elapsedRealtime();
        final ServiceManager.Snapshot session = ServiceManager.getInstance().snapshot();
        final long generation = session.generation;
        final String id;
        final String key;
        final boolean callback;
        volatile boolean rpcFinished;
        Operation(String id, String key, boolean callback) {
            this.id = id; this.key = key; this.callback = callback;
        }
    }

    private static final class CallbackSink {
        final WeakReference<SystemServiceTestActivity> host;
        final Operation operation;
        CallbackSink(SystemServiceTestActivity activity, Operation value) {
            host = new WeakReference<>(activity); operation = value;
        }
        void receive(String value) {
            SystemServiceTestActivity activity = host.get();
            if (activity != null) activity.finishOperation(operation, "CALLBACK", value);
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("System Service Test");
        evidence = new File(getExternalFilesDir(null), "system-tests-" + System.currentTimeMillis() + ".jsonl");
        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setFocusableInTouchMode(true);
        content.requestFocus();
        int pad = (int) (12 * getResources().getDisplayMetrics().density);
        content.setPadding(pad, pad, pad, pad);
        scroll.addView(content);
        setContentView(scroll);
        status = text(content, "Connecting…");
        button(content, "Refresh system interfaces", () -> services.refreshSystemModules());
        text(content, "Select a group and case. Results are observations; confirm postconditions on the POS.");
        try (InputStream input = getAssets().open("sysdemo_cases.json")) {
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count;
            while ((count = input.read(buffer)) != -1) bytes.write(buffer, 0, count);
            JSONArray all = new JSONArray(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
            for (int i = 0; i < all.length(); i++) cases.add(all.getJSONObject(i));
        } catch (Exception error) {
            text(content, "Catalog error: " + error);
            return;
        }
        Spinner group = new Spinner(this);
        content.addView(group);
        Spinner method = new Spinner(this);
        content.addView(method);
        form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        content.addView(form);
        run = button(content, "Run selected case", this::confirmRun);
        restore = button(content, "Restore recorded value", this::confirmRestore);
        restore.setEnabled(false);
        output = text(content, "No operation executed.\nEvidence: " + evidence.getAbsolutePath());
        output.setTextIsSelectable(true);
        String[] groups = {"System", "Device Info", "Network", "Settings"};
        group.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groups));
        group.setOnItemSelectedListener(selection(position -> {
            List<JSONObject> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            for (JSONObject entry : cases) {
                if (groups[position].equals(entry.optString("group"))) {
                    entries.add(entry);
                    labels.add(entry.optString("id") + " " + entry.optString("method"));
                }
            }
            method.setOnItemSelectedListener(null);
            method.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labels));
            method.setOnItemSelectedListener(selection(index -> render(entries.get(index))));
        }));
        services.connect(this, listener);
    }

    private interface Selection { void select(int position); }
    private AdapterView.OnItemSelectedListener selection(Selection action) {
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { action.select(position); }
            public void onNothingSelected(AdapterView<?> parent) { }
        };
    }

    private void render(JSONObject entry) {
        selected = entry;
        fields.clear();
        form.removeAllViews();
        text(form, entry.optString("source") + " → " + entry.optString("method"));
        button(form, "Parameters and expected results (API manual)", () -> {
            TextView help = new TextView(this);
            help.setPadding(16, 16, 16, 16);
            help.setText(entry.optString("help"));
            help.setTextIsSelectable(true);
            ScrollView scroll = new ScrollView(this);
            scroll.addView(help);
            new AlertDialog.Builder(this).setTitle(entry.optString("method"))
                    .setView(scroll).setPositiveButton("Close", null).show();
        });
        JSONArray parameters = entry.optJSONArray("params");
        JSONArray defaults = entry.optJSONArray("defaults");
        if (parameters.length() > 0) {
            text(form, "Use <null> for null; empty text stays empty. Bytes: hex. Bundle: JSON (number=int, boolean, string; typed values: {\"type\":\"long\",\"value\":1}).");
        }
        JSONArray presets = entry.optJSONArray("presets");
        Spinner preset = new Spinner(this);
        if (presets != null) form.addView(preset);
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject parameter = parameters.optJSONObject(i);
            String type = parameter.optString("type");
            String name = parameter.optString("name");
            text(form, name + " : " + type);
            EditText input = new EditText(this);
            input.setSingleLine(!"Bundle".equals(type));
            input.setText(expand(defaults.optString(i)));
            input.setContentDescription(name);
            form.addView(input);
            fields.add(input);
            if (type.startsWith("I")) {
                input.setEnabled(false);
                input.setText("Callback recorded automatically (60 second timeout)");
            }
            if ((type.equals("String") && (name.toLowerCase(java.util.Locale.US).contains("path")
                    || name.endsWith("Bin"))) || type.equals("Bitmap")) {
                button(form, "Choose file for " + name, () -> pickFile(input));
            }
        }
        if (presets != null) {
            List<String> labels = new ArrayList<>();
            for (int i = 0; i < presets.length(); i++) labels.add(presets.optJSONObject(i).optString("label"));
            preset.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labels));
            preset.setOnItemSelectedListener(selection(index -> {
                JSONArray values = presets.optJSONObject(index).optJSONArray("values");
                for (int i = 0; i < fields.size(); i++) {
                    if (fields.get(i).isEnabled()) fields.get(i).setText(expand(values.optString(i)));
                }
            }));
        }
        refreshState();
    }

    private String expand(String value) {
        return value.replace("${PACKAGE}", getPackageName()).replace("${NOW}", Long.toString(System.currentTimeMillis()))
                .replace("${SELF_APK}", getApplicationInfo().sourceDir);
    }

    private void refreshState() {
        if (destroyed || run == null) return;
        status.setText("Embedded System: " + services.getSystemState());
        boolean ready = services.getSystemManager() != null;
        if (operation != null && operation.gate.isPending()
                && operation.generation != services.getGeneration()) {
            finishOperation(operation, "ERROR", "Service connection changed; operation was not replayed");
        }
        run.setEnabled(ready && (operation == null || (!operation.gate.isPending() && operation.rpcFinished)));
        restore.setEnabled(ready && restoreArgs != null && restoreGeneration == services.getGeneration()
                && (operation == null || (!operation.gate.isPending() && operation.rpcFinished)));
    }

    private void confirmRun() {
        if (selected == null) return;
        JSONObject entry = selected;
        List<String> inputs = new ArrayList<>();
        for (EditText field : fields) inputs.add(field.getText().toString());
        if (entry.optBoolean("mutating")) {
            new AlertDialog.Builder(this).setTitle("Execute " + entry.optString("method") + "?")
                    .setMessage("This changes the POS. Use a dedicated test device and prepared files/packages. "
                            + "Record original settings; reboot, shutdown, upgrade, uninstall and network changes may interrupt this session. "
                            + "A timeout does not cancel a device operation.\n\nParameters: " + inputs)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Execute once", (dialog, which) -> execute(entry, inputs, null)).show();
        } else execute(entry, inputs, null);
    }

    private void confirmRestore() {
        if (restoreArgs == null || selected == null) return;
        final String key = restoreKey;
        final Object[] args = restoreArgs.clone();
        final long token = restoreGeneration;
        new AlertDialog.Builder(this).setTitle("Restore " + key + "?")
                .setMessage("Restore the value captured before the last change, then read it back.")
                .setNegativeButton("Cancel", null).setPositiveButton("Restore", (d, w) -> {
                    if (token != services.getGeneration()) return;
                    for (JSONObject entry : cases) if (key.equals(entry.optString("key"))) {
                        execute(entry, java.util.Collections.emptyList(), args); break;
                    }
                }).show();
    }

    private void execute(JSONObject entry, List<String> inputs, Object[] restoring) {
        if (services.getSystemManager() == null || (operation != null
                && (operation.gate.isPending() || !operation.rpcFinished))) return;
        Operation op = new Operation(entry.optString("id"), entry.optString("key"), entry.optBoolean("callback"));
        operation = op;
        record(op, "START", (restoring == null ? inputs.toString() : "Restore captured value"));
        refreshState();
        main.postDelayed(() -> finishOperation(op, "TIMEOUT", "No completion within 60 seconds; device operation may still be running. Do not automatically retry."), 60000L);
        worker.execute(() -> {
            Object[] args = restoring;
            try {
                if (destroyed || op.session.system == null || op.generation != services.getGeneration()) {
                    finishOperation(op, "NOT_RUN", "Connection or page changed before execution"); return;
                }
                if (args == null) {
                    JSONArray params = entry.optJSONArray("params");
                    args = new Object[params.length()];
                    CallbackSink sink = new CallbackSink(this, op);
                    for (int i = 0; i < args.length; i++) {
                        String type = params.optJSONObject(i).optString("type");
                        args[i] = type.startsWith("I") ? callback(type, sink)
                                : SystemTestValues.parse(this, type, inputs.get(i));
                        if (params.optJSONObject(i).optString("name").equals("apkPath") && args[i] != null) {
                            record(op, "APK_INPUT", inspectApk((String) args[i]));
                        }
                    }
                }
                String getter = pairedGetter(op.key);
                Object original = null;
                if (getter != null) {
                    original = SystemContractCalls.invoke(op.session, getter, new Object[0]);
                    record(op, "ORIGINAL", SystemTestValues.describe(original));
                    if (restoring == null) {
                        final Object captured = original;
                        main.post(() -> {
                            if (!destroyed && op.generation == services.getGeneration()) {
                                restoreKey = op.key; restoreArgs = new Object[]{captured}; restoreGeneration = op.generation;
                            }
                        });
                    }
                }
                if (!op.gate.isPending() || op.generation != services.getGeneration()) return;
                Object result = SystemContractCalls.invoke(op.session, op.key, args);
                String description = result == null ? "void/null returned; confirm the postcondition" : SystemTestValues.describe(result);
                if (result instanceof Bitmap) {
                    File capture = new File(getExternalFilesDir(null), "capture-" + System.currentTimeMillis() + ".png");
                    try (FileOutputStream stream = new FileOutputStream(capture)) {
                        ((Bitmap) result).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    }
                    description += "; saved " + capture.getAbsolutePath();
                    ((Bitmap) result).recycle();
                }
                if (getter != null) {
                    Object observed = SystemContractCalls.invoke(op.session, getter, new Object[0]);
                    description += "; readback=" + SystemTestValues.describe(observed)
                            + "; matches requested=" + java.util.Objects.equals(args[0], observed);
                }
                if (op.callback) record(op, "SUBMITTED", description + "; awaiting callback");
                else finishOperation(op, "RETURNED", description);
            } catch (Exception error) {
                finishOperation(op, "ERROR", error.toString());
                if (error instanceof RemoteException) services.onRemoteException(op.generation, (RemoteException) error);
                else if (error.toString().contains("SYSTEM_MODULE_")) services.refreshSystemModules();
            } finally {
                if (args != null) for (Object arg : args) if (arg instanceof Bitmap) ((Bitmap) arg).recycle();
                op.rpcFinished = true;
                main.post(this::refreshState);
            }
        });
    }

    private static String pairedGetter(String key) {
        switch (key) {
            case "ISystemManager.changeScreenBrightness": return "ISystemManager.getScreenBrightness";
            case "INetworkManager.setNetworkType": return "INetworkManager.getNetworkType";
            case "INetworkManager.setMultiNetwork": return "INetworkManager.isMultiNetwork";
            case "INetworkManager.setMultiNetworkPrefer": return "INetworkManager.getMultiNetworkPrefer";
            case "INetworkManager.setMobilePreferredNetworkType": return "INetworkManager.getMobilePreferredNetworkType";
            case "INetworkManager.setWifiProxyState": return "INetworkManager.getWifiProxyState";
            case "ISettingsManager.setScreenLock": return "ISettingsManager.isScreenLock";
            case "ISettingsManager.setSleepDuration": return "ISettingsManager.getSleepDuration";
            default: return null;
        }
    }

    private static Object callback(String type, CallbackSink sink) {
        switch (type) {
            case "IAppInstallObserver": return new IAppInstallObserver.Stub() {
                public void onInstallFinished(String name, int code) { sink.receive("onInstallFinished package=" + name + " returnCode=" + code); }
            };
            case "IAppDeleteObserver": return new IAppDeleteObserver.Stub() {
                public void onDeleteFinished(String name, int code) { sink.receive("onDeleteFinished package=" + name + " returnCode=" + code); }
            };
            case "IVerifysignCallback": return new IVerifysignCallback.Stub() {
                public void onVerifySignResult(boolean result) { sink.receive("onVerifySignResult=" + result); }
            };
            case "IAddNetworkAllowedListObserver": return new IAddNetworkAllowedListObserver.Stub() {
                public void onResult(int result) { sink.receive("onResult=" + result); }
            };
            case "ICustomerConfigurationUpdateListener": return new ICustomerConfigurationUpdateListener.Stub() {
                public void onError(int code, String message) { sink.receive("onError code=" + code + " message=" + message); }
            };
            default: throw new IllegalArgumentException("Unknown callback " + type);
        }
    }

    private void finishOperation(Operation op, String event, String detail) {
        main.post(() -> {
            if (destroyed || operation != op) return;
            if (op.generation != services.getGeneration()) {
                if (op.gate.complete()) record(op, "ERROR", "Connection changed; discarded late result: " + event);
            } else if (op.gate.complete()) record(op, event, detail);
            refreshState();
        });
    }

    private synchronized void record(Operation op, String event, String detail) {
        try {
            JSONObject json = new JSONObject();
            json.put("time", System.currentTimeMillis());
            json.put("caseId", op.id);
            json.put("method", op.key);
            json.put("event", event);
            json.put("elapsedMs", SystemClock.elapsedRealtime() - op.start);
            json.put("generation", op.generation);
            json.put("detail", detail);
            String line = json.toString();
            try (FileOutputStream stream = new FileOutputStream(evidence, true)) {
                stream.write((line + "\n").getBytes(StandardCharsets.UTF_8));
            }
            android.util.Log.i("SystemServiceTest", line);
            main.post(() -> {
                if (!destroyed && operation == op && (!"SUBMITTED".equals(event) || op.gate.isPending())) output.setText(event + " " + op.id + " (" + (SystemClock.elapsedRealtime() - op.start)
                        + " ms)\n" + detail + "\n\nEvidence: " + evidence.getAbsolutePath());
            });
        } catch (Exception error) {
            android.util.Log.e("SystemServiceTest", "Evidence write failed: " + evidence, error);
        }
    }

    private void pickFile(EditText target) {
        fileTarget = target;
        fileCase = selected.optString("key");
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FILE);
            return;
        }
        Intent pick = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(pick, PICK_FILE);
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == PICK_FILE && fileTarget != null
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) pickFile(fileTarget);
        else if (output != null) output.setText("Storage permission unavailable. Enter a path readable by the service process.");
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_FILE || resultCode != RESULT_OK || data == null || data.getData() == null) return;
        Uri uri = data.getData();
        EditText target = fileTarget;
        String key = fileCase;
        worker.execute(() -> {
            try {
                File directory = new File(android.os.Environment.getExternalStoragePublicDirectory(
                        android.os.Environment.DIRECTORY_DOWNLOADS), "DTKSystemTest");
                if (!directory.isDirectory() && !directory.mkdirs()) throw new java.io.IOException("Cannot create " + directory);
                String name = "selected-file";
                try (android.database.Cursor cursor = getContentResolver().query(uri,
                        new String[]{android.provider.OpenableColumns.DISPLAY_NAME}, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) name = cursor.getString(0);
                }
                name = name == null ? "selected-file" : name.replaceAll("[^A-Za-z0-9._-]", "_");
                File file = new File(directory, "input-" + System.currentTimeMillis() + "-" + name);
                try (InputStream input = getContentResolver().openInputStream(uri); FileOutputStream stream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[8192]; int count;
                    while ((count = input.read(buffer)) != -1) stream.write(buffer, 0, count);
                }
                main.post(() -> {
                    if (!destroyed && selected.optString("key").equals(key) && fields.contains(target)) {
                        target.setText(file.getAbsolutePath());
                        output.setText("Copied to " + file + "\nClient copy complete. Service readability remains unverified until its operation returns/callbacks.");
                    }
                });
            } catch (Exception error) {
                main.post(() -> { if (!destroyed) output.setText("File copy error: " + error); });
            }
        });
    }

    private String inspectApk(String path) throws Exception {
        PackageInfo info = getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_SIGNATURES);
        if (info == null) throw new IllegalArgumentException("Cannot inspect APK package/version/signature: " + path);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream stream = new java.io.FileInputStream(path)) {
            byte[] buffer = new byte[8192]; int count;
            while ((count = stream.read(buffer)) != -1) digest.update(buffer, 0, count);
        }
        StringBuilder result = new StringBuilder("path=").append(path).append(" package=").append(info.packageName)
                .append(" version=").append(info.versionName).append(" code=").append(info.versionCode)
                .append(" sha256=").append(SystemTestValues.describe(digest.digest()));
        if (info.signatures != null) for (android.content.pm.Signature signature : info.signatures) {
            result.append(" signerSha256=").append(SystemTestValues.describe(digest.digest(signature.toByteArray())));
        }
        return result.toString();
    }

    private TextView text(LinearLayout parent, String value) {
        TextView text = new TextView(this); text.setText(value); text.setTextSize(15); parent.addView(text); return text;
    }
    private Button button(LinearLayout parent, String value, Runnable action) {
        Button button = new Button(this); button.setText(value); button.setAllCaps(false);
        parent.addView(button); button.setOnClickListener(view -> action.run()); return button;
    }

    @Override protected void onDestroy() {
        if (operation != null) {
            if (operation.gate.isPending()) record(operation, "ABANDONED", "Page destroyed; device operation is not cancelled");
            operation.gate.close();
        }
        destroyed = true;
        services.disconnect(listener);
        main.removeCallbacksAndMessages(null);
        worker.shutdownNow();
        super.onDestroy();
    }
}
