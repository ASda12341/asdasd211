package com.cinefan.app.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {
    private static final String NOMBRE_PREF = "CineFanPrefs";
    private static final String KEY_USUARIO_ID = "usuario_id";
    private static final String KEY_NOMBRE_USUARIO = "nombre_usuario";
    private static final String KEY_MUSICA_ACTIVA = "musica_activa";
    
    private SharedPreferences prefs;
    
    public Preferencias(Context context) {
        prefs = context.getSharedPreferences(NOMBRE_PREF, Context.MODE_PRIVATE);
    }
    
    // guarda el id del usuario
    public void guardarUsuarioId(int id) {
        prefs.edit().putInt(KEY_USUARIO_ID, id).apply();
    }
    
    // obtiene el id del usuario
    public int obtenerUsuarioId() {
        return prefs.getInt(KEY_USUARIO_ID, -1);
    }
    
    // guarda el nombre de usuario
    public void guardarNombreUsuario(String nombre) {
        prefs.edit().putString(KEY_NOMBRE_USUARIO, nombre).apply();
    }
    
    // obtiene el nombre de usuario
    public String obtenerNombreUsuario() {
        return prefs.getString(KEY_NOMBRE_USUARIO, "");
    }
    
    // guarda si la musica esta activa
    public void guardarMusicaActiva(boolean activa) {
        prefs.edit().putBoolean(KEY_MUSICA_ACTIVA, activa).apply();
    }
    
    // obtiene si la musica esta activa
    public boolean obtenerMusicaActiva() {
        return prefs.getBoolean(KEY_MUSICA_ACTIVA, true);
    }
    
    // limpia todas las preferencias
    public void limpiarPreferencias() {
        prefs.edit().clear().apply();
    }
}