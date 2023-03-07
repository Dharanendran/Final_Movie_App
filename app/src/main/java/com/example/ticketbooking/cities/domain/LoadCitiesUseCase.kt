package com.example.ticketbooking.cities.domain

import com.example.ticketbooking.cities.domain.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadCitiesUseCase(private val repository:LoadCitiesRepository)
{
    fun loadCities(onSuccess:(MutableList<City>?)->Unit, onFailure:()->Unit)
    {
        CoroutineScope(Dispatchers.IO).launch{
            delay(1000)
            repository.loadCities(onSuccess, onFailure)
        }
    }
}