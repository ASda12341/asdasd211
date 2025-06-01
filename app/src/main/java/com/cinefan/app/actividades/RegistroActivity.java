package com.cinefan.app.actividades;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cinefan.app.R;
import com.cinefan.app.utilidades.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {
    
    private EditText campoUsuario, campoCorreo, campoContrasena;
    private Button botonRegistrar;
    private TextView textoLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        
        // inicializa las vistas
        campoUsuario = findViewById(R.id.campo_usuario);
        campoCorreo = findViewById(R.id.campo_correo);
        campoContrasena = findViewById(R.id.campo_contrasena);
        botonRegistrar = findViewById(R.id.boton_registrar);
        textoLogin = findViewById(R.id.texto_login);
        
        // configura el boton de registro
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarRegistro();
            }
        });
        
        // configura el texto para ir al login
        textoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    // valida los campos del registro
    private void validarRegistro() {
        String usuario = campoUsuario.getText().toString().trim();
        String correo = campoCorreo.getText().toString().trim();
        String contrasena = campoContrasena.getText().toString().trim();
        
        // valida campo vacio
        if (usuario.isEmpty()) {
            campoUsuario.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida longitud minima del usuario
        if (usuario.length() < 3) {
            campoUsuario.setError("El usuario debe tener al menos 3 caracteres");
            return;
        }
        
        // valida correo vacio
        if (correo.isEmpty()) {
            campoCorreo.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida formato del correo
        if (!correo.contains("@") || !correo.contains(".")) {
            campoCorreo.setError("Formato de correo invalido");
            return;
        }
        
        // valida contrasena vacia
        if (contrasena.isEmpty()) {
            campoContrasena.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // valida longitud minima de contrasena
        if (contrasena.length() < 6) {
            campoContrasena.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        
        // ejecuta el registro
        new RegistroTask().execute(usuario, correo, contrasena);
    }
    
    // tarea asincrona para registrar usuario
    private class RegistroTask extends AsyncTask<String, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "INSERT INTO usuarios (nombre_usuario, correo, contrasena, fecha_registro) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
                // obtiene la fecha actual
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fechaActual = sdf.format(new Date());
                
                stmt.setString(1, params[0]);
                stmt.setString(2, params[1]);
                stmt.setString(3, params[2]);
                stmt.setString(4, fechaActual);
                
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
                Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegistroActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}