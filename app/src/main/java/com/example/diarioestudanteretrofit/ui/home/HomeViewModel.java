package com.example.diarioestudanteretrofit.ui.home;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarioestudanteretrofit.model.Estudante;
import com.example.diarioestudanteretrofit.util.EstudanteRetrofit;
import com.example.diarioestudanteretrofit.util.EstudanteRepositorio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel implements LifecycleObserver {

    private final MutableLiveData<String> mText = new MutableLiveData<>("Lista de Estudantes");
    private final MutableLiveData<List<Estudante>> estudantes = new MutableLiveData<>();

    private final EstudanteRepositorio repositorio;

    public HomeViewModel() {
        repositorio = EstudanteRetrofit.getEstudanteRepositorio();
        carregarEstudantes();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Estudante>> getEstudantes() {
        return estudantes;
    }

    public void carregarEstudantes() {
        repositorio.buscarEstudantes().enqueue(new Callback<List<Estudante>>() {
            @Override
            public void onResponse(Call<List<Estudante>> call, Response<List<Estudante>> response) {
                if (response.isSuccessful()) {
                    estudantes.setValue(response.body());
                } else {
                    mText.setValue("Erro ao carregar estudantes: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Estudante>> call, Throwable t) {
                mText.setValue("Falha na conexão: " + t.getMessage());
            }
        });
    }
}
