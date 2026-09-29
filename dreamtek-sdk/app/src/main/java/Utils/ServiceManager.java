package Utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.dreamtek.smartpos.deviceservice.aidl.IBeeper;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.ILed;
import com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IScanner;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IServiceInfo;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReaderEx;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbToken;
import com.dreamtek.smartpos.deviceservice.aidl.IWirelessBaseHelper;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IFelica;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IICodeCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.INtagCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardC;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardEV1;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardNano;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IKLD;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IMKSK;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;
import com.dreamtek.smartpos.deviceservice.aidl.sde.ISde;
import com.dreamtek.smartpos.deviceservice.aidl.utils.IUtils;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * One connection and one set of module objects for this application process.
 *
 * Customer usage:
 *   ServiceManager services = ServiceManager.getInstance();
 *   services.connect(activity, listener);
 *   // Wait for onSystemStateChanged("READY"), then call on a worker:
 *   String serialNo = services.getSysDeviceInfo().getSerialNo();
 *   // Release only this screen's subscription:
 *   services.disconnect(listener);
 *
 * Copy this file together with the unified AAR. It has no dependency on the test UI.
 * Device getters cache by method and parameter for the current connection. System
 * modules are acquired together on a worker. A new binding clears all cached objects.
 */
public final class ServiceManager {
    public static final String PACKAGE_NAME = "com.dreamtek.smartpos.deviceservice";
    public static final String ACTION = "com.dreamtek.smartpos.device_service";
    public static final String SERVICE_CLASS = "com.dreamtek.smartpos.service.DreamTekDeviceService";
    private static final ServiceManager INSTANCE = new ServiceManager();

    public interface Listener {
        default void onConnected(IDeviceService service) { }
        default void onDisconnected() { }
        default void onConnectionFailed(String message) { }
        default void onSystemStateChanged(String state) { }
    }

    private final Handler main = new Handler(Looper.getMainLooper());
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final CopyOnWriteArraySet<Listener> listeners = new CopyOnWriteArraySet<>();
    private Context context;
    private ServiceConnection connection;
    private boolean bound;
    private int reconnectAttempts;
    private volatile long generation;
    private volatile DeviceModules devices;
    private volatile ISystemManager systemManager;
    private volatile ISysDeviceInfo sysDeviceInfo;
    private volatile INetworkManager networkManager;
    private volatile ISettingsManager settingsManager;
    private volatile String systemState = "DISCONNECTED";

    private ServiceManager() { }
    public static ServiceManager getInstance() { return INSTANCE; }

    /** Calls and listener notifications are serialized on the main thread. */
    public void connect(Context context, Listener listener) {
        Context application = context.getApplicationContext();
        main.post(() -> {
            this.context = application;
            boolean added = listeners.add(listener);
            if (devices != null) {
                if (added) {
                    listener.onConnected(devices.service);
                    listener.onSystemStateChanged(systemState);
                }
            } else if (!bound) bind();
        });
    }

    /** Another screen can keep using the same connection after this screen closes. */
    public void disconnect(Listener listener) {
        main.post(() -> {
            listeners.remove(listener);
            if (listeners.isEmpty()) releaseBinding();
        });
    }

    public void reconnect() {
        main.post(() -> {
            reconnectAttempts = 0;
            releaseBinding();
            if (!listeners.isEmpty()) bind();
        });
    }

    public IDeviceService getDeviceService() {
        DeviceModules current = devices;
        return current == null ? null : current.service;
    }
    public ISystemManager getSystemManager() { return systemManager; }
    public ISysDeviceInfo getSysDeviceInfo() { return sysDeviceInfo; }
    public INetworkManager getNetworkManager() { return networkManager; }
    public ISettingsManager getSettingsManager() { return settingsManager; }
    public String getSystemState() { return systemState; }
    public long getGeneration() { return generation; }

    // Named, strongly typed device-module getters. Call these from a worker thread.
    public IBeeper getBeeper() throws RemoteException {
        return getDeviceModule("getBeeper", service -> service.getBeeper());
    }

    public ILed getLed() throws RemoteException {
        return getDeviceModule("getLed", service -> service.getLed());
    }

    public ISerialPort getSerialPort(String deviceType) throws RemoteException {
        return getDeviceModule("getSerialPort" + ":" + deviceType, service -> service.getSerialPort(deviceType));
    }

    public IScanner getScanner(int cameraId) throws RemoteException {
        return getDeviceModule("getScanner" + ":" + cameraId, service -> service.getScanner(cameraId));
    }

    public IMagCardReader getMagCardReader() throws RemoteException {
        return getDeviceModule("getMagCardReader", service -> service.getMagCardReader());
    }

    public IRFCardReader getRFCardReader() throws RemoteException {
        return getDeviceModule("getRFCardReader", service -> service.getRFCardReader());
    }

    public IPinpad getPinpad(int kapId) throws RemoteException {
        return getDeviceModule("getPinpad" + ":" + kapId, service -> service.getPinpad(kapId));
    }

