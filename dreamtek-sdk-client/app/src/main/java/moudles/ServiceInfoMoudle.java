package moudles;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.dreamtek.smartpos.deviceservice.aidl.IServiceInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;

public class ServiceInfoMoudle {

    Context context;
    IServiceInfo iServiceInfo;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    LogUtils logUtils;
    ArrayList<String> getSercurityDriverVersion = new ArrayList<String>();
    ArrayList<String> getKernelVersion = new ArrayList<String>();
    ArrayList<String> getServiceVersion = new ArrayList<String>();

    public ServiceInfoMoudle(Context context,IServiceInfo iServiceInfo){
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iServiceInfo = iServiceInfo;
        addAllapi();
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.ServiceInfoMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "F01":
                            getSercurityDriverVersion.add(i.getName());
                            break;
                        case "F02":
                            getKernelVersion.add(i.getName());
                            break;
                        case "F03":
                            getServiceVersion.add(i.getName());
                            break;
                    }
                }
            }
            caseNames.add(getSercurityDriverVersion);
            caseNames.add(getKernelVersion);
            caseNames.add(getServiceVersion);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String My01getSecurityDriverVersion(){
        try {
            String result = iServiceInfo.getSercurityDriverVersion();
            this.printMsgTool("The execution result：SecurityDriver version=" + result);
            logUtils.addCaseLog("SecurityDriver version=" + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bundle My02getKernelVersion(){
        try {
            Bundle result = iServiceInfo.getKernelVersion();
            logUtils.addCaseLog("SmartEMV = " + result.getString("SmartEMV"));
            logUtils.addCaseLog("Visa = " + result.getString("Visa"));
            logUtils.addCaseLog("MasterCard = " + result.getString("MasterCard"));
            logUtils.addCaseLog("JCB = " + result.getString("JCB"));
            logUtils.addCaseLog("AMEX = " + result.getString("AMEX"));
            logUtils.addCaseLog("Discover = " + result.getString("Discover"));
            logUtils.addCaseLog("QuickPass = " + result.getString("QuickPass"));
            logUtils.addCaseLog("GemaltoPure = " + result.getString("GemaltoPure"));
            logUtils.addCaseLog("RuPay = " + result.getString("RuPay"));
            logUtils.addCaseLog("Mir = " + result.getString("Mir"));
            logUtils.addCaseLog("SmartEMV_Checksum = " + result.getString("SmartEMV_Checksum"));
            logUtils.addCaseLog("Visa_Checksum = " + result.getString("Visa_Checksum"));
            logUtils.addCaseLog("MasterCard_Checksum = " + result.getString("MasterCard_Checksum"));
            logUtils.addCaseLog("JCB_Checksum = " + result.getString("JCB_Checksum"));
            logUtils.addCaseLog("AMEX_Checksum = " + result.getString("AMEX_Checksum"));
            logUtils.addCaseLog("Discover_Checksum = " + result.getString("Discover_Checksum"));
            logUtils.addCaseLog("QuickPass_Checksum = " + result.getString("QuickPass_Checksum"));
            logUtils.addCaseLog("GemaltoPure_Checksum = " + result.getString("GemaltoPure_Checksum"));
            logUtils.addCaseLog("RuPay_Checksum = " + result.getString("RuPay_Checksum"));
            this.printMsgTool("The execution result：SmartEMV =" + result.getString("SmartEMV"));
            this.printMsgTool("The execution result：Visa =" + result.getString("Visa"));
            this.printMsgTool("The execution result：MasterCard =" + result.getString("MasterCard"));
            this.printMsgTool("The execution result：JCB =" + result.getString("JCB"));
            this.printMsgTool("The execution result：AMEX =" + result.getString("AMEX"));
            this.printMsgTool("The execution result：Discover =" + result.getString("Discover"));
            this.printMsgTool("The execution result：QuickPass =" + result.getString("QuickPass"));
            this.printMsgTool("The execution result：GemaltoPure =" + result.getString("GemaltoPure"));
            this.printMsgTool("The execution result：RuPay =" + result.getString("RuPay"));
            this.printMsgTool("The execution result：Mir =" + result.getString("Mir"));

            this.printMsgTool("The execution result：SmartEMV_Checksum =" + result.getString("SmartEMV_Checksum"));
            this.printMsgTool("The execution result：Visa_Checksum =" + result.getString("Visa_Checksum"));
            this.printMsgTool("The execution result：MasterCard_Checksum =" + result.getString("MasterCard_Checksum"));
            this.printMsgTool("The execution result：JCB_Checksum =" + result.getString("JCB_Checksum"));
            this.printMsgTool("The execution result：AMEX_Checksum =" + result.getString("AMEX_Checksum"));
            this.printMsgTool("The execution result：Discover_Checksum =" + result.getString("Discover_Checksum"));
            this.printMsgTool("The execution result：QuickPass_Checksum =" + result.getString("QuickPass_Checksum"));
            this.printMsgTool("The execution result：GemaltoPure_Checksum =" + result.getString("GemaltoPure_Checksum"));
            this.printMsgTool("The execution result：RuPay_Checksum =" + result.getString("RuPay_Checksum"));
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String My03getServiceVersion(){
        try {
            String result = iServiceInfo.getServiceVersion();
            this.printMsgTool("The execution result：service result =" + result);
            logUtils.addCaseLog("service result = " + result);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void F01001() {
        My01getSecurityDriverVersion();
    }

    public void F02001() {
        My02getKernelVersion();
    }

    public void F03001(){
        My03getServiceVersion();
    }



    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.ServiceInfoMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "Perform case");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }

    private void printMsgTool(String msg) {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
