package com.electiva3.proyecto_android_electiva3.entities;

import java.util.HashMap;

public class Modelo {


    private String key;
    private String modelo;
    private String estado;
    private String descripcion;
    private String keyMarca;
    private String fechaCreacion;
    private HashMap<String , Object> modeloMap =  new HashMap<>();

    public Modelo(){

    }

    public  Modelo(String key , String modelo , String estado , String keyMarca){
        this.key =  key;
        this.modelo =  modelo;
        this.estado = estado;
        this.keyMarca =  keyMarca;
    }

    public void setKey(String key){
        this.key =  key;
    }

    public String getKey(){
        return key;
    }

    public void setModelo(String modelo){
        this.modelo =  modelo;
    }

    public String getModelo(){
        return modelo;
    }

    public void setEstado(String estado){
        this.estado =  estado;
    }

    public String getEstado(){
        return estado;
    }

    public void setKeyMarca(String keyMarca){
        this.keyMarca =  keyMarca;
    }

    public String getKeyMarca(){
        return keyMarca;
    }

    public void setDescripcion(String descripcion){
        this.descripcion =  descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setFechaCreacion(String fechaCreacion){
        this.fechaCreacion =  fechaCreacion;
    }

    public  String getFechaCreacion(){
        return fechaCreacion;
    }

    public HashMap<String, Object> getModeloMap() {
        return modeloMap;
    }

    public void setModeloMap(HashMap<String, Object> modeloMap) {
        this.modeloMap = modeloMap;
    }


    public  void updateModelo(){
        modeloMap.put("keyMarca" , keyMarca   );
        modeloMap.put("descripcion" , descripcion);
        modeloMap.put("fechaCreacion" , fechaCreacion);
        modeloMap.put("estado" , estado);
        modeloMap.put("modelo" ,  modelo);
    }

    public String toString(){
        return modelo;
    }
}
