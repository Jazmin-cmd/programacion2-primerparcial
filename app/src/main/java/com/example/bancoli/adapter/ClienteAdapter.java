package com.example.bancoli.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bancoli.data.entity.Cliente;
import com.example.bancoli.databinding.ItemClienteBinding;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clienteList;
    private final OnClienteListener onClienteListener;

    public interface OnClienteListener {
        void onEditCliente(Cliente cliente);
        void onDeleteCliente(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> clienteList, OnClienteListener onClienteListener) {
        this.clienteList = clienteList;
        this.onClienteListener = onClienteListener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClienteBinding binding = ItemClienteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ClienteViewHolder(binding, onClienteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clienteList.get(position);
        holder.bind(cliente);
    }

    @Override
    public int getItemCount() {
        return clienteList == null ? 0 : clienteList.size();
    }

    public void setClientes(List<Cliente> clientes) {
        this.clienteList = clientes;
        notifyDataSetChanged(); // Se podría optimizar con DiffUtil para listas grandes
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        private final ItemClienteBinding binding;
        private final OnClienteListener onClienteListener;

        public ClienteViewHolder(ItemClienteBinding binding, OnClienteListener onClienteListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onClienteListener = onClienteListener;
        }

        public void bind(final Cliente cliente) {
            binding.textviewClienteNombreCompleto.setText(cliente.getNombre() + " " + cliente.getApellido());
            binding.textviewClienteId.setText("ID: " + cliente.getClienteId());
            binding.textviewClienteTelefono.setText("Tel: " + cliente.getTelefono());

            // Click listener para editar (todo el item)
            itemView.setOnClickListener(v -> {
                if (onClienteListener != null) {
                    onClienteListener.onEditCliente(cliente);
                }
            });

            // Podríamos añadir un botón específico para eliminar en item_cliente.xml
            // y asignarle el onDeleteCliente aquí, o usar un menú contextual.
            // Por ahora, el click largo podría ser una opción para eliminar:
            itemView.setOnLongClickListener(v -> {
                if (onClienteListener != null) {
                    onClienteListener.onDeleteCliente(cliente);
                    return true; // Indica que el evento ha sido consumido
                }
                return false;
            });
        }
    }
}
