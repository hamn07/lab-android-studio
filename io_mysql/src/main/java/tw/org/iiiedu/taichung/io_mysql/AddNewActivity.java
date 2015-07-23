package tw.org.iiiedu.taichung.io_mysql;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AddNewActivity extends ActionBarActivity {
    private EditText et_name;
    private EditText et_password;
    private EditText et_cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);


        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        et_cname = (EditText) findViewById(R.id.et_cname);
    }

    public void addNew(View v){
        new MyThread().start();
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://10.0.3.2/LabJava/project/test/Demo_PDO_basic_iii.php?"+
                    "name="+et_name.getText()+
                    "password="+et_password.getText()+
                    "cname="+et_cname.getText());

            HttpResponse response = null;
            try {
                response = client.execute(get);

                HttpEntity entity = response.getEntity();

                InputStream is = entity.getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line=br.readLine())!=null){
                    Log.i("henry", line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new, menu);
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
