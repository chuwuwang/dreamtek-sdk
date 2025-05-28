package base;

/**
 * Created by Yaping_Z1 on 2017/1/17.
 */

public class TransResult {
    public static final int EMV_COMPLETE =9;   // - EMV简易流程结束 </li>
    public static final int EMV_ERROR= 11; // - EMV内核错误</li>
    public static final int EMV_FALLBACK= 12; // - FALLBACK </li>
    public static final int EMV_DATA_AUTH_FAIL= 13; // - 脱机数据认证失败 </li>
    public static final int EMV_APP_BLOCKED =14; // - 应用被锁定 </li>
    public static final int EMV_NOT_ECCARD =15; // - 非电子现金卡 </li>
    public static final int EMV_UNSUPPORT_ECCARD=16; // - 该交易不支持电子现金卡 </li>
    public static final int EMV_AMOUNT_EXCEED_ON_PURELYEC=17; // - 纯电子现金卡消费金额超限 </li>
    public static final int EMV_SET_PARAM_ERROR= 18; // - 参数设置错误(9F7A) </li>
    public static final int EMV_PAN_NOT_MATCH_TRACK2= 19; // - 主账号与二磁道不符 </li>
    public static final int EMV_CARD_HOLDER_VALIDATE_ERROR= 20; // - 持卡人认证失败 </li>
    public static final int EMV_PURELYEC_REJECT=21;//) - 纯电子现金卡被拒绝交易 </li>
    public static final int EMV_BALANCE_INSUFFICIENT=22;//) - 余额不足 </li>
    public static final int EMV_AMOUNT_EXCEED_ON_RFLIMIT_CHECK=23; // - 交易金额超过非接限额检查 </li>
    public static final int EMV_CARD_BIN_CHECK_FAIL=24; // - 卡BIN检查失败 </li>
    public static final int EMV_CARD_BLOCKED=25 ;//) - 卡被锁 </li>
    public static final int EMV_MULTI_CARD_ERROR=26; // - 多卡冲突 </li>
    public static final int EMV_BALANCE_EXCEED=27;// - 余额超出 </li>
    public static final int EMV_RFCARD_PASS_FAIL=60; // - 挥卡失败 </li>

    public static final int AARESULT_TC= 0;// - 行为分析结果，交易批准(脱机)</li>
    public static final int AARESULT_AAC=1;// - 行为分析结果，交易拒绝 </li>
    public static final int QPBOC_AAC=202;// - qPBOC交易结果，交易拒绝</li>
    public static final int QPBOC_ERROR=203;// - qPBOC交易结果，交易失败 </li>
    public static final int QPBOC_TC=204;// - qPBOC交易结果，交易批准 </li>
    public static final int QPBOC_CONT=205;// - qPBOC结果，转接触式卡 </li>
    public static final int QPBOC_NO_APP=206;// - qPBOC交易结果，无应用(可转UP Card)</li>
    public static final int QPBOC_NOT_CPU_CARD=207;// - qPBOC交易结果，该卡非TYPE B/PRO卡</li>

}
