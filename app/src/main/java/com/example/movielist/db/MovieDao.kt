package com.example.movielist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.data.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("select * from movies")
    fun getAll(): List<Movie>

    @Query("delete from movies where _id = :id")
    fun delete (id: Int?)
}