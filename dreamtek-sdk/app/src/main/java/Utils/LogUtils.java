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
 * Used to control output
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
     * Pass resource ID, automatically loads the corresponding string by ID
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
     * Clear log display
     */
    public void clearLog() {
        caselog = "";
        if (caseLogTv != null) {
            caseLogTv.setText("");
        }
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
     * Pass the name of the string, get the corresponding string value from string.xml
     *
     * @param value the passed name
     * @return the corresponding string
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
