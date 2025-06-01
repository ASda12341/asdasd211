package com.cinefan.app.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import androidx.core.app.NotificationCompat;
import com.cinefan.app.R;

public class ReceptorLlamadas extends BroadcastReceiver {
    
    private static final String CANAL_ID = "CineFanChannel";
    private static final int NOTIFICACION_ID = 1;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        // verifica si es una llamada entrante
        String estado = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        
        if (estado != null && estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // muestra notificacion cuando hay llamada entrante
            mostrarNotificacion(context);
        }
    }
    
    // muestra una notificacion simple
    private void mostrarNotificacion(Context context) {
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // crea el canal de notificacion para android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                CANAL_ID, 
                "CineFan Notificaciones", 
                NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(canal);
        }
        
        // construye la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL_ID)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentTitle("CineFan")
                .setContentText("No olvides revisar las nuevas reseñas!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        
        // muestra la notificacion
        notificationManager.notify(NOTIFICACION_ID, builder.build());
    }
}