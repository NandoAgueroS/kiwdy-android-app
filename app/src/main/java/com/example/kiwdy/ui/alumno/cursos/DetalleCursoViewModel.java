package com.example.kiwdy.ui.alumno.cursos;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
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
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleCursoViewModel extends AndroidViewModel {

    private MutableLiveData<CursoResponse> mCurso;
    private MutableLiveData<CursoResponse> mNavegarASeccion;
    private MutableLiveData<Integer> mNavegarAProgreso;
    private MutableLiveData<Boolean> mMostrarBtResumir;
    private MutableLiveData<Boolean> mMostrarBtInscribir;
    private MutableLiveData<Boolean> mOcultarBtInscribir;
    private MutableLiveData<Boolean> mMostrarBtVerProgreso;
    private MutableLiveData<Boolean> mHabilitarOnClickSeccion;
    private MutableLiveData<InscripcionResponse> mInscripcionSolicitada;
    private MutableLiveData<String> mRequiereExamen;
    private MutableLiveData<String> mError;

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

    public LiveData<Integer> getmNavegarAProgreso(){
        if (mNavegarAProgreso == null) {
            mNavegarAProgreso = new MutableLiveData<>();
        }
        return mNavegarAProgreso;
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

    public LiveData<Boolean> getmOcultarBtInscribir(){
        if (mOcultarBtInscribir == null) {
            mOcultarBtInscribir = new MutableLiveData<>();
        }
        return mOcultarBtInscribir;
    }

    public LiveData<Boolean> getmMostrarBtVerProgreso(){
        if (mMostrarBtVerProgreso == null) {
            mMostrarBtVerProgreso = new MutableLiveData<>();
        }
        return mMostrarBtVerProgreso;
    }

    public LiveData<Boolean> getmHabilitarOnClickSeccion(){
        if (mHabilitarOnClickSeccion == null) {
            mHabilitarOnClickSeccion = new MutableLiveData<>();
        }
        return mHabilitarOnClickSeccion;
    }

    public LiveData<InscripcionResponse> getmInscripcionSolicitada(){
        if (mInscripcionSolicitada == null) {
            mInscripcionSolicitada = new MutableLiveData<>();
        }
        return mInscripcionSolicitada;
    }

    public LiveData<String> getmRequiereExamen(){
        if (mRequiereExamen == null) {
            mRequiereExamen = new MutableLiveData<>();
        }
        return mRequiereExamen;
    }

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
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
                   if (response.body().getNotaAprobacion() == -1){
                       mRequiereExamen.postValue("No requiere exámen");
                   }else{
                       mRequiereExamen.postValue("Requiere aprobar un exámen");
                   }
                   buscarInscripcion(idCurso);
               }
               else Toast.makeText(getApplication(), "Error al recuperar el curso: " + response.code(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CursoResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void manejarVistaSegunEstadoInscripcion(InscripcionResponse inscripcion){
        if (inscripcion == null) {
            mMostrarBtInscribir.postValue(true);
        }else if (inscripcion.getEstado().equals("Solicitada")){
            mInscripcionSolicitada.postValue(inscripcion);
            mOcultarBtInscribir.postValue(true);
        }else{
            mHabilitarOnClickSeccion.postValue(true);
            mMostrarBtVerProgreso.postValue(true);
            mOcultarBtInscribir.postValue(true);
        }
        if (inscripcion != null && !inscripcion.getEstado().equals("Solicitada") && !inscripcion.getEstado().equals("Certificada")){
            mMostrarBtResumir.postValue(true);
        }
    }
    public void buscarInscripcion(int idCurso){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<InscripcionResponse> inscripcionCall = ApiClient.getInscripcionesService().buscarInscripcionPorCurso(token, idCurso);

        inscripcionCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful()){
                    manejarVistaSegunEstadoInscripcion(response.body());
                }else if (response.code() == 404){
                    manejarVistaSegunEstadoInscripcion(null);
                }else{
                    mError.postValue("Ocurrió un error al verificar la inscripción");
                }
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado al verificar la inscripción");
                Log.d("API_ERROR", "Error al recuperar la inscripción", t);
            }
        });
    }

    public void inscribir() {
        String token =  SharedPreferencesUtil.leerToken(getApplication());
        int idCurso = mCurso.getValue().getIdCurso();

        Call<InscripcionResponse> inscribirCall = ApiClient.getInscripcionesService().inscribir(token, idCurso);
        inscribirCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful()){
                    buscarInscripcion(idCurso);
                    Toast.makeText(getApplication(), "Inscripto correctamente: " + response.code(), Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getApplication(), "Error al recuperar el curso: " + response.code(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void mostrarSecciones() {
        mNavegarASeccion.setValue(mCurso.getValue());
    }

    public void mostrarProgreso() {
       mNavegarAProgreso.setValue(mCurso.getValue().getIdCurso());
    }

    public void limpiarMutables() {
        mNavegarASeccion = null;
        mNavegarAProgreso = null;
    }
}