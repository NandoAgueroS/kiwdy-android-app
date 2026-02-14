package com.example.kiwdy.api;

import com.example.kiwdy.BuildConfig;
import com.example.kiwdy.api.service.CursosService;
import com.example.kiwdy.api.service.ExamenesService;
import com.example.kiwdy.api.service.InscripcionesService;
import com.example.kiwdy.api.service.SeccionesService;
import com.example.kiwdy.api.service.UsuariosService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    public static final String URL_BASE = BuildConfig.URL_BASE_API;

    private static Retrofit getRetrofit(){
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE + "/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static UsuariosService getUsuariosService(){
        return getRetrofit().create(UsuariosService.class);
    }

    public static CursosService getCursosService(){
        return getRetrofit().create(CursosService.class);
    }

    public static SeccionesService getSeccionesService(){
        return getRetrofit().create(SeccionesService.class);
    }

    public static InscripcionesService getInscripcionesService() {
        return getRetrofit().create(InscripcionesService.class);
    }

    public static ExamenesService getExamenesService() {
        return getRetrofit().create(ExamenesService.class);
    }
}
