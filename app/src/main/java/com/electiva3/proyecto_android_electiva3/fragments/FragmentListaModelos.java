package com.electiva3.proyecto_android_electiva3.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.ModelosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Modelo;
import com.electiva3.proyecto_android_electiva3.flujoModelos.crear_modelo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListaModelos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaModelos extends Fragment {


    private RecyclerView rvListaModelos;
    private ArrayList<Modelo> modelos;
    private FloatingActionButton fabAgregarModelo;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListaModelos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListaModelos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListaModelos newInstance(String param1, String param2) {
        FragmentListaModelos fragment = new FragmentListaModelos();
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
        View view = inflater.inflate(R.layout.fragment_lista_modelos, container, false);

        modelos =  new ArrayList<>();

        rvListaModelos  = view.findViewById(R.id.rvListaModelos);
        fabAgregarModelo =  view.findViewById(R.id.fabAgregarModelo);


        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListaModelos.setLayoutManager(linearLayoutManager);
        final String key = getArguments().getString("key");
        listarModelosMarca(key);

        fabAgregarModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent( getActivity() , crear_modelo.class );
                intent.putExtra("keyMarca" , key);
                getActivity().startActivity(intent);

            }
        });

        return view;
    }



    public void listarModelosMarca(String keyMarca){


        FirebaseDatabase.getInstance().getReference().child("modelos").getRef().orderByChild("keyMarca").equalTo(keyMarca).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    modelos.clear();

                    for( DataSnapshot ds: snapshot.getChildren()){

                        String key = ds.getKey();
                        String modelo = ds.child("modelo").getValue().toString();
                        String estado = ds.child("estado").getValue().toString();
                        String keyMarca =  ds.child("keyMarca").getValue().toString();
                        modelos.add(new Modelo(key , modelo , estado ,  keyMarca));

                    }

                    ModelosAdapter modelosAdapter =  new ModelosAdapter( getActivity() , modelos);
                    rvListaModelos.setAdapter(modelosAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}