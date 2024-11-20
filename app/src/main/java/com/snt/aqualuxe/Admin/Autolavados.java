package com.snt.aqualuxe.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.Clientes.MainActivity;
import com.snt.aqualuxe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Autolavados extends BarraDeNavegacion {
    private RecyclerView recyclerViewAutolavados;
    private AutolavadoAdapter adapter;
    private List<Autolavado> autolavadoList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_autolavados, findViewById(R.id.frameLayout));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        recyclerViewAutolavados = findViewById(R.id.recycler_autolavados);
        recyclerViewAutolavados.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de autolavados
        autolavadoList = new ArrayList<>();

        cargarAutolavados();

        // Configura el adaptador
        adapter = new AutolavadoAdapter(autolavadoList, this, new AutolavadoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Autolavado autolavado) {
                // Crear intent para abrir la vista de detalles
                Intent intent = new Intent(Autolavados.this, ModificarAutolavado.class);

                // Pasar el ID del autolavado a la nueva actividad
                intent.putExtra("ID_AUTOLAVADO", autolavado.getId_autolavado());

                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });

        recyclerViewAutolavados.setAdapter(adapter);

        Button btn = findViewById(R.id.btn_autolavados);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(Autolavados.this, CrearAutolavado.class);
                startActivity(intent);
            }
        });
    }


        private void cargarAutolavados() {

            // URL de tu API para obtener autolavados
            String url = getString(R.string.url_api) + "/autolavados/autolavados";

            // Crear solicitud JsonArrayRequest (cambiamos de JsonObjectRequest a JsonArrayRequest)
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                // Limpiar lista anterior
                                autolavadoList.clear();

                                // Iterar sobre los autolavados
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);

                                    Autolavado autolavado = new Autolavado(
                                            obj.getInt("id_autolavado"),
                                            obj.getString("nombre_autolavado"),
                                            obj.getString("horario"),
                                            obj.getString("direccion")
                                    );

                                    autolavadoList.add(autolavado);
                                }

                                // Notificar al adaptador que los datos han cambiado
                                adapter.notifyDataSetChanged();

                                // Verificar si la lista está vacía
                                if (autolavadoList.isEmpty()) {
                                    Toast.makeText(Autolavados.this, "No se encontraron autolavados", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Autolavados.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
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
                                    errorMessage = "No se encontraron autolavados";
                                } else if (statusCode == 500) {
                                    errorMessage = "Error en el servidor. Intenta más tarde.";
                                }
                            } else if (error instanceof TimeoutError) {
                                errorMessage = "Tiempo de espera agotado. Intenta nuevamente.";
                            } else if (error instanceof NoConnectionError) {
                                errorMessage = "Sin conexión a Internet. Revisa tu conexión.";
                            }

                            Log.e("CargarAutolavados", "Error: " + errorMessage);
                            Toast.makeText(Autolavados.this, errorMessage, Toast.LENGTH_SHORT).show();
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