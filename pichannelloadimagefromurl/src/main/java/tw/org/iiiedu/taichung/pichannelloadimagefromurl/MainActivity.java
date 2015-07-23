package tw.org.iiiedu.taichung.pichannelloadimagefromurl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class MainActivity extends Activity {

    private ImageView imgv;
    private String sUrl = "https://lh5.googleusercontent.com/jaJyrqFyEMSpe7hfm2Qc5_iELcWf5TYJ_aMZowxJGYM3eLcNl87vBfyaRuFbOSj_aONwJwweD7ql8GbDUW79w-rtXZ_z8NTk2cKakXttRA2MWtPYfxvTZb9J";
    private Drawable drawable;
    private LoadImageHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgv = (ImageView) findViewById(R.id.imgv);
        handler = new LoadImageHandler();

        new LoadImageThread().start();
    }

    private class LoadImageThread extends Thread {
        @Override
        public void run() {
            try {
                InputStream is = (InputStream) new URL(sUrl).getContent();
                drawable = Drawable.createFromStream(is, "src");
                handler.sendEmptyMessage(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadImageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            imgv.setImageDrawable(drawable);

        }
    }
}
