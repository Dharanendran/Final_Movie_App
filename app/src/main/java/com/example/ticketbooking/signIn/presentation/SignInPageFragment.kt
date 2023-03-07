package com.example.ticketbooking.signIn.presentation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigator
import com.bumptech.glide.Glide
import com.example.ticketbooking.R
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.dataRepository.roomDatabase.entities.User
import com.example.ticketbooking.databinding.FragmentLoginBinding
import com.example.ticketbooking.log
import com.example.ticketbooking.movie.presentation.MoviePageFragment
import com.example.ticketbooking.sharedPreference
import com.example.ticketbooking.signup.presentation.SignUpPageFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class SignInPageFragment : Fragment(), SignInPageViewModel.ToastMaker {


    interface Navigator{
        fun signInSuccessFullNavigate()
        fun signUpOnClickNavigate()
    }

    var navigator: Navigator? = null
    private lateinit var activityContext: Context
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private lateinit var viewModel: SignInPageViewModel

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {

                    val account = task.result

                    val name = account.displayName.toString()
                    val phoneNo = ""
                    val email = account.email.toString()
                    val profilePicture = account.photoUrl.toString()

                    viewModel.userNameExistCheckingUseCase?.isUserNameExist(email) {
                        if (!it)
                            viewModel.createUserAccountUseCase?.createAccount(
                                User(
                                    name,
                                    phoneNo,
                                    email,
                                    profilePicture
                                ), "", "", false
                            )
                            {
                                viewModel.setIsLoginSuccessFull(true)
                            }

                    }


                } catch (e: ApiException) {
                    Toast.makeText(activityContext, "failure", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onResume() {

        (activity as MainActivity).let {
            it.activityMainBinding.headerLayout.visibility = View.GONE
            it.activityMainBinding.bottomNavigationView.visibility = View.GONE
        }
        super.onResume()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        fragmentLoginBinding.signUpTextView.setOnClickListener { navigator?.signUpOnClickNavigate()  }
        fragmentLoginBinding.signInButton.setOnClickListener {
            viewModel.signInOnClick()
        }
        fragmentLoginBinding.googleSignInButton.setOnClickListener { googleSignInActivity() }


        viewModel.isLoginSuccessFull().observe(viewLifecycleOwner) {

            if (it) {
                Log.v("asa", sharedPreference?.getBoolean(MainActivity.SHARED_PREFERENCE_STRING_SIGN_IN, false).toString())
                sharedPreference?.let{ sp ->
                    val editor = sp.edit()
                    editor.putBoolean(MainActivity.SHARED_PREFERENCE_STRING_SIGN_IN, true).commit()
                }
                Log.v("asa", sharedPreference?.getBoolean(MainActivity.SHARED_PREFERENCE_STRING_SIGN_IN, false).toString())
                navigator?.signInSuccessFullNavigate()
            }
        }




        viewModel.isProgressBarStarted().observe(viewLifecycleOwner, Observer {

            if (it) {
                viewModel.startLoadingAnim()
                fragmentLoginBinding.signInProgressBar.visibility = View.VISIBLE
                fragmentLoginBinding.overlapView.visibility = View.VISIBLE
            }
            else {
                fragmentLoginBinding.signInProgressBar.visibility = View.GONE
                fragmentLoginBinding.overlapView.visibility = View.GONE
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        activityContext = activity?.applicationContext as Context

        viewModel = ViewModelProvider(this)[SignInPageViewModel::class.java].apply {

            toastMaker = this@SignInPageFragment

            isUserExistUseCase = DependencyFactory.getInstance(activityContext).let {
                it.getIsUserExistUseCase(it.getIsUserExistRepositoryImpl())
            }
            createUserAccountUseCase = DependencyFactory.getInstance(activityContext).let {
                it.getCreateUserAccountUseCase(it.getCreateUserAccountRepositoryImpl())
            }

            userNameExistCheckingUseCase = DependencyFactory.getInstance(activityContext).let {
                it.getUserNameExistCheckingUseCase(it.getIsUserNameExistRepositoryImpl())
            }
        }

        fragmentLoginBinding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        ).apply {
            viewModel = this@SignInPageFragment.viewModel
            lifecycleOwner = this@SignInPageFragment
        }


        return fragmentLoginBinding.root

    }




//
//        (activity as MainActivity).fragmentTransactionProcess(
//            true,
//            SignUpPageFragment::class.simpleName,
//            SignUpPageFragment()
//        )



    private fun googleSignInActivity() {
//        CoroutineScope(Dispatchers.IO).launch {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activityContext, gso)
        val signInIntent = googleSignInClient.signInIntent
        startForResult.launch(signInIntent)
//        }
    }

    override fun makeToast(message: String) { // also we can use Live data for this
        Toast.makeText(activityContext, message, Toast.LENGTH_SHORT).show()
    }

}