package com.snt.aqualuxe;

public class ServicioSpinnerItem {
    private int id;
    private String nombre;

    // Constructor
    public ServicioSpinnerItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getter y Setter para ID y nombre
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre; // Lo que se mostrará en el Spinner es el nombre
    }
}
