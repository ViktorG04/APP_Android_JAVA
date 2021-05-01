package com.electiva3.proyecto_android_electiva3.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import com.electiva3.proyecto_android_electiva3.flujoMarcas.activity_lista_marcas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentActualizarMarca#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentActualizarMarca extends Fragment {


    private EditText  edtMarca , edtDescripcion , edtFechaCreacion;
    private ArrayAdapter estadosAdapter;
    private Conexion conexion = new Conexion();
    private Button btnActualizarMarca;
    private String[] estados  = { "activo" , "inactivo"};
    private Spinner spnEstado;
    private Marca marca;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentActualizarMarca() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentActualizarMarca.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentActualizarMarca newInstance(String param1, String param2) {
        FragmentActualizarMarca fragment = new FragmentActualizarMarca();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_actualizar_marca, container, false);

        // Inflate the layout for this fragment
        String key = getArguments().getString("key");
        marca =  new Marca();

        conexion.inicializarFirabase(getActivity());

        edtMarca =  view.findViewById(R.id.edtMarca);
        edtDescripcion =  view.findViewById(R.id.edtDescripcion);
        edtFechaCreacion =  view.findViewById(R.id.edtFechaCreacion);
        spnEstado =  view.findViewById(R.id.spnEstado);
        btnActualizarMarca =  view.findViewById(R.id.btnActualizarMarca);

        listarEstados();
        obtenerInformacionMarca(key);

        btnActualizarMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateMarca();
            }
        });

        return view;
    }

    public void UpdateMarca(){

        String nombreMarca =  edtMarca.getText().toString();
        String estado = estados[spnEstado.getSelectedItemPosition()];
        String descripcion =  edtDescripcion.getText().toString();

        //Validaciones

        if(nombreMarca.isEmpty()){
            edtMarca.setError("Campo de marca requerido!");
        }else if(descripcion.isEmpty()  ){
            edtDescripcion.setError("Campo de descripcion requerido!");
        }else{

            marca.setMarca(nombreMarca);
            marca.setEstado(estado);
            marca.setDescripcion(descripcion);
            marca.UpdateMarca();
            conexion.getDatabaseReference().child("marcas").child(marca.getKey()).updateChildren(marca.getMarcaMap());

            Toast.makeText( getActivity()   , "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

            Intent intent= new Intent(getActivity() , activity_lista_marcas.class);
            startActivity(intent);
            getActivity().finish();
        }
    }


    public void listarEstados(){
        estadosAdapter =  new ArrayAdapter( getActivity() ,     R.layout.support_simple_spinner_dropdown_item ,  estados   );
        spnEstado.setAdapter(estadosAdapter);
    }


    public void obtenerInformacionMarca(String keyMarca){

        final DatabaseReference marcas =     FirebaseDatabase.getInstance().getReference().child("marcas").child(keyMarca);

        marcas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds = snapshot;
                if (ds.exists())
                {

                    marca.setKey(ds.getKey());
                    marca.setMarca(   ds.child("marca").getValue().toString()  );
                    marca.setEstado(  ds.child("estado").getValue().toString()   );
                    marca.setFechaCreacion(ds.child("fechaCreacion").getValue().toString());
                    marca.setDescripcion( ds.child("descripcion").getValue().toString()   );


                    int spinnerPosition = estadosAdapter.getPosition(marca.getEstado());
                    spnEstado.setSelection(spinnerPosition);
                    edtMarca.setText(marca.getMarca());
                    edtDescripcion.setText(marca.getDescripcion());
                    edtFechaCreacion.setText(marca.getFechaCreacion());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}