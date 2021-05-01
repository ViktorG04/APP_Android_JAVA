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
import com.electiva3.proyecto_android_electiva3.entities.Horario;
import java.util.ArrayList;

public class HorariosAdapter extends RecyclerView.Adapter<HorariosAdapter.MyViewHolder>  implements  View.OnClickListener {



    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Horario> horarios;
    private View.OnClickListener listener;


    public HorariosAdapter(Context context   , ArrayList<Horario> horarios){
        inflater =  LayoutInflater.from(context);
        this.context  = context;
        this.horarios  = horarios;
    }

    @NonNull
    @Override
    public HorariosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        HorariosAdapter.MyViewHolder holder = new HorariosAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }



    @Override
    public void onBindViewHolder(HorariosAdapter.MyViewHolder holder, int position)
    {
        //String p = String.valueOf(horarios.get(position).getNumeroContrato());
        //String c = String.valueOf(horarios.get(position).getCostoTotal());
        holder.imgIcon.setImageResource(R.drawable.ic_access_time_black_24dp);
        holder.tvTitulo.setText("");
        holder.tvDetalle.setText( horarios.get(position).getHora());
        holder.tvEstado.setText( null);
    }


    @Override
    public int getItemCount() {
        return horarios.size();
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

    class MyViewHolder extends RecyclerView.ViewHolder{


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
