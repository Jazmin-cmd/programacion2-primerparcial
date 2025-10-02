package com.example.bancoli;

import android.content.DialogInterface;
import android.content.Intent; // Import para Intent
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bancoli.adapter.ClienteAdapter;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.databinding.ActivityClienteBinding;

import java.util.ArrayList;
import java.util.List;

public class ClienteActivity extends AppCompatActivity implements ClienteAdapter.OnClienteListener {

    private ActivityClienteBinding binding;
    private BancoLiDatabase db;
    private ClienteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarCliente);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Clientes");
        }

        db = BancoLiDatabase.getDatabase(getApplicationContext());

        binding.recyclerviewClientes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClienteAdapter(new ArrayList<>(), this);
        binding.recyclerviewClientes.setAdapter(adapter);

        binding.fabAddCliente.setOnClickListener(view -> {
            Intent intent = new Intent(ClienteActivity.this, AddEditClienteActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClientes();
    }

    private void loadClientes() {
        new Thread(() -> {
            List<Cliente> updatedList = db.clienteDao().getAllClientes();
            runOnUiThread(() -> {
                adapter.setClientes(updatedList);
            });
        }).start();
    }

    @Override
    public void onEditCliente(Cliente cliente) { // Esta acción ahora abrirá CuentaActivity
        Intent intent = new Intent(this, CuentaActivity.class); // Cambiado a CuentaActivity
        intent.putExtra(CuentaActivity.EXTRA_CLIENTE_ID, cliente.getClienteId()); // Usar la constante de CuentaActivity
        startActivity(intent);
        // Toast.makeText(this, "Ver cuentas de: " + cliente.getNombre(), Toast.LENGTH_SHORT).show(); // Comentado o eliminado
    }

    @Override
    public void onDeleteCliente(Cliente cliente) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar a " + cliente.getNombre() + " " + cliente.getApellido() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    new Thread(() -> {
                        db.clienteDao().delete(cliente);
                        loadClientes(); // Recargar la lista
                    }).start();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
