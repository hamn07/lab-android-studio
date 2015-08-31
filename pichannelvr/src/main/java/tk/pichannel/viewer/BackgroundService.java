package tk.pichannel.viewer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

//    private final String url = "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07?apiKey=key1";
    private final String url = "http://192.168.1.10/api/user/hamn07/subscription/hamn07?apiKey=key1";
    private final String urlPut = "http://192.168.1.10/api/user/hamn07/subscription/hamn07";

    private Timer timer = new Timer();
    private RequestQueue queue;


    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        queue = Volley.newRequestQueue(this);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if(response.getBoolean("has_new_posts"))
                            {
                                Log.d("henry","true");
//                                StringRequest req = new StringRequest());
                                StringRequest request = new StringRequest(Request.Method.PUT, urlPut, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("henry",response);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> params = new HashMap<String, String>();
                                        // 參數一
                                        params.put("apiKey", "key1");

                                        // 參數2
                                        params.put("flag", "0");

                                        return params;
                                    }
                                };

                                queue.add(request);
                            }
                            else {
                                Log.d("henry","false");
                            }
                        } catch (JSONException e) {
                            Log.e("henry",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(request);
            }
        },0,30000);

    }
}
