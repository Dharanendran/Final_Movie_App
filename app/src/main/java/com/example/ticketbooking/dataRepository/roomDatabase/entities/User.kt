package com.example.ticketbooking.dataRepository.roomDatabase.entities

import android.net.Uri
import androidx.room.*

@Entity(
    tableName = "user_table",
)
class User(
    var name: String,
    var phoneNO: String,
    var mailId: String,
    var profilePicture:String?,
)
{
    @PrimaryKey( autoGenerate = true )
    var id: Long = 0
}