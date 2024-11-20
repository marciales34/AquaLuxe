package com.snt.aqualuxe.serviciosVehiculo;

public class VehiculoSpinnerItem {
    private int id;
    private String nombre;
    private String placa;

    // Constructor modificado
    public VehiculoSpinnerItem(int id, String placa) {
        this.id = id;
        this.placa = placa;  // Asignar la placa correctamente
        this.nombre = placa; // Usamos la placa como nombre, pero puedes cambiarlo si es necesario
    }

    // Getter y Setter para ID, nombre y placa
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return placa;  // Esto es lo que se mostrará en el Spinner
    }
}
