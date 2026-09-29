package com.dreamtek.systemservice.test.demo.fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.dreamtek.smartpos.system_service.aidl.ISystemManager;
import com.dreamtek.smartpos.system_service.aidl.device.ISysDeviceInfo;
import com.dreamtek.smartpos.system_service.aidl.networks.INetworkManager;
import com.dreamtek.smartpos.system_service.aidl.settings.ISettingsManager;
import com.dreamtek.systemservice.test.demo.R;
import com.dreamtek.systemservice.test.demo.utils.DeviceInfoConstant;
import com.dreamtek.systemservice.test.demo.utils.SystemServiceAccess;


public class DeviceInfoFragment extends Fragment {

    private final SystemServiceAccess.Listener mSystemServiceListener = new SystemServiceAccess.Listener() {
        @Override
        public void onSystemReady(ISystemManager system, ISysDeviceInfo deviceInfo,
                                  INetworkManager network, ISettingsManager settings) {
            Log.d(TAG, "system service ready");
            systemManager = system;
            settingsManager = settings;
            iNetworkManager = network;
            iSysDeviceInfo = deviceInfo;
        }

        @Override
        public void onSystemLost() {
            Log.d(TAG, "system service disconnected.");
            systemManager = null;
            // 通知用户服务已断开
            Toast.makeText(getContext(), "System service disconnected", Toast.LENGTH_SHORT).show();
        }
    };
    private static INetworkManager iNetworkManager = null;
    private static ISystemManager systemManager = null;
    private static ISettingsManager settingsManager = null;
    private static ISysDeviceInfo iSysDeviceInfo = null;
    Button getSerialNo,getIMSI,getIMEI,getICCID,getManufacture,getModel,getAndroidOSVersion,getAndroidKernelVersion,getROMVersion,
            getFirmwareVersion,getHardwareVersion,getPN,getRamTotal,getRamAvailable,getRomTotal,getRomAvailable,getMobileDataUsageTotal,
            getBootCounts,getPrintPaperLen,getMagCardUsedTimes,getSmartCardUsedTimes,getCTLSCardUsedTimes,getBatteryTemperature,getBatteryLevel,
            getBatteryChargingTimes,getButtonBatteryVol,getMEID,getTamperCode,getServiceVersion,getDeviceInfo,setPowerStatus,getCertificate,getDeviceStatus,getDeviceInfoEx;
    Switch sw_setpower;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        bindSystemService();
        initParam(view);
        initView();
        return view;
    }

    private void initParam(View view){
        getSerialNo = view.findViewById(R.id.getSerialNo);
        getIMSI = view.findViewById(R.id.getIMSI);
        getIMEI = view.findViewById(R.id.getIMEI);
        getICCID = view.findViewById(R.id.getICCID);
        getManufacture = view.findViewById(R.id.getManufacture);
        getModel = view.findViewById(R.id.getModel);
        getAndroidOSVersion = view.findViewById(R.id.getAndroidOSVersion);
        getAndroidKernelVersion = view.findViewById(R.id.getAndroidKernelVersion);
        getROMVersion = view.findViewById(R.id.getROMVersion);
        getFirmwareVersion = view.findViewById(R.id.getFirmwareVersion);
        getHardwareVersion = view.findViewById(R.id.getHardwareVersion);
        getPN = view.findViewById(R.id.getPN);
        getRamTotal = view.findViewById(R.id.getRamTotal);
        getRamAvailable = view.findViewById(R.id.getRamAvailable);
        getRomAvailable = view.findViewById(R.id.getRomAvailable);
        getMobileDataUsageTotal = view.findViewById(R.id.getMobileDataUsageTotal);
        getBootCounts = view.findViewById(R.id.getBootCounts);
        getPrintPaperLen = view.findViewById(R.id.getPrintPaperLen);
        getMagCardUsedTimes = view.findViewById(R.id.getMagCardUsedTimes);
        getSmartCardUsedTimes = view.findViewById(R.id.getSmartCardUsedTimes);
        getCTLSCardUsedTimes = view.findViewById(R.id.getCTLSCardUsedTimes);
        getBatteryTemperature = view.findViewById(R.id.getBatteryTemperature);
        getRomTotal = view.findViewById(R.id.getRomTotal);
        getBatteryLevel = view.findViewById(R.id.getBatteryLevel);
        getBatteryChargingTimes = view.findViewById(R.id.getBatteryChargingTimes);
        getButtonBatteryVol = view.findViewById(R.id.getButtonBatteryVol);
        getTamperCode = view.findViewById(R.id.getTamperCode);
        getServiceVersion = view.findViewById(R.id.getServiceVersion);
        getDeviceInfo = view.findViewById(R.id.getDeviceInfo);
        getMEID = view.findViewById(R.id.getMEID);
        sw_setpower = view.findViewById(R.id.sw_setpower);
        setPowerStatus = view.findViewById(R.id.setPowerStatus);

        getCertificate = view.findViewById(R.id.getCertificate);
        getDeviceStatus = view.findViewById(R.id.getDeviceStatus);
        getDeviceInfoEx = view.findViewById(R.id.getDeviceInfoEx);
    }

    public void initView() {
        setPowerStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = sw_setpower.isChecked();
                try {
                    iSysDeviceInfo.setPowerStatus(isChecked);
                    Toast.makeText(getContext(), "isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getSerialNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String serialNo = iSysDeviceInfo.getSerialNo();
                        Log.d(TAG,"serialNo = "+serialNo);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"sn="+serialNo,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getIMSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getIMSI = iSysDeviceInfo.getIMSI();
                        Log.d(TAG,"getIMSI = "+getIMSI);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getIMSI="+getIMSI,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getIMEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getIMEI = iSysDeviceInfo.getIMEI();
                        Log.d(TAG,"getIMEI = "+getIMEI);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getIMEI="+getIMEI,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getICCID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getICCID = iSysDeviceInfo.getICCID();
                        Log.d(TAG,"getICCID = "+getICCID);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getICCID="+getICCID,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getManufacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getManufacture = iSysDeviceInfo.getManufacture();
                        Log.d(TAG,"getManufacture = "+getManufacture);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getManufacture="+getManufacture,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getModel = iSysDeviceInfo.getModel();
                        Log.d(TAG,"getModel = "+getModel);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getModel="+getModel,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getAndroidOSVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getAndroidOSVersion = iSysDeviceInfo.getAndroidOSVersion();
                        Log.d(TAG,"getAndroidOSVersion = "+getAndroidOSVersion);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getAndroidOSVersion="+getAndroidOSVersion,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getAndroidKernelVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getAndroidKernelVersion = iSysDeviceInfo.getAndroidKernelVersion();
                        Log.d(TAG,"getAndroidKernelVersion = "+getAndroidKernelVersion);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getAndroidKernelVersion="+getAndroidKernelVersion,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getROMVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getROMVersion = iSysDeviceInfo.getROMVersion();
                        Log.d(TAG,"getROMVersion = "+getROMVersion);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getROMVersion="+getROMVersion,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getFirmwareVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getFirmwareVersion = iSysDeviceInfo.getFirmwareVersion();
                        Log.d(TAG,"getFirmwareVersion = "+getFirmwareVersion);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getFirmwareVersion="+getFirmwareVersion,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getHardwareVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getHardwareVersion = iSysDeviceInfo.getHardwareVersion();
                        Log.d(TAG,"getHardwareVersion = "+getHardwareVersion);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getHardwareVersion="+getHardwareVersion,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getPN = iSysDeviceInfo.getPN();
                        Log.d(TAG,"getPN = "+getPN);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getPN="+getPN,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getRamTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getRamTotal = iSysDeviceInfo.getRamTotal();
                        Log.d(TAG,"getRamTotal = "+getRamTotal);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getRamTotal="+getRamTotal,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getRamAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getRamAvailable = iSysDeviceInfo.getRamAvailable();
                        Log.d(TAG,"getRamAvailable = "+getRamAvailable);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getRamAvailable="+getRamAvailable,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getRomTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getRomTotal = iSysDeviceInfo.getRomTotal();
                        Log.d(TAG,"getRomTotal = "+getRomTotal);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getRomTotal="+getRomTotal,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getRomAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getRomAvailable = iSysDeviceInfo.getRomAvailable();
                        Log.d(TAG,"getRomAvailable = "+getRomAvailable);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getRomAvailable="+getRomAvailable,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getRomAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getRomAvailable = iSysDeviceInfo.getRomAvailable();
                        Log.d(TAG,"getRomAvailable = "+getRomAvailable);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getRomAvailable="+getRomAvailable,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getMobileDataUsageTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getMobileDataUsageTotal = iSysDeviceInfo.getMobileDataUsageTotal();
                        Log.d(TAG,"getMobileDataUsageTotal = "+getMobileDataUsageTotal);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getMobileDataUsageTotal="+getMobileDataUsageTotal,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getBootCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getbootconts = iSysDeviceInfo.getBootCounts();
                        Log.d(TAG,"getBootCounts = "+getbootconts);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getBootCounts="+getbootconts,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (iSysDeviceInfo!=null){
                        //根据实际情况参数填入0 或 1
                        String getbootconts = iSysDeviceInfo.getCertificate(0);
                        Log.d(TAG,"getCertificate = "+getbootconts);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getCertificate="+getbootconts,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getDeviceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (iSysDeviceInfo!=null){
                         Bundle bundle = new Bundle();
                         //根据需要填入想要获取的参数字段名，这里以PRINTER为例，即获取打印机状态
                         bundle.putString("DeviceType", "PRINTER");
                        int deviceStatus = iSysDeviceInfo.getDeviceStatus(bundle);
                        Log.d(TAG,"getDeviceStatus = "+deviceStatus);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getDeviceStatus="+deviceStatus,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getDeviceInfoEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (iSysDeviceInfo!=null){
                        Bundle bundle = new Bundle();
                        //可按需填入想要获取的字段名，例如想获取SN和RomVersion。想获取其他数据按照需要填入key即可
                        bundle.putString(DeviceInfoConstant.SN, "");
                        bundle.putString(DeviceInfoConstant.ROMVER, "");
                        Bundle deviceInfoEx = iSysDeviceInfo.getDeviceInfoEx(bundle);

                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("getDeviceStatus =");
                        if (deviceInfoEx != null) {
                            String sn = deviceInfoEx.getString(DeviceInfoConstant.SN);
                            String romV = deviceInfoEx.getString(DeviceInfoConstant.ROMVER);
                            stringBuffer.append("sn:" + sn + " ,");
                            stringBuffer.append("romV:" + romV + " ,");
                            Log.d(TAG, stringBuffer.toString());
                        } else {
                            Log.d(TAG,"getDeviceStatus = "+"null");
                        }
                        Toast.makeText(DeviceInfoFragment.this.getContext(), stringBuffer.toString(), Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getPrintPaperLen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getPrintPaperLen = iSysDeviceInfo.getPrintPaperLen();
                        Log.d(TAG,"getPrintPaperLen = "+getPrintPaperLen);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getPrintPaperLen="+getPrintPaperLen,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getMagCardUsedTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getMagCardUsedTimes = iSysDeviceInfo.getMagCardUsedTimes();
                        Log.d(TAG,"getMagCardUsedTimes = "+getMagCardUsedTimes);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getMagCardUsedTimes="+getMagCardUsedTimes,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getMobileDataUsageTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getMobileDataUsageTotal = iSysDeviceInfo.getMobileDataUsageTotal();
                        Log.d(TAG,"getMobileDataUsageTotal = "+getMobileDataUsageTotal);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getMobileDataUsageTotal="+getMobileDataUsageTotal,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getCTLSCardUsedTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getCTLSCardUsedTimes = iSysDeviceInfo.getCTLSCardUsedTimes();
                        Log.d(TAG,"getCTLSCardUsedTimes = "+getCTLSCardUsedTimes);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getCTLSCardUsedTimes="+getCTLSCardUsedTimes,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getSmartCardUsedTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getSmartCardUsedTimes = iSysDeviceInfo.getSmartCardUsedTimes();
                        Log.d(TAG,"getSmartCardUsedTimes = "+getSmartCardUsedTimes);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getSmartCardUsedTimes="+getSmartCardUsedTimes,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getBatteryTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getBatteryTemperature = iSysDeviceInfo.getBatteryTemperature();
                        Log.d(TAG,"getBatteryTemperature = "+getBatteryTemperature);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getBatteryTemperature="+getBatteryTemperature,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getBatteryLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getBatteryLevel = iSysDeviceInfo.getBatteryLevel();
                        Log.d(TAG,"getBatteryLevel = "+getBatteryLevel);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getBatteryLevel="+getBatteryLevel,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getBatteryChargingTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getBatteryChargingTimes = iSysDeviceInfo.getBatteryChargingTimes();
                        Log.d(TAG,"getBatteryChargingTimes = "+getBatteryChargingTimes);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getBatteryChargingTimes="+getBatteryChargingTimes,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getButtonBatteryVol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getButtonBatteryVol = iSysDeviceInfo.getButtonBatteryVol();
                        Log.d(TAG,"getButtonBatteryVol = "+getButtonBatteryVol);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getButtonBatteryVol="+getButtonBatteryVol,Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getMEID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getMEID = iSysDeviceInfo.getMEID();
                        Log.d(TAG,"getMEID = "+getMEID);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getMEID="+getMEID,Toast.LENGTH_SHORT).show();

                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getTamperCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getTamperCode = iSysDeviceInfo.getTamperCode();
                        Log.d(TAG,"getTamperCode = "+getTamperCode);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getTamperCode="+getTamperCode,Toast.LENGTH_SHORT).show();

                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getServiceVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        String getServiceVersion = iSysDeviceInfo.getServiceVersion();
                        Log.d(TAG,"getServiceVersion = "+getServiceVersion);
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getServiceVersion="+getServiceVersion,Toast.LENGTH_SHORT).show();

                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (iSysDeviceInfo!=null){
                        Bundle getDeviceInfo = iSysDeviceInfo.getDeviceInfo();
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("getDeviceInfo,");
                        if (getDeviceInfo != null) {
                            String sn = getDeviceInfo.getString(DeviceInfoConstant.SN);
                            String pn = getDeviceInfo.getString(DeviceInfoConstant.PN);
                            String imsi = getDeviceInfo.getString(DeviceInfoConstant.IMSI);
                            String imei = getDeviceInfo.getString(DeviceInfoConstant.IMEI);
                            String meid = getDeviceInfo.getString(DeviceInfoConstant.MEID);
                            String manufa = getDeviceInfo.getString(DeviceInfoConstant.MANUFACTURE);
                            String devicemodule = getDeviceInfo.getString(DeviceInfoConstant.DEVICEMODEL);
                            String androidOsv = getDeviceInfo.getString(DeviceInfoConstant.ANDROIDOSVER);
                            String androidKnernalv = getDeviceInfo.getString(DeviceInfoConstant.ANDROIDKERNALVER);
                            String romV = getDeviceInfo.getString(DeviceInfoConstant.ROMVER);
                            String firewareV = getDeviceInfo.getString(DeviceInfoConstant.FIRMWAREVER);
                            String hardwareV = getDeviceInfo.getString(DeviceInfoConstant.HARDWAREVER);
                            String dtkSserviceV = getDeviceInfo.getString(DeviceInfoConstant.DTKSYSSERIVCEVER);
                            String vrkSn = getDeviceInfo.getString(DeviceInfoConstant.VRKSN);
                            String sponsorID = getDeviceInfo.getString(DeviceInfoConstant.SPONSORID);
                            stringBuffer.append("sn: " + sn + ", pn:" + pn + " ,imsi:" + imsi + " ,imei:" + imei + " ,meid:" + meid + " ,manufacture:" + manufa + "\n,deviceModel:" +
                                    devicemodule + " ,androidOsV:" + androidOsv + " ,androidKnernalVersion:" + androidKnernalv + " ,romVersion:" + romV + " ,firewareVersion:" + firewareV +
                                    "\nhardwareVersion:" + hardwareV + " ,DTKSysSerivceVer:" + dtkSserviceV + " ,VRKSn:" + vrkSn + " ,SponsorID:" + sponsorID);

                            Log.d(TAG, stringBuffer.toString());
                        } else {
                            Log.d(TAG, stringBuffer.toString() + "null");
                        }
                        Toast.makeText(DeviceInfoFragment.this.getContext(),"getDeviceInfo="+stringBuffer.toString(),Toast.LENGTH_SHORT).show();

                    }else {
                        System.out.println("iSysDeviceInfo == null");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }



    private void bindSystemService() {
        // 通过宿主注入的 Provider 复用统一设备服务连接（新库 IDeviceService.getSystemManager()），
        // 不再直接绑定 com.dreamtek.smartpos.system_service。
        SystemServiceAccess.bind(getContext(), mSystemServiceListener);
    }

    @Override
    public void onDestroy() {
        SystemServiceAccess.unbind(mSystemServiceListener);
        super.onDestroy();
    }

}