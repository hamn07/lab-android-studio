package com.example.stopwatchtest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int UI_CHANGE_BUTTON = 1;
	private static final int UI_DISPLAY_CLOCK = 2;
	private static final int UI_UPDATE_LAP = 3;
	private TextView left, right;
	private TextView clock;
	private ListView lv;
	private boolean isStart;
	private MyHandler handler;
	private Timer timer;
	private CounterTask counterTask;
	private int intCounter;
	private LinkedList<HashMap<String,String>> data;
	private String[] from = {"lap_cont"};
	private int[] to = {R.id.lap_cont};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		timer = new Timer();
		handler = new MyHandler();
		clock = (TextView)findViewById(R.id.clock);
		left = (TextView)findViewById(R.id.left);
		right = (TextView)findViewById(R.id.right);
		lv = (ListView)findViewById(R.id.lv);
		
		data = new LinkedList<HashMap<String,String>>();
		
		isStart = false;
		intCounter = 0;
	}
	
	private class CounterTask extends TimerTask {
		@Override
		public void run() {
			intCounter++;
			handler.sendEmptyMessage(UI_DISPLAY_CLOCK);
		}
	}
	
	// Reset / Lap
	public void left(View v){
		if (isStart){
			// Lap
			HashMap<String,String> item = new HashMap<String, String>();
			item.put(from[0], toClock());
			data.add(0, item);			
			handler.sendEmptyMessage(UI_UPDATE_LAP);
		}else{
			// reset
			intCounter = 0;
			handler.sendEmptyMessage(UI_DISPLAY_CLOCK);
			data.clear();
			handler.sendEmptyMessage(UI_UPDATE_LAP);
		}
	}
	
	private void doLap(){
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.lap, from, to);
		lv.setAdapter(adapter);
	}
	
	// Start / stop
	public void right(View v){
		isStart = !isStart;
		handler.sendEmptyMessage(UI_CHANGE_BUTTON);
		if (isStart){
			counterTask = new CounterTask();
			timer.schedule(counterTask, 10, 10);
		}else{
			counterTask.cancel();
		}
	}
	
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case UI_CHANGE_BUTTON:
					if (isStart){
						left.setText("Lap");
						right.setText("Stop");
					}else{
						left.setText("Reset");
						right.setText("Start");
					}
					break;
				case UI_DISPLAY_CLOCK:
					clock.setText("" + toClock());
					break;
				case UI_UPDATE_LAP:
					doLap();
					break;
			}
			
		}
	}
	
	private String toClock(){
//		int hs = intCounter % 100;		// 小數點之後的數值
//		int temp = intCounter / 100;		// 總秒數
//		int ss = temp % 60;				// 秒數
//		temp = temp - ss;
//		int mm = temp % 60;				
//		int hh = temp / 60;
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		String showClock = df.format(new Date(intCounter*10));
		
		return showClock.substring(0, showClock.length()-1);
	}
	
	
	
	
	
	
	
}
