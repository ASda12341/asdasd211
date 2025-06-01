package com.cinefan.app.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // datos de la conexion
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/cinefan_db";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "password";
    
    private static Connection conexion = null;
    
    // obtiene la conexion a la base de datos
    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }
    
    // cierra la conexion
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}