package com.electiva3.proyecto_android_electiva3.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Marca;
import com.electiva3.proyecto_android_electiva3.flujoMarcas.actualizar_marca;

import java.util.ArrayList;

public class MarcasAdapter  extends RecyclerView.Adapter< MarcasAdapter.MyViewHolder> implements View.OnClickListener {


    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Marca> marcas;
    private View.OnClickListener listener;

    public MarcasAdapter(Context context ,  ArrayList<Marca> marcas){
        inflater  =  LayoutInflater.from(context);
        this.context =  context;
        this.marcas =  marcas;
    }

    @Override
    public MarcasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        MarcasAdapter.MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MarcasAdapter.MyViewHolder holder, final int position) {
        holder.imgIcon.setImageResource(R.drawable.ic_check);
        holder.tvTitulo.setText(  marcas.get(position).getMarca());
        holder.tvDetalle.setText(  "" );
        holder.tvEstado.setText(  marcas.get(position).getEstado()  );

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String key =  marcas.get(position).getKey();
                Intent actualizarMarca =  new Intent( context   , actualizar_marca.class     );
                actualizarMarca.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                actualizarMarca.putExtra("keyMarca" , key   );
                context.startActivity(actualizarMarca);
                //((Activity)context).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return marcas.size();
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
