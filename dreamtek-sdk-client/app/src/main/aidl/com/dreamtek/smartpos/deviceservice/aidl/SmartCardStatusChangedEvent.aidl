package com.dreamtek.smartpos.deviceservice.aidl;

/**
 * the listener of event
 *
 */
interface SmartCardStatusChangedEvent {
    void onChanged(int statusNow); //0x00-card not exist, 0x01-card exist, 0x02-card power on
    
    void onTimeout();

    void onError(int errorCode); // 0xF1-invalid params
}
