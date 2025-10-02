package com.example.bancoli;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Transaccion;
import com.example.bancoli.databinding.ActivityAddEditTransaccionBinding;

public class AddEditTransaccionActivity extends AppCompatActivity {

    public static final String EXTRA_TRANSACCION_ID = "com.example.bancoli.EXTRA_TRANSACCION_ID";
    public static final String EXTRA_CUENTA_ID = "com.example.bancoli.EXTRA_CUENTA_ID";

    private ActivityAddEditTransaccionBinding binding;
    private BancoLiDatabase db;
    private long transaccionId = -1;
    private long cuentaId = -1;
    private Transaccion currentTransaccion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTransaccionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // No usamos la Toolbar de la actividad principal, sino una propia si la definimos en el layout
        // setSupportActionBar(binding.toolbar); // Asumiendo que el layout incluye una toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = BancoLiDatabase.getDatabase(this);

        cuentaId = getIntent().getLongExtra(EXTRA_CUENTA_ID, -1);
        if (getIntent().hasExtra(EXTRA_TRANSACCION_ID)) {
            transaccionId = getIntent().getLongExtra(EXTRA_TRANSACCION_ID, -1);
            setTitle("Editar Transacción");
        } else {
            setTitle("Añadir Transacción");
        }

        if (cuentaId == -1 && transaccionId == -1) {
            finish();
            return;
        }

        setupSpinner();
        
        if (transaccionId != -1) {
            loadTransaccionData();
        }

        binding.buttonSaveTransaccion.setOnClickListener(v -> saveTransaccion());
    }

    private void setupSpinner() {
        String[] tipos = {"Depósito", "Retiro", "Transferencia Saliente", "Transferencia Entrante"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTransaccionTipo.setAdapter(adapter);
    }

    private void loadTransaccionData() {
        new Thread(() -> {
            currentTransaccion = db.transaccionDao().getTransaccionById(transaccionId);
            if (currentTransaccion != null) {
                runOnUiThread(() -> {
                    binding.edittextTransaccionMonto.setText(String.valueOf(currentTransaccion.monto));
                    binding.edittextTransaccionDescripcion.setText(currentTransaccion.descripcion);
                    // Seleccionar el tipo correcto en el spinner
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spinnerTransaccionTipo.getAdapter();
                    int position = adapter.getPosition(currentTransaccion.tipoTransaccion);
                    binding.spinnerTransaccionTipo.setSelection(position);
                });
            }
        }).start();
    }

    private void saveTransaccion() {
        String montoStr = binding.edittextTransaccionMonto.getText().toString();
        String descripcion = binding.edittextTransaccionDescripcion.getText().toString().trim();
        String tipo = binding.spinnerTransaccionTipo.getSelectedItem().toString();

        if (montoStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto = Double.parseDouble(montoStr);

        new Thread(() -> {
            if (transaccionId == -1) {
                Transaccion newTransaccion = new Transaccion(cuentaId, tipo, monto, System.currentTimeMillis(), descripcion);
                db.transaccionDao().insert(newTransaccion);
            } else {
                currentTransaccion.tipoTransaccion = tipo;
                currentTransaccion.monto = monto;
                currentTransaccion.descripcion = descripcion;
                db.transaccionDao().update(currentTransaccion);
            }
            runOnUiThread(this::finish);
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Manejar el botón de 'atrás' en la toolbar
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
