package tk.pichannel.viewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class MainActivity extends Activity {

    private ImageView iv;
    private ImageViewSwitchHandler handler;
    private Drawable drawableNextImage;
    private ServiceBroadcastReceiver receiver;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new ImageViewSwitchHandler();

        receiver = new ServiceBroadcastReceiver();
        registerReceiver(receiver,new IntentFilter("nextImageLoaded"));


        iv = (ImageView) findViewById(R.id.iv);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.show();
//        drawableNextImage = ((App)getApplication()).getDrawableNextImage();

        startService(new Intent(this, MainService.class));


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

            Log.i("henry", "handle");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/cache-image.jpg", options);
            iv.setImageBitmap(bitmap);


//            File f = new File(Environment.getExternalStorageDirectory(), "cache-image.jpg");
//            Log.i("henry",""+f.exists());

            iv.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale));

        }
        /**
         * 以最省記憶體的方式讀取本地資源的圖片
         * @param context
          @param resId
          @return
         */
        public Bitmap readBitMap(Context context, int resId){
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            //獲取資源圖片
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is,null,opt);
        }
    }

    private class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("henry","onReceive");


//            Bundle bundle = new Bundle();
//            bundle.putParcelable("image", intent.getParcelableExtra("image"));
//            Message msg = new Message();
//            msg.setData(bundle);
//            handler.sendMessage(msg);
            handler.sendEmptyMessage(0);
        }
    }


}
