package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;

import com.verifone.smartpos.utils.StringUtil;

public class RsaModule extends TestModule {
    public String T_generateRSAKeyPair(String params) {
        try {
            logUtils.addCaseLog("generate RSAKey execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("keyLength", BundleConfig_int),
                    new BundleConfig("returnPublicKeyFormat", BundleConfig_int)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            Bundle bundle = irsa.generateRSAKeyPair(pBundle);
            if (bundle.getBoolean("isSuccess")) {
                return bundle.getString("publicKey");
            } else {
                return "false";
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("generate RSAKey abnormal");
            e.printStackTrace();
            return "generate exception";
        }
    }

    public byte[] T_RSAEncryption(String params) {
        try {
            logUtils.addCaseLog("RSA encryption execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("data", BundleConfig_byte_A),
                    new BundleConfig("paddingType", BundleConfig_String)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            Bundle bundle = irsa.RSAEncryption(pBundle);
            if (bundle.getBoolean("isSuccess")) {
                return bundle.getByteArray("encryptedData");
            } else {
                logUtils.addCaseLog("encryption failed");
                return null;
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("RSA encryption abnormal");
            e.printStackTrace();
            return null;
        }
    }

    public byte[] T_RSADecryption(String params) {
        try {
            logUtils.addCaseLog("RSA decryption execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("data", BundleConfig_byte_A),
                    new BundleConfig("paddingType", BundleConfig_String)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            Bundle bundle = irsa.RSADecryption(pBundle);
            if (bundle.getBoolean("isSuccess")) {
                return bundle.getByteArray("encryptedData");
            } else {
                logUtils.addCaseLog("RSA decryption failed");
                return null;
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("RSA decryption exception");
            e.printStackTrace();
            return null;
        }
    }

    public boolean T_deleteRSAKey(String params) {
        try {
            logUtils.addCaseLog("delete RSA Key execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            return irsa.deleteRSAKey(pBundle);
        } catch (RemoteException e) {
            logUtils.addCaseLog("delete RSA Key exception");
            e.printStackTrace();
            return false;
        }
    }

    public String T_getPublicKey(String params) {
        try {
            logUtils.addCaseLog("get public Key execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("returnPublicKeyFormat", BundleConfig_int)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            Bundle bundle = irsa.getPublicKey(pBundle);
            if (bundle.getBoolean("isSuccess")) {
                return bundle.getString("publicKey");
            } else {
                logUtils.addCaseLog("get public key failed");
                return null;
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("get public key exception");
            e.printStackTrace();
            return null;
        }
    }

    public boolean T_savePublicKey(String params) {
        try {
            logUtils.addCaseLog("save public Key execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("modulus", BundleConfig_String),
                    new BundleConfig("exponent", BundleConfig_String)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            return irsa.savePublicKey(pBundle);
        } catch (RemoteException e) {
            logUtils.addCaseLog("save public Key exception");
            e.printStackTrace();
            return false;
        }
    }

    public byte[] T_RSASign(String params) {
        try {
            logUtils.addCaseLog("RSA sign execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("keyLength", BundleConfig_int),
                    new BundleConfig("hashAlgorithm", BundleConfig_String),
                    new BundleConfig("isHashData", BundleConfig_boolean),
                    new BundleConfig("data", BundleConfig_byte_A)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            Bundle bundle = irsa.RSASign(pBundle);
            if (bundle.getBoolean("isSuccess")) {
                return bundle.getByteArray("signature");
            } else {
                logUtils.addCaseLog("RSA sign failed");
                return null;
            }
        } catch (RemoteException e) {
            logUtils.addCaseLog("RSA sign exception");
            e.printStackTrace();
            return null;
        }
    }

    public boolean T_RSAVerify(String params) {
        try {
            logUtils.addCaseLog("RSA verify execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("hashAlgorithm", BundleConfig_String),
                    new BundleConfig("data", BundleConfig_byte_A),
                    new BundleConfig("signature", BundleConfig_byte_A)
            };
            Bundle pBundle = convert(params, bundleConfigs);
            return irsa.RSAVerify(pBundle).getBoolean("isSuccess");
        } catch (RemoteException e) {
            logUtils.addCaseLog("RSA verify exception");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_isKeyExist(String params) {
        try {
            logUtils.addCaseLog("is key exist execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("keyType", BundleConfig_int),
            };
            Bundle pBundle = convert(params, bundleConfigs);
            return irsa.isKeyExist(pBundle);
        } catch (RemoteException e) {
            logUtils.addCaseLog("is key exist exception");
            e.printStackTrace();
            return false;
        }
    }

    public boolean T_saveCertificate(String params) {
        try {
            logUtils.addCaseLog("save certificate execute");
            BundleConfig[] bundleConfigs = new BundleConfig[]{
                    new BundleConfig("keyIndex", BundleConfig_int),
                    new BundleConfig("data", BundleConfig_byte_A),
            };
            Bundle pBundle = convert(params, bundleConfigs);
            return irsa.saveCertificate(pBundle);
        } catch (RemoteException e) {
            logUtils.addCaseLog("save certificate exception");
            e.printStackTrace();
            return false;
        }
    }
}
