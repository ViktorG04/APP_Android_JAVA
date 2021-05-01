package com.electiva3.proyecto_android_electiva3.flujoMarcas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.ModelosAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import com.electiva3.proyecto_android_electiva3.entities.Modelo;
import com.electiva3.proyecto_android_electiva3.fragments.FragmentActualizarMarca;
import com.electiva3.proyecto_android_electiva3.fragments.FragmentListaModelos;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class actualizar_marca extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager  viewPager;
    private ArrayAdapter estadosAdapter;
    private String[] estados  = { "activo" , "inactivo"};
    Conexion conexion = new Conexion();
    EditText edtMarca;
    Spinner spnModelo , spnEstado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_marca);



        tabLayout =  findViewById(R.id.tabLayout);
        viewPager =  findViewById(R.id.viewPager);
        edtMarca =  findViewById(R.id.edtMarca);
        spnModelo =  findViewById(R.id.spnModelo);
        spnEstado =  findViewById(R.id.spnEstado);
        //btnActualizarMarca = findViewById(R.id.btnActualizarMarca);

        //Conexion con firebase
        conexion.inicializarFirabase(this);

        //Inializar el array de xxx
        ArrayList<String> arrayList =  new ArrayList<>();

        //Agregar titulos en arrayList
        arrayList.add("Datos de marca");
        arrayList.add("Modelos");

        String keyMarca = getIntent().getStringExtra("keyMarca");

        //Prepara view pager
        prepareViewPager(viewPager , arrayList , keyMarca);
        //Setup with the view  pager
        tabLayout.setupWithViewPager(viewPager);
    }


    private void prepareViewPager(ViewPager viewPager , ArrayList<String>  arrayList ,  String key ){
        //Initialize main adapter
        MainAdapter adapter  = new MainAdapter(  getSupportFragmentManager()   );
        //Inicialize main fragment
        Fragment fragment = new FragmentActualizarMarca();
        for(  int i = 0 ; i < arrayList.size() ; i++   ){
            Bundle bundle =  new Bundle();
            bundle.putString("title" , arrayList.get(i)   );
            bundle.putString("key" , key );
            fragment.setArguments(bundle);
            adapter.addFragment(fragment , arrayList.get(i));
            fragment=  new FragmentListaModelos();
        }
        viewPager.setAdapter(adapter);
    }


    private class MainAdapter extends FragmentPagerAdapter {
        //Initialize array list

        ArrayList<String> arrayList =  new ArrayList<>();
        List<Fragment>  fragmentList = new ArrayList<>();


        //Create constructor
        public void addFragment(Fragment fragment , String title  ){

            //Add Title
            arrayList.add(title);

            //Add fragment
            fragmentList.add(fragment);

        }


        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //Return fragment position
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            //Return fragment List size
            return fragmentList.size();
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //Return array list  position
            return arrayList.get(position);
        }
    }
}