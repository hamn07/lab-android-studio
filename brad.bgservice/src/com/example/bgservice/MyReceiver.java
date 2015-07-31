package com.example.bgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		Log.i("brad", "Rec OK");
		Intent it = new Intent(context, MainService.class);
		context.startService(it);
	}

}
