package com.example.kiwdy.api.dto.request;

public class ActualizarHabilitadoCursoRequest {
    private boolean habilitado;

    public ActualizarHabilitadoCursoRequest() {
    }

    public ActualizarHabilitadoCursoRequest(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
