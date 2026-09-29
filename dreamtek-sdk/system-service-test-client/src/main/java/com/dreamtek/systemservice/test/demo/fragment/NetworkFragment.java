package com.dreamtek.systemservice.test.demo.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dreamtek.smartpos.system_service.aidl.IAddNetworkAllowedListObserver;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import com.dreamtek.systemservice.test.demo.R;
import com.dreamtek.systemservice.test.demo.utils.SystemServiceAccess;


public class NetworkFragment extends Fragment {

    private static INetworkManager iNetworkManager = null;
    private static final String TAG = NetworkFragment.class.getSimpleName();
    private boolean airModeEnabled = true;
    private boolean mobileDataEnabled = true;

    private static ISystemManager systemManager = null;
    private static ISettingsManager settingsManager = null;
    private final SystemServiceAccess.Listener mSystemServiceListener = new SystemServiceAccess.Listener() {
        @Override
        public void onSystemReady(ISystemManager system, ISysDeviceInfo deviceInfo,
                                  INetworkManager network, ISettingsManager settings) {
            Log.d(TAG, "system service ready");
            systemManager = system;
            settingsManager = settings;
            iNetworkManager = network;
        }

        @Override
        public void onSystemLost() {
            Log.d(TAG, "system service disconnected.");
            systemManager = null;
            // 通知用户服务已断开
            Toast.makeText(getContext(), "System service disconnected", Toast.LENGTH_SHORT).show();
        }
    };
    private EditText et_setNetwork;


    Button setApn, deleteApn, get_MACAddress, get_NetDetails, setEthernetStaticIp, setWifiStaticIp, getWifiProxyState,
            setProxy, getSelectedApnInfo, connectWifi, addNetwork, getMobilePreferredNetworkType, setMobilePreferredNetworkType,
            setNetworkType, getNetworkType, enableAirplayMode, enableMobileData, getMultiNetworkPrefer, setMultiNetworkPrefer,
            selectMobileDataOnSlot, addNetworkAllowedList, jumptoAPN, setWifiProxyState, setMultiNetwork, isMultiNetwork,enableWifi;
    Switch sw_network,sw_wifi;
    EditText enterSlot, enterNumber, enterNetworkType, et_getmacaddress, et_setEthernetStaticIp, et_setAPN, et_setProxy, et_addNetworkAllowedList, et_addNetwork, et_addNetwork2;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        bindSystemService();
        initParam(view);

