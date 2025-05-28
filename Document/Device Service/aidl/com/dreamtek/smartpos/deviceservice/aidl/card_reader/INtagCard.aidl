// INtag.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.card_reader;


interface INtagCard {

	/**
     * obtain Ntag Library version message
	 * @return library version message
     * \en_e
     * \code{.java}
     * \endcode
     * @
     * @see
	 */
    byte[]	getVersion();

	/**
     * init NTag card(Need to reinitialize when the card is removed or powered off)
	 * @return 0-success other-fail
     * @
	 */
    int init();

	/**
     * authenticate through pwd
	 * @return 0-success other-fail
     * @
	 */
    int	pwdAuth(in byte[] pwd);

	/**
     * Read 4 pages of data (16 bytes) starting from the specified address
	 * @return 4 pages of data (16 bytes) starting from the specified address
     * @
	 */
    byte[]	read(byte addr);

	/**
     * read data from addrStart to addrEnd
	 * @return data of addrStart to addrEnd
     * @
	 */
    byte[]	fastRead(byte addrStart, byte addrEnd);

	/**
     * Write a page of data (4 bytes) to the specified address
     * @param  addr - Page address to be written
     * @param  dataBuf - Data to be written (4 bytes)
	 * @return 0-success other-fail
     * @
	 */
    int write(byte addr, in byte[] dataBuf);

	/**
     * Read signature data(The signature data is a 32-byte ECC data containing the chip manufacturer’s identification)
	 * @return null-failed
     * @
	 */
    byte[]	readSig();

	/**
     * Read counter
	 * @return null-failed
     * @
	 */
    byte[]	readCnt();
}