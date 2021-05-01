package com.electiva3.proyecto_android_electiva3.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Plan
{
    private String key;
    private String tipoPlan;
    private String duracion;
    private Double costo;
    private String estado;
    private String fechaRegistro;
    private ArrayList<String> servicios;
    private HashMap<String, Object> PlanMap = new HashMap<>();

    public Plan(){
    }

    public Plan(String key, String tipoPlan, Double costo, String estado) {
        this.key = key;
        this.tipoPlan = tipoPlan;
        this.costo = costo;
        this.estado = estado;
    }

    public Plan(String key, String tipoPlan, Double costo) {
        this.key = key;
        this.tipoPlan = tipoPlan;
        this.costo = costo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public ArrayList<String> getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList<String> servicios) {
        this.servicios = servicios;
    }

    public HashMap<String, Object> getPlanMap() {
        return PlanMap;
    }

    public void setPlanMap(HashMap<String, Object> planMap) {
        PlanMap = planMap;
    }

    public void UpdatePlan()
    {
        PlanMap.put("costo", costo);
        PlanMap.put("duracion", duracion);
        PlanMap.put("estado", estado);
        PlanMap.put("servicios", servicios);
        PlanMap.put("tipoPlan", tipoPlan);
    }

    public void ClearServicios(){
        servicios.clear();
    }

    public boolean Validar(String valor){
        boolean i = false;
        if (servicios.contains(valor)) {
             i = true;
        }
        return i;
    }
}
