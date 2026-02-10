package com.example.kiwdy.ui.instructor.inscripciones;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.EstadoInscripcion;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscripcionesViewModel extends AndroidViewModel {

    private MutableLiveData<List<InscripcionResponse>> mInscripciones;

    public InscripcionesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<InscripcionResponse>> getmInscripciones(){
        if (mInscripciones == null) {
           mInscripciones = new MutableLiveData<>();
        }
        return mInscripciones;
    }

    public void listarInscripciones(String estado, boolean seleccionado){
        if (!seleccionado) return;
        String token = SharedPreferencesUtil.leerToken(getApplication());
        int estadoInt = Integer.parseInt(estado);
        Log.d("INSCRIPCION", estado);

        Call<List<InscripcionResponse>> inscripcionesCall = ApiClient.getInscripcionesService().listarInscripciones(token, estadoInt);
        inscripcionesCall.enqueue(new Callback<List<InscripcionResponse>>() {
            @Override
            public void onResponse(Call<List<InscripcionResponse>> call, Response<List<InscripcionResponse>> response) {
                if (response.isSuccessful()) mInscripciones.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<InscripcionResponse>> call, Throwable t) {

            }
        });
    }
}