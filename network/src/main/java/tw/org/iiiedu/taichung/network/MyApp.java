package tw.org.iiiedu.taichung.network;

import android.app.Application;
import android.util.Log;

/**
 * Created by iii-user on 15/7/22.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("henry","me");
    }

}
