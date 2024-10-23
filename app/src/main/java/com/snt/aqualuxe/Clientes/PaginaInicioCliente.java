package com.snt.aqualuxe.Clientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.snt.aqualuxe.Admin.Autolavados;
import com.snt.aqualuxe.Admin.Empleados;
import com.snt.aqualuxe.Admin.InicioAdmin;
import com.snt.aqualuxe.Admin.Reservas;
import com.snt.aqualuxe.R;

public class PaginaInicioCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_inicio_cliente);


        Button btn = findViewById(R.id.btn_crear_reservas_clientes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(PaginaInicioCliente.this, PaginaReservasClientes.class);
                startActivity(intent);
            }
        });

        Button btn2 = findViewById(R.id.btn_vehiculos_clientes);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(PaginaInicioCliente.this, RegistrarVehiculosClientes.class);
                startActivity(intent);
            }
        });


        Button btn3 = findViewById(R.id.btn_autolavados_clientes);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(PaginaInicioCliente.this, UbicacionClientes.class);
                startActivity(intent);
            }
        });

        Button btn4 = findViewById(R.id.btn_perfil);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(PaginaInicioCliente.this, PerfilClientes.class);
                startActivity(intent);
            }
        });
    }
}