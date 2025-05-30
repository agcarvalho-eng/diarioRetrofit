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

public class InserirFrequenciaFragment extends Fragment {

    private FragmentInserirFrequenciaBinding binding;
    private InserirFrequenciaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInserirFrequenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InserirFrequenciaViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        int estudanteId = getArguments() != null ? getArguments().getInt("ESTUDANTE_ID", -1) : -1;
        viewModel.setEstudanteId(estudanteId);

        binding.radioGroupPresenca.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_presente) {
                viewModel.setPresenca(true);
            } else if (checkedId == R.id.radio_ausente) {
                viewModel.setPresenca(false);
            }
        });

        binding.btnSalvar.setOnClickListener(v -> {
            viewModel.salvarFrequencia(() -> {
                // Navegação simplificada e mais confiável
                NavController navController = Navigation.findNavController(v);

                // Cria um novo Bundle para garantir que os argumentos estão intactos
                Bundle args = new Bundle();
                args.putInt("ESTUDANTE_ID", estudanteId);

                // Navega para o fragmento de detalhes limpando a pilha
                navController.navigate(
                        R.id.detalhesEstudanteFragment,
                        args,
                        null
                );
            });
        });
    }
}


