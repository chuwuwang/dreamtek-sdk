package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * the listener of Issuer Update request
 */
interface IssuerUpdateHandler {
    /**
     * set callback for request issuer update script(CTLS request second tap)
     *
     */
    void onRequestIssuerUpdate();
}

