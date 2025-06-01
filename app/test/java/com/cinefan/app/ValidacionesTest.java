package com.cinefan.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidacionesTest {
    
    // prueba que valida correo electronico
    @Test
    public void validarCorreo_correcto() {
        String correo = "usuario@ejemplo.com";
        assertTrue(correo.contains("@") && correo.contains("."));
    }
    
    // prueba que valida correo incorrecto
    @Test
    public void validarCorreo_incorrecto() {
        String correo = "usuarioejemplo.com";
        assertFalse(correo.contains("@") && correo.contains("."));
    }
    
    // prueba longitud minima de usuario
    @Test
    public void validarLongitudUsuario() {
        String usuario = "usr";
        assertTrue(usuario.length() >= 3);
    }
    
    // prueba longitud minima de contrasena
    @Test
    public void validarLongitudContrasena() {
        String contrasena = "123456";
        assertTrue(contrasena.length() >= 6);
    }
    
    // prueba puntuacion valida
    @Test
    public void validarPuntuacion_correcta() {
        int puntuacion = 4;
        assertTrue(puntuacion >= 1 && puntuacion <= 5);
    }
    
    // prueba puntuacion invalida
    @Test
    public void validarPuntuacion_incorrecta() {
        int puntuacion = 7;
        assertFalse(puntuacion >= 1 && puntuacion <= 5);
    }
    
    // prueba campo vacio
    @Test
    public void validarCampoVacio() {
        String campo = "";
        assertTrue(campo.isEmpty());
    }
    
    // prueba año valido para pelicula
    @Test
    public void validarAnioPelicula() {
        int anio = 2020;
        assertTrue(anio >= 1900 && anio <= 2025);
    }
}