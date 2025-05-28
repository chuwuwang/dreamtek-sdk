package Utils;

import android.content.Context;
import android.util.Log;

import com.verifone.activity.BuildConfig;

public class SystemServiceManager extends ServiceManager {
    private static final String TAG = "SystemServiceManager";
    public static final String ACTION_SYSSERVICE = BuildConfig.SYS_INTENT_ACTION;
    public static final String PACKAGE_SYSSERVICE = BuildConfig.SYS_INTENT_PKGNAME;
//    public static final String CLASSNAME_SYSSERVICE = "com.vfi.smartpos.system_service.SystemService";
    public static final String CLASSNAME_SYSSERVICE = null;

    public SystemServiceManager(Context context) {
        super(context, PACKAGE_SYSSERVICE, CLASSNAME_SYSSERVICE, ACTION_SYSSERVICE,
                null);
        Log.d(TAG, "Create connection to System Service ");
    }

    public SystemServiceManager(Context context, ServiceManagerIF serviceManagerIF) {
        super(context, PACKAGE_SYSSERVICE, CLASSNAME_SYSSERVICE, ACTION_SYSSERVICE,
                serviceManagerIF);
        Log.d(TAG, "Create to connection to System Service ");
    }
}
