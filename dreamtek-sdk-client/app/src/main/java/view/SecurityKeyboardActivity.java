package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.verifone.activity.R;
import com.verifone.smartpos.api.entities.pinpad.KeyCoorInfo;
import com.verifone.smartpos.utils.StringUtil;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;
import com.dreamtek.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.dreamtek.smartpos.deviceservice.aidl.sde.ISde;
import com.dreamtek.smartpos.deviceservice.aidl.sde.SecurityKeyBoardListener;
import static android.content.ContentValues.TAG;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Utils.LogUtils;
import base.MyApplication;
import entity.cases.KeyBoardDatas;
import moudles.ServiceMoudle;
import moudles.newModules.SdeModule;
import moudles.newModules.data.PanBundleStore;

public class SecurityKeyboardActivity extends Activity {

/*


    private View v;
    private PinpadPopUpWindow mPinpadPopUpWindow;
    ServiceMoudle serviceMoudle;
    IPinpad iPinpad;
    ISde iSde;
    Context context;

    IDukpt iDukpt;
    LogUtils logUtils;
    private boolean isDestroyed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "oncreate...");
        logUtils = MyApplication.serviceMoudle.logUtils;
        isDestroyed =false;

        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_security_keyboard);
        v = LayoutInflater.from(this).inflate(R.layout.activity_security_keyboard, null);

        serviceMoudle = MyApplication.serviceMoudle;
        IDeviceService deviceService = serviceMoudle.deviceService;
        try {
            iPinpad = deviceService.getPinpad(0);
            iSde = deviceService.getSde();
            iDukpt = deviceService.getDUKPT();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mPinpadPopUpWindow = new PinpadPopUpWindow(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("message"));
            logUtils.showCaseLog();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume...");
    }

    @Override
    public void onAttachedToWindow() {
        Log.i(TAG, "onAttachedToWindow");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPinpadPopUpWindow.showPopupWindow(v);
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i(TAG, "onWindowFocusChanged forcuse:" + hasFocus);
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus)
            return;

        //获取param
        Intent intent = getIntent();
        Bundle bundleRecv = intent.getBundleExtra("initBundle");

        byte[] pinLimit = bundleRecv.getByteArray("pinLimit");
        byte[] displayKeyValue = bundleRecv.getByteArray("displayKeyValue");
        int timeout = bundleRecv.getInt("timeout");

        Log.e(TAG,"测试：displayKeyValue="+ Arrays.toString(displayKeyValue));
//        Boolean isOnline = bundleRecv.getBoolean("isOnline");
//        String panBlock = bundleRecv.getString("panBlock");
//        String promptString = bundleRecv.getString("promptString");
//        int desType = bundleRecv.getInt("desType");

        final Bundle param = new Bundle();

        param.putByteArray("displayKeyValue",displayKeyValue);
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", timeout);
//        param.putBoolean("isOnline", isOnline);
//        param.putString("pan", panBlock);
//        param.putString("promptString", promptString);
//        param.putInt("desType", desType);

        final SecurityKeyBoardListener securityKeyBoardListener = new SecurityKeyBoardListener.Stub() {
            @Override
            public void onInput(int len, int key) throws RemoteException {
                // the key is * always.
                Log.d(TAG, "onInput, length:" + len + ",key:" + key);
                char buf[] = new char[len];
                Arrays.fill(buf, '*');
                final String s = String.valueOf(buf);
                Log.d(TAG, s);
                // call up UI to update the view

                View contentView = mPinpadPopUpWindow.getContentView();
                contentView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPinpadPopUpWindow.dsp_pwd.setText(s);
                    }
                });
            }

            @Override
            public void onConfirm(Bundle panInfos) throws RemoteException {

                Log.d(TAG, "securityKeyBoardListener onConfirm executed");
//                printMsgTool("securityKeyBoardListener onConfirm executed");
                byte[] encryptedData = panInfos.getByteArray("encryptedData");
                String obfuscatedPAN = panInfos.getString("obfuscatedPAN");
                byte[] ksn = panInfos.getByteArray("ksn");
                byte[] initVec = panInfos.getByteArray("initVec");

//                KeyBoardDatas.getInstance().setEncryptedData(encryptedData);
//                KeyBoardDatas.getInstance().setObfuscatedPAN(obfuscatedPAN);
//                KeyBoardDatas.getInstance().setKsn(ksn);
//                KeyBoardDatas.getInstance().setInitVec(initVec);

                Log.d(TAG,"---------------result-----------------");
                Log.d(TAG, "encryptedData:"+StringUtil.byte2HexStr(encryptedData));
                Log.d(TAG, "obfuscatedPAN:"+obfuscatedPAN);
                Log.d(TAG, "ksn:"+StringUtil.byte2HexStr(ksn));
                Log.d(TAG,"initVec = "+StringUtil.byte2HexStr(initVec));

                Message message1 = new Message();
                message1.getData().putString("message","encryptedData:"+StringUtil.byte2HexStr(encryptedData)+"｜obfuscatedPAN:"+obfuscatedPAN+"｜ksn:"+StringUtil.byte2HexStr(ksn)+"｜initVec = "+StringUtil.byte2HexStr(initVec));
                handler.sendMessage(message1);


//                printMsgTool("encryptedData:"+StringUtil.byte2HexStr(encryptedData));
//                printMsgTool("obfuscatedPAN:"+obfuscatedPAN);
//                printMsgTool("ksn:"+StringUtil.byte2HexStr(ksn));
//                printMsgTool("initVec = "+StringUtil.byte2HexStr(initVec));
                if (ksn ==null){
                    Log.d(TAG, "getKSN failed");
                    String errString = iDukpt.getLastError();
                    if (errString != null) {
                        Log.d(TAG, errString);
                        Log.d(TAG, "Result：getKSN failed，error=" + errString);
                        logUtils.addCaseLog("Result：getKSN failed，error="+errString);
//                        printMsgTool("Result：getKSN failed，error="+errString);
                    }
                }else {
                    Log.d(TAG,"Result：getKSN success，result=" + StringUtil.byte2HexStr(ksn));
//                    printMsgTool("ksn = "+StringUtil.byte2HexStr(ksn));

                }

                exitKeyBoardOnUI();
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG, "onCancel");
                exitKeyBoardOnUI();
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                Log.d(TAG, "onError errorCode = "+errorCode);
//                printMsgTool("onError errorCode = "+errorCode);
                exitKeyBoardOnUI();
            }
        };

        View contentView = mPinpadPopUpWindow.getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                List<PinKeyCoorInfo> pinKeys = mPinpadPopUpWindow.getKeyCoorInfos();
                try {
                    Map keyMap = iSde.initSecurityKeyBoardView(param, pinKeys, securityKeyBoardListener);
                    Log.d(TAG, "size:" + keyMap.size());
                    Set<Map.Entry<String, String>> entrys = keyMap.entrySet();
                    for (Map.Entry<String, String> entry : entrys) {
                        Log.d(TAG, entry.getKey() + "--" + entry.getValue());
                        Button btn = null;
                        int index = entry.getKey().charAt(4) - '0';
                        if (index >= 0 && index < 10) {
                            mPinpadPopUpWindow.btn[index].setText(entry.getValue() + "\n[" + index + "]");
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("inputType", 1);
                    iSde.startSecurityKeyBoardEntry(bundle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public byte[] getDukptKsn(int keyIdx) {
        byte[] result = null;
        try {
            result = iDukpt.getDukptKsn(keyIdx);
            if (result == null) {
                Log.d(TAG,"getKSN failed");
                String errString = iDukpt.getLastError();
                if (errString != null) {
                    Log.d(TAG,errString);
                }
            } else {
                Log.d(TAG,"getKSN success");
                Log.d(TAG,"KSN: " + StringUtil.byte2HexStr(result));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void exitKeyBoardOnUI() {
        Log.i(TAG, "exitKeyBoardOnUI");

        synchronized (SdeModule.keyLock){
            if (SdeModule.activityCount > 0) {
                SdeModule.activityCount --;
            }
            Log.i(TAG,"Security activityConunt="+SdeModule.activityCount);
            try {
                Log.d(TAG,"等待300ms");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (SdeModule.activityCount == 0){
                SdeModule.keyLock.notify();
                Log.i(TAG,"解锁Security");
            }
        }

        runOnUiThread(new Runnable() {
            public void run() {
                mPinpadPopUpWindow.dismiss();
                finish();
            }
        });
//        try {
//            iPinpad.endPinInputCustomView();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }


    class PinpadPopUpWindow extends PopupWindow implements View.OnClickListener {
        private View conentView;
        public Button btn[] = new Button[10];
        public Button btn_conf, btn_cancel, btn_del;
        public TextView dsp_pwd;
        private Map<String, Button> keysMap = new HashMap<>();
        private LinearLayout layout_keyboard;
        TextView textView_nouse2;

        public PinpadPopUpWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.keyboard, null);
            this.setContentView(conentView);

            textView_nouse2 = getConentView().findViewById(R.id.textView_nouse2);

            updateTextViewContent();

            setBackgroundAlpha(0.2f);               // 设置背景透明度
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            this.setFocusable(false);
            this.setOutsideTouchable(false);
            this.update();

            //背景
            ColorDrawable dw = new ColorDrawable(0);
            this.setBackgroundDrawable(dw);
            findView(conentView);
        }

        @SuppressLint("SetTextI18n")
        private void updateTextViewContent() {
            if (null!=PanBundleStore.getBundle()){
                String pan = PanBundleStore.getBundle().getString("PAN");
                String pin = PanBundleStore.getBundle().getString("PIN");
                if (pan!=null || pin!=null ){
                    Log.d(TAG,"开始设置，PAN = "+pan + "; pin = "+pin);
                    textView_nouse2.setText("PAN = " + pan + "; PIN = " + pin);
                }else {
                    Log.d(TAG,"PAN 和 PIN空，无法设置");
                }
            }else {
                Log.d(TAG,"Don't need update text view Content");
            }

        }

        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                Log.d(TAG, "showPopupWindow");
                updateTextViewContent();
                this.showAtLocation(parent, Gravity.CENTER, 0, 0);
            } else {
                this.dismiss();
            }
        }

        public View getConentView() {
            return conentView;
        }

        // 设置屏幕背景透明度
        private void setBackgroundAlpha(float alpha) {
            WindowManager.LayoutParams mLayoutParams = getWindow().getAttributes();
            mLayoutParams.alpha = alpha;
            getWindow().setAttributes(mLayoutParams);
        }


        private void findView(View keyboardView) {

            btn[0] = (Button) keyboardView.findViewById(R.id.keyboard_btn0);
            btn[1] = (Button) keyboardView.findViewById(R.id.keyboard_btn1);
            btn[2] = (Button) keyboardView.findViewById(R.id.keyboard_btn2);
            btn[3] = (Button) keyboardView.findViewById(R.id.keyboard_btn3);
            btn[4] = (Button) keyboardView.findViewById(R.id.keyboard_btn4);
            btn[5] = (Button) keyboardView.findViewById(R.id.keyboard_btn5);
            btn[6] = (Button) keyboardView.findViewById(R.id.keyboard_btn6);
            btn[7] = (Button) keyboardView.findViewById(R.id.keyboard_btn7);
            btn[8] = (Button) keyboardView.findViewById(R.id.keyboard_btn8);
            btn[9] = (Button) keyboardView.findViewById(R.id.keyboard_btn9);
            btn_del = (Button) keyboardView.findViewById(R.id.keyboard_btn_delete);
            btn_cancel = (Button) keyboardView.findViewById(R.id.keyboard_btn_cancel);
            btn_conf = (Button) keyboardView.findViewById(R.id.keyboard_btn_confirm);
            for (int i = 0; i < 10; i++) {
                btn[i].setOnClickListener(this);
            }

            btn_conf.setOnClickListener(this);
            btn_cancel.setOnClickListener(this);
            btn_del.setOnClickListener(this);

            dsp_pwd = (TextView) keyboardView.findViewById(R.id.dsp_pwd);

            layout_keyboard = (LinearLayout) keyboardView.findViewById(R.id.keyboard_background);

            try {
                if ("X990 PINPad".equals(iDeviceInfo.getModel())) {
                    btn_cancel.setVisibility(View.GONE);
                    layout_keyboard.setVisibility(View.GONE);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onclick...");
        }

        public List<PinKeyCoorInfo> getKeyCoorInfos() {
            List<PinKeyCoorInfo> keyCoorInfos = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                keyCoorInfos.add(getKeyCoorInfo("btn_" + i, btn[i], KeyCoorInfo.TYPE_NUM));
            }
            keyCoorInfos.add(getKeyCoorInfo("btn_10", btn_conf, KeyCoorInfo.TYPE_CONF));
            keyCoorInfos.add(getKeyCoorInfo("btn_11", btn_cancel, KeyCoorInfo.TYPE_CANCEL));
            keyCoorInfos.add(getKeyCoorInfo("btn_12", btn_del, KeyCoorInfo.TYPE_DEL));
//            keyCoorInfos.add(getKeyCoorInfo("btn_12", btn_del, KeyCoorInfo.TYPE_DEL_ALL));
            return keyCoorInfos;
        }

        private PinKeyCoorInfo getKeyCoorInfo(String keyId, Button btn, int type) {
            keysMap.put(keyId, btn); // get the coordinate information and save to may
            //按钮保存到Map中，keyid与返回的map中keyid对应从而找到那个按钮，修改该button的显示内容
            int[] location = new int[2];
            btn.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            return new PinKeyCoorInfo(keyId, x, y, x + btn.getWidth(), y + btn.getHeight(), type);
        }
    }

//    private void printMsgTool(String msg){
//        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
//        try{
//            Thread.sleep(1000);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isDestroyed = true;
    }

    public boolean isActivityDestroyed(){
        return isDestroyed;
    }
*/
}