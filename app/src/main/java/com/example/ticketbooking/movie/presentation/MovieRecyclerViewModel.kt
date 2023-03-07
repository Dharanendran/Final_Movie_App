package com.example.ticketbooking.movie.presentation

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.ticketbooking.R
import com.example.ticketbooking.domain.businessModels.theatre.Movie
import com.example.ticketbooking.movie.domain.LoadMovieUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRecyclerViewModel: ViewModel(){

    lateinit var loadMovieUseCase: LoadMovieUseCase
    lateinit var resources: Resources
    lateinit var imageLoaderDelegate:(String)->Bitmap? // for the purpose of loading the image it need context , we get it from the fragment via delegate

    private val _movies by lazy{ MutableLiveData<List<MovieItemViewModel>>(emptyList()) }
    private val _progressBarForLoadMovies by lazy{ MutableLiveData<Boolean>(false) }

    private val _isMovieApiCalled by lazy{ MutableLiveData<Boolean>(false) }




    fun movies():LiveData<List<MovieItemViewModel>> = _movies
    fun progressBarForLoadMovies():LiveData<Boolean>  = _progressBarForLoadMovies
    fun isMovieApiCalled():LiveData<Boolean> = _isMovieApiCalled

    fun loadMovies()
    {
        _isMovieApiCalled.value = true
        _progressBarForLoadMovies.value = true

        fun onSuccess(movies:List<Movie>)
        {
            _movies.value = MovieItemViewModelMapper.businessModelToViewModelMapper(imageLoaderDelegate, resources, movies)
            _progressBarForLoadMovies.value = false

        }

        fun onFailure(e:Exception){
            _progressBarForLoadMovies.value = false
            e.printStackTrace()
            Log.v("jkvhkv", e.printStackTrace().toString())
        }

        loadMovieUseCase.loadMovies(::onSuccess, ::onFailure)
    }

}

object MovieItemViewModelMapper
{

    fun viewModelToBusinessModelMapper()
    {

    }

    fun businessModelToViewModelMapper(imageLoaderDelegate:(String)->Bitmap?, resources: Resources ,movieBusinessModels:List<Movie>):List<MovieItemViewModel>
    {
        val movieViewModels = mutableListOf<MovieItemViewModel>()

        movieBusinessModels.forEach {
            val movieItemViewModel = MovieItemViewModel()

            CoroutineScope(Dispatchers.IO).launch {
                val poster = imageLoaderDelegate(it.poster)
                CoroutineScope(Dispatchers.Main).launch{ movieItemViewModel.poster.value = poster }
            }

            movieItemViewModel.apply {
                title.value = it.movieName
                heroName.value = it.hero
                heroineName.value = it.heroine
                villainName.value = it.villain
                language.value = it.language
                synopsis.value = it.synopsis
                genre.value = it.genre as MutableList<String>
                director.value  = it.director
                musicDirector.value = it.musicDirector
            }

            movieViewModels.add(movieItemViewModel)
        }
        return movieViewModels
    }
}