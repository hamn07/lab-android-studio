package com.example.asynctasktest;

import android.app.Application;
import android.widget.TextView;

public class MyApp extends Application {
	private TextView tv;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	void setTextView(TextView tv){
		this.tv = tv;
	}
	
	TextView getTextView(){
		return tv;
	}
	
}
