package com.example.lin.notificacionprueba;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URI;

/**
 * Created by LIN on 25/03/2017.
 */

public class MiFirebaseMessagingService extends FirebaseMessagingService {
    public  static final String TAG="NOTICIAS";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from =remoteMessage.getFrom();
        Log.d(TAG,"Mensaje recibido de "+from);
        if(remoteMessage.getNotification()!=null){
            Log.d(TAG,"Notificacion "+remoteMessage.getNotification().getBody());
            mostrarNotificacion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData().size()>0){
            Log.d(TAG,"Data "+remoteMessage.getData());
        }
    }

    private void mostrarNotificacion(String title, String body) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder NotificacionBuilder=new NotificationCompat.Builder(this).setSmallIcon(R.drawable.restaurar).setContentTitle(title).setContentText(body).setAutoCancel(true).setSound(soundUri).setContentIntent(pendingIntent);
        NotificationManager notificationManage=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         notificationManage.notify(0,NotificacionBuilder.build());

    }
}
