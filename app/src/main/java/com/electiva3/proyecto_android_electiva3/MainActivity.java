package com.electiva3.proyecto_android_electiva3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.MyServiceMessagingService;
import com.electiva3.proyecto_android_electiva3.entities.UserLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity<CollectionReference> extends AppCompatActivity implements View.OnClickListener
{
    private EditText edtCorreo, edtContrasena, edtCorreo2;
    private TextView tvRecuperar;
    private ConstraintLayout vt2;
    private Button btnIngresar, btnRecuperar, btnCancelar;

    private String email;
    private String password;
    private ProgressDialog dialog;
    private FirebaseUser user;

    Conexion conexion = new Conexion();
    UserLogin userLogin = new UserLogin();
    MyServiceMessagingService mso = new MyServiceMessagingService();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        edtCorreo =  findViewById(R.id.edtCorreo);
        edtContrasena =  findViewById(R.id.edtContrasena);
        tvRecuperar = findViewById(R.id.tvRecuperar);
        btnIngresar = findViewById(R.id.btnIngresar);
        edtCorreo2 = findViewById(R.id.edtCorreo2);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnCancelar = findViewById(R.id.btnCancelar);
        vt2 = findViewById(R.id.vt2);

        dialog = new ProgressDialog(this);

        conexion.inicializarFirabase(this);

        userLogin.ConexionFirebase(conexion);
        mso.setConexion(conexion);

       btnIngresar.setOnClickListener(this);
       btnRecuperar.setOnClickListener(this);
       btnCancelar.setOnClickListener(this);
       tvRecuperar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnIngresar:
                login();
             break;

            case R.id.btnRecuperar:
                email = edtCorreo2.getText().toString();

                if(!email.isEmpty()) {
                    dialog.setMessage("Verificando...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    RecuperarPassword();
                }
                else {
                    edtCorreo2.setError("Campo requerido");
                }

                break;

            case R.id.tvRecuperar:
                edtCorreo.setText("");
                edtContrasena.setText("");
                edtCorreo.setVisibility(View.INVISIBLE);
                edtContrasena.setVisibility(View.INVISIBLE);
                btnIngresar.setVisibility(View.INVISIBLE);
                vt2.setVisibility(View.VISIBLE);
                break;

            case R.id.btnCancelar:
                edtCorreo2.setText("");
                visible();
                break;
        }
    }

    private void login(){

        email =  edtCorreo.getText().toString();
        password =  edtContrasena.getText().toString();

        if(email.isEmpty()){
            edtCorreo.setError("Campo requerido");
        }else  if(password.isEmpty()){
            edtContrasena.setError("Campo requerido");
        }else{
            conexion.getAuth().signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        ValidarEstado();
                    }else{
                        Toast.makeText(getApplicationContext(), "Contraseña o usuario incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void ValidarEstado()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String usuario= user.getUid();
        final Query q =  conexion.getDatabaseReference().child("usuarios").child(usuario);
                q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String estado = snapshot.child("estado").getValue().toString();
                    if(!estado.equals("Inactivo")) {

                        String passDB = snapshot.child("password").getValue().toString();
                        if(!passDB.equals(password)) {
                            userLogin.QueryFirebase(q);
                            userLogin.UpdateUsuario(password);
                        }
                        //metodo para obtener el token del usuario que se esta logueando
                        mso.getToken(usuario);

                        Intent i = new Intent(getApplicationContext(), activity_principal.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        conexion.getAuth().signOut();
                        Toast.makeText(getApplicationContext(), "Usuario Inactivo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void RecuperarPassword()
    {
        conexion.getAuth().setLanguageCode("es");
        conexion.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Revisa la bandeja de entrada de tu correo", Toast.LENGTH_SHORT).show();
                    visible();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Verifica el correo ingresado!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public void visible()
    {
        edtCorreo2.setText("");
        edtCorreo.setVisibility(View.VISIBLE);
        edtContrasena.setVisibility(View.VISIBLE);
        btnIngresar.setVisibility(View.VISIBLE);
        vt2.setVisibility(View.INVISIBLE);
    }

    //Si el usuario ya esta logueado y cierra la app valida nuevamente y lo regresa a la pantalla de menu siempre
    // que no haya finalizado la sesion en firebase
    @Override
    protected void onStart() {
        super.onStart();
        if(conexion.getAuth().getCurrentUser() != null)
        {
            Intent i = new Intent(getApplicationContext(), activity_principal.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
