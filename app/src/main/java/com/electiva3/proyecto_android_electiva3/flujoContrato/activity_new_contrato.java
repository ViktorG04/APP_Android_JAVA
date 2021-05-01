package com.electiva3.proyecto_android_electiva3.flujoContrato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.PlanListAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.UsuarioListAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.VehiculoListAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Contrato;
import com.electiva3.proyecto_android_electiva3.entities.Plan;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.electiva3.proyecto_android_electiva3.entities.Vehiculo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class activity_new_contrato extends AppCompatActivity implements View.OnClickListener
{
    private TextView txtCliente, txtVehiculo, txtPlan, txtFechaVen, txtNumManto, txtCosto, txtFechaA;
    private Spinner spnDuracionC;
    private CalendarView cvfechaActivacion;
    private ConstraintLayout vt2, vt3;
    private ListView lvlistar;
    private Button btnCrear, btnSeleccionar, btnCancelar, btnCancelar2;

    private ArrayList<Usuario>  usuarioList = new ArrayList<>();
    private ArrayList<Vehiculo> vehiculoList = new ArrayList<>();
    private ArrayList<Plan> planList = new ArrayList<>();
    private ArrayList<String> tiemposList = new ArrayList<>();

    private double costoPlan = 0.00;
    private double costo;
    private int numero;
    private int i;
    private int conteo;
    private String fecha;
    private String duracion;
    private String title = "Crer Nuevo Contrato";

    Conexion conexion = new Conexion();
    Contrato contrato = new Contrato();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contrato);

        getSupportActionBar().setTitle(title);

        txtCliente = findViewById(R.id.txtCliente);
        txtPlan = findViewById(R.id.txtPlan);
        txtFechaA = findViewById(R.id.txtFechaA);
        cvfechaActivacion = findViewById(R.id.cvfechaActivacion);
        txtFechaVen = findViewById(R.id.txtFechaVen);
        txtNumManto = findViewById(R.id.txtNumManto);
        txtCosto = findViewById(R.id.txtCosto);
        txtVehiculo = findViewById(R.id.txtVehiculo);
        spnDuracionC = findViewById(R.id.spnDuracionC);
        btnCrear = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSeleccionar = findViewById(R.id.btnSeleccionar);
        btnCancelar2 = findViewById(R.id.btnCancelar2);
        vt2 = findViewById(R.id.vt2);
        vt3 = findViewById(R.id.vt3);
        lvlistar = findViewById(R.id.lvlistar);

        //trae el numero de contratos listados
        conteo = getIntent().getIntExtra("conteo", i);

        //conexion firebase
        conexion.inicializarFirabase(this);

        txtCliente.setOnClickListener(this);
        txtVehiculo.setOnClickListener(this);
        txtPlan.setOnClickListener(this);
        txtFechaA.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnCancelar2.setOnClickListener(this);

        tiemposSelecionar();



        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cliente = txtCliente.getText().toString();
                String vehiculo = txtVehiculo.getText().toString();
                String plan = txtPlan.getText().toString();
                duracion = spnDuracionC.getSelectedItem().toString();
                String numMantos = txtNumManto.getText().toString();
                String dato = txtCosto.getText().toString();

                if(cliente.isEmpty()) {
                    txtCliente.setError("Seleccione un cliente");
                }
                else if (vehiculo.isEmpty()) {
                    txtVehiculo.setError("Seleccione un vehiculo");
                }
                else if (plan.isEmpty()) {
                    txtPlan.setError("Seleccione un Plan");
                }
                else if(duracion.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "seleccione una duracion del contrato", Toast.LENGTH_LONG).show();
                }
                else if(numMantos.isEmpty()) {
                    txtNumManto.setError("no hay numero mantenimientos");
                }
                else if(dato.isEmpty()) {
                    txtCosto.setError("No hay un costo Total");
                }
                else
                {
                    String key = (UUID.randomUUID().toString());
                    contrato.setNumeroContrato(conteo+1);
                    contrato.setFechaCreacion(conexion.ObtenerHora());
                    contrato.setEstado("Activo");
                    conexion.getDatabaseReference().child("Contratos").child(key).setValue(contrato);

                    Intent contratos = new Intent(getApplicationContext() ,   activity_lista_contratos.class);
                    startActivity(contratos);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.txtCliente:
                Seleccion();
                listarUsuarios();
                final ArrayList<String> listusuario = new ArrayList<>();
                lvlistar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        txtCliente.setText(usuarioList.get(position).getNombre());

                        listusuario.add(usuarioList.get(position).getKey());
                        listusuario.add(usuarioList.get(position).getNombre());
                        contrato.setCliente(listusuario);
                        vt2.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.txtVehiculo:
                Seleccion();
                listarVehiculos();
                final ArrayList<String> listVehiculos = new ArrayList<>();
                lvlistar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        txtVehiculo.setText(vehiculoList.get(position).getPlaca());

                        listVehiculos.add(vehiculoList.get(position).getKey());
                        listVehiculos.add(vehiculoList.get(position).getPlaca());
                        contrato.setVehiculo(listVehiculos);
                        vt2.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.txtPlan:

                Seleccion();
                listarPlanes();
                final ArrayList<String> listplan = new ArrayList<>();
                lvlistar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        costoPlan = planList.get(position).getCosto();
                        listplan.add(planList.get(position).getKey());
                        listplan.add(planList.get(position).getTipoPlan());
                        contrato.setPlan(listplan);
                        vt2.setVisibility(View.INVISIBLE);

                        costo = costoPlan * 12;
                        txtPlan.setText(planList.get(position).getTipoPlan());
                        txtCosto.setText(String.valueOf(costo));
                        numero = 4;
                        txtNumManto.setText(numero +" mantenimientos");

                        contrato.setNumeroMantenimientos(numero);
                        contrato.setCostoTotal(costo);
                    }
                });
                break;
            case R.id.txtFechaA:
                vt3.setVisibility(View.VISIBLE);
                cvfechaActivacion.setVisibility(View.VISIBLE);
                cvfechaActivacion.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Calendar calendar =  Calendar.getInstance();
                        Calendar calendar1  = Calendar.getInstance();

                        calendar.set(Calendar.YEAR ,  year);
                        calendar.set(Calendar.MONTH , month);
                        calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        fecha  = simpleDateFormat.format(calendar.getTime());  //Utilizara como fecha

                        long hoy = calendar1.getTimeInMillis();
                        long seleccion  = calendar.getTimeInMillis();

                        if(seleccion< hoy){
                            Toast.makeText(getApplicationContext(), "La fecha seleccionada no puede ser menor a la actual", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            txtFechaA.setText(fecha);
                            vt3.setVisibility(View.INVISIBLE);
                            contrato.setFechaActivacion(fecha);

                            duracion = spnDuracionC.getSelectedItem().toString();
                            if(duracion.equals("24 meses")){
                                calendar.set(Calendar.YEAR ,  year+2);
                            }
                            else if(duracion.equals("18 meses")){
                                calendar.set(Calendar.MONTH , month+18);
                            }
                            else{
                                calendar.set(Calendar.YEAR ,  year+1);
                            }
                            fecha  = simpleDateFormat.format(calendar.getTime());
                            txtFechaVen.setText(fecha);
                            contrato.setFechaVencimiento(fecha);
                            contrato.setDuracion(duracion);
                        }
                    }
                });
                break;
            case R.id.btnCancelar:

                Intent contratos = new Intent(getApplicationContext() ,   activity_lista_contratos.class);
                startActivity(contratos);
                finish();
                break;
            case R.id.btnCancelar2:
                vt2.setVisibility(View.INVISIBLE);
                cvfechaActivacion.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void listarUsuarios()
    {
        conexion.getDatabaseReference().child("usuarios").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    usuarioList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String rol = ds.child("rol").getValue().toString();

                        if(rol.equals("Cliente")) {
                            String key = ds.getKey();
                            String nombre = ds.child("nombre").getValue().toString();
                            usuarioList.add(new Usuario(key, nombre));
                        }
                    }
                    UsuarioListAdapter usuarioAdapter = new UsuarioListAdapter(getApplicationContext(), R.layout.item_servicio, usuarioList);
                    lvlistar.setAdapter(usuarioAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void listarVehiculos()
    {
        conexion.getDatabaseReference().child("vehiculos").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    vehiculoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        String placa = ds.child("placa").getValue().toString();
                        String marca = ds.child("marca").getValue().toString()+" "+ds.child("modelo").getValue().toString();

                        vehiculoList.add(new Vehiculo(key, placa, marca));
                    }
                    VehiculoListAdapter vehiculoAdapter = new VehiculoListAdapter(getApplicationContext() ,R.layout.item_servicio, vehiculoList );
                    lvlistar.setAdapter(vehiculoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void listarPlanes()
    {
        conexion.getDatabaseReference().child("planes").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    planList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String estado = ds.child("estado").getValue().toString();

                        if(estado.equals("Activo")) {
                            String key = ds.getKey();
                            String tipo = ds.child("tipoPlan").getValue().toString();
                            double costo = Double.parseDouble(ds.child("costo").getValue().toString());
                            planList.add(new Plan(key, tipo, costo));
                        }
                    }
                    PlanListAdapter planAdapter = new PlanListAdapter(getApplicationContext() ,R.layout.item_servicio, planList );
                    lvlistar.setAdapter(planAdapter);
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
                tiemposList.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot ds: snapshot.getChildren()) {

                        String tiempo = ds.child("tiempo").getValue().toString();
                        tiemposList.add(tiempo);
                    }
                    ArrayAdapter<String> tiempoAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, tiemposList);
                    spnDuracionC.setAdapter(tiempoAdapter);

                    spnDuracionC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String i = parent.getItemAtPosition(position).toString();

                            if(costoPlan == 0) {
                                txtCosto.setText(String.valueOf(costo));
                            }
                            else {
                                if(i.equals("24 meses")) {
                                    costo = costoPlan * 24;
                                    numero = 8;
                                }
                                else if(i.equals("18 meses")) {
                                    costo = costoPlan * 18;
                                    numero = 6;
                                }
                                else{
                                    costo = costoPlan * 12;
                                    numero = 4;
                                }
                                txtNumManto.setText(numero+" mantenimientos");
                                txtCosto.setText(String.valueOf(costo));
                                contrato.setCostoTotal(costo);
                                contrato.setNumeroMantenimientos(numero);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void Seleccion() {
        vt2.setVisibility(View.VISIBLE);
        cvfechaActivacion.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent  =  new Intent( getApplicationContext() , activity_lista_contratos.class  );
        startActivity(intent);
        finish();
    }

}