package view.custompinpad;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.dreamtek.smartpos.deviceservice.aidl.PinInputListener;
import com.dreamtek.smartpos.deviceservice.aidl.PinKeyCoorInfo;
import com.verifone.activity.R;
import com.verifone.smartpos.utils.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Utils.LogUtil;
import base.MyApplication;
import butterknife.BindView;
import butterknife.ButterKnife;
import testtools.ToastUtil;

public class InputPinActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.tv_tool_title)
    TextView tvTitle;
    @BindView(R.id.keyboardview_inputpin)
    PinKeyBoardView keyboardview_inputpin;
    @BindView(R.id.recyclerview_passwd_list)
    RecyclerView recyclerview_passwd_list;
    @BindView(R.id.ll_passwd_list)
    View llPasswdList;

    private PasswdListAdapter passwdListAdapter;
    private boolean onPreDrawFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        onPreDrawFirst = false;
        setContentView(R.layout.activity_input_pin);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        keyboardview_inputpin.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                LogUtil.i(TAG, "onPreDraw been called.");
                if (!onPreDrawFirst) {
                    onPreDrawFirst = true;
                    try {
                        LogUtil.i(TAG, "getRandomKeyBoradNumber.");
                        int[] numPositionList = initkeyboardAndPinpad(keyboardview_inputpin.getCoordinateList());
                        keyboardview_inputpin.setRandomNumber(numPositionList);
                        MyApplication.getServiceMoudle().getiPinpad().startPinInputCustomView();
                    } catch (Exception e) {
                        LogUtil.e(TAG, "FindView() exception happened.");
                        e.printStackTrace();
                    }
                }
                return onPreDrawFirst;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        passwdListAdapter = new PasswdListAdapter(12, llPasswdList);
        recyclerview_passwd_list.setLayoutManager(linearLayoutManager);
        recyclerview_passwd_list.setAdapter(passwdListAdapter);
