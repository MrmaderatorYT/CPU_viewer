package com.ccs.cpuviewer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceReceiver extends Service {

    private Handler handler;
    private Runnable updateCpuUsageRunnable;
    private String channelId = "MyForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler(Looper.getMainLooper());

        updateCpuUsageRunnable = new Runnable() {
            @Override
            public void run() {
                double cpuUsage = getCpuUsage();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceReceiver.this, channelId)
                        .setContentTitle("CPU Info")
                        .setContentText("Загрузка CPU: " + cpuUsage + "%")
                        .setSmallIcon(R.drawable.ic_launcher);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Foreground Service Channel",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                Notification notification = builder.build();
                startForeground(1, notification);
                handler.postDelayed(this, 1000); // Обновляем каждую секунду
            }
        };

        handler.post(updateCpuUsageRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateCpuUsageRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private double getCpuUsage() {
        try {
            Process process = Runtime.getRuntime().exec("top -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            double cpuUsage = 0.0;

            while ((line = reader.readLine()) != null) {
                if (line.contains("CPU:")) {
                    String[] tokens = line.split(",");
                    for (String token : tokens) {
                        if (token.contains("CPU:")) {
                            String cpuInfo = token.trim();
                            cpuUsage = Double.parseDouble(cpuInfo.replace("CPU:", "").replace("%", "").trim());
                            break;
                        }
                    }
                    break;
                }
            }

            reader.close();
            return cpuUsage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0; // Заглушка, замените этот код реальной логикой
    }
}