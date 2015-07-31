package com.example.mylist;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lv = (ListView)findViewById(R.id.lv);
		
		LinkedList<HashMap<String,String>> data = 
				new LinkedList<HashMap<String,String>>();
		String[] from = {"brad"};
		int[] to = {R.id.item_tv};
		
		HashMap<String,String> d0 = new HashMap<String, String>();
		d0.put(from[0], "切換到另一個Activity(無傳參數)");
		data.add(d0);
		
		HashMap<String,String> d1 = new HashMap<String, String>();
		d1.put(from[0], "切換到另一個Activity(有傳參數)");
		data.add(d1);

		HashMap<String,String> d2 = new HashMap<String, String>();
		d2.put(from[0], "回傳結果狀態123");
		data.add(d2);

		HashMap<String,String> d3 = new HashMap<String, String>();
		d3.put(from[0], "回傳結果狀態456");
		data.add(d3);
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				switch (index){
					case 0:
						doItem0();
						break;
					case 1:
						doItem1();
						break;
					case 2:
						doItem2();
						break;
					case 3:
						doItem3();
						break;
				}
			}
		});
		
	}
	
	private void doItem0(){
		//Toast.makeText(this, "OK1", Toast.LENGTH_SHORT).show();
		Intent it = new Intent(this, Page1Activity.class);
		startActivity(it);
	}
	
	private void doItem1(){
//		Toast.makeText(this, "OK2", Toast.LENGTH_SHORT).show();
		Intent it = new Intent(this, Page2Activity.class);
		it.putExtra("name", "Brad");
		it.putExtra("weight", 80.5);
		it.putExtra("age", 50);
		startActivity(it);
	}
	
	private void doItem2(){
		Intent it = new Intent(this, Page3Activity.class);
		//startActivity(it);
		startActivityForResult(it, 123);
	}

	private void doItem3(){
		Intent it = new Intent(this, Page4Activity.class);
		//startActivity(it);
		startActivityForResult(it, 456);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("brad", "back here: " + requestCode);
	}
	
}
