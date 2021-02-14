package com.example.movielist.data.net

import com.example.movielist.data.models.ActorListResponse
import com.example.movielist.data.models.Movie
import com.example.movielist.data.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieListResponse

    @GET("movie/{movieId}")
    suspend fun getMovieById(@Path("movieId") movieId: Int): Movie

    @GET("movie/{movieId}/credits")
    suspend fun getMovieActorsById(@Path("movieId") movieId: Int): ActorListResponse
}