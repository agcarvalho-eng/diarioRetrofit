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

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * O ViewModel responsável por carregar as informações de um estudante.
 * Ele se comunica com o repositório para buscar os dados do estudante por seu ID.
 */
public class DetalhesEstudanteViewModel extends ViewModel {

    private static final String TAG = "DetalhesEstudanteVM";
    private final MutableLiveData<Estudante> estudante = new MutableLiveData<>();
    private final EstudanteRepositorio repositorio;

    public DetalhesEstudanteViewModel() {
        // Inicializa o repositório para buscar os dados do estudante
        repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    /**
     * Retorna o LiveData que contém o estudante carregado.
     * Pode ser observado pela UI para atualizar a interface.
     */
    public LiveData<Estudante> getEstudante() {
        return estudante;
    }

    /**
     * Carrega os dados de um estudante a partir do ID fornecido.
     * Faz uma requisição de rede assíncrona para buscar o estudante no servidor.
     *
     * @param id O ID do estudante a ser carregado.
     */
    public void carregarEstudantePorId(int id) {
        repositorio.buscarEstudantePorId(id).enqueue(new Callback<Estudante>() {
            @Override
            // Método da interface Callback (objeto chamada original e resposta servidor)
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                if (response.isSuccessful()) {
                    Estudante estudanteResponse = response.body();
                    // Atualiza o LiveData com o estudante recebido
                    estudante.setValue(estudanteResponse);
                    Log.d(TAG, "Estudante carregado com sucesso: " + estudanteResponse);
                } else {
                    Log.e(TAG, "Erro na resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                Log.e(TAG, "Falha ao buscar estudante: " + t.getMessage(), t);
            }
        });
    }

    /**
     * Deletar um estudante a partir do ID fornecido.
     * Faz uma requisição de rede assíncrona para deletar o estudante no servidor.
     *
     * @param id O ID do estudante a ser carregado.
     */
    public void excluirEstudante(int id) {
        repositorio.deletarEstudante(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Estudante excluído com sucesso.");
                    // Pode usar um LiveData para notificar a UI
                    estudante.postValue(null); // ou criar outro LiveData se preferir
                } else {
                    Log.e(TAG, "Erro ao excluir estudante: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Falha ao excluir estudante: " + t.getMessage(), t);
            }
        });
    }

    /**
     * Recarrega os dados do estudante, se o estudante já estiver carregado.
     * Rechama a função para carregar o estudante usando o ID do estudante atual.
     */
    public void recarregarEstudante() {
        if (estudante.getValue() != null) {
            carregarEstudantePorId(estudante.getValue().getId());
        }
    }
}



