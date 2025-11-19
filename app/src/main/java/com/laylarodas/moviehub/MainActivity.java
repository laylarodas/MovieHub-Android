package com.laylarodas.moviehub;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.laylarodas.moviehub.model.Movie;
import com.laylarodas.moviehub.ui.MovieAdapter;
import com.laylarodas.moviehub.viewmodel.MovieViewModel;

/**
 * MainActivity - Pantalla principal de la app
 * 
 * ARQUITECTURA MVVM:
 * - VIEW: Esta clase (MainActivity)
 * - VIEWMODEL: MovieViewModel
 * - MODEL: Movie, Repository, ApiService
 * 
 * RESPONSABILIDADES:
 * - Configurar RecyclerView y Adapter
 * - Observar LiveData del ViewModel
 * - Actualizar UI según los datos
 * - Manejar clicks del usuario
 * 
 * NO hace:
 * - Llamadas a la API (lo hace Repository)
 * - Lógica de negocio (lo hace ViewModel)
 * - Solo se encarga de mostrar datos
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    // ==================== VARIABLES DE UI ====================
    
    /**
     * RecyclerView: Muestra la lista/grid de películas
     */
    private RecyclerView recyclerView;
    
    /**
     * ProgressBar: Indicador de carga (círculo girando)
     */
    private ProgressBar progressBar;
    
    /**
     * Adapter: Conecta los datos con el RecyclerView
     */
    private MovieAdapter adapter;
    
    // ==================== VARIABLES DE ARQUITECTURA ====================
    
    /**
     * ViewModel: Maneja la lógica y los datos de UI
     * Sobrevive a cambios de configuración (rotación)
     */
    private MovieViewModel viewModel;
    
    // ==================== LIFECYCLE METHODS ====================
    
    /**
     * onCreate - Se llama cuando la Activity se crea
     * 
     * FLUJO:
     * 1. Inicializar vistas (findViewById)
     * 2. Configurar RecyclerView
     * 3. Obtener ViewModel
     * 4. Observar LiveData
     * 5. Cargar películas
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // PASO 1: Inicializar las vistas
        initViews();
        
        // PASO 2: Configurar el RecyclerView
        setupRecyclerView();
        
        // PASO 3: Obtener el ViewModel
        setupViewModel();
        
        // PASO 4: Observar los LiveData del ViewModel
        observeViewModel();
        
        // PASO 5: Cargar las películas
        // Solo cargamos si es la primera vez (savedInstanceState == null)
        // Si hay rotación, el ViewModel ya tiene los datos
        if (savedInstanceState == null) {
            viewModel.loadPopularMovies();
        }
    }
    
    // ==================== SETUP METHODS ====================
    
    /**
     * Inicializa las referencias a las vistas.
     * 
     * findViewById busca la vista por su ID en el layout XML.
     */
    private void initViews() {
        recyclerView = findViewById(R.id.rv_movies);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    /**
     * Configura el RecyclerView.
     * 
     * ¿QUÉ HACE?
     * 1. Crear el Adapter
     * 2. Crear el LayoutManager (GridLayoutManager para 2 columnas)
     * 3. Asignar ambos al RecyclerView
     */
    private void setupRecyclerView() {
        
        // PASO 1: Crear el Adapter
        // Le pasamos 'this' como listener porque MainActivity implementa OnMovieClickListener
        adapter = new MovieAdapter(this);
        
        // PASO 2: Crear GridLayoutManager
        // GridLayoutManager muestra items en una cuadrícula
        // Parámetros:
        // - context: this
        // - spanCount: 2 (número de columnas)
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        
        // PASO 3: Asignar al RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        
        // OPCIONAL: Mejoras de rendimiento
        // Si sabes que el RecyclerView no cambiará de tamaño, mejora performance
        recyclerView.setHasFixedSize(true);
    }
    
    /**
     * Obtiene el ViewModel usando ViewModelProvider.
     * 
     * ¿POR QUÉ ViewModelProvider?
     * - Android maneja el ciclo de vida del ViewModel
     * - Si la Activity se recrea (rotación), devuelve la MISMA instancia
     * - Si la Activity se destruye permanentemente, destruye el ViewModel
     * 
     * ANALOGÍA:
     * - ViewModelProvider es como un "almacén"
     * - La primera vez crea el ViewModel y lo guarda
     * - Las siguientes veces devuelve el mismo que guardó
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }
    
    /**
     * Observa los LiveData del ViewModel.
     * 
     * PATRÓN OBSERVER:
     * - El ViewModel (Observable) tiene datos
     * - La Activity (Observer) observa cambios
     * - Cuando los datos cambian, la Activity se actualiza automáticamente
     * 
     * Es como "suscribirse" a notificaciones del ViewModel.
     */
    private void observeViewModel() {
        
        // OBSERVER 1: Observar lista de películas
        // Cada vez que 'movies' cambie, se ejecuta este código
        viewModel.getMovies().observe(this, movies -> {
            // movies es la nueva lista de películas
            
            if (movies != null && !movies.isEmpty()) {
                // Si hay películas, actualizarlas en el adapter
                adapter.setMovies(movies);
            }
        });
        
        // OBSERVER 2: Observar estado de carga
        // Muestra/oculta el ProgressBar según el estado
        viewModel.getIsLoading().observe(this, isLoading -> {
            // isLoading es true (cargando) o false (no cargando)
            
            if (isLoading != null && isLoading) {
                // Está cargando: MOSTRAR ProgressBar
                progressBar.setVisibility(View.VISIBLE);
            } else {
                // No está cargando: OCULTAR ProgressBar
                progressBar.setVisibility(View.GONE);
            }
        });
        
        // OBSERVER 3: Observar mensajes de error
        // Muestra un Toast cuando hay error
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            // errorMessage es el mensaje de error (o null si no hay)
            
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // Hay error: Mostrar Toast
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    // ==================== CLICK LISTENER ====================
    
    /**
     * Se llama cuando el usuario clickea una película.
     * 
     * ¿QUIÉN LO LLAMA?
     * - MovieAdapter cuando detecta un click
     * 
     * ¿QUÉ HACER AQUÍ?
     * - Abrir pantalla de detalles (Fase 5)
     * - Por ahora solo mostramos un Toast
     * 
     * @param movie La película clickeada
     */
    @Override
    public void onMovieClick(Movie movie) {
        // TODO: En Fase 5 abriremos DetailActivity
        // Por ahora solo mostramos un Toast
        String message = "Clicked: " + movie.getTitle();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * NOTAS ADICIONALES:
     * 
     * ¿QUÉ PASA EN UNA ROTACIÓN?
     * 1. Activity se destruye
     * 2. onCreate() se llama de nuevo
     * 3. ViewModelProvider devuelve el MISMO ViewModel (con datos intactos)
     * 4. observeViewModel() se vuelve a suscribir
     * 5. Los observers reciben los datos inmediatamente
     * 6. UI se actualiza sin recargar de la API ✅
     * 
     * FLUJO COMPLETO DE DATOS:
     * User abre app
     *   → onCreate()
     *   → viewModel.loadPopularMovies()
     *   → Repository.getPopularMovies()
     *   → Retrofit HTTP call (asíncrono)
     *   → onSuccess(movies)
     *   → ViewModel.movies.setValue(movies)
     *   → LiveData notifica a observers
     *   → MainActivity.observe() recibe movies
     *   → adapter.setMovies(movies)
     *   → RecyclerView actualiza UI
     *   → ¡Usuario ve películas! 🎬
     */
}