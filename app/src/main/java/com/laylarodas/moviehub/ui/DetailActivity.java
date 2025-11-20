package com.laylarodas.moviehub.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.laylarodas.moviehub.R;
import com.laylarodas.moviehub.utils.Constants;

/**
 * DetailActivity - Pantalla de detalles de una película
 * 
 * ¿QUÉ HACE?
 * - Muestra información detallada de una película
 * - Recibe datos desde MainActivity vía Intent
 * - Muestra backdrop, poster, título, rating, fecha y sinopsis
 * 
 * NAVEGACIÓN:
 * MainActivity → (Intent con datos) → DetailActivity
 * 
 * NO usa ViewModel porque solo muestra datos que ya recibió.
 * Para una app más compleja, podrías cargar más datos de la API aquí.
 */
public class DetailActivity extends AppCompatActivity {
    
    // ==================== CONSTANTES PARA INTENT EXTRAS ====================
    
    /**
     * Keys para los extras del Intent.
     * 
     * ¿POR QUÉ CONSTANTES?
     * - Evita errores de tipeo ("movie_id" vs "movieId")
     * - Fácil de cambiar en un solo lugar
     * - Buena práctica profesional
     * 
     * PREFIJO: Usamos el nombre completo del paquete para evitar conflictos
     */
    public static final String EXTRA_MOVIE_ID = "com.laylarodas.moviehub.MOVIE_ID";
    public static final String EXTRA_MOVIE_TITLE = "com.laylarodas.moviehub.MOVIE_TITLE";
    public static final String EXTRA_MOVIE_POSTER = "com.laylarodas.moviehub.MOVIE_POSTER";
    public static final String EXTRA_MOVIE_BACKDROP = "com.laylarodas.moviehub.MOVIE_BACKDROP";
    public static final String EXTRA_MOVIE_OVERVIEW = "com.laylarodas.moviehub.MOVIE_OVERVIEW";
    public static final String EXTRA_MOVIE_RATING = "com.laylarodas.moviehub.MOVIE_RATING";
    public static final String EXTRA_MOVIE_RELEASE_DATE = "com.laylarodas.moviehub.MOVIE_RELEASE_DATE";
    
    // ==================== VARIABLES DE UI ====================
    
    private ImageView backdropImageView;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;
    private TextView overviewTextView;
    
    // ==================== LIFECYCLE METHODS ====================
    
    /**
     * onCreate - Se llama cuando la Activity se crea
     * 
     * FLUJO:
     * 1. Inicializar vistas (findViewById)
     * 2. Obtener datos del Intent
     * 3. Mostrar datos en las vistas
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        // PASO 1: Inicializar las vistas
        initViews();
        
        // PASO 2: Obtener datos del Intent
        getIntentData();
        
        // PASO 3: Configurar botón de back en la toolbar
        setupToolbar();
    }
    
    // ==================== SETUP METHODS ====================
    
    /**
     * Inicializa las referencias a las vistas.
     */
    private void initViews() {
        backdropImageView = findViewById(R.id.iv_backdrop);
        posterImageView = findViewById(R.id.iv_poster);
        titleTextView = findViewById(R.id.tv_title);
        ratingTextView = findViewById(R.id.tv_rating);
        releaseDateTextView = findViewById(R.id.tv_release_date);
        overviewTextView = findViewById(R.id.tv_overview);
    }
    
