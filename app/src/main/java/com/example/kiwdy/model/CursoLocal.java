package com.example.kiwdy.model;

import java.util.LinkedList;
import java.util.List;

public class CursoLocal {
    private String titulo;
    private String descripcion;
    private List<SeccionLocal> seccionLocalList = new LinkedList<>();

    public CursoLocal() {
    }

    public CursoLocal(String titulo, String descripcion, List<SeccionLocal> seccionLocalList) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.seccionLocalList = seccionLocalList;
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
