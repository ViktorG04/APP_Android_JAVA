package com.electiva3.proyecto_android_electiva3.flujoUsuario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.RolUsuarioAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.GeneradorPassword;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class activity_new_usuario extends AppCompatActivity
{

    private EditText edtNombre, edtDui, edtNit, edtLicencia, edtTelefono, edtDireccion, edtCorreo;
    private Button btnAgregar, btnCancelar;
    private Spinner spnRol;
    private AlertDialog.Builder builder;
    private String emailLogin;
    private String passLogin;

    private String title="Agregar Nuevo Usuario";

    private ArrayList<String> roleslist = new ArrayList<>();

    Conexion conexion = new Conexion();
    Usuario usuario = new Usuario();
    GeneradorPassword pass = new GeneradorPassword();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_usuario);

        getSupportActionBar().setTitle(title);

        //inicializando conexion con firebase
        conexion.inicializarFirabase(this);


        edtNombre = findViewById(R.id.edtNombre);
        edtDui  = findViewById(R.id.edtDui);
        edtNit = findViewById(R.id.edtNit);
        edtLicencia = findViewById(R.id.edtLicencia);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);
        spnRol =  findViewById(R.id.spnRol);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnCancelar = findViewById(R.id.btnCancelar);

        //obteniendo datos de admin
        emailLogin = getIntent().getStringExtra("email");
        passLogin = getIntent().getStringExtra("pass");

        //llenando Spinner
        Roles();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usuarios = new Intent(getApplicationContext() ,   activity_lista_usuarios.class);
                startActivity(usuarios);
                finish();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String nombre = edtNombre.getText().toString();
                String dui = edtDui.getText().toString();
                String nit = edtNit.getText().toString();
                String licencia = edtLicencia.getText().toString();
                String correo = edtCorreo.getText().toString();
                String telefono = edtTelefono.getText().toString();
                String direccion = edtDireccion.getText().toString();
                String rol = spnRol.getSelectedItem().toString();

                if(nombre.isEmpty()) { edtNombre.setError("Campo Requerido");
                }
                else if(dui.isEmpty()) { edtDui.setError("Campo Requerido");
                }
                else if(nit.isEmpty()) { edtNit.setError("Campo Requerido");
                }
                else if(licencia.isEmpty()) { edtLicencia.setError("Campo Requerido");
                }
                else if(correo.isEmpty()) { edtCorreo.setError("Campo Requerido");
                }
                else if(telefono.isEmpty()) { edtTelefono.setError("Campo Requerido");
                }
                else if(direccion.isEmpty()) { edtDireccion.setError("Campo Requerido");
                }
                else if(rol.isEmpty()){
                    Toast.makeText(getApplicationContext(), "la variable esta vacia", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    usuario.setNombre(nombre);
                    usuario.setDui(dui);
                    usuario.setNit(nit);
                    usuario.setLicencia(licencia);
                    usuario.setCorreo(correo);
                    usuario.setPassword(pass.getPassword());
                    usuario.setTelefono(telefono);
                    usuario.setDireccion(direccion);
                    usuario.setRol(rol);
                    usuario.setEstado("Activo");

                    //primero se registra el correo y password, si la accion es exito se registra en database
                    conexion.getAuth().createUserWithEmailAndPassword(usuario.getCorreo(), usuario.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //obteniendo el ID del usuario registrado
                                String key = conexion.getAuth().getCurrentUser().getUid();

                                // cerrando sesion
                                conexion.getAuth().signOut();

                                //obteniendo la hora y fecha del mobile
                                usuario.setFechaRegistro(conexion.ObtenerHora());

                                //registrando Usuario
                                conexion.getDatabaseReference().child("usuarios").child(key).setValue(usuario);

                                builder.setMessage("Usuario: "+usuario.getCorreo()+"\nPassword: "+usuario.getPassword()+"\nFecha Creacion: "+usuario.getFechaRegistro());
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No se Puede registrar el correo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        builder= new AlertDialog.Builder(this);
        builder.setTitle("Usuario creado exitosamente!!");
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Reautenticar();
            }
        });

    }

    public void Roles()
    {
        conexion.getDatabaseReference().child("roles").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    roleslist.clear();

                    for (DataSnapshot ds : snapshot.getChildren())
                    {

                        String rol = Objects.requireNonNull(ds.child("rol").getValue()).toString();
                        roleslist.add(rol);
                    }
                    RolUsuarioAdapter rolAdapter = new RolUsuarioAdapter(getApplicationContext() , R.layout.custom_simple_spinner_item, roleslist);
                    spnRol.setAdapter(rolAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void Reautenticar()
    {
        conexion.getAuth().signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent intent= new Intent(getApplicationContext() , activity_lista_usuarios.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext() , activity_lista_usuarios.class);
        startActivity(intent);
        finish();
    }

}