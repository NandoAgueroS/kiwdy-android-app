package com.example.kiwdy.api.dto.request;

public class LoginRequest {

    private String email;

    private String clave;

    private int rol;

    public LoginRequest() {
    }

    public LoginRequest(String email, String clave, int rol) {
        this.email = email;
        this.clave = clave;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
}
