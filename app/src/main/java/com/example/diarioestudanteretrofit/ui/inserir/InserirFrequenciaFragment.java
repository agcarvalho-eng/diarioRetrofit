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
import com.example.diarioestudanteretrofit.databinding.FragmentInserirFrequenciaBinding;

/**
 * Fragmento responsável por permitir ao usuário registrar a frequência (presença ou ausência)
 * de um estudante específico.
 */
public class InserirFrequenciaFragment extends Fragment {

    private FragmentInserirFrequenciaBinding binding;
    private InserirFrequenciaViewModel viewModel;

    /**
     * Infla o layout do fragmento usando ViewBinding.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInserirFrequenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Configura os componentes de UI, o ViewModel e os eventos ao criar a view.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa o ViewModel e vincula ao layout
        viewModel = new ViewModelProvider(this).get(InserirFrequenciaViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Obtém o ID do estudante passado como argumento
        int estudanteId = getArguments() != null ? getArguments().getInt("ESTUDANTE_ID", -1) : -1;
        viewModel.setEstudanteId(estudanteId);

        // Define o comportamento ao selecionar uma opção de presença/ausência
        binding.radioGroupPresenca.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_presente) {
                viewModel.setPresenca(true);
            } else if (checkedId == R.id.radio_ausente) {
                viewModel.setPresenca(false);
            }
        });

        // Define ação ao clicar no botão "Salvar"
        binding.btnSalvar.setOnClickListener(v -> {
            // Salva a frequência e navega de volta para a tela de detalhes do estudante
            viewModel.salvarFrequencia(() -> {
                NavController navController = Navigation.findNavController(v);

                // Cria o bundle com o ID do estudante para enviar como argumento
                Bundle args = new Bundle();
                args.putInt("ESTUDANTE_ID", estudanteId);

                // Navega para o fragmento de detalhes (poderia ser ajustado com opções de popUpTo para limpeza da pilha)
                navController.navigate(
                        R.id.detalhesEstudanteFragment,
                        args,
                        null
                );
            });
        });
    }
}



