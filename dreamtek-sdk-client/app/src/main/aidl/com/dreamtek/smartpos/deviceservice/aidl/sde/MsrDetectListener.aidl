package com.dreamtek.smartpos.deviceservice.aidl.sde;

/**
 * the call back of magnetic card
 */
interface MsrDetectListener {
	/**
     * detect magnetic card successfully
     *
	 * @param track the card information
     * <ul>
     * <li>ObfuscatedPAN(String) obfuscated PAN</li><BR>
     * <li>PAN(String) encrypted PAN</li><BR>
     * <li>KSN_PAN(String) KSN for encrypted PAN</li>
     * <li>Cardholder(String) Cardholder name<li><BR>
     * <li>TRACK1(String) encrypted track 1</li><BR>
     * <li>KSN1(String) KSN for encrypted track 1</li><BR>
     * <li>TRACK2(String) encrypted track 2</li><BR>
     * <li>KSN2(String) KSN for encrypted track 2</li><BR>
     * <li>SERVICE_CODE(String) the service code</li><BR>
     * <li>EXPIRED_DATE(String) encrypted expired date</li><BR>
     * <li>KSN_EXPIRED_DATE(String) KSN for encrypted expired date</li>
     * </ul>
	 * @
	 */
	void onSuccess(in Bundle track);

	/**
	 * detect magnetic card failed
     *
     * @param error - the error code
     * <ul>
     * <li>REQUEST_EXCEPTION(100) - request exception</li>
     * </ul>
     * @param message - the message
	 * @
     */
     void onError(int error,String message );
	
	/**
	 * timeout occur
	 * @
	 */
	void onTimeout();
}
