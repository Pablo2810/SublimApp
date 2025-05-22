package com.tallerwebi.presentacion.dto;

public class DatosUsuario {
    private String email;
    private String nombre;

    public DatosUsuario() {

    }

    public DatosUsuario(String email) {
        this.email = email;
    }

    public DatosUsuario(String email, String nombre) {
        this.email = email;
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
