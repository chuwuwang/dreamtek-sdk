// IDukpt.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.key_manager;


interface IDukpt {
    /**
     * Check if Key is exists
     *
     * @param keyType
     * <ul>
     * <li>0x01 (3DES)dukpt key</li>
     * <li>0x02 (AES)dukpt key</li>
     * </ul>
     * @param keyId the id (index 0~254) of the key
     * @param extend - extend param for the future
     * @return true for exist, false for not exists
     *
     **/
    boolean isKeyExist(int keyId, int keyType, in Bundle bundle);

    /**
     * clear Dukpt key
     *
     * @param keyId clear key id
     * @param keyType
     * <ul>
     * <li> 0x01-DES DUKPT</li>
     * <li> 0x02-AES DUKPT</li>
     * </ul>
     * @return true for exist, false for not exists
     */
    boolean clearKey(int keyId, int keyType, in Bundle bundle);

    /**
     * load the DUKPT key
     *
     * @param keyId the id (index 0~254)
     * @param ksn the key serial number
     * @param key the key
     * @param checkValue the check value (default NULL)
     * @param extend - extend param
     * <ul>
     *     <li>isPlainKey(boolean) default value is true(key is plain key), value is false means the key is a encrypt key that encrypt by TEK</li>
     *     <li>TEKIndex(int) index of TEK,if isPlainKey is false, need to set this paramater</li>
     *     <li>KSNAutoIncrease(boolean) default value is true, if value is false, application use increaseKSN() to  increase KSN manually</li>
     *     <li>keyType(int), 0-3DES(default if not set), 1-AES</li>
     *     <li>AESinitialKeyType(byte), just for AES calculate<BR>
     *           |---2-AES128(default)<BR>
     *           |---3-AES192<BR>
     *           |---4-AES256</li>
     * </ul>
     * @return true on success, false on failure
     *
     */
    boolean loadDukptKey(int keyId, in byte[] ksn, in byte[] key, in byte[] checkValue, in Bundle extend);

    /**
     * calcute the MAC with given type
     *
     * @param index keyID(0~254)
     * @param type Calculation mode <BR>
     *     |---0x00-MAC X99;<BR>
     *     |---0x01-MAC X919;<BR>
     *     |---0x02-ECB (CUP standard ECB algorithm);<BR>
     *     |---0x03-MAC 9606;<BR>
     *     |---0x04-CBC MAC calculation;<BR>
     *     |---0x05-MAC_ISO9797; @Deprecated<BR>
     *     |---0x06-MAC_CMAC; CMAC algorithm (Use AES)<BR>
     * @param CBCInitVec - CBC initial vector. fixed length 8, can be null, default 8 bytes 0x00
     * @param data the source date
     * @param desType encrypt type<BR>
     *     |--0x00-des<BR>
     *     |--0x01-3des<BR>
     *     <b style="text-decoration:line-through;">|--0x02-sm4</b><BR>
     *     |--0x03-aes<BR>
     * @param extend - extend param
     * <ul>
     *     <li>variantRequestKey(boolean) for des/3des desType, true-Message Authentication, request or both ways; false-Message Authentication, response(default)</li>
     *     <li>AESKeyUsageIndicator(byte), just for AES calculate<BR>
     *           |---0-AES Data Encryption, use Msg. Auth. Gen. Key(default)<BR>
     *           |---1-AES Data Encryption, use Msg. Auth. Ver. Key<BR>
     *           |---2-AES Data Encryption, use M. Auth. Both Ways Key</li>
     *     <li>AESDispersionType(byte), just for AES calculate<BR>
     *           |---0-DES112<BR>
     *           |---1-DES168<BR>
     *           |---2-AES128(default)<BR>
     *           |---3-AES192<BR>
     *           |---4-AES256</li>
     * </ul>
     * @return Bundle include encrypted data and KSN involved in the calculation, null means failure
     * <ul>
     *     <li>encryptedData(byte[]) encrypted data block</li>
     *     <li>ksn(byte[]) </li>
     * </ul>
     */
    Bundle calculateMAC(int keyId, int type, in byte[] CBCInitVec, in byte[] data, int desType, in Bundle extend);

    /**
     * @brief encrypt data
     * @param index keyID(0~254)
     * @param encryptType<BR>
     *    |---TYPE_DES - 0x00 DES Type<BR>
     *    |---TYPE_3DES - 0x01 3DES Type<BR>
     *    |---TYPE_SM4 - 0x02 SM4 Type<BR>
     *    |---TYPE_AES - 0x03 AES Type<BR>
     * @param algorithmModel<BR>
     *     |--0x01 CBC encrypt<BR>
     *     |--0x02 ECB encrypt<BR>
     *     |--0x03 CBC decrypt<BR>
     *     |--0x04 ECB decrypt<BR>
     * @param data the source date
     * @param initVec cbc init vector
     * @param extend - extend param
     * <ul>
     *     <li>dukptDispersionType(byte) for des/3des encryptType<BR>
     *           |---0x00 Data Encryption request or both ways<BR>
     *           |---0x01 Data Encryption response (default)<BR>
     *           |---0x02 Customize, use Pin Variant constant</li>
     *     <li>AESKeyUsageIndicator(byte), just for AES calculate<BR>
     *           |---0-AES Data Encryption, encryption(default)<BR>
     *           |---1-AES Data Encryption, decryption<BR>
     *           |---2-AES Data Encryption, both encryption and decryption</li>
     *     <li>AESDispersionType(byte), just for AES calculate<BR>
     *           |---0-DES112<BR>
     *           |---1-DES168<BR>
     *           |---2-AES128(default)<BR>
     *           |---3-AES192<BR>
     *           |---4-AES256</li>
     * </ul>
     * @return Bundle include encrypted data and KSN involved in the calculation, null means failure
     * <ul>
     *     <li>encryptedData(byte[]) encrypted data block</li>
     *     <li>ksn(byte[]) </li>
     * </ul>
     */
    Bundle calculateData(int keyId, int encryptType, int algorithmModel, in byte[] data, in byte[] initVec, in Bundle extend);

    /**
     * get the last error
     *
     * @return the description of the last error
     */
    String getLastError();

    /**
     * increase ksn(just for DEX dukpt)
     * @param index keyID(0~254)
     * @param keyType 0-3DES(default if not set), 1-AES
     * @param extend extend other features for the future
     * @return current ksn
     */
    byte[] increaseKSN(int index, int keyType, in Bundle extend);

    /**
     * get KSN
     *
     * @param index keyID(0~254)
     * @param keyType 0-3DES(default if not set), 1-AES
     * @param extend extend other features for the future
     * @return dukpt ksn
     */
     byte[] getDukptKSN(int keyIdx, int keyType, in Bundle extend);
}

