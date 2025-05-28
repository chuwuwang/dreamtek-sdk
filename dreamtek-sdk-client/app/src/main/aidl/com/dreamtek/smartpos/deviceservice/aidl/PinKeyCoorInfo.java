package com.dreamtek.smartpos.deviceservice.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class PinKeyCoorInfo extends KeyCoorInfo {
    protected PinKeyCoorInfo(Parcel in) {
        super(in);
    }

    public static final Creator<PinKeyCoorInfo> CREATOR = new Creator<PinKeyCoorInfo>() {
        @Override
        public PinKeyCoorInfo createFromParcel(Parcel in) {
            return new PinKeyCoorInfo(in);
        }

        @Override
        public PinKeyCoorInfo[] newArray(int size) {
            return new PinKeyCoorInfo[size];
        }
    };

    public PinKeyCoorInfo(String keyName, int coor1_x, int coor1_y, int coor2_x, int coor2_y, int keyType) {
        super(keyName, coor1_x, coor1_y, coor2_x, coor2_y, keyType);
    }
}
