package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class UsbPortModule extends TestModule {
    private static final String TAG = "UsbPortModule";

    public boolean T_isUsbSerialConnect() {
        try {
            logUtils.addCaseLog("usb connect execute");
            return iUsbSerialPort.isUsbSerialConnect();
        } catch (RemoteException e) {
            logUtils.addCaseLog("usb connect abnormal");
            e.printStackTrace();
        }
        return false;
    }

    public int T_write(String data, String sRepeat) {
        int repeat = Integer.parseInt(sRepeat);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < repeat; i++) {
            stringBuffer.append(data);
        }
        return T_write(String.valueOf(stringBuffer));
    }

    public int T_write(String data) {
        try {
            logUtils.addCaseLog("write execute");
            byte[] datas = StringUtil.hexStr2Bytes(data);
            iUsbSerialPort.write(datas);
            return datas.length;
        } catch (RemoteException e) {
            logUtils.addCaseLog("write abnormal");
            e.printStackTrace();
            return 0;
        }
    }

    //    public int T_read(String buffer, String timeout) {
//        try {
//            logUtils.addCaseLog("read execute");
//            return iUsbSerialPort.read(StringUtil.hexStr2Bytes(buffer), Integer.parseInt(timeout));
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("read abnormal");
//            e.printStackTrace();
//            return 0;
//        }
//    }
    public byte[] T_read(String sExpectLen, String sTimeout) {
        int expectLen = Integer.valueOf(sExpectLen);
        int timeout = Integer.valueOf(sTimeout);
        try {
            byte[] data = new byte[expectLen];
            int ret = iUsbSerialPort.read(data, timeout);
            if (ret < 0) {
                return null;
            } else if (ret == 0) {
                return new byte[0];
            } else if (ret < expectLen) {
                byte[] data2 = new byte[ret];
                System.arraycopy(data, 0, data2, 0, ret);
                return data2;
            }
            return data;
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read()执行异常-");
            npe.printStackTrace();
            return null;
        } catch (RemoteException e) {
            logUtils.addCaseLog("read()执行异常");
            e.printStackTrace();
            return null;
        }
    }

    public byte[] T_read(String sExpectLen, String sTimeout, String sRepeat) {
        int expectLen = Integer.valueOf(sExpectLen);
        int repeat = Integer.valueOf(sRepeat);
        try {
            if (repeat <= 0) {
                return null;
            }
            byte[] data = T_read(String.valueOf(expectLen * repeat), sTimeout);
            if (null == data) {
                return null;
            }
            int ret = data.length;
            if (ret < 0) {
                return null;
            } else if (ret == 0) {
                return new byte[0];
            } else if (ret != (expectLen * repeat)) {
                Log.e(TAG, "没有读到所有数据，实际返回长度:" + ret);
                return null;
            }

            byte[] buffer = new byte[expectLen];
            int offset = 0;
            System.arraycopy(data, offset, buffer, 0, expectLen);
            do {
                // 之前已经有长度检查了
                for (int i = 0; i < expectLen; i++) {
                    if (buffer[i] != data[offset + i]) {
                        Log.e(TAG, "数据校验出错");
                        Log.d(TAG, StringUtil.byte2HexStr(buffer));
                        System.arraycopy(data, offset, buffer, 0, expectLen);
                        Log.d(TAG, StringUtil.byte2HexStr(buffer));
                        return null;
                    }
                }
                offset += expectLen;
            } while ((offset + expectLen) <= (expectLen * repeat));
            return buffer;
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read()执行异常-");
            npe.printStackTrace();
            return null;
        }
    }
}
