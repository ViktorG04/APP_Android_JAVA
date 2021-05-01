package com.electiva3.proyecto_android_electiva3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.electiva3.proyecto_android_electiva3.R;
import com.electiva3.proyecto_android_electiva3.entities.Contrato;
import java.util.ArrayList;
import java.util.List;

public class SpinnerContratosAdapter extends ArrayAdapter {

    private Context context;
    private List<Contrato> contratos;


    public SpinnerContratosAdapter(@NonNull Context context, int resource, @NonNull List<Contrato> contratos) {
        super(context, resource, contratos);
        this.context =  context;
        this.contratos  =  contratos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =  convertView;

        if(view==null){
            view = LayoutInflater.from(this.context).inflate(R.layout.custom_simple_spinner_item , null);
        }

        TextView texto =  view.findViewById(R.id.customSpinnerItem);
        texto.setText(  contratos.get(position).getPlan().get(1) );
        return view;
    }





}
