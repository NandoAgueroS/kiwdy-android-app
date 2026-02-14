package com.example.kiwdy.ui.compartido.examenes;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.request.CargarNotaRequest;
import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamenesViewModel extends AndroidViewModel {
    private MutableLiveData<List<ExamenResponse>> mExamenes;
    private MutableLiveData<String> mError;
    private MutableLiveData<String> mErrorValidacion;
    private MutableLiveData<Boolean> mMostrarBotonFinalizar;

    public ExamenesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ExamenResponse>> getmExamenes(){
        if (mExamenes == null) {
            mExamenes = new MutableLiveData<>();
        }
        return mExamenes;
    }

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<String> getmErrorValidacion(){
        if (mErrorValidacion == null) {
            mErrorValidacion = new MutableLiveData<>();
        }
        return mErrorValidacion;
    }

    public LiveData<Boolean> getmMostrarBotonFinalizar(){
        if (mMostrarBotonFinalizar == null) {
            mMostrarBotonFinalizar = new MutableLiveData<>();
        }
        return mMostrarBotonFinalizar;
    }

    public void obtenerExamenes(String tag, boolean checked){
        if (!checked) return;
        String token = SharedPreferencesUtil.leerToken(getApplication());
        Call<List<ExamenResponse>> listarExamenesCall;
        switch (tag){
            case "todos":
            listarExamenesCall = ApiClient.getExamenesService().listar(token);
            break;
            case "proximos":
                listarExamenesCall = ApiClient.getExamenesService().listar(token, LocalDateTime.now());
                break;
            default:
                listarExamenesCall = ApiClient.getExamenesService().listar(token, LocalDateTime.now());

        }


        listarExamenesCall.enqueue(new Callback<List<ExamenResponse>>() {
            @Override
            public void onResponse(Call<List<ExamenResponse>> call, Response<List<ExamenResponse>> response) {
                if (response.isSuccessful()) mExamenes.postValue(response.body());
                else mError.postValue("Error al obtener los exámenes");
            }

            @Override
            public void onFailure(Call<List<ExamenResponse>> call, Throwable t) {

                mError.postValue("Ocurrió un error inesperado");
                Log.d("API_ERROR","Error al obtener los exámenes", t);
            }
        });
    }

    public void guardarNota(int idExamen, String nota){
        int notaInt;
        try{
            notaInt = Integer.parseInt(nota);
        } catch (NumberFormatException e) {
            mErrorValidacion.setValue("La nota tiene que ser númerica");
            return;
        } catch (Exception e) {
            mError.setValue("Ocurrió un error inesperado");
            Log.d("PARSING_ERROR", "Error al parsear la nota", e);
            return;
        }

        String token = SharedPreferencesUtil.leerToken(getApplication());

        CargarNotaRequest notaRequest = new CargarNotaRequest(notaInt);

        Call<ExamenResponse> cargarNotaCall = ApiClient.getExamenesService().cargarNota(token,idExamen, notaRequest);

        cargarNotaCall.enqueue(new Callback<ExamenResponse>() {
            @Override
            public void onResponse(Call<ExamenResponse> call, Response<ExamenResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Nota guardada correctamente", Toast.LENGTH_LONG).show();
                    mMostrarBotonFinalizar.postValue(false);
                }
                else mError.postValue("Ocurrió un error al guardar la nota");
            }

            @Override
            public void onFailure(Call<ExamenResponse> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado al guardar la nota");
                Log.d("API_ERROR", "Error al guardar la nota", t);
            }
        });

    }
}