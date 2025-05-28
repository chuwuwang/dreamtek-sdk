package moudles.newModules;

import android.os.IBinder;
import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class BaseServiceModule extends TestModule {
/*
    public static BaseServiceConnection baseService;
    Object callbackLock = new Object();
    int bind_result;
    int update_result;
    int connect_result;
    int state_result;

    public int T_getBaseInfo() throws RemoteException {
        byte[] info = new byte[64];
        logUtils.addCaseLog("get Base Info execute");
        int result = baseService.getBaseInfo(info);
        if (result > 0) {
            logUtils.addCaseLog("baseInfo: " + info);
        } else {
            logUtils.addCaseLog("get Base Info fail");
        }
        return result;
    }

    public int T_getSavedInfo() throws RemoteException {
        SavedInfo savedInfo = new SavedInfo();
        logUtils.addCaseLog("get Saved Info execute");
        int result = baseService.getSavedInfo(savedInfo);
        logUtils.addCaseLog("savedInfo: " + savedInfo.getBaseSN());
        return result;
    }

    public int T_bindBase(String bindingSN, String bindingPN, String bindingWiFiMac, String deviceSN, String devicePN, String deviceWiFiMac, String isBinding) throws RemoteException {
        BindInfo bindInfo = new BindInfo();
        bindInfo.setBindingSN(bindingSN);
        bindInfo.setBindingPN(bindingPN);
        bindInfo.setBindingWiFiMac(bindingWiFiMac);
        bindInfo.setDeviceSN(deviceSN);
        bindInfo.setDevicePN(devicePN);
        bindInfo.setDeviceWiFiMac(deviceWiFiMac);
        bindInfo.setBinding(Boolean.parseBoolean(isBinding));
        baseService.bindBase(bindInfo, new StateListener.Stub() {
            @Override
            public void onState(int code) throws RemoteException {
                synchronized (callbackLock) {
                    bind_result = code;
                }
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        });
        synchronized (callbackLock) {
            try {
                callbackLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logUtils.addCaseLog("bind result: " + bind_result);
        return bind_result;
    }

    public int T_unbindBase() throws RemoteException {
        logUtils.addCaseLog("unbind Base execute");
        int result = baseService.unbindBase();
        if (result == 0) {
            logUtils.addCaseLog("unbind Base success");
        } else {
            logUtils.addCaseLog("unbind Base fail");
        }
        return result;
    }

    public int T_updateTerminal(String filePath) throws RemoteException {
        logUtils.addCaseLog("update Terminal execute");
        baseService.updateTerminal(filePath, new UpdateListener.Stub() {
            @Override
            public void onProgress(float percent) throws RemoteException {

            }

            @Override
            public void onResult(int code) throws RemoteException {
                synchronized (callbackLock) {
                    update_result = code;
                }
            }
        });
        synchronized (callbackLock) {
            try {
                callbackLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (update_result == 0) {
                logUtils.addCaseLog("update success");
            } else {
                logUtils.addCaseLog("update fail");
            }
            return update_result;
        }
    }

    public void T_getConnectState() throws RemoteException {
        logUtils.addCaseLog("get connect State execute");
        int result = baseService.getConnectState();
        logUtils.addCaseLog("connect State : " + result);
    }

    public void connectBase(String operation) throws RemoteException {
        logUtils.addCaseLog("connect Base execute");
        baseService.connectBase(Integer.valueOf(operation), new StateListener.Stub() {
            @Override
            public void onState(int code) throws RemoteException {
                synchronized (callbackLock) {
                    connect_result = code;
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
        if (connect_result == 0) {
            logUtils.addCaseLog("Connect State:连接已登录");
        } else if (connect_result == 01) {
            logUtils.addCaseLog("Connect State:连接未登录");
        } else if (connect_result == 02) {
            logUtils.addCaseLog("Connect State:未连接");
        } else {
            logUtils.addCaseLog("other fail");
        }
    }

    public void T_releaseListener() throws RemoteException {
        baseService.realeaseListener();
        logUtils.addCaseLog("release Listener");
    }

    public int T_setBaseInfo(String info) throws RemoteException {
        logUtils.addCaseLog("set Base Info execute");
        byte[] infos = new byte[64];
        infos = StringUtil.hexStr2Bytes(info);
        int result = baseService.setBaseInfo(infos);
        logUtils.addCaseLog("set Base Info: " + result);
        return result;
    }

    public int T_restartBase() throws RemoteException {
        logUtils.addCaseLog("restart Base execute");
        int result = baseService.restartBase();
        logUtils.addCaseLog("restart Base: " + result);
        return result;
    }

    public byte T_getAttachResult() throws RemoteException {
        logUtils.addCaseLog("get Attach Result execute");
        byte attachResult = baseService.getAttachResult();
        logUtils.addCaseLog("Attach Result: " + attachResult);
        return attachResult;
    }

    public void T_setBaseStateListener() throws RemoteException {
        logUtils.addCaseLog("set Base Listener execute");
        baseService.setBaseStateListener(new StateListener.Stub() {
            @Override
            public void onState(int code) throws RemoteException {
                synchronized (callbackLock) {
                    state_result = code;
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
        logUtils.addCaseLog("Base State: " + state_result);
    }
*/
}
