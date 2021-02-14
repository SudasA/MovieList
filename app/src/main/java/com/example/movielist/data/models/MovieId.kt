package com.example.movielist.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieId (
    @SerialName("id")
    internal val id: Int
)