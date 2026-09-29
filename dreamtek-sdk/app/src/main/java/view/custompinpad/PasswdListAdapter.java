package view.custompinpad;

import android.os.Looper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.verifone.activity.R;

import Utils.LogUtil;
import base.MyApplication;

public class PasswdListAdapter extends RecyclerView.Adapter {
    private final String TAG = this.getClass().getSimpleName();
    private final int MAX_PASSWD_LEN = 12;
    private int passwdBoxNum = 7;

    private int showPasswdLen = 0;
    private OnItemClickListener onItemClickListener;
    private StringBuffer passwdStringBuffer;

    private View parentView;

    public PasswdListAdapter(int passwdBoxNum, View parentView) {
        if (passwdBoxNum > 0 && passwdBoxNum <= MAX_PASSWD_LEN) {
            this.passwdBoxNum = passwdBoxNum;
        }
        LogUtil.d(TAG, "Passwd box number=" + passwdBoxNum);
        passwdStringBuffer = new StringBuffer();

        this.parentView = parentView;
    }

    public void showPasswd(int passwdLen) {
        LogUtil.d(TAG, "show passwd passwdlen=" + passwdLen + " max box=" + passwdBoxNum);
        if (passwdLen < 0 || passwdLen > passwdBoxNum) {
            return;
        }

        boolean isUIThread = (Looper.myLooper() == Looper.getMainLooper());
        if (!isUIThread) {
            throw new RuntimeException("Need call this function on UI thread!");
        }

        showPasswdLen = passwdLen;
        notifyDataSetChanged();
        int left = (560-40*passwdLen)/2;
        parentView.setPadding(left, 0, left, 0);
    }

    public void addOnePasswordChar(String onePasswdChar) {
        LogUtil.d("str buff len=" + passwdStringBuffer.length());
        if (passwdStringBuffer.length() < passwdBoxNum) {
            if (onePasswdChar != null && onePasswdChar.length() == 1) {
                showPasswdLen++;
                showPasswd(showPasswdLen);
                passwdStringBuffer.append(onePasswdChar);
            }
        }
    }

    public void delOnePasswordChar() {
        int passwdLen = passwdStringBuffer.length();
        if (passwdLen > 0) {
            showPasswdLen--;
            showPasswd(showPasswdLen);
            passwdStringBuffer.delete(passwdLen - 1, passwdLen);
        }
    }

    public String getPasswd() {
        return passwdStringBuffer.toString();
    }

    public void clearPasswd() {
        showPasswd(0);
        passwdStringBuffer.delete(0, passwdStringBuffer.length());
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_passwd, parent, false);
        viewRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, (int)v.getTag());
                }
            }
        });
        return new PasswdViewHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PasswdViewHolder passwdViewHolder = (PasswdViewHolder) holder;
        passwdViewHolder.getRootView().setTag(position);

        if (position <= showPasswdLen - 1) {
            passwdViewHolder.getIvPasswd().setImageDrawable(MyApplication.getContext().getDrawable(R.drawable.bg_pass_full));
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return passwdBoxNum;
    }

    private class PasswdViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private ImageView ivPasswd;

        public PasswdViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            ivPasswd = rootView.findViewById(R.id.iv_passwd);
        }


        public View getRootView() {
            return rootView;
        }

        public ImageView getIvPasswd() {
            return ivPasswd;
        }
    }
}
