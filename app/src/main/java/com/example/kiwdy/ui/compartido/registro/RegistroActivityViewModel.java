package com.example.kiwdy.ui.compartido.registro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.request.CrearUsuarioRequest;
import com.example.kiwdy.api.dto.response.UsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivityViewModel extends AndroidViewModel {

    private MutableLiveData<UsuarioResponse> mUsuarioCreado;

    public RegistroActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<UsuarioResponse> getmUsuarioCreado(){
        if (mUsuarioCreado == null) {
           mUsuarioCreado = new MutableLiveData<>();
        }
        return mUsuarioCreado;
    }

    public void registrarUsuario (String rol, String nombre, String apellido, String email, String telefono, String contrasenia, String confirmarContrasenia){
        CrearUsuarioRequest crearUsuarioRequest = new CrearUsuarioRequest();
        int rolInt = Integer.parseInt(rol);
        crearUsuarioRequest.setRol(rolInt);
        crearUsuarioRequest.setNombre(nombre);
        crearUsuarioRequest.setApellido(apellido);
        crearUsuarioRequest.setEmail(email);
        crearUsuarioRequest.setTelefono(telefono);
        crearUsuarioRequest.setClave(contrasenia);
        Call<UsuarioResponse> registrarUsuarioCall = ApiClient.getUsuariosService().registro(crearUsuarioRequest);
        registrarUsuarioCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful()) mUsuarioCreado.postValue(response.body());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }

}
