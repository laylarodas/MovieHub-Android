package com.laylarodas.moviehub.network;

import com.laylarodas.moviehub.model.Movie;
import com.laylarodas.moviehub.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface que define los endpoints de TMDB API.
 * 
 * ¿Qué es una Interface?
 * - Es un "contrato" que define métodos sin implementación
 * - Solo declara QUÉ se puede hacer, no CÓMO
 * 
 * ¿Por qué solo métodos sin cuerpo?
 * - Retrofit GENERA la implementación automáticamente
 * - Tú solo defines las "reglas", Retrofit hace el trabajo
 * 
 * Retrofit genera la implementación automáticamente usando las anotaciones.
 */
public interface ApiService {
    
    /**
     * Endpoint 1: Obtiene películas populares.
     * 
     * URL completa que se construye:
     * https://api.themoviedb.org/3/movie/popular?api_key=TU_API_KEY
     * 
     * ¿Cómo se construye?
     * - BASE_URL (de RetrofitClient): "https://api.themoviedb.org/3/"
     * - @GET añade: "movie/popular"
     * - @Query añade: "?api_key=valor"
     * 
     * @param apiKey Tu API key de TMDB (se agrega como ?api_key=XXX)
     * @return Call<MovieResponse> - Petición lista para ejecutar
     */
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);
    
    /**
     * Endpoint 2: Obtiene detalles de una película específica.
     * 
     * URL ejemplo:
     * https://api.themoviedb.org/3/movie/693134?api_key=TU_API_KEY
     * 
     * ¿Cómo funciona @Path?
     * - @GET("movie/{id}") tiene un placeholder {id}
     * - @Path("id") reemplaza {id} con el valor que pases
     * - Si pasas movieId=693134 → "/movie/693134"
     * 
     * @param movieId ID de la película (reemplaza {id} en la ruta)
     * @param apiKey Tu API key de TMDB
     * @return Call<Movie> - Devuelve UN solo objeto Movie
     */
    @GET("movie/{id}")
    Call<Movie> getMovieDetails(
        @Path("id") int movieId,
        @Query("api_key") String apiKey
    );
    
    /**
     * Endpoint 3: Busca películas por término de búsqueda.
     * 
     * URL ejemplo:
     * https://api.themoviedb.org/3/search/movie?api_key=XXX&query=dune
     * 
     * ¿Múltiples @Query?
     * - Cada @Query agrega un parámetro
     * - Se unen con &
     * - ?api_key=XXX&query=dune&page=1
     * 
     * @param apiKey Tu API key
     * @param query Término de búsqueda (ej: "dune", "spider-man")
     * @return Call<MovieResponse> - Lista de películas que coinciden
     */
    @GET("search/movie")
    Call<MovieResponse> searchMovies(
        @Query("api_key") String apiKey,
        @Query("query") String query
    );
    
    /**
     * NOTA: Más adelante podrías agregar más endpoints:
     * 
     * - Top Rated movies
     * - Now Playing movies
     * - Movie videos (trailers)
     * - Movie credits (actores)
     * - etc.
     * 
     * Solo necesitas agregar más métodos con sus anotaciones.
     */
}

