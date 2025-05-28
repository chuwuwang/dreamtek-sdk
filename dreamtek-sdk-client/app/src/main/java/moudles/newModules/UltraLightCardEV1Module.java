package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class UltraLightCardEV1Module extends CardReaderMod {
    public UltraLightCardEV1Module() {
        super();
        moduleType = 3;
    }

    public byte T_chkTearingEvent(String bCntNum) throws RemoteException {
        byte result = iUltraLightCardEV1.chkTearingEvent(Byte.valueOf(bCntNum));
        logUtils.addCaseLog("chk Tearing Event result : " + result);
        return result;
    }

    public byte[] T_fastRead(String bStartAddr, String bEndAddr) throws RemoteException {
        byte[] result = iUltraLightCardEV1.fastRead(Byte.valueOf(bStartAddr), Byte.valueOf(bEndAddr));
        logUtils.addCaseLog("fast read result: " + StringUtil.byte2HexStr(result));
        return result;
    }

    public int T_incrCnt(String bCntNum, String pCnt) throws RemoteException {
        int result = iUltraLightCardEV1.incrCnt(Byte.valueOf(bCntNum), StringUtil.hexStr2Bytes(pCnt));
        logUtils.addCaseLog("incr Cnt result: " + result);
        return result;
    }

    public byte[] T_pwdAuth(String pPwd) throws RemoteException {
        byte[] result = iUltraLightCardEV1.pwdAuth(StringUtil.hexStr2Bytes(pPwd));
        logUtils.addCaseLog("pwdAuth result: " + StringUtil.byte2HexStr(result));
        return result;
    }

    public byte[] T_readCnt(String bCntNum) throws RemoteException {
        byte[] result = iUltraLightCardEV1.readCnt(Byte.valueOf(bCntNum));
        logUtils.addCaseLog("readCnt result: " + StringUtil.byte2HexStr(result));
        return result;
    }

    public int T_virtualCardSelect(String pVCIIDbyte, String bVCIIDLen) throws RemoteException {
        int result = iUltraLightCardEV1.virtualCardSelect(StringUtil.hexStr2Bytes(pVCIIDbyte), Byte.valueOf(bVCIIDLen));
        logUtils.addCaseLog("virtual Card Select result: " + result);
        return result;
    }

    public String T_getVersion() throws RemoteException {
        String result = iUltraLightCardEV1.getVersion();
        logUtils.addCaseLog("get version result: " + result);
        return result;
    }
}
