package com.snt.aqualuxe.Trabajadores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.Clientes.PaginaInicioCliente;
import com.snt.aqualuxe.Clientes.PaginaReservasClientes;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

public class reservaTrabajadores extends BarraDeNavegacion{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_reserva_trabajadores, findViewById(R.id.frameLayout));

        RelativeLayout btn = findViewById(R.id.reservas_tr1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(reservaTrabajadores.this, gestionarReservasPendientes.class);
                startActivity(intent);
            }
        });

        RelativeLayout btn2 = findViewById(R.id.reservas_tr2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(reservaTrabajadores.this, gestionarReservasActivas.class);
                startActivity(intent);
            }
        });
    }
}