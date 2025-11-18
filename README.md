# 🎬 MovieHub Android

Android movie application that displays popular movies using The Movie Database (TMDB) API. Educational project built with MVVM architecture, Retrofit, Glide, and Material Design.

## 📱 Features

- [x] View popular movies in a grid layout
- [x] Display movie posters with smooth image loading
- [ ] Movie details screen (Coming soon)
- [ ] Search functionality (Coming soon)
- [ ] Favorites list (Coming soon)

## 🛠️ Technologies & Libraries

### Architecture & Design Pattern
- **MVVM** (Model-View-ViewModel) - Clean architecture pattern
- **LiveData** - Observable data holder
- **ViewModel** - UI-related data holder

### Networking & Data
- **Retrofit 2.9.0** - Type-safe HTTP client for Android
- **Gson** - JSON serialization/deserialization
- **TMDB API** - Movie database API

### UI & Image Loading
- **Glide 4.16.0** - Image loading and caching library
- **Material Design** - Modern UI components
- **RecyclerView** - Efficient list display

### Development
- **Java** - Programming language
- **Gradle** - Build automation
- **Android SDK 34** - Target SDK version

## 📁 Project Structure

```
app/src/main/java/com/laylarodas/moviehub/
├── model/
│   ├── Movie.java              # Movie entity model
│   └── MovieResponse.java      # API response wrapper
├── network/
│   ├── ApiService.java         # Retrofit API endpoints
│   └── RetrofitClient.java     # Retrofit configuration (Singleton)
├── repository/
│   └── MovieRepository.java    # Data abstraction layer (Coming soon)
├── viewmodel/
│   └── MovieViewModel.java     # UI logic handler (Coming soon)
├── ui/
│   ├── MainActivity.java       # Main screen
│   ├── DetailActivity.java     # Movie details (Coming soon)
│   └── MovieAdapter.java       # RecyclerView adapter (Coming soon)
└── utils/
    └── Constants.java          # App constants and API configuration
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK 21 (minimum) to 34 (target)
- TMDB API Key

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/laylarodas/MovieHub-Android.git
   cd MovieHub-Android
   ```

2. **Get your TMDB API Key**
   - Go to [TMDB API](https://www.themoviedb.org/settings/api)
   - Create an account if you don't have one
   - Request an API key (it's free)

3. **Add your API Key**
   - Open `app/src/main/java/com/laylarodas/moviehub/utils/Constants.java`
   - Replace `TU_API_KEY_AQUI` with your actual API key:
   ```java
   public static final String API_KEY = "your_api_key_here";
   ```

4. **Sync and Run**
   - Open the project in Android Studio
   - Sync Gradle files (File → Sync Project with Gradle Files)
   - Run the app on an emulator or physical device

## 📖 Learning Journey

This project follows a step-by-step educational approach:

### ✅ Phase 1: Setup & Configuration
- [x] Project dependencies (Retrofit, Glide, Lifecycle)
- [x] Internet permissions
- [x] Constants configuration

### ✅ Phase 2: Models & API
- [x] Movie and MovieResponse models
- [x] Retrofit API service interface
- [x] RetrofitClient singleton pattern
- [x] JSON to Java object mapping with Gson

### 🔄 Phase 3: Repository & ViewModel (In Progress)
- [ ] Repository pattern implementation
- [ ] ViewModel with LiveData
- [ ] Async API calls handling

### 📋 Phase 4: UI - Movie List (Planned)
- [ ] RecyclerView with GridLayoutManager
- [ ] Movie adapter
- [ ] Glide image loading
- [ ] Loading and error states

### 📋 Phase 5: Details Screen (Planned)
- [ ] Detail activity
- [ ] Intent data passing
- [ ] Enhanced movie information display

## 🌐 API Endpoints Used

| Endpoint | Description | Status |
|----------|-------------|--------|
| `/movie/popular` | Get popular movies | ✅ Configured |
| `/movie/{id}` | Get movie details | ✅ Configured |
| `/search/movie` | Search movies | ✅ Configured |

## 🔑 Key Concepts Learned

### Retrofit Annotations
- `@GET` - HTTP GET request
- `@Query` - Query parameters (?key=value)
- `@Path` - Path parameters (/movie/{id})

### Design Patterns
- **Singleton Pattern** - Single instance of RetrofitClient
- **Builder Pattern** - Retrofit configuration
- **Repository Pattern** - Data abstraction (upcoming)

### Android Architecture
- **MVVM** - Separation of concerns
- **LiveData** - Lifecycle-aware observable
- **ViewModel** - Survive configuration changes

## 📸 Screenshots

_Coming soon once UI is implemented_

## 🤝 Contributing

This is an educational project, but suggestions and improvements are welcome!

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [TMDB](https://www.themoviedb.org/) - For providing the free movie database API
- [Retrofit](https://square.github.io/retrofit/) - For the excellent HTTP client
- [Glide](https://github.com/bumptech/glide) - For efficient image loading

## 👤 Author

**Layla Rodas**
- GitHub: [@laylarodas](https://github.com/laylarodas)

## 📊 Project Status

🚧 **Active Development** - Currently implementing MVVM architecture and UI components

---

⭐ If you find this project helpful, please give it a star!

