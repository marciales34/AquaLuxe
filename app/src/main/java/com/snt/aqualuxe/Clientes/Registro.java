package com.snt.aqualuxe.Clientes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.snt.aqualuxe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText nombreEditText, correoEditText, contraseñaEditText, telefonoEditText, direccionEditText, ciudadEditText;
    private Button btnRegistrarse;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar las vistas
        nombreEditText = findViewById(R.id.nombreEditText);
        correoEditText = findViewById(R.id.correoEditText);
        contraseñaEditText = findViewById(R.id.contraseñaEditText);
        telefonoEditText = findViewById(R.id.telefonoEditText);
        direccionEditText = findViewById(R.id.direccionEditText);
        ciudadEditText = findViewById(R.id.ciudadEditText);
        btnRegistrarse = findViewById(R.id.btn_registrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = nombreEditText.getText().toString().trim();
        String correo = correoEditText.getText().toString().trim();
        String contraseña = contraseñaEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String direccion = direccionEditText.getText().toString().trim();
        String ciudad = ciudadEditText.getText().toString().trim();

        // Validar campos
        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || ciudad.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar ProgressDialog mientras se realiza la solicitud
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // URL de tu API para registrar usuario
        String url = "https://fcd7-181-59-2-175.ngrok-free.app/usuarios/registrar"; // Cambia esto por tu IP local

        // Crear el objeto JSON para la solicitud
        JSONObject postData = new JSONObject();
        try {
            postData.put("nombre", nombre);
            postData.put("correo", correo);
            postData.put("password", contraseña);
            postData.put("telefono", telefono);
            postData.put("direccion", direccion);
            postData.put("ciudad", ciudad);
            postData.put("rol", "cliente"); // Rol establecido como cliente
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud de registro
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();  // Ocultar ProgressDialog al recibir la respuesta
                        Log.d("RegistroUsuario", "Respuesta del servidor: " + response.toString());
                        try {
                            // Imprimir toda la respuesta para verificarla
                            Log.d("RegistroUsuario", "Respuesta completa: " + response.toString());

                            // Verificar si el campo "message" existe y cuál es su contenido
                            if (response.has("message")) {
                                String message = response.getString("message");
                                Log.d("RegistroUsuario", "Mensaje recibido: " + message);

                                if (message.equals("Usuario registrado exitosamente")) {
                                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                    // Cambiar a la actividad de inicio de sesión o donde quieras
                                    Intent intent = new Intent(Registro.this, AceptacionPermisos.class);
                                    startActivity(intent);
                                    finish(); // Opcional: cerrar la actividad actual
                                } else {
                                    Toast.makeText(Registro.this, "Error al registrar usuario: " + message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Si el campo "message" no existe
                                Toast.makeText(Registro.this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Registro.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();  // Ocultar ProgressDialog al recibir un error
                        String errorMessage = "Error desconocido";

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                errorMessage = "Error al registrar usuario. Intenta nuevamente.";
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

                        Log.e("RegistroUsuario", "Error en el registro: " + errorMessage);
                        Toast.makeText(Registro.this, errorMessage, Toast.LENGTH_SHORT).show();
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
