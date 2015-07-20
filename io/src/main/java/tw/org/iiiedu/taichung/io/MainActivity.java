package tw.org.iiiedu.taichung.io;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity {

    private TextView tv;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private File sdroot;
    private File dir_iotest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        sp = getSharedPreferences("sp.dat", MODE_PRIVATE);
        editor = sp.edit();


        editor.putString("name", "Henry");
        editor.putBoolean("show", true);
        editor.commit();


//        saveToSdCare();
        saveToSdCareAlt();

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
    public void saveToSdCare(){
        sdroot = Environment.getExternalStorageDirectory();
        Log.i("henry", sdroot.getAbsolutePath());

        dir_iotest = new File(sdroot,"iotest");
        if (!dir_iotest.exists()){
            dir_iotest.mkdirs();
        }

        File file1 = new File(dir_iotest,"henry.txt");

        try {
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write("Hello".getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(this,"save ok",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void doIoSdCard(View v){
        File file1 = new File(sdroot,"iotest/henry.txt");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,line,Toast.LENGTH_SHORT).show();
    }
    public void saveToSdCareAlt(){
        sdroot = Environment.getExternalStorageDirectory();

        dir_iotest = new File(sdroot,"/Android/data/" + getPackageName());
        if (!dir_iotest.exists()){
            dir_iotest.mkdirs();
        }

        File file1 = new File(dir_iotest,"henry.txt");

        try {
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write("Hello alt".getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(this,"save ok alt",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void doIoSdCardAlt(View v){
        File file1 = new File(dir_iotest, "henry.txt" );
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,line,Toast.LENGTH_SHORT).show();
    }
    public void doRes (View v){
        Resources res = getResources();
        BufferedReader br = new BufferedReader(new InputStreamReader(res.openRawResource(R.raw.game)));

        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,line,Toast.LENGTH_SHORT).show();

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
