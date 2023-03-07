package com.example.ticketbooking.dataRepository.roomDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_cities_table")
class PopularCities(
    var image:ByteArray,
    var name:String
)
{
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}