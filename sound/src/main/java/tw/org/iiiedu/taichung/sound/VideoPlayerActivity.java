package tw.org.iiiedu.taichung.sound;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

import java.io.File;


public class VideoPlayerActivity extends Activity {

    private VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        vv = (VideoView) findViewById(R.id.vv);
        vv.setVideoPath(new File(Environment.getExternalStorageDirectory(),"henry_video.mp4").getAbsolutePath());

        vv.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.ok));
        vv.setVideoURI(Uri.parse("http://....../*.mp4"));
        vv.requestFocus();
        vv.start();

    }


}
