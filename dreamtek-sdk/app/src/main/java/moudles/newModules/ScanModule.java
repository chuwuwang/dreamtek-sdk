package moudles.newModules;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.ScannerListener;

public class ScanModule extends TestModule {

    Object callbackLock = new Object();
    String code;

    public String T_startScan(String param, String timeout) {
        try {
            logUtils.addCaseLog("start scanner execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("topTitleString", BundleConfig_String),
                    new BundleConfig("upPromptString", BundleConfig_String),
                    new BundleConfig("upPromptString", BundleConfig_String),
                    new BundleConfig("showScannerBorder", BundleConfig_boolean),
                    new BundleConfig("scannerSelect", BundleConfig_int),
                    new BundleConfig("useMaxResolution", BundleConfig_boolean),
                    new BundleConfig("startPreView", BundleConfig_boolean),
                    new BundleConfig("decodeLibName", BundleConfig_String)
            };
            Bundle bundle = convert(param, bundleConfigs);
            iScanner.startScan(bundle, Long.parseLong(timeout), new ScannerListener.Stub() {
                @Override
                public void onSuccess(String barcode) throws RemoteException {
                    synchronized (callbackLock) {
                        code = "success";
                        callbackLock.notify();
                    }
                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    synchronized (callbackLock) {
                        code = String.valueOf(error);
                        callbackLock.notify();
                    }
                }

                @Override
                public void onTimeout() throws RemoteException {
                    synchronized (callbackLock) {
                        code = "time out";
                        callbackLock.notify();
                    }
                }

                @Override
                public void onCancel() throws RemoteException {
                    synchronized (callbackLock) {
                        code = "cancel";
                        callbackLock.notify();
                    }
                }
            });
            synchronized (callbackLock) {
                try {
                    callbackLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("start scanner abnormal");
            e.printStackTrace();
        }
        Log.d("scanTAG", code);
        return code;
    }

    public boolean T_stopScan() {
        try {
            logUtils.addCaseLog("stop scanner execute");
            iScanner.stopScan();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("stop scanner abnormal");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_scannerInit(String param) {
        try {
            logUtils.addCaseLog("scanner init execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("customUI", BundleConfig_boolean),
                    new BundleConfig("x1", BundleConfig_int),
                    new BundleConfig("y1", BundleConfig_int),
                    new BundleConfig("width", BundleConfig_int),
                    new BundleConfig("height", BundleConfig_int)
            };
            Bundle bundle = convert(param, bundleConfigs);
            iScanner.scannerInit(bundle);
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("scanner init abnormal");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_openFlashLight(String enable) {
        try {
            logUtils.addCaseLog("open flashlight execute");
            iScanner.openFlashLight(Boolean.parseBoolean(enable));
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("open flashlight abnormal");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_closeFlashLight(String enable) {
        try {
            logUtils.addCaseLog("close flashlight execute");
            iScanner.openFlashLight(Boolean.parseBoolean(enable));
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("close flashlight abnormal");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_switchScanner() {
        try {
            logUtils.addCaseLog("switch scanner execute");
            iScanner.switchScanner();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("switch scanner abnormal");
            e.printStackTrace();
            return false;
        }
    }
}
