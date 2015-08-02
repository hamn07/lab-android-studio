package tw.org.iiiedu.taichung.pichannelur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

//    private ImageView iv;
    private final String sUploadURL = "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07";
    private RequestQueue rQueue;
    private Bitmap bitmapFileToUpload;
    private String sFileContentToUpload;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        iv = (ImageView) findViewById(R.id.iv);
        rQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("uploading...");

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
//        Log.d("henry",imageUri.getPath());

        if (imageUri != null) {
            progressDialog.show();
            // Update UI to reflect image being shared
//            iv.setImageURI(imageUri);

            try {
                bitmapFileToUpload = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
//                iv.setImageBitmap(bitmapFileToUpload);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // POST Image to pichannel data center
            StringRequest request = new StringRequest(
                Request.Method.POST,
                sUploadURL,
                //response處理
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("henry","onResponse: "+response);
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "upload finished!", Toast.LENGTH_SHORT).show();
                        finish();
                        // Animation
//                        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.abc_fade_in);
//                        animation.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//                                Toast.makeText(MainActivity.this, "upload finished!", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                finish();
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
//                        iv.startAnimation(animation);
                    }
                },
                // error處理
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
            ){
                /**
                 * Returns a Map of parameters to be used for a POST or PUT request.  Can throw
                 * {@link AuthFailureError} as authentication may be required to provide these values.
                 * <p/>
                 * <p>Note that you can directly override {@link #getBody()} for custom data.</p>
                 *
                 * @throws AuthFailureError in the event of auth failure
                 */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    // 參數一
                    params.put("apiKey", "key1");

//                    params.put("s_file_contents",sFileContentToUpload);
                    // 參數2
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapFileToUpload.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
                    String sEncodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
                    params.put("s_file_contents",sEncodeImage);

                    return params;
                }

//                /**
//                 * Returns a list of extra HTTP headers to go along with this request. Can
//                 * throw {@link AuthFailureError} as authentication may be required to
//                 * provide these values.
//                 *
//                 * @throws AuthFailureError In the event of auth failure
//                 */
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    params.put("Content-Type","application/json");
////                    params.put("Content-Type","multipart/form-data");
//
//                    return params;
////                    return super.getHeaders();
//                }
            };
            rQueue.add(request);





//                PhotoMultipartRequest<Object> pmRequest =
//                    new PhotoMultipartRequest<Object>(sUploadURL, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("henry",error.getMessage());
//                        }
//                    }, new Response.Listener<Object>() {
//                        @Override
//                        public void onResponse(Object response) {
//                            Log.d("henry","OK:"+response);
//                        }
//                    }, new File(imageUri.getPath()));
//
//
//                rQueue.add(pmRequest);




        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }

    void convertToBase64StringFileContentToUpload(Uri uri) {








    }
    void convertToStringFileContentToUpload(Uri imageUri){
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(imageUri.getPath()));
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line=br.readLine())!=null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sFileContentToUpload = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
