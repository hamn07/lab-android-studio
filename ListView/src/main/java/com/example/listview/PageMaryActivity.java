package com.example.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PageMaryActivity extends Activity {
	private TextView page_mary_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_mary);
		
		page_mary_tv = (TextView)findViewById(R.id.tv_page_mary);
		Intent it = getIntent();
		String name = it.getStringExtra("name");
		int age = it.getIntExtra("age", -1);
		boolean gender = it.getBooleanExtra("gender", true);
		
		page_mary_tv.setText(name+" is "+age+" years old.("+(gender?"男":"女")+")");
	}
}
