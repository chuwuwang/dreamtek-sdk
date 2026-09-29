package moudles.newModules;

import android.os.RemoteException;

/**
 * Created by Simon on 2021/8/2
 */
class LedModule extends TestModule {

    public String T_turnOn(String led) {
        try {
            logUtils.addCaseLog("turnOn execute");
            iledDriver.turnOn( Integer.valueOf(led));
//            this.printLedInfo(led, 1);
        } catch (RemoteException e) {
            logUtils.addCaseLog("turnOn Perform abnormal");
            e.printStackTrace();
        }
        return null;
    }


    public String T_turnOff(String led) {
        try {
            logUtils.addCaseLog("turnOff execute");
            iledDriver.turnOff( Integer.valueOf(led));
            this.printLedInfo(Integer.valueOf(led), 0);
        } catch (RemoteException e) {
            logUtils.addCaseLog("turnOff Perform abnormal");
            e.printStackTrace();
        }
        return null;
    }

    public String T_ledControl(byte led, byte status) {
        try {
            logUtils.addCaseLog("ledControl execute");
            iledDriver.ledControl(led,  status);
            this.printLedInfo(led, status);
        } catch (RemoteException e) {
            logUtils.addCaseLog("ledControl execute abnormal");
            e.printStackTrace();
        }
        return null;
    }

    private void printLedInfo(int led, int status){
        String msg = "";
        if(led == 1){
            msg = "Execution result: blue light";
        } else if(led == 2){
            msg = "Execution result: yellow light";
        } else if(led == 3){
            msg = "Execution result: green light";
        } else if(led == 4){
            msg = "Execution result: red light";
        }

        if(status == 1){
            this.printMsgTool(msg + "Light up");
        } else if(status == 0){
            this.printMsgTool(msg + "Put out");
        } else if(status == 2){
            this.printMsgTool(msg + "flashing");
        }
    }

}
