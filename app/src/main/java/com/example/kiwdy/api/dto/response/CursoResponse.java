package com.example.kiwdy.api.dto.response;

import java.io.Serializable;
import java.util.List;

public class CursoResponse implements Serializable {
    private int idCurso;
    private String titulo;
    private String descripcion;
    private String portadaUrl;
    private List<SeccionResponse> secciones;
    private boolean estaInscripto;
    private boolean estaFinalizado;

    public CursoResponse() {
    }

    public CursoResponse(int idCurso, String titulo, String descripcion, String portadaUrl, List<SeccionResponse> secciones) {
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.portadaUrl = portadaUrl;
        this.secciones = secciones;
    }

    public boolean isEstaInscripto() {
        return estaInscripto;
    }

    public void setEstaInscripto(boolean estaInscripto) {
        this.estaInscripto = estaInscripto;
    }

    public boolean isEstaFinalizado() {
        return estaFinalizado;
    }

    public void setEstaFinalizado(boolean estaFinalizado) {
        this.estaFinalizado = estaFinalizado;
    }

    public String getPortadaUrl() {
        return portadaUrl;
    }

    public void setPortadaUrl(String portadaUrl) {
        this.portadaUrl = portadaUrl;
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