    public IPrinter getPrinter() throws RemoteException {
        return getDeviceModule("getPrinter", service -> service.getPrinter());
    }

    public IExternalSerialPort getExternalSerialPort() throws RemoteException {
        return getDeviceModule("getExternalSerialPort", service -> service.getExternalSerialPort());
    }

    public IUsbSerialPort getUsbSerialPort() throws RemoteException {
        return getDeviceModule("getUsbSerialPort", service -> service.getUsbSerialPort());
    }

    public ISmartCardReader getSmartCardReader(int slotNo) throws RemoteException {
        return getDeviceModule("getSmartCardReader" + ":" + slotNo, service -> service.getSmartCardReader(slotNo));
    }

    public ISmartCardReaderEx getSmartCardReaderEx() throws RemoteException {
        return getDeviceModule("getSmartCardReaderEx", service -> service.getSmartCardReaderEx());
    }

    public IEMV getEMV() throws RemoteException {
        return getDeviceModule("getEMV", service -> service.getEMV());
    }

    public IDukpt getDUKPT() throws RemoteException {
        return getDeviceModule("getDUKPT", service -> service.getDUKPT());
    }

    public IFelica getFelica() throws RemoteException {
        return getDeviceModule("getFelica", service -> service.getFelica());
    }

    public IUtils getUtils() throws RemoteException {
        return getDeviceModule("getUtils", service -> service.getUtils());
    }

    public IKLD getIKLD() throws RemoteException {
        return getDeviceModule("getIKLD", service -> service.getIKLD());
    }

    public INtagCard getNtag() throws RemoteException {
        return getDeviceModule("getNtag", service -> service.getNtag());
    }

    public IICodeCard getICode() throws RemoteException {
        return getDeviceModule("getICode", service -> service.getICode());
    }

    public IUltraLightCard getUtrlLightManager() throws RemoteException {
        return getDeviceModule("getUtrlLightManager", service -> service.getUtrlLightManager());
    }

    public IRSA getIRSA() throws RemoteException {
        return getDeviceModule("getIRSA", service -> service.getIRSA());
    }

    public IUltraLightCardEV1 getUtrlLightEV1Manager() throws RemoteException {
        return getDeviceModule("getUtrlLightEV1Manager", service -> service.getUtrlLightEV1Manager());
    }

    public IUltraLightCardC getUtrlLightCManager() throws RemoteException {
        return getDeviceModule("getUtrlLightCManager", service -> service.getUtrlLightCManager());
    }

    public IUltraLightCardNano getUtrlLightNanoManager() throws RemoteException {
        return getDeviceModule("getUtrlLightNanoManager", service -> service.getUtrlLightNanoManager());
    }

    public IWirelessBaseHelper getWirelessBaseHelper() throws RemoteException {
        return getDeviceModule("getWirelessBaseHelper", service -> service.getWirelessBaseHelper());
    }

    public ISde getSde() throws RemoteException {
        return getDeviceModule("getSde", service -> service.getSde());
    }

    public IMKSK getMKSK() throws RemoteException {
        return getDeviceModule("getMKSK", service -> service.getMKSK());
    }

    public IServiceInfo getServiceInfo() throws RemoteException {
        return getDeviceModule("getServiceInfo", service -> service.getServiceInfo());
    }

    public IUsbToken getUsbToken() throws RemoteException {
        return getDeviceModule("getUsbToken", service -> service.getUsbToken());
    }

    private interface ModuleFactory<T extends IInterface> {
        T get(IDeviceService service) throws RemoteException;
    }
    private static final class DeviceModules {
        final IDeviceService service;
        final Map<String, IInterface> cache = new HashMap<>();
        DeviceModules(IDeviceService service) { this.service = service; }
    }
    @SuppressWarnings("unchecked")
    private <T extends IInterface> T getDeviceModule(String key, ModuleFactory<T> factory) throws RemoteException {
        DeviceModules current = devices;
        if (current == null) throw new RemoteException("Device service is not connected");
        // This lock belongs to one connection's cache; binding/cleanup never waits on an RPC.
        synchronized (current) {
            T module = (T) current.cache.get(key);
            if (module == null) module = factory.get(current.service);
            if (devices != current) throw new DeadObjectException();
            if (module != null) current.cache.put(key, module);
            return module;
        }
    }

    private void bind() {
        if (context == null || bound || listeners.isEmpty()) return;
        setSystemState("CONNECTING");
        connection = new ServiceConnection() {
            @Override public void onServiceConnected(ComponentName name, IBinder binder) {
                if (connection != this || !bound) return;
                clearModules();
                devices = new DeviceModules(IDeviceService.Stub.asInterface(binder));
                long token = generation;
                for (Listener listener : listeners) listener.onConnected(devices.service);
                acquireSystemModules(token);
            }
            @Override public void onServiceDisconnected(ComponentName name) {
                if (connection != this || !bound) return;
                releaseBinding();
                scheduleReconnect();
            }
            @Override public void onBindingDied(ComponentName name) { onServiceDisconnected(name); }
            @Override public void onNullBinding(ComponentName name) { onServiceDisconnected(name); }
        };
        Intent intent = new Intent(ACTION).setClassName(PACKAGE_NAME, SERVICE_CLASS);
        try {
            bound = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
            if (!bound) bindFailed("bindService returned false: " + PACKAGE_NAME);
        } catch (RuntimeException error) {
            bound = false;
            bindFailed(error.toString());
        }
    }

