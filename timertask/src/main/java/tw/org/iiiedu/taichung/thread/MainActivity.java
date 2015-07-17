package tw.org.iiiedu.taichung.thread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;


public class MainActivity extends Activity {

    private TextView tv;
    private MyHandler handler;
    private Timer timer;
    private int intCount=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        handler = new MyHandler();
        timer = new Timer();
    }

    public void click(View v){
//        MyThread myThread = new MyThread();
//        myThread.start();

        timer.schedule(new MyTask(),1000,500);
    }
    @Override
    public void finish(){
        timer.purge();
        timer.cancel();
        timer=null;
        super.finish();
    }

    private class MyThread extends Thread{
        @Override
        public void run() {
            for (int i=1;i<=10;i++){
                Log.i("henry", "timer:" + i);
            }
        }
    }

    private class MyTask extends TimerTask{
        @Override
        public void run() {
            Log.i("henry", "timer:" + intCount);
            handler.sendEmptyMessage(intCount++);
        }
    }



    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            tv.setText(""+msg.what);
        }
    }
}
