package com.example.diarioestudanteretrofit.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe responsável por configurar e fornecer uma instância do Retrofit para fazer chamadas à API.
 * Utiliza um client inseguro (não recomendado para produção) para facilitar o desenvolvimento local.
 */
public class EstudanteRetrofit {

    // URL base da API. Para testes locais com Android, utiliza-se o IP do localhost (10.0.2.2).
    private static final String BASE_URL = "https://10.0.2.2:8080/estudantes/";

    // Instância do Retrofit que será reutilizada.
    private static Retrofit retrofit;

    /**
     * Método responsável por obter a instância do repositório de estudantes.
     * Se a instância do Retrofit ainda não foi criada, ela é configurada e inicializada aqui.
     *
     * @return Uma instância de EstudanteRepositorio para realizar as chamadas à API.
     */
    public static EstudanteRepositorio getEstudanteRepositorio() {
        if (retrofit == null) {
            // Se o Retrofit ainda não foi inicializado, configura e cria a instância.
            retrofit = new Retrofit.Builder()
                    // Define a URL base para a API
                    .baseUrl(BASE_URL)
                    // Adiciona o GsonConverterFactory para converter objetos JSON em objetos Java
                    .addConverterFactory(GsonConverterFactory.create())
                    // Configura o cliente OkHttp inseguro (não recomendado para produção)
                    .client(OkHttpClient.obterOkHttpClientInseguro())
                    .build();
        }
        // Retorna a instância do repositório, que permitirá realizar as operações de CRUD
        return retrofit.create(EstudanteRepositorio.class);
    }
}

