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

/**
 * ViewModel responsável pela lógica de criação de um novo estudante.
 * Ele gerencia os dados inseridos pelo usuário, validação dos campos e interação com o repositório para salvar o estudante.
 */
public class NovoEstudanteViewModel extends ViewModel {

    // Repositório para acessar os dados de estudantes via Retrofit
    private final EstudanteRepositorio repositorio;

    // MutableLiveData para armazenar os dados do estudante e o estado da UI
    private final MutableLiveData<String> nome = new MutableLiveData<>("");
    private final MutableLiveData<String> idade = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    /**
     * Construtor do ViewModel. Inicializa o repositório para realizar as operações de CRUD.
     */
    public NovoEstudanteViewModel() {
        this.repositorio = EstudanteRetrofit.getEstudanteRepositorio();
    }

    // Métodos getter para expor os LiveData

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

    /**
     * Define o nome do estudante a partir do que foi digitado pelo usuário.
     *
     * @param nome Nome do estudante.
     */
    public void setNome(String nome) {
        this.nome.setValue(nome);
    }

    /**
     * Define a idade do estudante a partir do que foi digitado pelo usuário.
     *
     * @param idade Idade do estudante.
     */
    public void setIdade(String idade) {
        this.idade.setValue(idade);
    }

    /**
     * Método que valida os dados do estudante e tenta salvá-lo no repositório.
     *
     * @param onSuccess Função a ser executada após o sucesso da operação.
     */
    public void salvarEstudante(Runnable onSuccess) {
        // Valida o nome
        if (nome.getValue() == null || nome.getValue().isEmpty()) {
            errorMessage.setValue("Nome é obrigatório");
            return;
        }

        // Valida a idade
        if (idade.getValue() == null || idade.getValue().isEmpty()) {
            errorMessage.setValue("Idade é obrigatória");
            return;
        }

        try {
            // Converte a idade para inteiro e valida se é positiva
            int idadeInt = Integer.parseInt(idade.getValue());
            if (idadeInt <= 0) {
                errorMessage.setValue("Idade deve ser positiva");
                return;
            }

            // Inicia o carregamento
            isLoading.setValue(true);

            // Cria o novo estudante com os dados fornecidos
            Estudante novoEstudante = new Estudante(nome.getValue(), idadeInt);

            // Realiza a chamada para salvar o estudante no servidor
            repositorio.criarEstudante(novoEstudante).enqueue(new Callback<Estudante>() {
                @Override
                public void onResponse(Call<Estudante> call, Response<Estudante> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful()) {
                        Log.d("NovoEstudanteVM", "Estudante criado com sucesso");
                        // Se a operação for bem-sucedida, executa o callback onSuccess
                        if (onSuccess != null) onSuccess.run();
                    } else {
                        // Se houver erro na resposta, exibe uma mensagem de erro
                        errorMessage.setValue("Erro ao criar estudante: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Estudante> call, Throwable t) {
                    // Se houver falha na chamada, exibe uma mensagem de erro
                    isLoading.setValue(false);
                    errorMessage.setValue("Falha na conexão: " + t.getMessage());
                    Log.e("NovoEstudanteVM", "Falha na chamada: " + t.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            // Se a idade não for um número válido, exibe uma mensagem de erro
            errorMessage.setValue("Idade deve ser um número válido");
        }
    }

    /**
     * Limpa a mensagem de erro exibida no UI.
     */
    public void limparErro() {
        errorMessage.setValue("");
    }
}
