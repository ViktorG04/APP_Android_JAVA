package com.electiva3.proyecto_android_electiva3.entities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.electiva3.proyecto_android_electiva3.MainActivity;
import com.electiva3.proyecto_android_electiva3.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyServiceMessagingService extends FirebaseMessagingService {

    private Conexion conexion;

    public MyServiceMessagingService() {
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    //obteniendo el token y registrandolo en db el token
    public void getToken(final String usuario)
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String idToken = instanceIdResult.getToken();
                conexion.getDatabaseReference().child("token").child(usuario).setValue(idToken);
            }
        });
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);


        if(remoteMessage.getData().size() >0)
        {
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");

            mayorQueOreo(titulo, detalle);
        }
    }

    private void mayorQueOreo(String titulo, String detalle){
        String id = "mensaje";

        NotificationManager nm = (NotificationManager)getSystemService((Context.NOTIFICATION_SERVICE));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);

            nc.setShowBadge(true);
            nm.createNotificationChannel(nc);
        }
        builder.setAutoCancel(true).setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_snauto)
                .setContentText(detalle)
                .setContentInfo("nuevo")
                .setContentIntent(clicknoti());

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        nm.notify(idNotify, builder.build());
    }

    public PendingIntent clicknoti(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, i, 0);
    }
}
