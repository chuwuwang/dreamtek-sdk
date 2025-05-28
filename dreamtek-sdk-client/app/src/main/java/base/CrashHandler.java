package base;
/**
 * Created by WenpengL1 on 2016/12/26.
 */
import android.content.Context;
import android.widget.Toast;

/**
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static CrashHandler INSTANCE;
    private Context context;
    //1.私有化构造方法
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
        Toast.makeText(context, "挂掉了，去报告开发", Toast.LENGTH_SHORT).show();
        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}