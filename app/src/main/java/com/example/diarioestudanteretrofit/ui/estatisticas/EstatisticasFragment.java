package com.example.diarioestudanteretrofit.ui.estatisticas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diarioestudanteretrofit.databinding.FragmentEstatisticasBinding;
import com.example.diarioestudanteretrofit.ui.home.EstudantesAdapter;

import java.util.ArrayList;

public class EstatisticasFragment extends Fragment {

    private FragmentEstatisticasBinding binding;
    private EstatisticasViewModel viewModel;
    private EstudantesAdapter aprovadosAdapter;
    private EstudantesAdapter reprovadosAdapter;

    /**
     * Infla o layout do fragmento usando ViewBinding.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEstatisticasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Após a view ser criada, inicializa o ViewModel, vincula o layout ao ciclo de vida e
     * configura os adapters e observadores LiveData.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EstatisticasViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupAdapters();   // Inicializa os adapters e configura os RecyclerViews
        setupObservers();  // Observa LiveData do ViewModel para atualizar a interface
    }

    /**
     * Configura os adapters dos RecyclerViews responsáveis por exibir
     * as listas de estudantes aprovados e reprovados.
     */
    private void setupAdapters() {
        aprovadosAdapter = new EstudantesAdapter(new ArrayList<>());
        reprovadosAdapter = new EstudantesAdapter(new ArrayList<>());

        binding.recyclerAprovados.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAprovados.setAdapter(aprovadosAdapter);

        binding.recyclerReprovados.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerReprovados.setAdapter(reprovadosAdapter);
    }

    /**
     * Observa os dados do ViewModel e atualiza os elementos da interface gráfica
     * conforme as mudanças de dados (ex: média, maior nota, listas de estudantes, etc.).
     */
    private void setupObservers() {
        viewModel.getMediaGeral().observe(getViewLifecycleOwner(), media -> {
            binding.textMediaGeral.setText(String.format("Média geral: %.2f", media));
        });

        viewModel.getMaiorNota().observe(getViewLifecycleOwner(), maior -> {
            binding.textMaiorNota.setText(String.format("Maior nota: %s", maior));
        });

        viewModel.getMenorNota().observe(getViewLifecycleOwner(), menor -> {
            binding.textMenorNota.setText(String.format("Menor nota: %s", menor));
        });

        viewModel.getMediaIdade().observe(getViewLifecycleOwner(), media -> {
            binding.textMediaIdade.setText(String.format("Média de idade (anos): %.0f", media));
        });

        viewModel.getAprovados().observe(getViewLifecycleOwner(), estudantes -> {
            aprovadosAdapter.setEstudantes(estudantes);
        });

        viewModel.getReprovados().observe(getViewLifecycleOwner(), estudantes -> {
            reprovadosAdapter.setEstudantes(estudantes);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * Libera o binding quando a view for destruída para evitar vazamento de memória.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
