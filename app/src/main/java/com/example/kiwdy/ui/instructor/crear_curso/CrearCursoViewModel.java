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
import com.example.kiwdy.api.utils.SharedPreferencesUtil;
import com.example.kiwdy.model.CursoLocal;
import com.example.kiwdy.model.MaterialExtra;
import com.example.kiwdy.model.SeccionLocal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class CrearCursoViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> mIdCurso;
    private MutableLiveData<CursoLocal> mCursoLocal;
    private MutableLiveData<Boolean> mSeccionAgregada = new MutableLiveData<>();
    private MutableLiveData<Uri> mVideoUri;
    private MutableLiveData<Uri> mImagenUri;
    private MutableLiveData<List<MaterialExtra>> mMaterialesExtra;
    private List<MaterialExtra> materialesExtra = new LinkedList<>();
    private MutableLiveData<Double> mMostrarNotaInput;
    private MutableLiveData<Double> mOcultarNotaInput;

    public CrearCursoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Integer> getmIdCurso() {
        if(mIdCurso == null) mIdCurso = new MutableLiveData<>();
        return mIdCurso;
    }

    public LiveData<CursoLocal> getmCursoLocal(){
        if (mCursoLocal == null) mCursoLocal = new MutableLiveData<>();
        return mCursoLocal;
    }

    public LiveData<Boolean> getmSeccionAgregada() {
        if (mSeccionAgregada == null) mSeccionAgregada = new MutableLiveData<>();
        return mSeccionAgregada;
    }

    public LiveData<Uri> getmVideoUri() {
        if (mVideoUri== null) mVideoUri= new MutableLiveData<>();
        return mVideoUri;
    }

    public LiveData<Uri> getmImagenUri() {
        if (mImagenUri== null) mImagenUri= new MutableLiveData<>();
        return mImagenUri;
    }

    public LiveData<List<MaterialExtra>> getmMaterialesExtra() {
        if (mMaterialesExtra== null) mMaterialesExtra= new MutableLiveData<>();
        return mMaterialesExtra;
    }
    public LiveData<Double> getmMostrarNotaInput(){
        if (mMostrarNotaInput == null) {
            mMostrarNotaInput = new MutableLiveData<>();
        }
        return mMostrarNotaInput;
    }

    public LiveData<Double> getmOcultarNotaInput(){
        if (mOcultarNotaInput == null) {
            mOcultarNotaInput = new MutableLiveData<>();
        }
        return mOcultarNotaInput;
    }

    public void mostrarNotaAprobacionInput(boolean checked){
        if (checked){
            double nota = 0;
            if (mCursoLocal.isInitialized() && mCursoLocal.getValue().getNotaAprobacion() != -1.0){
                nota = mCursoLocal.getValue().getNotaAprobacion();
            }
            mMostrarNotaInput.setValue(nota);
        }else{
            mOcultarNotaInput.setValue(-1.0);
        }
    }
    public CursoLocal validarCampos(String titulo, String descripcion, String precio, String notaAprobacion){
        if(mCursoLocal.getValue() == null) mCursoLocal.setValue(new CursoLocal());
        CursoLocal cursoLocal = mCursoLocal.getValue();
        cursoLocal.setTitulo(titulo);
        cursoLocal.setDescripcion(descripcion);
        try{
            float precioFloat = Float.parseFloat(precio);
            double notaDouble = Double.parseDouble(notaAprobacion);
            cursoLocal.setPrecio(precioFloat);
            cursoLocal.setNotaAprobacion(notaDouble);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return cursoLocal;
    }
    public void guardarProgresoCurso(String titulo, String descripcion, String precio, String notaAprobacion){

        validarCampos(titulo, descripcion, precio, notaAprobacion);
    }
    public void guardarCurso(String titulo, String descripcion, String precio, String notaAprobacion) {
        //validar campos
        CursoLocal cursoLocal = validarCampos(titulo, descripcion, precio, notaAprobacion);

        String token = SharedPreferencesUtil.leerToken(getApplication());

        RequestBody tituloField = RequestBody.create(MediaType.parse("text/plain"), cursoLocal.getTitulo());
        RequestBody descripcionField = RequestBody.create(MediaType.parse("text/plain"), cursoLocal.getDescripcion());
        RequestBody precioField = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cursoLocal.getPrecio()));
        RequestBody notaAprobacionField = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cursoLocal.getNotaAprobacion()));


        byte[] portada = transformarImagen();

        RequestBody portadaField= RequestBody.create(MediaType.parse("image/jpeg"), portada);

        MultipartBody.Part portadaPart = MultipartBody.Part.createFormData("portada", "imagen.jpg", portadaField);
        Call<CursoResponse> crearCursoCall = ApiClient.getCursosService().crearCurso(token, tituloField, descripcionField, precioField, notaAprobacionField, portadaPart);
        crearCursoCall.enqueue(new Callback<CursoResponse>() {
            @Override
            public void onResponse(Call<CursoResponse> call, Response<CursoResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplication(),"Curso creado", Toast.LENGTH_LONG).show();
                    guardarSecciones(response.body().getIdCurso());
                }else{
                    Toast.makeText(getApplication(),"Error al crear el curso", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CursoResponse> call, Throwable t) {
                Log.d("API_ERROR", t.getMessage());
                Toast.makeText(getApplication(),"Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void guardarSecciones(int idCurso){
        String token = SharedPreferencesUtil.leerToken(getApplication());
        List<SeccionLocal> seccionesLocales = mCursoLocal.getValue().getSeccionLocalList();
        SeccionesService seccionesService = ApiClient.getSeccionesService();

        for (SeccionLocal seccionLocal:
             seccionesLocales) {
            RequestBody idCursoField= RequestBody.create(MediaType.parse("text/plain"), idCurso + "");
            RequestBody tituloField = RequestBody.create(MediaType.parse("text/plain"), seccionLocal.getTitulo());
            RequestBody contenidoField= RequestBody.create(MediaType.parse("text/plain"), seccionLocal.getContenido());
            RequestBody ordenField= RequestBody.create(MediaType.parse("text/plain"), seccionLocal.getOrden() + "");

            MultipartBody.Part videoPart = null;
            File video;
            if (seccionLocal.getVideoUri() != null) {
                video = copiarUriAFile(seccionLocal.getVideoUri());

                RequestBody videoField = RequestBody.create(MediaType.parse("video/mp4"), video);
                videoPart= MultipartBody.Part.createFormData("video", video.getName(), videoField);
            }else{
                video = null;
            }
            List<MultipartBody.Part> archivosPart = new ArrayList<>();
            if (seccionLocal.getMaterialesExtra() != null){
            for (MaterialExtra materialExtra : seccionLocal.getMaterialesExtra()) {
                byte[] bytes = transformarArchivo(materialExtra.getUri());
                String formato = getApplication().getContentResolver().getType(materialExtra.getUri());
                RequestBody archivoBody = RequestBody.create(MediaType.parse(formato), bytes);
                archivosPart.add(MultipartBody.Part.createFormData("materialExtra", materialExtra.getNombre(), archivoBody));
            }}

            Call<SeccionResponse> seccionCall = seccionesService.crearSeccion(token, idCursoField, tituloField, contenidoField, ordenField, videoPart, archivosPart);
            seccionCall.enqueue(new Callback<SeccionResponse>() {
                @Override
                public void onResponse(Call<SeccionResponse> call, Response<SeccionResponse> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplication(),"Seccion creada", Toast.LENGTH_LONG).show();
                        if (video != null){
                            video.delete();
                        }
                    }else{
                        try {
                            Toast.makeText(getApplication(),"Error al crear el curso", Toast.LENGTH_LONG).show();
                            Log.d("API_ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SeccionResponse> call, Throwable t) {
                    Log.d("API_ERROR", t.getMessage());
                    Toast.makeText(getApplication(),"Error en el servidor", Toast.LENGTH_LONG).show();
                }
            });


        }
    }

    public void guardarProgresoSeccion(String titulo, String contenido) {
        SeccionLocal seccionLocal = new SeccionLocal();
        seccionLocal.setTitulo(titulo);
        seccionLocal.setContenido(contenido);
        List<SeccionLocal> seccionLocalList = mCursoLocal.getValue().getSeccionLocalList();
        if (seccionLocalList == null) {
           seccionLocalList = new LinkedList<>();
        }
        seccionLocal.setOrden(seccionLocalList.size() + 1);
        if (mMaterialesExtra.isInitialized()) seccionLocal.setMaterialesExtra(List.copyOf(mMaterialesExtra.getValue()));
        if (mVideoUri.getValue() != null) seccionLocal.setVideoUri(mVideoUri.getValue());
        mCursoLocal.getValue().getSeccionLocalList().add(seccionLocal);
        mSeccionAgregada.setValue(true);
        //mMaterialesExtra.setValue(new LinkedList<>());
        materialesExtra.clear();
    }

    public void limpiarMutables() {
        mSeccionAgregada = null;
        mVideoUri = null;
    }

    public void recibirVideo(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            mVideoUri.setValue(uri);
        }
    }
    private File copiarUriAFile(Uri uri) {
        File file = null;
        try {
            file = File.createTempFile("video", ".mp4",getApplication().getCacheDir());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public byte[] transformarArchivo(Uri uri){
        try {
            InputStream is = getApplication().getContentResolver().openInputStream(uri);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int n;

            byte[] datos = new byte[8192];
            while((n = is.read(datos)) != -1) buffer.write(datos, 0, n);

            return buffer.toByteArray();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recibirImagen(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            mImagenUri.setValue(uri);
        }
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = mImagenUri.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (
                FileNotFoundException er) {
            return new byte[]{};
        }
    }

    public void restaurarCurso(Bundle arguments){
        if (arguments == null) return;
        int idCurso = arguments.getInt("idCurso");

        String token = SharedPreferencesUtil.leerToken(getApplication());

        Call<CursoResponse> buscarCursoCall = ApiClient.getCursosService().buscarCurso(token, idCurso);

        buscarCursoCall.enqueue(new Callback<CursoResponse>() {
            @Override
            public void onResponse(Call<CursoResponse> call, Response<CursoResponse> response) {
                if (response.isSuccessful()) mostrarCurso(response.body());
                else Toast.makeText(getApplication(), "Error al recuperar el curso: " + response.code(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CursoResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();

            }
        });}
    public void mostrarCurso(CursoResponse curso) {

        if (curso != null){
            CursoLocal cursoLocal = new CursoLocal();

            cursoLocal.setTitulo(curso.getTitulo());
            cursoLocal.setDescripcion(curso.getDescripcion());
            for (SeccionResponse seccionResponse: curso.getSecciones()) {

                cursoLocal.getSeccionLocalList().add(new SeccionLocal(
                        seccionResponse.getIdSeccion(),
                        seccionResponse.getTitulo(),
                        seccionResponse.getContenido(),
                        seccionResponse.getOrden(),
                        seccionResponse.getVideoUrl()
                ));
            }
            mCursoLocal.setValue(cursoLocal);
            mSeccionAgregada.setValue(true);

        }


    }
}