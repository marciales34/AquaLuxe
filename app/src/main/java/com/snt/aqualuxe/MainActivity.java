package com.snt.aqualuxe;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

//sjjdfgj
        new Timer().schedule(new TimerTask() { // Se agrega TimerTask

            public void run() {
                Intent intent;
                intent = new Intent(MainActivity.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); // Se agrega intent al startActivity
                finish(); // Finaliza la SplashActivity
            }
        }, 2000); // 3000 milisegundos = 3 segundos
    }

}