package com.electiva3.proyecto_android_electiva3.flujoVehiculo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_principal;
import com.electiva3.proyecto_android_electiva3.adapters.VehiculosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Vehiculo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_lista_vehiculos extends AppCompatActivity
{

    private FloatingActionButton fabAgregarVehiculo;
    private Conexion conexion;
    private DatabaseReference databaseReference;
    private ArrayList<Vehiculo> vehiculos;
    private RecyclerView lvlista;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vehiculos);

        lvlista =  findViewById(R.id.lvlista);
        fabAgregarVehiculo =  findViewById(R.id.fabAgregarVehiculo);
        conexion =  new Conexion();
        vehiculos =  new ArrayList<>();

        getSupportActionBar().setTitle("Vehiculos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        conexion.inicializarFirabase(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("vehiculos");

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvlista.setLayoutManager(linearLayoutManager);

        //Referencia y conexion al nodo de la base de datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    vehiculos.clear();

                    for(DataSnapshot ds: snapshot.getChildren()){

                        String key = ds.getKey();
                        String marca = ds.child("marca").getValue().toString();
                        String modelo = ds.child("modelo").getValue().toString();
                        String placa =  ds.child("placa").getValue().toString();
                        String numChasis =  ds.child("numChasis").getValue().toString();
                        String anio =  ds.child("anio").getValue().toString();


                        vehiculos.add(  new Vehiculo(key  ,  placa , marca , anio , modelo )   );
                    }
                    VehiculosAdapter vehiculosAdapter =  new VehiculosAdapter(getApplicationContext() ,   vehiculos );

                    vehiculosAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String key  = vehiculos.get(lvlista.getChildAdapterPosition(v)).getKey();
                            Intent detalleVehiculo  =  new Intent(getApplicationContext() , activity_actualizar_vehiculo.class );
                            detalleVehiculo.putExtra("keyVehiculo" , key);
                            finish();
                        }
                    });

                    lvlista.setAdapter(vehiculosAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Evento para floating action button
        fabAgregarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agregarVehiculo  =  new Intent(  getApplicationContext() ,   activity_new_vehiculo.class   );
                startActivity(agregarVehiculo);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext() , activity_principal.class);
        startActivity(intent);
        finish();
    }

}
