package com.snt.aqualuxe.Admin;

public class ReservaView {

    private String usuario;
    private String vehiculo;
    private String autolavado;
    private String servicio;
    private String fecha;
    private String hora;

    public ReservaView(String usuario, String vehiculo, String autolavado, String servicio, String fecha, String hora) {
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.autolavado = autolavado;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public String getAutolavado() {
        return autolavado;
    }

    public String getServicio() {
        return servicio;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
}
