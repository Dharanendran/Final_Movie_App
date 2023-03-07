package com.example.ticketbooking.cities.domain

import android.content.Context
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitiesJsonToCitiesObjectMapper(private val context: Context)
{

    private lateinit var onSuccessFromDomainLayer:(MutableList<City>?)->Unit
    private lateinit var onFailureFromDomainLayer:()->Unit


    fun loadCities(onSuccess:(MutableList<City>?)->Unit, onFailure:()->Unit)
    {
        this.onSuccessFromDomainLayer = onSuccess
        this.onFailureFromDomainLayer = onFailure
        DependencyFactory.getInstance(context).getApiSource().getCitiesApi().getCities(this::onSuccess, this::onFailure)

    }



    private fun onSuccess(json:String?)
    {
        json?.let{
            val type = object : TypeToken<MutableList<City>>() {}.type
            val cities:MutableList<City> = Gson().fromJson(json, type)
            CoroutineScope(Dispatchers.Main).launch {
                this@CitiesJsonToCitiesObjectMapper.onSuccessFromDomainLayer(cities)
            }
        }?:CoroutineScope(Dispatchers.Main).launch {
            this@CitiesJsonToCitiesObjectMapper.onSuccessFromDomainLayer(null)
        }
    }

    private fun onFailure()
    {
        CoroutineScope(Dispatchers.Main).launch {
            this@CitiesJsonToCitiesObjectMapper.onFailureFromDomainLayer()
        }
    }

}