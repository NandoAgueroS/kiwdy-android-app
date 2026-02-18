package com.example.kiwdy.api.service;


import com.example.kiwdy.api.dto.request.CrearSeccionRequest;
import com.example.kiwdy.api.dto.response.SeccionResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @GET("secciones/{orden}/curso/{idCurso}")
    Call<SeccionResponse> buscar(@Header("Authorization") String token, @Path("idCurso") int idCurso,@Path("orden") int ordenSeccion);

    @GET("secciones/material/{idMaterial}")
    Call<ResponseBody> descargarMaterial(@Header("Authorization") String token, @Path("idMaterial") int idMaterial);
}
