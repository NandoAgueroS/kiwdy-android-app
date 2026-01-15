package com.example.kiwdy.ui.compartido.logout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kiwdy.api.utils.SharedPreferencesUtil;

public class LogoutViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mTokenEliminado;
    private MutableLiveData<Boolean> mSesionInvalida;

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

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        SharedPreferencesUtil.eliminarToken(getApplication());
    }

    public void eliminarToken(){
        SharedPreferencesUtil.eliminarToken(getApplication());
        mTokenEliminado.setValue(true);
    }
}