package moudles.newModules;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Simon on 2021/8/12
 */
public class KldModule extends TestModule {
    private static final String TAG = "KldModule";

/*
    protected int T_keyStoreTR34Payload( String jsonFilename ){

        File file = new File( jsonFilename );

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            Log.i(TAG, "json: " + stringBuilder.toString());
            bufferedReader.close();
            int ret = ikld.keyStoreTR34Payload(stringBuilder.toString().getBytes());
            if( ret < 0 ){
                Log.e(TAG, "keyStoreTR34Payload failed");
                return ret;
            }
            return ret;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -5;
    }

    protected int T_loadKBPK( String sSlot, String kbpk, String bundles ) throws RemoteException {

        Bundle bundle = convert( bundles, null );
        int ret = ikld.loadKBPK( Integer.valueOf(sSlot), kbpk, bundle );
        if( ret < 0 ){
            Log.e(TAG, "loadKBPK failed");
            return ret;
        }
        return ret;
    }

    protected int T_loadTR31Payload( String sSlot, String block, String bundles ) throws RemoteException {

        Bundle bundle = convert( bundles, null );
        int ret = ikld.loadTR31Payload( Integer.valueOf(sSlot), block, bundle );
        if( ret < 0 ){
            Log.e(TAG, "keyStoreTR31Payload failed");
            return ret;
        }
        return ret;
    }

*/
}
