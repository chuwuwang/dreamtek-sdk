package com.dreamtek.smartpos.deviceservice.aidl;
import com.dreamtek.smartpos.deviceservice.aidl.TusnData;

/**
 * <p> get service information
 */
interface IServiceInfo {

    /**
     * <p> get the Security driver version.
     *
     * @return {@code String}
     * @
     */
    String getSercurityDriverVersion();

   /**
     * <p> Get Kernel version.
     *
     * @return {@code android.os.Bundle}
     * <p> The return Bundle object key: <ul>
     *    <li> @{Code String} SmartEMV</li>
     *    <li> @{Code String} Visa</li>
     *    <li> @{Code String} MasterCard</li>
     *    <li> @{Code String} JCB</li>
     *    <li> @{Code String} AMEX</li>
     *    <li> @{Code String} Discover</li>
     *    <li> @{Code String} QuickPass</li>
     *    <li> @{Code String} GemaltoPure</li>
     *    <li> @{Code String} RuPay</li>
     *    <li> @{Code String} Mir (not support)</li>

     *    <li> @{Code String} SmartEMV_Checksum</li>
     *    <li> @{Code String} Visa_Checksum</li>
     *    <li> @{Code String} MasterCard_Checksum</li>
     *    <li> @{Code String} JCB_Checksum</li>
     *    <li> @{Code String} AMEX_Checksum</li>
     *    <li> @{Code String} Discover_Checksum</li>
     *    <li> @{Code String} QuickPass_Checksum</li>
     *    <li> @{Code String} GemaltoPure_Checksum</li>
     *    <li> @{Code String} RuPay_Checksum</li>
     *    <li> @{Code String} Mir_Checksum (not support)</li>
     * </ul>
     * <p><pre>{@code
     *     String smartEMV = bundle.getString("SmartEMV");
     *     String visa = bundle.getString("Visa");
     *     String masterCard = bundle.getString("MasterCard");
     *     String jcb = bundle.getString("JCB");
     *     String amex = bundle.getString("AMEX");
     *     String discover = bundle.getString("Discover");
     *     String quickPass = bundle.getString("QuickPass");
     *     String gemaltoPure = bundle.getString("GemaltoPure");
     * }
     * </pre>
     */
    Bundle getKernelVersion();

    /**
     * <p> get the DTKService Version.
     *
     * @return {@code String}
     * @
     */
    String getServiceVersion();

}
