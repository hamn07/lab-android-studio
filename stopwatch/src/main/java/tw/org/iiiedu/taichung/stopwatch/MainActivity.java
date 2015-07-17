package tw.org.iiiedu.taichung.stopwatch;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    private static final int BTN_START_CLICK = 1;
    private static final int CLOCK_CHANGE = 2;
    private static final int LV_CHANGE = 3;

    private TextView tv;
    private ListView lv;
    private Button btn_start;
    private Button btn_reset;

    private MyHandler handler;
    private Timer timer;
    private MyTimerTask myTimerTask;

    private boolean isRunning;
    private int intValue=0;
    private int intOrder=1;
    private LinkedList<HashMap<String,String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new MyHandler();
        timer = new Timer();

        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.lv);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        data = new LinkedList<HashMap<String, String>>();
    }

    public void start(View v){
        isRunning = !isRunning;
        handler.sendEmptyMessage(BTN_START_CLICK);

        if (isRunning){
            myTimerTask = new MyTimerTask();
            timer.schedule(myTimerTask,0,10);
        }else {
            if (myTimerTask!=null){
                myTimerTask.cancel();
            }
        }
    }

    public void reset(View v){
        if(isRunning){
            HashMap<String,String> aTap = new HashMap<String,String>();
            aTap.put("key",toClock());
            aTap.put("order","" + intOrder++);
            data.add(0, aTap);
            handler.sendEmptyMessage(LV_CHANGE);
        }else {
            intValue = 0;
            handler.sendEmptyMessage(CLOCK_CHANGE);
        }
    }

    private String toClock(){
        int ts = intValue % 100;
        int sec = intValue / 100;
        int ss = sec % 60;
        int min = sec / 60;
        int mm = min % 60;
        int hh = min / 60;
        return hh + ":" + mm + ":" + ss + "." + ts;
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BTN_START_CLICK:
                    btn_start.setText(isRunning?"Stop":"Start");
                    btn_reset.setText(isRunning?"Tap":"Reset");
                    break;
                case CLOCK_CHANGE:
                    tv.setText(toClock());
                    break;
                case LV_CHANGE:
                    Log.i("henry","LV_CHANGE");
                    SimpleAdapter adapter = new SimpleAdapter(
                            MainActivity.this,
                            data,
                            R.layout.tap,
                            new String[]{"key","order"},
                            new int[]{R.id.lap_tv,R.id.lap_order});
                    lv.setAdapter(adapter);
                    break;
            }
        }
    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            intValue++;
            handler.sendEmptyMessage(CLOCK_CHANGE);
        }
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
