package com.example.movielist

interface MovieClickListener {
    fun movieClicked(movieId: Int)
    fun backPressed()
}