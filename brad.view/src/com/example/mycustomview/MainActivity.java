package com.example.mycustomview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private MyView mv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mv = (MyView)findViewById(R.id.mv);
	}
	
	public void clear(View v){
		mv.clear();
	}
	public void undo(View v){
		mv.undo();
	}
	public void redo(View v){
		mv.redo();
	}
}
