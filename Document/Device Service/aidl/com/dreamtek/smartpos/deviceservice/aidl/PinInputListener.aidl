package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * the listener while input PIN

 */
interface PinInputListener {
    /**
     * on input (key press)
     *
     * @param len the length of the PIN inputted
     * @param key the mask key
     */
    void onInput(int len, int key);
    

    /**
     * on confirm the PIN
     *
     * @param data the PIN number, null if no pin inputed
     * <ul>
     *    <li>pinblock(byte[]) encrypted pin data or plain key if it's offline pin</li>
     *    <li>ksn(byte[]) ksn</li>
     *    <li>isByPass(boolean) pin status</li>
     *    <li>isEncrypt(boolean) encrypt(online)/plain(offline) key</li>
     * </ul>
     */
    void onConfirm(in Bundle pinInfos);

    /**
     * on cancel
     */
    void onCancel();
    
    /**
     * on error
     *
     * @param errorCode the error code<BR>
     * errorCode:<BR>
     * -1:input execption <BR>
     * -2:input time out <BR>
     * -3:plain text is null <BR>
     * -4:encrypt error <BR>
     * -5:cipher text is null <BR>
     * 0xff:other error <BR>
     */
    void onError(int errorCode);
}
