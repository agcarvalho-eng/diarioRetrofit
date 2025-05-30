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

public class NovoEstudanteFragment extends Fragment {
    private FragmentNovoEstudanteBinding binding;
    private NovoEstudanteViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNovoEstudanteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NovoEstudanteViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnSalvar.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (!errorMessage.isEmpty()) {
                Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG).show();
                viewModel.limparErro();
            }
        });
    }

    private void setupListeners() {
        binding.btnSalvar.setOnClickListener(v -> {
            viewModel.setNome(binding.editNome.getText().toString());
            viewModel.setIdade(binding.editIdade.getText().toString());

            viewModel.salvarEstudante(() -> {
                Bundle result = new Bundle();
                result.putBoolean("novo_estudante_salvo", true);
                getParentFragmentManager().setFragmentResult("novoEstudanteResult", result);

                Navigation.findNavController(v).popBackStack();

                Snackbar.make(binding.getRoot(), "Estudante criado com sucesso", Snackbar.LENGTH_LONG).show();
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}