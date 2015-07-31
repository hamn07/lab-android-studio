package com.example.mymaptest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class MapHandler extends Handler {
	private TextView tv;
	
	@Override
	public void handleMessage(Message msg) {
		//Log.i("brad", "MapHandler:" + msg.what);
		if (tv != null){
			tv.setText("" + msg.what);
		}
	}

	void setTextView(TextView tv){
		this.tv = tv;
	}
	
}
