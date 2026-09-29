package view.custompinpad;

import java.util.List;

/**
 * Created by fusheng.z on 2017/11/30.
 */

public class PinPadInitPinInputCustomViewParamIn {
    public static final int TDES_TYPE = 0;
    public static final int SM4_TYPE = 1;

    private int keyId;                // Pin key index
    private byte[] pinLimit;          // Allowed PIN length
    private int timeout;              // Input timeout in seconds
    private boolean isOnline;         // Online PIN flag
    private String promptString;      // Prompt message
    private String pan;               // Primary account number (PAN) for encrypting online PIN
    private int keyType;              // Key type / input method
    private int desType;              // Algorithm type
    private byte[] keyboardNumberPosition; // number position in keyboard, if it is null, display random number

    private byte[] random; // only used for pup project.

    private PinpadListener pinpadListener;
    public interface PinpadListener {
        public void onInput(int len, int key);
        public void onConfirm(byte[] data, boolean isNonePin);
        public void onCancel();
        public void onError(int errorCode, String errorMsg);
    }

    public int getKeyId() {
        return keyId;
    }

    public byte[] getPinLimit() {
        return pinLimit;
    }

    public boolean getOnLineState() {
        return isOnline;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getPromptString() {
        return promptString;
    }

    public String getPan() {
        return pan;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public void setPinLimit(byte[] pinLimit) {
        this.pinLimit = pinLimit;
    }

    public void setOnlineState(boolean online) {
        isOnline = online;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPromptString(String promptString) {
        this.promptString = promptString;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public int getDesType() {
        return desType;
    }

    public void setDesType(int desType) {
        this.desType = desType;
    }

    public void setPinpadListener(PinpadListener pinpadListener) {
        this.pinpadListener = pinpadListener;
    }

    public PinpadListener getPinpadListener() {
        return pinpadListener;
    }

    public byte[] getRandom() {
        return random;
    }

    public void setRandom(byte[] random) {
        this.random = random;
    }

    public byte[] getKeyboardNumberPosition() {
        return keyboardNumberPosition;
    }

    public void setKeyboardNumberPosition(byte[] keyboardNumberPosition) {
        this.keyboardNumberPosition = keyboardNumberPosition;
    }
}
