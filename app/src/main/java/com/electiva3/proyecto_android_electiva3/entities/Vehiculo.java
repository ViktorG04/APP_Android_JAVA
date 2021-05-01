package com.electiva3.proyecto_android_electiva3.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;


@IgnoreExtraProperties
public class Vehiculo
{
    private String key;
    private String anio;
    private String color;
    private String fechaRegistro;
    private String marca;
    private String modelo;
    private String numChasis;
    private String placa;
    private String estado;
    private String keyModelo;
    private String keyMarca;
    private HashMap<String  , Object>  vehiculoMap = new HashMap<>();


    public Vehiculo(){

    }

    public Vehiculo( String key , String placa , String  marca ){
        this.key = key;
        this.marca =  marca ;
        this.placa =   placa;
    }

    public Vehiculo( String key , String placa , String  marca ,String anio , String modelo ){
        this.key = key;
        this.marca =  marca ;
        this.placa =   placa;
        this.anio =  anio;
        this.modelo =  modelo;
    }

    public void setKey(String key){
        this.key =  key;
    }

    public String getKey(){
        return key;
    }

    public String getAnio(){
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumChasis() {
        return numChasis;
    }

    public void setNumChasis(String numChasis) {
        this.numChasis = numChasis;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void  setKeyMarca(String keyMarca){
        this.keyMarca =  keyMarca;
    }

    public String getKeyMarca(){
        return keyMarca;
    }

    public void setKeyModelo(String keyModelo){
        this.keyModelo = keyModelo;
    }

    public String getKeyModelo(){
        return keyModelo;
    }

    public HashMap<String, Object> getVehiculoMap() {
        return vehiculoMap;
    }

    public void setVehiculoMap(HashMap<String, Object> vehiculoMap) {
        this.vehiculoMap = vehiculoMap;
    }

    public void UpdateVehiculo(){
        vehiculoMap.put("anio" , anio);
        vehiculoMap.put("modelo", modelo);
        vehiculoMap.put("keyModelo" , keyModelo);
        vehiculoMap.put("keyMarca" , keyMarca);
        vehiculoMap.put("color" , color);
        vehiculoMap.put("fechaRegistro" , fechaRegistro);
        vehiculoMap.put("marca" , marca);
        vehiculoMap.put("placa" , placa);
        vehiculoMap.put("numChasis", numChasis);
        vehiculoMap.put("estado" , estado);
        vehiculoMap.put("fechaRegistro" , fechaRegistro);
    }

}
