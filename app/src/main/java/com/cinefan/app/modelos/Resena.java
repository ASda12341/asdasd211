package com.cinefan.app.modelos;

public class Resena {
    private int id;
    private int usuarioId;
    private int peliculaId;
    private String nombreUsuario;
    private String tituloPelicula;
    private int puntuacion;
    private String comentario;
    private String fecha;
    
    // constructor vacio
    public Resena() {}
    
    // constructor completo
    public Resena(int id, int usuarioId, int peliculaId, String nombreUsuario, 
                  String tituloPelicula, int puntuacion, String comentario, String fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.peliculaId = peliculaId;
        this.nombreUsuario = nombreUsuario;
        this.tituloPelicula = tituloPelicula;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }
    
    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public int getPeliculaId() { return peliculaId; }
    public void setPeliculaId(int peliculaId) { this.peliculaId = peliculaId; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getTituloPelicula() { return tituloPelicula; }
    public void setTituloPelicula(String tituloPelicula) { this.tituloPelicula = tituloPelicula; }
    
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}