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

import java.util.List;


public class EstadoAdapter extends ArrayAdapter<String>{
    private Context context;
    private List<String> estadosList;

    public EstadoAdapter(@NonNull Context context, int resource, @NonNull List<String> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.estadosList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(this.context).inflate(R.layout.custom_simple_spinner_item, null);
        }


        TextView texto = view.findViewById(R.id.customSpinnerItem);

        texto.setText(  estadosList.get(position) );

        return view;
    }
}
