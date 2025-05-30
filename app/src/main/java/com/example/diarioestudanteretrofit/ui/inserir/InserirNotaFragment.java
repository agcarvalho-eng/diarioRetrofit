package com.example.diarioestudanteretrofit.ui.inserir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.diarioestudanteretrofit.R;
import com.example.diarioestudanteretrofit.databinding.FragmentInserirNotaBinding;

/**
 * Fragment responsável por gerenciar a tela onde o usuário insere a nota de um estudante.
 */
public class InserirNotaFragment extends Fragment {

    private FragmentInserirNotaBinding binding;   // Binding gerado automaticamente para o layout
    private InserirNotaViewModel viewModel;      // ViewModel associado ao fragmento

    /**
     * Método responsável por inflar o layout do fragmento.
     *
     * @param inflater O inflater usado para inflar o layout
     * @param container O contêiner do fragmento
     * @param savedInstanceState Estado salvo, caso exista
     * @return A view do fragmento
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflando o layout do fragmento
        binding = FragmentInserirNotaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Método chamado após a view ser criada, onde a lógica de inicialização é executada.
     *
     * @param view A view que foi criada
     * @param savedInstanceState Estado salvo, caso exista
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializando o ViewModel
        viewModel = new ViewModelProvider(this).get(InserirNotaViewModel.class);
        binding.setViewModel(viewModel);  // Vincula o ViewModel ao layout
        binding.setLifecycleOwner(getViewLifecycleOwner()); // Vincula o ciclo de vida da view

        // Recuperando o ID do estudante dos argumentos, se presente
        int estudanteId = getArguments() != null ? getArguments().getInt("ESTUDANTE_ID", -1) : -1;
        viewModel.setEstudanteId(estudanteId);  // Definindo o ID do estudante no ViewModel

        // Configuração do botão de salvar a nota
        binding.btnSalvarNota.setOnClickListener(v -> {
            // Verificando se a nota inserida é válida
            if (viewModel.nota.getValue() != null && !viewModel.nota.getValue().isEmpty()) {
                // Chamando o método para salvar a nota
                viewModel.salvarNota(() -> {
                    // Envia um sinal de que a nota foi inserida com sucesso
                    Bundle result = new Bundle();
                    result.putBoolean("NOTA_INSERIDA", true);
                    getParentFragmentManager().setFragmentResult("resultado_nota", result);

                    // Retorna para a tela anterior após salvar a nota
                    Navigation.findNavController(v).popBackStack();
                });
            } else {
                // Caso a nota não seja válida, exibe um erro
                binding.layoutNota.setError("Informe uma nota válida");
            }
        });
    }

    /**
     * Método chamado quando a view é destruída. Limpa o binding.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Limpeza do binding para evitar vazamentos de memória
    }
}

