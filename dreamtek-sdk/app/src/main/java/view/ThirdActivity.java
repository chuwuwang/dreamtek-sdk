package view;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.verifone.activity.R;

import java.util.ArrayList;

import Utils.CaseNameUtils;
import Utils.MyExpandAdapter;
import base.MyApplication;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.verifone.activity.R.id.caselog_tv;

/**
 * Tertiary (third-level) UI screen.
 */
public class ThirdActivity extends AppCompatActivity {
    Intent intent;
    String value = "";
    ArrayList<String> parentNames;
    ArrayList<ArrayList<String>> childNames;
    MyExpandAdapter myExpandAdapter;
    CaseNameUtils caseNameUtils;
    @BindView(R.id.items_lv)
    ExpandableListView itemsLv;
    @BindView(R.id.caseinfo_tv)
    TextView caseinfoTv;
    @BindView(caselog_tv)
    TextView caselogTv;
    @BindView(R.id.execute_bt)
    Button executeBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);
        caseinfoTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        caselogTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        MyApplication.logUtils.setCaseInfoTv(caseinfoTv);
        MyApplication.logUtils.setCaseLogTv(caselogTv);
        intent = getIntent();
        if (null != intent) {
            value = intent.getStringExtra("name");
        }
        caseNameUtils = CaseNameUtils.getInstance(this);
        parentNames = caseNameUtils.getMoudleAPIs(value);
        // Get the second-level array: module name, method name, and method array size.
        childNames = caseNameUtils.getCaseNames(value);
        myExpandAdapter = new MyExpandAdapter(parentNames, childNames, caseinfoTv, caselogTv, value, caseNameUtils, this);
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
        myExpandAdapter.runTheMethod();
    }

    //public void editMethodParam(View view) {
    //}
}