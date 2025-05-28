package com.dreamtek.smartpos.deviceservice.aidl.sde;

// Declare any non-default types here with import statements

interface SecurityKeyBoardListener {
    /**
     * on input (PAN press)
     *
     * @param len the length of the PAN inputted
     * @param key the mask key
     */
    void onInput(int len, int key);

    /**
     * encrypted PAN
     * @param encrypt data of PAN number
     * <ul>
     *     <li>encryptedData(byte[]) encrypted data block</li>
     *     <li>obfuscatedPAN(String) o bfuscated PAN if inputType is 1</li>
     *     <li>ksn(byte[]) </li>
     *     <li>initVec(byte[]) - Initial Vector (optional) </li>
     * </ul>
     */
    void onConfirm(in Bundle panInfos);

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
     * -6:params invalid <BR>
     * 0xff:other error <BR>
     */
    void onError(int errorCode);
}