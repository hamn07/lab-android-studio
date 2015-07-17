package tw.org.iiiedu.taichung.io;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private TextView tv;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        sp = getSharedPreferences("sp.dat", MODE_PRIVATE);
        editor = sp.edit();


        editor.putString("name","Henry");
        editor.putBoolean("show",true);
        editor.commit();




    }

    public void doIo(View v){
        String name = sp.getString("name","nobody");
        Boolean isShow = sp.getBoolean("show",false);

        tv.setText("Name: " + name + "\n");
    }

    public void doIo2(View v){
        try {
            FileOutputStream fos = openFileOutput("henry.txt",MODE_PRIVATE);
            fos.write("Hello Henry".getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void doIoSdCard(View v){
        Log.i("henry", String.valueOf(Environment.getExternalStorageState()));

        File sdroot = Environment.getExternalStorageDirectory();
        Log.i("henry",sdroot.getAbsolutePath());

        tv.setText(sdroot.getAbsolutePath());

        File dir = new File(sdroot,"dir");
        if (!dir.exists()){
            dir.mkdir();
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
