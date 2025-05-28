package moudles.newModules;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.dreamtek.smartpos.system_service.aidl.IAppDeleteObserver;
import com.dreamtek.smartpos.system_service.aidl.IAppInstallObserver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LASCIATE OGNI SPERANZA, VOI CH'ENTRATE
 */
public class SystemServiceModule extends TestModule {
    private static final String TAG = "SystemServiceModule";
    public boolean isConnect = false;

    public SystemServiceModule() {
        super();

    }
    public SystemServiceModule(Context context) {
        this();
        this.context = context;
    }

    int installResult = -12345;

//
//    /**
//     * Install an apk
//     *
//     * @param apkPath              - apk's absolute url.
//     * @param installerPackageName - packagename of installer apk.
//     * @since 1.0.0
//     */
//    public String T_installApp(String apkPath, String installerPackageName) {
//        return T_installApp( apkPath, installerPackageName, "0");
//    }
//    public String T_installApp(String apkPath, String installerPackageName, String sDelay ) {
//        installResult = -12345;
////        if (TextUtils.isEmpty(apkPath) || TextUtils.isEmpty(installerPackageName)) {
////            logUtils.clearLog();
////            logUtils.addCaseLog("Case failed, invalid params");
////            return "invalid params";
////        }
//        IAppInstallObserver listener = new IAppInstallObserver.Stub() {
//            @Override
//            public void onInstallFinished(String packageName, int returnCode) throws RemoteException {
//                logUtils.clearLog();
//                logUtils.addCaseLog("install: [" + packageName + "] finished. Return Code " + returnCode);
//                synchronized ( SystemServiceModule.class ){
//                    installResult = returnCode;
//                }
//            }
//        };
//        try {
//            iSystemManager.installApp(apkPath, listener, installerPackageName);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        int result = -12345;
//        do{
//            try {
//                Thread.sleep( 1000 );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            synchronized ( SystemServiceModule.class ){
//                result = installResult;
//            }
//
//        } while ( result == -12345);
//        Log.d(TAG, "installApp returns:" + result );
//        logUtils.addCaseLog( "installApp returns:" + result );
//        int delay = Integer.valueOf( sDelay );
//        if( delay > 0 ){
//            try {
//                Log.d(TAG, "delay another " + delay + " ms, before returns" );
//                Thread.sleep( delay );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if( result == 0 ){
//            return "0";
//        } else {
//            return "Error:"+result;
//        }
//    }
//
//    /**
//     * remove an apk
//     *
//     * @param packageName - package name which need to remove.
//     * @since 1.0.0
//     */
//    public String T_uninstallApp(String packageName) {
//        installResult = -12345;
////        if (TextUtils.isEmpty(packageName)) {
////            logUtils.clearLog();
////            logUtils.addCaseLog("Case failed, invalid params");
////            return "invalid params";
////        }
//        IAppDeleteObserver listener = new IAppDeleteObserver.Stub() {
//
//            @Override
//            public void onDeleteFinished(String packageName, int returnCode) throws RemoteException {
//                logUtils.clearLog();
//                logUtils.addCaseLog(packageName + " delete finished. Return Code " + returnCode);
//                synchronized ( SystemServiceModule.class ){
//                    installResult = returnCode;
//                }
//            }
//
//        };
//        try {
//            iSystemManager.uninstallApp(packageName, listener);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        int result = -12345;
//        do{
//            try {
//                Thread.sleep( 1000 );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            synchronized ( SystemServiceModule.class ){
//                result = installResult;
//            }
//
//        } while ( result == -12345);
//        Log.d(TAG, "unInstallApp returns:" + result );
//        logUtils.addCaseLog( "unInstallApp returns:" + result );
//        if( result == 0 ){
//            return "0";
//        } else {
//            return "Error:"+result;
//        }
//    }
//    public String T_chekcK21Update(String sysPath, String binPath ) {
//        return T_chekcK21Update( sysPath, binPath, "0");
//    }
//    public String T_chekcK21Update(String sysPath, String binPath, String sDelay ) {
////        if (TextUtils.isEmpty(sysPath) || TextUtils.isEmpty(binPath)) {
////            logUtils.clearLog();
////            logUtils.addCaseLog("Case failed, invalid params");
////            return "invalid params";
////        }
//        try {
//            if( iSystemManager.chekcK21Update( sysPath, binPath ) ){
//                int delay = Integer.valueOf( sDelay );
//                if( delay > 0 ){
//                    try {
//                        Thread.sleep( delay);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                logUtils.addCaseLog("upgrade K21 success");
//                return "success";
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        logUtils.addCaseLog("upgrade K21 failed");
//        return "fail";
//    }
//
//    public String T_delay(String milliseconds) {
//        if (TextUtils.isEmpty(milliseconds)) {
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, invalid params");
//            return "";
//        }
//
//        int mseconds = Integer.valueOf(milliseconds);
//
//        printMsgTool( "Delay start: " + milliseconds, Log.DEBUG );
//        logUtils.printCaseLog( "Delay start: " + milliseconds );
//
//        try {
//            Thread.sleep( mseconds );
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.d(TAG, "Delay " + mseconds + " 毫秒" );
//        logUtils.printCaseLog( "Delay " + mseconds + " 毫秒" );
//        printMsgTool( "Delay finished ", Log.DEBUG );
//        return String.valueOf(mseconds);
//    }
//    public String T_getInfo( String type, String format ){
//        if( TextUtils.isEmpty( type )){
//            logUtils.clearLog();
//            logUtils.addCaseLog("Case failed, invalid params");
//            return "";
//        }
//        Log.d(TAG, "Call getInfo: " + type + " , " + format );
//        String result = "";
//
//        switch ( type.toLowerCase() ){
//            case "datetime":
//                SimpleDateFormat sdf;
//                if( format.length() > 1 ) {
//                    Log.d(TAG, "using format:" + format);
//                    sdf = new SimpleDateFormat(format); // "yyyy-MM-dd_HHmmss"
//                } else {
//                    Log.d(TAG, "using default format: MM-dd_HHmmss" );
//                    sdf = new SimpleDateFormat("MM-dd_HHmmss");
//                }
//                result = sdf.format(new Date()) ;
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//    public void T_reboot() throws RemoteException {
//        iSystemManager.reboot(  );
//    }
//
//    public void T_isMaskHomeKey(String state) throws RemoteException {
//        iSystemManager.isMaskHomeKey(Boolean.valueOf(state));
//    }
//
//    public void T_isMaskStatusBard(String state) throws RemoteException {
//        iSystemManager.isMaskHomeKey(Boolean.valueOf(state));
//    }
//
//    public void T_updateROM(String zipPath) throws RemoteException {
//        iSystemManager.updateROM(zipPath);
//    }
//
////        INetworkManager getNetworkManager();
//
//    public void T_setLocationMode(String status) throws RemoteException {
//        iSystemManager.setLocationMode(Integer.valueOf(status));
//    }
//
//    public boolean T_isAdbMode() throws RemoteException {
//        return  iSystemManager.isAdbMode(  );
//    }
//
//    public boolean T_killApplication(String packageName) throws RemoteException {
//        return iSystemManager.killApplication(packageName);
//    }
//
//    public boolean T_restartApplication(String packageName) throws RemoteException {
//        return iSystemManager.restartApplication(packageName);
//    }
///*void initLogcat(int logcatBufferSize, int logcatBufferSizeSuffix, in Bundle bundle);*/
//
//    public String T_getLogcat(String logcatFileName, String compressType) throws RemoteException {
//        return iSystemManager.getLogcat(logcatFileName,Integer.valueOf(compressType));
//    }
//
////    Bundle getLaunchAppsInfo(long beginTime, long endTime);
////    ISettingsManager getSettingsManager();
////    Bitmap takeCapture();
//
//    public void T_shutdownDevice() throws RemoteException {
//        iSystemManager.shutdownDevice(  );
//    }
//
//    public boolean T_UpdateSecurityDriver(String updatePackagePath) throws RemoteException {
//        return iSystemManager.UpdateSecurityDriver(updatePackagePath);
//    }
//    boolean T_isAppForeground(String packageName) throws RemoteException {
//        return iSystemManager.isAppForeground(packageName);
//    }
}
