package view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.verifone.activity.R;

import java.util.ArrayList;

import Utils.CaseNameUtils;
import Utils.MyExpandAdapter;
import Utils.NewExpandAdapter;
import base.MyApplication;
import butterknife.BindView;
import butterknife.ButterKnife;
import entity.cases.BaseCase;

import static com.verifone.activity.R.id.caselog_tv;

/**
 * Tertiary (third-level) UI screen.
 */
public class NewThirdActivity extends AppCompatActivity {
    private static final String TAG = "NewThirdActivity";
    Intent intent;
    String value = "";
    ArrayList<String> parentNames;
    ArrayList<ArrayList<BaseCase>> childNames;
    NewExpandAdapter myExpandAdapter;
    CaseNameUtils caseNameUtils;
    @BindView(R.id.items_lv)
    ExpandableListView itemsLv;
    @BindView(R.id.caseinfo_tv)
    TextView caseinfoTv;
    @BindView(caselog_tv)
    TextView caselogTv;
    @BindView(R.id.execute_bt)
    Button executeBt;
    @BindView(R.id.runAll_bt)
    Button runAllBt;
    @BindView(R.id.edit_param_bt)
    Button editParamBt;

    private ArrayList<BaseCase> cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);  // Disable screenshots for testing.

        caseinfoTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        caselogTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        runAllBt.setVisibility(View.VISIBLE);
        editParamBt.setVisibility( View.VISIBLE);
        MyApplication.logUtils.setCaseInfoTv(caseinfoTv);
        MyApplication.logUtils.setCaseLogTv(caselogTv);
        MyApplication.logUtils.setButtons( executeBt, runAllBt );
        intent = getIntent();
        if (null != intent) {
            value = intent.getStringExtra("name");
            cases = intent.getParcelableArrayListExtra("cases");
        }
        caseNameUtils = CaseNameUtils.getInstance(this);
        // Get the second-level array: module name, method name, and method array size.
        childNames = caseNameUtils.getCaseNames(value,cases);
        parentNames = caseNameUtils.getMoudleAPIs(value, cases);

        if( null != childNames ) {
            Log.d(TAG, "child names: " + childNames.size());
        }
        if( null != parentNames ) {
            Log.d(TAG, "parent names: " + parentNames.size());
        }
        Log.d(TAG, "Name: " + value );

        myExpandAdapter = new NewExpandAdapter(parentNames, childNames, caseinfoTv, caselogTv, value, caseNameUtils, this);
        itemsLv.setGroupIndicator(null);
        itemsLv.setAdapter(myExpandAdapter);
        itemsLv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = itemsLv.getExpandableListAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (i != groupPosition) {
                        itemsLv.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.logUtils.removeText();
    }

    // Called by the view to run the method selected in the adapter.
    public void runTheMethod(View view) {
        MyApplication.logUtils.caseStarting(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                myExpandAdapter.runTheMethod();

            }
        }).start();;
    }

    public void runAllMethod(View view) {
        MyApplication.logUtils.caseStarting(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                myExpandAdapter.runAllMethod();
            }
        }).start();;
    }
    public void editMethodParam(View view) {
//        editParamBt.setClickable(false);
        myExpandAdapter.editMethodParam( this );
    }

}