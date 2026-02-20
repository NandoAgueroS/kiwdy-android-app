package com.example.kiwdy.ui.instructor.inscripciones;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.request.CargarNotaRequest;
import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.example.kiwdy.model.ArchivoDescargado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
    private MutableLiveData<String> mError;
    private MutableLiveData<ArchivoDescargado> mCertificadoGuardado;
    private MutableLiveData<Boolean> mCertificadoGuardadoLegacy;
    private MutableLiveData<List<ExamenResponse>> mExamenes;
    private MutableLiveData<String> mErrorValidacion;
    private MutableLiveData<Boolean> mMostrarBotonFinalizar;
    private int idInscripcion;

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

    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<ArchivoDescargado> getmCertificadoGuardado(){
        if (mCertificadoGuardado == null) {
            mCertificadoGuardado = new MutableLiveData<>();
        }
        return mCertificadoGuardado;
    }

    public LiveData<Boolean> getmCertificadoGuardadoLegacy(){
        if (mCertificadoGuardadoLegacy == null) {
            mCertificadoGuardadoLegacy = new MutableLiveData<>();
        }
        return mCertificadoGuardadoLegacy;
    }

    public LiveData<List<ExamenResponse>> getmExamenes(){
        if (mExamenes == null) {
            mExamenes = new MutableLiveData<>();
        }
        return mExamenes;
    }

    public LiveData<String> getmErrorValidacion(){
        if (mErrorValidacion == null) {
            mErrorValidacion = new MutableLiveData<>();
        }
        return mErrorValidacion;
    }

    public LiveData<Boolean> getmMostrarBotonFinalizar(){
        if (mMostrarBotonFinalizar == null) {
            mMostrarBotonFinalizar = new MutableLiveData<>();
        }
        return mMostrarBotonFinalizar;
    }

    public void buscarInscripcion(Bundle arguments){
        if (arguments == null || !arguments.containsKey("idInscripcion")) return;

        idInscripcion = arguments.getInt("idInscripcion");
        buscarInscripcion(idInscripcion);
    }

    private void buscarInscripcion(int idInscripcion){
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
                            listarExamenes(idInscripcion);
                            break;
                        case "Certificada": {
                            mEstadoCertificada.postValue(response.body());
                            obtenerCertificado(idInscripcion);
                            listarExamenes(idInscripcion);
                        }
                            break;
                    }
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {
                mError.postValue("Ocurrió error al recuperar la inscripcion");
                Log.d("API_ERROR", "Error al recuperar la inscripción", t);
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
                    if (certificado == null)return;
                    mCertificadoPdf.postValue(certificado);
                }else if (response.code() == 404){
                    mError.postValue("No se encontró el certificado");
                }else{
                    mError.postValue("Ocurrió un error al recuperar el certificado");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("API_ERROR", "Error al recuperar el certificado", t);
                mError.postValue("Ocurrió un error inesperado");
            }
        });
    }

    public void listarExamenes(int idInscripcion){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<List<ExamenResponse>> listarExamenesCall = ApiClient.getExamenesService().listarPorInscripcion(token, idInscripcion);

        listarExamenesCall.enqueue(new Callback<List<ExamenResponse>>() {
            @Override
            public void onResponse(Call<List<ExamenResponse>> call, Response<List<ExamenResponse>> response) {
                if (response.isSuccessful()) {
                    mExamenes.postValue(response.body());
                }else{
                    mError.postValue("Ocurrió un error al recuperar los exámenes");
                }
            }

            @Override
            public void onFailure(Call<List<ExamenResponse>> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado recuperar los exámenes");
                Log.d("API_ERROR", "Error al recuperar los exámenes", t);
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
            e.printStackTrace();
            mError.postValue("Ocurrió un error al recuperar el certifiado");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            mError.postValue("Ocurrió un error al recuperar el certifiado");
            return null;
        }
    }

    public void guardarCertificadoEnDescargas(){
        File file = mCertificadoPdf.getValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ArchivoDescargado archivo = guardarCertificado(file);
            if (archivo == null) return;
            mCertificadoGuardado.setValue(archivo);
        }else{
            guardarCertificadoLegacy(file);
            mCertificadoGuardadoLegacy.setValue(true);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ArchivoDescargado guardarCertificado(File file){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, file.getName());
        values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
        values.put(MediaStore.Downloads.IS_PENDING, 1);

        ContentResolver contentResolver = getApplication().getContentResolver();

        Uri collection = null;
            collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

        Uri uri = contentResolver.insert(collection, values);
        if (uri == null){
            mError.setValue("Ocurrió un error al guardar el certificado");
            return null;
        }

        try(OutputStream output = contentResolver.openOutputStream(uri);
            InputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1){
                output.write(buffer, 0, bytesRead);
            }
            } catch (IOException e) {
            e.printStackTrace();
            mError.setValue("Ocurrió un error al guardar el certificado");

        }
        values.clear();
        values.put(MediaStore.Downloads.IS_PENDING, 0);
        contentResolver.update(uri, values, null, null);
        return new ArchivoDescargado(uri, "application/pdf");
    }

    private void guardarCertificadoLegacy(File file){

        File descargasDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File archivoDestino = new File(descargasDir, file.getName());

        try(OutputStream output = new FileOutputStream(file);
            InputStream input = new FileInputStream(archivoDestino)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = input.read()) != -1){
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mError.setValue("Ocurrió un error al guardar el certificado");
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

    public void guardarNota(int idExamen, String nota){
        int notaInt;
        try{
            notaInt = Integer.parseInt(nota);
        } catch (NumberFormatException e) {
            mErrorValidacion.setValue("La nota tiene que ser númerica");
            return;
        } catch (Exception e) {
            mError.setValue("Ocurrió un error inesperado");
            Log.d("PARSING_ERROR", "Error al parsear la nota", e);
            return;
        }

        String token = SharedPreferencesUtil.leerToken(getApplication());

        CargarNotaRequest notaRequest = new CargarNotaRequest(notaInt);

        Call<ExamenResponse> cargarNotaCall = ApiClient.getExamenesService().cargarNota(token,idExamen, notaRequest);

        cargarNotaCall.enqueue(new Callback<ExamenResponse>() {
            @Override
            public void onResponse(Call<ExamenResponse> call, Response<ExamenResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Nota guardada correctamente", Toast.LENGTH_LONG).show();
                    mMostrarBotonFinalizar.postValue(false);
                    buscarInscripcion(idInscripcion);
                }
                else mError.postValue("Ocurrió un error al guardar la nota");
            }

            @Override
            public void onFailure(Call<ExamenResponse> call, Throwable t) {
                mError.postValue("Ocurrió un error inesperado al guardar la nota");
                Log.d("API_ERROR", "Error al guardar la nota", t);
            }
        });

    }
}