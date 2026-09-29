package base;
/**
 * Created by WenpengL1 on 2016/12/26.
 */
import android.content.Context;
import android.widget.Toast;

import com.verifone.activity.R;

/**
 * Custom exception handler class, implements UncaughtExceptionHandler interface
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    // Requirement: only one MyCrash-Handler for the entire application
    private static CrashHandler INSTANCE;
    private Context context;
    //1. Privatize the constructor method
    private CrashHandler(Context context) {
        this.context=context;
    }
    public static synchronized CrashHandler getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler( context);
        return INSTANCE;
    }
    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        Toast.makeText(context, context.getString(R.string.toast_crashed), Toast.LENGTH_SHORT).show();
        // Kill the current process
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}