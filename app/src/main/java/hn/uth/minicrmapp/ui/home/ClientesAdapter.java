package hn.uth.minicrmapp.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hn.uth.minicrmapp.OnItemClickListener;
import hn.uth.minicrmapp.database.Cliente;
import hn.uth.minicrmapp.databinding.ClienteItemBinding;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ViewHolder> {

    private List<Cliente> dataset;
    private OnItemClickListener<Cliente> manejadorEventoClick;

    public ClientesAdapter(List<Cliente> dataset, OnItemClickListener<Cliente> manejadorEventoClick) {
        this.dataset = dataset;
        this.manejadorEventoClick = manejadorEventoClick;
    }

    @NonNull
    @Override
    public ClientesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClienteItemBinding binding = ClienteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesAdapter.ViewHolder holder, int position) {
        Cliente clienteMostrar = dataset.get(position);
        holder.binding.tvNombre.setText(clienteMostrar.getNombre());
        holder.binding.tvTelefono.setText(clienteMostrar.getTelefono());
        holder.binding.tvCorreo.setText(clienteMostrar.getCorreo());
        holder.setOnClickListener(clienteMostrar, manejadorEventoClick);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Cliente> clientes){
        this.dataset = clientes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ClienteItemBinding binding;
        public ViewHolder(@NonNull ClienteItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setOnClickListener(Cliente clienteMostrar, OnItemClickListener<Cliente> listener) {
            this.binding.cvCliente.setOnClickListener(v -> listener.onItemClick(clienteMostrar));
        }
    }
}
