package tk.pichannel.viewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends Activity {

    private ImageView iv;
    private TextView tv;
    private ImageViewSwitchHandler handler;
    private ServiceBroadcastReceiver receiver;
    private ProgressDialog dialog;
    private Animation scaleAnimation;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- instantiate member
        handler = new ImageViewSwitchHandler();
        iv = (ImageView) findViewById(R.id.iv);
        tv = (TextView) findViewById(R.id.tv);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
        dialog = new ProgressDialog(this);

        // 註冊Receiver
        receiver = new ServiceBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("nextImageLoaded"));


    }

    @Override
    protected void onStart() {
        super.onStart();

        // 啟動服務
        startService(new Intent(this, MainService.class));
        //startService(new Intent(this, BackgroundService.class));


        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MainService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private class ImageViewSwitchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (dialog.isShowing()){
                dialog.dismiss();
            }

//            Log.i("henry", "handle");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/cache-image.jpg", options);

            iv.setImageBitmap(bitmap);
//            frameLayout.startAnimation(scaleAnimation);
            iv.startAnimation(scaleAnimation);

        }
    }

    private class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.i("henry","onReceive");

            handler.sendEmptyMessage(0);
        }
    }
}
