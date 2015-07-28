package tw.org.iiiedu.taichung.pichannelloadimagefromurl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends Activity {

    private ImageView imgv;
    private String sUrl = "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07?apiKey=key1";
    private Drawable drawable;
    private LoadImageHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgv = (ImageView) findViewById(R.id.imgv);
        handler = new LoadImageHandler();


        new LoadImageThread().start();

    }

    private class LoadImageThread extends Thread {
        @Override
        public void run() {
            try {

                InputStream is = (InputStream) new URL(sUrl).getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuilder sb = new StringBuilder();

                String line;

                while ((line=br.readLine())!=null){
                    sb.append(line);
                }


                try {
                    JSONArray json_arr = new JSONArray(sb.toString());
                    for ( int i=0;i<json_arr.length();i++ ){
                        JSONObject json = json_arr.getJSONObject(i);
                        Log.i("henry", "json.image_src : " + json.getString("image_src"));

                        InputStream is_img = (InputStream) new URL(json.getString("image_src").replace("localhost","10.0.3.2")).getContent();
                        Intent it = new Intent();

                        drawable = Drawable.createFromStream(is_img, "src");
                        handler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadImageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            imgv.setImageDrawable(drawable);

        }
    }
}
