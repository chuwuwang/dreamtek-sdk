package moudles;

import android.content.Context;
import android.os.RemoteException;

import com.verifone.smartpos.utils.HexUtil;
import com.dreamtek.smartpos.deviceservice.aidl.card_reader.IUltraLightCardC;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Utils.LogUtils;
import base.MyApplication;

public class UltraLightCardCMoudle {
    Context context;
    IUltraLightCardC iUltraLightCardC;
    LogUtils logUtils;
    ArrayList<String> apiList = new ArrayList<String>();
    ArrayList<ArrayList<String>> caseNames = new ArrayList<ArrayList<String>>();
    ArrayList<String> read = new ArrayList<String>();
    ArrayList<String> write = new ArrayList<String>();

    public UltraLightCardCMoudle(Context context, UltraLightCardCMoudle ultraLightCardCMoudle) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iUltraLightCardC = iUltraLightCardC;
        addAllapi();
    }

    public ArrayList<String> getApiList() {
        return apiList;
    }

    public ArrayList<ArrayList<String>> getCaseNames() {
        return caseNames;
    }

    private void addAllapi() {
        try {
            Class aClass = Class.forName("moudles.UltraLightCardCMoudle");
            Method[] methods = aClass.getDeclaredMethods();
            for (Method i : methods) {
                if (i.getName().startsWith("My")) {
                    apiList.add(i.getName().replace("My", ""));
                } else {
                    switch (i.getName().substring(0, 3)) {
                        case "U01":
                            read.add(i.getName());
                            break;
                        case "U02":
                            write.add(i.getName());
                            break;


                    }
                }
            }
            caseNames.add(read);
            caseNames.add(write);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] My01read(byte bAddress) {
        try {
            byte[] ret = iUltraLightCardC.read(bAddress);
            logUtils.addCaseLog("bAddress=" + HexUtil.toString(ret));
            return ret;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }




    public int My02write(byte bAddress, byte[] pData) {
        try
        {
            int result = iUltraLightCardC.write(bAddress, pData);
            logUtils.addCaseLog("write result " + result);
//                String res = bcd2Asc(pData);
            String res = new String(pData);

//                logUtils.addCaseLog("The pdata: " + res);
            return result;
        } catch(RemoteException e)
        {
            e.printStackTrace();
            logUtils.addCaseLog("ret = -1");
            return -1;

        }
    }


    private String bcd2Asc(byte[] my06cardReset) {
        return null;
    }

    public void U01001() {
        My01read((byte)0x01);
    }

    public void U01002() {
        My01read((byte)0x02);
    }

    public void U01003() {
        My01read((byte)0x03);
    }

    public void U01004() {
        My01read((byte)0X15);
    }
    public void U01005() {
        My01read((byte)0x016);
    }
    public void U01006() {
        My01read((byte)0x00);
    }


    public void U02001() {

        My01read((byte)0X15);

        byte[] key = new byte[4];
        key[0] = (byte) 0x88;
        key[1] = (byte) 0x67;
     /*   key[2] = (byte) 0xFF;
        key[3] = (byte) 0xFF;
        key[4] = (byte) 0xFF;
        key[5] = (byte) 0xFF;
        key[6] = (byte) 0xFF;
        key[7] = (byte) 0xFF;*/
       /* key[8] = (byte) 0xFF;
        key[9] = (byte) 0xFF;
        key[10] = (byte) 0xFF;
        key[11] = (byte) 0xFF;
        key[12] = (byte) 0xFF;
        key[13] = (byte) 0xFF;
        key[14] = (byte) 0xFF;
        key[15] = (byte) 0xFF;*/
//        key[16] = (byte) 0xFF;



        My02write((byte)0X15,key);
        My01read((byte)0X15);
    }

    public void U02002() {

        My01read((byte)0X00);

        byte[] key = new byte[4];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        My02write((byte)0X00,key);

    }


    public void U02003() {

        My01read((byte)0X01);

        byte[] key = new byte[4];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        My02write((byte)0X01,key);

    }

    public void U02004() {

        My01read((byte)0X02);

        byte[] key = new byte[4];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        My02write((byte)0X02,key);
        My01read((byte)0X02);

    }

    public void U02005() {

        My01read((byte)0X03);

        byte[] key = new byte[4];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        My02write((byte)0X03,key);
        My01read((byte)0X03);

    }

    public void U02006() {

        My01read((byte)0X16);

        byte[] key = new byte[4];
        key[0] = (byte) 0xFF;
        key[1] = (byte) 0xFF;
        My02write((byte)0X16,key);
        My01read((byte)0X16);

    }




    public void runTheMethod(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.clearLog();
        try {
            Class aClass = Class.forName("moudles.UltraLightCardCMoudle");
            Method method = aClass.getMethod(name);
            method.invoke(this);
            logUtils.addCaseLog(name + "execute 完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showCaseInfo(int groupPosition, int childPosition) {
        String name = caseNames.get(groupPosition).get(childPosition);
        logUtils.printCaseInfo(name);
    }
    public UltraLightCardCMoudle(Context context, IUltraLightCardC iUltraLightCardC) {
        this.context = context;
        logUtils = MyApplication.serviceMoudle.logUtils;
        this.iUltraLightCardC = iUltraLightCardC;
        addAllapi();
    }

}
