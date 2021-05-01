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
import com.electiva3.proyecto_android_electiva3.entities.Usuario;

import java.util.List;

public class UsuarioListAdapter extends ArrayAdapter<Usuario>
{
    private List<Usuario> usuariosList;
    private Context context;

    public UsuarioListAdapter(@NonNull Context context, int resource, @NonNull List<Usuario> objects) {
        super(context, resource, objects);
        this.context =  context;
        this.usuariosList =  objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Genera el contenido visual en base al contenido del array list pasado como parametro
        View view =  convertView;

        if(view == null)
        {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_servicio , null);
        }
        Usuario usuario = usuariosList.get(position);

        TextView texto =   view.findViewById(R.id.nombreServicio);
        texto.setText( usuario.getNombre());

        return view;
    }
}