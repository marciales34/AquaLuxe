package com.snt.aqualuxe.serviciosVehiculo;

import java.util.List;

public class VehiculosResponse {
    private String message;
    private List<Vehiculo> data;

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Vehiculo> getData() {
        return data;
    }

    public void setData(List<Vehiculo> data) {
        this.data = data;
    }
}