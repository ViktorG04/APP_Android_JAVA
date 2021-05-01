package com.electiva3.proyecto_android_electiva3.flujoServicio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.electiva3.proyecto_android_electiva3.adapters.CategoriaAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.EstadoAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.ServiciosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Servicio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class activity_lista_servicios extends AppCompatActivity
{

    private FloatingActionButton faAgregarServicio;
    private RecyclerView rvServicio;
    private String title="Lista Servicios";
    //para listar servicios
    private ArrayList<Servicio> serviciosList = new ArrayList<>();
    //para llenar spinner
    private ArrayList<String> categoriaList = new ArrayList<>();
    private ArrayList<String> estadosList = new ArrayList<>();;

    private ConstraintLayout Vt2;
    private TextView tvTitulo;
    private EditText edtTitulo, edtDescripcion, edtCosto;
    private Spinner spnCategoria, spnEstado;
    private Button btnAccion, btnCancelar;
    private  DataSnapshot ds;

    Conexion conexion = new Conexion();
    Servicio servicio = new Servicio();

    private String id;
    private String titulo;
    private String descripcion;
    private Double costo;
    private String c;
    private String categoria;
    private String estado;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_servicios);

        //variables lista layout
        faAgregarServicio = findViewById(R.id.fabAgregarServicio);
        rvServicio = findViewById(R.id.rvServicio);
        Vt2 = findViewById(R.id.vt2);

        //variables crear servicio
        tvTitulo = findViewById(R.id.tvTitulo);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtCosto = findViewById(R.id.edtCosto);
        spnCategoria = findViewById(R.id.spnCategoria);
        btnAccion = findViewById(R.id.btnAccion);
        btnCancelar = findViewById(R.id.btnCancelar);
        spnEstado = findViewById(R.id.spnEstado);
        getSupportActionBar().setTitle(title);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvServicio.setLayoutManager(linearLayoutManager);

        conexion.inicializarFirabase(this);
        ListarServicios();

        faAgregarServicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Vt2.setVisibility(View.VISIBLE);
                faAgregarServicio.setVisibility(View.INVISIBLE);
                tvTitulo.setText("Agregar Nuevo Servicio");
                btnAccion.setText("Crear");
                spnEstado.setEnabled(false);
                CategoriaServicios();
                mostrarEstados();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Vt2.setVisibility(View.INVISIBLE);
                faAgregarServicio.setVisibility(View.VISIBLE);
                edtCosto.setText("");
                edtDescripcion.setText("");
                edtTitulo.setText("");
                spnEstado.setEnabled(false);
                edtTitulo.setEnabled(true);
                edtDescripcion.setEnabled(true);
                edtCosto.setEnabled(true);
                spnCategoria.setEnabled(true);
            }
        });

        btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = btnAccion.getText().toString();

                if(btn.equals("Crear")) {
                    crearServicio();
                }
                else {
                    actualizarServicio();
                }
            }
        });

    }

    public void ListarServicios()
    {
        conexion.getDatabaseReference().child("ArticulosServicios").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    serviciosList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        String titulo = ds.child("titulo").getValue().toString();
                        String descripcion = ds.child("descripcion").getValue().toString()+"  $"+ds.child("costo").getValue().toString();
                        String estado = ds.child("estado").getValue().toString();

                        serviciosList.add(new Servicio(key, titulo, descripcion, estado));
                    }

                    ServiciosAdapter servicioAdapter = new ServiciosAdapter(getApplicationContext(), serviciosList);
                    servicioAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            id=  serviciosList.get(rvServicio.getChildAdapterPosition(v)).getKey();
                            updateServicio();
                        }
                    });

                    rvServicio.setAdapter(servicioAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void CategoriaServicios()
    {
        conexion.getDatabaseReference().child("categoriaServicios").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    categoriaList.clear();

                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String categoria = Objects.requireNonNull(ds.child("categoria").getValue()).toString();
                            categoriaList.add(categoria);
                    }
                    CategoriaAdapter categoriaAdapter = new CategoriaAdapter(getApplicationContext() , R.layout.custom_simple_spinner_item, categoriaList);
                    spnCategoria.setAdapter(categoriaAdapter);
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

                        if(estado.equals(servicio.getEstado()))
                        {
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

                        if(estado.equals("Activo") || estado.equals("Inactivo"))
                        {
                            if (!estadosList.contains(estado))
                            {
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

    public void updateServicio()
    {
        Vt2.setVisibility(View.VISIBLE);
        faAgregarServicio.setVisibility(View.INVISIBLE);
        tvTitulo.setText("Actualizar Servicio");
        btnAccion.setText("Actualizar");
        spnEstado.setEnabled(true);
        edtTitulo.setEnabled(false);
        edtDescripcion.setEnabled(false);
        edtCosto.setEnabled(false);
        spnCategoria.setEnabled(false);

        conexion.getDatabaseReference().child("ArticulosServicios").child(id).addValueEventListener
                (new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ds = snapshot;
                if (ds.exists())
                {
                    servicio.setEstado(ds.child("estado").getValue().toString());
                    servicio.setTitulo(ds.child("titulo").getValue().toString());
                    servicio.setCategoria(ds.child("categoria").getValue().toString());
                    servicio.setDescripcion(ds.child("descripcion").getValue().toString());
                    servicio.setCosto(Double.parseDouble(ds.child("costo").getValue().toString()));

                    edtTitulo.setText(servicio.getTitulo());
                    edtDescripcion.setText(servicio.getDescripcion());
                    c = String.valueOf(servicio.getCosto());
                    edtCosto.setText(c);
                    CategoriaAsignado(servicio.getCategoria());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void crearServicio()
    {
        titulo = edtTitulo.getText().toString();
        descripcion = edtDescripcion.getText().toString();
        //c = ;
        costo = Double.parseDouble(edtCosto.getText().toString());
        categoria = spnCategoria.getSelectedItem().toString();
        estado = spnEstado.getSelectedItem().toString();

        if(TextUtils.isEmpty(titulo)){
            edtTitulo.setError("Campo Requerido");
        }
        else if(TextUtils.isEmpty(descripcion)){
            edtDescripcion.setError("Campo Requerido");
        }
        else if(TextUtils.isEmpty(String.valueOf(costo))){
            edtCosto.setError("Campo Requerido");
        }
        else if(categoria.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "seleccione una categoria", Toast.LENGTH_LONG).show();
        }
        else
        {
            String key = (UUID.randomUUID().toString());
            servicio.setTitulo(titulo);
            servicio.setCategoria(categoria);
            servicio.setCosto(costo);
            servicio.setDescripcion(descripcion);
            servicio.setEstado(estado);

            conexion.getDatabaseReference().child("ArticulosServicios").child(key).setValue(servicio);
            Toast.makeText(getApplicationContext(), "Servicio creado Exitozamente!!", Toast.LENGTH_LONG).show();

            Vt2.setVisibility(View.INVISIBLE);
            faAgregarServicio.setVisibility(View.VISIBLE);
        }
    }

    public void actualizarServicio()
    {
        estado = spnEstado.getSelectedItem().toString();

        if(!estado.equals(servicio.getEstado())) {
            servicio.setEstado(estado);
            servicio.UpdateServicio();
            conexion.getDatabaseReference().child("ArticulosServicios").child(id).updateChildren(servicio.getServicioMap());
            Toast.makeText(getApplicationContext(), "Servicio Inactivo", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "No se realizaron Cambios", Toast.LENGTH_SHORT).show();
        }
        Vt2.setVisibility(View.INVISIBLE);
        faAgregarServicio.setVisibility(View.VISIBLE);
    }

    public void CategoriaAsignado(final String cat)
    {
        conexion.getDatabaseReference().child("categoriaServicios").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    categoriaList.clear();

                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String categoria = Objects.requireNonNull(ds.child("categoria").getValue()).toString();
                        if(categoria.equals(cat) ) {
                            categoriaList.add(categoria);
                        }
                    }
                    CategoriaAdapter categoriaAdapter = new CategoriaAdapter(getApplicationContext() , R.layout.custom_simple_spinner_item, categoriaList);
                    spnCategoria.setAdapter(categoriaAdapter);
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
