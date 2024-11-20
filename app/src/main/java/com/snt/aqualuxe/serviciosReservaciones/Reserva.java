package com.snt.aqualuxe.serviciosReservaciones;

public class Reserva {
    private int usuario_id;
    private int vehiculo_id;
    private int autolavado_id;
    private int servicio_id;
    private String fecha;
    private String hora;

    public Reserva(int idUsuario, int idVehiculo, int idAutolavado, int idServicio, String fecha, String hora) {
        this.usuario_id = idUsuario; // Cambiar a idUsuario
        this.vehiculo_id = idVehiculo;
        this.autolavado_id = idAutolavado;
        this.servicio_id = idServicio;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters
    public int getIdUsuario() { return usuario_id; }
    public int getIdVehiculo() { return vehiculo_id; }
    public int getIdAutolavado() { return autolavado_id; }
    public int getIdServicio() { return servicio_id; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
}