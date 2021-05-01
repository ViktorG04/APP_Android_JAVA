package com.electiva3.proyecto_android_electiva3.flujoMarcas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_principal;
import com.electiva3.proyecto_android_electiva3.adapters.MarcasAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_lista_marcas extends AppCompatActivity {

    private FloatingActionButton fabAgregarMarca;
    private DatabaseReference databaseReference;
    private RecyclerView rvMarcas;
    private ArrayList<Marca> marcas;
    private Conexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_marcas);

        rvMarcas =  findViewById(R.id.rvMarcas);

        conexion =  new Conexion();
        marcas =  new ArrayList<>();
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("marcas");
        fabAgregarMarca = findViewById(R.id.fabAgregarMarca);

        getSupportActionBar().setTitle("Marcas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        LinearLayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMarcas.setLayoutManager(layoutManager);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    marcas.clear();
                    for(  DataSnapshot ds:  snapshot.getChildren()  ){
                        String key    = ds.getKey();
                        String marca  = ds.child("marca").getValue().toString();
                        String estado = ds.child("estado").getValue().toString();
                        marcas.add( new Marca(key,marca,estado) );
                    }

                    MarcasAdapter  marcasAdapter =  new MarcasAdapter(  getApplicationContext() , marcas );
                    marcasAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String key =  marcas.get(rvMarcas.getChildAdapterPosition(v)).getKey();
                            Intent actualizarMarca =  new Intent( getApplicationContext() , actualizar_marca.class     );
                            actualizarMarca.putExtra("keyMarca" , key   );
                            finish();
                        }
                    });
                    rvMarcas.setAdapter(marcasAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fabAgregarMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                 
                Intent intent  =  new Intent(getApplicationContext() , crear_marca.class  );
                startActivity(intent);
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