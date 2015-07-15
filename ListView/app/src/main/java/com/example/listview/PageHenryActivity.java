package com.example.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PageHenryActivity extends Activity {
	private View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_henry);
		
		v = findViewById(R.id.tv_page_henry);
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
		Intent it = new Intent();
		it.putExtra("data", "III");
		setResult(123,it);
		super.finish();
	}
}
