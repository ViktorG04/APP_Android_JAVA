package com.electiva3.proyecto_android_electiva3.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contrato
{
    private String key;
    private int numeroContrato;
    private ArrayList<String> cliente;
    private ArrayList<String> vehiculo;
    private ArrayList<String> plan;
    private String tipoPlan;
    private String duracion;
    private String fechaActivacion;
    private String fechaVencimiento;
    private int numeroMantenimientos;
    private String estado;
    private double CostoTotal;
    private String fechaCreacion;
    private HashMap<String, Object> ContratoMap = new HashMap<>();

    public Contrato() {
    }

    public Contrato (String key, int numeroContrato, String tipoPlan, double CostoTotal, String estado)
    {
        this.key = key;
        this.numeroContrato = numeroContrato;
        this.tipoPlan = tipoPlan;
        this.CostoTotal = CostoTotal;
        this.estado = estado;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(int numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public ArrayList<String> getCliente() {
        return cliente;
    }

    public void setCliente(ArrayList<String> cliente) {
        this.cliente = cliente;
    }

    public ArrayList<String> getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(ArrayList<String> vehiculo) {
        this.vehiculo = vehiculo;
    }

    public ArrayList<String> getPlan() {
        return plan;
    }

    public void setPlan(ArrayList<String> plan) {
        this.plan = plan;
    }

    public String getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(String fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getNumeroMantenimientos() {
        return numeroMantenimientos;
    }

    public void setNumeroMantenimientos(int numeroMantenimientos) {
        this.numeroMantenimientos = numeroMantenimientos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCostoTotal() {
        return CostoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        CostoTotal = costoTotal;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public HashMap<String, Object> getContratoMap() {
        return ContratoMap;
    }

    public void setContratoMap(HashMap<String, Object> contratoMap) {
        ContratoMap = contratoMap;
    }

    public void UpdateContrato() {
        ContratoMap.put("cliente", cliente);
        ContratoMap.put("costoTotal", CostoTotal);
        ContratoMap.put("duracion", duracion);
        ContratoMap.put("estado", estado);
        ContratoMap.put("fechaActivacion", fechaActivacion);
        ContratoMap.put("fechaVencimiento", fechaVencimiento);
        ContratoMap.put("numeroContrato", numeroContrato);
        ContratoMap.put("numeroMantenimientos", numeroMantenimientos);
        ContratoMap.put("plan", plan);
        ContratoMap.put("vehiculo", vehiculo);
    }


    @Override
    public String toString() {
        return plan.get(1)+"-"+vehiculo.get(1);
    }
}
