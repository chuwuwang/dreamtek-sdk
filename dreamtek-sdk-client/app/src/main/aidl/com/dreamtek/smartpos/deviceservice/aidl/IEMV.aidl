// IEMV.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl;
import com.dreamtek.smartpos.deviceservice.aidl.CheckCardListener;
import com.dreamtek.smartpos.deviceservice.aidl.EMVHandler;
import com.dreamtek.smartpos.deviceservice.aidl.OnlineResultHandler;
import com.dreamtek.smartpos.deviceservice.aidl.CandidateAppInfo;
import com.dreamtek.smartpos.deviceservice.aidl.DRLData;
import com.dreamtek.smartpos.deviceservice.aidl.BLKData;
import com.dreamtek.smartpos.deviceservice.aidl.IssuerUpdateHandler;
import com.dreamtek.smartpos.deviceservice.aidl.EMVTransParams;
import com.dreamtek.smartpos.deviceservice.aidl.RequestACTypeHandler;

/**
 * EMV object for processing EMV

 */
interface IEMV {
       /**
        * check card, non-block method
        *
        * @param cardOption the card type (list)
        * <ul>
        * <li>supportMagCard(boolean) support magnetic card</li>
        * <li>supportSmartCard(boolean) support Smart card</li>
        * <li>supportCTLSCard(boolean) support CTLS card</li>
        * </ul>
        * @param timeout the time out(ms)
        * @param listener the listerner while found card
        *
        */
   	void checkCard(in Bundle cardOption, long timeout, CheckCardListener listener);

    /**
     * stop check card
	 * @
     */
	void stopCheckCard();

    /**
     * start EMV process
     *
     * @param transflow processing type
     * <ul>
     * <li>1：EMV processing</li>
     * <li>2：EMV simplified processing</li>
     * </ul>
     * @param intent request setting
     * <ul>
     * <li>cardType(int): card type
     *      * CARD_INSERT(0)- smart IC card
     *      * CARD_RF(1)- CTLS card </li>
     * <li>transProcessCode(byte): (1Byte) Translation type (9C first two digits of the ISO 8583:1987 Processing Code)</li>
     * <li>authAmount(long): auth-amount (transaction amount)</li>
     * <li>isSupportSM(boolean): is support SM </li>
     * <li>isForceOnline(boolean): is force online </li>
     * <li>merchantName(String):merchant Name (var. bytes)</li>
     * <li>merchantId(String): merchant ID (15 bytes)</li>
     * <li>terminalId(String):terminal ID (8 bytes)</li>
     * <li>transCurrCode(String): currency code(5F2A), if not set, kernel will find the tag in AID string.</li>
     * <li>otherAmount(String): set Other Amount (9F03) value</li>
     * <li>panConfirmTimeOut(int): set timeout of pan confirm, if not set then default 60s(just support smart card)</li>
     * <li>appSelectTimeOut(int): set timeout of selectApp, if not set then default 60s(just support smart card)</li>
     * <li>traceNo(String):trace no (var. bytes)</li>
     * <li>ctlsPriority(byte): CTLS application priority, no necessary, b0-MCCS MyDebit priorty b1-terminal priorty b2~b7 to be define</li>
     * <li>isForceOffline(boolean): is force offline, no necessary, false is default (just support AMEX kernel)</li>
     * <li>ctlsAidsForSingleTrans(ArrayList<String>): CTLS transaction input temporary aid params(AID + KernelID(9F2A01xx) + transType(DF2901xx) + transCurrCode(DF2A02xxxx))</li>
     * <li>isTerminalTypeSetInAID(boolean): default value is false(default vaule is 0x22), you should confirm this tag(9F35) in your AID String when you set this tag is true.</li>
     * <li>ctlsEmvAbortWhenAppBlocked(boolean): when CTLS app blocked then EMV abort</li>
     * <li>paramsGroupName(String): aid/rid/DRL group name</li>
     * <li>merchCateCode(String): set Merchant Category code for each EMV process, default value is 1234</li>
     * <li>isStartVccsFlag(boolean): set this param true(default is false), meanwhile DF2F tag in AID string B1b6 is 1, then open padding 00 rule to change 9F02</li>
     * <li>pureKernelDisable_AC_ECHO(boolean): pure kernel close AC_ECHO TTPI B3b2 (default is false)</li>
     * <li>pureKernelDisable_AC_OfflineCAM(boolean): pure kernel close AC_OfflineCAM TTPI B2b4 (default is false)</li>
     * </ul>
     * @param handler the call back handler, please refer EMVHandler
	 * @
     */
	void startEMV(int processType, in Bundle intent, EMVHandler handler);


