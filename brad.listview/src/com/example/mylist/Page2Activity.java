package com.example.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Page2Activity extends Activity {
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page2);
		
		tv = (TextView)findViewById(R.id.page2_tv);
		
		Intent it = getIntent();
		String name = it.getStringExtra("name");
		int age = it.getIntExtra("age", 0);
		double weight = it.getDoubleExtra("weight", 0);
		
 		tv.setText("Name: " + name + "\n" + 
 					"Age: " + age + "\n" + 
 					"Weight: " + weight);	
		
		
	}
}
