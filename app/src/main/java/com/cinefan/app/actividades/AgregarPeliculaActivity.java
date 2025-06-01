package com.cinefan.app.actividades;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cinefan.app.R;
import com.cinefan.app.utilidades.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;

public class AgregarPeliculaActivity extends AppCompatActivity {
    
    private EditText campoTitulo, campoDirector, campoAnio, campoGenero, campoSinopsis;
    private Button botonGuardar, botonCancelar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pelicula);
        
        // inicializa las vistas
        campoTitulo = findViewById(R.id.campo_titulo);
        campoDirector = findViewById(R.id.campo_director);
        campoAnio = findViewById(R.id.campo_anio);
        campoGenero = findViewById(R.id.campo_genero);
        campoSinopsis = findViewById(R.id.campo_sinopsis);
        botonGuardar = findViewById(R.id.boton_guardar);
        botonCancelar = findViewById(R.id.boton_cancelar);
        
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
    
    // valida los campos y guarda la pelicula
    private void validarYGuardar() {
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
        
        // ejecuta la tarea de guardar
        new GuardarPeliculaTask().execute(titulo, director, String.valueOf(anio), genero, sinopsis);
    }
    
    // tarea asincrona para guardar pelicula
    private class GuardarPeliculaTask extends AsyncTask<String, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "INSERT INTO peliculas (titulo, director, anio, genero, sinopsis) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
                stmt.setString(1, params[0]);
                stmt.setString(2, params[1]);
                stmt.setInt(3, Integer.parseInt(params[2]));
                stmt.setString(4, params[3]);
                stmt.setString(5, params[4]);
                
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
                Toast.makeText(AgregarPeliculaActivity.this, "Película guardada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AgregarPeliculaActivity.this, "Error al guardar la película", Toast.LENGTH_SHORT).show();
            }
        }
    }
}