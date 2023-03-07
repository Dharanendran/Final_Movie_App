package com.example.ticketbooking.cities.presentation

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ticketbooking.cities.domain.LoadCitiesUseCase
import kotlinx.coroutines.*
import com.example.ticketbooking.cities.domain.City
import com.example.ticketbooking.cities.domain.InsertPopularCitiesUseCase
import com.example.ticketbooking.cities.domain.LoadPopularCitiesUseCase
import com.example.ticketbooking.dataRepository.roomDatabase.entities.PopularCities
import java.io.ByteArrayOutputStream

class CitiesPageViewModel : ViewModel() {

    lateinit var loadCitiesUseCase: LoadCitiesUseCase
    lateinit var loadPopularCitiesUseCase: LoadPopularCitiesUseCase
    lateinit var insertPopularCitiesUseCase: InsertPopularCitiesUseCase

    private val mSearchViewHint by lazy { MutableLiveData("Search Here") }
    private val mCities by lazy { MutableLiveData<MutableList<CitiesItemViewModel>>( mutableListOf() ) }
    private val mPopularCities by lazy { MutableLiveData<MutableList<PopularCitiesItemViewModel>>( mutableListOf() ) }
    private val mIsProgressBarForLoadCityVisible by lazy{ MutableLiveData<Boolean>(false)}
    private val mIsProgressBarForLoadPopularCityVisible by lazy{ MutableLiveData<Boolean>(false)}

    var loadingAnimJob:Job? = null



    fun searchViewHint():LiveData<String> = mSearchViewHint
    fun cities():LiveData<MutableList<CitiesItemViewModel>?> = mCities
    fun popularCities():LiveData<MutableList<PopularCitiesItemViewModel>?> = mPopularCities
    fun isProgressBarForLoadCityVisible():LiveData<Boolean> = mIsProgressBarForLoadCityVisible
    fun isProgressBarForLoadPopularCityVisible():LiveData<Boolean> = mIsProgressBarForLoadPopularCityVisible



    fun startSearchBarHintAnim() {

            loadingAnimJob = CoroutineScope(Dispatchers.Main).launch {
                while (true) {
                    for (i in 0..2) {
                        mSearchViewHint.value = mSearchViewHint.value + "."
                        Log.v("dharan", mSearchViewHint.value.toString())
                        delay(500)
                    }
                    mSearchViewHint.value = "Search Here"
                }
            }
    }

    fun loadCities()
    {
        mIsProgressBarForLoadCityVisible.value = true
        fun onSuccess(citiesList:MutableList<City>?){
            mIsProgressBarForLoadCityVisible.value = false
            citiesList?.let{
                mCities.value = CitiesBusinessModelToViewModelMapper.convert(it)
            }?:run{ mCities.value = null }
        }

        fun onFailure(){}

        loadCitiesUseCase.loadCities(::onSuccess, ::onFailure)

    }

    fun loadPopularCities()
    {
        mIsProgressBarForLoadPopularCityVisible.value = true
        fun onSuccess(popularCities:List<PopularCities>)
        {
            this.mPopularCities.value = ViewModelMapper.businessModelToViewModelMapper(popularCities) as MutableList<PopularCitiesItemViewModel>

            mIsProgressBarForLoadPopularCityVisible.value = false
        }

        fun onFailure(){}

        loadPopularCitiesUseCase.getPopularCities(::onSuccess, ::onFailure)
    }

    fun insertPopularCities(cities:List<PopularCitiesItemViewModel>)
    {
        mIsProgressBarForLoadPopularCityVisible.value = true
        fun onSuccess(citiesId:List<Long>)
        {
            println(citiesId.size)
        }

        fun onFailure(e:Exception)
        {
            e.printStackTrace()

        }

        insertPopularCitiesUseCase.insertPopularCities(ViewModelMapper.viewModelToBusinessModelMapper(cities),::onSuccess, ::onFailure)
    }

}

object ViewModelMapper
{
    fun businessModelToViewModelMapper(businessCitiesModels:List<PopularCities>):List<PopularCitiesItemViewModel>
    {

        val popularCitiesItemViewModels = mutableListOf<PopularCitiesItemViewModel>()

        businessCitiesModels.forEach {

            val bitmap = BitmapFactory.decodeByteArray(it.image, 0, it.image.size )
            val drawable = BitmapDrawable(Resources.getSystem(),bitmap)

            val viewModelObj = PopularCitiesItemViewModel()
            viewModelObj.name.value = it.name
            viewModelObj.image.value = drawable

            popularCitiesItemViewModels.add(viewModelObj)

        }
        return popularCitiesItemViewModels

    }

    fun viewModelToBusinessModelMapper(popularCitiesItemViewModels:List<PopularCitiesItemViewModel>):List<PopularCities>
    {
        val businessCitiesModels = mutableListOf<PopularCities>()

        popularCitiesItemViewModels.forEach {

            val bitmap = (it.image.value as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageByteArray = stream.toByteArray()

            val businessCitiesModel = PopularCities(image = imageByteArray, name = it.name.value.toString() )

            businessCitiesModels.add(businessCitiesModel)
        }

        return businessCitiesModels
    }

}


