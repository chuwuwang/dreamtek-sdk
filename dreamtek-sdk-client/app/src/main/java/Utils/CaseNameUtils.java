package Utils;

import android.content.Context;

import java.util.ArrayList;

import base.MyApplication;
import entity.cases.BaseCase;

import static Utils.Constants.MKSK;
import static Utils.Constants.beerBt;
import static Utils.Constants.serialPortBt;
import static Utils.Constants.serviceInfoBt;
import static Utils.Constants.dukptBt;
import static Utils.Constants.emvBt;
import static Utils.Constants.externalSerialPortBt;
import static Utils.Constants.ledBt;
import static Utils.Constants.magcardBt;
import static Utils.Constants.notouchIcBt;
import static Utils.Constants.pbocBt;
import static Utils.Constants.pinpadBt;
import static Utils.Constants.pintBt;
import static Utils.Constants.scanBt;
import static Utils.Constants.sdeBt;
import static Utils.Constants.serviceBt;
import static Utils.Constants.touchicBt;
import static Utils.Constants.ultralightCardBt;
import static Utils.Constants.ultralightCardCBt;
import static Utils.Constants.ultralightCardEV1Bt;
import static Utils.Constants.usbPortBt;

/**
 * Created by WenpengL1 on 2016/12/26.
 */
public class CaseNameUtils {
    Context context;
    private ArrayList<String> parentNames = new ArrayList<String>();
    private ArrayList<ArrayList<String>> childNames = new ArrayList<ArrayList<String>>();
    private static CaseNameUtils caseNameUtils;

    private CaseNameUtils(Context context) {
        this.context = context;
    }

    //获取实例
    public static CaseNameUtils getInstance(Context context) {
        if (null == caseNameUtils) {
            caseNameUtils = new CaseNameUtils(context);
            return caseNameUtils;
        } else {
            return caseNameUtils;
        }
    }

    /**
     * 返回api的方法数组
     *
     * @param value
     * @return
     */
    public ArrayList<String> getMoudleAPIs(String value) {
        ArrayList<String> moudleAPIs = null;
        switch (value) {
            case serviceBt:
                moudleAPIs = MyApplication.serviceMoudle.getApiList();
                break;
            case magcardBt:
                moudleAPIs = MyApplication.serviceMoudle.getMagCardReaderMoudle().getApiList();
                break;
            case touchicBt:
                moudleAPIs = MyApplication.serviceMoudle.getInsertCardReaderMoudle().getApiList();
                break;
            case notouchIcBt:
                moudleAPIs = MyApplication.serviceMoudle.getIrfCardReaderMoudle().getApiList();
                break;
            case beerBt:
                moudleAPIs = MyApplication.serviceMoudle.getBeerMoudle().getApiList();
                break;
            case ledBt:
                moudleAPIs = MyApplication.serviceMoudle.getLedMoudle().getApiList();
                break;
            case scanBt:
                moudleAPIs = MyApplication.serviceMoudle.getScanBtMoudle().getApiList();
                break;
            case pintBt:
                moudleAPIs = MyApplication.serviceMoudle.getPintBtMoudle().getApiList();
                break;
            case serialPortBt:
                moudleAPIs = MyApplication.serviceMoudle.getSerialPortMoudle().getApiList();
                break;
            case pinpadBt:
                moudleAPIs = MyApplication.serviceMoudle.getPinpadMoudle().getApiList();
                break;
            case serviceInfoBt:
                moudleAPIs = MyApplication.serviceMoudle.getServiceInfoMoudle().getApiList();
                break;
            case usbPortBt:
                moudleAPIs = MyApplication.serviceMoudle.getUsbSerialModule().getApiList();
                break;
            case externalSerialPortBt:
                moudleAPIs = MyApplication.serviceMoudle.getExternalSerialPortModule().getApiList();
                break;
            case emvBt:
                moudleAPIs = MyApplication.serviceMoudle.getEmvMoudle().getApiList();
                break;
            case sdeBt:
                moudleAPIs = MyApplication.serviceMoudle.getSdeMoudle().getApiList();
                break;
            case dukptBt:
                moudleAPIs = MyApplication.serviceMoudle.getDukptMoudle().getApiList();
                break;
            case ultralightCardBt:
                moudleAPIs = MyApplication.serviceMoudle.getUltralightManage().getApiList();
                break;
            case ultralightCardCBt:
                moudleAPIs = MyApplication.serviceMoudle.getUltralightCManage().getApiList();
                break;
            case ultralightCardEV1Bt:
                moudleAPIs = MyApplication.serviceMoudle.getUltralightEV1Manage().getApiList();
                break;
            case MKSK:
                moudleAPIs = MyApplication.serviceMoudle.getMKSK().getApiList();
                break;
        }
        return moudleAPIs;
    }

