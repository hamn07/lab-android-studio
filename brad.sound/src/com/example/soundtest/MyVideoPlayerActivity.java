package com.example.soundtest;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;

public class MyVideoPlayerActivity extends Activity {
	private VideoView vv;
	private File sdroot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_video_player);
		
		vv = (VideoView)findViewById(R.id.vv);
		
		sdroot = Environment.getExternalStorageDirectory();
		vv.setVideoPath(new File(sdroot, "bradok.mp4").getAbsolutePath());
		vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.aircraft006));
		vv.setVideoURI(Uri.parse("http://xxxxxxx/brad.mp4"));
		
		vv.requestFocus();
		vv.start();
//		vv.pause();
//		vv.stopPlayback();
		
		
		
	}
}
