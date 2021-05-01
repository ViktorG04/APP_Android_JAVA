package com.electiva3.proyecto_android_electiva3.entities;

public class Articulo {
    private String servicio;
    private String precio;

    public Articulo(String servicio, String precio) {
        this.servicio = servicio;
        this.precio = precio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
