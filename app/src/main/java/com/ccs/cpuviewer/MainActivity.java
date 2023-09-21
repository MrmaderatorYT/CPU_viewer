package com.ccs.cpuviewer;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запускаем Foreground Service при запуске приложения
        Intent serviceIntent = new Intent(this, ServiceReceiver.class);
        startService(serviceIntent);
    }
}
