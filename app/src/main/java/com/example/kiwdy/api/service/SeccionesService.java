package com.example.kiwdy.api.service;


import com.example.kiwdy.api.dto.request.CrearSeccionRequest;
import com.example.kiwdy.api.dto.response.SeccionResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SeccionesService {

    @POST("secciones")
    @Multipart
    Call<SeccionResponse> crearSeccion(@Header("Authorization") String token,
                                       @Part("idCurso") RequestBody idCurso,
                                       @Part("titulo") RequestBody titulo,
                                       @Part("contenido") RequestBody contenido,
                                       @Part("orden") RequestBody orden,
                                       @Part MultipartBody.Part video,
                                       @Part List<MultipartBody.Part> materialExtra);
}
