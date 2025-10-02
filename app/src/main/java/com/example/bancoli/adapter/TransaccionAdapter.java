package com.example.bancoli.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bancoli.R;
import com.example.bancoli.data.entity.Transaccion;
import com.example.bancoli.databinding.ItemTransaccionBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {

    private List<Transaccion> transacciones;
    private final OnTransaccionListener listener;
    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public TransaccionAdapter(Context context, List<Transaccion> transacciones, OnTransaccionListener listener) {
        this.context = context;
        this.transacciones = transacciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransaccionBinding binding = ItemTransaccionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransaccionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);
        holder.bind(transaccion, listener, context, dateFormat);
    }

    @Override
    public int getItemCount() {
        return transacciones != null ? transacciones.size() : 0;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
        notifyDataSetChanged();
    }

    static class TransaccionViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransaccionBinding binding;

        public TransaccionViewHolder(ItemTransaccionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Transaccion transaccion, final OnTransaccionListener listener, Context context, SimpleDateFormat dateFormat) {
            binding.textviewTransaccionDescripcion.setText(transaccion.descripcion);
            binding.textviewTransaccionFecha.setText(dateFormat.format(new Date(transaccion.fechaHora)));

            String montoFormatted = String.format(Locale.getDefault(), "$%,.2f", transaccion.monto);

            boolean isIngreso = transaccion.tipoTransaccion.contains("Depósito") || transaccion.tipoTransaccion.contains("Entrante");

            if (isIngreso) {
                binding.imageviewTransaccionIcon.setImageResource(R.drawable.ic_arrow_upward_24);
                binding.textviewTransaccionMonto.setTextColor(ContextCompat.getColor(context, R.color.green_700));
                binding.textviewTransaccionMonto.setText("+ " + montoFormatted);
            } else { // Egreso
                binding.imageviewTransaccionIcon.setImageResource(R.drawable.ic_arrow_downward_24);
                binding.textviewTransaccionMonto.setTextColor(ContextCompat.getColor(context, R.color.red_500));
                binding.textviewTransaccionMonto.setText("- " + montoFormatted);
            }

            itemView.setOnClickListener(v -> listener.onEditTransaccion(transaccion));
            itemView.setOnLongClickListener(v -> {
                listener.onDeleteTransaccion(transaccion);
                return true;
            });
        }
    }

    public interface OnTransaccionListener {
        void onEditTransaccion(Transaccion transaccion);
        void onDeleteTransaccion(Transaccion transaccion);
    }
}
