package Utils;
/**
 * Created by WenpengL1 on 2016/12/28.
 */

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * 用来控制输出的
 */
public class LogUtils {
    private static final String TAG = "LogUtils";
    public TextView caseInfoTv;
    public TextView caseLogTv;
    String caselog = "";
    String caseinfo = "";
    Context context;
    Button runOne, runAll;

    public void setCaseInfoTv(TextView caseInfoTv) {
        this.caseInfoTv = caseInfoTv;
    }

    public LogUtils(Context context) {
        this.context = context;
    }

    public void setCaseLogTv(TextView caseLogTv) {
        this.caseLogTv = caseLogTv;
    }
    public void setButtons(Button runOne, Button runAll ){
        this.runAll = runAll;
        this.runOne = runOne;
    }

    /**
     * 传输资源id，自动加载对应的id的字符串
     *
     * @param string_id
     */
    public void printCaseInfo(String string_id) {
        if (null != caseInfoTv)
            caseInfoTv.setText(string_id + "\n" + getCaseMessage(string_id));
    }

    public void printCaseLog(String message) {
        if (null != caseLogTv) {
            caseLogTv.setText(message);
            caseLogTv.invalidate();
        }
    }

    public void addCaseLog(String str) {
        caselog = caselog + "\n" + str;
    }

    public void showCaseLog() {
        if (null!=caseLogTv){
            caseLogTv.setText(Html.fromHtml(caselog) );
            Log.i("showCaseLog", "showCaseLog: " + caselog);
            caseLogTv.invalidate();
        }
    }

    public String getCaseLog(){
        return caselog;
    }

    /**
     * 清除日志显示
     */
    public void clearLog() {
        caselog = "";
        caseLogTv.setText("");
    }

    public void removeText() {
        caseLogTv = null;
        caseInfoTv = null;
    }

    int runningType = 0;
    public void caseFinished( int type ){
        if( runningType != type ){
            return;
        }
        if( runOne != null ){
            runOne.setEnabled(true);
        }
        if( runAll != null ){
            runAll.setEnabled( true );
        }
    }
    public void caseStarting( int type ){
        runningType = type;
        if( runOne != null ){
            runOne.setEnabled(false);
        }
        if( runAll != null ){
            runAll.setEnabled( false );
        }
    }

    /**
     * 传入字符串的名字，得到对应string.xml里面的string值
     *
     * @param value 传入的name
     * @return 对应的字符串
     */
    private String getCaseMessage(String value) {
        String message = "";
        int resID = context.getResources().getIdentifier(value, "string", context.getPackageName());
        if (resID != 0) {
            message = context.getString(resID);
        } else {
            message = " - ";
        }
        return message;
    }
}