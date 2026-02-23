package com.example.kiwdy.ui.compartido.logout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

public class LogoutViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mTokenEliminado;
    private MutableLiveData<Boolean> mSesionInvalida;
    private MutableLiveData<Boolean> mNavegarAInicioAlumno;
    private MutableLiveData<Boolean> mNavegarAInicioInstructor;

    public LogoutViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Boolean> getMTokenEliminado(){
        if (mTokenEliminado == null) {
           mTokenEliminado = new MutableLiveData<>();
        }
        return mTokenEliminado;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        if (mSesionInvalida== null) {
            mSesionInvalida= new MutableLiveData<>();
        }
        return mSesionInvalida;
    }

    public LiveData<Boolean> getmNavegarAInicioAlumno(){
        if (mNavegarAInicioAlumno == null) {
            mNavegarAInicioAlumno = new MutableLiveData<>();
        }
        return mNavegarAInicioAlumno;
    }

    public LiveData<Boolean> getmNavegarAInicioInstructor(){
        if (mNavegarAInicioInstructor == null) {
            mNavegarAInicioInstructor = new MutableLiveData<>();
        }
        return mNavegarAInicioInstructor;
    }

    public void navegarAInicio(){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        String rol = JwtUtil.obtenerRol(token.replace("Bearer ", ""));
        switch (rol){
            case "Alumno": mNavegarAInicioAlumno.setValue(true);
            break;
            case "Instructor": mNavegarAInicioInstructor.setValue(true);
            break;
        }
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        SharedPreferencesUtil.eliminarToken(getApplication());
    }

    public void eliminarToken(){
        SharedPreferencesUtil.eliminarToken(getApplication());
        mTokenEliminado.setValue(true);
    }
}