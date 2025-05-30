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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private EstudantesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Escuta o resultado do NovoEstudanteFragment
        getParentFragmentManager().setFragmentResultListener("novoEstudanteResult", this,
                (requestKey, result) -> {
                    boolean salvo = result.getBoolean("novo_estudante_salvo", false);
                    if (salvo) {
                        homeViewModel.carregarEstudantes(); // ✅ Recarrega estudantes
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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

    private void setupRecyclerView() {
        adapter = new EstudantesAdapter(new ArrayList<>());
        adapter.setOnItemClickListener(this::onEstudanteClicado);
        binding.recyclerView.setAdapter(adapter);
    }

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

    public void onEstudanteClicado(Estudante estudante) {
        if (estudante != null) {
            Bundle args = new Bundle();
            args.putInt("ESTUDANTE_ID", estudante.getId());

            NavHostFragment.findNavController(this)
                    .navigate(R.id.detalhesEstudanteFragment, args);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


