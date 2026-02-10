package com.example.kiwdy.ui.instructor.inscripciones;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgresoAlumnoViewModel extends AndroidViewModel {
    private MutableLiveData<InscripcionResponse> mEstadoSolicitada;
    private MutableLiveData<InscripcionResponse> mEstadoEnCurso;
    private MutableLiveData<InscripcionResponse> mEstadoPendienteCertificacion;
    private MutableLiveData<InscripcionResponse> mEstadoCertificada;
    private MutableLiveData<Integer> mProgreso;

    public ProgresoAlumnoViewModel(@NonNull Application application) {
        super(application);
    }

   public LiveData<InscripcionResponse> getmEstadoSolicitada(){
       if (mEstadoSolicitada == null) {
          mEstadoSolicitada = new MutableLiveData<>();
       }
       return mEstadoSolicitada;
   }

    public LiveData<InscripcionResponse> getmEstadoEnCurso(){
        if (mEstadoEnCurso == null) {
            mEstadoEnCurso = new MutableLiveData<>();
        }
        return mEstadoEnCurso;
    }

    public LiveData<InscripcionResponse> getmEstadoPendienteCertificacion(){
        if (mEstadoPendienteCertificacion == null) {
            mEstadoPendienteCertificacion = new MutableLiveData<>();
        }
        return mEstadoPendienteCertificacion;
    }

    public LiveData<InscripcionResponse> getmEstadoCertificada(){
        if (mEstadoCertificada == null) {
            mEstadoCertificada = new MutableLiveData<>();
        }
        return mEstadoCertificada;
    }

    public LiveData<Integer> getmProgreso(){
        if (mProgreso == null) {
            mProgreso = new MutableLiveData<>();
        }
        return mProgreso;
    }

    public void buscarInscripcion(Bundle arguments){
        if (arguments == null || !arguments.containsKey("idInscripcion")) return;

        int idInscripcion = arguments.getInt("idInscripcion");

        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<InscripcionResponse> inscripcionCall = ApiClient.getInscripcionesService().buscarInscripcion(token, idInscripcion);

        inscripcionCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful())
                    switch (response.body().getEstado()){
                        case "Solicitada": mEstadoSolicitada.postValue(response.body());
                            break;
                        case "EnCurso": {
                            mEstadoEnCurso.postValue(response.body());

                            mProgreso.postValue(calcularProgreso(response.body()));
                    }
                            break;
                        case "PendienteCertificacion": mEstadoPendienteCertificacion.postValue(response.body());
                            break;
                        case "Certificada": mEstadoCertificada.postValue(response.body());
                            break;
                    }
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {

            }
        });
    }
    private int calcularProgreso(InscripcionResponse inscripcion){
        int ultimaSeccionCompletada = inscripcion.getUltimaSeccionCompletada();
        int totalSecciones = inscripcion.getCurso().getSecciones().size();
        float result = (( (float) ultimaSeccionCompletada / totalSecciones) * 100);
        return (int) result;

    }

    public void aceptarInscripcion() {
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<Void> actualizarEstadoCall = ApiClient.getInscripcionesService().actualizarEstado(token, mEstadoSolicitada.getValue().getIdInscripcion(), 1);

        actualizarEstadoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplication(), "Inscripción aceptada", Toast.LENGTH_LONG).show();
                mEstadoEnCurso.postValue(mEstadoSolicitada.getValue());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}