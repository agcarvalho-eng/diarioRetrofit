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

/**
 * ViewModel responsável por gerenciar os dados relacionados à inserção de notas de um estudante.
 */
public class InserirNotaViewModel extends ViewModel {

    // LiveData que mantém o valor da nota inserida pelo usuário
    public final MutableLiveData<String> nota = new MutableLiveData<>("");

    // ID do estudante, usado para buscar e atualizar o estudante
    private int estudanteId;

    // Repositório responsável pelas operações de rede para buscar e atualizar estudantes
    private final EstudanteRepositorio repositorio;

    /**
     * Construtor do ViewModel que inicializa o repositório de estudantes.
     */
    public InserirNotaViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    /**
     * Define o ID do estudante para realizar operações de busca e atualização.
     *
     * @param id ID do estudante
     */
    public void setEstudanteId(int id) {
        this.estudanteId = id;
    }

    /**
     * Salva a nota do estudante, validando e realizando a atualização do estudante no servidor.
     * @param onSucesso Runnable a ser executado caso a operação seja bem-sucedida
     */
    public void salvarNota(Runnable onSucesso) {
        // Verifica se a nota inserida é válida (não nula e não vazia)
        if (nota.getValue() == null || nota.getValue().isEmpty()) {
            return; // Se a nota for inválida, sai do método sem realizar nada
        }

        double novaNota;
        // Tenta converter a nota para um número (Double), se falhar, não faz nada
        try {
            novaNota = Double.parseDouble(nota.getValue());
        } catch (NumberFormatException e) {
            return; // Se a conversão falhar, sai do método
        }

        // Realiza a busca do estudante no servidor
        repositorio.buscarEstudantePorId(estudanteId).enqueue(new Callback<Estudante>() {
            @Override
            // Método da interface Callback (objeto chamada original e resposta servidor)
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                // Se a resposta for bem-sucedida, e o estudante não for nulo
                if (response.isSuccessful() && response.body() != null) {
                    Estudante estudante = response.body();
                    // Adiciona a nova nota à lista de notas do estudante
                    estudante.getNotas().add(novaNota);
                    // Atualiza o estudante no servidor com a nova nota
                    atualizarEstudanteNoServidor(estudante, onSucesso);
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                t.printStackTrace(); // Em caso de erro na requisição, exibe o erro
            }
        });
    }

    /**
     * Atualiza os dados do estudante no servidor após a modificação da nota.
     * @param estudante O estudante com a nova nota a ser atualizada
     * @param onSucesso Runnable a ser executado após a atualização bem-sucedida
     */
    private void atualizarEstudanteNoServidor(Estudante estudante, Runnable onSucesso) {
        // Envia a atualização para o servidor
        repositorio.atualizarEstudante(estudanteId, estudante).enqueue(new Callback<Estudante>() {
            @Override
            // Método da interface Callback (objeto chamada original e resposta servidor)
            public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                // Se a atualização for bem-sucedida, executa a ação de sucesso
                if (response.isSuccessful()) {
                    Log.d("NotaVM", "Nota atualizada com sucesso!");
                    if (onSucesso != null) onSucesso.run();
                } else {
                    // Se a atualização falhar, registra o erro
                    Log.e("NotaVM", "Erro ao atualizar: " + response.code());
                    try {
                        Log.e("NotaVM", "Erro body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace(); // Em caso de erro ao ler o corpo da resposta
                    }
                }
            }

            @Override
            public void onFailure(Call<Estudante> call, Throwable t) {
                // Registra o erro caso falhe na comunicação com o servidor
                Log.e("NotaVM", "Falha na chamada: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}

