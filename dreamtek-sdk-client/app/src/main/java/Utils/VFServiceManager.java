package Utils;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.activity.BuildConfig;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;

/**
 * Created by Simon on 2019/3/8.
 */

public class VFServiceManager extends ServiceManager {
    private static final String TAG = "VFServiceManager";
    public static final String ACTION_X9SERVICE = BuildConfig.INTENT_ACTION;//"com.vfi.smartpos.device_service";
    public static final String PACKAGE_X9SERVICE = BuildConfig.INTENT_PKGNAME;//"com.vfi.smartpos.deviceservice";
    // static device for VF-Service
    private static IDeviceService deviceService;
    private static IDukpt iDukpt;
    private static IPinpad iPinpad;

    /**
     * create connection to VF-Service, without callback function tell the result,
     * call getStatus() to get the status and get the member value/attribute
     * NOT try getStatus() JUST after connect() be called !
     *
     * @param context
     * @see ServiceManager.ServiceManagerIF
     * @see #connect()
     * @see #getStatus()
     */
    public VFServiceManager(Context context) {
        super(context, PACKAGE_X9SERVICE, ACTION_X9SERVICE, null);
        Log.d(TAG, "Create connection to VF Service ");
    }

    /**
     * create connection to VF-Service, with callback function tell the result,
     * wait callback be called and get the member value/attribute.
     * Or call getStatus() to get the status and get the member value/attribute
     * NOT try getStatus() JUST after connect() be called !
     *
     * @param context
     * @param serviceManagerIF the callback
     * @see ServiceManager.ServiceManagerIF
     * @see #connect()
     * @see #getStatus()
     */
    public VFServiceManager(Context context, ServiceManagerIF serviceManagerIF) {
        super(context, PACKAGE_X9SERVICE, ACTION_X9SERVICE, serviceManagerIF);
        Log.d(TAG, "Create to connection to VF Service ");
    }

    @Override
    protected void onConnected(IBinder iBinder) {
        Log.d(TAG, "onConnected");
        Log.i(TAG, "X990 device service bind success");
        deviceService = IDeviceService.Stub.asInterface(iBinder);
        if (deviceService == null) {
            Log.e(TAG, "no deviceService found, cannot install apk");
        }
        try {
            iDukpt = deviceService.getDUKPT();
            iPinpad = deviceService.getPinpad(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDisconnected() {
        Log.i(TAG, "X990 device service bind fail");
        deviceService = null;
    }

    public static IDeviceService GetDeviceService() {
        return deviceService;
    }

    public static IPinpad GetPinPad() {
        return iPinpad;
    }

    public static IDukpt GetDukpt() {
        return iDukpt;
    }
}
