package com.electiva3.proyecto_android_electiva3.entities;

import java.util.HashMap;

public class Marca {

    private String key;
    private String marca;
    private String estado;
    private String fechaCreacion;
    private String descripcion;
    private HashMap<String , Object> marcaMap  =  new HashMap<>();

    public Marca(){

    }

    public Marca(String key , String marca ,  String estado){
        this.key =  key;
        this.marca =  marca;
        this.estado =  estado;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setEstado(String estado){
        this.estado =  estado;
    }

    public String getEstado(){
        return estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion =  descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public HashMap<String, Object> getMarcaMap(){
        return marcaMap;
    }

    public void UpdateMarca(){
        marcaMap.put("marca" , marca );
        marcaMap.put("estado" , estado);
        marcaMap.put("fechaCreacion" , fechaCreacion);
        marcaMap.put("descripcion" , descripcion);
    }

    @Override
    public String toString() {
        return marca;
    }
}
