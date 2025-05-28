package moudles.newModules;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
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
 * 新增 测试模块 指南
 * <p>
 * 创建模块Class， 继承 此模块
 * <p>
 * 子类需要删除如下成员以使用父类的成员（不适用新创建的类）
 * Context context
 * LogUtils logUtils
 * ArrayList<String> apiList
 * ArrayList<ArrayList<BaseCase>> caseNames
 * <p>
 * 并删除部分 finial 对应的方法
 * <p>
 * 将需要测试的api（通常是My开头的方法）重命名以 T_ 开头，去掉T_的方法名为测试对象，
 * 函数的参数改为String或void类型，
 * 返回值改为String，可以返回null
 * 次方法可以作为api添加到自动测试用例里面
 * <p>
 * 增加对应AIDL对象（ILed。。。）并初始化
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
     * 所有API和API对应的cases
     **/
    ArrayList<ArrayList<BaseCase>> caseNames = new ArrayList<ArrayList<BaseCase>>();    // 按照函数名、将所有测试案例保存在对应的函数下
    ArrayList<BaseCase> allCases = new ArrayList<>();   // 保存原有顺序的所有案例
    /**
     * 所有API的name
     * 保存所有测试API的函数名
     **/
    ArrayList<String> apiList = new ArrayList<String>(); // 使用父类的

    int printMode;

    public TestModule() {
        printMode = 1;  // 1 随时打印，0 不打印，2 测完打印
        logUtilsHandler = MyApplication.logUtils;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static void updateService(IDeviceService iDevService) {
        Log.d(TAG, "updateService : IDeviceService");
        try {
            TestModule.iDevService = iDevService;
            iDukpt = iDevService.getDUKPT();
            iPinpad = iDevService.getPinpad(1);
            iScanner = iDevService.getScanner(1);
            iledDriver = iDevService.getLed();
            iBeeper = iDevService.getBeeper();
            iUsbSerialPort = iDevService.getUsbSerialPort();
            iExternalSerialPort = iDevService.getExternalSerialPort();
            irfCardReader = iDevService.getRFCardReader();
            ikld = iDevService.getIKLD();
            irsa = iDevService.getIRSA();
            iPrinter = iDevService.getPrinter();
            iemv = iDevService.getEMV();
            iSde = iDevService.getSde();
            iFelica = iDevService.getFelica();
            iiCodeCard = iDevService.getICode();
            iNtagCard = iDevService.getNtag();
            iMagCardReader = iDevService.getMagCardReader();
            iUltraLightCard = iDevService.getUtrlLightManager();
            iUltraLightCardC = iDevService.getUtrlLightCManager();
            iUltraLightCardEV1 = iDevService.getUtrlLightEV1Manager();
            iUltraLightCardNano = iDevService.getUtrlLightNanoManager();
            iSmartCardReaders = new ISmartCardReader[3];
            for (int i = 0; i < 3; i++) {
                iSmartCardReaders[i] = iDevService.getSmartCardReader(i);
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
        // EMV的都使用 Print in emv 进行打印 : ( this.getClass().getCanonicalName().compareTo( "moudles.newModules.EmvModule" ) == 0 )
        // 其他使用普通的
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
        // EMV的都使用 Print in emv 进行打印 : ( this.getClass().getCanonicalName().compareTo( "moudles.newModules.EmvModule" ) == 0 )
        // 其他使用普通的
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
        printMsgTool("\nCase ID|(描述)\n" + caseID + "|" + caseDesc.trim(), Log.DEBUG);
        String apiName = caseInfo.getApi().trim();

        String methodParam = caseInfo.getMethodParams();
        String resultType = caseInfo.getExpectResultType();
        Log.d(TAG,"过滤前的resultType"+resultType);
        //如果resultType包含public，过滤掉
        if (resultType.contains("public")) {
            resultType = resultType.replace("public", "").trim();
        }
        Log.d(TAG,"过滤后的resultType"+resultType);
        String expectResult = caseInfo.getExpectResult();
        String caseResult = "";
        String caseId = caseInfo.getCaseId();

        Object[] methods = new Object[]{};
        Class<?>[] params = new Class[]{};
        String[] parameters = null;
        if (!TextUtils.isEmpty(methodParam)) {
            methodParam = methodParam.replaceAll("\\|", "");    // 竖线为Excel填写参数换行时产生的（换行符转译为竖线），这里复原
            methodParam = methodParam.replaceAll("\\\\,", "，");
            parameters = methodParam.split(",");
            methodParam = "";
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = parameters[i].trim();
                if (parameters[i].compareTo("-") == 0) {
                    // trim 后，只有 - 的值，被赋值为空
                    parameters[i] = "";
                }
                parameters[i] = parameters[i].replaceAll("，", ","); // 将中文逗号转换回英文逗号
                if (parameters[i].startsWith("ask-file")) {
                    String file = selectFile("Select file for " + caseInfo.getValue(BaseCase.Items.CaseID) + ", " + caseInfo.getValue(BaseCase.Items.Describe), parameters[i]);
                    if (file.length() == 0) {
                        Log.w(TAG, " not set file, skip the case");
                        logUtils.addCaseLog(apiName + ", execute 失败：");
                        Log.e(TAG, apiName + ", execute 失败：");
                        this.printMsgTool(apiName + "|失败", Log.ERROR);

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
            /** 获取本方法所有参数类型 **/
            if (method == null) {
                Log.e(TAG, "Cannot find the method: " + apiName + " (argc=" + methods.length + "), Class: " + getClassName() + ", 注意，测试API的参数都要是String类型");
                logUtils.addCaseLog(apiName + "(argc=" + methods.length + "), <font color='#FF0000'>无匹配的API</font>：");
                Log.e(TAG, apiName + ", execute 失败：");
                this.printMsgTool(apiName + "(argc=" + methods.length + ")|无匹配的API", Log.ERROR);

            } else {
                method.setAccessible(true);
                Object result;
                if (2 == printMode) {
                    ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).setHungupPrinting(true);
                }
                waitPrintingFinish(3); // 等待打印完成再执行API。如果想测试打印和其他操作并发，这里不要等待！. By Simon.
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
                        String msg = apiName + "(): <font color='#0000FF'>成功</font>:" + ret + "<br>" + caseInfo.getValue(BaseCase.Items.ActualValue);
                        msg += "<br>时间 " + timeStamp + " 耗时: <b>" + (endTime - startTime) + "</b> ms<br>";
                        logUtils.addCaseLog(msg);   // 显示 可以使用 html 标签；打印不行
                        msg = apiName + "(), 成功:" + ret + "|" + caseInfo.getValue(BaseCase.Items.ActualValue);
                        msg += "\n时间 " + timeStamp + "|耗时: " + (endTime - startTime) + " ms";

                        this.printMsgTool(msg, Log.INFO);
                    } else {
                        String msg = apiName + "()|失败:" + ret + "\n |" + caseInfo.getErrorMessage(ret) + "\nAPI returns|" + caseInfo.getValue(BaseCase.Items.ActualValue);
                        Log.e(TAG, msg);

                        logUtils.addCaseLog(
                                apiName + ", <font color='#FF0000'>失败：</font>" + ret + ", API 返回:<br><font color='#FF0000'>"
                                        + caseInfo.getValue(BaseCase.Items.ActualValue)
                                        + "</font>"
                                        + "<br><font color='#0000FF'>" + caseInfo.getValue(BaseCase.Items.ExpectResult) + " (预期)</font><br>"
                        );
                        this.printMsgTool(msg, Log.ERROR);

                        msg = "时间 " + timeStamp + "|耗时: " + (endTime - startTime) + " ms";
                        msg += "\n预期结果:|" + caseInfo.getValue(BaseCase.Items.ExpectResult);
                        Log.e(TAG, msg);
                        this.printMsgTool(msg, Log.INFO);
                    }

                } else {
                    Log.w(TAG, "Not check the result!");
                }
            }
            logUtils.addCaseLog(caseId + "execute 完毕");
        } catch (Exception e) {
            e.printStackTrace();
            caseInfo.setResult("Exception");
            logUtils.addCaseLog(caseId + "<font color='#FF0000'><b> Exception:</b><br>" + e + "</font>通常是参数不符合目标格式：数字型、BCD、长度等问题或返回值类型不符合预期");

            if (2 == printMode) {
                ((PrinterModule) ((MyApplication) context).newServiceModule.getModule("Printer")).setHungupPrinting(false);
            }


            Log.w(TAG, "try to connect VF-service again");
            view.MainActivity.vfService.connect();
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
        logUtils.addCaseLog("\n所有案例执行完毕");
    }


    /**
     * 根据API Name 与json配置文件 加入 cases
     *
     * @param cases JSON 文件里配置额所有Cases
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
            // 添加所有的以 T_开头的测试方法（重复的、重写的只加载1次）
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
            // 添加所有的测试案例
            /** 开始读取cases 并配对API **/
            Iterator<BaseCase> caseIterator = cases.iterator();
            while (caseIterator.hasNext()) {
                BaseCase nextCase = caseIterator.next();
                /** 未配置API NAME 则跳过该cases **/
                if (nextCase == null || TextUtils.isEmpty(nextCase.getApi())) {
                    Log.w(TAG, "null case or empty case found. skip it");
                    continue;
                }
                String apiName = nextCase.getApi().trim();
                int index = apiList.indexOf(apiName);
                if (index >= 0) {
                    caseNames.get(index).add(nextCase); // 将案例添加到对应的函数下
                    allCases.add(nextCase);
                    Log.d(TAG, "API [" + apiName + "] add to: " + index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateService(ISystemManager iSysManager) {
        Log.d(TAG, "updateService : ISystemManager");
        iSystemManager = iSysManager;
        try {
            iNetworkManager = iSysManager.getNetworkManager();
            iSettingsManager = iSysManager.getSettingsManager();

        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot get network manager, setting manager");
        }
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
     * logUtils 为替换子类的类名，这样可以不用修改子类已经执行的logUtils的代码
     * 内部采用handle的方式刷新LogUtils
     */
    protected static class logUtils {
        public static void addCaseLog(String message) {
            // 只添加，需要调用 showCaseLog 显示
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
            //  UI 上显示
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
                // 转译 下划线
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
            // 参数已经指定 Bundle 的键名
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
                    // 第3个参数为数据类型，直接加载。这样就可以不修改程序，直接添加参数了
                    int type = Integer.valueOf(keyValue[2].trim());

                    if (keyValue[1].contains("\\\\-+")) {
                        // 转译 等号，这样可以嵌套写 Bundle参数
                        keyValue[1] = keyValue[1].replace("\\\\-+", "=");
                    }

                    insertBundle(bundle, keyValue[0].trim(), keyValue[1].trim(), type);
                } else if (null != bundleConfigs) {
                    for (BundleConfig bundleConfig : bundleConfigs) {
                        // 比对的时候，Excel输入的数据不能有下划线，此处要把key的下划线去掉
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

    // 将bundle转换为字符串
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
//                //目前只支持一位数字
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

    public byte[] StringtoCharArray(String myString){
        if (myString.equals("null")){
            return null;
        }
        char[] chars = myString.toCharArray();  //将字符串转换为字符数组
        byte[] bytes = new byte[chars.length];  //创建一个字节数组来存储结果
        for (int i = 0; i < chars.length; i++) {
            if (chars[i]>='A' && chars[i] <='E'){
                bytes[i] = (byte) (chars[i]-'A'+10);
            }else {
                bytes[i] = (byte) (chars[i] - '0');  //将字符转换为相应的数值类型值
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

    // 转换 键值对
    // ：PAN-5555555555555555.TRACK2-1234567812345678.CARD\\--SN.EXPIRED\\--DATE.DATE.TIME.BALANCE.CURRENCY=0
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
