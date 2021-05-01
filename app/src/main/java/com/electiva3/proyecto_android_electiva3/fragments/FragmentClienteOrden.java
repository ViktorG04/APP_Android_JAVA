package com.electiva3.proyecto_android_electiva3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import kotlinx.coroutines.flow.internal.NullSurrogateKt;

public class FragmentClienteOrden extends Fragment {

    private Usuario cliente;
    private Conexion conexion;
    private EditText edtCliente,
            edtCorreo,
            edtPlan,
            edtTelefono,
            edtActivacion,
            edtVencimiento;

    private String keyOrden;
    private String keyCliente;
    private String keyContrato;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_cliente_orden , container , false);


        cliente   =  new Usuario();
        conexion  =  new Conexion();
        conexion.inicializarFirabase( getActivity() );


        keyOrden = getArguments().getString("key");
        keyCliente  =  getArguments().getString("cliente");
        keyContrato =  getArguments().getString( "contrato");

        edtCliente  =  view.findViewById( R.id.edtCliente   );
        edtCorreo  =  view.findViewById(R.id.edtCorreo   );
        edtPlan  =  view.findViewById( R.id.edtPlan );
        edtTelefono  =  view.findViewById( R.id.edtTelefono);
        edtActivacion  =  view.findViewById( R.id.edtActivacion   );
        edtVencimiento  =  view.findViewById(  R.id.edtVencimiento );


        obtenerInformacionCliente();
        obtenerDatosContrato();

        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    private void obtenerInformacionCliente(){

        conexion.getDatabaseReference().child("usuarios").child(keyCliente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot  ds  = snapshot;

                if(snapshot.exists()){
                    cliente.setNombre( ds.child("nombre").getValue().toString()   );
                    cliente.setCorreo( ds.child("correo").getValue().toString()  );
                    cliente.setTelefono( ds.child("telefono").getValue().toString() );
                }
                edtCliente.setText( cliente.getNombre()  );
                edtCorreo.setText(  cliente.getCorreo()  );
                edtTelefono.setText(  cliente.getTelefono() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void obtenerDatosContrato(){

        conexion.getDatabaseReference().child("Contratos").child(keyContrato).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds  =  snapshot;

                if(ds.exists()){
                    edtPlan.setText( ds.child("plan").child("1").getValue().toString()  );
                    edtVencimiento.setText( ds.child("fechaVencimiento").getValue().toString()  );
                    edtActivacion.setText(  ds.child("fechaActivacion").getValue().toString()  );

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
