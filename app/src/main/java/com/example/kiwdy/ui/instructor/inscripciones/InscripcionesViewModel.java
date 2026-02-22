package com.example.kiwdy.ui.instructor.inscripciones;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.EstadoInscripcion;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscripcionesViewModel extends AndroidViewModel {

    private MutableLiveData<List<InscripcionResponse>> mInscripciones;
    private MutableLiveData<Integer> mIdCurso;
    private MutableLiveData<String> mError;
    private MutableLiveData<String> mMostrandoInscripcionesPorCurso;
    private MutableLiveData<Boolean> mMostrandoTodasLasInscripciones;

    public InscripcionesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<InscripcionResponse>> getmInscripciones(){
        if (mInscripciones == null) {
           mInscripciones = new MutableLiveData<>();
        }
        return mInscripciones;
    }
    public LiveData<Integer> getmIdCurso(){
        if (mIdCurso == null) {
            mIdCurso = new MutableLiveData<>();
        }
        return mIdCurso;
    }

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<String> getmMostrandoInscripcionesPorCurso(){
        if (mMostrandoInscripcionesPorCurso == null) {
           mMostrandoInscripcionesPorCurso = new MutableLiveData<>();
        }
        return mMostrandoInscripcionesPorCurso;
    }

    public LiveData<Boolean> getmMostrandoTodasLasInscripciones(){
        if (mMostrandoTodasLasInscripciones == null) {
            mMostrandoTodasLasInscripciones = new MutableLiveData<>();
        }
        return mMostrandoTodasLasInscripciones;
    }

    public void recuperarIdCurso(Bundle arguments){
        if (arguments != null && arguments.containsKey("idCurso") && arguments.containsKey("tituloCurso")){
            int idCurso = arguments.getInt("idCurso");
            String tituloCurso = arguments.getString("tituloCurso");
            mIdCurso.setValue(idCurso);
            mMostrandoInscripcionesPorCurso.setValue(tituloCurso);
        }else{
            mMostrandoTodasLasInscripciones.setValue(true);
        }
    }

    public void listarInscripciones(String estado, boolean seleccionado){
        if (!seleccionado) return;
        String token = SharedPreferencesUtil.leerToken(getApplication());
        int estadoInt = Integer.parseInt(estado);
        Log.d("INSCRIPCION", estado);
        Call<List<InscripcionResponse>> inscripcionesCall;
        if (mIdCurso.isInitialized()){
             inscripcionesCall = ApiClient.getInscripcionesService().listarInscripciones(token, estadoInt, mIdCurso.getValue());
        }else{
            inscripcionesCall = ApiClient.getInscripcionesService().listarInscripciones(token, estadoInt);
        }
        inscripcionesCall.enqueue(new Callback<List<InscripcionResponse>>() {
            @Override
            public void onResponse(Call<List<InscripcionResponse>> call, Response<List<InscripcionResponse>> response) {
                if (response.isSuccessful()) {
                    mInscripciones.postValue(response.body());
                }
                else mError.postValue("Error al obtener las inscripciones: " + response.code());
            }

            @Override
            public void onFailure(Call<List<InscripcionResponse>> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado al obtener las inscripciones");
                Log.d("API_ERROR", "Error al obtener las inscripciones", t);
            }
        });
    }
}