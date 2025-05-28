package Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.activity.R;

import java.util.ArrayList;

import base.MyApplication;
import moudles.ServiceMoudle;
import moudles.newModules.ServiceModule;

public class MyExpandAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> parents;
    private ArrayList<ArrayList<String>> childs;
    private Context context;
    private TextView caseInfoTv;
    private TextView caseLogTv;
    private ServiceMoudle serviceMoudle;
    private String mouduleName;
    private CaseNameUtils caseNameUtils;
    private int groupPosition;
    private int childPosition;

    /**
     * 得到适配器
     *
     * @param parents     一级列表
     * @param childs      二级列表
     * @param caseInfoTv  描述文本
     * @param caseLogTv   日志文本
     * @param mouduleName 模块名字（按钮名字）
     * @param context     上下文
     */
    public MyExpandAdapter(ArrayList<String> parents, ArrayList<ArrayList<String>> childs, TextView caseInfoTv,
                           TextView caseLogTv, String mouduleName, CaseNameUtils caseNameUtils, Context context) {
        this.parents = parents;
        this.childs = childs;
        this.context = context;
        this.caseInfoTv = caseInfoTv;
        this.caseLogTv = caseLogTv;
        this.mouduleName = mouduleName;
        serviceMoudle = MyApplication.serviceMoudle;
        this.caseNameUtils = caseNameUtils;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public int getGroupCount() {
        if (parents!=null){
//            System.out.println("******** getGroupCount return "+parents.size());
            return parents.size();
        }else {
//            System.out.println("******** getGroupCount return "+0);
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expand_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.text_tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.indicator);
        textView.setText(parents.get(groupPosition));
        if (isExpanded) {
            imageView.setBackground(context.getResources().getDrawable(R.drawable.expandable_selector));
        } else {
            imageView.setBackground(context.getResources().getDrawable(R.drawable.expandable_normal));
        }
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             final ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expand_item_layout, null);
        final TextView textView = (TextView) view.findViewById(R.id.item_text_tv);
        textView.setText(childs.get(groupPosition).get(childPosition));


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //重置
                for (int i = 0; i < parent.getChildCount(); i++) {
                        View childAt = parent.getChildAt(i);
                        final TextView childView = (TextView) childAt.findViewById(R.id.item_text_tv);
                        if (childView != null)
                            childView.setCompoundDrawables(null, null, null, null);
                }

                setRunTheMethod(groupPosition, childPosition);
                Drawable drawable = context.getResources().getDrawable(R.drawable.cb_select);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(drawable, null, null, null);


            }
        });
        return view;
    }

    private void setRunTheMethod(int groupPosition, int childPosition) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        serviceMoudle.showTheCaseInfo(mouduleName, groupPosition, childPosition);

    }

    public void runTheMethod() {
        serviceMoudle.runTheMethod(mouduleName, groupPosition, childPosition);
        ((MyApplication) context.getApplicationContext()).logUtils.showCaseLog();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}