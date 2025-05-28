package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.dreamtek.smartpos.deviceservice.aidl.TusnData;

import java.util.Set;

/**
 * Created by Simon on 2021/7/30
 */
public class DeviceInfoModule extends TestModule {
    private static final String TAG = "DeviceInfoModule";

    /*
    public String T_getSerialNo() {
        try {
            String num = iDeviceInfo.getSerialNo();
            this.printMsgTool("The execution result ：getSerialNo=" + num);
            logUtils.addCaseLog("Execute getSerialNo" + num);
            return num;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getSerialNo exception");
            e.printStackTrace();
            return "Failure";
        }
    }

    public String T_getIMSI() {
        try {
            String str = iDeviceInfo.getIMSI();
//            this.printMsgTool("The execution result：getIMSI=" + str);
            logUtils.addCaseLog("Execute getIMSI" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getIMSI exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getIMEI() {
        try {
            String imei = iDeviceInfo.getIMEI();
//            this.printMsgTool("The execution result：getIMEI=" + imei);
            logUtils.addCaseLog("Execute getIMEI" + imei);
            return imei;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getIMEI exception");
            e.printStackTrace();
            return null;
        }
    }

    public String T_getICCID() {
        try {
            String str = iDeviceInfo.getICCID();
//            this.printMsgTool("The execution result：getICCID=" + str);
            logUtils.addCaseLog("Execute getICCID" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getICCID exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getManufacture() {
        try {
            String str = iDeviceInfo.getManufacture();
//            this.printMsgTool("The execution result：getManufacture=" + str);
            logUtils.addCaseLog("Execute getManufacture" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getManufacture exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getModel() {
        try {
            String str = iDeviceInfo.getModel();
//            this.printMsgTool("The execution result：getModel=" + str);
            logUtils.addCaseLog("Execute getModel" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getModel exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getAndroidOSVersion() {
        try {
            String str = iDeviceInfo.getAndroidOSVersion();
//            this.printMsgTool("The execution result：getAndroidOSVersion=" + str);
            logUtils.addCaseLog("Execute getAndroidOSVersion" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getAndroidOSVersion exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getAndroidKernelVersion() {
        try {
            String str = iDeviceInfo.getAndroidKernelVersion();
//            this.printMsgTool("The execution result：getAndroidKernelVersion=" + str);
            logUtils.addCaseLog("Execute getAndroidKernelVersion" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getAndroidKernelVersion exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getROMVersion() {
        try {
            String str = iDeviceInfo.getROMVersion();
//            this.printMsgTool("The execution result：getROMVersion=" + str);
            logUtils.addCaseLog("Execute getROMVersion" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getROMVersion exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public String T_getFirmwareVersion() {
        try {
            String str = iDeviceInfo.getFirmwareVersion();
//            this.printMsgTool("The execution result：getFirmwareVersion=" + str);
            logUtils.addCaseLog("Execute getFirmwareVersion" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getFirmwareVersion exception");
            e.printStackTrace();
            return "failure";
        }
    }


    public String T_getHardwareVersion() {
        try {
            String str = iDeviceInfo.getHardwareVersion();
//            this.printMsgTool("The execution result：getHardwareVersion=" + str);
            logUtils.addCaseLog("Execute getHardwareVersion" + str);
            return str;
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getHardwareVersion exception");
            e.printStackTrace();
            return "failure";
        }
    }

    public boolean T_updateSystemTime(String date, String time) {
        try {
            logUtils.addCaseLog("Execute updateSystemTime");

            boolean isSuccess = iDeviceInfo.updateSystemTime(date, time);
//            this.printMsgTool("The execution result：updateSystemTime=" + isSuccess);
            logUtils.addCaseLog("End of execute updateSystemTime " + isSuccess);
            return isSuccess;
        } catch (Exception e) {
            Log.e("error", "My12updateSystemTime: " + "error");
            e.printStackTrace();
            logUtils.addCaseLog("Execute updateSystemTime exception");
            return false;
        }
    }

    public String T_setSystemFunction( boolean homeKey, boolean statusBar) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("HOMEKEY", homeKey);
            bundle.putBoolean("STATUSBARKEY", statusBar);
            boolean result = iDeviceInfo.setSystemFunction(bundle);
//            this.printMsgTool("The execution result：" + result);
            logUtils.addCaseLog("setSystemFunction=" + result);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute setSystemFunction exception");
            e.printStackTrace();
        }
        return null;
    }
    public String T_setSystemFunction( String parameters ) throws RemoteException {
        Bundle bundle = convert(parameters, null );
        boolean result = iDeviceInfo.setSystemFunction(bundle);
        return Boolean.toString( result );
    }

    public void T_getTUSN(int mode, byte[] input) {
        TusnData tusnData = null;
        try {
            tusnData = iDeviceInfo.getTUSN(mode, input);
//            this.printMsgTool("The execution result：getTusn=" + tusnData);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        logUtils.addCaseLog("return：tsn=" + tusnData.getTusn() + "\n" + "mac=" + tusnData.getMac());
    }

    public String T_getPN() {
        try {
            String result = iDeviceInfo.getPN();
            logUtils.addCaseLog("Model=" +  result );
//            this.printMsgTool("The execution result：getPN=" + result );
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void T_setPowerStatus(boolean status) {
        try {
            iDeviceInfo.setPowerStatus(status);
//            this.printMsgTool("The execution result：Set the power button to complete，status=" + status);
            logUtils.addCaseLog("Set the power button to complete");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String T_getRamTotal() {
        try {
            String result = iDeviceInfo.getRamTotal();
//            this.printMsgTool("The execution result：Total memory=" + result);
            logUtils.addCaseLog("Total memory=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getRamAvailable() {
        try {
            String result = iDeviceInfo.getRamAvailable();
//            this.printMsgTool("The execution result：Total memory available=" + result);
            logUtils.addCaseLog("Total memory available=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getRomTotal() {
        try {
            String result = iDeviceInfo.getRomTotal();
//            this.printMsgTool("The execution result：Total FLASH=" + result);
            logUtils.addCaseLog("Total FLASH=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getRomAvailable() {
        try {
            String result = iDeviceInfo.getRomAvailable();
//            this.printMsgTool("The execution result：Total amount of Flash available=" + result);
            logUtils.addCaseLog("Total amount of Flash available=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getMobileDataUsageTotal() {
        try {
            String result = iDeviceInfo.getMobileDataUsageTotal();
//            this.printMsgTool("The execution result：Mobile data usage traffic=" + result);
            logUtils.addCaseLog("Mobile data usage traffic=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getBootCounts() {
        try {
            String result = iDeviceInfo.getBootCounts();
//            this.printMsgTool("The execution result：Switching times=" + result);
            logUtils.addCaseLog("Switching times=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getPrintPaperLen() {
        try {
            String result = iDeviceInfo.getPrintPaperLen();
//            this.printMsgTool("The execution result：The feeding length=" + result);
            logUtils.addCaseLog("The feeding length=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getMagCardUsedTimes() {
        try {
            String result = iDeviceInfo.getMagCardUsedTimes();
//            this.printMsgTool("The execution result：The number of swipe card=" + result);
            logUtils.addCaseLog("Credit card number=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getSmartCardUsedTimes() {
        try {
            String result = iDeviceInfo.getSmartCardUsedTimes();
//            this.printMsgTool("The execution result：The number of insert card =" + result);
            logUtils.addCaseLog("The number of insert card=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getCTLSCardUsedTimes() {
        try {
            String result = iDeviceInfo.getCTLSCardUsedTimes();
//            this.printMsgTool("The execution result：The number of wave card=" + result);
            logUtils.addCaseLog("The number of wave card=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getBatteryTemperature() {
        try {
            String result = iDeviceInfo.getBatteryTemperature();
//            this.printMsgTool("The execution result：Battery temperature=" + result);
            logUtils.addCaseLog("Battery temperature=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getBatteryLevel() {
        try {
            String result = iDeviceInfo.getBatteryLevel();
//            this.printMsgTool("The execution result：battery's electricity=" + result);
            logUtils.addCaseLog("battery's electricity=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getK21Version() {
        try {
            String result = iDeviceInfo.getK21Version();
//            this.printMsgTool("The execution result：K21 version=" + result);
            logUtils.addCaseLog("K21 version=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getMEID() {
        try {
            String result = iDeviceInfo.getMEID();
//            this.printMsgTool("The execution result：MEID=" + result);
            logUtils.addCaseLog("MEID = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }}

    public String T_getTamperCode() {
        try {
            String result = iDeviceInfo.getTamperCode();
//            this.printMsgTool("The execution result：TamperCode =" + result);
            logUtils.addCaseLog("TamperCode = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getServiceVersion() {
        try {
            String result = iDeviceInfo.getServiceVersion();
//            this.printMsgTool("The execution result：service result =" + result);
            logUtils.addCaseLog("service result = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getKernelVersion(String want) {
        try {
            Bundle result = iDeviceInfo.getKernelVersion();
            String sResult = result.getString( want );
            boolean isAll = false;
            if( null == sResult || sResult.length() == 0 ){
                isAll = true;
                sResult = "";
            }
            String[] keys = new String[]{
                    "SmartEMV",
                    "Visa",
                    "MasterCard",
                    "JCB",
                    "AMEX",
                    "Discover",
                    "QuickPass",
                    "GemaltoPure",
            };
            for ( String key: keys ) {
                String value = result.getString( key );
                logUtils.addCaseLog( key + " = " + value);
                this.printMsgTool("The execution result：" + key +" = " + value);
                if( isAll ){
                    sResult += value;
                    sResult += "|";
                }
            }
            return sResult;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String T_getCertificate(int mode) {
        try {
            String result = iDeviceInfo.getCertificate(mode);
//            this.printMsgTool("The execution result：obtain Sponser ID =" + result);
            logUtils.addCaseLog("obtain Sponser ID = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String T_getBatteryChargingTimes() {
        try {
            String result = iDeviceInfo.getBatteryChargingTimes();
//            this.printMsgTool("The execution result：Battery charging(1-00) =" + result);
            logUtils.addCaseLog("Battery charging(1-00) = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String T_getDeviceInfo( String want) {
        try {
            Bundle result = iDeviceInfo.getDeviceInfo();
//            this.printMsgTool("The execution result： =" + result);
            logUtils.addCaseLog(" = " + result);

            String sResult = result.getString( want );
            boolean isAll = false;
            if( null == sResult || sResult.length() == 0 ){
                isAll = true;
                sResult = "";
            }
            Set<String> keys = result.keySet();

            for ( String key: keys ) {
                String value = result.getString( key );
                logUtils.addCaseLog( key + " = " + value);
                this.printMsgTool("The execution result：" + key +" = " + value);
                if( isAll ){
                    sResult += value;
                    sResult += "|";
                }
            }
            return sResult;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String T_getDeviceInfoEx( String want) throws RemoteException {
        Bundle req = convert( want, null );
        Bundle result = iDeviceInfo.getDeviceInfoEx( req );
        String sResult = "";
        for (String key: result.keySet() ) {
            sResult += result.get(key);
            sResult += "|";
        }

        return sResult;
    }

     */
}
