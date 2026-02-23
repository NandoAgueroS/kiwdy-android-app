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

    private MutableLiveData<Integer> mIdCurso;
    private MutableLiveData<SeccionLocal> mSeccionLocal;
    private MutableLiveData<Boolean> mSeccionAgregada;
    private MutableLiveData<Uri> mVideoUri;
    private MutableLiveData<List<MaterialExtra>> mMaterialesExtra;
    private List<MaterialExtra> materialesExtra = new LinkedList<>();
    private MutableLiveData<Boolean> mMostrarVistaPrevia;
    private MutableLiveData<Boolean> mOcultarVistaPrevia;
    private String nombreArchivoBorrador;
    private CursoLocal cursoLocal;

    public CrearSeccionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Integer> getmIdCurso() {
        if(mIdCurso == null) mIdCurso = new MutableLiveData<>();
        return mIdCurso;
    }

    public LiveData<SeccionLocal> getmSeccionLocal(){
        if (mSeccionLocal == null) mSeccionLocal = new MutableLiveData<>();
        return mSeccionLocal;
    }

    public LiveData<Boolean> getmSeccionAgregada(){
        if (mSeccionAgregada == null) mSeccionAgregada = new MutableLiveData<>();
        return mSeccionAgregada;
    }

    public LiveData<Uri> getmVideoUri() {
        if (mVideoUri== null) mVideoUri= new MutableLiveData<>();
        return mVideoUri;
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

    public void guardarProgresoSeccion(String titulo, String contenido) {

        SeccionLocal seccionLocal;
        List<SeccionLocal> seccionLocalList = cursoLocal.getSeccionLocalList();
        if (seccionLocalList == null) {
            seccionLocalList = new LinkedList<>();
        }
        if (mSeccionLocal.isInitialized()){
            seccionLocal = mSeccionLocal.getValue();
        }else{
            seccionLocal = new SeccionLocal();
            seccionLocal.setOrden(seccionLocalList.size() + 1);
        }
        seccionLocal.setTitulo(titulo);
        seccionLocal.setContenido(contenido);
        if (mMaterialesExtra.isInitialized()) seccionLocal.setMaterialesExtra(List.copyOf(mMaterialesExtra.getValue()));
        if (mVideoUri.getValue() != null) seccionLocal.setVideoUri(mVideoUri.getValue().toString());

        if (!mSeccionLocal.isInitialized()) cursoLocal.getSeccionLocalList().add(seccionLocal);

        //mMaterialesExtra.setValue(new LinkedList<>());
        materialesExtra.clear();
        guardarLocalmente(cursoLocal);
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

        nombreArchivoBorrador = arguments.getString("nombreArchivoBorrador");
        if (nombreArchivoBorrador == null) return;
        cursoLocal = leerLocal(nombreArchivoBorrador);

        if (!arguments.containsKey("orden")) return;
        int orden = arguments.getInt("orden");


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
            mVideoUri.setValue(uri);
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
            Uri uri = data.getData();

            Cursor cursor = getApplication().getContentResolver().query(uri, null, null, null, null);
            int nombreIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String nombre = cursor.getString(nombreIndex);
            cursor.close();
            MaterialExtra materialExtra = new MaterialExtra(nombre, uri);
            materialesExtra.add(materialExtra);
            mMaterialesExtra.setValue(materialesExtra);
        }
    }
}