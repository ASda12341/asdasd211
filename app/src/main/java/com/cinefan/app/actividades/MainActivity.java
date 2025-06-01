package com.cinefan.app.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.cinefan.app.R;
import com.cinefan.app.servicios.ServicioMusica;
import com.cinefan.app.utilidades.Preferencias;

public class MainActivity extends AppCompatActivity {
    
    private Button botonFeed, botonPeliculas, botonMapa, botonCerrar;
    private Switch switchMusica;
    private TextView textoBienvenida;
    private Preferencias preferencias;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // inicializa las vistas
        botonFeed = findViewById(R.id.boton_feed);
        botonPeliculas = findViewById(R.id.boton_peliculas);
        botonMapa = findViewById(R.id.boton_mapa);
        botonCerrar = findViewById(R.id.boton_cerrar);
        switchMusica = findViewById(R.id.switch_musica);
        textoBienvenida = findViewById(R.id.texto_bienvenida);
        
        preferencias = new Preferencias(this);
        
        // muestra el nombre del usuario
        String nombreUsuario = preferencias.obtenerNombreUsuario();
        textoBienvenida.setText(getString(R.string.bienvenido) + ", " + nombreUsuario);
        
        // configura el switch de musica
        switchMusica.setChecked(preferencias.obtenerMusicaActiva());
        if (preferencias.obtenerMusicaActiva()) {
            iniciarMusica();
        }
        
        // listener para el switch de musica
        switchMusica.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencias.guardarMusicaActiva(isChecked);
            if (isChecked) {
                iniciarMusica();
            } else {
                detenerMusica();
            }
        });
        
        // configura los botones
        botonFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FeedActivity.class));
            }
        });
        
        botonPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PeliculasActivity.class));
            }
        });
        
        botonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapaActivity.class));
            }
        });
        
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }
    
    // inicia el servicio de musica
    private void iniciarMusica() {
        Intent intent = new Intent(this, ServicioMusica.class);
        startService(intent);
    }
    
    // detiene el servicio de musica
    private void detenerMusica() {
        Intent intent = new Intent(this, ServicioMusica.class);
        stopService(intent);
    }
    
    // cierra la sesion del usuario
    private void cerrarSesion() {
        preferencias.limpiarPreferencias();
        detenerMusica();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // detiene la musica si no esta en el feed
        if (!isChangingConfigurations()) {
            detenerMusica();
        }
    }
}