    /**
    * start EMV process
    * @param EMVTransParam @see EMVTransParams.java
    * @param extend
    * <ul>
    *    <li>panConfirmTimeOut(int): set timeout of pan confirm, if not set then default 60s(just support smart card)</li>
    *    <li>appSelectTimeOut(int): set timeout of selectApp, if not set then default 60s(just support smart card)</li>
    *    <li>ctlsPriority(byte): CTLS application priority, no necessary, b0-MCCS MyDebit priority b1-terminal priority,use DF2D to custom set priority b2~b7 to be define</li>
    *    <li>isForceOffline(boolean): is force offline, no necessary, false is default (just support CTLS AMEX kernel)</li>
    *    <li>ctlsAidsForSingleTrans(ArrayList<String>): CTLS transaction input temporary aid params(AID + KernelID(9F2A01xx) + transType(DF2901xx) + transCurrCode(DF2A02xxxx))</li>
    *    <li>isTerminalTypeSetInAID(boolean): default value is false(default vaule is 0x22), you should confirm this tag(9F35) in your AID String when you set this tag is true.</li>
    *    <li>merchCateCode(String): set Merchant Category code for each EMV process, default value is 1234</li>
    * </ul>
    * @param handler the call back handler, please refer EMVHandler
    *
    */
	void startEmvWithTransParams(in EMVTransParams emvParams, in Bundle extend, EMVHandler handler);

    /**
     * stop EMV
     *
	 * @
     * */
	void abortEMV();

    /**
     * import amount
     *
     * There is nothing in this method. The amount should be set while call the startEMV.
     * @param amount the amount
	 * @
     * @deprecated
     */
    void importAmount(long amount);

    /**
     * select application (multi-application card)
     *
     * @param index the index of application, start from 1, and 0 means cancel
	 * @
     */
    void importAppSelection(int index);

    /**
     * import the PIN
     *
     * @param option(int) - the option
     * <ul>
     * <li> CANCEL(0) cancel</li>
     * <li> CONFIRM(1) confirm</li>
     * </ul>
     * @param pin the PIN data
	 * @
     */
    void importPin(int option, in byte[] pin);

    /**
     * import the result of card hodler verification
     *
     * @param option the option
     * <ul>
     * <li> CANCEL(0) cancel ( BYPASS )</li>
     * <li> CONFIRM(1) confirm</li>
     * <li> NOTMATCH(2) not match</li>
     * </ul>
	 * @
     */
    void importCertConfirmResult(int option);

    /**
     * import the result of card verification
     *
     * @param pass true on pass, false on error
	 * @
     */
    void importCardConfirmResult(boolean pass);

    /**
     * input the online response
     *
     * @param onlineResult  set the result ( response )
     * <ul>
     * <li> isOnline(boolean)is online</li>
     * <li> respCode(String) the response code</li>
     * <li> authCode(String) the authorize code</li>
     * <li> field55(String) the response of field 55 data form HOST(include ARPC or script)</li>
     * </ul>
     * @param handler the result , please refer OnlineResultHandler
	 * @
     */
    void importOnlineResult(in Bundle onlineResult, OnlineResultHandler handler);

     /**
     * set smart card request online after Application Selection and before GAC,
     * it can set TVR B4b4(Merchant forced transaction online) is true.
     *
     * @return result, 0 on success, others on failure
     * @
     */
    int emvProcessingRequestOnline();

