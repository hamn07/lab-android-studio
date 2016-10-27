package tk.pichannel.viewer.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import tk.pichannel.Utilities;
import tk.pichannel.viewer.BuildConfig;
import tk.pichannel.viewer.MainSingleton;
import tk.pichannel.viewer.data.PichannelContentProvider;
import tk.pichannel.viewer.data.Post;
import tk.pichannel.viewer.data.PostTable;

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

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (int i=0;i<posts_jsonArray.length();i++) {

            try {
                JSONObject post_jsonObject = posts_jsonArray.getJSONObject(i);

                Post post = new Post.Builder()
                        .id(post_jsonObject.getString("id"))
                        .postUnixtimestampeOriginal(post_jsonObject.getString("post_time"))
                        .userId("hamn07")
                        .imageSrc(post_jsonObject.getString("image_src"))
                        .text(post_jsonObject.getString("text"))
                        .build();

                Log.i(TAG, "((id)) = "+post_jsonObject.getInt(PichannelContentProvider.Post.ID));

                downloadImageFileIfNotExists(post);

                updatePostTable(post);


//                operations.add(
//                        ContentProviderOperation.newInsert(PichannelContentProvider.Post.CONTENT_URI)
//                                .withValue(PichannelContentProvider.Post.ID, post_jsonObject.get("id"))
//                                .withValue(PichannelContentProvider.Post.POST_UNIXTIMESTAMP_ORIGINAL, post_jsonObject.get("post_time"))
//                                .withValue(PichannelContentProvider.Post.USER_ID, "hamn07")
//                                .withValue(PichannelContentProvider.Post.IMAGE_FILE_NAME, Utilities.getImageFileNameAsStringByURL(post_jsonObject.getString("image_src")))
//                                .withValue(PichannelContentProvider.Post.TEXT, post_jsonObject.getString("text"))
//                                .build());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



//        try {
//
//            Log.i(TAG,"((apply batch))");
//            ContentProviderResult[] results = mContentResolver.applyBatch(PichannelContentProvider.AUTHORITY, operations);
//
//            Log.i(TAG,"((results_length))"+results);
//
//            for (ContentProviderResult result:results) {
//                if (result.count > 0) {
//                    Log.i(TAG,"insert successfully!");
//                }
//                else
//                {
//                    Log.i(TAG,"CONFLICT_IGNORE");
//                }
//            }
//
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }






    }

    private void updatePostTable(Post post) {

        ContentValues cv = new ContentValues();
        cv.put(PostTable.COLUMN_ID,post.getId());
        cv.put(PostTable.COLUMN_POST_UNIXTIMESTAMP_ORIGINAL,post.getPostUnixtimestampeOriginal());
        cv.put(PostTable.COLUMN_USER_ID, post.getUserId());
        cv.put(PostTable.COLUMN_IMAGE_FILE_NAME, post.getImageFileName());
        cv.put(PostTable.COLUMN_TEXT, post.getText());

        mContentResolver.insert(PichannelContentProvider.Post.CONTENT_URI,cv);
    }

    private void downloadImageFileIfNotExists(final Post post) {

        if (Utilities.fileExists(getContext(),post.getImageFileName()))
            return;


        int maxWidth=0;
        int maxHeight=0;
        ImageView.ScaleType scaleType=null;
        Bitmap.Config decodeConfig=null;

        ImageRequest imageRequest = new ImageRequest(post.getImageSrc(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {

                        FileOutputStream fOut = null;

                        try {

                            fOut = getContext().openFileOutput(post.getImageFileName(), Context.MODE_PRIVATE);
                            response.compress(Bitmap.CompressFormat.JPEG,85,fOut);
                            fOut.flush();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();

                        } finally {
                            try {

                                if (fOut!=null) {
                                    fOut.close();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, maxWidth, maxHeight, scaleType, decodeConfig,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        mRequestQueue.add(imageRequest);
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
