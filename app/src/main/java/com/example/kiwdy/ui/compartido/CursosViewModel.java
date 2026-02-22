package com.example.kiwdy.ui.compartido;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosViewModel extends AndroidViewModel {
    private MutableLiveData<List<CursoResponse>> mCursos;
    private MutableLiveData<String> mError;

    public CursosViewModel(@NonNull Application application) {
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

    public void buscarCursosPorTitulo(String titulo){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        Call<List<CursoResponse>> cursosCall;
        if (titulo.isBlank()){
            cursosCall = ApiClient.getCursosService().listarCursos(token);
        }else{
            cursosCall = ApiClient.getCursosService().listarCursosPorTitulo(token, titulo);
        }

        cursosCall.enqueue(new Callback<List<CursoResponse>>() {
            @Override
            public void onResponse(Call<List<CursoResponse>> call, Response<List<CursoResponse>> response) {
                if (response.isSuccessful()){
                    mCursos.postValue(response.body());

                }else{
                    mError.postValue("Ocurrió un error al recuperar los cursos");
                }
            }

            @Override
            public void onFailure(Call<List<CursoResponse>> call, Throwable t) {

                mError.postValue("Ocurrió un error inesperado al recuperar los cursos");
                Log.d("API_ERROR", "Error al recuperar los cursos", t);
            }
        });
    }
}