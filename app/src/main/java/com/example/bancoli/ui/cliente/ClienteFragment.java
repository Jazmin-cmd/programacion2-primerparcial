package com.example.bancoli.ui.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bancoli.AddEditClienteActivity;
import com.example.bancoli.CuentaActivity;
import com.example.bancoli.R;
import com.example.bancoli.adapter.ClienteAdapter;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.databinding.FragmentClienteBinding;

import java.util.ArrayList;
import java.util.List;

public class ClienteFragment extends Fragment implements ClienteAdapter.OnClienteListener {

    private FragmentClienteBinding binding;
    private BancoLiDatabase db;
    private ClienteAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicar que este fragmento tiene un menú de opciones
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClienteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = BancoLiDatabase.getDatabase(requireContext());

        setupRecyclerView();

        // YA NO NECESITAMOS EL LISTENER DEL BOTÓN FLOTANTE
        // binding.fabAddCliente.setOnClickListener(v -> {
        //     Intent intent = new Intent(requireContext(), AddEditClienteActivity.class);
        //     startActivity(intent);
        // });
    }

    // --- INICIO DE LA CORRECCIÓN ---
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cliente_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_cliente) {
            Intent intent = new Intent(requireContext(), AddEditClienteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- FIN DE LA CORRECCIÓN ---

    private void setupRecyclerView() {
        binding.recyclerviewClientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ClienteAdapter(new ArrayList<>(), this);
        binding.recyclerviewClientes.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClientes();
    }

    private void loadClientes() {
        new Thread(() -> {
            List<Cliente> updatedList = db.clienteDao().getAllClientes();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (adapter != null) {
                        adapter.setClientes(updatedList);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onEditCliente(Cliente cliente) {
        Intent intent = new Intent(requireContext(), CuentaActivity.class);
        intent.putExtra(CuentaActivity.EXTRA_CLIENTE_ID, cliente.getClienteId());
        startActivity(intent);
    }

    @Override
    public void onDeleteCliente(Cliente cliente) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar a " + cliente.getNombre() + " " + cliente.getApellido() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    new Thread(() -> {
                        db.clienteDao().delete(cliente);
                        loadClientes();
                    }).start();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evitar memory leaks
    }
}
