package com.snt.aqualuxe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.snt.aqualuxe.Admin.InicioAdmin;
import com.snt.aqualuxe.Clientes.PaginaInicioCliente;
import com.snt.aqualuxe.SuperAdmin.InicioSuperAdmin;
import com.snt.aqualuxe.Trabajadores.gestionarReservasActivas;
import com.snt.aqualuxe.Trabajadores.inicioTrabajadores;
import com.snt.aqualuxe.Trabajadores.reservaTrabajadores;

public class VistaRoles extends BarraDeNavegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer el contenido principal de VistaRoles en el frame de la barra de navegación
        getLayoutInflater().inflate(R.layout.activity_vista_roles, findViewById(R.id.frameLayout));


        Button btn2 = findViewById(R.id.btn_admin);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, InicioAdmin.class);
                startActivity(intent);
            }
        });

        Button btn = findViewById(R.id.btn_super);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, InicioSuperAdmin.class);
                startActivity(intent);
            }
        });

        Button btn3 = findViewById(R.id.btn_empleado);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, inicioTrabajadores.class);
                startActivity(intent);
            }
        });

        Button btn4 = findViewById(R.id.btn_cliente);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(VistaRoles.this, PaginaInicioCliente.class);
                startActivity(intent);
            }
        });
    }
}
