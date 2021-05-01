package com.electiva3.proyecto_android_electiva3.flujoModelos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Modelo;
import com.electiva3.proyecto_android_electiva3.flujoMarcas.activity_lista_marcas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class crear_modelo extends AppCompatActivity {


    private EditText edtMarca , edtModelo , edtDescripcion ;
    private Button btnCrearModelo;
    private ArrayAdapter<String> estadosAdapter;
    private String[] estados =  { "activo" , "inactivo" } ;
    private Modelo modelo;
    private Conexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_modelo);

        //Key de la marca a asociar
        String keyMarca =  getIntent().getStringExtra("keyMarca");

        modelo = new Modelo();
        conexion =  new Conexion();

        modelo.setKeyMarca(keyMarca);
        conexion.inicializarFirabase(this);


        edtMarca =  findViewById(R.id.edtMarca);
        edtModelo =  findViewById(R.id.edtModelo);
        edtDescripcion =  findViewById(R.id.edtDescripcion);
        btnCrearModelo =  findViewById(R.id.btnCrearModelo);


        getDatosMarca(keyMarca);

        btnCrearModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombreModelo  =  edtModelo.getText().toString();
                String descripcion   =  edtDescripcion.getText().toString();

                if(nombreModelo.isEmpty()){
                    edtModelo.setError("El nombre del modelo es requerido");
                }else if(descripcion.isEmpty()){
                    edtDescripcion.setError("Descripcion requerida");
                }else{
                    String key = (UUID.randomUUID().toString());
                    modelo.setModelo(nombreModelo);
                    modelo.setDescripcion(descripcion);
                    modelo.setEstado( estados[0]);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String fecha =  dateFormat.format(date);

                    modelo.setFechaCreacion(fecha);
                    conexion.getDatabaseReference().child("modelos").child(key).setValue(modelo);


                    Toast.makeText(crear_modelo.this, "Datos ingresados exitosamente", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(getApplicationContext() , activity_lista_marcas.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void getDatosMarca(String keyMarca){
        final DatabaseReference marcas =     FirebaseDatabase.getInstance().getReference().child("marcas").child(keyMarca);
        marcas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot ds = snapshot;
                if (ds.exists()) {
                    edtMarca.setText(ds.child("marca").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}