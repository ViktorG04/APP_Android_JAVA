package com.electiva3.proyecto_android_electiva3.entities;


import java.util.HashMap;
import java.util.Map;


public class Usuario
{
    private String key;
    private String nombre;
    private String dui;
    private String nit;
    private String licencia;
    private String correo;
    private String telefono;
    private String direccion;
    private String password;
    private String rol;
    private String estado;
    private String fechaRegistro;
    Map<String, Object> usuarioMap = new HashMap<>();

    public Usuario() {
    }

    public Usuario(String key, String nombre, String correo, String estado) {
        this.key = key;
        this.nombre = nombre;
        this.correo = correo;
        this.estado = estado;
    }

    public Usuario(String key, String nombre) {
        this.key = key;
        this.nombre = nombre;
    }

    public Usuario(String nombre, String dui, String nit, String licencia, String correo, String telefono, String direccion, String rol, String estado, String fechaRegistro)
    {
        this.nombre = nombre;
        this.dui = dui;
        this.nit = nit;
        this.licencia = licencia;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public Map<String, Object> getUsuarioMap() {
        return usuarioMap;
    }

    public void setUsuarioMap(Map<String, Object> usuarioMap) {
        this.usuarioMap = usuarioMap;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRol() { return rol; }

    public void setRol(String rol) {
        this.rol = rol;
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


    public void Comparar(String nom, String du, String ni, String li, String co, String pa, String tel, String dire, String rl, String es)
    {
        if(nom.equals(nombre) && du.equals(dui) && ni.equals(nit) && li.equals(licencia) && co.equals(correo)
                && pa.equals(password) && tel.equals(telefono) && dire.equals(direccion) && rl.equals(rol) && es.equals(estado))
        {
            usuarioMap.clear();
        }
        else
        {
            usuarioMap.put("nombre", nom);
            usuarioMap.put("dui", du);
            usuarioMap.put("nit", ni);
            usuarioMap.put("licencia", li);
            usuarioMap.put("correo", co);
            usuarioMap.put("password", pa);
            usuarioMap.put("direccion", dire);
            usuarioMap.put("telefono", tel);
            usuarioMap.put("rol" , rl);
            usuarioMap.put("estado", es);
        }
    }


    public void UpdateDatos(String pass)
    {
        usuarioMap.put("nombre", nombre);
        usuarioMap.put("dui", dui);
        usuarioMap.put("nit", nit);
        usuarioMap.put("licencia", licencia);
        usuarioMap.put("correo", correo);
        usuarioMap.put("password", pass);
        usuarioMap.put("direccion", direccion);
        usuarioMap.put("telefono", telefono);
        usuarioMap.put("rol" , rol);
        usuarioMap.put("estado", estado);
    }

    public void UpdateDatos()
    {
        usuarioMap.put("nombre", nombre);
        usuarioMap.put("dui", dui);
        usuarioMap.put("nit", nit);
        usuarioMap.put("licencia", licencia);
        usuarioMap.put("correo", correo);
        usuarioMap.put("password", password);
        usuarioMap.put("direccion", direccion);
        usuarioMap.put("telefono", telefono);
        usuarioMap.put("rol" , rol);
        usuarioMap.put("estado", estado);
    }

    @Override
    public String toString() {
        return nombre;
    }
}

