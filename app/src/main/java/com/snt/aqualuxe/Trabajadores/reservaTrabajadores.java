package com.snt.aqualuxe.Trabajadores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.snt.aqualuxe.Admin.ReservaAdapter;
import com.snt.aqualuxe.Admin.ReservaView;
import com.snt.aqualuxe.Admin.Reservas;
import com.snt.aqualuxe.Clientes.PaginaInicioCliente;
import com.snt.aqualuxe.Clientes.PaginaReservasClientes;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reservaTrabajadores extends BarraDeNavegacion{

    private RecyclerView recyclerViewReservas;
    private ReservaAdapter adapter;
    private List<ReservaView> reservaList;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_reserva_trabajadores, findViewById(R.id.frameLayout));



    requestQueue = Volley.newRequestQueue(this);

    recyclerViewReservas = findViewById(R.id.recycler_reservas);
        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(this));

    // Inicializar lista de reservas
    reservaList = new ArrayList<>();

    cargarReservas();

    // Configura el adaptador
    adapter = new ReservaAdapter(reservaList, this);

        recyclerViewReservas.setAdapter(adapter);
}


private void cargarReservas() {

    // URL de tu API para obtener reservas
    String url = getString(R.string.url_api) + "/reserva/reservaciones";

    // Crear solicitud JsonArrayRequest (cambiamos de JsonObjectRequest a JsonArrayRequest)
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        // Limpiar lista anterior
                        reservaList.clear();

                        // Iterar sobre los reservas
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            ReservaView reserva = new ReservaView(
                                    obj.getString("usuario"),
                                    obj.getString("vehiculo"),
                                    obj.getString("autolavado"),
                                    obj.getString("servicio"),
                                    obj.getString("fecha"),
                                    obj.getString("hora")
                            );

                            reservaList.add(reserva);
                        }

                        // Notificar al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();

                        // Verificar si la lista está vacía
                        if (reservaList.isEmpty()) {
                            Toast.makeText(reservaTrabajadores.this, "No se encontraron reservas", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(reservaTrabajadores.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // Manejar errores de la solicitud
                    String errorMessage = "Error desconocido";

                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 404) {
                            errorMessage = "No se encontraron reservas";
                        } else if (statusCode == 500) {
                            errorMessage = "Error en el servidor. Intenta más tarde.";
                        }
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Tiempo de espera agotado. Intenta nuevamente.";
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "Sin conexión a Internet. Revisa tu conexión.";
                    }

                    Log.e("CargarReservas", "Error: " + errorMessage);
                    Toast.makeText(reservaTrabajadores.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            return headers;
        }
    };

    // Agregar solicitud a la cola
    requestQueue.add(jsonArrayRequest);
}


}