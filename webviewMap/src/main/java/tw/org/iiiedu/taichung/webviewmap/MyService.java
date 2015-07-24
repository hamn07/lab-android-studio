package tw.org.iiiedu.taichung.webviewmap;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private LocationManager lmgr;
    private MyLocationListener listener;
    private MapHandler map_handler;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i("henry","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lmgr.removeUpdates(listener);
//        Log.i("henry","onDestroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i("henry","onCreate");
        map_handler = ((MyApplication) getApplication()).getHandler();

        listener = new MyLocationListener();
        lmgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
//            Log.i("henry",lat+","+lng);
//            map_handler.sendEmptyMessage(123);
            Intent it = new Intent("henrymap");
            it.putExtra("lat",lat);
            it.putExtra("lng",lng);
            sendBroadcast(it);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
