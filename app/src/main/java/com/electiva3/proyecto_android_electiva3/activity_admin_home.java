package com.electiva3.proyecto_android_electiva3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.electiva3.proyecto_android_electiva3.flujoContrato.activity_lista_contratos;
import com.electiva3.proyecto_android_electiva3.flujoPlan.activity_lista_planes;
import com.electiva3.proyecto_android_electiva3.flujoServicio.activity_lista_servicios;
import com.electiva3.proyecto_android_electiva3.flujoUsuario.activity_lista_usuarios;
import com.electiva3.proyecto_android_electiva3.flujoVehiculo.activity_lista_vehiculos;

public class activity_admin_home extends AppCompatActivity implements View.OnClickListener
{
    ImageView imgUsuario, imgContrato, imgMarca, imgVehiculo, imgServicio, imgPlan, imgOrdenServicio, imgReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        imgUsuario = findViewById(R.id.imgUsuario);
        imgContrato = findViewById(R.id.imgContrato);
        imgMarca = findViewById(R.id.imgMarca);
        imgVehiculo = findViewById(R.id.imgVehiculo);
        imgServicio = findViewById(R.id.imgServicio);
        imgPlan = findViewById(R.id.imgPlan);
        imgOrdenServicio = findViewById(R.id.imgOrdenServicio);
        imgReserva = findViewById(R.id.imgReserva);

        imgUsuario.setOnClickListener(this);
        imgContrato.setOnClickListener(this);
        imgMarca.setOnClickListener(this);
        imgVehiculo.setOnClickListener(this);
        imgServicio.setOnClickListener(this);
        imgPlan.setOnClickListener(this);
        imgOrdenServicio.setOnClickListener(this);
        imgReserva.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgUsuario:
                Intent usuarios = new Intent(getApplicationContext() ,   activity_lista_usuarios.class);
                startActivity(usuarios);
                break;
            case R.id.imgContrato:
                Intent contratos = new Intent(getApplicationContext() ,   activity_lista_contratos.class);
                startActivity(contratos);
                break;
            case R.id.imgVehiculo:
                Intent vehiculos = new Intent(getApplicationContext() ,   activity_lista_vehiculos.class);
                startActivity(vehiculos);
                finish();
                break;
            case R.id.imgServicio:
                Intent servicios = new Intent(getApplicationContext() ,   activity_lista_servicios.class);
                startActivity(servicios);
                break;
            case R.id.imgPlan:
                Intent planes = new Intent(getApplicationContext() ,   activity_lista_planes.class);
                startActivity(planes);
                break;

            case R.id.imgReserva:

                Intent reservas =  new Intent(getApplicationContext() , activity_lista_reservas.class);
                startActivity(reservas);
                break;

            case R.id.imgOrdenServicio:
                Intent ordenes  =  new Intent( getApplicationContext() , activity_ordenes.class);
                startActivity(ordenes);
                break;
        }
    }
}