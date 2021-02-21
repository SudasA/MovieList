package com.example.movielist

import android.app.Application
import com.example.movielist.data.net.NetworkConnectionInterceptor
import com.example.movielist.db.MovieDatabase


lateinit var db: MovieDatabase
lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor

class App : Application() {
    companion object {
        lateinit var INSTANCE : App
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        db = MovieDatabase.create(this)
        networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        INSTANCE = this
    }
}