package com.example.bancoli.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bancoli.data.entity.Cuenta;
import com.example.bancoli.databinding.ItemCuentaBinding;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CuentaAdapter extends RecyclerView.Adapter<CuentaAdapter.CuentaViewHolder> {

    private List<Cuenta> cuentaList;
    private final OnCuentaListener onCuentaListener;

    // --- INICIO DE LA CORRECCIÓN ---
    public interface OnCuentaListener {
        void onCuentaClicked(Cuenta cuenta); // Para ver transacciones
        void onEditCuenta(Cuenta cuenta);    // Acción secundaria (ej. menú contextual)
        void onDeleteCuenta(Cuenta cuenta);  // Acción de pulsación larga
    }
    // --- FIN DE LA CORRECCIÓN ---

    public CuentaAdapter(List<Cuenta> cuentaList, OnCuentaListener onCuentaListener) {
        this.cuentaList = cuentaList;
        this.onCuentaListener = onCuentaListener;
    }

    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCuentaBinding binding = ItemCuentaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CuentaViewHolder(binding, onCuentaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentaViewHolder holder, int position) {
        Cuenta cuenta = cuentaList.get(position);
        holder.bind(cuenta);
    }

    @Override
    public int getItemCount() {
        return cuentaList == null ? 0 : cuentaList.size();
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentaList = cuentas;
        notifyDataSetChanged();
    }

    static class CuentaViewHolder extends RecyclerView.ViewHolder {
        private final ItemCuentaBinding binding;
        private final OnCuentaListener onCuentaListener;

        public CuentaViewHolder(ItemCuentaBinding binding, OnCuentaListener onCuentaListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onCuentaListener = onCuentaListener;
        }

        public void bind(final Cuenta cuenta) {
            binding.textviewNumeroCuenta.setText("Nº Cuenta: " + cuenta.getNumeroCuenta());
            binding.textviewTipoCuenta.setText("Tipo: " + cuenta.getTipoCuenta());
            
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US); // Formato de moneda
            binding.textviewSaldoCuenta.setText("Saldo: " + currencyFormatter.format(cuenta.getSaldo()));

            // --- INICIO DE LA CORRECCIÓN ---
            itemView.setOnClickListener(v -> {
                if (onCuentaListener != null) {
                    onCuentaListener.onCuentaClicked(cuenta); // Acción principal: ver transacciones
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (onCuentaListener != null) {
                    onCuentaListener.onDeleteCuenta(cuenta); // La pulsación larga elimina
                    return true;
                }
                return false;
            });
            // --- FIN DE LA CORRECCIÓN ---
        }
    }
}
