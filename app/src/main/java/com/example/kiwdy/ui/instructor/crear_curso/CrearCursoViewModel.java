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

public class CrearCursoViewModel extends AndroidViewModel {

    private MutableLiveData<String> mError;
    private MutableLiveData<String> mErrorDeValidacion;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<CursoLocal> mCursoLocal;
    private MutableLiveData<CursoLocal> mCursoLocalModoVisualizacion;
    private MutableLiveData<Boolean> mSeccionAgregada = new MutableLiveData<>();
    private MutableLiveData<String> mNavegarACrearSeccion;
    private MutableLiveData<Uri> mImagenUri;
    private MutableLiveData<Uri> mImagenUrlGLide;
    private MutableLiveData<Double> mMostrarNotaInput;
    private MutableLiveData<Double> mOcultarNotaInput;
    private MutableLiveData<Boolean> mActivarCheckRequiereExamen;
    private MutableLiveData<Boolean> mMostrarVistaPrevia;
    private MutableLiveData<Boolean> mOcultarVistaPrevia;
    private String nombreArchivoBorrador;
    private boolean guardadoExitoso = true;

    public CrearCursoViewModel(@NonNull Application application) {
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

    public LiveData<CursoLocal> getmCursoLocal(){
        if (mCursoLocal == null) mCursoLocal = new MutableLiveData<>();
        return mCursoLocal;
    }

    public LiveData<CursoLocal> getmCursoLocalModoVisualizacion(){
        if (mCursoLocalModoVisualizacion == null) mCursoLocalModoVisualizacion = new MutableLiveData<>();
        return mCursoLocalModoVisualizacion;
    }

    public LiveData<String> getmNavegarACrearSeccion(){
        if (mNavegarACrearSeccion == null) {
            mNavegarACrearSeccion = new MutableLiveData<>();
        }
        return mNavegarACrearSeccion;
    }

    public LiveData<Uri> getmImagenUri() {
        if (mImagenUri== null) mImagenUri= new MutableLiveData<>();
        return mImagenUri;
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
    public LiveData<Boolean> getmActivarCheckRequiereExamen(){
        if (mActivarCheckRequiereExamen == null) {
            mActivarCheckRequiereExamen = new MutableLiveData<>();
        }
        return mActivarCheckRequiereExamen;
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

    public CursoLocal validarCampos(String titulo, String descripcionVar, String descripcionEditText, String precio, String notaAprobacion, boolean requiereExamen, boolean modoVistaPrevia){
        String descripcion;
        float precioFloat = -1;
        double notaDouble = -1;
        if (modoVistaPrevia){
            descripcion = descripcionVar;
        }else{
            descripcion = descripcionEditText;
        }

        StringBuilder mensajes = new StringBuilder();

        boolean valido = true;

        if (titulo.isBlank()){
            mensajes.append("Debe ingresar un título\n");
            valido = false;
        }
        if (descripcion.isBlank()){
            mensajes.append("Debe ingresar una descripcion \n");
            valido = false;
        }
        try {
            precioFloat = Float.parseFloat(precio);
            if (precioFloat < 0){
                mensajes.append("Debe ingresar un precio válido \n");
                valido = false;
            }
        } catch (NumberFormatException e) {
            mensajes.append("Debe ingresar un precio numérico \n");
            valido = false;
        }
        if (requiereExamen){
            try {
                notaDouble = Double.parseDouble(notaAprobacion);
                if (notaDouble <= 0){
                    mensajes.append("Debe ingresar una nota mayor a 0 \n");
                    valido = false;
                }
            }catch (NumberFormatException e){
                mensajes.append("Debe ingresar una nota válida \n");
                valido = false;
            }
        }else{
            notaDouble = -1;
        }
        if (!mImagenUri.isInitialized()) {
            mensajes.append("Debe cargar una imágen de portada \n");
            valido = false;
        }else if (mImagenUri.getValue() == null){
            mensajes.append("Debe cargar una imágen de portada \n");
            valido = false;
        }
        if (!valido){
            mErrorDeValidacion.setValue("Datos inválidos, revise los campos e intente nuevamente");
            mMensaje.setValue(mensajes.toString());
            return null;
        }else{
            mMensaje.setValue("");
            if(mCursoLocal.getValue() == null) mCursoLocal.setValue(new CursoLocal());
            CursoLocal cursoLocal = mCursoLocal.getValue();
            cursoLocal.setPortadaUri(mImagenUri.getValue().toString());
            cursoLocal.setTitulo(titulo);
            cursoLocal.setDescripcion(descripcion);
            cursoLocal.setPrecio(precioFloat);
            cursoLocal.setNotaAprobacion(notaDouble);
            cursoLocal.setRequiereExamen(requiereExamen);
            return cursoLocal;
        }
    }

    public void guardarProgresoCurso(String titulo, String descripcionVar, String descripcionEditText, String precio, String notaAprobacion, boolean requiereExamen, boolean modoVistaPrevia, boolean navegarACrearSeccion, int ordenSeccion){
        CursoLocal cursoLocal = validarCampos(titulo, descripcionVar, descripcionEditText, precio, notaAprobacion, requiereExamen, modoVistaPrevia);
        if (cursoLocal != null){
            guardarLocalmente(cursoLocal);
            if (navegarACrearSeccion){
                mNavegarACrearSeccion.setValue(nombreArchivoBorrador);
            }
        }
    }

    public void guardarCurso(String titulo, String descripcionVar, String descripcionEditText, String precio, String notaAprobacion, boolean requiereExamen, boolean modoVistaPrevia) {
        //validar campos
        CursoLocal cursoLocal = validarCampos(titulo, descripcionVar, descripcionEditText, precio, notaAprobacion, requiereExamen, modoVistaPrevia);
        if (cursoLocal == null) return;
        if (cursoLocal.getSeccionLocalList() == null || cursoLocal.getSeccionLocalList().isEmpty()){
            mErrorDeValidacion.setValue("El curso debe tener al menos una sección");
            return;
        }

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
                    guardadoExitoso = false;
                }
            }

            @Override
            public void onFailure(Call<CursoResponse> call, Throwable t) {
                Log.d("API_ERROR", t.getMessage());
                Toast.makeText(getApplication(),"Error en el servidor", Toast.LENGTH_LONG).show();
                guardadoExitoso = false;
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
                video = leerVideoLocal(seccionLocal.getVideoUri().substring(seccionLocal.getVideoUri().lastIndexOf("/") + 1));

                RequestBody videoField = RequestBody.create(MediaType.parse("video/mp4"), video);
                videoPart= MultipartBody.Part.createFormData("video", video.getName(), videoField);
            }else{
                video = null;
            }
            List<MultipartBody.Part> archivosPart = new ArrayList<>();
            if (seccionLocal.getMaterialesExtra() != null){
            for (MaterialExtra materialExtra : seccionLocal.getMaterialesExtra()) {
                Uri uri = Uri.parse(materialExtra.getUri());
                String uriString = uri.toString();
                File archivo = leerVideoLocal(uriString.substring(uriString.lastIndexOf("/") + 1));
                String formato = URLConnection.guessContentTypeFromName(archivo.getName());
                RequestBody archivoBody = RequestBody.create(MediaType.parse(formato), archivo);
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
                        guardadoExitoso = false;
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
                    guardadoExitoso = false;
                }
            });
            if (guardadoExitoso && nombreArchivoBorrador != null){
                boolean eliminado = eliminarBorrador(nombreArchivoBorrador);
            }


        }
    }

    public void alternarVistaPreviaDescripcion(boolean isChecked){
        if (isChecked){
            mMostrarVistaPrevia.setValue(true);
        }else{
            mOcultarVistaPrevia.setValue(true);
        }
    }

    public File leerVideoLocal(String nombreVideo){
        File file = new File(getApplication().getFilesDir(), nombreVideo);
        if (file.exists()){
            return file;
        }else{
            return null;
        }
    }
    public void leerLocal(String nombreArchivoBorrador){
        if (nombreArchivoBorrador != null){
            this.nombreArchivoBorrador = nombreArchivoBorrador;
        }
        File dir = new File(getApplication().getFilesDir(), "borradores");
        File file = new File(dir, nombreArchivoBorrador);
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(file);
            CursoLocal cursoLocal = gson.fromJson(reader, CursoLocal.class);
            mCursoLocal.setValue(cursoLocal);
            if (cursoLocal.getNotaAprobacion() != -1){
                mActivarCheckRequiereExamen.setValue(true);
            }
            mImagenUri.setValue(Uri.parse(cursoLocal.getPortadaUri()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean eliminarBorrador(String nombreArchivoBorrador){
        File dir = new File(getApplication().getFilesDir(), "borradores");
        File file = new File(dir, nombreArchivoBorrador);
        return file.delete();
    }
    public void guardarLocalmente(CursoLocal cursoLocal){
        if (nombreArchivoBorrador == null){
            nombreArchivoBorrador = JwtUtil.obtenerId(SharedPreferencesUtil.leerToken(getApplication())) + "_borrador_" + System.currentTimeMillis();
        }
        File dir = new File(getApplication().getFilesDir(), "borradores");
        if (!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir, nombreArchivoBorrador);
        cursoLocal.setNombreArchivoBorrador(nombreArchivoBorrador);
        mCursoLocal.getValue().setNombreArchivoBorrador(nombreArchivoBorrador);
        Gson gson = new Gson();

        String json = gson.toJson(cursoLocal);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void limpiarMutables() {
        mSeccionAgregada = null;
        mNavegarACrearSeccion = null;
        mActivarCheckRequiereExamen = null;
        mMostrarNotaInput = null;
        mOcultarNotaInput = null;
        mError = null;
        mErrorDeValidacion = null;
    }

    private File copiarUriAFile(Uri uri, String prefijo, String extencion) {
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
            String tipo = getApplication().getContentResolver().getType(data.getData());
            if (!tipo.startsWith("image/")){
                mErrorDeValidacion.setValue("El archivo no es una imágen");
                return;
            }
            Uri uri = Uri.fromFile(copiarUriAFile(data.getData(), "portada", null));
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

    public void cargarBorrador(Bundle arguments){
        if (arguments != null){
            if (arguments.containsKey("nombreArchivoBorrador")){
                nombreArchivoBorrador = arguments.getString("nombreArchivoBorrador");
                leerLocal(nombreArchivoBorrador);
            }else if (arguments.containsKey("idCurso")){
                restaurarCurso(arguments);
            }
        }else if (nombreArchivoBorrador != null){
            leerLocal(nombreArchivoBorrador);
        }else{
            mCursoLocal.setValue(new CursoLocal());
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
                if (response.isSuccessful()) mapearCursoResponseACursoLocal(response.body());
                else Toast.makeText(getApplication(), "Error al recuperar el curso: " + response.code(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CursoResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();

            }
        });}
    public void mapearCursoResponseACursoLocal(CursoResponse curso) {

        if (curso != null){
            CursoLocal cursoLocal = new CursoLocal();

            cursoLocal.setIdCurso(curso.getIdCurso());
            cursoLocal.setTitulo(curso.getTitulo());
            cursoLocal.setDescripcion(curso.getDescripcion());
            cursoLocal.setNotaAprobacion(curso.getNotaAprobacion());
            cursoLocal.setPortadaUrl(curso.getPortadaUrl());
            if (curso.getNotaAprobacion() != -1){
                cursoLocal.setRequiereExamen(true);
                mMostrarNotaInput.postValue(curso.getNotaAprobacion());
                mActivarCheckRequiereExamen.postValue(true);
            }
            cursoLocal.setPrecio(curso.getPrecio());
            for (SeccionResponse seccionResponse: curso.getSecciones()) {

                cursoLocal.getSeccionLocalList().add(new SeccionLocal(
                        seccionResponse.getIdSeccion(),
                        seccionResponse.getTitulo(),
                        seccionResponse.getContenido(),
                        seccionResponse.getOrden(),
                        seccionResponse.getVideoUrl()
                ));
            }
            mCursoLocalModoVisualizacion.postValue(cursoLocal);
            mMostrarVistaPrevia.postValue(true);
            //mSeccionAgregada.setValue(true);


        }


    }
}