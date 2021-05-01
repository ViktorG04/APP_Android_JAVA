package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Vehiculo;
import com.electiva3.proyecto_android_electiva3.flujoVehiculo.activity_actualizar_vehiculo;

import java.util.ArrayList;

public class VehiculosAdapter extends RecyclerView.Adapter<VehiculosAdapter.MyViewHolder>  implements View.OnClickListener {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Vehiculo> vehiculos;
    private View.OnClickListener listener;

    public VehiculosAdapter(Context context , ArrayList<Vehiculo> vehiculos){
            inflater = LayoutInflater.from(context);
            this.context =  context;
            this.vehiculos =  vehiculos;
    }


    @Override
    public VehiculosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        VehiculosAdapter.MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(VehiculosAdapter.MyViewHolder holder, final int position) {
        holder.imgIcon.setImageResource(R.drawable.ic_car);
        holder.tvTitulo.setText(  vehiculos.get(position).getMarca()+ "  - " +vehiculos.get(position).getModelo() );
        holder.tvDetalle.setText(  vehiculos.get(position).getPlaca() );
        holder.tvEstado.setText(  vehiculos.get(position).getAnio()  );


        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String key  = vehiculos.get(position).getKey();
                Intent detalleVehiculo  =  new Intent(context  , activity_actualizar_vehiculo.class );
                detalleVehiculo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detalleVehiculo.putExtra("keyVehiculo" , key);
                context.startActivity(detalleVehiculo);
                //((Activity)context).finish();

            }
        });
    }


    @Override
    public int getItemCount() {
        return vehiculos.size();
    }

    @Override
    public void onClick(View v)
    {
        if(listener!=null)
        {
            listener.onClick(v);
        }
    }

    public void SetOnClickListener(View.OnClickListener Listener)
    {
        this.listener = Listener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgIcon;
        TextView tvTitulo;
        TextView tvDetalle;
        TextView tvEstado;


        public MyViewHolder(View itemView) {
            super(itemView);
            //serial_number = (TextView)itemView.findViewById(R.id.serialNo_CL);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDetalle =  itemView.findViewById(R.id.tvDetalle);
            tvEstado =  itemView.findViewById(R.id.tvEstado);
        }
    }
}
