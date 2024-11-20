package com.snt.aqualuxe.serviciosVehiculo;

public class RegistrarVehiculo {
    private String marca;
    private String tipo;
    private String modelo;
    private String placa;
    private String color;
    private String usuario_id;
    private String imagenUrl; // Si deseas enviar la imagen como URL

    public RegistrarVehiculo(String marca, String tipo, String modelo, String placa, String color, String usuarioId, String imagenUrl) {
        this.marca = marca;
        this.tipo = tipo;
        this.modelo = modelo;
        this.placa = placa;
        this.color = color;
        this.usuario_id = usuarioId;
        this.imagenUrl = imagenUrl;
    }

    // Getters y setters
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUsuarioId() {
        return usuario_id;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuario_id = usuarioId;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}

