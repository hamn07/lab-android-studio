package com.example.ntest;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class MyService extends Service {
	private NotificationManager mgr;
	private Timer timer;
	private int nid;
	private File sdroot;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		timer = new Timer();
		sdroot = Environment.getExternalStorageDirectory();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timer.schedule(new MyTask(), 10 * 1000);
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class MyTask extends TimerTask {
		@Override
		public void run() {
			sendNotice();
		}
	}
	
	private void sendNotice(){
		Intent nextIntent = new Intent(this, NoticePageActivity.class);
		nextIntent.putExtra("var1", 123);
		
		
		// 用來產生一個 PendingIntent
		TaskStackBuilder  stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(NoticePageActivity.class);
		stackBuilder.addNextIntent(nextIntent);
		PendingIntent pending = 
			stackBuilder.getPendingIntent(124, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// 準備建立一個 Notification 物件實體
		Notification.Builder builder = new Notification.Builder(this);
		builder.setTicker("超級重要的通知");
		builder.setAutoCancel(true);
		builder.setSmallIcon(R.drawable.mm);
		builder.setLargeIcon(
			BitmapFactory.decodeResource(getResources(), R.drawable.mmb));
		builder.setContentInfo("Info");
		builder.setContentText("Text:" + (int)(Math.random()*100));
		builder.setContentTitle("Title");
		builder.setWhen(System.currentTimeMillis());
		builder.setContentIntent(pending);
		//builder.setSound(Uri.fromFile(new File(sdroot, "aircraft006.mp3")));
		
		
		int dot = 200;      // Length of a Morse Code "dot" in milliseconds
		int dash = 500;     // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200;    // Length of Gap Between dots/dashes
		int medium_gap = 500;   // Length of Gap Between Letters
		int long_gap = 1000;    // Length of Gap Between Words
		long[] pattern = {
		    0,  // Start immediately
		    dot, short_gap, dot, short_gap, dot,    // s
		    medium_gap,
		    dash, short_gap, dash, short_gap, dash, // o
		    medium_gap,
		    dot, short_gap, dot, short_gap, dot,    // s
		    long_gap
		};

		builder.setVibrate(pattern);
		
		
		// API Level 11+
//		Notification notification = builder.getNotification();
		// API Level 16+ (4.1.2+)
		Notification notification = builder.build();
		
		// 發出通知了
		mgr.notify(nid++, notification);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
