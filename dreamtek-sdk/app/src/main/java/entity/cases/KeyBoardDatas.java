package entity.cases;

public class KeyBoardDatas {
    private static KeyBoardDatas instance;

    private byte[] encryptedData;
    private String obfuscatedPAN;
    private byte[] ksn;
    private byte[] initVec;
    private byte[] pinBlock;

    private String pinBlock1;

    public String getPinBlock1() {
        return pinBlock1;
    }

    public void setPinBlock1(String pinBlock1) {
        this.pinBlock1 = pinBlock1;
    }

    public byte[] getPinBlock() {
        return pinBlock;
    }

    public void setPinBlock(byte[] pinBlock) {
        this.pinBlock = pinBlock;
    }


    public static synchronized KeyBoardDatas getInstance() {
        if (instance == null){
            instance = new KeyBoardDatas();
        }
        return instance;
    }

//    public static void setInstance(KeyBoardDatas instance) {
//        KeyBoardDatas.instance = instance;
//    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getObfuscatedPAN() {
        return obfuscatedPAN;
    }

    public void setObfuscatedPAN(String obfuscatedPAN) {
        this.obfuscatedPAN = obfuscatedPAN;
    }

    public byte[] getKsn() {
        return ksn;
    }

    public void setKsn(byte[] ksn) {
        this.ksn = ksn;
    }

    public byte[] getInitVec() {
        return initVec;
    }

    public void setInitVec(byte[] initVec) {
        this.initVec = initVec;
    }
}
