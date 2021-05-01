package com.electiva3.proyecto_android_electiva3.flujoVehiculo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.adapters.MarcaSpinnerAdapter;
import com.electiva3.proyecto_android_electiva3.adapters.ModelosSpinnerAdapter;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Imagen;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import com.electiva3.proyecto_android_electiva3.entities.Modelo;
import com.electiva3.proyecto_android_electiva3.entities.Vehiculo;
import com.electiva3.proyecto_android_electiva3.flujoMarcas.activity_lista_marcas;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class activity_new_vehiculo extends AppCompatActivity implements View.OnClickListener
{

    private EditText  edtPlaca, edtChasis, edtAnio, edtColor;
    private static final int GALLERY_INTENT = 1;
    private Spinner spnMarca, spnModelo;
    private ConstraintLayout vt2;
    private ListView lvlistar;
    private Button btnCrear, btnCancelar, btnCancelar2;
    private ArrayList<Marca> marcas;
    private ArrayList<Modelo> modelos;
    private DatabaseReference marcasReference;
    private DatabaseReference modelosReference;
    private StorageReference storageReference;
    private ImageView imvImagen;

    private Conexion conexion;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vehiculo);

        conexion =  new Conexion();
        conexion.inicializarFirabase(this);
        marcas = new ArrayList<>();
        modelos =  new ArrayList<>();



        imvImagen =  findViewById(R.id.imvImagen);
        edtPlaca = findViewById(R.id.edtPlaca);
        edtChasis = findViewById(R.id.edtChasis);
        edtAnio = findViewById(R.id.edtAnio);
        edtColor = findViewById(R.id.edtColor);
        spnMarca = findViewById(R.id.spnMarca);
        spnModelo = findViewById(R.id.spnModelo);
        btnCrear = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar2 = findViewById(R.id.btnCancelar2);
        vt2 = findViewById(R.id.vt2);
        lvlistar = findViewById(R.id.lvlistar);

        storageReference = FirebaseStorage.getInstance().getReference();


        spnMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarModelos(   marcas.get(position).getKey()  );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imvImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =  new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , GALLERY_INTENT);



            }
        });


        btnCancelar.setOnClickListener(this);
        btnCancelar2.setOnClickListener(this);
        btnCrear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               crearVehiculo();
            }
        });

        cargarMarcas();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnCancelar:
                Intent vehiculos = new Intent(getApplicationContext() ,   activity_lista_vehiculos.class);
                startActivity(vehiculos);
                finish();
                break;
            case R.id.btnCancelar2:
                vt2.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT  &&  resultCode == RESULT_OK){
            uri = data.getData();
            try {
                Bitmap bitmap  = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver() , uri);
                imvImagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void guardarFotoVehiculo(final String keyVehiculo){

        StorageReference filePath  =  storageReference.child("vehiculos").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> descargarFoto = taskSnapshot.getStorage().getDownloadUrl();

                while(!descargarFoto.isComplete());
                Uri url  = descargarFoto.getResult();

                String stringUrl  =url.toString();

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);
                Glide.with(getApplicationContext()).load(stringUrl).apply(options).into(imvImagen);

                Imagen imagen  = new Imagen();
                imagen.setUrl(stringUrl);
                conexion.getDatabaseReference().child("vehiculos").child(keyVehiculo).child("imagenes").setValue(imagen);

            }
        });
    }




    public  void  cargarMarcas(){

        marcasReference =  conexion.getDatabaseReference().child("marcas");
        marcasReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    marcas.clear();
                    for( DataSnapshot ds: snapshot.getChildren() ){
                        String nombreMarca  =   ds.child("marca").getValue().toString();
                        String keyMarca  = ds.getKey();
                        Marca  marca = new Marca();
                        marca.setKey(keyMarca);
                        marca.setMarca(nombreMarca);
                        marcas.add(marca);
                    }

                    MarcaSpinnerAdapter marcaSpinnerAdapter =  new MarcaSpinnerAdapter(getApplicationContext() ,  R.layout.custom_simple_spinner_item ,    marcas  );
                    spnMarca.setAdapter(marcaSpinnerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void cargarModelos(String keyMarca){


        modelos.clear();
        ModelosSpinnerAdapter modelosSpinnerAdapter =  new ModelosSpinnerAdapter( getApplicationContext() , R.layout.custom_simple_spinner_item ,  modelos );
        spnModelo.setAdapter( modelosSpinnerAdapter );

        modelosReference =  conexion.getDatabaseReference().child("modelos");


        modelosReference.getRef().orderByChild("keyMarca").equalTo(keyMarca).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot ds:  snapshot.getChildren()  ){

                     String keyModelo =  ds.getKey();
                     String nombreModelo = ds.child("modelo").getValue().toString();

                     Modelo modelo  =  new Modelo();

                     modelo.setKey(keyModelo);
                     modelo.setModelo(nombreModelo);

                     modelos.add(modelo);
                    }

                    ModelosSpinnerAdapter modelosSpinnerAdapter =  new ModelosSpinnerAdapter( getApplicationContext() , R.layout.custom_simple_spinner_item ,  modelos );
                    spnModelo.setAdapter( modelosSpinnerAdapter );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void crearVehiculo(){

        String placa = edtPlaca.getText().toString();
        String chasis = edtChasis.getText().toString();
        String anio = edtAnio.getText().toString();
        String color = edtColor.getText().toString();


        if(TextUtils.isEmpty(placa)) {
            edtPlaca.setError("Campo Requerido");
        }
        else if(TextUtils.isEmpty(chasis)) {
            edtChasis.setError("Campo Requerido");
        }
        else if(TextUtils.isEmpty(anio)) {
            edtAnio.setError("Campo Requerido");
        }
        else if(TextUtils.isEmpty(color)) {
            edtColor.setError("Campo Requerido");
        }else{

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String fecha =  dateFormat.format(date);

            String key = (UUID.randomUUID().toString());

            int indexMarca = spnMarca.getSelectedItemPosition();
            int indexModelo =  spnModelo.getSelectedItemPosition();

            Vehiculo vehiculo =  new Vehiculo();

            vehiculo.setMarca(marcas.get(indexMarca).getMarca());
            vehiculo.setKeyMarca(marcas.get(indexMarca).getKey());
            vehiculo.setModelo( modelos.get(indexModelo).getModelo());
            vehiculo.setKeyModelo( modelos.get(indexModelo).getKey());
            vehiculo.setPlaca(placa);
            vehiculo.setNumChasis( chasis);
            vehiculo.setAnio(anio);
            vehiculo.setColor(color);
            vehiculo.setFechaRegistro(fecha);
            vehiculo.setEstado("activo");

            conexion.getDatabaseReference().child("vehiculos").child(key).setValue(vehiculo);
            guardarFotoVehiculo(key);

            Toast.makeText(activity_new_vehiculo.this, "Datos ingresados exitosamente", Toast.LENGTH_SHORT).show();

            Intent intent= new Intent(getApplicationContext() , activity_lista_marcas.class);
            startActivity(intent);
            finish();

        }


    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext() , activity_lista_vehiculos.class);
        startActivity(intent);
        finish();
    }
}
