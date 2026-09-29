package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.RFSearchListener;

abstract class CardReaderMod extends TestModule {
    private static final String TAG = "CardReaderMod";
    protected int moduleType;
    Object callback = new Object();
    int result;


    protected int T_init() throws RemoteException {
        int result = -1;
        if (moduleType == 0) {
            result = iNtagCard.init();
        } else if (moduleType == 1) {
            result = iUltraLightCard.init();
        } else if (moduleType == 2) {
            result = iUltraLightCardC.init();
        } else if (moduleType == 3) {
            result = iUltraLightCardEV1.init();
        } else if (moduleType == 4) {
            result = iUltraLightCardNano.init();
        }
        logUtils.addCaseLog("init : " + result);
        return result;
    }

    public int T_searchCard(String timeout) throws RemoteException {

        irfCardReader.searchCard(new RFSearchListener.Stub() {
            @Override
            public void onCardPass(int cardType) throws RemoteException {
                synchronized (callback) {
                    result = cardType;
                    callback.notify();
                    logUtils.addCaseLog("search card : " + cardType);
                }
            }

            @Override
            public void onFail(int error, String message) throws RemoteException {
                synchronized (callback) {
                    result = error;
                    callback.notify();
                    logUtils.addCaseLog("search card onFail");
                }
            }
        }, Integer.valueOf(timeout));

        synchronized (callback) {
            try {
                callback.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected int T_compatibilityWrite(String bAddress, String pData) throws RemoteException {
        byte address = Byte.valueOf(bAddress);
        byte[] data = StringUtil.hexStr2Bytes(pData);
        int result = -1;
        if (moduleType == 1) {
            result = iUltraLightCard.compatibilityWrite(address, data);
        } else if (moduleType == 2) {
            result = iUltraLightCardC.compatibilityWrite(address, data);
        } else if (moduleType == 3) {
            result = iUltraLightCardEV1.compatibilityWrite(address, data);
        } else if (moduleType == 4) {
            result = iUltraLightCardNano.compatibilityWrite(address, data);
        }
        logUtils.addCaseLog("compatibility write : " + result);
        return result;
    }

    protected byte[] T_read(String bAddress) throws RemoteException {
        byte address = Byte.valueOf(bAddress);
        byte[] result = new byte[16];
        if (moduleType == 0) {
            result = iNtagCard.read(address);
        } else if (moduleType == 1) {
            result = iUltraLightCard.read(address);
        } else if (moduleType == 2) {
            result = iUltraLightCardC.read(address);
        } else if (moduleType == 3) {
            result = iUltraLightCardEV1.read(address);
        } else if (moduleType == 4) {
            result = iUltraLightCardNano.read(address);
        }
        logUtils.addCaseLog("read result: " + StringUtil.byte2HexStr(result));
        return result;
    }

    protected int T_write(String bAddress, String pData) throws RemoteException {
        byte address = Byte.valueOf(bAddress);
        byte[] data = StringUtil.hexStr2Bytes(pData);
        int result = -1;
        if (moduleType == 0) {
            iNtagCard.write(address, data);
        } else if (moduleType == 1) {
            result = iUltraLightCard.write(address, data);
        } else if (moduleType == 2) {
            result = iUltraLightCardC.write(address, data);
        } else if (moduleType == 3) {
            result = iUltraLightCardEV1.write(address, data);
        } else if (moduleType == 4) {
            result = iUltraLightCardNano.write(address, data);
        }
        logUtils.addCaseLog("write result: " + result);
        return result;
    }

    protected int T_writeSign(String bAddress, String pData) throws RemoteException {
        byte address = Byte.valueOf(bAddress);
        byte[] data = StringUtil.hexStr2Bytes(pData);
        int result = -1;
        if (moduleType == 1) {
            result = iUltraLightCard.writeSign(address, data);
        } else if (moduleType == 4) {
            result = iUltraLightCardNano.writeSign(address, data);
        }
        logUtils.addCaseLog("write sign result: " + result);
        return result;
    }

    protected byte[] T_readSign(String bAddr) throws RemoteException {
        byte[] result = new byte[32];
        if (moduleType == 3) {
            iUltraLightCardEV1.readSign(Byte.valueOf(bAddr));
        } else if (moduleType == 4) {
            iUltraLightCardNano.readSign(Byte.valueOf(bAddr));
        }
        logUtils.addCaseLog("read Sign result: " + StringUtil.byte2HexStr(result));
        return result;
    }

}
