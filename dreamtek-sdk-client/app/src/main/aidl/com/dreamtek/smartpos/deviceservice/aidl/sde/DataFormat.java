package com.dreamtek.smartpos.deviceservice.aidl.sde;

public class DataFormat {

    public static class DataType {
        public static final int DATA_TYPE_DEFAULT = 0;
        public static final int DATA_TYPE_CARD_PAN = 1;
        public static final int DATA_TYPE_TRACK1 = 2;
        public static final int DATA_TYPE_TRACK2 = 3;
        public static final int DATA_TYPE_TRACK3 = 4;
        public static final int DATA_TYPE_CARDHOLDER = 5;

    }
    public static class FormatType {
        public static final byte FORMAT_TYPE_ASCII = 0;
        public static final byte FORMAT_TYPE_BCD = 1;
    }

}
