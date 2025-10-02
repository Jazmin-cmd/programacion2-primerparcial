package com.example.bancoli;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Cuenta;
import com.example.bancoli.databinding.ActivityAddEditCuentaBinding;

public class AddEditCuentaActivity extends AppCompatActivity {

    public static final String EXTRA_CUENTA_ID = "com.example.bancoli.EXTRA_CUENTA_ID";
    public static final String EXTRA_CLIENTE_ID = "com.example.bancoli.EXTRA_CLIENTE_ID"; // Para saber a qué cliente pertenece

    private ActivityAddEditCuentaBinding binding;
    private BancoLiDatabase db;
    private long cuentaId = -1; // -1 indica nueva cuenta, >0 indica cuenta existente para editar
    private long fkClienteId = -1; // ID del cliente al que pertenece esta cuenta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = BancoLiDatabase.getDatabase(getApplicationContext());

        setSupportActionBar(binding.toolbarAddEditCuenta);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fkClienteId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1);
        if (fkClienteId == -1) {
            Toast.makeText(this, "Error: ID de Cliente no proporcionado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (getIntent().hasExtra(EXTRA_CUENTA_ID)) {
            cuentaId = getIntent().getLongExtra(EXTRA_CUENTA_ID, -1);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Cuenta");
            }
            binding.buttonSaveCuenta.setText("Actualizar Cuenta");
            loadCuentaData();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Añadir Nueva Cuenta");
            }
            binding.buttonSaveCuenta.setText("Guardar Cuenta");
        }

        binding.buttonSaveCuenta.setOnClickListener(v -> saveCuenta());
    }

    private void loadCuentaData() {
        if (cuentaId == -1) return;
        new Thread(() -> {
            Cuenta cuenta = db.cuentaDao().getCuentaById(cuentaId);
            if (cuenta != null) {
                runOnUiThread(() -> {
                    binding.etCuentaNumero.setText(cuenta.getNumeroCuenta());
                    binding.etCuentaTipo.setText(cuenta.getTipoCuenta());
                    binding.etCuentaSaldo.setText(String.valueOf(cuenta.getSaldo()));
                });
            }
        }).start();
    }

    private void saveCuenta() {
        String numeroCuenta = binding.etCuentaNumero.getText().toString().trim();
        String tipoCuenta = binding.etCuentaTipo.getText().toString().trim();
        String saldoStr = binding.etCuentaSaldo.getText().toString().trim();

        if (TextUtils.isEmpty(numeroCuenta) || TextUtils.isEmpty(tipoCuenta) || TextUtils.isEmpty(saldoStr)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double saldo;
        try {
            saldo = Double.parseDouble(saldoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Saldo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        long fechaApertura = System.currentTimeMillis(); // Para nuevas cuentas

        new Thread(() -> {
            if (cuentaId == -1) { // Nueva cuenta
                Cuenta nuevaCuenta = new Cuenta(fkClienteId, numeroCuenta, tipoCuenta, saldo, fechaApertura);
                db.cuentaDao().insert(nuevaCuenta);
                runOnUiThread(() -> Toast.makeText(this, "Cuenta guardada", Toast.LENGTH_SHORT).show());
            } else { // Editar cuenta existente
                Cuenta cuentaExistente = db.cuentaDao().getCuentaById(cuentaId);
                if (cuentaExistente != null) {
                    cuentaExistente.setNumeroCuenta(numeroCuenta);
                    cuentaExistente.setTipoCuenta(tipoCuenta);
                    cuentaExistente.setSaldo(saldo);
                    // No actualizamos fechaApertura ni fkClienteId al editar
                    db.cuentaDao().update(cuentaExistente);
                    runOnUiThread(() -> Toast.makeText(this, "Cuenta actualizada", Toast.LENGTH_SHORT).show());
                }
            }
            finish(); // Cierra la actividad después de guardar/actualizar
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Maneja el botón de regreso en la Toolbar
        return true;
    }
}
