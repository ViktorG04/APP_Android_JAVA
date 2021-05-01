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
import com.electiva3.proyecto_android_electiva3.entities.Contrato;

import java.util.ArrayList;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.MyViewHolder> implements View.OnClickListener
{
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Contrato> contratos;
    private View.OnClickListener listener;

    public ContratoAdapter(Context context, ArrayList<Contrato> contratos) {
        inflater =  LayoutInflater.from(context);
        this.context = context;
        this.contratos = contratos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        ContratoAdapter.MyViewHolder holder = new ContratoAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContratoAdapter.MyViewHolder holder, int position)
    {
        String p = String.valueOf(contratos.get(position).getNumeroContrato());
        String c = String.valueOf(contratos.get(position).getCostoTotal());
        holder.imgIcon.setImageResource(R.drawable.ic_document);
        holder.tvTitulo.setText("Contrato #"+p);
        holder.tvDetalle.setText( contratos.get(position).getTipoPlan()+"    $"+c);
        holder.tvEstado.setText(  contratos.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return contratos.size();
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
