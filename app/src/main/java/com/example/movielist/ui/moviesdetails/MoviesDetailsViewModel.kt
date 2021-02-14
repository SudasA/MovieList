package com.stopkaaaa.androidacademyproject.ui.moviesdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.net.RetrofitClient
import com.stopkaaaa.androidacademyproject.data.models.Actor
import com.stopkaaaa.androidacademyproject.data.models.Movie
import kotlinx.coroutines.launch

class MoviesDetailsViewModel : ViewModel() {

    private val _mutableLoadingState = MutableLiveData<Boolean>(false)
    private val _mutableCurrentMovie = MutableLiveData<Movie>()
    private val _mutableActorsList = MutableLiveData<List<Actor>>(emptyList())

    val loadingState: LiveData<Boolean> get() = _mutableLoadingState
    val currentMovie: LiveData<Movie> get() = _mutableCurrentMovie
    val actorsList: LiveData<List<Actor>> get() = _mutableActorsList

    fun loadMovieById(id: Int) {
        viewModelScope.launch {

            _mutableLoadingState.value = true

            val movie = RetrofitClient.getMovieById(id)
            _mutableCurrentMovie.value = movie

            val actors = RetrofitClient.getMovieActorsById(id).actors
            _mutableActorsList.value = actors

            _mutableLoadingState.value = false

        }
    }
}