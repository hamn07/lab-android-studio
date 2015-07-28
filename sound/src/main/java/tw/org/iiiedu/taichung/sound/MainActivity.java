package tw.org.iiiedu.taichung.sound;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;


public class MainActivity extends ActionBarActivity {


    private SoundPool sp;
    private MediaRecorder mr;
    private File sdroot;
    private SurfaceView sv;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdroot = Environment.getExternalStorageDirectory();
        imageView = (ImageView) findViewById(R.id.img);
        sv = (SurfaceView) findViewById(R.id.sv);
        sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        sp.load(this, R.raw.ok ,1);
    }

    public void sound1(View v) {
//        sp.play()
    }

    public void rec1(View v) {
        Log.i("henry","rec1");
        Intent it = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(it, 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==14&&resultCode==RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bmp = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(bmp);

            try {
                bmp.compress(Bitmap.CompressFormat.JPEG,85,new FileOutputStream(new File(sdroot,"henry_photo.jpg")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Log.i("henry",uri.toString());
            Log.i("henry",getRealPathFromURI(uri));
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void rec2(View v) {
        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mr.setOutputFile(new File(sdroot, "henry_sound.3gp").getAbsolutePath());

        try {
            mr.prepare();
            mr.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rec3(View v) {
        if (mr!=null) {
            mr.stop();
            mr.release();
        }
    }


    public void video1(View v) {
        Intent it = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(it, 13);
    }

    public void video2(View v) {
        Intent it = new Intent(this,VideoActivity.class);
        startActivity(it);
    }


    public void camera1(View v) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 14);
    }

    public void camera2(View v) {
        Intent it = new Intent(this,CameraActivity.class);
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
