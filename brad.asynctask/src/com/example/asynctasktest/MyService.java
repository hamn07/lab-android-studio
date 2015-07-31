package com.example.asynctasktest;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class MyService extends Service {
	private TextView tv;
	private MyTask myTask;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//tv = ((MyApp)getApplication()).getTextView();
		
		tv.setText("Karen");
		
		//myTask = new MyTask();
		//myTask.execute();
		
	}
	
	private class MyTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			publishProgress();
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			tv.setText("Brad");
		}
		
	}
	
	
	
}
