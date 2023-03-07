package com.example.ticketbooking.movie.domain

import com.example.ticketbooking.domain.businessModels.theatre.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class LoadMovieUseCase(private val repository:LoadMovieRepository) {

    fun loadMovies(onSuccess:(List<Movie>)->Unit, onFailure:(e:Exception)->Unit)
    {
        CoroutineScope(Dispatchers.IO).launch { repository.loadMovies(onSuccess, onFailure) }
    }

}