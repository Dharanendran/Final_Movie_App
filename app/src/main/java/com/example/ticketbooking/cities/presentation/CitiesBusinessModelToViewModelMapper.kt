package com.example.ticketbooking.cities.presentation

import com.example.ticketbooking.cities.domain.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CitiesBusinessModelToViewModelMapper { //need to be separate layer


    fun convert(citiesBusinessModel:MutableList<City>):MutableList<CitiesItemViewModel>
    {
        val citiesViewModel = mutableListOf<CitiesItemViewModel>()

        citiesBusinessModel.forEach {
            citiesViewModel.add(CitiesItemViewModel().apply { name.value = it.name })
        }

        return citiesViewModel
    }
}