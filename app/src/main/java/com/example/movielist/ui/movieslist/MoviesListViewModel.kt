package com.example.movielist.ui.movieslist


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.models.Movie
import com.example.movielist.data.net.RetrofitClient
import kotlinx.coroutines.launch


class MoviesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _mutableMoviesList = MutableLiveData<List<Movie>>(emptyList())
    private val _mutableLoadingState = MutableLiveData(Status.DONE)

    val moviesList: LiveData<List<Movie>> get() = _mutableMoviesList
    val loadingState: LiveData<Status> get() = _mutableLoadingState

    fun load() {
        viewModelScope.launch {
            _mutableLoadingState.value = Status.FIRST_LOADING

            val moviesResponse =  RetrofitClient.getPopularMovies()

            val movies = List(moviesResponse.moviesIdList.size) {
                RetrofitClient.getMovieById(moviesResponse.moviesIdList[it].id)
            }


            _mutableMoviesList.value = movies

            _mutableLoadingState.value = Status.DONE
        }
    }
}

enum class Status { FIRST_LOADING, DONE }