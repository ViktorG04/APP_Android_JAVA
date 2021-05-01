package com.electiva3.proyecto_android_electiva3.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserLogin {
    private Conexion conexion;
    private Query query;


    public UserLogin() {
    }

    public void ConexionFirebase(@NonNull Conexion conexion) {
        this.conexion = conexion;
    }

    public void QueryFirebase(@NonNull Query q) {
        this.query = q;
    }

    public void UpdateUsuario(final String password) {

        final Usuario usuario = new Usuario();

        query.addValueEventListener(new ValueEventListener() {
            DataSnapshot ds;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ds = snapshot;
                if(ds.exists()){
                    usuario.setNombre(ds.child("nombre").getValue().toString());
                    usuario.setDui(ds.child("dui").getValue().toString());
                    usuario.setNit(ds.child("nit").getValue().toString());
                    usuario.setLicencia(ds.child("licencia").getValue().toString());
                    usuario.setCorreo(ds.child("correo").getValue().toString());
                    usuario.setDireccion(ds.child("direccion").getValue().toString());
                    usuario.setPassword(password);
                    usuario.setTelefono(ds.child("telefono").getValue().toString());
                    usuario.setEstado(ds.child("estado").getValue().toString());
                    usuario.setRol(ds.child("rol").getValue().toString());

                    //actualizar el password del usuario si lo ha modificado por correo
                    usuario.UpdateDatos();
                    conexion.getDatabaseReference().child("usuarios").child(ds.getKey()).updateChildren(usuario.getUsuarioMap());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
