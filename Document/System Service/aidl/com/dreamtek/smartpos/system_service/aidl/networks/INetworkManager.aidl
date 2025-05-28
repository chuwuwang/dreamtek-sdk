package com.dreamtek.smartpos.system_service.aidl.networks;

import com.dreamtek.smartpos.system_service.aidl.IAddNetworkAllowedListObserver;

// Declare any non-default types here with import statements
import android.os.Bundle;
/*
 *Exposed APIs for Application to control terminal networks
 *
*/
interface INetworkManager {

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
     */
    void setNetworkType(int mode);

    /**
     * Get current network type of terminal
     * @return networkType
     */
    int getNetworkType();

    /**
     * Control WiFi
     * @param state true: open Wifi false: close Wifi
     */
    void enableWifi(boolean state);

    /**
     * Control Airplane mode
     * @param state true: open AirplayMode false: close AirplayMode
     */
    void enableAirplayMode(boolean state);

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
         infos.putString("name", "test01");//Must be passed in
         infos.putString("apn", "test01");//Must be passed in
         infos.putString("authtype", "-1");
         infos.putString("proxy","");
         infos.putString("port","");
         infos.putString("mmsproxy", "");
         infos.putString("mmsport", "");
         infos.putString("user", "");
         infos.putString("server", "");
         infos.putString("password","");
         infos.putString("mmsc", "");
         infos.putString("protocol", "IP");
         infos.putString("roaming_protocol", "IP");
         infos.putString("bearer", "0");
         infos.putString("max_conns", "0");
         infos.putString("max_conns_time", "0");
         infos.putString("mvno_match_data", "");
         infos.putString("mvno_type", "");
         infos.putString("type", "");
         infos.putBoolean("selected",true);

         The following options deprecated:
//         infos.putString("current", "1");
//         infos.putString("carrier_enabled", "1");
//         infos.putString("modem_cognitive", "0");
//         infos.putString("localized_name", "");
//         infos.putString("profile_id", "0");
//         infos.putString("read_only", "0");
//         infos.putString("sub_id", "1");
    * }
    * </pre>
    * @retrun result
    */
    int setAPN(in Bundle infos);

    /**
     * Control Mobile data
     * @param state true: open MobileData false: close MobileData
     */
    void enableMobileData( boolean state);

    /**
     * Get current selected APN configurations
     * @return current selected APN configurations
     */
    Bundle getSelectedApnInfo();

    /**
     * select which to use for mobile data
     * @param slotIdx 1 or 2 to use for mobile data
     * @return result
     */
    int selectMobileDataOnSlot( int slotIdx);

    /**
    * Is enable MultiNetwork
    * @return true-enable multiNetwork false-disable multiNetwork
    */
    boolean isMultiNetwork();

    /**
    * Control MultiNetwork
    * @param enable true: able multiNetwork false: disable multiNetwork
    */
    void setMultiNetwork(boolean enable);

    /**
    * Get the prefer of MultiNetwork
    * @return the prefer of MultiNetwork
    */
    String getMultiNetworkPrefer();

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
    */
    boolean setMultiNetworkPrefer(String prefer);

    /**
     * set ethernet static ip <br/>
     * noted: need re-insert to take effect
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
     */
    void setEthernetStaticIp(in Bundle bundle);

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
     */
    void setWifiStaticIp(in Bundle bundle);

    /**
    * set mobile preferred network type, only the current SIM card
    * @param type 2G:only use 2G
                  3G:use 2G/3G
                  4G:use 2G/3G/4G
    */
   void setMobilePreferredNetworkType(String type);

   /**
   * get mobile preferred network type, only the current SIM card
   * @return result 2G 3G 4G for success, null for failure
   */
  String getMobilePreferredNetworkType();

    /**
    * Delete exist APN
    * @param apn : which you set-in setAPN()
    */
    int deleteAPN(in String apn);

    /**
    * set Data Roaming state
    * @param enabled : true-enable dataRoaming; false-disable dataRoaming
    * @param slotId : the index of sim card slot
    */
    int setDataRoamingEnabled(boolean enabled, int slotId);

    /**
    * Add a new Network
    * @param Bundle :
       paramName | type   | description
       ----------------------------------
        SSID     | String | network name
        password | String | password
        type     | int    | security type : 1-NONE 2-WEP 3-WPA(default)
       -----------------------------------
    * @return the ID of the newly created network description. This is used in
    *         other operations to specified the network to be acted upon.
    *         Returns {@code -1} on failure.
    */
    int addNetwork(in Bundle bundle);

    /**
    * Connect to wifi
    * @param SSID : network name
    * @reture true-success, false-failed
    */
    boolean connectWifi(String SSID);

	/**
	 * Add network allowed list to terminal
	 * A zip package will be used, and it should be signed by uKey.
	 * Only one file should be in the zip, and it's name must be "system.allowed_list"
	 * Notice:
	 *      1. Terminal will reboot automatically after it done
	 *      2. whiteliet have to add "connectivitycheck.gstatic.com"(This is the address used by ROM to determine whether the network is available.)
	 *
	 * The structure of the zip is as below:
	 * ..zip
	 *   -system.allowed_list
	 *   -META-INF/
	 *     -singature files
	 *
	 * This is example for "system.allowed_list", [uses-ip-1] have to add in the whitelist:
     * [uses-ip-1]
     * ip=connectivitycheck.gstatic.com
	 * [uses-ip-2]
     * ip=192.168.100.1
     * [uses-ip-3]
     * ip=www.baidu.com
     * [uses-ip-4]
     * ip=http://192.262.2.2/test
     *
	 * @param zipPath - path of zip package
	 * @param observer - callback handler.
	 * @param isReboot - Reserved parameter, not used
	 * @see IAddNetworkAllowedListObserver
	 */
    void addNetworkAllowedList(String zipPath, IAddNetworkAllowedListObserver observer, boolean isReboot);

     /**
        * Set a network-independent global http proxy.It will take effect after reboot.
            This is not normally what you want for typical HTTP proxies - they are general network dependent.
            However if you're doing something unusual like general internal filtering this may be useful.
            On a private network where the proxy is not accessible, you may break HTTP using this.
        * @param proxy : use format ip:host, if proxy = "" will clear the proxy.
        * @param extend : for future use
        * noted: After setting, need using adb commands to get hostIp and port:
                adb shell settings get global global_http_proxy_host
                adb shell settings get global global_http_proxy_port
        */
    int setProxy(String proxy, in Bundle extend);

    /**
     *
     * @param type 0-WiFI 1-ETH
     * @return MAC address
     * @throws RemoteException
     */
    String getMacAddress(int type);

    /**
     * get Wifi proxy menu show or hidden
     * @return true-wifi proxy menu show; false- wifi proxy function is hidden
     * @throws RemoteException
     */
    boolean getWifiProxyState();

      /**
     * Set wifi proxy menu show or hidden, after setting, need reboot device
     * @param state: true-wifi proxy menu show; false- wifi proxy menu is hidden
     * check method: Settings > Network & internet > Click your current connected wifi >
                        get into Network Details interface and click the top right button > click Advanced options
                        > if enable, show Proxy menu; if disable, Proxy menu is hidden.
     * @throws RemoteException
       */
    void setWifiProxyState(boolean state);

    /**
     *
     * @return bundle
     * <ul>
     *     <li>IP(String)</li>
     *     <li>MAC(String)</li>
     *     <li>DNS(String)</li>
     * </ul>
     * @throws RemoteException
     */
    Bundle getCurrentNetworkDetails();
}
