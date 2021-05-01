package com.electiva3.proyecto_android_electiva3.entities;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Notificaciones {

    Context context;
    Conexion conexion = new Conexion();
    Mensajes mensajes = new Mensajes();

    public void setContext(Context context) {
        this.context = context;
    }

    public void MensajeSegunToken(final String idUsuario, final String titulo, final String detalle){

        final RequestQueue myrequest = Volley.newRequestQueue(context);
        final JSONObject json = new JSONObject();
        final String key = (UUID.randomUUID().toString());

        conexion.inicializarFirabase(context);
        conexion.getDatabaseReference().child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String id = ds.getKey();
                        if(id.equals(idUsuario)) {
                            String token = Objects.requireNonNull(ds.getValue()).toString();

                            try{
                                json.put("to", token);
                                JSONObject notificacion = new JSONObject();
                                notificacion.put("titulo", titulo);
                                notificacion.put("detalle", detalle);

                                json.put("data", notificacion);

                                String URL = "https://fcm.googleapis.com/fcm/send";

                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,json, null, null){
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> header = new HashMap<>();

                                        header.put("content-type", "application/json");
                                        //clave del proyecto de firebase que esta en Cloud Mesa
                                        header.put("authorization", "key=AAAAlPemOMg:APA91bGezbeU-8afpaIHRTZ9vqgAEy-1lYEkqf8ko6UyYS51D1vKMv4uceaYaJ6KltXsvKidPiOwlm8JLNB1WOEXUOb8cbSFfe8CvoXNKlB69zA_R_rVqNGjh6Za9vwvT45lLwZHF0wk");
                                        return header;
                                    }
                                };

                                myrequest.add(request);

                                //agregando mensajes a database
                                mensajes.setKeyUsuario(idUsuario);
                                mensajes.setTitulo(titulo);
                                mensajes.setDetalle(detalle);
                                conexion.getDatabaseReference().child("notificaciones").child(key).setValue(mensajes);

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
