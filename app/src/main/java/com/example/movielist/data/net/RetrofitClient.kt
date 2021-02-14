package com.example.movielist.data.net


import com.example.movielist.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder

object RetrofitClient {

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient().newBuilder()
        .addNetworkInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val contentType = "application/json".toMediaType()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val retrofit: Retrofit = Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.TMDB_BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val moviesApi: MoviesApi = retrofit.create(MoviesApi::class.java)

    suspend fun getPopularMovies() = withContext(Dispatchers.IO) {
        moviesApi.getPopularMovies()
    }

    suspend fun getMovieById(movieId: Int) = withContext(Dispatchers.IO) {
        moviesApi.getMovieById(movieId)
    }

    suspend fun getMovieActorsById(movieId: Int) = withContext(Dispatchers.IO) {
        moviesApi.getMovieActorsById(movieId)
    }
}