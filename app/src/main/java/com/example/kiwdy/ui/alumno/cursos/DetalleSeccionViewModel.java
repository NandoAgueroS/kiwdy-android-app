package com.example.kiwdy.ui.alumno.cursos;

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
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.EstadoInscripcion;
import com.example.kiwdy.api.dto.request.MarcarSeccionCompletadaRequest;
import com.example.kiwdy.api.dto.response.InscripcionResponse;
import com.example.kiwdy.api.dto.response.SeccionResponse;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.example.kiwdy.model.ArchivoDescargado;
import com.example.kiwdy.model.CursoFinalizadoMensaje;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleSeccionViewModel extends AndroidViewModel {

    private MutableLiveData<SeccionResponse> mSeccion;
    private MutableLiveData<InscripcionResponse> mInscripcion;
    private MutableLiveData<Boolean> mMostrarBotonMarcarCompletada;
    private MutableLiveData<String> mError;
    private MutableLiveData<Boolean> mMostrarBotonSiguiente;
    private MutableLiveData<Boolean> mMostrarBotonAnterior;
    private MutableLiveData<ArchivoDescargado> mAbrirArchivoDescargado;
    private MutableLiveData<String> mArchivoDescargado;
    private MutableLiveData<String> mMostrarVideo;
    private MutableLiveData<CursoFinalizadoMensaje> mSeccionesFinalizadas;
    private int ordenActual = 0;

    public DetalleSeccionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<SeccionResponse> getmSeccion(){
        if (mSeccion == null) {
           mSeccion = new MutableLiveData<>();
        }
        return mSeccion;
    }

    public LiveData<InscripcionResponse> getmInscripcion(){
        if (mInscripcion == null) {
            mInscripcion = new MutableLiveData<>();
        }
        return mInscripcion;
    }

    public LiveData<Boolean> getmMostrarBotonMarcarCompletada(){
        if (mMostrarBotonMarcarCompletada == null) {
            mMostrarBotonMarcarCompletada = new MutableLiveData<>();
        }
        return mMostrarBotonMarcarCompletada;
    }

    public LiveData<Boolean> getmMostrarBotonSiguiente(){
        if (mMostrarBotonSiguiente == null) {
            mMostrarBotonSiguiente = new MutableLiveData<>();
        }
        return mMostrarBotonSiguiente;
    }
    public LiveData<Boolean> getmMostrarBotonAnterior(){
        if (mMostrarBotonAnterior == null) {
            mMostrarBotonAnterior = new MutableLiveData<>();
        }
        return mMostrarBotonAnterior;
    }
    public LiveData<String> getmError(){
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public LiveData<ArchivoDescargado> getmAbrirArchivoDescargado(){
        if (mAbrirArchivoDescargado == null) {
            mAbrirArchivoDescargado = new MutableLiveData<>();
        }
        return mAbrirArchivoDescargado;
    }

    public LiveData<String> getmArchivoDescargado(){
        if (mArchivoDescargado == null) {
            mArchivoDescargado = new MutableLiveData<>();
        }
        return mArchivoDescargado;
    }

    public LiveData<String> getmMostrarVideo(){
        if (mMostrarVideo == null) {
            mMostrarVideo = new MutableLiveData<>();
        }
        return mMostrarVideo;
    }
    public LiveData<CursoFinalizadoMensaje> getmSeccionesFinalizadas(){
        if (mSeccionesFinalizadas == null) {
            mSeccionesFinalizadas = new MutableLiveData<>();
        }
        return mSeccionesFinalizadas;
    }

    public void recuperarCurso(Bundle arguments){
        if (arguments == null) return;
        int idCurso = arguments.getInt("idCurso", -1);
        if (idCurso == -1) return;
        buscarInscripcion(idCurso);
    }

    public void buscarInscripcion(int idCurso){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<InscripcionResponse> inscripcionCall = ApiClient.getInscripcionesService().buscarInscripcionPorCurso(token, idCurso);

        inscripcionCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful()) mInscripcion.postValue(response.body());
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {

            }
        });

    }

    public void siguienteSeccion() {
        ++ordenActual;
        mostrarSeccion(ordenActual);
    }
    public void retrocederSeccion() {
        --ordenActual;
        mostrarSeccion(ordenActual);
    }
    public void cargarSeccion(Bundle bundle){
        if (bundle != null && bundle.containsKey("orden")){
            ordenActual = bundle.getInt("orden");
        }else if (ordenActual == 0) {
            InscripcionResponse inscripcion = mInscripcion.getValue();
            if (inscripcion.getUltimaSeccionCompletada() == inscripcion.getCurso().getSecciones().size()){
                ordenActual = inscripcion.getUltimaSeccionCompletada();
            }else {
                ordenActual = inscripcion.getUltimaSeccionCompletada() + 1;
            }
        }
        mostrarSeccion(ordenActual);
    }

    public void mostrarSeccion(int ordenSeccion){
        if (ordenActual== mInscripcion.getValue().getCurso().getSecciones().size()) {
            mMostrarBotonSiguiente.postValue(false);
        }else {
            mMostrarBotonSiguiente.postValue(true);
        }
        if (ordenActual== 1){
            mMostrarBotonAnterior.postValue(false);
        }else{
            mMostrarBotonAnterior.postValue(true);
        }
        if (ordenActual <= mInscripcion.getValue().getUltimaSeccionCompletada()) {
            mMostrarBotonMarcarCompletada.postValue(false);
        }else {
            mMostrarBotonMarcarCompletada.postValue(true);
        }
        int idCurso = mInscripcion.getValue().getCurso().getIdCurso();
        if (idCurso == 0) return;
        String token = SharedPreferencesUtil.leerToken(getApplication());
        SeccionResponse seccion = mInscripcion.getValue().getCurso().getSecciones().stream().filter(s -> s.getOrden() == ordenSeccion).findFirst().orElse(null);
        mSeccion.setValue(seccion);
        if (seccion.getVideoUrl() != null) mMostrarVideo.setValue(seccion.getVideoUrl());

        Call<SeccionResponse> seccionCall = ApiClient.getSeccionesService().buscar(token, idCurso, ordenSeccion);
/*
        seccionCall.enqueue(new Callback<SeccionResponse>() {
            @Override
            public void onResponse(Call<SeccionResponse> call, Response<SeccionResponse> response) {
                if (response.isSuccessful()) mSeccion.setValue(response.body());
                else mError.postValue("Error al recuperar la seccion: " + response.code());
            }

            @Override
            public void onFailure(Call<SeccionResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });*/
    }

    public void marcarCompletada(){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        if (ordenActual - 1 != mInscripcion.getValue().getUltimaSeccionCompletada()){
            mError.setValue("Primero tiene que completar las secciones anteriores");
            return;
        }
        MarcarSeccionCompletadaRequest seccion = new MarcarSeccionCompletadaRequest(mSeccion.getValue().getIdSeccion());
        int idInscripcion = mInscripcion.getValue().getIdInscripcion();

        Call<InscripcionResponse> seccionCompletadaCall = ApiClient.getInscripcionesService().marcarSeccionCompletada(token, idInscripcion, seccion);

        seccionCompletadaCall.enqueue(new Callback<InscripcionResponse>() {
            @Override
            public void onResponse(Call<InscripcionResponse> call, Response<InscripcionResponse> response) {
                if (response.isSuccessful()) {
                    mMostrarBotonMarcarCompletada.setValue(false);
                    mInscripcion.getValue().setUltimaSeccionCompletada(ordenActual);
                    InscripcionResponse inscripcionResponse = response.body();
                    switch (inscripcionResponse.getEstado()){
                        case "PendienteCertificacion": mSeccionesFinalizadas.postValue(new CursoFinalizadoMensaje("Felicidades! ha finalizado todas las secciones. \n Ahora espere a que su instructor le asigne una fecha de exámen", mInscripcion.getValue().getCurso().getIdCurso()));
                        break;
                        case "Certificada": mSeccionesFinalizadas.postValue(new CursoFinalizadoMensaje("Felicidades! ha finalizado el curso. \n Ahora puede ver su certificado en la sección de progreso", mInscripcion.getValue().getCurso().getIdCurso()));
                            break;
                    }
                }
                else {
                    mError.postValue("Error al completar la seccion");
                }
            }

            @Override
            public void onFailure(Call<InscripcionResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public void descargarArchivo(int idArchivo, String nombreArchivo){
        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<ResponseBody> archivoCall = ApiClient.getSeccionesService().descargarMaterial(token, idArchivo);

        archivoCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ResponseBody body = response.body();
                    InputStream input = body.byteStream();
                    OutputStream output;
                    Uri uri;
                    String mime;

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo);
                            mime = URLConnection.guessContentTypeFromName(nombreArchivo);
                            if (mime == null){
                                mime = "application/octet-stream";
                            }
                        values.put(MediaStore.MediaColumns.MIME_TYPE, mime);
                        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                        ContentResolver contentResolver = getApplication().getContentResolver();
                            uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                            if (uri == null) {
                                mError.postValue("Error al guardar el archivo");
                                return;
                            }
                            output = contentResolver.openOutputStream(uri);
                            escribirArchivo(output, input);
                            mAbrirArchivoDescargado.postValue(new ArchivoDescargado(uri, mime));
                        }else{
                            File file = new File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    nombreArchivo
                            );
                            output = new FileOutputStream(file);
                            escribirArchivo(output, input);
                            mArchivoDescargado.postValue("Archivo guardado en Descargas");
                        }

                    } catch (IOException e) {
                        mError.postValue("Ocurrió un error al descargar el material");
                        Log.d("ERROR", "Error al descargar el archivo", e);
                    } catch (Exception e) {
                        Log.d("ERROR", "Error al descargar el archivo", e);
                    }

                }else {
                    mError.postValue("Ocurrió un error al descargar el material");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void escribirArchivo(OutputStream output, InputStream input) throws IOException{
        byte[] buffer = new byte[4096];
        int read;

        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }

        output.flush();
        output.close();
        input.close();
    }
}