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
import com.electiva3.proyecto_android_electiva3.entities.Servicio;

import java.util.ArrayList;

public class ServiciosAdapter extends RecyclerView.Adapter<ServiciosAdapter.MyViewHolder> implements View.OnClickListener
{
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Servicio> servicios;
    private View.OnClickListener listener;

    public ServiciosAdapter(Context context, ArrayList<Servicio> servicios)
    {
        inflater =  LayoutInflater.from(context);
        this.context = context;
        this.servicios = servicios;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_lista, parent, false);
        ServiciosAdapter.MyViewHolder holder = new ServiciosAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imgIcon.setImageResource(R.drawable.ic_settings_black_24dp);
        holder.tvTitulo.setText(  servicios.get(position).getTitulo());
        holder.tvDetalle.setText( servicios.get(position).getDescripcion());
        holder.tvEstado.setText(  servicios.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    @Override
    public void onClick(View v) {
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

        public MyViewHolder(View itemView)
        {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDetalle =  itemView.findViewById(R.id.tvDetalle);
            tvEstado =  itemView.findViewById(R.id.tvEstado);
        }
    }
}
