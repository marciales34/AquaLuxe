package com.snt.aqualuxe.serviciosClientes;

public class Usuario {
    private String nombre;       // Nombre del usuario
    private String correo;       // Correo electrónico del usuario
    private String telefono;     // Teléfono del usuario
    private String ciudad;       // Ciudad del usuario
    private String direccion;    // Dirección del usuario

    // Constructor
    public Usuario(String nombre, String correo, String telefono, String ciudad, String direccion) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}