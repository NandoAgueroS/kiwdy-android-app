package com.example.kiwdy.ui.compartido.inicio;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.kiwdy.R;
import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.UsuarioResponse;
import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mSesionInvalida;
    private MutableLiveData<String> mError;
    private MutableLiveData<List<CursoResponse>> mCursos;
    private MutableLiveData<Boolean> mAlumnoNavigation;
    private MutableLiveData<Boolean> mInstructorNavigation;
    private MutableLiveData<Boolean> mVistaInstructor;
    private MutableLiveData<Boolean> mVistaAlumno;
    private MutableLiveData<UsuarioResponse> mPerfilUsuario;

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getmSesionInvalida() {
        if (mSesionInvalida == null) {
            mSesionInvalida = new MutableLiveData<>();
        }
        return mSesionInvalida;
    }

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<List<CursoResponse>> getmCursos(){
        if (mCursos == null) {
           mCursos = new MutableLiveData<>();
        }
        return mCursos;
    }

    public LiveData<Boolean> getmAlumnoNavigation(){
        if (mAlumnoNavigation == null) {
            mAlumnoNavigation = new MutableLiveData<>();
        }
        return mAlumnoNavigation;
    }

    public LiveData<Boolean> getmInstructorNavigation(){
        if (mInstructorNavigation == null) {
            mInstructorNavigation = new MutableLiveData<>();
        }
        return mInstructorNavigation;
    }

    public LiveData<Boolean> getmVistaInstructor(){
        if (mVistaInstructor == null) {
            mVistaInstructor = new MutableLiveData<>();
        }
        return mVistaInstructor;
    }

    public LiveData<Boolean> getmVistaAlumno(){
        if (mVistaAlumno == null) {
            mVistaAlumno = new MutableLiveData<>();
        }
        return mVistaAlumno;
    }

    public LiveData<UsuarioResponse> getmPerfilUsuario(){
        if (mPerfilUsuario == null) {
            mPerfilUsuario = new MutableLiveData<>();
        }
        return mPerfilUsuario;
    }

    public void recuperarPerfil(){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<UsuarioResponse> perfilCall = ApiClient.getUsuariosService().getPerfil(token);

        perfilCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful()){
                    mPerfilUsuario.postValue(response.body());
                }else if (response.code() == 401){
                    mSesionInvalida.postValue(true);
                }else{
                    mError.postValue("Ocurrió un error al recuperar el perfil del usuario");
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado al recuperar el perfil del usuario");
                Log.d("API_ERROR", "Error al recuperar el perfil del usuario", t);

            }
        });
    }

    public void verificarRol(){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        String rol = JwtUtil.obtenerRol(token.replace("Bearer ", ""));
        if (rol == null){
            mError.setValue("Ocurrió un error inesperado");
            return;
        }
        if (rol.isBlank()){
            mError.setValue("Ocurrió un error inesperado");
            return;
        }

        switch (rol){
            case "Instructor": mVistaInstructor.setValue(true);
            break;
            case "Alumno": mVistaAlumno.setValue(true);
            break;
        }


    }
    public void cargarListaCursos(){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<List<CursoResponse>> cursosCall = ApiClient.getCursosService().listarCursosPopulares(token);
        cursosCall.enqueue(new Callback<List<CursoResponse>>() {
            @Override
            public void onResponse(Call<List<CursoResponse>> call, Response<List<CursoResponse>> response) {
                if (response.isSuccessful()) {
                    mCursos.setValue(response.body());
                }else if (response.code() == 401){
                    mSesionInvalida.postValue(true);
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