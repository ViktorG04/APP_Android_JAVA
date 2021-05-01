package com.electiva3.proyecto_android_electiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.electiva3.proyecto_android_electiva3.fragments.FragmentClienteOrden;
import com.electiva3.proyecto_android_electiva3.fragments.FragmentOrdenOrden;
import com.electiva3.proyecto_android_electiva3.fragments.FragmentVehiculoOrden;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class detalle_orden extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private String key;
    private String cliente;
    private String contrato;
    private String supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_orden);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent =  getIntent();
        key = intent.getStringExtra("key");
        cliente =  intent.getStringExtra("cliente");
        contrato =  intent.getStringExtra("contrato");
        supervisor =  intent.getStringExtra("supervisor");



        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        Fragment selectedFragment =  new FragmentClienteOrden();


        selectedFragment.setArguments( getBundle() );

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , ( selectedFragment   ))
                .commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.page_1:
                            selectedFragment  = new FragmentClienteOrden();
                            break;

                        case R.id.page_2:
                            selectedFragment  =  new FragmentOrdenOrden();
                            break;

                        case R.id.page_3:
                            selectedFragment  =  new FragmentVehiculoOrden();
                            break;
                    }



                    selectedFragment.setArguments(getBundle());


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectedFragment)
                    .commit();

                    return true;
                }
            };

    private  Bundle getBundle(){
        Bundle  bundle =  new Bundle();
        bundle.putString( "key" ,  key    );  //Key de la orden
        bundle.putString("cliente" , cliente);
        bundle.putString("supervisor" , supervisor );
        bundle.putString("contrato" ,  contrato);
        return bundle;
    }
}
