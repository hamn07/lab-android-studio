package com.example.relativelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int UI_APPEND_HIST = 1;
	private TextView hist;
	private EditText input_number;
	private String ans;
	private	UiHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("henry", "Create");
		
		handler = new UiHandler();

		hist = (TextView) findViewById(R.id.hist);
		input_number = (EditText) findViewById(R.id.input_number);

		ans = "845";

//		Log.i("henry", ans);

	}

	private class UiHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			Log.i("henry", ""+msg.what);
			switch (msg.what) {
			case UI_APPEND_HIST:
				hist.append(input_number.getText().toString()+"\n");
				break;

			default:
				break;
			}
		}
	}
	
	public void actGuess(View v){
		
//		Log.i("henry", input_number.getText().toString());
		handler.sendEmptyMessage(UI_APPEND_HIST);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("henry", "Start");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("henry", "Resume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("henry", "Pause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("henry", "Stop");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("henry", "Restart");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("henry", "Destory");
	}

}
