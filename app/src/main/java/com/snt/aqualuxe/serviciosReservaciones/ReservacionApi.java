package com.snt.aqualuxe.serviciosReservaciones;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReservacionApi {
    @POST("reservaciones/registrar")
    Call<ResponseBody> registrarReserva(@Body Reserva reserva);
}

