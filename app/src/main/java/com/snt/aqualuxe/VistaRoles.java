package com.snt.aqualuxe;

import android.os.Bundle;

public class VistaRoles extends BarraDeNavegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer el contenido principal de VistaRoles en el frame de la barra de navegación
        getLayoutInflater().inflate(R.layout.activity_vista_roles, findViewById(R.id.frameLayout));
    }
}
