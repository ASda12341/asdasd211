package com.cinefan.app.actividades;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.cinefan.app.R;
import com.cinefan.app.adaptadores.ResenaAdapter;
import com.cinefan.app.modelos.Resena;
import com.cinefan.app.servicios.ServicioMusica;
import com.cinefan.app.utilidades.ConexionBD;
import com.cinefan.app.utilidades.Preferencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private ResenaAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAgregar;
    private List<Resena> listaResenas;
    private Preferencias preferencias;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        
        // inicializa las vistas
        recyclerView = findViewById(R.id.recycler_resenas);
        progressBar = findViewById(R.id.progress_bar);
        fabAgregar = findViewById(R.id.fab_agregar);
        
        preferencias = new Preferencias(this);
        listaResenas = new ArrayList<>();
        
        // configura el recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResenaAdapter(listaResenas, this);
        recyclerView.setAdapter(adapter);
        
        // configura el boton flotante
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedActivity.this, AgregarResenaActivity.class);
                startActivity(intent);
            }
        });
        
        // inicia la musica si esta activada
        if (preferencias.obtenerMusicaActiva()) {
            Intent intent = new Intent(this, ServicioMusica.class);
            startService(intent);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // carga las resenas
        new CargarResenasTask().execute();
    }
    
    // tarea para cargar resenas de la base de datos
    private class CargarResenasTask extends AsyncTask<Void, Void, List<Resena>> {
        
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        
        @Override
        protected List<Resena> doInBackground(Void... params) {
            List<Resena> resenas = new ArrayList<>();
            
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "SELECT r.*, u.nombre_usuario, p.titulo " +
                           "FROM resenas r " +
                           "JOIN usuarios u ON r.usuario_id = u.id " +
                           "JOIN peliculas p ON r.pelicula_id = p.id " +
                           "ORDER BY r.fecha DESC";
                
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Resena resena = new Resena();
                    resena.setId(rs.getInt("id"));
                    resena.setUsuarioId(rs.getInt("usuario_id"));
                    resena.setPeliculaId(rs.getInt("pelicula_id"));
                    resena.setNombreUsuario(rs.getString("nombre_usuario"));
                    resena.setTituloPelicula(rs.getString("titulo"));
                    resena.setPuntuacion(rs.getInt("puntuacion"));
                    resena.setComentario(rs.getString("comentario"));
                    resena.setFecha(rs.getString("fecha"));
                    
                    resenas.add(resena);
                }
                
                rs.close();
                stmt.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return resenas;
        }
        
        @Override
        protected void onPostExecute(List<Resena> resenas) {
            progressBar.setVisibility(View.GONE);
            listaResenas.clear();
            listaResenas.addAll(resenas);
            adapter.notifyDataSetChanged();
        }
    }
}