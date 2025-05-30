package com.example.diarioestudanteretrofit.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstudanteRetrofit {

    private static final String BASE_URL = "https://10.0.2.2:8080/estudantes/";
    private static Retrofit retrofit;

    public static EstudanteRepositorio getEstudanteRepositorio() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.obterOkHttpClientInseguro())
                    .build();
        }
        return retrofit.create(EstudanteRepositorio.class);
    }
}
