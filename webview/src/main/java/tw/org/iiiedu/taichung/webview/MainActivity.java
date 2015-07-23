package tw.org.iiiedu.taichung.webview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.webview);
        tv = (TextView) findViewById(R.id.tv);
        handler = new MyHandler();

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
    // Android App ---> WebView
    public void test1(View v) {
        webview.loadUrl("javascript:test1()");
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
