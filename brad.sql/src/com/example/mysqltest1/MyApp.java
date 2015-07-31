package com.example.mysqltest1;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;

public class MyApp extends Application {
	private DefaultHttpClient client;
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	private void init(){
		client = new DefaultHttpClient();
	}
	
	DefaultHttpClient getHttpClient(){ return client;}
	
}
