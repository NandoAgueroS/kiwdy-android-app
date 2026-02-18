package com.example.kiwdy.api.dto.response;

public class ArchivoSeccionResponse{
    private int idMaterial;
    private String nombre;
    private String url;

    public ArchivoSeccionResponse(int idMaterial, String nombre, String url) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
        this.url = url;
    }

    public ArchivoSeccionResponse() {
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
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

