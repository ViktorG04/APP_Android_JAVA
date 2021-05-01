package com.electiva3.proyecto_android_electiva3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.adapters.OrdenAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Orden;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class activity_ordenes extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{


    String title ="Ordenes";
    RecyclerView rvOrdenes;
    FloatingActionButton fabFiltroFechaOrdenes;
    private Conexion conexion;
    private ArrayList<Orden> ordenes;
    private Usuario usuario;
    private int contador;

    private TextView tvNotificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);



        contador = 0 ;
        ordenes  = new ArrayList<>();
        conexion =  new Conexion();
        conexion.inicializarFirabase(  getApplicationContext()  );
        usuario  =  new Usuario();

        tvNotificacion = findViewById(R.id.tvNotificacion);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        fabFiltroFechaOrdenes =   findViewById(R.id.fabFiltroFechaOrdenes);
        rvOrdenes  =  findViewById(R.id.rvOrdenes);

        obtenerInformacionUsuarioActual();

        fabFiltroFechaOrdenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker  = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager() , "date picker");
            }
        });


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrdenes.setLayoutManager(linearLayoutManager);
    }


    private void listarOrdenes(){


        ordenes.clear();
        OrdenAdapter ordenAdapter =  new OrdenAdapter(getApplicationContext() ,   ordenes );
        rvOrdenes.setAdapter(ordenAdapter);
        Query ordenesQuery;

        //Evaluar el rol del usuario actual
        if( usuario.getRol().equals("Cliente")  ){
            ordenesQuery  =  conexion.getDatabaseReference().child("ordenes").orderByChild( "cliente" ).equalTo(usuario.getKey() );
        }else{
            ordenesQuery  =  conexion.getDatabaseReference().child("ordenes");
        }

        ordenesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

                if(snapshot.exists()){

                    for(  DataSnapshot ds:  snapshot.getChildren()  ) {
                        Orden  orden =  new Orden();
                        orden.setKey( ds.getKey()   );
                        orden.setCliente(  ds.child("cliente").getValue().toString()  );
                        orden.setNombreCliente(    ds.child("nombreCliente").getValue().toString()    );
                        //orden.setFecha(  ds.child("fecha").getValue().toString()  );
                        orden.setNumeroOrden(  ds.child("numeroOrden").getValue().toString()  );
                        orden.setEstado( ds.child("estado").getValue().toString()    );
                        orden.setContrato( ds.child("contrato").getValue().toString() );

                        try {
                            orden.setFecha( sdf2.format(  sdf.parse(ds.child("fecha") .getValue().toString())    )  );
                        }catch (Exception ex){

                        }


                        ordenes.add(orden);
                    }

                    OrdenAdapter ordenAdapter =  new OrdenAdapter(getApplicationContext() ,   ordenes );
                    rvOrdenes.setAdapter(ordenAdapter);

                }
                else {
                    tvNotificacion.setText("No Tienes Ordenes");
                    tvNotificacion.setGravity(Gravity.CENTER);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filtrarOrdenesEstado(String estado){

        Query queryOrdenes;

        if(estado.equals("Pendiente")){
            queryOrdenes  =  conexion.getDatabaseReference().child("ordenes").orderByChild("estado").equalTo( "Pendiente" );
        }else if( estado.equals("En Proceso")  ){
            queryOrdenes  =  conexion.getDatabaseReference().child("ordenes").orderByChild("estado").equalTo( "En Proceso" );
        }else{
            queryOrdenes  =  conexion.getDatabaseReference().child("ordenes").orderByChild("estado").equalTo( "Procesada" );
        }

        if(  usuario.getRol().equals( "Cliente"  ) ){
            //queryOrdenes = queryOrdenes.orderByChild("cliente").equalTo(usuario.getKey());
        }

        ordenes.clear();

        OrdenAdapter ordenAdapter =  new OrdenAdapter(getApplicationContext() ,   ordenes );
        rvOrdenes.setAdapter(ordenAdapter);

        queryOrdenes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");




                if(snapshot.exists()){

                    for(  DataSnapshot ds:  snapshot.getChildren()  ) {
                        Orden  orden =  new Orden();
                        orden.setKey( ds.getKey()   );
                        orden.setCliente(  ds.child("cliente").getValue().toString()  );
                        orden.setNombreCliente(    ds.child("nombreCliente").getValue().toString()    );
                        //orden.setFecha(  ds.child("fecha").getValue().toString()  );
                        orden.setNumeroOrden(  ds.child("numeroOrden").getValue().toString()  );
                        orden.setEstado( ds.child("estado").getValue().toString()    );
                        orden.setContrato( ds.child("contrato").getValue().toString() );

                        try {
                            orden.setFecha( sdf2.format(  sdf.parse(ds.child("fecha") .getValue().toString())    )  );
                        }catch (Exception ex){

                        }

                        //Si el usuario no es cliente o si es cliente y la orden es igual a su key
                        if( !usuario.getRol().equals("Cliente") ||  usuario.getKey().equals( orden.getCliente()   )      ){
                            ordenes.add(orden);
                        }

                    }

                    OrdenAdapter ordenAdapter =  new OrdenAdapter(getApplicationContext() ,   ordenes );
                    rvOrdenes.setAdapter(ordenAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //juanperez@gmail.com
    private void obtenerInformacionUsuarioActual(){

        //uid del usuario actualmente logeado
        final String key = conexion.getAuth().getCurrentUser().getUid();

        conexion.getDatabaseReference().child("usuarios").child(key).addValueEventListener(new ValueEventListener() {
            DataSnapshot ds;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ds = snapshot;
                if (ds.exists())
                {
                    //llenando el objeto

                    usuario.setKey(key);
                    usuario.setNombre(ds.child("nombre").getValue().toString());
                    usuario.setDui(ds.child("dui").getValue().toString());
                    usuario.setNit(ds.child("nit").getValue().toString());
                    usuario.setLicencia(ds.child("licencia").getValue().toString());
                    usuario.setCorreo(ds.child("correo").getValue().toString());
                    usuario.setDireccion(ds.child("direccion").getValue().toString());
                    usuario.setPassword(ds.child("password").getValue().toString());
                    usuario.setTelefono(ds.child("telefono").getValue().toString());
                    usuario.setEstado(ds.child("estado").getValue().toString());
                    usuario.setRol(ds.child("rol").getValue().toString());
                }

                listarOrdenes();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.filtrar_estados , menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.estadoPendiente:
                filtrarOrdenesEstado("Pendiente");
                break;

            case R.id.estadoEnProceso:
                filtrarOrdenesEstado("En Proceso");
                break;

            case R.id.estadoProcesadas:
                filtrarOrdenesEstado("Procesada");
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext() , activity_principal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar =  Calendar.getInstance();
        calendar.set(Calendar.YEAR ,  year);
        calendar.set(Calendar.MONTH , month);
        calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaSeleccionada  = simpleDateFormat.format(calendar.getTime());


        filtraOrdenesFecha(fechaSeleccionada);

    }

    private void filtraOrdenesFecha(final String fechaFiltro){



        ordenes.clear();

        OrdenAdapter ordenAdapter =  new OrdenAdapter(getApplicationContext() ,   ordenes );
        rvOrdenes.setAdapter(ordenAdapter);


        conexion.getDatabaseReference().child("ordenes").orderByChild("fecha").startAt(fechaFiltro).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");



                if(snapshot.exists()){

                    for(  DataSnapshot ds:  snapshot.getChildren()  ) {
                        Orden  orden =  new Orden();
                        orden.setKey( ds.getKey()   );
                        orden.setCliente(  ds.child("cliente").getValue().toString()  );
                        orden.setNombreCliente(    ds.child("nombreCliente").getValue().toString()    );
                        //orden.setFecha(  ds.child("fecha").getValue().toString()  );
                        orden.setNumeroOrden(  ds.child("numeroOrden").getValue().toString()  );
                        orden.setEstado( ds.child("estado").getValue().toString()    );
                        orden.setContrato( ds.child("contrato").getValue().toString() );

                        try {
                            orden.setFecha( sdf2.format(  sdf.parse(ds.child("fecha") .getValue().toString())    )  );
                        }catch (Exception ex){

                        }

                        //Si el usuario no es cliente o si es cliente y la orden es igual a su key
                        if( !usuario.getRol().equals("Cliente") ||  usuario.getKey().equals( orden.getCliente()   )      ){

                            if( ds.child("fecha").getValue().toString().substring(0,10).equals(fechaFiltro)  ){
                                ordenes.add(orden);
                            }

                        }

                    }

                    OrdenAdapter ordenAdapter =  new OrdenAdapter(getApplicationContext() ,   ordenes );
                    rvOrdenes.setAdapter(ordenAdapter);

                }


            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
