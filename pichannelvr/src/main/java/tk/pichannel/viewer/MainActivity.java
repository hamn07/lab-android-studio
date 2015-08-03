package tk.pichannel.viewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;



public class MainActivity extends Activity {

    private ImageView iv;
    private ImageViewSwitchHandler handler;
    private ServiceBroadcastReceiver receiver;
    private ProgressDialog dialog;
    private Animation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- instantiate member
        handler = new ImageViewSwitchHandler();
        iv = (ImageView) findViewById(R.id.iv);
        scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);

        // 註冊Receiver
        receiver = new ServiceBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("nextImageLoaded"));

        // 啟動服務
        startService(new Intent(this, MainService.class));

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    public void finish() {
        unregisterReceiver(receiver);
        stopService(new Intent(this,MainService.class));
        super.finish();

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
