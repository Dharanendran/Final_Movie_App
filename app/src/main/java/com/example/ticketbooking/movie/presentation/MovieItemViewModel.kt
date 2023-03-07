package com.example.ticketbooking.movie.presentation

import android.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MovieItemViewModel() :ViewModel(),java.io.Serializable {

    val poster by lazy{ MutableLiveData<Bitmap>() }
    val title by lazy{ MutableLiveData<String>("Movie Name") }
    val heroName by lazy{ MutableLiveData<String>("Hero Name") }
    val heroineName by lazy{ MutableLiveData<String>("Heroine Name") }
    val villainName by lazy{ MutableLiveData<String>("Villain Name") }
    val language by lazy{ MutableLiveData<String>("Language") }
    val sensorCertificates by lazy{ MutableLiveData<List<String>>(listOf("U","U/A","A","TBA","U","U/A",)) }
    val synopsis by lazy{ MutableLiveData<String>() }
    val genre by lazy{ MutableLiveData<MutableList<String>>() }
    val releaseDate: MutableLiveData<String> by lazy{ MutableLiveData<String>() }
    val director by lazy{ MutableLiveData<String>() }
    val musicDirector by lazy{ MutableLiveData<String>() }

    val dominantColorFromPoster by lazy{ MutableLiveData<String?>(null) }



}