    /**
     * set before startEMV(), set callback for request issuer update script(CTLS request second tap)
     *
     */
    void setIssuerUpdateHandler(in IssuerUpdateHandler issuerUpdateHandler);

    /**
     * After CTLS second tap to update script
     *
     */
    void setIssuerUpdateScript();

     /**
     * set before startEMV(), set RequestACTypeHandler callback.
     *
     */
    void setRequestACTypeCallBack(RequestACTypeHandler requestACTypehandler);

    /**
     * re-set 1GAC AC type
     * @param requestACType: chip card can change 1GAC AC Type(not necessary), 0-AAC, 1-ARQC, 2-TC
     *
     */
    void setRequestACType(int requestACType);


    /**
     * set EMV (kernel) data in trans process (DCC)
     *
     * In emv flow(onConfirmCardInfo callback or onRequestInputPIN callback), you can modify the emv data. <b>just support smartcard</b><br>
     * for example:<br>
     * 1.firt you set aidString 5F2A=0156, but in onConfirmCardInfo callback you want to reset this tag 5F2A=0116, you can use this interface.<br>
     * 2.second you set authAmount=100(9F02) in startEmv, in onConfirmCardInfo callback you can reset the auth amount.
     * @param tlvList the TLV list
	 * @
     */
    void setEMVData(in List<String> tlvList);

    /**
     * get kernal data list in Tag-Length-Value format
     *
	 * @param taglist the tag list want query
	 * @return the response in TLV format(sensitive tag are not included when SRED is on), null means no response got
	 * @
	 * <pre>
     * {
     *     String[] strlist = {"9F33", "9F40", "9F10", "9F26", "95", "9F37", "9F1E", "9F36",
     *             "82", "9F1A", "9A", "9B", "50", "84", "5F2A", "8F"};
     *     String strs = iemv.getAppTLVList(strlist);
     *  }
	 * </pre>
	 */
	String getAppTLVList(in String[] taglist);

    /**
     * get card (emv) data by tag
     *
	 * @param tagName the tag name
	 * @return the emv data got
	 * @
	 */
	byte[] getCardData(String tagName);

	/**
     * get EMV data
     *
     * such as card number, valid dtae, card serial number, etc.
     * <em> will return null if the data is not avalible at the current EMV process</em>
	 * @param tagName tag name
	 * <ul>
     * <li> PAN card No.</li>
     * <li> TRACK2 track No.2</li>
     * <li> CARD_SN card SN (Serial Number)</li>
     * <li> EXPIRED_DATE expried date</li>
     * <li> DATE date</li>
     * <li> TIME time</li>
     * <li> BALANCE balance</li>
     * <li> CURRENCY currency</li>
     * </ul>
	 * @return the return data of EMV
	 * @
	 */
	String getEMVData(String tagName);

	/**
     * Obtain the CTLS card type
	 * @return see below:
	 * <pre>
	 *   0-No Type
	 *   1-JCB_CHIP
	 *   2-JCB_MSD
	 *   3-JCB_Legcy
	 *   4-VISA_w1
	 *   5-VISA_w3
	 *   6-Master_EMV
	 *   7-Master_MSD
	 *   8-qPBOC_qUICS
	 * </pre>
	 * @
     * @deprecated
	*/
	int getProcessCardType();

	/**
     * get parameters from AID/RID
     * @param paramType.
     * <ul>
     * <li>1：AID contact</li>
     * <li>2：AID contactless</li>
     * <li>3：RID/CAPK </li>
     * </ul>
     * @param Bundle
     * <ul>
     *    <li>groupName(String): group name</li>
     * </ul>
     * @return if groupName does not exist, then return null;
     */
    String[] getGroupParams(int paramType, in Bundle extend);

