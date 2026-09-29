package base;

/**
 * Created by Yaping_Z1 on 2017/1/17.
 */

public class TransResult {
    public static final int EMV_COMPLETE =9;   // - EMV simple process completed </li>
    public static final int EMV_ERROR= 11; // - EMV kernel error</li>
    public static final int EMV_FALLBACK= 12; // - FALLBACK </li>
    public static final int EMV_DATA_AUTH_FAIL= 13; // - Offline data authentication failed </li>
    public static final int EMV_APP_BLOCKED =14; // - Application blocked </li>
    public static final int EMV_NOT_ECCARD =15; // - Not an electronic cash card </li>
    public static final int EMV_UNSUPPORT_ECCARD=16; // - Electronic cash card not supported for this transaction </li>
    public static final int EMV_AMOUNT_EXCEED_ON_PURELYEC=17; // - Pure electronic cash card purchase amount exceeded </li>
    public static final int EMV_SET_PARAM_ERROR= 18; // - Parameter setting error (9F7A) </li>
    public static final int EMV_PAN_NOT_MATCH_TRACK2= 19; // - PAN does not match track 2 </li>
    public static final int EMV_CARD_HOLDER_VALIDATE_ERROR= 20; // - Cardholder verification failed </li>
    public static final int EMV_PURELYEC_REJECT=21;// - Pure electronic cash card transaction rejected </li>
    public static final int EMV_BALANCE_INSUFFICIENT=22;// - Insufficient balance </li>
    public static final int EMV_AMOUNT_EXCEED_ON_RFLIMIT_CHECK=23; // - Transaction amount exceeds contactless limit </li>
    public static final int EMV_CARD_BIN_CHECK_FAIL=24; // - Card BIN check failed </li>
    public static final int EMV_CARD_BLOCKED=25 ;// - Card locked </li>
    public static final int EMV_MULTI_CARD_ERROR=26; // - Multiple card conflict </li>
    public static final int EMV_BALANCE_EXCEED=27;// - Balance exceeded </li>
    public static final int EMV_RFCARD_PASS_FAIL=60; // - Tap card failed </li>

    public static final int AARESULT_TC= 0;// - Issuer action result, transaction approved (offline)</li>
    public static final int AARESULT_AAC=1;// - Issuer action result, transaction declined </li>
    public static final int QPBOC_AAC=202;// - qPBOC transaction result, declined</li>
    public static final int QPBOC_ERROR=203;// - qPBOC transaction result, failed </li>
    public static final int QPBOC_TC=204;// - qPBOC transaction result, approved </li>
    public static final int QPBOC_CONT=205;// - qPBOC result, refer to contact card </li>
    public static final int QPBOC_NO_APP=206;// - qPBOC transaction result, no application (can switch to UP Card)</li>
    public static final int QPBOC_NOT_CPU_CARD=207;// - qPBOC transaction result, card is not TYPE B/PRO card</li>

}
