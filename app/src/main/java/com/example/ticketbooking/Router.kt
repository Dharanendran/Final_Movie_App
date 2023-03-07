package com.example.ticketbooking

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ticketbooking.cities.presentation.CityPageFragment
import com.example.ticketbooking.movie.presentation.MovieDetailingView
import com.example.ticketbooking.movie.presentation.MovieItemViewModel
import com.example.ticketbooking.movie.presentation.MoviePageFragment
import com.example.ticketbooking.signIn.presentation.SignInPageFragment
import com.example.ticketbooking.signup.presentation.SignUpPageFragment

class Router(private val context: Context, private val fragmentManager: FragmentManager) {

//
//
//    All fragment pages objects are created here only , and apply navigators
//
//
//
//
//

    private val signInFragment by lazy {

        // navigator impl
        val navigatorImpl = object : SignInPageFragment.Navigator {

            override fun signInSuccessFullNavigate() {
                fragmentTransactionProcess(
                    false,
                    MoviePageFragment::class.simpleName,
                    moviePageFragment
                )

            }

            override fun signUpOnClickNavigate() {
                fragmentTransactionProcess(
                    true,
                    SignUpPageFragment::class.simpleName,
                    signUpPageFragment
                )
            }
        }

        // object creation and set navigator impl
        SignInPageFragment().apply { this.navigator = navigatorImpl }
    }


    val signUpPageFragment by lazy {

        // navigator impl

        val navigatorImpl = object : SignUpPageFragment.Navigator {

            override fun submitOnclickNavigate() {

                fragmentTransactionProcess( false, MoviePageFragment::class.simpleName, moviePageFragment)
                fragmentManager.popBackStack(SignInPageFragment::class.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            override fun onClickProfilePictureNavigate(): Bitmap? {

                var bitmapResult:Bitmap? = null

                dialogProfilePicture.bitmapGetter = {
                    bitmapResult = it
                    Toast.makeText(context, bitmapResult.toString(),Toast.LENGTH_SHORT).show()
                }

                val transaction = fragmentManager.beginTransaction()
                transaction.add(R.id.activity_main_container, dialogProfilePicture)
                transaction.addToBackStack(DialogFragment::class.simpleName)
                transaction.commit()
                return bitmapResult
            }
        }

        // object creation and set navigator impl

        SignUpPageFragment().apply { this.navigator = navigatorImpl }
    }


    val moviePageFragment by lazy {

        // navigator impl

        val navigatorImpl = object : MoviePageFragment.Navigator {

            override fun onItemClickNavigator(movieViewModel: MovieItemViewModel?) {

                val fragment = movieDetailingView.apply {
                    arguments = Bundle().apply {
                        putSerializable("MovieViewViewModel", movieViewModel)
                    }
                }
                fragmentTransactionProcess(true, MovieDetailingView::class.simpleName, fragment)
            }
        }

        // object creation  and set navigator impl
        MoviePageFragment().apply { this.navigator = navigatorImpl }
    }


    val citiesPageFragment by lazy {

        // navigator impl

        val navigatorImpl = object : CityPageFragment.Navigator {
            override fun itemOnClickNavigate() {

            }

        }

        // object creation and set navigator impl

        CityPageFragment().apply { navigator = navigatorImpl }
    }


    val movieDetailingView by lazy {

        val navigatorImpl = object : MovieDetailingView.Navigator {
            override fun onClickBookNavigate() {
                Toast.makeText(context, "Booked", Toast.LENGTH_SHORT).show()
            }

        }
        MovieDetailingView().apply { navigator = navigatorImpl }
    }



    val dialogProfilePicture by lazy{
        DialogFragment()
    }

    //
//
//
//
//
//
//
    fun showSignInPage() {
        fragmentTransactionProcess(false, SignInPageFragment::class.java.simpleName, signInFragment)
    }

    fun showMoviePage() {
        fragmentTransactionProcess(
            false,
            MoviePageFragment::class.java.simpleName,
            moviePageFragment
        )

    }

    fun showCityPage() {
        fragmentTransactionProcess(true, CityPageFragment::class.simpleName, citiesPageFragment)
    }


    fun fragmentTransactionProcess(
        addToBackStack: Boolean,
        fragmentTagName: String?,
        fragment: Fragment
    ) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.activity_main_container, fragment)
        if (addToBackStack)
            transaction.addToBackStack(fragmentTagName)
        transaction.commit()
    }

}