package com.electiva3.proyecto_android_electiva3.entities;


import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Conexion
{
    FirebaseDatabase firabaseDataBase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    public FirebaseDatabase getFirabaseDataBase() {
        return firabaseDataBase;
    }

    public void setFirabaseDataBase(FirebaseDatabase firabaseDataBase) {
        this.firabaseDataBase = firabaseDataBase;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public void inicializarFirabase(Context context)
    {
        FirebaseApp.initializeApp(context);
        firabaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firabaseDataBase.getReference();
        auth = FirebaseAuth.getInstance();

    }

    public String ObtenerHora()
    {
        SimpleDateFormat dfDate_day= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dt="";
        Calendar c = Calendar.getInstance();
        dt = dfDate_day.format(c.getTime());
        return dt;
    }
}
