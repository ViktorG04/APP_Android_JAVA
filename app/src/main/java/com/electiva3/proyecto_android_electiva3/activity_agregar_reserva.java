package com.electiva3.proyecto_android_electiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.electiva3.proyecto_android_electiva3.adapters.SpinnerContratosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Contrato;
import com.electiva3.proyecto_android_electiva3.entities.Horario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class activity_agregar_reserva extends AppCompatActivity {

    private String keyCliente;
    private EditText txtCliente;
    private Spinner spnContrato;
    private Button btnSiguiente;
    private ArrayList<Contrato> contratosCliente;
    private String plan;
    private String cliente;
    private String vehiculo;
    private CalendarView calendarReserva;
    private String fechaSeleccionada;
    private String nombreCliente;
    private long numericCurrentDate;
    private long numericSelectedDate;


    private ArrayList<String> clienteContrato = new ArrayList<>();
    private ArrayList<String> planContrato = new ArrayList<>();
    private ArrayList<String> vehiculoContrato = new ArrayList<>();
    private ArrayList<String> estadosList = new ArrayList<>();

    private Contrato contrato = new Contrato();

    private Conexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_reserva);

        conexion  =  new Conexion();

        txtCliente =  findViewById(R.id.txtCliente);
        spnContrato =  findViewById(R.id.spnContrato);
        btnSiguiente   = findViewById(R.id.btnSiguiente);
        Intent prevIntent = getIntent();
        keyCliente =  prevIntent.getExtras().getString("keyCliente");
        nombreCliente  =  prevIntent.getExtras().getString("nombreCliente");
        calendarReserva  =  findViewById(R.id.calendarReserva);

        establecerFechaActual(); //Establecer la fecha solicitada a la fecha actual
        obtenerDatosCliente(  keyCliente   );  //Obtiene el nombre del cliente para visualizacion
        obtenerContratosCliente();  //Obtiene los contratos del cliente y los lista
        //Obtendra los horarios de la fecha designada
        //generarHorario("2020-11-10");

        //Eventos del calendario
        calendarReserva.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar  =  Calendar.getInstance();
                Calendar calendar1  =  Calendar.getInstance();

                calendar.set(Calendar.YEAR  , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH ,dayOfMonth  );

                SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");

                fechaSeleccionada  = simpleDateFormat.format(calendar.getTime());

                numericCurrentDate  = calendar1.getTimeInMillis();
                numericSelectedDate  =  calendar.getTimeInMillis();

                //Restriccion por seleccion de fecha
                if( numericSelectedDate  <  numericCurrentDate   ){
                    Toast.makeText(activity_agregar_reserva.this, "La fecha seleccionada no puede ser menor a la fecha actual", Toast.LENGTH_SHORT).show();
                }

            }
        });



        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( numericSelectedDate  <  numericCurrentDate   ){
                    Toast.makeText(activity_agregar_reserva.this, "La fecha seleccionada no puede ser menor a la fecha actual", Toast.LENGTH_SHORT).show();
                }else{
                    String keyContrato  = contratosCliente.get(spnContrato.getSelectedItemPosition()).getKey();
                    String numeroContrato  =  String.valueOf(  contratosCliente.get(spnContrato.getSelectedItemPosition()).getNumeroContrato()  )    ;
                    Intent seleccionarHorario  =  new Intent( getApplicationContext() ,  SeleccionarHorarioReserva.class );

                    //Fecha seleccionada
                    seleccionarHorario.putExtra("fecha" , fechaSeleccionada);
                    //Pasar UIDD del cliente
                    seleccionarHorario.putExtra("cliente" ,  keyCliente  );
                    //Contrato seleccionado
                    seleccionarHorario.putExtra("contrato" , keyContrato);

                    //Nombre del cliente Seleccionado
                    seleccionarHorario.putExtra(  "nombreCliente" , nombreCliente );

                    //Numero de contrato
                    seleccionarHorario.putExtra(   "numeroContrato" ,   numeroContrato  );


                    startActivity(seleccionarHorario);
                    finish();
                }


            }
        });
    }


    private void establecerFechaActual(){

        Calendar calendar  =  Calendar.getInstance();

        SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");

        fechaSeleccionada  = simpleDateFormat.format(calendar.getTime());

    }



    private void obtenerContratosCliente(){

            contratosCliente  =  new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("Contratos").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if(snapshot.exists()){

                        for(DataSnapshot ds : snapshot.getChildren()) {

                            if( ds.child("cliente").child("0").getValue().toString().equals(keyCliente)  ){
                                String key = null;
                                contrato  = new Contrato();
                                contrato.setCostoTotal(Double.parseDouble(ds.child("costoTotal").getValue().toString()));
                                contrato.setDuracion(ds.child("duracion").getValue().toString());
                                contrato.setEstado(ds.child("estado").getValue().toString());
                                contrato.setFechaActivacion(ds.child("fechaActivacion").getValue().toString());
                                contrato.setFechaVencimiento(ds.child("fechaVencimiento").getValue().toString());
                                contrato.setNumeroContrato(Integer.parseInt(ds.child("numeroContrato").getValue().toString()));
                                contrato.setNumeroMantenimientos(Integer.parseInt(ds.child("numeroMantenimientos").getValue().toString()));


                                key = ds.child("plan").child("0").getValue().toString();
                                plan = ds.child("plan").child("1").getValue().toString();
                                planContrato.add(key);
                                planContrato.add(plan);

                                key = ds.child("cliente").child("0").getValue().toString();
                                cliente = ds.child("cliente").child("1").getValue().toString();
                                clienteContrato.add(key);
                                clienteContrato.add(cliente);

                                key = ds.child("vehiculo").child("0").getValue().toString();
                                vehiculo = ds.child("vehiculo").child("1").getValue().toString();
                                vehiculoContrato.add(key);
                                vehiculoContrato.add(vehiculo);

                                String keyContrato    = ds.getKey();

                                contrato.setCliente(clienteContrato);
                                contrato.setVehiculo(vehiculoContrato);
                                contrato.setPlan(planContrato);
                                contrato.setKey(keyContrato);
                                contratosCliente.add(contrato);


                            }


                        }


                        SpinnerContratosAdapter spinnerContratosAdapter  =  new SpinnerContratosAdapter(  getApplicationContext() , R.layout.custom_simple_spinner_item ,  contratosCliente     );
                        spnContrato.setAdapter(  spinnerContratosAdapter   );

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

    }

    private void  obtenerDatosCliente(String keyCliente){

        DatabaseReference cliente = FirebaseDatabase.getInstance().getReference().child("usuarios").child(keyCliente);

        cliente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                DataSnapshot ds = snapshot;
                if (ds.exists()) {
                    //Nombre del cliente
                    txtCliente.setText(  ds.child("nombre").getValue().toString()   );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}