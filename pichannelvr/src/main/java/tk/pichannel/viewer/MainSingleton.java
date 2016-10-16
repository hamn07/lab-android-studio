package tk.pichannel.viewer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HamnLee on 2016/9/30.
 */
public class MainSingleton {
    private static MainSingleton ourInstance;
    private RequestQueue mRequestQueue;
    private Context mCtx;


    public static synchronized MainSingleton getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MainSingleton(context);
        }
        return ourInstance;
    }

    private MainSingleton(Context context) {
        mCtx = context;

        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
