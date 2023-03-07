package com.example.ticketbooking.cities.domain

import com.example.ticketbooking.cities.presentation.PopularCitiesItemViewModel
import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsertPopularCitiesUseCase( private val repository:InsertPopularCitiesRepository )
{
    fun insertPopularCities(cities:List<PopularCities>, onSuccess:(List<Long>)->Unit, onFailure:(e:Exception)->Unit)
    {

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(cities, onSuccess, onFailure)
        }
    }
}

