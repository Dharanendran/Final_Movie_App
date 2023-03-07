package com.example.ticketbooking.dataRepository.api.apiDaos

import okhttp3.*
import java.io.IOException

class MovieApi {

    private val client = OkHttpClient()

    private val GET_URL = "https://raw.githubusercontent.com/Dharanendran/MovieJsons/main/MovieJsons.json"
//    private const val POST_URL = "https://raw.githubusercontent.com/Dharanendran/MovieJsons/main/MovieJsons.json"
//    private const val PUT_URL = "https://raw.githubusercontent.com/Dharanendran/MovieJsons/main/MovieJsons.json"
//    private const val DELETE_URL = "https://raw.githubusercontent.com/Dharanendran/MovieJsons/main/MovieJsons.json"
//        .......


    fun getMovies(onSuccess: (json: String?) -> Unit, onFailure: (e: Exception) -> Unit) {

        val request = Request.Builder().url(GET_URL).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                onSuccess(json)
                response.close()
            }

        })

    }

    fun postCities() {}
    fun putCities() {}
    fun deleteCities() {}

}