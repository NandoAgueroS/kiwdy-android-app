package com.example.kiwdy.model;

import android.net.Uri;

public class MaterialExtra {
    private String nombre;
    private Uri uri;

    public MaterialExtra() {
    }

    public MaterialExtra(String nombre, Uri uri) {
        this.nombre = nombre;
        this.uri = uri;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
