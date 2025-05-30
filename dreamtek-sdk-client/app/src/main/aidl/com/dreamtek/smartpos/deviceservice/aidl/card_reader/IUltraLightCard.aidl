// IUltraLightCard.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.card_reader;

interface IUltraLightCard {
    /**
     * execute write command
     * @param bAddress address of write
     * @param pData data need to write (16Byte)
     * @return 0:success other:fail
     * @
     */
    int	compatibilityWrite(byte bAddress, in byte[] pData);

    /**
     * init UltraLight card
     * @return 0:success other:fail
     * @
     */
    int	init();

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
     * Ultralight NANO Write signature command
     * @param bAddress address of need to write signature
     * @param pData 4 bytes data
     * @return 0-success other-fail
     * @deprecated
     */
    int	writeSign(byte bAddress, in byte[] pData);
}