package com.example.ticketbooking.dataRepository.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ticketbooking.dataRepository.roomDatabase.daos.*
import com.example.ticketbooking.dataRepository.roomDatabase.entities.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Database(
    entities = [
        User::class,
        Amenity::class,
        Booking::class,
        CurrentlyPlayingMovie::class,
        Movie::class,
        Offer::class,
        Order::class,
        Payment::class,
        Show::class,
        ShowTime::class,
        Theatre::class,
        TheatreAmenityLinker::class,
        UserCredential::class,
        FavouriteMovie::class,
        FavouriteTheatre::class,
        TicketNumber::class,
        PopularCities::class
    ],
    version = 12
)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getMovieDao(): MovieDao
    abstract fun getTheatreDao(): TheatreDao
    abstract fun getCurrentlyPlayingDao(): CurrentlyPlayingDao
    abstract fun getOfferDao(): OfferDao
    abstract fun getOrderDao(): OrderDao
    abstract fun getPaymentDao(): PaymentDao
    abstract fun getShowDao(): ShowDao
    abstract fun getDShowTimeDao(): ShowTimeDao
    abstract fun getTheatreAmenityDao(): TheatreAmenityDao
    abstract fun getUserCredentialDao(): UserCredentialDao
    abstract fun getAmenityDao(): AmenityDao
    abstract fun getBookingDao(): BookingDao
    abstract fun getFavouriteMovieDao(): FavouriteMovieDao
    abstract fun getFavouriteTheatreDao(): FavouriteTheatreDao
    abstract fun getTicketNumberDao():TicketNumberDao
    abstract fun getPopularCitiesDao():PopularCitiesDao

    companion object {
        private val mutex = Mutex()
        private var instance: MovieDataBase? = null

        class NoResponseException(msg: String) : Exception(msg) {

        }

        suspend fun getInstance(applicationContext: Context): MovieDataBase {
            instance ?: let {
                mutex.withLock {
                    instance ?: let {
                        instance = Room.databaseBuilder(
                            applicationContext,
                            MovieDataBase::class.java,
                            "MovieDataBase"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }

            return instance as MovieDataBase
        }
    }
}