package com.snt.aqualuxe.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModificarAutolavado extends BarraDeNavegacion {
    private int idAutolavado;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    // Campos para mostrar los detalles
    private EditText editNombre;
    private EditText editHorario;
    private EditText editDireccion;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_modificar_autolavado, findViewById(R.id.frameLayout));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Obtener el ID pasado desde la actividad anterior
        idAutolavado = getIntent().getIntExtra("ID_AUTOLAVADO", -1);

        // Inicializar vistas
        editNombre = findViewById(R.id.campo_nombre);
        editHorario = findViewById(R.id.campo_horario);
        editDireccion = findViewById(R.id.campo_direccion);
        btnGuardar = findViewById(R.id.btn_guardar);

        // Cargar detalles del autolavado
        cargarDetallesAutolavado();

        // Configurar botón guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });
    }

    private void cargarDetallesAutolavado() {
        // Validar que se haya recibido un ID válido
        if (idAutolavado == -1) {
            Toast.makeText(this, "ID de autolavado no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // URL para obtener detalles de un autolavado específico
        String url = getString(R.string.url_api) + "/autolavados/autolavado/" + idAutolavado;

        // Crear solicitud JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // Llenar campos con la información recibida
                            editNombre.setText(response.getString("nombre_autolavado"));
                            editHorario.setText(response.getString("horario"));
                            editDireccion.setText(response.getString("direccion"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ModificarAutolavado.this,
                                    "Error al procesar los datos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores
                        String errorMessage = "Error al cargar detalles";
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                errorMessage = "Autolavado no encontrado";
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor";
                            }
                        }

                        Toast.makeText(ModificarAutolavado.this,
                                errorMessage,
                                Toast.LENGTH_SHORT).show();
                        finish(); // Cerrar actividad en caso de error
                    }
                });

        // Agregar solicitud a la cola
        requestQueue.add(jsonObjectRequest);
    }

    private void guardarCambios() {

        // URL para actualizar autolavado
        String url = getString(R.string.url_api) + "/autolavados/editar-autolavado/" + idAutolavado;

        // Crear objeto JSON con los datos actualizados
        JSONObject putData = new JSONObject();
        try {
            putData.put("nombre_autolavado", editNombre.getText().toString());
            putData.put("horario", editHorario.getText().toString());
            putData.put("direccion", editDireccion.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear solicitud JsonObjectRequest para actualizar
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, putData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(ModificarAutolavado.this,
                                "Autolavado actualizado exitosamente",
                                Toast.LENGTH_SHORT).show();

                        // Opcional: volver a la lista de autolavados
                        Intent intent = new Intent(ModificarAutolavado.this, Autolavados.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Manejar errores
                        String errorMessage = "Error al actualizar";
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                errorMessage = "Autolavado no encontrado";
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor";
                            }
                        }

                        Toast.makeText(ModificarAutolavado.this,
                                errorMessage,
                                Toast.LENGTH_SHORT).show();
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
        requestQueue.add(jsonObjectRequest);
    }
}