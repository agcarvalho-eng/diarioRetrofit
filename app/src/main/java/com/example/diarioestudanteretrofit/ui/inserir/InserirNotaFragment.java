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

public class InserirNotaFragment extends Fragment {
    private FragmentInserirNotaBinding binding;
    private InserirNotaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInserirNotaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InserirNotaViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        int estudanteId = getArguments() != null ? getArguments().getInt("ESTUDANTE_ID", -1) : -1;
        viewModel.setEstudanteId(estudanteId);

        binding.btnSalvarNota.setOnClickListener(v -> {
            if (viewModel.nota.getValue() != null && !viewModel.nota.getValue().isEmpty()) {
                viewModel.salvarNota(() -> {
                    // Envia um sinal de que a nota foi inserida
                    Bundle result = new Bundle();
                    result.putBoolean("NOTA_INSERIDA", true);
                    getParentFragmentManager().setFragmentResult("resultado_nota", result);

                    // Volta para a tela anterior
                    Navigation.findNavController(v).popBackStack();
                });
            } else {
                binding.layoutNota.setError("Informe uma nota válida");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
