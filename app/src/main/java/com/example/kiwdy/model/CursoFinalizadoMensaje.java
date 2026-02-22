package com.example.kiwdy.model;

public class CursoFinalizadoMensaje {
    private String mensaje;
    private int idCurso;

    public CursoFinalizadoMensaje() {
    }

    public CursoFinalizadoMensaje(String mensaje, int idCurso) {
        this.mensaje = mensaje;
        this.idCurso = idCurso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
}
