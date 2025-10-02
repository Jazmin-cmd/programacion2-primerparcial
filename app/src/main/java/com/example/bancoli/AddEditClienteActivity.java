package com.example.bancoli;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.databinding.ActivityAddEditClienteBinding;

public class AddEditClienteActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENTE_ID = "com.example.bancoli.EXTRA_CLIENTE_ID";
    private ActivityAddEditClienteBinding binding;
    private BancoLiDatabase db;
    private long clienteId = -1; // -1 indica nuevo cliente, >0 indica cliente existente para editar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = BancoLiDatabase.getDatabase(getApplicationContext());

        setSupportActionBar(binding.toolbarAddEditCliente);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent().hasExtra(EXTRA_CLIENTE_ID)) {
            clienteId = getIntent().getLongExtra(EXTRA_CLIENTE_ID, -1);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Cliente");
            }
            binding.buttonSaveCliente.setText("Actualizar Cliente");
            loadClienteData();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Añadir Nuevo Cliente");
            }
            binding.buttonSaveCliente.setText("Guardar Cliente");
        }

        binding.buttonSaveCliente.setOnClickListener(v -> saveCliente());
    }

    private void loadClienteData() {
        if (clienteId == -1) return;
        new Thread(() -> {
            Cliente cliente = db.clienteDao().getClienteById(clienteId);
            if (cliente != null) {
                runOnUiThread(() -> {
                    binding.etClienteNombre.setText(cliente.getNombre());
                    binding.etClienteApellido.setText(cliente.getApellido());
                    binding.etClienteDireccion.setText(cliente.getDireccion());
                    binding.etClienteTelefono.setText(cliente.getTelefono());
                });
            }
        }).start();
    }

    private void saveCliente() {
        String nombre = binding.etClienteNombre.getText().toString().trim();
        String apellido = binding.etClienteApellido.getText().toString().trim();
        String direccion = binding.etClienteDireccion.getText().toString().trim();
        String telefono = binding.etClienteTelefono.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido)) {
            Toast.makeText(this, "Nombre y Apellido son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        long fechaRegistro = System.currentTimeMillis(); // Para nuevos clientes

        new Thread(() -> {
            if (clienteId == -1) { // Nuevo cliente
                Cliente nuevoCliente = new Cliente(nombre, apellido, direccion, telefono, fechaRegistro);
                db.clienteDao().insert(nuevoCliente);
                runOnUiThread(() -> Toast.makeText(this, "Cliente guardado", Toast.LENGTH_SHORT).show());
            } else { // Editar cliente existente
                Cliente clienteExistente = db.clienteDao().getClienteById(clienteId);
                if (clienteExistente != null) {
                    clienteExistente.setNombre(nombre);
                    clienteExistente.setApellido(apellido);
                    clienteExistente.setDireccion(direccion);
                    clienteExistente.setTelefono(telefono);
                    // No actualizamos fechaRegistro al editar
                    db.clienteDao().update(clienteExistente);
                    runOnUiThread(() -> Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show());
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
