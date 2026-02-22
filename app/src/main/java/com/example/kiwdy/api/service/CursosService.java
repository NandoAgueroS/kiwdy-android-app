package com.example.kiwdy.api.service;

import com.example.kiwdy.api.dto.response.CursoInscripcionResponse;
import com.example.kiwdy.api.dto.response.CursoResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CursosService {

    @POST("cursos")
    @Multipart
    Call<CursoResponse> crearCurso(@Header("Authorization") String token,
                                   @Part("titulo") RequestBody titulo,
                                   @Part("descripcion") RequestBody descripcion,
                                   @Part("precio") RequestBody precio,
                                   @Part("notaAprobacion") RequestBody notaAprobacion,
                                   @Part MultipartBody.Part portada);

    @GET("cursos/listar")
    Call<List<CursoResponse>> listarCursos(@Header("Authorization") String token);

    @GET("cursos/listar")
    Call<List<CursoResponse>> listarCursosPorTitulo(@Header("Authorization") String token, @Query("tituloCurso") String tituloCurso);

    @GET("cursos/listar/populares")
    Call<List<CursoResponse>> listarCursosPopulares(@Header("Authorization") String token);

    @GET("cursos/{idCurso}")
    Call<CursoResponse> buscarCurso(@Header("Authorization") String token, @Path("idCurso") int idCurso);

    @GET("cursos/{idCurso}/inscripcion-detalle")
    Call<CursoInscripcionResponse> buscarCursoInscripcion(@Header("Authorization") String token, @Path("idCurso") int idCurso);

}
