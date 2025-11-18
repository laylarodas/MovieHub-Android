package com.laylarodas.moviehub.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo que representa una película.
 * - La API nos devuelve JSON (texto)
 * - Necesitamos convertirlo a objetos Java para usarlo en nuestro código
 * - Gson hace esta conversión automáticamente usando esta clase como "plantilla"
 * 
 * Los campos coinciden con la respuesta JSON de TMDB API.
 */
public class Movie {
    
    // @SerializedName: Le dice a Gson cómo mapear el campo JSON al campo Java
    // Ejemplo: "id" en JSON → id en Java
    @SerializedName("id")
    private int id;
    
    @SerializedName("title")
    private String title;
    
    // Nota: En JSON es "poster_path" (snake_case)
    // En Java usamos "posterPath" (camelCase) 
    @SerializedName("poster_path")
    private String posterPath;
    
    @SerializedName("backdrop_path")
    private String backdropPath;
    
    @SerializedName("overview")
    private String overview;  // Sinopsis de la película
    
    @SerializedName("vote_average")
    private double voteAverage;  // Calificación (0.0 - 10.0)
    
    @SerializedName("release_date")
    private String releaseDate;  // Formato: "2024-02-27"
    
    /**
     * Constructor vacío.
     * - Gson usa reflexión para crear objetos
     * - Necesita un constructor sin parámetros
     */
    public Movie() {
    }
    
    /**
     * Constructor completo.
     * Útil para crear películas manualmente (testing, etc.)
     */
    public Movie(int id, String title, String posterPath, String backdropPath, 
                 String overview, double voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }
    
    // ==================== GETTERS Y SETTERS ====================
    // 1. Acceder a los datos privados desde otras clases
    // 2. Modificar los datos si es necesario
    // 3. Gson los usa para asignar valores
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getPosterPath() {
        return posterPath;
    }
    
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    
    public String getBackdropPath() {
        return backdropPath;
    }
    
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public double getVoteAverage() {
        return voteAverage;
    }
    
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    /**
     * toString para debugging.
     * Útil para ver el contenido de la película en logs.
     */
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}

