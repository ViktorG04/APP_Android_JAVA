package com.electiva3.proyecto_android_electiva3.entities;

public class DetalleOrden {

    private String key;
    private String orden;
    private String nombreServicio;
    private String servicio;
    private String estado;
    private String cantidad;
    private String precio;
    private boolean notificar;
    private boolean aprovacion;


    public DetalleOrden(){

    }

    public DetalleOrden(String nombreServicio, String estado, String precio) {
        this.nombreServicio = nombreServicio;
        this.estado = estado;
        this.precio = precio;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

    public void setOrden(String orden){
        this.orden =  orden;
    }

    public String getOrden(){
        return orden;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isNotificar() {
        return notificar;
    }

    public void setNotificar(boolean notificar) {
        this.notificar = notificar;
    }

    public boolean isAprovacion() {
        return aprovacion;
    }

    public void setAprovacion(boolean aprovacion) {
        this.aprovacion = aprovacion;
    }

}
