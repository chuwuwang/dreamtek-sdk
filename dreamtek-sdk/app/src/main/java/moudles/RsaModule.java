package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;
import com.verifone.smartpos.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.ArrayList;

import Utils.LogUtil;
import Utils.LogUtils;
import base.MyApplication;

public class RsaModule {
    public static final String TAG = "RsaModule";
    static LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> generateRSAKeyPair = new ArrayList<String>();
    ArrayList<String> RSAEncryption = new ArrayList<String>();
    ArrayList<String> RSADecryption = new ArrayList<String>();
    ArrayList<String> deleteRSAKey = new ArrayList<String>();
    ArrayList<String> getPublicKey = new ArrayList<String>();
    ArrayList<String> savePublicKey = new ArrayList<String>();
    ArrayList<String> savePrivateKey = new ArrayList<String>();
    ArrayList<String> RSASign = new ArrayList<String>();
    ArrayList<String> RSAVerify = new ArrayList<String>();
    ArrayList<String> isKeyExist = new ArrayList<String>();
    ArrayList<String> saveCertificate = new ArrayList<String>();

    private IRSA irsa;
    private static RsaModule mRsaModule = null;
    private Context mContext;
    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public RsaModule(Context context, IRSA irsa) {
        this.irsa = irsa;
        logUtils = MyApplication.serviceMoudle.logUtils;
        addAllapi();
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        Log.d(TAG, "runTheMethod name:" + name);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.RsaModule");
            Method method = aClass.getDeclaredMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "Case execution completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "runTheMethod exception:" + e);
        }
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.RsaModule");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "R01":
                            generateRSAKeyPair.add(i.getName());
                            break;
                        case "R02":
                            RSAEncryption.add(i.getName());
                            break;
                        case "R03":
                            RSADecryption.add(i.getName());
                            break;
                        case "R04":
                            deleteRSAKey.add(i.getName());
                            break;
                        case "R05":
                            getPublicKey.add(i.getName());
                            break;
                        case "R06":
                            savePublicKey.add(i.getName());
                            break;
                        case "R07":
                            savePrivateKey.add(i.getName());
                            break;
                        case "R08":
                            RSASign.add(i.getName());
                            break;
                        case "R09":
                            RSAVerify.add(i.getName());
                            break;
                        case "R10":
                            isKeyExist.add(i.getName());
                            break;
                        case "R11":
                            saveCertificate.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(generateRSAKeyPair);
            caseNames.add(RSAEncryption);
            caseNames.add(RSADecryption);
            caseNames.add(deleteRSAKey);
            caseNames.add(getPublicKey);

