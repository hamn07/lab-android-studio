package tw.org.iiiedu.taichung.network;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;



public class MainActivity extends ActionBarActivity {

    private TextView tv;
    private ImageView iv;
    File sdroot;
    private ProgressDialog dialog;
    private MyHandler handler;
    String filename = "icon1.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);

        handler = new MyHandler();

        sdroot = Environment.getExternalStorageDirectory();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("downloading...");
    }

    public void tcp(View v){
        new tcpThread().start();
    }

    private class tcpThread extends Thread{
        @Override
        public void run() {
            try {
                Socket socket = new Socket("10.2.24.116", 8888);
                OutputStream out = socket.getOutputStream();
                out.write("Hi, I'm Henry!!".getBytes());
                out.flush();
                out.close();
                socket.close();
		    } catch (UnknownHostException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    } catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    }
        }
    }
    private class udpThread extends Thread{
        @Override
        public void run() {

            try {
                byte[] buf = "Hello".getBytes();
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(buf, buf.length,
//                        InetAddress.getByName("10.2.24.116"),7777);
                        InetAddress.getByName("10.0.3.2"),7777);
                socket.send(packet);
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void udp(View v){
        new udpThread().start();

    }

    public void http1(View v){
        dialog.show();
        new Http1Thread().start();
    }

    private class Http1Thread extends Thread {
        @Override
        public void run() {
            // 1.Browser
            DefaultHttpClient client = new DefaultHttpClient();
            // 2.input URL
//            HttpGet get = new HttpGet("http://10.0.3.2/LabPhp/index.html");
            HttpGet get = new HttpGet("https://pdfmyurl.com/?url=tw.yahoo.com");
            // 3.press ENTER
            try {
                File mypdf = new File(sdroot,"yahoo.pdf");
                FileOutputStream fos = new FileOutputStream(mypdf);

                HttpResponse response;
                response = client.execute(get);

                // 4.get response
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();

//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String line;
//                while((line=br.readLine())!=null){
//                    Log.i("henry",line);
//                }

                int temp;
                while ((temp=is.read())!=-1)
                {
                    fos.write(temp);
                }

                is.close();

                fos.flush();
                fos.close();



            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.sendEmptyMessage(0);

        }
    }

    public void http2(View v){
        Log.i("henry","http2");
        new Http2Thread().start();
    }

    private class Http2Thread extends Thread {
        @Override
        public void run() {
            Log.i("henry","http2thread");


            DefaultHttpClient client = new DefaultHttpClient();

            HttpGet get = new HttpGet("https://pdfmyurl.com/img/icon1.png");

            try {
                HttpResponse response = client.execute(get);

                HttpEntity entity = response.getEntity();

                InputStream is = entity.getContent();
                Log.i("henry",filename);
                File file_image = new File(sdroot,filename);
                FileOutputStream fos = new FileOutputStream(file_image);
                int temp;

                while((temp=is.read())!=-1){
                    fos.write(temp);
                }

                is.close();

                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);
            // need handler

        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dialog.dismiss();
                    break;
                case 1:
                    Bitmap bm = BitmapFactory.decodeFile(sdroot+"/"+filename);
                    iv.setImageBitmap(bm);
                    break;
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
