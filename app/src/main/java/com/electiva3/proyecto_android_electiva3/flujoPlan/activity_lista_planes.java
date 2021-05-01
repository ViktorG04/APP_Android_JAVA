package com.electiva3.proyecto_android_electiva3.flujoPlan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_principal;
import com.electiva3.proyecto_android_electiva3.adapters.PlanAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.ServicioListAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Plan;
import com.electiva3.proyecto_android_electiva3.entities.Servicio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class activity_lista_planes extends AppCompatActivity implements View.OnClickListener
{
    private RecyclerView rvPlanes;
    private FloatingActionButton faAgregarPlan;
    private ListView lvServicios;
    private ConstraintLayout vt2, vt3;
    private Spinner spnTipoPlan,  spnDuracion;
    private TextView txtCosto, tvcostoSeleccionado;
    private Button btnCrear, btnCancelar, btnServicios, btnSeleccionar, btnCancelar2;

    private String title = "Lista de Planes";
    private ArrayList<Plan> planList = new ArrayList<>();
    private ArrayList<Servicio> serviciosList = new ArrayList<>();
    private ArrayList<String> tiposList = new ArrayList<>();
    private ArrayList<String> tiemposList = new ArrayList<>();
    private ArrayList<String> serSelecList = new ArrayList<>();
    private Double costoTotal = 0.00;

    Conexion conexion = new Conexion();
    Plan plan = new Plan();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planes);

        rvPlanes = findViewById(R.id.rvPlanes);
        faAgregarPlan = findViewById(R.id.fabAgregarPlan);

        getSupportActionBar().setTitle(title);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPlanes.setLayoutManager(linearLayoutManager);

        conexion.inicializarFirabase(this);

        listarPlanes();

        //variables de crear plan
        vt2 = findViewById(R.id.vt2);
        btnCrear = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnServicios = findViewById(R.id.btnServicios);
        spnTipoPlan = findViewById(R.id.spnTipoPlan);
        spnDuracion = findViewById(R.id.spnDuracion);
        txtCosto = findViewById(R.id.tvCosto);

        //variables seleccionar servicios
        vt3 = findViewById(R.id.vt3);
        lvServicios = findViewById(R.id.lvServicios);
        btnSeleccionar = findViewById(R.id.btnSeleccionar);
        btnCancelar2 = findViewById(R.id.btnCancelar2);
        tvcostoSeleccionado = findViewById(R.id.tvCostoSeleccionado);

        //botones
        faAgregarPlan.setOnClickListener(this);
        btnServicios.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnCancelar2.setOnClickListener(this);
        btnSeleccionar.setOnClickListener(this);

       lvServicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idServicio = String.valueOf(serviciosList.get(position).getKey());
                String costoServicio = String.valueOf(serviciosList.get(position).getCosto());

                boolean exist = serSelecList.contains(idServicio);
                if(exist) {
                    lvServicios.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                    costoTotal = costoTotal - Double.parseDouble(costoServicio);
                    serSelecList.remove(idServicio);
                }
                else {
                    lvServicios.getChildAt(position).setBackgroundColor(Color.BLUE);
                    costoTotal = costoTotal + Double.parseDouble(costoServicio);
                    serSelecList.add(idServicio);
                }
                tvcostoSeleccionado.setText(String.valueOf(costoTotal));
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String tipo = spnTipoPlan.getSelectedItem().toString();
                String duracion = spnDuracion.getSelectedItem().toString();

                if (tipo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "seleccione un tipo Plan", Toast.LENGTH_LONG).show();
                }
                else if (duracion.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "seleccione el tiempo de vigencia", Toast.LENGTH_LONG).show();
                }
                else if(serSelecList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No hay servicios seleccionados", Toast.LENGTH_LONG).show();
                }
                else if(costoTotal == 0.0)
                {
                    txtCosto.setError("No hay un costo para este Plan");
                }
                else {
                    String key = (UUID.randomUUID().toString());
                    plan.setTipoPlan(tipo);
                    plan.setDuracion(duracion);
                    plan.setCosto(costoTotal);
                    plan.setEstado("Activo");
                    plan.setServicios(serSelecList);
                    plan.setFechaRegistro(conexion.ObtenerHora());

                    conexion.getDatabaseReference().child("planes").child(key).setValue(plan);

                    Toast.makeText(getApplicationContext(), "Plan registrado!!", Toast.LENGTH_SHORT).show();

                    vt2.setVisibility(View.INVISIBLE);
                    faAgregarPlan.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //acciones de botones
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fabAgregarPlan:
                vt2.setVisibility(View.VISIBLE);
                faAgregarPlan.setVisibility(View.INVISIBLE);
                tiposList.clear();
                tiemposList.clear();
                serviciosList.clear();
                costoTotal = 0.0;
                tvcostoSeleccionado.setText("");
                txtCosto.setText("");
                lvServicios.clearChoices();
                tipoPlanes();
                tiemposSelecionar();
                break;
            case R.id.btnServicios:
                vt3.setVisibility(View.VISIBLE);
                listarServicios();

                break;
            case R.id.btnCancelar:

                vt2.setVisibility(View.INVISIBLE);
                faAgregarPlan.setVisibility(View.VISIBLE);
                txtCosto.setText("");
                costoTotal = 0.0;
                serSelecList.clear();
                lvServicios.clearChoices();
                tvcostoSeleccionado.setText("");
                break;

            case R.id.btnCancelar2:
                vt3.setVisibility(View.INVISIBLE);
                lvServicios.clearChoices();
                tvcostoSeleccionado.setText("");
                serSelecList.clear();
                costoTotal = 0.00;
                break;

            case R.id.btnSeleccionar:
                vt3.setVisibility(View.INVISIBLE);
                txtCosto.setText(String.valueOf(costoTotal));
                break;
        }
    }

    public void listarPlanes()
    {
        conexion.getDatabaseReference().child("planes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    planList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        String tipo = ds.child("tipoPlan").getValue().toString()+"  "+ds.child("duracion").getValue().toString();
                        Double costo = Double.parseDouble(ds.child("costo").getValue().toString());
                        String estado = ds.child("estado").getValue().toString();
                        planList.add(new Plan(key, tipo, costo, estado));

                    }
                    PlanAdapter planAdapter = new PlanAdapter(getApplicationContext(), planList);
                    planAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            serviciosList.clear();
                            String idPlan =  planList.get(rvPlanes.getChildAdapterPosition(v)).getKey();
                            Intent intent  =  new Intent(getApplicationContext(),   activity_actualizar_plan.class  );
                            intent.putExtra("id", idPlan);
                            intent.putExtra("llamado", "plan");
                            startActivity(intent);
                            finish();
                        }
                    });
                    rvPlanes.setAdapter(planAdapter);
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
                        tiposList.add(tipoPlan);
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

    public void tiemposSelecionar()
    {
        conexion.getDatabaseReference().child("duracionActividad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for(DataSnapshot ds: snapshot.getChildren()) {

                        String tiempo = ds.child("tiempo").getValue().toString();
                        tiemposList.add(tiempo);
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

    public void listarServicios()
    {
        conexion.getDatabaseReference().child("ArticulosServicios").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    serviciosList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String estado = ds.child("estado").getValue().toString();

                        if(estado.equals("Activo"))
                        {
                            String key = ds.getKey();
                            String descripcion = ds.child("descripcion").getValue().toString();
                            double costo = Double.parseDouble(ds.child("costo").getValue().toString());
                            serviciosList.add(new Servicio(key, descripcion, costo));
                        }
                    }

                    ServicioListAdapter servicioAdapter = new ServicioListAdapter(getApplicationContext() ,R.layout.item_servicio, serviciosList );
                    lvServicios.setAdapter(servicioAdapter);
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


