        view.findViewById(R.id.isMultiNetwork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(), "" + iNetworkManager.isMultiNetwork(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.setMultiNetwork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iNetworkManager.setMultiNetwork(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        view.findViewById(R.id.setRoamingEnable_slot1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = iNetworkManager.setDataRoamingEnabled(true, 1);
                    Toast.makeText(NetworkFragment.this.getContext(), "setRoamingEnable_slot1 result:" + i, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Log.d(TAG, "setRoamingEnable_slot1 error: " + e);
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.setRoamingDisable_slot1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = iNetworkManager.setDataRoamingEnabled(false, 1);
                    Toast.makeText(NetworkFragment.this.getContext(), "setRoamingDisable_slot1 result:" + i, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Log.d(TAG, "setRoamingDisable_slot1 error: " + e);
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.setRoamingEnable_slot2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = iNetworkManager.setDataRoamingEnabled(true, 2);
                    Toast.makeText(NetworkFragment.this.getContext(), "setRoamingEnable_slot2 result:" + i, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Log.d(TAG, "setRoamingEnable_slot2 error: " + e);
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.setRoamingDisable_slot2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = iNetworkManager.setDataRoamingEnabled(false, 2);
                    Toast.makeText(NetworkFragment.this.getContext(), "setRoamingDisable_slot2 result:" + i, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Log.d(TAG, "setRoamingDisable_slot2 error: " + e);
                    e.printStackTrace();
                }
            }
        });

        initView();
        return view;
    }

    private void initParam(View view) {
        setApn = view.findViewById(R.id.setApn);
        deleteApn = view.findViewById(R.id.deleteApn);
        get_MACAddress = view.findViewById(R.id.get_MACAddress);
        get_NetDetails = view.findViewById(R.id.get_NetDetails);
        setEthernetStaticIp = view.findViewById(R.id.setEthernetStaticIp);
        setWifiStaticIp = view.findViewById(R.id.setWifiStaticIp);
        getWifiProxyState = view.findViewById(R.id.getWifiProxyState);
        setNetworkType = view.findViewById(R.id.setNetworkType);
        getNetworkType = view.findViewById(R.id.getNetworkType);
        et_setNetwork = view.findViewById(R.id.et_setNetwork);
        enableAirplayMode = view.findViewById(R.id.enableAirplayMode);
        enableMobileData = view.findViewById(R.id.enableMobileData);
        connectWifi = view.findViewById(R.id.connectWifi);
        addNetwork = view.findViewById(R.id.addNetwork);
        getMobilePreferredNetworkType = view.findViewById(R.id.getMobilePreferredNetworkType);
        setMobilePreferredNetworkType = view.findViewById(R.id.setMobilePreferredNetworkType);
        getMultiNetworkPrefer = view.findViewById(R.id.getMultiNetworkPrefer);
        setMultiNetworkPrefer = view.findViewById(R.id.setMultiNetworkPrefer);
        selectMobileDataOnSlot = view.findViewById(R.id.selectMobileDataOnSlot);
        addNetworkAllowedList = view.findViewById(R.id.addNetworkAllowedList);
        enterSlot = view.findViewById(R.id.enterSlot);
        enterNumber = view.findViewById(R.id.enterNumber);
        enterNetworkType = view.findViewById(R.id.enterNetworkType);
        setProxy = view.findViewById(R.id.setProxy);
        et_getmacaddress = view.findViewById(R.id.et_getmacaddress);
        et_setEthernetStaticIp = view.findViewById(R.id.et_setEthernetStaticIp);
        et_setAPN = view.findViewById(R.id.et_setAPN);
        et_setProxy = view.findViewById(R.id.et_setProxy);
        jumptoAPN = view.findViewById(R.id.jumptoAPN);
        et_addNetworkAllowedList = view.findViewById(R.id.et_addNetworkAllowedList);
        getSelectedApnInfo = view.findViewById(R.id.getSelectedApnInfo);
        setWifiProxyState = view.findViewById(R.id.setWifiProxyState);
        enableWifi = view.findViewById(R.id.enablewifi);
        setMultiNetwork = view.findViewById(R.id.setMultiNetwork);
        isMultiNetwork = view.findViewById(R.id.isMultiNetwork);
        sw_network = view.findViewById(R.id.sw_network);
        sw_wifi = view.findViewById(R.id.sw_wifi);
        et_addNetwork2 = view.findViewById(R.id.et_addNetwork2);
        et_addNetwork = view.findViewById(R.id.et_addNetwork);
    }

    public void initView() {

        setApn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setApn(view);
            }
        });
        deleteApn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteApn(view);
            }
        });
        get_MACAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_MACAddress(view);
            }
        });
        get_NetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_NetDetails(view);
            }
        });
        setEthernetStaticIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEthernetStaticIp(view);
            }
        });
        setWifiStaticIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWifiStaticIp(view);
            }
        });
        getWifiProxyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NetworkFragment.this.getContext(), "wifiProxyState is:" + getWifiProxyState(), Toast.LENGTH_SHORT).show();

            }
        });

        setNetworkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNetworkType(view);
            }
        });
        getNetworkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNetworkType(view);
            }
        });

        connectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWifi(view);
            }
        });
        addNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNetwork(view);
            }
        });
        enableAirplayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableAirplayMode(view);
            }
        });
        enableMobileData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableMobileData(view);
            }
        });

        getMobilePreferredNetworkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMobilePreferredNetworkType(view);
            }
        });
        setMobilePreferredNetworkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMobilePreferredNetworkType(view);
            }
        });
        getMultiNetworkPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMultiNetworkPrefer(view);
            }
        });
        setMultiNetworkPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMultiNetworkPrefer(view);
            }
        });
