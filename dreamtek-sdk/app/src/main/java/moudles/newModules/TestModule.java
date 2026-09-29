package moudles.newModules;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;

import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IBeeper;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.ILed;
import com.dreamtek.smartpos.deviceservice.aidl.IMagCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.IPrinter;
import com.dreamtek.smartpos.deviceservice.aidl.IRFCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IScanner;
import com.dreamtek.smartpos.deviceservice.aidl.ISerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.ISmartCardReader;
import com.dreamtek.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IFelica;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IICodeCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.INtagCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCard;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardC;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardEV1;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardNano;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IKLD;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IRSA;
import com.dreamtek.smartpos.deviceservice.aidl.sde.ISde;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.LogUtil;
import Utils.LogUtils;
import base.MyApplication;
import entity.cases.BaseCase;
import moudles.newModules.data.PanBundleStore;

/**
 * Created by Simon on 2021/7/13
 * <p>
 * Guide for adding a new test module:
 * <p>
 * 1. Create a module class that extends this class.
 * <p>
 * 2. Subclasses must delete the following members to use the parent members
 *    (not applicable to newly created classes):
 *    - Context context
 *    - LogUtils logUtils
 *    - ArrayList<String> apiList
 *    - ArrayList<ArrayList<BaseCase>> caseNames
 * <p>
 * 3. Also delete the corresponding final methods.
 * <p>
 * 4. Rename the API methods to be tested (usually prefixed with "My") by
 *    changing the prefix to "T_". The method name after removing "T_" is
 *    the test target. Change function parameters to String or void type.
 *    Change return value to String (can return null).
 *    This method can then be added to automated test cases as an API.
 * <p>
 * 5. Add and initialize the corresponding AIDL objects (e.g., ILed, etc.).
 */
public abstract class TestModule {
    private static String TAG = "TestModule";
    Context context;
    private static LogUtils logUtilsHandler;

    static IDeviceService iDevService;
    static IDukpt iDukpt;
    static IPinpad iPinpad;
    static IScanner iScanner;
    static ILed iledDriver;
    static IBeeper iBeeper;
    static IKLD ikld;
    static IRSA irsa;
    static ISerialPort iSerialPort;
    static IUsbSerialPort iUsbSerialPort;
    static IExternalSerialPort iExternalSerialPort;
    static ISmartCardReader[] iSmartCardReaders;
    static IRFCardReader irfCardReader;

    static IFelica iFelica;
    static IICodeCard iiCodeCard;
    static INtagCard iNtagCard;
    static IMagCardReader iMagCardReader;
    static IUltraLightCard iUltraLightCard;
    static IUltraLightCardC iUltraLightCardC;
    static IUltraLightCardEV1 iUltraLightCardEV1;
    static IUltraLightCardNano iUltraLightCardNano;

    static IPrinter iPrinter;
    static IEMV iemv;
    static ISde iSde;


    int smartCardSlot = 0;


    static ISystemManager iSystemManager;
    static INetworkManager iNetworkManager;
    static ISettingsManager iSettingsManager;

    protected String module;

    /**
     * All APIs and their corresponding cases.
     **/
    ArrayList<ArrayList<BaseCase>> caseNames = new ArrayList<ArrayList<BaseCase>>();    // Store all test cases grouped by function name.
    ArrayList<BaseCase> allCases = new ArrayList<>();   // Store all cases in original order.
    /**
     * All API names.
     * Stores the function names of all test APIs.
     **/
    ArrayList<String> apiList = new ArrayList<String>(); // Use the parent class version.

    int printMode;

