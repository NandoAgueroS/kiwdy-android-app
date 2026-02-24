package com.example.kiwdy.model;

import android.net.Uri;

import com.example.kiwdy.api.dto.request.CrearSeccionRequest;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SeccionLocal implements Serializable {

    private int idSeccion;

    private String titulo;

    private String contenido;

    private int orden;

    private String videoUri;
    private String videoUrl;

    private List<MaterialExtra> materialesExtra = new LinkedList<>();

    public SeccionLocal() {
    }

    public SeccionLocal(int idSeccion, String titulo, String contenido, int orden, String videoUri, List<MaterialExtra> materialesExtra) {
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.orden = orden;
        this.videoUri = videoUri;
        this.materialesExtra = materialesExtra;
    }

    public SeccionLocal(int idSeccion, String titulo, String contenido, int orden, String videoUrl) {
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.orden = orden;
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public List<MaterialExtra> getMaterialesExtra() {
        return materialesExtra;
    }

    public void setMaterialesExtra(List<MaterialExtra> materialesExtra) {
        this.materialesExtra = materialesExtra;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }
}
