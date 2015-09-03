package tk.pichannel.viewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ThiefAlertActivity extends Activity {

    private final String url = "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07/camera?apiKey=key1";

    private ImageView iv;
    private RequestQueue queue;
    private Bitmap bitmap;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thief_alert);

        iv = (ImageView) findViewById(R.id.iv_thief);
        queue = Volley.newRequestQueue(this);
        dialog = new ProgressDialog(this);



    }




    @Override
    protected void onStart() {
        super.onStart();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    queue.add(new ImageRequest(response.getString("image_src"), new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(Bitmap response) {

                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }

                            bitmap = response;

                            new Handler() {
                                @Override
                                public void handleMessage(Message msg) {

                                    iv.setImageBitmap(bitmap);

                                    Toast.makeText(ThiefAlertActivity.this,"we are on the way to catch the intruder!",Toast.LENGTH_LONG).show();


                                }
                            }.sendEmptyMessage(0);
                        }
                    }, 800, 800, null, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }));
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);


        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.show();
    }

    public void confirmAlarm(View v) {

        Toast.makeText(ThiefAlertActivity.this,"we are on the way to catch the intruder!",Toast.LENGTH_LONG).show();
    }

    public void retractAlarm(View v) {

        Toast.makeText(ThiefAlertActivity.this,"we will abort this mission, thanks for your report.",Toast.LENGTH_LONG).show();
    }
}
