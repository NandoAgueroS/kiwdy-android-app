package com.example.kiwdy.ui.compartido;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosViewModel extends AndroidViewModel {
    private MutableLiveData<List<CursoResponse>> mCursos;

    public CursosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<CursoResponse>> getmCursos(){
        if (mCursos == null) {
            mCursos = new MutableLiveData<>();
        }
        return mCursos;
    }

    public void cargarListaCursos(){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<List<CursoResponse>> cursosCall = ApiClient.getCursosService().listarCursos(token);
        cursosCall.enqueue(new Callback<List<CursoResponse>>() {
            @Override
            public void onResponse(Call<List<CursoResponse>> call, Response<List<CursoResponse>> response) {
                if (response.isSuccessful()){
                    mCursos.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<CursoResponse>> call, Throwable t) {

            }
        });
}}