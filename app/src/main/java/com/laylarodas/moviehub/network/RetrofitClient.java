package com.laylarodas.moviehub.network;

import com.laylarodas.moviehub.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton que configura y provee la instancia de Retrofit.
 * 
 * ¿Qué es un Singleton?
 * - Patrón de diseño que asegura que solo existe UNA instancia de una clase
 * - Todos en la app usan la misma instancia
 * 
 * ¿Por qué Singleton para Retrofit?
 * - Crear Retrofit es costoso (memoria, configuración)
 * - Solo necesitamos una instancia para toda la app
 * - Mejora el rendimiento y ahorra memoria
 * 
 * Patrón Singleton: Una sola instancia en toda la app.
 */
public class RetrofitClient {
    
    /**
     * Variable estática (shared por todas las instancias).
     * - static = Pertenece a la CLASE, no a objetos individuales
     * - null = Todavía no se ha creado
     * - private = Solo esta clase puede modificarla
     */
    private static Retrofit retrofit = null;
    
    /**
     * Obtiene la instancia de Retrofit.
     * Si no existe, la crea. Si ya existe, devuelve la existente.
     * 
     * Este es el patrón "Lazy Initialization":
     * - No crea el objeto hasta que alguien lo necesite
     * - Ahorra recursos si nunca se usa
     * 
     * @return Instancia de Retrofit configurada y lista para usar
     */
    public static Retrofit getClient() {
        // ¿Primera vez que alguien pide Retrofit?
        if (retrofit == null) {
            // SÍ: Vamos a crear la instancia
            
            /**
             * Retrofit.Builder() - Patrón Builder
             * 
             * ¿Qué es Builder Pattern?
             * - Forma elegante de construir objetos complejos
             * - Usa métodos encadenados (.método1().método2())
             * - Más legible que un constructor con 10 parámetros
             */
            retrofit = new Retrofit.Builder()
                    // 1. Base URL - Dirección base de la API
                    .baseUrl(Constants.BASE_URL)  // "https://api.themoviedb.org/3/"
                    
                    // 2. Converter Factory - Cómo parsear JSON
                    // GsonConverterFactory: Convierte JSON ↔ Java Objects
                    // Sin esto, Retrofit no sabría cómo interpretar la respuesta
                    .addConverterFactory(GsonConverterFactory.create())
                    
                    // 3. Build - Construye la instancia final
                    .build();
        }
        
        // Devuelve la instancia (nueva o existente)
        return retrofit;
    }
    
    /**
     * Método de conveniencia para obtener ApiService directamente.
     * 
     * En lugar de hacer:
     *   Retrofit retrofit = RetrofitClient.getClient();
     *   ApiService service = retrofit.create(ApiService.class);
     * 
     * Puedes hacer:
     *   ApiService service = RetrofitClient.getApiService();
     * 
     * ¿Qué hace .create()?
     * - Retrofit lee las anotaciones de ApiService
     * - Genera la implementación automáticamente
     * - Devuelve un objeto que puedes usar
     * 
     * 
     * @return ApiService listo para hacer llamadas a la API
     */
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
    
    /**
     * NOTAS TÉCNICAS ADICIONALES:
     * 
     * 1. Thread Safety (seguridad de hilos):
     *    - Esta implementación NO es thread-safe
     *    - Podría crear 2 instancias si 2 hilos llaman simultáneamente
     *    - Para producción real, usa Double-Checked Locking o Kotlin object
     *    - Para este tutorial está bien así
     * 
     * 2. Configuraciones adicionales que podrías agregar:
     *    - .addInterceptor() - Para logging o headers
     *    - .client() - Para configurar OkHttpClient
     *    - .callTimeout() - Para timeouts personalizados
     * 
     * 3. Buenas prácticas:
     *    - En apps grandes, usarías Dependency Injection (Dagger/Hilt)
     *    - Esto es perfecto para aprender los fundamentos
     */
}

