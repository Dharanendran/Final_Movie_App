package com.example.ticketbooking

import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.ticketbooking.cities.presentation.CityPageFragment
import com.example.ticketbooking.movie.presentation.MoviePageFragment
import com.example.ticketbooking.signIn.presentation.SignInPageFragment
import com.example.ticketbooking.signup.presentation.SignUpPageFragment
import kotlinx.coroutines.*

class ActivityMainViewModel: ViewModel(){

    var scrollAnimJob: Job? = null

    fun textScrollAnim(textWidthInPx:Float, textViewWidthInPx:Int, padValueInPx:Double, iconWidthInPx:Double , selectCityTextView: TextView)
    {
        scrollAnimJob = CoroutineScope(Dispatchers.Main).launch {
            var count = 0
            if (textWidthInPx > (textViewWidthInPx - padValueInPx - iconWidthInPx))
                while (true) {
                    count++
                    selectCityTextView.scrollBy(1, 0)
                    delay(10)
                    if (textWidthInPx.toInt() == count) {
                        selectCityTextView.scrollTo(1, 0)
                        count = 0
                    }
                }
        }
    }
}

