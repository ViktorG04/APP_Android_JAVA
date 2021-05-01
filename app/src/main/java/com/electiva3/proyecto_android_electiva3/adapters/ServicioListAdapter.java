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
import com.electiva3.proyecto_android_electiva3.entities.Servicio;

import java.util.List;

public class ServicioListAdapter extends ArrayAdapter<Servicio> {

    private List<Servicio> serviciosList;
    private Context context;

    public ServicioListAdapter(@NonNull Context context, int resource, @NonNull List<Servicio> objects) {
        super(context, resource, objects);
        this.context =  context;
        this.serviciosList =  objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Genera el contenido visual en base al contenido del array list pasado como parametro
        View view =  convertView;

        if(view == null)
        {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_servicio , null);
        }

        Servicio servicio = serviciosList.get(position);

        TextView texto =   view.findViewById(R.id.nombreServicio);
        texto.setText( servicio.getDescripcion()+"   -   $"+ servicio.getCosto());

        return view;
    }
}
