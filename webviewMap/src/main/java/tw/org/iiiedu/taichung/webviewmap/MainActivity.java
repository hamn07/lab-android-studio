package tw.org.iiiedu.taichung.webviewmap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private WebView webview;
    private TextView tv;
    private MyHandler handler;
    private MapHandler map_handler;
    private MyReciever reciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.webview);
        tv = (TextView) findViewById(R.id.tv);
        handler = new MyHandler();
        map_handler = ((MyApplication) getApplication()).getHandler();


        reciever = new MyReciever();
//        IntentFilter filter = new IntentFilter("henrymap");
        registerReceiver(reciever,new IntentFilter("henrymap"));

        initWebView();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
//        WebViewClient wvclient = new WebViewClient();
//        webview.setWebViewClient(wvclient);
//        webview.loadUrl("http://hamn07.github.io");

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScript(),"my_js");
        webview.loadUrl("file:///android_asset/hello.html");


    }

    @Override
    public void finish() {
        unregisterReceiver(reciever);
        super.finish();
    }

    public void test2(View v){
        Intent it = new Intent(this,MyService.class);
        startService(it);
    }

    public void test3(View v){
        Intent it = new Intent(this,MyService.class);
        stopService(it);
    }





    // Android App ---> WebView
    public void test1(View v) {
        double dlat=22.016735;
        double dlng=120.743480;
        webview.loadUrl("javascript:goto("+dlat+","+dlng+")");
    }

    // WebView ---> Android App
    public class MyJavaScript {
        @JavascriptInterface
        public void showName(String data){
            Log.i("henry", data);

            Message message = new Message();
            Bundle bdata = new Bundle();
            bdata.putString("data",data);
            message.setData(bdata);
            handler.sendMessage(message);
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            String data = msg.getData().getString("data");

            tv.setText(data);
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(data);
            dialog = builder.create();
            dialog.show();
        }
    }

    private class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double lat = intent.getDoubleExtra("lat",0);
            double lng = intent.getDoubleExtra("lng",0);
            Log.i("henry","onReceive");
            webview.loadUrl("javascript:goto("+lat+","+lng+")");
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
