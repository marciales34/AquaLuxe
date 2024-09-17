package com.snt.aqualuxe.Clientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.snt.aqualuxe.R;
import com.snt.aqualuxe.VistaRoles;

public class InicioDeSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_de_sesion);

        Button vistaRoles = findViewById(R.id.btn_iniciar_sesion);
        vistaRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioDeSesion.this, VistaRoles.class);
                startActivity(intent);
            }
        });
    }
}