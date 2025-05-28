package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class NtagCardModule extends CardReaderMod {
    private static final String TAG = "NtagCardModule";

    public NtagCardModule() {
        super();
        moduleType = 0;
    }

    public boolean T_getVersion() {
        try {
            byte[] version = new byte[32];
            version = iNtagCard.getVersion();
            Log.d(TAG, "card version: " + version);
            logUtils.addCaseLog("get version result: " + StringUtil.byte2HexStr(version));
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("get version exception");
            e.printStackTrace();
            return false;
        }
    }

    public byte[] T_fastRead(String addrStart, String addrEnd) throws RemoteException {
        byte[] result = iNtagCard.fastRead(Byte.valueOf(addrStart), Byte.valueOf(addrEnd));
        logUtils.addCaseLog("fastRead result: " + result);
        return result;
    }

    public boolean T_readSig() throws RemoteException {
        byte[] result = iNtagCard.readSig();
        if (result.length > 0) {
            logUtils.addCaseLog("readSig result: " + result);
            return true;
        } else {
            logUtils.addCaseLog("readSign failed");
            return false;
        }
    }

    public byte[] T_readCnt() throws RemoteException {
        byte[] result = iNtagCard.readCnt();
        logUtils.addCaseLog("readCnt result: " + result);
        return result;
    }
}
