package com.example.bancoli.ui.beneficiario;

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

import com.example.bancoli.AddEditBeneficiarioActivity;
import com.example.bancoli.R;
import com.example.bancoli.adapter.BeneficiarioAdapter;
import com.example.bancoli.data.BancoLiDatabase;
import com.example.bancoli.data.entity.Beneficiario;
import com.example.bancoli.databinding.FragmentBeneficiarioBinding;

import java.util.ArrayList;
import java.util.List;

public class BeneficiarioFragment extends Fragment implements BeneficiarioAdapter.OnBeneficiarioListener {

    private FragmentBeneficiarioBinding binding;
    private BancoLiDatabase db;
    private BeneficiarioAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicar que este fragmento tiene un menú de opciones
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBeneficiarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = BancoLiDatabase.getDatabase(requireContext());

        setupRecyclerView();

        // El listener del botón flotante ya no es necesario
    }

    // --- INICIO DE LA CORRECCIÓN ---
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.beneficiario_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_beneficiario) {
            Intent intent = new Intent(requireContext(), AddEditBeneficiarioActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- FIN DE LA CORRECCIÓN ---

    private void setupRecyclerView() {
        binding.recyclerviewBeneficiarios.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BeneficiarioAdapter(new ArrayList<>(), this);
        binding.recyclerviewBeneficiarios.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBeneficiarios();
    }

    private void loadBeneficiarios() {
        new Thread(() -> {
            List<Beneficiario> updatedList = db.beneficiarioDao().getAllBeneficiarios();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (adapter != null) {
                        adapter.setBeneficiarios(updatedList);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onEditBeneficiario(Beneficiario beneficiario) {
        Intent intent = new Intent(requireContext(), AddEditBeneficiarioActivity.class);
        intent.putExtra(AddEditBeneficiarioActivity.EXTRA_BENEFICIARIO_ID, beneficiario.getBeneficiarioId());
        startActivity(intent);
    }

    @Override
    public void onDeleteBeneficiario(Beneficiario beneficiario) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar a " + beneficiario.getNombreCompletoBeneficiario() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    new Thread(() -> {
                        db.beneficiarioDao().delete(beneficiario);
                        loadBeneficiarios(); // Recargar la lista
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
