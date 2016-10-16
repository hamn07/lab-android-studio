package tk.pichannel.viewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Intent it = new Intent(context, BackgroundService.class);
        //context.startService(it);
        //context.startService(new Intent(context, MainService.class));
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent it = new Intent(context, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }
}
