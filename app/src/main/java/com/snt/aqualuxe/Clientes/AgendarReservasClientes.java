package com.snt.aqualuxe.Clientes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

import java.util.Calendar;

public class AgendarReservasClientes extends BarraDeNavegacion {

    private Spinner spinnerOpciones;
    private Button btnSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_agendar_reservas_clientes, findViewById(R.id.frameLayout));

        spinnerOpciones = findViewById(R.id.spinner_opciones);
        btnSelectDate = findViewById(R.id.btn_select_date);

        String[] opciones = {
                "Seleccione un Autolavado",
                "Lavadero Calle 34",
                "Lavadero Calle 100",
                "Lavadero Venecia"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                opciones
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);

        spinnerOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = opciones[position];
                if (position != 0) {
                    Toast.makeText(AgendarReservasClientes.this, "Seleccionaste: " + seleccion, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSelectDate.setText("Seleccionar Fecha");
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Button btn = findViewById(R.id.btn_continuar_disponibilidad_clientes2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(AgendarReservasClientes.this, RealizarReservaClientes.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AgendarReservasClientes.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth += 1;
                    String selectedDate = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                    btnSelectDate.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();


    }

}
