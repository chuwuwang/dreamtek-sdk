// FelicaHandler.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.card_reader;

import com.dreamtek.smartpos.deviceservice.aidl.card_reader.FelicaInfomation;
interface FelicaListener {

    /**
     * Felica search card result
     *
	 * @param ret 0-success 1-timeout -1-failed
	 * @param felicaInfos list of felica data;
     * @
	 */
    void onSearchResult(int ret, in List<FelicaInfomation> felicaInfos);
}
