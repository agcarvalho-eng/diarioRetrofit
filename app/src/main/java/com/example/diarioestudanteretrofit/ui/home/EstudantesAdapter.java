package com.example.diarioestudanteretrofit.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarioestudanteretrofit.databinding.ItemEstudanteBinding;
import com.example.diarioestudanteretrofit.model.Estudante;

import java.util.List;

/**
 * Adaptador para exibir uma lista de estudantes em um RecyclerView.
 * Usa View Binding para mapear dados de cada estudante para o layout XML.
 */
public class EstudantesAdapter extends RecyclerView.Adapter<EstudantesAdapter.EstudanteViewHolder> {

    private List<Estudante> estudantes;
    private OnItemClickListener listener;

    /**
     * Interface para tratar cliques em itens da lista.
     */
    public interface OnItemClickListener {
        void onItemClick(Estudante estudante);
    }

    /**
     * Construtor que recebe uma lista inicial de estudantes.
     */
    public EstudantesAdapter(List<Estudante> estudantes) {
        this.estudantes = estudantes;
    }

    /**
     * Define um listener para capturar eventos de clique nos itens da lista.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Atualiza a lista de estudantes exibida e notifica o RecyclerView para recarregar os itens.
     */
    public void setEstudantes(List<Estudante> estudantes) {
        this.estudantes = estudantes;
        notifyDataSetChanged(); // Atualiza a lista completa
    }

    /**
     * Cria um novo ViewHolder inflando o layout do item (item_estudante.xml).
     */
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

    /**
     * Associa os dados de um estudante à ViewHolder.
     * Também define o comportamento de clique para o item.
     */
    @Override
    public void onBindViewHolder(@NonNull EstudanteViewHolder holder, int position) {
        Estudante estudante = estudantes.get(position);
        holder.binding.setEstudante(estudante); // Data Binding
        holder.binding.executePendingBindings(); // Garante atualização imediata

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(estudante);
            }
        });
    }

    /**
     * Retorna o número total de estudantes na lista.
     */
    @Override
    public int getItemCount() {
        return estudantes != null ? estudantes.size() : 0;
    }

    /**
     * ViewHolder que representa cada item da lista de estudantes.
     */
    static class EstudanteViewHolder extends RecyclerView.ViewHolder {
        final ItemEstudanteBinding binding;

        EstudanteViewHolder(ItemEstudanteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
