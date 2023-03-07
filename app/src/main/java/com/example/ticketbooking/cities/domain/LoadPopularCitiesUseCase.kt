package com.example.ticketbooking.cities.domain

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.example.ticketbooking.cities.presentation.PopularCitiesItemViewModel
import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class LoadPopularCitiesUseCase(private val repository:LoadPopularCitiesRepository)
{

    fun getPopularCities(onSuccess:(List<PopularCities>)->Unit, onFailure:()->Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            repository.loadPopularCities(onSuccess, onFailure)
        }
    }


}


