package com.dreamtek.smartpos.deviceservice.aidl.sde;

public class DataFormat {

    public static class DataType {
        public static final String DATA_TYPE_CARD_PAN = "pan";
        public static final String DATA_TYPE_TRACK1 = "track1";
        public static final String DATA_TYPE_TRACK2 = "track2";
        public static final String DATA_TYPE_TRACK3 = "track3";
        public static final String DATA_TYPE_EXPIRED_DATE = "expiredDate";

    }
    public static class FormatType {
        public static final byte FORMAT_TYPE_ASCII = 0;
        public static final byte FORMAT_TYPE_BCD = 1;
    }

}
