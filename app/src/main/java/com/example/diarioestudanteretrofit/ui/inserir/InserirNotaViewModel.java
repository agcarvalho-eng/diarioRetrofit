package com.example.diarioestudanteretrofit.ui.inserir;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarioestudanteretrofit.model.Estudante;
import com.example.diarioestudanteretrofit.util.EstudanteRepositorio;
import com.example.diarioestudanteretrofit.util.EstudanteRetrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InserirNotaViewModel extends ViewModel {
    public final MutableLiveData<String> nota = new MutableLiveData<>("");
    private int estudanteId;
    private final EstudanteRepositorio repositorio;

    public InserirNotaViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    public void setEstudanteId(int id) {
        this.estudanteId = id;
    }

    public void salvarNota(Runnable onSucesso) {
        if (nota.getValue() == null || nota.getValue().isEmpty()) {
            return;
        }

        double novaNota;
        try {
            novaNota = Double.parseDouble(nota.getValue());
        } catch (NumberFormatException e) {
            return;
        }

        repositorio.buscarEstudantePorId(estudanteId).enqueue(new Callback<Estudante>() {
            @Override
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Estudante estudante = response.body();
                    estudante.getNotas().add(novaNota);
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
                    Log.d("NotaVM", "Nota atualizada com sucesso");
                    if (onSucesso != null) onSucesso.run();
                } else {
                    Log.e("NotaVM", "Erro ao atualizar: " + response.code());
                    try {
                        Log.e("NotaVM", "Erro body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                Log.e("NotaVM", "Falha na chamada: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
