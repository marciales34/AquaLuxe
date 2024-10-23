package com.snt.aqualuxe.Trabajadores;

import static com.snt.aqualuxe.R.id.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.Clientes.PaginaInicioCliente;
import com.snt.aqualuxe.Clientes.PaginaReservasClientes;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

public class inicioTrabajadores extends BarraDeNavegacion {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_inicio_trabajadores, findViewById(R.id.frameLayout));

        Button btn = findViewById(R.id.btn_crear_reservas2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(inicioTrabajadores.this, reservaTrabajadores.class);
                startActivity(intent);
            }
        });

        Button btn2 = findViewById(R.id.btn_perfil2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(inicioTrabajadores.this, perfilTrabajador.class);
                startActivity(intent);
            }
        });
    }
}