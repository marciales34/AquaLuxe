package com.snt.aqualuxe.serviciosAutolavados;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AutolavadoApi {
    @GET("autolavados/autolavados")
    Call<List<Autolavado>> obtenerAutolavados();
}
