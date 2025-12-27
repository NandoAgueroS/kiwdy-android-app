package com.example.kiwdy.api.service;

import com.example.kiwdy.api.dto.request.CrearCursoRequest;
import com.example.kiwdy.api.dto.response.CursoResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CursosService {

    @POST("cursos")
    @Multipart
    Call<CursoResponse> crearCurso(@Header("Authorization") String token,
                                   @Part("titulo") RequestBody titulo,
                                   @Part("descripcion") RequestBody descripcion,
                                   @Part("precio") RequestBody precio,
                                   @Part MultipartBody.Part portada);
}
