package com.example.kiwdy.ui.alumno.mis_inscripciones;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisIncripcionesViewModel extends AndroidViewModel {
    private MutableLiveData<List<CursoResponse>> mCursos;
    private MutableLiveData<String> mError;

    public MisIncripcionesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<CursoResponse>> getmCursos(){
        if (mCursos == null) {
            mCursos = new MutableLiveData<>();
        }
        return mCursos;
    }

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public void buscarInscripcionesPorTitulo(String titulo){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        Call<List<InscripcionResponse>> inscrpcionesCall;
        if (titulo.isBlank()){
            inscrpcionesCall = ApiClient.getInscripcionesService().listarInscripcionesDelAlumno(token);
        }else{
            inscrpcionesCall = ApiClient.getInscripcionesService().listarInscripcionesDelAlumnoPorTituloDelCurso(token, titulo);
        }

        inscrpcionesCall.enqueue(new Callback<List<InscripcionResponse>>() {
            @Override
            public void onResponse(Call<List<InscripcionResponse>> call, Response<List<InscripcionResponse>> response) {
                if (response.isSuccessful()){
                    extraerCursosDeInscripciones(response.body());
                }else{
                    mError.postValue("Ocurrió un error al recuperar las inscripciones");
                }
            }

            @Override
            public void onFailure(Call<List<InscripcionResponse>> call, Throwable t) {

                mError.postValue("Ocurrió un error inesperado al recuperar las inscripciones");
                Log.d("API_ERROR", "Error al recuperar las inscripciones", t);
            }
        });
    }

    public void extraerCursosDeInscripciones(List<InscripcionResponse> inscripciones){
        if (inscripciones == null){
            mCursos.postValue(new LinkedList<>());
        }else{
            mCursos.postValue(
                    inscripciones.stream().map(i -> i.getCurso()).collect(Collectors.toList())
            );
        }

    }
}