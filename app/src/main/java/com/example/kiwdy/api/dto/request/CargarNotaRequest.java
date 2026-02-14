package com.example.kiwdy.api.dto.request;

public class CargarNotaRequest {
    private int nota;

    public CargarNotaRequest() {
    }

    public CargarNotaRequest(int nota) {
        this.nota = nota;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
