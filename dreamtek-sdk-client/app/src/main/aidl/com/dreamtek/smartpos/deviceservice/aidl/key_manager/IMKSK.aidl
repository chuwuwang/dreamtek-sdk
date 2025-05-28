package com.dreamtek.smartpos.deviceservice.aidl.key_manager;

interface IMKSK {
	/**
     * Check if Key is exists
     *
	 * @param keyId the id (index 0~254) of the key
	 * @param keyType
	 * <ul>
	 * <li>0x00 MASTER(main) key</li>
	 * <li>0x01 MAC key</li>
	 * <li>0x02 PIN(work) key</li>
	 * <li>0x03 TD key</li>
	 * <li>0x04 TEK</li>
	 * <li>0x10 (AES) MASTER key</li>
	 * <li>0x11 (AES) MAC key</li>
	 * <li>0x12 (AES) PIN key</li>
	 * <li>0x13 (AES) TD key</li>
	 * <li>0x14 (AES)TEK</li>
	 * </ul>
	 * @param extend - extend param for the future
	 * @return true for exist, false for not exists
     *
	 **/
	boolean isKeyExist(int keyId, int keyType, in Bundle extend);

    /**
     * clear M/S key
     *
     * @param keyId clear key id
     * @param keyType
     * <ul>
     * <li> 0x00-DES MK</li>
     * <li> 0x01-SM4 MK</li>
     * <li> 0x02-AES MK</li>
     * <li> 0x10-DES PIN</li>
     * <li> 0x11-SM4 PIN</li>
     * <li> 0x12-AES PIN</li>
     * <li> 0x20-DES MAC</li>
     * <li> 0x21-SM4 MAC</li>
     * <li> 0x22-AES MAC</li>
     * <li> 0x30-DES DATA</li>
     * <li> 0x31-SM4 DATA</li>
     * <li> 0x32-AES DATA</li>
     * <li> 0x40-DES TEK</li>
     * <li> 0x41-AES TEK</li>
     * </ul>
	 * @param extend - extend param for the future
     */
	boolean clearKey(int keyId, int keyType, in Bundle bundle );

    /**
     * load TEK key with Algorithm Type given
     *
     * TEK is the transfer key to encrypt master key
	 * @param keyId the id (index, 0 to 254)
	 * @param key the key
	 * @param algorithmType<BR>
	 *     |---0x01-3des encrypted key <BR>
	 *     |---0x02-3des plain key <BR>
	 *     |---0x03-SM4 encrypte key <BR>
	 *     |---0x04-SM4 plain key <BR>
	 *     |---0x05-AES encrypted key <BR>
	 *     |---0x06-AES plain key
	 *     |---0x81-CBC 3des encrypted key <BR>
	 *     |---0x82-CBC 3des plain key <BR>
	 *     |---0x83-CBC SM4 encrypte key <BR>
	 *     |---0x84-CBC SM4 plain key <BR>
	 *     |---0x85-CBC AES encrypted key <BR>
	 *     |---0x86-CBC AES plain key
	 * @param checkValue the check value
	 * @param extend - extend param
     * <ul>
     *     <li>isCBCType(boolean) judge the mk encrypt mode whether is CBC mode(default false)</li>
     *     <li>initVec(byte[]) cbc initVec(default 16byte 0)</li>
     * </ul>
	 * @return true on success, false on failure
	 */
	boolean loadTEK(int keyId, in byte[] key, in int algorithmType, in byte[] checkValue, in Bundle extend);

	/**
     * load the encrypted master key given Algorithm Type
     *
	 * @param keyId the id (index, 0 to 254)
	 * @param key the encrypted key
	 * @param algorithmType 0x01-3des algorithm<BR> 0x03-SM4 algorithm<BR> 0x05-AES algorithm<BR> 0x81-3des(cbc)<BR> 0x83-SM4(cbc)<BR> 0x85-AES(cbc)<BR>
	 * @param check value (default NULL)
	 * @param extend - extend param
     * <ul>
     *     <li>isCBCType(boolean) judge the mk encrypt mode whether is CBC mode(default false)</li>
     *     <li>initVec(byte[]) cbc initVec(default 16byte 0)</li>
     *     <li>isMasterEncMasterMode(boolean) master key can encrypt master key, if pos has loaded Master key</li>
     *     <li>decryptKeyIndex(int) (index 0 to 254)Decrypt key index. Will use the KeyId if not set and after that the last key will be overwritten.</li>
     * </ul>
	 * @return true for success, false for failure
	 */

