package view;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.verifone.activity.R;
import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;
import com.dreamtek.smartpos.deviceservice.aidl.IScanner;
import com.dreamtek.smartpos.deviceservice.aidl.ScannerListener;

import base.MyApplication;
import moudles.ServiceMoudle;

public class CustomCameraActivity extends AppCompatActivity {

    ServiceMoudle serviceMoudle;
    IScanner iScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG", "oncreate...");
        setContentView(R.layout.activity_scanner);

        serviceMoudle = MyApplication.serviceMoudle;
        IDeviceService deviceService = serviceMoudle.deviceService;
        try {
            iScanner = deviceService.getScanner(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void openCustomCamera(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("customUI", true);
        bundle.putInt("x1", 0);
        bundle.putInt("y1", 0);
        bundle.putInt("width", 400);
        bundle.putInt("height", 600);
        try {
            iScanner.scannerInit(bundle);
            iScanner.startScan(bundle, 60, new ScannerListener.Stub() {
                @Override
                public void onSuccess(String barcode) throws RemoteException {
                    Log.d("TAG", "scaner success");

                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    Log.d("TAG", "scaner error");

                }

                @Override
                public void onTimeout() throws RemoteException {
                    Log.d("TAG", "scaner timout");

                }

                @Override
                public void onCancel() throws RemoteException {
                    Log.d("TAG", "scaner cannel");

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void flashOn(View view) {
        try {
            iScanner.openFlashLight(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void flashOff(View view) {
        try {
            iScanner.openFlashLight(false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void switchCamera(View view) {
        try {
            iScanner.switchScanner();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void openInnerCamera(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("customUI", false);
        try {
            iScanner.scannerInit(bundle);
            iScanner.startScan(bundle, 60, new ScannerListener.Stub() {
                @Override
                public void onSuccess(String barcode) throws RemoteException {

                }

                @Override
                public void onError(int error, String message) throws RemoteException {

                }

                @Override
                public void onTimeout() throws RemoteException {

                }

                @Override
                public void onCancel() throws RemoteException {

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
