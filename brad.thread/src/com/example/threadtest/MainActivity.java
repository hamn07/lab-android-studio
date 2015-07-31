package com.example.threadtest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv;
	private MyThread mt1, mt2, mt3;
	private MyHandler handler;
	private Timer timer;
	private MyTask mt4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		timer = new Timer();
		
		handler = new MyHandler();
		
		tv = (TextView)findViewById(R.id.tv);
		mt1 = new MyThread("A");
//		mt2 = new MyThread("B");
//		mt3 = new MyThread("C");
		
	}
	
	public void test3(View v){
		mt4.cancel();
		
		timer.purge();
		timer.cancel();
		timer = null;
	}
	
	public void test2(View v){
		mt4 = new MyTask();
		timer.schedule(mt4, 1000, 500);
	}
	
	private class MyTask extends TimerTask {

		@Override
		public void run() {
			Log.i("brad", "OK2");
		}
		
	}
	
	
	public void test1(View v){
		mt1.start();
//		mt2.start();
//		mt3.start();
	}
	
	private class MyThread extends Thread {
		private String name;
		public MyThread(String name) {
			this.name = name;
		}
		@Override
		public void run() {
			for (int i=0; i<10; i++){
				Message mesg = new Message();
				Bundle data = new Bundle();
				data.putString("data", name + ":" + i);
				mesg.setData(data);
				handler.sendMessage(mesg);

				//tv.setText(name + ":" + i);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			String temp = data.getString("data", "XX");
			tv.setText(temp);
		}
	}
	
	
}






