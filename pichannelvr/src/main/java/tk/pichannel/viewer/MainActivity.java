package tk.pichannel.viewer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private ImageView iv;
    private ImageViewSwitchHandler handler;
    Drawable draw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new ImageViewSwitchHandler();

        iv = (ImageView) findViewById(R.id.iv);

        new Timer().schedule(new ImageViewSwitchTask(),500,7000);
//        new Thread(new ImageViewSwitchThread()).start();
    }

    private class ImageViewSwitchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            iv.setImageDrawable(draw);
            iv.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale));

        }
    }
}
