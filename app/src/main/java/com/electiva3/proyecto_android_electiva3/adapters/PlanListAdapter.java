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
import com.electiva3.proyecto_android_electiva3.entities.Plan;

import java.util.List;

public class PlanListAdapter extends ArrayAdapter<Plan>
{
    private List<Plan> planesList;
    private Context context;

    public PlanListAdapter(@NonNull Context context, int resource, @NonNull List<Plan> objects) {
        super(context, resource, objects);
        this.context =  context;
        this.planesList =  objects;
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
        Plan plan = planesList.get(position);

        TextView texto =   view.findViewById(R.id.nombreServicio);
        texto.setText( plan.getTipoPlan()+"  -  $"+ plan.getCosto());

        return view;
    }
}
