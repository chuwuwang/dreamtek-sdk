package com.dreamtek.smartpos.deviceservice.aidl.sde;

/**
 * the listener of check card
 */
interface CardDetectionListener {
	/**
     * found magnetic card
     *
	 * @param track the track information
	 * <ul>
     * <li>ObfuscatedPAN(String) obfuscated PAN</li><BR>
     * <li>PAN(String) encrypted PAN</li><BR>
     * <li>KSN_PAN(String) KSN for encrypted PAN</li>
     * <li>CARD_HOLDER(String) Cardholder name<li><BR>
     * <li>TRACK1(String) encrypted track 1</li><BR>
     * <li>KSNTrk1(String) KSN for encrypted track 1</li><BR>
     * <li>TRACK2(String) encrypted track 2</li><BR>
     * <li>KSNTrk2(String) KSN for encrypted track 2</li><BR>
     * <li>TRACK3(String) encrypted track 3</li><BR>
     * <li>KSNTrk3(String) KSN for encrypted track 3</li><BR>
     * <li>SERVICE_CODE(String) the service code</li><BR>
     * <li>EXPIRED_DATE(String) encrypted expired date</li><BR>
     * <li>KSN_EXPIRED_DATE(String) KSN for encrypted expired date</li>
     * <li>IS_EXPIRED(boolean) true - card has expired, false - card hasn't expired</li><BR>
	 * </ul>
     * @
	 */
	void onCardSwiped(in Bundle track);
	
	/**
     * found smart card
     *
     * run the IPBOC#startEMV to start EMV workflow
     * @
     *
	 */
	void onCardPowerUp();
	
	/**
     * found contactless card
     *
     * run the IPBOC#startEMV to start EMV workflow
     * @
     *
	 */
	void onCardActivate();
	
	/**
     * timeout
     *
     * @
	 */
	void onTimeout();
	
	/**
     * While error got
     *
	 * @param error the error code
	 * <ul>
	 * <li>MAG_SWIPE_ERROR(1) - read magnetic error</li>
	 * <li>IC_INSERT_ERROR(2) - read smart card error</li>
	 * <li>IC_POWERUP_ERROR(3) - smart card cannot power up</li>
	 * <li>RF_PASS_ERROR(4) - read contactless card error</li>
	 * <li>RF_ACTIVATE_ERROR(5) - contactless card active error</li>
	 * <li>MULTI_CARD_CONFLICT_ERROR(6) - found multi-cards</li>
	 * <li>M1_CARD_UNSUPPORTED_EMV_ERROR(7) - [M1Sn]M1 card unsupported in EMV process</li>
	 * <li>FELICA_CARD_UNSUPPORTED_EMV_ERROR(8) - emv unsupported Felica card</li>
	 * <li>DESFIRE_CARD_UNSUPPORTED_EMV_ERROR(9) -[DesFireSN] DesFire card unsupported in EMV process</li>
	 * <li>NTAG_CARD_UNSUPPORTED_EMV_ERROR(10) -[NtagSN] Ntag card unsupported in EMV process</li>
	 * <li>ULTRALIGHT_CARD_UNSUPPORTED_EMV_ERROR(11) -[UltralightSN] Ultralight card unsupported in EMV process</li>
	 * <li>INVALID_PARAM_ERROR(100) - invalid params</li>
	 * <li>REQUEST_EXCEPTION(101) - encounter exception</li>
	 * <li>OTHER_ERROR(102) - other error</li>
	 * <li>SDE_ERROR(103) - SDE mode is true, pls replace ISde.checkEMVCard() interface</li>
	 * </ul>
	 * @param message the description.
     * @
	 */
	void onError(int error, String message);
}