    private void bindFailed(String message) {
        clearModules();
        setSystemState("DISCONNECTED: " + message);
        for (Listener listener : listeners) listener.onConnectionFailed(message);
        scheduleReconnect();
    }

    private void scheduleReconnect() {
        if (listeners.isEmpty() || reconnectAttempts++ >= 3) return;
        long token = generation;
        main.postDelayed(() -> {
            if (generation == token && !listeners.isEmpty() && !bound) bind();
        }, 1000L);
    }

    private void releaseBinding() {
        if (bound) {
            bound = false;
            try { context.unbindService(connection); }
            catch (IllegalArgumentException ignored) { /* Android already removed this binding. */ }
        }
        connection = null;
        clearModules();
        setSystemState("DISCONNECTED");
        for (Listener listener : listeners) listener.onDisconnected();
    }

    private synchronized void clearModules() {
        ++generation;
        devices = null;
        systemManager = null;
        sysDeviceInfo = null;
        networkManager = null;
        settingsManager = null;
    }

    public void refreshSystemModules() {
        main.post(() -> {
            if (devices == null) return;
            synchronized (this) {
                ++generation;
                systemManager = null;
                sysDeviceInfo = null;
                networkManager = null;
                settingsManager = null;
            }
            acquireSystemModules(generation);
        });
    }

    private void acquireSystemModules(long token) {
        IDeviceService device = getDeviceService();
        if (device == null) return;
        setSystemState("SYSTEM_MODULE_INITIALIZING");
        worker.execute(() -> acquireSystemModules(device, token, 0));
        main.postDelayed(() -> {
            if (generation == token && systemState.startsWith("SYSTEM_MODULE_INITIALIZING")) {
                // Keep the device root available; invalidate only this acquisition's late response.
                synchronized (this) { ++generation; }
                setSystemState("NOT_READY: system interfaces timed out after 15 seconds");
            }
        }, 15000L);
    }

    private void acquireSystemModules(IDeviceService device, long token, int attempt) {
        if (generation != token || device != getDeviceService()) return;
        try {
            ISystemManager system = device.getSystemManager();
            ISysDeviceInfo info = system == null ? null : system.getSysDeviceInfo();
            INetworkManager network = system == null ? null : system.getNetworkManager();
            ISettingsManager settings = system == null ? null : system.getSettingsManager();
            main.post(() -> {
                if (generation != token || device != getDeviceService()) return;
                if (system == null) {
                    setSystemState("DISABLED: system functions not enabled (LEGACY_COMPAT)");
                } else if (info == null || network == null || settings == null) {
                    setSystemState("NOT_READY: a system module is null");
                } else {
                    synchronized (this) {
                        systemManager = system;
                        sysDeviceInfo = info;
                        networkManager = network;
                        settingsManager = settings;
                    }
                    setSystemState("READY");
                }
            });
        } catch (Exception error) {
            main.post(() -> {
                if (generation != token) return;
                if (error.toString().contains("SYSTEM_MODULE_INITIALIZING") && attempt < 5) {
                    setSystemState("SYSTEM_MODULE_INITIALIZING: retry " + (attempt + 1) + "/5");
                    main.postDelayed(() -> worker.execute(() -> acquireSystemModules(device, token, attempt + 1)), 1000L);
                } else if (error instanceof RemoteException) {
                    onRemoteException(token, (RemoteException) error);
                } else setSystemState(error.toString());
            });
        }
    }

    /** Call from the RPC catch block. Only the failed connection may be invalidated. */
    public void onRemoteException(long token, RemoteException error) {
        main.post(() -> {
            if (token != generation) return;
            releaseBinding();
            setSystemState("REMOTE_ERROR: " + error);
            scheduleReconnect();
        });
    }

    private void setSystemState(String value) {
        if ("READY".equals(value) || value.startsWith("DISABLED")) reconnectAttempts = 0;
        systemState = value;
        for (Listener listener : listeners) listener.onSystemStateChanged(value);
    }

    /** The test runner captures one set of interfaces for a whole operation. */
    public static final class Snapshot {
        public final long generation;
        public final ISystemManager system;
        public final ISysDeviceInfo deviceInfo;
        public final INetworkManager network;
        public final ISettingsManager settings;
        private Snapshot(ServiceManager manager) {
            generation = manager.generation;
            system = manager.systemManager;
            deviceInfo = manager.sysDeviceInfo;
            network = manager.networkManager;
            settings = manager.settingsManager;
        }
    }
    public synchronized Snapshot snapshot() { return new Snapshot(this); }
}
