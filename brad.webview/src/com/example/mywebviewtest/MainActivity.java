package com.example.mywebviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		handler = new MyHandler();
		
		tv = (TextView)findViewById(R.id.tv);
		webview = (WebView)findViewById(R.id.webview);
		initWebView();
		
	}
	
	@SuppressLint("JavascriptInterface")
	private void initWebView(){
		
		WebViewClient client = new WebViewClient();
		webview.setWebViewClient(client);
		
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		
		webview.addJavascriptInterface(new BradJavaScript(), "brad");
		
//		webview.loadUrl("http://tw.yahoo.com");
		
		webview.loadUrl("file:///android_asset/brad.html");
		
		//String data = "<h1>Brad Big Company</h1><hr /> 歡迎您光臨布萊德大公司 ";
		//webview.loadData(data, "text/html; charset=utf-8", null);		
	}
	
	// 將Android App --> WebView
	public void mytest1(View v){
		int time = 6;
		webview.loadUrl("javascript:test1(" + time + ")");
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
	
}
