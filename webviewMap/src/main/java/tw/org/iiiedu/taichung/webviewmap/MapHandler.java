package tw.org.iiiedu.taichung.webviewmap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by iii-user on 15/7/24.
 */
public class MapHandler extends Handler {

    private TextView tv;

    @Override
    public void handleMessage(Message msg) {
//        super.handleMessage(msg);

//        Log.i("henry",""+msg.what);
        if (tv!=null){
            tv.setText(msg.what);
        }
    }

    void setTextView(TextView tv) {
        this.tv = tv;
    }
}
