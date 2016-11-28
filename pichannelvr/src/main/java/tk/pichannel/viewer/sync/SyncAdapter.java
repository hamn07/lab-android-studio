package tk.pichannel.viewer.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import tk.pichannel.viewer.BuildConfig;
import tk.pichannel.viewer.data.PichannelContentProvider;
import tk.pichannel.viewer.data.Post;

import static tk.pichannel.Utilities.fileExists;

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
     * Content resolver, for performing database insertOperations.
     */
    private final ContentResolver mContentResolver;

    private boolean mHasNewImageFileDownloaded;

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
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
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
        mHasNewImageFileDownloaded = false;

        try {

            JSONArray posts_jsonArray = downloadPostsData_asJsonArray(new URL(FEED_POSTS_URL));

            new PostsDataSynchronizer().syncDatabaseAndFiles(posts_jsonArray);

            if (mHasNewImageFileDownloaded) {
                mContentResolver.notifyChange(PichannelContentProvider.Post.CONTENT_URI, null, false);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray downloadPostsData_asJsonArray(URL url) throws JSONException, IOException {

        BufferedReader reader = null;
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) url.openConnection();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();

            return new JSONArray(json);



        } finally {
            conn.disconnect();
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "JSON feed closed", e);
                }
            }
        }
    }

    private class PostsDataSynchronizer {

        private static final int BUFFER_SIZE = 1024;
        ArrayList<ContentProviderOperation> insertOperations = new ArrayList<>();

        public void syncDatabaseAndFiles(JSONArray posts_jsonArray) {

            for (int i=0;i<posts_jsonArray.length();i++) {

                try {
                    JSONObject post_jsonObject = posts_jsonArray.getJSONObject(i);

                    final Post post = new Post.Builder()
                            .id(post_jsonObject.getString("id"))
                            .postUnixtimestampeOriginal(post_jsonObject.getString("post_time"))
                            .userId("hamn07")
                            .imageSrc(post_jsonObject.getString("image_src"))
                            .text(post_jsonObject.getString("text"))
                            .build();


                    if (fileExists(getContext(), post.getImageFileName())) {
                        addInsertOperation(post);
                    }
                    else {
                        Log.i(TAG,"((post content))"+post.toString());

                        downloadImageFile(post);
                        addInsertOperation(post);
                        mHasNewImageFileDownloaded = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            try {

                mContentResolver.applyBatch(PichannelContentProvider.AUTHORITY, insertOperations);

            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
        }

        private void downloadImageFile(Post post) {

            URL url = null;
            try {
                url = new URL(post.getImageSrc());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();

                InputStream in = null;
                FileOutputStream out = null;

                try {


                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();

                        out = getContext().openFileOutput(post.getImageFileName(), Context.MODE_PRIVATE);

                        int bytesRead;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }

                        out.close();
                        in.close();
                    }
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null){
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        private void addInsertOperation(Post post) {

            insertOperations.add(
                    ContentProviderOperation.newInsert(PichannelContentProvider.Post.CONTENT_URI)
                            .withValue(PichannelContentProvider.Post.ID, post.getId())
                            .withValue(PichannelContentProvider.Post.POST_UNIXTIMESTAMP_ORIGINAL, post.getPostUnixtimestampeOriginal())
                            .withValue(PichannelContentProvider.Post.USER_ID, post.getUserId())
                            .withValue(PichannelContentProvider.Post.IMAGE_FILE_NAME, post.getImageFileName())
                            .withValue(PichannelContentProvider.Post.TEXT, post.getText())
                            .build());
        }
    }
}
