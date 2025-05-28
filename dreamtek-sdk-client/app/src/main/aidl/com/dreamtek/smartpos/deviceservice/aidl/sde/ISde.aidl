// ISde.aidl
package com.dreamtek.smartpos.deviceservice.aidl.sde;
import com.dreamtek.smartpos.deviceservice.aidl.sde.CardDetectionListener;
import com.dreamtek.smartpos.deviceservice.aidl.sde.MsrDetectListener;
import com.dreamtek.smartpos.deviceservice.aidl.sde.SecurityKeyBoardListener;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;
import com.dreamtek.smartpos.deviceservice.aidl.sde.SecurityPinInputListener;

interface ISde {

//================== Key =======================
     /**
      * key must exist, otherwise return false;
      * @param dataKey
      *     <ul>
      *     <li>keyMngType(int) 0-dukpt_TDES(default); 1-dukpt_AES</li>
      *     <li>index(int) key index, 0~254, 0 is default value</li>
      *     </ul>
      * @param mode
      *     <ul>
      *     <li>encModel(int) set cbc or ecb<BR>
      *         |--0x01 ECB encrypt (default)<BR>
      *         |--0x02 CBC encrypt</li>
      *     <li>IVType(int) initialisation vector vector<BR>
      *         |--0x01 Zero (default)<BR>
      *         |--0x02 Randoom<BR>
      *         |--0x03 User defined</li>
      *     <li> IV(byte[]) user defined data
      *     <li>paddingType(int) padding type, TDES padding based on 8bytes, AES padding base on 16bytes, use setPaddingAlignType() to change it <BR>
      *         |--0x01 zero left padding, data padding with 0x00: 00 00 00 00 XX XX XX XX<BR>
      *         |--0x02 F right padding, data padding with 0xff: XX XX XX XX ff ff ff ff<BR>
      *         |--0x03 PKCS7 Each padding byte equals the padding length. Example: XX XX XX XX 04 04 04 04<BR>
      *         |--0x04 X923 Final padding byte equals the padding length and all other padding bytes equal 0x00. Example: XX XX XX XX 00 00 00 04<BR>
      *         |--0x05 ISO7816 First padding byte equals 0x80 and all other padding bytes equal 0x00. Example: XX XX XX XX 80 00 00 00<BR>
      *         |--0x06 ISO9797_1 Padding bytes are 0x00 if needed to fill block. Example: XX XX XX XX 00 00 00 00 (default)<BR>
      *         |--0x07 space right padding, data padding with 0xff: XX XX XX XX 20 20 20 20</li>
      *     <li>TDESKeyUsageIndicator(byte) for 3DES encryptType<BR>
      *           |---0x00 Data Encryption request or both ways<BR>
      *           |---0x01 Data Encryption response (default)</li>
      *     <li>AESKeyUsageIndicator(byte), just for AES calculate<BR>
      *           |---0x00 AES Data Encryption, encryption(default)<BR>
      *     <li>AESDispersionType(byte), just for AES calculate<BR>
      *           |---0-TDES112<BR>
      *           |---1-TDES168<BR>
      *           |---2-AES128(default)<BR>
      *           |---3-AES192<BR>
      *           |---4-AES256</li>
      *     </ul>
      */
    boolean setEncryptDataKey(in Bundle dataKey, in Bundle mode);

//================== SDE Calculate =======================
    /**
     * Get encrypted input message data
     * @param data data needs to be encrypted
     * @param extend extend settings
      *     <ul>
      *     <li>paddingAlignType(byte), optional,<BR>
      *     for 3DES, 0 - 8 bytes alignment; 1 - 16 bytes; 2 - 24 bytes;<BR>
      *     for AES, 0 - 16 bytes alignment; 1 - 32 bytes; 2 - 48 bytes; </li>
      *     </ul>
     * @return result
     *     <ul>
     *     <li>encryptedData(byte[]) encrypted data</li>
     *     <li>ksn(byte[]) current ksn</li>
     *     <li>initVec(byte[]) - Initial Vector (optional) </li>
     *     </ul>
     * @throws RemoteException
     */
    Bundle getEncryptedMsgData(in byte[] data, in Bundle extend);
//================== EMV =======================

