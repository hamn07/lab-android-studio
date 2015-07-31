package tw.org.iiiedu.taichung.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private NotificationManager mgr;
    private Timer timer;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendNotice();
            }
        },10000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotice() {

        mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // instantiate PendingIntent
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(NoticePageActivity.class);
        taskStackBuilder.addNextIntent(new Intent(this,NoticePageActivity.class));
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(124,PendingIntent.FLAG_UPDATE_CURRENT);

        // instantiate Notification.Builder
        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker("super important");
        builder.setAutoCancel(true);
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.sym_def_app_icon));
        builder.setSound(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"sound.wav")));
        builder.setContentInfo("info");
        builder.setContentText("text");
        builder.setContentTitle("title");
        builder.setContentIntent(pendingIntent);


        // API Level 11+
        //Notification notification = builder.getNotification();

        // API Level 16+ (4.1.2+)
        Notification notification = builder.build();

        // send notification
        mgr.notify(0,notification);
    }
}