    public TestModule() {
        printMode = 1;  // 1 = print anytime, 0 = no print, 2 = print after test
        logUtilsHandler = MyApplication.logUtils;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static void updateService(IDeviceService iDevService) {
        Log.d(TAG, "updateService : IDeviceService");
        clearDeviceServices();
        if (iDevService == null) return;
        Utils.ServiceManager services = Utils.ServiceManager.getInstance();
        try {
            TestModule.iDevService = iDevService;
            iDukpt = services.getDUKPT();
            iPinpad = services.getPinpad(1);
            iScanner = services.getScanner(1);
            iledDriver = services.getLed();
            iBeeper = services.getBeeper();
            iUsbSerialPort = services.getUsbSerialPort();
            iExternalSerialPort = services.getExternalSerialPort();
            irfCardReader = services.getRFCardReader();
            ikld = services.getIKLD();
            irsa = services.getIRSA();
            iPrinter = services.getPrinter();
            iemv = services.getEMV();
            iSde = services.getSde();
            iFelica = services.getFelica();
            iiCodeCard = services.getICode();
            iNtagCard = services.getNtag();
            iMagCardReader = services.getMagCardReader();
            iUltraLightCard = services.getUtrlLightManager();
            iUltraLightCardC = services.getUtrlLightCManager();
            iUltraLightCardEV1 = services.getUtrlLightEV1Manager();
            iUltraLightCardNano = services.getUtrlLightNanoManager();
            iSmartCardReaders = new ISmartCardReader[3];
            for (int i = 0; i < 3; i++) {
                iSmartCardReaders[i] = services.getSmartCardReader(i);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public void enablePrinter(int mode) {
        printMode = mode;
    }

    protected void printMsgTool(String msg) {
        printMsgTool(msg, Log.INFO);
    }

    private ArrayList<String> printMsgTool_msg = new ArrayList<>();
    private ArrayList<java.lang.Integer> printMsgTool_level = new ArrayList<>();

    protected void printMsgTool(String msg, int logLevel) {

        if (0 == printMode) {
            Log.d(TAG, "Skip printing");
            return;
        } else if (apiRunning) {
            printMsgTool_msg.add(msg);
            printMsgTool_level.add(logLevel);
            Log.d(TAG, "cache printing");
            return;
        }

        if (printMsgTool_msg.size() > 0) {
            int offset = 0;
            for (String m : printMsgTool_msg) {
                int l = printMsgTool_level.get(offset);
                printReceipt(m, l);
            }
            printMsgTool_msg.clear();
            printMsgTool_level.clear();
        }
        printReceipt(msg, logLevel);
    }

    private void printReceipt(String msg, int logLevel) {

        if (this.getClass().getCanonicalName().compareTo("moudles.newModules.PrinterModule") == 0) {
            Log.d(TAG, "Skip printing on PrinterModule");
            return;
        }


        switch (logLevel) {
            case Log.DEBUG:
                ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).printDbgMsg(msg, true,
                        (this.getClass().getCanonicalName().compareTo("moudles.newModules.EmvModule") == 0));
                break;
            case Log.ERROR:
                ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).printErrMsg(msg, true,
                        (this.getClass().getCanonicalName().compareTo("moudles.newModules.EmvModule") == 0));
                break;
            default:
                ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).printMsg(msg, true,
                        (this.getClass().getCanonicalName().compareTo("moudles.newModules.EmvModule") == 0));
                break;
        }
//
//        } else {
//            switch ( logLevel ){
//                case Log.DEBUG:
//
//                    ((MyApplication) context).serviceMoudle.getPintBtMoudle(true).printDbgMsg(msg, true);
////                MyApplication.serviceMoudle.getPintBtMoudle()
//                    break;
//                case Log.ERROR:
//                    ((MyApplication) context).serviceMoudle.getPintBtMoudle(true).printErrMsg(msg, true);
//                    break;
//                default:
//                    ((MyApplication) context).serviceMoudle.getPintBtMoudle(true).printMsg(msg, true);
//                    break;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }

    }

    protected void printMsgToolAppend(String msg, int logLevel) {

        if (0 == printMode) {
            Log.d(TAG, "Skip printing");
            return;
        } else {
            Log.d(TAG, "Enable printing");
        }
        if (this.getClass().getCanonicalName().compareTo("moudles.newModules.PrinterModule") == 0) {
            Log.d(TAG, "Skip printing on PrinterModule");
            return;
        }


        ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).appendMsg(msg, logLevel,
                (this.getClass().getCanonicalName().compareTo("moudles.newModules.EmvModule") == 0));
    }

