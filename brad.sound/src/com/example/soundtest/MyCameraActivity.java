package com.example.soundtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class MyCameraActivity extends Activity {
	private FrameLayout frame;
	private Camera camera;
	private CameraPreview preview;
	private File sdroot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_camera);
		
		sdroot = Environment.getExternalStorageDirectory();
		
		frame = (FrameLayout)findViewById(R.id.flayout);
		
		camera = Camera.open();
		preview = new CameraPreview(this, camera);
		frame.addView(preview);
	}
	
	public void ok(View v){
		camera.takePicture(new MyShutter(), null, new MyPicCallBack());
		
	}
	
	private class MyShutter implements ShutterCallback {
		@Override
		public void onShutter() {
			Log.i("brad", "onShutter");
			
		}
	}
	
	private class MyPicCallBack implements PictureCallback {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pic = new File(sdroot, "mypic.jpg");
			try {
				FileOutputStream fout = new FileOutputStream(pic);
				fout.write(data);
				fout.flush();
				fout.close();
			} catch (Exception e) {
			}
			
			camera.startPreview();
			
			
			
		}
		
	}
	
	
}
