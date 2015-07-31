package com.example.mysqltest1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class Page2Activity extends Activity {
	private TextView tv;
	private MyHandler handler;
	private MyApp myApp;
	private String line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page2);
		
		myApp = (MyApp)getApplication();
		
		tv = (TextView)findViewById(R.id.page2_tv);
		handler = new MyHandler();
		
		new GetPage2().start();
		
	}
	
	private class GetPage2 extends Thread {
		@Override
		public void run() {
			DefaultHttpClient client = myApp.getHttpClient();
		  	HttpGet get = new HttpGet("http://10.0.3.2/page2.php");
		  	try{
				HttpResponse resp = client.execute(get);
				
				// 4. return
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = 
					new BufferedReader(new InputStreamReader(in));
				line = br.readLine();
				//Log.i("brad", line);
				handler.sendEmptyMessage(0);
		  	}catch (Exception ee){
				//Log.i("brad", ee.toString());
		  	}					
		}
	}
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			tv.setText(line);
		}
	}
	
}
