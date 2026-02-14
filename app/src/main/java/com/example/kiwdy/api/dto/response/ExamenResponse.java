package com.example.kiwdy.api.dto.response;

import java.time.LocalDateTime;

public class ExamenResponse {

    private int idExamen;

    private LocalDateTime fechaYHora;

    private int modalidad;

    private String link;

    private String direccion;

    private int nota;

    private InscripcionResponse inscripcion;

    public ExamenResponse() {
    }

    public ExamenResponse(int idExamen, LocalDateTime fechaYHora, int modalidad, String link, String direccion, int nota, InscripcionResponse inscripcion) {
        this.idExamen = idExamen;
        this.fechaYHora = fechaYHora;
        this.modalidad = modalidad;
        this.link = link;
        this.direccion = direccion;
        this.nota = nota;
        this.inscripcion = inscripcion;
    }

    public int getModalidad() {
        return modalidad;
    }

    public void setModalidad(int modalidad) {
        this.modalidad = modalidad;
    }

    public int getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(int idExamen) {
        this.idExamen = idExamen;
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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public InscripcionResponse getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(InscripcionResponse inscripcion) {
        this.inscripcion = inscripcion;
    }
}
