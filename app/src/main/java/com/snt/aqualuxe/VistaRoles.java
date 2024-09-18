package com.snt.aqualuxe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.Admin.InicioAdmin;
import com.snt.aqualuxe.Clientes.InicioDeSesion;
import com.snt.aqualuxe.Clientes.Login;
import com.snt.aqualuxe.Clientes.PaginaInicioCliente;
import com.snt.aqualuxe.SuperAdmin.InicioSuperAdmin;
import com.snt.aqualuxe.Trabajadores.inicioTrabajadores;

public class VistaRoles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_roles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button Admin = findViewById(R.id.btn_admin);
        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, InicioAdmin.class);
                startActivity(intent);
            }
        });

        Button Empleado = findViewById(R.id.btn_empleado);
        Empleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, inicioTrabajadores.class);
                startActivity(intent);
            }
        });

        Button Clientes = findViewById(R.id.btn_cliente);
        Clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, PaginaInicioCliente.class);
                startActivity(intent);
            }
        });

        Button Super = findViewById(R.id.btn_super);
        Super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, InicioSuperAdmin.class);
                startActivity(intent);
            }
        });
    }
}