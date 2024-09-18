package com.snt.aqualuxe.Clientes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.snt.aqualuxe.R;

public class RealizarReservaClientes extends AppCompatActivity {

    private Button btn_agendar_reserva_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_realizar_reserva_clientes);

        // Definir las opciones de los spinners
        String[] opcionesLavadero = {"Seleccionar servicio", "Lavado 1", "Lavado Premium", "Lavado Platinium"};
        String[] opcionesVehiculo = {"Seleccionar vehículo", "Vehículo 1", "Vehículo 2", "Vehículo 3"};

        // Llamar a la función genérica para configurar cada Spinner
        configurarSpinner(R.id.spinner_vehiculo, opcionesVehiculo);
        configurarSpinner(R.id.spinner_lavadero, opcionesLavadero);

        // Referenciar el botón y configurar el listener
        btn_agendar_reserva_cliente = findViewById(R.id.btn_agendar_reserva_cliente);
        btn_agendar_reserva_cliente.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    // Función genérica para configurar los Spinners
    private void configurarSpinner(int spinnerId, String[] opciones) {
        Spinner spinner = findViewById(spinnerId);

        // Crear un ArrayAdapter usando el layout personalizado
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, opciones);

        // Especificar el layout que se usará cuando las opciones aparezcan (dropdown)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adjuntar el adaptador al Spinner
        spinner.setAdapter(adapter);

        // Manejar la selección de una opción en el Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la opción seleccionada
                String seleccion = opciones[position];

                // Verificar si la opción predeterminada fue seleccionada
                if (position != 0) {
                    // Mostrar un mensaje con la opción seleccionada
                    Toast.makeText(RealizarReservaClientes.this, "Seleccionaste: " + seleccion, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acción cuando no se selecciona nada
            }
        });
    }

    // Función para mostrar un cuadro de diálogo de confirmación
    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Confirmar Reserva");
        builder.setMessage("¿Está seguro que desea confirmar la reserva?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Reserva confirmada", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }

}
