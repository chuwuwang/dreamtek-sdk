package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * the listener of scanner
 *
 */
interface ScannerListener {
	/**
     * Scan code successfully
     *
	 * @param barcode the barcode string
     * @
	 */
    void onSuccess(String barcode);
    
    /**
     * Scan code error
     *
     * @param error the error code  1-scan failed
     * @param message the message of the error
     * @
     */
    void onError(int error, String message);
    
	/**
     * Scan timeout
     * @
	 */
    void onTimeout();
    
    /**
     * Scan cancel
     * @
     */
    void onCancel();
}
