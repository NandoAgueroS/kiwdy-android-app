package com.example.kiwdy.api.dto.response;

import java.util.List;

public class SeccionResponse {
    private int idSeccion;
    private String titulo;
    private String contenido;
    private int orden;
    private String videoUrl;
    private List<ArchivoSeccionResponse> materiales;
    private int idCurso;
    private boolean completada;

    public SeccionResponse() {
    }

    public SeccionResponse(int idSeccion, String titulo, String contenido, int orden, String videoUrl, List<ArchivoSeccionResponse> materiales, boolean completada, int idCurso) {
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.orden = orden;
        this.videoUrl = videoUrl;
        this.materiales = materiales;
        this.completada = completada;
        this.idCurso = idCurso;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
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

    public List<ArchivoSeccionResponse> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<ArchivoSeccionResponse> materiales) {
        this.materiales = materiales;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
