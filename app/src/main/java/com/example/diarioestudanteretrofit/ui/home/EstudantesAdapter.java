package com.example.diarioestudanteretrofit.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarioestudanteretrofit.databinding.ItemEstudanteBinding;
import com.example.diarioestudanteretrofit.model.Estudante;

import java.util.List;

public class EstudantesAdapter extends RecyclerView.Adapter<EstudantesAdapter.EstudanteViewHolder> {

    private List<Estudante> estudantes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Estudante estudante);
    }

    public EstudantesAdapter(List<Estudante> estudantes) {
        this.estudantes = estudantes;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setEstudantes(List<Estudante> estudantes) {
        this.estudantes = estudantes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EstudanteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEstudanteBinding binding = ItemEstudanteBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new EstudanteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudanteViewHolder holder, int position) {
        Estudante estudante = estudantes.get(position);
        holder.binding.setEstudante(estudante);
        holder.binding.executePendingBindings();

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(estudante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return estudantes != null ? estudantes.size() : 0;
    }

    static class EstudanteViewHolder extends RecyclerView.ViewHolder {
        final ItemEstudanteBinding binding;

        EstudanteViewHolder(ItemEstudanteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
