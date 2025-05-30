package com.example.diarioestudanteretrofit.ui.detalhes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.diarioestudanteretrofit.databinding.FragmentDetalhesEstudanteBinding;

public class DetalhesEstudanteFragment extends Fragment {

    private FragmentDetalhesEstudanteBinding binding;
    private DetalhesEstudanteViewModel viewModel;
    private int estudanteId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalhesEstudanteBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true); // Habilita o menu para interceptar "up" button
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetalhesEstudanteViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        estudanteId = getArguments() != null ? getArguments().getInt("ESTUDANTE_ID", -1) : -1;

        if (estudanteId != -1) {
            viewModel.carregarEstudantePorId(estudanteId);
        }

        // Atualiza a view com os dados do estudante
        viewModel.getEstudante().observe(getViewLifecycleOwner(), estudante -> {
            binding.invalidateAll();
        });

        // Escuta se houve alteração ao voltar de inserir nota
        getParentFragmentManager().setFragmentResultListener("resultado_nota", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean notaInserida = result.getBoolean("NOTA_INSERIDA", false);
                    if (notaInserida && estudanteId != -1) {
                        viewModel.carregarEstudantePorId(estudanteId);
                    }
                });

        // Navegação para inserir nota ou frequência
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putInt("ESTUDANTE_ID", estudanteId);

            if (item.getItemId() == R.id.nav_inserir_nota) {
                navController.navigate(R.id.action_detalhesEstudanteFragment_to_inserirNotaFragment, bundle);
                return true;
            } else if (item.getItemId() == R.id.nav_inserir_frequencia) {
                navController.navigate(R.id.action_detalhesEstudanteFragment_to_inserirFrequenciaFragment, bundle);
                return true;
            }

            return false;
        });

        // Habilita a seta de "voltar" no app bar (caso esteja usando toolbar customizada)
        requireActivity().setTitle("Detalhes do Estudante");
        requireActivity().getActionBar(); // Só necessário se usar ActionBar padrão
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Força retorno direto ao HomeFragment, limpando a pilha
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.nav_home, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_home, true)
                    .build());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}