            caseNames.add(savePublicKey);
            caseNames.add(savePrivateKey);
            caseNames.add(RSASign);
            caseNames.add(RSAVerify);
            caseNames.add(isKeyExist);
            caseNames.add(saveCertificate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bundle My01generateRSAKeyPair(Bundle params) {
        try {
            return irsa.generateRSAKeyPair(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle My02RSAEncryption(Bundle params) {
        try {
            return irsa.RSAEncryption(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle My03RSADecryption(Bundle params) {
        try {
            return irsa.RSADecryption(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean My04deleteRSAKey(Bundle params) {
        try {
            return irsa.deleteRSAKey(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle My05getPublicKey(Bundle params) {
        try {
            return irsa.getPublicKey(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean My06savePublicKey(Bundle params) {
        try {
            return irsa.savePublicKey(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean My07savePrivateKey(Bundle params) {
        try {
            return irsa.savePrivateKey(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle My08RSASign(Bundle params) {
        try {
            return irsa.RSASign(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle My09RSAVerify(Bundle params) {
        try {
            return irsa.RSAVerify(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean My10isKeyExist(Bundle params) {
        try {
            return irsa.isKeyExist(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean My11saveCertificate(Bundle params) {
        try {
            return irsa.saveCertificate(params);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void R01001() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PEM;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01002() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PEM;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01003() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_DER;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01004() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_DER;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01005() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PRIVATE;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01006() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PRIVATE;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01007() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
//        int format = -1;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
//        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);
        byte[] publicKey = result.getByteArray("publicKey");
        LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
//            publicKeyFinal = StringUtil.byte2HexStr(publicKey);

        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + StringUtil.byte2HexStr(publicKey) + "]");
    }

    public void R01008() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PEM;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
//        bundle.putInt("keyLength", 2048);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }

    public void R01009() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PEM;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 4096);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My01generateRSAKeyPair(bundle);

        String publicKeyFinal = "";
        if (format == FORMAT_DER) {
            byte[] publicKey = result.getByteArray("publicKey");
            LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
            publicKeyFinal = StringUtil.byte2HexStr(publicKey);
        } else {
            String publicKeyStr = result.getString("publicKey");
            LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
            publicKeyFinal = publicKeyStr;
        }
        boolean ret = result.getBoolean("isSuccess");

        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("publicKey=[" + publicKeyFinal + "]");
    }


    public void R02001() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
        bundle.putString("paddingType", "PKCS1Padding");
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        logUtils.addCaseLog("ret=" + ret);
//        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(result.getByteArray("encryptedData")) + "]");
        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");

    }

    public void R02002() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
        bundle.putString("paddingType", "NoPadding");
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        logUtils.addCaseLog("ret=" + ret);
//        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(result.getByteArray("encryptedData")) + "]");
        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");

    }

    public void R02003() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
        bundle.putString("paddingType", "OAEPPadding");
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        logUtils.addCaseLog("ret=" + ret);
//        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(result.getByteArray("encryptedData")) + "]");
        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");

    }

    public void R02004() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
        bundle.putString("paddingType", "");
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        logUtils.addCaseLog("ret=" + ret);
//        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(result.getByteArray("encryptedData")) + "]");
        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");

    }

    public void R02005() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
//        bundle.putString("paddingType", "PKCS1Padding");
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        logUtils.addCaseLog("ret=" + ret);
//        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(result.getByteArray("encryptedData")) + "]");
        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");

    }

    public void R02006() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
        bundle.putString("paddingType", null);
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        logUtils.addCaseLog("ret=" + ret);
//        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(result.getByteArray("encryptedData")) + "]");
        logUtils.addCaseLog("encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");

    }



    /**

     * @return
     */
    private byte[] getEncryptData() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putByteArray("data", "111111".getBytes());
//        bundle.putString("paddingType", "PKCS1Padding");
        Bundle result = My02RSAEncryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        mEncryptedData = null;
        mEncryptedData= result.getByteArray("encryptedData");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "encryptData=[" + StringUtil.byte2HexStr(mEncryptedData) + "]");
        return mEncryptedData;
    }
    private byte[] mEncryptedData;
    public void R03001() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        byte[] r02001New = getEncryptData();
        bundle.putByteArray("encryptedData", r02001New);
        bundle.putString("paddingType", "PKCS1Padding");
        Log.d(TAG, "encryptData2=[" + StringUtil.byte2HexStr(r02001New) + "]");
        Bundle result = My03RSADecryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
//        Log.d(TAG, "decryptData11=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
    }

    public void R03002() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        byte[] r02001New = getEncryptData();
        bundle.putByteArray("encryptedData", r02001New);
        bundle.putString("paddingType", "NoPadding");
        Log.d(TAG, "encryptData2=[" + StringUtil.byte2HexStr(r02001New) + "]");
        Bundle result = My03RSADecryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("decryptData=[" + StringUtil.byte2HexStr(result.getByteArray("data")) + "]");
//        Log.d(TAG, "decryptData11=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "decryptData=[" + StringUtil.byte2HexStr(result.getByteArray("data")) + "]");
    }

    public void R03003() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        byte[] r02001New = getEncryptData();
        bundle.putByteArray("encryptedData", r02001New);
        bundle.putString("paddingType", "OAEPPadding");
        Log.d(TAG, "encryptData2=[" + StringUtil.byte2HexStr(r02001New) + "]");
        Bundle result = My03RSADecryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
//        Log.d(TAG, "decryptData11=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
    }

    public void R03004() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        byte[] r02001New = getEncryptData();
        bundle.putByteArray("encryptedData", r02001New);
        bundle.putString("paddingType", "");
        Log.d(TAG, "encryptData2=[" + StringUtil.byte2HexStr(r02001New) + "]");
        Bundle result = My03RSADecryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
//        Log.d(TAG, "decryptData11=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
    }

    public void R03005() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        byte[] r02001New = getEncryptData();
        bundle.putByteArray("encryptedData", r02001New);
//        bundle.putString("paddingType", "PKCS1Padding");
        Log.d(TAG, "encryptData2=[" + StringUtil.byte2HexStr(r02001New) + "]");
        Bundle result = My03RSADecryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
//        Log.d(TAG, "decryptData11=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
    }

    public void R03006() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        byte[] r02001New = getEncryptData();
        bundle.putByteArray("encryptedData", r02001New);
        bundle.putString("paddingType", null);
        Log.d(TAG, "encryptData2=[" + StringUtil.byte2HexStr(r02001New) + "]");
        Bundle result = My03RSADecryption(bundle);

        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        logUtils.addCaseLog("decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
//        Log.d(TAG, "decryptData11=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
        Log.d(TAG, "ret=" + ret);
        Log.d(TAG, "decryptData=[" + StringUtil.byteToGBK(result.getByteArray("data")) + "]");
    }

    public void R04001() {
        for (int i = 0; i < 100; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("keyIndex", i);
            boolean b = My04deleteRSAKey(bundle);
            logUtils.addCaseLog("ret=" + b);
        }
    }


    public void R05001() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PEM;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My05getPublicKey(bundle);
        String publicKeyStr = result.getString("publicKey");
        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
        logUtils.addCaseLog("publicKeyStr=[" + publicKeyStr + "]");
    }

    public void R05002() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_DER;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My05getPublicKey(bundle);
        byte[] publicKey = result.getByteArray("publicKey");
        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
        logUtils.addCaseLog("publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
    }

    public void R05003() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
        int format = FORMAT_PRIVATE;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My05getPublicKey(bundle);
        String publicKeyStr = result.getString("publicKey");
        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        LogUtil.d(TAG, "publicKeyStr=[" + publicKeyStr + "]");
        logUtils.addCaseLog("publicKeyStr=[" + publicKeyStr + "]");
    }

    public void R05004() {
        int FORMAT_PEM = 0;
        int FORMAT_DER = 1;
        int FORMAT_PRIVATE = 2;
//        int format = -1;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
//        bundle.putInt("returnPublicKeyFormat", format);
        Bundle result = My05getPublicKey(bundle);
        byte[] publicKey = result.getByteArray("publicKey");
        boolean ret = result.getBoolean("isSuccess");
        logUtils.addCaseLog("ret=" + ret);
        LogUtil.d(TAG, "publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
        logUtils.addCaseLog("publicKey hex=[" + StringUtil.byte2HexStr(publicKey) + "]");
    }


    public void R06001() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("keyIndex", 1);
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            int keyLength = 1024;
            keyPairGen.initialize(keyLength);

            KeyPair keyPair = keyPairGen.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            bundle.putString("modulus", publicKey.getModulus().toString(16));
            bundle.putString("exponent", publicKey.getPublicExponent().toString(16));
            boolean b = My06savePublicKey(bundle);
            logUtils.addCaseLog("save public key isSuccess=[" + b + "]");
            LogUtil.d(TAG, "save public key isSuccess=[" + b + "]");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



    public void R07001() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            int keyLength = 1024;
            keyPairGen.initialize(keyLength);

            KeyPair keyPair = keyPairGen.generateKeyPair();

            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = keyFac.getKeySpec(keyPair.getPrivate(), RSAPrivateCrtKeySpec.class);

            Bundle bundle = new Bundle();
            bundle.putInt("keyIndex", 1);
            bundle.putString("modulus", rsaPrivateCrtKeySpec.getModulus().toString(16));
            bundle.putString("publicExponent", rsaPrivateCrtKeySpec.getPublicExponent().toString(16));
            bundle.putString("privateExponent", rsaPrivateCrtKeySpec.getPrivateExponent().toString(16));
            bundle.putString("prime1", rsaPrivateCrtKeySpec.getPrimeP().toString(16));
            bundle.putString("prime2", rsaPrivateCrtKeySpec.getPrimeQ().toString(16));
            bundle.putString("exponent1", rsaPrivateCrtKeySpec.getPrimeExponentP().toString(16));
            bundle.putString("exponent2", rsaPrivateCrtKeySpec.getPrimeExponentQ().toString(16));
            bundle.putString("coefficient", rsaPrivateCrtKeySpec.getCrtCoefficient().toString(16));
            boolean isSuccess = My07savePrivateKey(bundle);
            logUtils.addCaseLog("save private key isSuccess=[" + isSuccess + "]");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
    public void R08001() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08002() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "MD5";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08003() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08004() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA256";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08005() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA384";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08006() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA512";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }



    public void R08008() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = null;
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("hashAlgorithm=[" + hashAlgorithm + "]");
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08009() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
//        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08010() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08011() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "MD5";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08012() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08013() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA256";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08014() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA384";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08015() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA512";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }



    public void R08017() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", null);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08018() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
//        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }


    public void R08019() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 1024);
        bundle.putString("hashAlgorithm", hashAlgorithm);
//        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08020() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08021() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "MD5";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08022() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08023() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA256";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08024() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA384";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08025() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA512";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08026() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", true);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }

    public void R08027() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
//        bundle.putInt("keyLength", 2048);
        bundle.putString("hashAlgorithm", null);
        bundle.putBoolean("isHashData", false);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        logUtils.addCaseLog("service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        logUtils.addCaseLog("signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
    }





    /**

     * @return
     */
    private byte[] getSignData(String hashAlgorithm,int keyLength,boolean isHashData) {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyLength", keyLength);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putBoolean("isHashData", isHashData);
        bundle.putByteArray("data", data);
        Bundle result = My08RSASign(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);
        Log.d(TAG, "service sign isSuccess=[" + isSuccess + "]");
        byte[] signedData = result.getByteArray("signature");
        Log.d(TAG, "signedData hex=[" + StringUtil.byte2HexStr(signedData) + "]");
        return signedData;
    }
    public void R09001() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", "SHA1");
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA1", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09002() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "MD5";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("MD5", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09003() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA224";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA224", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09004() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA256";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA256", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09005() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA384";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA384", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09006() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA512";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA512", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09007() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA512";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", null);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA1", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09008() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
//        String hashAlgorithm = "SHA512";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
//        bundle.putString("hashAlgorithm", null);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA1", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09009() {
        byte[] data = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA1", 1024, false));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }

    public void R09010() {
        byte[] data = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes();
        String hashAlgorithm = "SHA1";
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putString("hashAlgorithm", hashAlgorithm);
        bundle.putByteArray("data", data);
        bundle.putByteArray("signature", getSignData("SHA1", 1024, true));
        Bundle result = My09RSAVerify(bundle);
        boolean isSuccess = result.getBoolean("isSuccess", false);

        logUtils.addCaseLog("verify isSuccess=[" + isSuccess + "]");
    }



    public void R10001() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyType", 0);

        boolean isSuccess = My10isKeyExist(bundle);
        logUtils.addCaseLog("isKeyExits=[" + isSuccess + "]");
    }

    public void R10002() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyType", 1);

        boolean isSuccess = My10isKeyExist(bundle);
        logUtils.addCaseLog("isKeyExits=[" + isSuccess + "]");
    }

    public void R10003() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
//        bundle.putInt("keyType", 0);

        boolean isSuccess = My10isKeyExist(bundle);
        logUtils.addCaseLog("isKeyExits=[" + isSuccess + "]");
    }

    public void R10004() {
        Bundle bundle = new Bundle();
        bundle.putInt("keyIndex", 1);
        bundle.putInt("keyType", -1);

        boolean isSuccess = My10isKeyExist(bundle);
        logUtils.addCaseLog("isKeyExits=[" + isSuccess + "]");
    }



    public void R11001() {

        try {
            Bundle bundle = new Bundle();
            bundle.putInt("keyIndex", 1);
            bundle.putByteArray("data", getCertificationBytes());
            boolean isSuccess = My11saveCertificate(bundle);
            logUtils.addCaseLog("saveCertificate=[" + isSuccess + "]");
        } catch (CertificateException e) {
            e.printStackTrace();
        }


    }

    private byte[] getCertificationBytes() throws CertificateException {
        String base64 = "MIIDPTCCAiUCFGyfh+8XRO4rgTum2rGTe+OpgKONMA0GCSqGSIb3DQEBCwUAMFsxCzAJBgNVBAYTAmZkMQowCAYDVQQIDAFhMQowCAYDVQQHDAFhMQowCAYDVQQKDAFhMQowCAYDVQQLDAFhMQowCAYDVQQDDAFhMRAwDgYJKoZIhvcNAQkBFgFhMB4XDTIxMDYwMzA5NTgyMVoXDTIyMDYwMzA5NTgyMVowWzELMAkGA1UEBhMCZmQxCjAIBgNVBAgMAWExCjAIBgNVBAcMAWExCjAIBgNVBAoMAWExCjAIBgNVBAsMAWExCjAIBgNVBAMMAWExEDAOBgkqhkiG9w0BCQEWAWEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDIAptuDIobPfoX3ZRfiVbNQb6cAjLyMbWkLmOsUQHZFP3oguEBeRnGjYugLCMrzyvFzZhUKugbSqkQWuEWDKKCWYQ+rlhbDmOIfbjMBVoblZilM4Uo07PozeSbVcOSG1aOuuQkFts7pYTex1GUZVw7apu1iwIzsoYzPeRKiUKEE/K6RJs//nmpCncsDWnUGQE8jSvDDAPNPLu686QcFY7p79vet1KBg3gdnjsD1WiGYH2AjCo8H1gkJc6Fjp6fwWeVY2giVRUrW1zYRzPWbCUGWwO36G6BitAfVwBJytt5R3RVdeNB8BKeOBCT5HMT8s8XeoCs7calkEB2DAYRUJhXAgMBAAEwDQYJKoZIhvcNAQELBQADggEBABs6yZG+7R/Kqw/2YeysctNYaRJsqC4nd1gdMDBfi0uzGdHd34+RUJs7hjbmPOaVtKXUUlxLe1NWTkT4I8Cm86LAlorHPMY9H/7daRplfVsDw7Vx74r1Ye5DgqvjzRtLBQ4jLgYEl3iMz2IbIVVs7zXDMfl1aOQGeac2RUzV6kpJWfhVUYefjEk0HmabDMEnG5qTJATfxpF1mv8bZcogQFSsoaeq144IbsIli/E7UXHMgSn6NfPzCbueDMCNVfxtjqRIxFZyDd+dAW4RTiTZJGOpJ1bb7X50Qy5GBgWZfebMy1oonxC+aek+bjTDCDI4WM9NqceeH4ltOidLHxaQUGI=";

        byte[] base64Bytes = Base64.decode(base64, Base64.NO_WRAP);

        X509Certificate x509Certificate = null;
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream inputStream = new ByteArrayInputStream(base64Bytes);
        x509Certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);

        return x509Certificate.getEncoded();
    }
}
