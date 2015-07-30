package com.example.ntest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NoticePageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_page);
		
		Intent it1 = getIntent();
		int var1 = it1.getIntExtra("var1", 0);
		Log.i("brad", "" + var1);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("brad", "here 2");
		
	}
	
}