	boolean loadMasterKey(int keyId, in byte[] key, int algorithmType, in byte[] checkValue, in Bundle extend);

	/**
     * load the plain master key given the algorithm Type (support ECB\CBC)
     *
	 * @param keyId the id (index)
	 * @param key the key
	 * @param algorithmType
	            0x02-3des ecb algorithm<BR> 0x04-SM4 ecb algorithm<BR> 0x06-AES ecb algorithm<BR>
	            0x82-3des cbc algorithm<BR> 0x84-SM4 cbc algorithm<BR> 0x86-AES cbc algorithm<BR>
	 * @param checkValue the check value (default NULL)
	 * @return true on success, false on failure
     *
	 */
	boolean loadPlainMasterKey(int keyId, in byte[] key, int algorithmType, in byte[] checkValue);

	/**
     * load the work key given decrypt type
     *
	 * @param keyType select the workkey type<BR>
	 *     |---1-MAC key, 2-PIN key, 3-TD key<BR>
	 *     |---5-(SM4)MAC key, 6-(SM4)PIN key, 7-(SM4)TD key<BR>
	 *     |---9-(AES)MAC key, 10-(AES)PIN key, 11-(AES)TD key<BR>
	 * @param mkId the id of master key for decrypt work key
	 * @param wkId set the workkey id (index 0~254)
	 * @param decKeyType select decrypt key type<BR>
	 *     |---0x00-3DES master key<BR>
	 *     |---0x01-transport key<BR>
	 *     |---0x02-SM4 master key<BR>
	 *     |---0x03-AES master key<BR>
	 *     |---0x04-SM4 transport key<BR>
	 *     |---0x05-AES transport key<BR>
	 *     |---0x80-CBC 3DES master key<BR>
	 *     |---0x81-CBC transport key<BR>
	 *     |---0x82-CBC SM4 master key<BR>
	 *     |---0x83-CBC AES master key<BR>
	 *     |---0x84-CBC SM4 transport key<BR>
	 *     |---0x85-CBC AES transport key<BR>
	 * @param encrypt key
	 * @param checkValue check value (null for none)
	 * @param extend - extend param
     * <ul>
     *     <li>isCBCType(boolean) judge the mk encrypt mode whether is CBC mode(default false)</li>
     *     <li>initVec(byte[]) cbc initVec(default 16byte 0)</li>
     * </ul>
	 * @return true on success, false on failure
	 */
	boolean loadSessionKey(int keyType, int mkId, int wkId, int decKeyType, in byte[] key, in byte[] checkValue, in Bundle extend);

	/**
     * calcute the MAC with given type
     *
	 * @param keyId the index of MAC KEY(0~254)
	 * @param type Calculation mode 0x00-MAC X99; 0x01-MAC X919; 0x02-ECB (CUP standard ECB algorithm); 0x03-MAC 9606; 0x04-CBC MAC calculation
	 * @param CBCInitVec - CBC initial vector. fixed length 8, can be null, default 8 bytes 0x00
	 * @param data the source date
	 * @param desType encrypt type<BR>
     * <b style="text-decoration:line-through;">|--0x00-des</b><BR>
     * |--0x01-3des<BR>
     * |--0x02-sm4<BR>
     * |--0x03-aes<BR>
	 * @param extend - extend param for the future
	 * @return the mac value, null means failure
	 */
	byte[] calculateMAC(int keyId, int type, in byte[] CBCInitVec, in byte[] data, int desType, in Bundle extend);

    /**
     * encrypt or decrypt data
     *
     * @param mode the mode of encrypt or decrypt
     * <ul>
     * <li>0x00 MK/SK ECB encrypt </li>
     * <li>0x01 MK/SK ECB decrypt </li>
     * <li>0x02 MK/SK CBC encrypt </li>
     * <li>0x03 MK/SK CBC decrypt </li>
     * </ul>
     * @param desType the type of encrypt or decrypt
     * <ul>
     * <li>TYPE_DES - 0x00 DES Type </li>
     * <li>TYPE_3DES - 0x01 3DES Type(EBC) </li>
     * <li>TYPE_SM4 - 0x02 SM4 Type </li>
     * <li>TYPE_AES - 0x03 AES Type</li>
     * </ul>
	 * @param key the source key
	 * @param data the source date
	 * @param initVec 3des cbc init vector
	 * @param extend - extend param for the future
     *
     * @return the encrypted or decrypted data, null means failure
     */
	byte[] calculateData(int mode, int desType, in byte[] key, in byte[] data, in byte[] initVec, in Bundle extend);

