package com.cinefan.app.modelos;

public class Pelicula {
    private int id;
    private String titulo;
    private String director;
    private int anio;
    private String genero;
    private String sinopsis;
    private String imagenUrl;
    
    // constructor vacio
    public Pelicula() {}
    
    // constructor completo
    public Pelicula(int id, String titulo, String director, int anio, String genero, String sinopsis, String imagenUrl) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.imagenUrl = imagenUrl;
    }
    
    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}