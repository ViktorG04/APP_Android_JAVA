package com.electiva3.proyecto_android_electiva3.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Estado
{
    private int key;
    private String estado;
    private ArrayList<Estado> estadoslist = new ArrayList<>();


    public Estado() {
    }

    public Estado(int key, String estado)
    {
        super();
        this.key = key;
        this.estado = estado;
    }

    public Integer getKey() { return key;
    }

    public void setKey(Integer key) { this.key = key;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<Estado> getEstadoslist() {
        return estadoslist;
    }

    public void setEstadoslist(ArrayList<Estado> estadoslist) {
        this.estadoslist = estadoslist;
    }

    public void EstadoList(Conexion conexion)
    {
        conexion.getDatabaseReference().child("estados").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    estadoslist.clear();

                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        estado = Objects.requireNonNull(ds.child("estado").getValue()).toString();

                        if(estado.equals("Activo") || estado.equals("Inactivo"))
                        {
                            estadoslist.add(new Estado(key, estado));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
