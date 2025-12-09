package com.example.kiwdy.api.service;

import com.example.kiwdy.api.dto.request.CrearUsuarioRequest;
import com.example.kiwdy.api.dto.request.LoginRequest;
import com.example.kiwdy.api.dto.response.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UsuariosService {

    @POST("usuarios/login")
    Call<String> login(@Body LoginRequest loginRequest);

    @POST("usuarios")
    Call<UsuarioResponse> registro(@Body CrearUsuarioRequest usuarioRequest);

    @GET("usuarios/perfil")
    Call<UsuarioResponse> getPerfil(@Header("Authorization") String token);

    @GET("usuarios/token-valido")
    Call<Void> validarToken(@Header("Authorization") String token);

}
