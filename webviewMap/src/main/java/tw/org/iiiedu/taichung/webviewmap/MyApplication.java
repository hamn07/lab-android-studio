package tw.org.iiiedu.taichung.webviewmap;

import android.app.Application;

/**
 * Created by iii-user on 15/7/24.
 */
public class MyApplication extends Application {
    private MapHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new MapHandler();
    }

    public MapHandler getHandler(){
        return handler;
    }
}
