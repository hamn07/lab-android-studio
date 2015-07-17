package com.example.relativelayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}
	public void startMain(View v){
		
		Intent it = new Intent();
		it.setClass(this, MainActivity.class);
		
		startActivity(it);
		
		finish();
		
	}
}
