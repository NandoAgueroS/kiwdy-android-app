package com.example.kiwdy.ui.compartido.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.request.LoginRequest;
import com.example.kiwdy.api.service.UsuariosService;
import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mLoginInstructor;
    private MutableLiveData<String> mLoginAlumno;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<String> mEmailGuardado;
    private MutableLiveData<Boolean> mSesionInvalida;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getmLoginInstructor() {
        if (mLoginInstructor == null) {
           mLoginInstructor = new MutableLiveData<>();
        }
        return mLoginInstructor;
    }

    public LiveData<String> getmLoginAlumno() {
        if (mLoginAlumno == null) {
            mLoginAlumno = new MutableLiveData<>();
        }
        return mLoginAlumno;
    }

    public LiveData<String> getmMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public LiveData<String> getmEmailGuardado() {
        if (mEmailGuardado == null) {
            mEmailGuardado = new MutableLiveData<>();
        }
        return mEmailGuardado;
    }


    public LiveData<Boolean> getmSesionInvalida() {
        if (mSesionInvalida == null) {
            mSesionInvalida = new MutableLiveData<>();
        }
        return mSesionInvalida;
    }

    public void login(String email, String clave, int rol){

        if (validarCampos(email, clave)){
            UsuariosService usuariosService = ApiClient.getUsuariosService();

            LoginRequest loginRequest = new LoginRequest(email, clave, rol);
            Call<String> tokenCall = usuariosService.login(loginRequest);
            tokenCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        String token = response.body();
                        SharedPreferencesUtil.guardarToken(getApplication(), token);
                        //if (JwtUtil.obtenerRol(token).equals("Instructor"))
                        //    mLoginInstructor.setValue("");
                        switch (JwtUtil.obtenerRol(token)){
                            case "Instructor": mLoginInstructor.setValue("");
                            break;
                            case "Alumno": mLoginAlumno.setValue("");
                            break;
                            default:
                        }
                    }else{
                        mMensaje.postValue("El usuario y/o la contraseña son incorrectos");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mMensaje.postValue("Logueo incorrecto");
                    Log.d("API_ERROR", t.getMessage());
                }
            });
        }
    }

    public boolean validarCampos(String email, String clave){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (email.isBlank() && clave.isBlank()){
            mMensaje.setValue("Debe ingresar un email y una clave");
            return false;
        }else if (!matcher.matches() && clave.isBlank()){
            mMensaje.setValue("Debe ingresar un email válido y una clave");
            return false;
        }else if (email.isBlank()){
            mMensaje.setValue("Debe ingresar un email");
            return false;
        }else if (clave.isBlank()){
            mMensaje.setValue("Debe ingresar una clave");
            return false;
        }else if (!matcher.matches()){
            mMensaje.setValue("Debe ingresar un email válido");
            return false;
        }
        return true;
    }

    public void guardarEmail(String email){
        SharedPreferencesUtil.guardarEmail(getApplication(), email);
    }

    public void recuperarEmail(){
        String email = SharedPreferencesUtil.leerEmail(getApplication());
        if (email != null && !email.isBlank()) {
            mEmailGuardado.setValue(email);
        }
    }

    public void verificarSesionExpirada(Intent intent){
        if (intent.getBooleanExtra("desde_sesion_expirada", false)){
            mSesionInvalida.setValue(true);
        }
    }


}
