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
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddNewActivity extends Activity {
	private EditText account, passwd, cname;
	private MyApp myApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);
		
		myApp = (MyApp)getApplication();
		
		account = (EditText)findViewById(R.id.account);
		passwd = (EditText)findViewById(R.id.passwd);
		cname = (EditText)findViewById(R.id.cname);
		
	}
	
	public void addnew(View v){
		new AddNewThread().start();
	}
	
	private class AddNewThread extends Thread {
		@Override
		public void run() {
			DefaultHttpClient client = myApp.getHttpClient();
		  	HttpGet get = new HttpGet("http://10.0.3.2/addnew.php?" +
		  			"account=" + account.getText().toString() + "&" + 
		  			"passwd=" + passwd.getText().toString() + "&" + 
		  			"cname=" + cname.getText().toString()
			);
		  	try{
				HttpResponse resp = client.execute(get);
				
				// 4. return
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				BufferedReader br = 
					new BufferedReader(new InputStreamReader(in));
				String line = br.readLine();
				Log.i("brad", line);
		  	}catch (Exception ee){
				Log.i("brad", ee.toString());
		  	}
			
			
		}
	}
	
}
