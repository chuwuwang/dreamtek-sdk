package com.dreamtek.systemservice.test.demo.utils;

import android.content.Context;
import android.util.Log;

import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;

/**
 * System service access for the ported DTKSystemServiceTestClient pages.
 *
 * The host app installs a {@link Provider} backed by the unified
 * dreamtek_deviceservice AAR (IDeviceService.getSystemManager()) before launching
 * {@code BottomNavActivity}; pages no longer bind com.dreamtek.smartpos.system_service directly.
 * Callbacks are delivered on the main thread, like the original ServiceConnection callbacks.
 */
public final class SystemServiceAccess {
    private static final String TAG = "SystemServiceAccess";

    /** Implemented by the host app; bridges to its shared device-service connection. */
    public interface Provider {
        ISystemManager getSystemManager();
        ISysDeviceInfo getSysDeviceInfo();
        INetworkManager getNetworkManager();
        ISettingsManager getSettingsManager();
        void connect(Context context, Listener listener);
        void disconnect(Listener listener);
    }

    /** Notified when the shared connection state changes. */
    public interface Listener {
        void onSystemReady(ISystemManager systemManager, ISysDeviceInfo sysDeviceInfo,
                           INetworkManager networkManager, ISettingsManager settingsManager);
        void onSystemLost();
    }

    private static volatile Provider provider;

    private SystemServiceAccess() { }

    public static void setProvider(Provider value) {
        provider = value;
    }

    /** Subscribes {@code listener} to the shared connection; safe to call again with the same instance. */
    public static boolean bind(Context context, Listener listener) {
        Provider current = provider;
        if (current == null) {
            Log.e(TAG, "SystemServiceProvider not installed by the host application");
            return false;
        }
        current.connect(context.getApplicationContext(), listener);
        return true;
    }

    /** Releases only this page's subscription; the shared connection stays for other pages. */
    public static void unbind(Listener listener) {
        Provider current = provider;
        if (current != null && listener != null) {
            current.disconnect(listener);
        }
    }
}
