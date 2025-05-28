package moudles.newModules;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;

/**
 * Created by Simon on 2021/8/13
 */
public class SerialModule extends TestModule {
    private static String TAG = "SerialModule";

/*
    private boolean T_getDevice( String deviceName ) throws RemoteException {
        iSerialPort = iDevService.getSerialPort( deviceName );
        return true;
    }

    private boolean T_open() {
        try {
            return iSerialPort.open();
        } catch (RemoteException e) {
            logUtils.addCaseLog("open()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    private boolean T_close() {
        try {
            return iSerialPort.close();
        } catch (RemoteException e) {
            logUtils.addCaseLog("close()执行异常");
            e.printStackTrace();
            return false;
        }
    }

    private boolean T_init(String sbps, String spar, String sdbs) {
        try {
            int bps = Integer.valueOf(sbps);
            int par = Integer.valueOf(spar);
            int dbs = Integer.valueOf(sdbs);
            logUtils.addCaseLog("bps=" + bps + ", par=" + par + ", dbs=" + dbs);
            return iSerialPort.init(bps, par, dbs);
        } catch (RemoteException e) {
            logUtils.addCaseLog("init()执行异常");
            e.printStackTrace();
            return false;
        }
    }
    private int T_write(String sBCDData, String sTimeout, String sRepeat) {

        int repeat = Integer.valueOf( sRepeat);
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < repeat ; i++) {
            sBuffer.append(sBCDData);
        }
        return T_write(String.valueOf(sBuffer), sTimeout );
    }

    private int T_write(String sBCDData, String sTimeout) {
        int timeout = Integer.valueOf(sTimeout);

        try {
            byte[] data = StringUtil.hexStr2Bytes(sBCDData);
            //logUtils.addCaseLog("data len=" + data.length + ", timeout=" + timeout);
            return iSerialPort.write(data, timeout);
        } catch (RemoteException e) {
            logUtils.addCaseLog("write()执行异常");
            e.printStackTrace();
            return 0;
        }
    }
    private int T_writeEx(String sBCDData, String sTimeout, String sRepeat) throws RemoteException {

        int repeat = Integer.valueOf( sRepeat);
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < repeat ; i++) {
            sBuffer.append(sBCDData);
        }
        return T_writeEx(String.valueOf(sBuffer), sTimeout );
    }

    private int T_writeEx(String sBCDData, String sTimeout) throws RemoteException {
        int timeout = Integer.valueOf(sTimeout);

        byte[] data = StringUtil.hexStr2Bytes(sBCDData);
        //logUtils.addCaseLog("data len=" + data.length + ", timeout=" + timeout);
        return iSerialPort.writeEx(data, timeout, null);
    }

    private byte[] T_read(String sExpectLen, String sTimeout) {
        int expectLen = Integer.valueOf(sExpectLen);
        int timeout = Integer.valueOf(sTimeout);
        try {
            byte[] data = new byte[expectLen];
            int ret = iSerialPort.read( data, expectLen, timeout);
            if( ret < 0 ){
                return null;
            } else if( ret == 0 ){
                return new byte[0];
            } else if( ret < expectLen ){
                byte[] data2 = new byte[ret];
                System.arraycopy( data, 0, data2, 0, ret );
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

    private byte[] T_read(String sExpectLen, String sTimeout, String sRepeat) {
        int expectLen = Integer.valueOf(sExpectLen);

        int repeat = Integer.valueOf( sRepeat );
        try {
            if( repeat <= 0 ){
                return null;
            }
            byte[] data = T_read( String.valueOf(expectLen*repeat), sTimeout);
            if( null == data ){
                return null;
            }

            int ret = data.length;
            if( ret < 0 ) {
                return null;
            } else if( ret == 0 ){
                return new byte[0];
            } else if (ret != (expectLen*repeat)){
                Log.e(TAG, "没有读到所有数据，实际返回长度:" + ret );
                return null;
            }

            byte[] buffer = new byte[expectLen];
            int offset = 0;
            System.arraycopy(data, offset, buffer, 0, expectLen);
            do{
                // 之前已经有长度检查了
                for (int i = 0; i < expectLen ; i++) {
                    if( buffer[i] != data[offset+i] ){
                        Log.e(TAG, "数据校验出错");
                        Log.d(TAG, StringUtil.byte2HexStr(buffer) );
                        System.arraycopy(data, offset, buffer, 0, expectLen);
                        Log.d(TAG, StringUtil.byte2HexStr(buffer) );
                        return null;
                    }
                }
                offset += expectLen;
            }while ( (offset + expectLen) <= (expectLen*repeat) );
            return buffer;
        } catch (NullPointerException npe) {
            logUtils.addCaseLog("read()执行异常-");
            npe.printStackTrace();
            return null;
        }
    }

    private boolean T_clearInputBuffer() {
        try {
            return iSerialPort.clearInputBuffer();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean T_isBufferEmpty(String input) {
        boolean chk = Boolean.valueOf(input);
        try {
            return iSerialPort.isBufferEmpty(chk);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
*/

}
