package com.laylarodas.moviehub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laylarodas.moviehub.model.Movie;
import com.laylarodas.moviehub.repository.MovieRepository;

import java.util.List;

/**
 * MovieViewModel - Patrón MVVM
 * 
 * ¿QUÉ ES UN VIEWMODEL?
 * - Clase que almacena y maneja datos relacionados con la UI
 * - SOBREVIVE a cambios de configuración (rotación de pantalla)
 * - NO tiene referencias directas a Views/Activities (evita memory leaks)
 * 
 * ¿POR QUÉ HEREDAR DE ViewModel?
 * - Android sabe manejar su ciclo de vida
 * - Se destruye solo cuando la Activity se destruye permanentemente
 * - Permite usar ViewModelProvider
 * 
 * RESPONSABILIDADES:
 * - Pedir datos al Repository
 * - Exponer datos a la UI usando LiveData
 * - Manejar estados (loading, error, success)
 * - Transformar datos si es necesario
 */
public class MovieViewModel extends ViewModel {
    
    // ==================== REPOSITORY ====================
    
    /**
     * Repository: Fuente única de datos.
     * El ViewModel NO hace llamadas directas a la API.
     * Siempre usa el Repository como intermediario.
     */
    private final MovieRepository repository;
    
    // ==================== LIVEDATA - PELÍCULAS ====================
    
    /**
     * MutableLiveData PRIVADO - Solo el ViewModel puede modificarlo.
     * 
     * ¿Por qué MutableLiveData?
     * - Permite cambiar el valor con setValue() o postValue()
     * - Es "mutable" = puede mutar/cambiar
     * 
     * ¿Por qué privado?
     * - La Activity NO debe modificar directamente los datos
     * - Solo el ViewModel tiene ese poder
     * - Principio de encapsulación
     */
    private MutableLiveData<List<Movie>> movies;
    
    /**
     * LiveData PÚBLICO - La Activity puede observar pero NO modificar.
     * 
     * Este método devuelve movies pero como LiveData (inmutable desde fuera).
     * Es el getter que expone los datos a la UI.
     * 
     * @return LiveData con la lista de películas (solo lectura)
     */
    public LiveData<List<Movie>> getMovies() {
        // Lazy initialization: Solo crea el LiveData si no existe
        if (movies == null) {
            movies = new MutableLiveData<>();
            // Puedes inicializar con lista vacía si quieres
            // movies.setValue(new ArrayList<>());
        }
        return movies;
    }
    
    // ==================== LIVEDATA - ESTADO DE CARGA ====================
    
    /**
     * LiveData para indicar si está cargando datos.
     * 
     * Estados:
     * - true: Está cargando (mostrar ProgressBar)
     * - false: No está cargando (ocultar ProgressBar)
     * 
     * Boolean con mayúscula porque puede ser null
     */
    private MutableLiveData<Boolean> isLoading;
    
    public LiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>(false);  // Por defecto no está cargando
        }
        return isLoading;
    }
    
    // ==================== LIVEDATA - MENSAJES DE ERROR ====================
    
    /**
     * LiveData para mensajes de error.
     * 
     * Cuando hay un error:
     * - errorMessage.setValue("Error al cargar películas")
     * - La Activity observa y muestra un Toast o Snackbar
     * 
     * String porque puede ser null (sin error)
     */
    private MutableLiveData<String> errorMessage;
    
    public LiveData<String> getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = new MutableLiveData<>();
        }
        return errorMessage;
    }
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor por defecto. Inicializa el Repository real.
     * 
     * NOTA: Idealmente usarías Dependency Injection (Hilt/Dagger) para inyectar el Repository.
     * Para facilitar los tests unitarios, proveemos también un constructor que recibe el Repository.
     */
    public MovieViewModel() {
        this(new MovieRepository());
    }

    /**
     * Constructor adicional para testing.
     * 
     * @param repository Repository a utilizar (puede ser falso para tests)
     */
    public MovieViewModel(MovieRepository repository) {
        this.repository = repository;
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    /**
     * Carga películas populares desde el Repository.
     * 
     * FLUJO COMPLETO:
     * 1. Activity llama: viewModel.loadPopularMovies()
     * 2. ViewModel pone isLoading = true (muestra ProgressBar)
     * 3. ViewModel pide datos al Repository
     * 4. Repository hace llamada HTTP asíncrona
     * 5a. Si éxito: ViewModel actualiza movies (Activity se actualiza automáticamente)
     * 5b. Si error: ViewModel actualiza errorMessage (Activity muestra error)
     * 6. ViewModel pone isLoading = false (oculta ProgressBar)
     * 
     * La Activity solo observa los LiveData y reacciona a los cambios.
     */
    public void loadPopularMovies() {
        // Asegurar que los LiveData estén inicializados
        if (movies == null) {
            getMovies();
        }
        if (isLoading == null) {
            getIsLoading();
        }
        if (errorMessage == null) {
            getErrorMessage();
        }
        
        // PASO 1: Indicar que está cargando
        // Esto hace que la UI muestre un ProgressBar
        isLoading.setValue(true);
        
        // PASO 2: Pedir datos al Repository
        repository.getPopularMovies(new MovieRepository.OnMoviesLoadedListener() {
            
            /**
             * onSuccess: Se llama cuando los datos se cargaron correctamente.
             * 
             * @param movieList Lista de películas obtenida de la API
             */
            @Override
            public void onSuccess(List<Movie> movieList) {
                
                // PASO 3a: Actualizar LiveData con las películas
                // setValue() DEBE llamarse desde el main thread
                // postValue() puede llamarse desde cualquier thread
                movies.setValue(movieList);
                
                // PASO 4: Indicar que ya no está cargando
                isLoading.setValue(false);
                
                // Opcional: Limpiar mensaje de error previo
                errorMessage.setValue(null);
                
                // DEBUG: Log para ver cuántas películas se cargaron
                // Log.d("MovieViewModel", "Loaded " + movieList.size() + " movies");
            }
            
            /**
             * onError: Se llama cuando hubo un error al cargar.
             * 
             * @param error Mensaje de error descriptivo
             */
            @Override
            public void onError(String error) {
                
                // PASO 3b: Actualizar LiveData con el error
                errorMessage.setValue(error);
                
                // PASO 4: Indicar que ya no está cargando
                isLoading.setValue(false);
                
                // Opcional: Limpiar lista de películas
                // movies.setValue(new ArrayList<>());
                
                // DEBUG: Log para ver el error
                // Log.e("MovieViewModel", "Error: " + error);
            }
        });
    }
    
    /**
     * Método onCleared(): Se llama cuando el ViewModel se destruye.
     * 
     * ¿Cuándo se llama?
     * - Cuando la Activity se destruye PERMANENTEMENTE (no por rotación)
     * - Ejemplo: Usuario presiona back o finish()
     * 
     * ¿Para qué sirve?
     * - Limpiar recursos (cerrar conexiones, cancelar operaciones)
     * - En nuestro caso no es crítico, pero es buena práctica
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Aquí podrías cancelar llamadas pendientes si fuera necesario
        // Ejemplo: call.cancel();
    }
    
    /**
     * TODO: Métodos adicionales para el futuro:
     * 
     * - searchMovies(String query)
     * - loadMovieDetails(int movieId)
     * - loadTopRatedMovies()
     * - refreshMovies() (pull to refresh)
     */
}

