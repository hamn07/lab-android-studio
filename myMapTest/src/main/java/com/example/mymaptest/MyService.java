package com.example.mymaptest;

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
	private MyGPSListener listener;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lmgr = (LocationManager)getSystemService(LOCATION_SERVICE);
		listener = new MyGPSListener();
		lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
				0, 0, listener);
		
	}
	
	private class MyGPSListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			Log.i("brad", lat + " x " + lng);
//			sendBroadcast(intent);
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		lmgr.removeUpdates(listener);
	}

//	@Override
//	public void onStart(Intent intent, int startId) {
//		super.onStart(intent, startId);
//	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("brad", "Service: onStartCommand");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	

}
