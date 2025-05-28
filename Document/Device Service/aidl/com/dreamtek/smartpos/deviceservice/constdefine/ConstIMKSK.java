package com.dreamtek.smartpos.deviceservice.constdefine;

public class ConstIMKSK {
    public class isKeyExist {
        public class keyType {
            public static final int Master_Key_3DES = 0x00;
            public static final int MAC_Key_3DES = 0x01;
            public static final int PIN_Key_3DES = 0x02;
            public static final int TD_Key_3DES = 0x03;
            public static final int TEK_Key_3DES = 0x04;

            public static final int Master_Key_AES = 0x10;
            public static final int MAC_Key_AES = 0x11;
            public static final int PIN_Key_AES = 0x12;
            public static final int TD_Key_AES = 0x13;
            public static final int TEK_Key_AES = 0x14;
        }
    }
    public class loadTEK {
        public class algorithmType {
            public static final int Encrypt_Key_3DES_ECB = 0x01;
            public static final int Plain_Key_3DES_ECB = 0x02;
            public static final int Encrypt_Key_SM4= 0x03;
            public static final int Plain_Key_SM4_ECB = 0x04;
            public static final int Encrypt_Key_AES_ECB = 0x05;
            public static final int Plain_Key_AES_ECB = 0x06;

            public static final int Encrypt_Key_3DES_CBC = 0x81;
            public static final int Plain_Key_3DES_CBC = 0x82;
            public static final int Encrypt_Key_SM4_CBC= 0x83;
            public static final int Plain_Key_SM4_CBC = 0x84;
            public static final int Encrypt_Key_AES_CBC = 0x85;
            public static final int Plain_Key_AES_CBC = 0x86;
        }
    }

    public class calculateByMasterKey {
        public class keyType {
            public static final int Master_Key = 0x01;
            public static final int SM4_Master_Key = 0x02;
            public static final int AES_Master_Key = 0x03;
        }

        public class algorithmMode {
            public static final int Encrypt_ECB = 0x00;
            public static final int Decrypt_ECB = 0x01;
            public static final int Encrypt_CBC = 0x02;
            public static final int Decrypt_CBC = 0x03;
        }
    }
}
