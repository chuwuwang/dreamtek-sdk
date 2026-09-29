package moudles;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;


import com.dreamtek.smartpos.deviceservice.aidl.IBeeper;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.ILed;
import com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IScanner;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardC;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardEV1;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IKLD;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IMKSK;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;
import com.dreamtek.smartpos.deviceservice.aidl.sde.ISde;
import com.dreamtek.smartpos.deviceservice.aidl.IServiceInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.Constants;
import Utils.LogUtils;
import automation.AutomaticCaseResult;
import automation.IcCardAutomationSession;
import automation.MagCardAutomationSession;
import base.MyApplication;

public class ServiceMoudle {
    //MyApplication
    Context context;
    // Use as a global variable reference
    public IDeviceService deviceService;
    public boolean isConnect = false;
    public LogUtils logUtils;
    /**
     * Obtain the AIDL remote reference.
     */
    IMagCardReader iMagCardReader;
    ISmartCardReader iInsertCardReader;
    IRFCardReader irfCardReader;
    IRSA irsa;
    IUltraLightCard iUltraLightCard;
    IUltraLightCardC iUltraLightCardC;
    IUltraLightCardEV1 iUltraLightCardEV1;

    ILed iledDriver;
    IBeeper iBeeper;
    IScanner iScanner;
    IPrinter iPrinter;

    ISerialPort iSerialport;
    ISerialPort iSerialPortNew;
    IPinpad iPinpad;
    IUsbSerialPort iUsbSerialPort;
    IEMV iemv;
    IDukpt iDukpt;
    ISde iSde;
    IKLD ikld;
    IMKSK iMKSK;

    IServiceInfo iServiceInfo;
    /**
     * Handwritten module objects.
     */
    BeerMoudle beerMoudle;
    ServiceInfoMoudle serviceInfoMoudle;
    InsertCardReaderMoudle insertCardReaderMoudle;
    IrfCardReaderMoudle irfCardReaderMoudle;
    MKSKModule mkskModule;
    X990PinpadMoudle x990PinpadMoudle;

    LedMoudle ledMoudle;
    MagCardReaderMoudle magCardReaderMoudle;
    PbocBtMoudle pbocBtMoudle;
    PinpadMoudle pinpadMoudle;

    PrintBtMoudle pintBtMoudle;
    SerialPortMoudle serialPortMoudle;
    ScanBtMoudle scanBtMoudle;
    UsbSerialPortMoudle usbSerialModule;

    ExternalSerialPortModule externalSerialPortModule;
    EmvMoudle emvMoudle;
    SdeMoudle sdeMoudle;
    DukptMoudle dukptMoudle;
    UltraLightCardMoudle ultraLightCardMoudle;
    UltraLightCardCMoudle ultraLightCardCMoudle;
    UltraLightCardEV1Moudle ultraLightCardEV1Moudle;
    EppMoudle eppMoudle;

    RsaModule rsaModule;

    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> getBeeper = new ArrayList<String>();
    ArrayList<String> getLed = new ArrayList<String>();
    ArrayList<String> getPrinter = new ArrayList<String>();
    ArrayList<String> getScanner = new ArrayList<String>();
    ArrayList<String> getSerialPort = new ArrayList<String>();
    ArrayList<String> getMagCardReader = new ArrayList<String>();
    ArrayList<String> getInsertCardReader = new ArrayList<String>();
    ArrayList<String> getRFCardReader = new ArrayList<String>();
    ArrayList<String> getPinpad = new ArrayList<String>();
    ArrayList<String> getPBOC = new ArrayList<String>();
    ArrayList<String> getServiceInfo = new ArrayList<String>();
    ArrayList<String> getUsbSerialPort = new ArrayList<String>();
    ArrayList<String> getEMV = new ArrayList<String>();
    ArrayList<String> getDUKPT = new ArrayList<String>();
    ArrayList<String> autoService = new ArrayList<String>();
    ArrayList<String> getUltraLightCard = new ArrayList<String>();
    ArrayList<String> getUltraLightCardC = new ArrayList<String>();
    ArrayList<String> getUltraLightCardEV1 = new ArrayList<String>();
    ArrayList<String> getMKSK = new ArrayList<String>();

    ArrayList<String> getRsa = new ArrayList<String>();