    /**
     * Obtiene los datos del Intent y los muestra.
     * 
     * ¿CÓMO FUNCIONA?
     * 1. getIntent() obtiene el Intent que inició esta Activity
     * 2. get[Type]Extra() obtiene cada dato por su key
     * 3. Mostramos los datos en las vistas
     */
    private void getIntentData() {
        // Obtener el Intent que inició esta Activity
        android.content.Intent intent = getIntent();
        
        // Verificar que el Intent existe y tiene extras
        if (intent != null && intent.hasExtra(EXTRA_MOVIE_TITLE)) {
            
            // OBTENER DATOS DEL INTENT
            // Cada tipo de dato tiene su método específico:
            // - getString() para Strings
            // - getInt() para integers
            // - getDouble() para doubles
            // - etc.
            
            String title = intent.getStringExtra(EXTRA_MOVIE_TITLE);
            String posterPath = intent.getStringExtra(EXTRA_MOVIE_POSTER);
            String backdropPath = intent.getStringExtra(EXTRA_MOVIE_BACKDROP);
            String overview = intent.getStringExtra(EXTRA_MOVIE_OVERVIEW);
            double rating = intent.getDoubleExtra(EXTRA_MOVIE_RATING, 0.0);
            String releaseDate = intent.getStringExtra(EXTRA_MOVIE_RELEASE_DATE);
            
            // MOSTRAR DATOS EN LAS VISTAS
            
            // 1. Título
            titleTextView.setText(title);
            
            // 2. Rating (formatear a 1 decimal)
            String ratingText = String.format("%.1f", rating);
            ratingTextView.setText(ratingText);
            
            // 3. Fecha de estreno (formatear)
            // Si quieres formato "Released: Feb 27, 2024", usa SimpleDateFormat
            // Por ahora mostramos simple
            String releaseDateFormatted = "Released: " + releaseDate;
            releaseDateTextView.setText(releaseDateFormatted);
            
            // 4. Overview (sinopsis)
            // Si el overview está vacío, mostrar mensaje
            if (overview != null && !overview.isEmpty()) {
                overviewTextView.setText(overview);
            } else {
                overviewTextView.setText("No overview available.");
            }
            
            // 5. Cargar Backdrop con Glide
            // Construir URL completa
            String backdropUrl = Constants.getBackdropUrl(backdropPath);
            Glide.with(this)
                    .load(backdropUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(backdropImageView);
            
            // 6. Cargar Poster con Glide
            String posterUrl = Constants.getImageUrl(posterPath);
            Glide.with(this)
                    .load(posterUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(posterImageView);
            
        } else {
            // No hay datos en el Intent (no debería pasar)
            // Podrías mostrar un error o cerrar la Activity
            finish();
        }
    }
    
    /**
     * Configura la toolbar con botón de back.
     * 
     * ¿QUÉ HACE?
     * - Muestra flecha "<-" en la toolbar
     * - Al tocarla, cierra esta Activity y vuelve a MainActivity
     */
    private void setupToolbar() {
        // Habilitar botón de back (flecha)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    
    /**
     * Maneja el click del botón back.
     * 
     * ¿CUÁNDO SE LLAMA?
     * - Cuando el usuario toca la flecha "<-" en la toolbar
     * 
     * @return true si se manejó el evento
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Cerrar esta Activity y volver a la anterior
        onBackPressed();
        return true;
    }
    
    /**
     * NOTAS ADICIONALES:
     * 
     * MEJORAS FUTURAS:
     * 1. Formatear fecha con SimpleDateFormat
     * 2. Agregar más información (género, duración, actores)
     * 3. Usar ViewModel para cargar datos adicionales de la API
     * 4. Agregar botón de favoritos
     * 5. Mostrar trailer (si está disponible)
     * 6. Animaciones de transición
     * 
     * PATRÓN DE INTENT EXTRAS:
     * - Enviando (MainActivity):
     *   intent.putExtra(DetailActivity.EXTRA_MOVIE_TITLE, movie.getTitle());
     * 
     * - Recibiendo (DetailActivity):
     *   String title = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
     * 
     * LIFECYCLE:
     * User clickea película en MainActivity
     *   → MainActivity.onMovieClick()
     *   → Intent creado con extras
     *   → startActivity(intent)
     *   → DetailActivity.onCreate()
     *   → getIntentData()
     *   → Vistas actualizadas
     *   → Usuario ve detalles ✅
     */
}

