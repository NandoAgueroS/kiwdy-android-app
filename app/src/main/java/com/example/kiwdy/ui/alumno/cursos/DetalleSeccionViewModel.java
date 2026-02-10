package com.example.kiwdy.ui.alumno.cursos;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.request.MarcarSeccionCompletadaRequest;
import com.example.kiwdy.api.dto.response.CursoInscripcionResponse;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.dto.response.SeccionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleSeccionViewModel extends AndroidViewModel {

    private MutableLiveData<SeccionResponse> mSeccion;
    private MutableLiveData<InscripcionResponse> mInscripcion;
    private MutableLiveData<Boolean> mMostrarBotonMarcarCompletada;
    private MutableLiveData<String> mError;
    private MutableLiveData<Boolean> mMostrarBotonSiguiente;
    private MutableLiveData<Boolean> mMostrarBotonAnterior;
    private int ordenActual = 0;

    public DetalleSeccionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<SeccionResponse> getmSeccion(){
        if (mSeccion == null) {
           mSeccion = new MutableLiveData<>();
        }
        return mSeccion;
    }

    public LiveData<InscripcionResponse> getmInscripcion(){
        if (mInscripcion == null) {
            mInscripcion = new MutableLiveData<>();
        }
        return mInscripcion;
    }

    public LiveData<Boolean> getmMostrarBotonMarcarCompletada(){
        if (mMostrarBotonMarcarCompletada == null) {
            mMostrarBotonMarcarCompletada = new MutableLiveData<>();
        }
        return mMostrarBotonMarcarCompletada;
    }

    public LiveData<Boolean> getmMostrarBotonSiguiente(){
        if (mMostrarBotonSiguiente == null) {
            mMostrarBotonSiguiente = new MutableLiveData<>();
        }
        return mMostrarBotonSiguiente;
    }
    public LiveData<Boolean> getmMostrarBotonAnterior(){
        if (mMostrarBotonAnterior == null) {
            mMostrarBotonAnterior = new MutableLiveData<>();
        }
        return mMostrarBotonAnterior;
    }
    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public void recuperarCurso(Bundle arguments){
        if (arguments == null) return;
        int idCurso = arguments.getInt("idCurso", -1);
        if (idCurso == -1) return;
        buscarInscripcion(idCurso);
    }

    public void buscarInscripcion(int idCurso){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<InscripcionResponse> inscripcionCall = ApiClient.getInscripcionesService().buscarInscripcionPorCurso(token, idCurso);

        inscripcionCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful()) mInscripcion.postValue(response.body());
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {

            }
        });

    }

    public void siguienteSeccion() {
        ++ordenActual;
        mostrarSeccion(ordenActual);
    }
    public void retrocederSeccion() {
        --ordenActual;
        mostrarSeccion(ordenActual);
    }
    public void cargarSeccion(Bundle bundle){
        if (bundle != null && bundle.containsKey("orden")){
            ordenActual = bundle.getInt("orden");
        }else if (ordenActual == 0) {
                ordenActual= mInscripcion.getValue().getUltimaSeccionCompletada();
        }
        mostrarSeccion(ordenActual);
    }

    public void mostrarSeccion(int ordenSeccion){
        if (ordenActual== mInscripcion.getValue().getCurso().getSecciones().size()) {
            mMostrarBotonSiguiente.postValue(false);
        }else {
            mMostrarBotonSiguiente.postValue(true);
        }
        if (ordenActual== 1){
            mMostrarBotonAnterior.postValue(false);
        }else{
            mMostrarBotonAnterior.postValue(true);
        }
        if (ordenActual <= mInscripcion.getValue().getUltimaSeccionCompletada()) {
            mMostrarBotonMarcarCompletada.postValue(false);
        }else {
            mMostrarBotonMarcarCompletada.postValue(true);
        }
        int idCurso = mInscripcion.getValue().getCurso().getIdCurso();
        if (idCurso == 0) return;
        String token = SharedPreferencesUtil.leerToken(getApplication());
        SeccionResponse seccion = mInscripcion.getValue().getCurso().getSecciones().stream().filter(s -> s.getOrden() == ordenSeccion).findFirst().orElse(null);
        mSeccion.setValue(seccion);

        Call<SeccionResponse> seccionCall = ApiClient.getSeccionesService().buscar(token, idCurso, ordenSeccion);
/*
        seccionCall.enqueue(new Callback<SeccionResponse>() {
            @Override
            public void onResponse(Call<SeccionResponse> call, Response<SeccionResponse> response) {
                if (response.isSuccessful()) mSeccion.setValue(response.body());
                else mError.postValue("Error al recuperar la seccion: " + response.code());
            }

            @Override
            public void onFailure(Call<SeccionResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });*/
    }

    public void marcarCompletada(){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        if (ordenActual - 1 != mInscripcion.getValue().getUltimaSeccionCompletada()){
            mError.setValue("Primero tiene que completar las secciones anteriores");
            return;
        }
        MarcarSeccionCompletadaRequest seccion = new MarcarSeccionCompletadaRequest(mSeccion.getValue().getIdSeccion());
        int idInscripcion = mInscripcion.getValue().getIdInscripcion();

        Call<Void> seccionCompletadaCall = ApiClient.getInscripcionesService().marcarSeccionCompletada(token, idInscripcion, seccion);

        seccionCompletadaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mMostrarBotonMarcarCompletada.setValue(false);
                    mInscripcion.getValue().setUltimaSeccionCompletada(ordenActual);
                }
                else {
                    mError.postValue("Error al completar la seccion");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplication(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}