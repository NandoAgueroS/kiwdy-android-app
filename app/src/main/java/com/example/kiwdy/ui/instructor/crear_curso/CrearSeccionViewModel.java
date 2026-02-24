package com.example.kiwdy.ui.instructor.crear_curso;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kiwdy.api.ApiClient;
import com.example.kiwdy.api.dto.response.ArchivoSeccionResponse;
import com.example.kiwdy.api.dto.response.CursoResponse;
import com.example.kiwdy.api.dto.response.SeccionResponse;
import com.example.kiwdy.api.service.SeccionesService;
import com.example.kiwdy.api.utils.JwtUtil;
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.example.kiwdy.model.CursoLocal;
import com.example.kiwdy.model.MaterialExtra;
import com.example.kiwdy.model.SeccionLocal;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearSeccionViewModel extends AndroidViewModel {

    private MutableLiveData<String> mError;
    private MutableLiveData<String> mErrorDeValidacion;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Integer> mIdCurso;
    private MutableLiveData<SeccionLocal> mSeccionLocal;
    private MutableLiveData<SeccionLocal> mSeccionLocalModoVisualizacion;
    private MutableLiveData<Boolean> mSeccionAgregada;
    private MutableLiveData<Uri> mVideoUri;
    private MutableLiveData<String> mVideoUrlPath;
    private MutableLiveData<List<MaterialExtra>> mMaterialesExtra;
    private List<MaterialExtra> materialesExtra = new LinkedList<>();
    private MutableLiveData<Boolean> mMostrarVistaPrevia;
    private MutableLiveData<Boolean> mOcultarVistaPrevia;
    private String nombreArchivoBorrador;
    private CursoLocal cursoLocal;

    public CrearSeccionViewModel(@NonNull Application application) {
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
    public LiveData<String> getmMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return  mMensaje;
    }

    public LiveData<Integer> getmIdCurso() {
        if(mIdCurso == null) mIdCurso = new MutableLiveData<>();
        return mIdCurso;
    }

    public LiveData<SeccionLocal> getmSeccionLocal(){
        if (mSeccionLocal == null) mSeccionLocal = new MutableLiveData<>();
        return mSeccionLocal;
    }

    public LiveData<SeccionLocal> getmSeccionLocalModoVisualizacion(){
        if (mSeccionLocalModoVisualizacion == null) mSeccionLocalModoVisualizacion = new MutableLiveData<>();
        return mSeccionLocalModoVisualizacion;
    }

    public LiveData<Boolean> getmSeccionAgregada(){
        if (mSeccionAgregada == null) mSeccionAgregada = new MutableLiveData<>();
        return mSeccionAgregada;
    }

    public LiveData<Uri> getmVideoUri() {
        if (mVideoUri== null) mVideoUri= new MutableLiveData<>();
        return mVideoUri;
    }

    public LiveData<String> getmVideoUrlPath() {
        if (mVideoUrlPath== null) mVideoUrlPath= new MutableLiveData<>();
        return mVideoUrlPath;
    }

    public LiveData<List<MaterialExtra>> getmMaterialesExtra() {
        if (mMaterialesExtra== null) mMaterialesExtra= new MutableLiveData<>();
        return mMaterialesExtra;
    }

    public LiveData<Boolean> getmMostrarVistaPrevia(){
        if (mMostrarVistaPrevia == null) {
            mMostrarVistaPrevia = new MutableLiveData<>();
        }
        return mMostrarVistaPrevia;
    }

    public LiveData<Boolean> getmOcultarVistaPrevia(){
        if (mOcultarVistaPrevia == null) {
            mOcultarVistaPrevia = new MutableLiveData<>();
        }
        return mOcultarVistaPrevia;
    }

    private SeccionLocal validarCampos(String titulo, String contenidoVar, String contenidoEditText, boolean modoVistaPrevia){

        String contenido;
        String video = null;
        if (modoVistaPrevia){
            contenido = contenidoVar;
        }else{
            contenido = contenidoEditText;
        }

        StringBuilder mensajes = new StringBuilder();

        boolean valido = true;

        if (titulo.isBlank()){
            mensajes.append("Debe ingresar un título \n");
            valido = false;
        }
        if (contenido.isBlank()){
            mensajes.append("Debe ingresar un contenido \n");
            valido = false;
        }
        if (!valido){
            mErrorDeValidacion.setValue("Datos inválidos, revise los campos e intente nuevamente");
            mMensaje.setValue(mensajes.toString());
            return null;
        }else {
            mMensaje.setValue("");
            SeccionLocal seccionLocal;
            List<SeccionLocal> seccionLocalList = cursoLocal.getSeccionLocalList();
            if (seccionLocalList == null) {
                seccionLocalList = new LinkedList<>();
            }


            if (!mSeccionLocal.isInitialized()) {
                seccionLocal = new SeccionLocal();
                seccionLocal.setOrden(seccionLocalList.size() + 1);
                cursoLocal.getSeccionLocalList().add(seccionLocal);
            }else{
                seccionLocal = mSeccionLocal.getValue();
            }

            if (mMaterialesExtra.isInitialized() && !mMaterialesExtra.getValue().isEmpty()) {
                seccionLocal.setMaterialesExtra(List.copyOf(mMaterialesExtra.getValue()));
            }else{
                seccionLocal.setMaterialesExtra(new LinkedList<>());
            }
            //if (mSeccionLocal.getValue() == null) mSeccionLocal.setValue(new SeccionLocal());

            if (video != null){
                seccionLocal.setVideoUri(video);
            }
            seccionLocal.setTitulo(titulo);
            seccionLocal.setContenido(contenido);
            return seccionLocal;
        }
    }


    public void guardarProgresoSeccion(String titulo, String contenidoVar, String contenidoEditText, boolean modoVistaPrevia) {


        //mMaterialesExtra.setValue(new LinkedList<>());
        if (validarCampos(titulo, contenidoVar, contenidoEditText, modoVistaPrevia) != null){
            materialesExtra.clear();
            guardarLocalmente(cursoLocal);
        }
    }

    public void alternarVistaPreviaDescripcion(boolean isChecked){
        if (isChecked){
            mMostrarVistaPrevia.setValue(true);
        }else{
            mOcultarVistaPrevia.setValue(true);
        }
    }


    public void guardarLocalmente(CursoLocal cursoLocal){
        File dir = new File(getApplication().getFilesDir(), "borradores");
        if (!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir, nombreArchivoBorrador);
        cursoLocal.setNombreArchivoBorrador(nombreArchivoBorrador);
        Gson gson = new Gson();

        String json = gson.toJson(cursoLocal);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
            mSeccionAgregada.setValue(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void recuperarCurso(Bundle arguments){
        if (arguments == null) return;
        if (arguments.containsKey("nombreArchivoBorrador") && arguments.containsKey("orden")){
           restaurarSeccionDesdeArchivo(
                   arguments.getInt("orden"),
                   arguments.getString("nombreArchivoBorrador")
           );
        }else if (arguments.containsKey("idCurso") && arguments.containsKey("orden")){
           restaurarSeccionDesdeServidor(
                   arguments.getInt("orden"),
                   arguments.getInt("idCurso")
           );
        }
    }
    public void restaurarSeccionDesdeServidor(int orden, int idCurso){
       String token = SharedPreferencesUtil.leerToken(getApplication());

       Call<SeccionResponse> seccionCall = ApiClient.getSeccionesService().buscar(token, idCurso, orden);

       seccionCall.enqueue(new Callback<SeccionResponse>() {
           @Override
           public void onResponse(Call<SeccionResponse> call, Response<SeccionResponse> response) {
               if (response.isSuccessful() && response.body() != null){
                   mSeccionLocalModoVisualizacion.postValue(
                           mapearSeccionResponseASeccionLocal(response.body())
                   );
                   if (response.body().getVideoUrl() != null && !response.body().getVideoUrl().isBlank()){
                       mVideoUrlPath.postValue(response.body().getVideoUrl());
                   }
                   mMostrarVistaPrevia.postValue(true);
               }else{
                   mError.postValue("Ocurrió un error al recuperar la sección");
               }
           }

           @Override
           public void onFailure(Call<SeccionResponse> call, Throwable t) {
               mError.postValue("Ocurrió un error al recuperar la sección");
               Log.d("API_ERROR", "Error al recuperar la sección", t);
           }
       });
    }

    private SeccionLocal mapearSeccionResponseASeccionLocal(SeccionResponse seccionResponse){
        SeccionLocal seccionLocal = new SeccionLocal();
        seccionLocal.setTitulo(seccionResponse.getTitulo());
        seccionLocal.setContenido(seccionResponse.getContenido());
        seccionLocal.setOrden(seccionResponse.getOrden());
        seccionLocal.setVideoUrl(seccionResponse.getVideoUrl());
        List<MaterialExtra> materialesLocal = new LinkedList<>();
        for (ArchivoSeccionResponse material:
             seccionResponse.getMateriales()) {
            MaterialExtra materialExtra = new MaterialExtra();
            materialExtra.setNombre(material.getNombre());
            materialesLocal.add(materialExtra);
        }
        seccionLocal.setMaterialesExtra(materialesLocal);
        return seccionLocal;

    }

    public void restaurarSeccionDesdeArchivo(int orden, String nombreArchivoBorrador){

        this.nombreArchivoBorrador = nombreArchivoBorrador;
        if (nombreArchivoBorrador == null) return;
        cursoLocal = leerLocal(nombreArchivoBorrador);

        SeccionLocal seccionLocal = cursoLocal.getSeccionLocalList().stream().filter(s -> s.getOrden() == orden).findFirst().orElse(null);
        if (seccionLocal == null) return;
        mSeccionLocal.setValue(seccionLocal);
        if (seccionLocal.getMaterialesExtra() != null) {
            mMaterialesExtra.setValue(seccionLocal.getMaterialesExtra());
        }
        if (seccionLocal.getVideoUri() != null){
        mVideoUri.setValue(Uri.parse(seccionLocal.getVideoUri()));}
    }

    public void limpiarMutables() {
        mVideoUri = null;
        mMaterialesExtra = null;
        mSeccionAgregada = null;
    }

    public void recibirVideo(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            File file = copiarUriAFile(data.getData(), "video");
            Uri uri = Uri.fromFile(file);
            MediaType mediaType = MediaType.parse(URLConnection.guessContentTypeFromName(file.getName()));
            if (mediaType == null){
                mError.setValue("Ocurrió un error al leer el formato del video");
            }else if (!mediaType.toString().equals("video/mp4")) {
                mErrorDeValidacion.setValue("Solo se permiten videos con formato mp4");
            }else{
                mVideoUri.setValue(uri);
            }
        }
    }
    private File copiarUriAFile(Uri uri, String prefijo) {
        File file = null;
            String tipo = getApplication().getContentResolver().getType(uri);
            String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(tipo);
            String nombreArchivo = prefijo + System.currentTimeMillis() +  "." + extension;
            file = new File(getApplication().getFilesDir(), nombreArchivo);
        try (
                InputStream inputStream = getApplication()
                    .getContentResolver()
                    .openInputStream(uri);
                OutputStream outputStream = new FileOutputStream(file)){

                byte[] buffer = new byte[8192];
                int length;

            while((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }

            return file;
        } catch (
                FileNotFoundException er) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CursoLocal leerLocal(String rutaArchivo){
        File dir = new File(getApplication().getFilesDir(), "borradores");
        File file = new File(dir, rutaArchivo);
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(file);

            return gson.fromJson(reader, CursoLocal.class);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void recibirArchivo(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uriOriginal = data.getData();
            File file = copiarUriAFile(uriOriginal, "archivo_extra");
            Uri uriCopia = Uri.fromFile(file);

            Cursor cursor = getApplication().getContentResolver().query(uriOriginal, null, null, null, null);
            int nombreIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String nombre = cursor.getString(nombreIndex);
            cursor.close();
            MaterialExtra materialExtra = new MaterialExtra(nombre, uriCopia.toString());
            materialesExtra.add(materialExtra);
            mMaterialesExtra.setValue(materialesExtra);
        }
    }
}