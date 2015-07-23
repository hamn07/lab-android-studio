package tw.org.iiiedu.taichung.pichannelloadimagefromurl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
//    private String sUrl = "https://lh5.googleusercontent.com/jaJyrqFyEMSpe7hfm2Qc5_iELcWf5TYJ_aMZowxJGYM3eLcNl87vBfyaRuFbOSj_aONwJwweD7ql8GbDUW79w-rtXZ_z8NTk2cKakXttRA2MWtPYfxvTZb9J";
    private String sUrl = "http://10.0.3.2/api/user/hamn07?apiKey=key1&max-result=10";
    private Drawable drawable;
    private LoadImageHandler handler;
    private LinearLayout ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgv = (ImageView) findViewById(R.id.imgv);
        handler = new LoadImageHandler();


        new LoadImageThread().start();


        ll = (LinearLayout) findViewById(R.id.ll);
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("henry","click on LinearLayout");
                return false;
            }
        });
    }

    public void click(View v){
        Log.i("henry","click on TextView");
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
