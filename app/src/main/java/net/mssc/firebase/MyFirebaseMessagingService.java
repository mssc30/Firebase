package net.mssc.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "RAID";
    private static final String CHANNEL_ID = "ID";

    /** METODO QUE SE EJECUTA CUANDO UN NUEVO TOKEN ES GENERADO O UNO EXISTE SE ACTUALIZA (LA APP SE INSTALO EN OTRO
    DISPOSITIVO, UN USUARIO REINSTALO LA APP O LIMPIO LOS DATOS DE LA APP **/
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        //ESTE ES EL TOKEN DE MI CELULAR
        //cJKqQVSsQiqsJNBlP3Aj13:APA91bGwB56RkH2C2dpvl9Iwf4G4pqa9Hw6PKIVc3RYwGY2dy4b2ez8ncrjJ72a2Rdy6Yyst3Kj_NE4hz_eXZfkBd7pqGqdasd0-uIFxHhcSVjjU2At9mS0d3Y0J03nx4sefxEaxEWEI

        //ASOCIAR EL TOKEN CON ID DEL SISTEMA, POR EJEMPLO: MATRICULA
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //VERIFICAR SI UN MENSAJE ENTRANTE CONTIENE DATOS
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        //VERIFICAR SI EL MENSAJE CONTIENE UNA NOTIFICACIÓN
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            //LLAMAR METODOS PARA MOSTRAR LA NOTIFICACION ANDROID
            createNotificationChannel();
            construirNotificacion(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    public void construirNotificacion(String titulo, String contenido){
        //CREAR UN INTENT EXPLICITO PARA LA APP
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //CONSTRUIR LA NOTIFICACION
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //LANZAR NOTIFICACION CON ID UNICO
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1001, builder.build());
    }

    //CREAR UN CANAL DE NOTIFICACION PERO SOLO PARA LA API 26+
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CANAL";
            String description = "DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            //REGISTRAR EL CANAL EN EL SISTEMA
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
