package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Plan;
import com.electiva3.proyecto_android_electiva3.flujoPlan.activity_actualizar_plan;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> implements View.OnClickListener
{
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Plan> planes;
    private View.OnClickListener listener;

    public PlanAdapter(Context context, ArrayList<Plan> planes) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.planes = planes;
    }

    @NonNull
    @Override
    public PlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_lista, parent, false);
        PlanAdapter.MyViewHolder holder = new PlanAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.MyViewHolder holder, int position) {
        holder.imgIcon.setImageResource(R.drawable.ic_document);
        holder.tvTitulo.setText(  planes.get(position).getTipoPlan());
        holder.tvDetalle.setText("$"+ String.valueOf(planes.get(position).getCosto()));
        holder.tvEstado.setText(  planes.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return planes.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
