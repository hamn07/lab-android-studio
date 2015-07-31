package com.example.soundtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private SoundPool sp;
	private int sound1, sound2;
	private MediaRecorder mr;
	private File sdroot;
	private ImageView img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		img = (ImageView)findViewById(R.id.img);
		
		sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		sound1 = sp.load(this, R.raw.aircraft006, 1);
		sound2 = sp.load(this, R.raw.aircraft010, 1);
		
		sdroot = Environment.getExternalStorageDirectory();
		
	}
	
	public void sound1(View v){
		sp.play(sound1, 0.5f, 0.5f, 0, 0, 1f);
	}
	public void sound2(View v){
		sp.play(sound2, 0.5f, 0.5f, 0, 0, 1f);
	}
	public void rec1(View v){
		Intent it = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		startActivityForResult(it, 12);
	}
	public void rec2(View v){
		mr = new MediaRecorder();
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);
		mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mr.setOutputFile(new File(sdroot,"bradtest.3gp").getAbsolutePath());
		
		try {
			mr.prepare();
			mr.start();
		} catch (Exception e) {
		}
	}
	
	public void rec3(View v){
		if (mr != null){
			mr.stop();
			mr.release();
			mr = null;
		}
	}
	
	public void video1(View v){
		Intent it = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(it, 13);
		
	}
	public void video2(View v){
		Intent it = new Intent(this, MyVideoActivity.class);
		startActivityForResult(it, 444);
		
	}
	
	public void camera1(View v){
		Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(it, 77);
		
	}

	public void camera2(View v){
		Intent it = new Intent(this, MyCameraActivity.class);
		startActivity(it);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Log.i("brad", "result: " + resultCode);
//		Log.i("brad", "" + RESULT_OK + " : " + RESULT_CANCELED );
		if (requestCode == 444){
			Intent it = new Intent(this, MyVideoPlayerActivity.class);
			startActivity(it);
		}
		
		
		if (requestCode == 77 && resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			Bitmap bmp = (Bitmap)bundle.get("data");
			img.setImageBitmap(bmp);
			
			FileOutputStream fout;
			try {
				fout = new FileOutputStream(new File(sdroot, "brad.jpg"));
				bmp.compress(CompressFormat.JPEG, 85, fout);
			} catch (Exception e) {
			}
			
		
		}else if (resultCode == RESULT_OK){
			Uri uri = data.getData();
			Log.i("brad", "Uri: " + uri.getPath());
			Log.i("brad", getRealPathFromURI(this, uri));
		}
	}
	
	public static String getRealPathFromURI(Context c, Uri contentUri) {
	       String[] proj = { MediaStore.Audio.Media.DATA };
	       Cursor cursor = c.getContentResolver().query(contentUri, proj, null,
	    		   null, null);
	       int column_index = cursor
	             .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
	       cursor.moveToFirst();
	       return cursor.getString(column_index);
	 }	
	
	
}
