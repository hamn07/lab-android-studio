package tw.org.iiiedu.taichung.io_mysql;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {
    private ListView list;
    private SimpleAdapter adapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        list = getListView();


        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        String[] from = { "name" };
        int[] to = { R.id.title };

        HashMap<String, String> d0 = new HashMap<String, String>();
        d0.put(from[0], "Henry");
        data.add(d0);

        HashMap<String, String> d1 = new HashMap<String, String>();
        d1.put(from[0], "Mary");
        data.add(d1);

        HashMap<String, String> d2 = new HashMap<String, String>();
        d2.put(from[0], "Jerry");
        data.add(d2);

        HashMap<String, String> d3 = new HashMap<String, String>();
        d3.put(from[0], "Ferry");
        data.add(d3);

        HashMap<String, String> d4 = new HashMap<String, String>();
        d4.put(from[0], "Bella");
        data.add(d4);

        HashMap<String, String> d5 = new HashMap<String, String>();
        d5.put(from[0], "Log in");
        data.add(d5);

        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                // TODO Auto-generated method stub
                switchTask(index);
            }
        });

    }
    private void switchTask(int index) {
        switch (index){
            case 0:
                addnew();
                break;
            case 1:
                login();
                break;
            case 2:
                postnew();
                break;
        }
    }
    private void postnew(){
        new Thread(){
            @Override
            public void run() {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://...");
//                StringEntity name = new StringEntity("hamn07");



            }
        }.start();
    }




    private void addnew(){
        Intent it = new Intent(this,AddNewActivity.class);
        startActivity(it);
    }
    private void login(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_new);
        dialog.setTitle("Login");

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_name = (EditText) dialog.findViewById(R.id.et_name);

                checkName(et_name.getText().toString());
            }
        });

        dialog.show();


    }

    private void checkName(String name){
        new CheckNameThread().start();
    }


    private class CheckNameThread extends Thread {
        @Override
        public void run() {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://10.0.3.2/LabJava/project/test/Demo_PDO_basic_iii.php?");

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
}
