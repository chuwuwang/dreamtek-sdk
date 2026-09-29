package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class UltraLightCardCModule extends CardReaderMod {
    public UltraLightCardCModule() {
        super();
        moduleType = 2;
    }

    public int T_pwdAuth(String pPWd) throws RemoteException {
        byte[] pwd = StringUtil.hexStr2Bytes(pPWd);
        int result = iUltraLightCardC.pwdAuth(pwd);
        logUtils.addCaseLog("pwdAuth result: " + result);
        return result;
    }
}
