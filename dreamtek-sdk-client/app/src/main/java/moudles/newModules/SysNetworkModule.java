package moudles.newModules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;

/**
 * Created by Simon on 2021/9/8
 */
public class SysNetworkModule extends TestModule {
    private static String TAG = "SysNetworkModule";
    /**
     * Set network type of terminal :
     * @param mode status code:
    7 = Global
    6 = EvDo only
    5 = CDMA w/o EvDo
    4 = CDMA / EvDo auto
    3 = GSM / WCDMA auto
    2 = WCDMA only
    1 = GSM only
    0 = GSM / WCDMA preferred
    -1 = acquiring failed
     * @since 1.0.0
     */

    void T_setNetworkType(String mode ) throws RemoteException {
        int iMode = Integer.valueOf( mode );
        iNetworkManager.setNetworkType (iMode);
    }

    /**
     * Get current network type of terminal
     * @return networkType
     * @since 1.0.0
     */
    int T_getNetworkType() throws RemoteException {
        return iNetworkManager.getNetworkType ();
    }

    /**
     * Control WiFi
     * @param state true: open Wifi false: close Wifi
     * @since 1.0.0
     */
    void T_enableWifi(String state) throws RemoteException {
        boolean bState = Boolean.valueOf( state );
        iNetworkManager.enableWifi ( bState);
//
//        WifiManager wifiManager;
//        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled( Boolean.valueOf(state) );
//
//        int maxRetry = 10;
//        do {
//            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo info = manager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE);
//            if( info == null ){
//                Log.e(TAG, "Network info is null");
//            } else {
//                if( info.isConnected() && info.isAvailable() ) {
//                    Log.d(TAG, "Connected");
//                } else {
//                    Log.w( TAG, "Mobile: " + info.isConnected() +", " + info.isAvailable() );
//                }
//            }
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } while (--maxRetry >= 0 );
    }

    /**
     * Control Airplane mode
     * @param state true: open AirplayMode false: close AirplayMode
     * @since 1.0.0
     */
    void T_enableAirplayMode(String state) throws RemoteException {
        boolean bState = Boolean.valueOf( state );

        iNetworkManager.enableAirplayMode ( bState);
    }

    /**
     * Add a new APN configuration and set it to default <br/>
     * The auth type column can have 4 values: 0 (None), 1 (PAP), 2 (CHAP)
     3 (PAP or CHAP). To avoid breaking compatibility, with already working
     APNs, the unset value (-1) will be used. If the value is -1.
     the authentication will default to 0 (if no user / password) is specified
     or to 3. Currently, there have been no reported problems with
     pre-configured APNs and hence it is set to -1 for them. Similarly,
     if the user, has added a new APN, we set the authentication type
     to -1.
     * @param infos add APN infos in bundle, example:
     * <p><pre>{@code
    Bundle infos = new Bundle();
    infos.putString("name", "test01");
    infos.putString("apn", "test01");
    infos.putString("authtype", "-1");
    //numeric is mcc and mnc
    infos.putString("numeric", "46002");
    //     infos.putString("mcc", "460");
    //     infos.putString("mnc", "02");
    infos.putString("proxy","");
    infos.putString("port","");
    infos.putString("mmsproxy", "");
    infos.putString("mmsport", "");
    infos.putString("user", "");
    infos.putString("server", "");
    infos.putString("password","");
    infos.putString("mmsc", "");
    infos.putString("current", "1");
    infos.putString("carrier_enabled", "1");
    infos.putString("protocol", "IP");
    infos.putString("roaming_protocol", "IP");
    infos.putString("bearer", "0");
    infos.putString("max_conns", "0");
    infos.putString("max_conns_time", "0");
    infos.putString("modem_cognitive", "0");
    infos.putString("localized_name", "");
    infos.putString("mvno_match_data", "");
    infos.putString("mvno_type", "");
    infos.putString("profile_id", "0");
    infos.putString("read_only", "0");
    infos.putString("sub_id", "1");
    infos.putString("type", "");
    // "SLOT"   // Add by Simon on version 1.6.0.3
    // SLOT: 1 or 2 for SIM card in slot 1 or 2.
    // using the active slot as default if there is no SLOT setting & no "fixed_numeric" setting
    infos.putString("SLOT", "1");
    // "fixed_numeric"  // add by Simon on version 1.6.0.3
    // fixed the numeric to fixed_numeric for specific SIM card
    // using the "SLOT" if there is no "fixed_numeric" setting
    infos.putString("fixed_numeric", "46002");
     * }
     * </pre>
     * @retrun result
     * @since 1.0.0
     */
    int T_setAPN(String infos) throws RemoteException {
        BundleConfig[] bundleConfigs = new BundleConfig[ ]{

        };
        Bundle param = convert( infos, bundleConfigs );

        return iNetworkManager.setAPN ( param );
    }

    /**
     * Control Mobile data
     * @param state true: open MobileData false: close MobileData
     * @since 1.0.0
     */
    void T_enableMobileData( String state) throws RemoteException {

        iNetworkManager.enableMobileData ( Boolean.valueOf( state ));
    }

    /**
     * Get current selected APN configurations
     * @return current selected APN configurations
     */
    String T_getSelectedApnInfo() throws RemoteException {
        Bundle ret = iNetworkManager.getSelectedApnInfo ();
        Log.d(TAG, ret.toString() );
        String result = "";
        for ( String key: ret.keySet() ) {
            Log.d(TAG, key + " : " + ret.get( key ) );
        }
        return ret.toString();
    }

    /**
     * select which to use for mobile data
     * @param slotIdx 1 or 2 to use for mobile data
     * @return result
     * @since 1.7.0.1
     */
    int T_selectMobileDataOnSlot( String slotIdx) throws RemoteException {
        return iNetworkManager.selectMobileDataOnSlot ( Integer.valueOf(slotIdx) );
    }

    /**
     * Is enable MultiNetwork
     * @return true-enable multiNetwork false-disable multiNetwork
     * @since 1.8.0
     */
    boolean T_isMultiNetwork() throws RemoteException {
        return iNetworkManager.isMultiNetwork ();
    }

    /**
     * Control MultiNetwork
     * @param enable true: able multiNetwork false: disable multiNetwork
     * @since 1.8.0
     */
    void T_setMultiNetwork(String enable) throws RemoteException {
        iNetworkManager.setMultiNetwork ( Boolean.valueOf(enable) );
    }

    /**
     * Get the prefer of MultiNetwork
     * @return the prefer of MultiNetwork
     * @since 1.8.0
     */
    String T_getMultiNetworkPrefer() throws RemoteException {
        return iNetworkManager.getMultiNetworkPrefer ();
    }

    /**
     * set the prefer of MultiNetwork
     * @param prefer the prefer to set
     * <p><pre>{@code
    public static final String TRANSPORT_WIFI_ETHERNET_CELLULAR = "wifi,ethernet,cellular";
    public static final String TRANSPORT_WIFI_CELLULAR_ETHERNET = "wifi,cellular,ethernet";
    public static final String TRANSPORT_CELLULAR_WIFI_ETHERNET = "cellular,wifi,ethernet";
    public static final String TRANSPORT_CELLULAR_ETHERNET_WIFI = "cellular,ethernet,wifi";
    public static final String TRANSPORT_ETHERNET_CELLULAR_WIFI = "ethernet,cellular,wifi";
    public static final String TRANSPORT_ETHERNET_WIFI_CELLULAR = "ethernet,wifi,cellular";
     * }
     * </pre>
     * @return result true for success, false for failure
     * @since 1.8.0
     */

    // 由于这个API的参数使用 逗号分隔的，与自动测试的API的参数规则存在冲突，所以另外创建了几个API用于处理这种情况。
    // 或者在 创建案例时，参数使用转译符代替 逗号
    boolean T_setMultiNetworkPrefer(String prefer) throws RemoteException {
        return iNetworkManager.setMultiNetworkPrefer (prefer);
    }
    boolean T_setMultiNetworkPrefer(String a, String b) throws RemoteException {
        return T_setMultiNetworkPrefer (a + "," + b);
    }
    boolean T_setMultiNetworkPrefer(String a, String b, String c) throws RemoteException {
        return T_setMultiNetworkPrefer (a + "," + b + "," + c);
    }

    /**
     * set ethernet static ip <br/>
     * set STATIC_IP 0.0.0.0 or 0 to change connection type to DHCP
     * @param infos - add static ip infos in bundle, example:
     * <p><pre>{@code
    Bundle infos = new Bundle();
    infos.putString("STATIC_IP", "192.168.1.1");
    infos.putString("STATIC_GATEWAY", "192.168.1.1");
    infos.putString("STATIC_NETMASK", "255.255.255.0");
    infos.putString("STATIC_DNS1", "192.168.1.1");
    infos.putString("STATIC_DNS2", "192.168.1.1");
     * }
     * </pre>
     * @since 1.8.1
     */
    void T_setEthernetStaticIp(String infos) throws RemoteException {
        BundleConfig[] bundleConfigs = new BundleConfig[]{

        };
        Bundle bundle = convert( infos, bundleConfigs);
        iNetworkManager.setEthernetStaticIp (bundle);
    }

    /**
     * set wifi static ip <br/>
     * set STATIC_IP 0.0.0.0 or 0 to change connection type to DHCP
     * <p><pre>{@code
    Bundle infos = new Bundle();
    infos.putString("STATIC_IP", "192.168.1.1");
    infos.putString("STATIC_GATEWAY", "192.168.1.1");
    infos.putString("STATIC_NETMASK", "255.255.255.0");
    infos.putString("STATIC_DNS1", "192.168.1.1");
    infos.putString("STATIC_DNS2", "192.168.1.1");
     * }
     * </pre>
     * @since 1.8.5
     */
    void T_setWifiStaticIp(String infos) throws RemoteException {
        BundleConfig[] bundleConfigs = new BundleConfig[]{

        };
        Bundle bundle = convert( infos, bundleConfigs);
        iNetworkManager.setWifiStaticIp (bundle);
    }

    /**
     * set mobile preferred network type, only the current SIM card
     * @param type 2G:only use 2G
    3G:use 2G/3G
    4G:use 2G/3G/4G
     * @since 1.8.4
     */
    void T_setMobilePreferredNetworkType(String type) throws RemoteException {
        iNetworkManager.setMobilePreferredNetworkType (type);
    }

    /**
     * get mobile preferred network type, only the current SIM card
     * @return result 2G 3G 4G for success, null for failure
     * @since 1.8.4
     */
    String T_getMobilePreferredNetworkType() throws RemoteException {
        return iNetworkManager.getMobilePreferredNetworkType();
    }

    /**
     * Delete exist APN
     * @param apn : which you set-in setAPN()
     * @since 1.0.0
     */
    int T_deleteAPN(String apn) throws RemoteException {
        return iNetworkManager.deleteAPN(apn);
    }

    /**
     * set Data Roaming state
     * @param enabled : true-enable dataRoaming; false-disable dataRoaming
     * @param slotId : the index of sim card slot
     * @since 1.0.0
     */
    int T_setDataRoamingEnabled(String enabled, String slotId) throws RemoteException {
        return iNetworkManager.setDataRoamingEnabled(Boolean.valueOf(enabled), Integer.valueOf(slotId) );
    }

    /**
     * Add a new Network
     * @param infos :
    paramName | type   | description
    ----------------------------------
    SSID     | String | network name
    password | String | password
    type     | int    | security type : 1-NONE 2-WEP 3-WPA(default)
    -----------------------------------
     * @return the ID of the newly created network description. This is used in
     *         other operations to specified the network to be acted upon.
     *         Returns {@code -1} on failure.
     * @since 1.8.12
     */
    int T_addNetwork(String infos) throws RemoteException {
        BundleConfig[] bundleConfigs = new BundleConfig[]{

        };
        Bundle bundle = convert( infos, bundleConfigs);
        return iNetworkManager.addNetwork(bundle);
    }

    /**
     * Connect to wifi
     * @param SSID : network name
     * @reture true-success, false-failed
     * @since 1.8.12
     */
    private boolean T_connectWifi(String SSID) throws RemoteException {
        return iNetworkManager.connectWifi( SSID);
    }

  /*  int addNetworkAllowedList(String zipPath, String isReboot) throws RemoteException {
        final int[] ret = new int[1];
        synchronized ( cbLock ){
            ret[0] = 0;
        }

        IAddNetworkAllowedListObserver observer = new IAddNetworkAllowedListObserver.Stub() {
            @Override
            public void onResult(int result) throws RemoteException {
                Log.d(TAG, "onResult tells: " + result );
                synchronized (cbLock) {
                    ret[0] = result;
                    cbLock.notify();
                }
            }
        };
        iNetworkManager.addNetworkAllowedList(zipPath, observer, Boolean.valueOf(isReboot));
        synchronized (cbLock) {
            try {
                cbLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return ret[0];
    }*/

}
