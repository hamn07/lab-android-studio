package com.example.asynctasktest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv1, tv2, tv3, tv4;
	private MyTask myTask;
	private ProgressDialog dialog;
	private int ii;
	private DownloadTask dt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv1 = (TextView)findViewById(R.id.tv1);
		tv2 = (TextView)findViewById(R.id.tv2);
		tv3 = (TextView)findViewById(R.id.tv3);
		tv4 = (TextView)findViewById(R.id.tv4);
		
		((MyApp)getApplication()).setTextView(tv1);
		
		
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMax(100);
		dialog.setProgress(ii);
		
		
	}
	
	public void test1(View v){
//		dialog.show();
//		
//		myTask = new MyTask();
//		myTask.execute("test1", "test2", "test3", "test4", "test5", "test6", "test7");
		
//		Intent it = new Intent(this, MyService.class);
//		startService(it);

		dt = new DownloadTask();
		dt.execute();
		
		
	}
	public void test2(View v){
		//myTask.cancel(true);
		
		dt.cancel(false);
	}
	
	private class DownloadTask extends AsyncTask<Void, Integer, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			String ret = "download finish";
			try {
				URL url = 
					new URL("http://opendata.cwb.gov.tw/member/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-E291D06C-CF52-470E-B20A-4D1C3257FEAE");
				HttpURLConnection conn = 
						(HttpURLConnection)url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.connect();
				BufferedReader br = 
					new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line; int intLine = 0;
				while ((line = br.readLine()) != null){
					intLine++;
					publishProgress(intLine);
					Log.i("brad", line);
					
					if (isCancelled()){
						ret = "download kill";
						break;
					}
					
				}
				br.close();
			}catch(Exception ee){
				ret = "download exception";
			}
				
			return ret;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			tv1.setText("download..." + values[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tv2.setText(result);
		}
		
		@Override
		protected void onCancelled(String result) {
			super.onCancelled(result);
			tv2.setText(result);
		}
		
	}
	
	
	private class MyTask extends AsyncTask<String, Integer, String> {
		private int intCounter;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		
		@Override
		protected String doInBackground(String... names) {
			for (int i = 0; i<100; i++){
				ii = i;
				publishProgress();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
//			for (String name: names){
//				intCounter++;
//				publishProgress(intCounter, intCounter*10, intCounter*100);
//				
//				if (isCancelled()){
//					break;
//				}
//				
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//				}
//			}
//			Log.i("brad", "" + intCounter);
			return "Brad";
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tv4.setText(result);
			dialog.dismiss();
		}


		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
//			tv1.setText("OK" + values[0]);
//			tv2.setText("OK" + values[1]);
//			tv3.setText("OK" + values[2]);
			
			dialog.setProgress(ii);
			
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.i("brad", "onCancelled1");
		}

		@Override
		protected void onCancelled(String result) {
			super.onCancelled(result);
			Log.i("brad", "onCancelled2:" + result);
			dialog.dismiss();
		}
		
		
		
	}
	
	
}
