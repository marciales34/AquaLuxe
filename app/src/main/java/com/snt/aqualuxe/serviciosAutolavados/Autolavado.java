package com.snt.aqualuxe.serviciosAutolavados;

public class Autolavado {
    private int id_autolavado;
    private String nombre_autolavado;

    // Constructor
    public Autolavado(int id_autolavado, String nombre_autolavado) {
        this.id_autolavado = id_autolavado;
        this.nombre_autolavado = nombre_autolavado;
    }

    // Getters
    public int getIdAutolavado() {
        return id_autolavado;
    }

    public String getNombreAutolavado() {
        return nombre_autolavado;
    }

    // Método toString() para mostrar solo el nombre en el Spinner
    @Override
    public String toString() {
        return nombre_autolavado; // Esto es lo que aparecerá en el Spinner
    }
}

