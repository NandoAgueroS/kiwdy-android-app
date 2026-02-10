package com.example.kiwdy.api.dto;

public enum EstadoInscripcion {
    SOLICITADA(0),
    EN_CURSO(1),
    PENDIENTE_CERTIFICACION(2),
    CERTIFICADA(3);

    private int codigo;
    EstadoInscripcion(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
