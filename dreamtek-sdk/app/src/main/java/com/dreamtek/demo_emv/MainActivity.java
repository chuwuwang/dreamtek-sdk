package com.dreamtek.demo_emv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamtek.smartpos.deviceservice.aidl.CheckCardListener;
import com.dreamtek.smartpos.deviceservice.aidl.EMVHandler;
import com.dreamtek.smartpos.deviceservice.aidl.IBeeper;
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IMKSK;
import com.dreamtek.smartpos.deviceservice.constdefine.*;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.OnlineResultHandler;
import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dreamtek.demo_emv.Utilities.*;
import com.dreamtek.demo_emv.basic.ISO8583;
import com.dreamtek.demo_emv.caseA.ISO8583u;
import com.dreamtek.demo_emv.usecase.EmvSetAidRid;

/**
 * \Brief this a EMV workflow demo
 * <p>
 * Here you can find how to start EMV, build the 8583 packet, transfer packet from server
 * start pinpad, download AID, RID
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EMVDemo";
    public static IDeviceService idevice;

    IEMV iemv;
    IPinpad ipinpad;
    IMKSK imksk;
    IDukpt iDukpt;
    IBeeper iBeeper;

    EMVHandler emvHandler;
    PinInputListener pinInputListener;

    Button btnCheckCard;
    Button btnPinPad;
    Button btnSetKeys;
    Button btnSetAID;
    Button btnSetRID;
    Button btnClearAID;
    Button btnClearRID;
    Button btnTest;
    Button btnTransSignIn;
    Button btnTransBalance;
    Button btnPurchase;
    Button btnCustomPin;

    EditText edIP;
    EditText edPort;
    EditText edAmount;

    String hostIP = null;
    int hostPort;

    // some client static
    String terminalID = "01020304";
    String merchantName = "X990 EMV Demo";
    String merchantID = "ABCDE0123456789";

    // keys
    int mainKeyId = 97;
    int workKeyId = 1;

//    String pinKey_WorkKey = "B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E";
    String pinKey_WorkKey = "B0BCE9315C0AA31E5E6667A037DE0ABC";
    String macKey = "";
    String mainKey_MasterKey = "758F0CD0C866348099109BAF9EADFA6E";

//    String savedPan = "8880197100005603384";
    String savedPan = "0197100005603384";
    byte[] savedPinblock = null;


    SparseArray<String> data8583_i = null;
    /**
     * field-value map to save iso data
     */
    SparseArray<String> data8583 = null;
    SparseArray<String> tagOfF55 = null;

    /**
     * \Brief the transaction type
     * <p>
     * Prefix
     * T_ means transaction
     * M_ means management
     */
    enum TransType {
        T_BANLANCE, T_PURCHASE,
        M_SIGNIN
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheckCard = findViewById(R.id.btnCheckCard);
        btnCheckCard.setOnClickListener(onClickListener);

        btnPinPad = findViewById(R.id.btnPinPad);
        btnPinPad.setOnClickListener(onClickListener);

        btnSetKeys = findViewById(R.id.btnSetKeys);
        btnSetKeys.setOnClickListener(onClickListener);

        btnSetAID = findViewById(R.id.btnSetAID);
        btnSetAID.setOnClickListener(onClickListener);

        btnSetRID = findViewById(R.id.btnRID);
        btnSetRID.setOnClickListener(onClickListener);

        btnClearAID = findViewById(R.id.btnClearAID);
        btnClearAID.setOnClickListener(onClickListener);

        btnClearRID = findViewById(R.id.btnClearRID);
        btnClearRID.setOnClickListener(onClickListener);

        btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(onClickListener);

        btnTransSignIn = findViewById(R.id.btnTransSignIn);
        btnTransSignIn.setOnClickListener(onClickListener);

        btnTransBalance = findViewById(R.id.btnBalance);
        btnTransBalance.setOnClickListener(onClickListener);

        btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(onClickListener);

        btnCustomPin = findViewById(R.id.btnCustomPinpad);
        btnCustomPin.setOnClickListener(onClickListener);

        edIP = findViewById(R.id.edIP);
        if (null == edIP) {
            Log.e(TAG, "cannot get the IP edit");
        }
        edPort = findViewById(R.id.edPort);
        if (null == edPort) {
            Log.e(TAG, "cannot get the Port edit");
        }
        edAmount = findViewById(R.id.edAmount);

        initialize8583data();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("com.dreamtek.smartpos.device_service");
        intent.setPackage("com.dreamtek.smartpos.deviceservice");
        boolean isSucc = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        if (!isSucc) {
            Log.i("TAG", "deviceService connect fail!");
        } else {
            Log.i("TAG", "deviceService connect success");
            initializeEMV();
            initializePinInputListener();
        }

    }

    // button -- start
    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnCheckCard) {
                doBalance();
            } else if (view == btnPinPad) {
                doPinPad();
            } else if (view == btnSetKeys) {
                doSetKeys();
            } else if (view == btnSetAID) {
                doSetAID(1);
            } else if (view == btnClearAID) {
                doSetAID(3);
            } else if (view == btnSetRID) {
                doSetRID(1);
            } else if (view == btnClearRID) {
                doSetRID(3);
            } else if (view == btnTransSignIn) {
                if (null == edIP) {
                    Log.e(TAG, "cannot get the IP edit");
                }
                if (null == edPort) {
                    Log.e(TAG, "cannot get the Port edit");
                }

                hostIP = edIP.getText().toString();
                String port = edPort.getText().toString();
                if (port.length() == 0) {
                    Log.e(TAG, "cannot read port");
                    hostPort = 5555;
                } else {
                    hostPort = Integer.valueOf(port);
                }
                Log.e(TAG, "Host:" + hostIP + ":" + hostPort);

                doSignIn();
            } else if (view == btnTransBalance) {
                if (null == hostIP) {
                    toastShow("Sign In first!");
                    // return;
                }
                doBalance();

            } else if (view == btnPurchase) {
                doPurchase();
            } else if (view == btnTest) {
                // test redefine the AID to kernel
                Log.d(TAG, "re-define the AID to kernel");
                Map<String, Integer> map = new HashMap<>();
                map.put("A000000003", CTLSKernelID.CTLS_KERNEL_ID_01_VISA); // define the AID - Kernel may
                map.put("A000000004", CTLSKernelID.CTLS_KERNEL_ID_01_VISA); // define the AID - Kernel may

//                try {
//                    iemv.registerKernelAID( map);   // set to kernel
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                Log.d(TAG, "re-define the AID to kernel -- done");
            } else if (view == btnCustomPin) {
                doStartCustonViewPinpad();
            }
        }
    };

    // connect service -- start
    /**
     */
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            idevice = IDeviceService.Stub.asInterface(service);
            try {
                iemv = idevice.getEMV();
                ipinpad = idevice.getPinpad(1);
                iBeeper = idevice.getBeeper();
                imksk = idevice.getMKSK();
                iDukpt = idevice.getDUKPT();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "bind service success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    // connect service -- end

    // log & display
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String string = msg.getData().getString("string");
            super.handleMessage(msg);
            Log.d(TAG, msg.getData().getString("msg"));
            Toast.makeText(MainActivity.this, msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();

        }
    };

    void toastShow(String str) {
        Message msg = new Message();
        msg.getData().putString("msg", str);
        handler.sendMessage(msg);
    }


    void initializeEMV() {

        /**
         * \brief initialize the call back listener of EMV
         *
         *  \code{.java}
         * \endcode
         * @version
         * @see
         *
         */
        emvHandler = new EMVHandler.Stub() {
            @Override
            public void onRequestAmount() throws RemoteException {
                // this is an deprecated callback
//                toastShow("onRequestAmount...");
//                iemv.importAmount(234);
            }

            @Override
            public void onSelectApplication(List<Bundle> appList) throws RemoteException {
                for (Bundle aidBundle : appList) {
                    String aidName = aidBundle.getString("aidName");
                    String aid = aidBundle.getString("aid");
                    String aidLabel = aidBundle.getString("aidLabel");
                    Log.i(TAG, "AID Name=" + aidName + " | AID Label=" + aidLabel + " | AID=" + aid);
                }
                toastShow("onSelectApplication..." + appList.get(0));
                iemv.importAppSelection(0);
            }

            /**
             * \brief confirm the card info
             *
             * show the card info and import the confirm result
             * \code{.java}
             * \endcode
             *
             */
            @Override
            public void onConfirmCardInfo(Bundle info) throws RemoteException {
                Log.d(TAG, "onConfirmCardInfo...");
                savedPan = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_PAN_String);

                String result = "onConfirmCardInfo callback, " +
                        "\nPAN:" + savedPan +
                        "\nTRACK2:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String) +
                        "\nCARD_SN:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_CARD_SN_String) +
                        "\nSERVICE_CODE:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_SERVICE_CODE_String) +
                        "\nEXPIRED_DATE:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_EXPIRED_DATE_String);

                byte[] tlv = iemv.getCardData("9F51");
                result += ("\n9F51:" + Utility.byte2HexStr(tlv));

                String track2 = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String);
                if (null != track2) {
                    int a = track2.indexOf('D');
                    if (a > 0) {
                        track2 = track2.substring(0, a);
                    }
                    data8583.put(ISO8583u.F_Track_2_Data_35, track2);
                }

                toastShow("onConfirmCardInfo:" + result);

                iemv.importCardConfirmResult(ConstIPBOC.importCardConfirmResult.pass.allowed);
            }

            /**
             * \brief show the pin pad
             *
             *  \code{.java}
             * \endcode
             *
             */
            @Override
            public void onRequestInputPIN(boolean isOnlinePin, int retryTimes) throws RemoteException {
                toastShow("onRequestInputPIN isOnlinePin:" + isOnlinePin);
                // show the pin pad, import the pin block
                doPinPad();
            }

            @Override
            public void onConfirmCertInfo(String certType, String certInfo) throws RemoteException {
                toastShow("onConfirmCertInfo, type:" + certType + ",info:" + certInfo);

                iemv.importCertConfirmResult(ConstIPBOC.importCertConfirmResult.option.CONFIRM);
            }

            @Override
            public void onRequestOnlineProcess(Bundle aaResult) throws RemoteException {
                Log.d(TAG, "onRequestOnlineProcess...");
                int result = aaResult.getInt(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_RESULT_int);
                boolean signature = aaResult.getBoolean("SIGNATURE");
                toastShow("onRequestOnlineProcess result=" + result + " signal=" + signature);
                switch (result) {
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_AARESULT_ARQC:
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_QPBOC_ARQC:
                        toastShow(aaResult.getString(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_ARQC_DATA_String));
                        break;
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_PAYPASS_EMV_ARQC:
                        break;
                }

                byte[] tlv;
                tagOfF55 = new SparseArray<>();

                int[] tagList = {
                        0x9F26,
                        0x9F27,
                        0x9F10,
                        0x9F37,
                        0x9F36,
                        0x95,
                        0x9A,
                        0x9C,
                        0x9F02,
                        0x5F2A,
                        0x82,
                        0x9F1A,
                        0x9F03,
                        0x9F33,
                        0x9F74,
                        0x9F24,
                };

                for (int tag : tagList) {
                    tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                    if (null != tlv && tlv.length > 0) {
                        Log.d(TAG, Utility.byte2HexStr(tlv));
                        tagOfF55.put(tag, Utility.byte2HexStr(tlv));  // build up the field 55
                    } else {
                        Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                    }
                }

                // set the pin block
                data8583.put(ISO8583u.F_PINData_52, Utility.byte2HexStr(savedPinblock));


                Log.d(TAG, "start online request");
                onlineRequest.run();
                Log.d(TAG, "online request finished");

                // import the online result
                Bundle onlineResult = new Bundle();
                onlineResult.putBoolean(ConstIPBOC.inputOnlineResult.onlineResult.KEY_isOnline_boolean, true);
                if (isoResponse.unpackValidField[ISO8583u.F_ResponseCode_39]) {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, isoResponse.getUnpack(ISO8583u.F_ResponseCode_39));
                } else {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_respCode_String, "00");
                }

                if (isoResponse.unpackValidField[ISO8583u.F_AuthorizationIdentificationResponseCode_38]) {
                    //
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, isoResponse.getUnpack(ISO8583u.F_AuthorizationIdentificationResponseCode_38));
                } else {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, "123456");
                }

                // onlineResult.putString( ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
                if (isoResponse.unpackValidField[55]) {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, isoResponse.getUnpack(55));

                } else {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, "5F3401019F3303E0F9C8950500000000009F1A0201569A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0201569F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
                }
