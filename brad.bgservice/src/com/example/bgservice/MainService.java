package com.example.bgservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainService extends Service {
	private Timer timer;
	private TelephonyManager tmgr;
	private String device_id;
	private File sdroot, approot, updateDir, infoDir, logDir, apkFile;
	private RequestQueue queue;
	private boolean isChecking;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("brad", "Service OK");
		
		queue = Volley.newRequestQueue(this);		
		
		tmgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		device_id = tmgr.getDeviceId();
		device_id = "brad_0000";

		sdroot = Environment.getExternalStorageDirectory();
		
		approot = new File(sdroot, device_id);
		if (!approot.exists()) approot.mkdirs();
		
		updateDir = new File(approot, "update");
		logDir = new File(approot, "log");
		infoDir = new File(approot, "info");
		if (!updateDir.exists()) updateDir.mkdirs();
		if (!logDir.exists()) logDir.mkdirs();
		if (!infoDir.exists()) infoDir.mkdirs();
		
		apkFile = new File(updateDir, "brad.apk");
		
		timer = new Timer();
		timer.schedule(new CheckUpdateTask(), 1000, 5 * 1000);
	}
	
	private class CheckUpdateTask extends TimerTask {
		@Override
		public void run() {
			if (!isChecking && !apkFile.exists()){
				Log.i("brad", "checkProcess...");
				checkProcess();
			}
		}
	}
	
	private void checkProcess(){
		isChecking = true;
		StringRequest stringRequest = 
				new StringRequest(Request.Method.GET, 
						"http://10.0.3.2/check_update.php?device_id="
								+device_id+"&version=" + "2015072700", 
						new MyListener(), 
						new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {
								isChecking = false;
							}
						});
			queue.add(stringRequest);		
	}
	private class MyListener implements Response.Listener<String> {
		@Override
		public void onResponse(String resp) {
			try{
				int len = Integer.parseInt(resp);
				if (len>1024){
					new DownloadThread(len).start();;
				}else{
					isChecking = false;
				}
			}catch(Exception ee){
				isChecking = false;
			}
		}
	}	
	
	private class DownloadThread extends Thread {
		int len;
		DownloadThread(int len){
			this.len = len;
		}
		@Override
		public void run() {
			downloadVersion(len);
		}
	}
	
	
	private void downloadVersion(int len){
		byte[] buf = new byte[len];
		try {
			URL url = 
				new URL("http://10.0.3.2/" + device_id + "/update/brad.apk");
			HttpURLConnection conn = 
					(HttpURLConnection)url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.connect();
			BufferedInputStream bin = 
				new BufferedInputStream(conn.getInputStream());
			bin.read(buf);
			bin.close();
			
			File update = new File(updateDir, "brad.apk");
			FileOutputStream fout = new FileOutputStream(update);
			fout.write(buf);
			fout.flush();
			fout.close();
			Log.i("brad", "download ok");
		}catch(Exception ee){
			Log.i("brad", "download err:" + ee.toString());
		}finally {
			isChecking = false;
		}
	}

}
