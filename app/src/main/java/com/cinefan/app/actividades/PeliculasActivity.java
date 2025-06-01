package com.cinefan.app.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.cinefan.app.R;
import com.cinefan.app.adaptadores.PeliculaAdapter;
import com.cinefan.app.modelos.Pelicula;
import com.cinefan.app.utilidades.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PeliculasActivity extends AppCompatActivity implements PeliculaAdapter.OnPeliculaListener {
    
    private RecyclerView recyclerView;
    private PeliculaAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAgregar;
    private List<Pelicula> listaPeliculas;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);
        
        // inicializa las vistas
        recyclerView = findViewById(R.id.recycler_peliculas);
        progressBar = findViewById(R.id.progress_bar);
        fabAgregar = findViewById(R.id.fab_agregar);
        
        listaPeliculas = new ArrayList<>();
        
        // configura el recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PeliculaAdapter(listaPeliculas, this);
        recyclerView.setAdapter(adapter);
        
        // configura el boton flotante
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeliculasActivity.this, AgregarPeliculaActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // carga las peliculas
        new CargarPeliculasTask().execute();
    }
    
    @Override
    public void onPeliculaClick(int position) {
        // abre la actividad de editar pelicula
        Pelicula pelicula = listaPeliculas.get(position);
        Intent intent = new Intent(this, EditarPeliculaActivity.class);
        intent.putExtra("pelicula_id", pelicula.getId());
        startActivity(intent);
    }
    
    @Override
    public void onPeliculaLongClick(int position) {
        // muestra dialogo para eliminar
        Pelicula pelicula = listaPeliculas.get(position);
        mostrarDialogoEliminar(pelicula);
    }
    
    // muestra dialogo de confirmacion para eliminar
    private void mostrarDialogoEliminar(final Pelicula pelicula) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar película");
        builder.setMessage("¿Estás seguro de eliminar " + pelicula.getTitulo() + "?");
        
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new EliminarPeliculaTask().execute(pelicula.getId());
            }
        });
        
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    
    // tarea para cargar peliculas
    private class CargarPeliculasTask extends AsyncTask<Void, Void, List<Pelicula>> {
        
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        
        @Override
        protected List<Pelicula> doInBackground(Void... params) {
            List<Pelicula> peliculas = new ArrayList<>();
            
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "SELECT * FROM peliculas ORDER BY titulo";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(rs.getInt("id"));
                    pelicula.setTitulo(rs.getString("titulo"));
                    pelicula.setDirector(rs.getString("director"));
                    pelicula.setAnio(rs.getInt("anio"));
                    pelicula.setGenero(rs.getString("genero"));
                    pelicula.setSinopsis(rs.getString("sinopsis"));
                    pelicula.setImagenUrl(rs.getString("imagen_url"));
                    
                    peliculas.add(pelicula);
                }
                
                rs.close();
                stmt.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return peliculas;
        }
        
        @Override
        protected void onPostExecute(List<Pelicula> peliculas) {
            progressBar.setVisibility(View.GONE);
            listaPeliculas.clear();
            listaPeliculas.addAll(peliculas);
            adapter.notifyDataSetChanged();
        }
    }
    
    // tarea para eliminar pelicula
    private class EliminarPeliculaTask extends AsyncTask<Integer, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "DELETE FROM peliculas WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, params[0]);
                
                int resultado = stmt.executeUpdate();
                stmt.close();
                
                return resultado > 0;
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        
        @Override
        protected void onPostExecute(Boolean exito) {
            if (exito) {
                Toast.makeText(PeliculasActivity.this, "Película eliminada", Toast.LENGTH_SHORT).show();
                new CargarPeliculasTask().execute();
            } else {
                Toast.makeText(PeliculasActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}