    public ArrayList<ArrayList<String>> getCaseNames(String value) {
        ArrayList<ArrayList<String>> caseNames = null;
        switch (value) {
            case serviceBt:
                caseNames = MyApplication.serviceMoudle.getCaseNames();
                break;
            case magcardBt:
                caseNames = MyApplication.serviceMoudle.getMagCardReaderMoudle().getCaseNames();
                break;
            case touchicBt:
                caseNames = MyApplication.serviceMoudle.getInsertCardReaderMoudle().getCaseNames();
                break;
            case notouchIcBt:
                caseNames = MyApplication.serviceMoudle.getIrfCardReaderMoudle().getCaseNames();
                break;
            case beerBt:
                caseNames = MyApplication.serviceMoudle.getBeerMoudle().getCaseNames();
                break;
            case ledBt:
                caseNames = MyApplication.serviceMoudle.getLedMoudle().getCaseNames();
                break;
            case scanBt:
                caseNames = MyApplication.serviceMoudle.getScanBtMoudle().getCaseNames();
                break;
            case pintBt:
                caseNames = MyApplication.serviceMoudle.getPintBtMoudle().getCaseNames();
                break;
            case serialPortBt:
                caseNames = MyApplication.serviceMoudle.getSerialPortMoudle().getCaseNames();
                break;
            case pinpadBt:
                caseNames = MyApplication.serviceMoudle.getPinpadMoudle().getCaseNames();
                break;
            case serviceInfoBt:
                caseNames = MyApplication.serviceMoudle.getServiceInfoMoudle().getCaseNames();
                break;
            case usbPortBt:
                caseNames = MyApplication.serviceMoudle.getUsbSerialModule().getCaseNames();
                break;
            case externalSerialPortBt:
                caseNames = MyApplication.serviceMoudle.getExternalSerialPortModule().getCaseNames();
                break;
            case emvBt:
                caseNames = MyApplication.serviceMoudle.getEmvMoudle().getCaseNames();
                break;
            case sdeBt:
                caseNames = MyApplication.serviceMoudle.getSdeMoudle().getCaseNames();
                break;
            case dukptBt:
                caseNames = MyApplication.serviceMoudle.getDukptMoudle().getCaseNames();
                break;
            case ultralightCardBt:
                caseNames = MyApplication.serviceMoudle.getUltralightManage().getCaseNames();
                break;
            case ultralightCardCBt:
                caseNames = MyApplication.serviceMoudle.getUltralightCManage().getCaseNames();
                break;
            case ultralightCardEV1Bt:
                caseNames = MyApplication.serviceMoudle.getUltralightEV1Manage().getCaseNames();
                break;
            case MKSK:
                caseNames = MyApplication.serviceMoudle.getMKSK().getCaseNames();
                break;
        }
        return caseNames;
    }

    /**
     * 支持json配置的module使用
     * @param value
     * @param cases
     * @return
     */
    public ArrayList<String> getMoudleAPIs(String value, ArrayList<BaseCase> cases) {
        ArrayList<String> moudleAPIs = null;
        switch (value) {
            case "ALL":
                moudleAPIs = MyApplication.newServiceModule.getApiList();
                break;
            default:
                moudleAPIs = MyApplication.newServiceModule.getModule( value ).getApiList();
                break;
        }
        return moudleAPIs;
    }

    /**
     * 支持json配置的module使用
     * @param value
     * @param cases
     * @return
     */
    public ArrayList<ArrayList<BaseCase>> getCaseNames(String value, ArrayList<BaseCase> cases) {
        ArrayList<ArrayList<BaseCase>> caseNames = null;
        switch (value) {
            case "ALL":
                caseNames = MyApplication.newServiceModule.getAllCaseNames();
                break;
            default:
                caseNames = MyApplication.newServiceModule.getModule( value ).getCaseNames();
                break;

        }
        return caseNames;
    }
}