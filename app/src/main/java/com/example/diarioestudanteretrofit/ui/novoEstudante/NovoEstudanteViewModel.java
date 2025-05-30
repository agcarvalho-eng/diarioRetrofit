package com.example.diarioestudanteretrofit.ui.novoEstudante;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.example.diarioestudanteretrofit.model.Estudante;
import com.example.diarioestudanteretrofit.util.EstudanteRepositorio;
import com.example.diarioestudanteretrofit.util.EstudanteRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovoEstudanteViewModel extends ViewModel {
    private final EstudanteRepositorio repositorio;
    private final MutableLiveData<String> nome = new MutableLiveData<>("");
    private final MutableLiveData<String> idade = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    public NovoEstudanteViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    public LiveData<String> getNome() {
        return nome;
    }

    public LiveData<String> getIdade() {
        return idade;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setNome(String nome) {
        this.nome.setValue(nome);
    }

    public void setIdade(String idade) {
        this.idade.setValue(idade);
    }

    public void salvarEstudante(Runnable onSuccess) {
        if (nome.getValue() == null || nome.getValue().isEmpty()) {
            errorMessage.setValue("Nome é obrigatório");
            return;
        }

        if (idade.getValue() == null || idade.getValue().isEmpty()) {
            errorMessage.setValue("Idade é obrigatória");
            return;
        }

        try {
            int idadeInt = Integer.parseInt(idade.getValue());
            if (idadeInt <= 0) {
                errorMessage.setValue("Idade deve ser positiva");
                return;
            }

            isLoading.setValue(true);
            Estudante novoEstudante = new Estudante(nome.getValue(), idadeInt);

            repositorio.criarEstudante(novoEstudante).enqueue(new Callback<Estudante>() {
                @Override
                public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful()) {
                        Log.d("NovoEstudanteVM", "Estudante criado com sucesso");
                        if (onSuccess != null) onSuccess.run();
                    } else {
                        errorMessage.setValue("Erro ao criar estudante: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Estudante> call, Throwable t) {
                    isLoading.setValue(false);
                    errorMessage.setValue("Falha na conexão: " + t.getMessage());
                    Log.e("NovoEstudanteVM", "Falha na chamada: " + t.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            errorMessage.setValue("Idade deve ser um número válido");
        }
    }

    public void limparErro() {
        errorMessage.setValue("");
    }
}