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

/**
 * ViewModel responsável por gerenciar a lógica de inserir e salvar a frequência de um estudante.
 */
public class InserirFrequenciaViewModel extends ViewModel {

    private final EstudanteRepositorio repositorio;
    private int estudanteId;               // ID do estudante para o qual a frequência será salva
    private Boolean presencaSelecionada;    // Presença selecionada (presente ou ausente)

    /**
     * Construtor, inicializa o repositório do estudante.
     */
    public InserirFrequenciaViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    /**
     * Define o ID do estudante que terá a frequência registrada.
     *
     * @param id ID do estudante
     */
    public void setEstudanteId(int id) {
        this.estudanteId = id;
    }

    /**
     * Define a presença selecionada (se o estudante está presente ou ausente).
     *
     * @param presenca Booleano indicando a presença (true para presente, false para ausente)
     */
    public void setPresenca(boolean presenca) {
        this.presencaSelecionada = presenca;
    }

    /**
     * Salva a frequência do estudante. Se a presença não foi selecionada, não faz nada.
     *
     * @param onSucesso Runnable que será executado após a frequência ser salva com sucesso
     */
    public void salvarFrequencia(Runnable onSucesso) {
        // Se a presença não foi selecionada, não faz nada
        if (presencaSelecionada == null) {
            return;
        }

        // Busca o estudante pelo ID para adicionar a presença selecionada
        repositorio.buscarEstudantePorId(estudanteId).enqueue(new Callback<Estudante>() {
            @Override
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                // Se a resposta for bem-sucedida e o corpo não for nulo, adiciona a presença
                if (response.isSuccessful() && response.body() != null) {
                    Estudante estudante = response.body();
                    estudante.getPresenca().add(presencaSelecionada);  // Adiciona a presença na lista do estudante
                    atualizarEstudanteNoServidor(estudante, onSucesso);  // Atualiza o estudante no servidor
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                // Se a requisição falhar, imprime o erro
                t.printStackTrace();
            }
        });
    }

    /**
     * Atualiza as informações do estudante no servidor.
     *
     * @param estudante O estudante com a presença já registrada
     * @param onSucesso Runnable a ser executado se a atualização for bem-sucedida
     */
    private void atualizarEstudanteNoServidor(Estudante estudante, Runnable onSucesso) {
        // Realiza a atualização do estudante no servidor
        repositorio.atualizarEstudante(estudanteId, estudante).enqueue(new Callback<Estudante>() {
            @Override
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                // Se a atualização for bem-sucedida, executa o callback onSucesso
                if (response.isSuccessful()) {
                    Log.d("FrequenciaVM", "Frequência atualizada com sucesso");
                    if (onSucesso != null) onSucesso.run();
                } else {
                    // Caso ocorra um erro na atualização, imprime o código de erro
                    Log.e("FrequenciaVM", "Erro ao atualizar: " + response.code());
                    try {
                        // Imprime a mensagem de erro do corpo da resposta
                        Log.e("FrequenciaVM", "Erro body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                // Se a requisição falhar, imprime o erro
                Log.e("FrequenciaVM", "Falha na chamada: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}





