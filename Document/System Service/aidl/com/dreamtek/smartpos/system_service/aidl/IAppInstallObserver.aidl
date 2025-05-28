// IAppInstallObserver.aidl
package com.dreamtek.smartpos.system_service.aidl;

interface IAppInstallObserver {
      /* Install Callback：returnCode：0-sucess，other value-fail */
    	/**
    	 * Install Callback：returnCode：0-sucess，other value-fail
    	 * @param packageName - package name.
    	 * @param returnCode - package name.<BR>
         * <<The following is the returnCode for V4<BR>>>
         * public static final int STATUS_FAILURE = 1;<BR>
         * public static final int STATUS_FAILURE_BLOCKED = 2;<BR>
         * public static final int STATUS_FAILURE_ABORTED = 3;<BR>
         * public static final int STATUS_FAILURE_INVALID = 4;<BR>
         * public static final int STATUS_FAILURE_CONFLICT = 5;<BR>
         * public static final int STATUS_FAILURE_STORAGE = 6;<BR>
         * public static final int STATUS_FAILURE_INCOMPATIBLE = 7;<BR>
         * public static final int STATUS_PENDING_USER_ACTION = -1;<BR>
	 */
    void onInstallFinished(String packageName,int returnCode);
}
