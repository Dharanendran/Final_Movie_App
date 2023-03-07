package com.example.ticketbooking.movie.repository

import android.content.Context
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.example.ticketbooking.domain.businessModels.theatre.Movie
import com.example.ticketbooking.movie.domain.LoadMovieRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadMoviesRepositoryImpl(private val context: Context) : LoadMovieRepository
{

    override suspend fun loadMovies(onSuccess: (List<Movie>) -> Unit, onFailure: (e: Exception) -> Unit) {

        val onSuccessFromDomainLayer: (List<Movie>) -> Unit = onSuccess
        val onFailureFromDomainLayer: (e: Exception) -> Unit = onFailure


        fun onSuccess(json: String?)
        {
            json?.let {
                val movies = MovieEntityMapper.jsonToEntityMapper(json)
                CoroutineScope(Dispatchers.Main).launch{ onSuccessFromDomainLayer(movies) }
            }?:CoroutineScope(Dispatchers.Main).launch { onSuccessFromDomainLayer(emptyList()) }
        }


        fun onFailure(e: Exception)
        {
            CoroutineScope(Dispatchers.Main).launch{ onFailureFromDomainLayer(e) }
        }

        DependencyFactory.getInstance(context).getApiSource().getMoviesApi().getMovies(::onSuccess, ::onFailure)
    }

}



object MovieEntityMapper {

    fun entityModelToJsonMapper() {

    }

    fun jsonToEntityMapper(json: String): List<Movie> {
        val type = object : TypeToken<MutableList<Movie>>() {}.type
        val movies: MutableList<Movie> = Gson().fromJson(json, type)
        return movies
    }
}
