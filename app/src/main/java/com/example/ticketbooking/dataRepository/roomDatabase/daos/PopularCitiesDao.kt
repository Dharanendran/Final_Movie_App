package com.example.ticketbooking.dataRepository.roomDatabase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities

@Dao
interface PopularCitiesDao
{
    @Query("SELECT * FROM POPULAR_CITIES_TABLE ")
    suspend fun getAllPopularCities():List<PopularCities>

    @Insert
    suspend fun insertPopularCities(cities : List<PopularCities>):List<Long>

}