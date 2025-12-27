package com.example.kiwdy.api.dto.request;

public class CrearSeccionRequest {

    private String titulo;

    private String contenido;

    private int idCurso;

    private int orden;

    public CrearSeccionRequest() {
    }

    public CrearSeccionRequest(String titulo, String contenido, int idCurso, int orden) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.idCurso = idCurso;
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
}
