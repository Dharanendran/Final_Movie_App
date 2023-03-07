package com.example.ticketbooking.cities.domain

import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities

interface LoadPopularCitiesRepository {

    suspend fun loadPopularCities(onSuccess:(List<PopularCities>)->Unit, onFailure:()->Unit)
}