package com.example.kiwdy.model;

import com.example.kiwdy.api.dto.response.ExamenResponse;
import com.example.kiwdy.api.dto.response.InscripcionResponse;

import java.util.List;

public class InscripcionExamenes {
    private InscripcionResponse inscripcionResponse;
    private List<ExamenResponse> examenResponses;
    private String estadoAprobacion;

    public InscripcionExamenes() {
    }

    public InscripcionExamenes(InscripcionResponse inscripcionResponse, List<ExamenResponse> examenResponses, String estadoAprobacion) {
        this.inscripcionResponse = inscripcionResponse;
        this.examenResponses = examenResponses;
        this.estadoAprobacion = estadoAprobacion;
    }

    public String getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(String estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }

    public InscripcionResponse getInscripcionResponse() {
        return inscripcionResponse;
    }

    public void setInscripcionResponse(InscripcionResponse inscripcionResponse) {
        this.inscripcionResponse = inscripcionResponse;
    }

    public List<ExamenResponse> getExamenResponses() {
        return examenResponses;
    }

    public void setExamenResponses(List<ExamenResponse> examenResponses) {
        this.examenResponses = examenResponses;
    }
}
