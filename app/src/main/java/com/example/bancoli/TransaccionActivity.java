package com.example.bancoli;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bancoli.adapter.TransaccionAdapter;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Transaccion;
import com.example.bancoli.databinding.ActivityTransaccionBinding;
import java.util.ArrayList;
import java.util.List;

public class TransaccionActivity extends AppCompatActivity implements TransaccionAdapter.OnTransaccionListener {

    public static final String EXTRA_CUENTA_ID = "com.example.bancoli.EXTRA_CUENTA_ID";

    private ActivityTransaccionBinding binding;
    private BancoLiDatabase db;
    private TransaccionAdapter adapter;
    private long cuentaId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransaccionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarTransacciones);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = BancoLiDatabase.getDatabase(this);

        cuentaId = getIntent().getLongExtra(EXTRA_CUENTA_ID, -1);
        if (cuentaId == -1) {
            finish(); // Si no hay ID de cuenta, no podemos continuar.
            return;
        }

        setTitle("Transacciones");
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTransacciones();
    }

    private void setupRecyclerView() {
        binding.recyclerviewTransacciones.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransaccionAdapter(this, new ArrayList<>(), this);
        binding.recyclerviewTransacciones.setAdapter(adapter);
    }

    private void loadTransacciones() {
        new Thread(() -> {
            List<Transaccion> transacciones = db.transaccionDao().getTransaccionesByCuentaId(cuentaId);
            runOnUiThread(() -> adapter.setTransacciones(transacciones));
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaccion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_transaccion) {
            // --- INICIO DE LA CORRECCIÓN ---
            Intent intent = new Intent(this, AddEditTransaccionActivity.class);
            intent.putExtra(AddEditTransaccionActivity.EXTRA_CUENTA_ID, cuentaId);
            startActivity(intent);
            // --- FIN DE LA CORRECCIÓN ---
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditTransaccion(Transaccion transaccion) {
        // --- INICIO DE LA CORRECCIÓN ---
        Intent intent = new Intent(this, AddEditTransaccionActivity.class);
        intent.putExtra(AddEditTransaccionActivity.EXTRA_TRANSACCION_ID, transaccion.transaccionId);
        intent.putExtra(AddEditTransaccionActivity.EXTRA_CUENTA_ID, cuentaId);
        startActivity(intent);
        // --- FIN DE LA CORRECCIÓN ---
    }

    @Override
    public void onDeleteTransaccion(Transaccion transaccion) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Seguro que quieres eliminar esta transacción?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    new Thread(() -> {
                        db.transaccionDao().delete(transaccion);
                        loadTransacciones();
                    }).start();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
