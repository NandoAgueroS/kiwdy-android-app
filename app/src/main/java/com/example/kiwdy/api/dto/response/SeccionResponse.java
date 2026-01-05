package com.example.kiwdy.api.dto.response;

import java.util.List;

public class SeccionResponse {
    private int idSeccion;
    private String titulo;
    private String contenido;
    private int orden;
    private String videoUrl;
    private List<ArchivoSeccionResponse> archivos;

    public SeccionResponse() {
    }

    public SeccionResponse(int idSeccion, String titulo, String contenido, int orden, String videoUrl, List<ArchivoSeccionResponse> archivos) {
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.orden = orden;
        this.videoUrl = videoUrl;
        this.archivos = archivos;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
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

    public List<ArchivoSeccionResponse> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoSeccionResponse> archivos) {
        this.archivos = archivos;
    }
}
