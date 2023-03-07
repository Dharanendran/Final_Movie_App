package com.example.ticketbooking.dependencyInjection

import android.content.Context
import com.example.ticketbooking.cities.domain.LoadCitiesUseCase
import com.example.ticketbooking.cities.domain.CitiesJsonToCitiesObjectMapper
import com.example.ticketbooking.cities.domain.InsertPopularCitiesUseCase
import com.example.ticketbooking.cities.domain.LoadPopularCitiesUseCase
import com.example.ticketbooking.cities.repository.InsertPopularCitiesRepositoryImpl
import com.example.ticketbooking.cities.repository.LoadCitiesRepositoryImpl
import com.example.ticketbooking.cities.repository.LoadPopularCitiesRepositoryImpl
import com.example.ticketbooking.dataRepository.api.ApiSource
import com.example.ticketbooking.signup.domain.ValidationUseCase
import com.example.ticketbooking.dataRepository.roomDatabase.MovieDataBase
import com.example.ticketbooking.movie.domain.LoadMovieUseCase
import com.example.ticketbooking.movie.repository.LoadMoviesRepositoryImpl
import com.example.ticketbooking.signIn.domain.IisUserExistRepository
import com.example.ticketbooking.signIn.domain.IsUserExistUseCase
import com.example.ticketbooking.signIn.repository.IsUserExistRepositoryImpl
import com.example.ticketbooking.signup.domain.CreateUserAccountUseCase
import com.example.ticketbooking.signup.domain.IcreateUserAccountRepository
import com.example.ticketbooking.signup.domain.IisUserNameExistRepository
import com.example.ticketbooking.signup.domain.UserNameExistCheckingUseCase
import com.example.ticketbooking.signup.repository.CreateUserAccountRepositoryImpl
import com.example.ticketbooking.signup.repository.IsUserNameExistRepositoryImpl

class DependencyFactory private constructor() {

    private lateinit var context: Context
    private var databaseInstance: MovieDataBase? = null


    companion object {

        private var instance: DependencyFactory? = null
        fun getInstance(applicationContext: Context): DependencyFactory {
            instance ?: run {
                instance = DependencyFactory().apply { this.context = applicationContext }
            }
            return instance as DependencyFactory
        }

    }
    //repositories
    //
    //
    //
    //
    //
    suspend fun getDataBaseObject(): MovieDataBase {
        databaseInstance ?: run {
            databaseInstance = MovieDataBase.getInstance(context)
        }
        return databaseInstance as MovieDataBase
    }

    //sharedPreferences
    //
    //
    //
    //
    //


    // Api sources
    //
    //
    //
    //
    fun getApiSource() = ApiSource


    // mappers
    //
    //
    //
    //

    fun getCitiesJsonToCitiesObjectMapper() = CitiesJsonToCitiesObjectMapper(context)


    //usecases
    //
    //
    //
    //

    fun getUserNameExistCheckingUseCase(repository: IisUserNameExistRepository) = UserNameExistCheckingUseCase(repository)
    fun getCreateUserAccountUseCase(repository: IcreateUserAccountRepository) = CreateUserAccountUseCase(repository)
    fun getCreateUserAccountRepositoryImpl() = CreateUserAccountRepositoryImpl(context)
    fun getIsUserNameExistRepositoryImpl() = IsUserNameExistRepositoryImpl(context)



    fun getIsUserExistUseCase( repository: IisUserExistRepository ) = IsUserExistUseCase(repository)
    fun getIsUserExistRepositoryImpl() = IsUserExistRepositoryImpl( context )

    fun getValidationUseCase() = ValidationUseCase


    fun getLoadCitiesUseCase() = LoadCitiesUseCase(getLoadCitiesRepositoryImpl())
    private fun getLoadCitiesRepositoryImpl() = LoadCitiesRepositoryImpl(context, getCitiesJsonToCitiesObjectMapper())

    fun getLoadPopularCitesUseCase() = LoadPopularCitiesUseCase(getLoadPopularCitiesRepositoryImpl())
    private fun getLoadPopularCitiesRepositoryImpl() = LoadPopularCitiesRepositoryImpl(context)

    fun getInsertPopularCitiesUseCase() = InsertPopularCitiesUseCase(getInsertPopularCitiesRepositoryImpl())
    private fun getInsertPopularCitiesRepositoryImpl() = InsertPopularCitiesRepositoryImpl(context)


    fun getLoadMoviesUseCase() = LoadMovieUseCase(getLoadMoviesRepositoryImpl())
    private fun getLoadMoviesRepositoryImpl() = LoadMoviesRepositoryImpl(context)
}