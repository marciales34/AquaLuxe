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

public class ModificarEmpleado extends BarraDeNavegacion {
    private int idUsuario;
    private RequestQueue requestQueue;

    // Campos para mostrar los detalles
    private EditText editNombre;
    private EditText editDireccion;
    private EditText editCiudad;
    private EditText editCorreo;
    private EditText editTelefono;
    private EditText editPassword;
    private Button btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_modificar_empleado, findViewById(R.id.frameLayout));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Obtener el ID pasado desde la actividad anterior
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        // Inicializar vistas
        editNombre = findViewById(R.id.editTextName); // ID para nombre
        editDireccion = findViewById(R.id.editTextAddress); // ID para dirección
        editCiudad = findViewById(R.id.editTextCity); // ID para ciudad
        editCorreo = findViewById(R.id.editTextEmail); // ID para correo
        editTelefono = findViewById(R.id.editTextPhone); // ID para teléfono
        editPassword = findViewById(R.id.editTextPassword); // ID para contraseña
        btnGuardar = findViewById(R.id.btn_guardar); // ID para botón guardar

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
        if (idUsuario == -1) {
            Toast.makeText(this, "ID de empleado no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // URL para obtener detalles de un autolavado específico
        String url = getString(R.string.url_api) + "/usuarios/usuario/" + idUsuario;

        // Crear solicitud JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Llenar campos con la información recibida
                            editNombre.setText(response.getString("nombre"));
                            editDireccion.setText(response.getString("direccion"));
                            editCiudad.setText(response.getString("ciudad"));
                            editCorreo.setText(response.getString("correo"));
                            editTelefono.setText(response.getString("telefono"));
                            editPassword.setText(response.getString("password"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ModificarEmpleado.this,
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
                                errorMessage = "Empleado no encontrado";
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor";
                            }
                        }

                        Toast.makeText(ModificarEmpleado.this,
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
        String url = getString(R.string.url_api) + "/usuarios/editar-usuario/" + idUsuario;

        // Crear objeto JSON con los datos actualizados
        JSONObject putData = new JSONObject();
        try {
            putData.put("nombre", editNombre.getText().toString());
            putData.put("direccion", editDireccion.getText().toString());
            putData.put("ciudad", editCiudad.getText().toString());
            putData.put("correo", editCorreo.getText().toString());
            putData.put("telefono", editTelefono.getText().toString());
            putData.put("password", editPassword.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear solicitud JsonObjectRequest para actualizar
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, putData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ModificarEmpleado.this,
                                "Empleado actualizado exitosamente",
                                Toast.LENGTH_SHORT).show();

                        // Opcional: volver a la lista de autolavados
                        Intent intent = new Intent(ModificarEmpleado.this, Empleados.class);
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
                                errorMessage = "Empleado no encontrado";
                            } else if (statusCode == 500) {
                                errorMessage = "Error en el servidor";
                            }
                        }

                        Toast.makeText(ModificarEmpleado.this,
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