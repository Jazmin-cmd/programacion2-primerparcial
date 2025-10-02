package com.example.bancoli.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancoli.data.entity.Beneficiario;
import com.example.bancoli.databinding.ItemBeneficiarioBinding;

import java.util.List;

public class BeneficiarioAdapter extends RecyclerView.Adapter<BeneficiarioAdapter.BeneficiarioViewHolder> {

    private List<Beneficiario> beneficiarios;
    private final OnBeneficiarioListener listener;

    public BeneficiarioAdapter(List<Beneficiario> beneficiarios, OnBeneficiarioListener listener) {
        this.beneficiarios = beneficiarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BeneficiarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBeneficiarioBinding binding = ItemBeneficiarioBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BeneficiarioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiarioViewHolder holder, int position) {
        Beneficiario beneficiario = beneficiarios.get(position);
        holder.bind(beneficiario, listener);
    }

    @Override
    public int getItemCount() {
        return beneficiarios != null ? beneficiarios.size() : 0;
    }

    public void setBeneficiarios(List<Beneficiario> beneficiarios) {
        this.beneficiarios = beneficiarios;
        notifyDataSetChanged();
    }

    static class BeneficiarioViewHolder extends RecyclerView.ViewHolder {
        private final ItemBeneficiarioBinding binding;

        public BeneficiarioViewHolder(ItemBeneficiarioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Beneficiario beneficiario, final OnBeneficiarioListener listener) {
            binding.textviewBeneficiarioNombre.setText(beneficiario.getNombreCompletoBeneficiario());
            String cuentaInfo = beneficiario.getBancoBeneficiario() + " - " + beneficiario.getNumeroCuentaBeneficiario();
            binding.textviewBeneficiarioRelacion.setText(cuentaInfo);

            // El toque corto edita
            itemView.setOnClickListener(v -> listener.onEditBeneficiario(beneficiario));

            // --- INICIO DE LA CORRECCIÓN ---
            // El toque largo elimina
            itemView.setOnLongClickListener(v -> {
                listener.onDeleteBeneficiario(beneficiario);
                return true; // Indica que hemos manejado el evento
            });
            // --- FIN DE LA CORRECCIÓN ---
        }
    }

    public interface OnBeneficiarioListener {
        void onEditBeneficiario(Beneficiario beneficiario);
        void onDeleteBeneficiario(Beneficiario beneficiario);
    }
}
