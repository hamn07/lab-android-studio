package tw.org.iiiedu.taichung.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private File file_sdroot,file_music;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isStart = intent.getBooleanExtra("status",false);
        int seekto = intent.getIntExtra("seekto",0);
        if(isStart){
            if (seekto!=0){
                mediaPlayer.seekTo(seekto);
            } else {
                mediaPlayer.start();
                mediaPlayer.setVolume(0.5f,0.5f);
            }
        }else {
            mediaPlayer.pause();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        timer.schedule(new MyTask(),0,100);

        file_sdroot = Environment.getExternalStorageDirectory();
        file_music = new File(file_sdroot,"golden age of country music.mp3");

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file_music.getAbsolutePath());
            mediaPlayer.prepare();

            Intent it = new Intent("music");
            it.putExtra("len",mediaPlayer.getDuration());
            sendBroadcast(it);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
                Intent it = new Intent("music");
                it.putExtra("now", mediaPlayer.getCurrentPosition());
                sendBroadcast(it);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer=null;
        }
        if (timer!=null){
            timer.cancel();
            timer=null;
        }

        super.onDestroy();
    }
}
