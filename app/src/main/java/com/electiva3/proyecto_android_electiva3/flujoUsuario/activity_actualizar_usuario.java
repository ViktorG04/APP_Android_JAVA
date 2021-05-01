package com.electiva3.proyecto_android_electiva3.flujoUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.EstadoAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.RolUsuarioAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class activity_actualizar_usuario extends AppCompatActivity
{
    private EditText edtNombre, edtDui, edtNit, edtLicencia, edtTelefono, edtDireccion, edtCorreo, edtContrasena, edtContrasena2;
    private Button btnActualizar, btnCancelar;
    private Spinner spnRol, spnEstado;

    private ArrayList<String> roleslist = new ArrayList<>();
    private ArrayList<String> estadoUsuariosList = new ArrayList<>();
    private String id;
    private String emailLogin;
    private String passLogin;
    private FirebaseUser user;
    Conexion conexion = new Conexion();
    Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_usuario);

        edtNombre = findViewById(R.id.edtNombre);
        edtDui  = findViewById(R.id.edtDui);
        edtNit = findViewById(R.id.edtNit);
        edtLicencia = findViewById(R.id.edtLicencia);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtContrasena = findViewById(R.id.edtContrasena);
        edtContrasena2 = findViewById(R.id.edtContrasena2);
        spnEstado = findViewById(R.id.spnEstado);
        spnRol =  findViewById(R.id.spnRol);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        //establecer la conexion
        conexion.inicializarFirabase(this);
        //recuperar el id del objeto
        id = getIntent().getStringExtra("id");
        emailLogin = getIntent().getStringExtra("email");
        passLogin = getIntent().getStringExtra("pass");

        //realizar consulta y mostrar los datos a partir del id recibido
        MostrarDatos();

        btnActualizar.setOnClickListener(new View.OnClickListener()
        {
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
                String password = edtContrasena.getText().toString();
                String password2 = edtContrasena2.getText().toString();
                String estadonuevo = spnEstado.getSelectedItem().toString();
                String rol = spnRol.getSelectedItem().toString();

                if(password.equals(password2))
                {
                    //metodo para comparar los valores que tiene el objeto de fire con los editText
                    //si se encontro un cambio el metodo llena el HaspMap sino lo deja null y se hace la validacion con el if
                    usuario.Comparar(nombre, dui, nit, licencia, correo, password, telefono, direccion, rol, estadonuevo);

                    if (usuario.getUsuarioMap().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "No hay cambios", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(!password.equals(usuario.getPassword()))
                        {
                            UpdatePassword(password);
                        }
                        else if(!correo.equals(usuario.getCorreo()))
                        {
                            UpdateCorreo(correo);
                        }

                        conexion.getDatabaseReference().child("usuarios").child(id).updateChildren(usuario.getUsuarioMap());
                    }
                    Intent usuarios = new Intent(getApplicationContext() ,   activity_lista_usuarios.class);
                    startActivity(usuarios);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Las Contraseñas no son Iguales", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usuarios = new Intent(getApplicationContext() ,   activity_lista_usuarios.class);
                startActivity(usuarios);
                finish();
            }
        });

    }

    public void MostrarDatos()
    {
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
                    edtDui.setText(usuario.getDui());
                    edtNit.setText(usuario.getNit());
                    edtLicencia.setText(usuario.getLicencia());
                    edtCorreo.setText(usuario.getCorreo());
                    edtTelefono.setText(usuario.getTelefono());
                    edtDireccion.setText(usuario.getDireccion());
                    edtContrasena.setText(usuario.getPassword());
                    edtContrasena2.setText(usuario.getPassword());
                    Roles();
                    Estados();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void Roles()
    {
        conexion.getDatabaseReference().child("roles").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    roleslist.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String rol = Objects.requireNonNull(ds.child("rol").getValue()).toString();
                        if(rol.equals(usuario.getRol()))
                        {
                            roleslist.add(rol);
                        }
                    }
                    CompararRoles();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void CompararRoles()
    {
        conexion.getDatabaseReference().child("roles").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String rol = Objects.requireNonNull(ds.child("rol").getValue()).toString();
                        if(!roleslist.contains(rol)) {
                            roleslist.add(rol);
                        }
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

    public void Estados()
    {
        conexion.getDatabaseReference().child("estados").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    estadoUsuariosList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String estado = Objects.requireNonNull(ds.child("estado").getValue()).toString();

                        if(estado.equals(usuario.getEstado())) {
                            estadoUsuariosList.add(estado);
                        }
                    }
                    CompararEstados();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void CompararEstados()
    {
        conexion.getDatabaseReference().child("estados").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String estado = Objects.requireNonNull(ds.child("estado").getValue()).toString();

                        if(estado.equals("Activo") || estado.equals("Inactivo")) {
                            if (!estadoUsuariosList.contains(estado)) {
                                estadoUsuariosList.add(estado);
                            }
                        }
                    }
                    EstadoAdapter estadoAdapter = new EstadoAdapter(getApplicationContext() , R.layout.custom_simple_spinner_item,estadoUsuariosList);
                    spnEstado.setAdapter(estadoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void UpdatePassword(final String nuevopass)
    {
        //primero logueamos el usuario
        conexion.getAuth().signInWithEmailAndPassword(usuario.getCorreo() , usuario.getPassword()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //despues obtenemos el user logueado para hacer el update
                             user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                //pasamos el parametro nuevopass por el metodo update
                                user.updatePassword(nuevopass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Password Actualizado", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "user is null", Toast.LENGTH_LONG).show();
                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "no se logueo", Toast.LENGTH_LONG).show();
                        }

                        conexion.getAuth().signOut();
                        Reautenticar();
                    }
                });
    }

    public void UpdateCorreo(final String nuevocorreo)
    {
        //primero logueamos el usuario
        conexion.getAuth().signInWithEmailAndPassword(usuario.getCorreo() , usuario.getPassword()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.updateEmail(nuevocorreo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()){
                                            Toast.makeText(getApplicationContext(), "Correo Actualizado", Toast.LENGTH_LONG).show();
                                        }
                                       else{
                                            Toast.makeText(getApplicationContext(), "Error al actualizar correo", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "user is null", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "no se logueo", Toast.LENGTH_LONG).show();
                        }
                        conexion.getAuth().signOut();
                        Reautenticar();
                    }
                });
    }

    public void Reautenticar() {
        conexion.getAuth().signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
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

