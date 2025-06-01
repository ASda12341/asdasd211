package com.cinefan.app.actividades;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cinefan.app.R;
import com.cinefan.app.modelos.Pelicula;
import com.cinefan.app.utilidades.ConexionBD;
import com.cinefan.app.utilidades.Preferencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgregarResenaActivity extends AppCompatActivity {
    
    private Spinner spinnerPeliculas;
    private RatingBar ratingBar;
    private EditText campoComentario;
    private Button botonGuardar, botonCancelar;
    private List<Pelicula> listaPeliculas;
    private int peliculaSeleccionadaId = -1;
    private Preferencias preferencias;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_resena);
        
        // inicializa las vistas
        spinnerPeliculas = findViewById(R.id.spinner_peliculas);
        ratingBar = findViewById(R.id.rating_bar);
        campoComentario = findViewById(R.id.campo_comentario);
        botonGuardar = findViewById(R.id.boton_guardar);
        botonCancelar = findViewById(R.id.boton_cancelar);
        
        preferencias = new Preferencias(this);
        listaPeliculas = new ArrayList<>();
        
        // carga las peliculas
        new CargarPeliculasTask().execute();
        
        // configura el spinner
        spinnerPeliculas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    peliculaSeleccionadaId = listaPeliculas.get(position - 1).getId();
                } else {
                    peliculaSeleccionadaId = -1;
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                peliculaSeleccionadaId = -1;
            }
        });
        
        // configura los botones
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarYGuardar();
            }
        });
        
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    // valida los campos y guarda la resena
    private void validarYGuardar() {
        // valida que se haya seleccionado una pelicula
        if (peliculaSeleccionadaId == -1) {
            Toast.makeText(this, "Debes seleccionar una película", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // obtiene la puntuacion
        int puntuacion = (int) ratingBar.getRating();
        if (puntuacion == 0) {
            Toast.makeText(this, "Debes dar una puntuación", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // obtiene el comentario
        String comentario = campoComentario.getText().toString().trim();
        
        // valida comentario vacio
        if (comentario.isEmpty()) {
            campoComentario.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida longitud maxima del comentario
        if (comentario.length() > 500) {
            campoComentario.setError("El comentario no puede tener más de 500 caracteres");
            return;
        }
        
        // ejecuta la tarea de guardar
        new GuardarResenaTask().execute(String.valueOf(peliculaSeleccionadaId), 
                                       String.valueOf(puntuacion), 
                                       comentario);
    }
    
    // tarea para cargar las peliculas
    private class CargarPeliculasTask extends AsyncTask<Void, Void, List<Pelicula>> {
        
        @Override
        protected List<Pelicula> doInBackground(Void... params) {
            List<Pelicula> peliculas = new ArrayList<>();
            
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "SELECT id, titulo FROM peliculas ORDER BY titulo";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(rs.getInt("id"));
                    pelicula.setTitulo(rs.getString("titulo"));
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
            listaPeliculas.clear();
            listaPeliculas.addAll(peliculas);
            
            // crea la lista de titulos para el spinner
            List<String> titulos = new ArrayList<>();
            titulos.add("Selecciona una película");
            
            for (Pelicula p : peliculas) {
                titulos.add(p.getTitulo());
            }
            
            // configura el adaptador del spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AgregarResenaActivity.this,
                android.R.layout.simple_spinner_item,
                titulos
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPeliculas.setAdapter(adapter);
        }
    }
    
    // tarea para guardar la resena
    private class GuardarResenaTask extends AsyncTask<String, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "INSERT INTO resenas (usuario_id, pelicula_id, puntuacion, comentario, fecha) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
                // obtiene la fecha actual
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String fechaActual = sdf.format(new Date());
                
                stmt.setInt(1, preferencias.obtenerUsuarioId());
                stmt.setInt(2, Integer.parseInt(params[0]));
                stmt.setInt(3, Integer.parseInt(params[1]));
                stmt.setString(4, params[2]);
                stmt.setString(5, fechaActual);
                
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
                Toast.makeText(AgregarResenaActivity.this, "Reseña guardada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AgregarResenaActivity.this, "Error al guardar la reseña", Toast.LENGTH_SHORT).show();
            }
        }
    }
}