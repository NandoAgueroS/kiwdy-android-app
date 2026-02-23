package com.example.kiwdy.model;

import android.net.Uri;

import java.io.Serializable;

public class MaterialExtra implements Serializable {
    private String nombre;
    private String uri;

    public MaterialExtra() {
    }

    public MaterialExtra(String nombre, String uri) {
        this.nombre = nombre;
        this.uri = uri;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
