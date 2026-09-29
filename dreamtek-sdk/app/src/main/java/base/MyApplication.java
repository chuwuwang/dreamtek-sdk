package base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.blankj.ALog;
import com.verifone.activity.BuildConfig;

import Utils.ServiceManager;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.systemservice.test.demo.utils.SystemServiceAccess;
import moudles.newModules.TestModule;
import Utils.LocaleHelper;
import Utils.LogUtils;
import moudles.ServiceMoudle;
import moudles.newModules.ServiceModule;
import moudles.newModules.SystemServiceModule;
import sysdemo.SystemServiceClientProvider;

/**
 * Application entry point.
 * Initializes global service modules and applies locale settings.
 */
public class MyApplication extends Application {
    private static Context testAppCtx;

    /** Old service module (deprecated). **/
    public static ServiceMoudle serviceMoudle;
    public static LogUtils logUtils;
    public static ServiceModule newServiceModule;
    public static SystemServiceModule systemServiceModule;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.attachBaseContext(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        testAppCtx = getApplicationContext();
        logUtils = new LogUtils(this);
        serviceMoudle = new ServiceMoudle(this);
        newServiceModule = new ServiceModule(this);
        systemServiceModule = new SystemServiceModule(this);
        // The test application owns the shared connection for its process lifetime.
        // Customer Activities may instead own it with connect/disconnect(listener).
        ServiceManager.getInstance().connect(this, legacyModulesListener);
        // Ported SystemServiceTestClient pages get the system interfaces through the same
        // unified connection (new dreamtek_deviceservice AAR) via this provider.
        SystemServiceAccess.setProvider(new SystemServiceClientProvider());
    }

    // Adapt the old test fields here; ServiceManager itself is independent of this application.
    private final ServiceManager.Listener legacyModulesListener = new ServiceManager.Listener() {
        @Override public void onConnected(IDeviceService deviceService) {
            serviceMoudle.deviceService = deviceService;
            serviceMoudle.isConnect = true;
            newServiceModule.deviceService = deviceService;
            newServiceModule.isConnect = true;
            newServiceModule.updateService(deviceService);
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
        }

        @Override public void onDisconnected() {
            serviceMoudle.clearServices();
            newServiceModule.deviceService = null;
            newServiceModule.isConnect = false;
            TestModule.clearDeviceServices();
            TestModule.setSystemServices(null, null, null);
        }

        @Override public void onSystemStateChanged(String state) {
            ServiceManager services = ServiceManager.getInstance();
            TestModule.setSystemServices(services.getSystemManager(), services.getNetworkManager(),
                    services.getSettingsManager());
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Locale is handled in attachBaseContext.
    }

    public static Context getContext() {
        return testAppCtx;
    }

    public void initALog() {
        ALog.Config config = ALog.init(this)
                .setLogSwitch(BuildConfig.DEBUG)// Set log master switch, including output to console and file, default on
                .setConsoleSwitch(BuildConfig.DEBUG)// Set console output switch, default on
                .setGlobalTag(null)// Set log global tag, default empty
                // When global tag is not empty, all log output uses this tag,
                // when empty, if the passed tag is empty, class name is shown, otherwise the tag
                .setLogHeadSwitch(true)// Set log header info switch, default on
                .setLog2FileSwitch(false)// Switch for saving log to file, default off
                .setDir("")// When custom path is empty, write to app's /cache/log/ directory
                .setFilePrefix("")// When file prefix is empty, defaults to "alog", i.e. "alog-MM-dd.txt"
                .setBorderSwitch(true)// Switch for log output with border, default on
                .setSingleTagSwitch(true)// Single log output per line, default on, for beautifying AS 3.1 Logcat
                .setConsoleFilter(ALog.V)// Console filter, same as logcat filter, default Verbose
                .setFileFilter(ALog.V)// File filter, same as logcat filter, default Verbose
                .setStackDeep(1)// Log stack depth, default 1
                .setStackOffset(0);// Set stack offset, needed for secondary wrapping, default 0
        ALog.d(config.toString());
    }

    public static ServiceMoudle getServiceMoudle() {
        return serviceMoudle;
    }
}