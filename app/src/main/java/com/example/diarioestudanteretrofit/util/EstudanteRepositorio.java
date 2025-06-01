package com.example.diarioestudanteretrofit.util;

import com.example.diarioestudanteretrofit.model.Estudante;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EstudanteRepositorio {

    @GET("./")
    Call<List<Estudante>> buscarEstudantes();

    @GET("{id}")
    Call<Estudante> buscarEstudantePorId(@Path("id") int id);

    @PUT("{id}") // Atualizar um recurso
    Call<Estudante> atualizarEstudante(@Path("id") int id, @Body Estudante estudante);

    @POST("./") // Criar um recurso
    Call<Estudante> criarEstudante(@Body Estudante estudante);

    @DELETE("{id}")
    Call<Void> deletarEstudante(@Path("id") int id);
}
