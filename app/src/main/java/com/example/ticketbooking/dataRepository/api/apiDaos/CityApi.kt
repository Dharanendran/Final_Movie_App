package com.example.ticketbooking.dataRepository.api.apiDaos

import android.util.Log
import okhttp3.*
import java.io.IOException

object CityApi {

    private const val GET_URL = "https://raw.githubusercontent.com/Dharanendran/Indian-Cities-JSON/master/cities.json"

    fun getCities(onSuccess:(json:String?)->Unit, onFailure:()->Unit)
    {
        val client = OkHttpClient()
        val request = Request.Builder().url(GET_URL).build()

        client.newCall(request).enqueue( object: Callback {

            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                Log.v("dharani",call.isCanceled().toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                onSuccess(json)
                response.close()
            }

        })

    }

    fun postCities(){}
    fun putCities(){}
    fun deleteCities(){}

}