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
import com.electiva3.proyecto_android_electiva3.entities.Vehiculo;

import java.util.List;

public class VehiculoListAdapter extends ArrayAdapter<Vehiculo>
{
    private List<Vehiculo> vehiculosList;
    private Context context;

    public VehiculoListAdapter(@NonNull Context context, int resource, @NonNull List<Vehiculo> objects) {
        super(context, resource, objects);
        this.context =  context;
        this.vehiculosList =  objects;
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

        Vehiculo vehiculo = vehiculosList.get(position);

        TextView texto =   view.findViewById(R.id.nombreServicio);
        texto.setText( vehiculo.getPlaca()+"   -   "+ vehiculo.getMarca());

        return view;
    }
}
