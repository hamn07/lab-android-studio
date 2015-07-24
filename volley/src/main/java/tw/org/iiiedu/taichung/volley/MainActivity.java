package tw.org.iiiedu.taichung.volley;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    private RequestQueue queue;
    private TextView tv;
    private ImageView imgv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        queue = Volley.newRequestQueue(this);


        tv = (TextView) findViewById(R.id.tv);
        imgv = (ImageView) findViewById(R.id.imgv);




    }


    public void test1(View v) {
        StringRequest request = new StringRequest(Request.Method.GET,
                "http://10.0.3.2/phpinfo.php", new MyListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    public void test2(View v) {
        ImageRequest request = new ImageRequest("http://10.0.3.2/sample-image-music/1644159.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imgv.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imgv.setImageResource(R.drawable.abc_ab_share_pack_mtrl_alpha);
                    }
                });
        queue.add(request);
    }

    private class MyListener implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            tv.setText(response.substring(0,50));
        }
    }

    public void test3(View v) {
        new GetXML().start();
    }

    private class GetXML extends Thread {
        @Override
        public void run() {

            try {
                URL url = new URL("http://opendata.cwb.gov.tw/member/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-E291D06C-CF52-470E-B20A-4D1C3257FEAE");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setReadTimeout(10000);

                connection.setConnectTimeout(15000);

                connection.setRequestMethod("GET");

                connection.connect();

//                BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) connection.getContent()));

//                String line;
//
//                while ((line=br.readLine())!=null){
//                    Log.i("henry",line);
//                }

//                br.close();

                XmlPullParserFactory xmlObj = XmlPullParserFactory.newInstance();
                XmlPullParser xmlParser = xmlObj.newPullParser();

                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

//                xmlParser.setInput(connection.getInputStream(), null);
                xmlParser.setInput((InputStream) url.getContent(), null);

                parseXmlParser(xmlParser);


                connection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    public void test4(View v) {

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "http://opendata.cwb.gov.tw/member/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-E291D06C-CF52-470E-B20A-4D1C3257FEAE", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray root = null;
                        try {
                            root = response.getJSONArray("root");
                            for(int i=0; i< root.length(); i++) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        queue.add(jsObjRequest);
    }

    private void parseXmlParser(XmlPullParser xmlParser) {
        int event;
        String text=null;
        try {
            event = xmlParser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                String name = xmlParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("locationName")){
                            Log.i("henry",text);
                        }
                        break;
                }
                event = xmlParser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
