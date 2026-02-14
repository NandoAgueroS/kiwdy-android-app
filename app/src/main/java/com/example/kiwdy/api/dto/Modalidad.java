package com.example.kiwdy.api.dto;

public enum Modalidad {
    VIRTUAL (0),
    PRESENCIAL (1);

    private int codigo;

    Modalidad(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static Modalidad fromCodigo(int codigo){
        for (Modalidad modalidad : values()){
            if (modalidad.codigo == codigo){
                return modalidad;
            }
        }

        throw new IllegalArgumentException(String.format("No existe una modalidad con el código %d", codigo));
    }
}
