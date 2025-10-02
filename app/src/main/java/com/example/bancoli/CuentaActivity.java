package com.example.bancoli;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bancoli.adapter.CuentaAdapter;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.data.entity.Cuenta;
import com.example.bancoli.databinding.ActivityCuentaBinding;

import java.util.ArrayList;
import java.util.List;

public class CuentaActivity extends AppCompatActivity implements CuentaAdapter.OnCuentaListener {

    public static final String EXTRA_CLIENTE_ID = "com.example.bancoli.EXTRA_CLIENTE_ID";
    private ActivityCuentaBinding binding;
    private BancoLiDatabase db;
    private long clienteId = -1;
    private CuentaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = BancoLiDatabase.getDatabase(getApplicationContext());

        setSupportActionBar(binding.toolbarCuenta);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        clienteId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1);

        if (clienteId == -1) {
            Toast.makeText(this, "ID de Cliente no válido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadClienteNombre();

        binding.recyclerviewCuentas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CuentaAdapter(new ArrayList<>(), this);
        binding.recyclerviewCuentas.setAdapter(adapter);

        binding.fabAddCuenta.setOnClickListener(view -> {
            Intent intent = new Intent(CuentaActivity.this, AddEditCuentaActivity.class);
            intent.putExtra(AddEditCuentaActivity.EXTRA_CLIENTE_ID, clienteId);
            startActivity(intent);
        });
    }

    private void loadClienteNombre() {
        new Thread(() -> {
            Cliente cliente = db.clienteDao().getClienteById(clienteId);
            if (cliente != null && getSupportActionBar() != null) {
                runOnUiThread(() -> getSupportActionBar().setTitle("Cuentas de " + cliente.getNombre()));
            } else if (getSupportActionBar() != null) {
                 runOnUiThread(() -> getSupportActionBar().setTitle("Cuentas"));
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clienteId != -1) {
            loadCuentas(); 
        }
    }

    private void loadCuentas() {
        new Thread(() -> {
            List<Cuenta> updatedList = db.cuentaDao().getCuentasByClienteId(clienteId);
            runOnUiThread(() -> {
                if (adapter != null) {
                    adapter.setCuentas(updatedList);
                }
            });
        }).start();
    }

    // --- INICIO DE LA CORRECCIÓN ---
    @Override
    public void onCuentaClicked(Cuenta cuenta) {
        Intent intent = new Intent(this, TransaccionActivity.class);
        intent.putExtra(TransaccionActivity.EXTRA_CUENTA_ID, cuenta.getCuentaId());
        startActivity(intent);
    }

    @Override
    public void onEditCuenta(Cuenta cuenta) {
        // Esta acción ahora podría ser activada por un botón de menú en la tarjeta, por ejemplo.
        // Por ahora, lo dejamos como una acción secundaria.
        Intent intent = new Intent(this, AddEditCuentaActivity.class);
        intent.putExtra(AddEditCuentaActivity.EXTRA_CUENTA_ID, cuenta.getCuentaId());
        intent.putExtra(AddEditCuentaActivity.EXTRA_CLIENTE_ID, clienteId);
        startActivity(intent);
    }
    // --- FIN DE LA CORRECCIÓN ---

    @Override
    public void onDeleteCuenta(Cuenta cuenta) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar la cuenta " + cuenta.getNumeroCuenta() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    new Thread(() -> {
                        db.cuentaDao().delete(cuenta);
                        loadCuentas(); 
                    }).start();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
