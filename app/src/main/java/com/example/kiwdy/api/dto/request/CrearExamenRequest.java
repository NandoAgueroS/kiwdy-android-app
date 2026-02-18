package com.example.kiwdy.api.dto.request;

import java.time.LocalDateTime;

public class CrearExamenRequest {

    private int modalidad;

    private LocalDateTime fechaYHora;

    private String link;

    private String direccion;

    private float precio;

    private double notaAprobacion;

    private int idInscripcion;

    public CrearExamenRequest() {
    }

    public CrearExamenRequest(int modalidad, LocalDateTime fechaYHora, String link, String direccion, float precio, double notaAprobacion, int idInscripcion) {
        this.modalidad = modalidad;
        this.fechaYHora = fechaYHora;
        this.link = link;
        this.direccion = direccion;
        this.precio = precio;
        this.notaAprobacion = notaAprobacion;
        this.idInscripcion = idInscripcion;
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

    public int getModalidad() {
        return modalidad;
    }

    public void setModalidad(int modalidad) {
        this.modalidad = modalidad;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }
}
