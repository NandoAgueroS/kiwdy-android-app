package com.example.kiwdy.api.dto.request;

public class MarcarSeccionCompletadaRequest {
    private int idSeccion;

    public MarcarSeccionCompletadaRequest(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public MarcarSeccionCompletadaRequest() {
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }
}
