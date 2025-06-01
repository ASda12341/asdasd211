package com.cinefan.app.servicios;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.cinefan.app.R;

public class ServicioMusica extends Service {
    
    private MediaPlayer reproductor;
    
    @Override
    public void onCreate() {
        super.onCreate();
        // crea el reproductor con la musica de fondo
        reproductor = MediaPlayer.create(this, R.raw.musica_fondo);
        reproductor.setLooping(true);
        reproductor.setVolume(0.5f, 0.5f);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // inicia la reproduccion si no esta sonando
        if (!reproductor.isPlaying()) {
            reproductor.start();
        }
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        // detiene y libera el reproductor
        if (reproductor != null) {
            reproductor.stop();
            reproductor.release();
            reproductor = null;
        }
        super.onDestroy();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}