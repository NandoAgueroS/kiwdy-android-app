package com.example.kiwdy.api.dto.request;

import java.math.BigDecimal;

public class CrearCursoRequest {

    private String titulo;

    private String descripcion;

    private BigDecimal precio;

    public CrearCursoRequest() {
    }

    public CrearCursoRequest(String titulo, String descripcion, BigDecimal precio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
