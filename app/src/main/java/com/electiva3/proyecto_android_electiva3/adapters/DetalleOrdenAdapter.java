package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.DetalleOrden;

import java.util.ArrayList;

public class DetalleOrdenAdapter extends RecyclerView.Adapter<DetalleOrdenAdapter.MyViewHolder>  implements  View.OnClickListener   {


    private LayoutInflater inflater;
    private Context context;
    private ArrayList<DetalleOrden>  detallesOrden;
    private View.OnClickListener listener;

    public DetalleOrdenAdapter(Context context,ArrayList<DetalleOrden> detallesOrden) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.detallesOrden  =  detallesOrden;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imgIcon.setImageResource(R.drawable.ic_check);
        holder.tvTitulo.setText(  detallesOrden.get(position).getNombreServicio()   );
        holder.tvDetalle.setText(  detallesOrden.get(position).getEstado() );
        holder.tvEstado.setText(  "$"+detallesOrden.get(position).getPrecio());
    }

    @Override
    public int getItemCount() {
        return detallesOrden.size();
    }

    public void SetOnClickListener(View.OnClickListener Listener)
    {
        this.listener = Listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)
        {
            listener.onClick(v);
        }
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

