package view;

import static android.content.ContentValues.TAG;

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
import com.dreamtek.smartpos.deviceservice.aidl.IEMV;
import com.dreamtek.smartpos.deviceservice.aidl.IPinpad;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;
import com.dreamtek.smartpos.deviceservice.aidl.sde.ISde;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import base.MyApplication;
import entity.cases.KeyBoardDatas;
import moudles.ServiceMoudle;
import Utils.LogUtils;
import moudles.newModules.SdeModule;
import moudles.newModules.data.PanBundleStore;


import com.dreamtek.smartpos.deviceservice.aidl.sde.SecurityPinInputListener;


public class PinInputActivity extends Activity {

/*
    private View v;
    private PinpadPopUpWindow mPinpadPopUpWindow;
    ServiceMoudle serviceMoudle;
    IPinpad iPinpad;
    ISde iSde;
    IEMV iemv;
    Context context;
//    static String pinBlock;
//    static String ksn;
//    static boolean isByPass;
//    static boolean isEncrypt;

    LogUtils logUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG", "oncreate...");
        logUtils = MyApplication.serviceMoudle.logUtils;


        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin_input);
        v = LayoutInflater.from(this).inflate(R.layout.activity_pin_input, null);

        serviceMoudle = MyApplication.serviceMoudle;
        IDeviceService deviceService = serviceMoudle.deviceService;
        try {
            iPinpad = deviceService.getPinpad(0);
            iSde = deviceService.getSde();
            iemv = deviceService.getEMV();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mPinpadPopUpWindow = new PinpadPopUpWindow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "onResume...");
    }

    @Override
    public void onAttachedToWindow() {
        Log.i("TAG", "onAttachedToWindow");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPinpadPopUpWindow.showPopupWindow(v);
            }
        });
    }

    public void MImportPin(int option, byte[] pin) {
        try {
            long startTime = System.currentTimeMillis();
            iemv.importPin(option, pin);
            long endTime = System.currentTimeMillis();
            logUtils.addCaseLog("importPin executeTime" + (endTime - startTime));
        } catch (RemoteException e) {
            logUtils.addCaseLog("importPin execute exception");
            e.printStackTrace();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i("TAG", "onWindowFocusChanged forcuse:" + hasFocus);
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) return;

        Intent intent = getIntent();
        final Bundle bundleRecv = intent.getBundleExtra("initBundle");

        //getImportData
        final Bundle importData = bundleRecv.getBundle("importData");
        if (importData != null) {
            Log.d(TAG, "importPIN = " + importData.getString("importPIN"));
        }

        //initPinInputView
        final int keyId = bundleRecv.getInt("keyId");
        Boolean isOnline = bundleRecv.getBoolean("isOnline");
        byte[] displayKeyValue = bundleRecv.getByteArray("displayKeyValue");
        byte[] pinLimit = bundleRecv.getByteArray("pinLimit");
        int timeout = bundleRecv.getInt("timeout");

        final Bundle param = new Bundle();
        param.putBoolean("isOnline", isOnline);
        param.putByteArray("displayKeyValue", displayKeyValue);
        param.putByteArray("pinLimit", pinLimit);
        param.putInt("timeout", timeout);

        //startPinpad
        final int keyId2 = bundleRecv.getInt("keyId2");
        final int pinBlockType = bundleRecv.getInt("pinBlockType");
        final int keySystem = bundleRecv.getInt("keySystem");
        final int mode = bundleRecv.getInt("mode");
        final Bundle extraParams = new Bundle();
        extraParams.putByte("dispersionType", bundleRecv.getByte("dispersionType"));
        Log.d(TAG, "dispersionType = " + bundleRecv.getByte("dispersionType"));
        extraParams.putBoolean("notifyPinLenError", bundleRecv.getBoolean("notifyPinLenError"));
        extraParams.putByteArray("random", bundleRecv.getByteArray("random"));


        final SecurityPinInputListener securityPinInputListener = new SecurityPinInputListener.Stub() {
            @Override
            public void onInput(int len, int key) throws RemoteException {
                // the key is * always.
                Log.d("TAG", "onInput, length:" + len + ",key:" + key);
                char buf[] = new char[len];
                Arrays.fill(buf, '*');
                final String s = String.valueOf(buf);
                Log.d("TAG", s);
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
            public void onConfirm(Bundle pinInfos) throws RemoteException {
                Log.d("TAG", "onConfirm");
                Log.d("TAG", "pinInfos pinBlock = " + StringUtil.byte2HexStr(pinInfos.getByteArray("pinblock")));
                Log.d("TAG", "pinInfos ksn = " + StringUtil.byte2HexStr(pinInfos.getByteArray("ksn")));
                Log.d("TAG", "pinInfos isByPass = " + pinInfos.getBoolean("isByPass"));
                Log.d("TAG", "pinInfos isEncrypt = " + pinInfos.getBoolean("isEncrypt"));


                byte[] pinBlock = pinInfos.getByteArray("pinblock");
                byte[] ksn = pinInfos.getByteArray("ksn");
                KeyBoardDatas.getInstance().setPinBlock(pinBlock);

                String pinBlock1 = StringUtil.byte2HexStr(pinBlock);
                String ksn1 = StringUtil.byte2HexStr(ksn);
                boolean isByPass = pinInfos.getBoolean("isByPass");
                boolean isEncrypt = pinInfos.getBoolean("isEncrypt");

                Message message = new Message();
                message.getData().putString("message", "pinBlock = " + StringUtil.byte2HexStr(pinBlock) + "|ksn = " + ksn1 + "|isByPass = " + isByPass + "|isEncrypt = " + isEncrypt);
                handler.sendMessage(message);

//                String importPin = null;
//                if (importData != null) {
//                    importPin = importData.getString("importPIN");
//                }
//                if (importPin == null) {
//                    Log.d(TAG,"importPin == null");
////                    MImportPin(1, null);
//                }
//                else {
//                    Log.d(TAG,"importPin!=null importPin = "+ Arrays.toString(importPin.getBytes()));
////                    MImportPin(1, importPin.getBytes());
//
//                }

                MImportPin(1, pinBlock);
                Log.d(TAG, "传入pinBlock=" + StringUtil.byte2HexStr(pinBlock));

                printMsgTool("pinBlock = " + StringUtil.byte2HexStr(pinBlock));

                exitKeyBoardOnUI();


            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d("TAG", "onCannel");
                MImportPin(0, null);
                exitKeyBoardOnUI();
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                Log.d("TAG", "onError");
                String errorMsg = convertErrCodeToMsg(errorCode);
                printMsgTool("onError() executed,ERROR = " + errorMsg);
                MImportPin(0, null);
                Message message = new Message();
                message.getData().putString("message", "ERROR: " + errorMsg);
                handler.sendMessage(message);
                exitKeyBoardOnUI();
            }
        };


        View contentView = mPinpadPopUpWindow.getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                List<PinKeyCoorInfo> pinKeys = mPinpadPopUpWindow.getKeyCoorInfos();
                try {
                    Map<String, String> keyMap = iSde.initPinInputView(keyId, param, pinKeys);
                    Log.d("TAG", "size:" + keyMap.size());
                    Set<Map.Entry<String, String>> entrys = keyMap.entrySet();
                    for (Map.Entry<String, String> entry : entrys) {
                        Log.d("TAG", entry.getKey() + "--" + entry.getValue());
                        Button btn = null;
                        int index = entry.getKey().charAt(4) - '0';
                        if (index >= 0 && index < 10) {
                            mPinpadPopUpWindow.btn[index].setText(entry.getValue() + "\n[" + index + "]");
                        }
                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("isPanInput", true);
//                    iSde.startSecurityKeyBoardEntry(bundle);

//                    Bundle extraParams = new Bundle();
//                    extraParams.putInt("dispersionType",2);
//                    extraParams.putBoolean("notifyPinLenError",false);

                    iSde.startPinpad(keyId2, pinBlockType, keySystem, mode, extraParams, securityPinInputListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    *//**
     * @param errorCode the error code<BR>
     *                  errorCode:<BR>
     *                  -1:input execption <BR>
     *                  -2:input time out <BR>
     *                  -3:plain text is null <BR>
     *                  -4:encrypt error <BR>
     *                  -5:cipher text is null <BR>
     *                  -6:params invalid <BR>
     *                  -7:PAN is null <BR>
     *                  0xff:other error <BR>
     *//*
    private String convertErrCodeToMsg(int errorCode) {
        switch (errorCode) {
            case -1:
                return "input exception";
            case -2:
                return "input time out";
            case -3:
                return "plain text is null";
            case -4:
                return "encrypt error";
            case -5:
                return "cipher text is null";
            case -6:
                return "params invalid";
            case -7:
                return "PAN is null ";

        }
        return "other error ";
    }

    public void exitKeyBoardOnUI() {
        Log.i("TAG", "exitKeyBoardOnUI");

        synchronized (SdeModule.keyLock) {
            if (SdeModule.activityCount > 0) {
                SdeModule.activityCount--;
            }
            Log.i(TAG, "PIninput activityConunt=" + SdeModule.activityCount);
            if (SdeModule.activityCount == 0) {
                SdeModule.keyLock.notify();
                Log.i(TAG, "解锁pinInput");
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                mPinpadPopUpWindow.dismiss();
                finish();
            }
        });


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
            textView_nouse2 = getContentView().findViewById(R.id.textView_nouse2);
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
            if (null != PanBundleStore.getBundle()) {
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
                Log.d("TAG", "showPopupWindow");
                updateTextViewContent();  // 显示弹窗时更新 textView_nouse2 的内容
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
            Log.d("TAG", "onclick...");
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

    private void printMsgTool(String msg) {
        ((MyApplication) context).serviceMoudle.getPintBtMoudle().printMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logUtils.addCaseLog(msg.getData().getString("message"));
            logUtils.showCaseLog();
        }
    }*/;
}
