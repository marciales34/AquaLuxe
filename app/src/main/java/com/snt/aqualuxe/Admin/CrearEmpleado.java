package com.snt.aqualuxe.Admin;

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
import com.snt.aqualuxe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearEmpleado extends BarraDeNavegacion {
    private EditText nombre, direccion, ciudad, correo, telefono, password;
    private Button btn_empleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_crear_empleado, findViewById(R.id.frameLayout));        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombre = findViewById(R.id.editTextName); // Cambiado a editTextText
        direccion = findViewById(R.id.editTextAddress); // Cambiado a editTextTextPostalAddress
        ciudad = findViewById(R.id.editTextCity); // ID para ciudad
        correo = findViewById(R.id.editTextTextEmailAddress); // ID para correo
        telefono = findViewById(R.id.editTextPhone); // ID para teléfono
        password = findViewById(R.id.editTextPassword); // ID para contraseña
        btn_empleado = findViewById(R.id.btn_empleado); // Cambiado a btn_empleado

        btn_empleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEmpleado();
            }
        });
    }


    private void registrarEmpleado() {
        // Obtener valores de los campos de texto
        String nombre = this.nombre.getText().toString().trim();
        String direccion = this.direccion.getText().toString().trim();
        String ciudad = this.ciudad.getText().toString().trim();
        String correo = this.correo.getText().toString().trim();
        String telefono = this.telefono.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String rol = "trabajador";

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || correo.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL de tu API para registrar el autolavado
        String url = getString(R.string.url_api) + "/usuarios/registrar"; // Cambia esto por tu URL de backend

        // Crear el objeto JSON para la solicitud
        JSONObject postData = new JSONObject();
        try {
            postData.put("nombre", nombre);
            postData.put("direccion", direccion);
            postData.put("ciudad", ciudad);
            postData.put("correo", correo);
            postData.put("telefono", telefono);
            postData.put("password", password);
            postData.put("rol", rol);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud de registro
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Registrar Empleado", "Respuesta del servidor: " + response.toString());
                        try {
                            if (response.has("message")) {
                                String message = response.getString("message");
                                if (message.equals("Usuario registrado exitosamente")) {
                                    Toast.makeText(CrearEmpleado.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                    // Cambiar a la actividad deseada
                                    Intent intent = new Intent(CrearEmpleado.this, Empleados.class);
                                    startActivity(intent);
                                    finish(); // Opcional: cerrar la actividad actual
                                } else {
                                    Toast.makeText(CrearEmpleado.this, "Error al registrar Empleado: " + message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CrearEmpleado.this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CrearEmpleado.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error desconocido";

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                errorMessage = "No se pudo registrar el Empleado. Intenta nuevamente.";
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

                        Log.e("RegistrarEmpleado", "Error en el registro: " + errorMessage);
                        Toast.makeText(CrearEmpleado.this, errorMessage, Toast.LENGTH_SHORT).show();
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