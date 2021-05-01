package com.electiva3.proyecto_android_electiva3.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.activity_principal;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class PerfilFragment extends Fragment {

    private EditText edtNombre, edtCorreo, edtTelefono, edtPassword, edtConfirmPassword;
    private Button btnActualizarPerfil;
    private String id;
    private FirebaseUser user;

    Conexion conexion = new Conexion();
    Usuario usuario = new Usuario();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =inflater.inflate(R.layout.fragment_perfil, container, false);

        //inicializar firebase
        conexion.inicializarFirabase(getActivity());

        edtNombre = view.findViewById(R.id.edtNombre);
        edtCorreo = view.findViewById(R.id.edtCorreo);
        edtTelefono = view.findViewById(R.id.edtTelefono);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnActualizarPerfil = view.findViewById(R.id.btnActualizarPerfil);

        //obtener usuario logueado
        user = FirebaseAuth.getInstance().getCurrentUser();

        ObtenerDatos();

        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUsuario();
            }
        });

        return view;
    }

    public void ObtenerDatos()
    {
        //obtener ID del usuario
        id =  user.getUid();
        conexion.getDatabaseReference().child("usuarios").child(id).addValueEventListener(new ValueEventListener()
        {
            DataSnapshot ds;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ds = snapshot;
                if (ds.exists())
                {
                    //llenando el objeto
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

                    //mostrando en vista
                    edtNombre.setText(usuario.getNombre());
                    edtCorreo.setText(usuario.getCorreo());
                    edtTelefono.setText(usuario.getTelefono());
                    edtPassword.setText(usuario.getPassword());
                    edtConfirmPassword.setText(usuario.getPassword());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void UpdateUsuario()
    {
        String telefono = edtTelefono.getText().toString();
        String pass = edtPassword.getText().toString();
        String confirmar = edtConfirmPassword.getText().toString();

        if(telefono.isEmpty()){
            edtTelefono.setError("El campo telefono es requerido");
        }
        else if(pass.isEmpty()){
            edtPassword.setError("El campo de contraseña es requerido");
        }
        else if(confirmar.isEmpty()){
            edtConfirmPassword.setError("Debes confirmar tu contraseña");
        }
        else if(!confirmar.equals(pass)){
            Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!pass.equals(usuario.getPassword())) {
                UpdatePassword2(pass);
            }
            else if(!telefono.equals(usuario.getTelefono()))
            {
                usuario.setTelefono(telefono);
            }
            usuario.UpdateDatos(pass);
            conexion.getDatabaseReference().child("usuarios").child(id).updateChildren(usuario.getUsuarioMap());
            Toast.makeText(getActivity(), "Datos Actualizados", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), activity_principal.class);
            startActivity(intent);
        }
    }

    public void UpdatePassword2(final String nuevopass)
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //pasamos el parametro nuevopass por el metodo update
            user.updatePassword(nuevopass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }
        else {
                Toast.makeText(getActivity(), "user is null", Toast.LENGTH_LONG).show();
        }

    }



}
