package com.example.kiwdy.ui.instructor.inscripciones;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgresoAlumnoViewModel extends AndroidViewModel {
    private MutableLiveData<InscripcionResponse> mEstadoSolicitada;
    private MutableLiveData<InscripcionResponse> mEstadoEnCurso;
    private MutableLiveData<InscripcionResponse> mEstadoPendienteCertificacion;
    private MutableLiveData<InscripcionResponse> mEstadoCertificada;
    private MutableLiveData<Integer> mProgreso;
    private MutableLiveData<File> mCertificadoPdf;

    public ProgresoAlumnoViewModel(@NonNull Application application) {
        super(application);
    }

   public LiveData<InscripcionResponse> getmEstadoSolicitada(){
       if (mEstadoSolicitada == null) {
          mEstadoSolicitada = new MutableLiveData<>();
       }
       return mEstadoSolicitada;
   }

    public LiveData<InscripcionResponse> getmEstadoEnCurso(){
        if (mEstadoEnCurso == null) {
            mEstadoEnCurso = new MutableLiveData<>();
        }
        return mEstadoEnCurso;
    }

    public LiveData<InscripcionResponse> getmEstadoPendienteCertificacion(){
        if (mEstadoPendienteCertificacion == null) {
            mEstadoPendienteCertificacion = new MutableLiveData<>();
        }
        return mEstadoPendienteCertificacion;
    }

    public LiveData<InscripcionResponse> getmEstadoCertificada(){
        if (mEstadoCertificada == null) {
            mEstadoCertificada = new MutableLiveData<>();
        }
        return mEstadoCertificada;
    }

    public LiveData<Integer> getmProgreso(){
        if (mProgreso == null) {
            mProgreso = new MutableLiveData<>();
        }
        return mProgreso;
    }

    public LiveData<File> getmCertificadoPdf(){
        if (mCertificadoPdf == null) {
            mCertificadoPdf = new MutableLiveData<>();
        }
        return mCertificadoPdf;
    }

    public void buscarInscripcion(Bundle arguments){
        if (arguments == null || !arguments.containsKey("idInscripcion")) return;

        int idInscripcion = arguments.getInt("idInscripcion");

        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<InscripcionResponse> inscripcionCall = ApiClient.getInscripcionesService().buscarInscripcion(token, idInscripcion);

        inscripcionCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful())
                    switch (response.body().getEstado()){
                        case "Solicitada": mEstadoSolicitada.postValue(response.body());
                            break;
                        case "EnCurso": {
                            mEstadoEnCurso.postValue(response.body());

                            mProgreso.postValue(calcularProgreso(response.body()));
                    }
                            break;
                        case "PendienteCertificacion":
                            mEstadoPendienteCertificacion.postValue(response.body());
                            mProgreso.postValue(100);
                            break;
                        case "Certificada": {
                            mEstadoCertificada.postValue(response.body());
                            obtenerCertificado(idInscripcion);
                        }
                            break;
                    }
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {

            }
        });
    }
    private int calcularProgreso(InscripcionResponse inscripcion){
        int ultimaSeccionCompletada = inscripcion.getUltimaSeccionCompletada();
        int totalSecciones = inscripcion.getCurso().getSecciones().size();
        float result = (( (float) ultimaSeccionCompletada / totalSecciones) * 100);
        return (int) result;

    }
    public void obtenerCertificado(int idInscripcion){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<ResponseBody> certificadoCall = ApiClient.getInscripcionesService().buscarCertificado(token, idInscripcion);

        certificadoCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    File certificado = guardarCertificadoEnCache(response.body());
                    mCertificadoPdf.postValue(certificado);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private File guardarCertificadoEnCache(ResponseBody body){
        try{
            File file = new File(getApplication().getCacheDir(), "certificado_"+System.currentTimeMillis()+".pdf");
            InputStream inputStream = body.byteStream();
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
           return file;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void aceptarInscripcion() {
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<Void> actualizarEstadoCall = ApiClient.getInscripcionesService().actualizarEstado(token, mEstadoSolicitada.getValue().getIdInscripcion(), 1);

        actualizarEstadoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplication(), "Inscripción aceptada", Toast.LENGTH_LONG).show();
                mEstadoEnCurso.postValue(mEstadoSolicitada.getValue());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}