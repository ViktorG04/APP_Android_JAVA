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
import com.electiva3.proyecto_android_electiva3.actualizar_detalle_reserva;
import com.electiva3.proyecto_android_electiva3.entities.Conexion;
import com.electiva3.proyecto_android_electiva3.entities.Reserva;

import java.util.ArrayList;

public class ReservasAdapter  extends RecyclerView.Adapter<ReservasAdapter.MyViewHolder>  {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Reserva> reservas;
    private Conexion conexion;

    public ReservasAdapter(Context context , ArrayList<Reserva> reservas ){
        inflater =  LayoutInflater.from(context);
        this.context= context;
        this.reservas = reservas;
    }

    @Override
    public ReservasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        ReservasAdapter.MyViewHolder holder = new ReservasAdapter.MyViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(final ReservasAdapter.MyViewHolder holder, final int position) {
        holder.imgIcon.setImageResource(R.drawable.ic_check);
        holder.tvTitulo.setText(  reservas.get(position).getNombreCliente()   );
        holder.tvDetalle.setText(  "Contrato: #"+reservas.get(position).getNumeroContrato() );
        holder.tvEstado.setText(  reservas.get(position).getFechaSolicitada()+" - "+reservas.get(position).getHora()+
                        "\n"+reservas.get(position).getEstado());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =  new Intent( context ,   actualizar_detalle_reserva.class   );
                intent.putExtra(  "key" , reservas.get(position).getKey()   );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
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
