package com.example.listview;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView list;
	private SimpleAdapter adapter;
	private boolean[] checkedItems;
	private String[] items;
	private EditText account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list_container);

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String[] from = { "name" };
		int[] to = { R.id.title };

		HashMap<String, String> d0 = new HashMap<String, String>();
		d0.put(from[0], "Henry");
		data.add(d0);

		HashMap<String, String> d1 = new HashMap<String, String>();
		d1.put(from[0], "Mary");
		data.add(d1);

		HashMap<String, String> d2 = new HashMap<String, String>();
		d2.put(from[0], "Jerry");
		data.add(d2);

		HashMap<String, String> d3 = new HashMap<String, String>();
		d3.put(from[0], "Ferry");
		data.add(d3);

		HashMap<String, String> d4 = new HashMap<String, String>();
		d4.put(from[0], "Bella");
		data.add(d4);

		HashMap<String, String> d5 = new HashMap<String, String>();
		d5.put(from[0], "Log in");
		data.add(d5);

		adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				switchTask(index);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			Log.i("Henry", "requestCode:" + requestCode + " resultCode:"
					+ resultCode + " data:" + data.getStringExtra("data"));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void switchTask(int index) {
		Intent it;
		switch (index) {
		case 0:
			it = new Intent(this, PageHenryActivity.class);
			// startActivity(it);
			startActivityForResult(it, 0);
			break;
		case 1:
			it = new Intent(this, PageMaryActivity.class);
			it.putExtra("name", "Mary");
			it.putExtra("gender", false);
			it.putExtra("age", 18);
			startActivityForResult(it, 1);
			break;
		case 2:
			Toast.makeText(this, "Hello Jerry", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			showDialogOne();
			break;
		case 4:
			showDialogTwo();

		case 5:
			showDialogThree();
		default:
			break;
		}
	}

	private void showDialogOne() {
		// AlertDialog alert;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("OK");
		// builder.setMessage("Hello Ferry");
		// builder.setCancelable(false);
		items = new String[] { "Android", "iOS", "Windows", "Others" };
		// builder.setItems(items, new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// builder.setSingleChoiceItems(items, 1, new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		checkedItems = new boolean[] { true, false, true, false };
		builder.setMultiChoiceItems(items, checkedItems,
				new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub

					}
				});
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();

				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < checkedItems.length; i++) {
					if (checkedItems[i]) {
						sb.append(items[i] + "\n");
					}
				}
				Toast.makeText(MainActivity.this, sb.toString(),
						Toast.LENGTH_LONG).show();
			}

		});
		builder.setNegativeButton("CANCEL", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});
		builder.setNeutralButton("Later", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showDialogTwo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Bella Toy");
		String[] from = { "img", "title", "desc" };
		int[] to = { R.id.main_img, R.id.main_title, R.id.main_desc };
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> t0 = new HashMap<String, Object>();
		t0.put(from[0], R.drawable.ic_launcher);
		t0.put(from[1], "green man");
		t0.put(from[2], "from android");
		data.add(t0);
		HashMap<String, Object> t1 = new HashMap<String, Object>();
		t1.put(from[0], R.drawable.ic_launcher);
		t1.put(from[1], "green man");
		t1.put(from[2], "from android");
		data.add(t1);
		HashMap<String, Object> t2 = new HashMap<String, Object>();
		t2.put(from[0], R.drawable.ic_launcher);
		t2.put(from[1], "green man");
		t2.put(from[2], "from android");
		data.add(t2);
		HashMap<String, Object> t3 = new HashMap<String, Object>();
		t3.put(from[0], R.drawable.ic_launcher);
		t3.put(from[1], "green man");
		t3.put(from[2], "from android");
		data.add(t3);

		builder.setAdapter(
				new SimpleAdapter(this, data, R.layout.toy, from, to),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showDialogThree() {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.login);
		dialog.setTitle("pure view");
		Button login_ok = (Button) dialog.findViewById(R.id.login_ok);
		account = (EditText) dialog.findViewById(R.id.login_account);
		login_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, account.getText().toString(),
						Toast.LENGTH_LONG).show();
			}
		});
		dialog.show();
	}
}
