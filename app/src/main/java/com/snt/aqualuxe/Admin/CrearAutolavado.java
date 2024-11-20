package com.snt.aqualuxe.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.snt.aqualuxe.Clientes.AceptacionPermisos;
import com.snt.aqualuxe.Clientes.Registro;
import com.snt.aqualuxe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearAutolavado extends BarraDeNavegacion {

    private EditText nombre, direccion, horario;
    private Button btn_autolavado;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_crear_autolavado, findViewById(R.id.frameLayout));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar las vistas
        nombre = findViewById(R.id.campo_nombre);
        horario = findViewById(R.id.campo_horario);
        direccion = findViewById(R.id.campo_direccion);

        btn_autolavado = findViewById(R.id.btn_autolavado);


        btn_autolavado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarAutolavado();
            }
        });

    }

    private void registrarAutolavado() {
        // Obtener valores de los campos de texto
        String nombre = ((EditText) findViewById(R.id.campo_nombre)).getText().toString().trim();
        String horario = ((EditText) findViewById(R.id.campo_horario)).getText().toString().trim();
        String direccion = ((EditText) findViewById(R.id.campo_direccion)).getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || horario.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar ProgressDialog mientras se realiza la solicitud
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando autolavado...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // URL de tu API para registrar el autolavado
        String url = getString(R.string.url_api)+"/autolavados/crear-autolavado"; // Cambia esto por tu URL de backend

        // Crear el objeto JSON para la solicitud
        JSONObject postData = new JSONObject();
        try {
            postData.put("nombre_autolavado", nombre);
            postData.put("horario", horario);
            postData.put("direccion", direccion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud de registro
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss(); // Ocultar ProgressDialog al recibir la respuesta
                        Log.d("RegistrarAutolavado", "Respuesta del servidor: " + response.toString());
                        try {
                            if (response.has("message")) {
                                String message = response.getString("message");
                                if (message.equals("Autolavado registrado exitosamente")) {
                                    Toast.makeText(CrearAutolavado.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                    // Cambiar a la actividad deseada
                                    Intent intent = new Intent(CrearAutolavado.this, Autolavados.class);
                                    startActivity(intent);
                                    finish(); // Opcional: cerrar la actividad actual
                                } else {
                                    Toast.makeText(CrearAutolavado.this, "Error al registrar autolavado: " + message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CrearAutolavado.this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CrearAutolavado.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss(); // Ocultar ProgressDialog al recibir un error
                        String errorMessage = "Error desconocido";

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                errorMessage = "No se pudo registrar el autolavado. Intenta nuevamente.";
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor. Intenta más tarde.";
                            }
                        } else if (error instanceof com.android.volley.TimeoutError) {
                            errorMessage = "Tiempo de espera agotado. Intenta nuevamente.";
                        } else if (error instanceof com.android.volley.NoConnectionError) {
                            errorMessage = "Sin conexión a Internet. Revisa tu conexión.";
                        } else {
                            errorMessage = error.toString();
                        }

                        Log.e("RegistrarAutolavado", "Error en el registro: " + errorMessage);
                        Toast.makeText(CrearAutolavado.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Agregar la solicitud a la cola
        requestQueue.add(jsonObjectRequest);
    }

}