    public void clearServices() {
        deviceService = null;
        isConnect = false;
        iMagCardReader = null;
        iInsertCardReader = null;
        irfCardReader = null;
        irsa = null;
        iUltraLightCard = null;
        iUltraLightCardC = null;
        iUltraLightCardEV1 = null;
        iledDriver = null;
        iBeeper = null;
        iScanner = null;
        iPrinter = null;
        iSerialport = null;
        iSerialPortNew = null;
        iPinpad = null;
        iUsbSerialPort = null;
        iemv = null;
        iDukpt = null;
        iSde = null;
        ikld = null;
        iMKSK = null;
        iServiceInfo = null;
        beerMoudle = null;
        serviceInfoMoudle = null;
        insertCardReaderMoudle = null;
        irfCardReaderMoudle = null;
        mkskModule = null;
        x990PinpadMoudle = null;
        ledMoudle = null;
        magCardReaderMoudle = null;
        pbocBtMoudle = null;
        pinpadMoudle = null;
        pintBtMoudle = null;
        serialPortMoudle = null;
        scanBtMoudle = null;
        usbSerialModule = null;
        externalSerialPortModule = null;
        emvMoudle = null;
        sdeMoudle = null;
        dukptMoudle = null;
        ultraLightCardMoudle = null;
        ultraLightCardCMoudle = null;
        ultraLightCardEV1Moudle = null;
        eppMoudle = null;
        rsaModule = null;
    }

    public ServiceMoudle(Context context) {
        this.context = context;
        logUtils = MyApplication.logUtils;
        addAllapis();
        addCaseNames();
    }

