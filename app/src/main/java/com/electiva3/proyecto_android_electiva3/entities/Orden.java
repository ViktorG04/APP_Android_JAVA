package com.electiva3.proyecto_android_electiva3.entities;

import java.util.HashMap;

public class Orden {

    private String key;
    private String reserva;
    private String cliente;
    private String nombreCliente;
    private String supervisor;
    private String contrato;
    private String numeroOrden;
    private String estado;
    private String fecha;
    private HashMap<String , Object> ordenMap  =  new HashMap<>();


    public Orden(){

    }

    public Orden(String numeroOrden , String cliente ,  String estado ,  String fecha){
        this.numeroOrden = numeroOrden;
        this.cliente = cliente;
        this.estado  = estado;
        this.fecha = fecha;
    }

    public void setKey(String key){
        this.key =  key;
    }

    public String getKey(){
        return key;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getReserva() {
        return reserva;
    }

    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }


    public void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }

    public String getNombreCliente(){
        return nombreCliente;
    }


    public void UpdateOrden(){
        ordenMap.put("cliente" , cliente );
        ordenMap.put("contrato" , contrato );
        ordenMap.put("supervisor" ,   supervisor   );
        ordenMap.put("reserva" , reserva );
        ordenMap.put("numeroOrden" , numeroOrden );
        ordenMap.put("estado" , estado );
        ordenMap.put("fecha" , fecha );
    }

    public HashMap<String, Object> getOrdenMap() {
        return ordenMap;
    }

    public void setOrdenMap(HashMap<String, Object> ordenMap) {
        this.ordenMap = ordenMap;
    }
}
