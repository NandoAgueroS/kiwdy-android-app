package com.example.kiwdy.api.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InscripcionesService {
    @POST("inscripciones/{idCurso}")
    Call<Void> inscribir(@Header("Authorization") String token, @Path("idCurso") int idCurso);
}
