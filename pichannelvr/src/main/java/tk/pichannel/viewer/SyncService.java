package tk.pichannel.viewer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private final String URL_POSTS_REQUEST_STRING = "http://52.198.106.239/api/user/hamn07?apiKey=key1";

    public SyncService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
