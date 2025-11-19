package com.laylarodas.moviehub.repository;

import com.laylarodas.moviehub.model.Movie;
import com.laylarodas.moviehub.model.MovieResponse;
import com.laylarodas.moviehub.network.ApiService;
import com.laylarodas.moviehub.network.RetrofitClient;
import com.laylarodas.moviehub.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MovieRepository - Patrón Repository
 * 
 * ¿QUÉ HACE?
 * - Es la ÚNICA fuente de datos para el ViewModel
 * - Abstrae de DÓNDE vienen los datos (API, DB, Cache)
 * - Maneja las llamadas a Retrofit
 * 
 * ¿POR QUÉ EXISTE?
 * - Separa responsabilidades (Single Responsibility Principle)
 * - ViewModel no necesita saber cómo se obtienen los datos
 * - Fácil agregar cache o cambiar de API
 * 
 * ANALOGÍA: Es como un "mesero" que trae datos al ViewModel
 */
public class MovieRepository {
    
    /**
     * Instancia de ApiService para hacer llamadas HTTP.
     * Es final porque nunca cambiará durante la vida del Repository.
     */
    private final ApiService apiService;
    
    /**
     * Constructor: Inicializa el ApiService.
     * 
     * ¿Por qué en el constructor?
     * - Solo se configura una vez cuando se crea el Repository
     * - Patrón: Dependency Injection (aunque aquí es simple)
     */
    public MovieRepository() {
        // Obtiene la instancia de ApiService de RetrofitClient (Singleton)
        this.apiService = RetrofitClient.getApiService();
    }
    
    // ==================== INTERFACES DE CALLBACKS ====================
    
    /**
     * Interface para notificar cuando las películas se carguen.
     * 
     * ¿QUÉ ES UNA INTERFACE DE CALLBACK?
     * - Es un "contrato" que dice qué métodos llamar cuando algo suceda
     * - Como darle tu número de teléfono al delivery
     * 
     * TIENE DOS MÉTODOS:
     * 1. onSuccess - Se llama cuando TODO salió bien
     * 2. onError - Se llama cuando hubo un problema
     */
    public interface OnMoviesLoadedListener {
        /**
         * Se llama cuando las películas se cargaron exitosamente.
         * @param movies Lista de películas obtenida de la API
         */
        void onSuccess(List<Movie> movies);
        
        /**
         * Se llama cuando hubo un error al cargar.
         * @param errorMessage Mensaje de error descriptivo
         */
        void onError(String errorMessage);
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    /**
     * Obtiene películas populares de forma ASÍNCRONA.
     * 
     * ¿QUÉ SIGNIFICA ASÍNCRONA?
     * - No bloquea la UI mientras espera respuesta
     * - Es como pedir comida a domicilio: pides y sigues haciendo otras cosas
     * 
     * FLUJO:
     * 1. ViewModel llama a este método pasando un listener
     * 2. Este método hace la llamada HTTP a la API
     * 3. Mientras espera, el código continúa (no bloquea)
     * 4. Cuando llega respuesta, llama a listener.onSuccess() o listener.onError()
     * 
     * @param listener Callback que será notificado cuando termine
     */
    public void getPopularMovies(OnMoviesLoadedListener listener) {
        
        // PASO 1: Crear la llamada (Call) usando Retrofit
        // Esto NO ejecuta la llamada, solo la prepara
        Call<MovieResponse> call = apiService.getPopularMovies(Constants.API_KEY);
        
        // PASO 2: Ejecutar la llamada de forma ASÍNCRONA con enqueue()
        // enqueue() hace la llamada en un hilo secundario (background thread)
        call.enqueue(new Callback<MovieResponse>() {
            
            /**
             * onResponse - Se llama cuando la API responde (exitosa o no).
             * 
             * IMPORTANTE: onResponse NO significa éxito automáticamente
             * Significa que hubo comunicación con el servidor
             * Aún necesitas verificar si response.isSuccessful()
             * 
             * @param call La llamada original
             * @param response La respuesta del servidor
             */
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                
                // ¿La respuesta fue exitosa? (código 200-299)
                if (response.isSuccessful() && response.body() != null) {
                    
                    // ÉXITO: Obtener la lista de películas
                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getResults();
                    
                    // Notificar al listener (ViewModel) que todo salió bien
                    listener.onSuccess(movies);
                    
                } else {
                    // La API respondió pero con error (404, 500, etc.)
                    String errorMsg = "Error: " + response.code() + " - " + response.message();
                    listener.onError(errorMsg);
                }
            }
            
            /**
             * onFailure - Se llama cuando NO hubo comunicación con el servidor.
             * 
             * Causas comunes:
             * - No hay internet
             * - Timeout (tardó mucho)
             * - URL incorrecta
             * - Servidor caído
             * 
             * @param call La llamada original
             * @param t El error (Throwable)
             */
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                
                // Construir mensaje de error amigable
                String errorMsg = "Network error: " + t.getMessage();
                
                // Notificar al listener del error
                listener.onError(errorMsg);
            }
        });
    }
    
    /**
     * TODO: Agregar más métodos en el futuro:
     * - getMovieDetails(int id, OnMovieDetailsListener listener)
     * - searchMovies(String query, OnMoviesLoadedListener listener)
     * - getTopRatedMovies(OnMoviesLoadedListener listener)
     */
}

