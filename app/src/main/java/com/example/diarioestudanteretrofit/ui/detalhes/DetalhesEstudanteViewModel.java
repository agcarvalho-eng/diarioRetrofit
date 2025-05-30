package com.example.diarioestudanteretrofit.ui.detalhes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarioestudanteretrofit.model.Estudante;
import com.example.diarioestudanteretrofit.util.EstudanteRepositorio;
import com.example.diarioestudanteretrofit.util.EstudanteRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesEstudanteViewModel extends ViewModel {

    private static final String TAG = "DetalhesEstudanteVM";
    private final MutableLiveData<Estudante> estudante = new MutableLiveData<>();
    private final EstudanteRepositorio repositorio;

    public DetalhesEstudanteViewModel() {
        repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    public LiveData<Estudante> getEstudante() {
        return estudante;
    }

    public void carregarEstudantePorId(int id) {
        repositorio.buscarEstudantePorId(id).enqueue(new Callback<Estudante>() {
            @Override
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                if (response.isSuccessful()) {
                    Estudante estudanteResponse = response.body();
                    estudante.setValue(estudanteResponse);
                    Log.d(TAG, "Estudante carregado com sucesso: " +estudanteResponse);
                } else {
                    Log.e(TAG,"Erro na resposta: " +response.code());
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                Log.e(TAG,"Falha ao buscar estudante: " + t.getMessage(), t);
            }
        });
    }

    public void recarregarEstudante() {
        if (estudante.getValue() != null) {
            carregarEstudantePorId(estudante.getValue().getId());
        }
    }
}


