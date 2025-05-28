package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class ICodeCardModule extends TestModule {
    private static final String TAG = "ICodeCardModule";

    protected int T_initialized(String CodingType, String ModulationType) {
        try {
            logUtils.addCaseLog("initialized execute");
            return iiCodeCard.initialize(Byte.valueOf(CodingType), Byte.valueOf(ModulationType));
        } catch (RemoteException e) {
            logUtils.addCaseLog("initialized exception");
            e.printStackTrace();
            return 0;
        }
    }

    protected int T_deinitialize(String keep_on) {
        try {
            logUtils.addCaseLog("deinitialize execute");
            return iiCodeCard.deinitialize(Byte.valueOf(keep_on));
        } catch (RemoteException e) {
            logUtils.addCaseLog("deinitialize exception");
            e.printStackTrace();
            return 0;
        }
    }

    protected byte[] T_inventory(String slotcnt, String maskLength, String mask, String maxCards) {
        try {
            logUtils.addCaseLog("inventory execute");
            return iiCodeCard.inventory(Byte.valueOf(slotcnt), Byte.valueOf(maskLength), StringUtil.hexStr2Bytes(mask), Byte.valueOf(maxCards));
        } catch (RemoteException e) {
            logUtils.addCaseLog("inventory exception");
            e.printStackTrace();
            return null;
        }
    }

    protected int T_sendStayQuiet(String ProximityCard) {
        try {
            logUtils.addCaseLog("send stay quiet execute");
            return iiCodeCard.sendStayQuiet(StringUtil.hexStr2Bytes(ProximityCard));
        } catch (RemoteException e) {
            logUtils.addCaseLog("send stay quiet exception");
            e.printStackTrace();
            return 0;
        }
    }

    protected int T_selectPicc(String ProximityCard) {
        try {
            logUtils.addCaseLog("select picc execute");
            return iiCodeCard.selectPicc(StringUtil.hexStr2Bytes(ProximityCard));
        } catch (RemoteException e) {
            logUtils.addCaseLog("select picc exception");
            e.printStackTrace();
            return 0;
        }
    }

    protected byte[] T_getPiccSystemInformation(String ProximityCard) {
        try {
            logUtils.addCaseLog("get Picc System information execute");
            return iiCodeCard.getPiccSystemInformation(StringUtil.hexStr2Bytes(ProximityCard));
        } catch (RemoteException e) {
            logUtils.addCaseLog("get Picc System information exception");
            e.printStackTrace();
            return null;
        }
    }

    protected byte[] T_readSingleBlock(String ProximityCard) {
        try {
            logUtils.addCaseLog("read SingleBlock execute");
            return iiCodeCard.readSingleBlock(StringUtil.hexStr2Bytes(ProximityCard));
        } catch (RemoteException e) {
            logUtils.addCaseLog("read SingleBlock exception");
            e.printStackTrace();
            return null;
        }
    }

    protected byte[] T_readMultipleBlocks(String ProximityCard, String startblock, String count) {
        try {
            logUtils.addCaseLog("read multiple blocks execute");
            return iiCodeCard.readMultipleBlocks(StringUtil.hexStr2Bytes(ProximityCard), Byte.valueOf(startblock), Byte.valueOf(count));
        } catch (RemoteException e) {
            logUtils.addCaseLog("read multiple blocks exception");
            e.printStackTrace();
            return null;
        }
    }

    protected int T_writeSingleBlock(String ProximityCard, String flags, String memBlock) {
        try {
            logUtils.addCaseLog("write single block execute");
            return iiCodeCard.writeSingleBlock(StringUtil.hexStr2Bytes(ProximityCard), Byte.valueOf(flags), StringUtil.hexStr2Bytes(memBlock));
        } catch (RemoteException e) {
            logUtils.addCaseLog("write single block exception");
            e.printStackTrace();
            return 0;
        }
    }
}
