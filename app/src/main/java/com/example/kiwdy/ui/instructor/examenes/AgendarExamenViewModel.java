package com.example.kiwdy.ui.instructor.examenes;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.Modalidad;
import com.example.kiwdy.api.dto.request.CrearExamenRequest;
import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendarExamenViewModel extends AndroidViewModel {
    private MutableLiveData<String> mError;
    private MutableLiveData<String> mErrorDeValidacion;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<ExamenResponse> mExamenAgendado;
    private MutableLiveData<InscripcionResponse> mInscripcion;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public AgendarExamenViewModel(@NonNull Application application) {
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

    public LiveData<ExamenResponse> getmExamenAgendado() {
        if (mExamenAgendado == null) {
            mExamenAgendado = new MutableLiveData<>();
        }
        return mExamenAgendado;
    }

    public LiveData<InscripcionResponse> getmInscripcion() {
        if (mInscripcion == null) {
            mInscripcion = new MutableLiveData<>();
        }
        return mInscripcion;
    }

    public void recuperarInscripcion(Bundle arguments){
        InscripcionResponse inscripcion = (InscripcionResponse) arguments.getSerializable("inscripcion");
        if (inscripcion == null){
            mError.setValue("Error al recuperar la inscripción");
            return;
        }
        mInscripcion.setValue(inscripcion);
    }

    public void agendar(String modalidad, String fecha, String hora, String linkODireccion){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        
        CrearExamenRequest examen = validarCampos(modalidad, fecha, hora, linkODireccion);
        if (examen == null) return;

        Call<ExamenResponse> crearExamenCall = ApiClient.getExamenesService().crear(token, examen);
        crearExamenCall.enqueue(new Callback<ExamenResponse>() {
            @Override
            public void onResponse(Call<ExamenResponse> call, Response<ExamenResponse> response) {
                if (response.isSuccessful()){
                    mExamenAgendado.postValue(response.body());
                }else if (response.code() == 400){
                    mError.postValue("Petición inválida");
                }else{
                    mError.postValue("Error al procesar la solicitud");
                }
            }

            @Override
            public void onFailure(Call<ExamenResponse> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado");
                Log.d("API_ERROR", "Error al crear el exámen", t);
            }
        });
    }

    private CrearExamenRequest validarCampos(String modalidad, String fecha, String hora, String linkODireccion) {
        int modalidadCodigo = -1;
        LocalDateTime fechaYHora = null;
        boolean valido = true;
        StringBuilder errores = new StringBuilder();
       try{
           modalidadCodigo = Integer.parseInt(modalidad);
       } catch (NumberFormatException e) {
           mError.setValue("Ocurrió un error al obtener la modalidad");
           return null;
       } catch (Exception e) {
           mError.setValue("Ocurrió un error inesperado");
           return null;
       }
        if (fecha.isBlank()){
            errores.append("Tiene que ingresar una fecha \n");
            valido = false;
        }
        if (hora.isBlank()){
            errores.append("Tiene que ingresar una hora \n");
            valido = false;
        }
       try{
           if (!fecha.isBlank() && !hora.isBlank()) {
               fechaYHora = LocalDateTime.of(
                       LocalDate.parse(fecha, dateFormatter),
                       LocalTime.parse(hora, timeFormatter));
           }

       } catch (Exception e) {
           mError.setValue("Ocurrió un error al recuperar la fecha y hora");
           valido = false;
       }


        if (fechaYHora != null && fechaYHora.isBefore(LocalDateTime.now())){
            errores.append("La fecha y hora debe ser posterior a la actual \n");
            valido = false;
        }
       if (!(Modalidad.PRESENCIAL.getCodigo() == modalidadCodigo || Modalidad.VIRTUAL.getCodigo() == modalidadCodigo)){
           mError.setValue("Error al obtener la modalidad \n");
           valido = false;
       }
        if (linkODireccion.isBlank()){
            errores.append("Tiene que ingresar un link o direccion \n");
            valido = false;
        }
        if (!valido){
            mMensaje.setValue(errores.toString());
            mErrorDeValidacion.setValue("Dátos inválidos, revise los campos e intente nuevamente");
            return null;
        }else{
            CrearExamenRequest examen = new CrearExamenRequest();
            examen.setModalidad(modalidadCodigo);
            Modalidad modalidadEnum = Modalidad.fromCodigo(modalidadCodigo);
            switch (modalidadEnum){
                case VIRTUAL: examen.setLink(linkODireccion);
                    break;
                case PRESENCIAL: examen.setDireccion(linkODireccion);
            }
            examen.setFechaYHora(fechaYHora);
            examen.setIdInscripcion(mInscripcion.getValue().getIdInscripcion());
            return examen;
        }

    }
}