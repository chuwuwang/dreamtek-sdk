// ISmartCardReader.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl;
import com.dreamtek.smartpos.deviceservice.aidl.SmartCardStatusChangedEvent;

/**
 * the object of smart card (contact card, or IC card)
 */
interface ISmartCardReader {
	/**
     * power up the card
     *
	 * @return true for success, false for failure
	 * @
     *
	 */
	boolean powerUp();

	/**
     * power down the card
     *
	 * @return true for success, false for failure
	 * @
	 *
	 */
	boolean powerDown();

	/**
     * check if the card exist
     *
	 * @return true for available, false for unavailable
	 * @
	 */
	boolean isCardIn();

	/**
     * execute an apdu command
     *
	 * @param apdu apdu command input
	 * @return response of the command, null means no response got
	 * @
	 */
	byte[] exchangeApdu(in byte[] apdu);

	/**
     * check if the PSAM card exist
	 * @
     */
    boolean isPSAMCardExists();

	/**
     * check card status
	 * @return 0x00-card not exist, 0x01-card exist, 0x02-card power on
     *
     */
    byte checkCardStatus();

	/**
     * get ATR data of card power up
	 * @return ATR data
     *
     */
    byte[] getPowerUpATR();

	/**
     * power up the card
     * @param param the parameter
     * <ul>
     * <li>ATRCheck(boolean) enable/disable ATR check(default is enable)</li>
     * </ul>
	 * @return ATR data
     *
     */
    byte[] powerUpWithConfig(in Bundle param);

    /**
     *
     * @param timeout second, (1~120s)
     * @param callback
     * @throws RemoteException
     */
    void detectCardStatusChanged(int timeout, SmartCardStatusChangedEvent callback);
}
