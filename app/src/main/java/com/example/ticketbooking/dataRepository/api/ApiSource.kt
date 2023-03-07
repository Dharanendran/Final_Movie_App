package com.example.ticketbooking.dataRepository.api

import com.example.ticketbooking.dataRepository.api.apiDaos.CityApi
import com.example.ticketbooking.dataRepository.api.apiDaos.MovieApi

class ApiSource {

    companion object
    {
        fun getCitiesApi() = CityApi
        fun getMoviesApi() = MovieApi()
        //    fun getMovieApi() = TODO()
        //    fun getTheatreApi() = TODO()
        //    fun getUserApi() = TODO()
        //        ......

    }

}