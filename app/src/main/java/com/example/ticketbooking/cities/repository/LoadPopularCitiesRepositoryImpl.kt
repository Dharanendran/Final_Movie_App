package com.example.ticketbooking.cities.repository

import android.content.Context
import android.util.Log
import com.example.ticketbooking.cities.domain.LoadPopularCitiesRepository
import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadPopularCitiesRepositoryImpl(private val context:Context):LoadPopularCitiesRepository
{

    override suspend fun loadPopularCities( onSuccess:(List<PopularCities>) -> Unit, onFailure: () -> Unit )
    {
        try
        {
            val popularCities = DependencyFactory.getInstance(context).getDataBaseObject().getPopularCitiesDao().getAllPopularCities()
            CoroutineScope(Dispatchers.Main).launch { onSuccess(popularCities) }
        }

        catch (e:Exception)
        {
            CoroutineScope(Dispatchers.Main).launch{ onFailure() }
        }
    }
}