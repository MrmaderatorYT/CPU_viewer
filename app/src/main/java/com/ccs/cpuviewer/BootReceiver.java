package com.ccs.cpuviewer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Запускаем Foreground Service при перезагрузке устройства
            Intent serviceIntent = new Intent(context, ServiceReceiver.class);
            context.startService(serviceIntent);
        }
    }
}
