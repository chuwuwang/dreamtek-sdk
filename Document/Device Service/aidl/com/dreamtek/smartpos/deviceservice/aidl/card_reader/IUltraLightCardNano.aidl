// IUltraLightCard.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.card_reader;

interface IUltraLightCardNano {
    /**
     * init UltraLight card
     * @return 0:success other:fail
     * @
     */
    int	init();

    /**
     * execute write command
     * @param bAddress address of write
     * @param pData data need to write (16Byte)
     * @return 0:success other:fail
     * @
     */
    int	compatibilityWrite(byte bAddress, in byte[] pData);

    /**
     * @param bAddress
     * @return 16 bytes data of address, other is fail
     * @
     */
    byte[] read(byte bAddress);

    /**
     * @param bAddress address to write in
     * @param pData 4 bytes data
     * @return 0-success other-fail
     * @
     */
    int	write(byte bAddress, in byte[] pData);

    /**
     * Ultralight get version
     * @return get version
     * @
     */
    String getVersion();

    /**
     * Ultralight NANO Lock Signature command
     * @param bLockMode :
     *      0-Option to unlock the signature;
     *      1-Option to temporary lock the signature;
     *      2-Option to permanently lock the signature;
     * @return 0:success other:fail
     * @
     */
    int	lockSign(byte bLockMode);

    /**
     * Ultralight NANO read signature command
     * @param bAddr addr is always 0x00
     * @return 32 bytes data, other is fail
     * @
     */
    byte[] readSign(byte bAddr);

    /**
     * Ultralight NANO Write signature command
     * @param bAddress address of need to write signature
     * @param pData 4 bytes data
     * @return 0-success other-fail
     * @
     */
    int	writeSign(byte bAddress, in byte[] pData);
}