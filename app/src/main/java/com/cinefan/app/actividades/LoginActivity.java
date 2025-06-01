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
import com.cinefan.app.modelos.Usuario;
import com.cinefan.app.utilidades.ConexionBD;
import com.cinefan.app.utilidades.Preferencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity {
    
    private EditText campoUsuario, campoContrasena;
    private Button botonIniciar;
    private TextView textoRegistro;
    private Preferencias preferencias;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // inicializa las vistas
        campoUsuario = findViewById(R.id.campo_usuario);
        campoContrasena = findViewById(R.id.campo_contrasena);
        botonIniciar = findViewById(R.id.boton_iniciar);
        textoRegistro = findViewById(R.id.texto_registro);
        
        preferencias = new Preferencias(this);
        
        // configura el boton de iniciar sesion
        botonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
        
        // configura el texto de registro
        textoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }
    
    // valida los campos y hace login
    private void validarLogin() {
        String usuario = campoUsuario.getText().toString().trim();
        String contrasena = campoContrasena.getText().toString().trim();
        
        // valida campos vacios
        if (usuario.isEmpty()) {
            campoUsuario.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        if (contrasena.isEmpty()) {
            campoContrasena.setError(getString(R.string.error_campo_vacio));
            return;
        }
        
        // ejecuta el login en segundo plano
        new LoginTask().execute(usuario, contrasena);
    }
    
    // tarea asincrona para hacer login
    private class LoginTask extends AsyncTask<String, Void, Usuario> {
        
        @Override
        protected Usuario doInBackground(String... params) {
            String usuario = params[0];
            String contrasena = params[1];
            
            try {
                Connection conn = ConexionBD.obtenerConexion();
                String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, usuario);
                stmt.setString(2, contrasena);
                
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // crea el usuario con los datos
                    Usuario user = new Usuario();
                    user.setId(rs.getInt("id"));
                    user.setNombreUsuario(rs.getString("nombre_usuario"));
                    user.setCorreo(rs.getString("correo"));
                    user.setFechaRegistro(rs.getString("fecha_registro"));
                    
                    rs.close();
                    stmt.close();
                    return user;
                }
                
                rs.close();
                stmt.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario != null) {
                // guarda los datos del usuario
                preferencias.guardarUsuarioId(usuario.getId());
                preferencias.guardarNombreUsuario(usuario.getNombreUsuario());
                
                // va a la pantalla principal
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            }
        }
    }
}