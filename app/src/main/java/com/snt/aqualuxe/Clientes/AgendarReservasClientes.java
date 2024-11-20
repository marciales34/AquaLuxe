package com.snt.aqualuxe.Clientes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;
import com.snt.aqualuxe.RetrofitClient;
import com.snt.aqualuxe.Servicio;
import com.snt.aqualuxe.ServicioApi;
import com.snt.aqualuxe.ServicioSpinnerItem;
import com.snt.aqualuxe.serviciosAutolavados.Autolavado;
import com.snt.aqualuxe.serviciosAutolavados.AutolavadoApi;
import com.snt.aqualuxe.serviciosReservaciones.Reserva;
import com.snt.aqualuxe.serviciosReservaciones.ReservacionApi;
import com.snt.aqualuxe.serviciosVehiculo.Vehiculo;
import com.snt.aqualuxe.serviciosVehiculo.VehiculoApi;
import com.snt.aqualuxe.serviciosVehiculo.VehiculoSpinnerItem;
import com.snt.aqualuxe.serviciosVehiculo.VehiculosResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AgendarReservasClientes extends BarraDeNavegacion {

    private Spinner spinnerServicios;
    private int servicioSeleccionadoId = -1; // Variable para guardar el ID seleccionado
    private Spinner spinnerVehiculos;
    private Spinner usuarioSpinner;
    private Spinner spinnerAutolavados;// Agregamos el Spinner para el ID del usuario
    private EditText fechaEditText;
    private EditText horaEditText;
    private Button registrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_agendar_reservas_clientes, findViewById(R.id.frameLayout));

        // Inicializa el Spinner de servicios
        spinnerServicios = findViewById(R.id.servicio_spinner);

        // Configura el listener para capturar la selección
        spinnerServicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Ignora la primera opción "Seleccione un servicio"
                    // Obtén el servicio seleccionado y guarda su ID
                    ServicioSpinnerItem selectedItem = (ServicioSpinnerItem) parent.getItemAtPosition(position);
                    servicioSeleccionadoId = selectedItem.getId();
                    Toast.makeText(
                            AgendarReservasClientes.this,
                            "ID seleccionado: " + servicioSeleccionadoId,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    servicioSeleccionadoId = -1; // Resetea el ID si se selecciona la opción inicial
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no hay selección
            }
        });

        // Llama para cargar los servicios
        obtenerServicios();

        // Inicializa el Spinner de vehículos
        spinnerVehiculos = findViewById(R.id.vehiculo_spinner);

        // Inicializa el Spinner para el ID del usuario
        usuarioSpinner = findViewById(R.id.usuario_spinner);

        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = Integer.parseInt(sharedPreferences.getString("userID", "0")); // Obtén el ID guardado (por defecto 0 si no existe)

        // Crear un ArrayList con solo el userId para mostrarlo en el Spinner
        List<String> usuarioList = new ArrayList<>();
        usuarioList.add(String.valueOf(userId));  // Agrega el userId como texto en el Spinner

        // Crear un ArrayAdapter con el valor que contiene solo el userId
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usuarioList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer el adaptador al Spinner
        usuarioSpinner.setAdapter(adapter);

        // Deshabilitar el Spinner para que no se pueda editar ni cambiar
        usuarioSpinner.setEnabled(false);

        // Si el ID es válido, realizamos la petición
        if (userId != 0) {
            obtenerVehiculos(userId); // Llamamos a la función pasando el ID
        } else {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show();
        }

        spinnerAutolavados = findViewById(R.id.autolavado_spinner);
        obtenerAutolavados();

        fechaEditText = findViewById(R.id.fecha);
        horaEditText = findViewById(R.id.hora);

        configurarFechaYHora();

        registrar = findViewById(R.id.btn_registrar);
        registrar.setOnClickListener(v -> {
            VehiculoSpinnerItem vehiculoSeleccionado = (VehiculoSpinnerItem) spinnerVehiculos.getSelectedItem();
            Autolavado autolavadoSeleccionado = (Autolavado) spinnerAutolavados.getSelectedItem();
            ServicioSpinnerItem servicioSeleccionado = (ServicioSpinnerItem) spinnerServicios.getSelectedItem();

            if (vehiculoSeleccionado.getId() == -1 || autolavadoSeleccionado.getIdAutolavado() == -1 ||
                    servicioSeleccionado.getId() == -1 || fechaEditText.getText().toString().isEmpty() ||
                    horaEditText.getText().toString().isEmpty()) {
                Toast.makeText(AgendarReservasClientes.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            int idUsuario = userId; // Asume que tienes el ID del usuario almacenado
            int idVehiculo = vehiculoSeleccionado.getId();
            int idAutolavado = autolavadoSeleccionado.getIdAutolavado();
            int idServicio = servicioSeleccionado.getId();
            String fecha = fechaEditText.getText().toString();
            String hora = horaEditText.getText().toString();

            // Llamar al método de registrar reserva
            registrarReserva(idUsuario, idVehiculo, idAutolavado, idServicio, fecha, hora);
        });


    }


    private void obtenerServicios() {
        // Inicializa Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ServicioApi servicioApi = retrofit.create(ServicioApi.class);

        // Realiza la solicitud
        Call<List<Servicio>> call = servicioApi.obtenerServicios();
        call.enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtiene la lista de servicios
                    List<Servicio> servicios = response.body();

                    // Lista para los items del Spinner (ID y nombre)
                    List<ServicioSpinnerItem> spinnerItems = new ArrayList<>();
                    spinnerItems.add(new ServicioSpinnerItem(-1, "Seleccione un servicio"));

                    // Agregar los servicios a la lista del Spinner
                    for (Servicio servicio : servicios) {
                        spinnerItems.add(new ServicioSpinnerItem(servicio.getId(), servicio.getNombre()));
                    }

                    // Configura el adaptador del Spinner
                    ArrayAdapter<ServicioSpinnerItem> adapter = new ArrayAdapter<>(
                            AgendarReservasClientes.this,
                            android.R.layout.simple_spinner_item,
                            spinnerItems
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerServicios.setAdapter(adapter);
                } else {
                    Toast.makeText(AgendarReservasClientes.this, "No se encontraron servicios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                Toast.makeText(AgendarReservasClientes.this, "Error al cargar los servicios: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener el ID del servicio seleccionado
    public int getServicioSeleccionadoId() {
        return servicioSeleccionadoId;
    }

    private void obtenerVehiculos(int userId) {
        // Crear instancia de Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        VehiculoApi vehiculoApi = retrofit.create(VehiculoApi.class);

        // Llamamos a la API pasando el userId dinámicamente
        Call<VehiculosResponse> call = vehiculoApi.obtenerVehiculosPorUsuario(userId);

        // Enviamos la petición
        call.enqueue(new Callback<VehiculosResponse>() {
            @Override
            public void onResponse(Call<VehiculosResponse> call, Response<VehiculosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtener la lista de vehículos desde el campo 'data'
                    List<Vehiculo> vehiculos = response.body().getData();

                    // Crear una lista para los objetos VehiculoSpinnerItem
                    List<VehiculoSpinnerItem> vehiculosSpinner = new ArrayList<>();

                    // Agregar el primer elemento para seleccionar "Seleccione un vehículo"
                    vehiculosSpinner.add(new VehiculoSpinnerItem(-1, "Seleccione un vehículo"));

                    // Llenar la lista con los vehículos (id y placa)
                    for (Vehiculo vehiculo : vehiculos) {
                        vehiculosSpinner.add(new VehiculoSpinnerItem(vehiculo.getId(), vehiculo.getPlaca()));
                    }

                    // Crear un adaptador para el Spinner
                    ArrayAdapter<VehiculoSpinnerItem> adapter = new ArrayAdapter<>(AgendarReservasClientes.this,
                            android.R.layout.simple_spinner_item, vehiculosSpinner);

                    // Establecer el adaptador al Spinner
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerVehiculos.setAdapter(adapter);

                    // Agregar el listener para la selección de un vehículo
                    spinnerVehiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) { // Ignora la opción de "Seleccione un vehículo"
                                // Obtén el vehículo seleccionado
                                VehiculoSpinnerItem selectedItem = (VehiculoSpinnerItem) parent.getItemAtPosition(position);
                                int vehiculoId = selectedItem.getId();
                                String placa = selectedItem.getNombre(); // Aquí obtienes la placa

                                // Mostrar un aviso con el ID y la placa del vehículo seleccionado
                                String mensaje = "ID del vehículo: " + vehiculoId + " - Placa: " + placa;
                                Toast.makeText(AgendarReservasClientes.this, mensaje, Toast.LENGTH_SHORT).show();

                                // Log para debug
                                Log.d("Vehiculo seleccionado", mensaje);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // No hacer nada si no hay selección
                        }
                    });

                } else {
                    Toast.makeText(AgendarReservasClientes.this, "Error al obtener los vehículos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VehiculosResponse> call, Throwable t) {
                Toast.makeText(AgendarReservasClientes.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerAutolavados() {
        // Crear instancia de Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AutolavadoApi autolavadoApi = retrofit.create(AutolavadoApi.class);

        // Hacer la solicitud para obtener los autolavados
        Call<List<Autolavado>> call = autolavadoApi.obtenerAutolavados();

        call.enqueue(new Callback<List<Autolavado>>() {
            @Override
            public void onResponse(Call<List<Autolavado>> call, Response<List<Autolavado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtener la lista de autolavados
                    List<Autolavado> autolavados = response.body();

                    // Crear una lista para los objetos AutolavadoSpinnerItem
                    List<Autolavado> autolavadosSpinner = new ArrayList<>();

                    // Agregar el primer elemento para seleccionar "Seleccione un autolavado"
                    autolavadosSpinner.add(new Autolavado(-1, "Seleccione un autolavado"));

                    // Llenar la lista con los autolavados (id y nombre)
                    autolavadosSpinner.addAll(autolavados);

                    // Crear el adaptador para el Spinner
                    ArrayAdapter<Autolavado> adapter = new ArrayAdapter<>(AgendarReservasClientes.this,
                            android.R.layout.simple_spinner_item, autolavadosSpinner);

                    // Establecer el adaptador al Spinner
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAutolavados.setAdapter(adapter);

                    // Agregar un listener para la selección de un autolavado
                    spinnerAutolavados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) { // Ignora la opción "Seleccione un autolavado"
                                // Obtén el autolavado seleccionado
                                Autolavado selectedAutolavado = (Autolavado) parent.getItemAtPosition(position);
                                int autolavadoId = selectedAutolavado.getIdAutolavado();
                                Log.d("Autolavado seleccionado", "ID del autolavado: " + autolavadoId);

                                // Mostrar un aviso con el ID y la placa del vehículo seleccionado
                                String mensaje = "ID del Autolavado : " + autolavadoId;
                                Toast.makeText(AgendarReservasClientes.this, mensaje, Toast.LENGTH_SHORT).show();

                                // Aquí puedes guardar el ID para enviarlo al servidor en el registro
                                // Por ejemplo, puedes guardar en una variable global o pasarlo al método de registro
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // No hacer nada si no hay selección
                        }
                    });

                } else {
                    Toast.makeText(AgendarReservasClientes.this, "Error al obtener los autolavados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Autolavado>> call, Throwable t) {
                Toast.makeText(AgendarReservasClientes.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarFechaYHora() {
        fechaEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AgendarReservasClientes.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Cambia el formato de la fecha a YYYY-MM-DD
                        String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        fechaEditText.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        horaEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AgendarReservasClientes.this,
                    (view, hourOfDay, minute1) -> {
                        // Asegúrate de que la hora esté en formato HH:mm:ss
                        String selectedTime = String.format("%02d:%02d:00", hourOfDay, minute1); // Añade ":00" para el segundo
                        horaEditText.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    // Método para convertir Reserva a Map
    private Map<String, Object> reservaToMap(Reserva reserva) {
        Map<String, Object> map = new HashMap<>();
        map.put("usuario_id", reserva.getIdUsuario());
        map.put("vehiculo_id", reserva.getIdVehiculo());
        map.put("autolavado_id", reserva.getIdAutolavado());
        map.put("servicio_id", reserva.getIdServicio());
        map.put("fecha", reserva.getFecha());
        map.put("hora", reserva.getHora());
        return map;
    }

    private void registrarReserva(int idUsuario, int idVehiculo, int idAutolavado, int idServicio, String fecha, String hora) {
        // Validación de campos
        if (idUsuario <= 0 || idVehiculo <= 0 || idAutolavado <= 0 || idServicio <= 0 || fecha == null || fecha.isEmpty() || hora == null || hora.isEmpty()) {
            Toast.makeText(AgendarReservasClientes.this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return; // Salir del método si hay un error
        }

        // Crear una instancia de Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ReservacionApi reservacionApi = retrofit.create(ReservacionApi.class);

        // Crear un objeto Reserva
        Reserva reserva = new Reserva(idUsuario, idVehiculo, idAutolavado, idServicio, fecha, hora);

        // Imprimir los valores en la consola para depuración
        Log.d("RegistrarReserva", "Datos a enviar:");
        Log.d("RegistrarReserva", "ID Usuario: " + idUsuario);
        Log.d("RegistrarReserva", "ID Vehículo: " + idVehiculo);
        Log.d("RegistrarReserva", "ID Autolavado: " + idAutolavado);
        Log.d("RegistrarReserva", "ID Servicio: " + idServicio);
        Log.d("RegistrarReserva", "Fecha: " + fecha);
        Log.d("RegistrarReserva", "Hora: " + hora);

        // Llamar a la API para registrar la reserva
        Call<ResponseBody> call = reservacionApi.registrarReserva(reserva);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Reserva registrada exitosamente
                    Toast.makeText(AgendarReservasClientes.this, "Reserva registrada exitosamente.", Toast.LENGTH_SHORT).show();

                    // Crear un Intent para ir a la actividad de reservas
                    Intent intent = new Intent(AgendarReservasClientes.this, PaginaReservasClientes.class);
                    startActivity(intent); // Iniciar la nueva actividad
                    finish(); // Opcional: cerrar la actividad actual si no la necesitas más
                } else {
                    // Manejo de errores en la respuesta
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("RegistrarReserva", "Error: " + errorBody);
                        Toast.makeText(AgendarReservasClientes.this, "Error al registrar la reserva: " + response.message(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Manejo de errores de conexión
                Toast.makeText(AgendarReservasClientes.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