//        setMultiNetwork.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setMultiNetwork(view);
//            }
//        });
        selectMobileDataOnSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMobileDataOnSlot(view);
            }
        });
        addNetworkAllowedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNetworkAllowedList(view);
            }
        });
        setProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String proxy = !TextUtils.isEmpty(et_setProxy.getText().toString()) ? et_setProxy.getText().toString() : null;
                    int result = iNetworkManager.setProxy(proxy, null);
                    Toast.makeText(NetworkFragment.this.getContext(), "result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        jumptoAPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$ApnSettingsActivity"));
                startActivity(intent);
            }
        });
        getSelectedApnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iNetworkManager != null) {
                    try {
                        Bundle bundle = iNetworkManager.getSelectedApnInfo();
                        Toast.makeText(getContext(), "bundle = " + bundle, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "getSelectedApnInfo = " + bundle);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        enableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_wifi.isChecked();
                try {
                    iNetworkManager.enableWifi(isChecked);
                    Toast.makeText(getContext(), "enableWifi isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        setWifiProxyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_network.isChecked();
                try {
                    iNetworkManager.setWifiProxyState(isChecked);
                    Toast.makeText(getContext(), "isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getWifiProxyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean state = iNetworkManager.getWifiProxyState();
                    Toast.makeText(getContext(), "state = " + state, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        enableAirplayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_network.isChecked();
                try {
                    iNetworkManager.enableAirplayMode(isChecked);
                    Toast.makeText(getContext(), "isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        enableMobileData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_network.isChecked();
                try {
                    iNetworkManager.enableMobileData(isChecked);
                    Toast.makeText(getContext(), "isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        setMultiNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = sw_network.isChecked();
                try {
                    iNetworkManager.setMultiNetwork(isChecked);
                    Toast.makeText(getContext(), "isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        isMultiNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean state = iNetworkManager.isMultiNetwork();
                    Toast.makeText(getContext(), "result = " + state, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        SystemServiceAccess.unbind(mSystemServiceListener);
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    private void bindSystemService() {
        // 通过宿主注入的 Provider 复用统一设备服务连接（新库 IDeviceService.getSystemManager()），
        // 不再直接绑定 com.dreamtek.smartpos.system_service。
        SystemServiceAccess.bind(getContext(), mSystemServiceListener);
    }

    public void setApn(View view) {
//        String test_apn_list[][] = {
////                {"Name46000", "APN46000", "", "46000" },
////                {"Name46001", "APN46001", "", "46001" },
////                {"Name46002", "APN46002", "", "46002" },
//                {"NameSlot1", "APNSlot1", "1", ""},
////                {"NameSlot2", "APNSlot2", "2", "" },
////                {"Name46006", "APN46006", "", "46006" },
////                {"NameNoSlot", "APNNoSlot", "", "" },
////                {"Name46007", "APN46007", "", "46007" },
//        };
//        for (String apn[] : test_apn_list) {

//            Bundle infos = new Bundle();
//            infos.putString("name", apn[0]);
//            infos.putString("apn", apn[1]);
//
//            // "SLOT"   // Add by Simon on version 1.6.0.2
//            // SLOT: 1 or 2 for SIM card in slot 1 or 2.
//            // using the slot 1 as default if there is no "fixed_numeric" setting
//            infos.putString("SLOT", apn[2]);
//            infos.putString("proxy", "192.168.8.112");
//            infos.putString("port", "808");
//            infos.putBoolean("selected",true);
//
//            // "fixed_numeric"  // add by Simon on version 1.6.0.2
//            // fixed the numeric to fixed_numeric for specific SIM card
//            // using the "SLOT" if there is no "fixed_numeric" setting
//            infos.putString("fixed_numeric", apn[3]);
        String configStr = !TextUtils.isEmpty(et_setAPN.getText().toString()) ? et_setAPN.getText().toString() : "";
        configStr.trim();
        Bundle infos = setAPN_parseConfigString(configStr);
        Log.d(TAG, "infos = " + infos);
        int ret = -1;
        try {
            ret = iNetworkManager.setAPN(infos);
            Toast.makeText(getContext(), "ret = " + ret + ";infos = " + infos, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "set APN:" + " apn[0]=" + apn[0] + "," + " apn[1]=" + apn[1] + "," + " apn[2]=" + apn[2] + "," + " apn[3]=" + apn[3] + ", return: " + ret);
    }


    public void deleteApn(View view) {
        try {
            String configStr = !TextUtils.isEmpty(et_setAPN.getText().toString()) ? et_setAPN.getText().toString() : null;
            Bundle infos = setAPN_parseConfigString(configStr);
            String apn = infos.getString("apn");
            Log.d(TAG, "apn = " + apn);
            int i = iNetworkManager.deleteAPN(apn);
            Log.d(TAG, "i = " + i);
            Toast.makeText(getContext(), "apn = " + apn + ";i = " + i, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void get_MACAddress(View view) {
        String MACAddress = "N/A";
        int type = Integer.parseInt(!TextUtils.isEmpty(et_getmacaddress.getText().toString()) ? et_getmacaddress.getText().toString() : "-1");
        try {
            MACAddress = iNetworkManager.getMacAddress(type);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Toast.makeText(NetworkFragment.this.getContext(), "type=" + type + ";MACAddress=" + MACAddress, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "MACAddress = " + MACAddress);
    }

    public void get_NetDetails(View view) {
        Bundle bundle = null;
        try {
            bundle = iNetworkManager.getCurrentNetworkDetails();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (bundle == null) {
            Toast.makeText(NetworkFragment.this.getContext(), "不支持", Toast.LENGTH_SHORT).show();
            return;
        }

        assert bundle != null;
        String dns = bundle.getString("DNS");
        Log.d(TAG, "IP:" + bundle.getString("IP")
                + "\nMAC:" + bundle.getString("MAC")
                + "\nDNS:" + dns);
        Toast.makeText(NetworkFragment.this.getContext(), "IP:" + bundle.getString("IP")
                        + "\nMAC:" + bundle.getString("MAC") + "\n" + "DNS:" + dns,
                Toast.LENGTH_LONG).show();
    }

    public void setEthernetStaticIp(View view) {
        try {
            String configBundle = !TextUtils.isEmpty(et_setEthernetStaticIp.getText().toString()) ? et_setEthernetStaticIp.getText().toString() : null;
            Bundle infos = setAPN_parseConfigString(configBundle);
            Log.d(TAG, "infos = " + infos);

            iNetworkManager.setEthernetStaticIp(infos);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static Bundle setAPN_parseConfigString(String configString) {
        Bundle infos = new Bundle();
        if (configString != null && !configString.isEmpty()) {
            String[] configs = configString.split("/");

            for (String config : configs) {
                String[] keyValue = config.split("=");
                if (keyValue.length == 2) {
                    if (keyValue[0].equals("selected")) {
                        if (keyValue[1].equals("true")) {
                            infos.putBoolean(keyValue[0], true);
                        } else if (keyValue[1].equals("false")) {
                            infos.putBoolean(keyValue[0], false);
                        }
                    } else {
                        infos.putString(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return infos;
    }

    public static Bundle addNetwork_parseConfigString(String configString) {
        Bundle infos = new Bundle();
        if (configString != null && !configString.isEmpty()) {
            String[] configs = configString.split("/");

            for (String config : configs) {
                String[] keyValue = config.split("=");
                if (keyValue.length == 2) {
                    if (keyValue[0].equals("type")) {
                        infos.putInt(keyValue[0], Integer.parseInt(keyValue[1]));
                    } else {
                        infos.putString(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return infos;
    }

    public void setWifiStaticIp(View view) {
        try {
            String configBundle = !TextUtils.isEmpty(et_setEthernetStaticIp.getText().toString()) ? et_setEthernetStaticIp.getText().toString() : null;
            Bundle infos = setAPN_parseConfigString(configBundle);
            Log.d(TAG, "infos = " + infos);

            iNetworkManager.setWifiStaticIp(infos);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean getWifiProxyState() {
        Log.d(TAG, "getWifiProxyState executed()");
        boolean wifiProxyState = false;
        try {
            if (iNetworkManager != null) {
                wifiProxyState = iNetworkManager.getWifiProxyState();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "wifiProxyState is " + wifiProxyState);
        return wifiProxyState;
    }

    public void setNetworkType(View view) {
        if (iNetworkManager != null) {
            try {
                iNetworkManager.setNetworkType(Integer.parseInt(!TextUtils.isEmpty(et_setNetwork.getText().toString()) ? et_setNetwork.getText().toString() : "7"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void getNetworkType(View view) {
        if (iNetworkManager != null) {
            try {
                int type = iNetworkManager.getNetworkType();
                Toast.makeText(this.getContext(), "get network type = " + type, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "getNetworkType = " + type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectWifi(View view) {//连接不上其他WiFi没法测试
        if (iNetworkManager != null) {
            String SSID = !TextUtils.isEmpty(et_addNetwork2.getText().toString()) ? et_addNetwork2.getText().toString() : "";
            Log.d(TAG, "your input value = " + SSID);
            try {
                boolean result = iNetworkManager.connectWifi(SSID);
                Toast.makeText(NetworkFragment.this.getContext(), "SSID = " + SSID + ";result = " + result, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.d(TAG, "ConnectWifi没有执行");
            }
        }
    }

    public void addNetwork(View view) {
        if (iNetworkManager != null) {
            String value = !TextUtils.isEmpty(et_addNetwork.getText().toString()) ? et_addNetwork.getText().toString() : "";
            if (value.isEmpty()) {
                try {
                    int result = iNetworkManager.addNetwork(new Bundle());
                    Log.d(TAG, "addNetwork result = " + result);
                    Toast.makeText(getContext(), "param is null, result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Bundle bundle = addNetwork_parseConfigString(value);
                try {
                    Log.d(TAG, "bundle=" + bundle);
                    int result = iNetworkManager.addNetwork(bundle);
                    Log.d(TAG, "addNetwork result = " + result);
                    Toast.makeText(NetworkFragment.this.getContext(), "bundle = " + bundle + " result = " + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public void getMobilePreferredNetworkType(View view) {
        if (iNetworkManager != null) {
            String type;
            try {
                type = iNetworkManager.getMobilePreferredNetworkType();
                Log.d(TAG, "type的值为" + type);
                Toast.makeText(getContext(), "tupe的值为" + type, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                Log.d(TAG, "getMobilePreferredNetworkType没有执行");
                e.printStackTrace();
            }
        }
    }

    public void setMobilePreferredNetworkType(View view) {
        //输入2 3 4，分别代表2G 3G 4G
        //输入 32代表3G/2G auto
        if (iNetworkManager != null && enterNetworkType.getText().toString() != null) {
            String type = enterNetworkType.getText().toString();
            if (type.equals("32")) {
                try {
                    iNetworkManager.setMobilePreferredNetworkType("3G/2G");
                    Toast.makeText(getContext(), "return " + iNetworkManager.getMobilePreferredNetworkType(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Log.d(TAG, "setMobilePreferredNetworkType开始执行");
                    iNetworkManager.setMobilePreferredNetworkType(type + "G");
                    Log.d(TAG, "getMobilePrefferd 为" + iNetworkManager.getMobilePreferredNetworkType());
                    Toast.makeText(getContext(), "return " + iNetworkManager.getMobilePreferredNetworkType(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void getMultiNetworkPrefer(View view) {
        if (iNetworkManager != null) {
            try {
                Log.d(TAG, "getMultiNetworkPrefer开始执行");
                String str = iNetworkManager.getMultiNetworkPrefer();
                Log.d(TAG, "str的值为" + str);
                Toast.makeText(NetworkFragment.this.getContext(), "str = " + str, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static final String TRANSPORT_WIFI_ETHERNET_CELLULAR = "wifi,ethernet,cellular";//1
    public static final String TRANSPORT_WIFI_CELLULAR_ETHERNET = "wifi,cellular,ethernet";//2
    public static final String TRANSPORT_CELLULAR_WIFI_ETHERNET = "cellular,wifi,ethernet";//3
    public static final String TRANSPORT_CELLULAR_ETHERNET_WIFI = "cellular,ethernet,wifi";//4
    public static final String TRANSPORT_ETHERNET_CELLULAR_WIFI = "ethernet,cellular,wifi";//5
    public static final String TRANSPORT_ETHERNET_WIFI_CELLULAR = "ethernet,wifi,cellular";//6

    public void setMultiNetworkPrefer(View view) {//6个数据返回null
        if (iNetworkManager != null && enterNumber.getText().toString() != null) {
            String prefer;
            int num = Integer.parseInt(enterNumber.getText().toString());
            switch (num) {
                case 1:
                    prefer = TRANSPORT_WIFI_ETHERNET_CELLULAR;
                    break;
                case 2:
                    prefer = TRANSPORT_WIFI_CELLULAR_ETHERNET;
                    break;
                case 3:
                    prefer = TRANSPORT_CELLULAR_WIFI_ETHERNET;
                    break;
                case 4:
                    prefer = TRANSPORT_CELLULAR_ETHERNET_WIFI;
                    break;
                case 5:
                    prefer = TRANSPORT_ETHERNET_CELLULAR_WIFI;
                    break;
                case 6:
                    prefer = TRANSPORT_ETHERNET_WIFI_CELLULAR;
                    break;
                default:
                    prefer = null;
            }
            Boolean returnNum;
            try {
                if (prefer != null) {
                    returnNum = iNetworkManager.setMultiNetworkPrefer(prefer);
                    Toast.makeText(getContext(), "return " + returnNum, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "return Num = " + returnNum);
                } else {
                    Toast.makeText(getContext(), "input is invalid", Toast.LENGTH_SHORT).show();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMultiNetwork(View view) {
        if (iNetworkManager != null) {
            try {
                iNetworkManager.setMultiNetwork(true);
                Log.d(TAG, "开启setMultiNetwork");
                Log.d(TAG, "isMultiNetwork返回值为：" + iNetworkManager.isMultiNetwork());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectMobileDataOnSlot(View view) {
        String slot;
        switch (enterSlot.getText().toString()) {
            case "0":
                slot = "0";
                break;
            case "1":
                slot = "1";
                break;
            case "2":
                slot = "2";
                break;
            default:
                slot = "-1";
                break;
        }
        if (iNetworkManager != null) {
            if (!slot.equals("-1")) {
                try {
                    int n;
                    n = iNetworkManager.selectMobileDataOnSlot(Integer.parseInt(slot));
                    Toast.makeText(getContext(), "return " + n, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "n = " + n);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void enableAirplayMode(View view) {
        if (iNetworkManager != null) {
            try {
                iNetworkManager.enableAirplayMode(airModeEnabled);
                airModeEnabled = !airModeEnabled;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void enableMobileData(View view) {
        if (iNetworkManager != null) {
            try {
                iNetworkManager.enableMobileData(mobileDataEnabled);
                mobileDataEnabled = !mobileDataEnabled;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addNetworkAllowedList(View view) {
        if (iNetworkManager != null) {
//            String zipPath = "/sdcard/system.allowed_list.zip";
            final String zipPath = !TextUtils.isEmpty(et_addNetworkAllowedList.getText().toString()) ? et_addNetworkAllowedList.getText().toString() : null;
            IAddNetworkAllowedListObserver observer = new IAddNetworkAllowedListObserver() {
                @Override
                public void onResult(int result) throws RemoteException {
                    Log.d(TAG, "result = " + result);
                    Toast.makeText(getContext(), "path = " + zipPath + "result = " + result, Toast.LENGTH_SHORT).show();
                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            };
            boolean isReboot = true;//reserved parameter,not used
            try {
                iNetworkManager.addNetworkAllowedList(zipPath, observer, isReboot);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }


}