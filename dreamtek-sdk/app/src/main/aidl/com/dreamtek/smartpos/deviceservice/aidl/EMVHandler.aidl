// EMVHandler.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * the callback handler of EMV transaction
 */
interface EMVHandler {
	/**
     * on request amount
     *
     * This method won't be called.
     * The amount should be set while calling the IEMV#startEMV.
     * @deprecated
	 */
	void onRequestAmount();

	/**
     * on select application
     *
     * @param appList the application bundle list
     * <ul>
     *     <li>aidName(String): TAG9F12 Application Preferred Name</li>
     *     <li>aidLabel(String): TAG50 Application Label</li>
     *     <li>aid(String): Application Identifier</li>
     *     <li>aidPriority(int): TAG87 Application Priority Indicator </li>
     *     <li>aidIssuerIdx(int): TAG9F11 Issuer Code Table Index </li>
     * </ul>
	 */
	void onSelectApplication(in List<Bundle> appList);

    /**
     * on confirm card information
     *
     * @param info the card information, if enable SERD feature then just return obfuscate PAN,
     *             others return FF( through iSde.getEmvSensitiveCardData() get encrypted data)
     * <ul>
     * <li>PAN(String) the PAN, if sred is true, data been encrypted if serd is true</li>
     * <li>TRACK1(String) track 1, data been encrypted if serd is true</li>
     * <li>TRACK2(String) track 2, data been encrypted if serd is true</li>
     * <li>CARD_SN(String) card serial number</li>
     * <li>SERVICE_CODE(String) service code</li>
     * <li>EXPIRED_DATE(String) expired date, data been encrypted if serd is true</li>
     * <li>CARD_TYPE(int) card type by CTLS<br>
     *    |---- 0 emv card <br>
     *    |---- 1 mstripe  card <br>
     *    |---- 2 other </li>
     * <li>ObfuscatedPAN(String) obfuscated PAN, if serd is true</li>
     * <li>KSN_PAN(String) KSN for encrypted PAN</li>
     * <li>KSNTrk1(String) KSN for encrypted track 1</li>
     * <li>KSNTrk2(String) KSN for encrypted track 2</li>
     * <li>KSN_EXPIRED_DATE(String) KSN for encrypted expired date</li>
     * <li>IS_EXPIRED(boolean) true - card has expired, false - card hasn't expired</li><BR>
     * </ul>
     */
    void onConfirmCardInfo(in Bundle info);

	/**
     * on request input pin
     *
     * @param isOnlinePin is online pin request
     * @param retryTimes the retry max times of offline pin
	 */
    void onRequestInputPIN(boolean isOnlinePin, int retryTimes);

	/**
     * on confirm card holder certinfo
     *
     * @param certType the cert type
     * @param certInfo the cert information
	 */
    void onConfirmCertInfo(String certType, String certInfo);

    /**
     * on request online process
     *
     * @param aaResult the result
     * <ul>
     * <li style="text-decoration:line-through;">SIGNATURE(boolean) need to sign</li>
     * <li>CTLS_CVMR(int) get ctls cvm<br>
     *     |---0 NO_CVM<br>
     *     |---1 CVM_PIN<br>
     *     |---2 CVM_SIGN<br>
     *     |---3 CVM_CDCVM</li>
     * <li>RESULT(int) result type<br>
     *   |----CTLS_ARQC(201) - CTLS_ARQC, online request, part of EMV standard<br>
     *   |----AARESULT_ARQC(2) - AARESULT ARQC, the action analysis result<br>
     *   |----PAYPASS_MAG_ARQC(302)-the mode of magnetic card on paypass request<br>
     *   |----PAYPASS_EMV_ARQC(303)- the mode of EMV on paypass request</li>
     * </ul>
     */
    void onRequestOnlineProcess(in Bundle aaResult);

