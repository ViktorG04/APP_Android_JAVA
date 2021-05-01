package com.electiva3.proyecto_android_electiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_lista_marca_modelo extends AppCompatActivity
{

    private ListView listaMarcas;
    private DatabaseReference databaseReference;
    private Conexion conexion;
    private ArrayList<Marca> marcas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_marca_modelo);

        listaMarcas =  findViewById(R.id.lvMarca);



    }
}
