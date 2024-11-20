package com.snt.aqualuxe.Admin;

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
import com.android.volley.toolbox.Volley;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Empleados extends BarraDeNavegacion {
    private RecyclerView recyclerViewUsuarios;
    private UsuarioAdapter adapter;
    private List<Usuario> usuarioList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_empleados, findViewById(R.id.frameLayout));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestQueue = Volley.newRequestQueue(this);
        recyclerViewUsuarios = findViewById(R.id.recycler_empleados);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de Usuarios
        usuarioList = new ArrayList<>(); // Cambiado a usuarioList

        cargarDatos();

         //Configura el adaptador
        adapter = new UsuarioAdapter(usuarioList, this, new UsuarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Usuario usuario) {
                // Crear intent para abrir la vista de detalles
                Intent intent = new Intent(Empleados.this, ModificarEmpleado.class);

                // Pasar el ID del autolavado a la nueva actividad
                intent.putExtra("ID_USUARIO", usuario.getId_usuarioe());

                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });
        recyclerViewUsuarios.setAdapter(adapter);

        Button btn2 = findViewById(R.id.btn_empleado);
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(Empleados.this, CrearEmpleado.class);
            startActivity(intent);
        });


    }
    private void cargarDatos() {

        String url = getString(R.string.url_api) + "/usuarios/todos-los-usuarios";

        // Crear solicitud JsonArrayRequest (cambiamos de JsonObjectRequest a JsonArrayRequest)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            // Limpiar lista anterior
                            usuarioList.clear();

                            // Iterar sobre los autolavados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                Usuario usuario = new Usuario(
                                        obj.getInt("id"),
                                        obj.getString("nombre"),
                                        obj.getString("correo"),
                                        obj.getString("password"),
                                        obj.getString("telefono"),
                                        obj.getString("ciudad"),
                                        obj.getString("direccion"),
                                        obj.getString("rol")
                                );
                                if(usuario.getRol().equals("trabajador")) {
                                    usuarioList.add(usuario);
                                }
                            }

                            // Verificar si la lista está vacía
                            if (usuarioList.isEmpty()) {
                                Toast.makeText(Empleados.this, "No se encontraron empleados", Toast.LENGTH_SHORT).show();
                            }

                            // Notificar al adaptador que los datos han cambiado
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Empleados.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
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
                                errorMessage = "No se encontraron empleados";
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor. Intenta más tarde.";
                            }
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Tiempo de espera agotado. Intenta nuevamente.";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "Sin conexión a Internet. Revisa tu conexión.";
                        }

                        Log.e("CargarAutolavados", "Error: " + errorMessage);
                        Toast.makeText(Empleados.this, errorMessage, Toast.LENGTH_SHORT).show();
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