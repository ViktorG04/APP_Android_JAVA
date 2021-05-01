package com.electiva3.proyecto_android_electiva3.flujoPlan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.EstadoAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Plan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class activity_actualizar_plan extends AppCompatActivity
{
    private Spinner spnTipoPlan,  spnDuracion, spnEstado;
    private TextView txtCosto;
    private Button btnCancelar, btnActualizar;
    private ListView lvServicios;

    private ArrayList<String> serviciosList = new ArrayList<>();
    private ArrayList<String> serviciosList2 = new ArrayList<>();
    private ArrayList<String> estadosList = new ArrayList<>();
    private ArrayList<String> tiposList = new ArrayList<>();
    private ArrayList<String> tiemposList = new ArrayList<>();
    private String id;
    private String llamado;
    private String title = "Ver Plan";

    Conexion conexion = new Conexion();
    Plan plan = new Plan();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_plan);

        txtCosto = findViewById(R.id.tvCosto);
        lvServicios = findViewById(R.id.lvServicios);
        btnActualizar = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelar);
        spnTipoPlan = findViewById(R.id.spnTipoPlan);
        spnDuracion = findViewById(R.id.spnDuracion);
        spnEstado = findViewById(R.id.spnEstado);

        getSupportActionBar().setTitle(title);
        //establecer la conexion
        conexion.inicializarFirabase(this);
        //recuperar el id del objeto
        id = getIntent().getStringExtra("id");
        llamado = getIntent().getStringExtra("llamado");

        MostrarDatos();

     /*   if(llamado.equals("contrato"))
        {
            btnActualizar.setVisibility(View.INVISIBLE);
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext() , activity_new_contrato.class);
                    startActivity(i);
                    finish();
                }
            });
        }else
        {

        }*/

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                serviciosList.clear();
                plan.ClearServicios();
                plan.setEstado("");
                lvServicios.clearChoices();
                Intent i = new Intent( getApplicationContext() , activity_lista_planes.class);
                startActivity(i);
                finish();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String estado = spnEstado.getSelectedItem().toString();

                if(!estado.equals(plan.getEstado())) {
                    plan.setEstado(estado);
                    plan.UpdatePlan();
                    conexion.getDatabaseReference().child("planes").child(id).updateChildren(plan.getPlanMap());
                    Toast.makeText(getApplicationContext(), "Plan "+estado, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No se realizaron Cambios", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent( getApplicationContext() , activity_lista_planes.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void MostrarDatos(){

        spnTipoPlan.setEnabled(false);
        spnDuracion.setEnabled(false);
        serviciosList.clear();
        conexion.getDatabaseReference().child("planes").child(id).addValueEventListener(new ValueEventListener() {
            DataSnapshot ds;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ds = snapshot;
                if (ds.exists())
                {
                    plan.setTipoPlan(ds.child("tipoPlan").getValue().toString());
                    plan.setEstado(ds.child("estado").getValue().toString());
                    plan.setDuracion(ds.child("duracion").getValue().toString());
                    plan.setCosto(Double.parseDouble(ds.child("costo").getValue().toString()));

                   for(DataSnapshot sp: ds.child("servicios").getChildren()){
                        String i = sp.getKey();
                        String servicio = ds.child("servicios").child(i).getValue().toString();
                       serviciosList.add(servicio);
                    }

                  if(serviciosList.isEmpty()) {
                      Toast.makeText(getApplicationContext(), "vacio", Toast.LENGTH_SHORT).show();
                  }
                  else {
                      plan.setServicios(serviciosList);

                      tipoPlanes();
                      tiemposSelecionar();
                      txtCosto.setText(String.valueOf(plan.getCosto()));
                      mostrarEstados();
                      ServiciosSeleccionados();
                  }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void ServiciosSeleccionados()
    {
        conexion.getDatabaseReference().child("ArticulosServicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        serviciosList2.clear();
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String key = ds.getKey();
                        boolean i = plan.Validar(key);
                        if(i != false){
                            String servicio = ds.child("descripcion").getValue().toString()+"  $"+ds.child("costo").getValue().toString();
                            serviciosList2.add(servicio);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, serviciosList2);
                        lvServicios.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void tiemposSelecionar()
    {
        conexion.getDatabaseReference().child("duracionActividad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for(DataSnapshot ds: snapshot.getChildren()) {

                        String tiempo = ds.child("tiempo").getValue().toString();
                        if(tiempo.equals(plan.getDuracion())) {
                            tiemposList.add(tiempo);
                        }
                    }
                    ArrayAdapter<String> tiemposAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, tiemposList);
                    spnDuracion.setAdapter(tiemposAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void tipoPlanes()
    {
        conexion.getDatabaseReference().child("tiposPlanes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for(DataSnapshot ds: snapshot.getChildren()) {

                        String tipoPlan = ds.child("tipo").getValue().toString();
                        if(tipoPlan.equals(plan.getTipoPlan())) {
                            tiposList.add(tipoPlan);
                        }

                    }
                    ArrayAdapter<String> tiposAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, tiposList);
                    spnTipoPlan.setAdapter(tiposAdapter);
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

                if (snapshot.exists())
                {
                    estadosList.clear();
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String estado = Objects.requireNonNull(ds.child("estado").getValue()).toString();
                        if(estado.equals(plan.getEstado())) {
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

                if (snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
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
}
