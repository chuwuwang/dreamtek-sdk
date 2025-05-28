package moudles;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import Utils.LogUtils;
import base.MyApplication;

public class EppMoudle {
/*
    Context context;
    static LogUtils logUtils;
    ISerialPort iSerialport;
    IPinEpp iPinEpp;
    IPinpad iPinpad;
    IDukpt iDukpt;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> serialInit = new ArrayList<>();
    ArrayList<String> pinpadEppStep = new ArrayList<>();
    ArrayList<String> startEppVerify = new ArrayList<>();
    ArrayList<String> startPinEpp = new ArrayList<>();
    ArrayList<String> encodeData = new ArrayList<>();
    ArrayList<String> decodeData = new ArrayList<>();

    private final Object lock = new Object();
    int certExchangeResult = -100;
    byte[] pinpadCert;
    Bundle bundleCertExchange;
    boolean isCertExchangeCompleted, isResolveNsCompleted, isResolveChallengeCompleted, isVerifyNsCompleted, isGetPinCompleted;
    byte[] resolveNsResult_data;
    byte[] encryptedChallenge;
    int verifyNsCode = -100;
    byte[] pinBlock;
    static CountDownLatch countDownLatch;

    private static final String FILE_CERT_NAME = "VFI_VRK_RSA";

    public EppMoudle(Context context, IPinEpp iPinEpp, ISerialPort iSerialport, IPinpad iPinpad, IDukpt iDukpt) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iSerialport = iSerialport;
        this.iPinEpp = iPinEpp;
        this.iPinpad = iPinpad;
        this.iDukpt = iDukpt;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.EppMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "P01":
                            serialInit.add(i.getName());
                            break;
                        case "P02":
                            pinpadEppStep.add(i.getName());
                            break;
                        case "P03":
                            startEppVerify.add(i.getName());
                            break;
                        case "P04":
                            startPinEpp.add(i.getName());
                            break;
                        case "P05":
                            encodeData.add(i.getName());
                            break;
                        case "P06":
                            decodeData.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(serialInit);
            caseNames.add(pinpadEppStep);
            caseNames.add(startEppVerify);
            caseNames.add(startPinEpp);
            caseNames.add(encodeData);
            caseNames.add(decodeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void My01serialInit() {
    }

    public void My02pinpadEppStep() {
    }

    public void My03startEppVerify(Bundle bundleExtra, IEppHandler iEppHandler) {
        try {
            iPinEpp.startEppVerify(bundleExtra, iEppHandler);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My04startPinEpp(Bundle bundleExtra, IPinekHandler iPinekHandler) {
        try {
            iPinEpp.startPinEpp(bundleExtra, iPinekHandler);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void My05encodeData(byte[] data) {
    }

    public void My06decodeData(byte[] data) {

    }

    public void P01001() {
        //Pinpad 小黑盒rs232串口 open & init
        MyApplication.serviceMoudle.getPortBtMoudle().G01015();
        MyApplication.serviceMoudle.getPortBtMoudle().G03001();
    }

    public void P01002() {
        logUtils.addCaseLog("DX30 rs232");
        //DX30 rs232 串口初始化   open & init
        MyApplication.serviceMoudle.getPortBtMoudle().G01006();

        MyApplication.serviceMoudle.getPortBtMoudle().G03001();
    }

    public void P01003() {
        logUtils.addCaseLog("DX16 rs232");
        Log.d(TAG, "DX16 rs232");
        //DX16 rs232 串口初始化  open & init
        MyApplication.serviceMoudle.getPortBtMoudle().G01009();
        MyApplication.serviceMoudle.getPortBtMoudle().G03001();
    }

    public void P01004() {
        logUtils.addCaseLog("清空串口缓存");
        MyApplication.serviceMoudle.getPortBtMoudle().G06005();
    }

    public void P02001() {
        //Epp 校验 初始代码
        logUtils.addCaseLog("epp校验");
        pinBlock = null;

        //-----------------------------------------cert exchange---------------------------------
        Log.d(TAG, "step1:Pinpad certExchange");
        //执行过程：Pinpad接收证书+Pinpad做证书校验+如果校验成功，获取Pinpad证书发送给X990
        final byte[] readCert = new byte[979];
        int readBytes;
        isCertExchangeCompleted = false;

        readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(readCert, 979, 60000);
        Log.d(TAG, "Pinpad 获取X990的证书，read()返回值:[" + readBytes + "] \n获取的X990证书：" + readCert);

        // 启动新的线程来执行 certExchange
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        bundleCertExchange = iPinEpp.certExchange((byte) 0x00, (byte) 0x00, readCert, FILE_CERT_NAME);
                        isCertExchangeCompleted = true;
                        lock.notify();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // 主线程等待 certExchange 完成
        synchronized (lock) {
            while (!isCertExchangeCompleted) {
                try {
                    Log.d(TAG, "Pinpad 等待 certExchange 结果...");
                    lock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "线程中断异常：" + e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        }
        certExchangeResult = bundleCertExchange.getInt("result");
        pinpadCert = bundleCertExchange.getByteArray("cert");
        Log.d(TAG, "Pinpad certExchange result =" + certExchangeResult);
        Log.d(TAG, "Pinpad certExchange result = " + certExchangeResult);
        if (certExchangeResult == 0) {
            //pinpad校验成功后，发送证书给X990
            byte[] myCert = pinpadCert;
            Log.d(TAG, "Pinpad get cert length: " + myCert.length);
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(myCert, 3000);
            Log.d(TAG, "Pinpad writeCert()返回值:[" + writtenBytes + "]");
            logUtils.addCaseLog("certExchange success, result = " + certExchangeResult);
        } else {
            Log.e(TAG, "Pinpad certExchange failed, exit");
            logUtils.addCaseLog("Pinpad certExchange failed, result = " + certExchangeResult + ", exit");
            return;
        }

        //-----------------------------------------resolve Ns---------------------------------

        //执行过程：Pinpad获取密文+Pinpad与安全芯片交换随机数+如果交换成功，返回密文并发送给X990
        Log.d(TAG, "step2:Pinpad resolveNs");

        final byte[] readEncryptData = new byte[256];
        int readBytes1;
        isResolveNsCompleted = false;

        readBytes1 = MyApplication.serviceMoudle.getPortBtMoudle().read(readEncryptData, 256, 5000);
        Log.d(TAG, "Pinpad 获取X990接收的initialNs返回的密文，read()返回值:[" + readBytes1 + "]");
        Log.d(TAG, "Pinpad readEncryptData = " + StringUtil.byte2HexStr(readEncryptData));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        resolveNsResult_data = iPinEpp.resolveNs(FILE_CERT_NAME, (byte) 0x00, readEncryptData);
                        isResolveNsCompleted = true;
                        lock.notify();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        synchronized (lock) {
            while (!isResolveNsCompleted) {
                try {
                    Log.d(TAG, "等待 resolveNs() 结果...");
                    lock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "resolveNs 线程中断异常：" + e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        }

        Log.d(TAG, "Pinpad resolveNs() result：" + StringUtil.byte2HexStr(resolveNsResult_data) + "\n resolveNsResult_data length = " + resolveNsResult_data.length);
        if (resolveNsResult_data != null) {
            //获取到结果后，发送resolveNsResult给X990
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(resolveNsResult_data, 5000);
            Log.d(TAG, "resolveNs write()返回值:[" + writtenBytes + "]");
            logUtils.addCaseLog("Pinpad resolveNsResult length = " + resolveNsResult_data.length);
        } else {
            logUtils.addCaseLog("resolveNsResult is empty,exit");
            Log.e(TAG, "resolveNsResult is empty,exit");
            return;
        }

        //-----------------------------------------verify Ns---------------------------------

        Log.d(TAG, "step3: Pinpad verifyNs");
        //执行过程：Pinpad获取Rb+校验随机数，校验通过后在本地生成 PINKEY和 PAK。返回响应码
        final byte[] rb = new byte[16];
        int readBytes2;
        isVerifyNsCompleted = false;

        readBytes2 = MyApplication.serviceMoudle.getPortBtMoudle().read(rb, 16, 5000);
        Log.d(TAG, "Pinpad read finalizeNs result from X990, read()返回值:[" + readBytes2 + "]");
        Log.d(TAG, "finalizeNs result data: " + StringUtil.byte2HexStr(rb));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        verifyNsCode = iPinEpp.verifyNs(rb);
                        isVerifyNsCompleted = true;
                        lock.notify();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        synchronized (lock) {
            while (!isVerifyNsCompleted) {
                try {
                    Log.d(TAG, "Pinpad 等待 verifyNs 结果...");
                    lock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "verifyNs 线程中断异常：" + e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (verifyNsCode != 0) {
            Log.e(TAG, "verifyNs fail, result：" + verifyNsCode);
            logUtils.addCaseLog("verifyNs fail, result：" + verifyNsCode);
            return;
        } else {
            Log.d(TAG, "verifyNs success, result:" + verifyNsCode);
            logUtils.addCaseLog("verifyNs success, result:" + verifyNsCode);
            byte[] verifyNsWriteCode = new byte[1];
            verifyNsWriteCode[0] = (byte) verifyNsCode;
            Log.d(TAG, "verifyNsWriteCode = " + StringUtil.byte2HexStr(verifyNsWriteCode));
            int writtenByteVerifyNsCode = MyApplication.serviceMoudle.getPortBtMoudle().write(verifyNsWriteCode, 5000);
            Log.d(TAG, "verifyNsWriteCode write返回值:[" + writtenByteVerifyNsCode + "]");
        }


        //-----------------------------------------resolveChallenge--------------------------------
        Log.d(TAG, "step4: Pinpad resolveChallenge");
        //执行过程：Pinpad接收challengeKey
        final byte[] readChallengeKey = new byte[25];
        int readChallengeBytes;
        isResolveChallengeCompleted = false;

        readChallengeBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(readChallengeKey, 25, 5000);
        logUtils.addCaseLog("readChallengeBytes=" + readChallengeBytes + "]");
        byte[][] keys = parseKeys(readChallengeKey);
        final byte[] challengeKey = keys[0];
        final byte[] workingKey = keys[1];
        logUtils.addCaseLog("challengeKey = " + StringUtil.byte2HexStr(challengeKey));
        logUtils.addCaseLog("workingKey = " + StringUtil.byte2HexStr(workingKey));

        // 启动新的线程来执行 resolveChallenge
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        encryptedChallenge = iPinEpp.resolveChallenge(challengeKey, workingKey);
                        isResolveChallengeCompleted = true;
                        lock.notify();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // 主线程等待 resolveChallenge 完成
        synchronized (lock) {
            while (!isResolveChallengeCompleted) {
                try {
                    logUtils.addCaseLog("等待 resolveChallenge 结果...");
                    lock.wait();
                } catch (InterruptedException e) {
                    logUtils.addCaseLog("线程中断异常：" + e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (encryptedChallenge.length > 0) {
            //发送encryptedChallenge给X990
            logUtils.addCaseLog("encryptedChallenge length: " + encryptedChallenge.length);
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(encryptedChallenge, 5000);
            logUtils.addCaseLog("write()返回值:[" + writtenBytes + "]");
        } else {
            logUtils.addCaseLog("resolveChallenge失败，退出");
            return;
        }
    }

    public void P02002() {
        logUtils.addCaseLog("getPin");
        //-----------------------------------------getPin--------------------------------
        final byte[] spinek = new byte[16];
        int readBytes4;
        isGetPinCompleted = false;
        Log.d(TAG, "getPin() before----- spinek = " + StringUtil.byte2HexStr(spinek));
        readBytes4 = MyApplication.serviceMoudle.getPortBtMoudle().read(spinek, 16, 5000);
        logUtils.addCaseLog("read spinek:" + readBytes4);
        Log.d(TAG, "spinek = " + StringUtil.byte2HexStr(spinek));

        //startPin input
        Log.d(TAG, "开始输密码");
        logUtils.addCaseLog("开始输密码");
        countDownLatch = new CountDownLatch(1);
        MyApplication.serviceMoudle.getPinpadMoudle().L08094();
//        MyApplication.serviceMoudle.getPinpadMoudle().L24002();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "输密码结束");
        logUtils.addCaseLog("输密码结束");

        try {
            pinBlock = iPinEpp.getPin(spinek);
            Log.d(TAG, "getPin() pinBlock = " + StringUtil.byte2HexStr(pinBlock));
            logUtils.addCaseLog("执行getPinBlock = " + StringUtil.byte2HexStr(pinBlock));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        if (pinBlock.length <= 0) {
            logUtils.addCaseLog("获取pinBlock失败，退出");
            return;
        } else {
            logUtils.addCaseLog("获取pinBlock成功");
            Log.d(TAG, "获取pinBlock成功，pinBlock = " + StringUtil.byte2HexStr(pinBlock));
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(pinBlock, 5000);
            Log.d(TAG, "write pinblock()返回值:[" + writtenBytes + "]");
            pinBlock = null;
            Log.d(TAG, "清空pinBlock, pinBlock = " + StringUtil.byte2HexStr(pinBlock));
            logUtils.addCaseLog("交换pin成功，请在X990检查pinBlock");
        }
    }

    public static byte[] combineKeys(byte[] challengeKey, byte[] workingKey) {
        // 将 byte[] 转换为 String
        String challengeKeyStr = new String(challengeKey, StandardCharsets.ISO_8859_1);
        String workingKeyStr = new String(workingKey, StandardCharsets.ISO_8859_1);

        // 使用逗号作为分隔符将两个字符串连接
        String combinedStr = challengeKeyStr + "," + workingKeyStr;

        // 将组合后的字符串转换为 byte[]
        return combinedStr.getBytes(StandardCharsets.ISO_8859_1);
    }

    public static byte[][] parseKeys(byte[] combinedData) {
        // 将 byte[] 转换为 String
        String combinedStr = new String(combinedData, StandardCharsets.ISO_8859_1);

        // 使用逗号分隔符拆分字符串
        String[] keysStr = combinedStr.split(",", 2);

        // 将拆分后的字符串转换回 byte[]
        byte[] challengeKey = keysStr[0].getBytes(StandardCharsets.ISO_8859_1);
        byte[] workingKey = keysStr[1].getBytes(StandardCharsets.ISO_8859_1);

        return new byte[][]{challengeKey, workingKey};
    }

    public void P03001() {
//        MyApplication.serviceMoudle.getPortBtMoudle().G06005();//清空串口缓存
//        MyApplication.serviceMoudle.getPinpadMoudle().L05054();//990下载pinkey
        logUtils.addCaseLog("startEppVerify:");
        Bundle bundleExtra = new Bundle();
        bundleExtra.putByte("algorithmMode", (byte) 0x00);
        bundleExtra.putString("alias", "VFI_PAIRING_RSA");
        My03startEppVerify(bundleExtra, new MyEppHandler());
    }

    public void P04001() {
        logUtils.addCaseLog("startPinEpp:");
        Bundle bundleExtra = new Bundle();
        bundleExtra.putByte("algorithmMode", (byte) 0x00);
        bundleExtra.putBoolean("isOnline", true);
        My04startPinEpp(bundleExtra, new MyPinekHandler());
    }

    public void P06001() {
        String data = "1234567890";

        try {
            byte[] encodeData = iPinEpp.encodeData(data.getBytes(StandardCharsets.ISO_8859_1));
            logUtils.addCaseLog("data = " + data + "; encodeData = " + StringUtil.byte2HexStr(encodeData));

            try {
                byte[] decodeData = iPinEpp.decodeData(encodeData);
                logUtils.addCaseLog("; decodeData = " + StringUtil.byte2HexStr(decodeData));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void P06002() {
        String data = "12345";

        try {
            byte[] encodeData = iPinEpp.encodeData(data.getBytes(StandardCharsets.ISO_8859_1));
            logUtils.addCaseLog("data = " + data + "; encodeData = " + StringUtil.byte2HexStr(encodeData));

            try {
                byte[] decodeData = iPinEpp.decodeData(encodeData);
                logUtils.addCaseLog("; decodeData = " + StringUtil.byte2HexStr(decodeData));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void P06003() {
        String data = "12345678";

        try {
            byte[] encodeData = iPinEpp.encodeData(data.getBytes(StandardCharsets.ISO_8859_1));
            logUtils.addCaseLog("data = " + data + "; encodeData = " + StringUtil.byte2HexStr(encodeData));

            try {
                byte[] decodeData = iPinEpp.decodeData(encodeData);
                logUtils.addCaseLog("; decodeData = " + StringUtil.byte2HexStr(decodeData));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void P06004() {
        String data = "";

        try {
            byte[] encodeData = iPinEpp.encodeData(data.getBytes(StandardCharsets.ISO_8859_1));
            logUtils.addCaseLog("data = " + data + "; encodeData = " + StringUtil.byte2HexStr(encodeData));

            try {
                byte[] decodeData = iPinEpp.decodeData(encodeData);
                logUtils.addCaseLog("; decodeData = " + StringUtil.byte2HexStr(decodeData));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void P06005() {
        String data = "12345678901234567890";

        try {
            byte[] encodeData = iPinEpp.encodeData(data.getBytes(StandardCharsets.ISO_8859_1));
            logUtils.addCaseLog("data = " + data + "; encodeData = " + StringUtil.byte2HexStr(encodeData));

            try {
                byte[] decodeData = iPinEpp.decodeData(encodeData);
                logUtils.addCaseLog("; decodeData = " + StringUtil.byte2HexStr(decodeData));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getNonNullString(String value, int fixLen, boolean isLeftPadding) {
        if (value == null) {
            value = "";
        }

        int valueLen = value.length();
        if (valueLen < fixLen) {
            fixLen = fixLen - value.length();
            String format = "%0" + fixLen + "d";
            if (isLeftPadding) {
                return String.format(format, 0) + value;
            } else {
                return value + String.format(format, 0);
            }
        } else if (valueLen > fixLen) {
            if (isLeftPadding) {
                return value.substring(valueLen - fixLen, valueLen);
            } else {
                return value.substring(0, fixLen);
            }
        } else {
            return value;
        }
    }

//    private class MyEppHandler2 extends IEppHandler.Stub {
//        // TODO: 2024/11/6 修改前的代码
//        String msg;
//
//        public MyEppHandler2() {
//        }
//
//        @Override
//        public void getCertification(byte[] cert) throws RemoteException {
//            msg = "getCertification callback, x990 cert length = "+cert.length;
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            Log.d(TAG, "getCertification() callback, X990 cert = " + StringUtil.byte2HexStr(cert));
//            logUtils.addCaseLog("getCertification() callback, cert length: " + cert.length);
//            Log.d(TAG,"获取X990 cert = "+StringUtil.byte2HexStr(cert));
//            int writtenBytes;
//            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(cert, 5000);
//            logUtils.addCaseLog("write x990 cert返回值:[" + writtenBytes + "]");
//
//            final byte[] readPinpadCert = new byte[979];
//            int readBytes;
//
//            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(readPinpadCert, 979, 60000);
//            logUtils.addCaseLog("read pinpadCert 返回值:[" + readBytes + "]");
//            Log.d(TAG,"read pinpad cert = "+StringUtil.byte2HexStr(readPinpadCert));
//            iPinEpp.importPinpadCert(readPinpadCert);
//
//
//        }
//
//        @Override
//        public void getInitialNsResult(byte[] initialNsData) throws RemoteException {
//            msg = "getInitialNsResult() callback, initialNsData length = " + initialNsData.length;
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            Log.d(TAG, "getInitialNsResult() callback, initialNsData = " + StringUtil.byte2HexStr(initialNsData));
//            logUtils.addCaseLog("initialNs length = " + initialNsData.length);
//            int writtenBytes;
//            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(initialNsData, 5000);
//            logUtils.addCaseLog("initialNsData write()返回值:[" + writtenBytes + "]");
//
//            final byte[] resolveNsData = new byte[256];//等待从pinpad获取的结果
//            int readBytes;
//
//            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(resolveNsData, 256, 5000);
//            logUtils.addCaseLog("resolveNsData read()返回值:[" + readBytes + "]");
//            iPinEpp.importResolveNsData(resolveNsData);
//        }
//
//        @Override
//        public void getFinalizeNsResult(byte[] finalizeNsData) throws RemoteException {
//            msg = "getFinalizeNsRb() callback, finalizeNsData length = " + finalizeNsData.length;
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            Log.d(TAG, "getFinalizeNsRb() callback, finalizeNsData = " + StringUtil.byte2HexStr(finalizeNsData));
//            Log.d(TAG,"finalizeNsData length = " + finalizeNsData.length);
//            int writtenBytes;
//            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(finalizeNsData, 5000);
//            Log.d(TAG,"finalizeNsData write()返回值:[" + writtenBytes + "]");
//
//            final byte[] verifyNsResult = new byte[1];//等待从pinpad获取的结果verifyNsResult
//            int readBytes;
//
//            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(verifyNsResult, 1, 5000);
//            Log.d(TAG,"verifyNsResult read()返回值:[" + readBytes + "]");
//            Log.d(TAG,"X990 read verifyNs result = "+verifyNsResult[0]);
//            iPinEpp.importVerifyNsResult(verifyNsResult[0]);
//        }
//
//        @Override
//        public void getChallenge(Bundle ChallengeKey) throws RemoteException {
//            msg = "getChallenge() callback";
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            Log.d(TAG, "getChallenge() callback");
//            if (ChallengeKey != null) {
//                byte[] challengeKey = ChallengeKey.getByteArray("challengeKey");
//                byte[] workingKey = ChallengeKey.getByteArray("workingKey");
//
//                if (challengeKey != null && workingKey != null) {
//                    Log.d(TAG, "challengeKey = " + StringUtil.byte2HexStr(challengeKey) + "; \n workingKey = " + StringUtil.byte2HexStr(workingKey));
//                    logUtils.addCaseLog("challengeKey size = " + challengeKey.length + "; workingkey size = " + workingKey.length);
//                    byte[] combinedData = combineKeys(challengeKey, workingKey);
//                    int writtenBytes;
//                    writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(combinedData, 5000);
//                    Log.d(TAG,"write combinedData" + writtenBytes + "]");
//                    Log.d(TAG,"开始read encryptedChallenge");
//                    final byte[] encryptedChallenge = new byte[8];//等待从pinpad获取的结果
//                    int readBytes;
//
//                    readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(encryptedChallenge, 8, 3000);
//                    logUtils.addCaseLog("read encryptedChallenge:[" + readBytes + "]");
//                    Log.d(TAG,"read encryptedChallenge = "+StringUtil.byte2HexStr(encryptedChallenge));
//                    iPinEpp.importEncryptedChallenge(encryptedChallenge);
//                } else {
//                    logUtils.addCaseLog("challengeKey || workingKey is null");
//                }
//
//            } else {
//                logUtils.addCaseLog("ChallengeKey is null");
//            }
//
//        }
//
//        @Override
//        public void getSpinek(byte[] encipher) throws RemoteException {
//            msg = "getSpinek() callback, encipher size= "+encipher.length;
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            Log.d(TAG, "getSpinek() callback, encipher = " + StringUtil.byte2HexStr(encipher));
//            logUtils.addCaseLog("encipher length: " + encipher.length);
//            int writtenBytes;
//            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(encipher, 5000);
//            logUtils.addCaseLog("write()返回值:[" + writtenBytes + "]");
//
//            final byte[] readPinBlock = new byte[8];
//            int readBytes;
//
//            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(readPinBlock, 8, 60000);
//            Log.d(TAG,"read pinblock = "+StringUtil.byte2HexStr(readPinBlock)+"\n len = "+readBytes);
//            logUtils.addCaseLog("read pibBlock:[" + StringUtil.byte2HexStr(readPinBlock) + "]");
//
//            Bundle bundle = new Bundle();
//            bundle.putByteArray("pan","6226901508781352".getBytes());
//            bundle.putByteArray("encipheredPIN",readPinBlock);
//            bundle.putInt("formatType",2);
//            bundle.putInt("pinKeyId",99);
//
//            iPinEpp.importEncipheredPinBundle(bundle);
//        }
//
//        @Override
//        public void onResult(int result,String info) throws RemoteException {
//            msg = "onResultcallback, result = "+result+"; info = "+info;
//            Message message = new Message();
//            message.getData().putString("message", msg);
//            handler.sendMessage(message);
//
//            logUtils.addCaseLog("onResult() callback, result = " + result+"; info = "+info);
//            Log.d(TAG, "onResult() callback, result = " + result+"; info = "+info);
//        }
//    }

    private class MyPinekHandler extends IPinekHandler.Stub {

        String msg;

        public MyPinekHandler() {
        }

        @Override
        public void getSpinek(byte[] encipher) throws RemoteException {
            msg = "getSpinek() callback, encipher size= " + encipher.length;
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            Log.d(TAG, "getSpinek() callback, encipher = " + StringUtil.byte2HexStr(encipher));
            logUtils.addCaseLog("encipher length: " + encipher.length);
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(encipher, 5000);
            logUtils.addCaseLog("write()返回值:[" + writtenBytes + "]");

            final byte[] readPinBlock = new byte[8];
            int readBytes;

            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(readPinBlock, 8, 60000);
            Log.d(TAG, "read pinblock = " + StringUtil.byte2HexStr(readPinBlock) + "\n len = " + readBytes);
            logUtils.addCaseLog("read pibBlock:[" + StringUtil.byte2HexStr(readPinBlock) + "]");

            Bundle bundle = new Bundle();
            bundle.putByteArray("pan", "6226901508781352".getBytes());
            bundle.putByteArray("encipheredPIN", readPinBlock);
            bundle.putInt("formatType", 2);
            bundle.putInt("pinKeyId", 99);

            iPinEpp.importEncipheredPinBundle(bundle);
        }

        @Override
        public void onResult(int result, String info) throws RemoteException {
            msg = "PinEpp onResult callback, result = " + result + "; info = " + info;
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);
            logUtils.addCaseLog("onResult() callback, result = " + result + "; info = " + info);
            if (result == 0) {
                Log.d(TAG, "success, pinepp ending -------> info = " + StringUtil.byte2HexStr(info.getBytes()));
            } else {
                Log.d(TAG, "onResult() callback, result = " + result + "; info = " + info);
            }
        }
    }

    private class MyEppHandler extends IEppHandler.Stub {
        String msg;

        public MyEppHandler() {
        }

        @Override
        public void getCertification(byte[] cert) throws RemoteException {
            msg = "getCertification callback, x990 cert length = " + cert.length;
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            Log.d(TAG, "getCertification() callback, X990 cert = " + StringUtil.byte2HexStr(cert));
            logUtils.addCaseLog("getCertification() callback, cert length: " + cert.length);
            Log.d(TAG, "获取X990 cert = " + StringUtil.byte2HexStr(cert));
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(cert, 5000);
            logUtils.addCaseLog("write x990 cert返回值:[" + writtenBytes + "]");

            final byte[] readPinpadCert = new byte[979];
            int readBytes;

            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(readPinpadCert, 979, 60000);
            logUtils.addCaseLog("read pinpadCert 返回值:[" + readBytes + "]");
            Log.d(TAG, "read pinpad cert = " + StringUtil.byte2HexStr(readPinpadCert));
            iPinEpp.importPinpadCert(readPinpadCert);


        }

        @Override
        public void getInitialNsResult(byte[] initialNsData) throws RemoteException {
            msg = "getInitialNsResult() callback, initialNsData length = " + initialNsData.length;
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            Log.d(TAG, "getInitialNsResult() callback, initialNsData = " + StringUtil.byte2HexStr(initialNsData));
            logUtils.addCaseLog("initialNs length = " + initialNsData.length);
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(initialNsData, 5000);
            logUtils.addCaseLog("initialNsData write()返回值:[" + writtenBytes + "]");

            final byte[] resolveNsData = new byte[256];//等待从pinpad获取的结果
            int readBytes;

            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(resolveNsData, 256, 5000);
            logUtils.addCaseLog("resolveNsData read()返回值:[" + readBytes + "]");
            iPinEpp.importResolveNsData(resolveNsData);
        }

        @Override
        public void getFinalizeNsResult(byte[] finalizeNsData) throws RemoteException {
            msg = "getFinalizeNsRb() callback, finalizeNsData length = " + finalizeNsData.length;
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            Log.d(TAG, "getFinalizeNsRb() callback, finalizeNsData = " + StringUtil.byte2HexStr(finalizeNsData));
            Log.d(TAG, "finalizeNsData length = " + finalizeNsData.length);
            int writtenBytes;
            writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(finalizeNsData, 5000);
            Log.d(TAG, "finalizeNsData write()返回值:[" + writtenBytes + "]");

            final byte[] verifyNsResult = new byte[1];//等待从pinpad获取的结果verifyNsResult
            int readBytes;

            readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(verifyNsResult, 1, 5000);
            Log.d(TAG, "verifyNsResult read()返回值:[" + readBytes + "]");
            Log.d(TAG, "X990 read verifyNs result = " + verifyNsResult[0]);
            iPinEpp.importVerifyNsResult(verifyNsResult[0]);
        }

        @Override
        public void getChallenge(Bundle ChallengeKey) throws RemoteException {
            msg = "getChallenge() callback";
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            Log.d(TAG, "getChallenge() callback");
            if (ChallengeKey != null) {
                byte[] challengeKey = ChallengeKey.getByteArray("challengeKey");
                byte[] workingKey = ChallengeKey.getByteArray("workingKey");

                if (challengeKey != null && workingKey != null) {
                    Log.d(TAG, "challengeKey = " + StringUtil.byte2HexStr(challengeKey) + "; \n workingKey = " + StringUtil.byte2HexStr(workingKey));
                    logUtils.addCaseLog("challengeKey size = " + challengeKey.length + "; workingkey size = " + workingKey.length);
                    byte[] combinedData = combineKeys(challengeKey, workingKey);
                    int writtenBytes;
                    writtenBytes = MyApplication.serviceMoudle.getPortBtMoudle().write(combinedData, 5000);
                    Log.d(TAG, "write combinedData" + writtenBytes + "]");
                    Log.d(TAG, "开始read encryptedChallenge");
                    final byte[] encryptedChallenge = new byte[8];//等待从pinpad获取的结果
                    int readBytes;

                    readBytes = MyApplication.serviceMoudle.getPortBtMoudle().read(encryptedChallenge, 8, 3000);
                    logUtils.addCaseLog("read encryptedChallenge:[" + readBytes + "]");
                    Log.d(TAG, "read encryptedChallenge = " + StringUtil.byte2HexStr(encryptedChallenge));
                    iPinEpp.importEncryptedChallenge(encryptedChallenge);
                } else {
                    logUtils.addCaseLog("challengeKey || workingKey is null");
                }

            } else {
                logUtils.addCaseLog("ChallengeKey is null");
            }

        }

        @Override
        public void onResult(int result, String info) throws RemoteException {
            msg = "PinEppVerify onResult callback, result = " + result + "; info = " + info;
            Message message = new Message();
            message.getData().putString("message", msg);
            handler.sendMessage(message);

            logUtils.addCaseLog("onResult() callback, result = " + result + "; info = " + info);
            Log.d(TAG, "onResult() callback, result = " + result + "; info = " + info);
        }
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.EppMoudle");
            Log.i("aClass.getMethod", name);
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "案例执行完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    private void printMsgTool(String msg) {
        if (null == msg || "null".equals(msg) || msg.contains("null")) {
            msg = "The execution result：";
        }
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
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
}
