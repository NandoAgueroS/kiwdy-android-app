package com.example.kiwdy.ui.instructor.borradores;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.example.kiwdy.model.CursoLocal;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BorradoresViewModel extends AndroidViewModel {
    private MutableLiveData<List<CursoLocal>> mBorradores;

    public BorradoresViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<CursoLocal>> getmBorradores(){
        if (mBorradores == null) {
           mBorradores = new MutableLiveData<>();
        }
        return mBorradores;
    }

    public void leerBorradores(){
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                leerBorradoresDelUsuario();
            }
        });
    }

    public void leerBorradoresDelUsuario(){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        String idUsuario = JwtUtil.obtenerId(token);

        String prefijo = idUsuario + "_borrador_";

        List<CursoLocal> borradores = new ArrayList<>();

        File dir = new File(getApplication().getFilesDir(), "borradores");
        if (!dir.exists()){
            mBorradores.postValue(new ArrayList<>());
            return;
        }
        File[] files = dir.listFiles();
        if (files != null){
            for (File file:
                    files) {
                if (file.getName().startsWith(prefijo)){
                    borradores.add(leerLocal(file));
                }
            }
            mBorradores.postValue(borradores);
        }

    }
    private CursoLocal leerLocal(File file){
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(file);
            return gson.fromJson(reader, CursoLocal.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    // TODO: Implement the ViewModel
}