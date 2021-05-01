package com.electiva3.proyecto_android_electiva3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.electiva3.proyecto_android_electiva3.adapters.SpinnerUsuariosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.DetalleOrden;
import com.electiva3.proyecto_android_electiva3.entities.Notificaciones;
import com.electiva3.proyecto_android_electiva3.entities.Orden;
import com.electiva3.proyecto_android_electiva3.entities.Reserva;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class actualizar_detalle_reserva extends AppCompatActivity {


    private EditText txtNombre ,  txtPlan , txtFecha , txtHora;
    private TextView tvEstadoReserva, tvNumOrden;
    private Spinner spnEstado , spnSupervisor;
    private String key;
    private String keyOrden;
    private Conexion conexion;
    private Reserva reserva;
    private Button btnActualizar;
    private LinearLayout containerSupervisor , containerButton;
    String title ="Detalle reserva";
    private ArrayList<String> estadosReservas;
    private ArrayList<Usuario> usuarios;
    private Usuario usuario;
    public int i = 0;

    //clase notificaciones para enviar mensajes
    Notificaciones notificaciones = new Notificaciones();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_detalle_reserva);


        estadosReservas  = new ArrayList<>();
        usuarios  =  new ArrayList<>();
        usuario  =  new Usuario();

        //recursos para enviar notificaciones
        notificaciones.setContext(getApplicationContext());

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conexion  = new Conexion();
        reserva =  new Reserva();
        conexion.inicializarFirabase(getApplicationContext());

        txtNombre  = findViewById(R.id.txtNombre);
        txtPlan  =  findViewById(R.id.txtPlan);
        txtFecha  =  findViewById(R.id.txtFecha);
        txtHora  =  findViewById(R.id.txtHora);
        spnEstado  =  findViewById(R.id.spnEstado);
        spnSupervisor =  findViewById(R.id.spnSupervisor);
        btnActualizar =   findViewById(R.id.btnActualizar);
        containerSupervisor =  findViewById(R.id.containerSupervisor);
        containerButton =  findViewById(R.id.containerButton);
        tvEstadoReserva  =  findViewById(R.id.tvEstadoReserva);
        tvNumOrden = findViewById(R.id.tvNumOrden);
        tvNumOrden.setVisibility(View.INVISIBLE);

        tvEstadoReserva.setVisibility( View.GONE );

        Intent previusIntent =  getIntent();
        key  = previusIntent.getStringExtra("key");

        obtenerDatosReserva();
        obtenerUsuariosSupervisores();
        obtenerInformacionUsuarioActual();
        numerodenesActuales();

        spnEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String estado  =  estadosReservas.get(  position  );
                //Toast.makeText(actualizar_detalle_reserva.this, estado , Toast.LENGTH_SHORT).show();
                validarEstadoReserva(estado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String estadoSeleccionar = estadosReservas.get(spnEstado.getSelectedItemPosition());
                String supervisor = usuarios.get(spnSupervisor.getSelectedItemPosition()).getKey();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String fecha = dateFormat.format(date);

                if(reserva.getEstado().equals("Rechazado")){
                    Toast.makeText(actualizar_detalle_reserva.this, "Esta reserva ya fue rechazada, ya no puedes actualizarla", Toast.LENGTH_SHORT).show();
                }

                //Si el estado actual de la reserva es Pendiente entonces crear nuevo registro de orden junto con la aceptacion de la orden
                else if (estadoSeleccionar.equals("Aceptada") && reserva.getEstado().equals("Pendiente")) {

                    //Creacion de la orden
                    keyOrden = (UUID.randomUUID().toString());
                    Orden orden = new Orden();

                    orden.setCliente(reserva.getCliente());
                    orden.setContrato(reserva.getContrato());
                    orden.setNombreCliente(reserva.getNombreCliente());
                    orden.setEstado("Pendiente");
                    orden.setNumeroOrden(tvNumOrden.getText().toString());
                    orden.setFecha(fecha);
                    orden.setReserva(reserva.getKey());
                    orden.setSupervisor(supervisor);
                    conexion.getDatabaseReference().child("ordenes").child(keyOrden).setValue(orden);


                    //Actualizacion del estado de la reserva
                    reserva.setEstado(estadoSeleccionar);
                    reserva.UpdateReserva();

                    conexion.getDatabaseReference().child("reservacion").child(key).updateChildren(reserva.getReservaMap());

                    generarDetalleOrden();

                    //envia un mensaje al supervisor seleccionado que la orden le a sido asignado
                    NotificacionSupervisor(supervisor, orden.getNumeroOrden(), fecha);

                }else{
                    //Actualizacion del estado de la reserva
                    reserva.setEstado(estadoSeleccionar);
                    reserva.UpdateReserva();
                    conexion.getDatabaseReference().child("reservacion").child(key).updateChildren(reserva.getReservaMap());

                }

                //Si la reserva sera rechazada esta tendra que rechazar la orden tambien

                // envia un mensaje al cliente del estado de su reservacion segun la seleccion
                NotificacionCliente(reserva.getCliente(), estadoSeleccionar);
            }
        });

    }

    private void generarDetalleOrden(){

        conexion.getDatabaseReference().child("Contratos").child(reserva.getContrato()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds = snapshot;

                if(ds.exists()){

                    String keyPlan =  ds.child("plan").child("0").getValue().toString();
                    copiarServiciosPlan( keyPlan );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void copiarServiciosPlan(String keyPlan){

        conexion.getDatabaseReference().child("planes").child(keyPlan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                DataSnapshot ds =  snapshot;

                if(ds.exists()){

                    int contador = 0;
                    for (DataSnapshot df : ds.child("servicios").getChildren()){


                        DetalleOrden detalleOrden  = new DetalleOrden();


                        detalleOrden.setOrden(keyOrden);
                        detalleOrden.setCantidad("1");

                        detalleOrden.setServicio(  ds.child("servicios").child(String.valueOf(contador)).getValue().toString()  );
                        detalleOrden.setAprovacion(true);
                        detalleOrden.setNotificar(false);
                        detalleOrden.setEstado("Pendiente");

                        String key = (UUID.randomUUID().toString());

                        //detalleOrdenServicios
                        conexion.getDatabaseReference().child("detalleOrdenServicios").child(key).setValue(detalleOrden);

                        contador++;
                    }

                    Toast.makeText(actualizar_detalle_reserva.this, "Orden generada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void validarEstadoReserva(String estado){

            if( estado.equals("Pendiente")  ){
                containerSupervisor.setVisibility( LinearLayout.INVISIBLE   );
                containerButton.setVisibility(   LinearLayout.INVISIBLE );
                btnActualizar.setText( "Aceptar y crear orden"  );
            }else if( estado.equals("Aceptada")){
                containerSupervisor.setVisibility( LinearLayout.VISIBLE   );
                containerButton.setVisibility(   LinearLayout.VISIBLE );
                btnActualizar.setText( "Aceptar y crear orden"  );
            }else if(estado.equals("Rechazado")){
                containerSupervisor.setVisibility( LinearLayout.INVISIBLE   );
                containerButton.setVisibility(   LinearLayout.VISIBLE );
                btnActualizar.setText( "Actualizar"  );
            }else{
                containerSupervisor.setVisibility( LinearLayout.VISIBLE   );
                containerButton.setVisibility(   LinearLayout.VISIBLE );
                btnActualizar.setText( "Actualizar"  );
            }

    }

    private void obtenerDatosReserva(){

        conexion.getDatabaseReference().child("reservacion").child(key).addValueEventListener(new ValueEventListener() {
            DataSnapshot ds;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ds = snapshot;
                if (ds.exists())
                {
                    reserva.setKey(  ds.getKey() );
                    reserva.setHora(  ds.child("hora").getValue().toString()   );
                    reserva.setNumeroContrato(  ds.child("numeroContrato").getValue().toString()  );
                    reserva.setCliente(  ds.child("cliente").getValue().toString()  );
                    reserva.setFecha( ds.child("fechaSolicitada").getValue().toString()  );
                    reserva.setNombreCliente(  ds.child("nombreCliente").getValue().toString() );
                    reserva.setContrato(  ds.child("contrato").getValue().toString()  );
                    reserva.setFechaSolicitada( ds.child("fechaSolicitada").getValue().toString() );
                    reserva.setEstado(  ds.child("estado").getValue().toString()  );
                    reserva.setFechaHoraCreacion( ds.child("fechaHoraCreacion").getValue().toString() );

                    //Si la reserva aun esta pendiente entonces no mostrar el listado de supervisores
                }
                txtNombre.setText(  reserva.getNombreCliente()   );
                txtFecha.setText( reserva.getFechaSolicitada() );
                txtHora.setText(  reserva.getHora()  );
                txtPlan.setText( reserva.getNumeroContrato()  );
                obtenerEstados();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void obtenerEstados(){


        if( reserva.getEstado().equals("Pendiente")  ){
            estadosReservas.add("Pendiente");
        }

        estadosReservas.add("Aceptada");


        if( !reserva.getEstado().equals("Pendiente")  ){
            estadosReservas.add("En Proceso");
        }

        estadosReservas.add("Rechazado");




        ArrayAdapter arrayAdapter  =  new ArrayAdapter( getApplicationContext() , R.layout.support_simple_spinner_dropdown_item ,   estadosReservas     );
        spnEstado.setAdapter(  arrayAdapter );
        int indexEstado = buscarEstado();
        spnEstado.setSelection(indexEstado);
        validarEstadoReserva(reserva.getEstado());


    }

    private int buscarEstado(){

        int index = -1;

        for(int i=0;  i < estadosReservas.size() ; i++ ){
            //Toast.makeText(this, reserva.getEstado()  , Toast.LENGTH_SHORT).show();
            if(estadosReservas.get(i).equals( reserva.getEstado()  )   ){
                index = i;
                break;
            }
        }

        return index;
    }




    private void obtenerUsuariosSupervisores(){


        FirebaseDatabase.getInstance().getReference().child("usuarios").getRef().orderByChild("rol").equalTo("Supervisor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if( snapshot.exists()){

                    for(DataSnapshot ds :  snapshot.getChildren()   ){
                        Usuario usuario  =  new Usuario();
                        usuario.setKey(  ds.getKey() );
                        usuario.setNombre( ds.child("nombre").getValue().toString()  );
                        usuarios.add(usuario);
                    }

                    SpinnerUsuariosAdapter spinnerUsuariosAdapter  =  new SpinnerUsuariosAdapter( getApplicationContext() , R.layout.support_simple_spinner_dropdown_item ,
                            usuarios );


                    spnSupervisor.setAdapter(  spinnerUsuariosAdapter  );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                    if(  usuario.getRol().equals("Cliente")  ){
                        spnEstado.setVisibility(View.GONE );
                        tvEstadoReserva.setVisibility(View.VISIBLE);
                        tvEstadoReserva.setText(  reserva.getEstado()  );
                    }

                }

                //listarReservas();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void NotificacionSupervisor(String idSupervisor, String numeroOrden, String fecha)
    {
        String titulo = "La orden #"+numeroOrden;
        String detalle = "le a sido asignado con fecha "+fecha;
        notificaciones.MensajeSegunToken(idSupervisor, titulo, detalle);
    }

    public void NotificacionCliente(String idCliente, String estado){
        String titulo = "Estado de su Reservacion";
        String detalle = "Su reservacion a sido :"+estado;

        notificaciones.MensajeSegunToken(idCliente, titulo, detalle);
    }

    public void numerodenesActuales(){

        conexion.getDatabaseReference().child("ordenes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        i += 1;
                    }
                    tvNumOrden.setText(String.valueOf(i+=1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
