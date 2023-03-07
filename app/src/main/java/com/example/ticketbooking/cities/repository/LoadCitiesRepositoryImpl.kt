package com.example.ticketbooking.cities.repository

import android.content.Context
import com.example.ticketbooking.cities.domain.LoadCitiesRepository
import com.example.ticketbooking.cities.domain.CitiesJsonToCitiesObjectMapper
import com.example.ticketbooking.cities.domain.City
import com.example.ticketbooking.dependencyInjection.DependencyFactory

class LoadCitiesRepositoryImpl(private val context:Context, private val mapper: CitiesJsonToCitiesObjectMapper):LoadCitiesRepository
{

    override fun loadCities( onSuccess: (MutableList<City>?) -> Unit, onFailure: () -> Unit)
    {
        DependencyFactory.getInstance(context).getCitiesJsonToCitiesObjectMapper().loadCities(onSuccess, onFailure)
    }

}