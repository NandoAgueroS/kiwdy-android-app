package com.example.kiwdy.api.dto.response;

import java.io.Serializable;
import java.util.List;

public class CursoResponse implements Serializable {
    private int idCurso;
    private String titulo;
    private String descripcion;
    private String portadaUrl;
    private UsuarioResponse usuarioInstructor;
    private double notaAprobacion;
    private float precio;
    private boolean habilitado;
    private List<SeccionResponse> secciones;
    private boolean estaInscripto;
    private boolean estaFinalizado;

    public CursoResponse() {
    }

    public CursoResponse(int idCurso, String titulo, String descripcion, String portadaUrl, double notaAprobacion, List<SeccionResponse> secciones, boolean estaInscripto, boolean estaFinalizado, UsuarioResponse usuarioInstructor, float precio, boolean habilitado) {
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.portadaUrl = portadaUrl;
        this.notaAprobacion = notaAprobacion;
        this.secciones = secciones;
        this.estaInscripto = estaInscripto;
        this.estaFinalizado = estaFinalizado;
        this.usuarioInstructor = usuarioInstructor;
        this.precio = precio;
        this.habilitado = habilitado;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public UsuarioResponse getUsuarioInstructor() {
        return usuarioInstructor;
    }

    public void setUsuarioInstructor(UsuarioResponse usuarioInstructor) {
        this.usuarioInstructor = usuarioInstructor;
    }

    public double getNotaAprobacion() {
        return notaAprobacion;
    }

    public void setNotaAprobacion(double notaAprobacion) {
        this.notaAprobacion = notaAprobacion;
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
