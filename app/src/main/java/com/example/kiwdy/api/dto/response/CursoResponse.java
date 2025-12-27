package com.example.kiwdy.api.dto.response;

import java.util.List;

public class CursoResponse {
    private int idCurso;
    private String titulo;
    private String descripcion;
    private List<SeccionResponse> secciones;

    public CursoResponse() {
    }

    public CursoResponse(int idCurso, String titulo, String descripcion, List<SeccionResponse> secciones) {
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.secciones = secciones;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SeccionResponse> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<SeccionResponse> secciones) {
        this.secciones = secciones;
    }
}