    /**
     * update AID/CAPK/DRL parameter
     *
     * @param operation the setting
     * <ul>
     * <li>1：append</li>
     * <li>2：clear</li>
     * </ul>
     * @param paramType.
     * <ul>
     * <li>1：AID contact</li>
     * <li>2：AID contactless</li>
     * <li>3：RID/CAPK</li>
     * <li>4：DRL</li>
     * </ul>
     * @param params the AID/RID/CAPK parameter value, if DRL type, set NULL.
    * @param extend
    * <ul>
    *    <li>groupName(String): group name</li>
    *    <li>DRLData(Parcelable): input DRLData object if paramType is 4</li>
    * </ul>
     * @return result, true on success, false on failure
     *
     */
    boolean updateGroupParam(int operation,int paramType, String params, in Bundle extend);


    /**
     * update card black parameter
     *
     * @param operation the setting
     * <ul>
     * <li>1：append</li>
     * <li>2：clear</li>
     * </ul>
     * @param BLKData data of card black
     * @param type of card black parameter
     * <ul>
     * <li>1：contact(smart card)</li>
     * <li>2：contactless</li>
     * </ul>
     * @return result, true on success, false on failure
	 * @
     */
    boolean updateCardBlk(int operation, in BLKData blkData, int type);

    /**
     * enable Track(only support SDE = false, enable track1/2/3 by default)
	 * @param trkNum - bit0=1 enable trk1; bit1=1 enable trk2; bit2=1 enable trk3. If trkNum < 0 or trkNum > 7 the previous settings will be maintained
     *
     */
	void enableTrack(int trkNum);

    /**
     * set ctls pre process(It's not necessary.)
     * @param param setting
     * <ul>
     * <li>traceNo(String):trace no (var. bytes)</li>
     * <li>transProcessCode(byte): (1Byte) Translation type (9C first two digits of the ISO 8583:1987 Processing Code)</li>
     * <li>transCurrCode(String): currency code(5F2A), if not set, kernel will find the tag in AID string.</li>
     * <li>otherAmount(String): set Other Amount (9F03) value</li>
     * <li>authAmount(long): auth-amount (transaction amount)</li>
     * <li>isForceOnline(boolean): is force online </li>
     * <li>ctlsPriority(byte): CTLS application priority, no necessary, b0-MyDebit b1~b7 to be define</li>
     * <li>isForceOffline(boolean): is force offline, no necessary, false is default</li>
     * </ul>
     * @return true-sucess false-failed
     *
     */
	boolean setCtlsPreProcess(in Bundle param);

    /**
     * set EMV kernel to use. set this interface before startEMV()
	 * @param customAidList see below:
	 * <pre>
	 * Map < String, Integer >
	 * String - custom aid
	 * Integer - kernelID(check CTLSKernelID)
	 * </pre>
	 * @since 2.x.x
	 */
	void registerKernelAID(in Map customAidList);

    /**
     * reset emvHandler in EMV process
     * @param EMVHandler please refer EMVHandler
     *
     */
	void resetEmvHandler(EMVHandler handler);

    /**
     * set bypass all PIN
     * @param byPassAllPin: default is false, if set true, will bypass all PIN
     *
     */
	void setByPassAllPin(boolean byPassAllPin);

    /**
     * get bypass all PIN status
	 * @return true - bypass all PIN, false - not bypass all PIN
     *
     */
	boolean isByPassAllPin();



    /**
     * obtain card issuer CTLS specification version.
     * @return Map - Spec version of all CTLS kernels
     * <pre>{@code
      Map <String kernel, String SpecVer>
           kernel: AMEX; MASTER; JCB; VISA; DISCOVER; CUP; RUPAY; PURE; MIR
      }</pre>
     *
     */
    Map getCtlsSpecVer();


    /**
     * check AID and CAPK file valid or not, this file via updateAID/RID to create
     * @param fileType 0-AID 1-CAPK
     * @return
     */
    boolean checkFileValidity(int fileType);


    /**
     * get medium type when kernel conduct CDCVM.
     * @return 0-unknow; 1-card; 2-mobile phone
	 *
     */
    int getCDCVMTransactionMediumType();
}
