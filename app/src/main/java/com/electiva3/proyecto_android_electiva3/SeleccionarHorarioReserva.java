package com.electiva3.proyecto_android_electiva3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.adapters.HorariosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Horario;
import com.electiva3.proyecto_android_electiva3.entities.Notificaciones;
import com.electiva3.proyecto_android_electiva3.entities.Reserva;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class SeleccionarHorarioReserva extends AppCompatActivity {

    private RecyclerView rvHorarios;
    private ArrayList<Horario> horarios;
    private ArrayList<Horario> horariosDisponibles;
    private String fechaSeleccionada;
    private HorariosAdapter horariosAdapter;
    private AlertDialog.Builder builder;
    private Reserva reserva;
    private Conexion conexion = new Conexion();

    private String keyCliente;
    private String keyContrato;
    private String nombreCliente;
    private String numeroContrato;

    Notificaciones notificaciones = new Notificaciones();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_horario_reserva);

        conexion.inicializarFirabase(this);

        notificaciones.setContext(getApplicationContext());

        Intent previousActivity = getIntent();
        keyCliente  =   previousActivity.getStringExtra("cliente");
        keyContrato  =  previousActivity.getStringExtra("contrato");
        nombreCliente  =  previousActivity.getStringExtra("nombreCliente");
        numeroContrato  =  previousActivity.getStringExtra( "numeroContrato");
        fechaSeleccionada = previousActivity.getStringExtra("fecha");

        reserva  =  new Reserva();
        horarios  =  new ArrayList<>();
        rvHorarios  = findViewById(R.id.rvHorarios);

        //Evaluar rol y establecer el usuario al cual se le realizara la reserva
        builder= new AlertDialog.Builder(this);
        builder.setTitle("Realizar reserva");
        builder.setCancelable(true);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //Obteniendo la fecha actual en formato yyyy-MM-dd HH:mm:ss
                Calendar calendar  =  Calendar.getInstance();
                SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fechaHoraCreacion = simpleDateFormat.format(calendar.getTime());

                reserva.setCliente(keyCliente);
                reserva.setContrato(keyContrato);
                reserva.setFechaSolicitada(fechaSeleccionada);
                reserva.setFechaHoraCreacion( fechaHoraCreacion );
                reserva.setEstado("Pendiente");
                reserva.setNombreCliente(  nombreCliente  );
                reserva.setNumeroContrato(  numeroContrato  );
                String key = (UUID.randomUUID().toString());
                conexion.getDatabaseReference().child("reservacion").child(key).setValue(reserva);

                //envia notificacion de la creacion a cliente, admin y recepcionista
                notificacionReservacion(keyCliente, reserva.getFechaSolicitada());

                Toast.makeText(SeleccionarHorarioReserva.this, "Reserva realizada con exito", Toast.LENGTH_SHORT).show();
                Intent intent  =  new Intent(  getApplicationContext() , activity_lista_reservas.class   );
                startActivity( intent );
                finish();

            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        LinearLayoutManager linearLayoutManager   = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHorarios.setLayoutManager(  linearLayoutManager );
        crearHoras();
    }

    private void realizarReserva() {
    }

    private void crearHoras(){

       horariosDisponibles = new ArrayList<>();

        int horariosFin =20; //Horario de finalizacion hasta las 8pm

        for( int horariosInicio = 8 ; horariosInicio < horariosFin ; horariosInicio++  ){
            String hora  = ( horariosInicio < 10 ) ? "0"+horariosInicio+":00:00"  :  horariosInicio+":00:00";
            Horario  horario  =  new Horario();
            horario.setHora(hora);
            horariosDisponibles.add(horario);
        }

        horariosAdapter =  new HorariosAdapter(getApplicationContext(), horariosDisponibles );
        rvHorarios.setAdapter(horariosAdapter);
        horariosAdapter.SetOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hora =  horariosDisponibles.get(rvHorarios.getChildAdapterPosition(v)).getHora();
                reserva.setHora(hora);
                builder.setMessage("Tu reserva sera realizada para el dia "+fechaSeleccionada+" a las "+hora+" horas"  );
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        consultarDisponibilidadHoras(fechaSeleccionada);
    }

    private void consultarDisponibilidadHoras(String fecha){

        FirebaseDatabase.getInstance().getReference().child("reservacion").orderByChild("fechaSolicitada").equalTo(fecha)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            for(DataSnapshot ds: snapshot.getChildren()){
                                int searchIndex = search( ds.child("hora").getValue().toString() );
                                if(searchIndex!=-1){
                                    horariosDisponibles.remove(searchIndex);
                                }
                            }

                            horariosAdapter =  new HorariosAdapter(getApplicationContext(), horariosDisponibles );
                            horariosAdapter.SetOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String hora =  horariosDisponibles.get(rvHorarios.getChildAdapterPosition(v)).getHora();
                                    reserva.setHora(hora);
                                    builder.setMessage("Tu reserva sera realizada para el dia "+fechaSeleccionada+" a las "+hora+" horas"  );
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            });

                            rvHorarios.setAdapter(horariosAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }});
    }

    private int search(String hour ){
        int left = 0 ;
        int size = horariosDisponibles.size();
        int rigth = size - 1;
        int middle = ((rigth + left) / 2);

        int searchValue = Integer.parseInt(hour.substring(0 ,2 )) ;

        while( left < rigth ){
            if( Integer.parseInt(horariosDisponibles.get(middle).getHora().substring(0,2))==searchValue ){
                return middle;
            }else{
                if(Integer.parseInt(horariosDisponibles.get(middle).getHora().substring(0 ,2 ))  >  searchValue  ){
                    rigth = middle-1;
                }else {
                    left = middle+1;
                }
                middle = ((rigth + left) / 2);
            }
        }
        return -1;
    }


    public void notificacionReservacion(final String keyCliente, final String fecha){

        notificar(keyCliente, fecha);

        conexion.getDatabaseReference().child("usuarios").getRef().orderByChild("rol").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String rol = ds.child("rol").getValue().toString();
                        String estado = ds.child("estado").getValue().toString();

                        if(!rol.equals("Cliente") && !rol.equals("Supervisor") && estado.equals("Activo")){

                            String key = ds.getKey();
                            notificar(key, fecha);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void notificar(String key, String fecha){
        String titulo = "Reservacion creada";
        String detalle = "En espera de aprobacion para la fecha "+fecha;

        notificaciones.MensajeSegunToken(key, titulo, detalle);
    }


}