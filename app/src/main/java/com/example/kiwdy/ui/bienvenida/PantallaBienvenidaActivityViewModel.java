package com.example.kiwdy.ui.bienvenida;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.service.UsuariosService;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaBienvenidaActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mYaLogueadoInstructor = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida= new MutableLiveData<>();
    private MutableLiveData<Boolean> mSinToken= new MutableLiveData<>();

    public PantallaBienvenidaActivityViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Boolean> getmYaLogueadoInstructor(){
        return mYaLogueadoInstructor;
    }


    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }
    public LiveData<Boolean> getmSinToken() {
        return mSinToken;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        SharedPreferencesUtil.eliminarToken(getApplication());
    }
    public void verificarLogueado() {
        String token = SharedPreferencesUtil.leerToken(getApplication());
        UsuariosService usuariosService = ApiClient.getUsuariosService();
        if (token != null && !token.isBlank()) {
            Call<Void> call = usuariosService.validarToken(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mYaLogueadoInstructor.postValue(true);
                    } else if (response.code() == 401) {
                        sesionInvalida();
                        Toast.makeText(getApplication(), "Sesión expirada", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("API_ERROR", t.getMessage());
                    Toast.makeText(getApplication(), "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mSinToken.setValue(true);
        }
    }
    }
