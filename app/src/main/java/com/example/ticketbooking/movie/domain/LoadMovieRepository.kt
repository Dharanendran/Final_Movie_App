package com.example.ticketbooking.movie.domain

import com.example.ticketbooking.domain.businessModels.theatre.Movie
import com.example.ticketbooking.movie.presentation.MovieItemViewModel

interface LoadMovieRepository
{
    suspend fun loadMovies(onSuccess:(List<Movie>)->Unit, onFailure:(e:Exception)->Unit)
}