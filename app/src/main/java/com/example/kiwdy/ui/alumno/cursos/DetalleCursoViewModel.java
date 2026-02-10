package com.example.kiwdy.ui.alumno.cursos;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.kiwdy.R;
import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoInscripcionResponse;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleCursoViewModel extends AndroidViewModel {

    private MutableLiveData<CursoResponse> mCurso;
    private MutableLiveData<CursoResponse> mNavegarASeccion;
    private MutableLiveData<Boolean> mMostrarBtResumir;
    private MutableLiveData<Boolean> mMostrarBtInscribir;

    public DetalleCursoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<CursoResponse> getmCurso(){
        if (mCurso == null) {
            mCurso = new MutableLiveData<>();
        }
        return mCurso;
    }

    public LiveData<CursoResponse> getmNavegarASeccion(){
        if (mNavegarASeccion == null) {
            mNavegarASeccion = new MutableLiveData<>();
        }
        return mNavegarASeccion;
    }
    public LiveData<Boolean> getmMostrarBtResumir(){
        if (mMostrarBtResumir == null) {
            mMostrarBtResumir = new MutableLiveData<>();
        }
        return mMostrarBtResumir;
    }

    public LiveData<Boolean> getmMostrarBtInscribir(){
        if (mMostrarBtInscribir == null) {
            mMostrarBtInscribir = new MutableLiveData<>();
        }
        return mMostrarBtInscribir;
    }


    public void mostrarCurso(Bundle arguments){
        if (arguments == null) return;
        int idCurso = arguments.getInt("idCurso");

        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<CursoResponse> buscarCursoCall = ApiClient.getCursosService().buscarCurso(token, idCurso);

        buscarCursoCall.enqueue(new Callback<CursoResponse>() {
            @Override
            public void onResponse(Call<CursoResponse> call, Response<CursoResponse> response) {
               if (response.isSuccessful()) {
                   mCurso.postValue(response.body());
                   if (!response.body().isEstaInscripto()){
                       mMostrarBtInscribir.postValue(true);
                   }else if (!response.body().isEstaFinalizado()){
                       mMostrarBtResumir.postValue(true);
                   }
               }
               else Toast.makeText(getApplication(), "Error al recuperar el curso: " + response.code(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CursoResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void inscribir() {
        String token =  SharedPreferencesUtil.leerToken(getApplication());
        int idCurso = mCurso.getValue().getIdCurso();

        Call<Void> inscribirCall = ApiClient.getInscripcionesService().inscribir(token, idCurso);
        inscribirCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    Toast.makeText(getApplication(), "Inscripto correctamente: " + response.code(), Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplication(), "Error al recuperar el curso: " + response.code(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void mostrarSecciones() {
        mNavegarASeccion.setValue(mCurso.getValue());
    }

    public void limpiarMutables() {
        mNavegarASeccion = null;
    }
}