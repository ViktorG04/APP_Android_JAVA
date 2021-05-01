package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import java.util.ArrayList;

public class MarcaSpinnerAdapter  extends ArrayAdapter {


    private  Context context;
    private ArrayList<Marca> marcas;

    public MarcaSpinnerAdapter(@NonNull Context context, int resource, ArrayList<Marca> marcas) {
        super(context , resource , marcas);
        this.context =  context;
        this.marcas =  marcas;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view  =  convertView;

        if(view==null){
            view = LayoutInflater.from(  this.context ).inflate(R.layout.custom_simple_spinner_item,  null);
        }

        TextView texto  = view.findViewById(R.id.customSpinnerItem);
        texto.setText(  marcas.get(position).getMarca()  );
        return super.getView(position, convertView, parent);
    }
}
