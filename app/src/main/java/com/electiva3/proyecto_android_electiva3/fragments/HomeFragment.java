package com.electiva3.proyecto_android_electiva3.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_lista_reservas;
import com.electiva3.proyecto_android_electiva3.activity_ordenes;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.flujoContrato.activity_lista_contratos;
import com.electiva3.proyecto_android_electiva3.flujoContrato.activity_mostrar_contrato;
import com.electiva3.proyecto_android_electiva3.flujoMarcas.activity_lista_marcas;
import com.electiva3.proyecto_android_electiva3.flujoPlan.activity_lista_planes;
import com.electiva3.proyecto_android_electiva3.flujoServicio.activity_lista_servicios;
import com.electiva3.proyecto_android_electiva3.flujoUsuario.activity_lista_usuarios;
import com.electiva3.proyecto_android_electiva3.flujoVehiculo.activity_lista_vehiculos;
import com.electiva3.proyecto_android_electiva3.flujoVehiculo.activity_mostrar_vehiculo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment  implements View.OnClickListener  {

    LinearLayout layoutUsuario, layoutContrato, layoutMarca, layoutVehiculo, layoutServicio, layoutPlan, layoutOrdenServicio, layoutReserva;
    ImageView imgUsuario, imgMarca, imgContrato;
    TextView tvLayout1, tvLayout2, tvVehiculo, tvMarca;

    Conexion conexion = new Conexion();
    private FirebaseUser user;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home,container,false);

        conexion.inicializarFirabase(getActivity());
        user = FirebaseAuth.getInstance().getCurrentUser();

        layoutUsuario = view.findViewById(R.id.layoutUsuario);
        
        layoutContrato = view.findViewById(R.id.LayoutContrato);
        layoutMarca = view.findViewById(R.id.layoutMarca);
        layoutVehiculo = view.findViewById(R.id.LayoutVehiculo);
        layoutServicio = view.findViewById(R.id.LayoutServicio);
        layoutPlan = view.findViewById(R.id.LayoutPlan);
        layoutOrdenServicio = view.findViewById(R.id.LayoutOrden);
        layoutReserva = view.findViewById(R.id.LayoutReserva);

        tvLayout1 = view.findViewById(R.id.tvLayout1);
        imgUsuario = view.findViewById(R.id.imgUsuario);

        tvLayout2 = view.findViewById(R.id.tvLayout2);
        imgContrato = view.findViewById(R.id.imgContrato);
        tvVehiculo = view.findViewById(R.id.tvVehiculo);
        imgMarca = view.findViewById(R.id.imgMarca);
        tvMarca = view.findViewById(R.id.tvMarca);

        layoutUsuario.setOnClickListener(this);
        layoutContrato.setOnClickListener(this);
        layoutMarca.setOnClickListener(this);
        layoutVehiculo.setOnClickListener(this);
        layoutServicio.setOnClickListener(this);
        layoutPlan.setOnClickListener(this);
        layoutOrdenServicio.setOnClickListener(this);
        layoutReserva.setOnClickListener(this);




        conexion.getDatabaseReference().child("usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                  String  rol = snapshot.child("rol").getValue().toString();

                    if(rol.equals("Cliente")) {

                         imgUsuario.setImageResource(R.drawable.reserva);
                         tvLayout1.setText("Reservaciones");

                         tvLayout2.setText("Contrato");
                         tvVehiculo.setText("Vehiculo");

                         imgMarca.setImageResource(R.drawable.orden_servicio);
                         tvMarca.setText("Ordenes");

                         NivelCliente();

                         layoutPlan.setVisibility(LinearLayout.INVISIBLE);
                         layoutServicio.setVisibility(LinearLayout.INVISIBLE);
                         layoutReserva.setVisibility(LinearLayout.INVISIBLE);
                         layoutOrdenServicio.setVisibility(LinearLayout.INVISIBLE);
                    }
                    else if (rol.equals("Recepcionista")) {

                        imgUsuario.setImageResource(R.drawable.reserva);
                        tvLayout1.setText("Reservaciones");

                        imgContrato.setImageResource(R.drawable.orden_servicio);
                        tvLayout2.setText("Ordenes de Servicio");

                        NivelRecepcion();

                        layoutVehiculo.setVisibility(LinearLayout.INVISIBLE);
                        layoutMarca.setVisibility(LinearLayout.INVISIBLE);
                        layoutPlan.setVisibility(LinearLayout.INVISIBLE);
                        layoutServicio.setVisibility(LinearLayout.INVISIBLE);
                        layoutReserva.setVisibility(LinearLayout.INVISIBLE);
                        layoutOrdenServicio.setVisibility(LinearLayout.INVISIBLE);

                    }
                    else if(rol.equals("Supervisor")){
                        imgUsuario.setImageResource(R.drawable.orden_servicio);
                        tvLayout1.setText("Ordenes de Servicio");

                        layoutUsuario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent ordenes = new Intent(getActivity() , activity_ordenes.class);
                                startActivity(ordenes);
                                getActivity().finish();
                            }
                        });


                        layoutContrato.setVisibility(LinearLayout.INVISIBLE);
                        layoutVehiculo.setVisibility(LinearLayout.INVISIBLE);
                        layoutMarca.setVisibility(LinearLayout.INVISIBLE);
                        layoutPlan.setVisibility(LinearLayout.INVISIBLE);
                        layoutServicio.setVisibility(LinearLayout.INVISIBLE);
                        layoutReserva.setVisibility(LinearLayout.INVISIBLE);
                        layoutOrdenServicio.setVisibility(LinearLayout.INVISIBLE);
                    }
                    else {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.layoutUsuario:
                final Intent usuarios = new Intent(getActivity() ,   activity_lista_usuarios.class);
                startActivity(usuarios);
                getActivity().finish();
                break;
            case R.id.layoutMarca:
                Intent marcaModelo  = new Intent( getActivity() , activity_lista_marcas.class);
                startActivity(marcaModelo);
                getActivity().finish();
                break;
            case R.id.LayoutContrato:
                Intent contratos = new Intent(getActivity() ,   activity_lista_contratos.class);
                startActivity(contratos);
                getActivity().finish();
                break;
            case R.id.LayoutVehiculo:
                Intent vehiculos = new Intent(getActivity() ,   activity_lista_vehiculos.class);
                startActivity(vehiculos);
                getActivity().finish();
                break;
            case R.id.LayoutServicio:
                Intent servicios = new Intent(getActivity() ,   activity_lista_servicios.class);
                startActivity(servicios);
                getActivity().finish();
                break;
            case R.id.LayoutPlan:
                Intent planes = new Intent(getActivity() ,   activity_lista_planes.class);
                startActivity(planes);
                getActivity().finish();
                break;

            case R.id.LayoutReserva:
                Intent reservas =  new Intent(getActivity() , activity_lista_reservas.class);
                startActivity(reservas);
                getActivity().finish();
                break;

            case R.id.LayoutOrden:
                Intent ordenes  =  new Intent( getActivity() , activity_ordenes.class);
                startActivity(ordenes);
                getActivity().finish();
                break;
        }
    }

    public void NivelCliente()
    {
        conexion.getDatabaseReference().child("Contratos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String cliente = ds.child("cliente").child("0").getValue().toString();
                        if(cliente.equals(user.getUid())) {

                          final String idContrato =  ds.getKey();
                          final String idVehiculo = ds.child("vehiculo").child("0").getValue().toString();

                            layoutContrato.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent contratos = new Intent(getActivity() , activity_mostrar_contrato.class);
                                   contratos.putExtra("id", idContrato);
                                    startActivity(contratos);
                                    getActivity().finish();
                                }
                            });

                            layoutVehiculo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent vehiculos = new Intent(getActivity() ,   activity_mostrar_vehiculo.class);
                                    vehiculos.putExtra("keyVehiculo", idVehiculo);
                                    startActivity(vehiculos);
                                    getActivity().finish();
                                }
                            });

                            layoutUsuario.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent usuariofinal = new Intent(getActivity() ,   activity_lista_reservas.class);
                                    startActivity(usuariofinal);
                                    getActivity().finish();
                                }
                            });

                            layoutMarca.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent usuarioOrdenes  =  new Intent( getActivity() , activity_ordenes.class);
                                    startActivity(usuarioOrdenes);
                                    getActivity().finish();
                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void NivelRecepcion()
    {
        layoutUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reservas =  new Intent(getActivity() , activity_lista_reservas.class);
                startActivity(reservas);
                getActivity().finish();
            }
        });

        layoutContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contratos = new Intent(getActivity() , activity_ordenes.class);
                startActivity(contratos);
                getActivity().finish();
            }
        });
    }


}
