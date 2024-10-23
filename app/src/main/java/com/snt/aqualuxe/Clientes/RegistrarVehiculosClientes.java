package com.snt.aqualuxe.Clientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.R;

public class RegistrarVehiculosClientes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_vehiculos_clientes);

        Button btn = findViewById(R.id.btn_crear_vehiculos_clientes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(RegistrarVehiculosClientes.this, GestionarVehiculosClientes.class);
                startActivity(intent);
            }
        });
    }
}