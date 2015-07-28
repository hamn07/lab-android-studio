package tw.org.iiiedu.taichung.bgservice;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {

    private Timer timer;
    private TelephonyManager tmgr;
    private String device_id;
    private File sdroot, approot, upadateDir, infoDir, logDir;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        timer = new Timer();
        Log.i("henry","OK");

        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        device_id = tmgr.getDeviceId();
        device_id = "henry_dev_0000";
        Log.i("henry",device_id);

        sdroot = Environment.getExternalStorageDirectory();

        approot = new File(sdroot,device_id);
        if (!approot.exists()){
            approot.mkdir();
        }

        upadateDir = new File(approot,"update");
        if (!upadateDir.exists()) {
            upadateDir.mkdir();
        }

        logDir = new File(approot,"log");
        if (!logDir.exists()) {
            logDir.mkdir();
        }

        infoDir = new File(approot,"info");
        if (!infoDir.exists()) {
            infoDir.mkdir();
        }

    }

    private class CheckTask extends TimerTask {
        @Override
        public void run() {

        }
    }
}
