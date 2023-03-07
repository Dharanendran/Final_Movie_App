package com.example.ticketbooking.cities.domain

import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities

interface InsertPopularCitiesRepository {

    suspend fun insert(cities:List<PopularCities>, onSuccess:(List<Long>)->Unit, onFailure:(Exception)->Unit)

}