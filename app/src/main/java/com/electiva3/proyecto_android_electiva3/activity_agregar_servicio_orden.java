package com.electiva3.proyecto_android_electiva3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.adapters.ArticulosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.DetalleOrden;
import com.electiva3.proyecto_android_electiva3.entities.Notificaciones;
import com.electiva3.proyecto_android_electiva3.entities.Servicio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class activity_agregar_servicio_orden extends AppCompatActivity {

    String title ="Agregar servicio";
    ImageView filtroServicios;
    RecyclerView rvArticulosServicios;
    private ArrayList<Servicio> servicios;
    private Conexion conexion;
    private ArticulosAdapter articulosAdapter;
    private String keyOrden;
    TextView tvCliente;

    //clase notificaciones para enviar mensajes
    Notificaciones notificaciones = new Notificaciones();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_servicio_orden);

        conexion  = new Conexion();
        servicios   = new ArrayList<>();
        conexion.inicializarFirabase( getApplicationContext()   );

        //recursos para enviar notificaciones
        notificaciones.setContext(getApplicationContext());
        tvCliente = findViewById(R.id.tvCliente);
        tvCliente.setVisibility(View.INVISIBLE);

        Intent intent  =  getIntent();

        keyOrden  =  intent.getStringExtra("keyOrden");

        rvArticulosServicios =  findViewById(R.id.rvArticulosServicios);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvArticulosServicios.setLayoutManager(linearLayoutManager);

        listarArticulosServicios();
        ObtenerTokenCliente(keyOrden);
    }

    private void listarArticulosServicios(){
        conexion.getDatabaseReference().child("ArticulosServicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot ds : snapshot.getChildren()){

                        Servicio  servicio  = new Servicio();

                        servicio.setKey( ds.getKey()  );
                        servicio.setTitulo( ds.child("titulo").getValue().toString()  );
                        servicio.setDescripcion( ds.child("descripcion").getValue().toString()  );
                        servicio.setCosto( Double.parseDouble(ds.child("costo").getValue().toString())   );
                        servicio.setCategoria( ds.child("categoria").getValue().toString()  );

                        servicios.add(servicio);
                    }

                    articulosAdapter =  new ArticulosAdapter( getApplicationContext() ,   servicios  );

                    articulosAdapter.SetOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Servicio  servicio  =  servicios.get(rvArticulosServicios.getChildAdapterPosition(v) );

                            // create an alert builder
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity_agregar_servicio_orden.this);
                            // set the custom layout
                            final View customLayout = getLayoutInflater().inflate(R.layout.modal_layout, null);



                            TextView tvTituloServicio  =  customLayout.findViewById( R.id.tvTituloServicio   );
                            TextView tvDescripcion  =  customLayout.findViewById( R.id.tvDescripcion   );
                            TextView tvCosto =   customLayout.findViewById(  R.id.tvCosto  );
                            Button   btnAgregar  =   customLayout.findViewById(  R.id.btnAgregar  );
                            Button   btnAgregarNotificar  =  customLayout.findViewById(  R.id.btnAgregarNotificar   );


                            tvTituloServicio.setText( servicio.getTitulo()   );
                            tvDescripcion.setText( servicio.getDescripcion()   );
                            tvCosto.setText( "$"+String.valueOf( servicio.getCosto()   ) );

                            final String tokenCliente = tvCliente.getText().toString();

                            btnAgregar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DetalleOrden detalleOrden  =  new DetalleOrden();

                                    detalleOrden.setNotificar(false);
                                    detalleOrden.setAprovacion( true );
                                    detalleOrden.setOrden(  keyOrden );
                                    detalleOrden.setServicio( servicio.getKey() );
                                    detalleOrden.setCantidad( "1");
                                    detalleOrden.setEstado("Pendiente");
                                    String key = (UUID.randomUUID().toString());

                                    conexion.getDatabaseReference().child("detalleOrdenServicios").child(key).setValue(detalleOrden);

                                    notificacionCliente(tokenCliente, detalleOrden.getEstado());

                                    Toast.makeText(activity_agregar_servicio_orden.this, "Servicio agregado correctamento", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            });

                            //supervisor o admin envian notificacion al cliente que se ha agregado un servicio y debe de ser aprobado o rechazado
                            btnAgregarNotificar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DetalleOrden detalleOrden  =  new DetalleOrden();

                                    detalleOrden.setNotificar(true);
                                    detalleOrden.setAprovacion( false );
                                    detalleOrden.setOrden(  keyOrden );
                                    detalleOrden.setServicio( servicio.getKey() );
                                    detalleOrden.setCantidad( "1");
                                    String key = (UUID.randomUUID().toString());
                                    detalleOrden.setEstado("Pendiente de aprobacion");

                                    conexion.getDatabaseReference().child("detalleOrdenServicios").child(key).setValue(detalleOrden);

                                    notificacionCliente(tokenCliente, detalleOrden.getEstado());

                                    Toast.makeText(activity_agregar_servicio_orden.this, "Servicio agregado correctamento", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            });



                            builder.setView(customLayout);
                            // add a button
                            builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
                                }
                            });
                            // create and show the alert dialog
                           AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });

                    rvArticulosServicios.setAdapter(articulosAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(  R.menu.search_menu , menu  );
        MenuItem item  = menu.findItem(R.id.action_search);
        SearchView searchView =  (SearchView)  item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                articulosAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void ObtenerTokenCliente(final String idOrden){

        conexion.getDatabaseReference().child("ordenes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        if (id.equals(idOrden)){
                            String cliente = ds.child("cliente").getValue().toString();
                            tvCliente.setText(cliente);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void notificacionCliente(String token, String estado)
    {
        String titulo = "Nuevo Servicio Agregado";
        String detalle;

        if(!estado.equals("Pendiente")) {
            detalle = "Se a agregado un servicio a su orden que no esta en su contrato, desea aceptar o rechazar el servicio, " +
                    "cualquier consulta comunicarse al tel.25056667";
        }
        else{
            detalle = "Se a agregado un nuevo servicio durante el mantenimiento a su orden de servicio, con su consentimiento";
        }

        notificaciones.MensajeSegunToken(token, titulo, detalle);
    }



}