    /**
     * card detection
     *
     * @param cardOption the card type (list)
     * <ul>
     *      <li>supportMagCard(boolean) support magnetic card</li>
     *      <li>supportSmartCard(boolean) support Smart card</li>
     *      <li>supportCTLSCard(boolean) support CTLS card</li>
     * </ul>
     * @param timeout the time out(unit:MS)
     * @param listener the listener while found card
	 *
     */
	void cardDetection(in Bundle cardOption, int timeout, CardDetectionListener listener);

    /**
     * set Obfuscated PAN number (panFront + panRear must greater than 10)
     * @param panFront 6 digits default
     * @param panRear 4 digits default
     * @throws RemoteException
     */
    boolean setObfuscatedPanNum(int first, int last);

    /**
     * get sensitive EMV card data
     * @param cardType  CARD_INSERT(0)-contact IC card,CARD_RF(1)-contactless card
     * @param tag Tag name
     * @param extend optional settings, set null then parameters paddingType & paddingAlignType in setEncryptDataKey will be used.
     * <ul>
     *    <li>paddingType(int) padding type, TDES padding based on 8bytes, AES padding base on 16bytes<BR>
     *        |--0x01 zero left padding, data padding with 0x00: 00 00 00 00 XX XX XX XX<BR>
     *        |--0x02 F right padding, data padding with 0xff: XX XX XX XX ff ff ff ff<BR>
     *        |--0x03 PKCS7 Each padding byte equals the padding length. Example: XX XX XX XX 04 04 04 04<BR>
     *        |--0x04 X923 Final padding byte equals the padding length and all other padding bytes equal 0x00. Example: XX XX XX XX 00 00 00 04<BR>
     *        |--0x05 ISO7816 First padding byte equals 0x80 and all other padding bytes equal 0x00. Example: XX XX XX XX 80 00 00 00<BR>
     *        |--0x06 ISO9797_1 Padding bytes are 0x00 if needed to fill block. Example: XX XX XX XX 00 00 00 00 (default)<BR>
     *        |--0x07 space right padding, data padding with 0xff: XX XX XX XX 20 20 20 20</li>
     * <ul>
     * @return bundle
     * <ul>
     *    <li>encryptedData(byte[]): encrypted tag value</li>
     *    <li>ksn(byte[]): ksn</li>
     *    <li>initVec(byte[]) - optional parameter</li>
     *    <li>result(int) - result message<BR>
     *         |--- 0-success<BR>
     *         |--- -1-the tag isn't sensitive tag<BR>
     *         |--- -2-invalid params <li>
     *         |--- -3-encryption error <li>
     * </ul>
     */
    Bundle getEncryptedData(int cardType, String tag, in Bundle extend);
//================== security keybord =======================
    /**
     * security keyboard ui interface
     *
     * @param param       <BR>
     *                    <ul>
     *                         <li>displayKeyValue(byte[]) customized the sequence key number of keyboard</li>
     *                         <li>inputLimit(byte[]) the valid length(s) array of the input length length (such as {16,17,18,19} means the valid length is 16~19)</li>
     *                         <li>timeout(int) the timeout, unit is second, default 60s</li>
     *                    </ul>
     * @keyInfos - the list of PinKeyCoorInfo
     * @param listener     callback
     * @return  map<String String> - the value of 0~9 key to display
	 *
     * */
    Map initSecurityKeyBoardView(in Bundle param, in List<PinKeyCoorInfo> keyInfos, SecurityKeyBoardListener listener);

