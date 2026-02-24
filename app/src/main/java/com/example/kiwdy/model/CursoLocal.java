package com.example.kiwdy.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CursoLocal implements Serializable {
    private int idCurso;
    private String titulo;
    private String descripcion;
    private float precio;
    private boolean requiereExamen;
    private double notaAprobacion;
    private String portadaUri;
    private String portadaUrl;
    private String nombreArchivoBorrador;
    private boolean habilitado;
    private List<SeccionLocal> seccionLocalList = new LinkedList<>();

    public CursoLocal() {
    }

    public CursoLocal(String titulo, String descripcion, float precio, double notaAprobacion, List<SeccionLocal> seccionLocalList, String portadaUri, String nombreArchivoBorrador, boolean requiereExamen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.notaAprobacion = notaAprobacion;
        this.seccionLocalList = seccionLocalList;
        this.portadaUri = portadaUri;
        this.nombreArchivoBorrador = nombreArchivoBorrador;
        this.requiereExamen = requiereExamen;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getPortadaUrl() {
        return portadaUrl;
    }

    public void setPortadaUrl(String portadaUrl) {
        this.portadaUrl = portadaUrl;
    }

    public boolean isRequiereExamen() {
        return requiereExamen;
    }

    public void setRequiereExamen(boolean requiereExamen) {
        this.requiereExamen = requiereExamen;
    }

    public String getNombreArchivoBorrador() {
        return nombreArchivoBorrador;
    }

    public void setNombreArchivoBorrador(String nombreArchivoBorrador) {
        this.nombreArchivoBorrador = nombreArchivoBorrador;
    }

    public String getPortadaUri() {
        return portadaUri;
    }

    public void setPortadaUri(String portadaUri) {
        this.portadaUri = portadaUri;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public double getNotaAprobacion() {
        return notaAprobacion;
    }

    public void setNotaAprobacion(double notaAprobacion) {
        this.notaAprobacion = notaAprobacion;
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

    public List<SeccionLocal> getSeccionLocalList() {
        return seccionLocalList;
    }

    public void setSeccionLocalList(List<SeccionLocal> seccionLocalList) {
        this.seccionLocalList = seccionLocalList;
    }
}
