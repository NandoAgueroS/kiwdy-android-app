package com.example.kiwdy.api.dto.request;

import java.time.LocalDateTime;

public class CrearExamenRequest {

    public int modalidad;

    public LocalDateTime fechaYHora;

    public String link;

    public String direccion;

    public int idInscripcion;

    public CrearExamenRequest() {
    }

    public CrearExamenRequest(int modalidad, LocalDateTime fechaYHora, String link, String direccion, int idInscripcion) {
        this.modalidad = modalidad;
        this.fechaYHora = fechaYHora;
        this.link = link;
        this.direccion = direccion;
        this.idInscripcion = idInscripcion;
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