//                onlineResult.putBoolean("getPBOCData", true);
//                onlineResult.putInt("importAppSelectResult", 1);
//                onlineResult.putInt("IsPinInput", 1);
//                onlineResult.putString("importPIN", "123456");
//                onlineResult.putInt("importAmount", 101);
//                onlineResult.putBoolean("cancelCardConfirmResult", false);


                iemv.inputOnlineResult(onlineResult, new OnlineResultHandler.Stub() {
                    @Override
                    public void onProccessResult(int result, Bundle data) throws RemoteException {
                        Log.i(TAG, "onProccessResult callback:");
                        String str = "RESULT:" + result +
                                "\nTC_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_TC_DATA_String, "not defined") +
                                "\nSCRIPT_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_SCRIPT_DATA_String, "not defined") +
                                "\nREVERSAL_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_REVERSAL_DATA_String, "not defined");
                        toastShow(str);

                        switch (result) {
                            case ConstOnlineResultHandler.onProccessResult.result.TC:
                                toastShow("TC");
                                break;
                            case ConstOnlineResultHandler.onProccessResult.result.Online_AAC:
                                toastShow("Online_AAC");
                                break;
                            default:
                                toastShow("error, code:" + result);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onTransactionResult(int result, Bundle data) throws RemoteException {
                Log.d(TAG, "onTransactionResult");
                String msg = data.getString("ERROR");
                toastShow("onTransactionResult result = " + result + ",msg = " + msg);

                switch (result) {

                    case ConstPBOCHandler.onTransactionResult.result.EMV_CARD_BIN_CHECK_FAIL:
                        // read card fail
                        toastShow("read card fail");
                        return;
                    case ConstPBOCHandler.onTransactionResult.result.EMV_MULTI_CARD_ERROR:
                        // multi-cards found
                        toastShow(data.getString(ConstPBOCHandler.onTransactionResult.data.KEY_ERROR_String));
                        return;
                }

            }
        };
    }

    void doSearchCard(final TransType transType) {
        toastShow("start check card\nUse you card please");
        Bundle cardOption = new Bundle();
//        cardOption.putBoolean( ConstIPBOC.checkCard.cardOption.KEY_Contactless_boolean, ConstIPBOC.checkCard.cardOption.VALUE_unsupported);
        // cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_Contactless_boolean, ConstIPBOC.checkCard.cardOption.VALUE_supported);
        cardOption.putBoolean("supportCTLSCard", true);
//        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_SmartCard_boolean, ConstIPBOC.checkCard.cardOption.VALUE_supported);
        cardOption.putBoolean("supportSmartCard", true);
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_MagneticCard_boolean, ConstIPBOC.checkCard.cardOption.VALUE_unsupported);


        try {
            iemv.checkCard(cardOption, 30, new CheckCardListener.Stub() {
                        @Override
                        public void onCardSwiped(Bundle track) throws RemoteException {
                            Log.d(TAG, "onCardSwiped ...");
//                            iemv.stopCheckCard();
//                            iemv.abortPBOC();

                            iBeeper.startBeep(200);

                            String pan = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_PAN_String);
                            String track1 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK1_String);
                            String track2 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK2_String);
                            String track3 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK3_String);
                            String serviceCode = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_SERVICE_CODE_String);

                            Log.d(TAG, "onCardSwiped ...1");
                            byte[] bytes = Utility.hexStr2Byte(track2);
                            Log.d(TAG, "Track2:" + track2 + " (" + Utility.byte2HexStr(bytes) + ")");

                            Boolean bIsKeyExist = iDukpt.isKeyExist(1, 0x01,null);
                            if (!bIsKeyExist) {
                                Log.e(TAG, "no key exist type: 12, @: 1");
                            }
//                            byte[] enctypted = iDukpt.dukptEncryptData(1, 1, 1, bytes, new byte[]{0, 0, 0, 0, 0, 0, 0, 0,});
                            Bundle bundle = iDukpt.calculateData(1, 1, 1, bytes, new byte[]{0, 0, 0, 0, 0, 0, 0, 0,},null);
                            byte[] enctypted =null;
                            if(bundle!=null){
                                enctypted = bundle.getByteArray("encryptedData");
                            }
                            if (null == enctypted) {
                                Log.e(TAG, "NO DUKPT Encrypted got");
                            } else {
                                Log.d(TAG, "DUKPT:" + Utility.byte2HexStr(enctypted));
                            }
                            bIsKeyExist = iDukpt.isKeyExist(1, 0x01,null);
                            if (!bIsKeyExist) {
                                Log.e(TAG, "no key exist type: 12, @: 1");
                            }


                            if (null != track3) {
                                data8583.put(ISO8583u.F_Track_3_Data_36, track3);

                            }
                            String validDate = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_EXPIRED_DATE_String);
                            if (null != validDate) {
                                data8583.put(ISO8583u.F_DateOfExpired_14, validDate);
                            }
                            Log.d(TAG, "onCardSwiped ...3");
                            onlineRequest.run();
                            toastShow("response:" + isoResponse.getField(ISO8583u.F_ResponseCode_39));
                        }

                        @Override
                        public void onCardPowerUp() throws RemoteException {
                            iemv.stopCheckCard();
                            iemv.abortEMV();
                            iBeeper.startBeep(200);
                            List<String> tags = new ArrayList<String>();
                            tags.add("5F2A020840");

                            iemv.setEMVData( tags );
                            doEMV( ConstIPBOC.startEMV.intent.VALUE_cardType_smart_card , transType );
                        }

                        @Override
                        public void onCardActivate() throws RemoteException {
                            iemv.stopCheckCard();
                            iemv.abortEMV();
                            iBeeper.startBeep(200);
                            doEMV(ConstIPBOC.startEMV.intent.VALUE_cardType_contactless, transType);

                        }

                        @Override
                        public void onTimeout() throws RemoteException {
                            toastShow("timeout");
                        }

                        @Override
                        public void onError(int error, String message) throws RemoteException {
                            toastShow("error:" + error + ", msg:" + message);
                        }
                    }
            );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * \brief sample of EMV
     * <p>
     * \code{.java}
     * \endcode
     *
     * @see
     */
    void doEMV(int type, TransType transType) {
        //
        Log.i(TAG, "start EMV demo");

        Bundle emvIntent = new Bundle();
        emvIntent.putInt(ConstIPBOC.startEMV.intent.KEY_cardType_int, type);
        if (transType == TransType.T_PURCHASE) {
            emvIntent.putLong(ConstIPBOC.startEMV.intent.KEY_authAmount_long, Long.valueOf(edAmount.getText().toString()));
        }
        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_merchantName_String, merchantName);

        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_merchantId_String, merchantID);  // 010001020270123
        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_terminalId_String, terminalID);   // 00000001
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportQ_boolean, ConstIPBOC.startEMV.intent.VALUE_supported);
//        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportQ_boolean, ConstIPBOC.startEMV.intent.VALUE_unsupported);
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportSM_boolean, ConstIPBOC.startEMV.intent.VALUE_supported);
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isQPBOCForceOnline_boolean, ConstIPBOC.startEMV.intent.VALUE_unforced);
        if (type == ConstIPBOC.startEMV.intent.VALUE_cardType_contactless) {   // todo, check here
            emvIntent.putByte(ConstIPBOC.startEMV.intent.KEY_transProcessCode_byte, (byte) 0x00);
        }
        emvIntent.putBoolean("isSupportPBOCFirst", false);
        emvIntent.putString("transCurrCode", "0156");
        emvIntent.putString("otherAmount", "0");

        try {
            iemv.startEMV(ConstIPBOC.startEMV.processType.full_process, emvIntent, emvHandler);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * \brief set main key and work key
     * <p>
     * \code{.java}
     * \endcode
     *
     * @see
     */
    void doSetKeys() {
        // Load Main key
        // 758F0CD0C866348099109BAF9EADFA6E
        boolean bRet;
        try {
//            bRet = ipinpad.loadMainKey(mainKeyId, Utility.hexStr2Byte(mainKey_MasterKey), null);
            bRet = imksk.loadPlainMasterKey(mainKeyId, Utility.hexStr2Byte(mainKey_MasterKey), 0x02,null);
            toastShow("loadMainKey:" + bRet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String DukptSN = "01020304050607080900";
        String dukptKey = "34343434343434343434343434343434";


        // Load work key
        // 89B07B35A1B3F47E89B07B35A1B3F488
        try {
//            bRet = ipinpad.loadWorkKey(PinpadKeyType.PINKEY, mainKeyId, workKeyId, Utility.hexStr2Byte(pinKey_WorkKey), null);
            bRet = imksk.loadSessionKey(2, mainKeyId, workKeyId, 0x00,Utility.hexStr2Byte(pinKey_WorkKey), null,new Bundle());
            toastShow("loadWorkKey:" + bRet);

//            bRet = ipinpad.loadDukptKey(1, Utility.hexStr2Byte(DukptSN), Utility.hexStr2Byte(dukptKey), null);
            bRet = iDukpt.loadDukptKey(1, Utility.hexStr2Byte(DukptSN), Utility.hexStr2Byte(dukptKey), null,new Bundle());
            toastShow("loadDukptKey:" + bRet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * \brief show the pinpad
     * <p>
     * \code{.java}
     * \endcode
     */
    void doPinPad(){
        Bundle param = new Bundle() ;
        Bundle globeParam = new Bundle() ;
        String panBlock = savedPan ;
        byte [] pinLimit = {4,5, 6};
        param.putByteArray(ConstIPinpad.startPinInput.param.KEY_pinLimit_ByteArray, pinLimit) ;
        param.putInt( ConstIPinpad.startPinInput.param.KEY_timeout_int, 20 );
        param.putBoolean( ConstIPinpad.startPinInput.param.KEY_isOnline_boolean, true );
        param.putString( ConstIPinpad.startPinInput.param.KEY_pan_String , panBlock) ;
        param.putInt( ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_3DES);
        globeParam.putString( ConstIPinpad.startPinInput.globleParam.KEY_Display_One_String, "[1]");
        try {
            ipinpad.startPinInput(workKeyId , param , globeParam, pinInputListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    /**
     * \brief initialize the pin pad listener
     * <p>
     * \code{.java}
     * \endcode
     */
    void initializePinInputListener() {
        pinInputListener = new PinInputListener.Stub() {
            @Override
            public void onInput(int len, int key) throws RemoteException {
                Log.d(TAG, "PinPad onInput, len:" + len + ", key:" + key);
            }

            @Override
            public void onConfirm(Bundle pinInfos) throws RemoteException {
                Log.d(TAG, "PinPad onConfirm");
                byte[] data = pinInfos.getByteArray("pinblock");
                iemv.importPin(1, data);
                savedPinblock = data;
            }


            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG, "PinPad onCancel");
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                Log.d(TAG, "PinPad onError, code:" + errorCode);
            }
        };
    }

    private void doSetAID(int type) {
        toastShow("Set AID start");
        EmvSetAidRid emvSetAidRid = new EmvSetAidRid(iemv);
        emvSetAidRid.setAID(type);
        try {
            iBeeper.startBeep(200);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        toastShow("Set AID DONE");
    }

    private void doSetRID(int type) {
        toastShow("Set RID start");
        EmvSetAidRid emvSetAidRid = new EmvSetAidRid(iemv);
        emvSetAidRid.setRID(type);
        try {
            iBeeper.startBeep(200);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        toastShow("Set RID DONE");
    }


    Handler onlineResponse = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i(TAG, "handle message:" + val);
        }
    };

    public ISO8583u isoResponse = null;

    Runnable onlineRequest = new Runnable() {
        @Override
        public void run() {

            ISO8583u iso8583u = new ISO8583u();
            if (tagOfF55 != null) {
                for (int i = 0; i < tagOfF55.size(); i++) {
                    int tag = tagOfF55.keyAt(i);
                    String value = tagOfF55.valueAt(i);
                    if (value.length() > 0) {
                        byte[] tmp = iso8583u.appendF55(tag, value);
                        if (tmp == null) {
                            Log.e(TAG, "error of tag:" + Integer.toHexString(tag) + ", value:" + value);
                        } else {
                            Log.d(TAG, "append F55 tag:" + Integer.toHexString(tag) + ", value:" + Utility.byte2HexStr(tmp));
                        }
                    }
                }
                tagOfF55 = null;

            }
            byte[] packet = iso8583u.makePacket(data8583, ISO8583.PACKET_TYPE.PACKET_TYPE_HEXLEN_BUF);
//            byte[] packet = iso8583u.makePacket(data8583, ISO8583.PACKET_TYPE.PACKET_TYPE_NONE);

            Comm comm = new Comm(hostIP, hostPort);
            if (false == comm.connect()) {
                Log.e(TAG, "connect server error");
                return;
            }

            comm.send(packet);
            byte[] response = comm.receive(1024, 30);
            if (null == response) {
                Log.e(TAG, "receive error");
            }
            comm.disconnect();

            if (response == null) {
                Log.e(TAG, "Test fails");
            } else {
                Log.i(TAG, "Test return length:" + response.length);
                Log.i(TAG, Utility.byte2HexStr(response));
                isoResponse = new ISO8583u();
                if (isoResponse.unpack(response, 2)) {
                    String message = "";
                    String s;
                    String type = "";

                    s = isoResponse.getUnpack(0);
                    if (null != s) {
                        type = s;
                        message += "Message Type:";
                        message += s;
                        message += "\n";
                    }

                    s = isoResponse.getUnpack(39);
                    if (null != s) {
                        message += "Response(39):";
                        message += s;
                        message += "\n";
                    }
                    if (type.equals("0810")) {
                        s = isoResponse.getUnpack(62 + 200);
                        if (null != s) {
                            Log.d(TAG, "Field62:" + s);
                            if (s.length() == 48) {
                                pinKey_WorkKey = s.substring(0, 32);
                                macKey = s.substring(32, 48);
                            } else if (s.length() == 80) {
                                pinKey_WorkKey = s.substring(0, 64);
                                macKey = s.substring(64, 80);
                            } else if (s.length() == 120) {
                                pinKey_WorkKey = s.substring(0, 64);
                                macKey = s.substring(64, 80);
                            }
                            message += "pinKey:";
                            message += pinKey_WorkKey;
                            message += "\n";
                            message += "macKey:";
                            message += macKey;
                            message += "\n";
                        }
                    } else if (type.equals("0210")) {
                        s = isoResponse.getUnpack(54);
                        if (null != s) {
                            message += "Balance(54):";
                            message += s.substring(0, 2) + "," + s.substring(2, 4) + "," + s.substring(4, 7) + "," + s.substring(7, 8);
                            message += "\n" + Integer.valueOf(s.substring(8, s.length() - 1));
                            message += "\n";
                        }

                    }

                    toastShow(message);
                }
            }


            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "receive finished");
            msg.setData(data);
            onlineResponse.sendMessage(msg);
        }
    };

    /**
     * \Brief make purchase fields
     */
    void doPurchase() {
        //
        SparseArray<String> data8583_u_purchase = new SparseArray<>();
        data8583_u_purchase.put(ISO8583u.F_MessageType_00, "0200");
        data8583_u_purchase.put(ISO8583u.F_AccountNumber_02, "");
        data8583_u_purchase.put(3, "00 00 00");
        data8583_u_purchase.put(ISO8583u.F_AmountOfTransactions_04, edAmount.getText().toString());
        data8583_u_purchase.put(11, "01 02 03");
        data8583_u_purchase.put(22, "02 1");   // 02 mag, 05 smart, 07 ctls; 1 pin
        data8583_u_purchase.put(25, "00");
//        data8583_u_purchase.put( 26, "" );  // pin
        data8583_u_purchase.put(49, "156");
//        data8583_u_purchase.put( 52 , "" ); // pin
//        data8583_u_purchase.put( 55 , "" );
//        data8583_u_purchase.put( 59 , "" );
//        data8583_u_purchase.put( 60 , "" );

        data8583 = data8583_u_purchase;


        doTransaction(TransType.T_PURCHASE);


    }

    /**
     * \Brief make balance fields
     */
    void doBalance() {
        SparseArray<String> data8583_u_balance = new SparseArray<>();
        data8583_u_balance.put(0, "0200");
        data8583_u_balance.put(2, savedPan);
        data8583_u_balance.put(3, "310000");
        data8583_u_balance.put(11, "010203");
        data8583_u_balance.put(14, "9912");
        data8583_u_balance.put(22, "020");  // mag
//        data8583_u_balance.put(22, "021");  // mag + pin
        data8583_u_balance.put(25, "00");
//        data8583_u_balance.put(26, ""); // pin
        data8583_u_balance.put(35, ""); // track 2

        data8583_u_balance.put(49, "156");  // RMB
//        data8583_u_balance.put(55, ""); // IC
        data8583_u_balance.put(60, "01654321");

        data8583 = data8583_u_balance;

        doTransaction(TransType.T_BANLANCE);

    }

    /**
     * \Brief make sign in fields
     */
    void doSignIn() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        Date dt = new Date();

        SparseArray<String> data8583_u_signin = new SparseArray<>();
        data8583_u_signin.put(0, "0800");
        // data8583_u_signin.put( 1, "");
        data8583_u_signin.put(11, "012345");
        sdf = new SimpleDateFormat("HHmmss");
        data8583_u_signin.put(12, sdf.format(dt));
        sdf = new SimpleDateFormat("MMdd");
        data8583_u_signin.put(13, sdf.format(dt));
        data8583_u_signin.put(32, "12345678");
        data8583_u_signin.put(37, "ABCDEF123456");
        data8583_u_signin.put(39, "00");
        data8583_u_signin.put(60, "000008673720");
        data8583_u_signin.put(62, "10O");
        data8583_u_signin.put(63, "001");

        data8583 = data8583_u_signin;

        doTransaction(TransType.M_SIGNIN);
    }

    void doTransaction(TransType transType) {

        if (transType == TransType.M_SIGNIN) {
            // management, no card need
            // start onlineRequest
            new Thread(onlineRequest).start();
        } else {
            // set some fields
            data8583.put(41, terminalID);
            data8583.put(42, merchantID);

            // do search card and online request
            doSearchCard(transType);
        }
    }

    void doStartCustonViewPinpad() {
        Intent intent = new Intent(this, CustomPinActivity.class);
        startActivity(intent);
    }

    void initialize8583data() {
        // build up the iso data

        data8583_i = new SparseArray<>();
        data8583_i.put(0, "02 00");
//        data8583_i.put(1, "73 A4 0E 10 20 C0 90 51 ");
        data8583_i.put(2, "62 26 20 01 02 84 80 00");
        data8583_i.put(3, "00 00 00");
        data8583_i.put(4, "000000001234");
        data8583_i.put(7, "20 18 08 23 13 ");
        data8583_i.put(8, "0");
        data8583_i.put(9, "00 00 11 00");
        data8583_i.put(11, "00 01 16");
        data8583_i.put(14, "26 12");
        data8583_i.put(21, "0010001");
        data8583_i.put(22, "00 07");
        data8583_i.put(23, " 00 00");
        data8583_i.put(28, "00");
        data8583_i.put(35, "6226200102848000=26122200119442300000");
        data8583_i.put(41, "88888888       ");
        data8583_i.put(42, "123456789012");
        data8583_i.put(49, "001");
        data8583_i.put(52, "09 65 5D 76 DC C2 81 7D");
        data8583_i.put(58, "dreamtek-X-EMV-DEMO-0.0.1");
        data8583_i.put(60, "60 00 00 00 00 00 00 00");
        data8583_i.put(64, "12 34 56 78 90 12 34 56");

    }


}
