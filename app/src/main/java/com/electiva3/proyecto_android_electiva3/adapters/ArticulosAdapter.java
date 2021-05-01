package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.detalle_orden;
import com.electiva3.proyecto_android_electiva3.entities.Articulo;
import com.electiva3.proyecto_android_electiva3.entities.Orden;
import com.electiva3.proyecto_android_electiva3.entities.Servicio;
import com.electiva3.proyecto_android_electiva3.entities.Usuario;

import java.util.ArrayList;
import java.util.Collection;

public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosAdapter.MyViewHolder>  implements  View.OnClickListener ,     Filterable {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Servicio> articulos;
    private ArrayList<Servicio> articulosAll;
    private View.OnClickListener listener;

    public ArticulosAdapter(Context context,ArrayList<Servicio> articulos) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.articulos  =  articulos;
        this.articulosAll  =  new ArrayList<>(articulos);
    }

    @Override
    public ArticulosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        ArticulosAdapter.MyViewHolder holder = new ArticulosAdapter.MyViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ArticulosAdapter.MyViewHolder holder, final int position) {
        holder.imgIcon.setImageResource(R.drawable.ic_filter_black_24dp);
        holder.tvTitulo.setText(  articulos.get(position).getTitulo()  );
        holder.tvDetalle.setText(  String.valueOf(articulos.get(position).getCosto())   );
        holder.tvEstado.setText("");

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Toast.makeText(context,  articulos.get(position).getTitulo()   , Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return articulos.size();
    }

    Filter filter  = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Servicio>  serviciosFiltered =  new ArrayList<>();

            if(  constraint.toString().isEmpty()  ){
                serviciosFiltered.addAll(  articulosAll   );
            }else{
                for(int i = 0;  i < articulosAll.size() ;  i++ ){
                    if(articulosAll.get(i).getTitulo().toLowerCase().contains(  constraint.toString().toLowerCase()   )){
                        serviciosFiltered.add(   articulosAll.get(i));
                    }
                }
            }

            FilterResults filterResults  =  new FilterResults();
            filterResults.values  = serviciosFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            articulos.clear();
            articulos.addAll((Collection<? extends Servicio>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
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