//        hideTransBackBtn();
    }

    public int[] initkeyboardAndPinpad(List<PinKeyCoordinate> pinKeyCoordinates) {
        PinPadInitPinInputCustomViewParamIn paramIn = InputPinParameterCache.getPinPadInitPinInputCustomViewParamIn();

        Bundle bundle = new Bundle();
        bundle.putByteArray("pinLimit", paramIn.getPinLimit());
        bundle.putInt("timeout", paramIn.getTimeout());
        bundle.putBoolean("isOnline", paramIn.getOnLineState());
        bundle.putString("promptString", paramIn.getPromptString());
        bundle.putString("pan", paramIn.getPan());
        bundle.putInt("keysType", paramIn.getKeyType());
        bundle.putInt("desType", paramIn.getDesType());
        if (paramIn.getRandom() != null) {
            bundle.putByteArray("random", paramIn.getRandom());
        }
        if (paramIn.getKeyboardNumberPosition() != null) {
            LogUtil.d(TAG, "displayKeyValue=" + new String(paramIn.getKeyboardNumberPosition()));
            bundle.putByteArray("displayKeyValue", paramIn.getKeyboardNumberPosition());
        }

        PinPadInitPinInputCustomViewParamIn.PinpadListener pinpadListener = paramIn.getPinpadListener();
        List<PinKeyCoorInfo> pinKeyCoorInfos = new ArrayList<>();
        if (pinKeyCoordinates != null) {
            Iterator<PinKeyCoordinate> iterator = pinKeyCoordinates.iterator();
            while (iterator.hasNext()) {
                PinKeyCoordinate pinKeyCoordinate = iterator.next();
                String keyName = pinKeyCoordinate.getKeyName();
                int x1 = pinKeyCoordinate.getLeftTopX();
                int y1 = pinKeyCoordinate.getLeftTopY();
                int x2 = pinKeyCoordinate.getRightBottomX();
                int y2 = pinKeyCoordinate.getRightBottomY();
                int keyType = pinKeyCoordinate.getKeyType();

                LogUtil.d("TAG", "===keyName=" + keyName + " x1=" + x1 + " y1=" + y1 + " x2=" + x2 + " y2=" + y2 + " keyType=" + keyType);
                pinKeyCoorInfos.add(new PinKeyCoorInfo(keyName, x1, y1, x2, y2, keyType));
            }
        }

        try {
            Map<String, String> map = MyApplication.getServiceMoudle().getiPinpad().initPinInputCustomView(paramIn.getKeyId(), bundle, pinKeyCoorInfos, new PinInputListener.Stub() {
                @Override
                public void onInput(int len, int key) throws RemoteException {
                    LogUtil.i(TAG, "onInput len=" + len + " key=" + key);
                    if (pinpadListener != null) {
                        pinpadListener.onInput(len, key);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPassword(len);
                        }
                    });
                    if (paramIn.getPinpadListener() != null) {
                        paramIn.getPinpadListener().onInput(len, key);
                    }
                }

                @Override
                public void onConfirm(Bundle pinInfos) throws RemoteException {
                    byte[] encryptedPinBlock = pinInfos.getByteArray("pinblock");
                    boolean isByPass = pinInfos.getBoolean("isByPass");
                    MyApplication.getServiceMoudle().getiPinpad().endPinInputCustomView();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToastLong("isByPass=" + isByPass + " pinblock=[" + StringUtil.byte2HexStr(encryptedPinBlock) + "]");
                        }
                    });
                    finish();
                    if (paramIn.getPinpadListener() != null) {
                        paramIn.getPinpadListener().onConfirm(encryptedPinBlock, isByPass);
                    }
                }

                @Override
                public void onCancel() throws RemoteException {
                    LogUtil.i(TAG, "onCancel");
                    MyApplication.getServiceMoudle().getiPinpad().endPinInputCustomView();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToastLong("cancelled");
                        }
                    });
                    if (paramIn.getPinpadListener() != null) {
                        paramIn.getPinpadListener().onCancel();
                    }
                    finish();
                }

                @Override
                public void onError(int errorCode) throws RemoteException {
                    MyApplication.getServiceMoudle().getiPinpad().endPinInputCustomView();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToastLong("errorCode=[" + errorCode + "]");
                        }
                    });
                    if (paramIn.getPinpadListener() != null) {
                        paramIn.getPinpadListener().onError(errorCode, "");
                    }
                    finish();
                }
            });

            LogUtil.d("btn_0=" + map.get("btn_0"));
            LogUtil.d("btn_1=" + map.get("btn_1"));
            LogUtil.d("btn_2=" + map.get("btn_2"));
            LogUtil.d("btn_3=" + map.get("btn_3"));
            LogUtil.d("btn_4=" + map.get("btn_4"));
            LogUtil.d("btn_5=" + map.get("btn_5"));
            LogUtil.d("btn_6=" + map.get("btn_6"));
            LogUtil.d("btn_7=" + map.get("btn_7"));
            LogUtil.d("btn_8=" + map.get("btn_8"));
            LogUtil.d("btn_9=" + map.get("btn_9"));
            LogUtil.d("btn_10=" + map.get("btn_10"));
            LogUtil.d("btn_11=" + map.get("btn_11"));
            LogUtil.d("btn_12=" + map.get("btn_12"));
            int btn_0 = Integer.parseInt(map.get("btn_0"));
            int btn_1 = Integer.parseInt(map.get("btn_1"));
            int btn_2 = Integer.parseInt(map.get("btn_2"));
            int btn_3 = Integer.parseInt(map.get("btn_3"));
            int btn_4 = Integer.parseInt(map.get("btn_4"));
            int btn_5 = Integer.parseInt(map.get("btn_5"));
            int btn_6 = Integer.parseInt(map.get("btn_6"));
            int btn_7 = Integer.parseInt(map.get("btn_7"));
            int btn_8 = Integer.parseInt(map.get("btn_8"));
            int btn_9 = Integer.parseInt(map.get("btn_9"));

            int[] randomNumbers = new int[]{btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9};
            return randomNumbers;
        } catch (Exception e) {
            // Exception happened. example: request online pin but online pin key not found.
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        }
    }

    @Override
    protected void onStart() {
//        inputPinPresenter.setPowerKeyStatus(false);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        inputPinPresenter.setPowerKeyStatus(true);
    }

    public void showPassword(int len) {
        passwdListAdapter.showPasswd(len);
    }

    @Override
    public void onBackPressed() {
    }
}
