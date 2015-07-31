package com.example.myiotest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private File sdroot, approot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView)findViewById(R.id.tv);
		
		sp = getSharedPreferences("gamedata", MODE_PRIVATE);
		editor = sp.edit();
		
//		tv.setText(Environment.getExternalStorageState());
		sdroot = Environment.getExternalStorageDirectory();
//		tv.setText(sdroot.getAbsolutePath());

		approot = new File(sdroot, "Android/data/" + getPackageName());
		if (!approot.exists()){
			approot.mkdirs();
		}
		
	}
	
	public void iosave1(View v){
		editor.putBoolean("sound", true);
		editor.putInt("stage", 6);
		editor.putString("username", "Brad");
		editor.commit();
	}
	public void iorestore1(View v){
		String username = sp.getString("username", "nobody");
		int stage = sp.getInt("stage", 0);
		boolean sound = sp.getBoolean("sound", false);
		tv.setText("UserName:" + username + "\n" +
					"Stage: " + stage + "\n" + 
					"Sound: " + (sound?"On":"Off"));
		
	}
	
	public void iosave2(View v){
		try {
			FileOutputStream fout = openFileOutput("brad.ok1", MODE_PRIVATE);
			fout.write("Hello, Brad".getBytes());
			fout.flush();
			fout.close();
		} catch (Exception e) {
		}
	}
	public void iorestore2(View v){
		try {
			FileInputStream fin = openFileInput("brad.ok1");
			BufferedReader br = 
				new BufferedReader(new InputStreamReader(fin));
			tv.setText(br.readLine());
			fin.close();
		} catch (Exception e) {
		}
		
		
	}
	
	public void iosave3(View v){
		File file1 = new File(approot, "file1.txt");
		try{
			FileOutputStream fout = new FileOutputStream(file1);
			fout.flush();
			fout.close();
		}catch(Exception ee){}
		
		File file2 = new File(sdroot, "file2.txt");
		try{
			FileOutputStream fout = new FileOutputStream(file2);
			fout.flush();
			fout.close();
		}catch(Exception ee){}
		
	}
	public void iorestore3(View v){
	}
	
}
