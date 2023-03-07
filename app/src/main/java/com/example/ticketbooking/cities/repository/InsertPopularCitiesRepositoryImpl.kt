package com.example.ticketbooking.cities.repository

import android.content.Context
import android.util.Log
import com.example.ticketbooking.cities.domain.InsertPopularCitiesRepository
import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import java.lang.Exception

class InsertPopularCitiesRepositoryImpl(private val context: Context):InsertPopularCitiesRepository {

    override suspend fun insert(
        cities: List<PopularCities>,
        onSuccess: (List<Long>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try
        {
            val res = DependencyFactory.getInstance(context).getDataBaseObject().getPopularCitiesDao().insertPopularCities(cities)
            onSuccess(res)
        }
        catch (e: Exception)
        {
            onFailure(e)
        }
    }

}