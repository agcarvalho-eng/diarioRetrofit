package com.example.diarioestudanteretrofit.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diarioestudanteretrofit.R;
import com.example.diarioestudanteretrofit.databinding.FragmentHomeBinding;
import com.example.diarioestudanteretrofit.model.Estudante;

import java.util.ArrayList;

/**
 * Fragmento principal que exibe a lista de estudantes.
 * Permite navegar para os detalhes de um estudante ao clicar em um item.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private EstudantesAdapter adapter;

    /**
     * Executado quando o fragmento é criado.
     * Aqui é adicionado um listener para escutar o retorno de NovoEstudanteFragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Escuta o resultado do NovoEstudanteFragment
        getParentFragmentManager().setFragmentResultListener("novoEstudanteResult", this,
                (requestKey, result) -> {
                    boolean salvo = result.getBoolean("novo_estudante_salvo", false);
                    if (salvo) {
                        // Recarrega a lista de estudantes se um novo foi salvo
                        homeViewModel.carregarEstudantes();
                    }
                });
    }

    /**
     * Cria a view do fragmento inflando o layout com view binding.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Método chamado após a view ser criada.
     * Inicializa o ViewModel, configura o RecyclerView e os observers.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        getLifecycle().addObserver(homeViewModel);

        binding.setViewModel(homeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupRecyclerView();
        setupObservers();
    }

    /**
     * Configura o RecyclerView com o adaptador de estudantes e o listener de clique.
     */
    private void setupRecyclerView() {
        adapter = new EstudantesAdapter(new ArrayList<>());
        adapter.setOnItemClickListener(this::onEstudanteClicado);
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * Observa as mudanças nos dados do ViewModel e atualiza a UI.
     */
    private void setupObservers() {
        homeViewModel.getEstudantes().observe(getViewLifecycleOwner(), estudantes -> {
            if (estudantes != null) {
                adapter.setEstudantes(estudantes);
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            binding.textHome.setText(text);
        });
    }

    /**
     * Callback chamado quando um estudante é clicado.
     * Navega para o fragmento de detalhes do estudante passando o ID.
     */
    public void onEstudanteClicado(Estudante estudante) {
        if (estudante != null) {
            Bundle args = new Bundle();
            args.putInt("ESTUDANTE_ID", estudante.getId());

            NavHostFragment.findNavController(this)
                    .navigate(R.id.detalhesEstudanteFragment, args);
        }
    }

    /**
     * Libera o binding para evitar memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



