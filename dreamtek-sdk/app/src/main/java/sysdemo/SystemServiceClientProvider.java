package sysdemo;

import android.content.Context;

import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import com.dreamtek.systemservice.test.demo.utils.SystemServiceAccess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Utils.ServiceManager;

/**
 * Bridges the ported SystemServiceTestClient pages to the shared unified connection.
 * The pages obtain the system interfaces through the new
 * dreamtek_deviceservice AAR (IDeviceService.getSystemManager()) via ServiceManager,
 * instead of binding com.dreamtek.smartpos.system_service directly.
 */
public final class SystemServiceClientProvider implements SystemServiceAccess.Provider {
    private final ServiceManager services = ServiceManager.getInstance();
    // One adapter per subscribed page; several fragments may be alive at the same time.
    private final Map<SystemServiceAccess.Listener, Adapter> adapters = new ConcurrentHashMap<>();

    private final class Adapter implements ServiceManager.Listener {
        private final SystemServiceAccess.Listener page;
        private boolean wasReady;

        Adapter(SystemServiceAccess.Listener page) {
            this.page = page;
        }

        @Override
        public void onSystemStateChanged(String state) {
            boolean ready = "READY".equals(state);
            if (ready) {
                page.onSystemReady(services.getSystemManager(), services.getSysDeviceInfo(),
                        services.getNetworkManager(), services.getSettingsManager());
            } else if (wasReady) {
                page.onSystemLost();
            }
            wasReady = ready;
        }

        @Override
        public void onDisconnected() {
            wasReady = false;
            page.onSystemLost();
        }

        @Override
        public void onConnectionFailed(String message) {
            wasReady = false;
            page.onSystemLost();
        }
    }

    @Override
    public ISystemManager getSystemManager() {
        return services.getSystemManager();
    }

    @Override
    public ISysDeviceInfo getSysDeviceInfo() {
        return services.getSysDeviceInfo();
    }

    @Override
    public INetworkManager getNetworkManager() {
        return services.getNetworkManager();
    }

    @Override
    public ISettingsManager getSettingsManager() {
        return services.getSettingsManager();
    }

    @Override
    public void connect(Context context, SystemServiceAccess.Listener pageListener) {
        // Rebinding the same page listener must not create a second adapter.
        Adapter adapter = adapters.get(pageListener);
        if (adapter == null) {
            adapter = new Adapter(pageListener);
            Adapter previous = adapters.putIfAbsent(pageListener, adapter);
            if (previous != null) {
                adapter = previous;
            }
        }
        services.connect(context, adapter);
    }

    @Override
    public void disconnect(SystemServiceAccess.Listener pageListener) {
        Adapter adapter = adapters.remove(pageListener);
        if (adapter != null) {
            services.disconnect(adapter);
        }
    }
}