    private void addCaseNames() {
        try {
            Class aClass = Class.forName("moudles.ServiceMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                Log.d("TAG", i.getName());
                switch (i.getName().substring(0, 3)) {
                    case "A01":
                        getBeeper.add(i.getName());
                        break;
                    case "A02":
                        getLed.add(i.getName());
                        break;
                    case "A03":
                        getPrinter.add(i.getName());
                        break;
                    case "A04":
                        getScanner.add(i.getName());
                        break;
                    case "A05":
                        getSerialPort.add(i.getName());
                        break;
                    case "A06":
                        getServiceInfo.add(i.getName());
                        break;
                    case "A07":
                        getPBOC.add(i.getName());
                        break;
                    case "A08":
                        getMagCardReader.add(i.getName());
                        break;
                    case "A09":
                        getInsertCardReader.add(i.getName());
                        break;
                    case "A10":
                        getRFCardReader.add(i.getName());
                        break;
                    case "A11":
                        getPinpad.add(i.getName());
                        break;
                    case "A12":
                        getUsbSerialPort.add(i.getName());
                        break;
                    case "A13":
                        getEMV.add(i.getName());
                        break;
                    case "A14":
                        getDUKPT.add(i.getName());
                        break;
                    case "A15":
                        autoService.add(i.getName());
                        break;
                    case "A16":
                        getUltraLightCard.add(i.getName());
                        break;
                    case "A17":
                        getUltraLightCardC.add(i.getName());
                        break;
                    case "A18":
                        getUltraLightCardEV1.add(i.getName());
                        break;
                    case "A19":
                        getMKSK.add(i.getName());
                        break;
                    case "A20":
                        getRsa.add(i.getName());
                        break;
                }
            }
            caseNames.add(getBeeper);
            caseNames.add(getLed);
            caseNames.add(getPrinter);
            caseNames.add(getScanner);
            caseNames.add(getSerialPort);
            caseNames.add(getServiceInfo);
            caseNames.add(getPBOC);
            caseNames.add(getMagCardReader);
            caseNames.add(getInsertCardReader);
            caseNames.add(getRFCardReader);
            caseNames.add(getPinpad);
            caseNames.add(getUsbSerialPort);
            caseNames.add(getEMV);
            caseNames.add(getDUKPT);
            caseNames.add(autoService);
            caseNames.add(getUltraLightCard);
            caseNames.add(getUltraLightCardC);
            caseNames.add(getUltraLightCardEV1);
            caseNames.add(getMKSK);
            caseNames.add(getRsa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAllapis() {
        try {
            Class aClass = Class.forName("moudles.ServiceMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IBeeper My01getiBeeper() {
        try {
            logUtils.addCaseLog("Execute getBeeper");
            iBeeper = deviceService.getBeeper();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getBeeper exception");
            e.printStackTrace();
        }
        return iBeeper;
    }

    private ILed My02getIledDriver() {
        try {
            logUtils.addCaseLog("Execute getLed");
            iledDriver = deviceService.getLed();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getLed exception");
            e.printStackTrace();
        }
        return iledDriver;
    }

    private IPrinter My03getiPrinter() {
        try {
            logUtils.addCaseLog("Execute getPrinter");
            iPrinter = deviceService.getPrinter();
        } catch (Exception e) {
            logUtils.addCaseLog("Execute getPrinter exception");
            e.printStackTrace();
        }
        return iPrinter;
    }

    private IPrinter My03getiPrinter(boolean silence) {
        try {
            if (!silence) {
                logUtils.addCaseLog("Execute getPrinter");
            }
            iPrinter = deviceService.getPrinter();
        } catch (Exception e) {
            logUtils.addCaseLog("Execute getPrinter exception");
            e.printStackTrace();
        }
        return iPrinter;
    }

    private IScanner My04getiScanner(int cameraId) {
        try {
            logUtils.addCaseLog("Execute getScanner");
            iScanner = deviceService.getScanner(cameraId);
            if (cameraId == ScanBtMoudle.BACK_CAMERA) {
                logUtils.addCaseLog("Return to rear camera successfully");
            } else {
                if (cameraId == ScanBtMoudle.FRONT_CAMERA) {
                    logUtils.addCaseLog("Back to front camera successfully");
                } else {
                    logUtils.addCaseLog("Invalid parameter");
                }

            }
        } catch (RemoteException e) {
            if (cameraId == ScanBtMoudle.BACK_CAMERA) {
                logUtils.addCaseLog("Return Rear camera exception");
            } else {
                logUtils.addCaseLog("Return front camera exception");
            }
            e.printStackTrace();
        }
        return iScanner;
    }

    private ISerialPort My05getiSerialport(String deviceType) {
        try {
            logUtils.addCaseLog("Execute getSerialPort");
            iSerialport = deviceService.getSerialPort(deviceType);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getSerialPort exception");
            e.printStackTrace();
        }
        return iSerialport;
    }

    private IServiceInfo My06getiServiceInfo() {
        try {
            logUtils.addCaseLog("Execute getServiceInfo");
            iServiceInfo = deviceService.getServiceInfo();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getServiceInfo exception");
            e.printStackTrace();
        }
        return iServiceInfo;
    }

//    private IPBOC My07getIpboc() {
//        try {
//            logUtils.addCaseLog("Execute getPBOC");
//            ipboc = deviceService.getPBOC();
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("Execute getPBOC exception");
//            e.printStackTrace();
//        }
//        return ipboc;
//    }

    private IMagCardReader My08getiMagCardReader() {
        try {
            logUtils.addCaseLog("Execute getMagCardReader");
            iMagCardReader = deviceService.getMagCardReader();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getMagCardReader exception");
            e.printStackTrace();
        }
        return iMagCardReader;
    }

    private ISmartCardReader My09getiInsertCardReader(int slotNo) {
        try {
            logUtils.addCaseLog("Execute getInsertCardReader");
            iInsertCardReader = deviceService.getSmartCardReader(slotNo);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getInsertCardReader exception");
            e.printStackTrace();
        }
        return iInsertCardReader;
    }

    private IRFCardReader My10getIrfCardReader() {
        try {
            logUtils.addCaseLog("Execute getRFCardReader");
            irfCardReader = deviceService.getRFCardReader();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getRFCardReader exception");
            e.printStackTrace();
        }
        return irfCardReader;
    }

    private IPinpad My11getiPinpad(int kapId) {
        try {
            logUtils.addCaseLog("Execute getPinpad");
            iPinpad = deviceService.getPinpad(kapId);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getPinpad exception");
            e.printStackTrace();
        }
        return iPinpad;
    }

    private IUsbSerialPort My12getUsbSerialPort() {
        try {
            logUtils.addCaseLog("Execute getUsbSerialPort");
            iUsbSerialPort = deviceService.getUsbSerialPort();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getUsbSerialPort exception");
            e.printStackTrace();
        }
        return iUsbSerialPort;
    }

    private IEMV My13getEMV() {
        try {
            logUtils.addCaseLog("Execute getEMV");
            iemv = deviceService.getEMV();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getEMV exception");
            e.printStackTrace();
        }
        return iemv;
    }

    private IDukpt My14getDUKPT() {
        try {
            logUtils.addCaseLog("Execute getDUKPT");
            iDukpt = deviceService.getDUKPT();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getDUKPT exception");
            e.printStackTrace();
        }
        return iDukpt;
    }

    private IDukpt My15autoSerive() {
        try {
            logUtils.addCaseLog("Execute autoService");
            iDukpt = deviceService.getDUKPT();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute autoService exception");
            e.printStackTrace();
        }
        return iDukpt;
    }


    private IUltraLightCard My16getUtrlLightManager() {
        try {
            logUtils.addCaseLog("Execute UltralignthCard");
            iUltraLightCard = deviceService.getUtrlLightManager();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute UltralignthCard exception");
            e.printStackTrace();
        }
        return iUltraLightCard;
    }

    private IUltraLightCardC My17getUtrlLightCManager() {
        try {
            logUtils.addCaseLog("Execute UltralignthCardC");
            iUltraLightCardC = deviceService.getUtrlLightCManager();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute UltralignthCardC exception");
            e.printStackTrace();
        }
        return iUltraLightCardC;
    }

    private IUltraLightCardEV1 My18getUtrlLightEV1Manager() {
        try {
            logUtils.addCaseLog("Execute UltralignthCardEV1");
            iUltraLightCardEV1 = deviceService.getUtrlLightEV1Manager();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute UltralignthCardEV1 exception");
            e.printStackTrace();
        }
        return iUltraLightCardEV1;
    }

    private IMKSK My19getMKSK() {
        try {
            logUtils.addCaseLog("Execute MKSK");
            iMKSK = deviceService.getMKSK();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute SmartCardReaderEx exception");
            e.printStackTrace();
        }
        return iMKSK;
    }

    //
//    private IX990Pinpad My20getX990Pinpad() {
//        try {
//            logUtils.addCaseLog("Excute getX990Pinpad");
//            ix990Pinpad = deviceService.getX990Pinpad();
//        } catch (RemoteException e) {
//            logUtils.addCaseLog("Execute getX990Pinpad exception");
//            e.printStackTrace();
//        }
//        return ix990Pinpad;
//
//    }
    private ISde My21getSde() {
        try {
            logUtils.addCaseLog("Execute getSde");
            iSde = deviceService.getSde();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getSde exception");
            e.printStackTrace();
        }
        return iSde;
    }

    private IKLD My22getKLD() {
        try {
            logUtils.addCaseLog("Execute getKLD");
            ikld = deviceService.getIKLD();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getKLD exception");
            e.printStackTrace();
        }
        return ikld;
    }

    private ISerialPort My23getiSerialportNew(String deviceType) {
        try {
            logUtils.addCaseLog("Execute get New SerialPort");
            iSerialPortNew = deviceService.getSerialPort(deviceType);
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getSerialPortNew exception");
            e.printStackTrace();
        }
        return iSerialPortNew;
    }

//    private IPinEpp My24getPinEpp(){
//        logUtils.addCaseLog("Execute get epp");
//        try {
//            iPinEpp = deviceService.getPinEpp();
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//        return iPinEpp;
//    }

    private IRSA My30getIRSACoder() {
        try {
            logUtils.addCaseLog("Execute getIRSA");
            irsa = deviceService.getIRSA();
        } catch (RemoteException e) {
            logUtils.addCaseLog("Execute getIRSA exception");
            e.printStackTrace();
        }
        return irsa;
    }
    /**
     * Selects a scanner: 1 for front and 0 for rear.
     */

    public IScanner getiScanner(int cameraId) {
        return My04getiScanner(cameraId);
    }
    /** Returns the rear scanner by default. */

    public ScanBtMoudle getScanBtMoudle() {
        scanBtMoudle = new ScanBtMoudle(context, My04getiScanner(1), My03getiPrinter());
        return scanBtMoudle;
    }

    public SerialPortMoudle getSerialPortMoudle() {
        if (serialPortMoudle == null)
            serialPortMoudle = new SerialPortMoudle(context, My05getiSerialport("usb-rs232"), null);
        return serialPortMoudle;
    }

//    public void setDeviceType1() {
//        My05getiSerialport("usb2rs232-0403-6015-0");
//    }
    public void setDeviceType1() {
        My05getiSerialport("usb2rs232");
    }

    public void setDeviceType2() {
        My05getiSerialport("usb2rs232-0403-6015-1");
    }

    public void setDeviceType3() {
        My05getiSerialport("990-usb-dx16");
    }

    public void setDeviceType4() {
        My05getiSerialport("wireless-rs232");
    }

    public void setDeviceType5() {
        My05getiSerialport("wireless-pinpad");
    }

    public void setDeviceType6() {
        iSerialport = My05getiSerialport("usb-rs232");
    }

    public void setDeviceType7() {
        My05getiSerialport("pedestal-rs232");
    }

    public void setDeviceType8() {
        My05getiSerialport("pedestal-pinpad");
    }

    public void setDeviceType9() {
        // DX16 USB port.
        My05getiSerialport("counterTop-usb2dx16");
    }

    public void setDeviceType10() {
        // PIN pad controller over RS-232.
        My05getiSerialport("pinpad-usb-rs232");
    }

    public void setDeviceType11() {
        //UX com1
        My05getiSerialport("ux-com-1");
    }

    public void setDeviceType12() {
        //UX com2
        My05getiSerialport("ux-com-2");
    }

    public void setDeviceType13() {
        //usb2rs232-VID-PID
        My05getiSerialport("usb2rs232-1A86-7523");
    }

    public void setNewDeviceType1() {
        My23getiSerialportNew("pedestal-pinpad");
    }

    public void setNewDeviceType2() {
        My23getiSerialportNew("pedestal-rs232");
    }

    public void setNewDeviceType3() {
        My23getiSerialportNew("counterTop-usb2dx16");
    }

    public PrintBtMoudle getPintBtMoudle() {
        pintBtMoudle = new PrintBtMoudle(context, My03getiPrinter());
        return pintBtMoudle;
    }

    public PrintBtMoudle getPintBtMoudle(boolean silence) {
        pintBtMoudle = new PrintBtMoudle(context, My03getiPrinter(silence));
        return pintBtMoudle;
    }

    public PinpadMoudle getPinpadMoudle() {
        pinpadMoudle = new PinpadMoudle(context, My11getiPinpad(1),My19getMKSK(), My14getDUKPT(), My10getIrfCardReader(), My05getiSerialport("pinpad-usb-rs232"), My13getEMV(), My12getUsbSerialPort(), My22getKLD());
        return pinpadMoudle;
    }
   /* public UltraLightCardMoudle getUltraLightCardMoudle() {
        ultraLightCardMoudle = new UltraLightCardMoudle(context, My16getUtrlLightManager());
        return ultraLightCardMoudle;
    }
    public UltraLightCardCMoudle getUltraLightCardCMoudle() {
        ultraLightCardCMoudle = new UltraLightCardCMoudle(context, My17getUtrlLightCManager());
        return ultraLightCardCMoudle;
    }
    public UltraLightCardEV1Moudle getUltraLightCardEV1Moudle() {
        ultraLightCardEV1Moudle = new UltraLightCardEV1Moudle(context, My18getUtrlLightEV1Manager());
        return ultraLightCardEV1Moudle;
    }*/


//    public PbocBtMoudle getPbocBtMoudle() {
//        pbocBtMoudle = new PbocBtMoudle(context, My07getIpboc());
//        return pbocBtMoudle;
//    }

    public MagCardReaderMoudle getMagCardReaderMoudle() {
        magCardReaderMoudle = new MagCardReaderMoudle(context, My08getiMagCardReader());
        return magCardReaderMoudle;
    }

    public LedMoudle getLedMoudle() {
        ledMoudle = new LedMoudle(context, My02getIledDriver());
        return ledMoudle;
    }

    public IrfCardReaderMoudle getIrfCardReaderMoudle() {
        irfCardReaderMoudle = new IrfCardReaderMoudle(context, My10getIrfCardReader(), My30getIRSACoder());
        return irfCardReaderMoudle;
    }

    public InsertCardReaderMoudle getInsertCardReaderMoudle() {
        insertCardReaderMoudle = new InsertCardReaderMoudle(context,
                My09getiInsertCardReader(0), My09getiInsertCardReader(1), My03getiPrinter());
        return insertCardReaderMoudle;
    }

    public ServiceInfoMoudle getServiceInfoMoudle(){
        serviceInfoMoudle = new ServiceInfoMoudle(context,My06getiServiceInfo());
        return serviceInfoMoudle;
    }

    public BeerMoudle getBeerMoudle() {
        beerMoudle = new BeerMoudle(context, My01getiBeeper());
        return beerMoudle;
    }

    public UsbSerialPortMoudle getUsbSerialModule() {
        usbSerialModule = new UsbSerialPortMoudle(context, My12getUsbSerialPort());
        return usbSerialModule;
    }

    public ExternalSerialPortModule getExternalSerialPortModule() {
        try {
            externalSerialPortModule = new ExternalSerialPortModule(context, deviceService.getExternalSerialPort(), deviceService.getWirelessBaseHelper());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return externalSerialPortModule;
    }

    public EmvMoudle getEmvMoudle() {
        emvMoudle = new EmvMoudle(context, My13getEMV(), My02getIledDriver(), My22getKLD(), My10getIrfCardReader());
        return emvMoudle;
    }

    public SdeMoudle getSdeMoudle() {
        sdeMoudle = new SdeMoudle(context, My21getSde(), My13getEMV(), My14getDUKPT(), My11getiPinpad(1));
        return sdeMoudle;
    }


    public DukptMoudle getDukptMoudle() {
        dukptMoudle = new DukptMoudle(context, My14getDUKPT());
        return dukptMoudle;
    }


    public UltraLightCardMoudle getUltralightManage() {
        ultraLightCardMoudle = new UltraLightCardMoudle(context, My16getUtrlLightManager());

        return ultraLightCardMoudle;
    }

    public UltraLightCardCMoudle getUltralightCManage() {
        ultraLightCardCMoudle = new UltraLightCardCMoudle(context, My17getUtrlLightCManager());

        return ultraLightCardCMoudle;
    }

    public UltraLightCardEV1Moudle getUltralightEV1Manage() {
        ultraLightCardEV1Moudle = new UltraLightCardEV1Moudle(context, My18getUtrlLightEV1Manager());

        return ultraLightCardEV1Moudle;
    }

    public MKSKModule getMKSK() {
        mkskModule = new MKSKModule(context, My19getMKSK());
        return mkskModule;
    }

//    public X990PinpadMoudle getX990PinpadMoudle(){
//        x990PinpadMoudle = new X990PinpadMoudle(context,My20getX990Pinpad(),My11getiPinpad(1),My10getIrfCardReader(),My05getiSerialport("usb-rs232"),My13getEMV(),My30getIRSACoder());
//        return x990PinpadMoudle;
//    }

//    public EppMoudle getEppMoudle(){
//        eppMoudle = new EppMoudle(context,My24getPinEpp(),My05getiSerialport("usb-rs232"),My11getiPinpad(1),My14getDUKPT());
//        return eppMoudle;
//    }

    public RsaModule getRsa() {
        rsaModule = new RsaModule(context, My30getIRSACoder());
//        rsaModule = RsaModule.getInstance(context,My30getIRSACoder());
        return rsaModule;
    }
    /** Called by the adapter to dispatch a selected case. */


    public void runTheMethod(String mouduleName, int groupPosition, int childPosition) {
        switch (mouduleName) {
            case Constants.serviceBt:
                runTheSelfMethod(groupPosition, childPosition);
                break;
            case Constants.magcardBt:
                getMagCardReaderMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.touchicBt:
                getInsertCardReaderMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.notouchIcBt:
                getIrfCardReaderMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.beerBt:
                getBeerMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.ledBt:
                getLedMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.scanBt:
                getScanBtMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.pintBt:
                getPintBtMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.serialPortBt:
                getSerialPortMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.pinpadBt:
                // The extra dispatch parameter is unused by this legacy module.
                getPinpadMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.pbocBt:
            case Constants.eppBt:
            case Constants.x990PinpadBt:
                logUtils.addCaseLog("UNAVAILABLE: the legacy " + mouduleName
                        + " AIDL implementation is disabled in this source tree");
                break;
            case Constants.MKSK:
                getMKSK().runTheMethod(groupPosition,childPosition);
                break;
//            case Constants.pbocBt:
//                getPbocBtMoudle().runTheMethod(groupPosition, childPosition);
//                break;
            case Constants.serviceInfoBt:
                getServiceInfoMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.usbPortBt:
                getUsbSerialModule().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.externalSerialPortBt:
                getExternalSerialPortModule().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.emvBt:
                getEmvMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.sdeBt:
                getSdeMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.dukptBt:
                getDukptMoudle().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.ultralightCardBt:
                getUltralightManage().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.ultralightCardCBt:
                getUltralightCManage().runTheMethod(groupPosition, childPosition);
                break;
            case Constants.ultralightCardEV1Bt:
                getUltralightEV1Manage().runTheMethod(groupPosition, childPosition);
                break;
//            case Constants.smartCardReaderExBt:
//                getSmartCardReaderEx().runTheMethod(groupPosition, childPosition);
//                break;
//            case Constants.eppBt:
//                getEppMoudle().runTheMethod(groupPosition,childPosition);
//                break;
            case Constants.rsa:
                getRsa().runTheMethod(groupPosition, childPosition);
                break;
        }
    }
    /** Invokes a case implemented by this module. */


    private void runTheSelfMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        try {
            Class aClass = Class.forName("moudles.ServiceMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "End of execution cases");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute and evaluate a Get Service case without an operator verdict.
     *
     * The atomic cases require a live Binder object when their contracts define one. Unsupported
     * serial types must return null. Scanner IDs outside 0 and 1 remain NOT_RUN because the API and
     * legacy case resources define no expected result. A15_AUTO aggregates the same contracts
     * instead of invoking the legacy printer-based runner.
     */
    public AutomaticCaseResult runAutomaticServiceCase(int groupPosition, int childPosition)
            throws RemoteException {
        String caseName = caseNames.get(groupPosition).get(childPosition);
        if ("A15_AUTO".equals(caseName)) {
            int checked = 0;
            ArrayList<String> failures = new ArrayList<>();
            ArrayList<String> undefined = new ArrayList<>();
            for (ArrayList<String> group : caseNames) {
                for (String atomicCase : group) {
                    if ("A15_AUTO".equals(atomicCase)) {
                        continue;
                    }
                    checked++;
                    AutomaticCaseResult result = evaluateAutomaticServiceCase(atomicCase);
                    if ("FAIL".equals(result.status)) {
                        failures.add(atomicCase + ": " + result.actualResult);
                    } else if ("NOT_RUN".equals(result.status)) {
                        undefined.add(atomicCase + ": " + result.actualResult);
                    }
                }
            }
            if (!failures.isEmpty()) {
                return AutomaticCaseResult.fail(
                        "Aggregate validation failed: " + joinDetails(failures));
            }
            if (!undefined.isEmpty()) {
                return AutomaticCaseResult.notRun("Validated "
                        + (checked - undefined.size()) + " of " + checked
                        + " atomic contracts; undefined cases: " + joinDetails(undefined));
            }
            return AutomaticCaseResult.pass(
                    "Validated " + checked + " Get Service return contracts");
        }
        return evaluateAutomaticServiceCase(caseName);
    }

    public MagCardAutomationSession beginAutomaticMagCardCase(String caseName)
            throws RemoteException {
        return MagCardAutomationSession.start(deviceService.getMagCardReader(), caseName);
    }

    public IcCardAutomationSession beginAutomaticIcCardCase(String caseName)
            throws RemoteException {
        return IcCardAutomationSession.start(deviceService.getSmartCardReader(0),
                deviceService.getSmartCardReader(1), caseName);
    }

    private AutomaticCaseResult evaluateAutomaticServiceCase(String caseName)
            throws RemoteException {
        switch (caseName) {
            case "A01001":
                return liveBinder(caseName, "getBeeper", deviceService.getBeeper());
            case "A02001":
                return liveBinder(caseName, "getLed", deviceService.getLed());
            case "A03001":
                return liveBinder(caseName, "getPrinter", deviceService.getPrinter());
            case "A04001":
                return liveBinder(caseName, "getScanner(0)", deviceService.getScanner(0));
            case "A04002":
                return liveBinder(caseName, "getScanner(1)", deviceService.getScanner(1));
            case "A04003":
                return undefinedScanner(caseName, -1, deviceService.getScanner(-1));
            case "A04004":
                return undefinedScanner(caseName, 2, deviceService.getScanner(2));
            case "A05001":
                return liveBinder(caseName, "getSerialPort(rs232)",
                        deviceService.getSerialPort("rs232"));
            case "A05002":
                return liveBinder(caseName, "getSerialPort(usb-rs232)",
                        deviceService.getSerialPort("usb-rs232"));
            case "A05003":
                return rejectedSerialType(caseName, "wanghui",
                        deviceService.getSerialPort("wanghui"));
            case "A05004":
                return liveBinder(caseName, "getSerialPort(usb2rs232-11CA-0240)",
                        deviceService.getSerialPort("usb2rs232-11CA-0240"));
            case "A05005":
                return liveBinder(caseName, "getSerialPort(usb2rs232-11CA-0204)",
                        deviceService.getSerialPort("usb2rs232-11CA-0204"));
            case "A06001":
                return liveBinder(caseName, "getServiceInfo", deviceService.getServiceInfo());
            case "A08001":
                return liveBinder(caseName, "getMagCardReader",
                        deviceService.getMagCardReader());
            case "A09001":
                return liveBinder(caseName, "getSmartCardReader(0)",
                        deviceService.getSmartCardReader(0));
            case "A10001":
                return liveBinder(caseName, "getRFCardReader", deviceService.getRFCardReader());
            case "A11001":
                return liveBinder(caseName, "getPinpad(1)", deviceService.getPinpad(1));
            case "A11002":
                return liveBinder(caseName, "getPinpad(0)", deviceService.getPinpad(0));
            case "A12001":
                return liveBinder(caseName, "getUsbSerialPort",
                        deviceService.getUsbSerialPort());
            case "A13001":
                return liveBinder(caseName, "getEMV", deviceService.getEMV());
            case "A14001":
                return liveBinder(caseName, "getDUKPT", deviceService.getDUKPT());
            default:
                return AutomaticCaseResult.fail(
                        "No automatic acceptance contract for " + caseName);
        }
    }

    private static AutomaticCaseResult liveBinder(String caseName, String api, IInterface value) {
        if (value == null) {
            return AutomaticCaseResult.fail(api + " returned null");
        }
        IBinder binder = value.asBinder();
        if (binder == null) {
            return AutomaticCaseResult.fail(api + " returned an object without a Binder");
        }
        if (!binder.isBinderAlive()) {
            return AutomaticCaseResult.fail(api + " returned a dead Binder");
        }
        return AutomaticCaseResult.pass(caseName + ": " + api + " returned a live Binder");
    }

    private static AutomaticCaseResult undefinedScanner(String caseName, int cameraId,
                                                        IScanner value) {
        String observed;
        if (value == null) {
            observed = "returned null";
        } else {
            IBinder binder = value.asBinder();
            observed = binder != null && binder.isBinderAlive()
                    ? "returned a live Binder" : "returned a non-live Binder object";
        }
        return AutomaticCaseResult.notRun(caseName + ": getScanner(" + cameraId + ") "
                + observed + "; the API contract defines only camera IDs 0 and 1, and this case "
                + "has no expected result");
    }

    private static AutomaticCaseResult rejectedSerialType(String caseName, String deviceType,
                                                           ISerialPort value) {
        if (value == null) {
            return AutomaticCaseResult.pass(caseName + ": getSerialPort(" + deviceType
                    + ") rejected the unsupported device type with null");
        }
        return AutomaticCaseResult.fail("getSerialPort(" + deviceType
                + ") returned an object for an unsupported device type");
    }

    private static String joinDetails(ArrayList<String> details) {
        StringBuilder joined = new StringBuilder();
        for (String detail : details) {
            if (joined.length() > 0) {
                joined.append("; ");
            }
            joined.append(detail);
        }
        return joined.toString();
    }

    // Displays the selected case information.
    public void showTheCaseInfo(String moudle, int groupPosition, int childPosition) {
        logUtils.clearLog();
        switch (moudle) {
            case Constants.serviceBt:
                showSelfCaseInfo(groupPosition, childPosition);
                break;
            case Constants.magcardBt:
                getMagCardReaderMoudle().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.touchicBt:
                getInsertCardReaderMoudle().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.notouchIcBt:
                getIrfCardReaderMoudle().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.beerBt:
                getBeerMoudle().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.ledBt:
                getLedMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.scanBt:
                getScanBtMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.pintBt:
                getPintBtMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.serialPortBt:
                getSerialPortMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.pinpadBt:
                // The extra display parameter is unused by this legacy module.
                getPinpadMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.pbocBt:
            case Constants.eppBt:
            case Constants.x990PinpadBt:
                logUtils.printCaseInfo("UNAVAILABLE: the legacy " + moudle
                        + " AIDL implementation is disabled in this source tree");
                break;
//            case Constants.pbocBt:
//                getPbocBtMoudle().showTheCaseInfo(groupPosition, childPosition);
//                break;
            case Constants.serviceInfoBt:
                getServiceInfoMoudle().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.externalSerialPortBt:
                getExternalSerialPortModule().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.sdeBt:
                getSdeMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.emvBt:
                getEmvMoudle().showTheCaseInfo(groupPosition, childPosition);
                break;
            case Constants.dukptBt:
                getDukptMoudle().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.ultralightCardBt:
                getUltralightManage().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.ultralightCardCBt:
                getUltralightCManage().showCaseInfo(groupPosition, childPosition);
                break;
            case Constants.ultralightCardEV1Bt:
                getUltralightEV1Manage().showCaseInfo(groupPosition, childPosition);
                break;
//            case Constants.smartCardReaderExBt:
//                getSmartCardReaderEx().showCaseInfo(groupPosition, childPosition);
//                break;
        }
    }

    public void showSelfCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    public void A01001() {
        My01getiBeeper();
    }

    public void A02001() {
        My02getIledDriver();
    }

    public void A03001() {
        My03getiPrinter();
    }

    public void A04001() {
        My04getiScanner(0);
    }

    public void A04002() {
        My04getiScanner(1);
    }

    public void A04003() {
        My04getiScanner(-1);
    }

    public void A04004() {
        My04getiScanner(2);
    }

//    public void A04005() {
//        try {
//            deviceService.getIKLD().keyStoreTR34Payload(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }


    public void A05001() {
        My05getiSerialport("rs232");
    }

    public void A05002() {
        My05getiSerialport("usb-rs232");
    }

    public void A05003() {
        My05getiSerialport("wanghui");
    }

    public void A05004() {
        My05getiSerialport("usb2rs232-11CA-0240");
    }

    public void A05005() {
        My05getiSerialport("usb2rs232-11CA-0204");
    }

    public void A06001() {
        My06getiServiceInfo();
    }
//
//    public void A07001() {
//        My07getIpboc();
//    }

    public void A08001() {
        My08getiMagCardReader();
    }

    public void A09001() {
        My09getiInsertCardReader(0);
    }

    public void A10001() {
        My10getIrfCardReader();
    }

    public void A11001() {
        My11getiPinpad(1);
    }

    public void A11002() {
        My11getiPinpad(0);
    }

    public void A12001() {
        My12getUsbSerialPort();
    }

    public void A13001() {
        My13getEMV();
    }

    public void A14001() {
        My14getDUKPT();
    }


    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public void A15_AUTO() {

        try {
            // Resolve the module class for reflective case discovery.
            Class aClass = Class.forName("moudles.ServiceMoudle");
            this.printMsgTool("------Service module------");
            this.printMsgTool("Start execute AUTO_TEST");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("A") && !method.getName().equals("A15_AUTO")) {
                    this.printMsgTool(method.getName() + "Method starts executing......");
                    method.invoke(this);
                    this.printMsgTool(method.getName() + "Method end execution！！！");
                    Thread.sleep(1000);
                }
            }
            this.printMsgTool("The automated test case has been executed");
            logUtils.addCaseLog("The automated test case has been executed");
            ((MyApplication) context).serviceMoudle.getPintBtMoudle().D08018();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printMsgTool(String msg) {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IDeviceService getDeviceService() {
        return deviceService;
    }

    public IPinpad getiPinpad() {
        return iPinpad;
    }
}
