package com.snt.aqualuxe.Clientes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

public class PaginaInicioCliente extends BarraDeNavegacion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_pagina_inicio_cliente, findViewById(R.id.frameLayout));



    }

}