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

/**
 * ViewModel responsável por fornecer dados e lógica de negócios
 * para a tela principal (HomeFragment) que exibe a lista de estudantes.
 */
public class HomeViewModel extends ViewModel implements LifecycleObserver {

    // LiveData que mantém o texto do título da tela
    private final MutableLiveData<String> mText = new MutableLiveData<>("Lista de Estudantes");

    // LiveData que contém a lista de estudantes recuperada do repositório
    private final MutableLiveData<List<Estudante>> estudantes = new MutableLiveData<>();

    // Repositório para acesso à API de estudantes
    private final EstudanteRepositorio repositorio;

    /**
     * Construtor do ViewModel.
     * Inicializa o repositório e carrega os estudantes assim que criado.
     */
    public HomeViewModel() {
        repositorio = EstudanteRetrofit.getEstudanteRepositorio();
        carregarEstudantes();
    }

    /**
     * Retorna o LiveData com o texto exibido no topo da tela.
     */
    public LiveData<String> getText() {
        return mText;
    }

    /**
     * Retorna o LiveData contendo a lista atual de estudantes.
     */
    public LiveData<List<Estudante>> getEstudantes() {
        return estudantes;
    }

    /**
     * Realiza uma chamada assíncrona ao repositório para buscar os estudantes.
     * Atualiza os LiveData com os resultados ou mensagens de erro.
     */
    public void carregarEstudantes() {
        repositorio.buscarEstudantes().enqueue(new Callback<List<Estudante>>() {
            @Override
            // Método da interface Callback (objeto chamada original e resposta servidor)
            public void onResponse(Call<List<Estudante>> call, Response<List<Estudante>> response) {
                if (response.isSuccessful()) {
                    // Atualiza a lista se a resposta for bem-sucedida
                    estudantes.setValue(response.body());
                } else {
                    // Mostra mensagem de erro com o código da resposta
                    mText.setValue("Erro ao carregar estudantes: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Estudante>> call, Throwable t) {
                // Mostra mensagem de falha de conexão
                mText.setValue("Falha na conexão: " + t.getMessage());
            }
        });
    }
}
