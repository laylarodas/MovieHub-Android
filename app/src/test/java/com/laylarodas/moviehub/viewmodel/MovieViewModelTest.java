package com.laylarodas.moviehub.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.laylarodas.moviehub.model.Movie;
import com.laylarodas.moviehub.repository.MovieRepository;

import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests para MovieViewModel.
 *
 * Verifican que el ViewModel actualice los LiveData correctamente cuando el Repository
 * devuelve datos o errores. Utilizamos InstantTaskExecutorRule para ejecutar LiveData
 * de forma sincrónica en tests unitarios.
 */
public class MovieViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void loadPopularMovies_success_updatesMoviesLiveData() throws Exception {
        FakeMovieRepository repository = new FakeMovieRepository(false);
        MovieViewModel viewModel = new MovieViewModel(repository);

        viewModel.loadPopularMovies();

        List<Movie> result = getOrAwaitValue(viewModel.getMovies());
        assertEquals(2, result.size());
        assertEquals("Movie 1", result.get(0).getTitle());
        assertNull(viewModel.getErrorMessage().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoading().getValue());
    }

    @Test
    public void loadPopularMovies_error_updatesErrorLiveData() throws Exception {
        FakeMovieRepository repository = new FakeMovieRepository(true);
        MovieViewModel viewModel = new MovieViewModel(repository);

        viewModel.loadPopularMovies();

        String error = getOrAwaitValue(viewModel.getErrorMessage());
        assertEquals("Network error", error);
        List<Movie> movies = viewModel.getMovies().getValue();
        assertTrue(movies == null || movies.isEmpty());
        assertEquals(Boolean.FALSE, viewModel.getIsLoading().getValue());
    }

    /**
     * Repository falso para simular respuestas exitosas o con error.
     */
    private static class FakeMovieRepository extends MovieRepository {
        private final boolean shouldReturnError;
        private final List<Movie> fakeMovies;

        FakeMovieRepository(boolean shouldReturnError) {
            this.shouldReturnError = shouldReturnError;
            this.fakeMovies = Arrays.asList(
                    new Movie(1, "Movie 1", "/poster1.jpg", "/backdrop1.jpg",
                            "Overview 1", 8.5, "2024-01-01"),
                    new Movie(2, "Movie 2", "/poster2.jpg", "/backdrop2.jpg",
                            "Overview 2", 7.5, "2024-02-01")
            );
        }

        @Override
        public void getPopularMovies(OnMoviesLoadedListener listener) {
            if (shouldReturnError) {
                listener.onError("Network error");
            } else {
                listener.onSuccess(fakeMovies);
            }
        }
    }

    /**
     * Helper para obtener el valor actual de un LiveData de forma sincrónica.
     */
    private static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);

        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new AssertionError("LiveData value was never set.");
        }
        //noinspection unchecked
        return (T) data[0];
    }
}

