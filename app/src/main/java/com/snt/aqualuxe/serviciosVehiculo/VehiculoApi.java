package com.snt.aqualuxe.serviciosVehiculo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface VehiculoApi {

    // Cambia el endpoint según la ruta que tengas en tu API para obtener los vehículos
    @GET("vehiculos/VehiculoCliente/{userId}")
    Call<VehiculosResponse> obtenerVehiculosPorUsuario(@Path("userId") int userId);


    @POST("vehiculos/registrar")
    Call<VehiculoResponseAdapter> registrarVehiculo(@Body RegistrarVehiculo vehiculo);
}


