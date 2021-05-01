package com.electiva3.proyecto_android_electiva3.fragments;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Contrato;
import com.electiva3.proyecto_android_electiva3.entities.Vehiculo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FragmentVehiculoOrden extends Fragment {

    private Conexion conexion;
    private String keyOrden;
    private String contrato;
    private Vehiculo  vehiculo;
    private EditText edtPlaca , edtModelo , edtColor, edtAnio;
    private ImageView  imgVehiculo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_vehiculo_orden , container , false);


        imgVehiculo =  view.findViewById(R.id.imgVehiculo);
        edtPlaca =  view.findViewById(R.id.edtPlaca);
        edtModelo =  view.findViewById(  R.id.edtModelo  );
        edtColor   = view.findViewById(R.id.edtColor);
        edtAnio =  view.findViewById( R.id.edtAnio  );

        conexion = new Conexion();
        vehiculo =  new Vehiculo();

        keyOrden = getArguments().getString("key");
        contrato =  getArguments().getString( "contrato");
        conexion.inicializarFirabase(  getActivity()   );
        //return super.onCreateView(inflater, container, savedInstanceState);


        //Toast.makeText( getActivity()   , keyOrden , Toast.LENGTH_SHORT).show();
        obtenerInformacionVehiculo();


        return view;
    }


    private void obtenerInformacionVehiculo(){
        //Toast.makeText(getActivity(), contrato , Toast.LENGTH_SHORT).show();

        conexion.getDatabaseReference().child("Contratos").child(contrato).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds=  snapshot;

                if(ds.exists()){

                    String keyVehiculo = ds.child("vehiculo").child("0").getValue().toString();
                    obtenerDatosVehiculo(keyVehiculo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void obtenerDatosVehiculo(String keyVehiculo){

        conexion.getDatabaseReference().child("vehiculos").child(keyVehiculo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds =  snapshot;

                if(ds.exists()){
                    vehiculo.setModelo(  ds.child("modelo").getValue().toString() );
                    vehiculo.setPlaca( ds.child("placa").getValue().toString()  );
                    vehiculo.setColor( ds.child("color").getValue().toString());
                    vehiculo.setAnio( ds.child("anio").getValue().toString() );


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(getActivity()).load(ds.child("imagenes").child("url").getValue().toString()).apply(options).into(imgVehiculo);
                }

                edtPlaca.setText( vehiculo.getPlaca()  );
                edtAnio.setText( vehiculo.getAnio());
                edtColor.setText(  vehiculo.getColor()  );
                edtModelo.setText( vehiculo.getModelo());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}
