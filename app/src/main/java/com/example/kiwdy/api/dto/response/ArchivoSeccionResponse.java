package com.example.kiwdy.api.dto.response;

public class ArchivoSeccionResponse{
    private int idArchivo;
    private String nombre;
    private String url;

    public ArchivoSeccionResponse(int idArchivo, String nombre, String url) {
        this.idArchivo = idArchivo;
        this.nombre = nombre;
        this.url = url;
    }

    public ArchivoSeccionResponse() {
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

