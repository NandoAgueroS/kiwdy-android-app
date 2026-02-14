package com.example.kiwdy.api.service;

import com.example.kiwdy.api.dto.request.CargarNotaRequest;
import com.example.kiwdy.api.dto.request.CrearExamenRequest;
import com.example.kiwdy.api.dto.response.ExamenResponse;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExamenesService {

    @POST("examenes")
    public Call<ExamenResponse> crear(@Header("Authorization") String token, @Body CrearExamenRequest examen);

    @GET("examenes")
    public Call<List<ExamenResponse>> listar(@Header("Authorization") String token);

    @GET("examenes")
    public Call<List<ExamenResponse>> listar(@Header("Authorization") String token, @Query("posterioresA") LocalDateTime fechaYHora);

    @GET("examenes")
    public Call<List<ExamenResponse>> listar(@Header("Authorization") String token, @Query("idCurso") int idCurso);

    @GET("examenes")
    public Call<List<ExamenResponse>> listar(@Header("Authorization") String token, @Query("rendido") boolean rendido);

    @GET("examenes/{idExamen}")
    public Call<ExamenResponse> buscar(@Header("Authorization") String token, @Path("idExamen") int idExamen);

    @PATCH("examenes/{idExamen}")
    public Call<ExamenResponse> cargarNota(@Header("Authorization") String token, @Path("idExamen") int idExamen, @Body CargarNotaRequest nota);
}
