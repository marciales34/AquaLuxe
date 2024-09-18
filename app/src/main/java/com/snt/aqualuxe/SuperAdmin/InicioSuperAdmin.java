package com.snt.aqualuxe.SuperAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.Admin.Autolavados;
import com.snt.aqualuxe.Admin.Empleados;
import com.snt.aqualuxe.Admin.InicioAdmin;
import com.snt.aqualuxe.Admin.Reservas;
import com.snt.aqualuxe.Clientes.PerfilClientes;
import com.snt.aqualuxe.R;

public class InicioSuperAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_super_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.btn_reservas);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioSuperAdmin.this, Reservas.class);
                startActivity(intent);
            }
        });

        Button btn2 = findViewById(R.id.btn_empleados);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioSuperAdmin.this, Empleados.class);
                startActivity(intent);
            }
        });


        Button btn3 = findViewById(R.id.btn_autolavados);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioSuperAdmin.this, Autolavados.class);
                startActivity(intent);
            }
        });

        Button btn4 = findViewById(R.id.btn_perfil);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioSuperAdmin.this, PerfilClientes.class);
                startActivity(intent);
            }
        });

        Button btn5 = findViewById(R.id.btn_administradores);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioSuperAdmin.this, Administradores.class);
                startActivity(intent);
            }
        });

        Button btn6 = findViewById(R.id.btn_Servicios);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioSuperAdmin.this, Servicios.class);
                startActivity(intent);
            }
        });
    }
}