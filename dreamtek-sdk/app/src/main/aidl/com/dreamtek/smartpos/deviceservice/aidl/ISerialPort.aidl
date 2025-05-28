package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * <p> the object of serial port for serial communication
 *
 * @see IDeviceService#getSerialPort
 *
 * @author: baoxl
 */
interface ISerialPort {
	/**
     * <p> open serial port
     *
	 * @return {@code true} : success, {@code false} : fail
     * @
	 */
	boolean open();
	
	/**
     * <p> close serial port
     *
	 * @return {@code true} : success, {@code false} : fail
     * @
     *
	 */
	boolean close();
	
	/**
     * <p> initialize serial port
     *
	 * @param bps the bps
	 * <ul>
     * <li>1200 </li><BR>
     * <li>2400 </li><BR>
     * <li>4800 </li><BR>
     * <li>9600 </li><BR>
     * <li>14400 </li><BR>
     * <li>19200 </li><BR>
     * <li>28800 </li><BR>
     * <li>38400 </li><BR>
     * <li>57600 </li><BR>
     * <li>115200 </li><BR>
     * </ul>
	 * @param par
	 * <ul>
     * <li>0 - no check</li><BR>
     * <li>1 - odd</li><BR>
     * <li>2 - even</li><BR>
     * </ul>
	 * @param dbs
	 * @return {@code true} : success, {@code false} : fail
     * @
	 */
	boolean init(int bps, int par, int dbs); 
	
	/**
     * @brief read data
     *
	 * @param buffer the buffer
	 * @param timeout the timeout in millisecond
	 * @return the length read, or -1 on failure
     * @
	 */
	int read(inout byte[] buffer, int expectLen, int timeout);
	
    /**
     * <p> write data
     *
	 * @param data	the data buffer
	 * @param timeout	the timeout in millisecond
     * @param bundle For future expansion
	 * @return the length wrote, or -1 on failure
	 */
	int write(in byte[] data, int timeout, in Bundle bundle);

	/**
     * <p> clean up the input(read) buffer
     *
	 * @return {@code true} : success, {@code false} : fail
     * @
	 */
	boolean clearInputBuffer();
	
	/**
     * <p> check if there is data in buffer
     *
	 * @param {@code true}: check input(read) buffer, {@code false}: check output(write) buffer
	 * @return {@code true}: data available,  {@code false}: no data available
     * @
	 */
	 boolean isBufferEmpty(boolean input);

}
