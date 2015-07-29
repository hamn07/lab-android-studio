package com.example.mysigntest;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class MainActivity extends Activity {
	private MyView mv;
	private MyHandler handler;
	private File sdroot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sdroot = Environment.getExternalStorageDirectory();
		
		handler = new MyHandler();
		mv = (MyView)findViewById(R.id.mv);
		mv.setDrawingCacheEnabled(true);
	}
	
	@Override
	public void finish() {
		mv.getTimer().cancel();
		super.finish();
	}
	
	public void undo(View v){
		handler.sendEmptyMessage(1);
	}
	public void redo(View v){
		handler.sendEmptyMessage(2);
	}
	public void save(View v){
		Bitmap bmp = mv.getDrawingCache();
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(new File(sdroot, "mysign.jpg"));
			bmp.compress(CompressFormat.PNG, 100, fout);
		} catch (Exception e) {
		}
		
	}
	
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					mv.undo();
					break;
				case 2:
					mv.redo();
					break;
			}
			
		}
	}
	
	
}
