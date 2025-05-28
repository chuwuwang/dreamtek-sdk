package com.dreamtek.smartpos.deviceservice.aidl;

import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;

/**
 * the object of PIN pad
 * download keys, data encrypt, pin input
 */
interface IPinpad {
    /**
     * start PIN input
     *
     * @param keyId the index of PIN KEY(0~254) or dukpt key (0~4)
     * @param param the parameter
     * <ul>
     * <li>pinLimit(byte[]) the valid length(s) array of the PIN (such as {0,4,5,6} means the valid length is 0, 4~6, maximum value is 12)</li>
     * <li>timeout(int) the timeout, second</li>
     * <li>isOnline(boolean) is a online PIN</li>
     * <li>promptString(String) the prompt string</li>
     * <li>pan(String) the pan for encrypt online PIN</li>
     * <li>desType(int) calculate type<BR>
     *   |----0x01 MK/SK + 3DES (default)<BR>
     *   |----0x02 MK/SK + AES<BR>
     *   |----0x03 MK/SK + SM4<BR>
     *   |----0x04 DUKPT + 3DES<BR>
     *   |----0x05 DUKPT + AES<BR>
     * </li>
     * <li>numbersFont(String) - url of numbers ttf font (value "" is android system fonts)</li>
     * <li>promptsFont(String) - url of prompt ttf font(value "" is android system fonts)</li>
     * <li>otherFont(String) - url of other ttf font(confirm button & backspace button)(value "" is android system fonts)</li>
     * <li>displayKeyValue(byte[]) - custom the sequence key number of pinpad</li>
     * <li>random(byte[]) - random number participation in pinblock calculation(default not set)</li>
     * <li>notificatePinLenError(boolean) - Notification password is not long enough(default false)</li>
     * <li>pinFormatType(int) - default is format0, 0-format0, 1-format1, 2-format2, 3-format3, 4-format4</li>
     * <li>dispersionType(byte) - 0-DES112, 1-DES168, 2-AES128(default), 3-AES192, 4-AES256</li>
     * </ul>
     * @param listener the call back listener
     * @param globalParam - set global display (if set null, 0~9 are Arabic numerals and confirm/backspace button are english display)
     * <ul>
     *     <li>Display_One(String)</li>
     *     <li>Display_Two(String)</li>
     *     <li>Display_Three(String)</li>
     *     <li>Display_Four(String)</li>
     *     <li>Display_Five(String)</li>
     *     <li>Display_Six(String)</li>
     *     <li>Display_Seven(String)</li>
     *     <li>Display_Eight(String)</li>
     *     <li>Display_Nine(String)</li>
     *     <li>Display_Zero(String)</li>
     *     <li>Display_Confirm(String)</li>
     *     <li>Display_BackSpace(String)</li>
     * </ul>
     * @return
	 * @
     */
	void startPinInput(int keyId, in Bundle param, in Bundle globleParam, PinInputListener listener);


    /**
     * stop the pin input
	 */
    void stopPinInput();
    


	Map initPinInputCustomView(int keyId, in Bundle param, in List<PinKeyCoorInfo> pinKeyInfos, PinInputListener listener);

	/**
     * Execute this interface to activate pinpad.
     *
	 * If you get Map<string string>, you should traversal the map to get the value of key to display.
	 * @
	 * */
	void startPinInputCustomView();

	/**
     * stop custom pinpad
     *
	 * @
	 */
	void endPinInputCustomView();





}
