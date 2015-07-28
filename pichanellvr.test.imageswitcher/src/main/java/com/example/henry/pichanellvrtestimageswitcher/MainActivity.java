package com.example.henry.pichanellvrtestimageswitcher;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

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

import java.util.Iterator;
import java.util.LinkedList;


public class MainActivity extends Activity implements ViewSwitcher.ViewFactory{

    private ImageSwitcher is;
    private Drawable drawable;
    private ImageSwitcherHandler handler;
    private RequestQueue queue;
    private LinkedList<Drawable> imageList;
    private Iterator<Drawable> imageIterator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new NetworkThread().start();



        //<-- 設定Image Switcher -->
        handler = new ImageSwitcherHandler();

        is = (ImageSwitcher) findViewById(R.id.is);
        is.setFactory(this);


        is.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.anim_scale_out));


        is.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.anim_scale_out));

        is.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                handler.sendEmptyMessage(0);

                return false;

            }
        });


        //-- 取得post array --//
        imageList = new LinkedList<Drawable>();

        queue = Volley.newRequestQueue(this);

        String url = "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07?apiKey=key1";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("henry",""+response.length());
                for ( int i=0;i<response.length();i++ ){

                    try {
                        JSONObject jsonObj = response.getJSONObject(i);
                        Log.i("henry", "json.image_src : " + jsonObj.getString("image_src"));

                        ImageRequest req = new ImageRequest(jsonObj.getString("image_src"), new Response.Listener<Bitmap>() {

                            @Override
                            public void onResponse(Bitmap response) {
                                Log.i("henry","ImageRequest.onResponse");
                                imageList.add(new BitmapDrawable(getResources(), response));
                                imageIterator = imageList.iterator();

                            }
                        }, 0, 0, null, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("henry",error.getMessage());
                            }
                        });

                        queue.add(req);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("henry",error.getMessage());
            }
        });

        queue.add(request);


    }

//    private class NetworkThread extends Thread {
//        @Override
//        public void run() {
//
//
//            InputStream inputStream = null;
//            try {
//                inputStream = (InputStream) new URL(sUrl).getContent();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            drawable = Drawable.createFromStream(inputStream, "src");
//
//
//
//        }
//    }

    private class ImageSwitcherHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            Log.i("henry", "imageList.size() = " + imageList.size());

            if (imageIterator==null) {
            }

            if (imageIterator.hasNext()){

                is.setImageDrawable(null);

                is.setImageDrawable(imageIterator.next());

            }
        }
    }

<<<<<<< HEAD
=======



>>>>>>> 2f6bd700778281d2f793e1370a7d01e35931f266
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


    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new
                ImageSwitcher.LayoutParams(
                ImageSwitcher.LayoutParams.FILL_PARENT,
                ImageSwitcher.LayoutParams.FILL_PARENT));
        return imageView;
    }
}

