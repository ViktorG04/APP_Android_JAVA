package com.electiva3.proyecto_android_electiva3.entities;

import java.util.HashMap;

public class Reserva {

    private String key;
    private String cliente;
    private String nombreCliente;
    private String contrato;
    private String numeroContrato;
    private String fecha;
    private String fechaSolicitada;
    private String hora;
    private String fechaHoraCreacion;
    private String estado;
    private HashMap<String, Object> ReservaMap = new HashMap<>();

    public Reserva(){

    }

    public Reserva(String cliente, String contrato, String fecha, String estado) {
        this.cliente = cliente;
        this.contrato = contrato;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaSolicitada() {
        return fechaSolicitada;
    }

    public void setFechaSolicitada(String fechaSolicitada) {
        this.fechaSolicitada = fechaSolicitada;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public void setKey(String key){
        this.key  = key;
    }

    public String getKey(){
        return this.key;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public void UpdateReserva(){
        ReservaMap.put("contrato" ,  contrato  );
        ReservaMap.put("estado" , estado );
        ReservaMap.put("fechaSolicitada" , fecha);
        ReservaMap.put("hora" ,  hora  );
        ReservaMap.put("fechaHoraCreacion" , fechaHoraCreacion);
        ReservaMap.put("estado" , estado );
        ReservaMap.put("nombreCliente" ,  nombreCliente   );
        ReservaMap.put("numeroContrato" ,  numeroContrato   );
    }

    public HashMap<String, Object> getReservaMap() {
        return ReservaMap;
    }
}
