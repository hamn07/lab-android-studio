package tk.pichannel.viewer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainService extends Service {

    private final String url = "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07?apiKey=key1";
//    private final String url = "http://192.168.43.90/api/user/hamn07?apiKey=key1";

    private Timer timer = new Timer();
    private RequestQueue queue;
    private JSONArray jsonArrayPosts;
    private int intPostsPosition = 0;
    private MediaPlayer musicPlayer;


    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        queue = Volley.newRequestQueue(this);
        musicPlayer = new MediaPlayer();


        //-- 撥放音樂
        Uri musicUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.music);

        try {
            musicPlayer.setDataSource(this,musicUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            musicPlayer.prepare();
            musicPlayer.setLooping(true);
            musicPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //-- /撥放音樂



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 檢查是否有新post上傳
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                // 取得jsonArrayPosts
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        synchronized (this) {

                            if (jsonArrayPosts==null) {
                                jsonArrayPosts = response;
                                intPostsPosition = 0;
                                return;
                            }

                            if (!response.toString().equals(jsonArrayPosts.toString())) {
                                jsonArrayPosts = response;
                                intPostsPosition = 0;
                            }
//                    Log.i("henry",jsonArrayPosts.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(request);
            }
        },0,10000);

        // 設定schedule task to traverse jsonArrayPosts
        timer.schedule(new ImageViewSwitchTask(), 0, 6000);


        return super.onStartCommand(intent, flags, startId);
    }

    private class ImageViewSwitchTask extends TimerTask {
        @Override
        public void run() {
            // jsonArrayPosts還沒讀進來就直接離開
            if (jsonArrayPosts==null) {
                return;
            }
//            Log.i("henry",intPostsPosition+" of "+jsonArrayPosts.length());


            // jsonArrayPosts還有資料時，讀取下一張圖片
            if (jsonArrayPosts.length()>intPostsPosition) {
                try {
                    JSONObject jsonObjectPost = jsonArrayPosts.getJSONObject(intPostsPosition);
//                    Log.i("henry", "image_src=" + jsonObjectPost.getString("image_src"));

                    ImageRequest imageRequest = new ImageRequest(jsonObjectPost.getString("image_src"), new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(Bitmap response) {
//                            Log.i("henry","onResponse");

                            try {
                                //將圖片儲存於內部package空間
                                FileOutputStream fos = openFileOutput("cache-image.jpg",MODE_PRIVATE);
//                                Log.i("henry","fos result = "+response.compress(Bitmap.CompressFormat.JPEG,85,fos));
                                response.compress(Bitmap.CompressFormat.JPEG,85,fos);
                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            Intent it = new Intent("nextImageLoaded");
                            sendBroadcast(it);

                        }
                    }, 800, 800, null, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(imageRequest);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // jsonArrayPosts陣列位置+1
                intPostsPosition++;

                // looping jsonArrayPosts
                if (intPostsPosition==jsonArrayPosts.length()){
                    intPostsPosition = 0;
                }
            }
        }
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        if (musicPlayer!=null){
            if (musicPlayer.isPlaying()){
                musicPlayer.stop();
            }
            musicPlayer.release();
            musicPlayer=null;
        }
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
        super.onDestroy();

    }
}
