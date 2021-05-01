package com.electiva3.proyecto_android_electiva3.flujoVehiculo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.electiva3.proyecto_android_electiva3.flujoModelos.crear_modelo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class activity_actualizar_vehiculo extends AppCompatActivity
{
    private ImageView imvImagen;
    private EditText  edtPlaca, edtChasis, edtAnio, edtColor;
    private Spinner spnMarca, spnModelo, spnEstado;
    private Button btnActualizar, btnCancelar;
    private Conexion conexion;
    private Vehiculo vehiculo;
    private Imagen imagen;


    private DatabaseReference marcasReference;
    private DatabaseReference modelosReference;

    private ArrayList<Marca> marcas;
    private ArrayList<Modelo> modelos;
    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 1;
    private boolean setModelo =  false;
    private ArrayAdapter<String> estadosAdapter;

    private String[] estados  =  { "activo" , "inactivo" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_vehiculo);
        conexion  = new Conexion();
        conexion.inicializarFirabase(this);
        marcas  = new ArrayList<>();
        modelos  = new ArrayList<>();
        imagen  =  new Imagen();

        vehiculo =  new Vehiculo();


        String keyVehiculo  = getIntent().getStringExtra("keyVehiculo");
        vehiculo.setKey(keyVehiculo);


        storageReference = FirebaseStorage.getInstance().getReference();

        imvImagen = findViewById(R.id.imvImagen);
        edtPlaca = findViewById(R.id.edtPlaca);
        edtChasis = findViewById(R.id.edtChasis);
        edtAnio = findViewById(R.id.edtAnio);
        edtColor = findViewById(R.id.edtColor);
        spnMarca = findViewById(R.id.spnMarca);
        spnModelo = findViewById(R.id.spnModelo);
        spnEstado  =  findViewById(R.id.spnEstado);

        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vehiculos = new Intent(getApplicationContext() ,   activity_lista_vehiculos.class);
                startActivity(vehiculos);
                finish();
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


        spnMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarModelos(   marcas.get(position).getKey()  );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateVehiculo();
            }
        });

        cargarMarcas();
        cargarEstados();
        cargarDatosVehiculo(keyVehiculo);
        cargarImagenVehiculo(keyVehiculo);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT  &&  resultCode == RESULT_OK){
            Uri uri = data.getData();
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
                    guardarFotoVehiculo( stringUrl );
                    Toast.makeText(activity_actualizar_vehiculo.this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void guardarFotoVehiculo(String url ){
        Imagen imagen  = new Imagen();
        imagen.setUrl(url);
        conexion.getDatabaseReference().child("vehiculos").child(vehiculo.getKey()).child("imagenes").setValue(imagen);
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

                    if(!setModelo){
                        seleccionarModelo( vehiculo.getKeyModelo()  );
                        setModelo =  true;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        Marca marca = new Marca();
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


    public void cargarEstados(){
        estadosAdapter  = new ArrayAdapter<String>(getApplicationContext() , R.layout.support_simple_spinner_dropdown_item , estados  );
        spnEstado.setAdapter(estadosAdapter);
    }


    public void cargarImagenVehiculo(String keyVehiculo){

        final DatabaseReference imagenes  =  conexion.getDatabaseReference().child("vehiculos").child(keyVehiculo).child("imagenes");


        imagenes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds  = snapshot;

                if(ds.exists()){
                    imagen.setUrl(  ds.child("url").getValue().toString()   );

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(getApplicationContext()).load(imagen.getUrl()).apply(options).into(imvImagen);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void cargarDatosVehiculo(String keyVehiculo){


        final DatabaseReference vehiculoInfo  =  conexion.getDatabaseReference().child("vehiculos").child(keyVehiculo);

        vehiculoInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot ds  = snapshot;

                if(ds.exists()){

                    vehiculo.setKey( ds.getKey() );
                    vehiculo.setColor( ds.child("color").getValue().toString()  );
                    vehiculo.setPlaca(  ds.child("placa").getValue().toString()  );
                    vehiculo.setNumChasis( ds.child("numChasis").getValue().toString()   );
                    vehiculo.setKeyMarca(  ds.child("keyMarca").getValue().toString()   );
                    vehiculo.setKeyModelo( ds.child("keyModelo").getValue().toString());
                    vehiculo.setMarca(ds.child("marca").getValue().toString());
                    vehiculo.setModelo(ds.child("modelo").getValue().toString());
                    vehiculo.setAnio( ds.child("anio").getValue().toString()  );
                    vehiculo.setEstado( ds.child("estado").getValue().toString());
                    vehiculo.setFechaRegistro(   ds.child("fechaRegistro").toString() );

                    edtColor.setText(vehiculo.getColor());
                    edtPlaca.setText(vehiculo.getPlaca());
                    edtChasis.setText(vehiculo.getNumChasis());
                    edtAnio.setText(vehiculo.getAnio());
                    seleccionarMarca( vehiculo.getKeyMarca() );
                    seleccionarEstado(vehiculo.getEstado());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public int findIndexMarca(String keyMarca){
        int indexMarca = -1;
        for(  int i=0  ;  i  < marcas.size() ; i++ ){
            if(marcas.get(i).getKey().equals( keyMarca )){
                indexMarca = i;
                break;
            }
        }
        return indexMarca;
    }

    public int findIndexModelo(String keyModelo){
        int indexModelo = -1;
        for(int i=0; i < modelos.size(); i++){
            if(modelos.get(i).getKey().equals(keyModelo)){
                indexModelo = i;
                break;
            }
        }
        return indexModelo;
    }


    public void seleccionarMarca(String keyMarca ){
        int indexMarca  =  findIndexMarca(keyMarca);
        spnMarca.setSelection(indexMarca);
    }

    public void seleccionarModelo(String keyModelo){
        int indexModelo = findIndexModelo(keyModelo);
        spnModelo.setSelection(indexModelo);
    }

    public void seleccionarEstado(String estado){
        int spinnerPosition = estadosAdapter.getPosition(estado);
        spnEstado.setSelection(spinnerPosition);
    }

    public void UpdateVehiculo(){

        String placa = edtPlaca.getText().toString();
        String numChasis = edtChasis.getText().toString();
        String anio =  edtAnio.getText().toString();
        String color  =  edtColor.getText().toString();

        if(placa.isEmpty()){
            edtPlaca.setError("El campo placa es requerido!");
        }else if(numChasis.isEmpty()){
            edtChasis.setError("El campo número chasis es requerido!");
        }else if(anio.isEmpty()){
            edtAnio.setError("El campo año es requerido!");
        }else if(color.isEmpty()){
            edtColor.setError("El campo color es requerido!");
        }else{

            String estado = estados[spnEstado.getSelectedItemPosition()];;
            String marca  = marcas.get(spnMarca.getSelectedItemPosition()).getMarca();
            String keyMarca  = marcas.get(spnMarca.getSelectedItemPosition()).getKey();
            String modelo = modelos.get(  spnModelo.getSelectedItemPosition() ).getModelo();
            String keyModelo =  modelos.get(  spnModelo.getSelectedItemPosition()  ).getKey();

            vehiculo.setPlaca(placa);
            vehiculo.setNumChasis(numChasis);
            vehiculo.setAnio(anio);
            vehiculo.setColor(color);
            vehiculo.setKeyMarca(keyMarca);
            vehiculo.setKeyModelo(keyModelo);
            vehiculo.setMarca( marca );
            vehiculo.setEstado(estado);
            vehiculo.setModelo(modelo);

            vehiculo.UpdateVehiculo();
            conexion.getDatabaseReference().child("vehiculos").child( vehiculo.getKey() ).updateChildren(vehiculo.getVehiculoMap()  );

            Toast.makeText(activity_actualizar_vehiculo.this, "Datos actualizados exitosamente", Toast.LENGTH_SHORT).show();

            Intent intent= new Intent(getApplicationContext() , activity_lista_marcas.class);
            startActivity(intent);
            finish();
        }
    }


}
