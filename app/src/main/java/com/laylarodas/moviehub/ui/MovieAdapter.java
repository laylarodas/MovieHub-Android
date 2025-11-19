package com.laylarodas.moviehub.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laylarodas.moviehub.R;
import com.laylarodas.moviehub.model.Movie;
import com.laylarodas.moviehub.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * MovieAdapter - Adapter de RecyclerView
 * 
 * ¿QUÉ ES UN ADAPTER?
 * - Es el PUENTE entre tus datos (List<Movie>) y la UI (RecyclerView)
 * - Crea las vistas y las llena con datos
 * - Maneja el reciclaje de vistas
 * 
 * RESPONSABILIDADES:
 * 1. Crear ViewHolders (onCreateViewHolder)
 * 2. Enlazar datos con vistas (onBindViewHolder)
 * 3. Decir cuántos items hay (getItemCount)
 * 
 * HERENCIA:
 * - RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
 * - <> especifica qué tipo de ViewHolder usa
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    
    // ==================== VARIABLES ====================
    
    /**
     * Lista de películas a mostrar.
     * Usamos ArrayList porque es mutable (podemos cambiarla).
     */
    private List<Movie> movies;
    
    /**
     * Listener para clicks en películas.
     * Se define como interface para que MainActivity pueda implementarlo.
     */
    private OnMovieClickListener listener;
    
    // ==================== INTERFACES ====================
    
    /**
     * Interface para manejar clicks en películas.
     * 
     * ¿POR QUÉ UNA INTERFACE?
     * - El Adapter NO debe saber qué hacer cuando se clickea
     * - Solo notifica: "Se clickeó esta película"
     * - MainActivity decide qué hacer (abrir detalles, etc.)
     */
    public interface OnMovieClickListener {
        /**
         * Se llama cuando se clickea una película.
         * @param movie La película clickeada
         */
        void onMovieClick(Movie movie);
    }
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor del Adapter.
     * 
     * @param listener Listener para manejar clicks (puede ser null)
     */
    public MovieAdapter(OnMovieClickListener listener) {
        this.movies = new ArrayList<>();  // Inicializa con lista vacía
        this.listener = listener;
    }
    
    // ==================== MÉTODOS OBLIGATORIOS ====================
    
    /**
     * 1. onCreateViewHolder - Crea nuevas vistas (ViewHolder)
     * 
     * ¿CUÁNDO SE LLAMA?
     * - Cuando RecyclerView necesita una vista NUEVA
     * - Se llama ~10 veces al inicio (para llenar pantalla + buffer)
     * - Después REUTILIZA las vistas (reciclaje)
     * 
     * @param parent El RecyclerView
     * @param viewType Tipo de vista (útil si tienes diferentes tipos de items)
     * @return Un nuevo ViewHolder con su vista inflada
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        // PASO 1: Inflar el layout XML del item
        // "Inflar" = Convertir XML a objetos View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        
        // PASO 2: Crear y devolver un ViewHolder con esa vista
        return new MovieViewHolder(view);
    }
    
    /**
     * 2. onBindViewHolder - Enlaza datos con una vista
     * 
     * ¿CUÁNDO SE LLAMA?
     * - Cada vez que una vista se MUESTRA en pantalla
     * - Al hacer scroll, se llama para las nuevas vistas que aparecen
     * 
     * ANALOGÍA:
     * - onCreateViewHolder = Crear un formulario en blanco
     * - onBindViewHolder = Llenar el formulario con datos específicos
     * 
     * @param holder El ViewHolder a llenar
     * @param position Posición del item en la lista (0, 1, 2, ...)
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        
        // PASO 1: Obtener la película en esta posición
        Movie movie = movies.get(position);
        
        // PASO 2: Delegar el binding al ViewHolder
        holder.bind(movie);
    }
    
    /**
     * 3. getItemCount - Devuelve el número total de items
     * 
     * ¿PARA QUÉ?
     * - RecyclerView necesita saber cuántos items hay
     * - Para calcular el scroll, cuántas vistas crear, etc.
     * 
     * @return Cantidad de películas en la lista
     */
    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    /**
     * Actualiza la lista completa de películas.
     * 
     * ¿CUÁNDO SE USA?
     * - Cuando el ViewModel notifica nuevas películas
     * - Después de cargar datos de la API
     * 
     * notifyDataSetChanged() le dice al RecyclerView:
     * "Los datos cambiaron, actualiza toda la vista"
     * 
     * @param newMovies Nueva lista de películas
     */
    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies != null ? newMovies : new ArrayList<>();
        notifyDataSetChanged();  // Notifica cambios
    }
    
    /**
     * Obtiene una película en posición específica.
     * 
     * @param position Posición en la lista
     * @return Película en esa posición o null
     */
    public Movie getMovie(int position) {
        if (position >= 0 && position < movies.size()) {
            return movies.get(position);
        }
        return null;
    }
    
    // ==================== VIEWHOLDER ====================
    
    /**
     * MovieViewHolder - Guarda referencias a las vistas del item
     * 
     * ¿QUÉ ES UN VIEWHOLDER?
     * - Clase interna que GUARDA referencias a las vistas
     * - Evita llamar findViewById() repetidamente (optimización)
     * - Se crea una vez, se reutiliza muchas veces
     * 
     * ANALOGÍA:
     * - Es como una "tarjeta de contacto" con accesos directos
     * - En vez de buscar el teléfono cada vez, lo tienes guardado
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {
        
        // Referencias a las vistas del item
        private ImageView posterImageView;
        private TextView titleTextView;
        private TextView ratingTextView;
        
        /**
         * Constructor del ViewHolder.
         * Aquí se hace findViewById() UNA SOLA VEZ.
         * 
         * @param itemView La vista del item completo
         */
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // Buscar y guardar referencias (solo una vez)
            posterImageView = itemView.findViewById(R.id.iv_poster);
            titleTextView = itemView.findViewById(R.id.tv_title);
            ratingTextView = itemView.findViewById(R.id.tv_rating);
            
            // Configurar listener de click en el item completo
            itemView.setOnClickListener(v -> {
                // Obtener posición del item clickeado
                int position = getAdapterPosition();
                
                // Verificar que la posición sea válida y haya listener
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    // Notificar al listener (MainActivity)
                    listener.onMovieClick(movies.get(position));
                }
            });
        }
        
        /**
         * Enlaza una película con las vistas.
         * 
         * ¿QUÉ HACE?
         * - Toma los datos de la película
         * - Los pone en las vistas (TextViews, ImageView)
         * 
         * @param movie La película a mostrar
         */
        public void bind(Movie movie) {
            
            // PASO 1: Configurar título
            titleTextView.setText(movie.getTitle());
            
            // PASO 2: Configurar rating (calificación)
            // Formato: "⭐ 8.5"
            String rating = "⭐ " + String.format("%.1f", movie.getVoteAverage());
            ratingTextView.setText(rating);
            
            // PASO 3: Cargar imagen con Glide
            // ¿Qué es Glide?
            // - Librería que descarga, cachea y muestra imágenes
            // - Maneja todo automáticamente (threading, memoria, etc.)
            
            String posterUrl = Constants.getImageUrl(movie.getPosterPath());
            
            Glide.with(itemView.getContext())  // Contexto
                    .load(posterUrl)           // URL de la imagen
                    .placeholder(R.mipmap.ic_launcher)  // Imagen mientras carga
                    .error(R.mipmap.ic_launcher)        // Imagen si hay error
                    .centerCrop()              // Recortar para llenar espacio
                    .into(posterImageView);    // Vista donde cargar
        }
    }
}

