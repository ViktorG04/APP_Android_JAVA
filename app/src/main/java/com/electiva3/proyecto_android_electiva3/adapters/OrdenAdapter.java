package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.detalle_orden;
import com.electiva3.proyecto_android_electiva3.entities.DetalleOrden;
import com.electiva3.proyecto_android_electiva3.entities.Orden;

import java.util.ArrayList;

public class OrdenAdapter extends RecyclerView.Adapter<OrdenAdapter.MyViewHolder>   {


    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Orden>  ordenes;

    public OrdenAdapter(Context context,ArrayList<Orden> ordenes) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.ordenes  =  ordenes;
    }

    @Override
    public OrdenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        OrdenAdapter.MyViewHolder holder = new OrdenAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(OrdenAdapter.MyViewHolder holder, final int position) {
        holder.imgIcon.setImageResource(R.drawable.order);
        holder.tvTitulo.setText(  "#"+ordenes.get(position).getNumeroOrden()   );
        holder.tvDetalle.setText(  ordenes.get(position).getNombreCliente()  );
        holder.tvEstado.setText(  ordenes.get(position).getFecha()+"\n"+ordenes.get(position).getEstado() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalleOrden  =  new Intent(context  , detalle_orden.class );
                detalleOrden.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detalleOrden.putExtra("key" ,  ordenes.get(position).getKey()    );
                detalleOrden.putExtra("cliente" ,  ordenes.get(position).getCliente()    );
                detalleOrden.putExtra("contrato" ,  ordenes.get(position).getContrato()    );
                detalleOrden.putExtra("supervisor" ,  ordenes.get(position).getSupervisor()   );
                context.startActivity(detalleOrden);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordenes.size();
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
