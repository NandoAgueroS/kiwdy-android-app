package com.example.kiwdy.api.service;

import com.example.kiwdy.api.dto.request.MarcarSeccionCompletadaRequest;
import com.example.kiwdy.api.dto.response.InscripcionResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InscripcionesService {
    @POST("inscripciones/curso/{idCurso}")
    Call<Void> inscribir(@Header("Authorization") String token, @Path("idCurso") int idCurso);
    @GET("inscripciones")
    Call<List<InscripcionResponse>> listarInscripciones(@Header("Authorization") String token, @Query("estado") int estado, @Query("idCurso") int idCurso);

    @GET("inscripciones/{idInscripcion}")
    Call<InscripcionResponse> buscarInscripcion(@Header("Authorization") String token, @Path("idInscripcion") int idInscripcion);

    @GET("inscripciones/{idInscripcion}/certificado")
    Call<ResponseBody> buscarCertificado(@Header("Authorization") String token, @Path("idInscripcion") int idInscripcion);

    @GET("inscripciones/curso/{idCurso}")
    Call<InscripcionResponse> buscarInscripcionPorCurso(@Header("Authorization") String token, @Path("idCurso") int idCurso);

    @PATCH("inscripciones/{idInscripcion}/estado/{estado}")
    Call<Void> actualizarEstado(@Header("Authorization") String token, @Path("idInscripcion") int idInscripcion,@Path("estado") int i);

    @POST("inscripciones/{idInscripcion}/secciones")
    Call<Void> marcarSeccionCompletada(@Header("Authorization") String token, @Path("idInscripcion") int idInscripcion, @Body MarcarSeccionCompletadaRequest seccion);
}
