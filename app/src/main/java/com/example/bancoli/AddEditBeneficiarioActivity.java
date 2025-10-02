package com.example.bancoli;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Beneficiario;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.databinding.ActivityAddEditBeneficiarioBinding;

import java.util.List;
import java.util.stream.Collectors;

public class AddEditBeneficiarioActivity extends AppCompatActivity {

    public static final String EXTRA_BENEFICIARIO_ID = "com.example.bancoli.EXTRA_BENEFICIARIO_ID";

    private ActivityAddEditBeneficiarioBinding binding;
    private BancoLiDatabase db;
    private long beneficiarioId = -1;
    private List<Cliente> clienteList;
    private Beneficiario currentBeneficiario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBeneficiarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = BancoLiDatabase.getDatabase(this);

        if (getIntent().hasExtra(EXTRA_BENEFICIARIO_ID)) {
            beneficiarioId = getIntent().getLongExtra(EXTRA_BENEFICIARIO_ID, -1);
            setTitle("Editar Beneficiario");
        } else {
            setTitle("Añadir Beneficiario");
        }

        loadClientesAndSetupSpinner();

        binding.buttonSaveBeneficiario.setOnClickListener(v -> saveBeneficiario());
    }

    private void loadClientesAndSetupSpinner() {
        new Thread(() -> {
            clienteList = db.clienteDao().getAllClientes();
            List<String> clienteNames = clienteList.stream()
                    .map(cliente -> cliente.getNombre() + " " + cliente.getApellido())
                    .collect(Collectors.toList());

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clienteNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerClientes.setAdapter(adapter);

                if (beneficiarioId != -1) {
                    loadBeneficiarioData();
                }
            });
        }).start();
    }

    private void loadBeneficiarioData() {
        new Thread(() -> {
            currentBeneficiario = db.beneficiarioDao().getBeneficiarioById(beneficiarioId);
            if (currentBeneficiario != null) {
                runOnUiThread(() -> {
                    binding.edittextBeneficiarioNombreCompleto.setText(currentBeneficiario.getNombreCompletoBeneficiario());
                    binding.edittextBeneficiarioNumeroCuenta.setText(currentBeneficiario.getNumeroCuentaBeneficiario());
                    binding.edittextBeneficiarioBanco.setText(currentBeneficiario.getBancoBeneficiario());

                    for (int i = 0; i < clienteList.size(); i++) {
                        if (clienteList.get(i).getClienteId() == currentBeneficiario.getFkClienteId()) {
                            binding.spinnerClientes.setSelection(i);
                            break;
                        }
                    }
                });
            }
        }).start();
    }

    private void saveBeneficiario() {
        String nombreCompleto = binding.edittextBeneficiarioNombreCompleto.getText().toString().trim();
        String numeroCuenta = binding.edittextBeneficiarioNumeroCuenta.getText().toString().trim();
        String banco = binding.edittextBeneficiarioBanco.getText().toString().trim();
        int selectedClientePosition = binding.spinnerClientes.getSelectedItemPosition();

        if (nombreCompleto.isEmpty() || numeroCuenta.isEmpty() || banco.isEmpty() || selectedClientePosition < 0) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        long clienteId = clienteList.get(selectedClientePosition).getClienteId();

        new Thread(() -> {
            if (beneficiarioId == -1) {
                Beneficiario newBeneficiario = new Beneficiario(clienteId, nombreCompleto, numeroCuenta, banco);
                db.beneficiarioDao().insert(newBeneficiario);
            } else {
                currentBeneficiario.setFkClienteId(clienteId);
                currentBeneficiario.setNombreCompletoBeneficiario(nombreCompleto);
                currentBeneficiario.setNumeroCuentaBeneficiario(numeroCuenta);
                currentBeneficiario.setBancoBeneficiario(banco);
                db.beneficiarioDao().update(currentBeneficiario);
            }
            runOnUiThread(this::finish);
        }).start();
    }
}
