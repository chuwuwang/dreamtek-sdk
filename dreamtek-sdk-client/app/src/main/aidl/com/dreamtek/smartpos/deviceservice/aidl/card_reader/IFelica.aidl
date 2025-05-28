// IFelica.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.card_reader;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.FelicaListener;

interface IFelica {

	/**
     * Felica power on
	 * @return true on success, false on failure
     * @
	 */
    boolean powerOn();

	/**
     * Felica power off
	 * @return true on success, false on failure
     * @
	 */
    boolean powerOff();


	/**
     * search card
     *
	 * @param conflictType 1-conflict slot 1 2-conflict slot 2 4-confilict slot 4 8-confilict slot 8 16-conflict slot 16
	 * @param systemNumOne  System number 1, default to 0xFF, indicating the use of wildcard settings
	 * @param systemNumTwo  System number 2, default to 0xFF, indicating the use of wildcard settings
	 * @param requestType 0-FELICA_REQ_NO_REQUEST 1-FELICA_REQ_SYSTEM_CODE 2-FELICA_REQ_COM_PERFORMANCE
	 * @param listener the call back listener
	 * @param timeout {@code timeout in ms, should >= 1000}
     * @
     *
	 */
    void searchCard(int conflictType, byte systemNumOne, byte systemNumTwo, int requestType, in FelicaListener listener, int timeout);

	/**
     * communicate command
     *
	 * @param sendData - the command
	 * @return the response data
     * @
     *
	 */
    byte[] communicate(in byte[] sendData);

	/**
     * stop search card
     * @
	 */
    void stopSearch();
}
