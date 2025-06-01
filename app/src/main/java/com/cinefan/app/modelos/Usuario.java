package com.cinefan.app.modelos;

public class Usuario {
    private int id;
    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private String fechaRegistro;
    
    // constructor vacio
    public Usuario() {}
    
    // constructor completo
    public Usuario(int id, String nombreUsuario, String correo, String contrasena, String fechaRegistro) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fechaRegistro = fechaRegistro;
    }
    
    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}