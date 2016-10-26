package tk.pichannel.viewer.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.pichannel.viewer.BuildConfig;
import tk.pichannel.viewer.MainSingleton;
import tk.pichannel.viewer.data.PichannelContentProvider;

/**
 * Created by HamnLee on 2016/10/20.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";

    /**
     * URL to fetch content from during a sync.
     *
     */
    private static final String FEED_POSTS_URL = BuildConfig.API_HOST+ BuildConfig.API_USER_ENDPOINT
            +"/hamn07?apiKey=key1";
    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    private final RequestQueue mRequestQueue;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[] {
            PichannelContentProvider.Post._ID,
            PichannelContentProvider.Post.ID,
            PichannelContentProvider.Post.POST_UNIXTIMESTAMP_ORIGINAL,
            PichannelContentProvider.Post.USER_ID,
            PichannelContentProvider.Post.IMAGE_FOLDER_NAME,
            PichannelContentProvider.Post.IMAGE_FILE_NAME,
            PichannelContentProvider.Post.TEXT,
    };

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        mRequestQueue = MainSingleton.getInstance(context).getRequestQueue();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mRequestQueue = MainSingleton.getInstance(context).getRequestQueue();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link android.content.AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");

        downloadPosts_jsonArray(new IVolleyCallback() {

            @Override
            public void onSuccessResponse(JSONArray response) {
                synchronizeLocalPostData(response);
            }
        });
    }

    private void synchronizeLocalPostData(JSONArray posts_jsonArray) {

        for (int i=0;i<posts_jsonArray.length();i++) {
            try {
                JSONObject post_jsonObject = posts_jsonArray.getJSONObject(i);
                Log.i(TAG, "((id)) = "+post_jsonObject.getInt(PichannelContentProvider.Post.ID));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        downloadImageFileIfNotExists();
//        updatePostTable();
    }

    private void downloadPosts_jsonArray(final IVolleyCallback callback) {

        JsonArrayRequest posts_jsonArray_request = new JsonArrayRequest
                (Request.Method.GET, FEED_POSTS_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e + "error", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(posts_jsonArray_request);
    }
}
