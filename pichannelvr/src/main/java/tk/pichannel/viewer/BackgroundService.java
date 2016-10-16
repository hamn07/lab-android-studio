package tk.pichannel.viewer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

    private final String url = "http://52.198.106.239/api/user/hamn07/subscription/hamn07?apiKey=key1";
    private final String urlPut = "http://52.198.106.239/api/user/hamn07/subscription/hamn07";

    private Timer timer = new Timer();
    private RequestQueue queue;

    private NotificationManager mgr;
    private int nid;



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

        mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);



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

                                sendNotice();

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
        },0,3000);

    }

    private void sendNotice(){
        Intent nextIntent = new Intent(this, ThiefAlertActivity.class);
        nextIntent.putExtra("var1", 123);


        // 用來產生一個 PendingIntent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ThiefAlertActivity.class);
        stackBuilder.addNextIntent(nextIntent);
        PendingIntent pending =
                stackBuilder.getPendingIntent(124, PendingIntent.FLAG_UPDATE_CURRENT);

        // 準備建立一個 Notification 物件實體
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.stat_sys_warning);
        builder.setTicker("有人侵入");
        builder.setLargeIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.stop));
        builder.setAutoCancel(true);
        builder.setContentInfo("Info");
        //builder.setContentText("Text:" + (int)(Math.random()*100));
        builder.setContentText("請確認是否為陌生人");
        builder.setContentTitle("有人侵入");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pending);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.buzz);
        builder.setSound(sound);
        //builder.setSound(Uri.fromFile(new File(sdroot, "aircraft006.mp3")));


        int dot = 200;      // Length of a Morse Code "dot" in milliseconds
        int dash = 500;     // Length of a Morse Code "dash" in milliseconds
        int short_gap = 200;    // Length of Gap Between dots/dashes
        int medium_gap = 500;   // Length of Gap Between Letters
        int long_gap = 1000;    // Length of Gap Between Words
        long[] pattern = {
                0,  // Start immediately
                dot, short_gap, dot, short_gap, dot,    // s
                medium_gap,
                dash, short_gap, dash, short_gap, dash, // o
                medium_gap,
                dot, short_gap, dot, short_gap, dot,    // s
                long_gap
        };

        builder.setVibrate(pattern);


        // API Level 11+
//		Notification notification = builder.getNotification();
        // API Level 16+ (4.1.2+)
        Notification notification = builder.build();

        // 發出通知了
        mgr.notify(nid++, notification);

    }

}
