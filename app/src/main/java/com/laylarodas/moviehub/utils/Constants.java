package com.laylarodas.moviehub.utils;

/**
 * Clase que contiene todas las constantes de la aplicación.
 * Esto nos permite cambiar valores en un solo lugar.
 */
public class Constants {
    
    // ⚠️ IMPORTANTE: Debes obtener tu propia API Key en https://www.themoviedb.org/settings/api
    
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMjkyZWJhMWYwZmIwOTlhYjNlMWJhNGQ5N2Q2ODVhNyIsIm5iZiI6MTc2MzQ2MTk5My44NzMsInN1YiI6IjY5MWM0YjY5YWRmYjVkYTRkNDE3NjM5OSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.GVvNQuKCOSgDRJSEL6ZFF0eGD9UG4VU5D3u9bmD5GeM";
    
    // URL base de la API de TMDB (The Movie Database)
    // Todas las peticiones empezarán con esta URL
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    
    // URL base para cargar imágenes
    // TMDB almacena las imágenes en un servidor separado
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    
    // Tamaños de imágenes disponibles en TMDB
    // w500 = ancho de 500 píxeles (bueno para posters)
    public static final String POSTER_SIZE = "w500";
    
    // w780 = ancho de 780 píxeles (bueno para imágenes de fondo)
    public static final String BACKDROP_SIZE = "w780";
    
    /**
     * Método helper para construir la URL completa de un poster.
     * 
     * ¿Cómo funciona?
     * - La API solo devuelve el PATH: "/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg"
     * - Nosotros necesitamos la URL completa: "https://image.tmdb.org/t/p/w500/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg"
     * - Este método une las partes
     * 
     * @param path El path que devuelve la API (ej: "/abc123.jpg")
     * @return URL completa para cargar la imagen
     */
    public static String getImageUrl(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return IMAGE_BASE_URL + POSTER_SIZE + path;
    }
    
    /**
     * Método helper para construir la URL completa de un backdrop (imagen de fondo).
     * Igual que getImageUrl pero usa tamaño más grande.
     * 
     * @param path El path que devuelve la API
     * @return URL completa para cargar la imagen de fondo
     */
    public static String getBackdropUrl(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return IMAGE_BASE_URL + BACKDROP_SIZE + path;
    }
}

