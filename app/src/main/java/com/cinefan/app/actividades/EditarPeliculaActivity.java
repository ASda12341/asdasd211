package com.cinefan.app.actividades;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cinefan.app.R;
import com.cinefan.app.modelos.Pelicula;
import com.cinefan.app.utilidades.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class EditarPeliculaActivity extends AppCompatActivity {
    
    private EditText campoTitulo, campoDirector, campoAnio, campoGenero, campoSinopsis;
    private Button botonActualizar, botonCancelar;
    private int peliculaId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pelicula);
        
        // obtiene el id de la pelicula
        peliculaId = getIntent().getIntExtra("pelicula_id", -1);
        
        // inicializa las vistas
        campoTitulo = findViewById(R.id.campo_titulo);
        campoDirector = findViewById(R.id.campo_director);
        campoAnio = findViewById(R.id.campo_anio);
        campoGenero = findViewById(R.id.campo_genero);
        campoSinopsis = findViewById(R.id.campo_sinopsis);
        botonActualizar = findViewById(R.id.boton_actualizar);
        botonCancelar = findViewById(R.id.boton_cancelar);
        
        // carga los datos de la pelicula
        new CargarPeliculaTask().execute(peliculaId);
        
        // configura los botones
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarYActualizar();
            }
        });
        
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    // valida los campos y actualiza la pelicula
    private void validarYActualizar() {
        String titulo = campoTitulo.getText().toString().trim();
        String director = campoDirector.getText().toString().trim();
        String anioStr = campoAnio.getText().toString().trim();
        String genero = campoGenero.getText().toString().trim();
        String sinopsis = campoSinopsis.getText().toString().trim();
        
        // valida titulo vacio
        if (titulo.isEmpty()) {
            campoTitulo.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida longitud maxima del titulo
        if (titulo.length() > 200) {
            campoTitulo.setError("El título no puede tener más de 200 caracteres");
            return;
        }
        
        // valida director vacio
        if (director.isEmpty()) {
            campoDirector.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida longitud maxima del director
        if (director.length() > 100) {
            campoDirector.setError("El director no puede tener más de 100 caracteres");
            return;
        }
        
        // valida año vacio
        if (anioStr.isEmpty()) {
            campoAnio.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida que el año sea numerico
        int anio;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            campoAnio.setError("El año debe ser un número");
            return;
        }
        
        // valida rango del año
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        if (anio < 1895 || anio > anioActual + 5) {
            campoAnio.setError("El año debe estar entre 1895 y " + (anioActual + 5));
            return;
        }
        
        // valida genero vacio
        if (genero.isEmpty()) {
            campoGenero.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida longitud maxima del genero
        if (genero.length() > 50) {
            campoGenero.setError("El género no puede tener más de 50 caracteres");
            return;
        }
        
        // la sinopsis es opcional pero tiene limite
        if (sinopsis.length() > 1000) {
            campoSinopsis.setError("La sinopsis no puede tener más de 1000 caracteres");
            return;
        }
        
        // ejecuta la tarea de actualizar
        new ActualizarPeliculaTask().execute(titulo, director, String.valueOf(anio), genero, sinopsis);
    }
    
    // tarea para cargar los datos de la pelicula
    private class CargarPeliculaTask extends AsyncTask<Integer, Void, Pelicula> {
        
        @Override
        protected Pelicula doInBackground(Integer... params) {
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "SELECT * FROM peliculas WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, params[0]);
                
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(rs.getInt("id"));
                    pelicula.setTitulo(rs.getString("titulo"));
                    pelicula.setDirector(rs.getString("director"));
                    pelicula.setAnio(rs.getInt("anio"));
                    pelicula.setGenero(rs.getString("genero"));
                    pelicula.setSinopsis(rs.getString("sinopsis"));
                    
                    rs.close();
                    stmt.close();
                    return pelicula;
                }
                
                rs.close();
                stmt.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Pelicula pelicula) {
            if (pelicula != null) {
                // llena los campos con los datos
                campoTitulo.setText(pelicula.getTitulo());
                campoDirector.setText(pelicula.getDirector());
                campoAnio.setText(String.valueOf(pelicula.getAnio()));
                campoGenero.setText(pelicula.getGenero());
                campoSinopsis.setText(pelicula.getSinopsis());
            } else {
                Toast.makeText(EditarPeliculaActivity.this, "Error al cargar la película", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    // tarea para actualizar la pelicula
    private class ActualizarPeliculaTask extends AsyncTask<String, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "UPDATE peliculas SET titulo = ?, director = ?, anio = ?, genero = ?, sinopsis = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
                stmt.setString(1, params[0]);
                stmt.setString(2, params[1]);
                stmt.setInt(3, Integer.parseInt(params[2]));
                stmt.setString(4, params[3]);
                stmt.setString(5, params[4]);
                stmt.setInt(6, peliculaId);
                
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
                Toast.makeText(EditarPeliculaActivity.this, "Película actualizada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditarPeliculaActivity.this, "Error al actualizar la película", Toast.LENGTH_SHORT).show();
            }
        }
    }
}