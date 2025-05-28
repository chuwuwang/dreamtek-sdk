package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;


public class BeerModule extends TestModule {

    public boolean T_startBeep(String msec) {
        try {
            logUtils.addCaseLog("startBeep execute");
            iBeeper.startBeep(Integer.parseInt(msec));
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("start beep abnormal");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_stopBeep() {
        try {
            logUtils.addCaseLog("stopBeep execute");
            iBeeper.stopBeep();
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("stop beep abnormal");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_startBeepWithConfig(String msec, String bundle) {
        try {
            logUtils.addCaseLog("start beep with config");
            BundleConfig[] bundleConfigs = new BundleConfig[]{

            };
            Bundle bundle1 = convert(bundle, bundleConfigs);
            iBeeper.startBeepWithConfig(Integer.parseInt(msec), bundle1);
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("start beep abnormal");
            e.printStackTrace();
            return false;
        }
    }
}
