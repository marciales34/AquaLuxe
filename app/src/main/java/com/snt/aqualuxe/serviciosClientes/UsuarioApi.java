package com.snt.aqualuxe.serviciosClientes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioApi {
    @GET("usuarios/usuario/{id}") // Definimos el endpoint
    Call<Usuario> obtenerUsuarioPorId(@Path("id") int id);
}