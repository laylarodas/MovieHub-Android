package com.laylarodas.moviehub.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo que representa la respuesta completa de la API de películas.
 * 
 * - La API no devuelve solo películas
 * - Devuelve un objeto JSON que CONTIENE las películas + información adicional
 * - Este modelo representa ese "contenedor"
 * 
 * Ejemplo de JSON que recibimos:
 * {
 *   "page": 1,
 *   "results": [
 *     { película 1 },
 *     { película 2 },
 *     ...
 *   ],
 *   "total_pages": 500,
 *   "total_results": 10000
 * }
 */
public class MovieResponse {
    
    // Página actual (útil para paginación)
    @SerializedName("page")
    private int page;
    
    // ¡AQUÍ ESTÁN LAS PELÍCULAS!
    // Es una Lista de objetos Movie
    @SerializedName("results")
    private List<Movie> results;
    
    // Total de páginas disponibles
    @SerializedName("total_pages")
    private int totalPages;
    
    // Total de resultados disponibles
    @SerializedName("total_results")
    private int totalResults;
    
    /**
     * Constructor vacío (Gson lo necesita)
     */
    public MovieResponse() {
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    /**
     * Método más importante: devuelve la lista de películas.
     * Esto es lo que realmente nos interesa de toda la respuesta.
     */
    public List<Movie> getResults() {
        return results;
    }
    
    public void setResults(List<Movie> results) {
        this.results = results;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public int getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    
    /**
     * toString para debugging
     */
    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", resultsCount=" + (results != null ? results.size() : 0) +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                '}';
    }
}

