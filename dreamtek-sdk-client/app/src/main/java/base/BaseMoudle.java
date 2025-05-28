package base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import Utils.LogUtils;

import static base.MyApplication.serviceMoudle;

/**
 * Created by WenpengL1 on 2017/1/7.
 */
public abstract class BaseMoudle {
    protected Context context;
    protected LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("msg"));
        }
    };

    public BaseMoudle(Context context) {
        this.context = context;
        logUtils = serviceMoudle.logUtils;
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    public abstract void addAllapis();
}