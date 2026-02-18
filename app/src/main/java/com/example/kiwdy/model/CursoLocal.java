package com.example.kiwdy.model;

import java.util.LinkedList;
import java.util.List;

public class CursoLocal {
    private String titulo;
    private String descripcion;
    private float precio;
    private double notaAprobacion;
    private List<SeccionLocal> seccionLocalList = new LinkedList<>();

    public CursoLocal() {
    }

    public CursoLocal(String titulo, String descripcion, float precio, double notaAprobacion, List<SeccionLocal> seccionLocalList) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.notaAprobacion = notaAprobacion;
        this.seccionLocalList = seccionLocalList;
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
