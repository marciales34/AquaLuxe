package com.snt.aqualuxe.Admin;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String correo;
    private String password;
    private String telefono;
    private String ciudad;
    private String direccion;
    private String rol;

    // Constructor
    public Usuario(int id_usuario,String nombre, String correo, String password, String telefono, String ciudad, String direccion, String rol) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.rol = rol;
    }

    // Getters
    public int getId_usuarioe() { return id_usuario; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTelefono() { return telefono; }
    public String getCiudad() { return ciudad; }
    public String getDireccion() { return direccion; }
    public String getRol() { return rol; }

}