package moudles;

import static Utils.LogUtil.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.IWirelessBaseHelper;
import com.dreamtek.smartpos.deviceservice.aidl.SerialDataControl;
import com.dreamtek.smartpos.deviceservice.aidl.WirelessConnectListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;
import testtools.ToastUtil;

/**
 * Created by CuncheW1 on 2017/9/6.
 */

public class ExternalSerialPortModule {
    private Context context;
    private IExternalSerialPort iExternalSerialPort;
    private IWirelessBaseHelper iWirelessBaseHelper;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();

    ArrayList<String> setExtPinpadPortMode = new ArrayList<String>();
    ArrayList<String> isExternalConnected = new ArrayList<String>();
    ArrayList<String> openSerialPort = new ArrayList<String>();
    ArrayList<String> writeSerialPort = new ArrayList<String>();
    ArrayList<String> readSerialPort = new ArrayList<String>();
    ArrayList<String> safeWriteSerialPort = new ArrayList<String>();
    ArrayList<String> safeReadSerialPort = new ArrayList<String>();
    ArrayList<String> closeSerialPort = new ArrayList<String>();
    ArrayList<String> getBaseInfo = new ArrayList<String>();
    ArrayList<String> connectReceiver = new ArrayList<>();
    ArrayList<String> bindBase = new ArrayList<>();
    ArrayList<String> unbindBase = new ArrayList<>();
    ArrayList<String> connectStatus = new ArrayList<>();
    ArrayList<String> switchConnectMode = new ArrayList<>();
    ArrayList<String> changeBaseChannel = new ArrayList<>();
    ArrayList<String> getBaseChannel = new ArrayList<>();

