package com.electiva3.proyecto_android_electiva3.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_agregar_servicio_orden;
import com.electiva3.proyecto_android_electiva3.adapters.DetalleOrdenAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.DetalleOrden;
import com.electiva3.proyecto_android_electiva3.entities.Notificaciones;
import com.electiva3.proyecto_android_electiva3.entities.Orden;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentOrdenOrden extends Fragment   {

    RecyclerView rvDetalleOrdenes;
    FloatingActionButton fabAgregarDetalleOrden;
    TextView tvCliente, tvSupervisor, tvNumOrden;
    private Conexion conexion;
    private Orden orden;
    private String keyOrden;
    private ArrayList<DetalleOrden> detallesOrdenes;
    private ArrayList<String> servicios;
    private DetalleOrdenAdapter detalleOrdenAdapter;
    private int contador;
    private Usuario usuario;

    Notificaciones notificaciones = new Notificaciones();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.fragment_orden_orden,container,false);

        contador = 0 ;
        detallesOrdenes  =  new ArrayList<>();
        conexion  =  new Conexion();
        orden  =  new Orden();
        conexion.inicializarFirabase( getActivity() );
        keyOrden = getArguments().getString("key");
        usuario  = new Usuario();

        //datos necesarios para enviar notitificaciones
        notificaciones.setContext(getContext());
        tvCliente = view.findViewById(R.id.tvCliente);
        tvCliente.setVisibility(View.INVISIBLE);
        tvSupervisor = view.findViewById(R.id.tvSupervisor);
        tvSupervisor.setVisibility(View.INVISIBLE);
        tvNumOrden = view.findViewById(R.id.tvNumOrden);
        tvNumOrden.setVisibility(View.INVISIBLE);

        rvDetalleOrdenes =  view.findViewById(R.id.rvDetalleOrdenes);
        fabAgregarDetalleOrden =  view.findViewById(R.id.fabAgregarDetalleOrden);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetalleOrdenes.setLayoutManager(linearLayoutManager);


        obtenerInformacionUsuarioActual();
        obtenerDatosOrden( keyOrden  );
        consultarDetalleOrden();


        fabAgregarDetalleOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity() ,  activity_agregar_servicio_orden.class   );
            intent.putExtra(  "keyOrden" , keyOrden  );
            startActivity(intent);
            }
        });

        return view;
    }


    private void consultarDetalleOrden(){

        //Toast.makeText( getActivity()  , keyOrden , Toast.LENGTH_SHORT).show();


        conexion.getDatabaseReference().child("detalleOrdenServicios").orderByChild("orden").equalTo(keyOrden).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if( snapshot.exists() ){
                    detallesOrdenes.clear();
                    for( DataSnapshot ds :   snapshot.getChildren()  ){

                        DetalleOrden  detalleOrden  =  new DetalleOrden();

                        String key  =  ds.getKey();

                        //Toast.makeText( getActivity()  , ds.getKey() , Toast.LENGTH_SHORT).show();

                        detalleOrden.setKey(  key   );
                        detalleOrden.setNotificar(  Boolean.parseBoolean(ds.child("notificar").getValue().toString() )    );
                        detalleOrden.setAprovacion( Boolean.parseBoolean(ds.child("aprovacion").getValue().toString()    )   );
                        detalleOrden.setOrden( ds.child("orden").getValue().toString()  );
                        detalleOrden.setServicio( ds.child("servicio").getValue().toString()  );
                        detalleOrden.setEstado( ds.child("estado").getValue().toString()  );


                        if( detalleOrden.getEstado().equals("Realizado" )  ){
                            contador++;
                        }

                        detallesOrdenes.add( detalleOrden );
                        //Establecer el adaptador de los detalles
                        detalleOrdenAdapter =  new DetalleOrdenAdapter(getActivity() , detallesOrdenes   );

                        rvDetalleOrdenes.setAdapter(detalleOrdenAdapter);
                    }


                    //Si la cantidad de items es igual a la cantidad de items realiazdos
                    if( (contador+1) == detallesOrdenes.size() ){
                        procesar();
                    }else{
                        if(contador>=1 && contador < detallesOrdenes.size() ){
                            enProceso();
                        }
                    }

                    ServiciosSeleccionados();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void obtenerDatosOrden(String keyOrden){

        conexion.getDatabaseReference().child("ordenes").child( keyOrden  ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                DataSnapshot ds  =  snapshot;

                if(ds.exists()){

                    orden.setKey( ds.getKey()   );
                    orden.setCliente( ds.child("cliente").getValue().toString()   );
                    orden.setContrato( ds.child("contrato").getValue().toString()  );
                    orden.setEstado( ds.child( "estado").getValue().toString()  );
                    orden.setNombreCliente(  ds.child("nombreCliente").getValue().toString() );
                    orden.setNumeroOrden( ds.child("numeroOrden").getValue().toString()  );
                    orden.setReserva( ds.child("reserva").getValue().toString()  );
                    orden.setSupervisor(  ds.child("supervisor").getValue().toString() );
                    orden.setFecha( ds.child("fecha").getValue().toString()  );
                    tvCliente.setText(orden.getCliente());
                    tvSupervisor.setText(orden.getSupervisor());
                    tvNumOrden.setText(orden.getNumeroOrden());
                    if( ds.child("estado").getValue().toString().equals("Procesada")  ){
                        fabAgregarDetalleOrden.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void enProceso(){
        orden.setEstado("En Proceso");
        orden.UpdateOrden();
        conexion.getDatabaseReference().child("ordenes").child(keyOrden).updateChildren( orden.getOrdenMap()   );
        //fabAgregarDetalleOrden.setVisibility(View.GONE);
    }

    private void procesar(){
        orden.setEstado("Procesada");
        orden.UpdateOrden();
        conexion.getDatabaseReference().child("ordenes").child(keyOrden).updateChildren( orden.getOrdenMap()   );
        fabAgregarDetalleOrden.setVisibility(View.GONE);
    }


    public void ServiciosSeleccionados()
    {
        conexion.getDatabaseReference().child("ArticulosServicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot ds:  snapshot.getChildren()   ){
                        buscarAsignarServicio(  String.valueOf(ds.getKey())   , ds.child("titulo").getValue().toString()  ,  ds.child("costo").getValue().toString()   );
                    }

                    //Establecer el adaptador de los detalles
                    detalleOrdenAdapter =  new DetalleOrdenAdapter(getActivity() , detallesOrdenes   );

                    detalleOrdenAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final DetalleOrden servicio  =  detallesOrdenes.get(rvDetalleOrdenes.getChildAdapterPosition(v) );

                            // create an alert builder
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity() );
                            // set the custom layout


                            final View customLayout;
                            if(usuario.getRol().equals("Cliente")){
                                customLayout = getLayoutInflater().inflate(R.layout.modal_confirmacion_cliente, null);
                            }else{
                                customLayout = getLayoutInflater().inflate(R.layout.modal_detalle_orden, null);
                            }



                            TextView tvTituloServicio  =  customLayout.findViewById( R.id.tvArticulo   );
                            TextView tvEstado  =  customLayout.findViewById( R.id.tvEstado   );
                            TextView tvCosto =   customLayout.findViewById(  R.id.tvCosto  );
                            TextView tvDescripcion   =  customLayout.findViewById(R.id.tvDescripcion);
                            Button   btnRealizado  =   customLayout.findViewById(  R.id.btnRealizado  );
                            Button   btnEliminar  =  customLayout.findViewById(  R.id.btnEliminar   );
                            Button   btnAceptar =   customLayout.findViewById(R.id.btnConfirmar);


                            tvTituloServicio.setText( servicio.getNombreServicio()   );
                            tvCosto.setText( "$"+String.valueOf( servicio.getPrecio() ) );
                            tvEstado.setText(  servicio.getEstado()  );
                            tvDescripcion.setText("");

                            builder.setView(customLayout);
                            // add a button
                            builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
                                }
                            });
                            // create and show the alert dialog


                            final AlertDialog dialog = builder.create();

                            if( (!usuario.getRol().equals("Cliente") && !servicio.getEstado().equals("Realizado")) ||  usuario.getRol().equals("Cliente")  && servicio.isNotificar()){
                                dialog.show();
                            }

                            final String tokenSupervisor = tvSupervisor.getText().toString();
                            final String numOrden = tvNumOrden.getText().toString();
                            final String tokenCliente = tvCliente.getText().toString();

                            if(!usuario.getRol().equals("Cliente")){

                                btnRealizado.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //enviar notificacion si el supervisor a terminado un servicio
                                        if(servicio.isAprovacion()){
                                            servicio.setEstado("Realizado");
                                            conexion.getDatabaseReference().child("detalleOrdenServicios").child(servicio.getKey()).setValue(servicio);
                                            notificacionCliente(tokenCliente, numOrden, servicio.getNombreServicio(), servicio.getEstado());
                                            Toast.makeText(  getActivity()  , "Servicio actualizado de forma correcta", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }else{
                                            Toast.makeText(  getActivity()  , "El servicio no ha sido aprobado por el usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                btnEliminar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        eliminarDetalleOrden( servicio.getKey(), tokenSupervisor, numOrden );
                                        notificacionCliente(tokenCliente, numOrden, servicio.getNombreServicio(), "Rechazado con su autorizacion");
                                        dialog.cancel();
                                    }
                                });
                                //tendria que ser opciones de usuario
                            }else{

                                btnEliminar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        eliminarDetalleOrden( servicio.getKey(), tokenSupervisor, numOrden  );
                                        dialog.cancel();
                                    }
                                });

                                btnAceptar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aceptarDetalleOrden(servicio);
                                        Toast.makeText(getActivity(), orden.getNumeroOrden(), Toast.LENGTH_SHORT).show();
                                        notificacionSupervisor(tokenSupervisor, numOrden , "APROBADO");
                                    }
                                });
                            }
                        }
                    });
                    rvDetalleOrdenes.setAdapter(detalleOrdenAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void aceptarDetalleOrden( DetalleOrden servicio ){
        servicio.setEstado("Aprobado");
        Toast.makeText(getActivity(), "APROBADO", Toast.LENGTH_SHORT).show();
        conexion.getDatabaseReference().child("detalleOrdenServicios").child(servicio.getKey()).setValue(servicio);
    }

    private void eliminarDetalleOrden(String keyDetalleOrden, String token, String numOrden){

        conexion.getDatabaseReference().child("detalleOrdenServicios").child( keyDetalleOrden ).removeValue();
        notificacionSupervisor(token, numOrden , "RECHAZADO");
        Toast.makeText(  getActivity()  , "Servicio eliminado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void buscarAsignarServicio(String keyServicio , String nombreServicio , String costo){
        for( int i =0 ; i <  detallesOrdenes.size() ;  i++  ){
            if(  detallesOrdenes.get(i).getServicio().equals(keyServicio) ){
                detallesOrdenes.get(i).setNombreServicio( nombreServicio);
                detallesOrdenes.get(i).setPrecio(    costo   );
            }
        }
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

                if( usuario.getRol().equals("Cliente")  ){
                    fabAgregarDetalleOrden.setVisibility(View.GONE );
                }

                //listarOrdenes();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void notificacionSupervisor(String token, String orden, String estado)
    {
        String titulo = "Status de la Orden #"+orden;
        String detalle = "El Cliente a "+estado+" el servicio notificado";

        notificaciones.MensajeSegunToken(token, titulo, detalle);
    }

    public void notificacionCliente(String token, String orden, String servicio, String estado)
    {
        String titulo = "Status de la Orden #"+orden;
        String detalle = "El Serivicio "+servicio+" a sido "+estado;

        notificaciones.MensajeSegunToken(token, titulo, detalle);
    }

}
