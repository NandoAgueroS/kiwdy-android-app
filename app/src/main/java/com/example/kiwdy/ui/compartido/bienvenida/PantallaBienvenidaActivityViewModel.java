package com.example.kiwdy.ui.compartido.bienvenida;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.service.UsuariosService;
import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.example.kiwdy.ui.compartido.UIDialogs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaBienvenidaActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mError;
    private MutableLiveData<Boolean> mYaLogueadoInstructor;
    private MutableLiveData<Boolean> mYaLogueadoAlumno;
    private MutableLiveData<Boolean> mSesionInvalida;
    private MutableLiveData<Boolean> mSinToken;


    public PantallaBienvenidaActivityViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<Boolean> getmYaLogueadoInstructor(){
        if (mYaLogueadoInstructor == null) {
           mYaLogueadoInstructor = new MutableLiveData<>();
        }
        return mYaLogueadoInstructor;
    }

    public LiveData<Boolean> getmYaLogueadoAlumno(){
        if (mYaLogueadoAlumno == null) {
            mYaLogueadoAlumno = new MutableLiveData<>();
        }
        return mYaLogueadoAlumno;
    }


    public LiveData<Boolean> getmSesionInvalida() {
        if (mSesionInvalida == null) {
            mSesionInvalida = new MutableLiveData<>();
        }
        return mSesionInvalida;
    }
    public LiveData<Boolean> getmSinToken() {
        if (mSinToken == null) {
            mSinToken = new MutableLiveData<>();
        }
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
                            switch (JwtUtil.obtenerRol(token.replace("Bearer ", ""))){
                                case "Instructor": mYaLogueadoInstructor.setValue(true);
                                    break;
                                case "Alumno": mYaLogueadoAlumno.setValue(true);
                                    break;
                                default:
                            }
                    } else if (response.code() == 401) {
                        sesionInvalida();
                    } else {
                        mError.postValue("Ocurrió un error al verificar la sesión");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    mError.postValue("Ocurrió un error inesperado al verificar la sesión");
                    Log.d("API_ERROR", "Error al verificar la sesión", t);
                }
            });
        } else {
            mSinToken.setValue(true);
        }
    }
    }
