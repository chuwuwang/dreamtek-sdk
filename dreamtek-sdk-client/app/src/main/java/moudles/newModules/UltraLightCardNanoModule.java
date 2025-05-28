package moudles.newModules;

import android.os.RemoteException;

public class UltraLightCardNanoModule extends CardReaderMod {
    public UltraLightCardNanoModule() {
        super();
        moduleType = 4;
    }

    public String T_getVersion() throws RemoteException {
        String result = iUltraLightCardNano.getVersion();
        logUtils.addCaseLog("get version result: " + result);
        return result;
    }

    public int T_lockSign(String bLockMode) throws RemoteException {
        int result = iUltraLightCardNano.lockSign(Byte.valueOf(bLockMode));
        logUtils.addCaseLog("lock Sign result: " + result);
        return result;
    }


}
