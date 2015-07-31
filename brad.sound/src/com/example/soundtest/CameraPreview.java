package com.example.soundtest;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private Camera camera;
	private SurfaceHolder holder;
	private boolean isPreview;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.camera = camera;

		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		isPreview = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		if (isPreview) {
			camera.stopPreview();
		}
		Camera.Parameters param = camera.getParameters();
		param.setPictureSize(width, height);
		try {
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			//Toast.makeText(this, "無法預覽", 0).show();
		}
		camera.startPreview();
		this.isPreview = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		this.isPreview = false;
		camera.release();
	}

	// @Override
	// public void surfaceCreated(SurfaceHolder holder) {
	// Log.i("brad", "surfaceCreated");
	// }
	//
	// @Override
	// public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int
	// arg3) {
	// Log.i("brad", "surfaceChanged");
	//
	// if (isPreview){
	// camera.stopPreview();
	// }else{
	// camera.release();
	// camera = null;
	// }
	//
	// try {
	// camera.setPreviewDisplay(holder);
	// camera.startPreview();
	// isPreview = true;
	// } catch (IOException e) {
	// }
	//
	//
	// }
	//
	//
	// @Override
	// public void surfaceDestroyed(SurfaceHolder arg0) {
	// Log.i("brad", "surfaceDestroyed");
	// camera.release();
	// camera = null;
	// }

}
