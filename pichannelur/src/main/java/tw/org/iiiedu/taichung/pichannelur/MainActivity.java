package tw.org.iiiedu.taichung.pichannelur;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);

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
        if (imageUri != null) {
            // Update UI to reflect image being shared
            iv.setImageURI(imageUri);

            // POST Image to pichannel data center


            StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://ec2-52-26-138-212.us-west-2.compute.amazonaws.com/api/user/hamn07?apiKey=key1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
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
                    return super.getParams();
                }

                /**
                 * Returns a list of extra HTTP headers to go along with this request. Can
                 * throw {@link AuthFailureError} as authentication may be required to
                 * provide these values.
                 *
                 * @throws AuthFailureError In the event of auth failure
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("Content-Type","multipart/form-data");

                    return params;
//                    return super.getHeaders();
                }
            };

            // Animation
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Toast.makeText(MainActivity.this, "upload finished!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            iv.startAnimation(animation);
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
}
