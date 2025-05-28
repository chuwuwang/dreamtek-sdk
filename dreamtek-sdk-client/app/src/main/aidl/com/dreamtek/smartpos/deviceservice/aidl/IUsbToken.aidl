package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * <p> the object of usb port for token
 */
interface IUsbToken {
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
     * get status of the card
     *
     * @return -1:fail 0:power off 1:power up
     * @
     *
     */
    int getStatus();

    /**
     * execute an exchange command
     *
     * @param timeout format as ms
     * @param request request command
     * @param response response data
     * @return
     * @
     */
    boolean exchange( int timeout , in byte[] request ,inout byte[] response);


}