package com.electiva3.proyecto_android_electiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.electiva3.proyecto_android_electiva3.adapters.UsuariosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class activity_search_cliente extends AppCompatActivity {


    private DatabaseReference clientesReference;
    private RecyclerView rvClientes;
    private ArrayList<Usuario>  usuarios;
    private UsuariosAdapter usuariosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cliente);
        usuarios  =  new ArrayList<>();

        rvClientes =  findViewById(R.id.rvClientes);


        //Layout Manager del cliente
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(  getApplicationContext() );
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClientes.setLayoutManager(  linearLayoutManager  );




        FirebaseDatabase.getInstance().getReference().child("usuarios").getRef().orderByChild("rol").equalTo("Cliente").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               if(  snapshot.exists() ){
                   usuarios.clear();

                   for(DataSnapshot ds: snapshot.getChildren()){

                       String key  = ds.getKey();
                       String nombre  =  ds.child("nombre").getValue().toString();
                       String dui =  ds.child("dui").getValue().toString();
                       String correo =  ds.child("correo").getValue().toString();

                       Usuario usuario =  new Usuario();
                       usuario.setKey(key);
                       usuario.setNombre(nombre);
                       usuario.setDui(dui);
                       usuario.setCorreo(correo);
                       usuarios.add(usuario);
                   }

                   usuariosAdapter =  new UsuariosAdapter( getApplicationContext() , usuarios   );
                   rvClientes.setAdapter(  usuariosAdapter );


                   usuariosAdapter.SetOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                            String keyCliente  = usuarios.get(rvClientes.getChildAdapterPosition(v)).getKey();
                            String nombreCliente  =   usuarios.get(rvClientes.getChildAdapterPosition(v)).getNombre();


                           Intent realizarReserva = new Intent(  getApplicationContext() , activity_agregar_reserva.class  );
                           realizarReserva.putExtra("keyCliente" ,  keyCliente  );
                           realizarReserva.putExtra(  "nombreCliente" , nombreCliente  );
                           startActivity(realizarReserva);
                       }
                   });

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu ,  menu);
        MenuItem item  = menu.findItem(R.id.action_search);
        SearchView searchView =  (SearchView)  item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usuariosAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}