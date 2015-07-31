package com.example.soundtest;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class MyVideoActivity extends Activity implements Callback {
	private SurfaceView sv;
	private SurfaceHolder holder;
	private MediaRecorder mr;
	private Camera camera;
	private File sdroot;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_video);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	// 橫向
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	// 直向
		
		sv = (SurfaceView)findViewById(R.id.sv);
		holder = sv.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		sdroot = Environment.getExternalStorageDirectory();
		
		
		
		
	}
	
	private void initRecorder(Surface surface){
		if (camera == null){
			camera = Camera.open(1);
			camera.unlock();
		}
		if (mr == null ){
			mr = new MediaRecorder();
		}
		
		mr.setPreviewDisplay(surface);
		mr.setCamera(camera);
		
		mr.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mr.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
		mr.setOutputFile(new File(sdroot, "bradok.mp4").getAbsolutePath());
		mr.setVideoFrameRate(30);
		mr.setMaxDuration(-1);
		
		try {
			mr.prepare();
		} catch (Exception e) {
			Log.i("brad", e.toString());
		}
		
	}
	
	
	
	public void start(View v){
		initRecorder(holder.getSurface());
		mr.start();
		
	}
	public void stop(View v){
		mr.stop();
		mr.reset();
		mr = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (camera != null){
			camera.release();
			camera = null;
		}
	}
}
