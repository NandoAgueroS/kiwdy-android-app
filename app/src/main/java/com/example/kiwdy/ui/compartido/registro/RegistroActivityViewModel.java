package com.example.kiwdy.ui.compartido.registro;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.request.CrearUsuarioRequest;
import com.example.kiwdy.api.dto.response.UsuarioResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mError;
    private MutableLiveData<String> mErrorDeValidacion;
    private MutableLiveData<String> mMensaje;

    private MutableLiveData<UsuarioResponse> mUsuarioCreado;

    public RegistroActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<String> getmErrorDeValidacion(){
        if (mErrorDeValidacion == null) {
            mErrorDeValidacion = new MutableLiveData<>();
        }
        return mErrorDeValidacion;
    }

    public LiveData<String> getmMensaje(){
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public LiveData<UsuarioResponse> getmUsuarioCreado(){
        if (mUsuarioCreado == null) {
           mUsuarioCreado = new MutableLiveData<>();
        }
        return mUsuarioCreado;
    }

    public void registrarUsuario (String rol, String nombre, String apellido, String email, String telefono, String contrasenia, String confirmarContrasenia){
        if (!validarCampos(nombre, apellido, email, telefono, contrasenia, confirmarContrasenia)){
            return;
        }
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
                if (response.isSuccessful()) {
                    mUsuarioCreado.postValue(response.body());
                }else{
                    mError.postValue("Ocurrió un error al registrar el usuario");
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado al registrar al usuario");
                Log.d("API_ERROR", "Error al registrar el usuario", t);
            }
        });
    }

    public boolean validarCampos(String nombre, String apellido, String email, String telefono, String clave, String claveRepetida){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        StringBuilder mensajes = new StringBuilder();

        boolean valido = true;

        if (nombre.isBlank()){
            mensajes.append("Debe ingresar un nombre \n");
            valido = false;
        }
        if (apellido.isBlank()){
            mensajes.append("Debe ingresar un apellido \n");
            valido = false;
        }
        if (email.isBlank()){
            mensajes.append("Debe ingresar un email \n");
            valido = false;
        }else if (!matcher.matches()){
            mensajes.append("Debe ingresar un email válido \n");
            valido = false;
        }
        if (telefono.isBlank()){
            mensajes.append("Debe ingresar un teléfono \n");
            valido = false;
        }else{
            try{
                Integer.parseInt(telefono);
            } catch (NumberFormatException e) {
                mensajes.append("El campo telefono solo acepta numeros \n");
                valido = false;
            }
        }
        if (clave.isBlank()){
            mensajes.append("Debe ingresar una contraseña \n");
            valido = false;
        }else if (clave.length() < 8){
            mensajes.append("La contraseña debe tener al menos 8 caractéres \n");
            valido = false;
        }else if (!clave.equals(claveRepetida)){
            mensajes.append("Las contraseñas no coinciden \n");
            valido = false;
        }
        if (!valido){
            mErrorDeValidacion.setValue("Datos inválidos, revise los campos e intente nuevamente \n");
            mMensaje.setValue(mensajes.toString());
        }else{
            mMensaje.setValue("");
        }
        return valido;
    }

}
