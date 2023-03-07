package com.example.ticketbooking.domain.businessModels.theatre

import android.graphics.Bitmap
import com.example.ticketbooking.domain.businessModels.enums.*
import com.google.gson.annotations.SerializedName
import java.util.*


data class Movie(

    val id:Int = 0 ,

    @SerializedName("movie_poster")
    val poster: String,

    @SerializedName("movie_name")
    val movieName: String,

    @SerializedName("language")
    val language: String,

    @SerializedName("synopsis")
    var synopsis: String,

    @SerializedName("movie_genre")
    val genre: List<String>,

    @SerializedName("hero_name")
    val hero:String?,

    @SerializedName("heroine_name")
    val heroine:String?,

    @SerializedName("villain_name")
    val villain:String?,

    @SerializedName("director")
    val director: String,

    @SerializedName("music_director")
    val musicDirector: String,
)
