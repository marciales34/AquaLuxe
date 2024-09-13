package com.snt.aqualuxe;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;

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
    //Cambio de activity Splash
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent;
        new Timer().schedule({
                intent = new Intent(MainActivity.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StartActivity();

        },3000);
    }
}