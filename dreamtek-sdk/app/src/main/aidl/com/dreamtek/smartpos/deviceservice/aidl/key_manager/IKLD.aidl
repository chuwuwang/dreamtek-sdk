package com.dreamtek.smartpos.deviceservice.aidl.key_manager;

/**
 * Created by RuoYi
 * @
 */

interface IKLD {


    /**
     * KBPK and Master for AES are shared the same block & slots
     * So, please use them without conflict
     * @param slot the slot to inject the kbkp
     * @param kbpk the kbpk to be injected
     * @param extend KEY: overwrite(boolean), overwrite if not empty, otherwise return -1.
     *               KEY: checkValue(byte[]), the check value to verify the kbpk.
     * @return 0, success.
     *          -1, slot was injected a key, but not set overwrite or overwrite set to false.
     *          -2, inject failed, please if kbpk is correct or checkValue is correct.
     * @throws RemoteException
     */
    int loadKBPK( in int slot, String kbpk, in Bundle extend );

    /**
     *
     * @param slot the slot to inject the key.
     * @param data the data to be injected.
     * @param extend KEY KBPKSlot(int), the kbpk slot to decrypt the TR31.
     * @return 0 for success, others for error
     *              * 0x42 : Key length error
     *              * 0x64 : Other error
     *              * 0xA2 : invalid TR31 block
     * @throws RemoteException
     */
    int loadTR31Payload( in int slot, String data, in Bundle extend );
}
