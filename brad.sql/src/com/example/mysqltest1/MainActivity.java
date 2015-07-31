package com.example.mysqltest1;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {
	private ListView lv;
	private SimpleAdapter adapter;
	private Dialog dialogLogin;
	private MyApp myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.activity_main);
		// lv = (ListView)findViewById(R.id.lv);

		myApp = (MyApp)getApplication();
		
		lv = getListView();

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String[] from = { "food" };
		int[] to = { R.id.title };

		HashMap<String, String> d0 = new HashMap<String, String>();
		d0.put(from[0], "新增會員資料(GET)");
		data.add(d0);

		HashMap<String, String> d1 = new HashMap<String, String>();
		d1.put(from[0], "會員登入");
		data.add(d1);

		HashMap<String, String> d2 = new HashMap<String, String>();
		d2.put(from[0], "進入到main page");
		data.add(d2);

		HashMap<String, String> d3 = new HashMap<String, String>();
		d3.put(from[0], "進入到page2(必須登入先)");
		data.add(d3);

		HashMap<String, String> d4 = new HashMap<String, String>();
		d4.put(from[0], "新增會員(POST)");
		data.add(d4);

		HashMap<String, String> d5 = new HashMap<String, String>();
		d5.put(from[0], "Upload");
		data.add(d5);

		adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				switch(index){
					case 0:
						addnew();
						break;
					case 1:
						login();
						break;
					case 2:
						gotoMain();
						break;
					case 3:
						gotoPage2();
						break;
					case 4:
						addnew2();
						break;
					case 5:
						upload();
						break;
				}
			}
		});
	}
	
	private void addnew(){
		Intent it = new Intent(this, AddNewActivity.class);
		startActivity(it);
	}
	
	private void login(){
		dialogLogin  = new Dialog(this);
		dialogLogin.setContentView(R.layout.login);
		dialogLogin.setTitle("Login Dialog");
		dialogLogin.setCancelable(false);
		
		Button login = (Button)dialogLogin.findViewById(R.id.login_login);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText account = 
					(EditText)dialogLogin.findViewById(R.id.login_account);
				EditText passwd = 
						(EditText)dialogLogin.findViewById(R.id.login_passwd);
				checkAccount(account.getText().toString(), 
						passwd.getText().toString());
				dialogLogin.dismiss();
			}
		});
		Button cancel = (Button)dialogLogin.findViewById(R.id.login_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogLogin.dismiss();
			}
		});
		
		
		dialogLogin.show();
	}
	
	private void checkAccount(String account, String passwd){
		new CheckThread(account,passwd).start();
	}
	private class CheckThread extends Thread {
		private String account, passwd;
		CheckThread(String account, String passwd){
			this.account = account;
			this.passwd = passwd;
		}
		@Override
		public void run() {
			DefaultHttpClient client = myApp.getHttpClient();
		  	HttpGet get = new HttpGet("http://10.0.3.2/check.php?" +
		  			"account=" + account + "&" + 
		  			"passwd=" + passwd );
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
	
	private void gotoMain(){
		new GotoMainThread().start();
	}
	private class GotoMainThread extends Thread {
		@Override
		public void run() {
			DefaultHttpClient client = myApp.getHttpClient();
			
		  	HttpGet get = new HttpGet("http://10.0.3.2/main.php");
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
	
	private void gotoPage2(){
		Intent it = new Intent(this,Page2Activity.class);
		startActivity(it);
	}
	
	private void addnew2(){
		new Thread(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				DefaultHttpClient client = myApp.getHttpClient();
				HttpPost post = new HttpPost("http://10.0.3.2/addnew2.php");
				
				try {
					// 4.2.5 HttpClient
					StringBody account = new StringBody("Mary");
					StringBody passwd = new StringBody("22222");
					
					MultipartEntity entity = new MultipartEntity();
					entity.addPart("account", account);
					entity.addPart("passwd", passwd);
					
					post.setEntity(entity);
					
					// 4.5 HttpClient
//					MultipartEntityBuilder builder =  MultipartEntityBuilder.create();
//					builder.addTextBody("account", "Tony");
//					builder.addTextBody("passwd", "1111");
//					HttpEntity entity = builder.build();
//					post.setEntity(entity);

					client.execute(post);
					
				} catch (Exception e) {
					Log.i("brad", "post err");
				}
				
				
			}
		}.start();
	}
	
	private void upload(){
		new Thread(){
			public void run() {
				DefaultHttpClient client = myApp.getHttpClient();
				HttpPost post = new HttpPost("http://10.0.3.2/upload.php");
				
				try {
					File sdroot = Environment.getExternalStorageDirectory();
					// 4.2.5 HttpClient
					FileBody upload = new FileBody(new File(sdroot, "pchome.pdf"));
					
					MultipartEntity entity = new MultipartEntity();
					entity.addPart("upload", upload);
					
					post.setEntity(entity);
					
					client.execute(post);
					
				} catch (Exception e) {
					Log.i("brad", "post err");
				}				
			}
		}.start();
	}
	
}













