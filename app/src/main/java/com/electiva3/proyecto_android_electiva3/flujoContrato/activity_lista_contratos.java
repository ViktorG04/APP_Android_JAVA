package com.electiva3.proyecto_android_electiva3.flujoContrato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_principal;
import com.electiva3.proyecto_android_electiva3.adapters.ContratoAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Contrato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_lista_contratos extends AppCompatActivity
{

    private RecyclerView rvContrato;
    private FloatingActionButton fabAgregarConcontrato;
    private ArrayList<Contrato> contratoList = new ArrayList<>();
    private String title = "Lista Contratos";
    int i = 0;
    Conexion conexion = new Conexion();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contratos);

        rvContrato = findViewById(R.id.rvContrato);
        fabAgregarConcontrato = findViewById(R.id.fabAgregarContrato);

        getSupportActionBar().setTitle(title);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvContrato.setLayoutManager(linearLayoutManager);

        conexion.inicializarFirabase(this);

        listarContratos();


        fabAgregarConcontrato.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getApplicationContext() , activity_new_contrato.class);
                intent.putExtra("conteo", i);
                startActivity(intent);
                finish();
            }
        });
    }

    public void listarContratos()
    {
        conexion.getDatabaseReference().child("Contratos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    contratoList.clear();

                    for(DataSnapshot ds : snapshot.getChildren()) {
                        i +=1;
                        String key = ds.getKey();

                        int numContrato = Integer.parseInt(ds.child("numeroContrato").getValue().toString());
                        String estado = ds.child("estado").getValue().toString();
                        double costo = Double.parseDouble(ds.child("costoTotal").getValue().toString());
                        for(DataSnapshot sp: ds.child("plan").getChildren()) {
                            String i = sp.getKey();

                            if(i.equals("1")) {
                                String tipo = ds.child("plan").child(i).getValue().toString();
                                contratoList.add(new Contrato(key, numContrato, tipo, costo,estado));
                            }
                        }
                    }
                    ContratoAdapter contratoAdapter = new ContratoAdapter(getApplicationContext(), contratoList);

                    contratoAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id =  contratoList.get(rvContrato.getChildAdapterPosition(v)).getKey();
                            Intent intent  =  new Intent(getApplicationContext(),   activity_actualizar_contrato.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            finish();
                        }
                    });
                    rvContrato.setAdapter(contratoAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
