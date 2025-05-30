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

/**
 * Fragment para exibir os detalhes do estudante.
 * Oferece navegação para inserir nota ou frequência, além de permitir o retorno à tela inicial.
 */
public class DetalhesEstudanteFragment extends Fragment {

    private FragmentDetalhesEstudanteBinding binding;
    private DetalhesEstudanteViewModel viewModel;
    private int estudanteId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalhesEstudanteBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true); // Habilita o menu para interceptar o botão "up"
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetalhesEstudanteViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Recupera o ID do estudante que foi passado como argumento
        estudanteId = getArguments() != null ? getArguments().getInt("ESTUDANTE_ID", -1) : -1;

        // Se o ID do estudante for válido, carrega os detalhes
        if (estudanteId != -1) {
            viewModel.carregarEstudantePorId(estudanteId);
        }

        // Observa o LiveData do estudante e atualiza a view quando os dados mudam
        viewModel.getEstudante().observe(getViewLifecycleOwner(), estudante -> {
            binding.invalidateAll(); // Atualiza a view com os dados do estudante
        });

        // Ouve o resultado da inserção de nota
        getParentFragmentManager().setFragmentResultListener("resultado_nota", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean notaInserida = result.getBoolean("NOTA_INSERIDA", false);
                    if (notaInserida && estudanteId != -1) {
                        viewModel.carregarEstudantePorId(estudanteId); // Atualiza os dados do estudante
                    }
                });

        // Configura o listener do BottomNavigation para navegação
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putInt("ESTUDANTE_ID", estudanteId); // Passa o ID do estudante para os fragments

            if (item.getItemId() == R.id.nav_inserir_nota) {
                navController.navigate(R.id.action_detalhesEstudanteFragment_to_inserirNotaFragment, bundle);
                return true;
            } else if (item.getItemId() == R.id.nav_inserir_frequencia) {
                navController.navigate(R.id.action_detalhesEstudanteFragment_to_inserirFrequenciaFragment, bundle);
                return true;
            }

            return false;
        });

        // Configura o título da ActionBar ou Toolbar personalizada
        requireActivity().setTitle("Detalhes do Estudante");
    }

    /**
     * Intercepta o clique no botão "up" (seta de voltar) da ActionBar para garantir
     * que o usuário retorne à tela inicial.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Força o retorno direto ao HomeFragment, limpando a pilha de navegação
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.nav_home, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_home, true) // Limpa a pilha até o HomeFragment
                    .build());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Libera o binding para evitar vazamento de memória
    }
}








