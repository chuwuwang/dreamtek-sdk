package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * the call back of magnetic card
 */
interface MagCardListener {
	/**
     * detect magnetic card successfully
     *
	 * @param track the card information
     * <ul>
     * <li>PAN(String) the PAN</li><BR>
     * <li>TRACK1(String) the track 1</li><BR>
     * <li>TRACK2(String) track 2</li><BR>
     * <li>TRACK3(String) track 3</li><BR>
     * <li>SERVICE_CODE(String) the service code</li><BR>
     * <li>EXPIRED_DATE(String) the expired date</li><BR>
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
     * <li>SRED_EXCEPTION(101) - sred mode exception, use iSde.checkEMVCard() instead</li>
     * <li>PARAM_EXCEPTION(102) - invalid parameter exception</li>
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
