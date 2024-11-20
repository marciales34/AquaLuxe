package com.snt.aqualuxe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServicioApi {
    @GET("servicios/obtener") // Cambia "servicios" por el endpoint correcto de tu API
    Call<List<Servicio>> obtenerServicios();
}