    public ExternalSerialPortModule(Context context, IExternalSerialPort iExternalSerialPort, IWirelessBaseHelper iWirelessBaseHelper) {
        this.context = context;
        this.iExternalSerialPort = iExternalSerialPort;
        this.iWirelessBaseHelper = iWirelessBaseHelper;
        logUtils = MyApplication.serviceMoudle.logUtils;
//        addAllapi();
    }

/*
    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.ExternalSerialPortModule");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "Z01":
                            setExtPinpadPortMode.add(i.getName());
                            break;
                        case "Z02":
                            isExternalConnected.add(i.getName());
                            break;
                        case "Z03":
                            openSerialPort.add(i.getName());
                            break;
                        case "Z04":
                            readSerialPort.add(i.getName());
                            break;
                        case "Z05":
                            writeSerialPort.add(i.getName());
                            break;
                        case "Z06":
                            safeReadSerialPort.add(i.getName());
                            break;
                        case "Z07":
                            safeWriteSerialPort.add(i.getName());
                            break;
                        case "Z08":
                            closeSerialPort.add(i.getName());
                            break;
                        case "Z09":
                            getBaseInfo.add(i.getName());
                            break;
                        case "Z10":
                            connectReceiver.add(i.getName());
                            break;
                        case "Z11":
                            bindBase.add(i.getName());
                            break;
                        case "Z12":
                            unbindBase.add(i.getName());
                            break;
                        case "Z13":
                            connectStatus.add(i.getName());
                            break;
                        case "Z14":
                            switchConnectMode.add(i.getName());
                            break;
                        case "Z15":
                            changeBaseChannel.add(i.getName());
                            break;
                        case "Z16":
                            getBaseChannel.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(setExtPinpadPortMode);
            caseNames.add(isExternalConnected);
            caseNames.add(openSerialPort);
            caseNames.add(readSerialPort);
            caseNames.add(writeSerialPort);
            caseNames.add(safeReadSerialPort);
            caseNames.add(safeWriteSerialPort);
            caseNames.add(closeSerialPort);
            caseNames.add(getBaseInfo);
            caseNames.add(connectReceiver);
            caseNames.add(bindBase);
            caseNames.add(unbindBase);
            caseNames.add(connectStatus);
            caseNames.add(switchConnectMode);
            caseNames.add(changeBaseChannel);
            caseNames.add(getBaseChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int My01setExtPinpadPortMode(int portMode) throws RemoteException {
        logUtils.addCaseLog("Base Pinpad mouth mode is as follows：");
        logUtils.addCaseLog("Passthrough mode - " + ExternalSerialConst.MODE_TRANSPARENT);
        logUtils.addCaseLog("Lose close mode - " + ExternalSerialConst.MODE_PP1000V3_PINPAD);
        logUtils.addCaseLog("Not by model - " + ExternalSerialConst.MODE_PP1000V3_CTLS);
        logUtils.addCaseLog("Set the base Pinpad port mode to" + portMode);
        return iExternalSerialPort.setExtPinpadPortMode(portMode);
    }

    public boolean My02isExternalConnected() throws RemoteException {
        logUtils.addCaseLog("The base status flag is shown below: ");
        logUtils.addCaseLog("Connection status - " + true);
        logUtils.addCaseLog("Off state - " + false);
        boolean ret = iExternalSerialPort.isExternalConnected();
        logUtils.addCaseLog("ret = "+ret);
        return ret;
    }

    public boolean My03openSerialPort(int portNum, SerialDataControl dataControl) throws RemoteException {
        return iExternalSerialPort.openSerialPort(portNum, dataControl);
    }

    public int My04readSerialPort(int portNum, byte[] readData, int dataLength) throws RemoteException {
        return iExternalSerialPort.readSerialPort(portNum, readData, dataLength);
    }

    public int My05writeSerialPort(int portNum, byte[] writeData, int dataLength) throws RemoteException {
        return iExternalSerialPort.writeSerialPort(portNum, writeData, dataLength);
    }

    public int My06safeReadSerialPort(int portNum, byte[] readData, int Length, long timeoutMs) throws RemoteException {
        return iExternalSerialPort.safeReadSerialPort(portNum, readData, Length, timeoutMs);
    }

    public int My07safeWriteSerialPort(int portNum, byte[] writeData, int Length, long timeoutMs) throws RemoteException {
        return iExternalSerialPort.safeWriteSerialPort(portNum, writeData, Length, timeoutMs);
    }

    public void My08closeSerialPort(int portNum) throws RemoteException {
        iExternalSerialPort.closeSerialPort(portNum);
    }

    public void My09getBaseInfo() throws RemoteException {
        Bundle bundle = iWirelessBaseHelper.getBaseInfo(null);
        if (bundle != null) {
            String baseSn = bundle.getString(ConstWirelessBase.Info.BaseSN);
            //  16进制 - ASCLL
            byte[] s = StringUtil.hexStr2Bytes(baseSn);
            logUtils.addCaseLog("Connected baseSn: "+StringUtil.byteToStr(s));
            Log.d("TAG","Connected baseSn: "+StringUtil.byteToStr(s));
        } else {
            logUtils.addCaseLog("base haven't connect X990");
        }

    }

    public void My10connectReceiver(){
        //define at Z10001
    }

    public void My11bindBase(Bundle bundle){
        try {
            Bundle bundle1 = iWirelessBaseHelper.getBaseInfo(null);
            if (bundle1 !=null){
                String baseSn = bundle1.getString(ConstWirelessBase.Info.BaseSN);
                //  16进制 - ASCLL
                byte[] s = StringUtil.hexStr2Bytes(baseSn);
                logUtils.addCaseLog("Connected baseSn: "+StringUtil.byteToStr(s));
                Log.d("TAG","Connected baseSn: "+StringUtil.byteToStr(s));
                iWirelessBaseHelper.bindBase(bundle);
            }else {
                logUtils.addCaseLog("Connecting to Wireless Base, please wait...");
                iWirelessBaseHelper.bindBase(bundle);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My12unbindBase(){
        try {
            int ret = iWirelessBaseHelper.unbindBase();
            logUtils.addCaseLog("return result = "+ret);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My13connectStatus(int timeout, WirelessConnectListener wirelessConnectListener){
        try {
            int result = iWirelessBaseHelper.connectStatus(timeout,wirelessConnectListener);
            logUtils.addCaseLog("result = "+result);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My14switchConnectMode(int mode){
        try {
            int res = iWirelessBaseHelper.switchConnectMode(mode);
            logUtils.addCaseLog("mode = "+mode+";res="+res);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean My15changeBaseChannel(int value){
        try {
            return iWirelessBaseHelper.changeBaseChannel(value);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void My16getBaseChannel(){

    }

    public void Z01001() {
        try {
            My01setExtPinpadPortMode(ExternalSerialConst.MODE_TRANSPARENT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void Z01002() {
        try {
            My01setExtPinpadPortMode(ExternalSerialConst.MODE_PP1000V3_PINPAD);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void Z01003() {
        try {
            My01setExtPinpadPortMode(ExternalSerialConst.MODE_PP1000V3_CTLS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void Z02001() {
        boolean bRet = false;
        try {
            bRet = My02isExternalConnected();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("The current base state is：" + bRet);
    }

    public void Z03001() {
        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD115200, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
        boolean bRet = false;
        try {
            bRet = My03openSerialPort(ExternalSerialConst.PORT_PINPAD, dataControl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bRet) {
            logUtils.addCaseLog("The Pinpad port of the base was opened successfully");
        } else {
            logUtils.addCaseLog("Failed to open base Pinpad port");
        }
    }

    public void Z03002() {
        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD115200, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
        boolean bRet = false;
        try {
            bRet = My03openSerialPort(ExternalSerialConst.PORT_RS232, dataControl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bRet) {
            logUtils.addCaseLog("he RS232 port of the base was opened successfully");
        } else {
            logUtils.addCaseLog("Failed to open the base RS232 port");
        }
    }

    public void Z03003() {
        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD38400, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
        boolean bRet = false;
        try {
            bRet = My03openSerialPort(ExternalSerialConst.PORT_RS232, dataControl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bRet) {
            logUtils.addCaseLog("he RS232 port of the base was opened successfully");
        } else {
            logUtils.addCaseLog("Failed to open the base RS232 port");
        }
    }

    public void Z03004() {
        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD57600, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
        boolean bRet = false;
        try {
            bRet = My03openSerialPort(ExternalSerialConst.PORT_RS232, dataControl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bRet) {
            logUtils.addCaseLog("he RS232 port of the base was opened successfully");
        } else {
            logUtils.addCaseLog("Failed to open the base RS232 port");
        }
    }

    public void Z03005() {
        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD19200, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
        boolean bRet = false;
        try {
            bRet = My03openSerialPort(ExternalSerialConst.PORT_RS232, dataControl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bRet) {
            logUtils.addCaseLog("he RS232 port of the base was opened successfully");
        } else {
            logUtils.addCaseLog("Failed to open the base RS232 port");
        }
    }


    public void Z03006() {
        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD9600, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
        boolean bRet = false;
        try {
            bRet = My03openSerialPort(ExternalSerialConst.PORT_RS232, dataControl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bRet) {
            logUtils.addCaseLog("he RS232 port of the base was opened successfully");
        } else {
            logUtils.addCaseLog("Failed to open the base RS232 port");
        }
    }


    public void Z04001() {
        byte[] recvData = new byte[1024];
        int ret = 0;
        try {
            ret = My04readSerialPort(ExternalSerialConst.PORT_PINPAD, recvData, 1024);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Reading base Pinpad mouth ret=" + ret);
        byte[] tmp = new byte[ret];
        System.arraycopy(recvData, 0, tmp, 0, ret);
        logUtils.addCaseLog("data=" + new String(tmp));
        logUtils.addCaseLog("If you want to read the data, the PC needs to send the data first and then call this interface");
    }

    public void Z04002() {
        byte[] recvData = new byte[1024];
        int ret = 0;
        try {
            ret = My04readSerialPort(ExternalSerialConst.PORT_RS232, recvData, 1024);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Read the base RS232 port ret=" + ret);
        byte[] tmp = new byte[ret];
        System.arraycopy(recvData, 0, tmp, 0, ret);
        logUtils.addCaseLog("data=" + new String(tmp));
        logUtils.addCaseLog("If you want to read the data, the PC needs to send the data first and then call this interface");
    }

    public void Z05001() {
        String data = "dasfafdasfdsafjljl";
        int ret = 0;
        try {
            ret = My05writeSerialPort(ExternalSerialConst.PORT_PINPAD, data.getBytes(), data.length());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Base Pinpad to write data ret=" + ret);
    }

    public void Z05002() {
        String data = "12345678";
        byte[] sendData = StringUtil.hexStr2Bytes(data);
        int ret = 0;
        try {
            ret = My05writeSerialPort(ExternalSerialConst.PORT_RS232, sendData, sendData.length);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Base RS232 port write data ret=" + ret);
    }

    public void Z06001() {
        byte[] recvData = new byte[1024];
        int ret = 0;
        try {
            ret = My06safeReadSerialPort(ExternalSerialConst.PORT_PINPAD, recvData, 1024, 5000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Reading base Pinpad mouth ret=" + ret);
        byte[] tmp = new byte[ret];
        System.arraycopy(recvData, 0, tmp, 0, ret);
        logUtils.addCaseLog("data=" + new String(tmp));
    }

    public void Z06002() {
        byte[] recvData = new byte[1024];
        int ret = 0;
        try {
            ret = My06safeReadSerialPort(ExternalSerialConst.PORT_RS232, recvData, 1024, 5000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Read the base RS232 port ret=" + ret);
        byte[] tmp = new byte[ret];
        System.arraycopy(recvData, 0, tmp, 0, ret);
        logUtils.addCaseLog("data=" + new String(tmp));
    }

    public void Z07001() {
        String data = "1234567890-=qwertyuiopasdfghjklmnbvcxz";
        int ret = 0;
        try {
            ret = My07safeWriteSerialPort(ExternalSerialConst.PORT_PINPAD, data.getBytes(), data.length(), 5000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Base Pinpad to write data ret=" + ret);
    }

    public void Z07002() {
        String data = "1234567890-=qwertyuiopasdfghjklmnbvcxz";
        int ret = 0;
        try {
            ret = My07safeWriteSerialPort(ExternalSerialConst.PORT_RS232, data.getBytes(), data.length(), 5000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        logUtils.addCaseLog("Base RS232 port write data ret=" + ret);
    }

    public void Z08001() {
        try {
            My08closeSerialPort(ExternalSerialConst.PORT_PINPAD);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void Z08002() {
        try {
            My08closeSerialPort(ExternalSerialConst.PORT_RS232);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void Z09001() {
        try {
            My09getBaseInfo();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void Z09002(){
        //get base app version
        try {
            Bundle bundle = iWirelessBaseHelper.getBaseInfo(null);
            if (bundle != null) {
                String appVersion = bundle.getString(ConstWirelessBase.Info.BaseAppVersion);
                logUtils.addCaseLog("appVersion="+appVersion);
            }else {
                logUtils.addCaseLog("base haven't connect X990");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void Z10001(){
        try {
            iWirelessBaseHelper.connectReceiver(new ConnectReceiverListener.Stub() {
                @Override
                public void onSuccess(String receiver) throws RemoteException {
                    Log.d("TAG","receiver ="+receiver);
                    ToastUtil.showToastShort("receiver = "+receiver);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void Z10002(){
        try {
            iWirelessBaseHelper.connectReceiver(null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void Z11001(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,"V320000134");
        My11bindBase(bundle);
    }

    public void Z11002(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,"V320000134");
        My11bindBase(bundle);
    }

    public void Z11003(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,"V320000135");
        My11bindBase(bundle);
    }

    public void Z11004(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,"V320000135");
        My11bindBase(bundle);
    }

    public void Z11005(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,null);
        My11bindBase(bundle);
    }

    public void Z11006(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN," ");
        My11bindBase(bundle);
    }

    public void Z11007(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,"");
        My11bindBase(bundle);
    }

    public void Z11008(){
        Bundle bundle = new Bundle();
        bundle.putString(ConstWirelessBase.Info.BaseSN,"V320000178");
        My11bindBase(bundle);
    }

    public void Z11009() throws RemoteException {
        My11bindBase(new Bundle());
    }

    public void Z11010() throws RemoteException {
        My11bindBase(null);
    }

    public void Z12001(){
        My12unbindBase();
    }

    public void Z13001(){
        My13connectStatus(30, new MyWirelessConnectListener());
        // 尝试连接或绑定DX30（因为 timeout = 30 大于等于 20），并且使用 MyWirelessConnectListener 监听连接结果（非阻塞）。返回值为 0 表示正在处理。

        //尝试连接或绑定基站（非阻塞）。如果连接成功，onSuccess() 会被调用并记录 "onSuccess"；如果超时，onTimeout() 会被调用并记录 "onTimeout"；
        // 如果出现错误，onError(int errorCode) 会被调用并记录 "onError"。
    }

    public void Z13002(){
        My13connectStatus(40, new MyWirelessConnectListener());
        //类似 Z13001()，但等待时间更长（因为 timeout = 40 大于等于 20）。依然是非阻塞调用，结果通过 MyWirelessConnectListener 返回。

        //类似 Z13001()，但等待时间更长。监听器的行为与 Z13001() 一致。
    }

    public void Z13003(){
        My13connectStatus(0, new MyWirelessConnectListener());
        //不尝试连接或绑定基站（因为 timeout = 0），只获取连接状态。非阻塞调用，结果通过 MyWirelessConnectListener 返回。

        //只获取连接状态（非阻塞），不会尝试连接或绑定基站。如果当前已连接，onSuccess() 会被调用；
        // 如果当前未连接，但不会触发连接，可能不会触发任何回调。如果出错，onError(int errorCode) 会被调用。

    }

    public void Z13004(){
        My13connectStatus(20, new MyWirelessConnectListener());
        //尝试连接或绑定基站（因为 timeout = 20 大于等于 20），非阻塞调用，结果通过 MyWirelessConnectListener 返回。

        //尝试连接或绑定基站（非阻塞）。监听器的行为与 Z13001() 一致。
    }

    public void Z13005(){
        My13connectStatus(30,null);
        //尝试连接或绑定基站（因为 timeout = 30 大于等于 20），这是一个阻塞调用（因为 wirelessConnectListener = null），
        // 调用会阻塞直到连接成功或超时，返回值为 0 表示连接成功，-1 表示超时，其他值表示错误。


        //尝试连接或绑定基站（阻塞调用）。调用会阻塞直到连接成功或超时。由于没有监听器，结果不会通过回调记录。
    };
    public void Z13006(){
        My13connectStatus(0,null);
        //不尝试连接或绑定基站（因为 timeout = 0），只获取连接状态。阻塞调用，返回值表示当前的连接状态。

        //只获取连接状态（阻塞调用）。由于没有监听器，结果不会通过回调记录。
    }

    public void Z13007(){
        My13connectStatus(255,new MyWirelessConnectListener());
        //尝试连接或绑定基站（因为 timeout = 255 大于等于 20），非阻塞调用，结果通过 MyWirelessConnectListener 返回。

        //尝试连接或绑定基站（非阻塞）。监听器的行为与 Z13001() 一致。
    }

    public void Z13008(){
        My13connectStatus(256,new MyWirelessConnectListener());
        // timeout = 256 是无效值（因为有效范围是 0 到 255），可能会引发错误或异常行为，取决于实际代码实现。使用 MyWirelessConnectListener 监听连接结果。

        //由于 timeout = 256 是无效值，可能会引发错误。如果触发错误，onError(int errorCode) 会被调用并记录 "onError"。
    }

    public void Z13009(){
        My13connectStatus(-1,new MyWirelessConnectListener());
        //timeout = -1 是无效值，可能会引发错误或异常行为，取决于实际代码实现。使用 MyWirelessConnectListener 监听连接结果。

        // 由于 timeout = -1 是无效值，可能会引发错误。如果触发错误，onError(int errorCode) 会被调用并记录 "onError"。
    }

    public void Z13010(){
        My13connectStatus(-1,null);
        //类似 Z13009()，但这是阻塞调用（因为 wirelessConnectListener = null），可能会引发错误或异常行为。

        //类似 Z13009()，但由于没有监听器，结果不会通过回调记录。
    }
    public void Z13011(){
        My13connectStatus(10,new MyWirelessConnectListener());
        //阻塞 返回当前绑定状态
    }

    public void Z14001(){
        logUtils.addCaseLog("省电模式");
        My14switchConnectMode(0);
    }

    public void Z14002(){
        logUtils.addCaseLog("标准模式");
        My14switchConnectMode(1);
    }

    public void Z14003(){
        logUtils.addCaseLog("活跃模式");
        My14switchConnectMode(2);
    }

    public void Z14004(){
        logUtils.addCaseLog("省电模式");
        My14switchConnectMode(0);
    }

    public void Z14005(){
        logUtils.addCaseLog("标准模式");
        My14switchConnectMode(1);
    }

    public void Z14006(){
        logUtils.addCaseLog("活跃模式");
        My14switchConnectMode(2);
    }

    public void Z14007(){
        logUtils.addCaseLog("省电模式");
        My14switchConnectMode(0);
    }

    public void Z14008(){
        logUtils.addCaseLog("标准模式");
        My14switchConnectMode(1);
    }

    public void Z14009(){
        logUtils.addCaseLog("活跃模式");
        My14switchConnectMode(2);
    }

    public void Z14010(){
        logUtils.addCaseLog("标准模式");
        My14switchConnectMode(1);
    }

    public void Z14011(){
        logUtils.addCaseLog("错误模式");
        My14switchConnectMode(6);
    }

    public void Z15001(){
        boolean ret = My15changeBaseChannel(1);
        logUtils.addCaseLog("channel 1: ret = "+ret);
    }

    public void Z15002(){
        boolean ret = My15changeBaseChannel(6);
        logUtils.addCaseLog("channel 6: ret = "+ret);
    }

    public void Z15003(){
        boolean ret = My15changeBaseChannel(11);
        logUtils.addCaseLog("channel 11: ret = "+ret);
    }

    public void Z16001(){;
        try {
            int ret = iWirelessBaseHelper.getBaseChannel();
            logUtils.addCaseLog("getBaseChannel= "+ret);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private class MyWirelessConnectListener extends WirelessConnectListener.Stub {
        String msg;

        public MyWirelessConnectListener(){}

        @Override
        public void onSuccess() throws RemoteException {
            msg = "onSuccess";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            Log.d(TAG,"onSuccess");
        }

        @Override
        public void onTimeout() throws RemoteException {
            msg = "onTimeout";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            Log.d(TAG,"onTimeout");

        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            msg = "onError";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            Log.d(TAG,"onError");

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("message"));
            logUtils.showCaseLog();
        }
    };
*/

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.ExternalSerialPortModule");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "Case execution completed");
            logUtils.showCaseLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
