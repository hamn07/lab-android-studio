package com.example.mymaptest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends Activity {
	private WebView webview;
	private TextView tv;
	private MyHandler handler;
	private MapHandler mapHandler;
	private MyReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter("bradmap");
		registerReceiver(receiver, filter);
		
		mapHandler = ((MyApp)getApplication()).getMapHandler();
		handler = new MyHandler();
		
		tv = (TextView)findViewById(R.id.tv); 
		mapHandler.setTextView(tv);
		webview = (WebView)findViewById(R.id.webview);
		initWebView();
		
	}
	
	@Override
	public void finish() {
		unregisterReceiver(receiver);
		super.finish();
	}
	
	public void mytest2(View v){
		Intent it = new Intent(this, MyService.class);
		startService(it);
	}
	public void mytest3(View v){
		Intent it = new Intent(this, MyService.class);
		stopService(it);
	}
	
	@SuppressLint("JavascriptInterface")
	private void initWebView(){
		
		WebViewClient client = new WebViewClient();
		webview.setWebViewClient(client);
		
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		
		webview.addJavascriptInterface(new BradJavaScript(), "brad");
		
//		webview.loadUrl("http://tw.yahoo.com");
		
		webview.loadUrl("file:///android_asset/mymap.html");
		
		//String data = "<h1>Brad Big Company</h1><hr /> 歡迎您光臨布萊德大公司 ";
		//webview.loadData(data, "text/html; charset=utf-8", null);		
	}
	
	// 將Android App --> WebView
	public void mytest1(View v){
		double lat = 3.866999;
		double lng = 113.771298;
		webview.loadUrl("javascript:toSomewhere(" + lat + "," + lng + ")");
	}
	
	// 將 WebView --> Android App
	public class BradJavaScript {
		@JavascriptInterface
		public void showName(String data){
			Log.i("brad", data);
			
			Message msg = new Message();
			Bundle bdata = new Bundle();
			bdata.putString("data", data);
			msg.setData(bdata);
			handler.sendMessage(msg);
		}
	}
	
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			String data = msg.getData().getString("data");
			
			tv.setText(data);
			
			Log.i("brad", "debug:" + data);
			AlertDialog dialog;
			AlertDialog.Builder builder = 
				new AlertDialog.Builder(MainActivity.this);
			builder.setMessage(data);
			dialog = builder.create();
			dialog.show();
		}
	}
	
	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			double lat = intent.getDoubleExtra("lat", 0);
			double lng = intent.getDoubleExtra("lng", 0);
			Log.i("brad", lat + " x " + lng);
			webview.loadUrl("javascript:toSomewhere(" + lat + "," + lng + ")");
		}
		
	}
	
	
	
	
}
