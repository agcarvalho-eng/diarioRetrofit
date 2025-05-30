package com.example.diarioestudanteretrofit.ui.novoEstudante;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.diarioestudanteretrofit.R;
import com.example.diarioestudanteretrofit.databinding.FragmentNovoEstudanteBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * Fragment responsável pela inserção de um novo estudante.
 * Ele coleta os dados inseridos pelo usuário e chama o ViewModel para salvar o estudante.
 */
public class NovoEstudanteFragment extends Fragment {

    // Binding que conecta a UI com o ViewModel
    private FragmentNovoEstudanteBinding binding;

    // ViewModel que gerencia os dados e a lógica de negócios
    private NovoEstudanteViewModel viewModel;

    /**
     * Método responsável por inflar o layout do fragmento.
     *
     * @param inflater O inflater que irá inflar o layout
     * @param container O container onde o fragmento será inserido
     * @param savedInstanceState Estado salvo, caso haja
     * @return O root view do fragmento
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla o layout do fragmento e retorna a raiz da view
        binding = FragmentNovoEstudanteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Método chamado depois da criação da view.
     * Aqui o ViewModel é configurado, os observers e listeners são configurados.
     *
     * @param view A view do fragmento
     * @param savedInstanceState Estado salvo, caso haja
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instancia o ViewModel
        viewModel = new ViewModelProvider(this).get(NovoEstudanteViewModel.class);
        binding.setViewModel(viewModel); // Vincula o ViewModel ao layout
        binding.setLifecycleOwner(getViewLifecycleOwner()); // Define o ciclo de vida da view

        // Configura os observadores e listeners
        setupObservers();
        setupListeners();
    }

    /**
     * Configura os observers que reagem às mudanças nos dados do ViewModel.
     */
    private void setupObservers() {
        // Observa o estado de carregamento e exibe ou oculta o progresso
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE); // Mostra ou oculta o progress bar
            binding.btnSalvar.setEnabled(!isLoading); // Desabilita o botão de salvar durante o carregamento
        });

        // Observa as mensagens de erro e exibe uma Snackbar caso haja algum erro
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (!errorMessage.isEmpty()) {
                Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG).show(); // Exibe a mensagem de erro
                viewModel.limparErro(); // Limpa a mensagem de erro do ViewModel
            }
        });
    }

    /**
     * Configura os listeners para os eventos da UI, como o clique no botão de salvar.
     */
    private void setupListeners() {
        // Listener para o botão de salvar
        binding.btnSalvar.setOnClickListener(v -> {
            // Obtém os dados inseridos pelo usuário
            viewModel.setNome(binding.editNome.getText().toString());
            viewModel.setIdade(binding.editIdade.getText().toString());

            // Chama o método do ViewModel para salvar o estudante
            viewModel.salvarEstudante(() -> {
                // Ao salvar com sucesso, envia um resultado para o fragmento pai
                Bundle result = new Bundle();
                result.putBoolean("novo_estudante_salvo", true);
                getParentFragmentManager().setFragmentResult("novoEstudanteResult", result);

                // Volta para o fragmento anterior
                Navigation.findNavController(v).popBackStack();

                // Exibe uma mensagem de sucesso
                Snackbar.make(binding.getRoot(), "Estudante criado com sucesso", Snackbar.LENGTH_LONG).show();
            });
        });
    }

    /**
     * Método chamado quando a view do fragmento é destruída. Limpa o binding para evitar vazamentos de memória.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Limpa a referência ao binding
    }
}
