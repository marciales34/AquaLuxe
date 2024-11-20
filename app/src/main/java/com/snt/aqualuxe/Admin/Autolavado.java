package com.snt.aqualuxe.Admin;

public class Autolavado {
    private int id_autolavado;
    private String nombre_autolavado;
    private String horario;
    private String direccion;

    // Constructor
    public Autolavado(int id_autolavado, String nombre_autolavado, String horario, String direccion) {
        this.id_autolavado = id_autolavado;
        this.nombre_autolavado = nombre_autolavado;
        this.horario = horario;
        this.direccion = direccion;
    }

    // Getters
    public int getId_autolavado() { return id_autolavado; }
    public String getNombre_autolavado() { return nombre_autolavado; }
    public String getHorario() { return horario; }
    public String getDireccion() { return direccion; }
}

