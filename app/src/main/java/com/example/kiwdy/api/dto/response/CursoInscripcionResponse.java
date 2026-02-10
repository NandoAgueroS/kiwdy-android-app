package com.example.kiwdy.api.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CursoInscripcionResponse implements Serializable
    {
        private int idCurso;

        private String titulo;

        private String descripcion;

        private String portadaUrl;

        private BigDecimal precio;

        private String instructor;

        private boolean estaInscripto;

        private int ultimaSeccionCompletada;

        private int idInscripcion;

        private String estadoInscripcion;

        public List<SeccionResponse> secciones;

        public CursoInscripcionResponse(int idCurso, String titulo, String descripcion, String portadaUrl, BigDecimal precio, String instructor, boolean estaInscripto, int ultimaSeccionCompletada, int idInscripcion, String estadoInscripcion, List<SeccionResponse> secciones) {
            this.idCurso = idCurso;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.portadaUrl = portadaUrl;
            this.precio = precio;
            this.instructor = instructor;
            this.estaInscripto = estaInscripto;
            this.ultimaSeccionCompletada = ultimaSeccionCompletada;
            this.idInscripcion = idInscripcion;
            this.estadoInscripcion = estadoInscripcion;
            this.secciones = secciones;
        }

        public CursoInscripcionResponse() {
        }

        public int getUltimaSeccionCompletada() {
            return ultimaSeccionCompletada;
        }

        public void setUltimaSeccionCompletada(int ultimaSeccionCompletada) {
            this.ultimaSeccionCompletada = ultimaSeccionCompletada;
        }

        public int getIdInscripcion() {
            return idInscripcion;
        }

        public void setIdInscripcion(int idInscripcion) {
            this.idInscripcion = idInscripcion;
        }

        public List<SeccionResponse> getSecciones() {
            return secciones;
        }

        public void setSecciones(List<SeccionResponse> secciones) {
            this.secciones = secciones;
        }

        public int getIdCurso() {
            return idCurso;
        }

        public void setIdCurso(int idCurso) {
            this.idCurso = idCurso;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getPortadaUrl() {
            return portadaUrl;
        }

        public void setPortadaUrl(String portadaUrl) {
            this.portadaUrl = portadaUrl;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio;
        }

        public String getInstructor() {
            return instructor;
        }

        public void setInstructor(String instructor) {
            this.instructor = instructor;
        }

        public boolean isEstaInscripto() {
            return estaInscripto;
        }

        public void setEstaInscripto(boolean estaInscripto) {
            this.estaInscripto = estaInscripto;
        }

        public String getEstadoInscripcion() {
            return estadoInscripcion;
        }

        public void setEstadoInscripcion(String estadoInscripcion) {
            this.estadoInscripcion = estadoInscripcion;
        }
    }
