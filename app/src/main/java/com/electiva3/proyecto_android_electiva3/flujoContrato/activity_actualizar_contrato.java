package com.electiva3.proyecto_android_electiva3.flujoContrato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_principal;
import com.electiva3.proyecto_android_electiva3.adapters.EstadoAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Contrato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class activity_actualizar_contrato extends AppCompatActivity
{
    private EditText txtNombre, txtVehiculo, txtPlan, txtDuracion, txtFechaA, txtFechaV, txtNumManto, txtCostoT;
    private Spinner spnEstado;
    private Button btnActualizar, btnCancelar;

    private String title = "Ver Contrato";
    private String id;
    private String plan;
    private String cliente;
    private String vehiculo;

    private ArrayList<String> clienteContrato = new ArrayList<>();
    private ArrayList<String> planContrato = new ArrayList<>();
    private ArrayList<String> vehiculoContrato = new ArrayList<>();
    private ArrayList<String> estadosList = new ArrayList<>();

    private Contrato contrato = new Contrato();
    private Conexion conexion = new Conexion();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contrato);

        txtNombre = findViewById(R.id.txtCliente);
        txtVehiculo = findViewById(R.id.txtVehiculo);
        txtPlan = findViewById(R.id.txtPlan);
        txtDuracion = findViewById(R.id.txtDuracion);
        txtFechaA = findViewById(R.id.txtFechaA);
        txtFechaV = findViewById(R.id.txtFechaV);
        txtNumManto = findViewById(R.id.txtNumManto);
        txtCostoT = findViewById(R.id.txtCostoT);
        spnEstado = findViewById(R.id.spnEstado);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        getSupportActionBar().setTitle(title);
        //establecer la conexion
        conexion.inicializarFirabase(this);
        //recuperar el id del objeto
        id = getIntent().getStringExtra("id");

        MostrarDatos();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext() , activity_lista_contratos.class);
                startActivity(i);
                finish();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String estado = spnEstado.getSelectedItem().toString();

                if(!estado.equals(contrato.getEstado())) {

                    contrato.setEstado(estado);
                    contrato.UpdateContrato();

                    conexion.getDatabaseReference().child("Contratos").child(id).updateChildren(contrato.getContratoMap());
                    Toast.makeText(getApplicationContext(), "Contrato "+estado, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No se realizaron Cambios", Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent( getApplicationContext() , activity_lista_contratos.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void MostrarDatos()
    {
        conexion.getDatabaseReference().child("Contratos").child(id).addValueEventListener(new ValueEventListener() {
            DataSnapshot ds;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ds = snapshot;

                if(ds.exists())
                {
                    String key = null;
                    contrato.setCostoTotal(Double.parseDouble(ds.child("costoTotal").getValue().toString()));
                    contrato.setDuracion(ds.child("duracion").getValue().toString());
                    contrato.setEstado(ds.child("estado").getValue().toString());
                    contrato.setFechaActivacion(ds.child("fechaActivacion").getValue().toString());
                    contrato.setFechaVencimiento(ds.child("fechaVencimiento").getValue().toString());
                    contrato.setNumeroContrato(Integer.parseInt(ds.child("numeroContrato").getValue().toString()));
                    contrato.setNumeroMantenimientos(Integer.parseInt(ds.child("numeroMantenimientos").getValue().toString()));


                    key = ds.child("plan").child("0").getValue().toString();
                    plan = ds.child("plan").child("1").getValue().toString();
                    planContrato.add(key);
                    planContrato.add(plan);

                    key = ds.child("cliente").child("0").getValue().toString();
                    cliente = ds.child("cliente").child("1").getValue().toString();
                    clienteContrato.add(key);
                    clienteContrato.add(cliente);

                    key = ds.child("vehiculo").child("0").getValue().toString();
                    vehiculo = ds.child("vehiculo").child("1").getValue().toString();
                    vehiculoContrato.add(key);
                    vehiculoContrato.add(vehiculo);

                    contrato.setCliente(clienteContrato);
                    contrato.setVehiculo(vehiculoContrato);
                    contrato.setPlan(planContrato);

                    txtNombre.setText(cliente);
                    txtVehiculo.setText(vehiculo);
                    txtPlan.setText(plan);
                    txtNumManto.setText(String.valueOf(contrato.getNumeroMantenimientos())+" mantenimientos");
                    txtCostoT.setText(String.valueOf(contrato.getCostoTotal()));
                    txtDuracion.setText(contrato.getDuracion());
                    txtFechaA.setText(contrato.getFechaActivacion());
                    txtFechaV.setText(contrato.getFechaVencimiento());

                    mostrarEstados();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void mostrarEstados()
    {
        conexion.getDatabaseReference().child("estados").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    estadosList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String estado = Objects.requireNonNull(ds.child("estado").getValue()).toString();

                        if(estado.equals(contrato.getEstado())) {
                            estadosList.add(estado);
                        }
                    }
                    CompararEstados();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void CompararEstados()
    {
        conexion.getDatabaseReference().child("estados").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String estado = Objects.requireNonNull(ds.child("estado").getValue()).toString();

                        if(estado.equals("Activo") || estado.equals("Inactivo")) {
                            if (!estadosList.contains(estado)) {
                                estadosList.add(estado);
                            }
                        }
                    }
                    EstadoAdapter estadoAdapter = new EstadoAdapter(getApplicationContext() , R.layout.custom_simple_spinner_item,estadosList);
                    spnEstado.setAdapter(estadoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext() , activity_lista_contratos.class);
        startActivity(intent);
        finish();
    }
}