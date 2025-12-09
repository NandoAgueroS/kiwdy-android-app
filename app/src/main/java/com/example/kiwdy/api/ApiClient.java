package com.example.kiwdy.api;

import com.example.kiwdy.BuildConfig;
import com.example.kiwdy.api.service.UsuariosService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    public static final String URL_BASE = BuildConfig.URL_BASE_API + "api/";

    private static Retrofit getRetrofit(){
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static UsuariosService getUsuariosService(){
        return getRetrofit().create(UsuariosService.class);
    }

}
