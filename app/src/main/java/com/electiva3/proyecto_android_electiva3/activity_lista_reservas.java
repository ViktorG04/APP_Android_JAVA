package com.electiva3.proyecto_android_electiva3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.adapters.ReservasAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Reserva;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class activity_lista_reservas extends AppCompatActivity {


    private TextView tvNotificacion;
    private FloatingActionButton fabAgregarReserva;
    private Conexion conexion =  new Conexion()  ;
    private Usuario usuario  =  new Usuario();
    private ArrayList<Reserva> reservas;
    private ReservasAdapter reservasAdapter;
    String title="Reservas";
    RecyclerView rvReservas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas);

        reservas  =  new ArrayList<>();


        conexion.inicializarFirabase(getApplicationContext());

        tvNotificacion = findViewById(R.id.tvNotificacion);
        fabAgregarReserva =  findViewById(R.id.fabAgregarReserva);
        rvReservas =  findViewById(R.id.rvReservas);

        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getApplicationContext()     );
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvReservas.setLayoutManager(linearLayoutManager);



        obtenerInformacionUsuarioActual();
        fabAgregarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Validacion del rol del usuario para asignacion de usuario
                if(usuario.getRol().equals("Administrador")){
                    //Intent que redirigira a un listado de usuario
                    Intent intent =  new Intent( getApplicationContext()  , activity_search_cliente.class  );
                    startActivity(  intent );

                }else{
                    //Intent que redirigira a la vista de creacion de reservacion
                    Intent   intent  =  new Intent( getApplicationContext() , activity_agregar_reserva.class  );
                    intent.putExtra("keyCliente" ,   usuario.getKey() );
                    intent.putExtra( "nombreCliente" ,   usuario.getNombre()     );
                    startActivity(  intent );
                }



            }
        });


    }


    public ArrayList<Reserva> buildReservas(){
        ArrayList<Reserva>  reservas =  new ArrayList<Reserva>();
        reservas.add( new Reserva("Jorge De La Cruz" , "HM12020201"   , "202/09/2020" , "Pendiente aprobación" ) );
        reservas.add( new Reserva( "Douglas Guzman", "HMQ2020902" , "22/09/2020" , "Procesada"));
        reservas.add( new Reserva( "Juan Perez", "HMQ2020902" , "22/09/2020" , "Pendiente aprobación"));
        return reservas;
    }

    private void listarReservas(){


        Query reservaciones;

        //Listara solo las reservas de los usuarios
        if( usuario.getRol().equals("Cliente")  ){
            reservaciones  =   conexion.getDatabaseReference().child("reservacion").orderByChild("cliente").equalTo(usuario.getKey())  ;
        }else{
            reservaciones= conexion.getDatabaseReference().child("reservacion");
        }

        reservaciones.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");


                Reserva reserva;
                if(  snapshot.exists()  ){
                    reservas.clear();
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        reserva  =  new Reserva();
                        reserva.setKey(ds.getKey());
                        reserva.setEstado(  ds.child("estado").getValue().toString()   );
                        try {
                            reserva.setFechaSolicitada( sdf2.format(  sdf.parse(ds.child("fechaSolicitada") .getValue().toString())    )  );
                        }catch (Exception ex){

                        }

                        reserva.setFechaHoraCreacion( ds.child("fechaHoraCreacion").getValue().toString() );
                        reserva.setHora(  ds.child("hora").getValue().toString() );
                        reserva.setContrato( ds.child("contrato").getValue().toString()  );
                        reserva.setCliente( ds.child("cliente").getValue().toString() );
                        reserva.setNombreCliente( ds.child("nombreCliente").getValue().toString()   );
                        reserva.setNumeroContrato( ds.child("numeroContrato").getValue().toString()  );
                        reserva.setHora(  ds.child("hora").getValue().toString()  );
                        reservas.add(  reserva );
                    }

                    reservasAdapter =  new ReservasAdapter( getApplicationContext() ,  reservas  );
                    rvReservas.setAdapter( reservasAdapter  );
                }
                else {
                    tvNotificacion.setText("No Posees ninguna reservacion");
                    tvNotificacion.setGravity(Gravity.CENTER);
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

                listarReservas();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater  = getMenuInflater();
        menuInflater.inflate( R.menu.filtro_estados_reservas , menu  );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (  item.getItemId() ){

            case R.id.estadoReservaAceptadas:
                filtrarReservas("Aceptada");
                break;

            case R.id.estadoReservaPendientes:
                filtrarReservas("Pendiente");
                break;

            case R.id.estadoReservaRechazadas:
                filtrarReservas("Rechazada");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void filtrarReservas(String estado){

        Query queryOrdenes;


        if(estado.equals("Pendiente")){
            queryOrdenes  = conexion.getDatabaseReference().child("reservacion").orderByChild("estado").equalTo("Pendiente");
        }else if( estado.equals("Aceptada")  ){
            queryOrdenes  = conexion.getDatabaseReference().child("reservacion").orderByChild("estado").equalTo("Aceptada");
        }else{
            queryOrdenes  =  conexion.getDatabaseReference().child("reservacion").orderByChild("estado").equalTo("Rechazada");
        }

        reservas.clear();
        reservasAdapter =  new ReservasAdapter( getApplicationContext() ,  reservas  );
        rvReservas.setAdapter( reservasAdapter  );


        queryOrdenes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");


                Reserva reserva;
                if(  snapshot.exists()  ){
                    reservas.clear();
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        reserva  =  new Reserva();
                        reserva.setKey(ds.getKey());
                        reserva.setEstado(  ds.child("estado").getValue().toString()   );
                        try {
                            reserva.setFechaSolicitada( sdf2.format(  sdf.parse(ds.child("fechaSolicitada") .getValue().toString())    )  );
                        }catch (Exception ex){

                        }

                        reserva.setFechaHoraCreacion( ds.child("fechaHoraCreacion").getValue().toString() );
                        reserva.setHora(  ds.child("hora").getValue().toString() );
                        reserva.setContrato( ds.child("contrato").getValue().toString()  );
                        reserva.setCliente( ds.child("cliente").getValue().toString() );
                        reserva.setNombreCliente( ds.child("nombreCliente").getValue().toString()   );
                        reserva.setNumeroContrato( ds.child("numeroContrato").getValue().toString()  );
                        reserva.setHora(  ds.child("hora").getValue().toString()  );

                        if( !usuario.getRol().equals("Cliente") || (usuario.getRol().equals("Cliente") && usuario.getKey().equals( reserva.getCliente()  )    )   ){
                            reservas.add(  reserva );
                        }



                    }

                    reservasAdapter =  new ReservasAdapter( getApplicationContext() ,  reservas  );
                    rvReservas.setAdapter( reservasAdapter  );
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
