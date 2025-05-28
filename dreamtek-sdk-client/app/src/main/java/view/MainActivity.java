package view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.verifone.activity.R;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;

import Utils.InstallReceiverManager;
import Utils.LogUtil;
import Utils.ServiceManager;
import Utils.SystemServiceManager;
import Utils.VFServiceManager;
import base.MyApplication;
import moudles.ServiceMoudle;
import moudles.newModules.ServiceModule;
import moudles.newModules.SystemServiceModule;
import moudles.newModules.TestModule;
import testtools.MessageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 第一级界面
 */
public class MainActivity extends AppCompatActivity {
    private static String TAG = "TestClient";

    private static int pid = 0;

    //    MyConnection myConnection;
//    SystemserviceConnection systemserviceConnection;
    Button testBt, devpBt, newTestBt;
    ServiceMoudle serviceMoudle;
    ServiceModule newServiceModule;
    TestModule testModule;
    SystemServiceModule systemServiceModule;

    private SystemServiceManager systemService;
    public static VFServiceManager vfService;
    private InstallReceiverManager installReceiver;


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
        serviceMoudle = MyApplication.serviceMoudle;
        newServiceModule = MyApplication.newServiceModule;
        systemServiceModule = MyApplication.systemServiceModule;

        this.startService(new Intent(this, MessageService.class));

        startLogcat();

        installReceiver = new InstallReceiverManager(this, new InstallReceiverManager.InstallCallback() {
            @Override
            public void installSuccessCallback(String packageName) {
                if (SystemServiceManager.PACKAGE_SYSSERVICE.equals(packageName)) {
                    bindSystemService();
                    Log.i(TAG, "Install Success, PackageName=" + packageName);
                }

                if (VFServiceManager.PACKAGE_X9SERVICE.equals(packageName)) {
                    bindDeviceService();
                    Log.i(TAG, "Install Success, PackageName=" + packageName);
                }

            }
        });
        //注册安装系统服务和X9服务监听
        installReceiver.registerInstallReceiver();

        systemService = new SystemServiceManager(this, systemServiceIF);
        vfService = new VFServiceManager(this, vfServiceIF);

        systemService.connect();
        vfService.connect();
    }

    @Override
    public void finish() {
        vfService.disconnect();
        systemService.disconnect();
        installReceiver.unregisterInstallReceiver();
        super.finish();
//        if (myConnection != null)
//            unbindService(myConnection);
    }

    @Override
    protected void onDestroy() {
        installReceiver.unregisterInstallReceiver();
        vfService.disconnect();
        systemService.disconnect();
        super.onDestroy();
    }

    private void bindDeviceService() {
        Log.d(TAG, "connecting VF service");
        vfService.connect();
        /** bind VF Service **/
//        myConnection = new MyConnection();
//        Intent intent = new Intent();
//        intent.setAction("com.vfi.smartpos.device_service");
//        intent.setPackage("com.vfi.smartpos.deviceservice");
//        if (bindService(intent, myConnection, Context.BIND_AUTO_CREATE)) {
//            MyApplication.serviceMoudle.isConnect = true;
//            MyApplication.newServiceModule.isConnect = true;
//
//        } else {
//            MyApplication.serviceMoudle.isConnect = false;
//            MyApplication.newServiceModule.isConnect = false;
//        }

    }

    private void bindSystemService() {
        Log.d(TAG, "connecting System service");
        systemService.connect();
        /** bind System Service **/
//        systemserviceConnection = new SystemserviceConnection();
//        Intent intent = new Intent();
//        intent.setAction("com.vfi.smartpos.system_service");
//        intent.setPackage("com.vfi.smartpos.system_service");
//        if (bindService(intent, systemserviceConnection, Context.BIND_AUTO_CREATE)) {
//            MyApplication.systemServiceModule.isConnect = true;
//
//        } else {
//            MyApplication.systemServiceModule.isConnect = false;
//        }
    }

    public void getConnected(View view) {
        /** bind VF Service **/
        bindDeviceService();

        /** bind System Service **/
        bindSystemService();

    }

    //测试进去
    public void testIn(View v) {
        startActivity(new Intent(this, TestActivity.class));
    }

    //开发进入
    public void devpIn(View v) {
        startActivity(new Intent(this, DevelopActivity.class));
    }

    public void otherIn(View v) {
        startActivity(new Intent(this, OtherActivity.class));
    }

    public void newTestIn(View v) {
        startActivity(new Intent(this, NewTestActivity.class));
    }
