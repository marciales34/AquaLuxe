package com.snt.aqualuxe.serviciosVehiculo;

public class VehiculoResponseAdapter {

    private String message;
    private Vehiculo data;  // Cambiado a un solo objeto de tipo Vehiculo en lugar de lista

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Vehiculo getData() {
        return data;
    }

    public void setData(Vehiculo data) {
        this.data = data;
    }
}

