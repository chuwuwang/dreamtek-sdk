package moudles.newModules;

import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.SerialDataControl;

public class ExternalSerialPortModule extends TestModule {

    public int T_setExtPinpadPortMode(String portMode) {
        try {
            int result = iExternalSerialPort.setExtPinpadPortMode(Integer.parseInt(portMode));
            logUtils.addCaseLog("set extPinpad Port Mode result: " + result);
            return result;
        } catch (RemoteException e) {
            logUtils.addCaseLog("set extPinpad Port Mode exception");
            e.printStackTrace();
            return -1;
        }
    }

    public boolean T_isExternalConnected() {
        try {
            logUtils.addCaseLog("is external connected execute");
            return iExternalSerialPort.isExternalConnected();
        } catch (RemoteException e) {
            logUtils.addCaseLog("is external connected exception");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_openSerialPort(String portNum, String baudRate, String dataBits, String stopBits, String serialParity) throws RemoteException {
        SerialDataControl serialDataControl = new SerialDataControl(Integer.valueOf(baudRate), Integer.valueOf(dataBits), Integer.valueOf(stopBits), Integer.valueOf(serialParity));
        return iExternalSerialPort.openSerialPort(Integer.parseInt(portNum), serialDataControl);
    }

    public int T_writeSerialPort(String portNum, String writeData, String dataLength) throws RemoteException {
        int result = iExternalSerialPort.writeSerialPort(Integer.valueOf(portNum), StringUtil.hexStr2Bytes(writeData), Integer.valueOf(dataLength));
        logUtils.addCaseLog("write serial port result: " + result);
        return result;
    }

    public byte[] T_readSerialPort(String portNum, String dataLength) throws RemoteException {
        byte[] readData = new byte[64];
        int result = iExternalSerialPort.readSerialPort(Integer.valueOf(portNum), readData, Integer.valueOf(dataLength));
        if (result == 0) {
            logUtils.addCaseLog("read serial port result : " + readData);
            return readData;
        } else {
            logUtils.addCaseLog("read serial port Failed");
            return null;
        }
    }

    public int T_safeWriteSerialPort(String portNum, String writeData, String Length, String timeoutMs) throws RemoteException {
        int result = iExternalSerialPort.safeWriteSerialPort(Integer.valueOf(portNum), StringUtil.hexStr2Bytes(writeData), Integer.valueOf(Length), Long.valueOf(timeoutMs));
        logUtils.addCaseLog("safe write serial port result: " + result);
        return result;
    }

    public int T_safeReadSerialPort(String portNum, String Length, String timeoutMs) throws RemoteException {
        byte[] readData = new byte[64];
        int result = iExternalSerialPort.safeReadSerialPort(Integer.valueOf(portNum), readData, Integer.valueOf(Length), Long.valueOf(timeoutMs));
        logUtils.addCaseLog("safe read serial port : " + readData);
        return result;
    }

    public boolean closeSerialPort(String portNum) {
        try {
            iExternalSerialPort.closeSerialPort(Integer.parseInt(portNum));
            return true;
        } catch (RemoteException e) {
            logUtils.addCaseLog("close serial port exception");
            e.printStackTrace();
            return false;
        }
    }
}
