package com.example.ticketbooking.cities.domain

interface LoadCitiesRepository {
    fun loadCities(onSuccess:(MutableList<City>?)->Unit, onFailure:()->Unit)
}