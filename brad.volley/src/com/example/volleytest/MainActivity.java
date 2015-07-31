package com.example.volleytest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv;
	private RequestQueue queue;
	private ImageView img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView)findViewById(R.id.tv);
		img = (ImageView)findViewById(R.id.img);
		queue = Volley.newRequestQueue(this);
		
	}
	
	public void test4(View v){
		JsonObjectRequest jsObjRequest = new JsonObjectRequest
		        (Request.Method.GET, "", null, new Response.Listener<JSONObject>() {

		    @Override
		    public void onResponse(JSONObject jsonObj) {
		    		try {
						JSONArray root = jsonObj.getJSONArray("root");
						for (int i=0; i< root.length(); i++){
							//root.ge
							
						}
						
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	
		    	
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    }
		});
		queue.add(jsObjRequest);
	}
	
	public void test3(View v){
		new GetXML().start();
	}
	
	private class GetXML extends Thread {
		@Override
		public void run() {
			try {
				URL url = 
					new URL("http://opendata.cwb.gov.tw/member/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-E291D06C-CF52-470E-B20A-4D1C3257FEAE");
				HttpURLConnection conn = 
						(HttpURLConnection)url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.connect();
//				BufferedReader br = 
//					new BufferedReader(
//						new InputStreamReader(conn.getInputStream()));
//				String line;
//				while ((line = br.readLine()) != null){
//					Log.i("brad", line);
//				}
//				br.close();

				XmlPullParserFactory xmlObj = 
					XmlPullParserFactory.newInstance();
				
				XmlPullParser xmlParser =
					xmlObj.newPullParser();
				
				xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
				
				xmlParser.setInput(conn.getInputStream(), null);
				parseXMLProcess(xmlParser);
			}catch(Exception ee){
				Log.i("brad", "ee:" + ee.toString());
			}
		}
	}
	
	private void parseXMLProcess(XmlPullParser myParser){
		int event;
		String text = null;
		boolean isLocation = false;
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT){
				String name = myParser.getName();
				switch (event){
					case XmlPullParser.START_TAG:
						break;
					case XmlPullParser.TEXT:
						text = myParser.getText();
						break;
					case XmlPullParser.END_TAG:
						if (name.equals("locationName")){
							isLocation = true;
							Log.i("brad", text);
						}else if (name.equals("parameterName") && isLocation){
							isLocation = false;
							Log.i("brad", text);
						}
						break;
				}
				event = myParser.next();
			}
			
		} catch (XmlPullParserException e) {
			Log.i("brad", "e1");
		} catch (IOException e) {
			Log.i("brad", "e2: next");
		}
	}
	
	
	public void test2(View v){
		ImageRequest request = new ImageRequest(
				"https://s.yimg.com/bt/api/res/1.2/7jCuox3n91uDvZngmcF75A--/YXBwaWQ9eW5ld3M7cT04NTt3PTMxMA--/http://media.zenfs.com/zh_hant_tw/News/bcc/8c000d9115_cyj_20150724.jpg",
			    new Response.Listener<Bitmap>() {
			        @Override
			        public void onResponse(Bitmap bitmap) {
			            img.setImageBitmap(bitmap);
			        }
			    }, 0, 0, null,
			    new Response.ErrorListener() {
			        public void onErrorResponse(VolleyError error) {
			            img.setImageResource(R.drawable.ic_launcher);
			        }
			    });
		queue.add(request);
	}
	
	public void test1(View v){
		StringRequest stringRequest = 
			new StringRequest(Request.Method.GET, 
					"http://10.0.3.2/brad.html", 
					new MyListener(), 
					new MyErrListener());
		queue.add(stringRequest);
	}
	
	private class MyListener implements Response.Listener<String> {
		@Override
		public void onResponse(String resp) {
			tv.setText(resp);
		}
	}
	private class MyErrListener implements Response.ErrorListener {
		@Override
		public void onErrorResponse(VolleyError err) {
			
		}
	}
	
}
