package com.snt.aqualuxe.Clientes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.snt.aqualuxe.Admin.Autolavados;
import com.snt.aqualuxe.Admin.Empleados;
import com.snt.aqualuxe.Admin.InicioAdmin;
import com.snt.aqualuxe.Admin.Reservas;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

public class PaginaInicioCliente extends BarraDeNavegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_pagina_inicio_cliente, findViewById(R.id.frameLayout));


        Button btn = findViewById(R.id.btn_reservas_cliente);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(PaginaInicioCliente.this, PaginaReservasClientes.class);
                startActivity(intent);
            }
        });
        Button btn2 = findViewById(R.id.btn_vehiculos);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaginaInicioCliente.this, RegistrarVehiculosClientes.class);
                startActivity(intent);
            }
        });

        Button btn3 = findViewById(R.id.btn_autolavados);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaginaInicioCliente.this, UbicacionClientes.class);
                startActivity(intent);
            }
        });

        Button btn4 = findViewById(R.id.btn_perfilCliente);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaginaInicioCliente.this, PerfilClientes.class);
                startActivity(intent);
            }
        });
    }
}