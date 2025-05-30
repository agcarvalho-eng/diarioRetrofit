package com.example.diarioestudanteretrofit.ui.estatisticas;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarioestudanteretrofit.model.Estudante;
import com.example.diarioestudanteretrofit.util.CalculoEstatisticas;
import com.example.diarioestudanteretrofit.util.EstudanteRepositorio;
import com.example.diarioestudanteretrofit.util.EstudanteRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstatisticasViewModel extends ViewModel {

    private static final String TAG = "EstatisticasViewModel";

    private final MutableLiveData<Double> mediaGeral = new MutableLiveData<>(0.0);
    private final MutableLiveData<String> maiorNota = new MutableLiveData<>("N/A");
    private final MutableLiveData<String> menorNota = new MutableLiveData<>("N/A");
    private final MutableLiveData<Double> mediaIdade = new MutableLiveData<>(0.0);
    private final MutableLiveData<List<Estudante>> aprovados = new MutableLiveData<>();
    private final MutableLiveData<List<Estudante>> reprovados = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final EstudanteRepositorio repositorio;

    public EstatisticasViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
        carregarEstatisticas();
    }

    public LiveData<Double> getMediaGeral() {
        return mediaGeral;
    }

    public LiveData<String> getMaiorNota() {
        return maiorNota;
    }

    public LiveData<String> getMenorNota() {
        return menorNota;
    }

    public LiveData<Double> getMediaIdade() {
        return mediaIdade;
    }

    public LiveData<List<Estudante>> getAprovados() {
        return aprovados;
    }

    public LiveData<List<Estudante>> getReprovados() {
        return reprovados;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private void carregarEstatisticas() {
        Log.d(TAG, "Iniciando carregamento das estatísticas...");
        isLoading.setValue(true);

        // Primeira busca para obter id, nome e idade dos estudantes
        repositorio.buscarEstudantes().enqueue(new Callback<List<Estudante>>() {
            @Override
            public void onResponse(Call<List<Estudante>> call, Response<List<Estudante>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Estudante> estudantes = response.body();
                    Log.d(TAG, "Estudantes recebidos: " + estudantes.size());

                    // Para cada estudante, buscar detalhes completos
                    buscarDetalhesEstudantes(estudantes);
                } else {
                    Log.w(TAG, "Resposta mal sucedida ou corpo nulo!");
                    limparDados();
                }
            }

            @Override
            public void onFailure(Call<List<Estudante>> call, Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "Erro ao buscar estudantes: " + t.getMessage(), t);
                limparDados();
            }
        });
    }

    private void buscarDetalhesEstudantes(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) {
            Log.w(TAG, "Lista de estudantes vazia!");
            return;
        }

        // Criar uma lista para armazenar os estudantes com os dados completos
        List<Estudante> estudantesCompletos = new ArrayList<>();
        for (Estudante estudante : estudantes) {
            // Buscar os dados completos de cada estudante
            repositorio.buscarEstudantePorId(estudante.getId()).enqueue(new Callback<Estudante>() {
                @Override
                public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Estudante estudanteCompleto = response.body();
                        estudantesCompletos.add(estudanteCompleto);

                        // Atualizar LiveData assim que todos os estudantes forem carregados
                        if (estudantesCompletos.size() == estudantes.size()) {
                            Log.d(TAG, "Todos os dados dos estudantes foram carregados.");
                            calcularEAtualizarEstatisticas(estudantesCompletos);
                        }
                    } else {
                        Log.w(TAG, "Falha ao buscar estudante com ID: " + estudante.getId());
                    }
                }

                @Override
                public void onFailure(Call<Estudante> call, Throwable t) {
                    Log.e(TAG, "Erro ao buscar estudante com ID: " + estudante.getId(), t);
                }
            });
        }
    }

    private void calcularEAtualizarEstatisticas(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) {
            Log.w(TAG, "Lista de estudantes vazia!");
            return;
        }

        double mediaG = CalculoEstatisticas.calcularMediaGeral(estudantes);
        Log.d(TAG, "Média geral calculada: " + mediaG);

        Estudante maior = CalculoEstatisticas.encontrarMaiorNota(estudantes);
        String maiorStr = maior != null ?
                String.format("%s (%.2f)", maior.getNome(), maior.calcularMedia()) : "N/A";
        Log.d(TAG, "Maior nota: " + maiorStr);

        Estudante menor = CalculoEstatisticas.encontrarMenorNota(estudantes);
        String menorStr = menor != null ?
                String.format("%s (%.2f)", menor.getNome(), menor.calcularMedia()) : "N/A";
        Log.d(TAG, "Menor nota: " + menorStr);

        double mediaI = CalculoEstatisticas.calcularMediaIdade(estudantes);
        Log.d(TAG, "Média de idade: " + mediaI);

        List<Estudante> aprovadosList = CalculoEstatisticas.getAprovados(estudantes);
        Log.d(TAG, "Total de aprovados: " + (aprovadosList != null ? aprovadosList.size() : 0));

        List<Estudante> reprovadosList = CalculoEstatisticas.getReprovados(estudantes);
        Log.d(TAG, "Total de reprovados: " + (reprovadosList != null ? reprovadosList.size() : 0));

        // Atualiza LiveData
        mediaGeral.setValue(mediaG);
        maiorNota.setValue(maiorStr);
        menorNota.setValue(menorStr);
        mediaIdade.setValue(mediaI);
        aprovados.setValue(aprovadosList);
        reprovados.setValue(reprovadosList);
    }

    private void limparDados() {
        mediaGeral.setValue(0.0);
        maiorNota.setValue("N/A");
        menorNota.setValue("N/A");
        mediaIdade.setValue(0.0);
        aprovados.setValue(null);
        reprovados.setValue(null);
    }
}


