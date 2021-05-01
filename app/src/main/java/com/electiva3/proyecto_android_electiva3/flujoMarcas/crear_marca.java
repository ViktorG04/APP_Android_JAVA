package com.electiva3.proyecto_android_electiva3.flujoMarcas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Marca;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class crear_marca extends AppCompatActivity {

    private Conexion conexion =   new Conexion();
    private Button btnCrearMarca;
    private EditText edtMarca  ,  edtDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marca);

        conexion.inicializarFirabase(this);

        edtMarca =  findViewById(R.id.edtMarca);
        edtDescripcion =  findViewById(R.id.edtDescripcion);


        btnCrearMarca =  findViewById(R.id.btnCrearMarca);



        btnCrearMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombreMarca =  edtMarca.getText().toString();
                String description =  edtDescripcion.getText().toString();


                if(nombreMarca.isEmpty()){
                    edtMarca.setError("Campo marca es requerido!");
                }else if(description.isEmpty()){
                    edtDescripcion.setError("Campo descripción es requerido!");
                }else{

                    String estado = "activo";

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String fecha =  dateFormat.format(date);

                    Marca marca =  new Marca();

                    marca.setFechaCreacion(fecha);
                    marca.setMarca(nombreMarca);
                    marca.setEstado(estado);
                    marca.setDescripcion(description);
                    String key = (UUID.randomUUID().toString());

                    conexion.getDatabaseReference().child("marcas").child(key).setValue(marca);


                    Toast.makeText(crear_marca.this, "Datos Creados exitosamente", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(getApplicationContext() , activity_lista_marcas.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext() , activity_lista_marcas.class);
        startActivity(intent);
        finish();
    }
}