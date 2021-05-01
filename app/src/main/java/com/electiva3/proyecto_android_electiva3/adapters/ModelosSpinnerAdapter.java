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
import com.electiva3.proyecto_android_electiva3.entities.Modelo;
import java.util.ArrayList;

public class ModelosSpinnerAdapter extends ArrayAdapter {


    private Context context;
    private ArrayList<Modelo> modelos;


    public ModelosSpinnerAdapter(@NonNull Context context, int resource , ArrayList<Modelo> modelos) {
        super(context, resource, modelos);
        this.context = context;
        this.modelos = modelos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =  convertView;

        if(view==null){
            view = LayoutInflater.from(this.context).inflate(R.layout.custom_simple_spinner_item , null);
        }
        TextView text  =  view.findViewById(R.id.customSpinnerItem);

        text.setText(  modelos.get(position).getModelo());

        return super.getView(position, convertView, parent);
    }
}
