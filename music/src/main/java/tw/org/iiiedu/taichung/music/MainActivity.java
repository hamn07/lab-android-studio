package tw.org.iiiedu.taichung.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;


public class MainActivity extends ActionBarActivity {

    private SeekBar sb;
    private MyReciever reciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb = (SeekBar) findViewById(R.id.sb);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent it = new Intent(MainActivity.this,MyService.class);
                    it.putExtra("status",true);
                    it.putExtra("seekto",progress);
                    startService(it);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        reciever = new MyReciever();
        IntentFilter filter = new IntentFilter("music");
        registerReceiver(reciever,filter);
    }

    @Override
    public void finish() {
        unregisterReceiver(reciever);
        Intent intent = new Intent(this,MyService.class);
        stopService(intent);
        super.finish();
    }

    public void start(View v){
        Intent it = new Intent(this,MyService.class);
        it.putExtra("status",true);
        startService(it);
    }

    public void stop(View v){
        Intent it = new Intent(this,MyService.class);
        it.putExtra("status",false);
        startService(it);
    }

    private class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int len = intent.getIntExtra("len", -1);
            int now = intent.getIntExtra("now", -1);
            if (len!=-1){
                sb.setMax(len);
            }
            if (now!=-1){
                sb.setProgress(now);
            }
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