    /**
     * start security keyboard
     *
     * @param param       <BR>
     *                    <ul>
     *                    <li>inputType(int)</li> 1 - PAN input; 2 - PAN expired date;  3 - Amount;  others values for other input type</li>
     *                    </ul>
	 *
     * */
	void startSecurityKeyBoardEntry(in Bundle param);

//================== clean  =======================
    void cleanSensitiveData();

    /**
     * get SDE version
     * @return return SDE version if SRED switch = true, if not return ""
     * @throws RemoteException
     */
    String getSDEVer();

    /**
    * config card bin range for payment application
    * @param path  The path of card bin range configuration,which must be in json format
    * @return result
    */
    boolean configCardBinRange(String path);

    /**
     * default setting: 8 bytes align for 3DES, 16 bytes align for AES
     *
     * @param alignTypeMap Map <String, Byte>,
     *          Key - set alignment type for EMV tags, you can set for PAN(5A), expired of PAN(5F24),
     *              track2(57, 9F20-track2 DD, 9F6B-MSD track2, FF9006-MSD Pseudo track2), or default and
     *              other spcial EMV tags, pls refer to "con.dreamtek.smartpos.constdefine.ConstISde"
     *          Value:
     *              - for 3DES, 0 - 8 bytes alignment; 1 - 16 bytes; 2 - 24 bytes;
     *              - for AES, 0 - 16 bytes alignment; 1 - 32 bytes; 2 - 48 bytes;
     *
	 * <pre>
     * {
     *      Map<String,Byte> map = new HashMap<>();
     *      map.put("pan", 1); // 5A
     *      map.put("pan_expired", 1); //5F24
     *      map.put("track2", 1); //57, 9F20, 9F6B, FF9006
     *      map.put("default", 1);
     *      map.put("other tags", 1);
     *      setPaddingAlignType(map);
     * }
	 * </pre>
     * @return result: true or false
     */
    boolean setPaddingAlignType(in Map alignType);

    /**
     * return card's detail such issue data, isExpired message for refund transaction or other
     * doesn't have cardConfirm() callback transaction type, this API should be called in middle of
     * EMV process.
     * @cardType current EMV transaction type, 0-contact card; 1-contactless card
     * @return Bundle<BR>
     * <ul>
     * <li>issuerID(String)</li> get card's issuer ID</li>
     * <li>isExpired(boolean)</li> card expired or not</li>
     * <li>obfuscatedPAN(String)</li> get obfuscated PAN</li>
     * </ul>
     * @throws RemoteException
     */
    Bundle getCurrentCardDetails(int cardType);

    /**
     * stop security keyboard
     * @throws RemoteException
     */
	void stopSecurityKeyBoardEntry();

//    /**
//     * Configure the format of the returned data, currently supporting two formats: bcd (compressed) and ascii (uncompressed),
//     * corresponding to {@link com.dreamtek.smartpos.deviceservice.aidl.sde.DataFormat.FormatType.FORMAT_TYPE_BCD} and {@link com.dreamtek.smartpos.deviceservice.aidl.sde.DataFormat.FormatType.FORMAT_TYPE_ASCII}.
//     * For example, if I make the following configuration:
//     * configDataFormat(DataFormat. DataType.DATA_TYPE_TRACK2, DataFormat.FORMAT_TYPE_BCD),the track2 information will be encrypted in BCD format and returned.
//     * And also please note that if the data is set to {@link com.dreamtek.smartbos.service.aidl.sde.DataFormat.DataType.DATA_TYPE_DEFAULT}, the default settings will be used and all previous settings will be reset.
//     * @param data The data that needs to be configured, please refer to {@link com.dreamtek.smartpos.deviceservice.aidl.sde.DataFormat.DataType} for the currently supported types.
//     * @param format refer to {@link com.dreamtek.smartpos.deviceservice.aidl.sde.DataFormat.FormatType}
//     * @return success or not
//     * @throws RemoteException
//     */
//	boolean configDataFormat(int data, byte format);
}