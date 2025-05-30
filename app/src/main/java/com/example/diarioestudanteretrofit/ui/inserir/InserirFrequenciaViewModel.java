package com.example.diarioestudanteretrofit.ui.inserir;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.diarioestudanteretrofit.model.Estudante;
import com.example.diarioestudanteretrofit.util.EstudanteRepositorio;
import com.example.diarioestudanteretrofit.util.EstudanteRetrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InserirFrequenciaViewModel extends ViewModel {
    private final EstudanteRepositorio repositorio;
    private int estudanteId;
    private Boolean presencaSelecionada;

    public InserirFrequenciaViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    public void setEstudanteId(int id) {
        this.estudanteId = id;
    }

    public void setPresenca(boolean presenca) {
        this.presencaSelecionada = presenca;
    }

    public void salvarFrequencia(Runnable onSucesso) {
        if (presencaSelecionada == null) {
            return;
        }

        repositorio.buscarEstudantePorId(estudanteId).enqueue(new Callback<Estudante>() {
            @Override
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Estudante estudante = response.body();
                    estudante.getPresenca().add(presencaSelecionada);
                    atualizarEstudanteNoServidor(estudante, onSucesso);
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void atualizarEstudanteNoServidor(Estudante estudante, Runnable onSucesso) {
        repositorio.atualizarEstudante(estudanteId, estudante).enqueue(new Callback<Estudante>() {
            @Override
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                if (response.isSuccessful()) {
                    Log.d("FrequenciaVM", "Frequência atualizada com sucesso");
                    if (onSucesso != null) onSucesso.run();
                } else {
                    Log.e("FrequenciaVM", "Erro ao atualizar: " + response.code());
                    try {
                        Log.e("FrequenciaVM", "Erro body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                Log.e("FrequenciaVM", "Falha na chamada: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}




