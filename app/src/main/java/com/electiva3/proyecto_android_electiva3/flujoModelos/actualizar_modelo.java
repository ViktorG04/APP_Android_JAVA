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

public class actualizar_modelo extends AppCompatActivity {

    private Modelo modelo;
    private Conexion conexion;
    private EditText edtModelo ,  edtMarca ,  edtDescripcion , edtFechaCreacion;
    private Spinner spnEstado;
    private Button btnActualizarrModelo;
    private String[] estados = {  "activo" , "inactivo" };
    private ArrayAdapter adapterEstados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_modelo);

        conexion  = new Conexion();
        modelo    =  new Modelo();

        conexion.inicializarFirabase(this);
        String keyModelo =  getIntent().getStringExtra("keyModelo");

        modelo.setKey(keyModelo);


        edtModelo =  findViewById(R.id.edtModelo);
        edtMarca  =  findViewById(R.id.edtMarca);
        edtDescripcion =  findViewById(R.id.edtDescripcion);
        spnEstado = findViewById(R.id.spnEstado);
        btnActualizarrModelo =  findViewById(R.id.btnActualizarrModelo);
        edtFechaCreacion =  findViewById(R.id.edtFechaCreacion);




        adapterEstados = new ArrayAdapter( getApplicationContext()   , R.layout.custom_simple_spinner_item , estados     );
        spnEstado.setAdapter(adapterEstados);

        btnActualizarrModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarModelo();
            }
        });

        getDatosModelo(keyModelo);
    }


    public void actualizarModelo(){

        String nombreModelo =  edtModelo.getText().toString();
        String descripcion =  edtDescripcion.getText().toString();
        String estado = estados[spnEstado.getSelectedItemPosition()];


        if(  nombreModelo.isEmpty()  ){
            edtModelo.setError("Campo requerido!");
        }else if(  descripcion.isEmpty()  ){
            edtDescripcion.setError("Campo requerido");
        }else{
            modelo.setModelo( nombreModelo);
            modelo.setDescripcion(descripcion);
            modelo.setEstado(estado);
            modelo.updateModelo();
            conexion.getDatabaseReference().child("modelos").child(modelo.getKey()).updateChildren(modelo.getModeloMap());

            Toast.makeText(actualizar_modelo.this, "Datos actualizados exitosamente", Toast.LENGTH_SHORT).show();

            Intent intent= new Intent(getApplicationContext() , activity_lista_marcas.class);
            startActivity(intent);
            finish();
        }
    }


    public void getDatosModelo(String keyModelo){

        final DatabaseReference modeloReference =     FirebaseDatabase.getInstance().getReference().child("modelos").child(keyModelo);

        modeloReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot ds = snapshot;
                if(ds.exists()){

                    modelo.setKey( ds.getKey()   );
                    modelo.setModelo(  ds.child("modelo").getValue().toString());
                    modelo.setDescripcion(ds.child("descripcion").getValue().toString());
                    modelo.setEstado( ds.child("estado").getValue().toString());
                    modelo.setFechaCreacion(ds.child("fechaCreacion").getValue().toString());
                    modelo.setKeyMarca( ds.child("keyMarca").getValue().toString()  );


                    edtModelo.setText( modelo.getModelo());
                    edtDescripcion.setText(  modelo.getDescripcion() );
                    int spinnerPosition = adapterEstados.getPosition(modelo.getEstado());
                    spnEstado.setSelection(spinnerPosition);
                    edtFechaCreacion.setText(  modelo.getFechaCreacion());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}