//
//    public class MyConnection implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            serviceMoudle.deviceService = IDeviceService.Stub.asInterface(iBinder);
//            serviceMoudle.getScanBtMoudle();
//            serviceMoudle.getPortBtMoudle();
//            serviceMoudle.getPintBtMoudle();
//            serviceMoudle.getPinpadMoudle();
//            serviceMoudle.getPbocBtMoudle();
//            serviceMoudle.getMagCardReaderMoudle();
//            serviceMoudle.getLedMoudle();
//            serviceMoudle.getIrfCardReaderMoudle();
//            serviceMoudle.getInsertCardReaderMoudle();
//            serviceMoudle.getDeviceBtMoudle();
//            serviceMoudle.getBeerMoudle();
//            serviceMoudle.getUsbSerialModule();
//            testBt.setEnabled(true);
//            devpBt.setEnabled(true);
//            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
//
//
//            newServiceModule.deviceService = IDeviceService.Stub.asInterface(iBinder);
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            Toast.makeText(MainActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
//            serviceMoudle.deviceService = null;
//            newServiceModule.deviceService = null;
//            testBt.setEnabled(false);
//            newTestBt.setEnabled(false);
//        }
//    }

//    public class SystemserviceConnection implements ServiceConnection {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            systemServiceModule.iSystemManager = ISystemManager.Stub.asInterface(service);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(MainActivity.this, "systemservice断开连接", Toast.LENGTH_SHORT).show();
//            systemServiceModule.iSystemManager = null;
//        }
//    }

    private ServiceManager.ServiceManagerIF vfServiceIF = new ServiceManager.ServiceManagerIF() {
        @Override
        public void onBindSuccess() {
            MyApplication.serviceMoudle.isConnect = true;
            MyApplication.newServiceModule.isConnect = true;

        }

        @Override
        public void onBindFails() {
            MyApplication.serviceMoudle.isConnect = false;
            MyApplication.newServiceModule.isConnect = false;
        }

        @Override
        public void onConnected(IBinder iBinder) {
            serviceMoudle.deviceService = IDeviceService.Stub.asInterface(iBinder);
            serviceMoudle.getScanBtMoudle();
            serviceMoudle.getSerialPortMoudle();
            serviceMoudle.getPintBtMoudle();
            serviceMoudle.getPinpadMoudle();
            serviceMoudle.getMagCardReaderMoudle();
            serviceMoudle.getLedMoudle();
            serviceMoudle.getIrfCardReaderMoudle();
            serviceMoudle.getInsertCardReaderMoudle();
            serviceMoudle.getServiceInfoMoudle();
            serviceMoudle.getBeerMoudle();
            serviceMoudle.getUsbSerialModule();
            testBt.setEnabled(true);
            devpBt.setEnabled(true);
            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();


            newServiceModule.deviceService = IDeviceService.Stub.asInterface(iBinder);
            newServiceModule.updateService(IDeviceService.Stub.asInterface(iBinder));
            updateVersions();

        }

        @Override
        public void onDisconnected() {
            Toast.makeText(MainActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
            serviceMoudle.deviceService = null;
            newServiceModule.deviceService = null;
            testBt.setEnabled(false);
            newTestBt.setEnabled(false);
        }
    };

    private ServiceManager.ServiceManagerIF systemServiceIF = new ServiceManager.ServiceManagerIF() {
        @Override
        public void onBindSuccess() {
            MyApplication.systemServiceModule.isConnect = true;
        }

        @Override
        public void onBindFails() {
            MyApplication.systemServiceModule.isConnect = false;
        }

        @Override
        public void onConnected(IBinder service) {
            Log.d(TAG, "onConnected");
            Log.d(TAG, "X990 system service bind success");
//            systemServiceModule.iSystemManager = ISystemManager.Stub.asInterface(service);
            newServiceModule.updateService(ISystemManager.Stub.asInterface(service));

        }

        @Override
        public void onDisconnected() {
            Log.e(TAG, "X990 system service bind fail");
//            systemManagerIF = null;
            Toast.makeText(MainActivity.this, "systemservice断开连接", Toast.LENGTH_SHORT).show();
//            systemServiceModule.iSystemManager = null;
        }
    };


    private void startLogcat() {

        if (pid == Binder.getCallingPid()) {
            return;
        }
        pid = Binder.getCallingPid();
        // save all log to file
        String logPath = "/sdcard/";

        String logFileName = logPath + "testclient.log";
        // String command = "logcat -v threadtime -f " + logFileName + " -r 1000" +  " --pid=" + Binder.getCallingPid() + " *:I";
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
//            TextView tv = (TextView)findViewById(R.id.textView1);
//            tv.setText(log.toString());
        } catch (IOException e) {
        }
    }

    void updateVersions() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packInfo = null;

        TextView tv = (TextView) findViewById(R.id.textView);
        String versions = "";
        //            versions += "VF-Service : ";
//            versions += newServiceModule.deviceService.getDeviceInfo().getServiceVersion();

        try {
            packInfo = packageManager.getPackageInfo(vfService.getPackageName(), 0);
            String version = packInfo.versionName;
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String lastUpdate = sdf.format((new Date(packInfo.lastUpdateTime)));

            versions += ", " + lastUpdate;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            versions += "\nSys-Service : ";
            packInfo = packageManager.getPackageInfo(systemService.getPackageName(), 0);
            String version = packInfo.versionName;
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String lastUpdate = sdf.format((new Date(packInfo.lastUpdateTime)));

            versions += version + ", " + lastUpdate;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


//            versions += "\nROM : ";
//            versions += newServiceModule.deviceService.getDeviceInfo().getROMVersion();
//
//            versions += "\nSP : ";
//            versions += newServiceModule.deviceService.getDeviceInfo().getK21Version();
//
//            versions += "\nSN : ";
//            versions += newServiceModule.deviceService.getDeviceInfo().getSerialNo();

        //
        // int a =getApplicationContext().checkCallingOrSelfPermission("smartpos.deviceservice.permission.EMV");
        // versions +="\nPermission: ";
        // versions += a;

        tv.setText(versions);

    }


}