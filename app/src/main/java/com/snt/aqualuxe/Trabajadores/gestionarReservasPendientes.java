package com.snt.aqualuxe.Trabajadores;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

public class gestionarReservasPendientes extends BarraDeNavegacion {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_gestionar_reservas_pendientes, findViewById(R.id.frameLayout));

    }
}