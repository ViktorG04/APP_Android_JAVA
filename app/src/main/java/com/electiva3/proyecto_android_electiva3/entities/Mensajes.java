package com.electiva3.proyecto_android_electiva3.entities;

public class Mensajes {

    private String key;
    private String keyUsuario;
    private String titulo;
    private String detalle;

    public Mensajes() {
    }

    public Mensajes(String key, String keyUsuario, String titulo, String detalle) {
        this.key = key;
        this.keyUsuario = keyUsuario;
        this.titulo = titulo;
        this.detalle = detalle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyUsuario() {
        return keyUsuario;
    }

    public void setKeyUsuario(String keyUsuario) {
        this.keyUsuario = keyUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
