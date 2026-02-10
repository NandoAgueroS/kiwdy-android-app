package com.example.kiwdy.api.dto.response;

import java.time.LocalDateTime;

public class InscripcionResponse {
    private int idInscripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
    private CursoResponse curso;
    private int idUsuarioAlumno;
    private int ultimaSeccionCompletada;
    private UsuarioResponse usuarioAlumno;

    public InscripcionResponse() {
    }

    public InscripcionResponse(int idInscripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado, CursoResponse curso, int idUsuarioAlumno, int ultimaSeccionCompletada, UsuarioResponse usuarioAlumno) {
        this.idInscripcion = idInscripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.curso = curso;
        this.idUsuarioAlumno = idUsuarioAlumno;
        this.ultimaSeccionCompletada = ultimaSeccionCompletada;
        this.usuarioAlumno = usuarioAlumno;
    }

    public int getUltimaSeccionCompletada() {
        return ultimaSeccionCompletada;
    }

    public void setUltimaSeccionCompletada(int ultimaSeccionCompletada) {
        this.ultimaSeccionCompletada = ultimaSeccionCompletada;
    }

    public UsuarioResponse getUsuarioAlumno() {
        return usuarioAlumno;
    }

    public void setUsuarioAlumno(UsuarioResponse usuarioAlumno) {
        this.usuarioAlumno = usuarioAlumno;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CursoResponse getCurso() {
        return curso;
    }

    public void setCurso(CursoResponse curso) {
        this.curso = curso;
    }

    public int getIdUsuarioAlumno() {
        return idUsuarioAlumno;
    }

    public void setIdUsuarioAlumno(int idUsuarioAlumno) {
        this.idUsuarioAlumno = idUsuarioAlumno;
    }
}