    /**
     * the result of EMV, failure on transaction , etc
     * @param result the result
     * <ul>
     * <li>EMV_NO_APP(8) - emv no application(aid param)</li>
     * <li>EMV_COMPLETE(9) - emv complete </li>
     * <li>EMV_OTHER_ERROR(11) - emv other error,transaction abort</li>
     * <li>EMV_FALLBACK(12) - FALLBACK </li>
     * <li>EMV_DATA_AUTH_FAIL(13) - data auth fail </li>
     * <li>EMV_APP_BLOCKED(14) - app has been blocked </li>
     * <li>EMV_NOT_ECCARD(15) - not EC </li>
     * <li>EMV_UNSUPPORT_ECCARD(16) - unsupport EC </li>
     * <li>EMV_AMOUNT_EXCEED_ON_PURELYEC(17) - amount exceed EC </li>
     * <li>EMV_SET_PARAM_ERROR(18) - set parameter fail on 9F7A </li>
     * <li>EMV_PAN_NOT_MATCH_TRACK2(19) - pan not match track2 </li>
     * <li>EMV_CARD_HOLDER_VALIDATE_ERROR(20) - card holder validate error </li>
     * <li>EMV_PURELYEC_REJECT(21) - purely EC transaction reject </li>
     * <li>EMV_BALANCE_INSUFFICIENT(22) - balance insufficient</li>
     * <li>EMV_AMOUNT_EXCEED_ON_RFLIMIT_CHECK(23) - amount exceed the CTLS limit</li>
     * <li>EMV_CARD_BIN_CHECK_FAIL(24) - check card failed </li>
     * <li>EMV_CARD_BLOCKED(25) - card has been block </li>
     * <li>EMV_MULTI_CARD_ERROR(26) - multiple card conflict </li>
     * <li>EMV_INITERR_GPOCMD(27) - GPO Processing Options response error </li>
     * <li>EMV_GACERR_GACCMD(28) - GAC response error </li>
     * <li>EMV_TRY_AGAIN(29) - Try again </li>
     * <li>EMV_ODA_FAILED(30) - ODA failed </li>
     * <li>EMV_CVM_FAILED(31) - CVM response error</li>
     *
     * <li>EMV_RFCARD_PASS_FAIL(60) - tap card failure</li>
     * <li>AARESULT_TC(0) - TC on action analysis</li>
     * <li>AARESULT_AAC(1) - refuse on action analysis</li>
     *
     * <li>CTLS_AAC(202) - refuse on CTLS </li>
     * <li>CTLS_ERROR(203) - error on CTLS </li>
     * <li>CTLS_TC(204) - approval on CTLS</li>
     * <li>CTLS_CONT(205) - need contact</li>
     * <li>CTLS_NO_APP(206) - result of CTLS, no application (UP Card maybe available)</li>
     * <li>CTLS_NOT_CPU_CARD(207) - not a cpu card</li>
     * <li>CTLS_ABORT(208) - Transation abort</li>
     * <li>CTLS_ISSUERUPDATE_APPROVE(209) - Second tap, issuer update approve</li>
     * <li>CTLS_CARD_BLOCK(210) -  6A81 error card block</li>
     * <li>CTLS_SEL_FILE_INVALID(211) -  6283 error Selected file invalidated</li>
     *
     * <li>EMV_SEE_PHONE(150) - paypass result, please check the result on phone</li>
     * <li>QPBOC_KERNAL_INIT_FAILED(301) - CTLS kernel init failed</li>
     * </ul>
     *
     * @param data data of result
     * <ul>
     * <li>REVERSAL_DATA(String) - the string of reversal data</li>
     * <li>ERROR(String) - the error description ( from the result of EMV) </li>
     * <li style="text-decoration:line-through;">SIGNATURE(boolean) need to sign when result is "CTLS_TC(204)"</li>
     * <li>CTLS_CVMR(int) get ctls cvm when result is "CTLS_TC(204)"<br>
     *     |---0 NO_CVM<br>
     *     |---1 CVM_PIN<br>
     *     |---2 CVM_SIGN<br>
     *     |---3 CVM_CDCVM</li>
     * <li>CARD_TYPE(int) card type by CTLS<br>
     *    |---- 0 emv card <br>
     *    |---- 1 mstripe  card <br>
     *    |---- 2 other </li>
     * </ul>
     */
    void onTransactionResult(int result, in Bundle data);
}