    protected int waitPrintingFinish(int second) {
        int ret = ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).waitingPrintResult(second * 1000);
        if (ret != 0) {
            Log.w(TAG, "Printer is working: " + ret);
        }
        return ret;
    }

    public void showTheCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition).getCaseDescribe()
                + "<br><font color='#0000FF'>" + caseNames.get(groupPosition).get(childPosition).getApi()
                + "</font><br>" + caseNames.get(groupPosition).get(childPosition).getMethodParams();
        Log.d(TAG, "showTheCaseInfo: " + name);

        logUtils.printCaseInfo(name);
    }

    //    protected abstract String getClassName();
    protected String getClassName() {
        Log.d(TAG, "get class name:" + this.getClass().getCanonicalName());
        return this.getClass().getCanonicalName();
    }


    public void setCases(ArrayList<BaseCase> cases) {
        addAllapi(cases);
    }


    public final ArrayList<ArrayList<BaseCase>> getCaseNames() {
        return caseNames;
    }

    public final ArrayList<String> getApiList() {
        return apiList;
    }

    private boolean apiRunning = false;

    public void runTheMethod(BaseCase caseInfo) {
        apiRunning = false;
        String caseID = caseInfo.getCaseId().replace("]<", "] <");
        if (caseID.indexOf(">") > 0 && caseID.indexOf("> ") < 0) {
            caseID = caseID.replace(">", "> ");
        }
        String caseID_tmp = caseInfo.getCaseId();
        if (caseID_tmp.contains("]")) {
            caseID_tmp = caseID_tmp.substring(caseID_tmp.indexOf("]") + 1);
        }
        String caseDesc = caseInfo.getCaseDescribe().replace(caseID_tmp, "");
        printMsgTool("\nCase ID|(Description)\n" + caseID + "|" + caseDesc.trim(), Log.DEBUG);
        String apiName = caseInfo.getApi().trim();

        String methodParam = caseInfo.getMethodParams();
        String resultType = caseInfo.getExpectResultType();
        Log.d(TAG,"resultType before filtering: "+resultType);

        if (resultType.contains("public")) {
            resultType = resultType.replace("public", "").trim();
        }
        Log.d(TAG,"resultType after filtering: "+resultType);
        String expectResult = caseInfo.getExpectResult();
        String caseResult = "";
        String caseId = caseInfo.getCaseId();

        Object[] methods = new Object[]{};
        Class<?>[] params = new Class[]{};
        String[] parameters = null;
        if (!TextUtils.isEmpty(methodParam)) {
            methodParam = methodParam.replaceAll("\\|", "");    // Vertical bars result from newline-to-bar translation in Excel; restore here.
            methodParam = methodParam.replaceAll("\\\\,", "，");
            parameters = methodParam.split(",");
            methodParam = "";
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = parameters[i].trim();
                if (parameters[i].compareTo("-") == 0) {
                    // After trim, if only "-" remains, assign empty string.
                    parameters[i] = "";
                }
                parameters[i] = parameters[i].replaceAll("，", ","); // Convert Chinese comma back to English comma.
                if (parameters[i].startsWith("ask-file")) {
                    String file = selectFile("Select file for " + caseInfo.getValue(BaseCase.Items.CaseID) + ", " + caseInfo.getValue(BaseCase.Items.Describe), parameters[i]);
                    if (file.length() == 0) {
                        Log.w(TAG, " not set file, skip the case");
                        logUtils.addCaseLog(apiName + ", execute failed:");
                        Log.e(TAG, apiName + ", execute failed:");
                        this.printMsgTool(apiName + "|Failed", Log.ERROR);

                    } else {
                        parameters[i] = file;
                    }
                }
                methodParam += parameters[i];
                if ((i + 1) < parameters.length) {
                    methodParam += ",";
                }
            }
            methods = parameters;
        }
        if (methods.length > 0) {
            params = new Class[methods.length];
            for (int i = 0; i < methods.length; i++) {
                params[i] = String.class;
            }
        }
        logUtils.clearLog();
        logUtils.addCaseLog(caseId + "<br>Params:<br><u>" + methodParam + "</u><br>");
        try {
            Class<?> aClass = Class.forName(getClassName());  // "moudles.newModules.DukptModule"
            Method method = null;
            do {
                try {
                    Log.d(TAG, aClass.getCanonicalName());
                    if (methods.length > 0) {
                        method = aClass.getDeclaredMethod("T_" + apiName, params);
                    } else {
                        method = aClass.getDeclaredMethod("T_" + apiName);
                    }
                    if (method != null) {
                        break;
                    }
                } catch (java.lang.NoSuchMethodException e) {
                    aClass = aClass.getSuperclass();
                }
            } while (aClass != null);

            if (method == null) {
                Log.e(TAG, "Cannot find the method: " + apiName + " (argc=" + methods.length + "), Class: " + getClassName() + ". Note: test API parameters must be String type.");
                logUtils.addCaseLog(apiName + "(argc=" + methods.length + "), <font color='#FF0000'>No matching API</font>：");
                Log.e(TAG, apiName + ", execute failed:");
                this.printMsgTool(apiName + "(argc=" + methods.length + ")|No matching API", Log.ERROR);

            } else {
                method.setAccessible(true);
                Object result;
                if (2 == printMode) {
                    ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).setHungupPrinting(true);
                }
                waitPrintingFinish(3);
                apiRunning = true;
                long startTime = System.currentTimeMillis();
                if (methods.length > 0) {
                    result = method.invoke(this, methods);
                } else {
                    result = method.invoke(this);
                }
                long endTime = System.currentTimeMillis();
                apiRunning = false;
                if (2 == printMode) {
                    ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).setHungupPrinting(false);
                }

                if (resultType != null && expectResult != null) {
                    int ret = caseInfo.setResult(resultType, expectResult, result);
                    SimpleDateFormat sdf;
                    sdf = new SimpleDateFormat("HH:mm:ss");
                    String timeStamp = sdf.format(new Date());

                    if (ret >= 0) {
                        String msg = apiName + "(): <font color='#0000FF'>Success</font>:" + ret + "<br>" + caseInfo.getValue(BaseCase.Items.ActualValue);
                        msg += "<br>Time " + timeStamp + " Duration: <b>" + (endTime - startTime) + "</b> ms<br>";
                        logUtils.addCaseLog(msg);
                        msg = apiName + "(), Success:" + ret + "|" + caseInfo.getValue(BaseCase.Items.ActualValue);
                        msg += "\nTime " + timeStamp + "|Duration: " + (endTime - startTime) + " ms";

                        this.printMsgTool(msg, Log.INFO);
                    } else {
                        String msg = apiName + "()|Failed:" + ret + "\n |" + caseInfo.getErrorMessage(ret) + "\nAPI returns|" + caseInfo.getValue(BaseCase.Items.ActualValue);
                        Log.e(TAG, msg);

                        logUtils.addCaseLog(
                                apiName + ", <font color='#FF0000'>Failed:</font>" + ret + ", API returns:<br><font color='#FF0000'>"
                                        + caseInfo.getValue(BaseCase.Items.ActualValue)
                                        + "</font>"
                                        + "<br><font color='#0000FF'>" + caseInfo.getValue(BaseCase.Items.ExpectResult) + " (Expected)</font><br>"
                        );
                        this.printMsgTool(msg, Log.ERROR);

                        msg = "Time " + timeStamp + "|Duration: " + (endTime - startTime) + " ms";
                        msg += "\nExpected result:|" + caseInfo.getValue(BaseCase.Items.ExpectResult);
                        Log.e(TAG, msg);
                        this.printMsgTool(msg, Log.INFO);
                    }

                } else {
                    Log.w(TAG, "Not check the result!");
                }
            }
            logUtils.addCaseLog(caseId + "execute finished");
        } catch (Exception e) {
            e.printStackTrace();
            caseInfo.setResult("Exception");
            logUtils.addCaseLog(caseId + "<font color='#FF0000'><b> Exception:</b><br>" + e + "</font>Usually caused by parameters that do not match the expected format: numeric, BCD, length issues, or unexpected return value type.");

            if (2 == printMode) {
                ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).setHungupPrinting(false);
            }


            Log.w(TAG, "try to connect service again");
            Utils.ServiceManager.getInstance().reconnect();
        }
    }


    public void runAllMethod(final ServiceModule serviceModule) {

        for (BaseCase baseCase : allCases) {
            if (baseCase.getCaseStatus() <= 0) {
                continue;
            }
            serviceModule.runTheMethod(baseCase);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logUtils.addCaseLog("\nAll cases executed.");
    }


    /**
     * Add cases based on API name and JSON configuration file.
     *
     * @param cases All cases configured in the JSON file.
     */
    protected void addAllapi(ArrayList<BaseCase> cases) {
        if (cases == null || cases.size() == 0) {
            logUtils.addCaseLog("No Cases, please import cases");
            Log.w(TAG, "No Cases, please import cases");
            return;
        }
        allCases.clear();
        apiList.clear();
        caseNames.clear();

        try {
            // Add all test methods prefixed with T_ (duplicates and overrides are loaded only once).
            Class aClass = Class.forName(getClassName());
            do {
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    Log.v(TAG, "find method:" + method.getName());
                    if (method.getName().startsWith("T_")) {
                        String apiName = method.getName().replace("T_", "");
                        if (!apiList.contains(apiName)) {
                            apiList.add(apiName);
                            //                        testAPINames.add(apiName);
                            caseNames.add(new ArrayList<BaseCase>());
                        }
                    }
                }
                aClass = aClass.getSuperclass();    //
            } while (aClass != null);
            // Add all test cases.
            /** Start reading cases and match them with APIs. **/
            Iterator<BaseCase> caseIterator = cases.iterator();
            while (caseIterator.hasNext()) {
                BaseCase nextCase = caseIterator.next();
                /** Skip case if API NAME is not configured. **/
                if (nextCase == null || TextUtils.isEmpty(nextCase.getApi())) {
                    Log.w(TAG, "null case or empty case found. skip it");
                    continue;
                }
                String apiName = nextCase.getApi().trim();
                int index = apiList.indexOf(apiName);
                if (index >= 0) {
                    caseNames.get(index).add(nextCase); // Add case to the corresponding function group.
                    allCases.add(nextCase);
                    Log.d(TAG, "API [" + apiName + "] add to: " + index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateService(ISystemManager manager) {
        // ServiceManager acquires child interfaces on its worker before publishing them.
        setSystemServices(manager, Utils.ServiceManager.getInstance().getNetworkManager(),
                Utils.ServiceManager.getInstance().getSettingsManager());
    }

    public static synchronized void setSystemServices(ISystemManager manager,
            INetworkManager network, ISettingsManager settings) {
        iSystemManager = manager;
        iNetworkManager = network;
        iSettingsManager = settings;
        if (MyApplication.systemServiceModule != null) {
            MyApplication.systemServiceModule.isConnect = manager != null;
        }
    }

    public static synchronized void clearDeviceServices() {
        iDevService = null;
        iDukpt = null;
        iPinpad = null;
        iScanner = null;
        iledDriver = null;
        iBeeper = null;
        ikld = null;
        irsa = null;
        iSerialPort = null;
        iUsbSerialPort = null;
        iExternalSerialPort = null;
        iSmartCardReaders = null;
        irfCardReader = null;
        iFelica = null;
        iiCodeCard = null;
        iNtagCard = null;
        iMagCardReader = null;
        iUltraLightCard = null;
        iUltraLightCardC = null;
        iUltraLightCardEV1 = null;
        iUltraLightCardNano = null;
        iPrinter = null;
        iemv = null;
        iSde = null;
    }

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //            Log.i(TAG, "msg:" + msg.getData().getString("msg"));
            super.handleMessage(msg);
            String value = msg.getData().getString("msg");
            if (null != value && value.length() > 0) {
                logUtilsHandler.addCaseLog(value);
                logUtilsHandler.showCaseLog();
            }
            value = msg.getData().getString("clearLog");
            if (null != value && value.length() > 0) {
                logUtilsHandler.clearLog();
            }
            value = msg.getData().getString("showCaseLog");
            if (null != value && value.length() > 0) {
                logUtilsHandler.showCaseLog();
            }
            value = msg.getData().getString("addCaseLog");
            if (null != value && value.length() > 0) {
                logUtilsHandler.addCaseLog(value);
                logUtilsHandler.showCaseLog();
            }
            value = msg.getData().getString("printCaseInfo");
            if (null != value && value.length() > 0) {
                logUtilsHandler.printCaseInfo(value);
            }
            value = msg.getData().getString("printCaseLog");
            if (null != value && value.length() > 0) {
                logUtilsHandler.printCaseLog(value);
            }
            value = msg.getData().getString("caseFinished", "");
            if (value.length() > 0) {
                Log.d(TAG, "read message: caseFinished  " + value);
                int type = Integer.valueOf(value);
                if (type >= 0) {
                    logUtilsHandler.caseFinished(type);
                }
            }
        }
    };

    /**
     * logUtils replaces the subclass class name so that existing logUtils code
     * in subclasses does not need modification. Uses Handler internally to
     * refresh LogUtils.
     */
    protected static class logUtils {
        public static void addCaseLog(String message) {
            // Only add; call showCaseLog to display.
            Message msg = new Message();
            msg.getData().putString("addCaseLog", message);
            handler.sendMessage(msg);
        }

        public static void showCaseLog() {
            Message msg = new Message();
            msg.getData().putString("showCaseLog", "showCaseLog");
            handler.sendMessage(msg);
        }

        public static void clearLog() {
            Message msg = new Message();
            msg.getData().putString("clearLog", "clearLog");
            handler.sendMessage(msg);
        }

        public static void printCaseInfo(String message) {
            Message msg = new Message();
            msg.getData().putString("printCaseInfo", message);
            handler.sendMessage(msg);
        }

        public static void printCaseLog(String message) {
            // Display on UI.
            Message msg = new Message();
            msg.getData().putString("printCaseLog", message);
            handler.sendMessage(msg);
        }

        public static void caseFinished(int type) {
            Message msg = new Message();
            msg.getData().putString("caseFinished", String.valueOf(type));
            handler.sendMessage(msg);
            Log.d(TAG, "send message: caseFinished " + type);
        }

    }

    public static final int BundleConfig_String = 0;
    public static final int BundleConfig_int = 1;
    public static final int BundleConfig_boolean = 2;
    public static final int BundleConfig_byte = 3;
    public static final int BundleConfig_byte_A = 4;
    public static final int BundleConfig_long = 5;
    public static final int BundleConfig_float = 6;

    protected class BundleConfig {
        public String key;
        public int type;    // 0 -> string,    1 -> String[], 2 int, 3 int[], 4 boolean, 5 boolean[]
        // 6 byte, 7 byte[], 8 long, 9 long[]

        public BundleConfig(String key, int type) {
            this.key = key;
            this.type = type;
        }
    }

    final String bundleSplitSign = "=";

    protected Bundle convert(String parameter, BundleConfig[] bundleConfigs) {
        Bundle bundle = new Bundle();
        String[] parameters = parameter.split("_");
//        if( bundleConfigs == null ){
//            Log.w(TAG, "no bundle setting for convert" );
//            return bundle;
//        }
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].contains("\\\\--")) {
                // Escape underscores.
                parameters[i] = parameters[i].replace("\\\\--", "_");
            }
        }
        int offset = 0;
        if (bundleConfigs != null) {
            for (BundleConfig bundleConfig : bundleConfigs) {
                if (offset >= parameters.length) {
                    Log.w(TAG, "no more value set to bundle");
                    break;
                }
                String value = parameters[offset].trim();
                if (value.length() == 0) {
                    Log.d(TAG, "skip set value to " + bundleConfig.key);
                    ++offset;
                    continue;
                } else if (value.contains(bundleSplitSign)) {

                    break;
                } else {
                }
                ++offset;
                insertBundle(bundle, bundleConfig.key, value, bundleConfig.type);
            }
        }
        if (parameter.contains(bundleSplitSign)) {
            // Bundle key names already specified in parameters.
            for (int i = offset; i < parameters.length; i++) {
                if (!parameters[i].contains(bundleSplitSign)) {
                    Log.w(TAG, "Invalid parameter: " + parameters[i]);
                    continue;
                }
                String[] keyValue = parameters[i].split(bundleSplitSign);
                if (keyValue.length < 2) {
                    Log.w(TAG, "Invalid parameter: " + parameters[i]);
                    continue;
                }
                if (keyValue[0].trim().length() == 0 ||
                        keyValue[1].trim().length() == 0) {
                    Log.w(TAG, "Invalid parameter: " + parameters[i]);
                    continue;
                }
                Log.d(TAG, "reading parameter: " + parameters[i]);
                int count = bundle.size();
                if (keyValue.length == 3) {
                    // The third parameter is the data type; load directly so parameters can be added without modifying the program.
                    int type = Integer.valueOf(keyValue[2].trim());

                    if (keyValue[1].contains("\\\\-+")) {
                        // Escape equals sign so that Bundle parameters can be nested.
                        keyValue[1] = keyValue[1].replace("\\\\-+", "=");
                    }

                    insertBundle(bundle, keyValue[0].trim(), keyValue[1].trim(), type);
                } else if (null != bundleConfigs) {
                    for (BundleConfig bundleConfig : bundleConfigs) {
                        // When comparing, Excel data cannot contain underscores; remove underscores from key here.
                        if (bundleConfig.key.replace("_", "").equalsIgnoreCase(keyValue[0].trim())) {
                            insertBundle(bundle, bundleConfig.key, keyValue[1].trim(), bundleConfig.type);
                            break;
                        }
                    }
                } else {
                    Log.w(TAG, "Invalid input: " + parameters[i]);
                }
                if (count == bundle.size()) {
                    Log.w(TAG, "Not found: " + parameters[i]);
                }
            }
        }

        return bundle;
    }

    // Convert bundle to string.
    protected String convert(Bundle bundle) {
        String sResult = "";
        for (String key : bundle.keySet()) {
            if (sResult.length() > 0) {
                sResult += "|";
            }
            sResult += key;
            sResult += "=";
            sResult += bundle.get(key);
        }
        return sResult;
    }

    protected void insertBundle(Bundle bundle, String key, String value, int type) {
        if (null == key || null == value) {
            return;
        }
        if (key.length() == 0 || value.length() == 0) {
            return;
        }
        Log.v(TAG, "setting value [" + key.trim() + "] = " + value);

        switch (type) {
            case BundleConfig_String:
                bundle.putString(key, value);
                break;
            case BundleConfig_int:
                bundle.putInt(key, Integer.valueOf(value));
                break;
            case BundleConfig_boolean:
                bundle.putBoolean(key, Boolean.parseBoolean(value));
                break;
            case BundleConfig_byte:
                bundle.putByte(key, Byte.decode(value));
                break;
            case BundleConfig_byte_A:
                bundle.putByteArray(key, StringUtil.hexStr2Bytes(value));
//                // Currently only supports single-digit numbers.
//                if (value == null) {
//                    bundle.putByteArray(key, null);
//                } else {
//                    bundle.putByteArray(key, new byte[]{Byte.parseByte(value)});
//                }
                break;
            case BundleConfig_long:
                bundle.putLong(key, Long.valueOf(value));
                break;
            case BundleConfig_float:
                bundle.putFloat(key, Float.valueOf(value));
                break;
        }

    }

    public byte[] StringtoCharArray(String myString) {
        if (myString.equals("null")) {
            return null;
        }
        char[] chars = myString.toCharArray();  // Convert string to character array.
        byte[] bytes = new byte[chars.length];  // Create byte array to store the result.
        for (int i = 0; i < chars.length; i++) {
            if (chars[i]>='A' && chars[i] <='E'){
                bytes[i] = (byte) (chars[i]-'A'+10);
            }else {
                bytes[i] = (byte) (chars[i] - '0');
            }
        }
        Log.d(TAG,"bytes="+ Arrays.toString(bytes));
        return bytes;
    }


    // ask-file:json_json:/sdcard/
    private String selectFile(String title, String files) {
        String[] selectFile = files.split(":");

        if (selectFile.length < 2) {
            return "";
        }

        String path = "/sdcard/";
        if (selectFile.length >= 3) {
            if (selectFile[2].trim().length() > 0) {
                path = selectFile[2].trim();
            }
        }
        String extList = selectFile[1].trim().toLowerCase();
        String[] exts = extList.split("_");

        File defaultLogDirectory = new File(path);
        List<String> filelist = new ArrayList<>();
        for (File file : defaultLogDirectory.listFiles()) {
            String name = file.getName();
            Log.v(TAG, "found: " + name);
            for (String ext : exts) {
                if (name.toLowerCase().endsWith(ext)) {
                    // add
                    filelist.add(name);
                }

            }
        }
        String[] sFilelist = new String[filelist.size()];
        for (int i = 0; i < filelist.size(); i++) {
            sFilelist[i] = filelist.get(i);
        }
        final int[] selectIndex = {0};

        String selectedFile = "";
        final int[] selectedIndex = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(sFilelist, selectIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "on confirm " + which);
                synchronized (TestModule.class) {
                    selectedIndex[0] = which;
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedIndex[0] = -2;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        int sel = -1;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (TestModule.class) {
                sel = selectedIndex[0];
            }

        } while (sel == -1);

        if (sel >= 0) {
            return filelist.get(sel);
        }
        return "";


    }

    // Convert key-value pairs.
    // Example: PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY=0
    List<String[]> convert(String list, String regexItem, String regexKeyValue) {
        Log.d(TAG, "Convert: " + list + " , " + regexItem + " , " + regexKeyValue);
        List<String[]> keyValueLs = new ArrayList<String[]>();
        if (regexItem.equals(".")) {
            regexItem = "\\.";
        }
        if (regexKeyValue.equals(".")) {
            regexKeyValue = "\\.";
        }
        String[] keyValues = list.split(regexItem);
        for (String keyValue : keyValues
        ) {
            keyValue = keyValue.trim();
            if (keyValue.length() > 0) {
                String[] kv = keyValue.split(regexKeyValue);
                kv[0] = kv[0].trim();
                if (kv[0].trim().length() == 0) {
                    continue;
                }
                if (kv.length > 1) {
                    kv[1] = kv[1].trim();
                } else {
                }
                keyValueLs.add(kv);
            }
        }
        Log.d(TAG, "add items: " + keyValueLs.size());
        return keyValueLs;
    }

    List<String> convert(String list, char regex) {
        String regexItem = String.valueOf(regex);
        Log.d(TAG, "Convert: " + list + " , " + regexItem);
        List<String> keyValueLs = new ArrayList<String>();
        if (regexItem.equals(".")) {
            regexItem = "\\.";
        }
        String[] keyValues = list.split(regexItem);
        for (String keyValue : keyValues
        ) {
            keyValue = keyValue.trim();
            if (keyValue.length() > 0) {
                keyValueLs.add(keyValue);
            }
        }
        Log.d(TAG, "add items: " + keyValueLs.size());
        return keyValueLs;
    }

    String convertTLVinLines(String strTLV) {
        String lines = "";
        byte[] tlv = StringUtil.hexStr2Bytes(strTLV);
        List<String> tlvs = convertTLV(tlv);
        for (String line : tlvs) {
            lines += line;
            lines += "\n";
        }
        return lines;
    }

    List<String> convertTLV(byte[] tlv) {
        if (tlv == null) {
            return new ArrayList<String>();
        } else {
            List<String> tlvList = new ArrayList<>();
            int index = 0;
            int[] length;

            while (true) {
                while (index < tlv.length) {
                    byte[] tag;
                    if ((tlv[index] & 31) == 31) {
                        int tagInt = tlv[index] & 255;
                        int i = index;
                        int size = 1;

                        do {
                            ++size;
                            ++i;
                            tagInt <<= 8;
                            tagInt |= tlv[i] & 255;
                        } while ((tlv[i] & 128) == 128);

                        tag = new byte[size];
                        System.arraycopy(tlv, index, tag, 0, size);
                        index += size;
                        length = getTLV_length(tlv, index);
                    } else {
                        tag = new byte[1];
                        System.arraycopy(tlv, index, tag, 0, 1);

                        ++index;
                        length = getTLV_length(tlv, index);
//                        index = copyData(tlv, map, index, tag);
                    }
                    if (length[0] < 0) {
                        tlvList.add("ERROR to read more !!!");
                        byte[] value = new byte[tlv.length - index];
                        System.arraycopy(tlv, index, value, 0, tlv.length - index);
                        tlvList.add(StringUtil.byte2HexStr(value));
                        break;
                    }
                    // index = copyData(tlv, map, index, tag);
                    byte[] value = new byte[length[1]];
                    index += length[0];
                    System.arraycopy(tlv, index, value, 0, length[1]);

                    tlvList.add(String.format("%4s (%2d) %s", StringUtil.byte2HexStr(tag), length[1], StringUtil.byte2HexStr(value)));

                    index += length[1];
                }

                return tlvList;
            }
        }
    }


    String findTag(String strTLV,String tag) {
        byte[] tlv = StringUtil.hexStr2Bytes(strTLV);
        return convertTLV1(tlv, tag);
    }

    String convertTLV1(byte[] tlv, String tagFind) {
        if (tlv == null) {
            return null;
        } else {
            List<String> tlvList = new ArrayList<>();
            int index = 0;
            int[] length;

            while (true) {
                while (index < tlv.length) {
                    byte[] tag;
                    if ((tlv[index] & 31) == 31) {
                        int tagInt = tlv[index] & 255;
                        int i = index;
                        int size = 1;

                        do {
                            ++size;
                            ++i;
                            tagInt <<= 8;
                            tagInt |= tlv[i] & 255;
                        } while ((tlv[i] & 128) == 128);

                        tag = new byte[size];
                        System.arraycopy(tlv, index, tag, 0, size);
                        index += size;
                        length = getTLV_length(tlv, index);
                    } else {
                        tag = new byte[1];
                        System.arraycopy(tlv, index, tag, 0, 1);

                        ++index;
                        length = getTLV_length(tlv, index);
//                        index = copyData(tlv, map, index, tag);
                    }
                    if (length[0] < 0) {
                        tlvList.add("ERROR to read more !!!");
                        byte[] value = new byte[tlv.length - index];
                        System.arraycopy(tlv, index, value, 0, tlv.length - index);
                        tlvList.add(StringUtil.byte2HexStr(value));
                        break;
                    }
                    // index = copyData(tlv, map, index, tag);
                    byte[] value = new byte[length[1]];
                    index += length[0];
                    System.arraycopy(tlv, index, value, 0, length[1]);

                    if (tagFind.equals(StringUtil.byte2HexStr(tag)))
                        return StringUtil.byte2HexStr(value);

                    //tlvList.add(String.format("%4s (%2d) %s", StringUtil.byte2HexStr(tag), length[1], StringUtil.byte2HexStr(value)));

                    index += length[1];
                }
             }
        }
    }

    String findTag1(String strTLV,String tag) {
        byte[] tlv = StringUtil.hexStr2Bytes(strTLV);
        return convertTLV2(tlv, tag);
    }

    String convertTLV2(byte[] tlv, String tagFind) {
        if (tlv == null) {
            return null;
        } else {
            List<String> tlvList = new ArrayList<>();
            int index = 0;
            int[] length;


                while (index < tlv.length) {
                    byte[] tag;
                    if ((tlv[index] & 31) == 31) {
                        int tagInt = tlv[index] & 255;
                        int i = index;
                        int size = 1;

                        do {
                            ++size;
                            ++i;
                            tagInt <<= 8;
                            tagInt |= tlv[i] & 255;
                        } while ((tlv[i] & 128) == 128);

                        tag = new byte[size];
                        System.arraycopy(tlv, index, tag, 0, size);
                        index += size;
                        length = getTLV_length(tlv, index);
                    } else {
                        tag = new byte[1];
                        System.arraycopy(tlv, index, tag, 0, 1);

                        ++index;
                        length = getTLV_length(tlv, index);
//                        index = copyData(tlv, map, index, tag);
                    }
                    if (length[0] < 0) {
                        tlvList.add("ERROR to read more !!!");
                        byte[] value = new byte[tlv.length - index];
                        System.arraycopy(tlv, index, value, 0, tlv.length - index);
                        tlvList.add(StringUtil.byte2HexStr(value));
                        break;
                    }
                    // index = copyData(tlv, map, index, tag);
                    byte[] value = new byte[length[1]];
                    index += length[0];
                    System.arraycopy(tlv, index, value, 0, length[1]);

                    if (tagFind.equals(StringUtil.byte2HexStr(tag)))
                        return StringUtil.byte2HexStr(value);

                    //tlvList.add(String.format("%4s (%2d) %s", StringUtil.byte2HexStr(tag), length[1], StringUtil.byte2HexStr(value)));

                    index += length[1];
                }
            return null;
        }
    }

    int[] getTLV_length(byte[] tlv, int index) {
        int length = 0;
        int offset = index;
        if (tlv[index] >> 7 == 0) {
            length = tlv[index];
            ++index;
        } else {
            int lenlen = tlv[index] & 127;
            ++index;
            if (lenlen > 2) {
                Log.e(TAG, "Tlv L field byte length not greater than 3");
                return new int[]{-1, -1};
            }

            for (int i = 0; i < lenlen; ++i) {
                length <<= 8;
                length += tlv[index] & 255;
                ++index;
            }
        }
        return new int[]{(index - offset), length};
    }


    protected static class Log {
        public static final int ASSERT = 7;
        public static final int DEBUG = 3;
        public static final int ERROR = 6;
        public static final int INFO = 4;
        public static final int VERBOSE = 2;
        public static final int WARN = 5;

        static void i(String tag, String log) {
            LogUtil.i(tag, log);
        }

        static void e(String tag, String log) {
            LogUtil.e(tag, log);
        }

        static void w(String tag, String log) {
            LogUtil.w(tag, log);
        }

        static void d(String tag, String log) {
            LogUtil.d(tag, log);
        }

        static void v(String tag, String log) {
            LogUtil.v(tag, log);
        }
    }

    protected static class Integer {

        public static int valueOf(String number) {
            if (number.startsWith("0x")) {
                return java.lang.Integer.valueOf(number.substring(2), 16);
            } else {
                return java.lang.Integer.valueOf(number, 10);
            }
        }

        public static int valueOf(int a) {
            return a;
        }

        public static int parseInt(String number) {

            return  java.lang.Integer.decode(number);
//            return java.lang.Integer.parseInt(number);
        }

    }


}