	/**
     * encrypt data or decrypt data by data key
     *
	 * @param keyId - data key index(0~254)
	 * @param keyType - key type
	 *     |---0x01 data key<BR>
	 *     |---0x02 mac key<BR>
	 *     <b style="text-decoration:line-through;">|---0x03 pin key</b><BR>
	 * @param encAlg- encryption algorithm<BR>
	 *     |---0x01 3DES<BR>
	 *     |---0x02 SM4<BR>
	 *     |---0x03 AES<BR>
	 * @param encMode - encryption mode of operation<BR>
	 *     |--0x01 ECB<BR>
	 *     |--0x02 CBC<BR>
	 * @param encFlag - encryption flag<BR>
	 *     |--0x00 encrypt<BR>
	 *     |--0x01 decrypt<BR>
	 * @param data - data
	 * @param extend - extend param
     * <ul>
     *     <li>initVec(byte[]) cbc initVec(default null)</li>
     * </ul>
	 * @return the result of encrypt data or decrypt data;
	 */
    byte[] calculateBySessionKey(int keyId, int keyType, int encAlg, int encMode, int encFlag, in byte[] data, in Bundle extend);

	/**
     * calculate by MSKey
     *
	 * @param keyId - data key index(0~254)
	 * @param keyType - key type
	 *     |---0x01 3DES master key<BR>
	 *     |---0x02 SM4 master key<BR>
	 *     |---0x03 AES master key<BR>
	 * @param algorithmMode - encryption algorithm<BR>
	 *     |---0x00 encrypt ECB<BR>
	 *     |---0x01 decrypt ECB<BR>
	 *     |---0x02 encrypt CBC<BR>
	 *     |---0x03 decrypt CBC<BR>
	 * @param data - data
	 * @param extend - extend param
     * <ul>
     *     <li>initVec(byte[]) cbc initVec(default null if not set)</li>
     * </ul>
	 * @return the result of encrypt data or decrypt data;
	 */
    byte[] calculateByMasterKey(int keyId, int keyType, int algorithmMode, in byte[] data, in Bundle extend);

    /**
     * get the last error
     * @return the description of the last error
     */
	String getLastError();

    /**
     * Get the checkValue of key
     *
     * @param keyId   - the index of key
     * @param keyType <BR>
     *                <ul>
     *                <li>0x01 Data key</li>
     *                <li>0x02 PIN key</li>
     *                <li>0x03 MAC key</li>
     *                <li>0x04 transfer key</li>
     *                <li>0x05 Master key</li>
     *                <li>0x11 (SM4)Data key</li>
     *                <li>0x12 (SM4)PIN key</li>
     *                <li>0x13 (SM4)MAC key</li>
     *                <li>0x14 (SM4)transfer key</li>
     *                <li>0x15 (SM4)Master key</li>
     *                <li>0x21 (AES)Data key</li>
     *                <li>0x22 (AES)PIN key</li>
     *                <li>0x23 (AES)MAC key</li>
     *                <li>0x24 (AES)transfer key</li>
     *                <li>0x25 (AES)Master key</li>
     *                </ul>
     * @return the check value of the key
     */
     byte[] getKeyKCV(int keyIndex, int keyType);

    /**
     * encrypt pinblock from cardnumber & passwd
     *
     * @param pinKeyId the index of PIN KEY(0~254)
     * @param desType calculate type <BR>
     *   |----0x01 MK/SK + 3DES (default)<BR>
     *   |----0x02 MK/SK + AES<BR>
     *   |----0x03 MK/SK + SM4<BR>
     * @param cardNumber - card number (ascii type, such as "1234", you should input byte[4] = "31 32 33 34")
     * @param passws- plain password (String type, such as "1234", you should input String = "1234")
     */
	byte[] encryptPinFormat0(int pinKeyId, int desType, in byte[] cardNumber, String passwd);

    /**
     * get random data
     *
	 * @param length - length of random data(1~255)
	 * @return result of random data;
     */
    byte[] getRandom(byte length);
}

