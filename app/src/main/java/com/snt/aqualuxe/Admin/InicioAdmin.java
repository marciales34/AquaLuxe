package com.snt.aqualuxe.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.Clientes.PerfilClientes;
import com.snt.aqualuxe.R;

public class InicioAdmin extends BarraDeNavegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_inicio_admin, findViewById(R.id.frameLayout));
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
                Intent intent = new Intent(InicioAdmin.this, Reservas.class);
                startActivity(intent);
            }
        });

        Button btn2 = findViewById(R.id.btn_empleados);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioAdmin.this, Empleados.class);
                startActivity(intent);
            }
        });


        Button btn3 = findViewById(R.id.btn_autolavados);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioAdmin.this, Autolavados.class);
                startActivity(intent);
            }
        });

        Button btn4 = findViewById(R.id.btn_perfil);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioAdmin.this, PerfilClientes.class);
                startActivity(intent);
            }
        });

    }
}