package com.snt.aqualuxe.Clientes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.snt.aqualuxe.Admin.InicioAdmin;
import com.snt.aqualuxe.R;
import com.snt.aqualuxe.Trabajadores.inicioTrabajadores;
import com.snt.aqualuxe.VistaRoles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InicioDeSesion extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button btnIniciarSesion;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_de_sesion);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        String correo = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validar campos
        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("InicioDeSesion", "Correo: " + correo); // Log para el correo
        Log.d("InicioDeSesion", "Password: " + password); // Log para la contraseña

        // Mostrar ProgressDialog mientras se realiza la solicitud
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // URL de tu API para iniciar sesión
        String url = getString(R.string.url_api)+"/usuarios/login"; // Cambia esto por tu IP local
        Log.d("InicioDeSesion", "URL de inicio de sesión: " + url); // Log para la URL

        // Crear el objeto JSON para la solicitud
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", correo); // Usa "email" ya que es lo que espera el servidor
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Crear la solicitud de inicio de sesión
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();  // Ocultar ProgressDialog al recibir la respuesta
                        Log.d("InicioDeSesion", "Respuesta del servidor: " + response.toString()); // Log para la respuesta
                        try {
                            // Verificar si el servidor devuelve el mensaje de éxito
                            String message = response.getString("message");
                            if (message.equals("Inicio de sesión exitoso")) {
                                // Mostrar mensaje de éxito
                                Toast.makeText(InicioDeSesion.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                // Obtener el rol del usuario de la respuesta JSON
                                JSONObject usuario = response.getJSONObject("usuario");
                                String rol = usuario.getString("rol");

                                // Guardar el ID del usuario en SharedPreferences
                                String userId = usuario.getString("id"); // Asegúrate de que "id" es el campo correcto en tu respuesta JSON
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID", userId);  // Guardamos el ID del usuario
                                editor.apply();  // Aplicamos los cambios

                                // Mostrar el ID guardado en un Toast
                                Toast.makeText(InicioDeSesion.this, "ID guardado: " + userId, Toast.LENGTH_LONG).show();

                                // Redirigir según el rol
                                Intent intent;
                                switch (rol) {
                                    case "administrador":
                                        intent = new Intent(InicioDeSesion.this, InicioAdmin.class);
                                        break;
                                    case "trabajador":
                                        intent = new Intent(InicioDeSesion.this, inicioTrabajadores.class);
                                        break;
                                    case "cliente":
                                    default:
                                        intent = new Intent(InicioDeSesion.this, PaginaInicioCliente.class);
                                        break;
                                }

                                startActivity(intent);
                                finish(); // Cerrar la actividad actual
                            } else {
                                // Si las credenciales son incorrectas, mostrar mensaje de error
                                Toast.makeText(InicioDeSesion.this, "Credenciales incorrectas, intenta nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InicioDeSesion.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();  // Ocultar ProgressDialog al recibir un error
                        String errorMessage = "Error desconocido";

                        // Manejo de diferentes tipos de errores para mejor depuración
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                errorMessage = "Credenciales incorrectas"; // Mensaje específico para error 404
                            } else if (statusCode == 401) {
                                errorMessage = "Credenciales incorrectas"; // O manejar otro código de error si es necesario
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor. Intenta más tarde.";
                            }
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Tiempo de espera agotado. Intenta nuevamente.";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "Sin conexión a Internet. Revisa tu conexión.";
                        } else if (error instanceof AuthFailureError) {
                            errorMessage = "Error de autenticación. Credenciales incorrectas.";
                        } else {
                            errorMessage = error.toString();
                        }

                        Log.e("InicioDeSesion", "Error en el inicio de sesión: " + errorMessage); // Log para errores
                        Toast.makeText(InicioDeSesion.this, errorMessage, Toast.LENGTH_SHORT).show();
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
